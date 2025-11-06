package datos;

import modelo.Membresia;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class conexionMembresias {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String BBDD = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "gym";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public Connection conexionBBDD() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(BBDD + DB_NAME, USUARIO, PASSWORD);
    }

    public List<Membresia> mostrarMembresias() {
        List<Membresia> lista = new ArrayList<>();
        String sql = "SELECT * FROM membresias";

        try (Connection con = conexionBBDD();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Membresia(
                        rs.getInt("idMembresia"),
                        rs.getString("tipo"),
                        rs.getInt("duracionMeses"),
                        rs.getDouble("precio")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Membresia obtenerMembresia(int id) {
        String sql = "SELECT * FROM membresias WHERE idMembresia=?";
        try (Connection con = conexionBBDD();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Membresia(
                        rs.getInt("idMembresia"),
                        rs.getString("tipo"),
                        rs.getInt("duracionMeses"),
                        rs.getDouble("precio")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

