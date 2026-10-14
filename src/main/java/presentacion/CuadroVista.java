package presentacion;

import java.awt.Color;

/**
 * Cuadro capturado que puede pintarse sin consultar el dominio.
 */
public record CuadroVista(int x, int y, String dueno, Color color) {

}
