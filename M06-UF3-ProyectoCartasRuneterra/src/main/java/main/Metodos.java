package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import objetos.Carta;


public class Metodos {

	//Conexion a la base de datos atlas
	public static MongoClient crearConexion() {
		System.out.println("Conexión MongoDB");
		MongoClientURI uri = new MongoClientURI("mongodb+srv://Xons001:1234@legendsofruneterra-h7bku.mongodb.net/test?retryWrites=true&w=majority");

		MongoClient mongoClient = new MongoClient(uri);
		return mongoClient;
	}
	
	//Retorna true si existe tabla cartas en el mongoDB
	public static boolean collectionExists(String collectionName, MongoDatabase database) {
	    MongoIterable<String> collectionNames = database.listCollectionNames();
	    for (String name : collectionNames) {
	        if (name.equalsIgnoreCase(collectionName)) {
	            return true;
	        }
	    }
	    return false;
	}

	//Los insert de los datos por defecto del archivo json
	public static void datosJsonDBDefecto(MongoDatabase database) {

		MongoCollection<Document> collection = database.getCollection("Cards");

		collection.drop();

		JSONParser parser = new JSONParser();
		File f = new File("../M06-UF3-ProyectoCartasRuneterra/Cartas.json");

		try {
			FileReader fr = new FileReader(f);
			JSONArray array = (JSONArray) parser.parse(fr);
			Iterator<?> iterator = array.iterator();

			while (iterator.hasNext()) {
				JSONObject object = (JSONObject) iterator.next();
				Carta carta = new Carta();
				carta.setId(Integer.parseInt(object.get("id").toString()));
				carta.setTipo((String) object.get("tipo"));
				carta.setNombre_carta((String) object.get("nombre_carta"));
				carta.setCoste_invocacion(Integer.parseInt(object.get("coste_invocacion").toString()));
				carta.setAtaque(Integer.parseInt(object.get("ataque").toString()));
				carta.setVida(Integer.parseInt(object.get("vida").toString()));
				carta.setHabilidad_especial((String) object.get("habilidad_especial"));
				carta.setFaccion((String) object.get("faccion"));

				Document doc = new Document("id", carta.getId()).append("tipo", carta.getTipo())
						.append("nombre_carta", carta.getNombre_carta())
						.append("coste_invocacion", carta.getCoste_invocacion()).append("ataque", carta.getAtaque())
						.append("vida", carta.getVida()).append("habilidad_especial", carta.getHabilidad_especial())
						.append("faccion", carta.getFaccion());
				
				collection.insertOne(doc);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("Cartas por defecto insertadas");
	}

	//login inacabado
	public static boolean login(MongoClient mongo, MongoDatabase database, String nickname, String password) {
		// Select the "RuneterraDB" collection
		MongoCollection<Document> collection = database.getCollection("Users");
				
		Document findDocument = new Document("nickname", nickname);
		
		MongoCursor<Document> cursor = collection.find(findDocument).iterator();

		try {           
			while (cursor.hasNext()) {
				if(password.equals(cursor.next().get("password").toString())) {
					System.out.println("Usuario correcto");
					return true;
				}else {
					System.out.println("Usuario o password erronea");
					return false;
				}
			}
		} finally {
			cursor.close();
		}
		System.out.println("No entro en la busqueda");
		return false;   

	}
}
