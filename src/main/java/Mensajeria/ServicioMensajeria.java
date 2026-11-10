package Mensajeria;

import Contratos.IMensajeria;
import Contratos.ITransporteTCP;
import Libreria.ColaGenerica;
import java.io.IOException;

/**
 *
 * @author Dario
 */
public class ServicioMensajeria implements IMensajeria{
    private final String nombre;
    private final ITransporteTCP tcp;
    private final DirectorioLocal directorio = new DirectorioLocal();
    private final Buzon buzon = new Buzon();
    private final ColaGenerica<Mensaje> entrada = new ColaGenerica<>();
    private final ColaGenerica<Mensaje> salida = new ColaGenerica<>();
    private final Dispatcher dispatcher;
    private final Sender sender;
    private final Receptor receptor;
    private final Handler handler;
    private final int puertoLocal;

    public ServicioMensajeria(String nombre, String ip, int puerto, ITransporteTCP tcp)
            throws IOException {
        this.nombre = nombre;
        this.tcp = tcp;
        this.puertoLocal = tcp.iniciar(ip, puerto);
        DireccionFisica direccionLocal = new DireccionFisica(ip, puertoLocal);
        dispatcher = new Dispatcher(nombre, direccionLocal, directorio, salida);
        sender = new Sender(tcp, salida);
        receptor = new Receptor(tcp, entrada);
        handler = new Handler(nombre, entrada, buzon);
        sender.iniciar();
        receptor.iniciar();
        handler.iniciar();
    }

    public int getPuertoLocal() { return puertoLocal; }
    public String getNombre() { return nombre; }

    // Temporal: el Broker llenara el directorio automaticamente.
    public void registrarDestino(String nombre, String ip, int puerto) {
        directorio.registrar(nombre, ip, puerto);
    }

    @Override
    public void enviar(String destinatario, String contenido) {
        dispatcher.enviar(new Mensaje(destinatario, contenido));
    }

    @Override
    public String recibir(long esperaMs) throws InterruptedException {
        return buzon.recibir(esperaMs);
    }

    @Override
    public void close() throws IOException {
        sender.detener();
        receptor.detener();
        handler.detener();
        tcp.close();
    }
}
