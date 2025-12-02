package Modelos;

public class Domesticos extends RobGeneral {
    private int tareas;

    public Domesticos(String nombre, int energia, int serie, int tareas) {
        super(nombre, energia, serie);
        this.tareas = tareas;
    }

    public int getTareas() {
        return tareas;
    }

    public void setTareas(int tareas) {
        this.tareas = tareas;
    }

    @Override
    public String getTipo() {
        return "Domestico";
    }
}
