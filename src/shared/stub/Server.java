package shared.stub;

import database.model.User;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface Server extends Remote, Serializable {

    /*
    CLIENT API
     */

    boolean register(String token, Client client) throws RemoteException;

}
