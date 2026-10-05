package ensamblador;

import Vista.MarcadorView;
import Vista.TableroView;
import controlador.Controlador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.JFrame;
import modelo.FachadaModeloPartida;

/**
 * Clase esamblador encargada de inicializar e integrar los componentes del MVC.
 * Configura el modelo, el controlados y crea las vistas ademas de agregar 
 * los observadores a las vistas.
 * @author jesus
 */
public class Ensamblador {
    
    /**
     * Inicializa la partida, instancia el modelo y controlador contruye 
     * las vistas y agrega los observadores a las vistas.
     * Pendiente a cambios para agregar jugadores dinamicos.
     * Estado actual Hardcodeado.
     */
    public void ensamblar() {
        FachadaModeloPartida modelo = new FachadaModeloPartida();
        Controlador controlador = new Controlador();
        controlador.setModelo(modelo);

        List<String> nombres = List.of("Pepe", "Maria");
        modelo.setPartida(nombres, List.of(Color.RED, Color.BLUE));

        MarcadorView marcador1 = new MarcadorView();
        MarcadorView marcador2 = new MarcadorView();
        TableroView tablero1 = new TableroView();
        TableroView tablero2 = new TableroView();

        tablero1.setJugadorAsignado(modelo.getJugadores().get(0));
        tablero2.setJugadorAsignado(modelo.getJugadores().get(1));

        marcador1.add(tablero1, BorderLayout.CENTER);
        marcador2.add(tablero2, BorderLayout.CENTER);

        tablero1.setControlador(controlador);
        tablero1.setModelo(modelo);
        tablero2.setControlador(controlador);
        tablero2.setModelo(modelo);
        marcador1.setModelo(modelo);
        marcador2.setModelo(modelo);

        modelo.agregarObservador(tablero1);
        modelo.agregarObservador(tablero2);
        modelo.agregarObservador(marcador1);
        modelo.agregarObservador(marcador2);
        modelo.notificarObservadores();

        crearVentana("Timbiriche - Pantalla 1 (" + nombres.get(0) + ")", marcador1);
        crearVentana("Timbiriche - Pantalla 2 (" + nombres.get(1) + ")", marcador2);
    }

    /**
     * Crea, configura y despliega una ventana para las vistas del programa.
     * @param titulo Titulo de la ventana.
     * @param marcador Instancia de la vista de marcador del jugador.
     */
    private void crearVentana(String titulo, MarcadorView marcador) {
        JFrame ventana = new JFrame(titulo);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(720, 820);
        ventana.add(marcador);
        ventana.setVisible(true);
    }
}