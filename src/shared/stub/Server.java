package shared.stub;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote, Serializable {

    boolean register(String token, Client client) throws RemoteException;

}
