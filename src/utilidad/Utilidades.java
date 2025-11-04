package utilidad;

import appcontrolescolarfx.AppControlEscolarFX;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Utilidades {
    public static void mostrarAlertaSimple(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    
    public static boolean mostrarAlertaConfirmacion(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        Optional<ButtonType> buttonSeleccion = alerta.showAndWait();
        return(buttonSeleccion.get() == ButtonType.OK);
    }
    
    public static FXMLLoader obtenerVistaMemoria(String url) {
        return new FXMLLoader(AppControlEscolarFX.class.getResource(url));
    }
}
