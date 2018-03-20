package server;

import database.model.Conversation;
import database.model.Message;
import database.model.User;
import shared.Database;
import shared.Server;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Map;

@WebService(serviceName = "server", portName = "8080")
@Path("/")
public class ServerImpl implements Server
{
	private Database database;

	private Map<String, Client> clients;

	public ServerImpl()
	{
		try {
			this.database = (Database) Naming.lookup("rmi://127.0.0.1:2000/Database");
			User a = this.database.getUser(1);
			System.out.println(a);
		} catch (Exception e) {
			System.out.println("Database not found");
		}

		try {
			ServerImpl server = new ServerImpl();
			Server stub = (Server) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.getRegistry(2000);
			registry.bind("Server", stub);
		} catch (Exception e) {
			System.out.println("Server cannot be created");
		}
	}

	public void register(String token, shared.Client client)
	{

	}

	@POST
	@Path("connexion")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_XML)
	public Response connexion(@FormParam("login") String login, @FormParam("pwd") String pwd)
	{
	    User defaut = new User(0, "", "");
	    int status = 301;

		for(User user : this.database.getUsers()) {
			if (user.getLogin().equals(login) && user.getPwd().equals(pwd)) {
				// connect
                defaut = user;
                status = 200;
                break;
			}
		}

        return Response.status(status)
                .entity(defaut)
                .type(MediaType.APPLICATION_XML)
                .build();
	}

	@POST
	@PathParam("message")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response sendMessage(@FormParam("token") String token, @FormParam("body") String body)
	{
		if (body.length() > 140) {
			body = body.substring(0, 140);
		}

		Client c = this.clients.get(token);
		Message m = new Message(c.getUser(), new Date(), body);

		this.database.addMessage(c.getConv(), m);

		Client[] stubs = c.getConv().getStubs();
		for (stub : stubs) {
			stub.sendMessage(m.getFrom() + " : " + m.getBody());
		}

		return Response.status(200).build();
	}

	@GET
	@Path("contact/search/{login}")
	@Produces(MediaType.APPLICATION_XML)
	public Response searchContact(@PathParam("login") String search)
	{
        User defaut = new User(0, "", "");
        int status = 301;

		for (User user: this.database.getUsers()) {
		    if (user.getLogin().matches(search)) {
		        defaut = user;
		        status = 200;
            }
        }

        return Response.status(status)
                .entity(defaut)
                .type(MediaType.APPLICATION_XML)
                .build();
	}

	@POST
    @Path("conversation")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response conversation(JAXBElement<ConversationPayload> p)
    {
        try {
            ConversationPayload payload = p.getValue();
            Conversation conversation = this.database.getConversation(payload.users);

        } catch (Exception e) {

        }
    }
}
