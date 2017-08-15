package sg.edu.rp.webservices.dmsdchatapp;

import java.io.Serializable;

/**
 * Created by 15056201 on 15/8/2017.
 */

public class Message implements Serializable {
    private String id;
    private String displayName;
    private String dateTime;
    private String message;

    public Message() {

    }

    public Message(String displayName, String dateTime, String message) {
        this.displayName = displayName;
        this.dateTime = dateTime;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
