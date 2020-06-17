package Modelo;

/**
 * Clase del modelo para guardar los datos de una baraja asociada a un usuario. 
 * 
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class Baraja_de_usuario {
    
    /**
     * Atributo que guarda el nombre de la baraja
     */
    private String nombre_baraja;
    
    /**
     * Atributos enteros que guardan los valores de rondas y partidas ganadas y perdidas
     */
    private int rondas_ganadas,
                rondas_perdidas,
                rondas_empatadas,
                partidas_ganadas_main,
                partidas_perdidas_main,
                partidas_ganadas_side,
                partidas_perdidas_side,
                partidas_ganadas_total,
                partidas_jugadas_total,
                rondas_ganadas_total;
                
    /**
     * Atributos decimales que guardan los ratios de victorias porcentuales
     */
    private float porcentaje_partidas_ganadas_main,
                  porcentaje_partidas_ganadas_side,
                  porcentaje_partidas_ganadas_total,
                  porcentaje_rondas_ganadas;
    
    public Baraja_de_usuario(){ }

    public String getNombre_baraja() {
        return nombre_baraja;
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

    public int getPartidas_ganadas_main() {
        return partidas_ganadas_main;
    }

    public int getPartidas_perdidas_main() {
        return partidas_perdidas_main;
    }

    public int getPartidas_ganadas_side() {
        return partidas_ganadas_side;
    }

    public int getPartidas_perdidas_side() {
        return partidas_perdidas_side;
    }

    public int getPartidas_ganadas_total() {
        return partidas_ganadas_total;
    }

    public int getPartidas_jugadas_total() {
        return partidas_jugadas_total;
    }

    public int getRondas_ganadas_total() {
        return rondas_ganadas_total;
    }

    public float getPorcentaje_partidas_ganadas_main() {
        return porcentaje_partidas_ganadas_main;
    }

    public float getPorcentaje_partidas_ganadas_side() {
        return porcentaje_partidas_ganadas_side;
    }

    public float getPorcentaje_partidas_ganadas_total() {
        return porcentaje_partidas_ganadas_total;
    }

    public float getPorcentaje_rondas_ganadas() {
        return porcentaje_rondas_ganadas;
    }

    public void setNombre_baraja(String nombre_baraja) {
        this.nombre_baraja = nombre_baraja;
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

    public void setPartidas_ganadas_main(int partidas_ganadas_main) {
        this.partidas_ganadas_main = partidas_ganadas_main;
    }

    public void setPartidas_perdidas_main(int partidas_perdidas_main) {
        this.partidas_perdidas_main = partidas_perdidas_main;
    }

    public void setPartidas_ganadas_side(int partidas_ganadas_side) {
        this.partidas_ganadas_side = partidas_ganadas_side;
    }

    public void setPartidas_perdidas_side(int partidas_perdidas_side) {
        this.partidas_perdidas_side = partidas_perdidas_side;
    }

    public void setPartidas_ganadas_total(int partidas_ganadas_total) {
        this.partidas_ganadas_total = partidas_ganadas_total;
    }

    public void setPartidas_jugadas_total(int partidas_jugadas_total) {
        this.partidas_jugadas_total = partidas_jugadas_total;
    }

    public void setRondas_ganadas_total(int rondas_ganadas_total) {
        this.rondas_ganadas_total = rondas_ganadas_total;
    }

    public void setPorcentaje_partidas_ganadas_main(float porcentaje_partidas_ganadas_main) {
        this.porcentaje_partidas_ganadas_main = porcentaje_partidas_ganadas_main;
    }

    public void setPorcentaje_partidas_ganadas_side(float porcentaje_partidas_ganadas_side) {
        this.porcentaje_partidas_ganadas_side = porcentaje_partidas_ganadas_side;
    }

    public void setPorcentaje_partidas_ganadas_total(float porcentaje_partidas_ganadas_total) {
        this.porcentaje_partidas_ganadas_total = porcentaje_partidas_ganadas_total;
    }

    public void setPorcentaje_rondas_ganadas(float porcentaje_rondas_ganadas) {
        this.porcentaje_rondas_ganadas = porcentaje_rondas_ganadas;
    }
    
    /**
     * Método que calcula los ratios de victorias en base a los datos enteros. Este método se utilizará
     * cuando se modifiquen los valores enteros para actualizar automáticamente los ratios.
     */
    public void recalcula_porcentajes(){
       
        porcentaje_partidas_ganadas_main = partidas_ganadas_main * 100 / (partidas_ganadas_main + partidas_perdidas_main);
        porcentaje_partidas_ganadas_side = partidas_ganadas_side * 100 / (partidas_ganadas_side + partidas_perdidas_side);
        porcentaje_partidas_ganadas_total = partidas_ganadas_total + 100 / (partidas_ganadas_total + partidas_perdidas_side + partidas_perdidas_main);
        porcentaje_rondas_ganadas = rondas_ganadas * 100 / rondas_ganadas + rondas_empatadas + rondas_perdidas;
    }
    
}
