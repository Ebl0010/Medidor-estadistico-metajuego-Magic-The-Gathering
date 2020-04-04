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
    private int pj, pg, pe, pp, rj, rg, re, rp;
    private float por_partidas, por_rondas;
    
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
    
    public float getPor_rondas(){
        return por_rondas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getPj() {
        return pj;
    }

    public int getPg() {
        return pg;
    }

    public int getPe() {
        return pe;
    }

    public int getPp() {
        return pp;
    }

    public int getRj() {
        return rj;
    }

    public int getRg() {
        return rg;
    }

    public int getRe() {
        return re;
    }

    public int getRp() {
        return rp;
    }

    public float getPor_partidas() {
        return por_partidas;
    }

    public void setPj(int pj) {
        this.pj = pj;
    }

    public void setPg(int pg) {
        this.pg = pg;
    }

    public void setPe(int pe) {
        this.pe = pe;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public void setRj(int rj) {
        this.rj = rj;
    }

    public void setRg(int rg) {
        this.rg = rg;
    }

    public void setRe(int re) {
        this.re = re;
    }

    public void setRp(int rp) {
        this.rp = rp;
    }

    public void setPor_partidas(float por_partidas) {
        this.por_partidas = por_partidas;
    }

    public void setPor_rondas(float por_rondas) {
        this.por_rondas = por_rondas;
    }
    
    public ArrayList<Baraja_de_usuario> getLista_de_barajas_de_usuario(){
        return lista_de_barajas_de_usuario;
    }
    
    public void setLista_de_barajas_de_usuario(ArrayList<Baraja_de_usuario> barajas){
        lista_de_barajas_de_usuario = barajas;
    }
    
        
    
}
