package Controlador;

import Modelo.Baraja_de_usuario;
import Modelo.GestorBD;
import Modelo.RolUsuario;
import Modelo.Usuario;
import Util.Herramientas;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "formularioManagedBean")
@SessionScoped
public class FormularioManagedBean {

    private String nombre, clave, clave_repetir, correo, rol_solicitado, rol, nombre_nuevo, clave_nueva, clave_nueva_confirmacion;
    private int rondas_jugadas,
            rondas_ganadas,
            rondas_empatadas,
            rondas_perdidas,
            partidas_jugadas,
            partidas_ganadas,
            partidas_perdidas;

    private float porcentaje_rondas,
            porcentaje_partidas;

    private ArrayList<Baraja_de_usuario> lista_de_barajas_de_usuario;

    private ArrayList<String> roles;
    private ArrayList<RolUsuario> solicitudes;

    private GestorBD gestorBD;

    public FormularioManagedBean() {
        gestorBD = new GestorBD();
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

    public GestorBD getGestorBD() {
        return gestorBD;
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

    public void fallo() {
        nombre = null;
        clave = null;
        clave_repetir = null;
        correo = null;
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("agregar_usuario.xhtml");
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

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
            fallo();
        } else {
            nombre = Herramientas.tratar_nombre(nombre);
            Usuario usuarioNuevo = new Usuario(nombre, clave, correo);
            int valor_rol;

            try {
                valor_rol = gestorBD.crearUsuario(usuarioNuevo);
            } catch (SQLException e) {
                valor_rol = -1;
            }

            if (valor_rol == 1) {
                rol = "estandar";
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("homeUser.xhtml");
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            } else {
                if (valor_rol == 0) {
                    rol = "administrador";
                    try {
                        FacesContext.getCurrentInstance()
                                .getExternalContext()
                                .redirect("homeSuperUser.xhtml");
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                } else {

                    if (valor_rol == -1) {
                        fallo();
                    }
                }

            }
        }
    }

    

    public void login() throws SQLException {
        nombre = Herramientas.tratar_nombre(nombre);
        Usuario usuarioIntento = new Usuario(nombre, clave);
        int login = gestorBD.existeUsuario(usuarioIntento);
        String error;

        if (login == 1) {
            cargarUsuario(usuarioIntento);
            if (rol.equals("administrador")) {
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("homeSuperUser.xhtml");
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            } else {
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("homeUser.xhtml");
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }

        } else {
            if (login == -1) {
                error = "Clave incorrecta";
            } else {
                if (login == 0) {
                    error = "No existe ningún usuario con ese nombre";
                }
            }
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("index.xhtml");
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    public void salir() {
        try {
            nombre = null;
            clave = null;
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("index.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarUsuario(Usuario intento) throws SQLException {

        Usuario usuario = gestorBD.cargarUsuario(intento);

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

    public void carga_pagina_perfil_usuario() throws SQLException {
        nombre_nuevo = null;
        clave_nueva = null;
        clave_nueva_confirmacion = null;
        roles = gestorBD.carga_todos_los_roles();
        roles.remove(rol);
        roles.add(0, rol);
        solicitudes = gestorBD.lee_solicitudes(nombre);
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("perfil_usuario.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void confirmar_cambios() throws SQLException {
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
                if (!gestorBD.actualizarUsuario(nombre, nombre_nuevo, clave_nueva)) {
                    error = "error_base";
                }
            }

            if (error.equals("ok") && !rol.equals(rol_solicitado)) {
                //tengo que usaar nomre_nuevo, si no ha cambiado arriba es nombre, pero
                //si arriba ha cambiado, va a buscarlo en la tabla usuarios con el nombre "viejo"
                if (!gestorBD.gestionar_peticion_rol(nombre_nuevo, rol, rol_solicitado)) {
                    error = "error_rol";
                }
            }
        }

        if (error.equals("ok")) {
            nombre = nombre_nuevo;
            clave = clave_nueva;
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
                        .redirect("perfil_usuario.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void poner_a_cero() {
        nombre = null;
        clave = null;
        clave_repetir = null;
    }

    public void volverAlIndex() {
        poner_a_cero();
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("index.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

    private void lanza_error(String mensaje) {
        switch (mensaje) {
            case ("nombre_largo"):

        }
    }

}
