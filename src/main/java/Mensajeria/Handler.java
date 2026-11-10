package Mensajeria;

import Libreria.ColaGenerica;

/**
 *
 * @author Dario
 */
public class Handler {
    private final String nombreLocal;
    private final ColaGenerica<Mensaje> entrada;
    private final Buzon buzon;
    private final Thread hilo;
    private volatile boolean activo = true;

    public Handler(String nombreLocal, ColaGenerica<Mensaje> entrada, Buzon buzon) {
        this.nombreLocal = nombreLocal;
        this.entrada = entrada;
        this.buzon = buzon;
        hilo = new Thread(this::procesar, "mensajeria-handler");
        hilo.setDaemon(true);
    }

    public void iniciar() {
        hilo.start(); 
    }
    
    public void detener() {
        activo = false; hilo.interrupt(); 
    }

    private void procesar() {
        while (activo) {
            try {
                Mensaje mensaje = entrada.tomar(500);
                if (mensaje == null) continue;
                if (!nombreLocal.equals(mensaje.getDestinatario())) continue;

                if (mensaje.getTipo() == TipoMensaje.NORMAL) {
                    buzon.entregar(mensaje.getRemitente(), mensaje.getContenido());
                } else {
                    // Pendiente: delegar al componente de administracion o PingAlive.
                    System.out.println("Tipo administrativo pendiente: " + mensaje.getTipo());
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
