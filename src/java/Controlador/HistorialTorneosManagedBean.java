package Controlador;

import Modelo.GestorBD;
import Modelo.Torneo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

    private String nombre_baraja, nombre_baraja_filtro;
    private ArrayList<Torneo> torneos, filtrados;
    private ArrayList<String> barajas_usadas;
    private GestorBD gestorBD;

    public HistorialTorneosManagedBean() {
        gestorBD = new GestorBD();
    }

    public String getNombre_baraja_filtro() {
        return nombre_baraja_filtro;
    }

    public String getNombre_baraja() {
        return nombre_baraja;
    }
    
    public ArrayList<String> getBarajas_usadas(){
        return barajas_usadas;
    }
    
    public ArrayList<Torneo> getFiltrados() {
        return filtrados;
    }
    
    public ArrayList<Torneo> getTorneos() {
        return torneos;
    }

    public void setNombre_baraja_filtro(String nombre_baraja_filtro) {
        this.nombre_baraja_filtro = nombre_baraja_filtro;
    }

    public void setNombre_baraja(String nombre_baraja) {
        this.nombre_baraja = nombre_baraja;
    }

    public void setTorneos(ArrayList<Torneo> torneos) {
        this.torneos = torneos;
    }

    public void cargaTorneosDeUsuario(String nombre_de_usuario) throws SQLException {
        torneos = gestorBD.cargaTorneosDeUsuario(nombre_de_usuario);
        filtrados = new ArrayList<>();
        String nombre_baraja;
        if (torneos.size() > 0) {
            barajas_usadas = new ArrayList<>();
            barajas_usadas.add("todas");
            Torneo t;
            Iterator it = torneos.iterator();
            while (it.hasNext()) {
                t = (Torneo) it.next();
                nombre_baraja = t.getNombre_baraja();
                if (!barajas_usadas.contains(nombre_baraja)){
                    barajas_usadas.add(t.getNombre_baraja());
                }
            }
            filtrados.addAll(torneos);
        }

        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("vista_torneos_general.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void filtrar_por_baraja() {
        filtrados.clear();
        if (nombre_baraja_filtro.equals("todas")) {
            filtrados.addAll(torneos);
        } else {
            Torneo t;
            Iterator it = torneos.iterator();
            while (it.hasNext()) {
                t = (Torneo) it.next();
                if (t.getNombre_baraja().equals(nombre_baraja_filtro)) {
                    filtrados.add(t);
                }
            }
        }
    }

}
