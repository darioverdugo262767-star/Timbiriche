package modelo;

import java.awt.Color;

/**
 * Clase del modelo que representa a un jugador.
 * @author jesus
 */
public class Jugador {
    /*
    Nombre del jugador.
     */
    private String nombre;

    /*
    Color del jugador.
    */
    private Color color;

    /*
    Puntuacion del jugador.
    */
    private int puntuacion;

    /*
    Imagen del jugador.
    */
    private byte[] imagen;

    /**
     * Contructor del jugador.
     * @param nombre Nombre del jugador.
     * @param color Color del jugador.
     * @param imagen Imagen del jugador.
     */
    public Jugador(String nombre, Color color, byte[] imagen) {
        this.nombre = nombre;
        this.color = color;
        this.imagen = imagen;
        this.puntuacion = 0;
    }

    /**
     * Obtiene el nombre del jugador.
     * @return nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el color del jugador.
     * @return color del jugador.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Obtiene la imagen del jugador.
     * @return la imagen del jugador.
     */
    public byte[] getImagen() {
        return imagen;
    }

    /**
     * Obtiene la puntuacion del jugador.
     * @return puntuacion del jugador.
     */
    public int getPuntuacion() {
        return puntuacion;
    }

    /**
     * Actualiza la puntuacion del jugador.
     * @param puntuacion nuevo valor de la puntuacion.
     */
    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    /**
     * Asigna el nombre del jugador.
     * @param nombre nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Asigna el color del jugador.
     * @param color color a asignar.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Asigna la imagen del jugador.
     * @param imagen imagen a asignar.
     */
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

}
