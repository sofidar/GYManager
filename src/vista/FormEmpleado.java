package vista;

import datos.*;
import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class FormEmpleado {
    public JPanel panelPrincipal;
    public JTabbedPane tabbedPane;

    // Pestaña ACTIVIDADES
    public JPanel panelActividad;
    public JTextField txtNombreActividad;
    public JTextField txtHorarioActividad;
    public JComboBox comboSector;
    public JTextField txtCapacidad;
    public JButton btnCrearActividad;
    public JTextArea areaActividades;
    private JTable tablaActividades;
    private JButton btnActualizarAct;
    private JButton btnEliminarAct;

    // Pestaña SOCIOS
    public JPanel panelSocios;
    private JButton btnActualizar;
    private JTable table1;
    private JButton btnEliminarU;

    // Modelos de tablas
    private DefaultTableModel modeloSocios;
    private DefaultTableModel modeloActividades;

    public FormEmpleado() {
        configurarComboSector();
        configurarTablaSocios();
        configurarTablaActividades();
        cargarSocios();
        cargarActividades();

        // Eventos
        btnCrearActividad.addActionListener(this::crearActividad);
        btnActualizar.addActionListener(this::actualizarSocios);
        btnEliminarU.addActionListener(this::eliminarSocio);

        btnActualizarAct.addActionListener(this::actualizarActividades);
        btnEliminarAct.addActionListener(this::eliminarActividad);
    }

    // ==============================
    // PESTAÑA ACTIVIDADES
    // ==============================
    private void configurarComboSector() {
        comboSector.removeAllItems();
        conexionSectores cs = new conexionSectores();

        try {
            List<Sector> listaSectores = cs.mostrarSectores(); // Devuelve List<Sector>
            for (Sector s : listaSectores) {
                comboSector.addItem(s); // toString() de Sector mostrará el nombre
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal, "Error al cargar sectores: " + e.getMessage());
        }
    }


    private void configurarTablaActividades() {
        modeloActividades = new DefaultTableModel(new Object[]{"ID", "Nombre", "Horario", "Sector", "Capacidad"}, 0);
        tablaActividades.setModel(modeloActividades);
    }

    private void crearActividad(ActionEvent e) {
        String nombre = txtNombreActividad.getText().trim();
        String horario = txtHorarioActividad.getText().trim();
        String capacidadTexto = txtCapacidad.getText().trim();

        if (nombre.isEmpty() || horario.isEmpty() || capacidadTexto.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal, "Completa todos los campos.");
            return;
        }

        try {
            int capacidad = Integer.parseInt(capacidadTexto);
            Sector seleccionado = (Sector) comboSector.getSelectedItem();

            if (seleccionado == null) {
                JOptionPane.showMessageDialog(panelPrincipal, "Selecciona un sector.");
                return;
            }

            Actividad nueva = new Actividad(nombre, horario, seleccionado, capacidad);
            if (nueva.crear()) {
                JOptionPane.showMessageDialog(panelPrincipal, "Actividad creada correctamente.");
                txtNombreActividad.setText("");
                txtHorarioActividad.setText("");
                txtCapacidad.setText("");
                cargarActividades();
            } else {
                JOptionPane.showMessageDialog(panelPrincipal, "Error al crear actividad.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panelPrincipal, "La capacidad debe ser un número.");
        }
    }

    private void cargarActividades() {
        modeloActividades.setRowCount(0);
        areaActividades.setText("");

        List<Actividad> actividades = Actividad.listar();

        for (Actividad act : actividades) {
            Object[] fila = {
                    act.getIdActividad(),
                    act.getNombre(),
                    act.getHorario(),
                    act.getSector().getNombre(),
                    act.getCapacidad()
            };
            modeloActividades.addRow(fila);
            areaActividades.append(act.getNombre() + " - " + act.getHorario() + "\n");
        }
    }


    private void actualizarActividades(ActionEvent e) {
        cargarActividades();
    }

    private void eliminarActividad(ActionEvent e) {

        int fila = tablaActividades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(panelPrincipal, "Selecciona una actividad para eliminar.");
            return;
        }

        int idActividad = (int) modeloActividades.getValueAt(fila, 0);
        String nombre = (String) modeloActividades.getValueAt(fila, 1);
        String horario = (String) modeloActividades.getValueAt(fila, 2);
        String sectorNombre = (String) modeloActividades.getValueAt(fila, 3);
        int capacidad = (int) modeloActividades.getValueAt(fila, 4);

        // Crear sector ficticio (solo para cumplir con constructor)
        Sector sector = new Sector(0, sectorNombre, 0); // id y capacidad no usados

        Actividad act = new Actividad(idActividad, nombre, horario, sector, capacidad);

        System.out.println("ID desde tabla: " + idActividad);

        if (act.eliminar()) {
            JOptionPane.showMessageDialog(panelPrincipal, "Actividad eliminada correctamente.");
            cargarActividades();
        } else {
            JOptionPane.showMessageDialog(panelPrincipal, "Error al eliminar actividad.");
        }
    }


    // ==============================
// PESTAÑA SOCIOS
// ==============================
    private void configurarTablaSocios() {
        modeloSocios = new DefaultTableModel(new Object[]{"ID", "Nombre", "Correo", "Membresía"}, 0) {
            // Evitar edición directa en la tabla
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1.setModel(modeloSocios);
    }

    private void cargarSocios() {
        modeloSocios.setRowCount(0);

        List<Socio> socios = Socio.listar();

        for (Socio s : socios) {
            Object[] fila = {
                    s.getId(),
                    s.getNombre(),
                    s.getCorreo(),
                    s.getMembresia().getTipo()
            };
            modeloSocios.addRow(fila);
        }
    }

    private void actualizarSocios(ActionEvent e) {
        cargarSocios();
    }

    private void eliminarSocio(ActionEvent e) {
        int fila = table1.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(panelPrincipal, "Selecciona un socio para eliminar.");
            return;
        }

        int idSocio = (int) modeloSocios.getValueAt(fila, 0);
        String nombre = (String) modeloSocios.getValueAt(fila, 1);
        String correo = (String) modeloSocios.getValueAt(fila, 2);
        String tipoMembresia = (String) modeloSocios.getValueAt(fila, 3);

        // Crear socio mínimo para eliminar
        Membresia membresia = new Membresia(0, tipoMembresia, 0); // id y duración ficticios
        Socio socio = new Socio(idSocio, nombre, correo, "", membresia, null, null);

        if (socio.eliminar()) {
            JOptionPane.showMessageDialog(panelPrincipal, "Socio eliminado correctamente.");
            cargarSocios();
        } else {
            JOptionPane.showMessageDialog(panelPrincipal, "Error al eliminar socio.");
        }
    }


    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}


