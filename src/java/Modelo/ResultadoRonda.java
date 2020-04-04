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
public class ResultadoRonda {
    
    private int ganadas_main, perdidas_main, ganadas_side, perdidas_side;
    private String oponente;
    
    public ResultadoRonda(){
        
    }

    public int getGanadas_main() {
        return ganadas_main;
    }

    public int getPerdidas_main() {
        return perdidas_main;
    }

    public int getGanadas_side() {
        return ganadas_side;
    }

    public int getPerdidas_side() {
        return perdidas_side;
    }

    public String getOponente() {
        return oponente;
    }

    public void setGanadas_main(int ganadas_main) {
        this.ganadas_main = ganadas_main;
    }

    public void setPerdidas_main(int perdidas_main) {
        this.perdidas_main = perdidas_main;
    }

    public void setGanadas_side(int ganadas_side) {
        this.ganadas_side = ganadas_side;
    }

    public void setPerdidas_side(int perdidas_side) {
        this.perdidas_side = perdidas_side;
    }

    public void setOponente(String oponente) {
        this.oponente = oponente;
    }
    
    
    
}
