/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class Usuario {

    
    
    private String nombre, clave;
    private int rondas_ganadas,
                rondas_perdidas,
                rondas_empatadas,
                partidas_ganadas,
                partidas_perdidas;
    
    private float porcentaje_rondas,
                  porcentaje_partidas;
            
    
    private ArrayList<Baraja_de_usuario> lista_de_barajas_de_usuario;
    
   
    
    public Usuario(){
        lista_de_barajas_de_usuario = new ArrayList<Baraja_de_usuario>();
    }
    
    public Usuario(String n){
        nombre = n;
        lista_de_barajas_de_usuario = new ArrayList<Baraja_de_usuario>();
    }
    
    public Usuario(String n, String c){
        nombre = n;
        clave = c;
        lista_de_barajas_de_usuario = new ArrayList<Baraja_de_usuario>();
    }
    
    
    public String getNombre() {
        return nombre;
    }

    public String getClave() {
        return clave;
    }

    public int getRondas_ganadas() {
        return rondas_ganadas;
    }

    public int getRondas_perdidas() {
        return rondas_perdidas;
    }

    public int getRondas_empatadas() {
        return rondas_empatadas;
    }

    public int getPartidas_ganadas() {
        return partidas_ganadas;
    }

    public int getPartidas_perdidas() {
        return partidas_perdidas;
    }

    public float getPorcentaje_rondas() {
        return porcentaje_rondas;
    }

    public float getPorcentaje_partidas() {
        return porcentaje_partidas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setRondas_ganadas(int rondas_ganadas) {
        this.rondas_ganadas = rondas_ganadas;
    }

    public void setRondas_perdidas(int rondas_perdidas) {
        this.rondas_perdidas = rondas_perdidas;
    }

    public void setRondas_empatadas(int rondas_empatadas) {
        this.rondas_empatadas = rondas_empatadas;
    }

    public void setPartidas_ganadas(int partidas_ganadas) {
        this.partidas_ganadas = partidas_ganadas;
    }

    public void setPartidas_perdidas(int partidas_perdidas) {
        this.partidas_perdidas = partidas_perdidas;
    }

    public void setPorcentaje_rondas(float porcentaje_rondas) {
        this.porcentaje_rondas = porcentaje_rondas;
    }

    public void setPorcentaje_partidas(float porcentaje_partidas) {
        this.porcentaje_partidas = porcentaje_partidas;
    }
    
    
    public ArrayList<Baraja_de_usuario> getLista_de_barajas_de_usuario(){
        return lista_de_barajas_de_usuario;
    }
    
    public void setLista_de_barajas_de_usuario(ArrayList<Baraja_de_usuario> barajas){
        lista_de_barajas_de_usuario = barajas;
    }
    
        
    
}
