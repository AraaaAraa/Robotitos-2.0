package Controladores;

import Excepciones.EnergiaIncorrecta;
import Excepciones.SerieDuplicada;
import Excepciones.SerieInvalido;
import Modelos.Domesticos;
import Modelos.Industriales;
import Modelos.RobGeneral;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// Clases de JavaFX
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

public class VentanaPrincipalController implements Initializable {

    // Instancia del Manager (Capa de Negocio)
    private RobManager manager = new RobManager(); 

    // Controles FXML
    @FXML private TableView<RobGeneral> tblRobots;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEnergia;
    @FXML private TextField txtSerie;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtDetalle; // Para tareas o capacidad

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Inicializar ComboBox de Tipos de Robot
        cmbTipo.setItems(FXCollections.observableArrayList("Doméstico", "Industrial"));
        cmbTipo.setValue("Doméstico");
        
        // 2. Cargar datos iniciales
        cargarTabla();
    }
    
    private void cargarTabla() {
        tblRobots.setItems(FXCollections.observableArrayList(manager.getListaRobots()));
    }

    // =========================================================================
    // Métodos CRUD
    // =========================================================================

    @FXML
    private void handleAgregarRobot() {
        try {
            String nombre = txtNombre.getText();
            int energia = Integer.parseInt(txtEnergia.getText());
            int serie = Integer.parseInt(txtSerie.getText());
            String tipo = cmbTipo.getValue();
            int detalle = Integer.parseInt(txtDetalle.getText());

            RobGeneral nuevoRobot;

            if ("Doméstico".equals(tipo)) {
                // Constructor: (String nombre, int energia, int serie, int tareas)
                nuevoRobot = new Domesticos(nombre, energia, serie, detalle);
            } else if ("Industrial".equals(tipo)) {
                // Constructor: (String nombre, int energia, int serie, double capacidadKg)
                nuevoRobot = new Industriales(nombre, energia, serie, detalle);
            } else {
                mostrarAlerta(AlertType.ERROR, "Error de Tipo", "Debe seleccionar un tipo de robot válido.");
                return;
            }

            manager.agregarRobot(nuevoRobot); 
            
            cargarTabla();
            limpiarCampos();
            mostrarAlerta(AlertType.INFORMATION, "Éxito", "Robot agregado correctamente.");

        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Error de Formato", "Energía, Serie y Detalle deben ser números válidos.");
        
        } catch (EnergiaIncorrecta | SerieInvalido | SerieDuplicada e) {
            mostrarAlerta(AlertType.ERROR, "Error de Validación", e.getMessage());
        
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error Inesperado", "Ocurrió un error: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleEliminarRobot() {
        try {
            int serie = Integer.parseInt(txtSerie.getText());
            manager.eliminarRobot(serie);
            cargarTabla();
            limpiarCampos();
            mostrarAlerta(AlertType.INFORMATION, "Éxito", "Robot eliminado correctamente.");
            
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Ingrese un número de serie válido.");
        }
    }
    
    private void limpiarCampos() {
        txtNombre.clear();
        txtEnergia.clear();
        txtSerie.clear();
        txtDetalle.clear();
    }

    // =========================================================================
    // Apertura de la Ventana de Reporte
    // =========================================================================

    @FXML
    private void handleAbrirVentanaReporte() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VentanaBajaEnergia.fxml"));
            Parent root = loader.load();
            
            VentanaBajaEnergiaController reporteController = loader.getController();
            reporteController.setManager(manager);

            Stage stage = new Stage();
            stage.setTitle("Robots con Energía Baja ( < 20 )");
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (IOException e) {
             mostrarAlerta(AlertType.ERROR, "Error de Vista", "No se pudo cargar la ventana de reporte.");
        }
    }

    // =========================================================================
    // Método de Utilidad para Alertas
    // =========================================================================
    public static void mostrarAlerta(AlertType tipo, String titulo, String mensaje) { 
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}