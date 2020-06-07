package Controlador;

import GestorBD.BarajaBD;
import GestorBD.ResultadoBD;
import Modelo.Baraja;
import Modelo.Cruce;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "cuadroManagedBean")
@SessionScoped
public class CuadroManagedBean {

    private ArrayList<Baraja> barajas;
    private ArrayList<String> nombres_barajas;
    private String baraja_filtro;
    private ArrayList<Cruce> cruces;

    private final ResultadoBD resultadoBD;
    private final BarajaBD barajaBD;

    public CuadroManagedBean() {
        resultadoBD = new ResultadoBD();
        barajaBD = new BarajaBD();
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
        barajas = barajaBD.lee_todas_las_barajas();
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
            cruces = resultadoBD.leerCruces(baraja_filtro);
          
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
