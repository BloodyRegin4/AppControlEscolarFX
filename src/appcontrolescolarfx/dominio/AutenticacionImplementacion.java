package appcontrolescolarfx.dominio;

import appcontrolescolarfx.modelo.ConexionBaseDatos;
import appcontrolescolarfx.modelo.dao.AutenticacionDAO;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AutenticacionImplementacion {
    public static HashMap<String,Object> verificarSesionProfesor(String numeroPersonal, String contrasenia) {
        HashMap<String,Object> respuesta = new LinkedHashMap<>();
        try {
            ResultSet resultado = AutenticacionDAO.autenticarUsuario(numeroPersonal, contrasenia, ConexionBaseDatos.abrirConexionBD());
            
            if(resultado.next()) {
                Profesor profesorSesion = new Profesor();
                profesorSesion.setIdProfesor(resultado.getInt("idProfesor"));
                profesorSesion.setNombre(resultado.getString("nombre"));
                profesorSesion.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                profesorSesion.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                profesorSesion.setNumeroPersonal(resultado.getString("numeroPersonal"));
                profesorSesion.setIdRol(resultado.getInt("idRol"));
                profesorSesion.setRol(resultado.getString("rol"));
                respuesta.put("error", false);
                respuesta.put("mensaje", "Credenciales correctas.");
                respuesta.put("profesor", profesorSesion);
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Las credenciales proporcionadas son incorrectas, por favor verifica la información");
            }
            
            ConexionBaseDatos.cerrarConexionBD();
        } catch (SQLException ex) {
            respuesta.put("error", true);
            respuesta.put("mensaje", ex.getMessage());
        }
        
        return respuesta;
    }
}
