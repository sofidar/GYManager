package modelo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import datos.conexionSocios;
import java.sql.Connection;
import datos.conexionMembresias;
import java.util.*;


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
    public static Socio verificar(String correo, String contrasena) {
        conexionSocios conexion = new conexionSocios();
        conexionMembresias conexionMembresias = new conexionMembresias();
        Socio socio = null;

        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "SELECT * FROM socios WHERE correo = ? AND contrasena = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("idSocio");
                int idMembresia = rs.getInt("idMembresia");
                Date fechaInicio = rs.getDate("fechaInicio");
                Date fechaFin = rs.getDate("fechaFin");

                Membresia membresia = conexionMembresias.obtenerMembresia(idMembresia);

                socio = new Socio(
                        id,
                        rs.getString("nombre"),
                        correo,
                        contrasena,
                        membresia,
                        fechaInicio != null ? fechaInicio.toLocalDate() : null,
                        fechaFin != null ? fechaFin.toLocalDate() : null
                );
            }

        } catch (Exception ex) {
            System.err.println("Error al verificar socio: " + ex.getMessage());
        }

        return socio;
    }

    public void darseDeBaja() {
        conexionSocios conexion = new conexionSocios();
        conexion.eliminarSocio(this.getId());
    }

    public Membresia getMembresia() {
        return membresia;
    }
    public void setMembresia(Membresia membresia) {
        this.membresia = membresia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getIdSocio() {
        return getId();
    }

    public void setIdSocio(int anInt) {
        setId(anInt);
    }

    public boolean crear() {
        conexionSocios conexion = new conexionSocios();
        return conexion.insertarSocio(this);
    }

    public static List<Socio> listar() {
        List<Socio> socios = new ArrayList<>();
        conexionSocios conexion = new conexionSocios();
        conexionMembresias conexionMembresias = new conexionMembresias();

        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "SELECT s.*, m.tipo AS tipoMembresia FROM socios s JOIN membresias m ON s.idMembresia = m.idMembresia";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idSocio");
                String nombre = rs.getString("nombre");
                String correo = rs.getString("correo");
                String contrasena = rs.getString("contrasena");
                int idMembresia = rs.getInt("idMembresia");
                Membresia membresia = conexionMembresias.obtenerMembresia(idMembresia);

                Socio socio = new Socio(
                        id,
                        nombre,
                        correo,
                        contrasena,
                        membresia,
                        rs.getDate("fechaInicio").toLocalDate(),
                        rs.getDate("fechaFin").toLocalDate()
                );

                socios.add(socio);
            }

        } catch (Exception ex) {
            System.err.println("Error al listar socios: " + ex.getMessage());
        }

        return socios;
    }
    public boolean eliminar() {
        try {
            conexionSocios conexion = new conexionSocios();
            Connection conn = conexion.conexionBBDD();

            String sql = "DELETE FROM socios WHERE idSocio = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, this.id);
            ps.executeUpdate();

            conn.close();
            return true;
        } catch (Exception ex) {
            System.err.println("Error al eliminar socio: " + ex.getMessage());
            return false;
        }
    }
}


