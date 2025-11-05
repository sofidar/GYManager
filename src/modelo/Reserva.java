package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private int id;
    private LocalDate fecha;
    private String estado;
    private Socio socio;
    private Actividad actividad;

    public Reserva(int id, LocalDate fecha, String estado, Socio socio, Actividad actividad) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.socio = socio;
        this.actividad = actividad;
    }

    public void cancelar() {
        this.estado = "Cancelada";
        System.out.println("Reserva cancelada para " + socio.getNombre() + " en " + actividad.getNombre());
    }
}
