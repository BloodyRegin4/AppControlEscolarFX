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

public class FXMLAdminProfesorController implements Initializable, IObservador {
    @FXML
    private TableView <Profesor> tvProfesores;
    
    @FXML
    private TableColumn colNumPersonal;
    @FXML
    private TableColumn colApellidoPaterno;
    @FXML
    private TableColumn colApellidoMaterno;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colFechaContratacion;
    @FXML
    private TableColumn colRol;
    @FXML
    private TextField tfBuscar;
    
    private ObservableList<Profesor> profesores;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
    }
    
    private void configurarTabla(){
        colNumPersonal.setCellValueFactory(new PropertyValueFactory("noPersonal"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));        
        colFechaContratacion.setCellValueFactory(new PropertyValueFactory("fechaContratacion"));
        colRol.setCellValueFactory(new PropertyValueFactory("rol"));
    }
    
    private void cargarInformacion() {
        HashMap<String,Object> respuesta = ProfesorImplementacion.obtenerProfesores();
        boolean error = (boolean) respuesta.get("error");
        if(!error) {
            ArrayList<Profesor> profesoresBD = (ArrayList<Profesor>) respuesta.get("profesores");
            profesores = FXCollections.observableArrayList();
            profesores.addAll(profesoresBD);
            tvProfesores.setItems(profesores);
        } else {
            Utilidades.mostrarAlertaSimple("Error", " " + respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicBtnRegistrar(ActionEvent event) {
        irFormulario(null);
    }

    @FXML
    private void clicBtnModificar(ActionEvent event) {
    }

    @FXML
    private void clicBtnEliminar(ActionEvent event) {
    }
    
    private void irFormulario(Profesor profesor) {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLFormularioProfesor.fxml");
            Parent vista = cargador.load();
            FXMLFormularioProfesorController controlador = cargador.getController();
            controlador.inicializarDatos(this, profesor);
            Scene scene = new Scene(vista);
            Stage stage = new Stage();
            stage.setTitle("Formulario profesores");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void notificarOperacionExitosa(String tipoOperacion, String nombre) {
        System.out.println("Operación: " + tipoOperacion);
        System.out.println("Nombre: " + nombre);
        cargarInformacion();
    }
}
