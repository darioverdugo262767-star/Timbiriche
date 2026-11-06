package tcpip;

import Contratos.DatosRecibidosTCP;
import Contratos.ITransporteTCP;
import Libreria.ColaGenerica;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Dario
 */
public class TransporteTCP implements ITransporteTCP{
    private final ServidorTCP servidor = new ServidorTCP();
    private final ClienteTCP cliente = new ClienteTCP();
    private final ColaGenerica<DatosRecibidosTCP> cola = new ColaGenerica<>();
    private final Map<String, Socket> conexiones = new ConcurrentHashMap<>();
    private int siguienteId = 0;
    private volatile boolean activo;

    public synchronized int iniciar(String ipLocal, int puerto) throws IOException {
        if (activo) throw new IllegalStateException("Ya esta iniciado");
        int puertoAsignado = servidor.escuchar(ipLocal, puerto);
        activo = true;
        Thread hilo = new Thread(() -> {
            while (activo) {
                try {
                    registrar(servidor.aceptarConexion());
                } catch (IOException ex) {
                    if (activo) System.err.println("Error al aceptar: " + ex.getMessage());
                    break;
                }
            }
        }, "tcp-escucha");
        hilo.setDaemon(true);
        hilo.start();
        return puertoAsignado;
    }

    public String conectar(String ip, int puerto) throws IOException {
        if (!activo) throw new IllegalStateException("Primero llama a iniciar()");
        return registrar(cliente.conectar(ip, puerto));
    }

    private String registrar(Socket socket) {
        String id = generarId();
        conexiones.put(id, socket);
        Thread hilo = new Thread(() -> {
            try {
                while (activo) {
                    byte[] datos = servidor.recibir(socket);
                    cola.agregar(new DatosRecibidosTCP(id,
                            socket.getInetAddress().getHostAddress(), socket.getPort(), datos));
                }
            } catch (IOException ex) {
                // La conexion se cerro o fallo.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } finally {
                conexiones.remove(id);
                try {
                    socket.close(); 
                } catch (IOException ignorada) {
                
                }
            }
        }, "tcp-lector-" + id);
        hilo.setDaemon(true);
        hilo.start();
        return id;
    }

    public void enviar(String idConexion, byte[] datos) throws IOException {
        Socket socket = conexiones.get(idConexion);
        if (socket == null || socket.isClosed()) {
            throw new IOException("Conexion no disponible: " + idConexion);
        }
        cliente.enviar(socket, datos);
    }

    public DatosRecibidosTCP recibir(long esperaMs) throws InterruptedException {
        return cola.tomar(esperaMs);
    }

    public Map<String, String> obtenerConexiones() {
        Map<String, String> resultado = new HashMap<>();
        conexiones.forEach((id, socket) -> resultado.put(id,
                socket.getInetAddress().getHostAddress() + ":" + socket.getPort()));
        return resultado;
    }
    
    private synchronized String generarId() {
        siguienteId++;
        return "c" + siguienteId;
    }

    public synchronized void close() throws IOException {
        activo = false;
        servidor.close();
        for (Socket socket : conexiones.values()) {
            try {
                socket.close(); 
            } catch (IOException ignorada) {
            
            }
        }
        conexiones.clear();
    }
}
