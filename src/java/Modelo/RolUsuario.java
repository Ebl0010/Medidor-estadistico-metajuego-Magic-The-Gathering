package Modelo;

/**
 * Clase del modelo para almacenar el rol de un usuario. Se utiliza como un comodín para poder extraer de 
 * la base de datos la descripción del rol con un join y tenerlo guardado en una clase. También almacena
 * el estado, que se guarda en la base de datos como un entero, y lo traduce un String.
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class RolUsuario {
    
    /**
     * Atributo que guarda la descripción del rol
     */
    private String descripcion_rol;
    /**
     * Atributo que guarda el entero que vale el rol
     */
    private int idRol;
    /**
     * Atributo que guarda el nombre del usuario
     */
    private String nombre_usuario;
    /**
     * Atributo que guarda el estado del usuario con el rol 
     */
    private int estado;
    
    /**
     * Atributo que guarda la descrición del estado
     */
    private String s_estado;
    
    public RolUsuario(){
    }
    
    public RolUsuario(String descripcion_rol, int idRol, String nombre_usuario){
        this.descripcion_rol = descripcion_rol;
        this.idRol = idRol;
        this.nombre_usuario = nombre_usuario;
    }

    public String getDescripcion_rol() {
        return descripcion_rol;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setDescripcion_rol(String descripcion_rol) {
        this.descripcion_rol = descripcion_rol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public int getEstado() {
        return estado;
    }

    public String getS_estado() {
        return s_estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setS_estado(String s_estado) {
        this.s_estado = s_estado;
    }
    
    /**
     * Método que traduce el estado numérico a una cadena de texto más amigable para el usuario.
     */
    public void traducirEstado(){
        switch (estado){
            case(0): s_estado = "Concedido"; break;
            case(1): s_estado = "Solicitado"; break;
            case(2): s_estado = "Denegado"; break;           
        }
    }
    
                
}
