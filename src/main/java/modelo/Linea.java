/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jesus
 */
public class Linea {

    private final int x1;

    private final int y1;

    private final int x2;

    private final int y2;

    private final Jugador jugador;

    public Linea(int x1, int y1, int x2, int y2, Jugador jugador) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.jugador = jugador;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public Jugador getJugador() {
        return jugador;
    }

}
