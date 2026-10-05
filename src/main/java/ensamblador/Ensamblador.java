/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ensamblador;

import Vista.MarcadorView;
import Vista.TableroView;
import controlador.Controlador;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import modelo.FachadaModeloPartida;

/**
 *
 * @author jesus
 */
public class Ensamblador {

    public Ensamblador() {
    }

    public void ensamblar() {

        // ========================================
        // 1. Crear los objetos
        // ========================================
        FachadaModeloPartida modelo = new FachadaModeloPartida();

        Controlador controlador = new Controlador();

        int cantidadJugadores = 2;

        // Crear las vistas
        MarcadorView marcador = new MarcadorView(cantidadJugadores);
        TableroView tablero = new TableroView();

        // Conectar las vistas
        marcador.add(tablero, BorderLayout.CENTER);

        // Crear ventana
        JFrame ventana = new JFrame("Timbiriche");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(650, 700);
        ventana.setLocationRelativeTo(null);

        // Agregar la vista principal
        ventana.add(marcador);

        // ========================================
        // 2. Establecer las asociaciones
        // ========================================
        controlador.setModelo(modelo);

        tablero.setControlador(controlador);

        tablero.setModelo(modelo);
        marcador.setModelo(modelo);

        modelo.agregarObservador(tablero);
        modelo.agregarObservador(marcador);

        ventana.setVisible(true);

    }

}
