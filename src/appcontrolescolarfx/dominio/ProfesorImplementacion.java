package appcontrolescolarfx.dominio;

import appcontrolescolarfx.modelo.ConexionBaseDatos;
import appcontrolescolarfx.modelo.dao.ProfesorDAO;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProfesorImplementacion {
    public static HashMap<String,Object> obtenerProfesores(){
        HashMap<String,Object> respuesta = new LinkedHashMap<>();
        
        try{
            ResultSet resultado = ProfesorDAO.obtenerProfesores(ConexionBaseDatos.abrirConexionBD());
            ArrayList<Profesor> profesoresBaseDatos = new ArrayList<>();
            while (resultado.next()){
                Profesor profesor = new Profesor();
                profesor.setIdProfesor(resultado.getInt("idProfesor"));
                profesor.setNombre(resultado.getString("nombre"));
                profesor.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                profesor.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                profesor.setNumeroPersonal(resultado.getString("numeroPersonal"));
                profesor.setFechaNacimiento(resultado.getString("fechaNacimiento"));
                profesor.setFechaContratacion(resultado.getString("fechaContratacion"));
                profesor.setIdRol(resultado.getInt("idRol"));
                profesor.setRol(resultado.getString("rol"));
                
                profesoresBaseDatos.add(profesor);
            }
            
            respuesta.put("error", false);
            respuesta.put("profesores", profesoresBaseDatos);
            
            ConexionBaseDatos.cerrarConexionBD();
        } catch (SQLException e){
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        
        return respuesta;
    }
    
    public static HashMap<String, Object> registrarProfesor(Profesor profesor) {
        HashMap<String,Object> respuesta = new LinkedHashMap<>();
        
        try {
            int filasAfectadas = ProfesorDAO.registrarProfesor(profesor, ConexionBaseDatos.abrirConexionBD());
            if(filasAfectadas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del profesor(a) " + profesor.getNombre() + " fue guardado correctamente.");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Lo sentimos :( no se pudo guardar la informacion del profesor, por favor intentelo mas tarde.");
            }
            
            ConexionBaseDatos.cerrarConexionBD();
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        
        return respuesta;
    }

    public static HashMap<String,Object> editarProfesor(Profesor profesor) {
        HashMap<String,Object> respuesta = new LinkedHashMap<>();

        try {
            int filasAfectadas = ProfesorDAO.editarProfesor(profesor, ConexionBaseDatos.abrirConexionBD());

            if(filasAfectadas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del profesor(a) " + profesor.getNombre() + " fue editada correctamente.");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Lo sentimos :( no se pudo editar la información del profesfor, por favor intente más tarde.");
            }

            ConexionBaseDatos.cerrarConexionBD();
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }

        return respuesta;
    }
    
    public static HashMap<String,Object> eliminarProfesor(int idProfesor) {
        HashMap<String,Object> respuesta = new LinkedHashMap<>();
        
        try {
            int filasAfectadas = ProfesorDAO.eliminarProfesor(idProfesor, ConexionBaseDatos.abrirConexionBD());
            
            if(filasAfectadas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del profesor(a) fue eliminado correctamente.");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Lo sentimos :( no se pudo eliminar la información del profesor, por favor inténtelo mas tarde.");
            }
            
            ConexionBaseDatos.cerrarConexionBD();
        } catch(SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        
        return respuesta;
    }
}
