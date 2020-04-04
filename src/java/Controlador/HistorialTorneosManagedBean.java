package Controlador;

import Modelo.GestorBD;
import Modelo.Torneo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author admin
 */
@ManagedBean(name = "historialTorneosManagedBean")
@SessionScoped
public class HistorialTorneosManagedBean {

    //private String nombre_usuario;
    //private String nombre_baraja;
    private ArrayList<Torneo> torneos;
    private GestorBD gestorBD;

    public HistorialTorneosManagedBean() {
        gestorBD = new GestorBD();
    }

    /*
    public String getNombre_usuario() {
        return nombre_usuario;
    }
    

    public String getNombre_baraja() {
        return nombre_baraja;
    }
     */
    public ArrayList<Torneo> getTorneos() {
        return torneos;
    }

    /*
    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public void setNombre_baraja(String nombre_baraja) {
        this.nombre_baraja = nombre_baraja;
    }
     */
    public void setTorneos(ArrayList<Torneo> torneos) {
        this.torneos = torneos;
    }

    public void cargaTorneosDeUsuario(String nombre_usuario) throws SQLException {
        torneos = gestorBD.cargaTorneosDeUsuario(nombre_usuario);
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("vista_torneos_general.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
