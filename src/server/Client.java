package server;

import database.model.User;

public class Client {

    private String token;
    private User model;
    private shared.stub.Client stub;
    private Conversation conversation;

    public Client(String token, User model, shared.stub.Client stub)
    {
        this.token = token;
        this.stub = stub;
        this.model = model;
    }

    public String token()
    {
        return this.token;
    }

    public User getModel()
    {
        return this.model;
    }

    public shared.stub.Client getStub()
    {
        return stub;
    }

    public void setConversation(Conversation conversation)
    {
        this.conversation = conversation;
    }

    public Conversation getConversation()
    {
        return this.conversation;
    }

}
