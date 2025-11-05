package modelo;

import java.time.LocalDate;

public class Socio extends Usuario {
    private Membresia membresia;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Socio(int id, String nombre, String correo, String contrasena,
                 Membresia membresia, LocalDate fechaInicio, LocalDate fechaFin) {
        super(id, nombre, correo, contrasena);
        this.membresia = membresia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Membresia getMembresia() {
        return membresia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int getIdSocio() {
        return getId();
    }

    public void setIdSocio(int anInt) {
        setId(anInt);
    }
}

