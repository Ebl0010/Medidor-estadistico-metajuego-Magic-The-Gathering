/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author admin
 */
public class Torneo {
    
    private String nombre_usuario;
    private String nombre_baraja;
    private int puntos;
    private String resultado;
    private int repeticiones;
    
    public Torneo(String usuario, String baraja, int puntos, String resultado, int repeticiones){
        nombre_usuario = usuario;
        nombre_baraja = baraja;
        this.puntos = puntos;
        this.resultado = resultado;
        this.repeticiones = repeticiones;
    }

    public Torneo(){
        
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public String getNombre_baraja() {
        return nombre_baraja;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getResultado() {
        return resultado;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public void setNombre_baraja(String nombre_baraja) {
        this.nombre_baraja = nombre_baraja;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }
    
    
    
}
