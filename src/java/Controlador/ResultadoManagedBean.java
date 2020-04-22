package Controlador;

import Modelo.GestorBD;
import Modelo.ResultadoRonda;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "resultadoManagedBean")
@SessionScoped
public class ResultadoManagedBean {

    private String baraja1, baraja2, usuario, resultado_torneo, encabezado;
    private int main1, main2, side1, side2, num_rondas;
    private final GestorBD gestorBD;
    private ArrayList<String> todas_las_barajas, barajas_de_usuario;
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
        num_rondas = 0;
    }

    private void carga_todas_las_barajas() throws SQLException {
        todas_las_barajas = gestorBD.lee_nombres_barajas();
    }

    private void carga_nombres_barajas_de_usuario(String nombre_usuario) throws SQLException {
        barajas_de_usuario = gestorBD.devolver_nombres_barajas_de_usuario(usuario);
    }

    public ArrayList<String> getTodas_las_barajas() {
        return todas_las_barajas;
    }

    public ArrayList<String> getBarajas_de_usuario() {
        return barajas_de_usuario;
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

    public ArrayList<ResultadoRonda> getResultadosRonda() {
        return resultadosRondas;
    }

    public String getResultado_torneo() {
        return resultado_torneo;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public void carga_pagina_introducir_resultado() throws SQLException {
        // poner los datos al 0 al cargar la pagina por si se entra sin haber terminado otra insercion
        baraja1 = null;
        baraja2 = null;
        main1 = 0;
        main2 = 0;
        side1 = 0;
        side2 = 0;
        carga_todas_las_barajas();
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("introducir_resultado.xhtml");
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void introducirResultado() throws SQLException {
        // el CT de 2 string devuelve -1 en a.CT(b)
        // entonces si baraja1.compareTo(baraja2) devuelve 1 es que baraja2 va ANTES.
        //si es 1 es porque b.ct(a), --> b2 va ANTES que b1 hay que invertirlas

        if (baraja1.equals(baraja2)) {
            puestaCero();
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("introducir_resultado.xhtml");
            } catch (IOException e) {
                //e.printStackTrace();
            }
        } else {
            boolean control;
            // mando las barajas ordenadas pero si invierto el orden hay que invertirlo en los resultados
            if (baraja1.compareTo(baraja2) < 1) {
                control = gestorBD.introducirResultado(baraja1, baraja2, main1, main2, side1, side2);
            } else {
                control = gestorBD.introducirResultado(baraja2, baraja1, main2, main1, side2, side1);
            }
            puestaCero();
            if (control) {
                try {
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("resultado_introducido.xhtml");
                } catch (IOException e) {
                    //e.printStackTrace();
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

    public void introducirPrevioTorneo(String usuario) throws SQLException {
        this.usuario = usuario;
        num_rondas = 0;
        baraja1 = null;
        carga_nombres_barajas_de_usuario(usuario);
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("intoducir_torneo_1.xhtml");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void introducirTorneo1() throws SQLException {
        encabezado = "ronda 1";
        carga_todas_las_barajas();
        resultadosRondas = new ArrayList<>();
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("introducir_torneo_2.xhtml");
        } catch (IOException e) {
            baraja1 = null;
            num_rondas = 0;
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("resultado_no_introducido.xhtml");
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }

    }

    public void introducirResultadoTorneo() throws SQLException {
        ResultadoRonda ronda;

        /*
         si pongo num_rondas -1 no pide los datos en "la ronda extra" pero no guarda los datos
        de la ultima ronda. 
         */
        if (num_rondas > 0) {

            ronda = new ResultadoRonda();

            ronda.setGanadas_main(main1);
            ronda.setPerdidas_main(main2);
            ronda.setGanadas_side(side1);
            ronda.setPerdidas_side(side2);

            ronda.setOponente(baraja2);
            resultadosRondas.add(ronda);

            main1 = 0;
            main2 = 0;
            side1 = 0;
            side2 = 0;
            baraja2 = null;

            int val = resultadosRondas.size() + 1;
            encabezado = "Ronda: " + val;
            num_rondas--;
            // al recargar la pagina se ven los valores introducidos 

            //reduzco las rondas y si es 0 me voy a la pagina de validar, sino vuelvo a la misma
            //pagina, habiendo actualizado encabezado y num_rondas
        }
        if (num_rondas == 0) {
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("validar_torneo.xhtml");

            } catch (IOException e) {

            }
        } else {
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("introducir_torneo_2.xhtml");

            } catch (IOException e) {

            }
        }

    }

    public void guardar_torneo() throws SQLException {

        // en cada elemento del arraylist resultadosRondas tengo partidas ganadas y perdidas,
        // puedo iterar sobre este arraylis llamando al metodo que trabaja contra
        // la base de datos, guardando en cada iteracion lo que sea necesario,
        //  ya que hay que actualizar el cuadro cruces, y esto hay que hacerlo con un
        // update por cada elemento del arraylist (que corresponde a cada ronda del torneo).
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

        // Este bucle va a ir recorriendo todos los resultadosRonda en el arraylist, que son todas las
        // rondas individuales. Cada ronda engloba resultados para un par de barajas, que deben entrar a la tabla
        // de cruces de forma individual, pero, de forma global, se contabilizan todas las victorias y derrotas de
        // partidas y de rondas para el usuario y para las barajas de usuario, y como en ambas tablas no importa
        // la baraja oponente, en el bucle se iran extrayendo de cada iteracion sobre el arraylist y se ira
        // llamando a meter resultado, y luego se irán sumando para, cuando termine el bucle, añadirlas con
        // una sola operacion update a la base de datos sobre cada tabla, reduciendo el numero de operaciones
        // sobre la base de datos
        while (it.hasNext() && control) {
            resultado = it.next();
            int main1 = resultado.getGanadas_main();
            int main2 = resultado.getPerdidas_main();
            int side1 = resultado.getGanadas_side();
            int side2 = resultado.getPerdidas_side();
            String baraja2 = resultado.getOponente();

            // solo computo el resultado del emparejamiento si las barajas son distintas
            // se las paso ordenadas a introducirResultado
            if (!baraja1.equals(baraja2)) {

                if (baraja1.compareTo(baraja2) < 1) {
                    control = gestorBD.introducirResultado(baraja1, baraja2, main1, main2, side1, side2);
                } else {
                    control = gestorBD.introducirResultado(baraja2, baraja1, main1, main2, side1, side2);
                }
            } else {
                control = true;
            }

            if (control) {

                main_torneo1 = main_torneo1 + main1;
                main_torneo2 = main_torneo2 + main2;
                side_torneo1 = side_torneo1 + side1;
                side_torneo2 = side_torneo2 + side2;

                if (main1 + side1 > main2 + side2) {
                    rondas_ganadas++;
                } else {
                    if (main1 + side1 < main2 + side2) {
                        rondas_perdidas++;
                    } else {
                        rondas_empatadas++;
                    }
                }
            }

        } // cierra el while

        // en este punto se ha recorrido entero el arraylist de resultados, se han introducido en la tabla cruces
        // todos los resultados y se tienen los valores totales de partidas y rondas ganadas, perdidas y empatadas
        // a lo largo del torneo, que se deberan introducir con una sola operacion en cada tabla, con un solo metodo
        // cuando el it.hasNext devuelva false, control deberia ser true si todo se ha introducido bien
        if (control) {
            control = gestorBD.guardar_torneo(usuario, baraja1, main_torneo1, main_torneo2,
                    side_torneo1, side_torneo2, rondas_ganadas, rondas_perdidas, rondas_empatadas);

        }

        if (control) {
            String cadena_resultado = "" + rondas_ganadas + "-" + rondas_perdidas + "-" + rondas_empatadas;
            int puntos = rondas_ganadas * 3 + rondas_empatadas;
            control = gestorBD.guardar_resultado_torneo(usuario, baraja1, puntos, cadena_resultado);
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
