package datos;

import modelo.Membresia;
import modelo.Socio;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class conexionSocios {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String BBDD = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "gym";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public Connection conexionBBDD() {
        Connection conexion = null;
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(BBDD + DB_NAME, USUARIO, PASSWORD);
            System.out.println("Conexión OK a " + DB_NAME);
        } catch (ClassNotFoundException e) {
            System.err.println("Error en DRIVER\n" + e);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la BBDD\n" + e);
        }
        return conexion;
    }

    public void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión con la BBDD\n" + e);
        }
    }

    // ------------------------------------------
    // Comprobar si existe un socio por correo
    public boolean existeSocio(String correo) {
        String sql = "SELECT * FROM socios WHERE correo = ?";
        try (Connection con = conexionBBDD();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error en existeSocio: " + e);
            return false;
        }
    }

    // ------------------------------------------
    // Insertar socio
    public void insertarSocio(Socio socio) {
        String sql = "INSERT INTO socios (nombre, correo, contrasena, idMembresia, fechaInicio, fechaFin) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = conexionBBDD();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, socio.getNombre());
            ps.setString(2, socio.getCorreo());
            ps.setString(3, socio.getContrasena());
            ps.setInt(4, socio.getMembresia().getIdMembresia());
            ps.setDate(5, Date.valueOf(socio.getFechaInicio()));
            ps.setDate(6, Date.valueOf(socio.getFechaFin()));

            ps.executeUpdate();

            // Obtener id generado
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                socio.setIdSocio(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Error al insertarSocio: " + e);
        }
    }

    // ------------------------------------------
    // Modificar socio
    public void modificarSocio(Socio socio) {
        String sql = "UPDATE socios SET nombre = ?, correo = ?, contrasena = ?, idMembresia = ?, fechaInicio = ?, fechaFin = ? WHERE idSocios = ?";
        try (Connection con = conexionBBDD();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, socio.getNombre());
            ps.setString(2, socio.getCorreo());
            ps.setString(3, socio.getContrasena());
            ps.setInt(4, socio.getMembresia().getIdMembresia());
            ps.setDate(5, Date.valueOf(socio.getFechaInicio()));
            ps.setDate(6, Date.valueOf(socio.getFechaFin()));
            ps.setInt(7, socio.getIdSocio());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al modificarSocio: " + e);
        }
    }

    // ------------------------------------------
    // Eliminar socio
    public void eliminarSocio(int idSocio) {
        String sql = "DELETE FROM socios WHERE idSocios = ?";
        try (Connection con = conexionBBDD();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSocio);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminarSocio: " + e);
        }
    }

    // ------------------------------------------
    // Mostrar todos los socios
    public List<Socio> mostrarSocios() {
        List<Socio> lista = new ArrayList<>();
        String sql = "SELECT s.idSocios, s.nombre, s.correo, s.contrasena, s.idMembresia, s.fechaInicio, s.fechaFin, m.tipo, m.duracionMeses " +
                "FROM socios s JOIN membresias m ON s.idMembresia = m.idMembresia";
        try (Connection con = conexionBBDD();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Membresia m = new Membresia(rs.getInt("idMembresia"), rs.getString("tipo"), rs.getInt("duracionMeses"));
                Socio s = new Socio(
                        rs.getInt("idSocios"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        m,
                        rs.getDate("fechaInicio").toLocalDate(),
                        rs.getDate("fechaFin").toLocalDate()
                );
                lista.add(s);
            }

        } catch (SQLException e) {
            System.err.println("Error al mostrarSocios: " + e);
        }

        return lista;
    }
}



