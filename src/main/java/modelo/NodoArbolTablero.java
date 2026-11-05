package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Punto del tablero. Forma parte de un arbol binario de busqueda ordenado por
 * posicion y, por separado, conoce sus vecinos geometricos del juego. Las
 * adyacencias NO son ramas del arbol (Timbiriche contiene ciclos).
 */
public final class NodoArbolTablero {

    private final int x;
    private final int y;
    private NodoArbolTablero izquierdo;
    private NodoArbolTablero derecho;
    private final List<NodoArbolTablero> adyacentes = new ArrayList<>(4);

    public NodoArbolTablero(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public NodoArbolTablero getIzquierdo() {
        return izquierdo;
    }

    public NodoArbolTablero getDerecho() {
        return derecho;
    }

    public List<NodoArbolTablero> getAdyacentes() {
        return Collections.unmodifiableList(adyacentes);
    }

    void setIzquierdo(NodoArbolTablero izquierdo) {
        this.izquierdo = izquierdo;
    }

    void setDerecho(NodoArbolTablero derecho) {
        this.derecho = derecho;
    }

    void agregarAdyacente(NodoArbolTablero vecino) {
        if (vecino != null && vecino != this && !adyacentes.contains(vecino)) {
            adyacentes.add(vecino);
        }
    }

    public boolean esAdyacente(NodoArbolTablero otro) {
        return adyacentes.contains(otro);
    }
}
