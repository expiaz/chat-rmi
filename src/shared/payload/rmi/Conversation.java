package shared.payload.rmi;

import database.model.Message;

import java.util.Collection;

public class Conversation {

    public String name;
    public Collection<Message> messages;

    public Conversation(String name, Collection<Message> messages) {
        this.name = name;
        this.messages = messages;
    }
}
