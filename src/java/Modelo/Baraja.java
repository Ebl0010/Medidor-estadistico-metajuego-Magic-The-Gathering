package Modelo;

/**
 * Clase Baraja del modelo que guarda los atributos de una baraja: nombre, tier y ratios de victoria porcentuales.
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class Baraja {
    
    /**
     * atributo que guarda el nombre de la baraja
     */
    private String nombre;
    /**
     * Atributo que guarda el tier de la baraja
     */
    private int tier;
    /**
     * Atributos porcentuales para los ratios de victoria
     */
    float porcentaje_main, porcentaje_side, porcentaje_total;
    
    public Baraja(){}
    
    public Baraja(String nombre, int tier){
        this.nombre = nombre;
        this.tier = tier;
    }
    
    public Baraja (String nombre){
        this.nombre = nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public void setTier(int tier){
        this.tier = tier;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public int getTier(){
        return tier;
    }
    
    public float getPorcentaje_main(){
        return porcentaje_main;
    }
    
    public void setPorcentaje_main(float porcentaje_main){
        this.porcentaje_main = porcentaje_main;
    }
    
    public float getPorcentaje_side(){
        return porcentaje_side;
    }
    
    public void setPorcentaje_side(float porcentaje_side){
        this.porcentaje_side = porcentaje_side;
    }
    
    public float getPorcentaje_total(){
        return porcentaje_total;
    }
    
    public void setPorcentaje_total(float porcentaje_total){
        this.porcentaje_total = porcentaje_total;
    }
    
}
