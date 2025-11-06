package modelo;

import datos.conexionActividades;
import modelo.Sector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        try {
            conexionActividades conexion = new conexionActividades();
            Connection conn = conexion.conexionBBDD();

            String sql = "INSERT INTO actividades (nombre, horario, idSector, capacidad) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, horario);
            ps.setInt(3, sector.getIdSector());
            ps.setInt(4, capacidad);
            ps.executeUpdate();

            conn.close();
            return true;
        } catch (Exception ex) {
            System.err.println("Error al crear actividad: " + ex.getMessage());
            return false;
        }
    }

    public static List<Actividad> listar() {
        List<Actividad> actividades = new ArrayList<>();
        conexionActividades conexion = new conexionActividades();

        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "SELECT a.idActividad, a.nombre, a.horario, a.capacidad, " +
                    "s.idSector, s.nombre AS nombreSector, s.capacidadMax " +
                    "FROM actividades a " +
                    "JOIN sectores s ON a.idSector = s.idSector";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idActividad = rs.getInt("idActividad");
                String nombre = rs.getString("nombre");
                String horario = rs.getString("horario");
                int capacidad = rs.getInt("capacidad");

                Sector sector = new Sector(
                        rs.getInt("idSector"),
                        rs.getString("nombreSector"),
                        rs.getInt("capacidadMax")
                );

                Actividad actividad = new Actividad(idActividad, nombre, horario, sector, capacidad);
                actividades.add(actividad);
            }

        } catch (Exception ex) {
            System.err.println("Error al listar actividades: " + ex.getMessage());
        }

        return actividades;
    }


    public boolean eliminar() {
        try {
            conexionActividades conexion = new conexionActividades();
            Connection conn = conexion.conexionBBDD();
            System.out.println("Intentando eliminar actividad con ID: " + this.idActividad);
            System.out.println("SQL: DELETE FROM actividades WHERE idActividad = " + this.idActividad);

            String sql = "DELETE FROM actividades WHERE idActividad = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, this.idActividad);
            int filas = ps.executeUpdate(); // ← importante: verificar si afectó filas

            conn.close();
            return filas > 0;
        } catch (Exception ex) {
            System.err.println("Error al eliminar actividad: " + ex.getMessage());
            return false;
        }
    }

    public String getNombre() { return nombre; }

    public String getHorario() { return horario; }

    public Sector getSector() { return sector; }

    public int getCapacidad() { return capacidad; }
}

