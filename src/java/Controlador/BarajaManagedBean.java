package Controlador;

import GestorBD.BarajaBD;
import Modelo.Baraja;
import Util.Herramientas;
import Util.TipoMensaje;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "barajaManagedBean")
@SessionScoped
public class BarajaManagedBean {


    private String nombre, modificar, nuevo_nombre;
    private String nombre_nueva_baraja_usuario;
    
    private int tier, tier_nuevo;
    
    private ArrayList<Baraja> barajas;
    private ArrayList<String> nombres_barajas;
    
    private final BarajaBD barajaBD;

    public BarajaManagedBean() throws SQLException {
        barajaBD = new BarajaBD();

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
    
    private ArrayList<String> lee_nombres_barajas(){
        ArrayList<String> nombres_barajas = new ArrayList<>();
        barajas.forEach((b) -> {
            nombres_barajas.add(b.getNombre());
        });
        return nombres_barajas;
    }

    public void carga_pagina_gestionar_barajas() {
        modificar = null;
        nuevo_nombre = null;
        tier_nuevo = 0;
        
        try {
            barajas = barajaBD.lee_todas_las_barajas();
        } catch (SQLException e){
            //e.printStackTrace();
            //con.rollback();
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema con la base de datos",
                    "homeSuperUser.xhtml");
        }
        
        //nombres_barajas = gestorBD.lee_nombres_barajas();
        nombres_barajas = lee_nombres_barajas();
        nombres_barajas.add(0, "Modificar");
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("gestionar_barajas.xhtml");
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void carga_pagina_modificar_baraja() {
        boolean control = true;
        tier_nuevo = 0;
        nuevo_nombre = null;
        if (!modificar.equals("Modificar")) {
            
            boolean encontrada = false;
            int i=0;
            Baraja b;
            while (! encontrada && i<barajas.size()){
                b = barajas.get(i);
                if (b.getNombre().equals(modificar)){
                    tier = b.getTier();
                    encontrada = true;
                }
                i ++;
            }

            if (control) {
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("modificar_baraja.xhtml");
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }

    public void actualizarBaraja() {

        if (nuevo_nombre.equals("")) {
            nuevo_nombre = modificar;
        } else {
            nuevo_nombre = Herramientas.tratar_nombre(nuevo_nombre);
        }

        if (tier_nuevo == 0) {
            tier_nuevo = tier;
        }

        int resultado;
        
        try {
            resultado = barajaBD.actualizarBaraja(modificar, nuevo_nombre, tier_nuevo);
        } catch (SQLException e){
            //e.printStackTrace();
            resultado = 0;
        }
        
        if (resultado == 1) {
            carga_pagina_gestionar_barajas();
        } else {
            modificar = null;
            tier_nuevo = 0;
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Ya existe una baraja con ese nombre",
                    "modificar_baraja.xhtml");
        }

    }

    public void borrarBaraja()  {
        boolean funciona;
        try {
            funciona = barajaBD.borrarBaraja(modificar);
        } catch (SQLException e){
            //e.printStackTrace();
            funciona = false;
        }
        if (funciona){
            carga_pagina_gestionar_barajas();
        } else {
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "No se ha podido eliminar la baraja.",
                    "gestionar_barajas.xhtml");
        }
    }

    public void guardar_baraja() throws SQLException {

        if (!nuevo_nombre.equals("") && nuevo_nombre.length() <= 20 && tier_nuevo > 0) {
            nuevo_nombre = Herramientas.tratar_nombre(nuevo_nombre);
            Baraja baraja_nueva = new Baraja(nuevo_nombre, tier_nuevo);
            
            int valor = barajaBD.guardarBaraja(baraja_nueva);
            
            if (valor == 1) {
                nuevo_nombre = null;
                tier = 0;
                barajas = barajaBD.lee_todas_las_barajas();
                Herramientas.lanza_mensaje(TipoMensaje.CORRECTO, "La baraja se ha introducido",
                        "gestionar_barajas.xhtml");
            } else {
               Herramientas.lanza_mensaje(TipoMensaje.ERROR, "No se ha podido introducir la baraja",
                       "gestionar_barajas.xhtml");
            }
        }
    }

    public void carga_pagina_desglose_barajas_usuario() throws SQLException {
        nombres_barajas = lee_nombres_barajas();

        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("desglose_barajas_usuario.xhtml");
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void agregar_baraja_a_usuario(String nombre_usuario) throws SQLException {
        if (barajaBD.agregar_baraja_a_usuario(nombre_usuario, nombre_nueva_baraja_usuario)) {
            Herramientas.lanza_mensaje(TipoMensaje.CORRECTO, "La baraja se ha añadido correctamente",
                    "login");
        } else {
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "La baraja no se ha podido añadir",
                    "desglose_barajas_usuario.xhtml");
        }
    }

}
