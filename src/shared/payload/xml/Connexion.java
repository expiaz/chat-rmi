package shared.payload.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Connexion {

    public String login;
    public String token;
    public FriendList friends;

}
