package com.mycompany.timbiriche;

import Vista.MarcadorView;
import Vista.TableroView;
import ensamblador.Ensamblador;
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
            Ensamblador ensamblador = new Ensamblador();
            ensamblador.ensamblar();
        });
    }
}
