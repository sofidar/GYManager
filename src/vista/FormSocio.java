package vista;

import datos.SocioDAO;
import excepciones.ActividadNoDisponibleException;
import modelo.Actividad;
import modelo.Socio;
import modelo.Membresia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
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

    private JButton btnUnirse;

    // PESTAÑA ACTIVIDADES
    public JPanel panelActividades;
    private JTable tablaActividades;



    // PESTAÑA RESERVAS
    public JPanel panelReservas;
    private JTable tablaReservas;
    private JButton btnCancelarReserva;

    private Socio socio;
    private Membresia membresia;

    private DefaultTableModel modeloActividades;
    private DefaultTableModel modeloReservas;


    public FormSocio(Socio socio) {
        this.socio = socio;
        this.membresia = socio.getMembresia();

        // Inicializar modelo de tabla
        modeloActividades = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Horario", "Sector", "Capacidad"}, 0
        );
        tablaActividades.setModel(modeloActividades);

        modeloReservas = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Horario", "Sector"}, 0
        );
        tablaReservas.setModel(modeloReservas);
        cargarReservas();

        // Cargar actividades
        cargarActividades();

        // Inicializar etiquetas de membresía
        actualizarMembresia();

        // Eventos
        btnCambiarMembresia.addActionListener(e -> abrirFormElegirMembresia());
        btnBajaMembresia.addActionListener(e -> bajaMembresia());
        btnUnirse.addActionListener(e -> unirseActividad());
        btnCancelarReserva.addActionListener(e -> cancelarReserva());
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


    //Actividades '

    private void cargarActividades() {
        modeloActividades.setRowCount(0); // limpiar
        List<Actividad> actividades = Actividad.listar(); // usa DAO internamente

        for (Actividad a : actividades) {
            modeloActividades.addRow(new Object[]{
                    a.getIdActividad(),
                    a.getNombre(),
                    a.getHorario(),
                    a.getSector().getNombre(),
                    a.getCapacidad()
            });
        }
    }

    public void unirseActividad(){
        int fila = tablaActividades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(panelPrincipal, "Seleccioná una actividad.");
            return;
        }

        int idActividad = (int) modeloActividades.getValueAt(fila, 0);
        int maxPermitidas = socio.getMembresia().getMaximoActividades();
        int actuales = new SocioDAO().contarActividadesInscriptas(socio.getId());

        if (actuales >= maxPermitidas) {
            JOptionPane.showMessageDialog(panelPrincipal, "Ya alcanzaste el límite de actividades permitidas por tu membresía.");
            return;
        }

        try {
            socio.inscribirseAActividad(idActividad);
            JOptionPane.showMessageDialog(panelPrincipal, "Inscripción exitosa.");
        } catch (ActividadNoDisponibleException ex) {
            JOptionPane.showMessageDialog(panelPrincipal, ex.getMessage());
        }
        cargarReservas();
    }

    private void cargarReservas() {
        modeloReservas.setRowCount(0); // limpiar

        List<Actividad> actividades = new SocioDAO().listarActividadesReservadas(socio.getId());

        for (Actividad a : actividades) {
            modeloReservas.addRow(new Object[]{
                    a.getIdActividad(),
                    a.getNombre(),
                    a.getHorario(),
                    a.getSector().getNombre()
            });
        }
    }
    private void cancelarReserva() {
        int fila = tablaReservas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(panelPrincipal, "Seleccioná una actividad para cancelar.");
            return;
        }

        int idActividad = (int) modeloReservas.getValueAt(fila, 0);

        boolean exito = new SocioDAO().cancelarReserva(socio.getId(), idActividad);
        if (exito) {
            JOptionPane.showMessageDialog(panelPrincipal, "Reserva cancelada.");
            cargarReservas(); // refrescar tabla
        } else {
            JOptionPane.showMessageDialog(panelPrincipal, "No se pudo cancelar la reserva.");
        }
    }


    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}


