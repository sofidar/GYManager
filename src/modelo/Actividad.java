package modelo;

public class Actividad {
    private String nombre;
    private String horario;
    private Sector sector;
    private int capacidad;

    public Actividad(String nombre, String horario, Sector sector, int capacidad) {
        this.nombre = nombre;
        this.horario = horario;
        this.sector = sector;
        this.capacidad = capacidad;
    }

    public String getNombre() { return nombre; }

    public String getHorario() { return horario; }

    public Sector getSector() { return sector; }

    public int getCapacidad() { return capacidad; }
}

