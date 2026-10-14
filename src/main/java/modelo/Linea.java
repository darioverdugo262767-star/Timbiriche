package modelo;

/**
 * Arista entre nodos vecinos. Existe desde el inicio; se ocupa al jugar.
 */
public final class Linea {

    private final NodoArbolTablero nodoInicio;
    private final NodoArbolTablero nodoFin;
    private boolean ocupada;
    private Jugador dueno;

    public Linea(NodoArbolTablero nodoInicio, NodoArbolTablero nodoFin) {
        if (nodoInicio == null || nodoFin == null || !nodoInicio.esAdyacente(nodoFin)) {
            throw new IllegalArgumentException("Los extremos deben ser nodos adyacentes");
        }
        this.nodoInicio = nodoInicio;
        this.nodoFin = nodoFin;
    }

    public NodoArbolTablero getNodoInicio() {
        return nodoInicio;
    }

    public NodoArbolTablero getNodoFin() {
        return nodoFin;
    }

    public int getX1() {
        return nodoInicio.getX();
    }

    public int getY1() {
        return nodoInicio.getY();
    }

    public int getX2() {
        return nodoFin.getX();
    }

    public int getY2() {
        return nodoFin.getY();
    }

    public boolean estaOcupada() {
        return ocupada;
    }

    public Jugador getDueno() {
        return dueno;
    }

    public Jugador getJugador() {
        return dueno;
    }

    /**
     * Solo permite ocupar una linea una vez.
     */
    public boolean setOcupada(Jugador jugador) {
        if (ocupada || jugador == null) {
            return false;
        }
        dueno = jugador;
        ocupada = true;
        return true;
    }
}
