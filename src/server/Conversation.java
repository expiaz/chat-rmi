package server;

import database.model.Message;

import java.rmi.RemoteException;
import java.util.Set;

public class Conversation {

    private Set<Client> clients;
    private Set<Message> messages;

    public Conversation(Set<Client> clients, Set<Message> messages)
    {
        this.clients = clients;
        this.messages = messages;

        for(Client client : clients) {
            client.setConversation(this);
        }
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void removeClient(Client client)
    {
        this.clients.remove(client);
    }

    public boolean sendMessage(Message message)
    {
        this.messages.add(message);

        String msg = "[" + message.getTimestamp() + "] " + message.getFrom() + " : " + message.getBody();

        for(Client client : this.clients) {
            try {
                client.getStub().sendMessage(msg);
            } catch (RemoteException e) {
                // TODO deco
                this.removeClient(client);
                return false;
            }
        }
        return true;
    }

}
