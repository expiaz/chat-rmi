package shared.payload.xml;

import database.model.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashSet;

@XmlRootElement
public class FriendList {

    @XmlElement
    public ArrayList<ContactCard> friends;
    @XmlElement
    public ArrayList<ContactCard> requests;

    public FriendList() {
        this.friends = new ArrayList<>();
        this.requests = new ArrayList<>();
    }

}
