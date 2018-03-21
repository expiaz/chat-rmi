package server;

import database.model.User;
import shared.stub.Database;

import java.util.Map;
import java.util.Set;

public class ServerMain {

    public static void main(String[] args)
    {

    }

    private Database database;

    private Map<String, User> tokens;

    private Map<String, server.Client> clients;
    private Set<Conversation> conversations;

}
