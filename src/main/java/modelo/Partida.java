package modelo;

import java.util.LinkedList;

/**
 * Clase que representa a la partida en el modelo.
 * @author jesus
 */
public class Partida {

    /*
    Lista de jugadores en la partida.
    */
    private final LinkedList<Jugador> jugadores;

    /*
    Jugador con el turno actual para hacer un movimiento.
    */
    private Jugador turnoActual;

    /*
    Tablero asignado a la partida.
    */
    private final Tablero tablero;

    /**
     * Constructor de la partida.
     * @param jugadores Lista de jugadores en la partida.
     * @param tablero Tablero asignado a la partida.
     */
    public Partida(LinkedList<Jugador> jugadores, Tablero tablero) {
        this.jugadores = jugadores;
        this.tablero = tablero;
        this.turnoActual = jugadores.getFirst();
    }

    /**
     * Obtiene a los jugadores en la partida.
     * @return lista de jugadores en la partida.
     */
    public LinkedList<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Obtiene el tablero actual de la partida.
     * @return tablero de la partida.
     */
    public Tablero getTablero() {
        return tablero;
    }

    /**
     * Obtiene al jugador con el turno actual.
     * @return jugador con el turno.
     */
    public Jugador getTurnoActual() {
        return turnoActual;
    }

    /**
     * Asigna el turno a un jugador dentro de la partida.
     * @param turnoActual jugador a asignar el turno.
     */
    public void setTurnoActual(Jugador turnoActual) {
        this.turnoActual = turnoActual;
    }

}
