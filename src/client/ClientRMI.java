package client;


import shared.payload.rmi.Conversation;
import shared.stub.Client;
import shared.stub.Server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI implements Client{

    private Server server;
    private Client client;

    public ClientRMI(ClientMain clientMain) throws Exception{
        this.server = (Server) Naming.lookup("rmi://127.0.0.1:2000/Server");
        this.client = (Client) UnicastRemoteObject.exportObject(this,0);

    }

    public boolean connexion(String token) throws Exception{
        return this.server.register(token, this.client);
    }

    @Override
    public void startConversation(Conversation conversation) {

    }

    @Override
    public void sendMessage(String message) throws RemoteException {

    }
}
