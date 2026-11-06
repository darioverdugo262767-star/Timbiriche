package tcpip;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Dario
 */
public class ServidorTCP implements AutoCloseable{
    private ServerSocket servidor;

    public int escuchar(String ip, int puerto) throws IOException {
        servidor = new ServerSocket();
        servidor.bind(new InetSocketAddress(ip, puerto));
        return servidor.getLocalPort();
    }

    public Socket aceptarConexion() throws IOException {
        return servidor.accept();
    }

    public byte[] recibir(Socket socket) throws IOException {
        DataInputStream entrada = new DataInputStream(socket.getInputStream());
        int longitud = entrada.readInt();
        if (longitud < 0 || longitud > 1024 * 1024) {
            throw new IOException("Bloque demasiado grande o invalido");
        }
        byte[] datos = new byte[longitud];
        entrada.readFully(datos);
        return datos;
    }

    public void close() throws IOException {
        if (servidor != null) servidor.close();
    }
}
