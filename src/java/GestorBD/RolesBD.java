/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestorBD;

import Conectividad.ConectaBD;
import Modelo.RolUsuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class RolesBD {
    
    private Connection con = null;
    private PreparedStatement st = null;
    private ResultSet rs = null;
    private int resultUpdate = 0;
    
    public ArrayList<String> carga_todos_los_roles() throws SQLException {
        ArrayList<String> roles = new ArrayList<String>();
        String rol;
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement("Select descripcion from roles");
            rs = st.executeQuery();
            while (rs.next()) {
                rol = rs.getString("descripcion");
                roles.add(rol);
            }
            return roles;

        } catch (SQLException e) {
            return null;
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

    }
    
    
    public ArrayList<RolUsuario> lee_solicitudes(String nombre_usuario)
            throws SQLException {
        RolUsuario rol;
        ArrayList<RolUsuario> solicitudes = new ArrayList<RolUsuario>();
        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
            st = con.prepareStatement(
                    "select descripcion, estado " +
                    "from roles_usuarios left join roles on roles_usuarios.idRol = roles.idRol " +
                    "where nombre_usuario = ? and estado = 1 or estado = 2");
            st.setString(1, nombre_usuario);
            rs = st.executeQuery();
            while (rs.next()){
                rol = new RolUsuario();
                rol.setEstado(rs.getInt("estado"));
                rol.setDescripcion_rol(rs.getString("descripcion"));
                rol.traducirEstado();
                solicitudes.add(rol);
            }
            return solicitudes;
        } catch (SQLException e) {
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
    
    
    
    public boolean gestionar_peticion_rol(String nombre_usuario, String rol, String rol_solicitado)
            throws SQLException {
        boolean retorno = true;
        int valor_rol_actual = 0, valor_rol_solicitado;

        try {
            ConectaBD conectaBD = new ConectaBD();
            con = conectaBD.getConnection();
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
                    st = con.prepareStatement("insert into roles_usuarios values (?, ?, 1)");
                    st.setString(1, nombre_usuario);
                    st.setInt(2, valor_rol_solicitado);
                    retorno = (st.executeUpdate() == 1);
                }
            }

            return retorno;

        } catch (SQLException e) {
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
