package database.model;

import com.sun.xml.internal.txw2.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "user")
public class User {

    private int id;
    private String login;
    private String pwd;
    private List<User> contacts;

    public User(int id, String login, String pwd) {
        this(id, login, pwd, new ArrayList<>());
    }

    public User(int id, String login, String pwd, List<User> contacts) {
        this.id = id;
        this.login = login;
        this.pwd = pwd;
        this.contacts = contacts;
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

    @XmlElement
    public List<User> getContacts() {
        return contacts;
    }

    public void addContact(User contact) {
        this.contacts.add(contact);
    }
}
