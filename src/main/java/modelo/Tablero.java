package modelo;

import java.util.LinkedList;

/**
 * Clase que representa al tablero en el modelo.
 * @author jesus
 */
public class Tablero {

    /*
    Cantidad de columnas de nodos que tiene el tablero.
    */
    private final int columnasNodos;
    
    /*
    Cantidad de filas de nodos que tiene el tablero.
    */
    private final int filasNodos;
    
    /*
    Lista enlazada con todas las lineas trazadas en el tablero.
    */
    private final LinkedList<Linea> lineas;
    
    /*
    Matriz que representa las casillas pertenecientes al jugador.
    */
    private final Jugador [][] cuadros;

    /**
     * Contructor del tablero.
     * @param columnasNodos Cantidad de columnas de nodos que tiene el tablero.
     * @param filasNodos Cantidad de filas de nodos que tiene el tablero.
     * @param lineas  Lista enlazada con todas las lineas trazadas en el tablero.
     */
    public Tablero(int columnasNodos, int filasNodos, LinkedList<Linea> lineas) {
        this.columnasNodos = columnasNodos;
        this.filasNodos = filasNodos;
        this.lineas = lineas;
        this.cuadros = new Jugador[columnasNodos - 1][filasNodos - 1];
    }

    /**
     * Obtiene las columnas del tablero.
     * @return columnas del tablero.
     */
    public int getColumnasNodos() {
        return columnasNodos;
    }

    /**
     * Obtiene las filas del tablero.
     * @return filas del tablero.
     */
    public int getFilasNodos() {
        return filasNodos;
    }

    /**
     * Obtiene las lineas trazadas en el tablero.
     * @return lineas trazadas en el tablero.
     */
    public LinkedList<Linea> getLineas() {
        return lineas;
    }

    /**
     * Obtiene los cuadros pertenecientes a un jugador.
     * @return cuadros pertenecientes a un jugador
     */
    public Jugador[][] getCuadros() {
        return cuadros;
    }
    
    
    /**
     * Intenta crear una linea entre dos puntos dados.
     * @param xPuntoSeleccionado Coordenada x del punto inicial.
     * @param yPuntoSeleccionado Coordenada y del punto inicial.
     * @param x Coordenada x del punto final.
     * @param y Coordenada y del punto final.
     * @param turnoActual jugador que intenta trazar la linea.
     * @return La linea trazada.
     */
    public Linea crearLinea(int xPuntoSeleccionado, int yPuntoSeleccionado, int x, int y, Jugador turnoActual) {
        if (existeLinea(xPuntoSeleccionado, yPuntoSeleccionado, x, y)) {
            return null;
        }
        Linea nuevaLinea = new Linea(xPuntoSeleccionado, yPuntoSeleccionado, x, y, turnoActual);
        lineas.add(nuevaLinea);
        return nuevaLinea;
    }

    /**
     * Comprueba si existe un alinea trazada entre dos puntos.
     * @param x1 Coordenada X del primer punto.
     * @param y1 Coordenada Y del primer punto.
     * @param x2 Coordenada X del segundo punto.
     * @param y2 Coordenada Y del segundo punto.
     * @return si la linea ya fue trazada previamento o no.
     */
    private boolean existeLinea(int x1, int y1, int x2, int y2) {
        for (Linea linea : lineas) {
            if (linea.getX1() == x1
                    && linea.getY1() == y1
                    && linea.getX2() == x2
                    && linea.getY2() == y2) {
                return true;
            }
            if (linea.getX1() == x2
                    && linea.getY1() == y2
                    && linea.getX2() == x1
                    && linea.getY2() == y1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Evalua si una linea trazada completa uno o dos cuadros.
     * @param nueva instancia de la linea recien trazada.
     * @return numero de cuadros completados.
     */
    public int contarCuadrados(Linea nueva) {
        int creados = 0;
        int x1 = nueva.getX1();
        int y1 = nueva.getY1();
        int x2 = nueva.getX2();
        int y2 = nueva.getY2();
        Jugador jugador = nueva.getJugador();

        int maxCasillasX = columnasNodos - 1; // Limite dinamico X
        int maxCasillasY = filasNodos - 1;    // Limite dinamico Y

        //Linea Horizontal
        if (y1 == y2) {
            int minX = Math.min(x1, x2);

            // Cuadro de ARRIBA (valido si no es el borde superior)
            if (y1 > 0) {
                if (existeLinea(minX, y1 - 1, minX, y1)
                        && existeLinea(minX + 1, y1 - 1, minX + 1, y1)
                        && existeLinea(minX, y1 - 1, minX + 1, y1 - 1)) {
                    cuadros[minX][y1 - 1] = jugador;
                    creados++;
                }
            }

            // Cuadro de ABAJO (valido si no es el borde inferior)
            if (y1 < maxCasillasY) {
                if (existeLinea(minX, y1, minX, y1 + 1)
                        && existeLinea(minX + 1, y1, minX + 1, y1 + 1)
                        && existeLinea(minX, y1 + 1, minX + 1, y1 + 1)) {
                    cuadros[minX][y1] = jugador;
                    creados++;
                }
            }
        } 
        //Linea Vertical
        else if (x1 == x2) {
            int minY = Math.min(y1, y2);

            // Cuadro de la IZQUIERDA (valido si no es el borde izquierdo)
            if (x1 > 0) {
                if (existeLinea(x1 - 1, minY, x1, minY)
                        && existeLinea(x1 - 1, minY + 1, x1, minY + 1)
                        && existeLinea(x1 - 1, minY, x1 - 1, minY + 1)) {
                    cuadros[x1 - 1][minY] = jugador;
                    creados++;
                }
            }

            // Cuadro de la DERECHA (valido si no es el borde derecho)
            if (x1 < maxCasillasX) {
                if (existeLinea(x1, minY, x1 + 1, minY)
                        && existeLinea(x1, minY + 1, x1 + 1, minY + 1)
                        && existeLinea(x1 + 1, minY, x1 + 1, minY + 1)) {
                    cuadros[x1][minY] = jugador;
                    creados++;
                }
            }
        }
        return creados;
    }
    
}

