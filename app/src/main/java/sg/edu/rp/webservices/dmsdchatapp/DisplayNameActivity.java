package sg.edu.rp.webservices.dmsdchatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayNameActivity extends AppCompatActivity {

    private static final String TAG = "DisplayNameActivity";

    private EditText etDisplayName;
    private Button btnSetDisplayName;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userProfileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_name);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userProfileRef = firebaseDatabase.getReference("profiles");

        etDisplayName = (EditText)findViewById(R.id.editTextDisplayName);
        btnSetDisplayName = (Button)findViewById(R.id.button_setDisplayName);

        btnSetDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                        .setDisplayName(etDisplayName.getText().toString())
//                        .build();
//
//                firebaseUser.updateProfile(profileUpdates)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Log.d(TAG, "User profile updated.");
//                                }
//                            }
//                        });
//
//                DisplayName profile = new DisplayName(etDisplayName.getText().toString());
//                userProfileRef.setValue(profile);

                String uid = firebaseUser.getUid();
                String displayName = etDisplayName.getText().toString();
                userProfileRef.child(uid).setValue(displayName);

                Toast.makeText(DisplayNameActivity.this, "Display name set successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("displayName", displayName);
                startActivity(i);
            }
        });
    }
}
