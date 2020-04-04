/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class Cuadro {
    
    private ArrayList<String> linea;
    private ArrayList<ArrayList<String>> porcentajes;
    
    public Cuadro(ArrayList<ArrayList<String>> porcentajes){
        this.porcentajes = porcentajes;
    }
    
    public ArrayList<ArrayList<String>> getPorcentajes(){
        return porcentajes;
    }
    
    
    
    
    
}
