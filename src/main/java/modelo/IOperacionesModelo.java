package modelo;

import java.awt.Color;
import java.util.List;

/**
 * Interfaz de solo lectura que sigue el principio de segregacion de interfaces.
 * @author jesus
 */
public interface IOperacionesModelo {
    
    /**
     * Procesa la jugada mediante la interaccion del jugador con el tablero.
     * @param x Coordenada de la columna elegida.
     * @param y Coordenada de la fila elegida.
     */
    public void realizarJugada(int x, int y);
    
    /**
     * Inicializa una partida configurando los judaores, sus colores y el 
     * tamaño de el tablero segun los jugadores.
     * @param nombres Nombres de los jugadores.
     * @param colores Colores de los jugadores.
     */
    void setPartida(List<String> nombres, List<Color> colores);
    
}
