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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilidad.Utilidades;

public class FXMLInicioSesionController implements Initializable {
    @FXML
    private TextField tfNumPersonal;
    @FXML
    private TextField pfPassword;
    
    @FXML
    private Label lbErrorNumPersonal;
    @FXML
    private Label lbErrorPassword;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    @FXML
    private void clicIngresar(ActionEvent event) {
        String noPersonal = tfNumPersonal.getText();
        String password = pfPassword.getText();
        if(sonDatosValidos(noPersonal, password)) {
            validarSesion(noPersonal, password);
        }
    }
    
    private boolean sonDatosValidos(String noPersonal, String password) {
        boolean correcto = true;
        lbErrorNumPersonal.setText("");
        lbErrorPassword.setText("");
        if(noPersonal == null || noPersonal.isEmpty()) {
            correcto = false;
            lbErrorNumPersonal.setText("Numero de personal obligatorio");
        }
        if(password == null || password.isEmpty()) {
            correcto = false;
            lbErrorPassword.setText("Contraseña obligatoria");
        }
        return correcto;
    }
    
    private void validarSesion(String noPersonal, String password) {
        HashMap<String,Object> respuesta = AutenticacionImplementacion.verificarSesionProfesor(noPersonal, password);
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
            
            //Creamos escena y reutilizamos escenario
            Scene escena = new Scene(vista);
            Stage escenario = (Stage) tfNumPersonal.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Inicio");
            escenario.show();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
