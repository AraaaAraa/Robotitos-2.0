/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

// Importaciones necesarias
import Excepciones.SerieDuplicada;      // ← CON 'c'
import Excepciones.EnergiaIncorrecta;   // ← Agregada por si la necesitás
import Excepciones.SerieInvalido;       // ← Agregada por si la necesitás
import Modelos.RobGeneral;
import Modelos.Domesticos;
import Modelos.Industriales;

// Para JSON (requiere librería externa como Gson)
 import com.google.gson.Gson; 
 import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

// Para manejo de archivos (IO)
import java.io.BufferedReader; // Opcional para cargar JSON
import java.io.BufferedWriter; // Para escritura eficiente (Buenas Prácticas)
import java.io.File;
import java.io.FileReader;    // Para lectura de caracteres
import java.io.FileWriter;    // Para escritura de caracteres
import java.io.IOException;   // Para manejo de excepciones

import java.util.ArrayList;
import java.util.List;

public class RobManager {
    
    private List<RobGeneral> listaRobots;
    private static final String ARCHIVO_JSON = "robots_data.json"; // Ruta Relativa
    
    public RobManager() {
        this.listaRobots = new ArrayList<>();
        cargarRobots(); // Carga automática al iniciar
    }
    
    // =========================================================================
    // Métodos de UTILIDAD
    // =========================================================================

    public List<RobGeneral> getListaRobots() {
        return listaRobots;
    }
    
    public RobGeneral buscarRobot(int serie) {
        for (RobGeneral robot : listaRobots) {
            if (robot.getSerie() == serie) {
                return robot;
            }
        }
        return null;
    }

    private boolean existeSerie(int serie) {
        return buscarRobot(serie) != null;
    }
    
    // =========================================================================
    // 1. Implementación CRUD
    // =========================================================================

    public void agregarRobot(RobGeneral nuevoRobot) throws SerieDuplicada {
        // Valida unicidad antes de añadir
        if (existeSerie(nuevoRobot.getSerie())) {
            throw new SerieDuplicada("Error: El número de serie " + nuevoRobot.getSerie() + " ya está registrado.");
        }
        
        this.listaRobots.add(nuevoRobot);
        guardarRobots(); // Guardado automático
    }

    public void modificarRobot(RobGeneral robotModificado) {
        // Asumiendo que el robotModificado ya tiene la serie correcta (inmutable o no modificada)
        // La validación de los campos se hace en el setter de RobGeneral
        
        // No es necesario modificar si el objeto ya está en la lista (es el mismo objeto)
        // Solo verificamos que exista y guardamos.
        if (existeSerie(robotModificado.getSerie())) {
            guardarRobots(); // Guardado automático
        }
    }

    public void eliminarRobot(int serie) {
        boolean eliminado = this.listaRobots.removeIf(robot -> robot.getSerie() == serie);
        if (eliminado) {
            guardarRobots(); // Guardado automático
        }
    }
    
    // =========================================================================
    // 2. Persistencia JSON (Cargar/Guardar)
    // =========================================================================

    // NOTA: Esta implementación de persistencia JSON es conceptual, ya que 
    // la implementación real de Gson/Jackson para polimorfismo es compleja.
    
    private void guardarRobots() {
        // Aquí se usaría la librería JSON (Gson o Jackson) para escribir la lista.
        // Se usa try-with-resources para el cierre automático del recurso
      
        try (FileWriter writer = new FileWriter(ARCHIVO_JSON)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(listaRobots, writer);
        } catch (IOException e) {
            // Manejo de errores claro
            System.err.println("Error al guardar la colección en JSON: " + e.getMessage()); 
        }
        
        System.out.println(" Colección guardada automáticamente en JSON.");
    }

    private void cargarRobots() {
        File file = new File(ARCHIVO_JSON);
        if (file.exists()) { // Verificar existencia
            // Aquí se usaría la librería JSON para cargar la lista.
            
            try (FileReader reader = new FileReader(ARCHIVO_JSON)) {
                Gson gson = new GsonBuilder().create();
                java.lang.reflect.Type tipoLista = new TypeToken<List<RobGeneral>>() {}.getType();
                
                List<RobGeneral> robotsCargados = gson.fromJson(reader, tipoLista);
                if (robotsCargados != null) {
                    this.listaRobots = robotsCargados;
                }
            } catch (IOException e) {
                System.err.println("Error al cargar la colección de JSON: " + e.getMessage());
            }
            System.out.println("✅ Colección cargada desde JSON.");
        } else {
            System.out.println("Archivo JSON no encontrado. Iniciando con lista vacía.");
        }
    }
    
    // =========================================================================
    // 3. Exportación CSV (Robots con baja energía)
    // =========================================================================

    /**
     * Filtra y retorna los robots con un nivel de energía menor a 20.
     * Requisito de ventana de reporte
     */
    public List<RobGeneral> getRobotsBajaEnergia() {
        List<RobGeneral> bajaEnergia = new ArrayList<>();
        for (RobGeneral robot : listaRobots) {
            if (robot.getEnergia() < 20) {
                bajaEnergia.add(robot);
            }
        }
        return bajaEnergia;
    }

    /**
     * Exporta la lista de robots con baja energía a un archivo CSV.
     * Requisito de exportación para mantenimiento
     * Utiliza BufferedWriter y try-with-resources.
     */
    public void exportarBajaEnergiaCSV(String nombreArchivo) throws IOException {
        List<RobGeneral> robotsBajaEnergia = getRobotsBajaEnergia();
        
        // Uso de try-with-resources para asegurar el cierre del recurso
        // Uso de BufferedWriter para escritura eficiente
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            
            // 1. Cabecera CSV
            bw.write("Tipo,Nombre,Serie,Energia,Detalle_Especifico\n"); 
            
            // 2. Contenido
            for (RobGeneral robot : robotsBajaEnergia) {
                String linea = robot.getTipo() + "," + 
                               robot.getNombre() + "," + 
                               robot.getSerie() + "," + 
                               robot.getEnergia() + ",";

                // Detalle específico (Polimorfismo con instanceof)
                if (robot instanceof Domesticos) {
                    Domesticos d = (Domesticos) robot;
                    linea += "Tareas Domesticas: " + d.getTareasDomesticas();
                } else if (robot instanceof Industriales) {
                    Industriales i = (Industriales) robot;
                    linea += "Capacidad Max (kg): " + i.getCapacidadMax();
                }
                
                bw.write(linea);
                bw.newLine(); // Salto de línea seguro
            }
            
            System.out.println("✅ Reporte CSV de baja energía exportado a: " + nombreArchivo);
            
        } catch (IOException e) {
            // Re-lanza IOException para que la capa de la Interfaz Gráfica la capture y la muestre.
            throw new IOException("Error al escribir el archivo CSV: " + e.getMessage());
        }
    }
}
