package datos;

import java.sql.*;

public class conexionActividades {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String BBDD = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "gym";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; // Cambiar si tu MySQL tiene contraseña

    // Conexión a la base de datos
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

    // Cerrar conexión
    public void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión con la base de datos: " + e);
        }
    }

    // Crear tabla si no existe
    public void crearTablaActividades() {
        Connection conexion = null;
        Statement declaracion = null;
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(BBDD + DB_NAME, USUARIO, PASSWORD);
            declaracion = conexion.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS actividades (" +
                    "idActividad INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(50) NOT NULL, " +
                    "horario VARCHAR(50) NOT NULL, " +
                    "sector VARCHAR(50) NOT NULL, " +
                    "capacidad INT NOT NULL" +
                    ")";
            declaracion.executeUpdate(sql);

            System.out.println("Tabla actividades creada correctamente.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error en DRIVER\n" + e);
        } catch (SQLException e) {
            System.err.println("Error en la conexión o creación de tabla\n" + e);
        } finally {
            cerrarConexion(conexion);
        }
    }

    // ==============================
    // OPERACIONES
    // ==============================

    // Insertar actividad
    public void insertarActividad(String nombre, String horario, String sector, int capacidad) {
        Connection conexion = null;
        try {
            conexion = conexionBBDD();
            String sql = "INSERT INTO actividades (nombre, horario, sector, capacidad) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, horario);
            ps.setString(3, sector);
            ps.setInt(4, capacidad);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar actividad: " + e);
        } finally {
            cerrarConexion(conexion);
        }
    }

    // Eliminar actividad por id
    public void eliminarActividad(int idActividad) {
        Connection conexion = null;
        try {
            conexion = conexionBBDD();
            String sql = "DELETE FROM actividades WHERE idActividad = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idActividad);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar actividad: " + e);
        } finally {
            cerrarConexion(conexion);
        }
    }

    // Modificar actividad
    public void modificarActividad(int idActividad, String nombre, String horario, String sector, int capacidad) {
        Connection conexion = null;
        try {
            conexion = conexionBBDD();
            String sql = "UPDATE actividades SET nombre=?, horario=?, sector=?, capacidad=? WHERE idActividad=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, horario);
            ps.setString(3, sector);
            ps.setInt(4, capacidad);
            ps.setInt(5, idActividad);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al modificar actividad: " + e);
        } finally {
            cerrarConexion(conexion);
        }
    }

    // Mostrar todas las actividades
    public ResultSet mostrarActividades() {
        ResultSet rs = null;
        try {
            Connection conexion = conexionBBDD();
            String sql = "SELECT * FROM actividades";
            PreparedStatement ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al mostrar actividades: " + e);
        }
        return rs;
    }
}

