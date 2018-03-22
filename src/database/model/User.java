package database.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {

    private int id;
    private String login;
    private String pwd;
    private Set<User> contacts;

    private Set<User> friendRequests;

    public User(int id, String login, String pwd) {
        this(id, login, pwd, new HashSet<>(), new HashSet<>());
    }

    public User(int id, String login, String pwd, Set<User> contacts) {
        this(id, login, pwd, contacts, new HashSet<>());
    }

    public User(int id, String login, String pwd, Set<User> contacts, Set<User> pending) {
        this.id = id;
        this.login = login;
        this.pwd = pwd;
        this.contacts = contacts;
        this.friendRequests = pending;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPwd() {
        return pwd;
    }

    public Set<User> getContacts() {
        return contacts;
    }

    public void addContact(User contact) {
        this.contacts.add(contact);
    }

    public void friendRequest(User from) {
        if (this.contacts.contains(from)) {
            return;
        }

        this.friendRequests.add(from);
    }
}
