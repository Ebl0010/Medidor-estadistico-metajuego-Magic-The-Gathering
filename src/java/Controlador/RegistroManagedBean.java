package Controlador;

import Modelo.GestorBD;
import Modelo.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "registroManagedBean")
@RequestScoped
public class RegistroManagedBean {

    private String nombre, clave, clave_repetir, correo, rol_solicitado;
    private ArrayList<String> roles;
    private GestorBD gestorBD;
    
        

    public RegistroManagedBean() {
        gestorBD = new GestorBD();
    }

    public String getNombre() {
        return nombre;
    }

    public String getClave() {
        return clave;
    }

    public String getCorreo() {
        return correo;
    }
    
    public String getClave_repetir(){
        return clave_repetir;
    }
    
    public void setClave_repetir(String clave_repetir){
        this.clave_repetir = clave_repetir;
    }

    public ArrayList<String> getRolesa() {
        return roles;
    }

    public String getRol_solicitado() {
        return rol_solicitado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public void setRol_solicitado(String rol_solicitado) {
        this.rol_solicitado = rol_solicitado;
    }

    
   
    public void carga_pagina_registro(){
        roles = gestorBD.leeRoles();
        try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("agregar_usuario.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    
    /*
    public void guardarUsuario() {
        Usuario usuarioNuevo = new Usuario(nombre, clave);
        if (gestorBD.guardarUsuario(usuarioNuevo) ){
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("index.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        
        }
    }
*/
    
    

}
