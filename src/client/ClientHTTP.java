package client;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;


public class ClientHTTP
{

	private WebResource serviceClient = null;
	private String token;

	public ClientHTTP(ClientMain clientIU){
		this.token = "";
		this.serviceClient = com.sun.jersey.api.client.Client
				.create().resource("http://localhost:8080/ws_war_exploded/");
	}

	public ClientResponse connexion(String login, String password){
		Form form = new Form();
		form.add("login", login);
		form.add("password", password);
		return serviceClient.path("/login")
				 .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				 .post(ClientResponse.class, form);

	}

	public ClientResponse deconnexion(String token) throws Exception {

		return serviceClient.path("/logout/")
				.accept(MediaType.APPLICATION_XML_TYPE)
				.header(HttpHeaders.AUTHORIZATION, token)
				.get(ClientResponse.class);
	}

	public ClientResponse search(String token, String login) throws Exception {
		return serviceClient.path("/contact/search/"+login)
				.accept(MediaType.APPLICATION_XML_TYPE)
				.header(HttpHeaders.AUTHORIZATION, token)
				.get(ClientResponse.class);
	}

	public  ClientResponse add(String token, String pseudo)throws Exception {
		return serviceClient.path("/contact/"+pseudo)
				.accept(MediaType.APPLICATION_XML_TYPE)
				.header(HttpHeaders.AUTHORIZATION, token)
				.post(ClientResponse.class);
	}

	public ClientResponse subscribe(String login, String password)throws Exception {
		Form form = new Form();
		form.add("login", login);
		form.add("password", password);
		return serviceClient.path("/subscribe")
				.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.post(ClientResponse.class, form);
	}

	public ClientResponse delete(String token, String login){
		return serviceClient.path("/delete"+login)
				.accept(MediaType.APPLICATION_XML_TYPE)
				.header(HttpHeaders.AUTHORIZATION, token)
				.delete(ClientResponse.class);
	}

	public void setToken(String token){
		this.token = token;
	}

	/*public ClientResponse conversation(){

	}*/




	/*private static WebResource serviceMeteo = null;

	private static String donnerConditions(String ville) throws Exception 
	{
		return serviceMeteo.path("donnerConditions/" + ville).get(String.class);
	}

	private static String donnerTemperature(Ville ville) throws Exception 
	{       
		return serviceMeteo.path("donnerTemperature").put(String.class, ville);
	}

	private static Villes listerVilles() throws Exception 
	{
		String reponse;
		StringBuffer xmlStr;
		JAXBContext context;       
		JAXBElement<Villes> root;       
		Unmarshaller unmarshaller;

		/*
		 ** Instanciation du convertiseur XML => Objet Java
		 */
		/*context = JAXBContext.newInstance(Villes.class);
		unmarshaller = context.createUnmarshaller();

		reponse = serviceMeteo.get(String.class);       

		/*
		 ** Traitement de la reponse XML : transformation en une instance de la classe Villes
		 */
		/*xmlStr = new StringBuffer(reponse);
		root = unmarshaller.unmarshal(new StreamSource(new StringReader(xmlStr.toString())), Villes.class);

		return root.getValue();
	}

	public static void main(
	{
		Villes villes;

		serviceMeteo = client.ClientHTTP.create().resource("http://localhost:8080/ws_war_exploded/");

		villes = listerVilles();
		for(int i = 0; i < villes.liste.size(); i++) 
		{
			System.out.println("Meteo a "         + villes.liste.get(i).getNom());
			System.out.println("  temperature : " + donnerTemperature(villes.liste. get(i)));
			System.out.println("  conditions  : " + donnerConditions(villes.liste.get(i).getNom()));
		} 
	} */
}
