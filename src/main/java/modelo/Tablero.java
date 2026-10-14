package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tablero basado en un arbol binario de nodos.
 */
public final class Tablero {

    private final int columnas;
    private final int filas;
    private final NodoArbolTablero raiz;
    private final List<Linea> lineas = new ArrayList<>();
    private final List<Cuadro> cuadros = new ArrayList<>();
    private final Map<Long, Linea> lineasPorClave = new HashMap<>();

    public Tablero(int columnas, int filas) {
        if (columnas < 2 || filas < 2) {
            throw new IllegalArgumentException("El tablero necesita al menos 2x2 puntos");
        }
        this.columnas = columnas;
        this.filas = filas;
        raiz = construirArbol(0, columnas * filas - 1);
        crearAdyacenciasYLineas();
        crearCuadros();
    }

    private NodoArbolTablero construirArbol(int inicio, int fin) {
        if (inicio > fin) {
            return null;
        }
        int medio = inicio + (fin - inicio) / 2;
        NodoArbolTablero nodo = new NodoArbolTablero(medio % columnas, medio / columnas);
        nodo.setIzquierdo(construirArbol(inicio, medio - 1));
        nodo.setDerecho(construirArbol(medio + 1, fin));
        return nodo;
    }

    /**
     * Busca mediante el arbol (sin acceder a ninguna matriz).
     */
    public NodoArbolTablero buscarNodo(int x, int y) {
        if (x < 0 || y < 0 || x >= columnas || y >= filas) {
            return null;
        }
        int clave = y * columnas + x;
        NodoArbolTablero actual = raiz;
        while (actual != null) {
            int actualClave = actual.getY() * columnas + actual.getX();
            if (clave == actualClave) {
                return actual;
            }
            actual = clave < actualClave ? actual.getIzquierdo() : actual.getDerecho();
        }
        return null;
    }

    private void crearAdyacenciasYLineas() {
        for (NodoArbolTablero nodo : getNodos()) {
            int x = nodo.getX();
            int y = nodo.getY();
            if (x + 1 < columnas) {
                agregarConexion(nodo, buscarNodo(x + 1, y));
            }
            if (y + 1 < filas) {
                agregarConexion(nodo, buscarNodo(x, y + 1));
            }
        }
    }

    private void agregarConexion(NodoArbolTablero a, NodoArbolTablero b) {
        a.agregarAdyacente(b);
        b.agregarAdyacente(a);
        Linea linea = new Linea(a, b);
        lineas.add(linea);
        lineasPorClave.put(claveArista(a, b), linea);
    }

    private long claveArista(NodoArbolTablero a, NodoArbolTablero b) {
        int n1 = a.getY() * columnas + a.getX();
        int n2 = b.getY() * columnas + b.getX();
        return ((long) Math.min(n1, n2) << 32) | (Math.max(n1, n2) & 0xffffffffL);
    }

    public Linea buscarLinea(int x1, int y1, int x2, int y2) {
        NodoArbolTablero a = buscarNodo(x1, y1);
        NodoArbolTablero b = buscarNodo(x2, y2);
        if (a == null || b == null || !a.esAdyacente(b)) {
            return null;
        }
        return lineasPorClave.get(claveArista(a, b));
    }

    private void crearCuadros() {
        for (int y = 0; y < filas - 1; y++) {
            for (int x = 0; x < columnas - 1; x++) {
                cuadros.add(new Cuadro(x, y,
                        buscarLinea(x, y, x + 1, y),
                        buscarLinea(x, y + 1, x + 1, y + 1),
                        buscarLinea(x + 1, y, x + 1, y + 1),
                        buscarLinea(x, y, x, y + 1)));
            }
        }
    }

    private Cuadro buscarCuadro(int x, int y) {
        if (x < 0 || y < 0 || x >= columnas - 1 || y >= filas - 1) {
            return null;
        }
        return cuadros.get(y * (columnas - 1) + x);
    }

    /**
     * Devuelve null cuando la jugada es invalida o ya fue realizada.
     */
    public Linea crearLinea(int x1, int y1, int x2, int y2, Jugador jugador) {
        Linea linea = buscarLinea(x1, y1, x2, y2);
        return linea != null && linea.setOcupada(jugador) ? linea : null;
    }

    /**
     * Cuenta solamente los cuadros nuevos creados por esta linea.
     */
    public int contarCuadrados(Linea nueva) {
        if (nueva == null || !nueva.estaOcupada()) {
            return 0;
        }
        int x = Math.min(nueva.getX1(), nueva.getX2());
        int y = Math.min(nueva.getY1(), nueva.getY2());
        int completados = 0;
        if (nueva.getY1() == nueva.getY2()) {  // horizontal: arriba y abajo
            completados += asignarSiCompleto(buscarCuadro(x, y - 1), nueva.getDueno());
            completados += asignarSiCompleto(buscarCuadro(x, y), nueva.getDueno());
        } else { // vertical: izquierda y derecha
            completados += asignarSiCompleto(buscarCuadro(x - 1, y), nueva.getDueno());
            completados += asignarSiCompleto(buscarCuadro(x, y), nueva.getDueno());
        }
        return completados;
    }

    private int asignarSiCompleto(Cuadro cuadro, Jugador jugador) {
        return cuadro != null && cuadro.setDueno(jugador) ? 1 : 0;
    }

    public boolean hayMovimientosDisponibles() {
        for (Linea linea : lineas) {
            if (!linea.estaOcupada()) {
                return true;
            }
        }
        return false;
    }

    public int getColumnasNodos() {
        return columnas;
    }

    public int getFilasNodos() {
        return filas;
    }

    public NodoArbolTablero getRaiz() {
        return raiz;
    }

    public List<Linea> getLineas() {
        return Collections.unmodifiableList(lineas);
    }

    public List<Cuadro> getCuadros() {
        return Collections.unmodifiableList(cuadros);
    }

    /**
     * Recorre el arbol in-order; los nodos NO se guardan en una cuadricula.
     */
    public List<NodoArbolTablero> getNodos() {
        List<NodoArbolTablero> resultado = new ArrayList<>(columnas * filas);
        recorrer(raiz, resultado);
        return Collections.unmodifiableList(resultado);
    }

    private void recorrer(NodoArbolTablero nodo, List<NodoArbolTablero> resultado) {
        if (nodo == null) {
            return;
        }
        recorrer(nodo.getIzquierdo(), resultado);
        resultado.add(nodo);
        recorrer(nodo.getDerecho(), resultado);
    }
}
