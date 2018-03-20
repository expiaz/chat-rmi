import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import meteo.Ville;
import meteo.Villes;

public class Client
{
	private static WebResource serviceMeteo = null;

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
		context = JAXBContext.newInstance(Villes.class); 
		unmarshaller = context.createUnmarshaller();

		reponse = serviceMeteo.get(String.class);       

		/*
		 ** Traitement de la reponse XML : transformation en une instance de la classe Villes
		 */
		xmlStr = new StringBuffer(reponse);
		root = unmarshaller.unmarshal(new StreamSource(new StringReader(xmlStr.toString())), Villes.class);

		return root.getValue();
	}

	public static void main(String args[]) throws Exception 
	{
		Villes villes;

		serviceMeteo = Client.create().resource("http://localhost:8080/ws_war_exploded/");

		villes = listerVilles();
		for(int i = 0; i < villes.liste.size(); i++) 
		{
			System.out.println("Meteo a "         + villes.liste.get(i).getNom());
			System.out.println("  temperature : " + donnerTemperature(villes.liste. get(i)));
			System.out.println("  conditions  : " + donnerConditions(villes.liste.get(i).getNom()));
		} 
	}    
}
