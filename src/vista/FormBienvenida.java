package vista;

import javax.swing.*;

public class FormBienvenida {
    private JPanel panelPrincipal;
    private JButton btnIniciarSesion;
    private JButton btnCrearUsuario;

    public FormBienvenida() {

        // Botón Crear Usuario
        btnCrearUsuario.addActionListener(e -> {
            JFrame crearFrame = new JFrame("Crear Usuario");
            FormCrearUsuario formCrear = new FormCrearUsuario();
            crearFrame.setContentPane(formCrear.getPanelPrincipal());
            crearFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            crearFrame.pack();
            crearFrame.setLocationRelativeTo(null);
            crearFrame.setVisible(true);
        });

        // Botón Iniciar Sesión
        btnIniciarSesion.addActionListener(e -> {
            JFrame loginFrame = new JFrame("Iniciar Sesión");
            FormLogin loginForm = new FormLogin();
            loginFrame.setContentPane(loginForm.getPanelPrincipal());
            loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            loginFrame.pack();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });
    }
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}
