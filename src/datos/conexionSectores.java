package datos;

import modelo.Sector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class conexionSectores {
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

    public List<Sector> mostrarSectores() {
        List<Sector> lista = new ArrayList<>();
        try (Connection conexion = conexionBBDD()) {
            String sql = "SELECT * FROM sectores";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sector s = new Sector(
                        rs.getInt("idSector"),
                        rs.getString("nombre"),
                        rs.getInt("capacidadMax")
                );
                lista.add(s);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar sectores: " + e.getMessage());
        }
        return lista;
    }
}

