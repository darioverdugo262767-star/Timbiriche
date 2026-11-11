package Mensajeria;

import Libreria.ColaGenerica;

/**
 *
 * @author Dario
 */
public class Buzon {

    private final ColaGenerica<String> mensajes = new ColaGenerica<>();

    public void entregar(String contenido) {
        try {
            mensajes.agregar(contenido);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Se interrumpio la entrega al buzon", ex);
        }
    }

    public String recibir(long esperaMs) throws InterruptedException {
        return mensajes.tomar(esperaMs);
    }
}
