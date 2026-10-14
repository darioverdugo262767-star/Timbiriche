package presentacion;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.Cuadro;
import modelo.Jugador;
import modelo.Linea;
import modelo.NodoArbolTablero;

/**
 * Transforma entidades del dominio en datos para pintar; conoce los dos
 * contratos SEGREGADOS del modelo sin mostrar entidades a la Vista.
 */
public final class ControlPresentacionPartida {

    private final modelo.IConsultaModelo consultaModelo;
    private final modelo.IOperacionesModelo operacionesModelo;

    public ControlPresentacionPartida(modelo.IConsultaModelo consultaModelo,
            modelo.IOperacionesModelo operacionesModelo) {
        this.consultaModelo = consultaModelo;
        this.operacionesModelo = operacionesModelo;
    }

    public void seleccionarPunto(int x, int y) {
        operacionesModelo.seleccionarPunto(x, y);
    }

    public void realizarJugada(int x, int y) {
        operacionesModelo.realizarJugada(x, y);
    }

    public List<JugadorVista> obtenerVistaMarcador() {
        return prepararMarcador();
    }

    public List<JugadorVista> prepararMarcador() {
        List<JugadorVista> resultado = new ArrayList<>();
        for (Jugador j : consultaModelo.getJugadores()) {
            resultado.add(new JugadorVista(j.getNombre(), j.getColor(), j.getPuntuacion(),
                    j == consultaModelo.getTurnoActual()));
        }
        return Collections.unmodifiableList(resultado);
    }

    public List<PuntoVista> obtenerVistaTablero() {
        List<PuntoVista> resultado = new ArrayList<>();
        for (NodoArbolTablero nodo : consultaModelo.getNodos()) {
            resultado.add(new PuntoVista(nodo.getX(), nodo.getY()));
        }
        return Collections.unmodifiableList(resultado);
    }

    public List<LineaVista> obtenerLineasOcupadas() {
        List<LineaVista> resultado = new ArrayList<>();
        for (Linea linea : consultaModelo.getLineas()) {
            if (linea.estaOcupada()) {
                resultado.add(new LineaVista(linea.getX1(), linea.getY1(),
                        linea.getX2(), linea.getY2(), linea.getDueno().getColor()));
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public List<CuadroVista> obtenerCuadrosConDueno() {
        List<CuadroVista> resultado = new ArrayList<>();
        for (Cuadro cuadro : consultaModelo.getCuadros()) {
            if (cuadro.tieneDueno()) {
                resultado.add(new CuadroVista(cuadro.getX(), cuadro.getY(),
                        cuadro.getDueno().getNombre(), cuadro.getDueno().getColor()));
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public PuntoVista obtenerSeleccion() {
        Integer x = consultaModelo.getXPuntoSeleccionado();
        Integer y = consultaModelo.getYPuntoSeleccionado();
        return x == null || y == null ? null : new PuntoVista(x, y);
    }

    public List<PuntoVista> obtenerAdyacentesDisponibles() {
        PuntoVista seleccion = obtenerSeleccion();
        if (seleccion == null) {
            return Collections.emptyList();
        }
        NodoArbolTablero nodoSeleccionado = null;
        for (NodoArbolTablero nodo : consultaModelo.getNodos()) {
            if (nodo.getX() == seleccion.x() && nodo.getY() == seleccion.y()) {
                nodoSeleccionado = nodo;
                break;
            }
        }
        if (nodoSeleccionado == null) {
            return Collections.emptyList();
        }
        List<PuntoVista> resultado = new ArrayList<>();
        for (NodoArbolTablero adyacente : nodoSeleccionado.getAdyacentes()) {
            if (!estaDibujada(seleccion.x(), seleccion.y(), adyacente.getX(), adyacente.getY())) {
                resultado.add(new PuntoVista(adyacente.getX(), adyacente.getY()));
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    private boolean estaDibujada(int x1, int y1, int x2, int y2) {
        for (Linea linea : consultaModelo.getLineas()) {
            if (linea.estaOcupada()
                    && ((linea.getX1() == x1 && linea.getY1() == y1 && linea.getX2() == x2 && linea.getY2() == y2)
                    || (linea.getX2() == x1 && linea.getY2() == y1 && linea.getX1() == x2 && linea.getY1() == y2))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Traduccion centralizada para que clic y dibujo coincidan exactamente.
     */
    public Point traducirModeloACoordenadasPantalla(int x, int y, int ancho, int alto, int margen) {
        int cols = consultaModelo.getColumnasNodos();
        int filas = consultaModelo.getFilasNodos();
        if (cols < 2 || filas < 2 || ancho <= 2 * margen || alto <= 2 * margen) {
            return null;
        }
        double pasoX = (double) (ancho - 2 * margen) / (cols - 1);
        double pasoY = (double) (alto - 2 * margen) / (filas - 1);
        return new Point((int) Math.round(margen + x * pasoX),
                (int) Math.round(margen + y * pasoY));
    }

    public PuntoVista traducirClickACoordenadas(int px, int py, int ancho, int alto, int margen) {
        int cols = consultaModelo.getColumnasNodos();
        int filas = consultaModelo.getFilasNodos();
        if (cols < 2 || filas < 2 || ancho <= 2 * margen || alto <= 2 * margen) {
            return null;
        }
        double pasoX = (double) (ancho - 2 * margen) / (cols - 1);
        double pasoY = (double) (alto - 2 * margen) / (filas - 1);
        int x = (int) Math.round((px - margen) / pasoX);
        int y = (int) Math.round((py - margen) / pasoY);
        if (x < 0 || x >= cols || y < 0 || y >= filas) {
            return null;
        }
        Point real = traducirModeloACoordenadasPantalla(x, y, ancho, alto, margen);
        double tolerancia = Math.min(20, Math.max(6, Math.min(pasoX, pasoY) * 0.36));
        return real.distance(px, py) <= tolerancia ? new PuntoVista(x, y) : null;
    }

    public String obtenerResultado() {
        return prepararScore();
    }

    public String prepararScore() {
        if (!consultaModelo.estaFinalizada()) {
            return "Partida en curso";
        }
        List<Jugador> ganadores = consultaModelo.getGanadores();
        if (ganadores.size() == 1) {
            return "Ganador: " + ganadores.get(0).getNombre();
        }
        StringBuilder resultado = new StringBuilder("Empate: ");
        for (int i = 0; i < ganadores.size(); i++) {
            if (i > 0) {
                resultado.append(", ");
            }
            resultado.append(ganadores.get(i).getNombre());
        }
        return resultado.toString();
    }

    public int getColumnasNodos() {
        return consultaModelo.getColumnasNodos();
    }

    public int getFilasNodos() {
        return consultaModelo.getFilasNodos();
    }

    public String getTurnoActual() {
        Jugador actual = consultaModelo.getTurnoActual();
        return actual == null ? null : actual.getNombre();
    }

    public Color getColorTurno() {
        Jugador actual = consultaModelo.getTurnoActual();
        return actual == null ? Color.YELLOW : actual.getColor();
    }

    public boolean estaFinalizada() {
        return consultaModelo.estaFinalizada();
    }
}
