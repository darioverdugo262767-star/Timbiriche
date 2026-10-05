package modelo;

/**
 * Clase que representa una linea trazada en el modelo.
 * @author jesus
 */
public class Linea {

    /*
    Cordenada x del primer punto.
    */
    private final int x1;

    /*
    Cordenada y del primer punto.
    */
    private final int y1;

    /*
    Cordenada x del segundo punto.
    */
    private final int x2;

    /*
    Cordenada y del segundo punto.
    */
    private final int y2;

    /*
    Jugador que realizo la linea.
    */
    private final Jugador jugador;

    /**
     * Contructor de la linea.
     * @param x1 Cordenada x del primer punto.
     * @param y1 Cordenada y del primer punto.
     * @param x2 Cordenada x del segundo punto.
     * @param y2 Cordenada y del segundo punto.
     * @param jugador Jugador que realizo la linea.
     */
    public Linea(int x1, int y1, int x2, int y2, Jugador jugador) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.jugador = jugador;
    }

    /**
     * Obtiene la cordenada x del punto de origen.
     * @return cordenada x del punto de origen.
     */
    public int getX1() {
        return x1;
    }

    /**
     * Obtiene la cordenada y del punto de origen.
     * @return cordenada y del punto de origen.
     */
    public int getY1() {
        return y1;
    }

    /**
     * Obtiene la cordenada x del punto de destino.
     * @return cordenada x del punto de destino. 
     */
    public int getX2() {
        return x2;
    }

    /**
     * Obtiene la cordenada y del punto de destino.
     * @return cordenada y del punto de destino. 
     */
    public int getY2() {
        return y2;
    }

    /**
     * Obtiene al jugador propietario de la linea.
     * @return jugador propietario de la linea.
     */
    public Jugador getJugador() {
        return jugador;
    }

}
