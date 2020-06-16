package Controlador;

import GestorBD.FormularioBD;
import GestorBD.RolesBD;
import Modelo.Baraja_de_usuario;
import Modelo.RolUsuario;
import Modelo.Usuario;
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
 * Clase ManagedBean para manejar los datos referentes al usuario. Se utiliza
 * para el login, registro de usuarios nuevos, actualización de datos del perfil
 * y como variable de sesión para saber el usuario que está activo.
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
@ManagedBean(name = "formularioManagedBean")
@SessionScoped
public class FormularioManagedBean {

    /**
     * Atributos que almacenen el nombre del usuario y su clave para el login
     * además de todos los datos que se solicitan para un registro nuevo y para
     * actualizar el perfil.
     */
    private String nombre, clave, clave_repetir, correo, rol_solicitado, rol, nombre_nuevo, clave_nueva, clave_nueva_confirmacion;
    /**
     * Atributos enteros que almacenen los valores de las estadísticas de un
     * jugador correspondientes a sus rondas gandas, perdidas y empatadas y
     * partidas ganadas y perdidas.
     */
    private int rondas_jugadas,
            rondas_ganadas,
            rondas_empatadas,
            rondas_perdidas,
            partidas_jugadas,
            partidas_ganadas,
            partidas_perdidas;

    /**
     * Atributos decimales que almacenen los ratios de victoria por patidas y
     * por rondas del usuario.
     */
    private float porcentaje_rondas,
            porcentaje_partidas;

    /**
     * lista que almacena las barajas asignadas al usuario y sus estadísticas.
     */
    private ArrayList<Baraja_de_usuario> lista_de_barajas_de_usuario;

    /**
     * listas que almacenan las solicitudes de roles del usuario.
     */
    private ArrayList<String> roles;
    private ArrayList<RolUsuario> solicitudes;

    /**
     * Objetos que trabajan con la base de datos para realizar las consultas e
     * inserciones.
     */
    private final FormularioBD formularioBD;
    private RolesBD rolesBD; //no es final porque requeriria instanciarlo en el constructor

    /**
     * atributo logger para recoger las trazas de error de las excepciones
     */
    private static Logger l = null;

    public FormularioManagedBean() {
        formularioBD = new FormularioBD();
        l = LoggerFactory.getLogger(FormularioManagedBean.class);
    }

    public String getNombre() {
        return nombre;
    }

    public String getClave() {
        return clave;
    }

    public String getClave_repetir() {
        return clave_repetir;
    }

    public int getRondas_jugadas() {
        return rondas_jugadas;
    }

    public int getRondas_ganadas() {
        return rondas_ganadas;
    }

    public int getRondas_empatadas() {
        return rondas_empatadas;
    }

    public int getRondas_perdidas() {
        return rondas_perdidas;
    }

    public int getPartidas_jugadas() {
        return partidas_jugadas;
    }

    public int getPartidas_ganadas() {
        return partidas_ganadas;
    }

    public int getPartidas_perdidas() {
        return partidas_perdidas;
    }

    public float getPorcentaje_rondas() {
        return porcentaje_rondas;
    }

