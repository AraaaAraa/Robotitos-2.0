package Modelos;

public class Industriales extends RobGeneral {
    private double capacidadKg;

    public Industriales(String nombre, int energia, int serie, double capacidadKg) {
        super(nombre, energia, serie);
        this.capacidadKg = capacidadKg;
    }

    public double getCapacidadKg() {
        return capacidadKg;
    }

    public void setCapacidadKg(double capacidadKg) {
        this.capacidadKg = capacidadKg;
    }

    @Override
    public String getTipo() {
        return "Industrial";
    }
}