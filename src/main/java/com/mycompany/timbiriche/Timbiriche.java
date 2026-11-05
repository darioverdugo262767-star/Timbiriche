package com.mycompany.timbiriche;

import ensamblador.Ensamblador;
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
