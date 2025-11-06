package modelo;
import datos.conexionEmpleados;
import datos.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Empleado extends Usuario {

    // Constructor con id
    public Empleado(int id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena);
    }

    // Constructor sin id (para insertar)
    public Empleado(String nombre, String correo, String contrasena) {
        super(nombre, correo, contrasena);
    }

    public boolean crear() {
        conexionEmpleados ce = new conexionEmpleados();
        if (ce.existeEmpleado(correo)) {
            return false;
        }
        ce.insertarEmpleado(this);
        return true;
    }

    public static boolean verificar(String correo, String contrasena) {
        conexionEmpleados conexion = new conexionEmpleados();
        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "SELECT * FROM empleados WHERE correo = ? AND contrasena = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception ex) {
            System.err.println("Error al verificar empleado: " + ex.getMessage());
            return false;
        }
    }

    // Método propio de empleado
    public String getTipo() {
        return "Empleado";
    }

    public int getIdEmpleado() {
        return this.id;
    }
}
