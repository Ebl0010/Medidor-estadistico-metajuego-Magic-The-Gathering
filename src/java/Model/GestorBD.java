package Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GestorBD {

    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    private int resultUpdate = 0;

    public ArrayList<Usuario> leerUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        Usuario usuarioHallado;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select * from usuarios");

            if (!rs.next()) {
                System.out.println("no se ha encontrado nada");
                con.close();
                return null;
            } else {
                do {
                    String nombre = rs.getString("nombre");
                    String clave = rs.getString("clave");
                    usuarioHallado = new Usuario(nombre, clave);
                    usuarios.add(usuarioHallado);
                } while (rs.next());
                con.close();
                return usuarios;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean guardarUsuario(Usuario usuario) {
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            resultUpdate = st.executeUpdate("INSERT INTO usuarios VALUES ('"
                    + usuario.getNombre()
                    + "', '" + usuario.getClave() + "',0,0,0,0,0,0,0,0,0,0,false);");

            if (resultUpdate != 0) {
                con.close();
                return true;
            } else {
                con.close();
                System.out.println("no se ha agregado el usuario");
                return false;
            }
        } catch (Exception e) {
            System.out.println("fallo la base de datos");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * hay tres opciones: 0: el usuario no existe, 1: el usuario existe, 2:
     * tiene privilegios
     *
     * @param usuario
     * @return
     * @throws SQLException
     */
    public int existeUsuario(Usuario usuario) throws SQLException {
        int devolver = 0;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(
                    "select * from usuarios where nombre = '"
                    + usuario.getNombre() + "' and clave = '"
                    + usuario.getClave() + "'");

            if (!rs.next()) {
                devolver = 0;
            } else {
                if (rs.getBoolean("superUser")) {
                    devolver = 2;
                } else {
                    devolver = 1;
                }
            }
        } catch (Exception e) {
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
            return devolver;
        }
    }

    public Usuario cargarUsuario(Usuario parametro) throws SQLException {
        Usuario devolver = null;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(
                    "select * from usuarios where nombre = '"
                    + parametro.getNombre() + "' and clave = '"
                    + parametro.getClave() + "'");
            if (!rs.next()) {
                devolver = null;
            } else {
                devolver = new Usuario(parametro.getNombre(), parametro.getClave());
                devolver.setPj(rs.getInt("pj"));
                devolver.setPg(rs.getInt("pg"));
                devolver.setPe(rs.getInt("pe"));
                devolver.setPp(rs.getInt("pp"));
                devolver.setRj(rs.getInt("rj"));
                devolver.setRg(rs.getInt("rg"));
                devolver.setRe(rs.getInt("re"));
                devolver.setRp(rs.getInt("rp"));
                devolver.setPor_partidas(rs.getFloat("por_partidas"));
                devolver.setPor_rondas(rs.getFloat("por_rondas"));
                ArrayList<Baraja_de_usuario> barajas = leeBarajas_de_usuario(parametro);
                devolver.setLista_de_barajas_de_usuario(barajas);
            }

        } catch (Exception e) {
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
            return devolver;
        }

    }

    private ArrayList<Baraja_de_usuario> leeBarajas_de_usuario(Usuario usuario)
            throws SQLException {
        String nombre = usuario.getNombre();
        ArrayList<Baraja_de_usuario> lista_de_barajas = new ArrayList<Baraja_de_usuario>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(
                    "select * from barajas_usuarios inner join barajas "
                            + "on barajas_usuarios.baraja = barajas.baraja "
                            + "where nombre ='" + nombre + "' order by tier");
                    
            if (!rs.next()) {
                return lista_de_barajas;
            } else {
                do {
                    Baraja_de_usuario b = new Baraja_de_usuario();
                    b.setNombre_baraja(rs.getString("baraja"));
                    b.setGm(rs.getInt("gm"));
                    b.setEm(rs.getInt("em"));
                    b.setPm(rs.getInt("pm"));
                    b.setGs(rs.getInt("gs"));
                    b.setEs(rs.getInt("es"));
                    b.setPs(rs.getInt("ps"));
                    lista_de_barajas.add(b);
                } while (rs.next());
                return lista_de_barajas;
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public boolean guardarBaraja(Baraja baraja) throws SQLException {
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            resultUpdate = st.executeUpdate("INSERT INTO barajas VALUES ('"
                    + baraja.getNombre()
                    + "', " + baraja.getTier() + ")");

            if (resultUpdate != 0) {
                return true;
            } else {
                System.out.println("no se ha agregado la baraja");
                return false;
            }
        } catch (Exception e) {
            System.out.println("fallo la base de datos");
            e.printStackTrace();
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

    public ArrayList<Baraja> leeBarajas() throws SQLException {
        ArrayList<Baraja> barajas = new ArrayList<Baraja>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM barajas order by tier");

            if (!rs.next()) {
                return barajas;
            } else {
                do {
                    barajas.add(new Baraja(rs.getString("baraja"), rs.getInt("tier")));
                } while (rs.next());
                return barajas;
            }
        } catch (Exception e) {
            System.out.println("fallo la base de datos");
            e.printStackTrace();
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

    public Baraja lee_baraja_por_nombre(String nombre_baraja) throws SQLException {
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select * from barajas where baraja ='"
                    + nombre_baraja + "'");
            
            if (! rs.next()){
                return new Baraja("no hay", 0);
            } else {
                return new Baraja(rs.getString("baraja"), rs.getInt("tier"));
            }

        } catch (Exception e) {
            System.out.println("fallo la base de datos");
            e.printStackTrace();
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
    
    public boolean actualizarBaraja(String vieja, String nombre, int tier) throws SQLException{
      try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            resultUpdate = st.executeUpdate(
                    "update barajas set baraja = '"
                            + nombre + "', tier = '"
                            + tier + "' where baraja = '"
                            + vieja + "'"); 
                   
            if (resultUpdate != 0){
                return true;
            } else {
                return false;
            }
            
      } catch (Exception e) {
            System.out.println("fallo la base de datos");
            e.printStackTrace();
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
