/*
Serialización: Es el proceso de convertir un objeto en un flujo de bytes para que su estado pueda ser guardado o transmitido.
 */
package Modelos;

import java.io.Serializable;
import java.util.Objects;

import Excepciones.EnergiaIncorrecta;
import Excepciones.SerieInvalido;

public abstract class RobGeneral implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private int energia;
    private int serie;

    public RobGeneral(String nombre, int energia, int serie) {
        setNombre(nombre);
        setEnergia(energia);
        setSerie(serie);
    }

    public final void setEnergia(int energia) {
        if (energia < 0 || energia > 100) {
            throw new EnergiaIncorrecta("El nivel de energía debe estar entre 0 y 100.");
        }
        this.energia = energia;
    }

    public final void setSerie(int serie) {
        if (serie <= 0) {
            throw new SerieInvalido("El número de serie debe ser positivo.");
        }
        this.serie = serie;
    }

    public final void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public int getEnergia() {
        return energia;
    }

    public int getSerie() {
        return serie;
    }

    public abstract String getTipo();

    @Override
    public int hashCode() {
        return Objects.hash(nombre, energia, serie);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RobGeneral)) return false;
        RobGeneral other = (RobGeneral) obj;
        return energyEquals(other) && serie == other.serie && Objects.equals(nombre, other.nombre);
    }

    private boolean energyEquals(RobGeneral other) {
        return this.energia == other.energia;
    }
}
