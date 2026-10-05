/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author amacias
 */
public abstract class SujetoObservado {
    private final List<Observador> observadores;

    public SujetoObservado() {
        observadores = new ArrayList<>();
    }
    
   public void agregarObservador(Observador observador) {
        observadores.add(observador);
    }

    public void eliminarObservador(Observador observador) {
        observadores.remove(observador);
    }

    public void notificarObservadores() {

        for (Observador observador : observadores) {
            observador.actualizar();
        }
    }
}
