package Controlador;

import Util.TipoMensaje;
import java.io.IOException;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "mensajeManagedBean")
@SessionScoped
public class MensajeManagedBean {

    TipoMensaje tipoMensaje;
    String mensaje;
    String retorno;
    

    public MensajeManagedBean() {
    }

    public TipoMensaje getTipoMensaje(){
        return tipoMensaje;
    }
    
    public String getMensaje() {
        return mensaje;
    }

    public String getRetorno() {
        return retorno;
    }

     
    public void alert_mensaje(TipoMensaje tipoMensaje, String mensaje, String retorno) {
        
        this.tipoMensaje = tipoMensaje;
        this.mensaje = mensaje;
        this.retorno = retorno;
        
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("mensaje.xhtml");
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
        }
    }

    public void volver() throws SQLException {
        if (retorno.equals("login")) {
            FacesContext context = FacesContext.getCurrentInstance();
            FormularioManagedBean formularioMB = context.getApplication().evaluateExpressionGet(
                    context, "#{formularioManagedBean}", FormularioManagedBean.class);
            formularioMB.login();
        } else {
            try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect(retorno);
                } catch (IOException e) {
                    //e.printStackTrace();
                }
        }
    }
}
