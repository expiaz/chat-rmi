package database.model;

import java.util.Date;

public class Message {

    private User from;
    private String body;
    private Date timestamp;

    public Message(User from, String body, Date timestamp) {
        this.from = from;
        this.body = body;
        this.timestamp = timestamp;
    }

    public User getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
