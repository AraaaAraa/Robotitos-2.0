package Controladores;

import Modelos.RobGeneral;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;

public class VentanaBajaEnergiaController implements Initializable {
    
    private RobManager manager;
    
    @FXML private TableView<RobGeneral> tblBajaEnergia;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización de columnas, etc.
    }
    
    // Setter para recibir la instancia del manager de la ventana principal
    public void setManager(RobManager manager) {
        this.manager = manager;
        // Una vez que se recibe el manager, cargamos los datos filtrados
        cargarReporte();
    }
    
    private void cargarReporte() {
        if (manager != null) {
            // Carga solo los robots con energía < 20
            tblBajaEnergia.setItems(FXCollections.observableArrayList(manager.getRobotsBajaEnergia()));
        }
    }

    @FXML
    private void handleExportarCSV() {
        String nombreArchivo = "RobotsBajaEnergia.csv";
        try {
            manager.exportarBajaEnergiaCSV(nombreArchivo);
            
            // LLAMADA CORREGIDA: Usando el nombre de la clase
            VentanaPrincipalController.mostrarAlerta(AlertType.INFORMATION, "Exportación Exitosa", 
                    "Robots con baja energía exportados a " + nombreArchivo);
            
        } catch (IOException e) {
            // LLAMADA CORREGIDA: Usando el nombre de la clase
            VentanaPrincipalController.mostrarAlerta(AlertType.ERROR, "Error de Archivo", 
                    "No se pudo guardar el archivo CSV: " + e.getMessage());
        }
    }
    
    // (Método mostrarAlerta omitido por brevedad, es el mismo que en el controlador principal)
}