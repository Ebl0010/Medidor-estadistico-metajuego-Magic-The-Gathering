package Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que implementa el pool de conexiones en base al patrón singleton para tener
 * una única instancia.
 * 
 * @author <a href="mailto:ebl0010@alu.ubu.es">Eric Berlinches</a>
 * 
 * Clase MUY BASADA en el código proporcionado en la asignatura Aplicaciones de Bases de Datos,
 * con los autores:
 * @author <a href="mailto:jmaudes@ubu.es">Jesus Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Raul Marticorena</a>
 */
public class PoolDeConexiones {

    /**
     * Variable para almacenar el datasource.
     */
    private final DataSource dataSource;
    
    /**
     * variable para instanciar el pool de conexiones.
     */
    private static PoolDeConexiones pool;
    
    /**
     * atributo logger para recoger las trazas de error de las excepciones
     */
    private static Logger l = null;

    /**
     * Constructor privado que carga las propiedades de la conexión mediante un fichero externo al código
     * y después crea un DataSource con dichas propiedades.
     * @throws IOException posible excepción durante la lectura del fichero.
     * @throws Exception 
     */
    private PoolDeConexiones() throws IOException, Exception {
        InputStream fichero_entrada;
        l = LoggerFactory.getLogger(PoolDeConexiones.class);
        Properties propiedades = new Properties();
        try {
            fichero_entrada = new FileInputStream(
                    new File(
                            "C:\\Users\\admin\\Documents\\NetBeansProjects\\WebApplication5.5\\res\\config.properties"));
            propiedades.load(fichero_entrada);

            //propiedades.load(new FileInputStream("config"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dataSource = BasicDataSourceFactory.createDataSource(propiedades);
    }

    /**
     * Método heredado del código de Aplicaciones de bases de datos. 
     * Si el atributo pool está a nulo llama al constructor privado, y en cualquier caso, devuelve el atributo pool.
     * @return pool devuelve siempre la misma instancia del pool de conexiones.
     */
    public static PoolDeConexiones getInstance() {
        if (pool == null) {
            try {
                pool = new PoolDeConexiones();
            } catch (Exception e) {
                //e.printStackTrace();
                Herramientas.lanza_mensaje(TipoMensaje.ERROR,
                        "Se ha producido un error de conectividad con la base de datos",
                        "index.xhtml");
            }
        }
        return pool;
    }
    
    /**
     * Método que devuelve una conexión del dataSource. 
     * @return con deuvelve una conexión lógica.
     */
    public Connection getConnection(){
        Connection con = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
        } catch (SQLException ex) {
            Herramientas.lanza_mensaje(TipoMensaje.ERROR,
                        "Se ha producido un error de conectividad con la base de datos",
                        "index.xhtml");
        }
        
        return con;
    }

}
