package appcontrolescolarfx.controlador;

import appcontrolescolarfx.dominio.CatalogoImplementacion;
import appcontrolescolarfx.dominio.ProfesorImplementacion;
import appcontrolescolarfx.interfaces.IObservador;
import appcontrolescolarfx.modelo.pojo.Profesor;
import appcontrolescolarfx.modelo.pojo.Rol;
import java.net.URL;
import java.time.LocalDate;
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
    private TextField textFieldNumeroPersonal;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidoPaterno;
    @FXML
    private TextField textFieldApellidoMaterno;
    @FXML
    private TextField textFieldContrasenia; //pfPassword
    
    @FXML
    private DatePicker datePickerFechaNacimiento;
    @FXML
    private DatePicker datePickerFechaContratacion;

    @FXML
    private ComboBox<Rol> comboBoxRol;
    
    private ObservableList<Rol> observableListRoles;
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
            textFieldNombre.setText(profesor.getNombre());
            textFieldApellidoPaterno.setText(profesor.getApellidoPaterno());
            textFieldApellidoMaterno.setText(profesor.getApellidoMaterno());
            textFieldContrasenia.setText(profesor.getContrasenia());
            textFieldNumeroPersonal.setText(profesor.getNumeroPersonal());
            datePickerFechaNacimiento.setValue(LocalDate.parse(profesor.getFechaNacimiento()));
            datePickerFechaContratacion.setValue(LocalDate.parse(profesor.getFechaContratacion()));
            int posicion = obtenerRolSeleccionado(profesor.getIdRol());
            comboBoxRol.getSelectionModel().select(posicion);
        }
    }

    private int obtenerRolSeleccionado(int idRol) {
        for(int i = 0; i < observableListRoles.size(); i++) {
            if(observableListRoles.get(i).getIdRol() == idRol) {
                return i;
            }
        }

        return -1;
    }

    @FXML
    private void clicButtonGuardar(ActionEvent event) {
        if(sonCamposValidos()) {
            if(profesorEdicion == null) {
                registrarProfesor();
            } else {
                editarProfesor();
            }
        }
    }

    @FXML
    private void clicButtonCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cargarRolesProfesor() {
        HashMap<String,Object> respuesta = CatalogoImplementacion.obtenerRolesProfesor();
        
        if(!(boolean) respuesta.get("error")) {
            List<Rol> rolesBaseDatos = (List<Rol>) respuesta.get("roles");
            observableListRoles = FXCollections.observableArrayList();
            observableListRoles.addAll(rolesBaseDatos);
            comboBoxRol.setItems(observableListRoles);
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar roles del sistema", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private boolean sonCamposValidos() {
        boolean valido = true;
        String mensajeError = "Se encontraron los siguientes errores:\n";
        
        if(textFieldApellidoPaterno.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Apellido paterno del profesor requerido. \n";
        }
        
        if(textFieldApellidoMaterno.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Apellido materno del profesor requerido. \n";
        }
        
        if(textFieldNumeroPersonal.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Numero de personal requerido. \n";
        }
        
        if (profesorEdicion == null) {
            if (textFieldContrasenia.getText().isEmpty()) {
                valido = false;
                mensajeError += "- Contraseña requerida.\n";
            }
        }
        
        if(datePickerFechaNacimiento.getValue() == null) {
            valido = false;
            mensajeError += "- Fecha de nacimiento del profesor requerida. \n";
        }
        
        if(datePickerFechaContratacion.getValue() == null) {
            valido = false;
            mensajeError += "- Fecha de contratación del profesor requerida. \n";
        }
        
        if(comboBoxRol.getSelectionModel().isEmpty()) {
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
        
        profesor.setNombre(textFieldNombre.getText());
        profesor.setApellidoPaterno(textFieldApellidoPaterno.getText());
        profesor.setApellidoMaterno(textFieldApellidoMaterno.getText());
        profesor.setNumeroPersonal(textFieldNumeroPersonal.getText());
        profesor.setFechaNacimiento(datePickerFechaNacimiento.getValue().toString());
        profesor.setFechaContratacion(datePickerFechaContratacion.getValue().toString());
        
        String contraseniaNueva = textFieldContrasenia.getText();
        if(profesorEdicion != null && (contraseniaNueva == null || contraseniaNueva.isEmpty())) {
            profesor.setContrasenia(profesorEdicion.getContrasenia());
        } else {
            if(contraseniaNueva == null) {
                profesor.setContrasenia(""); 
            } else {
                profesor.setContrasenia(contraseniaNueva);
            }
        }
        
        Rol rolSeleccionado = comboBoxRol.getSelectionModel().getSelectedItem();
        if (rolSeleccionado != null) {
            profesor.setIdRol(rolSeleccionado.getIdRol());
        }
        
        return profesor;
    }

    private void editarProfesor() {
        Profesor profesorEdicion = obtenerProfesores();
        profesorEdicion.setIdProfesor(this.profesorEdicion.getIdProfesor());
        HashMap<String,Object> resultado = ProfesorImplementacion.editarProfesor(profesorEdicion);

        if(!(boolean) resultado.get("error")) {
            Utilidades.mostrarAlertaSimple("Profesor editado correctamente", resultado.get("mensaje").toString(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("editar", profesorEdicion.getNombre());
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al editar", resultado.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana() {
        ((Stage) textFieldNombre.getScene().getWindow()).close();
    }
}
