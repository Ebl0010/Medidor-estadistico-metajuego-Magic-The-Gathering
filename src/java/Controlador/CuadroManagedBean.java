package Controlador;

import GestorBD.BarajaBD;
import GestorBD.ResultadoBD;
import Modelo.Baraja;
import Modelo.Cruce;
import Util.Herramientas;
import Util.TipoMensaje;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase ManagedBean para manejar los datos referentes a los objetos Cruce y
 * resultados.
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
@ManagedBean(name = "cuadroManagedBean")
@SessionScoped
public class CuadroManagedBean {

    /**
     * Atributo que almacena la lista de todas las barajas.
     */
    private ArrayList<Baraja> barajas;
    /**
     * Atributo que almacena la lista de los nombres de todas las barajas.
     */
    private ArrayList<String> nombres_barajas;
    /**
     * Atributo que almacena el nombre de una baraja para utilizarlo como
     * filtro.
     */
    private String baraja_filtro;
    /**
     * Atributo que almacena una lista de objetos Cruce.
     */
    private ArrayList<Cruce> cruces;

    /**
     * Objeto resultadoBD para llamar a los métodos contra la base de datos
     */
    private final ResultadoBD resultadoBD;

    /**
     * Objeto barajaBD para llamar a los métodos contra la base de datos.
     */
    private final BarajaBD barajaBD;

    /**
     * atributo logger para recoger las trazas de error de las excepciones
     */
    private static Logger l = null;

    public CuadroManagedBean() {
        resultadoBD = new ResultadoBD();
        barajaBD = new BarajaBD();
        l = LoggerFactory.getLogger(Cruce.class);
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

    public ArrayList<Cruce> getCruces() {
        return cruces;
    }

    /**
     * Método que redirecciona a la vista "estadisticas_barajas.xhtml". Esta
     * vista lee datos de las variables de esta clase barajas y nombres_barajas
     * así que es necesario inicializarlas; la primera con el metodo
     * lee_todas_las_barajas y la segunda pasando un bucle sobre la lista de
     * barajas que guarde solo los nombres.
     */
    public void carga_pagina_estadisticas_barajas() {
        try {
            barajas = barajaBD.lee_todas_las_barajas();
        } catch (SQLException e) {
            l.error(e.getLocalizedMessage());
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema con la base de datos",
                    "login");
        }

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
            l.error(e.getLocalizedMessage());
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema al cargar la página",
                    "homeSuperUser.xhtml");
        }
    }

    /**
     * NOTA: ver el código de "estadisicas_barajas.xhtml" y "estadisticas_barajas_detalladas.xhtml".
     * La página estadisticas_barajas tiene un desplegable que lee del arraylist nombres barajas, donde
     * están los nombres de todas las barajas y la cadena "todas". A su lado hay un botón que indica "filtrar",
     * y cuando el usuario lo pulsa se lanza este método. 
     * 
     * Recoge en la variable baraja_filtro el string selecionado en el desplegable, y si es "todas" recarga la página.
     * Si no carga el array cruces llamando al método leerCruces del objeto resultadoBD, pasándole como argumento
     * el string (que es el nombre de una baraja) seleciconado en el desplegable. 
     */
    public void detalles_baraja() {

        if (!baraja_filtro.equals("todas")) {
            try {
                cruces = resultadoBD.leerCruces(baraja_filtro);
            } catch (SQLException e) {
                l.error(e.getLocalizedMessage());
                Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema con la base de datos",
                        "login");
            }

            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("estadisticas_barajas_detalladas.xhtml");
            } catch (IOException e) {
                l.error(e.getLocalizedMessage());
                Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema al cargar la página",
                        "login");
            }

        } else {
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("estadisticas_barajas.xhtml");
            } catch (IOException e) {
                l.error(e.getLocalizedMessage());
                Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema al cargar la página",
                        "login");
            }
        }

    }
}
