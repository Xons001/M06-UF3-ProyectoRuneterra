package objetos;

import java.util.ArrayList;
import java.util.Arrays;

public class Baraja {

	private int baraja_id;
	private String nombre_baraja;
	private int valor_baraja;
	private ArrayList<Integer> cartas_baraja;
	
	public Baraja() {
		
	}

	public Baraja(int baraja_id, String nombre_baraja, int valor_baraja, ArrayList<Integer> cartas_baraja) {
		this.baraja_id = baraja_id;
		this.nombre_baraja = nombre_baraja;
		this.valor_baraja = valor_baraja;
		this.cartas_baraja = cartas_baraja;
	}

	public int getBaraja_id() {
		return baraja_id;
	}

	public String getNombre_baraja() {
		return nombre_baraja;
	}

	public int getValor_baraja() {
		return valor_baraja;
	}

	public ArrayList<Integer> getCartas_baraja() {
		return cartas_baraja;
	}

	public void setBaraja_id(int baraja_id) {
		this.baraja_id = baraja_id;
	}

	public void setNombre_baraja(String nombre_baraja) {
		this.nombre_baraja = nombre_baraja;
	}

	public void setValor_baraja(int valor_baraja) {
		this.valor_baraja = valor_baraja;
	}

	public void setCartas_baraja(ArrayList<Integer> cartas_baraja) {
		this.cartas_baraja = cartas_baraja;
	}

	@Override
	public String toString() {
		return "Baraja [baraja_id=" + baraja_id + ", nombre_baraja=" + nombre_baraja + ", valor_baraja=" + valor_baraja
				+ ", cartas_baraja=" + cartas_baraja + "]";
	}
	
}
