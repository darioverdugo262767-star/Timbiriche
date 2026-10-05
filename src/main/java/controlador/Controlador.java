package controlador;

import modelo.IOperacionesModelo;

/**
 * Controlador principal del juego, intermediario entre la 
 * logica del modelo y la interfaz grafica.
 * @author jesus
 */
public class Controlador {

    /**
     * Interfaz que con las operaciones de modificacion del modelo.
     */
    private IOperacionesModelo modelo;

    /**
     * Contructor de la clase.
     */
    public Controlador() {
    }

    /**
     * Asigna la implementacion del modelo de operaciones. 
     * @param modelo instancia del modelo con las reglas del juego.
     */
    public void setModelo(IOperacionesModelo modelo) {
        this.modelo = modelo;
    }
    
    /**
     * Jugada realizada por el jugador en la interfaz.
     * @param x eje x donde se realizo la jugada.
     * @param y eje y donde se realizo la jugada.
     */
    public void realizarJugada(int x, int y){
        modelo.realizarJugada(x, y);
    }

}
