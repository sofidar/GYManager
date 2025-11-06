package modelo;

import datos.MembresiaDAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Membresia {
    private int idMembresia;
    private String tipo;
    private int duracionMeses;
    private double precio;
    private int maximoActividades;

    // Constructores
    public Membresia(int idMembresia, String tipo, int duracionMeses, double precio, int maximoActividades) {
        this.idMembresia = idMembresia;
        this.tipo = tipo;
        this.duracionMeses = duracionMeses;
        this.precio = precio;
        this.maximoActividades = maximoActividades;
    }

    public Membresia(int idMembresia, String tipo, int duracionMeses) {
        this(idMembresia, tipo, duracionMeses, 0.0, 1);
    }

    public Membresia(int idMembresia) {
        this(idMembresia, "", 0, 0.0, 1);
    }

    // Getters y setters
    public int getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(int idMembresia) {
        this.idMembresia = idMembresia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDuracionMeses() {
        return duracionMeses;
    }

    public void setDuracionMeses(int duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getMaximoActividades() {
        return maximoActividades;
    }

    public void setMaximoActividades(int maximoActividades) {
        this.maximoActividades = maximoActividades;
    }

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
            datos.put("actividades", "Máximo de actividades: " + maximoActividades);
        } else {
            datos.put("tipo", "Sin membresía");
            datos.put("inicio", "-");
            datos.put("fin", "-");
            datos.put("actividades", "-");
        }
        return datos;
    }

    // Aplica membresía a socio existente o crea uno nuevo
    public void aplicar(String nombre, String correo, String contrasena, Socio socioExistente, boolean esActualizacion) throws Exception {
        MembresiaDAO dao = new MembresiaDAO();
        dao.aplicarMembresia(this, nombre, correo, contrasena, socioExistente, esActualizacion);
    }
}




