package datos;

import interfaces.IDAO;
import modelo.Empleado;
import excepciones.UsuarioNoEncontradoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class EmpleadoDAO implements IDAO<Empleado> {

    public Empleado obtenerPorCredenciales(String correo, String contrasena) throws UsuarioNoEncontradoException {
        conexionEmpleados conexion = new conexionEmpleados();

        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "SELECT * FROM empleados WHERE correo = ? AND contrasena = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("idEmpleado");
                String nombre = rs.getString("nombre");

                return new Empleado(id, nombre, correo, contrasena);
            } else {
                throw new UsuarioNoEncontradoException("No se encontró un empleado con ese correo y contraseña.");
            }

        } catch (UsuarioNoEncontradoException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("Error técnico al verificar empleado: " + ex.getMessage());
            throw new UsuarioNoEncontradoException("Error técnico al verificar empleado.");
        }
    }


    @Override
    public List<Empleado> listar() {
        return null;
    }

    @Override
    public boolean crear(Empleado entidad) {
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        return false;
    }
}

