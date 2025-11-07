import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import utilidad.Utilidades;

public class FXMLFormularioAlumnoController implements Initializable {

    @FXML
    private ImageView imageViewAlumno;

    @FXML
    private TextField textFieldMatricula;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidoPaterno;
    @FXML
    private TextField textFieldApellidoMaterno;
    @FXML
    private TextField textFieldCorreo;

    @FXML
    private DatePicker textFieldFechaNacimiento;

    @FXML
    private ComboBox<?> comboBoxFacultad;
    @FXML
    private ComboBox<?> comboBoxCarrera;

    private File fotoSeleccionada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void clicButtonSeleccionarFoto(ActionEvent event) {
        mostrarDialogo();
    }

    @FXML
    private void clicButtonCancelar(ActionEvent event) {
    }

    @FXML
    private void clicButtonGuardar(ActionEvent event) {
    }

    private void mostrarDialogo() {
        FileChooser dialogoSeleccion = new FileChooser();
        dialogoSeleccion.setTitle("Selecciona una foto");
        FileChooser.ExtensionFilter filtroImagen = new FileChooser.ExtensionFilter("Archivos JPG (.jpg)", "*.jpg");
        dialogoSeleccion.getExtensionFilters().add(filtroImagen);
        fotoSeleccionada = dialogoSeleccion.showOpenDialog(textFieldNombre.getScene().getWindow());
        
        if(fotoSeleccionada != null) {
            mostrarFoto(fotoSeleccionada);
        }
    }

    private void mostrarFoto(File foto) {
        try {
            BufferedImage bufferedImage = ImageIO.read(foto);
            Image imagen = SwingFXUtils.toFXImage(bufferedImage, null);
            imageViewAlumno.setImage(imagen);
        } catch (IOException e) {
            Utilidades.mostrarAlertaSimple("Error en archivo", "Lo sentimos la imagen seleccionada no puede cargarse, por favor seleccione otro archivo.", Alert.AlertType.ERROR);
        }
    }
}
