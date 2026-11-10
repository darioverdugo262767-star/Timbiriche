package Mensajeria;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Dario
 */
public class DirectorioLocal {
    private final Map<String, DireccionFisica> direcciones = new ConcurrentHashMap<>();

    public void registrar(String nombre, String ip, int puerto) {
        direcciones.put(nombre, new DireccionFisica(ip, puerto));
    }

    public DireccionFisica buscar(String nombre) {
        return direcciones.get(nombre);
    }

    public void eliminar(String nombre) {
        direcciones.remove(nombre);
    }
}
