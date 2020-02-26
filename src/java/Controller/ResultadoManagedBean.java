package Controller;

import Model.Baraja;
import Model.Baraja_de_usuario;
import Model.GestorBD;
import Model.ResultadoRonda;
import Model.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "resultadoManagedBean")
@SessionScoped
public class ResultadoManagedBean {

    private String baraja1, baraja2, usuario;
    private int main1, main2, side1, side2, rondas_ganadas, rondas_perdidas, rondas_empatadas, num_rondas;
    private GestorBD gestorBD;
    private ArrayList<ResultadoRonda> resultadosRondas;

    public ResultadoManagedBean() throws SQLException {
        gestorBD = new GestorBD();
    }

    private void puestaCero() {
        baraja1 = null;
        baraja2 = null;
        main1 = 0;
        main2 = 0;
        side1 = 0;
        side2 = 0;
        rondas_ganadas = 0;
        rondas_perdidas = 0;
        rondas_empatadas = 0;
        num_rondas = 0;
    }

    public String getBaraja1() {
        return baraja1;
    }

    public String getBaraja2() {
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

    public int getNum_rondas() {
        return num_rondas;
    }

    public void setNum_rondas(int num_rondas) {
        this.num_rondas = num_rondas;
    }

    public void setBaraja1(String baraja1) {
        this.baraja1 = baraja1;
    }

    public void setBaraja2(String baraja2) {
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
    
    public ArrayList<ResultadoRonda> getResultadosRonda(){
        return resultadosRondas;
    }

    public void introducirResultado() throws SQLException {
        // el CT de 2 string devuelve -1 en a.CT(b)
        // entonces si baraja1.compareTo(baraja2) devuelve 1 es que baraja2 va ANTES.
        //si es 1 es porque b.ct(a), --> b2 va ANTES que b1 hay que invertirlas

        boolean control;
        if (baraja1.compareTo(baraja2) < 1) {
            control = gestorBD.introducirResultado(baraja1, baraja2, main1, main2, side1, side2);
        } else {
            control = gestorBD.introducirResultado(baraja2, baraja1, main1, main2, side1, side2);
        }
        puestaCero();
        if (control == true) {
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("resultado_introducido.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("resultado_no_introducido.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void introducirPrevioTorneo(String usuario) throws SQLException {
        usuario = usuario;
        if (gestorBD.usuario_tiene_baraja(usuario, baraja1)) {
            rondas_ganadas = 0;
            rondas_perdidas = 0;
            rondas_empatadas = 0;
            resultadosRondas = new ArrayList<ResultadoRonda>(num_rondas);
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("introducir_torneo_2.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            baraja1 = null;
            num_rondas = 0;
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("resultado_no_introducido.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void introducirResultadoTorneo() throws SQLException {
        ResultadoRonda ronda = new ResultadoRonda();
        while (resultadosRondas.size() < num_rondas) {

            if (gestorBD.existeBaraja(baraja2)) {
                ronda.setGanadas_main(main1);
                ronda.setPerdidas_main(main2);
                ronda.setGanadas_side(side1);
                ronda.setPerdidas_side(side2);
         
                if (main1 + side1 > main2 + side2) {
                    rondas_ganadas++;
                } else {
                    if (main1 + side1 < main2 + side2) {
                        rondas_perdidas++;
                    } else {
                        rondas_empatadas++;
                    }
                }
                ronda.setOponente(baraja2);
                resultadosRondas.add(ronda);


                main1 = 0;
                main2 = 0;
                side1 = 0;
                side2 = 0;
                baraja2 = null;
            }
            // solo se hacen las puestas a 0 e incrementos si la baraja2 existe, y si no existe
            // al recargar la pagina se ven los valores introducidos 
            
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("introducir_torneo_2.xhtml");

            } catch (IOException e) {
                e.printStackTrace();
            }

        } // termina el while ahora tengo que redirigir a la pagina de validacion del torneo
        puestaCero();
        try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("validar_torneo.xhtml");

            } catch (IOException e) {
                e.printStackTrace();
            }   
    }

}
