package appcontrolescolarfx.controlador;

import appcontrolescolarfx.AppControlEscolarFX;
import appcontrolescolarfx.dominio.AutenticacionImplementacion;
import appcontrolescolarfx.modelo.pojo.Profesor;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilidad.Utilidades;

public class FXMLInicioSesionController implements Initializable {
    @FXML
    private TextField textFieldNumeroPersonal;
    @FXML
    private PasswordField passwordFieldContrasenia;
    
    @FXML
    private Label labelErrorNumeroPersonal;
    @FXML
    private Label labelErrorContrasenia;
    
    @FXML
    private Button buttonIngresar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {    }

    @FXML
    private void clicButtonIngresar(ActionEvent event) {
        String numeroPersonal = textFieldNumeroPersonal.getText();
        String contrasenia = passwordFieldContrasenia.getText();
        
        if(sonDatosValidos(numeroPersonal, contrasenia)) {
            validarSesion(numeroPersonal, contrasenia);
        }
    }
    
    private boolean sonDatosValidos(String numeroPersonal, String contrasenia) {
        boolean correcto = true;
        labelErrorNumeroPersonal.setText("");
        labelErrorContrasenia.setText("");
        
        if(numeroPersonal == null || numeroPersonal.isEmpty()) {
            correcto = false;
            labelErrorNumeroPersonal.setText("Numero de personal obligatorio");
        }
        
        if(contrasenia == null || contrasenia.isEmpty()) {
            correcto = false;
            labelErrorContrasenia.setText("Contraseña obligatoria");
        }
        
        return correcto;
    }
    
    private void validarSesion(String numeroPersonal, String contrasenia) {
        HashMap<String,Object> respuesta = AutenticacionImplementacion.verificarSesionProfesor(numeroPersonal, contrasenia);
        boolean error = (boolean) respuesta.get("error");
        
        if(!error) {
            Profesor profesorSesion = (Profesor) respuesta.get("profesor");
            Utilidades.mostrarAlertaSimple("Credenciales correctas", "Bienvenido(a) profesor(a) " + profesorSesion.getNombre() + 
                    ", al sistema de administración escolar", Alert.AlertType.INFORMATION);
            irPantallaPrincipal(profesorSesion);
        } else {
            Utilidades.mostrarAlertaSimple("Credenciales incorrectas", 
                    "No. de personal y/o contraseña incorrectos, por favor verifica la información", Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaPrincipal(Profesor profesorSesion) {
        try {
            FXMLLoader cargador = new FXMLLoader(AppControlEscolarFX.class.getResource("vista/FXMLPrincipal.fxml"));
            Parent vista = cargador.load();
            FXMLPrincipalController controlador = cargador.getController();
            controlador.obtenerSesion(profesorSesion);
            Scene escena = new Scene(vista);
            Stage escenario = (Stage) textFieldNumeroPersonal.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Inicio");
            escenario.show();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
