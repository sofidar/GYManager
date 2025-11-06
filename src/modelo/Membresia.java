package modelo;

import datos.conexionSocios;
import modelo.Socio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Membresia {
    private int idMembresia;
    private String tipo;
    private int duracionMeses;
    private double precio;

    // Constructores
    public Membresia(int idMembresia, String tipo, int duracionMeses, double precio) {
        this.idMembresia = idMembresia;
        this.tipo = tipo;
        this.duracionMeses = duracionMeses;
        this.precio = precio;
    }

    public Membresia(int idMembresia, String tipo, int duracionMeses) {
        this(idMembresia, tipo, duracionMeses, 0.0);
    }

    public Membresia(int idMembresia) {
        this.idMembresia = idMembresia;
    }

    // Getters y setters
    public int getIdMembresia() { return idMembresia; }
    public String getTipo() { return tipo; }
    public int getDuracionMeses() { return duracionMeses; }
    public double getPrecio() { return precio; }

    public void setIdMembresia(int idMembresia) { this.idMembresia = idMembresia; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setDuracionMeses(int duracionMeses) { this.duracionMeses = duracionMeses; }
    public void setPrecio(double precio) { this.precio = precio; }

    @Override
    public String toString() {
        return tipo;
    }

    // Devuelve resumen textual para mostrar en UI
    public Map<String, String> actualizarMembresia(Socio socio) {
        Map<String, String> datos = new HashMap<>();
        if (socio != null && socio.getMembresia() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            datos.put("tipo", "Tipo de Membresía: " + tipo);
            datos.put("inicio", "Inicio: " + socio.getFechaInicio().format(formatter));
            datos.put("fin", "Fin: " + socio.getFechaFin().format(formatter));
        } else {
            datos.put("tipo", "Sin membresía");
            datos.put("inicio", "-");
            datos.put("fin", "-");
        }
        return datos;
    }

    // Aplica membresía a socio existente o crea uno nuevo
    public void aplicar(String nombre, String correo, String contrasena, Socio socioExistente, boolean esActualizacion) throws Exception {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = inicio.plusMonths(this.duracionMeses);

        conexionSocios conexion = new conexionSocios();

        if (esActualizacion && socioExistente != null) {
            String sql = "UPDATE socios SET idMembresia = ?, fechaInicio = ?, fechaFin = ? WHERE idSocio = ?";
            try (Connection con = conexion.conexionBBDD();
                 PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, this.idMembresia);
                ps.setDate(2, java.sql.Date.valueOf(inicio));
                ps.setDate(3, java.sql.Date.valueOf(fin));
                ps.setInt(4, socioExistente.getId());
                ps.executeUpdate();
            }
            socioExistente.setMembresia(this);
            socioExistente.setFechaInicio(inicio);
            socioExistente.setFechaFin(fin);
        } else {
            Socio nuevoSocio = new Socio(0, nombre, correo, contrasena, this, inicio, fin);
            nuevoSocio.crear();
        }
    }
}

