package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "mensajeManagedBean")
@SessionScoped
public class MensajeManagedBean {

    private String tipo;
    private String mensaje;
    private boolean login;

    public MensajeManagedBean() {
    }

    public String getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void alert_mensaje(String tipo, String mensaje, Boolean login) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.login = login;
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
        if (login) {
            FacesContext context = FacesContext.getCurrentInstance();
            FormularioManagedBean formularioMB = context.getApplication().evaluateExpressionGet(
                    context, "#{formularioManagedBean}", FormularioManagedBean.class);
            formularioMB.login();
        } else {
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
