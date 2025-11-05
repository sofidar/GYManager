package modelo;

public class Empleado extends Usuario {

    // Constructor con id
    public Empleado(int id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena);
    }

    // Constructor sin id (para insertar)
    public Empleado(String nombre, String correo, String contrasena) {
        super(nombre, correo, contrasena);
    }

    // Método propio de empleado
    public String getTipo() {
        return "Empleado";
    }

    public int getIdEmpleado() {
        return this.id;
    }
}
