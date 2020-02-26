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
            rs = st.executeQuery("SELECT * FROM usuarios");

            /*
            esto permite que si el usuario se logea y es el primero adquiere automaticamente 
            derechos de superusuario
             */
            if (!rs.next()) {
                resultUpdate = st.executeUpdate("INSERT INTO usuarios VALUES ('"
                        + usuario.getNombre()
                        + "', '" + usuario.getClave() + "',0,0,0,0,0,0,0,0,0,0,true);");
            } else {
                resultUpdate = st.executeUpdate("INSERT INTO usuarios VALUES ('"
                        + usuario.getNombre()
                        + "', '" + usuario.getClave() + "',0,0,0,0,0,0,0,0,0,0,false);");
            }

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

    public int devuelve_tier_baraja(String nombre_baraja) throws SQLException {
        int devolver;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select * from barajas where baraja ='"
                    + nombre_baraja + "'");

            if (!rs.next()) {
                devolver = 0;
            } else {
                devolver = rs.getInt("tier");
            }

            return devolver;

        } catch (Exception e) {
            System.out.println("fallo la base de datos");
            e.printStackTrace();
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

    public boolean actualizarBaraja(String modificar, String nuevo_nombre, int tier_nuevo) throws SQLException {
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            resultUpdate = st.executeUpdate(
                    "update barajas set baraja = '"
                    + nuevo_nombre + "', tier = '"
                    + tier_nuevo + "' where baraja = '"
                    + modificar + "'");

            if (resultUpdate != 0) {
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

    public boolean agregar_baraja_a_usuario(String nombre_usuario, String nombre_baraja) throws SQLException {
        boolean retorno = false;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            // no compruebo si el usuario es correcto porque viene del login. si tengo que comprobar
            // si la baraja existe

            rs = st.executeQuery("SELECT * FROM barajas WHERE baraja = '" + nombre_baraja + "'");

            if (!rs.next()) {
                return retorno;
            } else {
                // la baraja si existe. Se hace el insert
                resultUpdate = st.executeUpdate("insert into barajas_usuarios values ('"
                        + nombre_usuario + "', '"
                        + nombre_baraja + "', 0,0,0,0,0,0,0,0,0);");
                if (resultUpdate != 0) {
                    retorno = true;
                }
                return retorno;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return retorno;
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

    public boolean introducirResultado(String baraja1, String baraja2, int main1, int main2,
            int side1, int side2) throws SQLException {
        // NOTA: LAS BARAJAS YA DEBEN VENIR ORDENADAS
        boolean retorno = true;
        float porcentaje;
        try {

            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();

            rs = st.executeQuery(
                    "SELECT * FROM cruces WHERE (baraja1 = '" + baraja1 + "' and baraja2 = '" + baraja2 + "');");
            if (!rs.next()) {
                porcentaje = (main1 + side1) / (main1 + main2 + side1 + side2);
                resultUpdate = st.executeUpdate(
                        "INSERT INTO cruces VALUES ('"
                        + baraja1 + "', '"
                        + baraja2 + "', "
                        + main1 + ", "
                        + main2 + ", "
                        + side1 + ", "
                        + side2 + ", "
                        + porcentaje + ");");
                if (resultUpdate != 1) {
                    retorno = false;
                }
            } else {

                main1 = main1 + rs.getInt(3);
                main2 = main2 + rs.getInt(4);
                side1 = side1 + rs.getInt(5);
                side2 = side2 + rs.getInt(6);
                porcentaje = (main1 + side1) / (main1 + main2 + side1 + side2);
                resultUpdate = st.executeUpdate(
                        "UPDATE cruces SET gm1 = "
                        +main1+", gm2 = "
                        +main2+", gs1 = "
                        +side1+", gs2 = "
                        +side2+", porcentaje = "
                        +porcentaje+" where baraja1 = '"
                        +baraja1+"' and baraja2 = '"
                        +baraja2+"'");
                        
                        
                        /*
                        "UPDATE cruces SET gm1 = "
                        + main1 + ", gm2 = "
                        + main2 + ", gs1 = "
                        + side1 + ", gs2 = "
                        + side2 + ", porcentaje = "
                        + porcentaje + " where baraja1 = '"
                        + baraja1 + "' and baraja2 = '"
                        + baraja2 + "');");*/
                if (resultUpdate != 1) {
                    retorno = false;
                }
            }
            return retorno;

        } catch (SQLException e) {
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
    
    public boolean usuario_tiene_baraja(String nombre_usuario, String baraja) throws SQLException{
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            
            rs = st.executeQuery(
                    "SELECT * FROM barajas_usuarios WHERE (nombre = '" 
                            + nombre_usuario + "' and baraja = '" 
                            + baraja + "');");
            
            if (! rs.next()){
                return false;
            } else {
                return true;
            }
            
        } catch (SQLException e) {
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
    
    public boolean existeBaraja(String baraja) throws SQLException {
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(
                    "select * from barajas where baraja = '" + baraja + "'");

            if (!rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
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
