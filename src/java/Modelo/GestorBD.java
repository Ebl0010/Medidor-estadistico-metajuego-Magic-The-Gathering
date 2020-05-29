package Modelo;

import Conectividad.ConectaBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class GestorBD {

    private Connection con = null;
    private PreparedStatement st = null;
    private ResultSet rs = null;
    private int resultUpdate = 0;

    public ArrayList<String> leeRoles() {

        ArrayList<String> roles = new ArrayList<String>();
        String rolHallado;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("Select descripcion from roles");
            rs = st.executeQuery();

            if (!rs.next()) {
                //System.out.println("no se ha encontrado nada");
                con.close();
                return roles;
            } else {
                do {
                    roles.add(rs.getString("descripcion"));
                } while (rs.next());
                con.close();
                return roles;
            }

        } catch (SQLException e) {
        }
        return roles;
    }

    

    

    

    

 
    
    /*
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
    */

    public int devuelve_tier_baraja(String nombre_baraja) throws SQLException {
        int devolver;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("SELECT * FROM barajas where nombre_baraja = ?");
            st.setString(1, nombre_baraja);
            rs = st.executeQuery();

            if (!rs.next()) {
                devolver = 0;
            } else {
                devolver = rs.getInt("tier");
            }

            return devolver;

        } catch (SQLException e) {
            //con.rollback();
            return 0;
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

    /*
        comprobaciones que pueden ser omitidas: si la baraja no esta asignada al usuario no podemos llegar
        al punto donde se lanza este metodo, luego no es necesario comprobar eso.
        Tampoco es necesario, por ende, comprobar que el usuario y la baraja existen.
     */
    public boolean guardar_torneo(String usuario, String baraja1, int main1, int main2, int side1, int side2,
            int rondas_ganadas, int rondas_perdidas, int rondas_empatadas) throws SQLException {

        int main1N, main2N, side1N, side2N, rondas_ganadasN, rondas_empatadasN, rondas_perdidasN, partidas_jugadas_total;
        float por_main, por_side, por_partidas_total, por_rondas;

        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement(
                    "SELECT * FROM barajas_usuarios WHERE nombre_usuario = ? and nombre_baraja = ?");

            st.setString(1, usuario);
            st.setString(2, baraja1);

            boolean control = true;

            rs = st.executeQuery();
            rs.next();

            rondas_ganadasN = rs.getInt("rondas_ganadas") + rondas_ganadas;
            rondas_perdidasN = rs.getInt("rondas_perdidas") + rondas_perdidas;
            rondas_empatadasN = rs.getInt("rondas_empatadas") + rondas_empatadas;

            main1N = main1 + rs.getInt("partidas_ganadas_main");
            main2N = rs.getInt("partidas_perdidas_main") + main2;
            side1N = rs.getInt("partidas_ganadas_side") + side1;
            side2N = rs.getInt("partidas_perdidas_side") + side2;

            partidas_jugadas_total = main1N + main2N + side1N + side2N;

            // evito dividir por 0 y meter 100 a la base porque se va del rango. 
            // decision de diseño: los casos de 100% se van a dar, como mucho, en los primeros 5-6 registros
            if (main1N == 0) {
                por_main = 0;
            } else {
                if (main2N == 0) {
                    por_main = 99;
                } else {
                    por_main = main1N * 100 / (main1N + main2N);
                }
            }

            if (side1N == 0) {
                por_side = 0;
            } else {
                if (side2N == 0) {
                    por_side = 99;
                } else {
                    por_side = side1N * 100 / (side1N + side2N);
                }
            }

            if (main2N == 0 && side2N == 0) {
                por_partidas_total = 99;
            } else {
                por_partidas_total = (main1 + side1) * 100 / partidas_jugadas_total;
            }

            if (rondas_empatadasN == 0 && rondas_perdidasN == 0) {
                por_rondas = 99;
            } else {
                por_rondas = rondas_ganadasN * 100 / (rondas_ganadasN + rondas_empatadasN + rondas_perdidasN);
            }

            // con los porcentajes y los datos tengo que hacer un update en barajas_usuario
            st = con.prepareStatement(
                    "UPDATE barajas_usuarios SET "
                    + "partidas_ganadas_main = ?, "
                    + "partidas_perdidas_main = ?, "
                    + "partidas_ganadas_side = ?, "
                    + "partidas_perdidas_side = ?, "
                    + "rondas_ganadas = ?, "
                    + "rondas_perdidas = ?, "
                    + "rondas_empatadas = ?, "
                    + "porcentaje_partidas_ganadas_main = ?, "
                    + "porcentaje_partidas_ganadas_side = ?, "
                    + "porcentaje_partidas_ganadas_total = ?, "
                    + "porcentaje_rondas_ganadas = ? "
                    + "where nombre_usuario = ? "
                    + "and nombre_baraja = ?");

            //1, 2, 3 y 4 son partidas
            st.setInt(1, main1N);
            st.setInt(2, main2N);
            st.setInt(3, side1N);
            st.setInt(4, side2N);

            // 5, 6 y 7 son rondas
            st.setInt(5, rondas_ganadasN);
            st.setInt(6, rondas_perdidasN);
            st.setInt(7, rondas_empatadasN);

            //8,9 y 10 porcentajes de partidas, 11 porcentaje de rondas
            st.setFloat(8, por_main);
            st.setFloat(9, por_side);
            st.setFloat(10, por_partidas_total);
            st.setFloat(11, por_rondas);

            // 12 y 13 usuario y baraja
            st.setString(12, usuario);
            st.setString(13, baraja1);

            resultUpdate = st.executeUpdate();

            if (resultUpdate != 1) {
                control = false;
            }

            // esto actualizaria las barajas de usuario. Ahora faltaria actualizar los datos del usuario
            if (control) {
                st = con.prepareStatement("select * from usuarios where nombre_usuario = ?");
                st.setString(1, usuario);
                rs = st.executeQuery();
                rs.next();

                int partidas_ganadas = rs.getInt("partidas_ganadas") + main1 + side1;
                int partidas_perdidas = rs.getInt("partidas_perdidas") + main2 + side2;

                rondas_ganadasN = rs.getInt("rondas_ganadas") + rondas_ganadas;
                rondas_perdidasN = rs.getInt("rondas_perdidas") + rondas_perdidas;
                rondas_empatadasN = rs.getInt("rondas_empatadas") + rondas_empatadas;

                if (rondas_ganadasN == 0) {
                    por_rondas = 0;
                } else {
                    if (rondas_empatadasN == 0 && rondas_perdidasN == 0) {
                        por_rondas = 99;
                    } else {
                        por_rondas = rondas_ganadasN * 100 / (rondas_ganadasN + rondas_empatadasN + rondas_perdidasN);
                    }
                }

                if (partidas_ganadas == 0) {
                    por_partidas_total = 0;
                } else {
                    if (partidas_perdidas == 0) {
                        por_partidas_total = 99;
                    } else {
                        por_partidas_total = partidas_ganadas * 100 / (partidas_ganadas + partidas_perdidas);
                    }
                }

                st = con.prepareStatement(
                        "update usuarios set "
                        + "rondas_ganadas = ?, "
                        + "rondas_empatadas = ?, "
                        + "rondas_perdidas = ?, "
                        + "partidas_ganadas = ?, "
                        + "partidas_perdidas = ?, "
                        + "porcentaje_rondas = ?, "
                        + "porcentaje_partidas = ? "
                        + "where nombre_usuario = ?");

                //1, 2 y 3 rondas
                st.setInt(1, rondas_ganadasN);
                st.setInt(2, rondas_empatadasN);
                st.setInt(3, rondas_perdidasN);

                //4 y 5 partidas
                st.setInt(4, partidas_ganadas);
                st.setInt(5, partidas_perdidas);

                //6 y 7 porcentajes
                st.setFloat(6, por_rondas);
                st.setFloat(7, por_partidas_total);

                // 8 nombre
                st.setString(8, usuario);

                resultUpdate = st.executeUpdate();

                // y ahora compruebo que se ha actualizado una fila:
                if (resultUpdate != 1) {
                    control = false;
                }
            } // aqui cierra la ultima comprobacion de control

            /*
            if (control) {
                //con.commit();
            } else {
                //con.rollback();
            }
             */
            return control;

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

    public boolean guardar_resultado_torneo(String nombre_usuario, String nombre_baraja, int puntos, String cadena_resultado)
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

            resultUpdate = st.executeUpdate();
            if (resultUpdate == 1) {
                retorno = true;
            } else {

                st = con.prepareStatement("INSERT INTO torneos values (?, ?, ?, ?, 1)");
                st.setString(1, nombre_usuario);
                st.setString(2, nombre_baraja);
                st.setString(3, cadena_resultado);
                st.setInt(4, puntos);

                resultUpdate = st.executeUpdate();
                retorno = resultUpdate == 1;
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

    

    

    public ArrayList<RolUsuario> lee_peticiones_roles(String nombre_usuario_actual) throws SQLException {

        ArrayList<RolUsuario> roles = new ArrayList<>();
        RolUsuario rol;

        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement(
                    "select * from roles_usuarios left join roles on roles_usuarios.idRol = roles.idRol "
                    + "where estado = 1 or estado = 2 and nombre_usuario != ? "
                    + "order by estado;");
            st.setString(1, nombre_usuario_actual);
            rs = st.executeQuery();

            while (rs.next()) {
                rol = new RolUsuario();
                rol.setNombre_usuario(rs.getString("nombre_usuario"));
                rol.setIdRol(rs.getInt("idRol"));
                rol.setDescripcion_rol(rs.getString("descripcion"));
                roles.add(rol);
            }

            return roles;

        } catch (SQLException e) {
            return roles;
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

    public boolean actualizar_roles(ArrayList<RolUsuario> roles) throws SQLException {

        boolean control = true;
        PreparedStatement stBorrar;

        try {

            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement(
                    "update roles_usuarios set estado = ? where nombre_usuario = ? and idRol = 0");
            stBorrar = con.prepareStatement(
                    "delete from roles_usuarios where idRol > 0 and nombre_usuario = ?");

            for (RolUsuario rol : roles) {
                if (control) {
                    if (rol.getS_estado().equals("Conceder")) {
                        st.setInt(1, 0);
                        st.setString(2, rol.getNombre_usuario());
                        control = st.executeUpdate() == 1;
                        stBorrar.setString(1, rol.getNombre_usuario());
                        control = stBorrar.executeUpdate() == 1;
                    } else {
                        if (rol.getS_estado().equals("Denegar")) {
                            st.setInt(1, 2);
                            st.setString(2, rol.getNombre_usuario());
                            control = st.executeUpdate() == 1;
                        }
                    }

                }

            }

            return control;
        } catch (SQLException e) {
            return false;
        } finally {
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    

}
