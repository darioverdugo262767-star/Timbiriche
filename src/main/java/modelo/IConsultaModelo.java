package modelo;

import java.util.List;

/**
 * API de consulta de entidades del dominio; solo la capa Presentacion la usa.
 */
public interface IConsultaModelo {

    int getColumnasNodos();

    int getFilasNodos();

    List<Jugador> getJugadores();

    List<NodoArbolTablero> getNodos();

    List<Linea> getLineas();

    List<Cuadro> getCuadros();

    Jugador getTurnoActual();

    Integer getXPuntoSeleccionado();

    Integer getYPuntoSeleccionado();

    boolean estaFinalizada();

    List<Jugador> getGanadores();
}
