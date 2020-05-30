/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestorBD;

import Conectividad.ConectaBD;
import Modelo.Baraja;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class BarajaBD {

    private Connection con = null;
    private PreparedStatement st = null;
    private ResultSet rs = null;
    private int resultUpdate = 0;

    public ArrayList<Baraja> lee_todas_las_barajas() throws SQLException {
        ArrayList<Baraja> barajas = new ArrayList<>();
        Baraja baraja;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("select nombre_baraja, tier, porcentaje_main, porcentaje_side, porcentaje_total from barajas order by tier");
            rs = st.executeQuery();

            while (rs.next()) {
                baraja = new Baraja();
                baraja.setNombre(rs.getString("nombre_baraja"));
                baraja.setTier(rs.getInt("tier"));
                baraja.setPorcentaje_main(rs.getFloat("porcentaje_main"));
                baraja.setPorcentaje_side(rs.getFloat("porcentaje_side"));
                baraja.setPorcentaje_total(rs.getFloat("porcentaje_total"));
                barajas.add(baraja);
            }

            return barajas;

        } catch (SQLException e) {
            //e.printStackTrace();
            //con.rollback();
            return barajas;
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
     * puede dar error de clave primaria...
     *
     * @param modificar
     * @param nombre_nuevo
     * @param tier_nuevo
     * @return
     * @throws SQLException
     */
    public int actualizarBaraja(String modificar, String nombre_nuevo, int tier_nuevo) throws SQLException {
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("UPDATE barajas SET nombre_baraja = ?, tier = ? where nombre_baraja = ?");
            st.setString(1, nombre_nuevo);
            st.setInt(2, tier_nuevo);
            st.setString(3, modificar);

            resultUpdate = st.executeUpdate();

            return resultUpdate;

        } catch (SQLException e) {
            //con.rollback;
            //e.printStackTrace();
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

    public boolean borrarBaraja(String nombre_baraja) throws SQLException {
        int rs;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("delete from barajas where nombre_baraja = ?");
            st.setString(1, nombre_baraja);
            rs = st.executeUpdate();

            //con.commit();
            return rs == 1;

        } catch (SQLException e) {
            //con.rollback();
            //e.printStackTrace();
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

    public int guardarBaraja(Baraja baraja) throws SQLException {
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("INSERT INTO barajas VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            // 1 y 2 nombre y tier
            st.setString(1, baraja.getNombre());
            st.setInt(2, baraja.getTier());
            // 3, 4, 5 y 6 enteros
            st.setInt(3, 0);
            st.setInt(4, 0);
            st.setInt(5, 0);
            st.setInt(6, 0);
            // 7, 8 y 9 decimales.
            st.setFloat(7, 0);
            st.setFloat(8, 0);
            st.setFloat(9, 0);

            resultUpdate = st.executeUpdate();

            //con.commit();
            return resultUpdate;

        } catch (SQLException e) {
            //con.rollback();
            //e.printStackTrace();
            return -1;
        } finally {
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean agregar_baraja_a_usuario(String nombre_usuario, String nombre_baraja) throws SQLException {
        boolean retorno = false;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            // no hace falta comprobar si la baraja existe porque la esta cogiendo de las barajas existentes ya

            st = con.prepareStatement("INSERT INTO barajas_usuarios values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            // 1 y 2 los nombres
            st.setString(1, nombre_usuario);
            st.setString(2, nombre_baraja);
            // 3, 4 y 5 integeres de rondas ganadas perdidas y empatadas
            st.setInt(3, 0);
            st.setInt(4, 0);
            st.setInt(5, 0);
            // 6, 7, 8 y 9 integeres de partidas ganadas y perdidas de main y side
            st.setInt(6, 0);
            st.setInt(7, 0);
            st.setInt(8, 0);
            st.setInt(9, 0);
            // 10, 11, 12 y 13 porcentajes de promedios
            st.setFloat(10, 0);
            st.setFloat(11, 0);
            st.setFloat(12, 0);
            st.setFloat(13, 0);

            resultUpdate = st.executeUpdate();
            if (resultUpdate != 0) {
                retorno = true;
            }
            return retorno;
        } catch (SQLException e) {
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

}
