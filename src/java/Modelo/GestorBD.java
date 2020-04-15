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

    public ArrayList<Usuario> leerUsuarios() {

        ArrayList<Usuario> usuarios = new ArrayList<>();
        Usuario usuarioHallado;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("Select * from usuarios");
            rs = st.executeQuery();

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

        } catch (SQLException e) {
        }
        return null;
    }

    public boolean guardarUsuario(Usuario usuario) {
        boolean retorno = false;
        try {
            ConectaBD conectaBD = new ConectaBD();
            boolean superUser = false;

            con = conectaBD.getConnection();
            st = con.prepareStatement("Select * from usuarios");
            rs = st.executeQuery();

            if (!rs.next()) {
                superUser = true;
            }

            st = con.prepareStatement("INSERT INTO usuarios VALUES"
                    + " ?,?,?,?,?,?,?,?,?,?,?,?,?");
            st.setString(1, usuario.getNombre());
            st.setString(2, usuario.getClave());
            st.setInt(3, 0);
            st.setInt(4, 0);
            st.setInt(5, 0);
            st.setInt(6, 0);
            st.setInt(7, 0);
            st.setInt(8, 0);
            st.setInt(9, 0);
            st.setInt(10, 0);
            st.setInt(11, 0);
            st.setInt(12, 0);
            st.setBoolean(13, superUser);

            resultUpdate = st.executeUpdate();

            retorno = (resultUpdate != 0);
            //con.commit();

        } catch (SQLException e) {
            //con.rollback();
        }
        return retorno;
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
            st = con.prepareStatement("SELECT superUser FROM usuarios WHERE nombre = ? and clave = ?");
            st.setString(1, usuario.getNombre());
            st.setString(2, usuario.getClave());
            rs = st.executeQuery();

            if (!rs.next()) {
                devolver = 0;
            } else {
                if (rs.getBoolean(1)) {
                    devolver = 2;
                } else {
                    devolver = 1;
                }
            }
            //con.commit();
        } catch (SQLException e) {
            //con.rollback();
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

    public Usuario cargarUsuario(Usuario usuario) throws SQLException {
        Usuario devolver = null;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("SELECT * FROM usuarios WHERE nombre = ? and clave = ?");

            st.setString(1, usuario.getNombre());
            st.setString(2, usuario.getClave());

            rs = st.executeQuery();

            if (!rs.next()) {
                devolver = null;
            } else {
                devolver = new Usuario(usuario.getNombre(), usuario.getClave());
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
                ArrayList<Baraja_de_usuario> barajas = leeBarajas_de_usuario(usuario);
                devolver.setLista_de_barajas_de_usuario(barajas);
            }

        } catch (SQLException e) {
            //con.rollback();
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
        String nombre = usuario.getNombre();
        ArrayList<Baraja_de_usuario> lista_de_barajas = new ArrayList<>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement(
                    "select * from barajas_usuarios inner join barajas "
                    + "on barajas_usuarios.baraja = barajas.baraja "
                    + "where nombre = ? order by tier");
            st.setString(1, nombre);
            rs = st.executeQuery();

            if (!rs.next()) {
                return lista_de_barajas;
            } else {
                do {
                    Baraja_de_usuario b = new Baraja_de_usuario();
                    b.setNombre_baraja(rs.getString("baraja"));
                    b.setGm(rs.getInt("gm"));
                    b.setPm(rs.getInt("pm"));
                    b.setGs(rs.getInt("gs"));
                    b.setPs(rs.getInt("ps"));
                    lista_de_barajas.add(b);
                } while (rs.next());
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

    public ArrayList<String> devolver_barajas_de_usuario(String nombre_usuario) throws SQLException {
        ArrayList<String> barajas = new ArrayList<>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement(
                    "SELECT baraja from barajas_usuarios where nombre = ?");
            st.setString(1, nombre_usuario);
            rs = st.executeQuery();

            while (rs.next()) {
                barajas.add(rs.getString("baraja"));
            }
            return barajas;

        } catch (SQLException e) {
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

    public boolean guardarBaraja(Baraja baraja) throws SQLException {
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("INSERT INTO barajas VALUES (?, ?)");
            st.setString(1, baraja.getNombre());
            st.setInt(2, baraja.getTier());

            resultUpdate = st.executeUpdate();

            if (resultUpdate != 0) {
                return true;
            } else {
                System.out.println("no se ha agregado la baraja");
                return false;
            }
        } catch (SQLException e) {
            //con.rollback();
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
        ArrayList<Baraja> barajas = new ArrayList<>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("SELECT * FROM barajas order by tier");
            rs = st.executeQuery();

            if (!rs.next()) {
                return barajas;
            } else {
                do {
                    barajas.add(new Baraja(rs.getString("baraja"), rs.getInt("tier")));
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

    public int devuelve_tier_baraja(String nombre_baraja) throws SQLException {
        int devolver;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("SELECT * FROM barajas where baraja = ?");
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

    public boolean actualizarBaraja(String modificar, String nuevo_nombre, int tier_nuevo) throws SQLException {
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("UPDATE barajas SET baraja = ?, tier = ? where baraja = ?");
            st.setString(1, nuevo_nombre);
            st.setInt(2, tier_nuevo);
            st.setString(3, modificar);

            resultUpdate = st.executeUpdate();

            return resultUpdate != 0;

        } catch (SQLException e) {
            //con.rollback;
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
            st = con.prepareStatement("SELECT * FROM barajas where baraja = ?");
            st.setString(1, nombre_baraja);
            rs = st.executeQuery();

            if (!rs.next()) {
                return retorno;
            } else {
                // la baraja si existe. Se hace el insert

                st = con.prepareStatement("INSERT INTO barajas_usuarios values (?, ?, ?, ?, ?, ?, ?, ?, ?");
                st.setString(1, nombre_usuario);
                st.setString(2, nombre_baraja);
                st.setInt(3, 0);
                st.setInt(4, 0);
                st.setInt(5, 0);
                st.setInt(6, 0);
                st.setInt(7, 0);
                st.setInt(8, 0);
                st.setInt(9, 0);

                resultUpdate = st.executeUpdate();
                if (resultUpdate != 0) {
                    retorno = true;
                }
                return retorno;
            }
        } catch (SQLException e) {
            //con.rollback();
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
            st = con.prepareStatement(
                    "SELECT * FROM cruces WHERE (baraja1 = ? and baraja2 = ?)");
            st.setString(1, baraja1);
            st.setString(2, baraja2);
            rs = st.executeQuery();

            if (!rs.next()) {
                porcentaje = (main1 + side1) / (main1 + main2 + side1 + side2);

                st = con.prepareStatement(
                        "INSERT INTO cruces VALUES (?, ?, ?, ?, ?, ?, ?)");

                st.setString(1, baraja1);
                st.setString(2, baraja2);
                st.setInt(3, main1);
                st.setInt(4, main2);
                st.setInt(5, side1);
                st.setInt(6, side2);
                st.setFloat(7, porcentaje);

                resultUpdate = st.executeUpdate();

                if (resultUpdate != 1) {
                    retorno = false;
                }
            } else {

                main1 = main1 + rs.getInt(3);
                main2 = main2 + rs.getInt(4);
                side1 = side1 + rs.getInt(5);
                side2 = side2 + rs.getInt(6);
                porcentaje = (main1 + side1) / (main1 + main2 + side1 + side2);

                st = con.prepareStatement(
                        "UPDATE cruces SET gm1 = ?, gm2 = ?, gs1 = ?, gs2 = ?, porcentaje = ? where baraja1 = ? and baraja2 = ?");

                st.setInt(1, main1);
                st.setInt(2, main2);
                st.setInt(3, side1);
                st.setInt(4, side2);
                st.setFloat(5, porcentaje);
                st.setString(6, baraja1);
                st.setString(7, baraja2);

                resultUpdate = st.executeUpdate();

                if (resultUpdate != 1) {
                    retorno = false;
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
    }

    public boolean usuario_tiene_baraja(String nombre_usuario, String baraja) throws SQLException {
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();

            st = con.prepareStatement(
                    "SELECT * FROM barajas_usuarios WHERE nombre = ? and baraja = ?");

            st.setString(1, nombre_usuario);
            st.setString(2, baraja);

            rs = st.executeQuery();

            return rs.next();

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

    public boolean existeBaraja(String baraja) throws SQLException {
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement(
                    "SELECT * from barajas WHERE baraja = ?");
            st.setString(1, baraja);

            rs = st.executeQuery();

            return rs.next();
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

    /*
    public boolean guardar_torneo(String usuario, String baraja1, String baraja2, int main1, int main2, int side1, int side2,
            int rondas_ganadas, int rondas_perdidas, int rondas_empatadas) throws SQLException {
        /*
        comprobaciones que pueden ser omitidas: si la baraja no esta asignada al usuario no podemos llegar
        al punto donde se lanza este metodo, luego no es necesario comprobar eso.
        Tampoco es necesario, por ende, comprobar que el usuario y la baraja existen.
     */
    public boolean guardar_torneo(String usuario, String baraja1, int main1, int main2, int side1, int side2,
            int rondas_ganadas, int rondas_perdidas, int rondas_empatadas) throws SQLException {

        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();

            st = con.prepareStatement(
                    "SELECT * FROM barajas_usuarios WHERE nombre = ? and baraja = ?");

            st.setString(1, usuario);
            st.setString(2, baraja1);

            boolean control = true;

            rs = st.executeQuery();

            if (!rs.next()) {
                control = false;
            }

            if (control) {

                int main1N = rs.getInt("gm") + main1;
                int main2N = rs.getInt("pm") + main2;
                int side1N = rs.getInt("gs") + side1;
                int side2N = rs.getInt("ps") + side2;

                int partidas_jugadas_total = main1N + main2N + side1N + side2N;

                float por_main = (main1 * 100) / (main1 + main2);
                float por_side = (side1 * 100) / (side1 + side2);
                float por_partidas_total = (main1 + side1) * 100 / partidas_jugadas_total;

                // con los porcentajes y los datos tengo que hacer un update en barajas_usuario
                st = con.prepareStatement(
                        "UPDATE barajas_usuarios SET gm=?, pm=?, gs=?, ps=?, por_main=?, por_side=?, por_total=? "
                        + "where nombre = ? and baraja = ?");
                st.setInt(1, main1);
                st.setInt(2, main2);
                st.setInt(3, side1);
                st.setInt(4, side2);
                st.setFloat(5, por_main);
                st.setFloat(6, por_side);
                st.setFloat(7, por_partidas_total);
                st.setString(8, usuario);
                st.setString(9, baraja1);

                resultUpdate = st.executeUpdate();

                if (resultUpdate != 1) {
                    control = false;
                }
            }

            // esto actualizaria las barajas de usuario. Ahora faltaria actualizar los datos del usuario
            if (control) {
                rs = st.executeQuery("select * from usuarios where nombre = '" + usuario + "'");
                if (!rs.next()) {
                    control = false;
                }
            }

            if (control) {

                int partidas_ganadas = rs.getInt("pg") + main1 + side1;
                int partidas_perdidas = rs.getInt("pp") + main2 + side2;
                int partidas_jugadas = rs.getInt("pj") + partidas_ganadas + partidas_perdidas;

                int rgN = rs.getInt("rg") + rondas_ganadas;
                int rpN = rs.getInt("rp") + rondas_perdidas;
                int reN = rs.getInt("re") + rondas_empatadas;
                int rjN = rs.getInt("rj") + rondas_ganadas + rondas_perdidas + rondas_empatadas;

                float por_rondas = (rgN / rjN) * 100;
                float por_partidas = (partidas_ganadas / partidas_jugadas) * 100;

                st = con.prepareStatement(
                        "UPDATE usuarios SET pj=?, pg=?, pe=0, pp=?, rj=?, rg=?, re=?, rp=?, "
                        + "por_rondas=?, por_partidas=? where nombre = ?");

                st.setInt(1, partidas_jugadas);
                st.setInt(2, partidas_ganadas);
                st.setInt(3, partidas_perdidas);
                st.setInt(4, rjN);
                st.setInt(5, rgN);
                st.setInt(6, reN);
                st.setInt(7, rpN);
                st.setFloat(8, por_rondas);
                st.setFloat(9, por_partidas);
                st.setString(10, usuario);

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
                    "update torneos set repeticiones = repeticiones +1 where nombre = ? and baraja = ? and resultado = ?");
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
                st.setInt(3, puntos);
                st.setString(4, cadena_resultado);

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

    public ArrayList<String> carga_todas_las_barajas() throws SQLException {
        ArrayList<String> barajas = new ArrayList<>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("SELECT baraja FROM barajas");
            rs = st.executeQuery();

            while (rs.next()) {
                barajas.add(rs.getString(1));
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

    public ArrayList<Torneo> cargaTorneosDeUsuario(String nombre_usuario) throws SQLException {
        ArrayList<Torneo> torneos = new ArrayList<>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("SELECT * FROM torneos WHERE nombre = ? order by puntos");
            st.setString(1, nombre_usuario);
            rs = st.executeQuery();

            while (rs.next()) {
                Torneo t = new Torneo();
                t.setNombre_baraja(rs.getString("baraja"));
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

    public ArrayList<String> cargaLinea(String baraja, ArrayList<String> todas_las_barajas) throws SQLException {
        ArrayList<String> linea = new ArrayList<String>();
        String baraja2;
        String float_toString;
        Float val_porcentaje;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("select porcentaje from cruces where baraja1 = ? and baraja2 = ?");
            st.setString(1, baraja);

            for (int i = 0; i < todas_las_barajas.size(); i++) {
                st.setString(2, todas_las_barajas.get(i));
                rs = st.executeQuery();
                if (!rs.next()) {
                    linea.add("0");
                } else {
                    val_porcentaje = rs.getFloat("porcentaje");
                    float_toString = val_porcentaje.toString();
                    linea.add(float_toString);
                }
            }

            return linea;

        } catch (SQLException e) {
            con.rollback();
            return linea;
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

    public ArrayList<LineaDelCuadro> cargaCuadro(ArrayList<String> todas_las_barajas) throws SQLException {
        //ArrayList<ValorDelCuadro> linea = new ArrayList<ValorDelCuadro>();
        LineaDelCuadro linea = new LineaDelCuadro();
        ArrayList<LineaDelCuadro> porcentajes = new ArrayList<LineaDelCuadro>();
        String baraja1, baraja2, float_toString;
        ValorDelCuadro valorDelCuadro;
        Float val_porcentaje;

        // necesito que la celda (0,0) de porcentajes este vacia.
        // itero sobre las barajas cogiendo los Strings y añadiendolos a la primera columna de porcentajes
        valorDelCuadro = new ValorDelCuadro("");
        linea.addValor(valorDelCuadro);
        for (int i=0; i<todas_las_barajas.size(); i++){
            linea.addValor(new ValorDelCuadro(todas_las_barajas.get(i)));
        }
        porcentajes.add(linea);

        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("select porcentaje from cruces where baraja1 = ? and baraja2 = ?");

            for (int i = 0; i < todas_las_barajas.size(); i++) {
                baraja1 = todas_las_barajas.get(i);
                linea = new LineaDelCuadro();
                //lo primero que añado es la baraja B2
                linea.addValor(new ValorDelCuadro(baraja1));
                st.setString(1, baraja1);
                for (int j = 0; j < todas_las_barajas.size(); j++) {
                    baraja2 = todas_las_barajas.get(j);
                    if (baraja1.equals(baraja2)) {
                        linea.addValor(new ValorDelCuadro("XX"));
                    } else {
                        st.setString(2, baraja2);
                        rs = st.executeQuery();
                        if (!rs.next()) {
                            linea.addValor(new ValorDelCuadro("no hay datos"));
                        } else {
                            val_porcentaje = rs.getFloat(1);
                            float_toString = val_porcentaje.toString();
                            linea.addValor(new ValorDelCuadro(float_toString));
                        }
                    }
                } // termina el bucle que itera con la segunda baraja
                porcentajes.add(linea);
            }

            return porcentajes;

        } catch (SQLException e) {
            con.rollback();
            return porcentajes;
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
