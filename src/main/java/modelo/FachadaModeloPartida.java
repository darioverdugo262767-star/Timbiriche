package modelo;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * Fachada del modelo del juego
 * @author jesus
 */
public class FachadaModeloPartida extends SujetoObservado
        implements IConsultaModelo, IOperacionesModelo {

    private final ControlPartida controlPartida;

    public FachadaModeloPartida() {
        this.controlPartida = new ControlPartida();
    }

    /**
     * Obtiene el modelo de la partida.
     * @return La instancia de la partida.
     */
    @Override
    public LinkedList<Jugador> getJugadores() {
        if (controlPartida.getPartida() == null) return new LinkedList<>();
        return controlPartida.getPartida().getJugadores();
    }

    /**
     * Procesa la jugada mediante la interaccion del jugador con el tablero.
     * @param x Coordenada de la columna elegida.
     * @param y Coordenada de la fila elegida.
     */
    @Override
    public void realizarJugada(int x, int y) {
        controlPartida.realizarJugada(x, y);
        notificarObservadores();
    }

    @Override
    public LinkedList<Linea> getLineas() {
        if (controlPartida.getPartida() == null 
                || controlPartida.getPartida().getTablero() == null) {
            return new LinkedList<>();
        }
        return controlPartida.getPartida().getTablero().getLineas();
    }

    @Override
    public Jugador[][] getCuadros() {
        if (controlPartida.getPartida() == null 
                || controlPartida.getPartida().getTablero() == null) {
            return new Jugador[0][0];
        }
        return controlPartida.getPartida().getTablero().getCuadros();
    }

    @Override
    public Jugador getTurnoActual() {
        if (controlPartida.getPartida() == null) return null;
        return controlPartida.getPartida().getTurnoActual();
    }

     /**
     * Obtiene la cordenada x del punto seleccionado actualmente.
     * @return la posicion x de la seleccion.
     */
    @Override
    public Integer getXPuntoSeleccionado() {
        return controlPartida.getxPuntoSeleccionado();
    }

     /**
     * Obtiene la cordenada y del punto seleccionado actualmente.
     * @return la posicion y de la seleccion.
     */
    @Override
    public Integer getYPuntoSeleccionado() {
        return controlPartida.getyPuntoSeleccionado();
    }

    @Override
    public int getColumnasNodos() {
        if (controlPartida.getPartida() == null 
                || controlPartida.getPartida().getTablero() == null) {
            return 5; // Valor por defecto si no ha iniciado la partida
        }
        return controlPartida.getPartida().getTablero().getColumnasNodos();
    }

    @Override
    public int getFilasNodos() {
        if (controlPartida.getPartida() == null 
                || controlPartida.getPartida().getTablero() == null) {
            return 5; // Valor por defecto si no ha iniciado la partida
        }
        return controlPartida.getPartida().getTablero().getFilasNodos();
    }
    
    /**
     * Inicializa una partida configurando los judaores, sus colores y el 
     * tamaño de el tablero segun los jugadores.
     * @param nombres Nombres de los jugadores.
     * @param colores Colores de los jugadores.
     */
    @Override
    public void setPartida(List<String> nombres, List<Color> colores) {
        controlPartida.setPartida(nombres, colores);
        notificarObservadores();
    }
}
