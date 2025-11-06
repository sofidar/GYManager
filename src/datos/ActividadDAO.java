package datos;

import interfaces.IDAO;
import modelo.Actividad;
import modelo.Sector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ActividadDAO implements IDAO<Actividad> {

    public boolean crear(Actividad actividad) {
        try {
            conexionActividades conexion = new conexionActividades();
            Connection conn = conexion.conexionBBDD();

            String sql = "INSERT INTO actividades (nombre, horario, idSector, capacidad) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, actividad.getNombre());
            ps.setString(2, actividad.getHorario());
            ps.setInt(3, actividad.getSector().getIdSector());
            ps.setInt(4, actividad.getCapacidad());
            ps.executeUpdate();

            conn.close();
            return true;

        } catch (Exception ex) {
            System.err.println("Error al crear actividad: " + ex.getMessage());
            return false;
        }
    }


    public List<Actividad> listar() {
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


    public boolean eliminar(int idActividad) {
        try {
            conexionActividades conexion = new conexionActividades();
            Connection conn = conexion.conexionBBDD();

            System.out.println("Intentando eliminar actividad con ID: " + idActividad);
            System.out.println("SQL: DELETE FROM actividades WHERE idActividad = " + idActividad);

            String sql = "DELETE FROM actividades WHERE idActividad = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idActividad);
            int filas = ps.executeUpdate();

            conn.close();
            return filas > 0;

        } catch (Exception ex) {
            System.err.println("Error al eliminar actividad: " + ex.getMessage());
            return false;
        }

    }
}

