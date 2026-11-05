package Libreria;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Dario
 */
public class ColaGenerica<T> {
    private final BlockingQueue<T> cola = new LinkedBlockingQueue<>();

    public void agregar(T dato) throws InterruptedException {
        cola.put(dato);
    }

    public T tomar(long milisegundos) throws InterruptedException {
        return cola.poll(milisegundos, TimeUnit.MILLISECONDS);
    }
}
