package presentacion;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 * Contrato de solo lectura que usan las Vistas.
 */
public interface IConsultaModelo {

    int getColumnasNodos();

    int getFilasNodos();

    List<PuntoVista> getNodos();

    List<JugadorVista> getJugadores();

    List<LineaVista> getLineas();

    List<CuadroVista> getCuadros();

    String getTurnoActual();

    Color getColorTurno();

    PuntoVista getPuntoSeleccionado();

    List<PuntoVista> getAdyacentesDisponibles();

    PuntoVista traducirClickACoordenadas(int pixelX, int pixelY, int ancho, int alto, int margen);

    Point traducirModeloACoordenadasPantalla(int x, int y, int ancho, int alto, int margen);

    boolean estaFinalizada();

    String obtenerResultado();
}
