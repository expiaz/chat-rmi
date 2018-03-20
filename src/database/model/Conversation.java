package database.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "conversation")
public class Conversation {

    private List<User> users;
    private String name;

    private List<Message> messages;

    public Conversation(List<User> users, String name) {
        this.users = users;
        this.name = name;
        this.messages = new ArrayList<>();
    }

    @XmlElement
    public List<User> getUsers() {
        return users;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
