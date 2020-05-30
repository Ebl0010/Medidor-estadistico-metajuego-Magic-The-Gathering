/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestorBD;

import Conectividad.ConectaBD;
import Modelo.Resultado;
import Modelo.Usuario;
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
                    + "where nombre_usuario = ? order by tier");
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

    /*
    public boolean introducirResultado_a_baraja(String baraja, int main1, int main2,
            int side1, int side2) throws SQLException {
        boolean control = true;
        int main1N, main2N, side1N, side2N;
        float porcentaje_main, porcentaje_side, porcentaje_total;
        try {

            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("Select * from barajas where nombre_baraja = ?");
            st.setString(1, baraja);
            rs = st.executeQuery();

            if (rs.next()) {
                main1N = rs.getInt("ganadas_main") + main1;
                main2N = rs.getInt("perdidas_main") + main2;
                side1N = rs.getInt("ganadas_side") + side1;
                side2N = rs.getInt("perdidas_side") + side2;

                if (main1N == 0) {
                    porcentaje_main = 0;
                } else {
                    if (main2N == 0) {
                        porcentaje_main = 99;
                    } else {
                        porcentaje_main = main1N * 100 / (main1N + main2N);
                    }
                }

                if (side1N == 0) {
                    porcentaje_side = 0;
                } else {
                    if (side2N == 0) {
                        porcentaje_side = 99;
                    } else {
                        porcentaje_side = side1N * 100 / (side1N + side2N);
                    }
                }

                if (main1N + side1N == 0) {
                    porcentaje_total = 0;
                } else {
                    if (main2N + side2N == 0) {
                        porcentaje_total = 99;
                    } else {
                        porcentaje_total = (main1N + side1N) * 100 / (main1N + main2N + side1N + side2N);
                    }
                }

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

                st.setInt(1, main1N);
                st.setInt(2, main2N);
                st.setInt(3, side1N);
                st.setInt(4, side2N);
                st.setFloat(5, porcentaje_main);
                st.setFloat(6, porcentaje_side);
                st.setFloat(7, porcentaje_total);
                st.setString(8, baraja);

                if (st.executeUpdate() != 1) {
                    control = false;
                }

            } else {
                control = false;
            }

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
        return control;
    }
    
    
    public boolean introducirResultado(String baraja1, String baraja2, int main1, int main2,
            int side1, int side2) throws SQLException {

// NOTA: LAS BARAJAS DEBEN SER DISTINTAS se añaden resultados espejo, consideracion de diseño
        // deben venir ordenadas alfabéticamente para facilitar la busqueda
        float porcentaje_main, porcentaje_side, porcentaje_total;

        boolean retorno = (!baraja1.equals(baraja2));

        if (retorno) {
            retorno = introducirResultado_a_baraja(baraja1, main1, main2, side1, side2);
        }

        if (retorno) {
            retorno = introducirResultado_a_baraja(baraja2, main2, main1, side2, side1);
        }

        if (retorno) {
            try {

                ConectaBD conectaBD = new ConectaBD();
                con = conectaBD.getConnection();

                st = con.prepareStatement(
                        "SELECT * FROM cruces WHERE (nombre_baraja_1 = ? and nombre_baraja_2 = ?)");
                st.setString(1, baraja1);
                st.setString(2, baraja2);
                rs = st.executeQuery();

                if (!rs.next()) {

                    calcular_porcentajes(main1, main2, side1, side2);
                    
                    st = con.prepareStatement(
                            "insert into cruces values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

                    st.setString(1, baraja1);
                    st.setString(2, baraja2);
                    st.setInt(3, main1);
                    st.setInt(4, main2);
                    st.setInt(5, side1);
                    st.setInt(6, side2);
                    st.setFloat(7, porcentaje_main);
                    st.setFloat(8, porcentaje_side);
                    st.setFloat(9, porcentaje_total);

                    resultUpdate = st.executeUpdate();

                    // si no hay fila actualizada devuelvo falso y salgo
                    if (resultUpdate != 1) {
                        retorno = false;
                    }

                    // si se ha actualizado la primera fila, sigo, meto la fila para la segunda baraja
                    if (retorno) {

                        if (main2 == 0) {
                            porcentaje_main = 0;
                        } else {
                            if (main1 == 0) {
                                porcentaje_main = 99;
                            } else {
                                porcentaje_main = main2 * 100 / (main1 + main2);
                            }
                        }

                        if (side2 == 0) {
                            porcentaje_side = 0;
                        } else {
                            if (side1 == 0) {
                                porcentaje_side = 99;
                            } else {
                                porcentaje_side = side2 * 100 / (side1 + side2);
                            }
                        }

                        if (main2 == 0 && side2 == 0) {
                            porcentaje_total = 0;
                        } else {
                            if (main1 == 0 && side1 == 0) {
                                porcentaje_total = 99;
                            } else {
                                porcentaje_total = (main2 + side2) * 100 / (main1 + main2 + side1 + side2);
                            }
                        }

                        st.setString(1, baraja2);
                        st.setString(2, baraja1);
                        st.setInt(3, main2);
                        st.setInt(4, main1);
                        st.setInt(5, side2);
                        st.setInt(6, side1);
                        st.setFloat(7, porcentaje_main);
                        st.setFloat(8, porcentaje_side);
                        st.setFloat(9, porcentaje_total);

                        resultUpdate = st.executeUpdate();

                        if (resultUpdate != 1) {
                            retorno = false;
                        }
                    }

                    // si rs sí tenia next, es decir, existe ya el emparejamiento, tengo que actualizar sin insertar    
                } else {

                    // se recalculan los valores de victorias y derrotas main y side y los porcentajes...
                    main1 = main1 + rs.getInt("ganadas_main_1");
                    main2 = main2 + rs.getInt("perdidas_main_1");
                    side1 = side1 + rs.getInt("ganadas_side_1");
                    side2 = side2 + rs.getInt("perdidas_side_1");

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

                    if (main2 == 0 && side2 == 0) {
                        porcentaje_total = 99;
                    } else {
                        porcentaje_total = (main1 + side1) * 100 / (main1 + main2 + side1 + side2);
                    }

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

                    st.setInt(1, main1);
                    st.setInt(2, main2);
                    st.setInt(3, side1);
                    st.setInt(4, side2);
                    st.setFloat(5, porcentaje_main);
                    st.setFloat(6, porcentaje_side);
                    st.setFloat(7, porcentaje_total);
                    st.setString(8, baraja1);
                    st.setString(9, baraja2);

                    resultUpdate = st.executeUpdate();

                    if (resultUpdate != 1) {
                        retorno = false;
                    }

                    if (retorno) {

                        if (main2 == 0) {
                            porcentaje_main = 0;
                        } else {
                            if (main1 == 0) {
                                porcentaje_main = 99;
                            } else {
                                porcentaje_main = main2 * 100 / (main1 + main2);
                            }
                        }

                        if (side2 == 0) {
                            porcentaje_side = 0;
                        } else {
                            if (side1 == 0) {
                                porcentaje_side = 99;
                            } else {
                                porcentaje_side = side2 * 100 / (side1 + side2);
                            }
                        }

                        if (main2 == 0 && side2 == 0) {
                            porcentaje_total = 0;
                        } else {
                            if (main1 == 0 && side1 == 0) {
                                porcentaje_total = 99;
                            } else {
                                porcentaje_total = (main2 + side2) * 100 / (main1 + main2 + side1 + side2);
                            }
                        }

                        st.setInt(1, main2);
                        st.setInt(2, main1);
                        st.setInt(3, side2);
                        st.setInt(4, side1);
                        st.setFloat(5, porcentaje_main);
                        st.setFloat(6, porcentaje_side);
                        st.setFloat(7, porcentaje_total);
                        st.setString(8, baraja2);
                        st.setString(9, baraja1);

                        resultUpdate = st.executeUpdate();

                        if (resultUpdate != 1) {
                            retorno = false;
                        }

                    }
                }
                //con.commit();
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

        } else {
            return retorno;
        }
    }
    
    
    
    
     */
    
    private Resultado obtener_resultados(String baraja1, String baraja2) throws SQLException {
        Resultado res = new Resultado();
        
        try {

            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            
            if (baraja2 == null){
                st = con.prepareStatement(
                        "select ganadas_main, ganadas_side, perdidas_main, "
                        + "perdidas_side from barajas where nombre_baraja = ?");
                st.setString(1, baraja1);
            } else {
                st = con.prepareStatement(
                        "select ganadas_main_1, perdidas_main_1, ganadas_side_1, "
                        + "perdidas_side_1 from cruces where nombre_baraja_1 = ?"
                        + " and nombre_baraja_2 = ?");
            }
            
            rs = st.executeQuery();
            
            if (!rs.next()){
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
            
            
        } catch (SQLException e){
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
    
    
    private boolean introducir_variable_resultado(Resultado res) throws SQLException {
        boolean resultado;
        try {

            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            
            if (res.getBaraja2() == null){
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
                int resultUpdate = st.executeUpdate();
                if (resultUpdate == 1){
                    resultado = true;
                } else {
                    resultado = false;
                }
            } else {
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
                    st.setInt(1, res.getMain2());
                    st.setInt(1, res.getSide1());
                    st.setInt(1, res.getSide2());
                    st.setFloat(5, res.getPorcentaje_main());
                    st.setFloat(6, res.getPorcentaje_side());
                    st.setFloat(7, res.getPorcentaje_total());
                    st.setString(8, res.getBaraja1());
                    st.setString(9, res.getBaraja2());         
                int resultUpdate = st.executeUpdate();
                if (resultUpdate == 1){
                    resultado = true;
                } else {
                    resultado = false;
                }
            }
            
            //con.commit;
            return resultado;
            
        } catch (SQLException e){
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
    
    public boolean introducirResultado_a_baraja(String baraja, int main1, int main2,
            int side1, int side2) throws SQLException {

        Resultado res = obtener_resultados(baraja, null);
        res.introducir_datos(main1, main2, side1, side2);
        res.calcular_porcentajes();
        return introducir_variable_resultado(res);
               
    }
    
    
    public boolean introducirResultado(String baraja1, String baraja2, int main1, int main2,
            int side1, int side2) throws SQLException {
        
        Resultado res = obtener_resultados(baraja1, baraja2);
        res.introducir_datos(main1, main2, side1, side2);
        res.calcular_porcentajes();
        return introducir_variable_resultado(res);
    }
    

}
