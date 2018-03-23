package shared.payload.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Connexion {

    public String login;
    public String token;

    @XmlElement
    public FriendList friends;

    public Connexion() {

    }

}
