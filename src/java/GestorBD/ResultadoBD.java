package GestorBD;

import Modelo.Cruce;
import Modelo.Resultado;
import Modelo.ResultadoUsuarioBaraja;
import Modelo.ResultadoUsuarioGlobal;
import Modelo.Torneo;
import Util.PoolDeConexiones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de gestión de datos y comnunicación con la base de datos para la introduccion de resultados de torneos
 *
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 */
public class ResultadoBD {

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
    public ResultadoBD() {
        l = LoggerFactory.getLogger(ResultadoBD.class);
    }


    /**
     * Metodo que devuelve un arraylist con los nombres de las barajas ordenadas por tier. 
     * @return barajas devuelve un arraylist de String con los nombres de las barajas de la base de datos.
     * @throws SQLException posible excepción durante el cierre de recursos
     */
    public ArrayList<String> lee_nombres_barajas() throws SQLException {
        ArrayList<String> barajas = new ArrayList<>();
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement("SELECT nombre_baraja FROM barajas order by tier");
            rs = st.executeQuery();

            //no puedo hacer rs.next porque puede no haber barajas y tampoco quiero un nullPointer
            if (rs.next()){
                do {
                    barajas.add(rs.getString("nombre_baraja"));
                } while (rs.next());
            }
            con.commit();
            return barajas;
            
        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
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
     * Método que devuelve un ArrayList de String con los nombres de las barajas asignadas a un usuario.
     * No necesita comprobar si el usuario existe porque viene de la variable de sesión activa desde el login.
     * @param nombre_usuario nombre del usuario del que se quieren recuperar las barajas.
     * @return barajas_de_usuario ArrayList con los nombres de las barajas.
     * @throws SQLException posible excepción durante el cierre de recursos.
     */
    public ArrayList<String> devolver_nombres_barajas_de_usuario(String nombre_usuario) throws SQLException {
        ArrayList<String> barajas_de_usuario = new ArrayList<>();
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement("SELECT nombre_baraja FROM barajas_usuarios "
                    + "where nombre_usuario = ?");
            st.setString(1, nombre_usuario);
            rs = st.executeQuery();

            while (rs.next()) {
                barajas_de_usuario.add(rs.getString("nombre_baraja"));
            }

            con.commit();
            return barajas_de_usuario;

        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
            return barajas_de_usuario;
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
     * Método auxiliar que cumple varias funciones dependiendo de los argumentos que reciba:
     *  -caso1: recibe una sola baraja que no existe: si baraja_2 es nulo, hace una select de la tabla BARAJAS para
     *            cargar los datos de baraja_1 (partidas ganadas y perdidas de main y side de esta baraja).
     *  -caso2: recibe dos barajas (baraja_2 no es nula): busca en la tabla CRUCES si existe una entrada
     *            que relacione a estas dos barajas. Si lo encuentra carga los valores de dicho cruce en unas variables
     *            locales, y, si no existe, inicializa las variables locales a 0. 
     * NOTA: no hay que comprobar si las barajas existen porque vienen seleccionadas de una lista cargada
     * previamente con todas las barajas de la base de datos. 
     * 
     * @param baraja1 primera baraja del par.
     * @param baraja2 segunda baraja del par.
     * @return Resultado devuelve los datos del cruce entre esas dos barajas.
     * @throws SQLException posible excepción durante el cierre de recursos.
     */
    public Resultado obtener_resultados(String baraja1, String baraja2) throws SQLException {
        Resultado res = new Resultado();
        res.setBaraja1(baraja1);
        if (baraja2 != null) {
            res.setBaraja2(baraja2);
        }

        try {

            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();

            if (baraja2 == null) {
                st = con.prepareStatement(
                        "select ganadas_main, ganadas_side, perdidas_main, "
                        + "perdidas_side from barajas where nombre_baraja = ?");
                st.setString(1, baraja1);
            } else {
                st = con.prepareStatement(
                        "select ganadas_main_1, perdidas_main_1, ganadas_side_1, "
                        + "perdidas_side_1 from cruces where nombre_baraja_1 = ?"
                        + " and nombre_baraja_2 = ?");
                st.setString(1, baraja1);
                st.setString(2, baraja2);
            }

            rs = st.executeQuery();

            if (!rs.next()) {
                res.setMain1(0);
                res.setMain2(0);
                res.setSide1(0);
                res.setSide2(0);
            } else {
                // tienen que ser numeros por el distinto nombre de las columnas con 1 o 2 barajas
                res.setMain1(rs.getInt(1));
                res.setMain2(rs.getInt(2));
                res.setSide1(rs.getInt(3));
                res.setSide2(rs.getInt(4));
            }

            con.commit();
            return res;

        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
            return res;
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
     * Método que recibe como parametro un objeto  Resultado con los valores de victorias y derrotas de main y side y los
     * porcentajes correspondientes de baraja1 sobre baraja2. Si Baraja2 es nulo los datos se insertan en
     * la tabla barajas ya que se estan actualizado los datos globales de una unica baraja.
     * 
     * Si, por otro lado, baraja2 no es nulo, entonces se tienen que actualizar los valores en la tabla cruces,
     * y a su vez el metodo valora si el parametro existeCruce es verdadero para hacer un update o si es falso
     * para hacer un insert, en funcion de si ese cruce existia ya previamente o no. 
     * 
     * @param res variable resultado con los datos que se quieren introducir en la base de datos
     * @param existeCruce variable booleana para saber si hay que hacer insert o update
     * @return true o false en funcion de si la transaccion ha funcionado o no
     * @throws SQLException posible excepcion durante el cierre de recursos
     */
    public boolean introducir_variable_resultado(Resultado res, boolean existeCruce) throws SQLException {
        boolean resultado;
        try {

            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();

            if (res.getBaraja2() == null) {
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

                st.setInt(1, res.getMain1());
                st.setInt(2, res.getMain2());
                st.setInt(3, res.getSide1());
                st.setInt(4, res.getSide2());
                st.setFloat(5, res.getPorcentaje_main());
                st.setFloat(6, res.getPorcentaje_side());
                st.setFloat(7, res.getPorcentaje_total());
                st.setString(8, res.getBaraja1());
                
                resultado = st.executeUpdate() == 1;

            } else { //si baraja2 no es nulo hay que actualizar cruces, 
                //pero si el cruce antes no existia hay que insertarlo.

                if (existeCruce) {
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
                    
                    st.setInt(1, res.getMain1());
                    st.setInt(2, res.getMain2());
                    st.setInt(3, res.getSide1());
                    st.setInt(4, res.getSide2());
                    st.setFloat(5, res.getPorcentaje_main());
                    st.setFloat(6, res.getPorcentaje_side());
                    st.setFloat(7, res.getPorcentaje_total());
                    st.setString(8, res.getBaraja1());
                    st.setString(9, res.getBaraja2());
                    
                    resultado = st.executeUpdate() == 1;

                    
                } else { 
                    st = con.prepareStatement(
                            "insert into cruces values (?,?,?,?,?,?,?,?,?)");
                            
                    st.setString(1, res.getBaraja1());
                    st.setString(2, res.getBaraja2());
                    st.setInt(3, res.getMain1());
                    st.setInt(4, res.getMain2());
                    st.setInt(5, res.getSide1());
                    st.setInt(6, res.getSide2());
                    st.setFloat(7, res.getPorcentaje_main());
                    st.setFloat(8, res.getPorcentaje_side());
                    st.setFloat(9, res.getPorcentaje_total());
                    
                    resultado = st.executeUpdate() == 1;
                    
                }
                
            }

            con.commit();
            return resultado;

        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
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


    /**
     * Método que recibe como parámetros el nombre de un usuario y de una baraja y devuelve un objeto
     * resultadoUsuarioBaraja que contiene las partidas ganadas y perdidas de main y side y las rondas
     * ganadas, perdidas y empatadas de ese usuario con esa baraja. 
     * 
     * @param nombre_usuario usuario del que se quieren obtener los datos.
     * @param nombre_baraja baraja de la que se quieren obtener los datos.
     * @return ResultadoUsuarioBaraja que contiene los datos anteriormente mencionados.
     * @throws SQLException posible excepción durante el cierre de recursos.
     */
    public ResultadoUsuarioBaraja obtener_resultado_usuario_con_baraja(String nombre_usuario, String nombre_baraja)
            throws SQLException {

        ResultadoUsuarioBaraja res = new ResultadoUsuarioBaraja();
        res.setNombre_usuario(nombre_usuario);
        res.setNombre_baraja(nombre_baraja);

        try {

            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();

            st = con.prepareStatement(
                    "select rondas_ganadas, rondas_perdidas, rondas_empatadas, partidas_ganadas_main, "
                    + "partidas_perdidas_main, partidas_ganadas_side, partidas_perdidas_side "
                    + "from barajas_usuarios where nombre_baraja = ? "
                    + "and nombre_usuario = ?");

            st.setString(1, nombre_baraja);
            st.setString(2, nombre_usuario);

            rs = st.executeQuery();

            if (!rs.next()) {
                res.setGanadasMain(0);
                res.setPerdidasMain(0);
                res.setGanadasSide(0);
                res.setPerdidasSide(0);
                res.setRondasGanadas(0);
                res.setRondasPerdidas(0);
                res.setRondasEmpatadas(0);
            } else {
                // tienen que ser numeros por el distinto nombre de las columnas con 1 o 2 barajas
                res.setRondasGanadas(rs.getInt(1));
                res.setRondasPerdidas(rs.getInt(2));
                res.setRondasEmpatadas(rs.getInt(3));
                res.setGanadasMain(rs.getInt(4));
                res.setPerdidasMain(rs.getInt(5));
                res.setGanadasSide(rs.getInt(6));
                res.setPerdidasSide(rs.getInt(7));

            }

            con.commit();
            return res;

        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
            return res;
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
     * Método que recibe un objeto ResultadoUsuarioBaraja e introduce sus datos en la tabla barajas_usuarios.
     * @param res objeto resultado con los datos a introducir.
     * @return true o false en función de si la transacción funciona o no.
     * @throws SQLException Posible excepción producida durante el cierre de recursos.
     */
    public boolean introducir_variable_resultado_usuario_con_baraja(ResultadoUsuarioBaraja res)
            throws SQLException {
        boolean resultado;
        try {

            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();

            st = con.prepareStatement(
                    "update barajas_usuarios set "
                    + "rondas_ganadas = ?, "
                    + "rondas_empatadas = ?, "
                    + "rondas_perdidas = ?, "
                    + "partidas_ganadas_main = ?, "
                    + "partidas_perdidas_main = ?, "
                    + "partidas_ganadas_side = ?, "
                    + "partidas_perdidas_side = ?, "
                    + "porcentaje_rondas_ganadas = ?, "
                    + "porcentaje_partidas_ganadas_main = ?, "
                    + "porcentaje_partidas_ganadas_side = ?, "
                    + "porcentaje_partidas_ganadas_total = ? "
                    + "where nombre_baraja = ? "
                    + "and nombre_usuario = ?");

            st.setInt(1, res.getRondasGanadas());
            st.setInt(2, res.getRondasEmpatadas());
            st.setInt(3, res.getRondasPerdidas());
            st.setInt(4, res.getGanadasMain());
            st.setInt(5, res.getPerdidasMain());
            st.setInt(6, res.getGanadasSide());
            st.setInt(7, res.getPerdidasSide());
            st.setFloat(8, res.getPorcentajeRondasGanadas());
            st.setFloat(9, res.getPorcentajeMain());
            st.setFloat(10, res.getPorcentajeSide());
            st.setFloat(11, res.getPorcentajeTotal());
            st.setString(12, res.getNombre_baraja());
            st.setString(13, res.getNombre_usuario());

            resultado = st.executeUpdate() == 1;
          
            con.commit();
            return resultado;

        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
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

    /**
     * Método que introduce el resultado de un torneo en la base de datos. Este resultado está compuesto
     * por el nombre del usuario, el nombre de la baraja que ha utilizado, los puntos obtenidos y la cadena
     * resultado, que es un String de la forma "G-P-E", siendo G rondas ganadas, E empatadas y P perdidas.
     * A tener en consideración que no hay que comprobar si el usuario y la baraja existen previamente porque
     * vienen de un contexto de selección que no da lugar a ello.
     * @param nombre_usuario usuario que ha obtenido el resultado.
     * @param nombre_baraja baraja que se ha utilizado en el torneo.
     * @param puntos número de puntos obtenidos.
     * @param cadena_resultado cadena con el resultado descrita anteriormente.
     * @return true o false en función de si la transacción funciona o no.
     * @throws SQLException posible excepción durante el cierre de recursos.
     */
    public boolean guardar_resultado_final_torneo(String nombre_usuario, String nombre_baraja, int puntos,
            String cadena_resultado)
            throws SQLException {
        boolean retorno;
        try {
            // no tengo que comprobar usuario ni baraja porque sino el torneo no hubiese podido agregarse
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement(
                    "update torneos set repeticiones = repeticiones +1 where nombre_usuario = ? "
                    + "and nombre_baraja = ? and resultado = ?");
            st.setString(1, nombre_usuario);
            st.setString(2, nombre_baraja);
            st.setString(3, cadena_resultado);

            if (st.executeUpdate() == 1) {
                retorno = true;
            } else {

                st = con.prepareStatement("INSERT INTO torneos values (?, ?, ?, ?, 1)");
                st.setString(1, nombre_usuario);
                st.setString(2, nombre_baraja);
                st.setString(3, cadena_resultado);
                st.setInt(4, puntos);

                retorno = st.executeUpdate() == 1;
            }

            con.commit();
            return retorno;
        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
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

    /**
     * Método que lee todos los cruces para todas una baraja.
     * Esto es: dada una baraja, por su nombre, busca en la tabla cruces de la base de datos todas las 
     * entradas donde esta baraja aparece como "baraja_1", y, de cada una de esas entradas extrae el nombre
     * de la segunda baraja del cruce, las partidas ganadas y perdidas de main y side y los porcentajes. 
     * Introduce todos estos datos en un objeto Cruce, y los va añadiendo a un arrayList de Cruce que devuelve
     * al final.
     * @param nombre_baraja nombre de baraja de la que se quieren obtener los emparejamientos.
     * @return cruces devuelve los resultados de los cruces de la baraja pasada como parámetro.
     * @throws SQLException posible excepción durante el cierre de recursos.
     */
    public ArrayList<Cruce> leerCruces(String nombre_baraja) throws SQLException {
        ArrayList<Cruce> cruces = new ArrayList<>();
        Cruce cruce;

        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
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

            con.commit();
            return cruces;

        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
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

    /**
     * Método que devuelve todos los resultados de torneos de un usuario. Al haber varios registros va guardando
     * cada fila del resultSet en un objeto Cruce, que después añade al array que se devuelve al final.
     * @param nombre_usuario nombre del usuario cuyo historial de torneos se quiere devolver.
     * @return torneos devuelve un arrayList de Torneo con los resultados obtenidos por el usuario.
     * @throws SQLException posible excepción durante el cierre de recursos.
     */
    public ArrayList<Torneo> cargaTorneosDeUsuario(String nombre_usuario) throws SQLException {
        ArrayList<Torneo> torneos = new ArrayList<>();
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
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
            con.commit();
            return torneos;
        } catch (SQLException e) {
            con.rollback();
            l.error(e.getLocalizedMessage());
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
    
    /**
     * Método para obtener los resultados concretos de un usuario. Estos resultados son las rondas ganadas,
     * perdidas y empatadas y sus partidas ganadas y perdidas.
     * @param nombre_usuario nombre del usuario cuyos datos se quieren obtener.
     * @return ResultadoUsuarioGlobal devuelve un objeto con todos estos datos encapsulados.
     * @throws SQLException posible excepción durante el cierre de recursos.
     */
    public ResultadoUsuarioGlobal obtener_resultados_usuario(String nombre_usuario)
            throws SQLException{
        ResultadoUsuarioGlobal res = new ResultadoUsuarioGlobal();
        res.setNombre_usuario(nombre_usuario);
        
        try{
            
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
            st = con.prepareStatement(
                    "select rondas_ganadas, rondas_empatadas, rondas_perdidas, partidas_ganadas, "
                    + "partidas_perdidas from usuarios where nombre_usuario = ?");
            st.setString(1, nombre_usuario);
            rs = st.executeQuery();

            if (rs.next()){
                res.setRondas_ganadas(rs.getInt("rondas_ganadas"));
                res.setRondas_perdidas(rs.getInt("rondas_perdidas"));
                res.setRondas_empatadas(rs.getInt("rondas_empatadas"));
                res.setPartidas_ganadas(rs.getInt("partidas_ganadas"));
                res.setPartidas_perdidas(rs.getInt("partidas_perdidas"));             
            }
            
            
            con.commit();
            return res;
        
        }catch (SQLException e) {
           con.rollback();
            l.error(e.getLocalizedMessage());
            return res;
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
     * Método para introducir los resultados de un usuario. 
     * @param res resultado a introducir en la base de datos.
     * @return true o false si la transacción funciona o no.
     * @throws SQLException posible excepción durante el cierre de recursos.
     */
    public boolean introducir_resultados_usuario(ResultadoUsuarioGlobal res)
            throws SQLException {
        boolean resultado;
        try {
            PoolDeConexiones pool = PoolDeConexiones.getInstance();
            con = pool.getConnection();
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
            
            st.setInt(1, res.getRondas_ganadas());
            st.setInt(2, res.getRondas_empatadas());
            st.setInt(3, res.getRondas_perdidas());
            st.setInt(4, res.getPartidas_ganadas());
            st.setInt(5, res.getPartidas_perdidas());
            st.setFloat(6, res.getPorcentaje_rondas());
            st.setFloat(7, res.getPorcentaje_partidas());
            st.setString(8, res.getNombre_usuario());
            
            resultado = st.executeUpdate() == 1;
            con.commit();
            return resultado;
            
        } catch (SQLException e){
            con.rollback();
            l.error(e.getLocalizedMessage());
            return false;
        } finally {
            if (st != null){st.close();}
            if (con != null){con.close();}
        }
        
    }
        

}
