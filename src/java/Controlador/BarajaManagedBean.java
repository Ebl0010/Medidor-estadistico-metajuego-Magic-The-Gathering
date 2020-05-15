package Controlador;

import Modelo.Baraja;
import Modelo.GestorBD;
import Util.Herramientas;
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

    public ArrayList<String> getNombres_barajas() {
        return nombres_barajas;
    }

    public void carga_pagina_gestionar_barajas() throws SQLException {
        modificar = null;
        nuevo_nombre = null;
        tier_nuevo = 0;
        barajas = gestorBD.lee_todas_las_barajas();
        nombres_barajas = gestorBD.lee_nombres_barajas();
        nombres_barajas.add(0, "Modificar");
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("gestionar_barajas.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carga_pagina_modificar_baraja() {
        boolean control = true;
        tier_nuevo = 0;
        nuevo_nombre = null;
        if (!modificar.equals("Modificar")) {
            try {
                tier = gestorBD.devuelve_tier_baraja(modificar);
            } catch (SQLException e) {
                control = false;
            }

            if (control) {
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("modificar_baraja.xhtml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void actualizarBaraja() throws SQLException {

        if (nuevo_nombre.equals("")) {
            nuevo_nombre = modificar;
        }

        if (tier_nuevo == 0) {
            tier_nuevo = tier;
        }

        if (gestorBD.actualizarBaraja(modificar, nuevo_nombre, tier_nuevo)) {
            
            carga_pagina_gestionar_barajas();

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

    public void borrarBaraja() throws SQLException {
        if (gestorBD.borrarBaraja(modificar)) {
            carga_pagina_gestionar_barajas();
        } else {
            carga_pagina_modificar_baraja();
        }
    }

    public void guardar_baraja() throws SQLException {

        if (!nuevo_nombre.equals("") && nuevo_nombre.length() <= 20 && tier_nuevo > 0) {
            nuevo_nombre = Herramientas.tratar_nombre(nuevo_nombre);
            Baraja baraja_nueva = new Baraja(nuevo_nombre, tier_nuevo);
            if (gestorBD.guardarBaraja(baraja_nueva)) {
                nuevo_nombre = null;
                tier = 0;
                barajas = gestorBD.lee_todas_las_barajas();
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("gestionar_barajas.xhtml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                nuevo_nombre = null;
                tier_nuevo = 0;
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("gestionar_barajas.xhtml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void carga_pagina_desglose_barajas_usuario() throws SQLException {
        nombres_barajas = gestorBD.lee_nombres_barajas();

        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("desglose_barajas_usuario.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
