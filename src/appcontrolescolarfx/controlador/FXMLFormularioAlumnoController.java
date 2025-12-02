package appcontrolescolarfx.controlador;

import appcontrolescolarfx.dominio.AlumnoImplementacion;
import appcontrolescolarfx.dominio.CatalogoImplementacion;
import appcontrolescolarfx.interfaces.IObservador;
import appcontrolescolarfx.modelo.pojo.Alumno;
import appcontrolescolarfx.modelo.pojo.Carrera;
import appcontrolescolarfx.modelo.pojo.Facultad;
import appcontrolescolarfx.modelo.pojo.Respuesta;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;
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
    private DatePicker datePickerFechaNacimiento;
    
    @FXML
    private ComboBox<Facultad> comboBoxFacultad;
    @FXML
    private ComboBox<Carrera> comboBoxCarrera;

    private File fotoSeleccionada;
    private ObservableList<Facultad> observableListFacultades;
    private ObservableList<Carrera> observableListCarreras;
    private IObservador observador;
    private Alumno alumnoEdicion;
    
    private static final long FOTO_PESO_MAXIMO = 1024 * 1024;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarListenerFacultad();
        cargarFacultades();
    }

    @FXML
    private void clicButtonSeleccionarFoto(ActionEvent event) {
        mostrarDialogo();
    }

    @FXML
    private void clicButtonGuardar(ActionEvent event) throws IOException {
        if(sonCamposValidos()) {
            if(alumnoEdicion == null) {
                registrarAlumno();
            } else {
                editarAlumno();
            }
        }
    }
    
    @FXML
    private void clicButtonCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void configurarListenerFacultad() {
        comboBoxCarrera.setDisable(true);
        comboBoxFacultad.valueProperty().addListener(new ChangeListener<Facultad>() {
            @Override
            public void changed(ObservableValue<? extends Facultad> observable, Facultad oldValue, Facultad newValue) {
                if (newValue != null) {
                    comboBoxCarrera.setDisable(false);
                    cargarCarrerasPorFacultad(newValue.getIdFacultad());
                } else {
                    comboBoxCarrera.setDisable(true);
                    comboBoxCarrera.setItems(null); 
                }
            }
        });
    }
    
    private void cargarFacultades() {
        HashMap<String,Object> respuesta = CatalogoImplementacion.obtenerFacultades();
        
        if(!(boolean) respuesta.get("error")) {
            List<Facultad> facultadesBaseDatos = (List<Facultad>) respuesta.get("facultades");
            observableListFacultades = FXCollections.observableArrayList();
            observableListFacultades.addAll(facultadesBaseDatos);
            comboBoxFacultad.setItems(observableListFacultades);
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar facultades", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void cargarCarrerasPorFacultad(int idFacultad) {
        HashMap<String,Object> respuesta = CatalogoImplementacion.obtenerCarrerasPorFacultad(idFacultad);
        
        if(!(boolean) respuesta.get("error")) {
            List<Carrera> carrerasBaseDatos = (List<Carrera>) respuesta.get("carreras");
            observableListCarreras = FXCollections.observableArrayList();
            observableListCarreras.addAll(carrerasBaseDatos);
            comboBoxCarrera.setItems(observableListCarreras);
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar carreras", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void mostrarDialogo() {
        FileChooser dialogoSeleccion = new FileChooser();
        dialogoSeleccion.setTitle("Selecciona una foto");
        FileChooser.ExtensionFilter filtroImagen = new FileChooser.ExtensionFilter("Archivos de imagen (.jpg, .png)", "*.jpg", "*.png");
        dialogoSeleccion.getExtensionFilters().add(filtroImagen);
        File archivoTemporal = dialogoSeleccion.showOpenDialog(textFieldNombre.getScene().getWindow());
        
        if(archivoTemporal != null) {
            long tamanioArchivo = archivoTemporal.length();
            
            if (tamanioArchivo > FOTO_PESO_MAXIMO) {
                Utilidades.mostrarAlertaSimple("Error de archivo", "El archivo es demasiado grande. El peso máximo permitido es de 1MB.", Alert.AlertType.ERROR);
                fotoSeleccionada = null; 
                imageViewAlumno.setImage(null);
            } else {
                fotoSeleccionada = archivoTemporal;
                mostrarFoto(fotoSeleccionada);
            }
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
    
    private boolean sonCamposValidos() {
        boolean valido = true;
        String mensajeError = "Se encontraron los siguientes errores:\n";
        
        if(alumnoEdicion == null && fotoSeleccionada == null) {
            valido = false;
            mensajeError += "- Foto del alumno requerida. \n";
        }
        
        if(textFieldMatricula.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Matrícula del alumno requerida. \n";
        }
        
        if(textFieldNombre.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Nombre del alumno requerido. \n";
        }
        
        if(textFieldApellidoPaterno.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Apellido paterno del alumno requerido. \n";
        }
        
        if(textFieldApellidoMaterno.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Apellido materno del alumno requerido. \n";
        }
        
        if(textFieldCorreo.getText().isEmpty()) {
            valido = false;
            mensajeError += "- Correo del alumno requerido. \n";
        }
        
        if(datePickerFechaNacimiento.getValue() == null) {
            valido = false;
            mensajeError += "- Fecha de nacimiento del alumno requerida. \n";
        }
        
        if(comboBoxFacultad.getSelectionModel().getSelectedItem() == null) {
            valido = false;
            mensajeError += "- Facultad del alumno requerida. \n";
        }
        
        if(comboBoxCarrera.getSelectionModel().getSelectedItem() == null) {
            valido = false;
            mensajeError += "- Carrera del alumno requerida. \n";
        }
        
        if(!valido) {
            Utilidades.mostrarAlertaSimple("Campos vacíos", mensajeError, Alert.AlertType.WARNING);
        }
        
        return valido;
    }
    
    private Alumno obtenerAlumnos() throws IOException {
        Alumno alumno = new Alumno();

        alumno.setMatricula(textFieldMatricula.getText());
        alumno.setNombre(textFieldNombre.getText());
        alumno.setApellidoPaterno(textFieldApellidoPaterno.getText());
        alumno.setApellidoMaterno(textFieldApellidoMaterno.getText());
        alumno.setCorreo(textFieldCorreo.getText());
        alumno.setFechaNacimiento(datePickerFechaNacimiento.getValue().toString());
        Carrera carreraSeleccionada = comboBoxCarrera.getSelectionModel().getSelectedItem();
        alumno.setIdCarrera(carreraSeleccionada.getIdCarrera());
    
        if (fotoSeleccionada != null) {
            byte[] fotoBytes = Files.readAllBytes(fotoSeleccionada.toPath());
            alumno.setFoto(fotoBytes);
        } else if (alumnoEdicion != null) {
            alumno.setFoto(alumnoEdicion.getFoto());
        }
        
        return alumno;  
    }
    
    private void registrarAlumno() {
        try {
            Alumno alumnoNuevo = obtenerAlumnos();
            Respuesta respuesta = AlumnoImplementacion.registrarAlumno(alumnoNuevo);
            
            if(!respuesta.isError()) {
                Utilidades.mostrarAlertaSimple("Alumno registrado", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error al registrar", respuesta.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch(IOException e) {
            Utilidades.mostrarAlertaSimple("Error con archivo de imagen", "La foto seleccionada no puede ser guardada, por favor intenta con otra fotografía", Alert.AlertType.ERROR);
        }
    }
    
    private void editarAlumno() throws IOException {
        Alumno alumnoEdicion = obtenerAlumnos();
        alumnoEdicion.setIdAlumno(this.alumnoEdicion.getIdAlumno());
        HashMap<String,Object> resultado = AlumnoImplementacion.editarAlumno(alumnoEdicion);

        if(!(boolean) resultado.get("error")) {
            Utilidades.mostrarAlertaSimple("Alumno editado correctamente", resultado.get("mensaje").toString(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("editar", alumnoEdicion.getNombre());
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al editar", resultado.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }
    
    public void inicializarDatos(IObservador observador, Alumno alumnoDeTabla) {
        this.observador = observador;
    
        if (alumnoDeTabla != null) {
            HashMap<String, Object> respuesta = AlumnoImplementacion.obtenerAlumnoPorId(alumnoDeTabla.getIdAlumno());
        
            if (!(boolean) respuesta.get("error")) {
                this.alumnoEdicion = (Alumno) respuesta.get("alumno");
                textFieldNombre.setText(this.alumnoEdicion.getNombre());
                textFieldApellidoPaterno.setText(this.alumnoEdicion.getApellidoPaterno());
                textFieldApellidoMaterno.setText(this.alumnoEdicion.getApellidoMaterno());
                textFieldMatricula.setText(this.alumnoEdicion.getMatricula());
                textFieldCorreo.setText(this.alumnoEdicion.getCorreo());
            
                if(this.alumnoEdicion.getFechaNacimiento() != null) {
                    datePickerFechaNacimiento.setValue(LocalDate.parse(this.alumnoEdicion.getFechaNacimiento()));
                }
            
                int posicionFacultad = obtenerPosicionFacultad(this.alumnoEdicion.getIdFacultad());
                comboBoxFacultad.getSelectionModel().select(posicionFacultad);
                cargarCarrerasPorFacultad(this.alumnoEdicion.getIdFacultad());
                int posicionCarrera = obtenerPosicionCarrera(this.alumnoEdicion.getIdCarrera());
                comboBoxCarrera.getSelectionModel().select(posicionCarrera);
            
                if (this.alumnoEdicion.getFoto() != null && this.alumnoEdicion.getFoto().length > 0) {
                    try {
                        Image imagen = new Image(new ByteArrayInputStream(this.alumnoEdicion.getFoto()));
                        imageViewAlumno.setImage(imagen);
                    } catch (Exception e) {
                        System.out.println("Error al cargar la imagen en la vista: " + e.getMessage());
                    }
                }
            } else {
                Utilidades.mostrarAlertaSimple("Error de carga", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
                cerrarVentana();
            }
        }
    }
    
    private int obtenerPosicionFacultad(int idFacultad) {
        for(int i = 0; i < observableListFacultades.size(); i++) {
            if(observableListFacultades.get(i).getIdFacultad() == idFacultad) {
                return i;
            }
        }
        return -1;
    }
    
    private int obtenerPosicionCarrera(int idCarrera) {
        if (observableListCarreras == null) {
            return -1;
        }
        
        for(int i = 0; i < observableListCarreras.size(); i++) {
            if(observableListCarreras.get(i).getIdCarrera() == idCarrera) {
                return i;
            }
        }
        return -1;
    }
    
    private void cerrarVentana() {
        ((Stage) textFieldNombre.getScene().getWindow()).close();
    }
}