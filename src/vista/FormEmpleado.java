package vista;

import datos.conexionSectores;
import datos.conexionSocios;
import datos.conexionActividades;
import modelo.Sector;

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

            // Tomar el sector seleccionado
            Sector seleccionado = (Sector) comboSector.getSelectedItem();
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(panelPrincipal, "Selecciona un sector.");
                return;
            }

            int idSector = seleccionado.getIdSector();

            // Conexión y creación de actividad en la base de datos
            conexionActividades conexion = new conexionActividades();
            Connection conn = conexion.conexionBBDD();

            String sql = "INSERT INTO actividades (nombre, horario, idSector, capacidad) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, horario);
            ps.setInt(3, idSector);
            ps.setInt(4, capacidad);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(panelPrincipal, "Actividad creada correctamente.");

            // Limpiar campos
            txtNombreActividad.setText("");
            txtHorarioActividad.setText("");
            txtCapacidad.setText("");

            conn.close();

            // Recargar tabla de actividades
            cargarActividades();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panelPrincipal, "La capacidad debe ser un número.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelPrincipal, "Error al crear actividad: " + ex.getMessage());
        }
    }



    private void cargarActividades() {
        modeloActividades.setRowCount(0);
        conexionActividades conexion = new conexionActividades();

        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "SELECT * FROM actividades";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            areaActividades.setText("");
            while (rs.next()) {
                Object[] fila = {
                        rs.getInt("idActividad"),
                        rs.getString("nombre"),
                        rs.getString("horario"),
                        rs.getInt("idSector"),
                        rs.getInt("capacidad")
                };
                modeloActividades.addRow(fila);
                areaActividades.append(rs.getString("nombre") + " - " + rs.getString("horario") + "\n");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelPrincipal, "Error al cargar actividades: " + ex.getMessage());
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

        conexionActividades conexion = new conexionActividades();
        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "DELETE FROM actividades WHERE idActividad = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idActividad);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(panelPrincipal, "Actividad eliminada correctamente.");
            cargarActividades();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelPrincipal, "Error al eliminar actividad: " + ex.getMessage());
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
        conexionSocios conexion = new conexionSocios();

        try (Connection conn = conexion.conexionBBDD()) {
            // Hacer JOIN para obtener el tipo de membresía
            String sql = "SELECT s.idSocio, s.nombre, s.correo, m.tipo AS membresia " +
                    "FROM socios s " +
                    "JOIN membresias m ON s.idMembresia = m.idMembresia";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Limpiar la tabla
            modeloSocios.setRowCount(0);

            while (rs.next()) {
                Object[] fila = {
                        rs.getInt("idSocio"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("membresia") // nombre de la membresía
                };
                modeloSocios.addRow(fila);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelPrincipal, "Error al cargar socios: " + ex.getMessage());
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

        conexionSocios conexion = new conexionSocios();
        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "DELETE FROM socios WHERE idSocio = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idSocio);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(panelPrincipal, "Socio eliminado correctamente.");
            cargarSocios();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelPrincipal, "Error al eliminar socio: " + ex.getMessage());
        }
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}


