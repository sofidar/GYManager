package modelo;

public class Sector {
    private int idSector;
    private String nombre;
    private int capacidadMax;

    public Sector(int idSector, String nombre, int capacidadMax) {
        this.idSector = idSector;
        this.nombre = nombre;
        this.capacidadMax = capacidadMax;
    }

    public int getIdSector() {
        return idSector;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidadMax() {
        return capacidadMax;
    }

    @Override
    public String toString() {
        return nombre; // Esto hace que el comboBox muestre el nombre
    }
}
