package appcontrolescolarfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CatalogoDAO {
    public static ResultSet obtenerRoles(Connection conexionBD) throws SQLException {
        if(conexionBD != null) {
            String consulta = "SELECT * FROM rol";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        
        throw new SQLException("Lo sentimos no hay conexión a la base de datos");
    }
}
