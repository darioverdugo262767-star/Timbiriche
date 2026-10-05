package modelo;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que contiene toda la logica del juego de timbiriche.
 * Administra los turnos, las jugadas y las puntuaciones.
 * @author jesus
 */
public class ControlPartida {

    /*
    Modelo de la partida.
    */
    private Partida partida;

    /*
    Cordenada x del punto seleccionado por el jugador.
    */
    private Integer xPuntoSeleccionado;

    /*
    Cordenada y del punto seleccionado por el jugador.
    */
    private Integer yPuntoSeleccionado;

    /**
     * Constructor de la clase.
     */
    public ControlPartida() {
        this.partida = null;
        xPuntoSeleccionado = null;
        yPuntoSeleccionado = null;
    }
    
     /**
     * Obtiene el modelo de la partida.
     * @return La instancia de la partida.
     */
    public Partida getPartida() {
        return partida;
    }

    /**
     * Obtiene la cordenada x del punto seleccionado actualmente.
     * @return la posicion x de la seleccion.
     */
    public Integer getxPuntoSeleccionado() {
        return xPuntoSeleccionado;
    }

    /**
     * Obtiene la cordenada y del punto seleccionado actualmente.
     * @return la posicion y de la seleccion.
     */
    public Integer getyPuntoSeleccionado() {
        return yPuntoSeleccionado;
    }

    /**
     * Procesa la jugada mediante la interaccion del jugador con el tablero.
     * @param x Coordenada de la columna elegida.
     * @param y Coordenada de la fila elegida.
     */
    public void realizarJugada(int x, int y) {

        if (partida == null) {
            return;
        }

        //Primer clic: Seleccionar el primer nodo
        if (xPuntoSeleccionado == null && yPuntoSeleccionado == null) {
            xPuntoSeleccionado = x;
            yPuntoSeleccionado = y;
            return;
        }

        //Clic en el mismo nodo: Cancelar seleccion
        if (xPuntoSeleccionado == x && yPuntoSeleccionado == y) {
            xPuntoSeleccionado = null;
            yPuntoSeleccionado = null;
            return;
        }

        //Validar adyacencia
        int distM = Math.abs(xPuntoSeleccionado - x) + Math.abs(yPuntoSeleccionado - y);
        if (distM != 1) {
            // No son nodos vecinos adyacentes; se limpia la seleccion y se ignora el intento
            xPuntoSeleccionado = null;
            yPuntoSeleccionado = null;
            return;
        }

        //Intentar crear la linea en el tablero
        Linea lineaCreada = partida.getTablero().crearLinea(
                xPuntoSeleccionado, yPuntoSeleccionado, x, y, partida.getTurnoActual()
        );

        //Limpiar la seleccion de puntos
        xPuntoSeleccionado = null;
        yPuntoSeleccionado = null;

        //Si la línea ya existia, se ignora la jugada sin cambiar de turno
        if (lineaCreada == null) {
            return;
        }

        //Evaluar si se completaron cuadros con la nueva linea
        int cuadrados = partida.getTablero().contarCuadrados(lineaCreada);

        //Si se completa un cuadro no se cambia turno
        if (cuadrados > 0) {
            sumarPuntos(cuadrados);
        } else {
            cambiarTurno();
        }
    }

    /**
     * Cambia el turno del jugador.
     */
    private void cambiarTurno() {
        int tamano = partida.getJugadores().size() - 1;
        int index = partida.getJugadores().indexOf(partida.getTurnoActual());
        if (tamano == index) {
            partida.setTurnoActual(partida.getJugadores().getFirst());
        } else {
            partida.setTurnoActual(partida.getJugadores().get(index + 1));
        }

    }

    /**
     * Suma los puntos de los jugadores.
     * @param puntos Cantidad de puntos a sumar.
     */
    private void sumarPuntos(int puntos) {
        Jugador jugadorActual = partida.getTurnoActual();
        jugadorActual.setPuntuacion(jugadorActual.getPuntuacion() + puntos);

    }

    /**
     * Inicializa una partida configurando los judaores, sus colores y el 
     * tamaño de el tablero segun los jugadores.
     * @param nombres Nombres de los jugadores.
     * @param colores Colores de los jugadores.
     */
    public void setPartida(List<String> nombres, List<Color> colores) {
        LinkedList<Jugador> jugadores = new LinkedList<>();
        for (int i = 0; i < nombres.size(); i++) {
            jugadores.add(new Jugador(nombres.get(i), colores.get(i), null));
        }
        int dimension;
        if (jugadores.size() == 3) {
            dimension = 20;
        } else if (jugadores.size() >= 4) {
            dimension = 30;
        } else {
            dimension = 10;
        }
        Tablero tablero = new Tablero(dimension, dimension, new LinkedList<Linea>());
        this.partida = new Partida(jugadores, tablero);
        this.xPuntoSeleccionado = null;
        this.yPuntoSeleccionado = null;
    }
    
}
