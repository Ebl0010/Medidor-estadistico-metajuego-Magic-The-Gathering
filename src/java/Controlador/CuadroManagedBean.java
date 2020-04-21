package Controlador;

import Modelo.Baraja;
import Modelo.Baraja_de_usuario;
import Modelo.Cruce;
import Modelo.GestorBD;
import Modelo.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "cuadroManagedBean")
@SessionScoped
public class CuadroManagedBean {

    private ArrayList<Baraja> barajas;
    private ArrayList<String> nombres_barajas;
    private String baraja_filtro;
    private ArrayList<Cruce> cruces;

    private GestorBD gestorBD;

    public CuadroManagedBean() {
        gestorBD = new GestorBD();
    }

    public ArrayList<Baraja> getBarajas() {
        return barajas;
    }

    public ArrayList<String> getNombres_barajas() {
        return nombres_barajas;
    }

    public void setBaraja_filtro(String baraja_filtro) {
        this.baraja_filtro = baraja_filtro;
    }

    public String getBaraja_filtro() {
        return baraja_filtro;
    }

    public ArrayList<Cruce> getCruces(){
        return cruces;
    }
    
    

    public void carga_pagina_estadisticas_barajas() throws SQLException {
        barajas = gestorBD.lee_todas_las_barajas();
        nombres_barajas = new ArrayList<>();
        cruces = new ArrayList<>();
        nombres_barajas.add("todas");
        for (int i = 0; i < barajas.size(); i++) {
            nombres_barajas.add(barajas.get(i).getNombre());
        }
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("estadisticas_barajas.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void detalles_baraja() throws SQLException {

        if (!baraja_filtro.equals("todas")) {
            cruces = gestorBD.leerCruces(baraja_filtro);
          
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("estadisticas_barajas_detalladas.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        } else {
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("estadisticas_barajas.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
