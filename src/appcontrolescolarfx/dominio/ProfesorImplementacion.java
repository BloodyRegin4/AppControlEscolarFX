package appcontrolescolarfx.dominio;

import appcontrolescolarfx.modelo.ConexionBD;
import appcontrolescolarfx.modelo.dao.ProfesorDAO;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProfesorImplementacion {
    public static HashMap<String, Object> obtenerProfesores(){
    HashMap<String, Object> respuesta = new LinkedHashMap<>();
    try{
        ResultSet resultado = ProfesorDAO.obtenerProfesores(ConexionBD.abrirConexionBD());
        ArrayList<Profesor> profesores = new ArrayList<>();
        while (resultado.next()){
            Profesor profesor = new Profesor();
            profesor.setIdProfesor(resultado.getInt("idProfesor"));
            profesor.setIdRol(resultado.getInt("idRol"));
            profesor.setRol(resultado.getString("rol"));
            profesor.setNombre(resultado.getString("nombre"));
            profesor.setApellidoPaterno(resultado.getString("apellidoPaterno"));
            profesor.setApellidoMaterno(resultado.getString("apellidoMaterno"));
            profesor.setNoPersonal(resultado.getString("noPersonal"));
            profesor.setFechaNacimiento(resultado.getString("fechaNacimiento"));
            profesor.setFechaContratacion(resultado.getString("fechaContratacion"));
            
            profesores.add(profesor);
        }
        respuesta.put("error", false);
        respuesta.put("profesores", profesores);
        
        ConexionBD.cerrarConexionBD();
    } catch (SQLException e){
        respuesta.put("error", true);
        respuesta.put("mensaje", e.getMessage());
    }
    
    return respuesta;
   }
    
    public static HashMap<String, Object> registrarProfesor(Profesor profesor) {
        HashMap<String,Object> respuesta = new LinkedHashMap<>();
        
        try {
            int filasAfectadas = ProfesorDAO.registrarProfesor(profesor, ConexionBD.abrirConexionBD());
            if(filasAfectadas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del profesor(a) " + profesor.getNombre() + " fue guardado correctamente.");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Lo sentimos :( no se pudo guardar la informacion del profesor, por favor intentelo mas tarde.");
            }
            
            ConexionBD.cerrarConexionBD();
        } catch (SQLException ex) {
            respuesta.put("error", true);
            respuesta.put("mensaje", ex.getMessage());
        }
        
        return respuesta;
    }
}
