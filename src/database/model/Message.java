package database.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "message")
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
