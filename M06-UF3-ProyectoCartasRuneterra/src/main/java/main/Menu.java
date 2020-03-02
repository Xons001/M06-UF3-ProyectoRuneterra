package main;

import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Menu {

	private static Scanner lector = new Scanner(System.in);

	public static void main(String[] args) {
		MongoClient mongo = crearConexion();
		MongoDatabase database = mongo.getDatabase("RuneterraDB");

		if (mongo != null) {
			boolean salir = false, salir2 = false;
			while(salir == false) {
				login(mongo, database, "Xons001", "1234");

				while(salir2 == false) {
					System.out.println("=============================================");
					System.out.println("Menu");
					System.out.println("1.-Insertar documentos");
					System.out.println("2.-Modificar documentos");
					System.out.println("3.-Eliminar documentos");
					System.out.println("4.-Buscar documento");
					System.out.println("5.-Multiples resultados de la busqueda");
					System.out.println("6.-Salir");
					System.out.println("=============================================");
					int pos = lector.nextInt();

					switch (pos) {
					case 1:

						break;

					case 2:

						break;

					case 3:

						break;

					case 4:
						System.out.println("Fin del programa");
						salir = true;
						break;

					default:

						break;
					}

				}
			} 
		} else {
			System.out.println("Error: Conexión no establecida");
		}
	}

	public static MongoClient crearConexion() {
		System.out.println("Conexión MongoDB");
		MongoClientURI uri = new MongoClientURI("mongodb+srv://Xons001:1234@legendsofruneterra-h7bku.mongodb.net/test?retryWrites=true&w=majority");

		MongoClient mongoClient = new MongoClient(uri);
		return mongoClient;
	}

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
