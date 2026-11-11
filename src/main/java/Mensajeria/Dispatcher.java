package Mensajeria;

import Libreria.ColaGenerica;

/**
 *
 * @author Dario
 */
public class Dispatcher {
    private final String nombreLocal;
    private final DireccionFisica local;
    private final DirectorioLocal directorio;
    private final ColaGenerica<Mensaje> salida;

    public Dispatcher(String nombreLocal, DireccionFisica local,
                      DirectorioLocal directorio, ColaGenerica<Mensaje> salida) {
        this.nombreLocal = nombreLocal;
        this.local = local;
        this.directorio = directorio;
        this.salida = salida;
    }

    public void enviar(Mensaje mensaje) {
        DireccionFisica destino = directorio.buscar(mensaje.getDestinatario());
        if (destino == null) {
            throw new IllegalArgumentException("No existe el buzon: " + mensaje.getDestinatario());
        }
        enviarDirecto(mensaje, destino);
    }

    /** Solo para responder a un intento de registro que aun no esta en el directorio. */
    public void enviarDirecto(Mensaje mensaje, DireccionFisica destino) {
        if (destino == null) {
            throw new IllegalArgumentException("Direccion de destino no disponible");
        }
        mensaje.completar(nombreLocal, local, destino);
        try {
            salida.agregar(mensaje);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Se interrumpio el envio del mensaje", ex);
        }
    }
}