    public float getPorcentaje_partidas() {
        return porcentaje_partidas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setClave_repetir(String clave_repetir) {
        this.clave_repetir = clave_repetir;
    }

    public String getNombre_nuevo() {
        return nombre_nuevo;
    }

    public void setNombre_nuevo(String nombre_nuevo) {
        this.nombre_nuevo = nombre_nuevo;
    }

    public String getClave_nueva() {
        return clave_nueva;
    }

    public void setClave_nueva(String clave_nueva) {
        this.clave_nueva = clave_nueva;
    }

    public String getClave_nueva_confirmacion() {
        return clave_nueva_confirmacion;
    }

    public void setClave_nueva_confirmacion(String clave_nueva_confirmacion) {
        this.clave_nueva_confirmacion = clave_nueva_confirmacion;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol_solicitado() {
        return rol_solicitado;
    }

    public String getRol() {
        return rol;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setRol_solicitado(String rol_solicitado) {
        this.rol_solicitado = rol_solicitado;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRondas_jugadas(int rondas_jugadas) {
        this.rondas_jugadas = rondas_jugadas;
    }

    public void setRondas_ganadas(int rondas_ganadas) {
        this.rondas_ganadas = rondas_ganadas;
    }

    public void setRondas_empatadas(int rondas_empatadas) {
        this.rondas_empatadas = rondas_empatadas;
    }

    public void setRondas_perdidas(int rondas_perdidas) {
        this.rondas_perdidas = rondas_perdidas;
    }

    public void setPartidas_jugadas(int partidas_jugadas) {
        this.partidas_jugadas = partidas_jugadas;
    }

    public void setPartidas_ganadas(int partidas_ganadas) {
        this.partidas_ganadas = partidas_ganadas;
    }

    public void setPartidas_perdidas(int partidas_perdidas) {
        this.partidas_perdidas = partidas_perdidas;
    }

    public void setPorcentaje_rondas(float porcentaje_rondas) {
        this.porcentaje_rondas = porcentaje_rondas;
    }

    public void setPorcentaje_partidas(float porcentaje_partidas) {
        this.porcentaje_partidas = porcentaje_partidas;
    }

    public void setLista_de_barajas_de_usuario(ArrayList<Baraja_de_usuario> lista_de_barajas_de_usuario) {
        this.lista_de_barajas_de_usuario = lista_de_barajas_de_usuario;
    }

    public ArrayList<Baraja_de_usuario> getLista_de_barajas_de_usuario() {
        return lista_de_barajas_de_usuario;
    }

    public ArrayList<RolUsuario> getSolicitudes() {
        return solicitudes;
    }

    /**
     * Método que borra los datos que el usuario debe introducir en su registro.
     * Se utiliza cuando se redirecciona a las páginas de registro o hay algún
     * fallo.
     */
    public void poner_a_cero() {
        nombre = null;
        clave = null;
        clave_repetir = null;
        correo = null;
    }

    /**
     * Método que introduce un usuario nuevo en la base de datos. NOTA: ver el
     * código de "agregar_usuario.xhtml". El método utiliza una variable error
     * para el control de flujo de entrada. Cuando el usuario pulsa el botón
     * "confirmar" de la página agregar_usuario, las variables nombre, clave,
     * repetir_clave y correo toman los valores que el usuario haya introducido.
     *
     * Si el nombre no está entre 6 y 20 caracteres y las claves no son iguales
     * o no cumplen el requisito de longitud, la variable error se actualiza con
     * un mensaje de ayuda al usuario que indica el error producido.
     *
     * Si la variable error no vale "ok", se ponen todos los datos a cero y se
     * reenvía al usuario a la página de error. Si todos los datos introducidos
     * son correctos corrige el nombre y luego introduce al usuario en la base
     * de datos En la variable valor_rol almacena el retorno de crearUsuario de
     * la clase formularioBD, inicializa la variable rol a administrador o
     * estandar y reenvía al usuario al homeUser o al homeSuperUser en función
     * del rol. NOTA: Si el método crearUsuario devuelve -1 o -2 se reenvía a la
     * página de error indicando cuál ha sido el problema.
     */
    public void crearUsuario() {
        String error = "ok";
        if (nombre.length() > 20 || nombre.length() < 6) {
            error = "El nombre debe tener entre 6 y 20 caracteres.";
        } else {
            if (!clave.equals(clave_repetir)) {
                error = "Las contraseñas no son iguales.";
            } else {
                if (clave.length() > 16 || clave.length() < 6) {
                    error = "La contraseña debe tener entre 6 y 16 caracteres";
                }
            }
        }

        if (!"ok".equals(error)) {
            poner_a_cero();
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, error, "agregar_usuario.xhtml");
        } else {
            nombre = Herramientas.tratar_nombre(nombre);
            Usuario usuarioNuevo = new Usuario(nombre, clave, correo);
            int valor_rol;

            try {
                valor_rol = formularioBD.crearUsuario(usuarioNuevo);
            } catch (SQLException e) {
                valor_rol = -1;
                error = "No se ha podido introducir el usuario, ha habido un problema en la base de datos";
            }

            if (valor_rol == 1) {
                rol = "estandar";
                try {

                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("homeUser.xhtml");
                } catch (IOException e) {
                    l.error(e.getLocalizedMessage());
                    Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema al cargar la página",
                            "index.xhtml");
                }
            } else {
                if (valor_rol == 0) {
                    rol = "administrador";
                    try {
                        FacesContext.getCurrentInstance()
                                .getExternalContext()
                                .redirect("homeSuperUser.xhtml");
                    } catch (IOException e) {
                        l.error(e.getLocalizedMessage());
                        Herramientas.lanza_mensaje(TipoMensaje.ERROR,
                                "Se ha prodocido un problema al cargar la página",
                                "index.xhtml");
                    }
                } else {
                    if (valor_rol == -2) {
                        error = "Ya existe un usuario con ese nombre";
                    }
                    Herramientas.lanza_mensaje(TipoMensaje.ERROR, error, "index.xhtml");
                }
            }
        }
    }

    public void login() {
        nombre = Herramientas.tratar_nombre(nombre);
        Usuario usuarioIntento = new Usuario(nombre, clave);
        //lo inicializo a -1 para que no de nullPointerException en "login == x"
        int login = -1;
        String error = "";

        try {
            login = formularioBD.existeUsuario(usuarioIntento);
        } catch (SQLException e) {
            error = "Se ha producido un problema en la base de datos.";
            l.error(e.getLocalizedMessage());
        }

        if (login == 1) {

            cargarUsuario(usuarioIntento);

            if (rol.equals("administrador")) {
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("homeSuperUser.xhtml");
                } catch (IOException e) {
                    error = "Se ha producido un problema al cargar la página.";
                }
            } else {
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("homeUser.xhtml");
                } catch (IOException e) {
                    error = "Se ha producido un problema al cargar la página.";
                }
            }

        } else {
            switch (login) {
                case (-1):
                    error = "La clave introducida no es correcta";
                    break;
                case (0):
                    error = "No existe ningún usuario con ese nombre";
                    break;
                case (-2):
                    error = "Ha habido un problema. Vuelve a intentarlo";
                    break;

            } // cierra switch
        }

        poner_a_cero(); //aunque si se quedan en la cache del navegador al recargar no se borran
        Herramientas.lanza_mensaje(TipoMensaje.ERROR, error, "index.xhtml");
    }

