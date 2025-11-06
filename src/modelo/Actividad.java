package modelo;

import datos.ActividadDAO;
import datos.conexionActividades;
import interfaces.IPersistible;
import modelo.Sector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Actividad implements IPersistible {
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
    private int idActividad;

    public Actividad(int idActividad, String nombre, String horario, Sector sector, int capacidad) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.horario = horario;
        this.sector = sector;
        this.capacidad = capacidad;
    }

    public int getIdActividad() {
        return idActividad;
    }


    public boolean crear() {
        ActividadDAO dao = new ActividadDAO();
        return dao.crear(this);
    }

    public static List<Actividad> listar() {
        ActividadDAO dao = new ActividadDAO();
        return dao.listar();
    }

    public boolean eliminar() {
        ActividadDAO dao = new ActividadDAO();
        return dao.eliminar(this.idActividad);
    }

    public String getNombre() { return nombre; }

    public String getHorario() { return horario; }

    public Sector getSector() { return sector; }

    public int getCapacidad() { return capacidad; }
    @Override
    public String toString() {
        return nombre + " - " + horario + " - " + sector.getNombre();
    }

}

