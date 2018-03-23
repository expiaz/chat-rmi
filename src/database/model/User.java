package database.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {

    private int id;
    private String login;
    private String pwd;
    private HashSet<User> contacts;

    private HashSet<User> friendRequests;

    public User(int id, String login, String pwd) {
        this(id, login, pwd, new HashSet<>(), new HashSet<>());
    }

    public User(int id, String login, String pwd, HashSet<User> contacts) {
        this(id, login, pwd, contacts, new HashSet<>());
    }

    public User(int id, String login, String pwd, HashSet<User> contacts, HashSet<User> pending) {
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

    public HashSet<User> getContacts() {
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
