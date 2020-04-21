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
public class Baraja {
    
    private String nombre;
    private int tier;
    float porcentaje_main, porcentaje_side, porcentaje_total;
    
    public Baraja(){}
    
    public Baraja(String nombre, int tier){
        this.nombre = nombre;
        this.tier = tier;
    }
    
    public Baraja (String nombre){
        this.nombre = nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public void setTier(int tier){
        this.tier = tier;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public int getTier(){
        return tier;
    }
    
    public float getPorcentaje_main(){
        return porcentaje_main;
    }
    
    public void setPorcentaje_main(float porcentaje_main){
        this.porcentaje_main = porcentaje_main;
    }
    
    public float getPorcentaje_side(){
        return porcentaje_side;
    }
    
    public void setPorcentaje_side(float porcentaje_side){
        this.porcentaje_side = porcentaje_side;
    }
    
    public float getPorcentaje_total(){
        return porcentaje_total;
    }
    
    public void setPorcentaje_total(float porcentaje_total){
        this.porcentaje_total = porcentaje_total;
    }
    
}
