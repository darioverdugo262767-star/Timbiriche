package presentacion;

/**
 * Contrato de comandos para el controlador de la interfaz.
 */
public interface IOperacionesModelo {

    void seleccionarPunto(int x, int y);

    void realizarJugada(int x, int y);
}
