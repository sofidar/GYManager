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
    public JButton btnCambiarMembresia;
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
        btnCambiarMembresia.addActionListener(e -> abrirFormElegirMembresia());

        // Evento para darse de baja
        btnBajaMembresia.addActionListener(e -> bajaMembresia());
    }

    public FormSocio() {

    }


    private void actualizarMembresia() {
        if (membresia != null) {
            lblTipoMembresia.setText("Tipo de Membresía: " + membresia.getTipo());
            lblFechaInicio.setText("Inicio: " + socio.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            lblFechaFin.setText("Fin: " + socio.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            lblTipoMembresia.setText("Sin membresía");
            lblFechaInicio.setText("-");
            lblFechaFin.setText("-");
        }
    }


    private void abrirFormElegirMembresia() {
        JFrame elegirFrame = new JFrame("Elegir nueva membresía");
        elegirFrame.setContentPane(new FormElegirMembresia(socio).getPanelPrincipal());
        elegirFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        elegirFrame.pack();
        elegirFrame.setLocationRelativeTo(null);
        elegirFrame.setVisible(true);
    }


    private void bajaMembresia() {
        int confirm = JOptionPane.showConfirmDialog(panelPrincipal,
                "Estás seguro de que querés darte de baja? Esta acción eliminará tu cuenta.",
                "Confirmar baja",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            conexionSocios conexion = new conexionSocios();
            conexion.eliminarSocio(socio.getId());

            JOptionPane.showMessageDialog(panelPrincipal, "Tu cuenta fue eliminada correctamente.");
            JFrame ventana = (JFrame) SwingUtilities.getWindowAncestor(panelPrincipal);
            ventana.dispose(); // cerrar la ventana actual
        }
    }


    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}


