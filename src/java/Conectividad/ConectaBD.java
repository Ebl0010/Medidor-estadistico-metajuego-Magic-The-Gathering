package Conectividad;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.faces.context.FacesContext;

public class ConectaBD {

    private Connection conexion = null;
    private String servidor = "localhost";
    private String database = "default_schema";
    private String usuario = "root";
    private String password = "1234";
    private String url = "";

    public ConectaBD() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://" + servidor + "/" + database;
            conexion = DriverManager.getConnection(url, usuario,
                    password);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


public Connection getConnection() {
        return conexion;
    }

    public Connection cerrarConexion() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        conexion = null;
        return conexion;
    }
}
