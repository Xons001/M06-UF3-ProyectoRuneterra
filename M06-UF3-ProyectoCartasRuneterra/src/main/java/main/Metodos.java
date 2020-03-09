package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


public class Metodos {

	//Conexion a la base de datos atlas
	public static MongoClient crearConexion() {
		System.out.println("Conexión MongoDB");
		MongoClientURI uri = new MongoClientURI("mongodb+srv://Xons001:1234@legendsofruneterra-h7bku.mongodb.net/test?retryWrites=true&w=majority");

		MongoClient mongoClient = new MongoClient(uri);
		return mongoClient;
	}

	//Los insert de los datos por defecto del archivo json
	public static void datosJsonDBDefecto(MongoClient mongo, MongoDatabase database) {

		//JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();

		try (FileReader reader = new FileReader("../M06-UF3-ProyectoCartasRuneterra/Cartas.json"))
		{
			//Read JSON file
			Object obj = jsonParser.parse(reader);

			JSONArray cartasList = (JSONArray) obj;
			System.out.println(cartasList);
			
			MongoCollection<Document> collection = database.getCollection("Cartas");
			
			List<Document> documents = new ArrayList<Document>();
			for (int i = 0; i < 100; i++) {
			    //documents.add(new Document("id", carta.getId()).append("tipo", carta.getTipo()).append("nombre_carta", carta.getNombre_carta()).append("coste_invocacion", carta.getCoste_invocacion()).append("ataque", carta.getAtaque()).append("vida", carta.getVida()).append("habilidad_especial", carta.getHabilidad_especial()).append("faccion", carta.getFaccion());
			}

			collection.insertMany(documents);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void parseCartaObject(JSONObject carta) {
		//Get employee object within list
		JSONObject cartaObject = (JSONObject) carta.get("cartas");

		int idcarta = (int) cartaObject.get("id");    
		System.out.println(idcarta);

		String tipo = (String) cartaObject.get("tipo");  
		System.out.println(tipo);
		
		String nombre_carta = (String) cartaObject.get("nombre_carta");  
		System.out.println(nombre_carta);

		int coste_invocacion = (int) cartaObject.get("coste_invocacion");    
		System.out.println(coste_invocacion);
		
		int ataque = (int) cartaObject.get("ataque");    
		System.out.println(ataque);
		
		int vida = (int) cartaObject.get("vida");    
		System.out.println(vida);
		
		String habilidad_especial = (String) cartaObject.get("habilidad_especial");    
		System.out.println(habilidad_especial);
		
		String faccion = (String) cartaObject.get("faccion");    
		System.out.println(faccion);
		
	}

	//login inacabado
	public static void login(MongoClient mongo, MongoDatabase database, String nickname, String password) {
		// Select the "RuneterraDB" collection
		MongoCollection<Document> collection = database.getCollection("Users");

		MongoCursor<Document> cursor = collection.find().iterator();

		try {           
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				//Document nicknameDoc=(Document) doc.get("nickname");
				String nicknameText = doc.getString("nickname");
				System.out.println(nicknameText);
				if(nicknameText.equalsIgnoreCase(nickname)) {
					String cont=doc.getString("password");       
					if (cont.equalsIgnoreCase(password)) {

					}
				}
			}
		} finally {
			cursor.close();
		}   

	}
}
