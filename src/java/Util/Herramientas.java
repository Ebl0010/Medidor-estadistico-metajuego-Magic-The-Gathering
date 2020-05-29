/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Controlador.MensajeManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author admin
 */
public class Herramientas {

    public static String tratar_nombre(String nombre_bruto) {
        nombre_bruto = nombre_bruto.toLowerCase();
        char[] nombre_array = nombre_bruto.toCharArray();
        nombre_array[0] = Character.toUpperCase(nombre_array[0]);
        return String.valueOf(nombre_array);
    }

    public static void lanza_mensaje(TipoMensaje tipo, String error, String retorno) {
        FacesContext context = FacesContext.getCurrentInstance();
        MensajeManagedBean mensajeMB = context.getApplication().evaluateExpressionGet(
                context, "#{mensajeManagedBean}", MensajeManagedBean.class);
        mensajeMB.alert_mensaje(tipo, error, retorno);
    }

}
