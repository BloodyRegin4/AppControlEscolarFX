package appcontrolescolarfx.modelo.dao;

import appcontrolescolarfx.modelo.pojo.Profesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutenticacionDAO {
    public static ResultSet autenticarUsuario(String noPersonal, String password, Connection conexionBD) throws SQLException {
        Profesor profesorSesion = null;
        
        if(conexionBD != null)  {
            //Hay conexion con la BD
            String consulta = "SELECT idProfesor, nombre, " +
                    "apellidoPaterno, apellidoMaterno, noPersonal, p.idRol, rol " +
                    "FROM profesor p " +
                    "INNER JOIN rol r ON r.idRol = p.idRol " +
                    "WHERE noPersonal = ? AND password = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, noPersonal);
            sentencia.setString(2, password);
            sentencia.executeQuery();
            ResultSet resultado = sentencia.executeQuery();
            
            return resultado;
        }
        
        throw new SQLException("No hay conexión a la base de datos");
    }
}
