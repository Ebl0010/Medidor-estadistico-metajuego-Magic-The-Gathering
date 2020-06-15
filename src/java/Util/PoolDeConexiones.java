/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author admin
 */
public class PoolDeConexiones {

    private final DataSource dataSource;
    private static PoolDeConexiones pool;

    private PoolDeConexiones() throws IOException, Exception {
        InputStream fichero_entrada;
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
