package database;

import database.model.Conversation;
import database.model.Message;
import database.model.User;
import shared.stub.Database;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class DbMain {

	public static void main(final String[] args)
	{
		try {
			DatabaseImpl database = new DatabaseImpl();
			Database stub = (Database) UnicastRemoteObject.exportObject(database, 0);
			Registry registry = LocateRegistry.createRegistry(2000);
			registry.bind("Database", stub);


			User[] users = database.getUsers();
			System.out.println("Users - Resultats attendus : jeremy | tom | remi | jean");
			for (User user : users) {
				System.out.println(user.getLogin());
			}

			User user = database.getUser(1);
			System.out.println("\nUser - Resultat attendu : jeremy");
			System.out.println(user.getLogin());

			Integer[] ids = {1,2,3};
			Conversation conversation = database.getConversation(ids);
			System.out.println("\nConversation - Historique de conversation");
			for (Message message : conversation.getMessages()) {
				System.out.println(message.getFrom() + " : " + message.getBody());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
