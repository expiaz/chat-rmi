package shared;

import java.io.Serializable;
import java.rmi.Remote;

public interface Server extends Remote, Serializable {

    void register(String token, Client client);

}
