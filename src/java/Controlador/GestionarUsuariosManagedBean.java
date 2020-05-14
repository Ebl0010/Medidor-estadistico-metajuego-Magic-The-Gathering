package Controlador;

import Modelo.GestorBD;
import Modelo.RolUsuario;
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
    
        
   
    public void carga_pagina_gestionar_usuarios(String nombre_usuario_actual) throws SQLException{
        roles = gestorBD.lee_peticiones_roles(nombre_usuario_actual);
        
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
    
    public void guardarCambios(String nombre_usuario_actual) throws SQLException{
        if (gestorBD.actualizar_roles(roles)){
            roles.clear();
            carga_pagina_gestionar_usuarios(nombre_usuario_actual);
        }
    }
}