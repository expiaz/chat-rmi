package shared.payload.xml;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@XmlRootElement
public class FriendList {

    public Collection<ContactCard> friends;
    public Collection<ContactCard> requests;

}
