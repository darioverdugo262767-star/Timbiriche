package modelo;

import java.util.LinkedList;

/**
 * Interfaz de solo lectura que sigue el principio de segregacion de interfaces.
 * @author jesus
 */
public interface IConsultaModelo {
    
    /**
     * Obtiene las columnas del tablero.
     * @return columnas del tablero.
     */
    int getColumnasNodos();
    
    /**
     * Obtiene las filas del tablero.
     * @return filas del tablero.
     */
    int getFilasNodos();
    
    /**
     * Obtiene a los jugadores en la partida.
     * @return lista de jugadores en la partida.
     */
    LinkedList<Jugador> getJugadores();
    
    /**
     * Obtiene las lineas trazadas en el tablero.
     * @return lineas trazadas en el tablero.
     */
    LinkedList<Linea> getLineas();
    
    /**
     * Obtiene los cuadros pertenecientes a un jugador.
     * @return cuadros pertenecientes a un jugador
     */
    Jugador[][] getCuadros();
    
    /**
     * Obtiene al jugador con el turno actual.
     * @return jugador con el turno.
     */
    Jugador getTurnoActual();
    
    /**
     * Obtiene la cordenada x del punto seleccionado actualmente.
     * @return la posicion x de la seleccion.
     */
    Integer getXPuntoSeleccionado();
    
    /**
     * Obtiene la cordenada y del punto seleccionado actualmente.
     * @return la posicion y de la seleccion.
     */
    Integer getYPuntoSeleccionado();
    
}
