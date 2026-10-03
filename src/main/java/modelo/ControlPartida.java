/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.LinkedList;

/**
 *
 * @author jesus
 */
public class ControlPartida {

    private Partida partida;

    private Integer xPuntoSeleccionado;

    private Integer yPuntoSeleccionado;

    public ControlPartida() {
        this.partida = null;
        xPuntoSeleccionado = null;
        yPuntoSeleccionado = null;
    }

    public Linea actualizar(int x, int y) {

        if (partida == null) {

            return null;

        }

        if (xPuntoSeleccionado == null && yPuntoSeleccionado == null) {

            xPuntoSeleccionado = x;
            yPuntoSeleccionado = y;

            return null;

        }

        if (xPuntoSeleccionado == x && yPuntoSeleccionado == y) {

            xPuntoSeleccionado = null;
            yPuntoSeleccionado = null;

            return null;

        }

        Linea lineaCreada = partida.getTablero().crearLinea(xPuntoSeleccionado, yPuntoSeleccionado, x, y, partida.getTurnoActual());

        xPuntoSeleccionado = null;
        yPuntoSeleccionado = null;

        int cuadrados = partida.getTablero().contarCuadrados(lineaCreada);

        if (cuadrados > 0) {
            sumarPuntos(cuadrados);
        } else {
            cambiarTurno();
        }

        return lineaCreada;

    }

    private void cambiarTurno() {

        int tamano = partida.getJugadores().size() - 1;

        int index = partida.getJugadores().indexOf(partida.getTurnoActual());

        if (tamano == index) {

            partida.setTurnoActual(partida.getJugadores().getFirst());

        } else {

            partida.setTurnoActual(partida.getJugadores().get(index + 1));

        }

    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    private void sumarPuntos(int puntos) {

        Jugador jugadorActual = partida.getTurnoActual();

        jugadorActual.setPuntuacion(jugadorActual.getPuntuacion() + puntos);

    }

}
