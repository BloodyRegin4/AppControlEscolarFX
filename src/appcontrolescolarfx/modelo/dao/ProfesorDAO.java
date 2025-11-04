package appcontrolescolarfx.modelo.dao;

import appcontrolescolarfx.modelo.pojo.Profesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfesorDAO {
    public static ResultSet obtenerProfesores(Connection conexionBaseDatos) throws SQLException {
        if(conexionBaseDatos != null){  
            String consulta = "SELECT idProfesor, nombre, apellidoPaterno, apellidoMaterno," +
                    "numeroPersonal, fechaNacimiento, fechaContratacion, profesor.idRol, rol " +
                    "FROM profesor " +
                    "INNER JOIN rol ON rol.idRol = profesor.idRol;";
            PreparedStatement sentencia = conexionBaseDatos.prepareStatement(consulta);
            return sentencia.executeQuery();
              
        }

      throw new SQLException("No hay conexión a la base de datos."); 
    }
    
    public static int registrarProfesor(Profesor profesor, Connection conexionBaseDatos) throws SQLException {
        if(conexionBaseDatos != null) {
            String insercion = "INSERT INTO profesor (idRol, nombre, apellidoPaterno, apellidoMaterno, " +
                    "numeroPersonal, contrasenia, fechaNacimiento, fechaContratacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement sentencia =  conexionBaseDatos.prepareStatement(insercion);
            sentencia.setInt(1, profesor.getIdRol());
            sentencia.setString(2, profesor.getNombre());
            sentencia.setString(3, profesor.getApellidoPaterno());
            sentencia.setString(4, profesor.getApellidoMaterno());
            sentencia.setString(5, profesor.getNumeroPersonal());
            sentencia.setString(6, profesor.getContrasenia());
            sentencia.setString(7, profesor.getFechaNacimiento());
            sentencia.setString(8, profesor.getFechaContratacion());
            return sentencia.executeUpdate();
        }
        
        throw new SQLException("No hay conexión a la base de datos");
    }

    public static int editarProfesor(Profesor profesor, Connection conexionBaseDatos) throws SQLException {
        if(conexionBaseDatos != null) {
            String consulta = "UPDATE profesor SET nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?," + 
                    "contrasenia = ?, fechaNacimiento = ?, fechaContratacion = ? " +
                    "WHERE numeroPersonal = ?";
            PreparedStatement sentencia = conexionBaseDatos.prepareStatement(consulta);
            sentencia.setString(1, profesor.getNombre());
            sentencia.setString(2, profesor.getApellidoPaterno());
            sentencia.setString(3, profesor.getApellidoMaterno());
            sentencia.setString(4, profesor.getContrasenia());
            sentencia.setString(5, profesor.getFechaNacimiento());
            sentencia.setString(6, profesor.getFechaContratacion());
            sentencia.setString(7, profesor.getNumeroPersonal());
            return sentencia.executeUpdate();
        }

        throw new SQLException("No hay conexión a la base de datos.");
    }

    public static int eliminarProfesor(int idProfesor, Connection conexionBaseDatos) throws SQLException {
        if(conexionBaseDatos != null) {
            String eliminacion = "DELETE FROM profesor WHERE idProfesor = ?";
            PreparedStatement sentencia = conexionBaseDatos.prepareStatement(eliminacion);
            sentencia.setInt(1, idProfesor);
            return sentencia.executeUpdate();
        }

        throw new SQLException("No hay conexión a la base de datos.");
    }
}
