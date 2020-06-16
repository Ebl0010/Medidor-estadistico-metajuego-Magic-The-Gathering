package GestorBD;

import Modelo.Baraja;
import Util.PoolDeConexiones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de gestión de datos y comnunicación con la base de datos para las barajas.
 * 
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class BarajaBD {

    /**
     * Atributo conexion para conectarse a la base de datos
     */
    private Connection con = null;
    /**
     * atributo sentencia preparada para las operaciones SQL
     */
    private PreparedStatement st = null;
    /**
     * artibuto result set para recoger el resultado de las select
     */
    private ResultSet rs = null;
    /**
     * atributo logger para recoger las trazas de error de las excepciones
     */
    private static Logger l = null;
    
    /**
     * constructor sin argumentos que inicializa el logger.
     */
    public BarajaBD(){
        l = LoggerFactory.getLogger(BarajaBD.class);
    }
   

    /**
     * Metodo que lee todas las barajas y sus datos y lo devuelve en forma de lista de barajas.
     * 
     * @return barajas: es el arraylist con todas las barajas.
     * @throws SQLException lanza una posible excepción en el bloque finally al cerrar los recursos. 
     */
    public ArrayList<Baraja> lee_todas_las_barajas() throws SQLException {
        ArrayList<Baraja> barajas = new ArrayList<>();
        Baraja baraja;
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
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
            
            con.commit();
            return barajas;

        } catch (SQLException e) {
            l.error(e.getLocalizedMessage());
            con.rollback();
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
     * Metodo que actualiza la baraja con nombre "modificar", poniendo como nombre nuevo "nombre_nuevo"
     * y como tier "tier_nuevo". Si no se quiere modificar uno de los dos parámetros al método le llegará
     * el argumento con el valor constante.
     *
     * @param modificar el nombre de la baraja que se quiere actualizar.
     * @param nombre_nuevo el nombre nuevo que se le quiere dar a esa baraja.
     * @param tier_nuevo el tier nuevo que se le quiere dar a esa baraja.
     * @return resultIpudate: devuelve un entero en función de si la actualización se ha llevado a cabo (1) o no
     * (0) 
     * @throws SQLException lanza una posible excepción en el bloque finally al cerrar los recursos. 
     */
    public int actualizarBaraja(String modificar, String nombre_nuevo, int tier_nuevo) throws SQLException {
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement("UPDATE barajas SET nombre_baraja = ?, tier = ? where nombre_baraja = ?");
            st.setString(1, nombre_nuevo);
            st.setInt(2, tier_nuevo);
            st.setString(3, modificar);

            int resultUpdate = st.executeUpdate();

            con.commit();
            return resultUpdate;

        } catch (SQLException e) {
            l.error(e.getLocalizedMessage());
            con.rollback();
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

    /**
     * Método que elimina una baraja de la base de datos.
     * @param nombre_baraja nombre de la baraja que se quiere eliminar.
     * @return rs devuelve 1 si la baraja se ha eliminado y 0 si no.
     * @throws SQLException lanza una posible excepción en el bloque finally al cerrar los recursos. 
     */
    public int borrarBaraja(String nombre_baraja) throws SQLException {
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement("delete from barajas where nombre_baraja = ?");
            st.setString(1, nombre_baraja);
            int rs = st.executeUpdate();

            con.commit();
            return rs;

        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
            e.printStackTrace();
            return 0;
        } finally {
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Método que introduce una nueva baraja en la base de datos. Los únicos datos que el usuario puede
     * introducir manualmente son el nombre y el tier, y todos los valores estadístiscos se inicializan en 0.
     * 
     * @param baraja Objeto baraja con los datos ya introducidos por el usuario e inicializados a 0.
     * @return 1 si se ha introducido correctamente y 0 si no.
     * @throws SQLException lanza una posible excepción en el bloque finally al cerrar los recursos. 
     */
    public int guardarBaraja(Baraja baraja) throws SQLException {
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
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

            int resultUpdate = st.executeUpdate();

            con.commit();
            return resultUpdate;

        } catch (SQLException e) {
            try {
                st = con.prepareStatement("select nombre_baraja from barajas where nombre_baraja = ?");
                st.setString(1, baraja.getNombre());
                rs = st.executeQuery();
                con.rollback();
                if (rs.next()){
                     return -1;
                } else {
                    return 0;
                }
            } catch (SQLException e2){
                l.error(e.getLocalizedMessage());
                l.error(e2.getLocalizedMessage());
                return 0;
            }
                  
        } finally {
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    /**
     * Método que asocia una baraja a un usuario, añadiendo una fila a la tabla barajas_usuarios.
     * No puede dar un error de violación de clave ajena porque la baraja se selecciona de una lista cargada
     * previamente con las barajas existentes y el nombre del usuario es el que tiene la sesión activa.
     * 
     * @param nombre_usuario usuario con la sesión activa al cual se le añade la baraja seleccionada.
     * @param nombre_baraja nombre de la baraja que se añade al usuario.
     * @return 1 o 0 en función de si la fila se ha añadido, o no, a la tabla, respectivamente.
     * @throws SQLException lanza una posible excepción en el bloque finally al cerrar los recursos. 
     */
    public int agregar_baraja_a_usuario(String nombre_usuario, String nombre_baraja) throws SQLException {
        
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
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

            int retorno = st.executeUpdate();
            con.commit();
            return retorno;
        } catch (SQLException e) {
            con.commit();
            l.error(e.getLocalizedMessage());
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
