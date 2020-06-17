package Modelo;

/**
 * Clase del modelo para almacenar los datos de un cruce. Un cruce representa un emparejamiento
 * entre dos barajas, y tiene todos los atributos que corresponden a medidas brutas y ratios porcentuales.
 * (Aunque el cruce involucra a dos barajas, los objetos Cruce solo almacenen la segunda baraja porque
 * la primera viene referenciada en el contexto donde se utilizan estos objetos y no era necesario
 * añadirla en esta clase).
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class Cruce {
    
    /**
     * Atributo que almacena el nombre de la baraja oponente.
     */
    private String baraja2;
    /**
     * Atributos enteros que guardan los valores estadísticos brutos del cruce (victorias de cada una).
     */
    private int ganadas_main, perdidas_main, ganadas_side, perdidas_side;
    
    /**
     * Atributos decimales que guardan los valores de los ratios porcentuales.
     */
    private float porcentaje_main, porcentaje_side, porcentaje_total;
    
    public Cruce(){
        
    }

    public String getBaraja2() {
        return baraja2;
    }

    public int getGanadas_main() {
        return ganadas_main;
    }

    public int getPerdidas_main() {
        return perdidas_main;
    }

    public int getGanadas_side() {
        return ganadas_side;
    }

    public int getPerdidas_side() {
        return perdidas_side;
    }

    public float getPorcentaje_main() {
        return porcentaje_main;
    }

    public float getPorcentaje_side() {
        return porcentaje_side;
    }

    public float getPorcentaje_total() {
        return porcentaje_total;
    }

    public void setBaraja2(String baraja2) {
        this.baraja2 = baraja2;
    }

    public void setGanadas_main(int ganadas_main) {
        this.ganadas_main = ganadas_main;
    }

    public void setPerdidas_main(int perdidas_main) {
        this.perdidas_main = perdidas_main;
    }

    public void setGanadas_side(int ganadas_side) {
        this.ganadas_side = ganadas_side;
    }

    public void setPerdidas_side(int perdidas_side) {
        this.perdidas_side = perdidas_side;
    }

    public void setPorcentaje_main(float porcentaje_main) {
        this.porcentaje_main = porcentaje_main;
    }

    public void setPorcentaje_side(float porcentaje_side) {
        this.porcentaje_side = porcentaje_side;
    }

    public void setPorcentaje_total(float porcentaje_total) {
        this.porcentaje_total = porcentaje_total;
    }
    
    
    
}
