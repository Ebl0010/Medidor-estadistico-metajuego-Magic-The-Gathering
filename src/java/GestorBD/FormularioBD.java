package GestorBD;

import Modelo.Baraja_de_usuario;
import Modelo.Usuario;
import Util.PoolDeConexiones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de gestión de datos y comnunicación con la base de datos para el login
 * y usuario con sesión activa.
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class FormularioBD {

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
    public FormularioBD() {
        l = LoggerFactory.getLogger(FormularioBD.class);
    }

    /**
     * Método para introducir un usuario a la base de datos.Antes del insert,
     * realiza una select para saber si es el primer usuario que se registra y,
     * en caso de serlo, le asigna el rol de administrador. Toda cuenta de
     * usuario que ingrese posteriormente tendrá el rol "Estandar".
     *
     * Se inicializa una variable entera "retorno" en 1, y si no existen
     * usuarios previamente se reasigna a 0. Si la inserción del usuario
     * funciona, utilizando esta variable retorno se crea un registro en la
     * tabla roles_usuarios con el valor 0 (Admin) o 1 (Estandar) para ese
     * usuario. Si se produce un error durante la transacción la variable pasa a
     * valer -1 y no se produce esta inserción.
     *
     * Si se produce una violación de clave ajena porque el nombre del usuario
     * está repetido, se salta este bloque del código y llega al bloque catch,
     * donde se comprueba si ya existia un usuario con ese nombre para devolver
     * -2. En caso de que el usuario NO exista, escribe la excepción en el
     * logger y devuelve -1.
     *
     * @param usuario ojeto de la clase usuario con los parámetros nombre,
     * contraseña y correo que se introducen a la base de datos.
     * @return 0 o 1 si el usuario se agrega satisfactoriamente, -2 si ya
     * existía un usuario con ese nombre y -1 si se produce otro tipo de error.
     * @throws SQLException lanza una posible excepción en el bloque finally al
     * cerrar los recursos.
     *
     */
    public int crearUsuario(Usuario usuario) throws SQLException {

        int retorno = 1;

        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement("select * from usuarios");

            rs = st.executeQuery();
            if (!rs.next()) {
                retorno = 0;
            }

            st = con.prepareStatement("INSERT INTO usuarios VALUES"
                    + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            // 1, 2 y 3 nombre de usuario, clave y correo
            st.setString(1, usuario.getNombre());
            st.setString(2, usuario.getClave());
            st.setString(3, usuario.getCorreo());
            // 4, 5, 6, 7 y 8 enteros de rondas y partidas
            st.setInt(4, 0);
            st.setInt(5, 0);
            st.setInt(6, 0);
            st.setInt(7, 0);
            st.setInt(8, 0);
            // 9 y 10 porcentajes
            st.setFloat(9, 0);
            st.setFloat(10, 0);

            int resultUpdate = st.executeUpdate();

            if (resultUpdate == 0) {
                retorno = -1;
            }

            if (retorno != -1) {
                st = con.prepareStatement("insert into roles_usuarios values (?, ?, ?)");
                st.setString(1, usuario.getNombre());
                st.setInt(3, 0); //estado por defecto "condedido"
                // si retorno es 0 es Admin, sino Estandar 
                if (retorno == 0) {
                    st.setInt(2, 0);
                } else {
                    st.setInt(2, 1);
                }
                resultUpdate = st.executeUpdate();

                if (resultUpdate == 0) {
                    retorno = -1;
                }

            }

            con.commit();
            return retorno;

        } catch (SQLException e) {
            try {
                st = con.prepareStatement("select nombre_usuario from usuarios where nombre_usuario = ?");
                st.setString(1, usuario.getNombre());
                rs = st.executeQuery();
                if (rs.next()) {
                    String encontrado = rs.getString("nombre_usuario");
                    if (encontrado.equals(usuario.getNombre())) {
                        return -2;
                    }
                } else {
                    return -1;
                }
            } catch (SQLException e2) {
                con.rollback();
                l.error(e.getLocalizedMessage());
                l.error(e2.getLocalizedMessage());
                return -1;
            }

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
        return retorno;
    }

    /**
     * Método que se lanza cuando el usuario intenta hacer login con una
     * combinacion nombre/clave.El método comprueba si ese usuario existe (si no
     * inicializa devolver a 0), y comprueba la clave de ese usuario, si es
     * correcta inicializa devolver a 1 y sino a -1. Si se produce una excepción
     * devuelve -2.
     *
     * @param usuario objeto usuario con la combinación nombre/clave que ha
     * hecho login.
     * @return 1 si es correcto, 0 si el usuario no existe o -1 si existe pero
     * la contraseña es incorrecta o -2 si se produce algún error.
     * @throws SQLException posible excepción producida en el cierre de
     * recursos.
     */
    public int existeUsuario(Usuario usuario) throws SQLException {
        int devolver;
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement("Select clave from usuarios where nombre_usuario = ?");
            st.setString(1, usuario.getNombre());
            rs = st.executeQuery();

            if (!rs.next()) {
                devolver = 0;
            } else {
                if (rs.getString("clave").equals(usuario.getClave())) {
                    devolver = 1;
                } else {
                    devolver = -1;
                }
            }
            con.commit();
            return devolver;
        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
            return -2;
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
     * Método que carga los valores del usuario. Recibe una combinación
     * nombre-clave y devuelve un objeto usuario con todos los datos
     * estadísticos de dicho usuario de la base de datos.
     *
     * @param usuario Objeto usuario que únicamente contiene los valores de
     * nombre y clave.
     * @return Usuario devuelve un objeto Usuario con todos los valores respecto
     * a sus datos en la base de datos.
     * @throws SQLException posible excepción producida en el cierre de
     * recursos.
     */
    public Usuario cargarUsuario(Usuario usuario) throws SQLException {
        Usuario devolver = null;
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement("SELECT * FROM usuarios WHERE nombre_usuario = ? and clave = ?");

            st.setString(1, usuario.getNombre());
            st.setString(2, usuario.getClave());

            rs = st.executeQuery();

            rs.next();

            devolver = new Usuario(usuario.getNombre());
            devolver.setRondas_ganadas(rs.getInt("rondas_ganadas"));
            devolver.setRondas_empatadas(rs.getInt("rondas_empatadas"));
            devolver.setRondas_perdidas(rs.getInt("rondas_perdidas"));
            devolver.setPartidas_ganadas(rs.getInt("partidas_ganadas"));
            devolver.setPartidas_perdidas(rs.getInt("partidas_perdidas"));
            devolver.setPorcentaje_rondas(rs.getFloat("porcentaje_rondas"));
            devolver.setPorcentaje_partidas(rs.getFloat("porcentaje_partidas"));

            st = con.prepareStatement(
                    "select descripcion from roles_usuarios left join roles on roles.idRol = roles_usuarios.idRol "
                    + "where nombre_usuario = ?"
                    + "and estado = 0 order by roles.idRol");
            st.setString(1, usuario.getNombre());
            rs = st.executeQuery();
            rs.next();
            String rol = rs.getString("descripcion");
            devolver.setRol(rol);

            //como lee_barajas_de_usuario requiere una conexión, y es un metodo de esta misma clase,
            //si el commit va después la conexión ya no existe porque la habría cerrado ese método.
            //aun así, estos dos métodos pueden venir aquí dentro y estar después del commit.
            con.commit();

            ArrayList<Baraja_de_usuario> barajas = leeBarajas_de_usuario(usuario);
            devolver.setLista_de_barajas_de_usuario(barajas);

            return devolver;

        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
            return devolver;
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
     * Metodo privado que se llama durante "carga usuario" y que carga sobre un
     * usuario todas sus barajas. Este método realiza una select de la tabla
     * barajas_usuarios con el usuario que recibe como parámetro y por cada
     * baraja en el resultSet va cargando sus datos en un objeto
     * baraja_de_usuario que posteriormente introducirá en una lista. Finlamente
     * devuelve la lista con todas las barajas de usuario que ha encontrado.
     *
     * @param usuario usuario del que se quieren obtener sus barajas.
     * @return barajas_de_usuario es la lista con todas las barajas que tiene
     * ese usuario asignadas (y sus datos).
     * @throws SQLException posible excepción producida durante el cierre de
     * recursos.
     */
    private ArrayList<Baraja_de_usuario> leeBarajas_de_usuario(Usuario usuario)
            throws SQLException {
        ArrayList<Baraja_de_usuario> lista_de_barajas = new ArrayList<>();
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement(
                    "select * from barajas_usuarios where nombre_usuario = ?");
            st.setString(1, usuario.getNombre());
            rs = st.executeQuery();

            if (!rs.next()) {
                return lista_de_barajas;
            } else {
                do {
                    Baraja_de_usuario baraja = new Baraja_de_usuario();
                    baraja.setNombre_baraja(rs.getString("nombre_baraja"));
                    baraja.setRondas_ganadas(rs.getInt("rondas_ganadas"));
                    baraja.setRondas_perdidas(rs.getInt("rondas_perdidas"));
                    baraja.setRondas_empatadas(rs.getInt("rondas_empatadas"));
                    baraja.setPartidas_ganadas_main(rs.getInt("partidas_ganadas_main"));
                    baraja.setPartidas_ganadas_side(rs.getInt("partidas_ganadas_side"));
                    baraja.setPartidas_perdidas_main(rs.getInt("partidas_perdidas_main"));
                    baraja.setPartidas_perdidas_side(rs.getInt("partidas_perdidas_side"));
                    baraja.setPorcentaje_partidas_ganadas_main(rs.getFloat("porcentaje_partidas_ganadas_main"));
                    baraja.setPorcentaje_partidas_ganadas_side(rs.getFloat("porcentaje_partidas_ganadas_side"));
                    baraja.setPorcentaje_partidas_ganadas_total(rs.getFloat("porcentaje_partidas_ganadas_total"));
                    baraja.setPorcentaje_rondas_ganadas(rs.getFloat("porcentaje_rondas_ganadas"));

                    lista_de_barajas.add(baraja);

                } while (rs.next());
                con.commit();
                return lista_de_barajas;
            }
        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
            return lista_de_barajas;
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
     * Método que cambia el nombre, clave, o ambos, de un usuario. Antes de
     * hacerlo comprueba que el nombre nuevo no existe ya. El método está
     * programado utilizando una programación defensiva, si el usuario existe
     * previamente va a devolver un -1, si el cambio se efectúa correctamente un
     * 1, y sino un 0.
     *
     * @param nombre nombre antes de actualizar
     * @param nombre_nuevo nombre actualizado (si lo deja en blanco es el mismo)
     * @param clave_nueva clave nueva (si la deja en blanco es la misma)
     * @return 1 si los cambios han ido bien, -1 si el nick esta repetido y 0 si
     * no se han producido cambios.
     * @throws SQLException posible excepción producida durante el cierre de
     * recursos.
     */
    public int actualizarUsuario(String nombre, String nombre_nuevo, String clave_nueva, boolean cambia_nombre)
            throws SQLException {
        int resultado = 0;
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            if (cambia_nombre) {
                st = con.prepareStatement(
                        "select nombre_usuario from usuarios where nombre_usuario = ?");
                st.setString(1, nombre_nuevo);
                rs = st.executeQuery();
                if (rs.next()) {
                    resultado = -1;
                }
            }

            if (resultado == 0) {
                if (cambia_nombre) {
                    st = con.prepareStatement(
                            "update usuarios set nombre_usuario = ?, "
                            + "clave = ? where "
                            + "nombre_usuario = ?");
                    st.setString(1, nombre_nuevo);
                    st.setString(2, clave_nueva);
                    st.setString(3, nombre);
                } else {
                    st = con.prepareStatement(
                            "update usuarios set clave = ? where nombre_usuario = ?");
                    st.setString(1, clave_nueva);
                    st.setString(2, nombre);
                }

                resultado = st.executeUpdate();
            }

            con.commit();
            return resultado;

        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
            return -2;
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
