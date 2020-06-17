package Controlador;

import GestorBD.BarajaBD;
import GestorBD.ResultadoBD;
import Modelo.Baraja;
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
 * Clase ManagedBean para manejar los datos referentes a las barajas.
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
@ManagedBean(name = "barajaManagedBean")
@SessionScoped
public class BarajaManagedBean {

    /**
     * atributos para guardar el nombre de una baraja, el nombre de una baraja
     * que se vaya a modificar y el nombre nuevo que se le quiera dar a una
     * baraja.
     */
    private String nombre, modificar, nuevo_nombre;

    /**
     * atributo para guardar el nombre de una baraja que se va a asignar a un
     * usuario.
     */
    private String nombre_nueva_baraja_usuario;

    /**
     * Atributos numéricos para guardar el tier de una baraja y el valor del
     * tier cuando se quiere actualizar.
     */
    private int tier, tier_nuevo;

    /**
     * Lista de barajas para almacenar todas las barajas.
     */
    private ArrayList<Baraja> barajas;

    /**
     * Lista de String que almacena únicamente los nombres de todas las barajas.
     */
    private ArrayList<String> nombres_barajas;

    /**
     * Instancia de la clase que contiene todos los métodos que relacionan las
     * barajas con la base de datos.
     */
    private final BarajaBD barajaBD;

    /**
     * atributo logger para recoger las trazas de error de las excepciones
     */
    private static Logger l = null;

