package GestorBD;

import Modelo.RolUsuario;
import Util.PoolDeConexiones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de gestión de datos y comnunicación con la base de datos relacionada con las lecturas y peticiones de roles
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class RolesBD {

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
    public RolesBD() {
        l = LoggerFactory.getLogger(ResultadoBD.class);
    }

    /**
     * Método que devuelve una lista con las descripciones de todos los roles. 
     * @return roles lista de String donde cada elemento es la descripción de un rol.
     * @throws SQLException posible excepción surgida durante el cierre de recursos.
     */
    public ArrayList<String> carga_todos_los_roles() throws SQLException {
        ArrayList<String> roles = new ArrayList<>();
        String rol;
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement("Select descripcion from roles");
            rs = st.executeQuery();
            while (rs.next()) {
                rol = rs.getString("descripcion");
                roles.add(rol);
            }
            con.commit();
            return roles;

        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
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

    /**
     * Método que devuelve las solicitudes de cambio de rol del usaurio que recibe como argumento. La consulta
     * contra la base de datos hace un join entre las tablas roles y roles usuarios para obtener las descripciones
     * y devuelve los que tienen estado 1 y 2, que son en espera y denegada, respectivamente.
     * 
     * @param nombre_usuario nombre del usuario cuyas peticiones se quieren obtener.
     * @return solicitudes devuelve una lista de objetos RolUsuario con la descripción y el estado.
     * @throws SQLException posible excepción durante el cierre de recursos.
     */
    public ArrayList<RolUsuario> lee_solicitudes(String nombre_usuario)
            throws SQLException {
        RolUsuario rol;
        ArrayList<RolUsuario> solicitudes = new ArrayList<>();
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement(
                    "select descripcion, estado "
                    + "from roles_usuarios left join roles on roles_usuarios.idRol = roles.idRol "
                    + "where nombre_usuario = ? and estado = 1 or estado = 2");
            st.setString(1, nombre_usuario);
            rs = st.executeQuery();
            while (rs.next()) {
                rol = new RolUsuario();
                rol.setEstado(rs.getInt("estado"));
                rol.setDescripcion_rol(rs.getString("descripcion"));
                rol.traducirEstado();
                solicitudes.add(rol);
            }
            con.commit();
            return solicitudes;
        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
            return solicitudes;
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
     * Método que procesa una petición de cambio de rol. Recibe el nombre del usuario solicitante, el rol actual
     * de dicho usuario y el rol que solicita, ambos en forma de String. Obtiene el valor entero del rol solicitado
     * y del actual y si el valor del rol solicitado es MENOR (0 es el rol más permisivo, y bajar de rol a uno menos
     * permisivo no está contemplado) añade una fila a la tabla solicitudes con el usuario, el rol que solicita y el 
     * estado 1 (en espera).
     * 
     * @param nombre_usuario nombre del usuario que solicita el cambio de rol.
     * @param rol rol actual del usuario solicitante.
     * @param rol_solicitado rol que se solicita.
     * @return true o false en función de si la transacción se ha llevado a cabo o no.
     * @throws SQLException posible excepción durante el cierre de recursos.
     */
    public boolean gestionar_peticion_rol(String nombre_usuario, String rol, String rol_solicitado)
            throws SQLException {
        boolean retorno = true;
        // requiere inicializar a 0 valor_rol_actual porque sino da un error "no esta inicializado", aunque
        // se reasigna antes de ser utilizado.
        int valor_rol_actual = 0, valor_rol_solicitado;

        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement(
                    "select idRol from roles where descripcion = ?");
            st.setString(1, rol);
            rs = st.executeQuery();
            if (!rs.next()) {
                retorno = false;
            }
            if (retorno) {
                valor_rol_actual = rs.getInt("idRol");
                st.setString(1, rol_solicitado);
                rs = st.executeQuery();
                if (!rs.next()) {
                    retorno = false;
                }
            }

            if (retorno) {
                valor_rol_solicitado = rs.getInt("idRol");
                if (valor_rol_actual <= valor_rol_solicitado) {
                    retorno = false;
                } else {
                    st = con.prepareStatement("delete from roles_usuarios where "
                            + "nombre_usuario = ? and idrol = ?");
                    st.setString(1, nombre_usuario);
                    st.setInt(2, valor_rol_solicitado);
                    
                    if (st.executeUpdate() == 1){
                        st = con.prepareStatement("insert into roles_usuarios values (?, ?, 1)");
                        st.setString(1, nombre_usuario);
                        st.setInt(2, valor_rol_solicitado);
                        retorno = (st.executeUpdate() == 1);
                    } else {
                        retorno = false;
                    }
                }
            }

            con.commit();
            return retorno;

        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
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

    
    /**
     * Método que devuelve todas las peticiones de roles excepto las del usuario pasado como argumento. 
     * Para devolver la descripción también hace un join entre la tabla roles y roles_usuarios.
     * @param nombre_usuario_actual nombre del usuario cuyas peticiones no se quieren devolver.
     * @return roles devuelve una lista de RolUsuario con las peticiones de todos los demás usuarios de cambio de rol.
     * @throws SQLException posible excepción durante el cierre de recursos.
     */
    public ArrayList<RolUsuario> lee_peticiones_roles(String nombre_usuario_actual) throws SQLException {

        ArrayList<RolUsuario> roles = new ArrayList<>();
        RolUsuario rol;

        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
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
            
            con.commit();
            return roles;

        } catch (SQLException e) {
           con.rollback();
            l.error(e.getLocalizedMessage());
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

    /**
     * Método que actualiza el estado de todas las peticiones de rol en función de si ha sido concedida o denegada.
     * Recibe una lista de RolUsuario, que contiene las peticiones con nombre del usuario, rol y estado, y,
     * por cada una, si el estado es 2 (denegado), actualiza símplemente el estado, pero, si es concedido (0),
     * actualiza la tabla roles_usuarios poníendo el estado de concedido sobre esa petición y después borra todas
     * las entradas de la tabla roles_usuarios distintas a esta (borrando todas las demás solicitudes y roles antiguos).
     * 
     * @param roles lista con todas las peticiones de roles.
     * @return true o false en función de si la transacción se ha llevado a cabo o no. 
     * @throws SQLException 
     */
    public boolean actualizar_roles(ArrayList<RolUsuario> roles) throws SQLException {

        boolean control = true;
        PreparedStatement stBorrar;

        try {

            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
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

                        if (control) {
                            stBorrar.setString(1, rol.getNombre_usuario());
                            control = stBorrar.executeUpdate() == 1;
                        }
                    } else {
                        if (rol.getS_estado().equals("Denegar")) {
                            st.setInt(1, 2);
                            st.setString(2, rol.getNombre_usuario());
                            control = st.executeUpdate() == 1;
                        }
                    }

                }

            }

            con.commit();
            return control;
        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
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
