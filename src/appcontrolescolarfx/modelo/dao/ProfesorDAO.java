package appcontrolescolarfx.modelo.dao;

import appcontrolescolarfx.modelo.pojo.Profesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfesorDAO {
    public static ResultSet obtenerProfesores(Connection conexionBD) throws SQLException {
        if(conexionBD != null){  
            String consulta = "SELECT idProfesor, nombre, apellidoPaterno, apellidoMaterno," +
                    "noPersonal, fechaNacimiento, fechaContratacion, profesor.idRol, rol " +
                    "FROM profesor " +
                    "INNER JOIN rol ON rol.idRol = profesor.idRol;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
              
        }

      throw new SQLException("No hay conexion a la base de datos."); 
    }
    
    public static int registrarProfesor(Profesor profesor, Connection conexionBD) throws SQLException {
        if(conexionBD != null) {
            String insercion = "INSERT INTO profesor (idRol, nombre, apellidoPaterno, apellidoMaterno, " +
                    "noPersonal, password, fechaNacimiento, fechaContratacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement sentencia =  conexionBD.prepareStatement(insercion);
            sentencia.setInt(1, profesor.getIdRol());
            sentencia.setString(2, profesor.getNombre());
            sentencia.setString(3, profesor.getApellidoPaterno());
            sentencia.setString(4, profesor.getApellidoMaterno());
            sentencia.setString(5, profesor.getNoPersonal());
            sentencia.setString(6, profesor.getPassword());
            sentencia.setString(7, profesor.getFechaNacimiento());
            sentencia.setString(8, profesor.getFechaContratacion());
            return sentencia.executeUpdate();
        }
        
        throw new SQLException("No hay conexion a la base de datos");
    }
}