/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mensajeria;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author jesus
 */
public class GestorDirectorio {

    private final ServicioMensajeria mensajeria;
    private final String nombreBroker;
    private final Thread hilo;
    private final CountDownLatch directorioInicial = new CountDownLatch(1);
    private volatile boolean activo = true;
    private volatile boolean iniciado;
    private volatile String errorRegistro;
    private final Thread hiloAlive;
    private final long intervaloAliveMs;
    private volatile boolean enviarAlive = true;

    public GestorDirectorio(ServicioMensajeria mensajeria,
            String nombreBroker, String ipBroker, int puertoBroker,
            long intervaloAliveMs) {

        if (intervaloAliveMs <= 0) {
            throw new IllegalArgumentException(
                    "El intervalo ALIVE debe ser positivo");
        }

        this.mensajeria = mensajeria;
        this.intervaloAliveMs = intervaloAliveMs;
        this.nombreBroker = nombreBroker;

        mensajeria.registrarDestino(nombreBroker, ipBroker, puertoBroker);

        hilo = new Thread(this::procesar,
                "gestor-directorio-" + mensajeria.getNombre());

        hilo.setDaemon(true);

        hiloAlive = new Thread(this::enviarLatidos,
                "mensajeria-alive-" + mensajeria.getNombre());

        hiloAlive.setDaemon(true);
    }

    public synchronized void iniciar() {
        if (iniciado) {
            return;
        }

        iniciado = true;

        hilo.start();
        hiloAlive.start();

        registrarConBroker();
    }

    public void registrarConBroker() {
        // Remitente, IP y puerto local viajan en el sobre Mensaje que ya existe.
        mensajeria.enviarAdministrativo(nombreBroker, TipoMensaje.REGISTRAR_BUZON, "");
    }

    public boolean esperarDirectorio(long tiempoMs) throws InterruptedException {
        return directorioInicial.await(tiempoMs, TimeUnit.MILLISECONDS)
                && errorRegistro == null;
    }

    public String getErrorRegistro() {
        return errorRegistro;
    }

    /**
     * Solicita la baja antes de cerrar ServicioMensajeria.
     */
    public void solicitarBaja() {
        enviarAlive = false;
        hiloAlive.interrupt();

        mensajeria.enviarAdministrativo(
                nombreBroker,
                TipoMensaje.DESACTIVAR_BUZON,
                ""
        );
    }

    public void detener() {
        activo = false;
        enviarAlive = false;

        hilo.interrupt();
        hiloAlive.interrupt();
    }

    private static void validarRegistro(String nombre, String ip, int puerto) {
        if (nombre.isBlank() || ip.isBlank() || puerto < 1 || puerto > 65535) {
            throw new IllegalArgumentException("Datos de directorio invalidos");
        }
    }

    private void procesar() {
        while (activo) {
            try {
                Mensaje mensaje = mensajeria.recibirAdministrativo(500);
                if (mensaje == null) {
                    continue;
                }
                if (!nombreBroker.equals(mensaje.getRemitente())) {
                    continue;
                }

                switch (mensaje.getTipo()) {
                    case DIRECTORIO_COMPLETO:
                        Map<String, DireccionFisica> nuevo = new LinkedHashMap<>();
                        if (!mensaje.getContenido().isEmpty()) {
                            for (String linea : mensaje.getContenido().split("\\n", -1)) {
                                String[] campos = linea.split("\\|", -1);
                                if (campos.length != 3) {
                                    throw new IllegalArgumentException("Registro incompleto");
                                }
                                int puerto = Integer.parseInt(campos[2]);
                                validarRegistro(campos[0], campos[1], puerto);
                                if (nuevo.put(campos[0], new DireccionFisica(campos[1], puerto)) != null) {
                                    throw new IllegalArgumentException("Nombre repetido en el directorio");
                                }
                            }
                        }
                        mensajeria.reemplazarDestinos(nuevo);
                        errorRegistro = null;
                        directorioInicial.countDown();
                        break;
                    case CLIENTE_CONECTADO:
                        String[] campos = mensaje.getContenido().split("\\|", -1);
                        if (campos.length != 3) {
                            throw new IllegalArgumentException("Registro incompleto");
                        }
                        int puerto = Integer.parseInt(campos[2]);
                        validarRegistro(campos[0], campos[1], puerto);
                        mensajeria.registrarDestino(campos[0], campos[1], puerto);
                        break;
                    case CLIENTE_DESCONECTADO:
                        mensajeria.eliminarDestino(mensaje.getContenido());
                        break;
                    case REGISTRO_RECHAZADO:
                        errorRegistro = mensaje.getContenido();
                        directorioInicial.countDown();
                        break;
                    default:
                        break;
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            } catch (IllegalArgumentException ex) {
                System.err.println("Directorio invalido: " + ex.getMessage());
            }
        }
    }

    private void enviarLatidos() {
        try {
            directorioInicial.await();

            if (errorRegistro != null) {
                return;
            }

            while (activo && enviarAlive) {
                try {
                    mensajeria.enviarAdministrativo(
                            nombreBroker,
                            TipoMensaje.ALIVE,
                            ""
                    );

                } catch (RuntimeException ex) {
                    System.err.println(
                            "No se pudo enviar ALIVE: " + ex.getMessage()
                    );
                }

                Thread.sleep(intervaloAliveMs);
            }

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
