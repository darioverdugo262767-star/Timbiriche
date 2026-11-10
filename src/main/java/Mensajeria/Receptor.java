/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mensajeria;

import Contratos.DatosRecibidosTCP;
import Contratos.ITransporteTCP;
import Libreria.ColaGenerica;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Dario
 */
public class Receptor {
    private final ITransporteTCP tcp;
    private final ColaGenerica<Mensaje> entrada;
    private final Thread hilo;
    private volatile boolean activo = true;

    public Receptor(ITransporteTCP tcp, ColaGenerica<Mensaje> entrada) {
        this.tcp = tcp;
        this.entrada = entrada;
        hilo = new Thread(this::procesar, "mensajeria-receptor");
        hilo.setDaemon(true);
    }

    public void iniciar() {
        hilo.start(); 
    }
    
    public void detener() {
        activo = false; hilo.interrupt(); 
    }

    private void procesar() {
        while (activo) {
            try {
                DatosRecibidosTCP datos = tcp.recibir(500);
                if (datos == null) continue;
                String texto = new String(datos.getDatos(), StandardCharsets.UTF_8);
                entrada.agregar(Mensaje.desdeTexto(texto));
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            } catch (IllegalArgumentException ex) {
                System.err.println("Mensaje invalido: " + ex.getMessage());
            }
        }
    }
}
