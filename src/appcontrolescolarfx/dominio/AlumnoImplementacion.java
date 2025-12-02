package appcontrolescolarfx.dominio;

import appcontrolescolarfx.modelo.ConexionBaseDatos;
import appcontrolescolarfx.modelo.dao.AlumnoDAO;
import appcontrolescolarfx.modelo.pojo.Alumno;
import appcontrolescolarfx.modelo.pojo.Respuesta;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AlumnoImplementacion {
    public static HashMap<String,Object> obtenerAlumnos() {
        HashMap<String,Object> respuesta = new LinkedHashMap<>();
        
        try {
            ResultSet resultado = AlumnoDAO.obtenerAlumnos(ConexionBaseDatos.abrirConexionBD());
            ArrayList<Alumno> alumnosBaseDatos = new ArrayList<>();
            
            while(resultado.next()) {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(resultado.getInt("idAlumno"));
                alumno.setNombre(resultado.getString("nombre"));
                alumno.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                alumno.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                alumno.setFechaNacimiento(resultado.getString("fechaNacimiento"));
                alumno.setMatricula(resultado.getString("matricula"));
                alumno.setCorreo(resultado.getString("correo"));
                alumno.setIdCarrera(resultado.getInt("idCarrera"));
                alumno.setCarrera(resultado.getString("carrera"));
                alumno.setIdFacultad(resultado.getInt("idFacultad"));
                alumno.setFacultad(resultado.getString("facultad"));
                
                alumnosBaseDatos.add(alumno);
            }
            
            respuesta.put("error", false);
            respuesta.put("alumnos", alumnosBaseDatos);
            
            ConexionBaseDatos.cerrarConexionBD();
        } catch(SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerAlumnoPorId(int idAlumno) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
    
        try {
            Alumno alumno = AlumnoDAO.obtenerAlumnoPorId(ConexionBaseDatos.abrirConexionBD(), idAlumno);
            
            if (alumno != null) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "Alumno encontrado correctamente.");
                respuesta.put("alumno", alumno);
            } else {
                respuesta.put("error", false);
                respuesta.put("mensaje", "No se encontró el alumno con el ID proporcionado.");
            }
            
            ConexionBaseDatos.cerrarConexionBD();
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
    
        return respuesta;
    }
    
    public static HashMap<String,Object> obtenerFoto(int idAlumno) {
        HashMap<String,Object> respuesta = new LinkedHashMap<>();
        
        try {
            ResultSet resultado = AlumnoDAO.obtenerFoto(ConexionBaseDatos.abrirConexionBD(), idAlumno);
            if(resultado.next()) {
                byte[] foto;
                foto = resultado.getBytes("foto");
                respuesta.put("error", false);
                respuesta.put("foto", foto);
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo cargar la foto del alumno(a).");
            }
        } catch(SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        
        return respuesta;
    }
    
    public static Respuesta registrarAlumno(Alumno alumno) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        
        try {
            int filasAfectadas = AlumnoDAO.registrarAlumno(alumno, ConexionBaseDatos.abrirConexionBD());
            if(filasAfectadas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("La información del alumno(a) fue guardada correctamente.");
            } else {
                respuesta.setMensaje("Lo sentimos :( no se pudo guardar la informacion del alumno, por favor intentelo mas tarde.");
            }
            
            ConexionBaseDatos.cerrarConexionBD();
        } catch (SQLException e) {
            respuesta.setMensaje(e.getMessage());
        }
        
        return respuesta;
    }
    
    public static HashMap<String,Object> editarAlumno(Alumno alumno) {
        HashMap<String,Object> respuesta = new LinkedHashMap<>();

        try {
            int filasAfectadas = AlumnoDAO.editarAlumno(alumno, ConexionBaseDatos.abrirConexionBD());

            if(filasAfectadas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del alumno(a) " + alumno.getNombre() + " fue editada correctamente.");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Lo sentimos :( no se pudo editar la información del alumno, por favor intente más tarde.");
            }

            ConexionBaseDatos.cerrarConexionBD();
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }

        return respuesta;
    }
    
    public static HashMap<String,Object> eliminarAlumno(int idAlumno) {
        HashMap<String,Object> respuesta = new LinkedHashMap<>();
        
        try {
            int filasAfectadas = AlumnoDAO.eliminarAlumno(idAlumno, ConexionBaseDatos.abrirConexionBD());
            
            if(filasAfectadas > 0) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "El registro del alumno(a) fue eliminado correctamente.");
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Lo sentimos :( no se pudo eliminar la información del alumno, por favor inténtelo mas tarde.");
            }
            
            ConexionBaseDatos.cerrarConexionBD();
        } catch(SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
        }
        
        return respuesta;
    }
}
