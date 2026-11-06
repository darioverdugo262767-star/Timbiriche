package tcpip;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author Dario
 */
public class ClienteTCP {
    
    public Socket conectar(String ip, int puerto) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(ip, puerto), 3000);
        return socket;
    }

    public void enviar(Socket socket, byte[] datos) throws IOException {
        if (datos.length > 1024 * 1024) throw new IOException("Bloque demasiado grande");
        synchronized (socket) {
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            salida.writeInt(datos.length);
            salida.write(datos);
            salida.flush();
        }
    }
}
