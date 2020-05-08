package Controlador;

import Modelo.Baraja;
import Modelo.GestorBD;
import Modelo.RolUsuario;
import Util.Herramientas;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "gestionarUsuariosManagedBean")
@SessionScoped
public class GestionarUsuariosManagedBean {

    private GestorBD gestorBD;
    private ArrayList<RolUsuario> roles, peticiones;
    private String nombre_usuario_actual;
    private ArrayList<String> todos_los_estados;
    
    public GestionarUsuariosManagedBean(){
        gestorBD = new GestorBD();
        
    }
    
    public void setRoles(ArrayList<RolUsuario> roles){
        this.roles = roles;
    }
    
    public ArrayList<RolUsuario> getRoles(){
        return roles;
    }
    
    public ArrayList<RolUsuario> getPeticiones(){
        return peticiones;
    }
    
    public void leeRoles_y_peticiones() throws SQLException{
        roles = gestorBD.lee_roles_usuarios(nombre_usuario_actual);
        peticiones = gestorBD.lee_peticiones_rol();
    }
    
    
    
    public void carga_pagina_gestionar_usuarios(String nombre_usuario_actual) throws SQLException{
        leeRoles_y_peticiones();
        
        for (int i=0; i<roles.size(); i++){
            RolUsuario r = roles.get(i);
            r.traducirEstado();
        }
        try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("gestionar_usuarios.xhtml");
                } catch (IOException e) {
                    //e.printStackTrace();
                }
    }
}