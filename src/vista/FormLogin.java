package vista;

import datos.conexionMembresias;
import datos.conexionSocios;
import datos.conexionEmpleados;
import excepciones.UsuarioNoEncontradoException;
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
        try {
            // Intentar login como EMPLEADO
            Empleado empleado = new Empleado(0, "", correo, contrasena);
            if (empleado.verificar()) {
                JOptionPane.showMessageDialog(panelPrincipal, "Inicio de sesión exitoso como EMPLEADO.");
                JFrame empleadoFrame = new JFrame("Panel Empleado");
                empleadoFrame.setContentPane(new FormEmpleado().getPanelPrincipal());
                empleadoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                empleadoFrame.pack();
                empleadoFrame.setLocationRelativeTo(null);
                empleadoFrame.setVisible(true);
                ((JFrame) SwingUtilities.getWindowAncestor(panelPrincipal)).dispose();
                return;
            }
        } catch (UsuarioNoEncontradoException exEmpleado) {
            // No mostrar mensaje aún, se intenta como socio
        }

        try {
            // Intentar login como SOCIO
            Socio socioLogueado = new Socio(0, "", correo, contrasena, null, null, null);
            if (socioLogueado.verificar()) {
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
        } catch (UsuarioNoEncontradoException exSocio) {
            // Si tampoco es socio, mostrar mensaje final
            JOptionPane.showMessageDialog(panelPrincipal, "Correo o contraseña incorrectos.");
        }




        JOptionPane.showMessageDialog(panelPrincipal, "Correo o contraseña incorrectos.");
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}

