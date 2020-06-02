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
public class ResultadoUsuarioBaraja {
    
    private String nombre_usuario, nombre_baraja;
    private int ganadasMain, perdidasMain, ganadasSide, perdidasSide, rondasGanadas, rondasPerdidas, rondasEmpatadas;
    private float porcentajeMain, porcentajeSide, porcentajeTotal, porcentajeRondasGanadas;
    
    public ResultadoUsuarioBaraja(){
        
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public String getNombre_baraja() {
        return nombre_baraja;
    }

    public int getGanadasMain() {
        return ganadasMain;
    }

    public int getPerdidasMain() {
        return perdidasMain;
    }

    public int getGanadasSide() {
        return ganadasSide;
    }

    public int getPerdidasSide() {
        return perdidasSide;
    }

    public float getPorcentajeMain() {
        return porcentajeMain;
    }

    public float getPorcentajeSide() {
        return porcentajeSide;
    }

    public float getPorcentajeTotal() {
        return porcentajeTotal;
    }
    
    public float getPorcentajeRondasGanadas(){
        return porcentajeRondasGanadas;
    }
    
    public int getRondasGanadas(){
        return rondasGanadas;
    }
    
    public int getRondasPerdidas(){
        return rondasPerdidas;
    }
    
    public int getRondasEmpatadas(){
        return rondasEmpatadas;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public void setNombre_baraja(String nombre_baraja) {
        this.nombre_baraja = nombre_baraja;
    }

    public void setGanadasMain(int ganadasMain) {
        this.ganadasMain = ganadasMain;
    }

    public void setPerdidasMain(int perdidasMain) {
        this.perdidasMain = perdidasMain;
    }

    public void setGanadasSide(int ganadasSide) {
        this.ganadasSide = ganadasSide;
    }

    public void setPerdidasSide(int perdidasSide) {
        this.perdidasSide = perdidasSide;
    }

    public void setPorcentajeMain(float porcentajeMain) {
        this.porcentajeMain = porcentajeMain;
    }

    public void setPorcentajeSide(float porcentajeSide) {
        this.porcentajeSide = porcentajeSide;
    }

    public void setPorcentajeTotal(float porcentajeTotal) {
        this.porcentajeTotal = porcentajeTotal;
    }
    
    public void setPorcentajeRondasGanadas(float porcentajeRondas){
        porcentajeRondasGanadas = porcentajeRondas;
    }
    
    public void setRondasGanadas(int rg){
        rondasGanadas = rg;
    }
    
    public void setRondasEmpatadas(int re){
        rondasEmpatadas = re;
    }
    
    public void setRondasPerdidas(int rp){
        rondasPerdidas = rp;
    }
     
    public void introducir_datos(int main1N, int main2N, int side1N, int side2N, int rondasG, int rondasP, int rondasE){
        ganadasMain = ganadasMain + main1N;
        perdidasMain = perdidasMain + main2N;
        ganadasSide = ganadasSide + side1N;
        perdidasSide = perdidasSide + side2N;
        rondasGanadas = rondasGanadas + rondasG;
        rondasEmpatadas = rondasEmpatadas + rondasE;
        rondasPerdidas = rondasPerdidas + rondasP;
    }
    
    public void calcular_porcentajes() {
        if (ganadasMain == 0) {
            porcentajeMain = 0;
        } else {
            if (perdidasMain == 0) {
                porcentajeMain = 99;
            } else {
                porcentajeMain = ganadasMain * 100 / (ganadasMain + perdidasMain);
            }
        }

        if (ganadasSide == 0) {
            porcentajeSide = 0;
        } else {
            if (perdidasSide == 0) {
                porcentajeSide = 99;
            } else {
                porcentajeSide = ganadasSide * 100 / (ganadasSide + perdidasSide);
            }
        }

        if (ganadasMain + ganadasSide == 0) {
            porcentajeTotal = 0;
        } else {
            if (perdidasMain + perdidasSide == 0) {
                porcentajeTotal = 99;
            } else {
                porcentajeTotal = (ganadasMain + ganadasSide) * 100 / (ganadasMain + perdidasMain + ganadasSide + perdidasSide);
            }
        }
        
       if (rondasGanadas == 0){
           porcentajeRondasGanadas = 0;
       } else {
           if (rondasGanadas == rondasGanadas + rondasEmpatadas + rondasPerdidas){
               porcentajeRondasGanadas = 99;
           } else {
               porcentajeRondasGanadas = rondasGanadas * 100 / (rondasGanadas + rondasEmpatadas + rondasPerdidas);
           }
       }
    }
    
}
