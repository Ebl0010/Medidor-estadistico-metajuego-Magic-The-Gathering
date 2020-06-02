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

       

    
    

    

    

    

    

    

}
