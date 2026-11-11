package Mensajeria;

import Contratos.IMensajeria;
import Contratos.ITransporteTCP;
import Libreria.ColaGenerica;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Dario
 */
public class ServicioMensajeria implements IMensajeria {
    private final String nombre;
    private final ITransporteTCP tcp;
    private final DireccionFisica direccionLocal;
    private final DirectorioLocal directorio = new DirectorioLocal();
    private final Buzon buzon = new Buzon();
    private final ColaGenerica<Mensaje> entrada = new ColaGenerica<>();
    private final ColaGenerica<Mensaje> salida = new ColaGenerica<>();
    private final ColaGenerica<Mensaje> administrativos = new ColaGenerica<>();
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
        this.direccionLocal = new DireccionFisica(ip, puertoLocal);
        dispatcher = new Dispatcher(nombre, direccionLocal, directorio, salida);
        sender = new Sender(tcp, salida);
        receptor = new Receptor(tcp, entrada);
        handler = new Handler(nombre, entrada, buzon, administrativos);
        sender.iniciar();
        receptor.iniciar();
        handler.iniciar();
    }

    public int getPuertoLocal() { return puertoLocal; }
    public String getIpLocal() { return direccionLocal.getIp(); }
    public String getNombre() { return nombre; }

    public void registrarDestino(String nombre, String ip, int puerto) {
        directorio.registrar(nombre, ip, puerto);
    }

    public void eliminarDestino(String nombre) {
        directorio.eliminar(nombre);
    }

    public DireccionFisica buscarDestino(String nombre) {
        return directorio.buscar(nombre);
    }

    public Map<String, DireccionFisica> listarDestinos() {
        return directorio.listar();
    }

    public void reemplazarDestinos(Map<String, DireccionFisica> destinos) {
        directorio.reemplazar(destinos);
    }

    @Override
    public void enviar(String destinatario, String contenido) {
        dispatcher.enviar(new Mensaje(destinatario, contenido));
    }

    /** Mensajes de registro, altas, bajas y distribucion del directorio. */
    public void enviarAdministrativo(String destinatario, TipoMensaje tipo, String contenido) {
        if (tipo == TipoMensaje.NORMAL) {
            throw new IllegalArgumentException("Utiliza enviar para mensajes normales");
        }
        dispatcher.enviar(new Mensaje(tipo, destinatario, contenido));
    }

    /** Respuesta administrativa a una direccion no registrada (p. ej. duplicados). */
    void responderAdministrativo(String destinatario, TipoMensaje tipo, String contenido,
                                 String ip, int puerto) {
        dispatcher.enviarDirecto(new Mensaje(tipo, destinatario, contenido),
                                 new DireccionFisica(ip, puerto));
    }

    /** Consumo interno: no compite con el buzon de mensajes normales. */
    Mensaje recibirAdministrativo(long esperaMs) throws InterruptedException {
        return administrativos.tomar(esperaMs);
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
