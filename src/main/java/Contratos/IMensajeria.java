package Contratos;

import java.io.IOException;

/**
 *
 * @author Dario
 */
public interface IMensajeria extends AutoCloseable{
    
    void enviar(String destinatario, String contenido);

    String recibir(long esperaMs) throws InterruptedException;

    @Override
    void close() throws IOException;
}
