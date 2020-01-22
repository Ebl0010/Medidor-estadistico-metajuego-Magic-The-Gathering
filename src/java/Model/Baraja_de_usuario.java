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
public class Baraja_de_usuario {
    
    private String nombre_baraja;
    private int gm;
    private int em;
    private int pm;
    private int gs;
    private int es;
    private int ps;
    
    private int ganadas_total;
    private int jugadas_total;
    
    private int porcentaje_main;
    private int porcentaje_side;
    private int porcentaje_total;
    
    public Baraja_de_usuario(){
        
    }
        
    public void setNombre_baraja(String nombre_baraja){
        this.nombre_baraja = nombre_baraja;
    }
    
    public String getNombre_baraja(){
        return nombre_baraja;
    }

    public int getGm() {
        return gm;
    }

    public int getEm() {
        return em;
    }

    public int getPm() {
        return pm;
    }

    public int getGs() {
        return gs;
    }

    public int getEs() {
        return es;
    }

    public int getPs() {
        return ps;
    }

    public void setGm(int gm) {
        this.gm = gm;
    }

    public void setEm(int em) {
        this.em = em;
    }

    public void setPm(int pm) {
        this.pm = pm;
    }

    public void setGs(int gs) {
        this.gs = gs;
    }

    public void setEs(int es) {
        this.es = es;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public int getPorcentaje_main() {
        return porcentaje_main;
    }

    public int getPorcentaje_side() {
        return porcentaje_side;
    }

    public int getPorcentaje_total() {
        return porcentaje_total;
    }
    
    public int getGanadas_total(){
        return ganadas_total;
    }
    
    public int getJugadas_total(){
        return jugadas_total;
    }
    
    
    public void calculaTotales(){
        
        ganadas_total = gm+gs;
        jugadas_total = gm+gs+em+es+pm+ps;
        int jugadas_main = gm+pm+em;
        porcentaje_main = (gm / jugadas_main) * 100;
        int jugadas_side = gs+ps+es;
        porcentaje_side = (gs / jugadas_side) * 100;
        porcentaje_total = (ganadas_total / jugadas_total) * 100;
        
    }
    
}
