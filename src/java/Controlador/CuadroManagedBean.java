package Controlador;

import Modelo.Baraja;
import Modelo.Baraja_de_usuario;
import Modelo.GestorBD;
import Modelo.LineaDelCuadro;
import Modelo.Usuario;
import Modelo.ValorDelCuadro;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "cuadroManagedBean")
@SessionScoped
public class CuadroManagedBean {

    private ArrayList<String> barajas_ordenadas;
    private ArrayList<LineaDelCuadro> porcentajes;
    private GestorBD gestorBD;

    public CuadroManagedBean() throws SQLException {
        gestorBD = new GestorBD();
    }
    
    public ArrayList<String> getBarajas_ordenadas(){
        return barajas_ordenadas;
    }
    
    
    public ArrayList<LineaDelCuadro> getPorcentajes(){
        return porcentajes;
    }
    
    
    public void cargaCuadro(){
        try {
            barajas_ordenadas = gestorBD.carga_todas_las_barajas();
            //linea = gestorBD.cargaLinea("jund", barajas_ordenadas);
            porcentajes = gestorBD.cargaCuadro (barajas_ordenadas);
            
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("cuadro.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        } catch (SQLException ex) {
            // cosas del logger
        } 
    }

}
