package vista;

import datos.conexionSocios;
import datos.conexionMembresias;
import modelo.Membresia;
import modelo.Socio;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class FormElegirMembresia {
    public JPanel panelPrincipal;
    public JButton btnBasico;
    public JButton btnEspecial;
    public JButton btnCompleto;

    private String nombre, correo, contrasena;
    private List<Membresia> membresias;

    public FormElegirMembresia(String nombre, String correo, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;

        conexionMembresias conexion = new conexionMembresias();
        membresias = conexion.mostrarMembresias();

        // Botones de membresía
        btnBasico.addActionListener(e -> crearSocio("Basico"));
        btnEspecial.addActionListener(e -> crearSocio("Especial"));
        btnCompleto.addActionListener(e -> crearSocio("Completo"));
    }

    private void crearSocio(String tipoMembresia) {
        Membresia m = membresias.stream()
                .filter(mem -> mem.getTipo().equals(tipoMembresia))
                .findFirst()
                .orElse(null);

        if (m != null) {
            LocalDate inicio = LocalDate.now();
            LocalDate fin = inicio.plusMonths(m.getDuracionMeses());
            Socio socio = new Socio(0, nombre, correo, contrasena, m, inicio, fin);

            try {
                conexionSocios conexionSocio = new conexionSocios();
                conexionSocio.insertarSocio(socio);

                JOptionPane.showMessageDialog(panelPrincipal, "Socio creado con membresía " + m.getTipo());
                // Cerrar ventana actual y volver al FormBienvenida
                SwingUtilities.getWindowAncestor(panelPrincipal).dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panelPrincipal, "Error al crear socio: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(panelPrincipal, "Membresía no encontrada");
        }
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}


