/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author admin
 */
public class Resultado {
    
    private String baraja1, baraja2;
    
    private int main1, main2, side1, side2;

    private float porcentaje_main, porcentaje_side, porcentaje_total;
    
    public Resultado(){
    }
    
    public String getBaraja1(){
        return baraja1;
    }
    
    public String getBaraja2(){
        return baraja2;
    }

    public int getMain1() {
        return main1;
    }

    public int getMain2() {
        return main2;
    }

    public int getSide1() {
        return side1;
    }

    public int getSide2() {
        return side2;
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
    
    public void setBaraja1(String baraja1){
        this.baraja1 = baraja1;
    }
    
    public void setBaraja2(String baraja2){
        this.baraja2 = baraja2;
    }

    public void setMain1(int main1) {
        this.main1 = main1;
    }

    public void setMain2(int main2) {
        this.main2 = main2;
    }

    public void setSide1(int side1) {
        this.side1 = side1;
    }

    public void setSide2(int side2) {
        this.side2 = side2;
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
    
    public void introducir_datos(int main1N, int main2N, int side1N, int side2N){
        main1 = main1 + main1N;
        main2 = main2 + main2N;
        side1 = side1 + side1N;
        side2 = side2 + side2N;
    }
    
    public void calcular_porcentajes() {
        if (main1 == 0) {
            porcentaje_main = 0;
        } else {
            if (main2 == 0) {
                porcentaje_main = 99;
            } else {
                porcentaje_main = main1 * 100 / (main1 + main2);
            }
        }

        if (side1 == 0) {
            porcentaje_side = 0;
        } else {
            if (side2 == 0) {
                porcentaje_side = 99;
            } else {
                porcentaje_side = side1 * 100 / (side1 + side2);
            }
        }

        if (main1 + side1 == 0) {
            porcentaje_total = 0;
        } else {
            if (main2 + side2 == 0) {
                porcentaje_total = 99;
            } else {
                porcentaje_total = (main1 + side1) * 100 / (main1 + main2 + side1 + side2);
            }
        }
    }
    
}
