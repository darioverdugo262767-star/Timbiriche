package Mensajeria;

import Contratos.ITransporteTCP;
import Libreria.ColaGenerica;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dario
 */
public class Sender {
    private final ITransporteTCP tcp;
    private final ColaGenerica<Mensaje> salida;
    private final Map<String, String> conexiones = new HashMap<>();
    private final Thread hilo;
    private volatile boolean activo = true;

    public Sender(ITransporteTCP tcp, ColaGenerica<Mensaje> salida) {
        this.tcp = tcp;
        this.salida = salida;
        hilo = new Thread(this::procesar, "mensajeria-sender");
        hilo.setDaemon(true);
    }

    public void iniciar() { hilo.start(); }
    public void detener() { activo = false; hilo.interrupt(); }

    private void procesar() {
        while (activo) {
            try {
                Mensaje mensaje = salida.tomar(500);
                if (mensaje == null) continue;

                String destino = mensaje.getIpDestinatario() + ":" + mensaje.getPuertoDestinatario();
                String id = conexiones.get(destino);
                if (id == null) {
                    id = tcp.conectar(mensaje.getIpDestinatario(), mensaje.getPuertoDestinatario());
                    conexiones.put(destino, id);
                }
                try {
                    tcp.enviar(id, mensaje.aTexto().getBytes(StandardCharsets.UTF_8));
                } catch (IOException ex) {
                    conexiones.remove(destino); // Se reconectara en otro envio
                    throw ex;
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            } catch (IOException ex) {
                System.err.println("Error enviando mensaje: " + ex.getMessage());
            }
        }
    }
}
