package Mensajeria;

import Libreria.ColaGenerica;

/**
 *
 * @author Dario
 */
public class Handler {
    private final String nombreLocal;
    private final ColaGenerica<Mensaje> entrada;
    private final ColaGenerica<Mensaje> administrativos;
    private final Buzon buzon;
    private final Thread hilo;
    private volatile boolean activo = true;

    public Handler(String nombreLocal, ColaGenerica<Mensaje> entrada,
                   Buzon buzon, ColaGenerica<Mensaje> administrativos) {
        this.nombreLocal = nombreLocal;
        this.entrada = entrada;
        this.buzon = buzon;
        this.administrativos = administrativos;
        hilo = new Thread(this::procesar, "mensajeria-handler-" + nombreLocal);
        hilo.setDaemon(true);
    }

    public void iniciar() { hilo.start(); }

    public void detener() {
        activo = false;
        hilo.interrupt();
    }

    private void procesar() {
        while (activo) {
            try {
                Mensaje mensaje = entrada.tomar(500);
                if (mensaje == null) continue;
                if (!nombreLocal.equals(mensaje.getDestinatario())) continue;

                if (mensaje.getTipo() == TipoMensaje.NORMAL) {
                    // El Buzon conserva el String original, sin agregar "De ...".
                    buzon.entregar(mensaje.getContenido());
                } else {
                    // Broker o GestorDirectorio lo procesaran, no este Handler.
                    administrativos.agregar(mensaje);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
