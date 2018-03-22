package shared.stub;

import database.model.Conversation;
import database.model.User;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Database extends Remote, Serializable {


    User[] getUsers() throws RemoteException;

    User getUser(int id) throws RemoteException;

    void setUser(User user) throws RemoteException;

    Conversation getConversation(User[] users) throws RemoteException;

    Conversation getConversation(Integer[] ids) throws RemoteException;

    void setConversation(Conversation conversation) throws RemoteException;

}
