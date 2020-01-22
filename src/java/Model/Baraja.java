/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author admin
 */
public class Baraja {
    
    private String nombre;
    private int tier;
    
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
    
}
