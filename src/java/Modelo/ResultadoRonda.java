package Modelo;

/**
 * Clase del modelo para manejar el resultado de una ronda. Contiene un string que referencia la baraja
 * del oponente y los 4 valores referentes a victorias y derrotas de main y side en cada caso.
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class ResultadoRonda {
    
    /**
     * Atributos enteros para las victorias de main y side de cada baraja.
     */
    private int ganadas_main, perdidas_main, ganadas_side, perdidas_side;
    
    /**
     * Atributo string para almacenar el nombre de la baraja oponente.
     */
    private String oponente;
    
    public ResultadoRonda(){
        
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

    public String getOponente() {
        return oponente;
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

    public void setOponente(String oponente) {
        this.oponente = oponente;
    }
    
    
    
}
