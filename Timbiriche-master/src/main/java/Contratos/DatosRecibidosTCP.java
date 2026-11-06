package Contratos;

/**
 *
 * @author Dario
 */
public class DatosRecibidosTCP {
    private final String idConexion;
    private final String ipRemota;
    private final int puertoRemoto;
    private final byte[] datos;

    public DatosRecibidosTCP(String idConexion, String ipRemota, int puertoRemoto, byte[] datos) {
        this.idConexion = idConexion;
        this.ipRemota = ipRemota;
        this.puertoRemoto = puertoRemoto;
        this.datos = datos;
    }

    public String getIdConexion() {
        return idConexion; 
    }
    
    public String getIpRemota() {
        return ipRemota; 
    }
    
    public int getPuertoRemoto() {
        return puertoRemoto; 
    }
    
    public byte[] getDatos() {
        return datos; 
    }
    
}
