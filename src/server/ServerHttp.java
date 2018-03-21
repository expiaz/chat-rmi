package server;

import database.model.Conversation;
import database.model.Message;
import database.model.User;
import shared.payload.xml.Connexion;
import shared.payload.xml.ContactCard;
import shared.payload.xml.FriendList;
import shared.payload.xml.SearchResults;
import shared.stub.Database;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.JAXBElement;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;

@WebService(serviceName = "server", portName = "8080")
@Path("/")
public class ServerHttp /*implements Server*/
{
	private Database database;

	private Map<String, User> tokens;

	private Map<String, server.Client> clients;
	private Set<server.Conversation> conversations;

	public ServerHttp()
	{
	    this.tokens = new HashMap<>();
	    this.clients = new HashMap<>();
	    this.conversations = new HashSet<>();

		try {
			this.database = (Database) Naming.lookup("rmi://127.0.0.1:2000/Database");
			User a = this.database.getUser(1);
			System.out.println(a);
		} catch (Exception e) {
			System.out.println("Database not found");
		}

		/*try {
			ServerHttp server = new ServerHttp();
			Server stub = (Server) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.getRegistry(2000);
			registry.bind("Server", stub);
		} catch (Exception e) {
			System.out.println("Server cannot be created");
		}*/
	}

    @POST
    @Path("subscribe")
    public Response subscribe(@FormParam("login") String login, @FormParam("password") String pwd)
    {
		Collection<User> users;
		try {
			users = this.database.getUsers();
		} catch (RemoteException e) {
			return Response
                    .serverError()
                    .entity("Failed to fetch users")
                    .build();
		}

		for(User user : users) {
			if (user.getLogin().equals(login)) {
			    return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Name " + login + " is already taken")
                        .build();
            }
		}

		try {
            this.database.addUser(login, pwd);
        } catch (RemoteException e) {
            return Response
                    .serverError()
                    .entity("Failed to insert user")
                    .build();
        }

        return Response
                .ok()
                .build();
    }

	@POST
	@Path("connexion")
	public Response connexion(@FormParam("login") String login, @FormParam("password") String pwd)
	{
        Collection<User> users;
	    try {
	       users = this.database.getUsers();
        } catch (RemoteException e) {
	        return Response
                    .serverError()
                    .entity("Can't connect to Database")
                    .build();
        }

		for(User user : users) {
			if (user.getLogin().equals(login) && user.getPwd().equals(pwd)) {
                UUID uid = UUID.randomUUID();
                this.tokens.put(uid.toString(), user);

                Connexion payload = new Connexion();
                payload.login = user.getLogin();
                payload.token = uid.toString();
                payload.friends = new FriendList();

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

/*    public boolean register(String token, shared.stub.Client client)
    {
        if (!this.tokens.containsKey(token)) {
            return false;
        }
        User user = this.tokens.get(token);
        this.tokens.remove(token);
        this.clients.put(token, new Client(token, user, client));
        return true;
    }*/

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

		if (!this.clients.containsKey(token)) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Token doesn't exists")
                    .build();
        }

		server.Client from = this.clients.get(token);
		Message message = new Message(from.getModel(), body, new Date());

		// this.database.addMessage(client.getConversation(), m);

        if(! from.getConversation().sendMessage(message)) {
            return Response
                    .serverError()
                    .build();
        }
		return Response
                .ok()
                .build();
	}

	@GET
	@Path("contact/search/{login}")
	@Produces(MediaType.APPLICATION_XML)
	public Response searchContact(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String token,
            @PathParam("login") String search
    ) {
	    Collection<ContactCard> found = new ArrayList<>();
        Collection<User> users;
        try {
            users = this.database.getUsers();
        } catch (RemoteException e) {
            return Response.serverError().build();
        }

        Client from = this.clients.get(token);
        Set<User> contacts = from.getModel().getContacts();
		for (User user: users) {
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
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response conversation(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String token,
            JAXBElement<shared.payload.xml.Conversation> p
    ) {



        try {
            shared.payload.xml.Conversation payload = p.getValue();
            Conversation conversation = this.database.getConversation(payload.logins);

        } catch (Exception e) {

        }
    }
}
