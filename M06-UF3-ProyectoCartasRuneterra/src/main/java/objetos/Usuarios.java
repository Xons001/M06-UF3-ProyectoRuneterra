package objetos;

import java.util.ArrayList;

public class Usuarios {

	private int usuario_id;
	private String cont_usuario;
	private String nom_usuario;
	private ArrayList<Integer> barajas_usuario;
	private ArrayList<Integer> cartas_compradas;
	
	public Usuarios() {
		
	}
	
	public Usuarios(int usuario_id, String cont_usuario, String nom_usuario, ArrayList<Integer> barajas_usuario,
			ArrayList<Integer> cartas_compradas) {
		this.usuario_id = usuario_id;
		this.cont_usuario = cont_usuario;
		this.nom_usuario = nom_usuario;
		this.barajas_usuario = barajas_usuario;
		this.cartas_compradas = cartas_compradas;
	}

	public int getUsuario_id() {
		return usuario_id;
	}

	public String getCont_usuario() {
		return cont_usuario;
	}

	public String getNom_usuario() {
		return nom_usuario;
	}

	public ArrayList<Integer> getBarajas_usuario() {
		return barajas_usuario;
	}

	public ArrayList<Integer> getCartas_compradas() {
		return cartas_compradas;
	}

	public void setUsuario_id(int usuario_id) {
		this.usuario_id = usuario_id;
	}

	public void setCont_usuario(String cont_usuario) {
		this.cont_usuario = cont_usuario;
	}

	public void setNom_usuario(String nom_usuario) {
		this.nom_usuario = nom_usuario;
	}

	public void setBarajas_usuario(ArrayList<Integer> barajas_usuario) {
		this.barajas_usuario = barajas_usuario;
	}

	public void setCartas_compradas(ArrayList<Integer> cartas_compradas) {
		this.cartas_compradas = cartas_compradas;
	}

	@Override
	public String toString() {
		return "Usuarios [usuario_id=" + usuario_id + ", cont_usuario=" + cont_usuario + ", nom_usuario=" + nom_usuario
				+ ", barajas_usuario=" + barajas_usuario + ", cartas_compradas=" + cartas_compradas + "]";
	}
	
}
