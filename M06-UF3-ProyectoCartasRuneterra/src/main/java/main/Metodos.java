package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import objetos.Baraja;
import objetos.Carta;
import objetos.Usuarios;

import static com.mongodb.client.model.Filters.*;


public class Metodos {

	//Conexion a la base de datos atlas
	public static MongoClient crearConexion() {
		System.out.println("Conexión MongoDB");
		MongoClientURI uri = new MongoClientURI("mongodb+srv://Xons001:1234@legendsofruneterra-h7bku.mongodb.net/test?retryWrites=true&w=majority");

		MongoClient mongoClient = new MongoClient(uri);
		return mongoClient;
	}

	public static Number leerInt() {
		Scanner lector = new Scanner(System.in);
		Number num = lector.nextInt();
		return num;
	}

	public static String leerString() {
		Scanner lector = new Scanner(System.in);
		String texto = lector.nextLine();
		return texto;
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
	//Cartas
	public static void cartasJsonDefecto(MongoDatabase database) {

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

	//Barajas
	public static void barajasJsonDefecto(MongoDatabase database) {

		MongoCollection<Document> collection = database.getCollection("Decks");

		collection.drop();

		JSONParser parser = new JSONParser();
		File f = new File("../M06-UF3-ProyectoCartasRuneterra/Barajas.json");

		try {
			FileReader fr = new FileReader(f);
			JSONArray array = (JSONArray) parser.parse(fr);
			Iterator<?> iterator = array.iterator();

			while (iterator.hasNext()) {
				JSONObject object = (JSONObject) iterator.next();
				Baraja baraja = new Baraja();
				baraja.setBaraja_id(Integer.parseInt(object.get("baraja_id").toString()));
				baraja.setNombre_baraja((String) object.get("nombre_baraja"));
				baraja.setValor_baraja(Integer.parseInt(object.get("valor_baraja").toString()));
				baraja.setCartas_baraja((ArrayList<Integer>) object.get("cartas_barajas"));

				Document doc = new Document("baraja_id", baraja.getBaraja_id()).append("nombre_baraja", baraja.getNombre_baraja())
						.append("valor_baraja", baraja.getValor_baraja()).append("cartas_barajas", baraja.getCartas_baraja());

				collection.insertOne(doc);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("Barajas por defecto insertadas");
	}

	//Usuarios
	public static void usuariosJsonDefecto(MongoDatabase database) {

		MongoCollection<Document> collection = database.getCollection("Users");

		collection.drop();

		JSONParser parser = new JSONParser();
		File f = new File("../M06-UF3-ProyectoCartasRuneterra/Usuarios.json");

		try {
			FileReader fr = new FileReader(f);
			JSONArray array = (JSONArray) parser.parse(fr);
			Iterator<?> iterator = array.iterator();

			while (iterator.hasNext()) {
				JSONObject object = (JSONObject) iterator.next();
				Usuarios usuario = new Usuarios();
				usuario.setUsuario_id(Integer.parseInt(object.get("usuario_id").toString()));
				usuario.setCont_usuario((String) object.get("cont_usuario"));
				usuario.setNom_usuario((String) object.get("nom_usuario"));

				usuario.setBarajas_usuario((ArrayList<Integer>) object.get("barajas_usuario"));
				usuario.setCartas_compradas((ArrayList<Integer>) object.get("cartas_compradas"));

				Document doc = new Document("usuario_id", usuario.getUsuario_id()).append("cont_usuario", usuario.getCont_usuario())
						.append("nom_usuario", usuario.getNom_usuario()).append("barajas_usuario", usuario.getBarajas_usuario())
						.append("cartas_compradas", usuario.getCartas_compradas());

				collection.insertOne(doc);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("Usuarios por defecto insertados");
	}

	//la variable user con el login
	private static Usuarios loginUser = new Usuarios();

	//login
	public static boolean login(MongoClient mongo, MongoDatabase database) {

		boolean resultLogin = false; 

		// Select the "RuneterraDB" collection
		MongoCollection<Document> collection = database.getCollection("Users");

		Usuarios user = new Usuarios();

		System.out.println("-----------------------------------------------------");
		System.out.println("Iniciando sesion");
		System.out.print("Introduce el nombre del usuario: ");
		String nombreUser = leerString();
		System.out.print("Introduce la cont del usuario: ");
		String contUser = leerString();
		System.out.println("-----------------------------------------------------");

		try {           
			Document buscarDoc = new Document();

			buscarDoc.append("nom_usuario", nombreUser);
			buscarDoc.put("cont_usuario", contUser);

			Iterator<Document> iterator = collection.find(buscarDoc).iterator();

			if (iterator.hasNext()) {

				//Datos que estan guardados en el mongoDB Atlas
				Document doc = iterator.next();

				//user
				user.setUsuario_id(doc.getInteger("usuario_id"));
				user.setCont_usuario(doc.getString("cont_usuario"));
				user.setNom_usuario(doc.getString("nom_usuario"));
				user.setBarajas_usuario((ArrayList<Integer>) doc.get("barajas_usuario"));
				user.setCartas_compradas((ArrayList<Integer>) doc.get("cartas_compradas"));

				//user guardado como variable externa para poder utilizarla en otros metodos
				loginUser.setUsuario_id(user.getUsuario_id());
				loginUser.setCont_usuario(user.getCont_usuario());
				loginUser.setNom_usuario(user.getNom_usuario());
				loginUser.setBarajas_usuario((ArrayList<Integer>)user.getBarajas_usuario());
				loginUser.setCartas_compradas((ArrayList<Integer>)user.getCartas_compradas());

				System.out.println("Has entrado con exito!");
				System.out.println("-----------------------------------------------------");

				System.out.println("Informacion usuario:");
				System.out.println("ID: " + loginUser.getUsuario_id());
				System.out.println("Nombre: " + loginUser.getNom_usuario());
				System.out.println("Cartas compradas: " + loginUser.getCartas_compradas());
				System.out.println("Baraja que obtienes: " + loginUser.getBarajas_usuario());
				System.out.println("-----------------------------------------------------");

				resultLogin = true;

			} else {
				System.out.println("Usuario o contra incorrecta, vuelve a escribir los datos");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Usuario o contra incorrecta");
			resultLogin = false;
		}

		return resultLogin;
	}

	public static void comprarCartas(MongoClient mongo, MongoDatabase database) {
		// Select the "RuneterraDB" collection
		MongoCollection<Document> collectionUsers = database.getCollection("Users");
		MongoCollection<Document> collectionCards = database.getCollection("Cards");

		boolean salir = false;

		FindIterable<Document> todasCartas = collectionCards.find();

		ArrayList<Integer> arrayCartasCompradas = loginUser.getCartas_compradas();
		ArrayList<Integer> arrayCartasDisponibles = new ArrayList<Integer>();

		for (Document document : todasCartas) {
			arrayCartasDisponibles.add(document.getInteger("id"));
		}

		while (salir == false) {
			System.out.print("Escribe la carta id que quieres comprar, escribe un numero negativo para finalizar la compra => ");
			Number carta_id = leerInt();

			if (carta_id.intValue() < 0) {
				salir = true;
				System.out.println("Finalizacion de la compra de cartas");
			} else {
				boolean existe = false; 
				for (Number busquedaArrayDisponibles : arrayCartasDisponibles) {
					if (carta_id == busquedaArrayDisponibles) {
						for (Number busquedaArrayCompradas : arrayCartasCompradas) {
							if (!arrayCartasCompradas.contains(carta_id)) {
								System.out.println("Carta " + carta_id + " comprada para el usuario " + loginUser.getNom_usuario());
								arrayCartasCompradas.add(carta_id.intValue());
								existe = true;
								break;
							}
						}

					}
				}
				if (existe == false) {
					System.out.println("La carta no existe o ya esta en lista comprada, no se puede insertar");
				}

			}
		}

		Document query = new Document("usuario_id", loginUser.getUsuario_id());
		Document newDoc = new Document("cartas_compradas", arrayCartasCompradas);
		Document updateDoc = new Document("$set", newDoc);

		collectionUsers.updateOne(query, updateDoc);

		System.out.println("Cartas compradas => " + loginUser.getCartas_compradas());
	}

	public static void crearBaraja(MongoClient mongo, MongoDatabase database) {
		MongoCollection<Document> collectionDecks = database.getCollection("Decks");
		MongoCollection<Document> collectionCards = database.getCollection("Cards");

		boolean salir = false;
		int sumaCosteBaraja = 0;

		ArrayList<Integer> arrayCartasCompradas = loginUser.getCartas_compradas();
		ArrayList<Integer> arrayCartasBaraja = new ArrayList<Integer>();
		HashMap<Number, Integer> cartaRepetidas = new HashMap<Number, Integer>();

		Baraja baraja = new Baraja();
		Document barajaDocument = new Document();

		//Con esto contamos todas las barajas que hay en la base de datos y ponemos la siguiente id
		int baraja_id = (int) (collectionDecks.countDocuments() + 1);

		System.out.println("Introduce el nombre del mazo: ");
		String nombre_baraja = leerString();

		int cartaRepetida = 0;

		while(salir == false) {
			System.out.println("Introduce el id de la carta que vas a insertar en la baraja, escribe un numero negativo para finalizar la insercion => ");
			Number carta_id = leerInt();

			if (carta_id.intValue() < 0) {
				salir = true;
				System.out.println("Finalizacion de la compra de cartas");
			} else {
				//esta boleana servira para saber si se ha insertado en la baraja o no
				boolean existe = false;
				//recorremos todo el array de cartas compradas del usuario para saber si podemos insertarlo en la base de datos

				for (Number busquedaArrayCompradas : arrayCartasCompradas) {

					cartaRepetidas.put(busquedaArrayCompradas, cartaRepetida);

					Long l= Long.valueOf(carta_id.toString());

					if (arrayCartasCompradas.contains(l)) {
						//Con esto cogemos todos los datos de esta carta para saber su coste de invocacion y asi sumarlo al coste de la baraja
						FindIterable<Document> findIt = collectionCards.find(eq("id", carta_id));
						Document doc2 = findIt.first();

						//si su coste no supera los 60 se inserta la carta a la baraja
						if(sumaCosteBaraja < 60) {

							//ERROR AQUI
							if (cartaRepetidas.get(carta_id) < 2) {
								System.out.println("Carta " + carta_id + " insertada a la baraja " + nombre_baraja + " para el usuario " + loginUser.getNom_usuario());
								//Lo insertamos en la baraja
								arrayCartasBaraja.add(carta_id.intValue());
								//lo sumamos al coste de la baraja
								sumaCosteBaraja = sumaCosteBaraja + (Integer) doc2.get("coste_invocacion");
								//como la carta existia y se ha insertado la booleana se cambia para confirmar de que se ha insertado
								existe = true;
								//le sumamos 1 a la cantidad de la carta
								cartaRepetidas.put(carta_id, cartaRepetidas.get(carta_id) + 1);
								break;
							} else {
								System.out.println("Carta repetida, no se puede tener mas de dos cartas repetidas");
								cartaRepetida = 0;
							}
							
						} else {
							//Tambien finaliza el programa cuando supera el coste de invocacion de la baraja
							System.out.println("Baraja acabada. Coste de invocacion superado");
							salir = true;
						}

					}
				}

				if (existe == false) {
					System.out.println("La carta no existe o ya esta la baraja, no se puede insertar");
				}
			}
		}

		//finalmente insertamos la baraja en la base de datos
		baraja.setBaraja_id(baraja_id);
		baraja.setNombre_baraja(nombre_baraja);
		baraja.setValor_baraja(sumaCosteBaraja);
		baraja.setCartas_baraja(arrayCartasBaraja);

		barajaDocument = new Document("baraja_id", baraja.getBaraja_id()).append("nombre_baraja", baraja.getNombre_baraja())
				.append("valor_baraja", baraja.getValor_baraja()).append("cartas_barajas", baraja.getCartas_baraja());

		collectionDecks.insertOne(barajaDocument);
	}


	public static void barajasPredefenidas(MongoClient mongo, MongoDatabase database) {
		MongoCollection<Document> collectionDecks = database.getCollection("Decks");

		System.out.println("Baraja 1, 2 y 3 insertadas por defecto de nuevo");
		Document findDocument1 = new Document("baraja_id", "1");
		collectionDecks.findOneAndDelete(findDocument1);

		Document findDocument2 = new Document("baraja_id", "2");
		collectionDecks.findOneAndDelete(findDocument2);

		Document findDocument3 = new Document("baraja_id", "3");
		collectionDecks.findOneAndDelete(findDocument3);

		barajasJsonDefecto(database);
	}

}
