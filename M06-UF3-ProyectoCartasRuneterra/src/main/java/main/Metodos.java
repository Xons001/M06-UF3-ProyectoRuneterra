package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import objetos.Carta;

public class Metodos {

	//Conexion a la base de datos atlas
	public static MongoClient crearConexion() {
		System.out.println("Conexión MongoDB");
		MongoClientURI uri = new MongoClientURI("mongodb+srv://Xons001:1234@legendsofruneterra-h7bku.mongodb.net/test?retryWrites=true&w=majority");

		MongoClient mongoClient = new MongoClient(uri);
		return mongoClient;
	}
	
	//Los insert de los datos por defecto del archivo json
	public static void datosJsonDBDefecto(MongoClient mongo) {
		Gson gson = new Gson();
		String fichero = "";
		MongoDatabase database = mongo.getDatabase("Cartas");
 
		try (BufferedReader br = new BufferedReader(new FileReader("../M06-UF3-ProyectoCartasRuneterra/Cartas.json"))) {
		    String linea;
		    while ((linea = br.readLine()) != null) {
		        fichero += linea;
		    }
		    Carta carta = gson.fromJson(fichero, Carta.class);
		    
		    // Select the collection
			MongoCollection<Document> collection = database.getCollection("Consolas");
			
			// Insertamos los datos en el documento
			Document document = new Document("id", carta.getId()).append("tipo", carta.getTipo()).append("nombre_carta", carta.getNombre_carta()).append("coste_invocacion", carta.getCoste_invocacion()).append("ataque", carta.getAtaque()).append("vida", carta.getVida()).append("habilidad_especial", carta.getHabilidad_especial()).append("faccion", carta.getFaccion());
			// Insert the document in the collection
			collection.insertOne(document);
		} catch (FileNotFoundException ex) {
		    System.out.println(ex.getMessage());
		} catch (IOException ex) {
		    System.out.println(ex.getMessage());
		}
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
