package appcontrolescolarfx.modelo.dao;

import appcontrolescolarfx.modelo.pojo.Alumno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlumnoDAO {
    public static ResultSet obtenerAlumnos(Connection conexionBaseDatos) throws SQLException {
        if(conexionBaseDatos != null) {
            String consulta = "SELECT a.idAlumno, a.nombre, a.apellidoPaterno, a.apellidoMaterno, a.fechaNacimiento, a.matricula, a.correo, " +
                    "a.idCarrera, c.carrera AS carrera, " +
                    "f.idFacultad, f.facultad AS facultad " +
                    "FROM alumno a " +
                    "JOIN carrera c ON a.idCarrera = c.idCarrera " +
                    "JOIN facultad f ON c.idFacultad = f.idFacultad;";
            PreparedStatement sentencia = conexionBaseDatos.prepareStatement(consulta);
            return sentencia.executeQuery(); 
        }
        
        throw new SQLException("No hay conexión a la base de datos.");
    }
    
    public static Alumno obtenerAlumnoPorId(Connection conexionBaseDatos, int idAlumno) throws SQLException {
        Alumno alumno = null;
        
        if(conexionBaseDatos != null) {
            String consulta = "SELECT a.*, c.idFacultad " +
                    "FROM alumno a " +
                    "INNER JOIN carrera c ON a.idCarrera = c.idCarrera " +
                    "WHERE a.idAlumno = ?";
                          
            PreparedStatement sentencia = conexionBaseDatos.prepareStatement(consulta);
            sentencia.setInt(1, idAlumno);
            ResultSet resultSet = sentencia.executeQuery();
        
            if(resultSet.next()) {
                alumno = new Alumno();
                alumno.setIdAlumno(resultSet.getInt("idAlumno"));
                alumno.setNombre(resultSet.getString("nombre"));
                alumno.setApellidoPaterno(resultSet.getString("apellidoPaterno"));
                alumno.setApellidoMaterno(resultSet.getString("apellidoMaterno"));
                alumno.setMatricula(resultSet.getString("matricula"));
                alumno.setCorreo(resultSet.getString("correo"));
                alumno.setFechaNacimiento(resultSet.getString("fechaNacimiento"));
                alumno.setFoto(resultSet.getBytes("foto"));
                alumno.setIdCarrera(resultSet.getInt("idCarrera"));
                alumno.setIdFacultad(resultSet.getInt("idFacultad"));
            }
        
            return alumno;
        }
    
        throw new SQLException("No hay conexión a la base de datos.");
    }
    
    public static ResultSet obtenerFoto(Connection conexionBaseDatos, int idAlumno) throws SQLException {
        if(conexionBaseDatos != null) {
            String consulta = "SELECT foto FROM alumno WHERE idAlumno = ?;";
            PreparedStatement sentencia = conexionBaseDatos.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        
        throw new SQLException("No hay conexión a la base de datos.");
    }
    
    public static int registrarAlumno(Alumno alumno, Connection conexionBaseDatos) throws SQLException {
        if(conexionBaseDatos != null) {
            String insercion = "INSERT INTO alumno (nombre, apellidoPaterno, apellidoMaterno, " +
                    "fechaNacimiento, matricula, correo, foto, idCarrera) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement sentencia =  conexionBaseDatos.prepareStatement(insercion);
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getApellidoPaterno());
            sentencia.setString(3, alumno.getApellidoMaterno());
            sentencia.setString(4, alumno.getFechaNacimiento());
            sentencia.setString(5, alumno.getMatricula());
            sentencia.setString(6, alumno.getCorreo());
            sentencia.setBytes(7, alumno.getFoto());
            sentencia.setInt(8, alumno.getIdCarrera());
            
            return sentencia.executeUpdate();
        }
        
        throw new SQLException("No hay conexión a la base de datos");
    }
    
    public static int editarAlumno(Alumno alumno, Connection conexionBaseDatos) throws SQLException {
        if(conexionBaseDatos != null) {
            String consulta = "UPDATE alumno SET nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?," + 
                    "fechaNacimiento = ?, matricula = ?, correo = ?, foto = ?, idCarrera =? " +
                    "WHERE idAlumno = ?";
            PreparedStatement sentencia = conexionBaseDatos.prepareStatement(consulta);
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getApellidoPaterno());
            sentencia.setString(3, alumno.getApellidoMaterno());
            sentencia.setString(4, alumno.getFechaNacimiento());
            sentencia.setString(5, alumno.getMatricula());
            sentencia.setString(6, alumno.getCorreo());
            sentencia.setBytes(7, alumno.getFoto());
            sentencia.setInt(8, alumno.getIdCarrera());
            sentencia.setInt(9, alumno.getIdAlumno());
            
            return sentencia.executeUpdate();
        }

        throw new SQLException("No hay conexión a la base de datos.");
    }

    public static int eliminarAlumno(int idAlumno, Connection conexionBaseDatos) throws SQLException {
        if(conexionBaseDatos != null) {
            String eliminacion = "DELETE FROM alumno WHERE idAlumno = ?";
            PreparedStatement sentencia = conexionBaseDatos.prepareStatement(eliminacion);
            sentencia.setInt(1, idAlumno);
            return sentencia.executeUpdate();
        }

        throw new SQLException("No hay conexión a la base de datos.");
    }
}
