package vista;

import datos.conexionMembresias;
import datos.conexionSocios;
import datos.conexionEmpleados;
import modelo.Membresia;
import modelo.Socio;
import modelo.Empleado;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FormLogin {
    private JPanel panelPrincipal;
    private JTextField txtCorreo;
    private JPasswordField pwdContrasena;
    private JButton btnLogin;

    public FormLogin() {
        btnLogin.addActionListener(this::iniciarSesion);
    }

    private void iniciarSesion(ActionEvent e) {
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(pwdContrasena.getPassword()).trim();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal, "Completa todos los campos.");
            return;
        }

        // Intentar login como empleado primero
        if (verificarEmpleado(correo, contrasena)) {
            JOptionPane.showMessageDialog(panelPrincipal, "Inicio de sesion exitoso como EMPLEADO.");
            JFrame empleadoFrame = new JFrame("Panel Empleado");
            empleadoFrame.setContentPane(new FormEmpleado().getPanelPrincipal());
            empleadoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            empleadoFrame.pack();
            empleadoFrame.setLocationRelativeTo(null);
            empleadoFrame.setVisible(true);
            ((JFrame) SwingUtilities.getWindowAncestor(panelPrincipal)).dispose();
            return;
        }

        // Si no es empleado, intentar como socio
        Socio socioLogueado = verificarSocio(correo, contrasena);
        if (socioLogueado != null) {
            JOptionPane.showMessageDialog(panelPrincipal, "Inicio de sesión exitoso como SOCIO.");
            JFrame socioFrame = new JFrame("Panel Socio");
            socioFrame.setContentPane(new FormSocio(socioLogueado).getPanelPrincipal());
            socioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            socioFrame.pack();
            socioFrame.setLocationRelativeTo(null);
            socioFrame.setVisible(true);
            ((JFrame) SwingUtilities.getWindowAncestor(panelPrincipal)).dispose();
            return;
        }


        JOptionPane.showMessageDialog(panelPrincipal, "Correo o contraseña incorrectos.");
    }

    private boolean verificarEmpleado(String correo, String contrasena) {
        conexionEmpleados conexion = new conexionEmpleados();
        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "SELECT * FROM empleados WHERE correo = ? AND contrasena = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // existe el empleado
        } catch (Exception ex) {
            System.err.println("Error al verificar empleado: " + ex.getMessage());
            return false;
        }
    }

    private Socio verificarSocio(String correo, String contrasena) {
        conexionSocios conexion = new conexionSocios();
        conexionMembresias conexionMembresias = new conexionMembresias();
        Socio socio = null;

        try (Connection conn = conexion.conexionBBDD()) {
            String sql = "SELECT * FROM socios WHERE correo = ? AND contrasena = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("idSocio");
                int idMembresia = rs.getInt("idMembresia");
                Date fechaInicio = rs.getDate("fechaInicio");
                Date fechaFin = rs.getDate("fechaFin");

                Membresia membresia = conexionMembresias.obtenerMembresia(idMembresia);

                socio = new Socio(
                        id,
                        rs.getString("nombre"),
                        correo,
                        contrasena,
                        membresia,
                        fechaInicio != null ? fechaInicio.toLocalDate() : null,
                        fechaFin != null ? fechaFin.toLocalDate() : null
                );
            }

        } catch (Exception ex) {
            System.err.println("Error al verificar socio: " + ex.getMessage());
        }

        return socio;
    }



    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}

