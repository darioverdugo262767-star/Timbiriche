package pruebas;

import Contratos.ITransporteTCP;
import Contratos.DatosRecibidosTCP;
import tcpip.TransporteTCP;
import java.util.Scanner;

public class PruebaTCP {

    public static void main(String[] args) throws Exception {

        ITransporteTCP tcp = new TransporteTCP();
        Scanner teclado = new Scanner(System.in);

        int puerto = tcp.iniciar("127.0.0.1", 0);
        System.out.println("Puerto asignado: " + puerto);

        Thread receptor = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    DatosRecibidosTCP recibido = tcp.recibir(500);

                    if (recibido != null) {
                        System.out.println(
                            "\nRecibido de " + recibido.getIdConexion()
                            + ": " + new String(recibido.getDatos())
                        );
                    }
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        });

        receptor.setDaemon(true);
        receptor.start();

        try {
            while (true) {
                System.out.println("\n1. Conectar");
                System.out.println("2. Enviar mensaje");
                System.out.println("3. Ver conexiones");
                System.out.println("4. Salir");

                String opcion = teclado.nextLine();

                switch (opcion) {
                    case "1":
                        System.out.print("IP: ");
                        String ip = teclado.nextLine();

                        System.out.print("Puerto: ");
                        int destino = Integer.parseInt(teclado.nextLine());

                        String id = tcp.conectar(ip, destino);
                        System.out.println("Conexion creada: " + id);
                        break;

                    case "2":
                        System.out.print("ID de conexion: ");
                        String idConexion = teclado.nextLine();

                        System.out.print("Mensaje: ");
                        String mensaje = teclado.nextLine();

                        tcp.enviar(idConexion,mensaje.getBytes());
                        break;

                    case "3":
                        System.out.println(tcp.obtenerConexiones());
                        break;

                    case "4":
                        return;

                    default:
                        System.out.println("Opcion no valida");
                }
            }
        } finally {
            receptor.interrupt();
            tcp.close();
        }
    }
}

