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
public class ResultadoUsuarioGlobal {
    
    private String nombre_usuario;
    private int partidas_ganadas, partidas_perdidas, rondas_ganadas, rondas_perdidas, rondas_empatadas;
    private float porcentaje_rondas, porcentaje_partidas;
    
    public ResultadoUsuarioGlobal(){
        
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public int getPartidas_ganadas() {
        return partidas_ganadas;
    }

    public int getPartidas_perdidas() {
        return partidas_perdidas;
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

    public float getPorcentaje_rondas() {
        return porcentaje_rondas;
    }

    public float getPorcentaje_partidas() {
        return porcentaje_partidas;
    }

    public void setPartidas_ganadas(int partidas_ganadas) {
        this.partidas_ganadas = partidas_ganadas;
    }

    public void setPartidas_perdidas(int partidas_perdidas) {
        this.partidas_perdidas = partidas_perdidas;
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
    
    public void introducir_resultados(int par_ganadas, int par_perdidas, int ron_ganadas, int ron_perdidas, int ron_empatadas){
        partidas_ganadas = partidas_ganadas + par_ganadas;
        partidas_perdidas = partidas_perdidas + par_perdidas;
        rondas_ganadas = rondas_ganadas + ron_ganadas;
        rondas_empatadas = rondas_empatadas + ron_empatadas;
        rondas_perdidas = rondas_perdidas + ron_perdidas;
    }
    
    public void calcular_porcentajes(){
        if (partidas_ganadas == 0){
            porcentaje_partidas = 0;
        } else {
            if (partidas_perdidas == 0){
                porcentaje_partidas = 99;
            } else {
                porcentaje_partidas = partidas_ganadas * 100 / (partidas_ganadas + partidas_perdidas);
            }
        }
        
        if (rondas_ganadas == 0){
            porcentaje_rondas = 0;
        } else {
            if (rondas_perdidas == 0 && rondas_empatadas == 0){
                porcentaje_rondas = 99; 
            } else {
                porcentaje_rondas = rondas_ganadas + 100 / (rondas_ganadas + rondas_empatadas + rondas_perdidas);
            }
        }
    }
    
    
    
}
