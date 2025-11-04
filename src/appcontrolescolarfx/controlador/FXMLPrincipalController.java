package appcontrolescolarfx.controlador;

import appcontrolescolarfx.AppControlEscolarFX;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLPrincipalController implements Initializable {

    @FXML
    private Label labelNombre;
    
    @FXML
    private Label labelRol;
    
    @FXML
    private Label labelNumeroPersonal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void obtenerSesion(Profesor profesorSesion) {
        labelNombre.setText(profesorSesion.getNombre() + " " + profesorSesion.getApellidoPaterno() + " " + profesorSesion.getApellidoMaterno());
        labelNumeroPersonal.setText(profesorSesion.getNumeroPersonal());
        labelRol.setText("Rol: " + profesorSesion.getRol());
    }

    @FXML
    private void clicIrAdministrarProfesor(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(AppControlEscolarFX.class.getResource("vista/FXMLAdministrarProfesor.fxml"));
            Scene escenaAdministrarProfesor = new Scene(vista);
            Stage escenarioAdministrarProfesor = new Stage();
            escenarioAdministrarProfesor.setScene(escenaAdministrarProfesor);
            escenarioAdministrarProfesor.setTitle("Administración profesores");
            escenarioAdministrarProfesor.initModality(Modality.APPLICATION_MODAL);
            escenarioAdministrarProfesor.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicButtonCerrarSesion(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(AppControlEscolarFX.class.getResource("vista/FXMLInicioSesion.fxml"));
            Scene escena = new Scene(vista);
            Stage escenarioPrincipal = (Stage) labelNombre.getScene().getWindow();
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.setTitle("Iniciar sesión");
            escenarioPrincipal.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
