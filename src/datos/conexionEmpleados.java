package datos;

import modelo.Empleado;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class conexionEmpleados {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String BBDD = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "gym";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    //Conexión a la base
    public Connection conexionBBDD() {
        Connection conexion = null;
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(
                    BBDD + DB_NAME + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                    USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error en DRIVER: " + e);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la BBDD: " + e);
        }
        return conexion;
    }


    //Chequear si el empleado existe por correo
    public boolean existeEmpleado(String correo) {
        boolean existe = false;
        String sql = "SELECT idEmpleado FROM empleados WHERE correo = ?";
        Connection conexion = conexionBBDD();
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                existe = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar empleado: " + e.getMessage());
        } finally {
            cerrarConexion(conexion);
        }
        return existe;
    }

    //Insertar nuevo empleado
    public void insertarEmpleado(Empleado emp) {
        String sql = "INSERT INTO empleados (nombre, correo, contrasena) VALUES (?, ?, ?)";
        try (Connection con = conexionBBDD();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getCorreo());
            ps.setString(3, emp.getContrasena());
            ps.executeUpdate();

            // Obtener id generado automáticamente
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    emp.setId(rs.getInt(1));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar empleado: " + ex.getMessage());
        }
    }



    //Modificar empleado
    public void modificarEmpleado(Empleado empleado) {
        String sql = "UPDATE empleados SET nombre = ?, correo = ?, contrasena = ? WHERE idEmpleado = ?";
        Connection conexion = conexionBBDD();
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getCorreo());
            ps.setString(3, empleado.getContrasena());
            ps.setInt(4, empleado.getIdEmpleado());
            ps.executeUpdate();
            System.out.println("Empleado modificado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al modificar empleado: " + e.getMessage());
        } finally {
            cerrarConexion(conexion);
        }
    }

    //Eliminar empleado por id
    public void eliminarEmpleado(int idEmpleado) {
        String sql = "DELETE FROM empleados WHERE idEmpleado = ?";
        Connection conexion = conexionBBDD();
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idEmpleado);
            ps.executeUpdate();
            System.out.println("Empleado eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
        } finally {
            cerrarConexion(conexion);
        }
    }

    //Mostrar todos los empleados
    public List<Empleado> mostrarEmpleados() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleados";
        Connection conexion = conexionBBDD();
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Empleado e = new Empleado(
                        rs.getInt("idEmpleado"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena")
                );
                lista.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error al mostrar empleados: " + e.getMessage());
        } finally {
            cerrarConexion(conexion);
        }
        return lista;
    }

    //Cerrar conexion
    public void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
