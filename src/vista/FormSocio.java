package vista;

import datos.conexionSocios;
import modelo.Socio;
import modelo.Membresia;

import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

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
        Map<String, String> datos = membresia.actualizarMembresia(socio);
        actualizarMembresia();

        // Evento para renovar membresía
        btnCambiarMembresia.addActionListener(e -> abrirFormElegirMembresia());

        // Evento para darse de baja
        btnBajaMembresia.addActionListener(e -> bajaMembresia());
    }


    private void actualizarMembresia() {
        if (membresia != null) {
            Map<String, String> datos = membresia.actualizarMembresia(socio);
            lblTipoMembresia.setText(datos.get("tipo"));
            lblFechaInicio.setText(datos.get("inicio"));
            lblFechaFin.setText(datos.get("fin"));
        }
    }

    private void abrirFormElegirMembresia() {
        JFrame elegirFrame = new JFrame("Elegir nueva membresía");
        elegirFrame.setContentPane(new FormElegirMembresia(socio).getPanelPrincipal());
        elegirFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        elegirFrame.pack();
        elegirFrame.setLocationRelativeTo(null);

        elegirFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Refrescar datos del socio desde la base de datos cuando cierra la ventana
                membresia = socio.getMembresia();
                actualizarMembresia();
            }
        });

        elegirFrame.setVisible(true);
    }

    private void bajaMembresia() {
        int confirm = JOptionPane.showConfirmDialog(panelPrincipal,
                "¿Estás seguro de que querés darte de baja? Esta acción eliminará tu cuenta.",
                "Confirmar baja",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            socio.darseDeBaja(); // delega al modelo
            JOptionPane.showMessageDialog(panelPrincipal, "Tu cuenta fue eliminada correctamente.");
            JFrame ventana = (JFrame) SwingUtilities.getWindowAncestor(panelPrincipal);
            ventana.dispose();
        }
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}


