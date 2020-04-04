package Controlador;

import Modelo.Baraja;
import Modelo.Baraja_de_usuario;
import Modelo.GestorBD;
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

    private ArrayList<String> linea;
    private ArrayList<String> barajas_ordenadas;
    private ArrayList<ArrayList<ValorDelCuadro>> porcentajes;
    private GestorBD gestorBD;

    public CuadroManagedBean() throws SQLException {
        gestorBD = new GestorBD();
    }
    
    public ArrayList<String> getBarajas_ordenadas(){
        return barajas_ordenadas;
    }
    
    public ArrayList<String> getLinea(){
        return linea;
    }
    
    public ArrayList<ArrayList<ValorDelCuadro>> getPorcentajes(){
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
