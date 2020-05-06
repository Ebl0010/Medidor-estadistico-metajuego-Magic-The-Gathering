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

    public String getClave_repetir() {
        return clave_repetir;
    }

    public void setClave_repetir(String clave_repetir) {
        this.clave_repetir = clave_repetir;
    }

    public ArrayList<String> getRoles() {
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

    public void carga_pagina_registro() {
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    public void crearUsuario() {
        String error = "ok";
        if (nombre.length() > 20 || nombre.length() <= 6) {
            error = "El nombre debe tener entre 6 y 20 caracteres.";
        } else {
            if (!clave.equals(clave_repetir)) {
                error = "Las contraseñas no son iguales.";
            } else {
                if (clave.length() > 16 || clave.length() <= 6) {
                    error = "La contraseña debe tener entre 6 y 16 caracteres";
                }
            }
        }

        if (error.equals("ok")) {
            Usuario usuarioNuevo = new Usuario(nombre, clave, correo);
            if (gestorBD.crearUsuario(usuarioNuevo)) {
                

            }
        }

    }*/

}
