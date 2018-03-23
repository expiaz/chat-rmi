package server;

import database.model.User;
import shared.stub.Database;
import shared.stub.Server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerImpl implements Server {

    public static void main(String[] args) {
        ServerImpl.getInstance();
    }

    private static ServerImpl instance;

    /**
     * we need the server to be a singleton because Jersey Servlet have their own java instance
     * that we can't deal with, so the server need to be accessed from the servlet.
     * A jersey servlet is re-created each time a http request is made, so instead or re-binding and
     * un-binding each time, (that'll be error prone) we give each servlet the same instance
     * with the DB stub.
     * @return
     */
    public static ServerImpl getInstance() {
        if (null == instance) {
            instance = new ServerImpl();
        }
        return instance;
    }

    private Database database;
    private Map<String, User> tokens;
    private Map<String, Client> clients;

    /**
     * fetch the database stub, provide the server stub
     */
    private ServerImpl()
    {
        this.tokens = new HashMap<>();
        this.clients = new HashMap<>();

        System.out.println("__ServerImpl__");

        try {
            this.database = (Database) Naming.lookup("rmi://127.0.0.1:2000/Database");
			User a = this.database.getUser(1);
			System.out.println(a);
        } catch (RemoteException e) {
            System.out.println("DB : Erreur réseau");
            System.out.println(e.getMessage());
        } catch (NotBoundException e) {
            System.out.println("Database not found");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("DB : Bad URL");
            e.printStackTrace();
        }

        try {
            shared.stub.Server stub = (shared.stub.Server) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(2000);
            registry.bind("Server", stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * launch the RMI client-server connection
     * comsumes a token created when connecting via HTTP and register the client stub
     * @param token
     * @param client
     * @return
     */
    public boolean register(String token, shared.stub.Client client) {
        if (!this.tokens.containsKey(token)) {
            return false;
        }
        User user = this.tokens.get(token);
        this.tokens.remove(token);
        this.clients.put(token, new Client(token, user, client));
        return true;
    }

    /**
     * HTTP auth from login / password
     * creates a token for this user, that'll be consumed by RMI application
     * @see ServerImpl::register
     * @param user
     * @return
     */
    public String authenticate(User user) {
        String token = UUID.randomUUID().toString();
        this.tokens.put(token, user);
        return token;
    }

    /**
     * Check a given token
     * @param token
     * @return
     */
    public Client exists(String token) {
        if (!this.clients.containsKey(token)) {
            return null;
        }

        return this.clients.get(token);
    }

    /**
     * bridge to the database stub,
     * avoiding to get it from the registry each time a HTTP request is made
     * @return the database stub
     */
    public Database getDatabase() {
        return database;
    }

    public Map<String, Client> getClients() {
        return clients;
    }
}
