package client;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;


public class ClientHTTP {

    private WebResource serviceClient = null;

    private ClientMain ui;

    public ClientHTTP(ClientMain client) {
        this.ui = client;
        this.serviceClient = com.sun.jersey.api.client.Client
                .create()
                .resource("http://localhost:8080/chat_rmi_war_exploded/");
    }

    public ClientResponse connexion(String login, String password) {
        Form form = new Form();
        form.add("login", login);
        form.add("password", password);
        return serviceClient.path("/login")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(ClientResponse.class, form);
    }

    public ClientResponse subscribe(String login, String password) {
        Form form = new Form();
        form.add("login", login);
        form.add("password", password);
        return serviceClient.path("/subscribe")
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(ClientResponse.class, form);
    }

    public ClientResponse deconnexion(String token) {
        return serviceClient.path("/logout")
                .accept(MediaType.APPLICATION_XML_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(ClientResponse.class);
    }

    public ClientResponse contacts(String token) {
        return serviceClient.path("/contact/all")
                .accept(MediaType.APPLICATION_XML_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(ClientResponse.class);
    }

    public ClientResponse conversation(String token, String pseudos) {
        return serviceClient.path("/conversation")
                .accept(MediaType.APPLICATION_XML_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(ClientResponse.class, pseudos);
    }

    public ClientResponse sendMessage(String token, String body) {
        return serviceClient.path("/message")
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(ClientResponse.class, body);
    }

    public ClientResponse search(String token, String pseudo) {
        String encodedLogin = "";
        try {
            encodedLogin = URLEncoder.encode(pseudo, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return serviceClient.path("/contact/search/" + encodedLogin)
                .accept(MediaType.APPLICATION_XML_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(ClientResponse.class);
    }

    public ClientResponse add(String token, String pseudo) {
        return serviceClient.path("/contact/add/" + pseudo)
                .accept(MediaType.APPLICATION_XML_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(ClientResponse.class);
    }

    public ClientResponse delete(String token, String pseudo) {
        return serviceClient.path("/contact/delete/" + pseudo)
                .accept(MediaType.APPLICATION_XML_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .delete(ClientResponse.class);
    }

}
