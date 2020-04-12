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
		MongoClient mongo = Metodos.crearConexion();
		MongoDatabase database = mongo.getDatabase("RuneterraDB");

		if (mongo != null) {
			boolean loginResult  = false, salir = false, existeCartas, existeDecks, existeUsers;
			existeCartas = Metodos.collectionExists("Cards", database);
			existeDecks = Metodos.collectionExists("Decks", database);
			existeUsers = Metodos.collectionExists("Users", database);
			if (existeCartas==false) {
				//insert de los datos por defecto, estan en el archivo json
				Metodos.cartasJsonDefecto(database);
			}
			if (existeDecks==false) {
				//insert de los datos por defecto, estan en el archivo json
				Metodos.barajasJsonDefecto(database);
			}
			if (existeUsers==false) {
				//insert de los datos por defecto, estan en el archivo json
				Metodos.usuariosJsonDefecto(database);
			}
			
			while(loginResult == false) {
				loginResult = Metodos.login(mongo, database);
				
				if(loginResult == true ) {
					while(salir == false) {
						System.out.println("=============================================");
						System.out.println("Menu");
						System.out.println("1.-Comprar cartas");
						System.out.println("2.-Crear Baraja");
						System.out.println("3.-Editar baraja");
						System.out.println("4.-Volver a dejar las barajas por defecto sin borrar las nuevas");

						System.out.println("5.-Salir");
						System.out.println("=============================================");
						int pos = lector.nextInt();

						switch (pos) {
						case 1:
							Metodos.comprarCartas(mongo, database);
							break;

						case 2:
							Metodos.crearBaraja(mongo, database);
							break;

						case 3:

							break;

						case 4:
							Metodos.barajasPredefenidas(mongo, database);
							break;

						case 5:

							break;

						case 6:
							System.out.println("Fin del programa");
							salir = true;
							break;

						default:

							break;
						}

					}
				}
			}

		} else {
			System.out.println("Error: Conexión no establecida");
		}
	}


}
