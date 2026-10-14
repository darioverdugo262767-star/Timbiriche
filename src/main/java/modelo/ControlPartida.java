package modelo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Reglas del juego. Ninguna decision sobre jugadas depende de Swing.
 */
public final class ControlPartida {

    private Partida partida;
    private Integer puntoSeleccionadoX;
    private Integer puntoSeleccionadoY;

    public Partida getPartida() {
        return partida;
    }

    public Integer getxPuntoSeleccionado() {
        return puntoSeleccionadoX;
    }

    public Integer getyPuntoSeleccionado() {
        return puntoSeleccionadoY;
    }

    public void setPartida(List<String> nombres, List<Color> colores) {
        if (nombres == null || colores == null || nombres.size() != colores.size()
                || nombres.size() < 2 || nombres.size() > 4) {
            throw new IllegalArgumentException("Indica nombres y colores para 2 a 4 jugadores");
        }
        List<Jugador> jugadores = new ArrayList<>();
        for (int i = 0; i < nombres.size(); i++) {
            jugadores.add(new Jugador(nombres.get(i), colores.get(i), null));
        }
        int dimension = jugadores.size() == 3 ? 20 : jugadores.size() == 4 ? 30 : 10;
        iniciarPartida(jugadores, dimension, dimension);
    }

    /**
     * Permite configurar tamano en tests; la partida real usa 10/20/30.
     */
    public void iniciarPartida(List<Jugador> jugadores, int columnas, int filas) {
        partida = new Partida(jugadores, new Tablero(columnas, filas));
        limpiarPuntoSeleccionado();
    }

    /**
     * Primer clic selecciona; segundo clic juega (o cancela/corrige).
     */
    public void seleccionarPunto(int x, int y) {
        if (partida == null || partida.estaFinalizada()
                || partida.getTablero().buscarNodo(x, y) == null) {
            return;
        }

        if (puntoSeleccionadoX == null) {
            registrarPuntoSeleccionado(x, y);
            return;
        }
        int anteriorX = puntoSeleccionadoX;
        int anteriorY = puntoSeleccionadoY;
        limpiarPuntoSeleccionado();
        if (anteriorX != x || anteriorY != y) {
            if (!realizarJugada(anteriorX, anteriorY, x, y)) {
                cambiarTurno();
            }
        }
    }

    /**
     * Entrada compatible con el antiguo controlador de un clic.
     */
    public void realizarJugada(int x, int y) {
        seleccionarPunto(x, y);
    }

    /**
     * Juega una linea legal entre dos nodos adyacentes.
     */
    public boolean realizarJugada(int x1, int y1, int x2, int y2) {
        if (partida == null || partida.estaFinalizada()) {
            return false;
        }
        Linea linea = partida.getTablero().crearLinea(
                x1, y1, x2, y2, partida.getTurnoActual());
        if (linea == null) {
            return false;
        }

        int puntos = partida.getTablero().contarCuadrados(linea);
        if (puntos > 0) {
            sumarPuntos(puntos);
        } else {
            cambiarTurno();
        }

        if (!partida.getTablero().hayMovimientosDisponibles()) {
            partida.finalizarPartida();
        }
        return true;
    }

    private void cambiarTurno() {
        List<Jugador> jugadores = partida.getJugadores();
        int actual = jugadores.indexOf(partida.getTurnoActual());
        partida.setTurnoActual(jugadores.get((actual + 1) % jugadores.size()));
    }

    private void sumarPuntos(int puntos) {
        partida.sumarPuntos(puntos);
    }

    private void registrarPuntoSeleccionado(int x, int y) {
        puntoSeleccionadoX = x;
        puntoSeleccionadoY = y;
    }

    private void limpiarPuntoSeleccionado() {
        puntoSeleccionadoX = null;
        puntoSeleccionadoY = null;
    }
}
