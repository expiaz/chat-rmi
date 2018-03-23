package shared.payload.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class Conversation {

    public String name;

    @XmlElement
    public ArrayList<String> messages;

    public Conversation(String name, ArrayList<String> messages) {
        this.name = name;
        this.messages = messages;
    }
}
