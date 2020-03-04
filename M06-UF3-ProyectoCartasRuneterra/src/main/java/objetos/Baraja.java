package objetos;

import java.util.ArrayList;
import java.util.Arrays;

public class Baraja {

	private int baraja_id;
	private int valor_baraja;
	private ArrayList[] cartas_barajas;
	
	public Baraja() {
		
	}
	
	public Baraja(int baraja_id, int valor_baraja, ArrayList[] cartas_barajas) {
		this.baraja_id = baraja_id;
		this.valor_baraja = valor_baraja;
		this.cartas_barajas = cartas_barajas;
	}

	public int getBaraja_id() {
		return baraja_id;
	}

	public void setBaraja_id(int baraja_id) {
		this.baraja_id = baraja_id;
	}

	public int getValor_baraja() {
		return valor_baraja;
	}

	public void setValor_baraja(int valor_baraja) {
		this.valor_baraja = valor_baraja;
	}

	public ArrayList[] getCartas_barajas() {
		return cartas_barajas;
	}

	public void setCartas_barajas(ArrayList[] cartas_barajas) {
		this.cartas_barajas = cartas_barajas;
	}

	@Override
	public String toString() {
		return "Baraja [baraja_id=" + baraja_id + ", valor_baraja=" + valor_baraja + ", cartas_barajas="
				+ Arrays.toString(cartas_barajas) + "]";
	}
	
	
	
}
