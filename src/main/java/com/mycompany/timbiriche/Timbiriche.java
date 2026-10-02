package com.mycompany.timbiriche;

import Vista.MarcadorView;
import Vista.TableroView;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Dario
 */
public class Timbiriche {

    public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Timbiriche");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(650, 700);
            ventana.setLocationRelativeTo(null);
            int cantidadJugadores = 2;
            MarcadorView marcador = new MarcadorView(cantidadJugadores);
            TableroView tablero = new TableroView();
            marcador.add(tablero, BorderLayout.CENTER);
            ventana.add(marcador);
            ventana.setVisible(true);
        });
    }
}