    public BarajaManagedBean() throws SQLException {
        barajaBD = new BarajaBD();
        l = LoggerFactory.getLogger(ResultadoBD.class);
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

    /**
     * Método privado que rellena la lista nombres_barajas con el nombre de cada
     * una de las barajas en la lista barajas.
     *
     * @return nombre_barajas devuelve la lista con los nombres.
     */
    private ArrayList<String> lee_nombres_barajas() {
        ArrayList<String> nombres_barajas = new ArrayList<>();
        if (barajas.size() > 0) {
            barajas.forEach((b) -> {
                nombres_barajas.add(b.getNombre());
            });
        }

        return nombres_barajas;
    }

    /**
     * Método que redirecciona la vista del usuario a la página para modificar
     * las barajas. Pone a nulo y a 0 las variables donde se guardan qué baraja
     * se quiere modificar, qué nombre nuevo se le quiere dar y qué tier nuevo
     * se le quiere poner. Carga el arraylist barajas con el método que devuelve
     * una lista con todas las barajas de la base de datos y, si se produce un
     * error, redirecciona al usuario a la vista de error.
     *
     * Si esto funciona utiliza el método privado para cargar los nombres de
     * todas las barajas, añade por delante de todas la cadena "Modificar" y
     * redirecciona al usuario a esa vista.
     */
    public void carga_pagina_gestionar_barajas() {
        modificar = null;
        nuevo_nombre = null;
        tier_nuevo = 0;

        try {
            barajas = barajaBD.lee_todas_las_barajas();
        } catch (SQLException e) {
            l.error(e.getLocalizedMessage());
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema con la base de datos",
                    "homeSuperUser.xhtml");
        }

        nombres_barajas = lee_nombres_barajas();
        nombres_barajas.add(0, "Modificar");
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("gestionar_barajas.xhtml");
        } catch (IOException e) {
            l.error(e.getLocalizedMessage());
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema al cargar la página",
                    "homeSuperUser.xhtml");
        }
    }

    /**
     * NOTA: ver el código de "gestionar_barajas.xhtml". En la vista gestionar
     * barajas hay un desplegable cuyo valor seleccionado se guarda en
     * "modificar". Al lado de ese seleccionable hay un botón "modificar", que
     * lanza este método.
     *
     * Primero comprueba que el valor de la variable modificar es distinto de
     * "modificar", eso implica que el usuario ha seleccionado una baraja para
     * modificar sus datos. Pone los valores tier_nuevo y nombre_nuevo a 0 y
     * nulo y navega por la lista de barajas para guardar el tier valor del
     * atributo con el mismo nombre para la baraja que se quiere modificar (el
     * nombre de esa baraja está en la variable "modificar").
     *
     * Con estos valores cargados redirecciona a la vista "modificar_baraja".
     */
    public void carga_pagina_modificar_baraja() {
        boolean control = true;
        tier_nuevo = 0;
        nuevo_nombre = null;
        if (!modificar.equals("Modificar")) {

            boolean encontrada = false;
            int i = 0;
            Baraja b;
            while (!encontrada && i < barajas.size()) {
                b = barajas.get(i);
                if (b.getNombre().equals(modificar)) {
                    tier = b.getTier();
                    encontrada = true;
                }
                i++;
            }

            if (control) {
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("modificar_baraja.xhtml");
                } catch (IOException e) {
                    l.error(e.getLocalizedMessage());
                    Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema al cargar la página",
                            "homeSuperUser.xhtml");
                }
            }
        }
    }

    /**
     * NOTA: ver el código de "modificar_baraja.xhtml". En esa página hay un
     * cuadro de texto y un desplegable con enteros. Cuando el usuario pulsa el
     * botón actualizar, ambos valores se guardan en las variables de esta clase
     * "nuevo_nombre" y "tier_nuevo" y se lanza este método.
     *
     * Este método comprueba si el nombre nuevo se ha dejado en blanco, y si es
     * así lo iguala a "modificar", y sino, actualia modificar con el método
     * "trtar_nombre". Realiza la misma comprobación con "tier", considerando
     * que no se quiere modificar si se ha dejado en 0.
     *
     * Con los valores nuevos cargados llama al método actualizarBaraja de la
     * clase barajaBD, y en función del resultado que devuelva ese método
     * devuelve al usuario a la página con las barajas si ha ido bien, para que
     * vea los cambios, o le redirecciona a la vista de error si ha habido algún
     * problema.
     */
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
        } catch (SQLException e) {
            l.error(e.getLocalizedMessage());
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

    /**
     * Método que elimina una baraja. En la misma pagina
     * "modificar_baraja.xhtml" existe un botón "borrar_baraja". Este método
     * llama al método de la base de datos que elimina esa baraja de la base de
     * datos y, si se produce algún problema redirige al usuario a la página de
     * error. Si funciona, redirige al usuario a la página de las barajas para
     * que vea los cambios.
     */
    public void borrarBaraja() {
        int funciona;
        try {
            funciona = barajaBD.borrarBaraja(modificar);
        } catch (SQLException e) {
            l.error(e.getLocalizedMessage());
            funciona = -1;
        }
        if (funciona == 1) {
            carga_pagina_gestionar_barajas();
        } else {
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "No se ha podido eliminar la baraja.",
                    "gestionar_barajas.xhtml");
        }
    }

    /**
     * Método para guardar una nueva baraja. NOTA: ver código
     * "gestionar_barajas.xhtml". En esta página hay un cuadro a la derecha con
     * un cuadro de texto y un desplegable, donde indica al usuario que puede
     * introducir el nombre de una baraja nueva y su tier, y debajo un botón que
     * indica "añadir". Si el usuario pulsa este botón se lanza este método.
     *
     * Comprueba que el nombre cumple las condiciones (longitud menor o igual a
     * 20 caracteres) y crea un objeto nuevo del tipo Baraja, con el que llama
     * al método guardarBaraja de la clase BarajaBD.
     *
     * En función del resultado de este método, actualizará la página de barajas
     * para que vea los cambios o le enviará a la página de error indicándole si
     * el nombre de la baraja ya existía o si ha habido algún otro error.
     */
    public void guardar_baraja() {

        if (!nuevo_nombre.equals("") && nuevo_nombre.length() <= 20 && tier_nuevo > 0) {
            nuevo_nombre = Herramientas.tratar_nombre(nuevo_nombre);
            Baraja baraja_nueva = new Baraja(nuevo_nombre, tier_nuevo);
            int valor;

            try {
                valor = barajaBD.guardarBaraja(baraja_nueva);
            } catch (SQLException e) {
                l.error(e.getLocalizedMessage());
                valor = -2;
            }

            if (valor == 1) {
                nuevo_nombre = null;
                tier = 0;
                try {
                    barajas = barajaBD.lee_todas_las_barajas();
                } catch (SQLException e2) {
                    l.error(e2.getLocalizedMessage());
                    valor = -2;
                }
            }

            switch (valor) {
                case (1):
                    Herramientas.lanza_mensaje(TipoMensaje.CORRECTO, "La baraja se ha introducido",
                            "gestionar_barajas.xhtml");
                    break;
                case (0):
                    Herramientas.lanza_mensaje(TipoMensaje.ERROR, "No se ha podido introducir la baraja",
                            "gestionar_barajas.xhtml");
                    break;
                case (-1):
                    Herramientas.lanza_mensaje(TipoMensaje.INFO,
                            "No se ha podido introducir la baraja, ya existe otra con ese nombre.",
                            "gestionar_barajas.xhtml");
                    break;
                case (-2):
                    Herramientas.lanza_mensaje(TipoMensaje.ERROR,
                            "Se ha producido un problema con la base de datos.",
                            "gestionar_barajas.xhtml");
                    break;
            }

        }
    }

    /**
     * Método que redirecciona al usuario a la vista "desglose_barajas_usuario.xhtml".
     * Requiere los métodos que cargan las barajas porque puede ser invocado desde la vista "home_user" y 
     * entonces estas dos listas estarían vacías. 
     */
    public void carga_pagina_desglose_barajas_usuario() {
        try {
            barajas = barajaBD.lee_todas_las_barajas();
            nombres_barajas = lee_nombres_barajas();
        } catch (SQLException e){
            l.error(e.getLocalizedMessage());
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema al cargar la página",
                    "login");
        }

        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("desglose_barajas_usuario.xhtml");
        } catch (IOException e) {
            l.error(e.getLocalizedMessage());
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema al cargar la página",
                    "homeSuperUser.xhtml");
        }
    }

    /**
     * Método que asigna una baraja existente al usuario con sesión activa. 
     * NOTA: ver el código de "desglose_barajas_usuario.xhtml". 
     * Esta página tiene un cuadro en la parte superior y utiliza las mismas variables de esta clase
     * que "modificar_baraja". Cuando el usuario pulsa en un botón "añadir", al lado de un desplegable con
     * todas las barajas de la base de datos (que es el contenido de la lista nombres_baraja", el nombre
     * seleccionado se almacena en el atributo "nombre_nueva_baraja_usuario" y se lanza este método.
     * 
     * Redirecciona al usuario a la misma página para que vea los cambios si funciona, y sino, le redirige
     * a la página de error para indicarle que algo no ha funcionado.
     * 
     * @param nombre_usuario nombre del usuario con la sesión activa sobre el que se guarda la baraja nueva.
     */
    public void agregar_baraja_a_usuario(String nombre_usuario) {
        
        int resultado;
        try {
            resultado = barajaBD.agregar_baraja_a_usuario(nombre_usuario, nombre_nueva_baraja_usuario);
        } catch (SQLException e){
            l.error(e.getLocalizedMessage());
            resultado = -1;
        }
        
        switch(resultado){
            case(1):
                Herramientas.lanza_mensaje(TipoMensaje.CORRECTO, "La baraja se ha añadido correctamente",
                    "login");
                break;
                case(0):
                    Herramientas.lanza_mensaje(TipoMensaje.ERROR, 
                            "La baraja no se ha añadido correctamente",
                            "login");
                    break;
                    case(-1):
                        Herramientas.lanza_mensaje(TipoMensaje.ERROR, 
                            "Se ha producido un problema con la base de datos",
                            "login");
                        break;
        }
    }
       
}
