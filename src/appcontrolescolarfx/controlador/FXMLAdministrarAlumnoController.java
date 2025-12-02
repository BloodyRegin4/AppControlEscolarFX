package appcontrolescolarfx.controlador;

import appcontrolescolarfx.dominio.AlumnoImplementacion;
import appcontrolescolarfx.interfaces.IObservador;
import appcontrolescolarfx.modelo.pojo.Alumno;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilidad.Utilidades;

public class FXMLAdministrarAlumnoController implements Initializable, IObservador {
    @FXML
    private TextField textFieldBuscarAlumno;
    
    @FXML
    private TableView<Alumno> tableViewAlumnos;
    @FXML
    private TableColumn columnMatricula;
    @FXML
    private TableColumn columnApellidoPaterno;
    @FXML
    private TableColumn columnApellidoMaterno;
    @FXML
    private TableColumn columnNombre;
    @FXML
    private TableColumn columnFacultad;
    @FXML
    private TableColumn columnCarrera;
    @FXML
    private TableColumn columnFechaNacimiento;
    
    private ObservableList<Alumno> observableListAlumnos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
    }
    
    private void configurarTabla() {
        columnMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        columnApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        columnApellidoMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        columnNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        columnFacultad.setCellValueFactory(new PropertyValueFactory("facultad"));
        columnCarrera.setCellValueFactory(new PropertyValueFactory("carrera"));
        columnFechaNacimiento.setCellValueFactory(new PropertyValueFactory("fechaNacimiento"));
    }
    
    private void cargarInformacion() {
        HashMap<String,Object> respuesta = AlumnoImplementacion.obtenerAlumnos();
        boolean error = (boolean) respuesta.get("error");
        
        if(!error) {
            ArrayList<Alumno> alumnosBaseDatos = (ArrayList<Alumno>) respuesta.get("alumnos");
            observableListAlumnos = FXCollections.observableArrayList();
            observableListAlumnos.addAll(alumnosBaseDatos);
            tableViewAlumnos.setItems(observableListAlumnos);
            configurarBusqueda();
        } else {
            Utilidades.mostrarAlertaSimple("Error", " " + respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicIrRegistrarAlumno(ActionEvent event) {
        irFormulario(null);
    }

    @FXML
    private void clicIrModificarAlumno(ActionEvent event) {
        Alumno alumnoSeleccionado = tableViewAlumnos.getSelectionModel().getSelectedItem();
        if(alumnoSeleccionado != null) {
            irFormulario(alumnoSeleccionado);
        } else {
            Utilidades.mostrarAlertaSimple("Selecciona un alumno", "Primero debes seleccionar un alumno de la lista.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicIrEliminarAlumno(ActionEvent event) {
        Alumno alumno = tableViewAlumnos.getSelectionModel().getSelectedItem();
        
        if(alumno != null) {
            boolean confirmarEliminacion = Utilidades.mostrarAlertaConfirmacion("Eliminar alumno", "Estas seguro de eliminar la informacion del alumno " + alumno.getNombre() + "?\nEsta acción no puede ser revertida.");
            
            if(confirmarEliminacion) {
                eliminarAlumno(alumno.getIdAlumno());
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selecciona un alumno", "Para eliminar la informacion de un alumno, debes seleccionar el registro de la tabla.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicIrExportar(ActionEvent event) {
    }
    
    private void irFormulario(Alumno alumno) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLFormularioAlumno.fxml");
            Parent vista = cargador.load();
            
            FXMLFormularioAlumnoController controlador = cargador.getController();
            controlador.inicializarDatos(this, alumno);
            
            Scene escena = new Scene(vista);
            Stage escenario = new Stage();
            escenario.setTitle("Formulario alumnos");
            escenario.setScene(escena);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void eliminarAlumno(int idAlumno) {
        HashMap<String,Object> respuesta = AlumnoImplementacion.eliminarAlumno(idAlumno);
        
        if(!(boolean) respuesta.get("error")) {
            Utilidades.mostrarAlertaSimple("Registro eliminado", "La informacion del alumno ha sido eliminada correctamente.", Alert.AlertType.INFORMATION);
            cargarInformacion();
        } else {
            Utilidades.mostrarAlertaSimple("Error al eliminar", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    @Override
    public void notificarOperacionExitosa(String tipoOperacion, String nombre) {
        System.out.println("Operación: " + tipoOperacion);
        System.out.println("Nombre: " + nombre);
        configurarTabla();
        cargarInformacion();
    }
    
    public void configurarBusqueda() {
        if(observableListAlumnos != null && observableListAlumnos.size() > 0) {
            FilteredList<Alumno> filtradoAlumnos = new FilteredList<>(observableListAlumnos, p->true);
            textFieldBuscarAlumno.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtradoAlumnos.setPredicate(alumno -> {
                        if(newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        
                        String lowerNewValue = newValue.toLowerCase();
                        
                        // Criterio 1: Buscar por Nombre
                        if(alumno.getNombre().toLowerCase().contains(lowerNewValue)) {
                            return true;
                        }
                        // Criterio 2: Buscar por Matrícula
                        if(alumno.getMatricula().toLowerCase().contains(lowerNewValue)) {
                            return true;
                        }
                        // Criterio 3: Buscar por Apellido Paterno
                        if(alumno.getApellidoPaterno().toLowerCase().contains(lowerNewValue)) {
                            return true;
                        }
                        
                        return false;
                    });
                }
            });
            
            SortedList<Alumno> sortedAlumno = new SortedList<>(filtradoAlumnos);
            sortedAlumno.comparatorProperty().bind(tableViewAlumnos.comparatorProperty());
            tableViewAlumnos.setItems(sortedAlumno);
        }
    }
}