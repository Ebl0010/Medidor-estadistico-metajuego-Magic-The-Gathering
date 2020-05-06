/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

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

}
