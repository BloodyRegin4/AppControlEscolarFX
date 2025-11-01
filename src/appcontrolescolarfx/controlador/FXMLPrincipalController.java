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
    private Label lbNombre;
    
    @FXML
    private Label lbRol;
    
    @FXML
    private Label lbNumPersonal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void obtenerSesion(Profesor profesorSesion) {
        lbNombre.setText(profesorSesion.getNombre() + " " + profesorSesion.getApellidoPaterno() + " " + profesorSesion.getApellidoMaterno());
        lbNumPersonal.setText(profesorSesion.getNoPersonal());
        lbRol.setText("Rol: " + profesorSesion.getRol());
    }

    @FXML
    private void clicIrAdminProfesores(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(AppControlEscolarFX.class.getResource("vista/FXMLAdminProfesor.fxml"));
            Scene escenaAdmin = new Scene(vista);
            Stage stAdmin = new Stage();
            stAdmin.setScene(escenaAdmin);
            stAdmin.setTitle("Administración profesores");
            stAdmin.initModality(Modality.APPLICATION_MODAL);
            stAdmin.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(AppControlEscolarFX.class.getResource("vista/FXMLInicioSesion.fxml"));
            Scene escena = new Scene(vista);
            Stage stPrincipal = (Stage) lbNombre.getScene().getWindow();
            stPrincipal.setScene(escena);
            stPrincipal.setTitle("Iniciar sesión");
            stPrincipal.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
