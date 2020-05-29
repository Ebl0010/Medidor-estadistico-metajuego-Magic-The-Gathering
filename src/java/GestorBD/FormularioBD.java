/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestorBD;

import Conectividad.ConectaBD;
import Modelo.Baraja_de_usuario;
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
public class FormularioBD {

    private Connection con = null;
    private PreparedStatement st = null;
    private ResultSet rs = null;
    private int resultUpdate = 0;

    /**
     *
     * @param usuario usuario nuevo a crear
     * @return 0 si es el primer usuario, 1 si no y -1 si hay error
     *
     * comprueba si no hay ningun usuario previo para cambiar retorno a 0,
     * inserta el usuario nuevo con su nombre y su clave; pondría a -1 retorno
     * si no se insertase. Despues, si no ha habido errores inserta en roles
     * usuarios a este nuevo usuario con admin si es el primero o con cuenta
     * estandar si no lo es.
     */
    public int crearUsuario(Usuario usuario) {

        int retorno = 1;

        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
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

            resultUpdate = st.executeUpdate();

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

            //con.commit();
            return retorno;

        } catch (SQLException e) {
            try {
              st = con.prepareStatement("select nombre_usuario from usuarios where nombre_usuario = ?");
              rs = st.executeQuery();
              if (rs.next()){
                  String encontrado = rs.getString("nombre_usuario");
                  if (encontrado.equals(usuario.getNombre())){
                      return -2;
                  }
              }
            } catch (SQLException e2){
               //e.printStackTrace();
               //con.rollback();
               return -1;
            }
            
            return -1;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                return -1;
            }

        }

    }

    /*
    se comrpueba si el usuario existe. 
    Devuelve 1 si existe, 0 si no existe y -1 si existe pero la clave esta mal.
     */
    public int existeUsuario(Usuario usuario) {
        int devolver = 0;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
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
            //con.commit();
            return devolver;
        } catch (SQLException e) {
            //con.rollback();
            //e.printStackTrace();
            return -2;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                return -2;
            }
        }
    }
    
    
    public Usuario cargarUsuario(Usuario usuario) throws SQLException {
        Usuario devolver = null;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("SELECT * FROM usuarios WHERE nombre_usuario = ? and clave = ?");

            st.setString(1, usuario.getNombre());
            st.setString(2, usuario.getClave());

            rs = st.executeQuery();

            if (!rs.next()) {
                devolver = null;
            } else {
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

                ArrayList<Baraja_de_usuario> barajas = leeBarajas_de_usuario(usuario);
                devolver.setLista_de_barajas_de_usuario(barajas);
            }

        } catch (SQLException e) {
            //con.rollback();
            e.printStackTrace();
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
        return devolver;
    }
    
    
    private ArrayList<Baraja_de_usuario> leeBarajas_de_usuario(Usuario usuario)
            throws SQLException {
        ArrayList<Baraja_de_usuario> lista_de_barajas = new ArrayList<>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement(
                    "select  * from barajas_usuarios where nombre_usuario = ?");
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
                //con.commmit();
                return lista_de_barajas;
            }
        } catch (SQLException e) {
            //con.rollback();
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
     * cambia el nombre, clave o ambos de un usuario. Antes de hacerlo comprueba que el nombre nuevo
     * no existe ya
     * @param nombre nombre antes de actualizar
     * @param nombre_nuevo nombre actualizado (si lo deja en blanco es el mismo)
     * @param clave_nueva clave nueva (si la deja en blanco es la misma)
     * @return 1 si los cambios han ido bien, -1 si el nick esta repetido
     * @throws SQLException 
     */
    public int actualizarUsuario(String nombre, String nombre_nuevo, String clave_nueva)
            throws SQLException {
        int resultado = 0;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement(
            "select nombre_usuario from usuarios where nombre_usuario = ?");
            rs = st.executeQuery();
            if (rs.next()){
                resultado = -1;
            } else {
               st = con.prepareStatement(
                    "update usuarios set nombre_usuario = ?, "
                    + "clave = ? where "
                    + "nombre_usuario = ?");
            st.setString(1, nombre_nuevo);
            st.setString(2, clave_nueva);
            st.setString(3, nombre);

            resultado = st.executeUpdate(); 
            }
            
            return resultado;

        } catch (SQLException e) {
            //con.rollback;
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
    
    

}
