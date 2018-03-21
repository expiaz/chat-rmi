package shared.payload.xml;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@XmlRootElement
public class Conversation {

    public Collection<String> logins;

}
