package vista;

import datos.conexionEmpleados;
import datos.conexionSocios;
import modelo.Empleado;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FormCrearUsuario {
    private JPanel panelPrincipal;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField pwdContrasena;
    private JComboBox<String> comboTipoUsuario;
    private JButton btnRegistrar;

    private conexionSocios cs;
    private conexionEmpleados ce;

    public FormCrearUsuario() {
        cs = new conexionSocios();
        ce = new conexionEmpleados();

        // Inicializar combo
        comboTipoUsuario.addItem("Socio");
        comboTipoUsuario.addItem("Empleado");

        // Botón Crear
        btnRegistrar.addActionListener(this::crearUsuario);
    }

    private void crearUsuario(ActionEvent e) {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(pwdContrasena.getPassword());
        String tipo = (String) comboTipoUsuario.getSelectedItem();

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal, "Completa todos los campos");
            return;
        }

        if (tipo.equals("Socio")) {
            if (cs.existeSocio(correo)) {
                JOptionPane.showMessageDialog(panelPrincipal, "El correo ya está registrado");
                return;
            }

            // Crear un nuevo JFrame para elegir membresía
            JFrame frame = new JFrame("Elegir Membresía");
            FormElegirMembresia formM = new FormElegirMembresia(nombre, correo, contrasena);

            frame.setContentPane(formM.getPanelPrincipal());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // cierra solo esta ventana
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } else { // Empleado
            if (ce.existeEmpleado(correo)) {
                JOptionPane.showMessageDialog(panelPrincipal, "El correo ya está registrado");
                return;
            }

            // Crear empleado sin id (DB asigna auto-increment)
            Empleado emp = new Empleado(nombre, correo, contrasena);
            ce.insertarEmpleado(emp);
            JOptionPane.showMessageDialog(panelPrincipal, "Empleado registrado correctamente");
        }

        // Limpiar campos
        txtNombre.setText("");
        txtCorreo.setText("");
        pwdContrasena.setText("");
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}




