package Controlador;

import Modelo.Baraja;
import Modelo.GestorBD;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "barajaManagedBean")
@SessionScoped
public class BarajaManagedBean {

    /*
    @ManagedProperty("#{formularioManagedBean}")
    private FormularioManagedBean formularioManagedBean;
    */
    
    private String nombre, modificar, nuevo_nombre;
    private String nombre_nueva_baraja_usuario;
    private int tier, tier_nuevo;
    private ArrayList<Baraja> barajas;
    private ArrayList<String> nombres_barajas;
    private final GestorBD gestorBD;

    public BarajaManagedBean() throws SQLException {
        gestorBD = new GestorBD();

    }

    public String getNombre_nueva_baraja_usuario() {
        return nombre_nueva_baraja_usuario;
    }

    public void setNombre_nueva_baraja_usuario(String nombre_nueva_baraja_usuario) {
        this.nombre_nueva_baraja_usuario = nombre_nueva_baraja_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTier() {
        return tier;
    }

    public void setNombre(String s) {
        nombre = s;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public ArrayList<Baraja> getBarajas() {
        return barajas;
    }

    public void setModificar(String modificar) {
        this.modificar = modificar;
    }

    public String getModificar() {
        return modificar;
    }

    public String getNuevo_nombre() {
        return nuevo_nombre;
    }

    public int getTier_nuevo() {
        return tier_nuevo;
    }

    public void setNuevo_nombre(String nuevo_nombre) {
        this.nuevo_nombre = nuevo_nombre;
    }

    public void setTier_nuevo(int tier_nuevo) {
        this.tier_nuevo = tier_nuevo;
    }

    
    public ArrayList<String> getNombres_barajas(){
        return nombres_barajas;
    }

    public void carga_pagina_gestionar_barajas() throws SQLException {
        modificar = null;
        barajas = gestorBD.lee_nombres_barajas();
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("barajas.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarBaraja() throws SQLException {
        Baraja barajaNueva = new Baraja(nombre, tier);
        gestorBD.guardarBaraja(barajaNueva);
        barajas = gestorBD.lee_nombres_barajas();
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("barajas.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modificar() throws SQLException {
        int tier_auxiliar = gestorBD.devuelve_tier_baraja(modificar);
        if (tier_auxiliar != 0) {
            tier = tier_auxiliar;
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("modificar_baraja.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            modificar = null;
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("barajas.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void actualizarBaraja() throws SQLException {
        // como el managed bean es una variable de sesion aun no he tocado "modificar", que tiene
        // los datos de la baraja vieja. Asi que aqui es donde la pongo a nulo
        // en este punto los valores nomre y tier son los valores a los que quiero actualizar

        if (gestorBD.actualizarBaraja(modificar, nuevo_nombre, tier_nuevo)) {
            modificar = null;
            tier_nuevo = 0;
            barajas = gestorBD.lee_nombres_barajas();
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("barajas.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            modificar = null;
            tier_nuevo = 0;
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("modificar_baraja.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    
    public void carga_pagina_desglose_barajas_usuario() throws SQLException {
        barajas = gestorBD.lee_nombres_barajas();
        nombres_barajas = new ArrayList<>();
        barajas.forEach((baraja) -> {
            nombres_barajas.add(baraja.getNombre());
        });

        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("desglose_barajas_usuario.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    public void carga_pagina_agregar_baraja_a_usuario() throws SQLException {
        barajas = gestorBD.leeBarajas();
        nombres_barajas = new ArrayList<String>();
        for (Baraja baraja : barajas) {
            nombres_barajas.add(baraja.getNombre());
        }

        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("agregar_baraja_a_usuario.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */


    public void agregar_baraja_a_usuario(String nombre_usuario) throws SQLException {
        if (gestorBD.agregar_baraja_a_usuario(nombre_usuario, nombre_nueva_baraja_usuario)) {
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("resultado_introducido.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
           try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("resultado_no_introducido.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
    }

}
