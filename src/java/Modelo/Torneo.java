package Modelo;

/**
 * Clase del modelo utilizada para almacenar los datos de un torneo. Esta clase se utiliza para
 * acceder al historial de torneos de un usuario, rellenando una lista de objetos de esta clase.
 * 
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class Torneo {
    
    /**
     * Atributo que almacena el nombre del usuario que ha jugado el torneo
     */
    private String nombre_usuario;
    
    /**
     * Atributo que almacena el nombre de la baraja utilizada en el torneo
     */
    private String nombre_baraja;
    /**
     * Atributo que almacena los puntos conseguidos por ese usuario en ese torneo
     */
    private int puntos;
    
    /**
     * Atributo que almacena el string resultado del torneo. El resultado de un torneo se da en forma de cadena
     * donde primero aparece el número de rondas ganadas, después las empatadas y por último las perdidas. ASí un torneo
     * donde el resultado ha sido 3-1-0 significa 3 rondas ganads, 1 perdidas y 0 empatadas.
     */
    private String resultado;
    
    /**
     * Atributo que guarda el número de veces que se ha repetido un mismo caso (jugador J, baraja B, resultado R)
     */
    private int repeticiones;
    
    public Torneo(String usuario, String baraja, int puntos, String resultado, int repeticiones){
        nombre_usuario = usuario;
        nombre_baraja = baraja;
        this.puntos = puntos;
        this.resultado = resultado;
        this.repeticiones = repeticiones;
    }

    public Torneo(){
        
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public String getNombre_baraja() {
        return nombre_baraja;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getResultado() {
        return resultado;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public void setNombre_baraja(String nombre_baraja) {
        this.nombre_baraja = nombre_baraja;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }
    
    
    
}
