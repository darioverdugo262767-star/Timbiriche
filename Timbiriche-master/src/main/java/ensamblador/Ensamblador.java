package ensamblador;

import Vista.MarcadorView;
import Vista.ScoreView;
import Vista.TableroView;
import controlador.EjercerTurnoControlador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.JFrame;
import modelo.FachadaModeloPartida;
import presentacion.FachadaPresentacionPartida;

/**
 * Composition root: conecta Modelo -> Presentacion -> Vista/Controlador.
 */
public final class Ensamblador {

    public void ensamblar() {
        FachadaModeloPartida modelo = new FachadaModeloPartida();
        FachadaPresentacionPartida presentacion = new FachadaPresentacionPartida(modelo, modelo);
        modelo.agregarObservador(presentacion);
        EjercerTurnoControlador controlador = new EjercerTurnoControlador(presentacion);
 
        List<String> nombres = List.of("Pepe", "Maria");
        modelo.setPartida(nombres, List.of(Color.RED, Color.BLUE));
 
        for (String nombre : nombres) {
            MarcadorView marcador = new MarcadorView();
            TableroView tablero = new TableroView();
            tablero.setModelo(presentacion);
            tablero.setControlador(controlador);
            tablero.setJugadorAsignado(nombre);
            marcador.add(tablero, BorderLayout.CENTER);
            marcador.setJugadorLocal(nombre);
            marcador.setModelo(presentacion);
            presentacion.agregarObservador(tablero);
            presentacion.agregarObservador(marcador);
            crearVentana("Timbiriche - " + nombre, marcador);
        }
        ScoreView score = new ScoreView();
        score.setModelo(presentacion);
        presentacion.agregarObservador(score);
        presentacion.notificarObservadores();
    }
 
    private void crearVentana(String titulo, MarcadorView marcador) {
        JFrame ventana = new JFrame(titulo);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(720, 820);
        ventana.add(marcador);
        ventana.setLocationByPlatform(true);
        ventana.setVisible(true);
    }
}
