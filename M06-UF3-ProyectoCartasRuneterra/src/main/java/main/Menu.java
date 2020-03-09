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
			boolean salir = false, salir2 = false;
			//insert de los datos por defecto, estan en el archivo json
			Metodos.datosJsonDBDefecto(mongo, database);
			while(salir == false) {
				//Metodos.login(mongo, database, "Xons001", "1234");

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

	
}
