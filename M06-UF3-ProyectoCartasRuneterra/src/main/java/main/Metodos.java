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

	public static Number leerNumber() {
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

		ArrayList<Integer> arrayCartasCompradas = new ArrayList<Integer>();
		arrayCartasCompradas = loginUser.getCartas_compradas();
		ArrayList<Integer> arrayCartasDisponibles = new ArrayList<Integer>();

		System.out.println(arrayCartasCompradas.toString());
		for (Document document : todasCartas) {
			arrayCartasDisponibles.add(document.getInteger("id"));
		}

		while (salir == false) {
			System.out.print("Escribe la carta id que quieres comprar, escribe un numero negativo para finalizar la compra => ");
			int carta_id = (int) leerNumber();

			if (carta_id < 0) {
				salir = true;
				System.out.println("Finalizacion de la compra de cartas");
			} else {
				boolean existe = false; 
					if (arrayCartasDisponibles.contains(carta_id)) {
						
							if (!arrayCartasCompradas.contains(carta_id)) {
								
								System.out.println("Carta " + carta_id + " comprada para el usuario " + loginUser.getNom_usuario());
								System.out.println();
								arrayCartasCompradas.add(carta_id);
								existe = true;
								
							}
						
				}

				if (existe == false) {
					System.out.println("La carta no existe o ya esta en lista comprada, no se puede insertar");
					System.out.println();
				}

			}
		}

		loginUser.setCartas_compradas(arrayCartasCompradas);
		Document query = new Document("usuario_id", loginUser.getUsuario_id());
		Document newDoc = new Document("cartas_compradas", loginUser.getCartas_compradas());
		Document updateDoc = new Document("$set", newDoc);

		collectionUsers.updateOne(query, updateDoc);

		System.out.println("Cartas compradas => " + loginUser.getCartas_compradas());
	}

	public static void crearBaraja(MongoClient mongo, MongoDatabase database) {
		MongoCollection<Document> collectionDecks = database.getCollection("Decks");
		MongoCollection<Document> collectionCards = database.getCollection("Cards");
		MongoCollection<Document> collectionUsers = database.getCollection("Users");

		boolean salir = false;
		int sumaCosteBaraja = 0;

		ArrayList<Integer> arrayCartasCompradas = loginUser.getCartas_compradas();
		ArrayList<Integer> arrayCartasBaraja = new ArrayList<Integer>();
		HashMap<Integer, Integer> cartaRepetidas = new HashMap<Integer, Integer>();
		ArrayList<Integer> arrayBarajasUser = new ArrayList<Integer>();
		arrayBarajasUser = loginUser.getBarajas_usuario();

		Baraja baraja = new Baraja();
		Document barajaDocument = new Document();

		//Con esto contamos todas las barajas que hay en la base de datos y ponemos la siguiente id
		int baraja_id = (int) (collectionDecks.countDocuments() + 1);
		int cantidadCartas = 0;

		System.out.println("Introduce el nombre de la baraja: ");
		String nombre_baraja = leerString();

		while(salir == false) {
			while (cantidadCartas <= 20) {
				System.out.print("Introduce el id de la carta que vas a insertar en la baraja, escribe un numero negativo para finalizar la insercion => ");
				Number carta_id = leerNumber();

				if (carta_id.intValue() < 0) {
					salir = true;
					System.out.println("Finalizacion de la creacion de la baraja");
					break;
				} else {
					//esta boleana servira para saber si se ha insertado en la baraja o no
					boolean existe = false;
					//recorremos todo el array de cartas compradas del usuario para saber si podemos insertarlo en la base de datos

					for (Number busquedaArrayCompradas : arrayCartasCompradas) {

						if (busquedaArrayCompradas.intValue() == carta_id.intValue()) {

							Long l= Long.valueOf(carta_id.toString());

							if (arrayCartasCompradas.contains(l)) {
								//Con esto cogemos todos los datos de esta carta para saber su coste de invocacion y asi sumarlo al coste de la baraja
								FindIterable<Document> findIt = collectionCards.find(eq("id", carta_id));
								Document doc2 = findIt.first();

								//si su coste no supera los 60 se inserta la carta a la baraja
								if(sumaCosteBaraja < 60) {

									cartaRepetidas.put(busquedaArrayCompradas.intValue(), cartaRepetidas.get(carta_id.intValue()));

									//System.out.println("Resultado HashMap: " + cartaRepetidas.toString());
									if(cartaRepetidas.get(carta_id.intValue()) == null) {
										System.out.println("Carta " + carta_id + " insertada a la baraja " + nombre_baraja + " para el usuario " + loginUser.getNom_usuario());
										//Lo insertamos en la baraja
										arrayCartasBaraja.add(carta_id.intValue());
										//lo sumamos al coste de la baraja
										sumaCosteBaraja = sumaCosteBaraja + (Integer) doc2.get("coste_invocacion");
										//como la carta existia y se ha insertado la booleana se cambia para confirmar de que se ha insertado
										existe = true;
										//le sumamos 1 a la cantidad de la carta
										cartaRepetidas.put(carta_id.intValue(), 1);
										cantidadCartas++;
										break;

									}else if (cartaRepetidas.get(carta_id.intValue()) < 2 ) {
										System.out.println("Carta " + carta_id + " insertada a la baraja " + nombre_baraja + " para el usuario " + loginUser.getNom_usuario());
										//Lo insertamos en la baraja
										arrayCartasBaraja.add(carta_id.intValue());
										//lo sumamos al coste de la baraja
										sumaCosteBaraja = sumaCosteBaraja + (Integer) doc2.get("coste_invocacion");
										//como la carta existia y se ha insertado la booleana se cambia para confirmar de que se ha insertado
										existe = true;
										//le sumamos 1 a la cantidad de la carta
										cartaRepetidas.put(carta_id.intValue(), cartaRepetidas.get(carta_id.intValue()) + 1);
										cantidadCartas++;
										break;
									} else {
										System.out.println("Carta repetida, no se puede tener mas de dos cartas repetidas");
									}

								} else {
									//Tambien finaliza el programa cuando supera el coste de invocacion de la baraja
									System.out.println("Baraja acabada. Coste de invocacion superado");
									System.out.println();
									salir = true;
								}

							}
						}
					}

					if (existe == false) {
						System.out.println("La carta no existe o ya esta la baraja, no se puede insertar");
						System.out.println();
					} else if (cantidadCartas == 20) {
						System.out.println("Se ha llegado al numero maximo de cartas de la baraja");
						System.out.println();
						cantidadCartas++;
						salir = true;
					}
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

		arrayBarajasUser.add(baraja.getBaraja_id());

		Document buscarIdUsuario = new Document("usuario_id", loginUser.getUsuario_id());

		Document usuarioConBarajaNueva = new Document("$set", new Document("barajas_usuario", arrayBarajasUser));

		collectionUsers.findOneAndUpdate(buscarIdUsuario, usuarioConBarajaNueva);

		System.out.println("======================================================");
		System.out.println("Nueva baraja creada para " + loginUser.getNom_usuario());
		System.out.println("------------------------------------------------------");
		System.out.println("Id Baraja: "+ baraja.getBaraja_id());
		System.out.println("Nombre baraja: " + baraja.getNombre_baraja());
		System.out.println("Valor baraja: " + baraja.getValor_baraja());
		System.out.println("Cartas de la baraja: " + baraja.getCartas_baraja());
	}


	public static void editarBaraja(MongoClient mongo, MongoDatabase database) {
		MongoCollection<Document> collectionDecks = database.getCollection("Decks");
		MongoCollection<Document> collectionCards = database.getCollection("Cards");

		ArrayList<Integer> arrayCartasBaraja = new ArrayList<Integer>();
		ArrayList<Integer> arrayCartasCompradas = new ArrayList<Integer>();
		HashMap<Number, Integer> arrayCartasRepetidas = new HashMap<Number, Integer>();
		ArrayList<Integer> arrayBarajasUsuario = new ArrayList<Integer>();
		arrayBarajasUsuario = loginUser.getBarajas_usuario();

		Baraja baraja = new Baraja();
		Document barajaDoc = new Document();

		int cantidadCartasBaraja;
		int sumaCosteBaraja;

		FindIterable<Document> valorBarajaIte;
		Document valorBarajaDoc;
		int valorBaraja;

		System.out.print("Escribe la id de la baraja que quieres modificar, barajas que tiene " + loginUser.getNom_usuario() + ": " + arrayBarajasUsuario + " => ");
		int baraja_id = (int) leerNumber();

		if (arrayBarajasUsuario.contains(baraja_id)) {

			Document busquedaBaraja = new Document();
			busquedaBaraja.put("baraja_id", baraja_id);
			Iterator<Document> iter = collectionDecks.find(busquedaBaraja).iterator();

			if(iter.hasNext()) {
				barajaDoc = iter.next();

				baraja.setBaraja_id((int) barajaDoc.get("baraja_id"));
				baraja.setNombre_baraja((String) barajaDoc.get("nombre_baraja"));
				baraja.setValor_baraja((int) barajaDoc.get("valor_baraja"));
				baraja.setCartas_baraja((ArrayList<Integer>) barajaDoc.get("cartas_barajas"));

				System.out.println("=========================================================");
				System.out.println("Datos de la baraja:");
				System.out.println("Baraja id: " + baraja.getBaraja_id());
				System.out.println("Nombre baraja: " + baraja.getNombre_baraja());
				System.out.println("Valor de la baraja: " + baraja.getValor_baraja());
				System.out.println("Cartas de la baraja: " + baraja.getCartas_baraja().toString());
				System.out.println("=========================================================");

				System.out.println("Que quieres modificar de la baraja");
				System.out.println("1.-Nombre baraja");
				System.out.println("2.-Insertar carta a la baraja");
				System.out.println("3.-Eliminar carta de la baraja");
				System.out.print("Escoge la opcion que quieres: ");
				int caso = (int) leerNumber();

				switch (caso) {
				case 1:

					System.out.print("Introduce nuevo nombre del mazo: ");
					String nuevoNombreBaraja = leerString();
					baraja.setNombre_baraja(nuevoNombreBaraja);

					Document queryNombre = new Document("baraja_id", baraja.getBaraja_id());
					Document newDocNombre = new Document("nombre_baraja", baraja.getNombre_baraja());
					Document updateDocNombre = new Document("$set", newDocNombre);

					collectionDecks.updateOne(queryNombre, updateDocNombre);

					break;

				case 2:
					arrayCartasBaraja = baraja.getCartas_baraja();
					arrayCartasCompradas = loginUser.getCartas_compradas();
					ArrayList<Integer> arrayCartasBarajaCopia = new ArrayList<Integer>();

					for (Number carta_id : arrayCartasBaraja) {
						arrayCartasBarajaCopia.add(carta_id.intValue());

						for (Number carta_idBusqueda : arrayCartasBarajaCopia) {
							arrayCartasRepetidas.put(carta_idBusqueda.intValue(), arrayCartasRepetidas.get(carta_idBusqueda.intValue()));

							if(arrayCartasRepetidas.get(carta_idBusqueda.intValue()) == null) {
								//le sumamos 1 a la cantidad de la carta
								arrayCartasRepetidas.put(carta_idBusqueda.intValue(), 1);
								break;

							}else if (arrayCartasRepetidas.get(carta_idBusqueda.intValue()) < 2 ) {
								//le sumamos 1 a la cantidad de la carta
								arrayCartasRepetidas.put(carta_idBusqueda.intValue(), arrayCartasRepetidas.get(carta_idBusqueda.intValue()) + 1);
								break;

							}
						}
					}

					System.out.println("Hashmap: "+arrayCartasRepetidas);
					cantidadCartasBaraja = arrayCartasBaraja.size();
					sumaCosteBaraja = 0;

					valorBarajaIte = collectionDecks.find(eq("baraja_id", baraja_id));
					valorBarajaDoc = valorBarajaIte.first();
					valorBaraja = (Integer) valorBarajaDoc.get("valor_baraja");

					if(cantidadCartasBaraja < 20) {
						System.out.print("Introduce el id de la carta que quieres insertar en la baraja: ");
						Number idCarta = leerNumber();

						for (Number busquedaArrayCompradas : arrayCartasCompradas) {

							if (busquedaArrayCompradas.intValue() == idCarta.intValue()) {

								Long l= Long.valueOf(idCarta.toString());

								if (arrayCartasCompradas.contains(l)) {

									FindIterable<Document> valorCarta = collectionCards.find(eq("id", idCarta));
									Document valorCartaDoc = valorCarta.first();

									arrayCartasRepetidas.put(idCarta.intValue(), arrayCartasRepetidas.get(idCarta.intValue()));

									if(arrayCartasRepetidas.get(idCarta.intValue()) == null) {
										//lo sumamos al coste de la baraja
										sumaCosteBaraja = valorBaraja + (Integer) valorCartaDoc.get("coste_invocacion");

										if(sumaCosteBaraja <= 60) {

											arrayCartasBaraja.add(idCarta.intValue());

											baraja.setValor_baraja(sumaCosteBaraja);
											baraja.setCartas_baraja(arrayCartasBaraja);

											Document queryBarajaId = new Document("baraja_id", baraja.getBaraja_id());
											Document newDocValorBaraja = new Document("valor_baraja", baraja.getValor_baraja());
											Document updateDocValorBaraja = new Document("$set", newDocValorBaraja);

											collectionDecks.updateOne(queryBarajaId, updateDocValorBaraja);

											Document newDocCartasBaraja = new Document("cartas_barajas", baraja.getCartas_baraja());
											Document updateDocCartasBaraja = new Document("$set", newDocCartasBaraja);

											collectionDecks.updateOne(queryBarajaId, updateDocCartasBaraja);
											break;

										} else {
											System.out.println("No se ha podido insertar la carta porque ha superado el valor maximo de la baraja dando " + sumaCosteBaraja);
											System.out.println();
										}
									} else if(arrayCartasRepetidas.get(idCarta.intValue()) < 2) {
										//lo sumamos al coste de la baraja
										sumaCosteBaraja = valorBaraja + (Integer) valorCartaDoc.get("coste_invocacion");

										if(sumaCosteBaraja <= 60) {

											arrayCartasBaraja.add(idCarta.intValue());

											baraja.setValor_baraja(sumaCosteBaraja);
											baraja.setCartas_baraja(arrayCartasBaraja);

											Document queryBarajaId = new Document("baraja_id", baraja.getBaraja_id());
											Document newDocValorBaraja = new Document("valor_baraja", baraja.getValor_baraja());
											Document updateDocValorBaraja = new Document("$set", newDocValorBaraja);

											collectionDecks.updateOne(queryBarajaId, updateDocValorBaraja);

											Document newDocCartasBaraja = new Document("cartas_barajas", baraja.getCartas_baraja());
											Document updateDocCartasBaraja = new Document("$set", newDocCartasBaraja);

											collectionDecks.updateOne(queryBarajaId, updateDocCartasBaraja);

										} else {
											System.out.println("No se ha podido insertar la carta porque ha superado el valor maximo de la baraja dando " + sumaCosteBaraja);
										}
									}

								}
							}
						}

					} else {
						System.out.println("No se puede insertar cartas porque ya supera el maximo de cartas de cartas por baraja, que son 20");
						System.out.println();
					}


					break;

				case 3:
					arrayCartasBaraja = baraja.getCartas_baraja();
					cantidadCartasBaraja = arrayCartasBaraja.size();
					sumaCosteBaraja = 0;

					valorBarajaIte = collectionDecks.find(eq("baraja_id", baraja_id));
					valorBarajaDoc = valorBarajaIte.first();
					valorBaraja = (Integer) valorBarajaDoc.get("valor_baraja");

					System.out.println("===================================================");
					System.out.print("Escribe la id de la carta que quieres borrar de la baraja: ");
					Number cartaIdEliminar = leerNumber();

					if (arrayCartasBaraja.contains(cartaIdEliminar)) {

						if(cantidadCartasBaraja > 1) {

							int position = 0;

							for (Number cartaRepe : arrayCartasBaraja) {

								if(cartaRepe.intValue() == cartaIdEliminar.intValue()) {
									FindIterable<Document> valorCarta = collectionCards.find(eq("id", cartaIdEliminar));
									Document valorCartaDoc = valorCarta.first();
									int valorCartaEliminar =(Integer) valorCartaDoc.get("coste_invocacion");

									arrayCartasBaraja.remove(position);

									sumaCosteBaraja = valorBaraja - valorCartaEliminar;

									baraja.setValor_baraja(sumaCosteBaraja);
									baraja.setCartas_baraja(arrayCartasBaraja);

									Document queryBarajaId = new Document("baraja_id", baraja.getBaraja_id());
									Document newDocValorBaraja = new Document("valor_baraja", baraja.getValor_baraja());
									Document updateDocValorBaraja = new Document("$set", newDocValorBaraja);

									collectionDecks.updateOne(queryBarajaId, updateDocValorBaraja);

									Document newDocCartasBaraja = new Document("cartas_barajas", baraja.getCartas_baraja());
									Document updateDocCartasBaraja = new Document("$set", newDocCartasBaraja);

									collectionDecks.updateOne(queryBarajaId, updateDocCartasBaraja);

									break;
								}else {
									position++;
								}
							}

						} else {
							System.out.println("No podemos eliminar mas cartas porque si no, no habran mas cartas");
						}
					} else {
						System.out.println("Esta carta bi existe en la baraja");
					}

					break;

				default:
					System.out.println("No se modificara nada porque no se escogio ninguna opcion correcta");
					break;
				}

			}

		} else {
			System.out.println(loginUser.getNom_usuario() + " no tiene esta baraja");
		}


		System.out.println("=========================================================");
		System.out.println("Cambios de la baraja:");
		System.out.println("Baraja id: " + baraja.getBaraja_id());
		System.out.println("Nombre baraja: " + baraja.getNombre_baraja());
		System.out.println("*Valor de la baraja: " + baraja.getValor_baraja());
		System.out.println("*Cartas de la baraja: " + baraja.getCartas_baraja().toString());
		System.out.println("=========================================================");
		System.out.println("Edicion finalizada");
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
