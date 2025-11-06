package datos;

import interfaces.IDAO;
import modelo.Membresia;
import modelo.Socio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class MembresiaDAO implements IDAO<Membresia> {

    public void aplicarMembresia(Membresia membresia, String nombre, String correo, String contrasena, Socio socioExistente, boolean esActualizacion) throws Exception {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = inicio.plusMonths(membresia.getDuracionMeses());

        conexionSocios conexion = new conexionSocios();

        if (esActualizacion && socioExistente != null) {
            String sql = "UPDATE socios SET idMembresia = ?, fechaInicio = ?, fechaFin = ? WHERE idSocio = ?";
            try (Connection con = conexion.conexionBBDD();
                 PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, membresia.getIdMembresia());
                ps.setDate(2, java.sql.Date.valueOf(inicio));
                ps.setDate(3, java.sql.Date.valueOf(fin));
                ps.setInt(4, socioExistente.getId());
                ps.executeUpdate();
            }

            socioExistente.setMembresia(membresia);
            socioExistente.setFechaInicio(inicio);
            socioExistente.setFechaFin(fin);

        } else {
            Socio nuevoSocio = new Socio(0, nombre, correo, contrasena, membresia, inicio, fin);
            nuevoSocio.crear(); // Este método debería estar delegado en SocioDAO también
        }
    }


    @Override
    public List<Membresia> listar() {
        return null;
    }

    @Override
    public boolean crear(Membresia entidad) {
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        return false;
    }
}
