package database;

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
