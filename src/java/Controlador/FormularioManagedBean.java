package Controlador;

import Modelo.Baraja_de_usuario;
import Modelo.GestorBD;
import Modelo.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "formularioManagedBean")
@SessionScoped
public class FormularioManagedBean {

    private String nombre, clave;
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

    public void setGestorBD(GestorBD gestorBD) {
        this.gestorBD = gestorBD;
    }

    

    public ArrayList<Baraja_de_usuario> getLista_de_barajas_de_usuario() {
        return lista_de_barajas_de_usuario;
    }

    public void setLista_de_barajas_de_ususario(ArrayList<Baraja_de_usuario> lista) {
        lista_de_barajas_de_usuario = lista;
    }

    public void login() throws SQLException {
        Usuario usuarioIntento = new Usuario(nombre, clave);
        int login = gestorBD.existeUsuario(usuarioIntento);
        switch (login) {
            case (0):
                nombre = null;
                clave = null;
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("index.xhtml");
                } catch (IOException e) {
                    //e.printStackTrace();
                }
                break;

            case (1):
                cargarUsuario(gestorBD.cargarUsuario(usuarioIntento));
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("homeUser.xhtml");
                } catch (IOException e) {
                    //e.printStackTrace();
                }
                break;

            case (2):
                cargarUsuario(gestorBD.cargarUsuario(usuarioIntento));
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("homeUser.xhtml"); //AQUI TIENE QUE SER HOMESUPERUSER
                } catch (IOException e) {
                    //e.printStackTrace();
                }
                break;
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

    
    public void cargarUsuario(Usuario usuario) throws SQLException {

        rondas_ganadas = usuario.getRondas_ganadas();
        rondas_perdidas = usuario.getRondas_perdidas();
        rondas_empatadas = usuario.getRondas_empatadas();
        rondas_jugadas = rondas_ganadas + rondas_empatadas + rondas_perdidas;
        partidas_ganadas = usuario.getPartidas_ganadas();
        partidas_perdidas = usuario.getPartidas_perdidas();
        partidas_jugadas = partidas_ganadas + partidas_perdidas;
        porcentaje_partidas = usuario.getPorcentaje_partidas();
        porcentaje_rondas = usuario.getPorcentaje_rondas();
        
        
        lista_de_barajas_de_usuario = usuario.getLista_de_barajas_de_usuario();

    }
    
    public void crear_usuario(){
        Usuario nuevoUsuario = new Usuario(nombre, clave);
        boolean creado = gestorBD.guardarUsuario(nuevoUsuario);
        nombre = null;
        clave = null;
        if (creado) {
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
