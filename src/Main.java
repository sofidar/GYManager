import javax.swing.*;

import datos.conexionSocios;
import vista.FormBienvenida;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //Crea la ventana principal
            JFrame frame = new JFrame("Gestor GYM");
            FormBienvenida bienvenida = new FormBienvenida();

            //Asigna el panel principal de FormBienvenida
            frame.setContentPane(bienvenida.getPanelPrincipal());

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        //conexionSocios tabla = new conexionSocios();
        //tabla.conexionBBDD();
    }
}

