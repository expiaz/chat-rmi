package database;

import database.model.Conversation;
import database.model.Message;
import database.model.User;
import shared.stub.Database;

import java.rmi.RemoteException;
import java.util.*;

public class DatabaseImpl implements Database {

    private Map<Integer, User> users;
    private Map<List<Integer>, Conversation> conversations;

    public DatabaseImpl() {
        this.users = new HashMap<>();
        this.conversations = new HashMap<>();

        // Initialisation des utilisateurs
        User jeremy = new User(1, "jeremy", "jeremy");
        User tom = new User(2, "tom", "tom");
        User remi = new User(3, "remi", "remi");
        User jean = new User(4, "jean", "jean");

        // Initialisation des contacts
        jeremy.getContacts().add(tom);
        jeremy.getContacts().add(remi);
        tom.getContacts().add(jeremy);
        tom.getContacts().add(remi);
        remi.getContacts().add(jeremy);
        remi.getContacts().add(tom);

        // Insertion des Utilisateurs dans la BD dynamique
        try {
            this.addUser(jeremy);
            this.addUser(tom);
            this.addUser(remi);
            this.addUser(jean);
        } catch (Exception e) {

        }

        ArrayList cUsers = new ArrayList<User>();
        cUsers.add(jeremy);
        cUsers.add(remi);
        cUsers.add(tom);
        // Création d'une conversation
        Conversation conversation = new Conversation(cUsers, "Projet RMI");

        // Initialisation des messages
        Message message = new Message(jeremy, "Salut, où en êtes-vous du projet de votre côté ?", (new Date()));
        conversation.addMessage(message);
        message = new Message(tom, "En phase de tests de mon côté", (new Date()));
        conversation.addMessage(message);
        message = new Message(remi, "Très bien, on va bientôt pouvoir passer à l'intégration", (new Date()));
        conversation.addMessage(message);
        message = new Message(jeremy, "Ok, on se recontacte tout à l'heure...", (new Date()));
        conversation.addMessage(message);

        try {
            this.addConversation(conversation);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    @Override
    public User[] getUsers() {
        return this.users.values().toArray(new User[this.users.size()]);
    }

    @Override
    public User getUser(int id) throws RemoteException {
        return this.users.get(id);
    }

    @Override
    public void addUser(User user) throws RemoteException {
        this.users.put(user.getId(), user);
    }

    @Override
    public void addUser(String login, String pwd) throws RemoteException {
        this.addUser(new User(this.users.size()+1, login, pwd));
    }

    public Conversation getConversation(User[] users) throws RemoteException {
        return this.getConversation((Integer[]) Arrays.stream(users).map(user -> user.getId()).toArray());
    }

    public Conversation getConversation(Integer[] ids) throws RemoteException {
        /*Object[] tab = Arrays.stream(ids).sorted(Integer::compareTo).toArray();
        for (Object id :
                tab) {
            System.out.println(id);
        }*/
        Conversation conversation = this.conversations.get(Arrays.asList(Arrays.stream(ids).sorted(Integer::compareTo).toArray()));
        if (conversation == null){
            ArrayList users = new ArrayList();
            for (int i=0; i<ids.length; i++){
                users.add(this.getUser(ids[i]));
            }
            conversation = new Conversation(users, "Nouvelle conversation");
        }
        return conversation;
    }

    @Override
    public void addConversation(Conversation conversation) throws RemoteException {
        /*Conversation put = this.conversations.put(
                (Integer[]) conversation.getUsers().stream().map(user -> user.getId()).sorted(Integer::compareTo).toArray(),
                conversation
        );*/

        User[] users = conversation.getUsers().toArray(new User[conversation.getUsers().size()]);
        List<Integer> k = new ArrayList<>();
        for(int i=0; i<users.length; i++){
            k.add(users[i].getId());
        }
        k.sort(Integer::compareTo);
        this.conversations.put(k, conversation);
    }
}
