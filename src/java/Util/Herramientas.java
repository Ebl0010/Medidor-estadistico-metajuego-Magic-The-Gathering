package Util;

import Controlador.MensajeManagedBean;
import javax.faces.context.FacesContext;

/**
 * Clase de apoyo con métodos estáticos que se llaman desde cualquier punto del código.
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class Herramientas {

    /**
     * Método tratar nombre que se llama cada vez que un usuario introduce su nombre para registrarse,
     * para acceder al sistema o introduce el nombre de una baraja. Recibe una String, lo pasa a un array
     * de char, convierte todos sus elementos (letras) en minúsculas y después la primera en mayúsculas.
     * Finalmente vuelve a transformar el array en un String y lo devuelve.
     * @param nombre_bruto String antes del "tratamiento".
     * @return cadena resultante de la transformación con la primera letra en mayúsculas.
     */
    public static String tratar_nombre(String nombre_bruto) {
        nombre_bruto = nombre_bruto.toLowerCase();
        char[] nombre_array = nombre_bruto.toCharArray();
        nombre_array[0] = Character.toUpperCase(nombre_array[0]);
        return String.valueOf(nombre_array);
    }

    /**
     * Método para lanzar los mensajes de error. Recibe el tipo de mensaje de la enumeración, una cadena
     * que contiene el mensaje que se muestra al usuario, y otra cadena que indica la página a la que debe
     * redirigirse el sistema cuando el usuario pulse en aceptar.
     * Nota: el código de la página "mensaje.xhtml" tiene un único cuadro donde muestra el mensaje y
     * un botón de validación.
     * @param tipo tipo del mensaje.
     * @param error mensaje detallado que se quiere mostrar al usuario.
     * @param retorno página donde se quiere volver cuando el usuario pulse el botón "aceptar".
     */
    public static void lanza_mensaje(TipoMensaje tipo, String error, String retorno) {
        FacesContext context = FacesContext.getCurrentInstance();
        MensajeManagedBean mensajeMB = context.getApplication().evaluateExpressionGet(
                context, "#{mensajeManagedBean}", MensajeManagedBean.class);
        mensajeMB.alert_mensaje(tipo, error, retorno);
    }

}
