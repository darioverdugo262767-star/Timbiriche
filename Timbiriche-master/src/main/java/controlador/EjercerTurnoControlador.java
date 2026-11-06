package controlador;

import presentacion.IOperacionesModelo;

/**
 * El controlador solo conoce la interfaz de operaciones de Presentacion.
 */
public final class EjercerTurnoControlador {

    /**
     * Interfaz que con las operaciones de modificacion del modelo.
     */
    private IOperacionesModelo operacionesModelo;

    public EjercerTurnoControlador(IOperacionesModelo operacionesModelo) {
        this.operacionesModelo = operacionesModelo;
    }

    public void setOperacionesModelo(IOperacionesModelo operacionesModelo) {
        this.operacionesModelo = operacionesModelo;
    }

    public void seleccionarPunto(int x, int y) {
        if (operacionesModelo != null) {
            operacionesModelo.seleccionarPunto(x, y);
        }
    }

    public void realizarJugada(int x, int y) {
        if (operacionesModelo != null) {
            operacionesModelo.realizarJugada(x, y);
        }
    }
}
