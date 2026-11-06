package pruebas;

import Contratos.DatosRecibidosTCP;
import Contratos.ITransporteTCP;
import Libreria.ColaGenerica;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import tcpip.TransporteTCP;

public class PruebasRed {
    private static final String LOCAL = "127.0.0.1";
    private static final long ESPERA = 2000;

    public static void main(String[] args) throws Exception {
        probarCola();
        probarCicloDeVidaDelTransporte();
        probarEnvioBidireccional();
        probarOrdenYContenido();
        probarMensajeVacioYGrande();
        probarLimiteDeBloque();
        probarErroresDeConexion();
        probarDesconexionDelPeer();
        probarCierreDelTransporte();
        System.out.println("OK: cola, ciclo de vida, envio, orden, limites, errores, desconexion y cierre");
    }

    private static void probarCola() throws Exception {
        ColaGenerica<String> cola = new ColaGenerica<>();
        assert cola.tomar(50) == null;
        cola.agregar("a");
        cola.agregar("b");
        assert "a".equals(cola.tomar(50));
        assert "b".equals(cola.tomar(50));
        assert cola.tomar(50) == null;
    }

    private static void probarCicloDeVidaDelTransporte() throws Exception {
        try (ITransporteTCP t = new TransporteTCP()) {
            try {
                t.conectar(LOCAL, 1);
                assert false : "conectar antes de iniciar debe fallar";
            } catch (IllegalStateException esperado) {
            }
            int puerto = t.iniciar(LOCAL, 0);
            assert puerto > 0;
            try {
                t.iniciar(LOCAL, 0);
                assert false : "iniciar dos veces debe fallar";
            } catch (IllegalStateException esperado) {
            }
            assert t.obtenerConexiones().isEmpty();
        }
    }

    private static void probarEnvioBidireccional() throws Exception {
        try (TransporteTCP a = new TransporteTCP(); TransporteTCP b = new TransporteTCP()) {
            int puertoA = a.iniciar(LOCAL, 0);
            b.iniciar(LOCAL, 0);

            String idEnB = b.conectar(LOCAL, puertoA);
            assert idEnB != null && !idEnB.isEmpty();

            b.enviar(idEnB, texto("jugada:3,4"));
            DatosRecibidosTCP enA = a.recibir(ESPERA);
            assert enA != null;
            assert "jugada:3,4".equals(leer(enA));
            assert LOCAL.equals(enA.getIpRemota());
            assert enA.getPuertoRemoto() > 0;
            assert enA.getIdConexion() != null;

            a.enviar(enA.getIdConexion(), texto("ok"));
            DatosRecibidosTCP enB = b.recibir(ESPERA);
            assert enB != null && "ok".equals(leer(enB));
            assert idEnB.equals(enB.getIdConexion());

            esperarConexiones(a, 1);
            assert a.obtenerConexiones().size() == 1;
            assert b.obtenerConexiones().containsKey(idEnB);
            assert b.obtenerConexiones().get(idEnB).equals(LOCAL + ":" + puertoA);
        }
    }

    private static void probarOrdenYContenido() throws Exception {
        try (TransporteTCP a = new TransporteTCP(); TransporteTCP b = new TransporteTCP()) {
            int puertoA = a.iniciar(LOCAL, 0);
            b.iniciar(LOCAL, 0);
            String id = b.conectar(LOCAL, puertoA);

            for (int i = 0; i < 50; i++) {
                b.enviar(id, texto("msg-" + i));
            }
            for (int i = 0; i < 50; i++) {
                DatosRecibidosTCP d = a.recibir(ESPERA);
                assert d != null && ("msg-" + i).equals(leer(d)) : "fuera de orden en " + i;
            }

            byte[] binario = {0, 1, 2, (byte) 0xFF, (byte) 0x80, 10, 13};
            b.enviar(id, binario);
            DatosRecibidosTCP d = a.recibir(ESPERA);
            assert d != null && Arrays.equals(binario, d.getDatos());
        }
    }

