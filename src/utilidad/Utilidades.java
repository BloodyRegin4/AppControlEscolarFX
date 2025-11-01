package utilidad;

import appcontrolescolarfx.AppControlEscolarFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;

public class Utilidades {
    public static void mostrarAlertaSimple(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    
    public static FXMLLoader obtenerVistaMemoria(String url) {
        return new FXMLLoader(AppControlEscolarFX.class.getResource(url));
    }
}
