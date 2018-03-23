package client;

import com.sun.jersey.api.client.ClientResponse;
import shared.payload.xml.Connexion;
import shared.payload.xml.ContactCard;
import shared.payload.xml.Conversation;
import shared.payload.xml.FriendList;

import javax.ws.rs.core.Response;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientMain {

    public static void main(String args[]) throws Exception {
        new ClientMain().bootstrap();
    }

    private ClientHTTP http;
    private ClientRMI rmi;

    private Scanner input;
    private PrintStream output;

    private String displayName = "";
    private String token = "";

    public ClientMain() {
        this.http = new ClientHTTP(this);
        this.rmi = new ClientRMI(this);

        this.output = System.out;
        this.input = new Scanner(System.in);
    }

    boolean handleError(ClientResponse response) {
        if (response.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            this.output.println("Erreur serveur : ");
            this.output.println(response.getEntity(String.class));
            return false;
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            this.output.println("Erreur client : ");
            this.output.println(response.getEntity(String.class));
            return false;
        } else if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return true;
        } else {
            this.output.println("Erreur inconnue : " + response.getStatus());
            return false;
        }
    }

    public String getToken() {
        if (this.token.length() == 0) {
            // error
            this.output.println("Veuillez vous connectez avant de pouvoir acceder aux fonctionnalités autres");
        }
        return this.token;
    }

    public void bootstrap() {
        this.output.println("Bienvenue sur ChatBot 3.0");
        this.connect();
        while (true) {
            try {
                this.output.print("> ");
                this.parse(this.input.nextLine());
            } catch (RemoteException e) {
                this.output.println("Une erreur est survenu, c'est balo");
            }
        }
    }

    private void parse(String cmd) throws RemoteException {
        String[] pieces = cmd.trim().split(" ");
        ClientResponse res;

        if (pieces.length == 0) {
            return;
        }

        switch (pieces[0]) {
            case "list":
                // 0
                res = this.http.contacts(this.token);
                if (handleError(res)) {
                    displayFriendList(res.getEntity(FriendList.class).friends);
                }
                break;
            case "connect":
                if (pieces.length < 2) return;
                StringBuilder pseudos = new StringBuilder(pieces[1]);
                for(int i = 2; i < pieces.length; i++) {
                    pseudos.append(" ");
                    pseudos.append(pieces[i]);
                }
                res = this.http.conversation(this.token, pseudos.toString());
                if (handleError(res)) {
                    displayConversation(res.getEntity(Conversation.class));
                }
                // 1+ args
                break;
            case "disconnect":
                res = this.http.deconnexion(this.token);
                if (handleError(res)) {
                    this.parse("list");
                }
                // 0
                break;
            case "exit":
                res = this.http.deconnexion(this.token);
                if (handleError(res)) {
                    System.exit(0);
                }
                // 0
                break;
            case "add":
                if (pieces.length < 2) return;
                res = this.http.add(this.token, pieces[1]);
                if (handleError(res)) {
                    // TODO add
                }
                // 1
                break;
            case "delete":
                if (pieces.length < 2) return;
                res = this.http.delete(this.token, pieces[1]);
                if (handleError(res)) {
                        // TODO delete
                }
                // 1
                break;
            case "search":
                if (pieces.length < 2) return;
                res = this.http.search(this.token, pieces[1]);
                if (handleError(res)) {
                    // TODO search
                }
                // 1
                break;
            default:
                // message
                res = this.http.sendMessage(this.token, cmd);
                if (handleError(res)) {
                    // TODO print
                }
                //this.output.println("Command inconnue, commandes existantes : <help>");
        }

        switch (pieces.length) {
            case 2:
                // arg command
                break;
            default:
                // conversation
                break;
        }
    }

    private void connect() {
        String login;
        String password;
        boolean valid = false;
        ClientResponse response;

        while (!valid) {
            login = "";
            password = "";

            while (login.length() == 0) {
                this.output.print("Login : ");
                login = this.input.nextLine().trim();
            }

            while (password.length() == 0) {
                this.output.print("Password : ");
                password = this.input.nextLine().trim();
            }

            response = this.http.connexion(login, password);
            valid = handleError(response);

            if (valid) {
                Connexion connexion = response.getEntity(Connexion.class);
                this.token = connexion.token;
                this.displayName = connexion.login;

                this.output.println("Connecté en tant que " + this.displayName);

                try {
                    this.rmi.connexion(token);
                    this.output.println("Connecté au serveur de chat");
                } catch (RemoteException e) {
                    this.output.println("Impossible de se connected au serveur de chat");
                }

                FriendList friends = connexion.friends;
                this.displayFriendRequests(friends.requests);
                this.displayFriendList(friends.friends);
            }
        }
    }

    private void displayConversation(Conversation conversation) {
        this.output.println("Conversation " + conversation.name);
        for (String message : conversation.messages) {
            this.output.println(message);
        }
    }

    private void displayFriendRequests(ArrayList<ContactCard> requests) {
        if (requests.size() > 0) {
            String accept = "";
            this.output.println("\nVous avez des demandes d'amis");
            for (ContactCard request : requests) {
                this.output.println(request.login + " voudrait être votre amis, accepter ? (y/n)");
                //accept = this.input.nextLine();
            }
        }
    }

    private void displayFriendList(ArrayList<ContactCard> friends) {
        this.output.println("\nListe d'amis :");
        for(ContactCard contact : friends) {
            this.output.println("- " + contact.login + " " + (
                    contact.connected ? (contact.busy ? "[busy]" : "[on]") : ""
            ));
        }
    }

}
