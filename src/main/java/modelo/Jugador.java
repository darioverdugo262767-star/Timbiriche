/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.awt.Color;

/**
 *
 * @author jesus
 */
public class Jugador {

    private String nombre;

    private Color color;

    private int puntuacion;

    private byte[] imagen;

    public Jugador(String nombre, Color color, byte[] imagen) {
        this.nombre = nombre;
        this.color = color;
        this.imagen = imagen;
        this.puntuacion = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public Color getColor() {
        return color;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

}
