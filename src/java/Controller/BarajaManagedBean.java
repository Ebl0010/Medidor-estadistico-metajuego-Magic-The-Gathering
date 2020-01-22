package Controller;

import Model.Baraja;
import Model.Baraja_de_usuario;
import Model.GestorBD;
import Model.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "barajaManagedBean")
@SessionScoped
public class BarajaManagedBean {

    private String nombre, modificar, vieja;
    private int tier;
    private ArrayList<Baraja> barajas;
    private GestorBD gestorBD;

    public BarajaManagedBean() throws SQLException {
        gestorBD = new GestorBD();
        barajas = gestorBD.leeBarajas();
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
    
    public void setVieja(String vieja){
        this.vieja = vieja;
    }
    
    public String getVieja(){
        return vieja;
    }

    public void guardarBaraja() throws SQLException {
        Baraja barajaNueva = new Baraja(nombre, tier);
        gestorBD.guardarBaraja(barajaNueva);
        barajas = gestorBD.leeBarajas();
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("barajas.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modificar() throws SQLException {
        Baraja datos_nuevos = gestorBD.lee_baraja_por_nombre(modificar);
        if (!datos_nuevos.getNombre().equals("no hay")) {
            nombre = datos_nuevos.getNombre();
            tier = datos_nuevos.getTier();
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("modificar_baraja.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("barajas.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }     
    }
    
    public void actualizarBaraja() throws SQLException{
        // como el managed bean es una variable de sesion aun no he tocado "modificar", que tiene
        // los datos de la baraja vieja. Asi que aqui es donde la pongo a nulo
        // en este punto los valores nomre y tier son los valores a los que quiero actualizar
        
        if (gestorBD.actualizarBaraja(modificar, nombre, tier)){
            modificar = null;
            barajas = gestorBD.leeBarajas();
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("barajas.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        } else {
            nombre = null;
            tier = 0;
            
        } try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("modificar_baraja.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
