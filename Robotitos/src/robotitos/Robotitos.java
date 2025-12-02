package robotitos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Robotitos extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            // Carga la interfaz gráfica (FXML) de la ventana principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VentanaPrincipal.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            
            primaryStage.setTitle("Sistema de Gestión de Robots - UTN");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            System.err.println("Error al cargar la interfaz gráfica: " + e.getMessage());
            e.printStackTrace();
            // Una falla al cargar la vista es crítica
            System.exit(1); 
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
