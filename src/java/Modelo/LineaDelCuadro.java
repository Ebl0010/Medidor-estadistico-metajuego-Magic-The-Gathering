/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.ArrayList;


/**
 *
 * @author admin
 */
public class LineaDelCuadro {
    
    private ArrayList<ValorDelCuadro> linea;
    

    public LineaDelCuadro(){
    }
    
    public ArrayList<ValorDelCuadro> getLinea(){
        return linea;
    }
    
    public void addValor(ValorDelCuadro valor){
        linea.add(valor);
    }
    
    @Override
    public String toString(){
        String retorno = "";
        ValorDelCuadro v;
        for (int i=0; i<linea.size(); i++){
            v = linea.get(i);
            retorno = retorno + " " + v.getValor();
        }
        return retorno;
    }
    
}
