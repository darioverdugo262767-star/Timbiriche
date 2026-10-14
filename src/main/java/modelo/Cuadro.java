package modelo;

/**
 * Una casilla se completa cuando sus cuatro lados estan ocupados.
 */
public final class Cuadro {

    private final int x;
    private final int y;
    private final Linea lineaArriba;
    private final Linea lineaAbajo;
    private final Linea lineaDerecha;
    private final Linea lineaIzquierda;
    private Jugador dueno;

    public Cuadro(int x, int y, Linea arriba, Linea abajo,
            Linea derecha, Linea izquierda) {
        if (arriba == null || abajo == null || derecha == null || izquierda == null) {
            throw new IllegalArgumentException("El cuadro requiere cuatro lineas");
        }
        this.x = x;
        this.y = y;
        lineaArriba = arriba;
        lineaAbajo = abajo;
        lineaDerecha = derecha;
        lineaIzquierda = izquierda;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Linea getLineaArriba() {
        return lineaArriba;
    }

    public Linea getLineaAbajo() {
        return lineaAbajo;
    }

    public Linea getLineaDerecha() {
        return lineaDerecha;
    }

    public Linea getLineaIzquierda() {
        return lineaIzquierda;
    }

    public Jugador getDueno() {
        return dueno;
    }

    public boolean tieneDueno() {
        return dueno != null;
    }

    public boolean estaCompleto() {
        return lineaArriba.estaOcupada() && lineaAbajo.estaOcupada()
                && lineaDerecha.estaOcupada() && lineaIzquierda.estaOcupada();
    }

    /**
     * Devuelve true si esta jugada adjudico el cuadro por primera vez.
     */
    public boolean setDueno(Jugador jugador) {
        if (jugador == null || tieneDueno() || !estaCompleto()) {
            return false;
        }
        dueno = jugador;
        return true;
    }
}
