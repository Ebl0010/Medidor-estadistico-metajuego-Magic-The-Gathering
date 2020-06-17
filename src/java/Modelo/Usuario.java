package Modelo;

import java.util.ArrayList;

/**
 * Clase del modelo para almacenar los datos de un usuario. Los objetos de esta clase se utilizan cuando
 * el usuario hace login para cargar todos sus datos desde la base de datos y mostrarlos en las vistas
 * homeUser.xhtml y homeSuperUser.xhtml.
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class Usuario {

    
    /**
     * Atributos que almacenan el nombre la contraseña, el correo y la descripción del rol del usuario.
     */
    private String nombre, clave, rol, correo;
    /**
     * Atributos enteros que referencian a los datos brutos de partidas y rondas ganadas de ese usuario.
     */
    private int rondas_ganadas,
                rondas_perdidas,
                rondas_empatadas,
                partidas_ganadas,
                partidas_perdidas;
    
    /**
     * Atributos decimales que guardan los ratios de victorias porcentuales del usuario.
     */
    private float porcentaje_rondas,
                  porcentaje_partidas;
            
    /**
     * Lista de barajas de usuario que almacena los resultados del usuario con cada una de las
     * barajas que tiene asignadas.
     */
    private ArrayList<Baraja_de_usuario> lista_de_barajas_de_usuario;
    
   
    
    public Usuario(){
        lista_de_barajas_de_usuario = new ArrayList<>();
    }
    
    public Usuario(String n){
        nombre = n;
        lista_de_barajas_de_usuario = new ArrayList<>();
    }
    
    public Usuario(String nombre, String clave){
        this.nombre = nombre;
        this.clave = clave;
    }
    
    public Usuario(String nombre, String clave, String correo){
        this.nombre = nombre;
        this.clave = clave;
        this.correo = correo;
        lista_de_barajas_de_usuario = new ArrayList<>();
    }
    
    
    public String getNombre() {
        return nombre;
    }

    public String getClave() {
        return clave;
    }
    
    public String getCorreo(){
        return correo;
    }
    
    public String getRol(){
        return rol;
    }
    
    public void setRol(String rol){
        this.rol = rol;
    }

    public int getRondas_ganadas() {
        return rondas_ganadas;
    }

    public int getRondas_perdidas() {
        return rondas_perdidas;
    }

    public int getRondas_empatadas() {
        return rondas_empatadas;
    }

    public int getPartidas_ganadas() {
        return partidas_ganadas;
    }

    public int getPartidas_perdidas() {
        return partidas_perdidas;
    }

    public float getPorcentaje_rondas() {
        return porcentaje_rondas;
    }

    public float getPorcentaje_partidas() {
        return porcentaje_partidas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setRondas_ganadas(int rondas_ganadas) {
        this.rondas_ganadas = rondas_ganadas;
    }

    public void setRondas_perdidas(int rondas_perdidas) {
        this.rondas_perdidas = rondas_perdidas;
    }

    public void setRondas_empatadas(int rondas_empatadas) {
        this.rondas_empatadas = rondas_empatadas;
    }

    public void setPartidas_ganadas(int partidas_ganadas) {
        this.partidas_ganadas = partidas_ganadas;
    }

    public void setPartidas_perdidas(int partidas_perdidas) {
        this.partidas_perdidas = partidas_perdidas;
    }

    public void setPorcentaje_rondas(float porcentaje_rondas) {
        this.porcentaje_rondas = porcentaje_rondas;
    }

    public void setPorcentaje_partidas(float porcentaje_partidas) {
        this.porcentaje_partidas = porcentaje_partidas;
    }
    
    
    public ArrayList<Baraja_de_usuario> getLista_de_barajas_de_usuario(){
        return lista_de_barajas_de_usuario;
    }
    
    public void setLista_de_barajas_de_usuario(ArrayList<Baraja_de_usuario> barajas){
        lista_de_barajas_de_usuario = barajas;
    }
    
        
    
}
