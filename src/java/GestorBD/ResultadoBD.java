/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestorBD;

import Conectividad.ConectaBD;
import Modelo.Cruce;
import Modelo.Resultado;
import Modelo.ResultadoUsuarioBaraja;
import Modelo.Torneo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class ResultadoBD {

    private Connection con = null;
    private PreparedStatement st = null;
    private ResultSet rs = null;

    private float porcentaje_main, porcentaje_side, porcentaje_total;

    public ArrayList<String> lee_nombres_barajas() throws SQLException {
        ArrayList<String> barajas = new ArrayList<>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("SELECT nombre_baraja FROM barajas order by tier");
            rs = st.executeQuery();

            if (!rs.next()) {
                return barajas;
            } else {
                do {
                    barajas.add(rs.getString("nombre_baraja"));
                } while (rs.next());
                return barajas;
            }
        } catch (SQLException e) {
            //con.rollback();
            return null;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public ArrayList<String> devolver_nombres_barajas_de_usuario(String nombre_usuario) throws SQLException {
        ArrayList<String> barajas_de_usuario = new ArrayList<>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("SELECT nombre_baraja FROM barajas_usuarios "
                    + "where nombre_usuario = ?");
            st.setString(1, nombre_usuario);
            rs = st.executeQuery();

            while (rs.next()) {
                barajas_de_usuario.add(rs.getString("nombre_baraja"));
            }

            //con.commit;
            return barajas_de_usuario;

        } catch (SQLException e) {
            //con.rollback();
            //e.printStackTrace();
            return barajas_de_usuario;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }


    public Resultado obtener_resultados(String baraja1, String baraja2) throws SQLException {
        Resultado res = new Resultado();
        res.setBaraja1(baraja1);
        if (baraja2 != null) {
            res.setBaraja2(baraja2);
        }

        try {

            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();

            if (baraja2 == null) {
                st = con.prepareStatement(
                        "select ganadas_main, ganadas_side, perdidas_main, "
                        + "perdidas_side from barajas where nombre_baraja = ?");
                st.setString(1, baraja1);
            } else {
                st = con.prepareStatement(
                        "select ganadas_main_1, perdidas_main_1, ganadas_side_1, "
                        + "perdidas_side_1 from cruces where nombre_baraja_1 = ?"
                        + " and nombre_baraja_2 = ?");
                st.setString(1, baraja1);
                st.setString(2, baraja2);
            }

            rs = st.executeQuery();

            if (!rs.next()) {
                res.setMain1(0);
                res.setMain2(0);
                res.setSide1(0);
                res.setSide2(0);
            } else {
                // tienen que ser numeros por el distinto nombre de las columnas con 1 o 2 barajas
                res.setMain1(rs.getInt(1));
                res.setMain2(rs.getInt(2));
                res.setSide1(rs.getInt(3));
                res.setSide2(rs.getInt(4));
            }

            //con.commit;
            return res;

        } catch (SQLException e) {
            //e.printStackTrace();
            //con.rollback();
            return res;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }

    /**
     * Este metodo recibe un parametro Resultado con los valores de victorias y derrotas de main y side y los
     * porcentajes correspondientes de baraja1 sobre baraja2. Si Baraja2 es nulo los datos se insertan en
     * la tabla barajas ya que se estan actualizado los datos globales de una unica baraja.
     * 
     * Si, por otro lado, baraja2 no es nulo, entonces se tienen que actualizar los valores en la tabla cruces,
     * y a su vez el metodo valora si el parametro existeCruce es verdadero para hacer un update o si es falso
     * para hacer un insert, en funcion de si ese cruce existia ya previamente o no. 
     * @param res variable resultado con los datos que se quieren introducir en la base de datos
     * @param existeCruce variable booleana para saber si hay que hacer insert o update
     * @return true o false en funcion de si la transaccion ha funcionado o no
     * @throws SQLException 
     */
    public boolean introducir_variable_resultado(Resultado res, boolean existeCruce) throws SQLException {
        boolean resultado;
        try {

            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();

            if (res.getBaraja2() == null) {
                st = con.prepareStatement(
                        "update barajas set "
                        + "ganadas_main = ?, "
                        + "perdidas_main = ?, "
                        + "ganadas_side = ?, "
                        + "perdidas_side = ?, "
                        + "porcentaje_main = ?, "
                        + "porcentaje_side = ?, "
                        + "porcentaje_total = ? "
                        + "where nombre_baraja = ?");

                st.setInt(1, res.getMain1());
                st.setInt(2, res.getMain2());
                st.setInt(3, res.getSide1());
                st.setInt(4, res.getSide2());
                st.setFloat(5, res.getPorcentaje_main());
                st.setFloat(6, res.getPorcentaje_side());
                st.setFloat(7, res.getPorcentaje_total());
                st.setString(8, res.getBaraja1());
                
                resultado = st.executeUpdate() == 1;

            } else { //si baraja2 no es nulo hay que actualizar cruces, 
                //pero si el cruce antes no existia hay que insertarlo.

                if (existeCruce) {
                    st = con.prepareStatement(
                            "update cruces set "
                            + "ganadas_main_1 = ?, "
                            + "perdidas_main_1 = ?, "
                            + "ganadas_side_1 = ?, "
                            + "perdidas_side_1 = ?, "
                            + "porcentaje_main_1 = ?, "
                            + "porcentaje_side_1 = ?, "
                            + "porcentaje_total_1 = ? "
                            + "where nombre_baraja_1 = ? and "
                            + "nombre_baraja_2 = ?");
                    
                    st.setInt(1, res.getMain1());
                    st.setInt(2, res.getMain2());
                    st.setInt(3, res.getSide1());
                    st.setInt(4, res.getSide2());
                    st.setFloat(5, res.getPorcentaje_main());
                    st.setFloat(6, res.getPorcentaje_side());
                    st.setFloat(7, res.getPorcentaje_total());
                    st.setString(8, res.getBaraja1());
                    st.setString(9, res.getBaraja2());
                    
                    resultado = st.executeUpdate() == 1;

                    
                } else { 
                    st = con.prepareStatement(
                            "insert into cruces values (?,?,?,?,?,?,?,?,?)");
                            
                    st.setString(1, res.getBaraja1());
                    st.setString(2, res.getBaraja2());
                    st.setInt(3, res.getMain1());
                    st.setInt(4, res.getMain2());
                    st.setInt(5, res.getSide1());
                    st.setInt(6, res.getSide2());
                    st.setFloat(7, res.getPorcentaje_main());
                    st.setFloat(8, res.getPorcentaje_side());
                    st.setFloat(9, res.getPorcentaje_total());
                    
                    resultado = st.executeUpdate() == 1;
                    
                }
                
            }

            //con.commit;
            return resultado;

        } catch (SQLException e) {
            //e.printStackTrace();
            //con.rollback();
            return false;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }


    public ResultadoUsuarioBaraja obtener_resultado_usuario_con_baraja(String nombre_usuario, String nombre_baraja)
            throws SQLException {

        ResultadoUsuarioBaraja res = new ResultadoUsuarioBaraja();
        res.setNombre_usuario(nombre_usuario);
        res.setNombre_baraja(nombre_baraja);

        try {

            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();

            st = con.prepareStatement(
                    "select rondas_ganadas, rondas_perdidas, rondas_empatadas, partidas_ganadas_main, "
                    + "partidas_perdidas_main, partidas_ganadas_side, partidas_perdidas_side "
                    + "from barajas_usuarios where nombre_baraja = ? "
                    + "and nombre_usuario = ?");

            st.setString(1, nombre_baraja);
            st.setString(2, nombre_usuario);

            rs = st.executeQuery();

            if (!rs.next()) {
                res.setGanadasMain(0);
                res.setPerdidasMain(0);
                res.setGanadasSide(0);
                res.setPerdidasSide(0);
                res.setRondasGanadas(0);
                res.setRondasPerdidas(0);
                res.setRondasEmpatadas(0);
            } else {
                // tienen que ser numeros por el distinto nombre de las columnas con 1 o 2 barajas
                res.setRondasGanadas(rs.getInt(1));
                res.setRondasPerdidas(rs.getInt(2));
                res.setRondasEmpatadas(rs.getInt(3));
                res.setGanadasMain(rs.getInt(4));
                res.setPerdidasMain(rs.getInt(5));
                res.setGanadasSide(rs.getInt(6));
                res.setPerdidasSide(rs.getInt(7));

            }

            //con.commit;
            return res;

        } catch (SQLException e) {
            //e.printStackTrace();
            //con.rollback();
            return res;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }

    public boolean introducir_variable_resultado_usuario_con_baraja(ResultadoUsuarioBaraja res)
            throws SQLException {
        boolean resultado;
        try {

            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();

            st = con.prepareStatement(
                    "update barajas_usuarios set "
                    + "rondas_ganadas = ?, "
                    + "rondas_empatadas = ?, "
                    + "rondas_perdidas = ?, "
                    + "partidas_ganadas_main = ?, "
                    + "partidas_perdidas_main = ?, "
                    + "partidas_ganadas_side = ?, "
                    + "partidas_perdidas_side = ?, "
                    + "porcentaje_rondas_ganadas = ?, "
                    + "porcentaje_partidas_ganadas_main = ?, "
                    + "porcentaje_partidas_ganadas_side = ?, "
                    + "porcentaje_partidas_ganadas_total = ? "
                    + "where nombre_baraja = ? "
                    + "and nombre_usuario = ?");

            st.setInt(1, res.getRondasGanadas());
            st.setInt(2, res.getRondasEmpatadas());
            st.setInt(3, res.getRondasPerdidas());
            st.setInt(4, res.getGanadasMain());
            st.setInt(5, res.getPerdidasMain());
            st.setInt(6, res.getGanadasSide());
            st.setInt(7, res.getPerdidasSide());
            st.setFloat(8, res.getPorcentajeRondasGanadas());
            st.setFloat(9, res.getPorcentajeMain());
            st.setFloat(10, res.getPorcentajeSide());
            st.setFloat(11, res.getPorcentajeTotal());
            st.setString(12, res.getNombre_baraja());
            st.setString(13, res.getNombre_usuario());

            resultado = st.executeUpdate() == 1;

            //con.commit;
            return resultado;

        } catch (SQLException e) {
            //e.printStackTrace();
            //con.rollback();
            return false;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }

    public boolean guardar_resultado_final_torneo(String nombre_usuario, String nombre_baraja, int puntos,
            String cadena_resultado)
            throws SQLException {
        boolean retorno;
        try {
            // no tengo que comprobar usuario ni baraja porque sino el torneo no hubiese podido agregarse
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement(
                    "update torneos set repeticiones = repeticiones +1 where nombre_usuario = ? "
                    + "and nombre_baraja = ? and resultado = ?");
            st.setString(1, nombre_usuario);
            st.setString(2, nombre_baraja);
            st.setString(3, cadena_resultado);

            if (st.executeUpdate() == 1) {
                retorno = true;
            } else {

                st = con.prepareStatement("INSERT INTO torneos values (?, ?, ?, ?, 1)");
                st.setString(1, nombre_usuario);
                st.setString(2, nombre_baraja);
                st.setString(3, cadena_resultado);
                st.setInt(4, puntos);

                retorno = st.executeUpdate() == 1;
            }

            return retorno;
        } catch (SQLException e) {
            //con.rollback();
            return false;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public ArrayList<Cruce> leerCruces(String nombre_baraja) throws SQLException {
        ArrayList<Cruce> cruces = new ArrayList<Cruce>();
        Cruce cruce;

        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("select * from cruces where nombre_baraja_1 = ?");
            st.setString(1, nombre_baraja);
            rs = st.executeQuery();

            while (rs.next()) {
                cruce = new Cruce();
                cruce.setBaraja2(rs.getString("nombre_baraja_2"));
                cruce.setGanadas_main(rs.getInt("ganadas_main_1"));
                cruce.setPerdidas_main(rs.getInt("perdidas_main_1"));
                cruce.setGanadas_side(rs.getInt("ganadas_side_1"));
                cruce.setPerdidas_side(rs.getInt("perdidas_side_1"));
                cruce.setPorcentaje_main(rs.getFloat("porcentaje_main_1"));
                cruce.setPorcentaje_side(rs.getFloat("porcentaje_side_1"));
                cruce.setPorcentaje_total(rs.getFloat("porcentaje_total_1"));
                cruces.add(cruce);
            }

            return cruces;

        } catch (SQLException e) {
            //e.printStackTrace();
            //con.rollback();
            return cruces;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }

    public ArrayList<Torneo> cargaTorneosDeUsuario(String nombre_usuario) throws SQLException {
        ArrayList<Torneo> torneos = new ArrayList<>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("SELECT * FROM torneos WHERE nombre_usuario = ? order by puntos desc");
            st.setString(1, nombre_usuario);
            rs = st.executeQuery();

            while (rs.next()) {
                Torneo t = new Torneo();
                t.setNombre_baraja(rs.getString("nombre_baraja"));
                t.setPuntos(rs.getInt("puntos"));
                t.setResultado(rs.getString("resultado"));
                t.setRepeticiones(rs.getInt("repeticiones"));
                t.setNombre_usuario(nombre_usuario);
                torneos.add(t);
            }
            return torneos;
        } catch (SQLException e) {
            //e.printStackTrace();
            //con.rollback();
            return torneos;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

}
