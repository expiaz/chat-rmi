package server;

import database.model.Conversation;
import database.model.Message;
import database.model.User;
import shared.payload.xml.Connexion;
import shared.payload.xml.ContactCard;
import shared.payload.xml.FriendList;
import shared.payload.xml.SearchResults;
import shared.stub.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.JAXBElement;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

@Path("/")
public class ServerHttp {

    private ServerImpl server;
    private Database database;

    public ServerHttp() {
        System.out.println("__ServerHttp__");
        this.server = ServerImpl.getInstance();
        this.database = this.server.getDatabase();
    }

    @GET
    @Path("test")
    public Response test() {
        User[] users;
        try {
            users = this.database.getUsers();
            StringBuilder body = new StringBuilder("<ul>");
            for (User user : users) {
                body.append("<li>");
                body.append(user.getLogin());
                body.append("</li>");
            }
            body.append("</ul>");
            return Response.status(200).type(MediaType.TEXT_HTML_TYPE).entity(body.toString()).build();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return Response.serverError().build();
    }

    @POST
    @Path("subscribe")
    public Response subscribe(@FormParam("login") String login, @FormParam("password") String pwd) {
        User[] users;
        try {
            users = this.database.getUsers();
        } catch (RemoteException e) {
            return Response
                    .serverError()
                    .entity("Failed to fetch users")
                    .build();
        }

        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Name " + login + " is already taken")
                        .build();
            }
        }

		/*try {
            this.database.addUser(login, pwd);
        } catch (RemoteException e) {
            return Response
                    .serverError()
                    .entity("Failed to insert user")
                    .build();
        }*/

        return Response
                .ok()
                .build();
    }

    @POST
    @Path("login")
    public Response connexion(@FormParam("login") String login, @FormParam("password") String pwd) {
        User[] users;
        try {
            users = this.server.getDatabase().getUsers();
        } catch (RemoteException e) {
            return Response
                    .serverError()
                    .entity("Can't connect to Database")
                    .build();
        }

        System.out.println("__connexion__ " + login + " " + pwd);

        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPwd().equals(pwd)) {

                String token = this.server.authenticate(user);

                Connexion payload = new Connexion();
                payload.login = user.getLogin();
                payload.token = token;
                payload.friends = new FriendList();

                for (Client client : this.server.getClients().values()) {
                    for (User contact : user.getContacts()) {
                        if (client.getModel().getId() == contact.getId()) {
                            payload.friends.friends.add(new ContactCard(
                                    contact.getLogin(),
                                    true,
                                    client.getConversation() != null
                            ));
                        }
                    }
                }

                return Response
                    .ok(payload, MediaType.APPLICATION_XML)
                    .build();
            }
        }

        return Response
                .status(Response.Status.NOT_FOUND)
                .entity("Bad credentials")
                .build();
    }

    @POST
    @Path("message")
    public Response sendMessage(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String token,
            String body
    ) {
        if (body.length() == 0) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Body is empty")
                    .build();
        }
        if (body.length() > 140) {
            body = body.substring(0, 140);
        }

        Client client = this.server.exists(token);
        if (client == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Token doesn't exists")
                    .build();
        }

        if (client.getConversation() == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Not in a conversation")
                    .build();
        }

        Message message = new Message(client.getModel(), body, new Date());

        // this.database.addMessage(client.getConversation(), m);

        if (!client.getConversation().sendMessage(message)) {
            return Response
                    .serverError()
                    .entity("Failed to send message")
                    .build();
        }

        return Response
                .ok()
                .build();
    }

    @GET
    @Path("contact/search/{login}")
    public Response searchContact(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String token,
            @PathParam("login") String search
    ) {
        Collection<ContactCard> found = new ArrayList<>();
        User[] users;
        try {
            users = this.database.getUsers();
        } catch (RemoteException e) {
            return Response
                    .serverError()
                    .entity("Can't connect to Database")
                    .build();
        }

        Client client = this.server.exists(token);
        if (client == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Token doesn't exists")
                    .build();
        }

        Set<User> contacts = client.getModel().getContacts();
        for (User user : users) {
            if (!contacts.contains(user) && user.getLogin().matches(search)) {
                found.add(new ContactCard(user.getLogin(), false, false));
            }
        }

        SearchResults results = new SearchResults();
        results.results = found;
        return Response
                .ok(results, MediaType.APPLICATION_XML)
                .build();
    }

    @POST
    @Path("conversation")
    public Response conversation(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String token,
            String logins
    ) {

        Client client = this.server.exists(token);
        if (client == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Token doesn't exists")
                    .build();
        }

        Collection<Integer> ids = new ArrayList<>();
        ids.add(client.getModel().getId());

        Collection<Client> clients = this.server.getClients().values();
        for(String login : logins.split(" ")) {
            for(Client c : clients) {
                if (c.getModel().getLogin().equals(login)) {
                    if (c.getConversation() != null) {
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity("User " + login + " is busy")
                                .build();
                    }
                    ids.add(client.getModel().getId());
                }
            }
        }

        Conversation conversation;
        try {
            conversation = this.database.getConversation(ids.toArray(new Integer[ids.size()]));
        } catch (RemoteException e) {
            return Response
                    .serverError()
                    .entity("Can't retrieve conversation")
                    .build();
        }

        shared.payload.xml.Conversation transformedConv = new shared.payload.xml.Conversation(
                conversation.getName(),
                (ArrayList<String>) conversation.getMessages().stream()
                        .sorted(Comparator.comparing(a -> a.getTimestamp()))
                        .map(message -> "[" + message.getFrom() + "] " + message.getBody())
                        .collect(Collectors.toList())
        );

        return Response
                .ok(transformedConv, MediaType.APPLICATION_XML)
                .build();
    }
}
