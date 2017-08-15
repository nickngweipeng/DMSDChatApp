package sg.edu.rp.webservices.dmsdchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ListView lvMessage;
    private TextView tvWeatherForecast;
    ArrayList<Message> alMessage = new ArrayList<Message>();
    private Button btnMessage;
    private EditText etMessage;
    private Message message;
    private String displayName;

    CustomAdapter caMessage = null;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference messageListRef, nameRef, mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        messageListRef = firebaseDatabase.getReference("messages");

        nameRef = firebaseDatabase.getReference("profiles/");
        mName = nameRef.child(uid);

        mName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnMessage = (Button)findViewById(R.id.btnAddMessage);
        etMessage = (EditText)findViewById(R.id.etMessage);

        tvWeatherForecast = (TextView)findViewById(R.id.tvWeatherForecast);

        lvMessage = (ListView)findViewById(R.id.lvMessage);
        alMessage = new ArrayList<Message>();
        caMessage = new CustomAdapter(this, R.layout.row, alMessage);
        lvMessage.setAdapter(caMessage);

        HttpRequest request = new HttpRequest("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast");
        request.setMethod("GET");
        request.setAPIKey("api-key","ujqx8ANUp4U42CfZb9GAAteeVy0tsqLk");
        request.execute();

        try{
            String jsonString = request.getResponse();
            Log.d("info", jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray itemArray = jsonObject.getJSONArray("items");
            JSONObject itemObject = itemArray.getJSONObject(0);
            JSONArray forecastArray = itemObject.getJSONArray("forecasts");
            // Populate the arraylist personList
            for (int i = 0; i < forecastArray.length(); i++) {
                JSONObject obj = forecastArray.getJSONObject(i);
                if(obj.getString("area").equalsIgnoreCase("woodlands")){
                    tvWeatherForecast.setText("Weather Forecast @ Woodlands:" + obj.getString("forecast"));
                }

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etMessage.getText().toString();
                Calendar now = Calendar.getInstance(); //Create a Calendar object with current date/time
                String messageTime = now.get(Calendar.YEAR)+ "/"+
                        (now.get(Calendar.MONTH)+1) + "/" +
                        now.get(Calendar.DAY_OF_MONTH) + " " + now.get(Calendar.HOUR_OF_DAY)+":"+
                        now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);
                DateFormat.format("dd-MM-yyyy (HH:mm:ss)",now);

                Message messages = new Message(displayName, messageTime, msg);
                messageListRef.push().setValue(messages);
            }
        });

        messageListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("MainActivity", "onChildAdded()");
                Message msg = dataSnapshot.getValue(Message.class);
                if(msg != null) {
                    msg.setId(dataSnapshot.getKey());
                    alMessage.add(msg);
                    caMessage.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String selectedId = dataSnapshot.getKey();
                Message msg = dataSnapshot.getValue(Message.class);
                if(msg != null) {
                    for (int i = 0; i < alMessage.size(); i++) {
                        if (alMessage.get(i).getId().equals(selectedId)) {
                            msg.setId(selectedId);
                            alMessage.set(i, msg);
                        }
                    }
                    caMessage.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i("MainActivity", "onChildRemoved()");

                String selectedId = dataSnapshot.getKey();
                Message msg = dataSnapshot.getValue(Message.class);
                if(msg != null) {
                    for(int i = 0; i < alMessage.size(); i++) {
                        if(alMessage.get(i).getId().equals(selectedId)) {
                            msg.setId(selectedId);
                            alMessage.remove(i);
                        }
                    }
                    caMessage.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            firebaseAuth.signOut();

            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
