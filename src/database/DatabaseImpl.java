package database;

import database.model.Conversation;
import database.model.User;
import shared.Database;

import java.rmi.RemoteException;
import java.util.*;

public class DatabaseImpl implements Database {

    private Map<Integer, User> users;
    private Map<Integer[], Conversation> conversations;

    public DatabaseImpl() {
        this.users = new HashMap<>();
        this.conversations = new HashMap<>();

        User a = new User(1, "a", "a");
        User b = new User(2, "b", "b");
        User c = new User(3, "c", "c");

        a.getContacts().add(b);
        a.getContacts().add(c);
        b.getContacts().add(a);
        b.getContacts().add(c);
        c.getContacts().add(a);
        c.getContacts().add(b);

        try {
            this.setUser(a);
            this.setUser(b);
            this.setUser(c);
        } catch (Exception e) {

        }
    }

    @Override
    public Collection<User> getUsers() {
        return this.users.values();
    }

    @Override
    public User getUser(int id) throws RemoteException {
        return this.users.get(id);
    }

    @Override
    public void setUser(User user) throws RemoteException {
        this.users.put(user.getId(), user);
    }

    @Override
    public Conversation getConversation(List<User> users) throws RemoteException {
        return this.getConversation((Integer[]) users.stream().map(user -> user.getId()).toArray());
    }

    public Conversation getConversation(Integer[] ids) throws RemoteException {
        return this.conversations.get(Arrays.stream(ids).sorted(Integer::compareTo).toArray());
    }

    @Override
    public void setConversation(Conversation conversation) throws RemoteException {
        this.conversations.put(
            (Integer[]) conversation.getUsers().stream().map(user -> user.getId()).sorted(Integer::compareTo).toArray(),
            conversation
        );
    }
}
