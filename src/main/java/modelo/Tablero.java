/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.LinkedList;

/**
 *
 * @author jesus
 */
public class Tablero {

    private final LinkedList<Linea> lineas;

    public Tablero(boolean[][] tablero, LinkedList<Linea> lineas) {

        this.lineas = lineas;
    }

    public LinkedList<Linea> getLineas() {
        return lineas;
    }

    public Linea crearLinea(int xPuntoSeleccionado, int yPuntoSeleccionado, int x, int y, Jugador turnoActual) {

        Linea nuevaLinea = new Linea(xPuntoSeleccionado, yPuntoSeleccionado, x, y, turnoActual);

        lineas.add(nuevaLinea);

        return nuevaLinea;

    }

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

    public int contarCuadrados(Linea nueva) {

        int cuadrados = 0;

        int x1 = nueva.getX1();
        int y1 = nueva.getY1();
        int x2 = nueva.getX2();
        int y2 = nueva.getY2();

        if (y1 == y2) {

            if (y1 > 0) {

                if (existeLinea(x1, y1 - 1, x1, y1)
                        && existeLinea(x2, y2 - 1, x2, y2)
                        && existeLinea(x1, y1 - 1, x2, y2 - 1)) {

                    cuadrados++;
                }
            }

            if (existeLinea(x1, y1, x1, y1 + 1)
                    && existeLinea(x2, y2, x2, y2 + 1)
                    && existeLinea(x1, y1 + 1, x2, y2 + 1)) {

                cuadrados++;
            }
        } else if (x1 == x2) {

            if (x1 > 0) {

                if (existeLinea(x1 - 1, y1, x1, y1)
                        && existeLinea(x2 - 1, y2, x2, y2)
                        && existeLinea(x1 - 1, y1, x2 - 1, y2)) {

                    cuadrados++;
                }
            }

            if (existeLinea(x1, y1, x1 + 1, y1)
                    && existeLinea(x2, y2, x2 + 1, y2)
                    && existeLinea(x1 + 1, y1, x2 + 1, y2)) {

                cuadrados++;
            }
        }

        return cuadrados;
    }

}
