package modelo;
import datos.conexionEmpleados;
import datos.*;
import interfaces.IPersistible;
import interfaces.IVerificable;
import excepciones.UsuarioNoEncontradoException;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Empleado extends Usuario implements IPersistible, IVerificable {

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

    @Override
    public boolean eliminar() {
        return false;
    }

    @Override
    public boolean verificar() throws UsuarioNoEncontradoException {
        EmpleadoDAO dao = new EmpleadoDAO();
        Empleado empleadoCompleto = dao.obtenerPorCredenciales(this.correo, this.contrasena);

        this.id = empleadoCompleto.getId();
        this.nombre = empleadoCompleto.getNombre();

        return true;
    }


    // Método propio de empleado
    public String getTipo() {
        return "Empleado";
    }

    public int getIdEmpleado() {
        return this.id;
    }
}
