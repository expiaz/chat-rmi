package client;


import shared.payload.xml.Conversation;
import shared.stub.Client;
import shared.stub.Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI implements Client{

    private Server server;
    private Client client;

    private ClientMain ui;

    public ClientRMI(ClientMain client) {
        try {
            this.ui = client;
            this.server = (Server) Naming.lookup("rmi://127.0.0.1:2000/Server");
            this.client = (Client) UnicastRemoteObject.exportObject(this,0);
        } catch (RemoteException e) {

        } catch (MalformedURLException e) {

        } catch (NotBoundException e) {

        }
    }

    public boolean connexion(String token) throws RemoteException {
        return this.server.register(token, this.client);
    }

    @Override
    public void startConversation(Conversation conversation) {

    }

    @Override
    public void sendMessage(String message) throws RemoteException {

    }
}
