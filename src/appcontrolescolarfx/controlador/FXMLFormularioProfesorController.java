package appcontrolescolarfx.controlador;

import appcontrolescolarfx.dominio.CatalogoImplementacion;
import appcontrolescolarfx.dominio.ProfesorImplementacion;
import appcontrolescolarfx.interfaces.IObservador;
import appcontrolescolarfx.modelo.pojo.Profesor;
import appcontrolescolarfx.modelo.pojo.Rol;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilidad.Utilidades;

public class FXMLFormularioProfesorController implements Initializable {

    @FXML
    private TextField tfNumPersonal;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfPassword; //pfPassword
    
    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private DatePicker dpFechaContratacion;

    @FXML
    private ComboBox<Rol> cbRol;
    
    private ObservableList<Rol> roles;
    private IObservador observador;
    private Profesor profesorEdicion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarRolesProfesor();
    }
    
    public void inicializarDatos(IObservador observador, Profesor profesor) {
        this.observador = observador;
        this.profesorEdicion = profesor;

        if(profesor != null) {
            tfNombre.setText(profesor.getNombre());
            tfApellidoPaterno.setText(profesor.getApellidoPaterno());
            tfApellidoMaterno.setText(profesor.getApellidoMaterno());
            tfPassword.setText(profesor.getPassword());
            tfNumPersonal.setText(profesor.getNoPersonal());
            tfNumPersonal.setEditable(false);
            ftNumPersonal.setDisable(true);
            dpFechaNacimiento.setValue(LocalDate.parse(profesor.getFechaNacimiento());
            dpFechaContratacion.setValue(LocalDate.parse(profesor.getFechaContratacion());

            int posicion = obtenerRolSeleccionado(profesor.getIdRol());
            cbRol.getSelectionModel().select(posicion);
        }
    }

    private int obtenerRolSeleccionado(int idRol) {
        for(int i = 0; i < roles.size(); i++) {
            if(roles.get(i).getIdRol() == idRol) {
                return i;
            }
        }

        return -1;
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        if(sonCamposValidos()) {
            if(profesorEdicion == null) {
                registrarProfesor();
            } else {
                editarProfesor();
            }
        }
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cargarRolesProfesor() {
        HashMap<String,Object> respuesta = CatalogoImplementacion.obtenerRolesProfesor();
        
        if(!(boolean) respuesta.get("error")) {
            List<Rol> rolesBD = (List<Rol>) respuesta.get("roles");
            roles = FXCollections.observableArrayList();
            roles.addAll(rolesBD);
            cbRol.setItems(roles);
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar roles del sistema", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private boolean sonCamposValidos() {
        boolean valido = true;
        String mensajeError = "Se encontraron los siguientes errores:\n";
        
        if(tfNombre.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Nombre del profesor requerido. \n";
        }
        
        if(tfApellidoPaterno.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Apellido paterno del profesor requerido. \n";
        }
        
        if(tfApellidoMaterno.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Apellido materno del profesor requerido. \n";
        }
        
        if(tfNumPersonal.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Numero de personal requerido. \n";
        }
        
        if(tfPassword.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Contraseña requerido. \n";
        }
        
        if(dpFechaNacimiento.getValue() == null) {
            valido = false;
            mensajeError += "- Fecha de nacimiento del profesor requerida. \n";
        }
        
        if(dpFechaContratacion.getValue() == null) {
            valido = false;
            mensajeError += "- Fecha de contratación del profesor requerida. \n";
        }
        
        if(cbRol.getSelectionModel().isEmpty()) {
            valido = false;
            mensajeError += "- Selecciona un rol de sistema para el profesor.";
        }
        
        if(!valido) {
            Utilidades.mostrarAlertaSimple("Campos vacíos", mensajeError, Alert.AlertType.WARNING);
        }
        
        return valido;
    }
    
    private void registrarProfesor() {
        Profesor profesorNuevo = obtenerProfesores();
        HashMap<String,Object> resultado = ProfesorImplementacion.registrarProfesor(profesorNuevo);
        
        if(!(boolean) resultado.get("error")) {
            Utilidades.mostrarAlertaSimple("Profesor registrado correctamente", resultado.get("mensaje").toString(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("registrar", profesorNuevo.getNombre());
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al registrar", resultado.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private Profesor obtenerProfesores() {
        Profesor profesor = new Profesor();
        Rol rolSeleccionado = cbRol.getSelectionModel().getSelectedItem();
        profesor.setIdRol(rolSeleccionado.getIdRol());
        profesor.setNombre(tfNombre.getText());
        profesor.setApellidoPaterno(tfApellidoPaterno.getText());
        profesor.setApellidoMaterno(tfApellidoMaterno.getText());
        profesor.setNoPersonal(tfNumPersonal.getText());
        profesor.setPassword(tfPassword.getText());
        profesor.setFechaNacimiento(dpFechaNacimiento.getValue().toString());
        profesor.setFechaContratacion(dpFechaContratacion.getValue().toString());
        
        return profesor;
    }

    private void editarProfesor() {
        Profesor profesorEdicion = obtenerProfesor();
        profesorEdicion.setIdProfesor(this.profesorEdicion.getIdProfesor());
        HashMap<String,Object> resultado = ProfesorImplementacion.editarProfesor(profesorEdicion);

        if(!(boolean) resultado.get("error")) {
            Utilidades.mostrarAlertaSimple("Profesor registrado correctamente", resultado.get("mensaje").toString(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("editar", profesorEdicion.getNombre());
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al editar", resultado.get("error").toString(), Alert.AlertType.ERROR);
    
    private void cerrarVentana() {
        ((Stage) tfNombre.getScene().getWindow()).close();
    }
}
