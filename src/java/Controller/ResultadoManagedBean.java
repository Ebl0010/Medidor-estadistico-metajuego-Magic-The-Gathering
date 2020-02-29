package Controller;

import Model.Baraja;
import Model.Baraja_de_usuario;
import Model.GestorBD;
import Model.ResultadoRonda;
import Model.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "resultadoManagedBean")
@SessionScoped
public class ResultadoManagedBean {

    private String baraja1, baraja2, usuario, resultado_torneo;
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
    
    public String getResultado_torneo(){
        return resultado_torneo;
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
        if (control) {
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
        this.usuario = usuario;
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
        resultado_torneo = "";
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
        try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("validar_torneo.xhtml");

            } catch (IOException e) {
                e.printStackTrace();
            }   
    }
    
    
    public void guardar_torneo() throws SQLException{
       
        /* en cada elemento del arraylist resultadosRondas tengo partidas ganadas y perdidas,
            puedo iterar sobre este arraylis llamando al metodo que trabaja contra
            la base de datos, guardando en cada iteracion lo que sea necesario,
            ya que hay que actualizar el cuadro cruces, y esto hay que hacerlo con un
            update por cada elemento del arraylist (que corresponde a cada ronda del torneo).
        */ 
        
        int main_torneo1 = 0;
        int main_torneo2 = 0;
        int side_torneo1 = 0;
        int side_torneo2 = 0;
        int rondas_ganadas = 0;
        int rondas_perdidas = 0;
        int rondas_empatadas = 0;
        ResultadoRonda resultado;
        
        boolean control = true;
        Iterator<ResultadoRonda> it = resultadosRondas.iterator();
        
        /* Este bucle va a ir recorriendo todos los resultadosRonda en el arraylist, que son todas las
        rondas individuales. Cada ronda engloba resultados para un par de barajas, que deben entrar a la tabla
        de cruces de forma individual, pero, de forma global, se contabilizan todas las victorias y derrotas de
        partidas y de rondas para el usuario y para las barajas de usuario, y como en ambas tablas no importa
        la baraja oponente, en el bucle se iran extrayendo de cada iteracion sobre el arraylist y se ira
        llamando a meter resultado, y luego se irán sumando para, cuando termine el bucle, añadirlas con
        una sola operacion update a la base de datos sobre cada tabla, reduciendo el numero de operaciones
        sobre la base de datos
         */
        while (it.hasNext() && control){
            resultado = it.next();
            int main1 = resultado.getGanadas_main();
            int main2 = resultado.getPerdidas_main();
            int side1 = resultado.getGanadas_side();
            int side2 = resultado.getPerdidas_side();
            String baraja2 = resultado.getOponente();
            
            control = gestorBD.introducirResultado(baraja1, baraja2, main1, main2, side1, side2);
            
            main_torneo1 = main_torneo1 + main1;
            main_torneo2 = main_torneo2 + main2;
            side_torneo1 = side_torneo1 + side1;
            side_torneo2 = side_torneo2 + side2;
            
            if (main1 + side1 > main2 + side2){
                rondas_ganadas ++;
            } else {
                if (main1 + side1 < main2 + side2){
                    rondas_perdidas ++;
                } else {
                    rondas_empatadas ++;
                }
            }
       
        } // cierra el while
        baraja2 = null;
        main1 = 0;
        main2 = 0;
        side1 = 0;
        side2 = 0;
        /*
        en este punto se ha recorrido entero el arraylist de resultados, se han introducido en la tabla cruces
        todos los resultados y se tienen los valores totales de partidas y rondas ganadas, perdidas y empatadas
        a lo largo del torneo, que se deberan introducir con una sola operacion en cada tabla, con un solo metodo
        cuando el it.hasNext devuelva false, control deberia ser true si todo se ha introducido bien
        */
        
        if (control){
            control = gestorBD.guardar_torneo(usuario, baraja1, main_torneo1, main_torneo2, 
                    side_torneo1, side_torneo2, rondas_ganadas, rondas_perdidas, rondas_empatadas);
            
        }
            
                   
        if (control) {
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

}
