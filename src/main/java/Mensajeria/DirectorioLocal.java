package Mensajeria;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Dario
 */
public class DirectorioLocal {
    private volatile Map<String, DireccionFisica> direcciones = Collections.emptyMap();

    public synchronized void registrar(String nombre, String ip, int puerto) {
        Objects.requireNonNull(nombre, "nombre");
        Objects.requireNonNull(ip, "ip");
        Map<String, DireccionFisica> copia = new LinkedHashMap<>(direcciones);
        copia.put(nombre, new DireccionFisica(ip, puerto));
        direcciones = Collections.unmodifiableMap(copia);
    }

    public DireccionFisica buscar(String nombre) {
        return direcciones.get(nombre);
    }

    public synchronized void eliminar(String nombre) {
        Map<String, DireccionFisica> copia = new LinkedHashMap<>(direcciones);
        copia.remove(nombre);
        direcciones = Collections.unmodifiableMap(copia);
    }

    /** Devuelve una vista inmutable y consistente del directorio. */
    public Map<String, DireccionFisica> listar() {
        return direcciones;
    }

    /** Se utiliza cuando el broker entrega el directorio completo. */
    public synchronized void reemplazar(Map<String, DireccionFisica> nuevoDirectorio) {
        Objects.requireNonNull(nuevoDirectorio, "nuevoDirectorio");
        direcciones = Collections.unmodifiableMap(new LinkedHashMap<>(nuevoDirectorio));
    }
}
