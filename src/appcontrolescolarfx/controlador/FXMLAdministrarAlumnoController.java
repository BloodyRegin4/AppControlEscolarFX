package appcontrolescolarfx.controlador;

import appcontrolescolarfx.modelo.pojo.Alumno;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilidad.Utilidades;

public class FXMLAdministrarAlumnoController implements Initializable {

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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicIrRegistrarAlumno(ActionEvent event) {
        irFormulario();
    }

    @FXML
    private void clicIrModificarAlumno(ActionEvent event) {
    }

    @FXML
    private void clicIrEliminarAlumno(ActionEvent event) {
    }

    @FXML
    private void clicIrExportar(ActionEvent event) {
    }
    
    private void irFormulario() {
        try {
            FXMLLoader cargador = Utilidades.obtenerVistaMemoria("vista/FXMLFormularioAlumno.fxml");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);
            Stage escenario = new Stage();
            escenario.setTitle("Formulario alumno");
            escenario.setScene(escena);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
