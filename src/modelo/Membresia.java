package modelo;

import java.time.LocalDate;

public class Membresia {
    private int idMembresia;
    private String tipo;
    private int duracionMeses;
    private double precio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Membresia(int idMembresia, String tipo, int duracionMeses, double precio,
                     LocalDate fechaInicio, LocalDate fechaFin) {
        this.idMembresia = idMembresia;
        this.tipo = tipo;
        this.duracionMeses = duracionMeses;
        this.precio = precio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    // Nuevo constructor para usar en mostrarSocios()
    public Membresia(int idMembresia, String tipo, int duracionMeses) {
        this.idMembresia = idMembresia;
        this.tipo = tipo;
        this.duracionMeses = duracionMeses;
    }

    // Getters y setters
    public int getIdMembresia() { return idMembresia; }
    public String getTipo() { return tipo; }
    public int getDuracionMeses() { return duracionMeses; }
    public double getPrecio() { return precio; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }

    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public void renovar() {
        this.fechaInicio = LocalDate.now();
        this.fechaFin = fechaInicio.plusMonths(duracionMeses);
    }

    @Override
    public String toString() {
        return tipo + " (" + fechaInicio + " - " + fechaFin + ")";
    }
}
