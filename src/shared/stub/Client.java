package shared.stub;

import shared.payload.rmi.Conversation;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote, Serializable {

    void startConversation(Conversation conversation);

    void sendMessage(String message) throws RemoteException;

}
