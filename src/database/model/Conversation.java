package database.model;

import java.util.ArrayList;
import java.util.List;

public class Conversation {

    private List<User> users;
    private String name;

    private List<Message> messages;

    public Conversation(List<User> users, String name) {
        this.users = users;
        this.name = name;
        this.messages = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public String getName() {
        return name;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
