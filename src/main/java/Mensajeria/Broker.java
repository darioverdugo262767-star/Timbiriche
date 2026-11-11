/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mensajeria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author jesus
 */
public class Broker {

    private final ServicioMensajeria mensajeria;
    private final Thread hilo;
    private volatile boolean activo = true;

    private final Thread hiloVigilancia;

    private final Map<String, Long> ultimosAlive = new HashMap<>();

    private final long intervaloRevisionMs;
    private final long tiempoMaximoSinAliveMs;

    private boolean iniciado;

    public Broker(ServicioMensajeria mensajeria) {
        this(mensajeria, 1000, 10000);
    }

    public Broker(ServicioMensajeria mensajeria,
            long intervaloRevisionMs,
            long tiempoMaximoSinAliveMs) {

        if (intervaloRevisionMs <= 0
                || tiempoMaximoSinAliveMs <= 0) {

            throw new IllegalArgumentException(
                    "Los intervalos deben ser positivos"
            );
        }

        this.mensajeria = mensajeria;

        this.intervaloRevisionMs = intervaloRevisionMs;
        this.tiempoMaximoSinAliveMs = tiempoMaximoSinAliveMs;

        mensajeria.registrarDestino(
                mensajeria.getNombre(),
                mensajeria.getIpLocal(),
                mensajeria.getPuertoLocal()
        );

        hilo = new Thread(this::procesar, "mensajeria-broker");
        hilo.setDaemon(true);

        hiloVigilancia = new Thread(
                this::vigilarClientes,
                "mensajeria-broker-alive"
        );

        hiloVigilancia.setDaemon(true);
    }

    public synchronized void iniciar() {

        if (iniciado) {
            return;
        }

        iniciado = true;

        hilo.start();
        hiloVigilancia.start();
    }

    public void detener() {

        activo = false;

        hilo.interrupt();
        hiloVigilancia.interrupt();
    }

    public Map<String, DireccionFisica> obtenerDirectorioPrincipal() {
        return mensajeria.listarDestinos();
    }

