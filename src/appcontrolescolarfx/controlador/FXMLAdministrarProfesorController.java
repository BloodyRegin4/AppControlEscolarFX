package appcontrolescolarfx.controlador;

import appcontrolescolarfx.dominio.ProfesorImplementacion;
import appcontrolescolarfx.interfaces.IObservador;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class FXMLAdministrarProfesorController implements Initializable, IObservador {
    @FXML
    private TableView <Profesor> tableViewProfesores;
    @FXML
    private TableColumn columnNumeroPersonal;
    @FXML
    private TableColumn columnApellidoPaterno;
    @FXML
    private TableColumn columnApellidoMaterno;
    @FXML
    private TableColumn columnNombre;
    @FXML
    private TableColumn columnFechaContratacion;
    @FXML
    private TableColumn columnRol;
    
    @FXML
    private TextField textFieldBuscar;
    
    private ObservableList<Profesor> observableListProfesores;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
    }
    
    private void configurarTabla(){
        columnNumeroPersonal.setCellValueFactory(new PropertyValueFactory("numeroPersonal"));
        columnApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        columnApellidoMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        columnNombre.setCellValueFactory(new PropertyValueFactory("nombre"));        
        columnFechaContratacion.setCellValueFactory(new PropertyValueFactory("fechaContratacion"));
        columnRol.setCellValueFactory(new PropertyValueFactory("rol"));
    }
    
    private void cargarInformacion() {
        HashMap<String,Object> respuesta = ProfesorImplementacion.obtenerProfesores();
        boolean error = (boolean) respuesta.get("error");
        if(!error) {
            ArrayList<Profesor> profesoresBaseDatos = (ArrayList<Profesor>) respuesta.get("profesores");
            observableListProfesores = FXCollections.observableArrayList();
            observableListProfesores.addAll(profesoresBaseDatos);
            tableViewProfesores.setItems(observableListProfesores);
        } else {
            Utilidades.mostrarAlertaSimple("Error", " " + respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicButtonRegistrar(ActionEvent event) {
        irFormulario(null);
    }

    @FXML
    private void clicButtonModificar(ActionEvent event) {
        Profesor profesorSeleccionado = tableViewProfesores.getSelectionModel().getSelectedItem();
        if(profesorSeleccionado != null) {
            irFormulario(profesorSeleccionado);
        } else {
            Utilidades.mostrarAlertaSimple("Selecciona un profesor", "Primero debes seleccionar un profesor de la lista.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicButtonEliminar(ActionEvent event) {
        Profesor profesor = tableViewProfesores.getSelectionModel().getSelectedItem();
        
        if(profesor != null) {
            boolean confirmarEliminacion = Utilidades.mostrarAlertaConfirmacion("Eliminar profesor(a)", "Estas seguro de eliminar la informacion del profesor(a) " + profesor.getNombre() + "?\nAl eliminar un profesor(a) la informacion no puede ser recuperada.");
            
            if(confirmarEliminacion) {
                eliminarProfesor(profesor.getIdProfesor());
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selecciona un profesor(a).", "Para eliminar la informacion de un profesor(a), debes seleccionar el registro de la tabla.", Alert.AlertType.WARNING);
        }
    }
    
    private void irFormulario(Profesor profesor) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLFormularioProfesor.fxml");
            Parent vista = cargador.load();
            FXMLFormularioProfesorController controlador = cargador.getController();
            controlador.inicializarDatos(this, profesor);
            Scene escena = new Scene(vista);
            Stage escenario = new Stage();
            escenario.setTitle("Formulario profesores");
            escenario.setScene(escena);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void eliminarProfesor(int idProfesor) {
        HashMap<String,Object> respuesta = ProfesorImplementacion.eliminarProfesor(idProfesor);
        
        if(!(boolean) respuesta.get("error")) {
            Utilidades.mostrarAlertaSimple("Registro eliminado", "La informacion del profesor(a) ha sido eliminada correctamente.", Alert.AlertType.INFORMATION);
            cargarInformacion();
        } else {
            Utilidades.mostrarAlertaSimple("Error al eliminar", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    public void notificarOperacionExitosa(String tipoOperacion, String nombre) {
        System.out.println("Operación: " + tipoOperacion);
        System.out.println("Nombre: " + nombre);
        cargarInformacion();
    }
}
