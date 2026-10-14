package presentacion;

import java.awt.Color;

/**
 * Estado de un jugador sin exponer su entidad mutable del modelo.
 */
public record JugadorVista(String nombre, Color color, int puntuacion, boolean turno) {

}
