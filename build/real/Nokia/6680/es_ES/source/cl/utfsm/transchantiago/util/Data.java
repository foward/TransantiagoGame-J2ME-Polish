/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.utfsm.transchantiago.util;

/**
 *
 * @author eraddatz
 */
public class Data{

	// Se definen las variables de la aplicacion

	// Indica el nivel de volumen configurado
	public int volumen;
	// Indica si la vibracion se encuentra activada o no
	public int vibracion;
	// Indica el nivel de dificultad del juego
	public int dificultad;
	// Nombre y puntaje del primer ranking
	public String nombre1;
	public int puntaje1;
	// Nombre y puntaje del segundo ranking
	public String nombre2;
	public int puntaje2;
	// Nombre y puntaje del tercer ranking
	public String nombre3;
	public int puntaje3;
	// Nombre y puntaje del cuarto ranking
	public String nombre4;
	public int puntaje4;
	// Nombre y puntaje del quinto ranking
	public String nombre5;
	public int puntaje5;


	// Metodos para asignacion de valores
	public void setVolumen(int s){
		this.volumen = s;
	}
	public void setVibracion(int b){
		this.vibracion = b;
	}
	public void setDificultad(int d){
		this.dificultad = d;
	}
	public void setNombre1(String n){
		this.nombre1 = n;
	}
	public void setPuntaje1(int p){
		this.puntaje1 = p;
	}
	public void setNombre2(String n){
		this.nombre2 = n;
	}
	public void setPuntaje2(int p){
		this.puntaje2 = p;
	}
	public void setNombre3(String n){
		this.nombre3 = n;
	}
	public void setPuntaje3(int p){
		this.puntaje3 = p;
	}
	public void setNombre4(String n){
		this.nombre4 = n;
	}
	public void setPuntaje4(int p){
		this.puntaje4 = p;
	}
	public void setNombre5(String n){
		this.nombre5 = n;
	}
	public void setPuntaje5(int p){
		this.puntaje5 = p;
	}


	// Metodos para la obtencion de valore
	public int getVolumen(){
		return this.volumen;
	}
	public int getVibracion(){
		return this.vibracion;
	}
	public int getDificultad(){
		return this.dificultad;
	}
	public String getNombre1(){
		return this.nombre1;
	}
	public int getPuntaje1(){
		return this.puntaje1;
	}
	public String getNombre2(){
		return this.nombre2;
	}
	public int getPuntaje2(){
		return this.puntaje2;
	}
	public String getNombre3(){
		return this.nombre3;
	}
	public int getPuntaje3(){
		return this.puntaje3;
	}
	public String getNombre4(){
		return this.nombre4;
	}
	public int getPuntaje4(){
		return this.puntaje4;
	}
	public String getNombre5(){
		return this.nombre5;
	}
	public int getPuntaje5(){
		return this.puntaje5;
	}

}
