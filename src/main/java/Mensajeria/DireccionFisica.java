package Mensajeria;

/**
 *
 * @author Dario
 */
public class DireccionFisica {
    private final String ip;
    private final int puerto;

    public DireccionFisica(String ip, int puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }

    public String getIp() {
        return ip; 
    }
    
    public int getPuerto() {
        return puerto; 
    }
}
