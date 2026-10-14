package modelo;

import java.awt.Color;
import java.util.List;

/**
 * Comandos separados de las consultas (segregacion de interfaces).
 */
public interface IOperacionesModelo {

    void seleccionarPunto(int x, int y);

    void realizarJugada(int x, int y);

    void setPartida(List<String> nombres, List<Color> colores);
}
