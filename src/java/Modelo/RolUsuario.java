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
public class RolUsuario {
    
    private String descripcion_rol;
    private int idRol;
    private String nombre_usuario;
    private int estado;
    private String s_estado;
    
    public RolUsuario(){
    }
    
    public RolUsuario(String descripcion_rol, int idRol, String nombre_usuario){
        this.descripcion_rol = descripcion_rol;
        this.idRol = idRol;
        this.nombre_usuario = nombre_usuario;
    }

    public String getDescripcion_rol() {
        return descripcion_rol;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setDescripcion_rol(String descripcion_rol) {
        this.descripcion_rol = descripcion_rol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public int getEstado() {
        return estado;
    }

    public String getS_estado() {
        return s_estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setS_estado(String s_estado) {
        this.s_estado = s_estado;
    }
    
    public void traducirEstado(){
        switch (estado){
            case(0): s_estado = "Concedido"; break;
            case(1): s_estado = "Solicitado"; break;
            case(2): s_estado = "Denegado"; break;           
        }
    }
    
                
}
