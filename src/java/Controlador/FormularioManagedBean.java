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
    private int pj, pg, pe, pp, rj, rg, re, rp;
    private float por_partidas, por_rondas;
    private ArrayList<Baraja_de_usuario> lista_de_barajas_de_usuario;

    private GestorBD gestorBD;

    public FormularioManagedBean() {
        gestorBD = new GestorBD();
    }

    public String getNombre() {
        return nombre;
    }

    public int getPj() {
        return pj;
    }

    public int getPg() {
        return pg;
    }

    public int getPe() {
        return pe;
    }

    public int getPp() {
        return pp;
    }

    public int getRj() {
        return rj;
    }

    public int getRg() {
        return rg;
    }

    public int getRe() {
        return re;
    }

    public int getRp() {
        return rp;
    }

    public float getPor_partidas() {
        return por_partidas;
    }

    public float getPor_rondas() {
        return por_rondas;
    }

    public void setNombre(String s) {
        nombre = s;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String c) {
        clave = c;
    }

    public void setPj(int pj) {
        this.pj = pj;
    }

    public void setPg(int pg) {
        this.pg = pg;
    }

    public void setPe(int pe) {
        this.pe = pe;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public void setRj(int rj) {
        this.rj = rj;
    }

    public void setRg(int rg) {
        this.rg = rg;
    }

    public void setRe(int re) {
        this.re = re;
    }

    public void setRp(int rp) {
        this.rp = rp;
    }

    public void setPor_partidas(float por_partidas) {
        this.por_partidas = por_partidas;
    }

    public void setPor_rondas(float por_rondas) {
        this.por_rondas = por_rondas;
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
                    e.printStackTrace();
                }
                break;

            case (1):
                cargarUsuario(gestorBD.cargarUsuario(usuarioIntento));
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("homeUser.xhtml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case (2):
                cargarUsuario(gestorBD.cargarUsuario(usuarioIntento));
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("homeSuperUser.xhtml");
                } catch (IOException e) {
                    e.printStackTrace();
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

        pj = usuario.getPj();
        pg = usuario.getPg();
        //pe = usuario.getPe();
        pp = usuario.getPp();
        rj = usuario.getRj();
        rg = usuario.getRg();
        re = usuario.getRe();
        rp = usuario.getRp();
        por_partidas = usuario.getPor_partidas();
        por_rondas = usuario.getPor_rondas();
        lista_de_barajas_de_usuario = usuario.getLista_de_barajas_de_usuario();
        for (Baraja_de_usuario baraja : lista_de_barajas_de_usuario) {
            baraja.calculaTotales();
        }

    }

}
