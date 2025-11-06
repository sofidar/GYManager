package vista;

import datos.conexionSocios;
import datos.*;
import modelo.Membresia;
import modelo.Socio;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class FormElegirMembresia {
    public JPanel panelPrincipal;
    public JButton btnBasico;
    public JButton btnEspecial;
    public JButton btnCompleto;

    private String nombre, correo, contrasena;
    private List<Membresia> membresias;

    private boolean esActualizacion = false;
    private Socio socioExistente;


    public FormElegirMembresia(Socio socioExistente) {

        this.socioExistente = socioExistente;
        this.nombre = socioExistente.getNombre();
        this.correo = socioExistente.getCorreo();
        this.contrasena = socioExistente.getContrasena();
        this.esActualizacion = true;

        conexionMembresias conexion = new conexionMembresias();
        membresias = conexion.mostrarMembresias();

        btnBasico.addActionListener(e -> procesarMembresia("Basico"));
        btnEspecial.addActionListener(e -> procesarMembresia("Especial"));
        btnCompleto.addActionListener(e -> procesarMembresia("Completo"));
    }
    public FormElegirMembresia(String nombre, String correo, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.esActualizacion = false;

        conexionMembresias conexion = new conexionMembresias();
        membresias = conexion.mostrarMembresias();

        btnBasico.addActionListener(e -> procesarMembresia("Basico"));
        btnEspecial.addActionListener(e -> procesarMembresia("Especial"));
        btnCompleto.addActionListener(e -> procesarMembresia("Completo"));
    }


    private void procesarMembresia(String tipoMembresia) {
        Membresia m = membresias.stream()
                .filter(mem -> mem.getTipo().equals(tipoMembresia))
                .findFirst()
                .orElse(null);

        if (m == null) {
            JOptionPane.showMessageDialog(panelPrincipal, "Membresía no encontrada");
            return;
        }

        LocalDate inicio = LocalDate.now();
        LocalDate fin = inicio.plusMonths(m.getDuracionMeses());

        try {
            conexionSocios conexion = new conexionSocios();

            if (esActualizacion) {
                // 🔄 Actualizar socio existente
                String sql = "UPDATE socios SET idMembresia = ?, fechaInicio = ?, fechaFin = ? WHERE idSocio = ?";
                try (Connection con = conexion.conexionBBDD();
                     PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, m.getIdMembresia());
                    ps.setDate(2, java.sql.Date.valueOf(inicio));
                    ps.setDate(3, java.sql.Date.valueOf(fin));
                    ps.setInt(4, socioExistente.getId());
                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(panelPrincipal, "Membresía actualizada a " + m.getTipo());

            } else {
                // ✅ Crear nuevo socio
                Socio nuevoSocio = new Socio(0, nombre, correo, contrasena, m, inicio, fin);
                conexion.insertarSocio(nuevoSocio);
                JOptionPane.showMessageDialog(panelPrincipal, "Socio creado con membresía " + m.getTipo());
            }

            SwingUtilities.getWindowAncestor(panelPrincipal).dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelPrincipal, "Error al procesar membresía: " + ex.getMessage());
        }
    }


    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}


