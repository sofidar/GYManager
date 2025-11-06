package modelo;

import datos.conexionSocios;
import datos.conexionMembresias;
import excepciones.ActividadNoDisponibleException;
import interfaces.IPersistible;
import interfaces.IVerificable;
import excepciones.UsuarioNoEncontradoException;
import datos.SocioDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Socio extends Usuario implements IPersistible, IVerificable {
    private Membresia membresia;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Socio(int id, String nombre, String correo, String contrasena,
                 Membresia membresia, LocalDate fechaInicio, LocalDate fechaFin) {
        super(id, nombre, correo, contrasena);
        this.membresia = membresia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public boolean verificar() throws UsuarioNoEncontradoException {
        SocioDAO dao = new SocioDAO();
        Socio socioCompleto = dao.obtenerPorCredenciales(this.correo, this.contrasena);

        this.id = socioCompleto.id;
        this.nombre = socioCompleto.nombre;
        this.membresia = socioCompleto.membresia;
        this.fechaInicio = socioCompleto.fechaInicio;
        this.fechaFin = socioCompleto.fechaFin;

        return true;
    }


    public boolean crear() {
        return new SocioDAO().crear(this);
    }



    public boolean eliminar() {
        SocioDAO dao = new SocioDAO();
        return dao.eliminar(this.id);
    }
    public void darseDeBaja() {
        conexionSocios conexion = new conexionSocios();
        conexion.eliminarSocio(this.getId());
    }

    public static List<Socio> listar() {
        List<Socio> socios = new ArrayList<>();
        conexionSocios conexion = new conexionSocios();
        conexionMembresias conexionMembresias = new conexionMembresias();

        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "SELECT * FROM socios";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idSocio");
                String nombre = rs.getString("nombre");
                String correo = rs.getString("correo");
                String contrasena = rs.getString("contrasena");
                int idMembresia = rs.getInt("idMembresia");
                Date inicio = rs.getDate("fechaInicio");
                Date fin = rs.getDate("fechaFin");

                Membresia membresia = conexionMembresias.obtenerMembresia(idMembresia);

                Socio socio = new Socio(
                        id,
                        nombre,
                        correo,
                        contrasena,
                        membresia,
                        inicio != null ? inicio.toLocalDate() : null,
                        fin != null ? fin.toLocalDate() : null
                );

                socios.add(socio);
            }

        } catch (Exception ex) {
            System.err.println("Error al listar socios: " + ex.getMessage());
        }

        return socios;
    }
    public boolean inscribirseAActividad(int idActividad) throws ActividadNoDisponibleException {
        SocioDAO dao = new SocioDAO();
        int actividadesActuales = dao.contarActividadesInscriptas(this.id);
        int maxPermitidas = this.membresia.getMaximoActividades();

        if (actividadesActuales >= maxPermitidas) {
            throw new ActividadNoDisponibleException("Ya alcanzaste el máximo de actividades permitidas por tu membresía.");
        }

        return dao.inscribirActividad(this.id, idActividad);
    }


    // Getters y setters
    public Membresia getMembresia() {
        return membresia;
    }

    public void setMembresia(Membresia membresia) {
        this.membresia = membresia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getIdSocio() {
        return getId();
    }

    public void setIdSocio(int id) {
        setId(id);
    }
}


