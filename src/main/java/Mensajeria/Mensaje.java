package Mensajeria;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 *
 * @author Dario
 */
public class Mensaje {
    private final TipoMensaje tipo;
    private final String destinatario;
    private final String contenido;

    private String remitente;
    private String ipRemitente;
    private int puertoRemitente;
    private String ipDestinatario;
    private int puertoDestinatario;

    // Constructor utilizado por el cliente
    public Mensaje(String destinatario, String contenido) {
        this(TipoMensaje.NORMAL, destinatario, contenido);
    }

    /** Permite que Broker y GestorDirectorio envien mensajes administrativos. */
    public Mensaje(TipoMensaje tipo, String destinatario, String contenido) {
        this.tipo = java.util.Objects.requireNonNull(tipo, "tipo");
        this.destinatario = java.util.Objects.requireNonNull(destinatario, "destinatario");
        this.contenido = java.util.Objects.requireNonNull(contenido, "contenido");
    }

    // Constructor interno para reconstruir mensajes recibidos
    private Mensaje(TipoMensaje tipo, String remitente,
                    String destinatario, String ipRemitente,
                    int puertoRemitente, String ipDestinatario,
                    int puertoDestinatario, String contenido) {
        this.tipo = tipo;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.ipRemitente = ipRemitente;
        this.puertoRemitente = puertoRemitente;
        this.ipDestinatario = ipDestinatario;
        this.puertoDestinatario = puertoDestinatario;
        this.contenido = contenido;
    }

    // Solo accesible desde el paquete mensajeria
    void completar(String remitente, DireccionFisica local,
                   DireccionFisica destino) {
        this.remitente = remitente;
        this.ipRemitente = local.getIp();
        this.puertoRemitente = local.getPuerto();
        this.ipDestinatario = destino.getIp();
        this.puertoDestinatario = destino.getPuerto();
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getContenido() {
        return contenido;
    }

    TipoMensaje getTipo() {
        return tipo;
    }

    String getRemitente() {
        return remitente;
    }

    String getIpRemitente() {
        return ipRemitente;
    }

    int getPuertoRemitente() {
        return puertoRemitente;
    }

    String getIpDestinatario() {
        return ipDestinatario;
    }

    int getPuertoDestinatario() {
        return puertoDestinatario;
    }

    private static String codificar(String valor) {
        return Base64.getEncoder().encodeToString(
                valor.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodificar(String valor) {
        return new String(
                Base64.getDecoder().decode(valor),
                StandardCharsets.UTF_8);
    }

    String aTexto() {
        if (remitente == null || ipDestinatario == null) {
            throw new IllegalStateException("Mensaje incompleto");
        }

        return tipo.name() + "|" + codificar(remitente)
                + "|" + codificar(destinatario)
                + "|" + codificar(ipRemitente)
                + "|" + puertoRemitente
                + "|" + codificar(ipDestinatario)
                + "|" + puertoDestinatario
                + "|" + codificar(contenido);
    }

    static Mensaje desdeTexto(String texto) {
        String[] p = texto.split("\\|", -1);

        if (p.length != 8) {
            throw new IllegalArgumentException("Mensaje incompleto");
        }

        return new Mensaje(
                TipoMensaje.valueOf(p[0]),
                decodificar(p[1]),
                decodificar(p[2]),
                decodificar(p[3]),
                Integer.parseInt(p[4]),
                decodificar(p[5]),
                Integer.parseInt(p[6]),
                decodificar(p[7])
        );
    }
}