    private void procesar() {
        while (activo) {
            try {
                Mensaje mensaje = mensajeria.recibirAdministrativo(500);
                if (mensaje == null) {
                    continue;
                }
                switch (mensaje.getTipo()) {
                    case REGISTRAR_BUZON:
                        try {
                            registrarCliente(mensaje.getRemitente(),
                                    mensaje.getIpRemitente(), mensaje.getPuertoRemitente());
                        } catch (IllegalArgumentException ex) {
                            // Puede responder a un nombre duplicado aunque no este registrado.
                            mensajeria.responderAdministrativo(mensaje.getRemitente(),
                                    TipoMensaje.REGISTRO_RECHAZADO, ex.getMessage(),
                                    mensaje.getIpRemitente(), mensaje.getPuertoRemitente());
                        }
                        break;
                    case DESACTIVAR_BUZON:
                        DireccionFisica actual = mensajeria.buscarDestino(mensaje.getRemitente());
                        if (actual != null
                                && actual.getIp().equals(mensaje.getIpRemitente())
                                && actual.getPuerto() == mensaje.getPuertoRemitente()) {
                            eliminarCliente(mensaje.getRemitente());
                        }
                        break;
                    case ALIVE:
                        registrarAlive(mensaje);
                        break;
                    default:
                        // Los demas tipos no requieren accion del broker.
                        break;
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            } catch (RuntimeException ex) {
                System.err.println("Broker: " + ex.getMessage());
            }
        }
    }

    public synchronized void registrarCliente(
            String nombre, String ip, int puerto) {

        if (nombre == null || nombre.isBlank()
                || ip == null || ip.isBlank()
                || nombre.contains("|")
                || nombre.contains("\n")
                || nombre.contains("\r")
                || ip.contains("|")
                || ip.contains("\n")
                || ip.contains("\r")
                || puerto < 1 || puerto > 65535) {

            throw new IllegalArgumentException(
                    "Datos de registro invalidos"
            );
        }

        if (nombre.equals(mensajeria.getNombre())) {
            throw new IllegalArgumentException(
                    "El nombre del broker esta reservado"
            );
        }

        DireccionFisica anterior
                = mensajeria.buscarDestino(nombre);

        if (anterior != null) {

            if (!anterior.getIp().equals(ip)
                    || anterior.getPuerto() != puerto) {

                throw new IllegalArgumentException(
                        "Nombre logico ya registrado: " + nombre
                );
            }

            ultimosAlive.put(nombre, System.nanoTime());

            enviarDirectorio(nombre);
            return;
        }

        mensajeria.registrarDestino(nombre, ip, puerto);

        ultimosAlive.put(nombre, System.nanoTime());

        enviarDirectorio(nombre);
        notificarNuevoCliente(nombre);
    }

    public synchronized void eliminarCliente(String nombre) {

        if (nombre.equals(mensajeria.getNombre())) {
            return;
        }

        if (mensajeria.buscarDestino(nombre) == null) {
            return;
        }

        mensajeria.eliminarDestino(nombre);

        ultimosAlive.remove(nombre);

        notificarDesconexion(nombre);
    }

    private synchronized void registrarAlive(Mensaje mensaje) {

        DireccionFisica registrada
                = mensajeria.buscarDestino(mensaje.getRemitente());

        if (registrada != null
                && registrada.getIp().equals(mensaje.getIpRemitente())
                && registrada.getPuerto() == mensaje.getPuertoRemitente()
                && ultimosAlive.containsKey(mensaje.getRemitente())) {

            ultimosAlive.put(
                    mensaje.getRemitente(),
                    System.nanoTime()
            );
        }
    }

    public void enviarDirectorio(String destinatario) {
        // Cada registro queda como "nombre|ip|puerto"; el puerto se convierte a String.
        StringBuilder contenido = new StringBuilder();
        for (Map.Entry<String, DireccionFisica> registro : mensajeria.listarDestinos().entrySet()) {
            if (contenido.length() > 0) {
                contenido.append('\n');
            }
            contenido.append(registro.getKey()).append('|')
                    .append(registro.getValue().getIp()).append('|')
                    .append(String.valueOf(registro.getValue().getPuerto()));
        }
        mensajeria.enviarAdministrativo(destinatario, TipoMensaje.DIRECTORIO_COMPLETO,
                contenido.toString());
    }

    public void notificarNuevoCliente(String nombre) {
        DireccionFisica direccion = mensajeria.buscarDestino(nombre);
        if (direccion == null) {
            return;
        }
        String contenido = nombre + "|" + direccion.getIp() + "|"
                + String.valueOf(direccion.getPuerto());
        for (String cliente : mensajeria.listarDestinos().keySet()) {
            if (!cliente.equals(nombre) && !cliente.equals(mensajeria.getNombre())) {
                mensajeria.enviarAdministrativo(cliente, TipoMensaje.CLIENTE_CONECTADO, contenido);
            }
        }
    }

    public void notificarDesconexion(String nombre) {
        for (String cliente : mensajeria.listarDestinos().keySet()) {
            if (!cliente.equals(mensajeria.getNombre())) {
                mensajeria.enviarAdministrativo(cliente,
                        TipoMensaje.CLIENTE_DESCONECTADO, nombre);
            }
        }
    }

    private void vigilarClientes() {

        while (activo) {
            try {
                Thread.sleep(intervaloRevisionMs);

                eliminarClientesInactivos();

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;

            } catch (RuntimeException ex) {
                System.err.println(
                        "Vigilancia ALIVE: " + ex.getMessage()
                );
            }
        }
    }

    private synchronized void eliminarClientesInactivos() {

        long ahora = System.nanoTime();

        List<String> inactivos = new ArrayList<>();

        for (Map.Entry<String, Long> registro
                : ultimosAlive.entrySet()) {

            long tiempoSinAlive = TimeUnit.NANOSECONDS.toMillis(
                    ahora - registro.getValue()
            );

            if (tiempoSinAlive >= tiempoMaximoSinAliveMs) {
                inactivos.add(registro.getKey());
            }
        }

        for (String nombre : inactivos) {

            System.out.println(
                    "Broker: cliente inactivo: " + nombre
            );

            eliminarCliente(nombre);
        }
    }

}
