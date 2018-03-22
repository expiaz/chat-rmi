package database;

import database.model.Conversation;
import database.model.Message;
import database.model.User;
import shared.stub.Database;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class DbMain {

    public static void main(final String[] args) {
        try {
            DatabaseImpl database = new DatabaseImpl();
            Database stub = (Database) UnicastRemoteObject.exportObject(database, 0);
            Registry registry = LocateRegistry.createRegistry(2000);
            registry.bind("Database", stub);

			User[] users = database.getUsers();
			System.out.println("getUsers - Resultats attendus : jeremy | tom | remi | jean");
			for (User user : users) {
				System.out.println(user.getLogin());
			}

			User user = database.getUser(1);
			System.out.println("\ngetUser(1) - Resultat attendu : jeremy");
			System.out.println(user.getLogin());

			Integer[] ids = {3,2,1};
			Conversation conversation = database.getConversation(ids);
			System.out.println("\nConversation - Historique de conversation :");
			System.out.println(conversation.getName());
			for (Message message : conversation.getMessages()) {
				System.out.println(message.getFrom() + " : " + message.getBody());
			}
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

}
