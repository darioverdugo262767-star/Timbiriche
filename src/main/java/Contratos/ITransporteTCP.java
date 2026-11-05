package Contratos;

import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Dario
 */
public interface ITransporteTCP extends AutoCloseable {
    
    int iniciar(String ipLocal, int puerto) throws IOException;
    
    String conectar(String ip, int puerto) throws IOException;
    
    void enviar(String idConexion, byte[] datos) throws IOException;
    
    DatosRecibidosTCP recibir(long esperaMs) throws InterruptedException;
    
    Map<String, String> obtenerConexiones();
    
    void close() throws IOException;
}
