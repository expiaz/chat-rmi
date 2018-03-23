package shared.payload.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContactCard {

    public String login;
    public boolean connected;
    public boolean busy;

    public ContactCard() {

    }

    public ContactCard(String login, boolean connected, boolean busy)
    {
        this.login = login;
        this.connected = connected;
        this.busy = busy;
    }

}
