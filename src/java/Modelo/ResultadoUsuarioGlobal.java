package Modelo;

/**
 * Clase del modelo para almacenar los datos globales de un usuario. Esta clase se utiliza cuando se
 * introducen los datos de un torneo, guardando en ella los datos del usuario que ha jugado el torneo
 * de la base de datos, actualizándolos con los datos del nuevo torneo, y devolviendo todo el contenido
 * actualizado a la base de datos.
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class ResultadoUsuarioGlobal {
    
    /**
     * Atributo entero que almacena el nombre del usuario
     */
    private String nombre_usuario;
    /**
     * Atributos enteros que almacenan los valores brutos de partidas y rondas ganadas y perdidas
     */
    private int partidas_ganadas, partidas_perdidas, rondas_ganadas, rondas_perdidas, rondas_empatadas;
    
    /**
     * Atributos decimales que guardan los ratios de victoria porcentuales.
     */
    private float porcentaje_rondas, porcentaje_partidas;
    
    public ResultadoUsuarioGlobal(){
        
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public int getPartidas_ganadas() {
        return partidas_ganadas;
    }

    public int getPartidas_perdidas() {
        return partidas_perdidas;
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

    public float getPorcentaje_rondas() {
        return porcentaje_rondas;
    }

    public float getPorcentaje_partidas() {
        return porcentaje_partidas;
    }

    public void setPartidas_ganadas(int partidas_ganadas) {
        this.partidas_ganadas = partidas_ganadas;
    }

    public void setPartidas_perdidas(int partidas_perdidas) {
        this.partidas_perdidas = partidas_perdidas;
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
    
    /**
     * Método que actualiza los valores de los atributos incrementándolos con los valores de los parámetros.
     * @param par_ganadas incremento de las partidas totales ganadas.
     * @param par_perdidas incremento de las partidas totales perdidas.
     * @param ron_ganadas incremento de las rondas totales ganadas.
     * @param ron_perdidas incremento de las rondas totales perdidas.
     * @param ron_empatadas incremento de las rondas totales empatadas.
     */
    public void introducir_resultados(int par_ganadas, int par_perdidas, int ron_ganadas, int ron_perdidas, int ron_empatadas){
        partidas_ganadas = partidas_ganadas + par_ganadas;
        partidas_perdidas = partidas_perdidas + par_perdidas;
        rondas_ganadas = rondas_ganadas + ron_ganadas;
        rondas_empatadas = rondas_empatadas + ron_empatadas;
        rondas_perdidas = rondas_perdidas + ron_perdidas;
    }
    
    /**
     * Método que recalcula los porcentajes, utilizado a continuación del anterior, que devuelve
     * manualmente un 0 y un 99, evitando una posible divisón por, o entre 0, y un 100 que
     * requeriría una tercera cifra en la base de datos para un número ínfimo de casos.
     */
    public void calcular_porcentajes(){
        if (partidas_ganadas == 0){
            porcentaje_partidas = 0;
        } else {
            if (partidas_perdidas == 0){
                porcentaje_partidas = 99;
            } else {
                porcentaje_partidas = partidas_ganadas * 100 / (partidas_ganadas + partidas_perdidas);
            }
        }
        
        if (rondas_ganadas == 0){
            porcentaje_rondas = 0;
        } else {
            if (rondas_perdidas == 0 && rondas_empatadas == 0){
                porcentaje_rondas = 99; 
            } else {
                porcentaje_rondas = rondas_ganadas + 100 / (rondas_ganadas + rondas_empatadas + rondas_perdidas);
            }
        }
    }
    
    
    
}
