package Controller;

import Model.GestorBD;
import Model.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "registroManagedBean")
@RequestScoped
public class RegistroManagedBean {

    private String nombre;
    private String clave;
    private GestorBD gestorBD;
        

    public RegistroManagedBean() {
        gestorBD = new GestorBD();
    }

    public String getNombre() {
        return nombre;
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


    
    public void guardarUsuario() {
        Usuario usuarioNuevo = new Usuario(nombre, clave);
        if (gestorBD.guardarUsuario(usuarioNuevo) ){
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("index.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        
        }
    }
    
    

}
