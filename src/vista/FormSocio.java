package vista;

import datos.conexionSocios;
import modelo.Socio;
import modelo.Membresia;

import javax.swing.*;
import java.time.format.DateTimeFormatter;

public class FormSocio {

    public JPanel panelPrincipal;
    public JTabbedPane tabbedPane;

    // PESTAÑA MEMBRESIA
    public JPanel panelMembresia;
    public JLabel lblTipoMembresia;
    public JLabel lblFechaInicio;
    public JLabel lblFechaFin;
    public JButton btnRenovarMembresia;
    public JButton btnBajaMembresia;

    // PESTAÑA ACTIVIDADES
    public JPanel panelActividades;
    public JTextArea areaActividades;

    // PESTAÑA RESERVAS
    public JPanel panelReservas;
    public JTextArea areaReservas;

    private Socio socio;
    private Membresia membresia;

    public FormSocio(Socio socio) {
        this.socio = socio;
        this.membresia = socio.getMembresia();

        // Inicializar etiquetas de membresía
        actualizarMembresia();

        // Evento para renovar membresía
        btnRenovarMembresia.addActionListener(e -> renovarMembresia());

        // Evento para darse de baja
        btnBajaMembresia.addActionListener(e -> bajaMembresia());
    }

    public FormSocio() {

    }

    // Mostrar la membresía actual
    private void actualizarMembresia() {
        if (membresia != null) {
            lblTipoMembresia.setText("Tipo: " + membresia.getTipo());
            lblFechaInicio.setText("Inicio: " + membresia.getFechaInicio().format(DateTimeFormatter.ISO_DATE));
            lblFechaFin.setText("Fin: " + membresia.getFechaFin().format(DateTimeFormatter.ISO_DATE));
        } else {
            lblTipoMembresia.setText("Tipo: -");
            lblFechaInicio.setText("Inicio: -");
            lblFechaFin.setText("Fin: -");
        }
    }

    // Renovar membresía usando la clase Membresia
    private void renovarMembresia() {
        if (membresia != null) {
            membresia.renovar();
            actualizarMembresia();
            JOptionPane.showMessageDialog(panelPrincipal, "Membresía renovada!");
        }
    }

    // Darse de baja de la membresía (elimina el usuario)
    private void bajaMembresia() {
        int confirm = JOptionPane.showConfirmDialog(panelPrincipal,
                "¿Estás seguro de darte de baja?",
                "Confirmar baja",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                conexionSocios conexion = new conexionSocios();
                conexion.eliminarSocio(socio.getIdSocio());

                JOptionPane.showMessageDialog(panelPrincipal, "Te has dado de baja correctamente.");
                // Cierra la ventana del socio
                SwingUtilities.getWindowAncestor(panelPrincipal).dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(panelPrincipal, "Error al eliminar usuario: " + e.getMessage());
            }
        }
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}


