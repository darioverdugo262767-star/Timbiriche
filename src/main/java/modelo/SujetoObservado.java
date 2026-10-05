package modelo;

import java.util.ArrayList;
import java.util.List;


/**
 * Clase abtracta con el rol de ser el sujeto obsrvado, 
 * se encarga de registrar los observadores interesados y notificarlos.
 * @author jesus
 */
public abstract class SujetoObservado {
    
    /*
    Lista de los observadores suscritos.
    */
    private final List<Observador> observadores;

    /*
    Lista interna de observadores.
    */
    public SujetoObservado() {
        observadores = new ArrayList<>();
    }
    
    /**
     * Agrega un observador a la lista de suscriptores.
     * @param observador 
     */
    public void agregarObservador(Observador observador) {
        observadores.add(observador);
    }

    /**
     * Remueve un observador de la lista de suscriptores.
     * @param observador 
     */
    public void eliminarObservador(Observador observador) {
        observadores.remove(observador);
    }

    /**
     * Notifica a los observadores subscritos que se deben actualizar.
     */
    public void notificarObservadores() {
        for (Observador observador : observadores) {
            observador.actualizar();
        }
    }
    
}
