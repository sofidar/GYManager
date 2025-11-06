package datos;

import interfaces.IDAO;
import modelo.Actividad;
import modelo.Sector;
import modelo.Socio;
import modelo.Membresia;
import excepciones.UsuarioNoEncontradoException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SocioDAO implements IDAO<Socio> {

    public Socio obtenerPorCredenciales(String correo, String contrasena) throws UsuarioNoEncontradoException {
        conexionSocios conexion = new conexionSocios();
        conexionMembresias conexionMembresias = new conexionMembresias();

        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "SELECT * FROM socios WHERE correo = ? AND contrasena = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("idSocio");
                String nombre = rs.getString("nombre");
                int idMembresia = rs.getInt("idMembresia");
                Date inicio = rs.getDate("fechaInicio");
                Date fin = rs.getDate("fechaFin");

                Membresia membresia = conexionMembresias.obtenerMembresia(idMembresia);

                return new Socio(
                        id,
                        nombre,
                        correo,
                        contrasena,
                        membresia,
                        inicio != null ? inicio.toLocalDate() : null,
                        fin != null ? fin.toLocalDate() : null
                );
            } else {
                throw new UsuarioNoEncontradoException("No se encontró un socio con ese correo y contraseña.");
            }

        } catch (UsuarioNoEncontradoException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("Error técnico al obtener socio: " + ex.getMessage());
            throw new UsuarioNoEncontradoException("Error técnico al verificar socio.");
        }
    }

    public boolean eliminar(int idSocio) {
        try {
            conexionSocios conexion = new conexionSocios();
            Connection conn = conexion.conexionBBDD();

            String sql = "DELETE FROM socios WHERE idSocio = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idSocio);
            int filas = ps.executeUpdate();

            conn.close();
            return filas > 0;

        } catch (Exception ex) {
            System.err.println("Error al eliminar socio: " + ex.getMessage());
            return false;
        }
    }


    public List<Socio> listar() {
        List<Socio> socios = new ArrayList<>();
        conexionSocios conexion = new conexionSocios();
        conexionMembresias conexionMembresias = new conexionMembresias();

        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "SELECT * FROM socios";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idSocio");
                String nombre = rs.getString("nombre");
                String correo = rs.getString("correo");
                String contrasena = rs.getString("contrasena");
                int idMembresia = rs.getInt("idMembresia");
                Date inicio = rs.getDate("fechaInicio");
                Date fin = rs.getDate("fechaFin");

                Membresia membresia = conexionMembresias.obtenerMembresia(idMembresia);

                Socio socio = new Socio(
                        id,
                        nombre,
                        correo,
                        contrasena,
                        membresia,
                        inicio != null ? inicio.toLocalDate() : null,
                        fin != null ? fin.toLocalDate() : null
                );

                socios.add(socio);
            }

        } catch (Exception ex) {
            System.err.println("Error al listar socios: " + ex.getMessage());
        }

        return socios;
    }

    public int contarActividadesInscriptas(int idSocio) {
        try (Connection conn = new conexionSocios().conexionBBDD()) {
            String sql = "SELECT COUNT(*) FROM reservas WHERE idSocio = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idSocio);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception ex) {
            System.err.println("Error al contar inscripciones: " + ex.getMessage());
        }
        return 0;
    }
    public boolean inscribirActividad(int idSocio, int idActividad) {
        try (Connection conn = new conexionSocios().conexionBBDD()) {

            // Verificar si ya está inscripto
            String checkSql = "SELECT COUNT(*) FROM reservas WHERE idSocio = ? AND idActividad = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setInt(1, idSocio);
            checkPs.setInt(2, idActividad);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.err.println("El socio ya está inscripto en esta actividad.");
                return false; // ya está inscripto
            }

            // Insertar si no está inscripto
            String insertSql = "INSERT INTO reservas (idSocio, idActividad) VALUES (?, ?)";
            PreparedStatement insertPs = conn.prepareStatement(insertSql);
            insertPs.setInt(1, idSocio);
            insertPs.setInt(2, idActividad);
            int filas = insertPs.executeUpdate();

            return filas > 0;

        } catch (Exception ex) {
            System.err.println("Error al inscribir socio en actividad: " + ex.getMessage());
            return false;
        }
    }

    public List<Actividad> listarActividadesReservadas(int idSocio) {
        List<Actividad> lista = new ArrayList<>();

        try (Connection conn = new conexionSocios().conexionBBDD()) {
            String sql = "SELECT a.idActividad, a.nombre, a.horario, s.idSector, s.nombre AS nombreSector " +
                    "FROM reservas r " +
                    "JOIN actividades a ON r.idActividad = a.idActividad " +
                    "JOIN sectores s ON a.idSector = s.idSector " +
                    "WHERE r.idSocio = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idSocio);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sector sector = new Sector(
                        rs.getInt("idSector"),
                        rs.getString("nombreSector"),
                        0 // capacidad no necesaria acá
                );

                Actividad actividad = new Actividad(
                        rs.getInt("idActividad"),
                        rs.getString("nombre"),
                        rs.getString("horario"),
                        sector,
                        0 // capacidad no necesaria acá
                );

                lista.add(actividad);
            }

        } catch (Exception ex) {
            System.err.println("Error al listar reservas: " + ex.getMessage());
        }

        return lista;
    }
    public boolean cancelarReserva(int idSocio, int idActividad) {
        try (Connection conn = new conexionSocios().conexionBBDD()) {
            String sql = "DELETE FROM reservas WHERE idSocio = ? AND idActividad = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idSocio);
            ps.setInt(2, idActividad);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (Exception ex) {
            System.err.println("Error al cancelar reserva: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean crear(Socio entidad) {
        return false;
    }
}




