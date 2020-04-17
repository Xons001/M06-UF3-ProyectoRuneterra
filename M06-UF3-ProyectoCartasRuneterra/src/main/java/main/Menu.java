package main;

import java.util.Scanner;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu {

	private static Scanner lector = new Scanner(System.in);

	public static void main(String[] args) {
		disableLogs();

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
						System.out.print("Escoge la opcion que quieres: ");
						int pos = lector.nextInt();
						System.out.println("---------------------------------------------");
						switch (pos) {
						case 1:
							Metodos.comprarCartas(mongo, database);
							break;

						case 2:
							Metodos.crearBaraja(mongo, database);
							break;

						case 3:
							Metodos.editarBaraja(mongo, database);
							break;

						case 4:
							Metodos.barajasPredefenidas(mongo, database);
							break;

						case 5:
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


	private static void disableLogs() {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);
		mongoLogger.getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.management").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.cluster").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.protocol.insert").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.protocol.query").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.protocol.update").setLevel(Level.OFF);
	}
}