    /**
     * Método invocado desde casi cualquier página para cerrar la sesión actual
     * y volver a la página de login.
     */
    public void salir() {
        try {
            nombre = null;
            clave = null;
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("index.xhtml");
        } catch (IOException e) {
            String error = "Se ha producido un problema al cargar la página.";
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, error, "index.xhtml");
        }
    }

    /**
     * Método que carga los datos de un usuario en el objeto ManagedBean activo
     * toda la sesión. Este método recibe un objeto Usuario (intento) que solo
     * tiene inicializados un nombre y clave. Con este objeto se llama al método
     * cargarUsuario de formularioBD que devuelve otro objeto Usuario con todos
     * los datos del usuario que responde a ese nombre y contraseña, de la base
     * de datos. Si no lanza ninguna excepción, todos estos valores obtenidos
     * (partidas gandas, perdidas, rondas ganadas...) se copian en los atributos
     * de la clase para ser accesibles desde la vista.
     *
     * @param intento usuario que solo tiene el nombre y contraseña del que se
     * quieren cargar los datos.
     */
    public void cargarUsuario(Usuario intento) {

        Usuario usuario = new Usuario();

        try {
            usuario = formularioBD.cargarUsuario(intento);
        } catch (SQLException e) {
            Herramientas.lanza_mensaje(TipoMensaje.ERROR,
                    "No se ha podido cargar el usuario correctamente, vuelve a intentarlo.",
                    "index.xhtml");
        }

        //si hay un problema al cargar el usuario, en la excepción se redirige a otra página y estas
        //líneas no se ejecutan, pero, si no se inicializa el usuario antes de llamar al método cargarUsuario
        //salen errores de "variable no inicializada".
        rondas_ganadas = usuario.getRondas_ganadas();
        rondas_perdidas = usuario.getRondas_perdidas();
        rondas_empatadas = usuario.getRondas_empatadas();
        rondas_jugadas = rondas_ganadas + rondas_empatadas + rondas_perdidas;
        partidas_ganadas = usuario.getPartidas_ganadas();
        partidas_perdidas = usuario.getPartidas_perdidas();
        partidas_jugadas = partidas_ganadas + partidas_perdidas;
        porcentaje_partidas = usuario.getPorcentaje_partidas();
        porcentaje_rondas = usuario.getPorcentaje_rondas();
        rol = usuario.getRol();

        lista_de_barajas_de_usuario = usuario.getLista_de_barajas_de_usuario();

    }

    /**
     * Método que carga la página perfil_usuario, donde el usuario puede
     * modificar sus datos personales NOTA: ver código de la página
     * "perfil_usuario.xhtml". Para cargar el contenido del desplegable donde el
     * usuario selecciona la solicitud de rol que quiere hacer se tienen que
     * cargar los nombres de los roles en un arrayList, cosa que hace el método
     * carga_todos_los_roles.
     *
     * Para simplificar la forma en que se presentan los datos al usuario, su
     * rol actual se coloca el primero en este arraylist (por eso primero se
     * elimina y luego se añade en la posición 0).
     */
    public void carga_pagina_perfil_usuario() {
        nombre_nuevo = null;
        clave_nueva = null;
        clave_nueva_confirmacion = null;
        rolesBD = new RolesBD();

        try {
            roles = rolesBD.carga_todos_los_roles();
        } catch (SQLException e) {
            l.error(e.getLocalizedMessage());
            Herramientas.lanza_mensaje(TipoMensaje.ERROR,
                    "Se ha prodocido un problema con la base de datos",
                    "index.xhtml");
        }

        roles.remove(rol);
        roles.add(0, rol);

        try {
            solicitudes = rolesBD.lee_solicitudes(nombre);
        } catch (SQLException e2) {
            l.error(e2.getLocalizedMessage());
            Herramientas.lanza_mensaje(TipoMensaje.ERROR,
                    "Se ha prodocido un problema con la base de datos",
                    "index.xhtml");
        }

        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("perfil_usuario.xhtml");
        } catch (IOException e) {
            l.error(e.getLocalizedMessage());
            Herramientas.lanza_mensaje(TipoMensaje.ERROR,
                    "Se ha prodocido un problema al cargar la página",
                    "index.xhtml");
        }
    }

    /**
     * Método para actualizar los datos del usuario. NOTA: ver el código de
     * "perfil_usuario.xhtml". La página mencionada tiene varios cuadros de
     * texto donde el usuario puede introducir un nuevo nombre y una nueva
     * contraseña, que se pasan por un filtro de control de flujo y actualizan
     * la variable "error" si no los pasan.
     *
     * Si los pasan se llama al método actualizarUsuario con los datos nuevos, y
     * guarda el valor devuelto por este método en una variable entera
     * (actualiza) para ver si ha funcioado o si ha habido un error.
     *
     * Por otro lado se gestiona la solicitud de roles con el método
     * gestionar_petición_rol.
     *
     * Al final, si la variable error es "ok" redirige al usuario a la página de
     * "error" para notificarle que todo ha ido bien, y sino, carga la misma
     * página con el mensaje oportuno.
     */
    public void confirmar_cambios() {
        String error = "ok";
        if (nombre_nuevo.length() == 0) {
            nombre_nuevo = nombre;
        } else {
            if (nombre_nuevo.length() > 20 || nombre_nuevo.length() > 6) {
                error = "nombre_largo_o_corto";
            } else {
                nombre_nuevo = Herramientas.tratar_nombre(nombre_nuevo);
            }
        }
        if (error.equals("ok")
                && clave_nueva.length() == 0
                && clave_nueva_confirmacion.length() == 0) {

            clave_nueva = clave;
        } else {
            if (clave_nueva.length() > 16) {
                error = "clave_larga";
            } else {
                if (!clave_nueva.equals(clave_nueva_confirmacion)) {
                    error = "claves_distintas";
                }
            }
        }

        if (error.equals("ok")) {
            if (!nombre.equals(nombre_nuevo) || !clave.equals(clave_nueva)) {
                int actualiza = 0;
                try {
                    actualiza = formularioBD.actualizarUsuario(nombre, nombre_nuevo, clave_nueva);
                } catch (SQLException e) {
                    l.error(e.getLocalizedMessage());
                    Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema con la base de datos",
                            "login");
                }

                switch (actualiza) {
                    case (-1):
                        error = "Ese nombre de usuario ya está en uso.";
                        break;
                    case (-2):
                        error = "Error inesperdo, inténtelo de nuevo.";
                        break;
                } // cierra switch
            }

            if (error.equals("ok") && !rol.equals(rol_solicitado)) {
                //tengo que usaar nomre_nuevo, si no ha cambiado arriba es nombre, pero
                //si arriba ha cambiado, va a buscarlo en la tabla usuarios con el nombre "viejo"
                try {
                    if (!rolesBD.gestionar_peticion_rol(nombre_nuevo, rol, rol_solicitado)) {
                        error = "La petición de cambio de cuenta no ha podido gestionarse.";
                    }
                } catch (SQLException e2) {
                    l.error(e2.getLocalizedMessage());
                    Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema con la base de datos",
                            "login");
                }
            }
        }

        if (error.equals("ok")) {
            nombre = nombre_nuevo;
            clave = clave_nueva;
            error = "El cambio se ha producido correctamente";
            Herramientas.lanza_mensaje(TipoMensaje.CORRECTO, error, "perfil_usuario.xhtml");
        } else {
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, error, "perfil_usuario.xhtml");
        }
    }

    /**
     * Método que redirecciona al usuario a la página para nuevos registros.
     */
    public void carga_pagina_registrarse() {
        //poner a 0 no cambia todo porque esta ligado a modificar el perfil
        poner_a_cero();
        correo = null;
        rol = null;

        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("agregar_usuario.xhtml");
        } catch (IOException e) {
            l.error(e.getLocalizedMessage());
            Herramientas.lanza_mensaje(TipoMensaje.ERROR, "Se ha prodocido un problema al cargar la página",
                    "login");
        }
    }

}