    private static void probarMensajeVacioYGrande() throws Exception {
        try (TransporteTCP a = new TransporteTCP(); TransporteTCP b = new TransporteTCP()) {
            int puertoA = a.iniciar(LOCAL, 0);
            b.iniciar(LOCAL, 0);
            String id = b.conectar(LOCAL, puertoA);

            b.enviar(id, new byte[0]);
            DatosRecibidosTCP vacio = a.recibir(ESPERA);
            assert vacio != null && vacio.getDatos().length == 0;

            byte[] grande = new byte[512 * 1024];
            for (int i = 0; i < grande.length; i++) {
                grande[i] = (byte) (i * 31);
            }
            b.enviar(id, grande);
            DatosRecibidosTCP recibido = a.recibir(5000);
            assert recibido != null && Arrays.equals(grande, recibido.getDatos());
        }
    }

    private static void probarLimiteDeBloque() throws Exception {
        try (TransporteTCP a = new TransporteTCP(); TransporteTCP b = new TransporteTCP()) {
            int puertoA = a.iniciar(LOCAL, 0);
            b.iniciar(LOCAL, 0);
            String id = b.conectar(LOCAL, puertoA);

            try {
                b.enviar(id, new byte[1024 * 1024 + 1]);
                assert false : "un bloque mayor a 1 MB debe rechazarse";
            } catch (IOException esperado) {
            }
            b.enviar(id, texto("sigue viva"));
            DatosRecibidosTCP d = a.recibir(ESPERA);
            assert d != null && "sigue viva".equals(leer(d));

            b.enviar(id, new byte[1024 * 1024]);
            DatosRecibidosTCP limite = a.recibir(5000);
            assert limite != null && limite.getDatos().length == 1024 * 1024;
        }
    }

    private static void probarErroresDeConexion() throws Exception {
        try (TransporteTCP t = new TransporteTCP()) {
            t.iniciar(LOCAL, 0);
            try {
                t.enviar("c999", texto("x"));
                assert false : "enviar a una conexion inexistente debe fallar";
            } catch (IOException esperado) {
            }
            int puertoCerrado;
            try (ServerSocket libre = new ServerSocket(0)) {
                puertoCerrado = libre.getLocalPort();
            }
            try {
                t.conectar(LOCAL, puertoCerrado);
                assert false : "conectar a un puerto cerrado debe fallar";
            } catch (IOException esperado) {
            }
            assert t.obtenerConexiones().isEmpty();
        }
    }

    private static void probarDesconexionDelPeer() throws Exception {
        TransporteTCP a = new TransporteTCP();
        TransporteTCP b = new TransporteTCP();
        try {
            int puertoA = a.iniciar(LOCAL, 0);
            b.iniciar(LOCAL, 0);
            String idEnB = b.conectar(LOCAL, puertoA);
            b.enviar(idEnB, texto("hola"));
            DatosRecibidosTCP d = a.recibir(ESPERA);
            assert d != null;
            String idEnA = d.getIdConexion();
            assert a.obtenerConexiones().containsKey(idEnA);

            b.close();

            esperarConexiones(a, 0);
            assert a.obtenerConexiones().isEmpty() : "A debe quitar la conexion caida";
            try {
                a.enviar(idEnA, texto("sigues ahi?"));
                assert false : "enviar a un peer caido debe fallar";
            } catch (IOException esperado) {
            }
        } finally {
            a.close();
            b.close();
        }
    }

    private static void probarCierreDelTransporte() throws Exception {
        TransporteTCP a = new TransporteTCP();
        TransporteTCP b = new TransporteTCP();
        int puertoA = a.iniciar(LOCAL, 0);
        b.iniciar(LOCAL, 0);
        String id = b.conectar(LOCAL, puertoA);
        esperarConexiones(a, 1);

        a.close();
        assert a.obtenerConexiones().isEmpty();
        a.close();

        esperarConexiones(b, 0);
        assert b.obtenerConexiones().isEmpty() : "B debe enterarse de que A cerro";
        try {
            b.enviar(id, texto("x"));
            assert false;
        } catch (IOException esperado) {
        }
        b.close();
    }

    private static byte[] texto(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    private static String leer(DatosRecibidosTCP d) {
        return new String(d.getDatos(), StandardCharsets.UTF_8);
    }

    private static void esperarConexiones(TransporteTCP t, int cantidad) throws InterruptedException {
        long limite = System.currentTimeMillis() + ESPERA;
        while (t.obtenerConexiones().size() != cantidad && System.currentTimeMillis() < limite) {
            Thread.sleep(20);
        }
    }
}
