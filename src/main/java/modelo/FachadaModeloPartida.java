package modelo;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

/**
 * Entrada unica al modelo. Notifica cambios mediante Observer.
 */
public final class FachadaModeloPartida extends SujetoObservado
        implements IConsultaModelo, IOperacionesModelo {

    private final ControlPartida controlPartida = new ControlPartida();

    @Override
    public void seleccionarPunto(int x, int y) {
        controlPartida.seleccionarPunto(x, y);
        notificarObservadores();
    }

    @Override
    public void realizarJugada(int x, int y) {
        seleccionarPunto(x, y);
    }

    @Override
    public void setPartida(List<String> nombres, List<Color> colores) {
        controlPartida.setPartida(nombres, colores);
        notificarObservadores();
    }

    public ControlPartida getControlPartida() {
        return controlPartida;
    }

    private Partida partida() {
        return controlPartida.getPartida();
    }

    private Tablero tablero() {
        return partida() == null ? null : partida().getTablero();
    }

    @Override
    public int getColumnasNodos() {
        return tablero() == null ? 0 : tablero().getColumnasNodos();
    }

    @Override
    public int getFilasNodos() {
        return tablero() == null ? 0 : tablero().getFilasNodos();
    }

    @Override
    public List<Jugador> getJugadores() {
        return partida() == null ? Collections.emptyList() : partida().getJugadores();
    }

    @Override
    public List<NodoArbolTablero> getNodos() {
        return tablero() == null ? Collections.emptyList() : tablero().getNodos();
    }

    @Override
    public List<Linea> getLineas() {
        return tablero() == null ? Collections.emptyList() : tablero().getLineas();
    }

    @Override
    public List<Cuadro> getCuadros() {
        return tablero() == null ? Collections.emptyList() : tablero().getCuadros();
    }

    @Override
    public Jugador getTurnoActual() {
        return partida() == null ? null : partida().getTurnoActual();
    }

    @Override
    public Integer getXPuntoSeleccionado() {
        return controlPartida.getxPuntoSeleccionado();
    }

    @Override
    public Integer getYPuntoSeleccionado() {
        return controlPartida.getyPuntoSeleccionado();
    }

    @Override
    public boolean estaFinalizada() {
        return partida() != null && partida().estaFinalizada();
    }

    @Override
    public List<Jugador> getGanadores() {
        return partida() == null ? Collections.emptyList() : partida().determinarGanadores();
    }
}
