package presentacion;

import java.awt.Color;
import java.awt.Point;
import java.util.List;
import modelo.Observador;
import modelo.SujetoObservado;

/**
 * Puente entre Vista/Controlador y Modelo. Reenvia las notificaciones Observer
 * y esconde las entidades del dominio tras objetos de vista inmutables.
 */
public final class FachadaPresentacionPartida extends SujetoObservado
        implements IConsultaModelo, IOperacionesModelo, Observador {

    private final ControlPresentacionPartida control;

    public FachadaPresentacionPartida(modelo.IConsultaModelo consultas,
            modelo.IOperacionesModelo operaciones) {
        control = new ControlPresentacionPartida(consultas, operaciones);
    }

    @Override
    public void actualizar() {
        notificarObservadores();
    }

    @Override
    public void seleccionarPunto(int x, int y) {
        control.seleccionarPunto(x, y);
    }

    @Override
    public void realizarJugada(int x, int y) {
        control.realizarJugada(x, y);
    }

    @Override
    public int getColumnasNodos() {
        return control.getColumnasNodos();
    }

    @Override
    public int getFilasNodos() {
        return control.getFilasNodos();
    }

    @Override
    public List<PuntoVista> getNodos() {
        return control.obtenerVistaTablero();
    }

    @Override
    public List<JugadorVista> getJugadores() {
        return control.obtenerVistaMarcador();
    }

    @Override
    public List<LineaVista> getLineas() {
        return control.obtenerLineasOcupadas();
    }

    @Override
    public List<CuadroVista> getCuadros() {
        return control.obtenerCuadrosConDueno();
    }

    @Override
    public String getTurnoActual() {
        return control.getTurnoActual();
    }

    @Override
    public Color getColorTurno() {
        return control.getColorTurno();
    }

    @Override
    public PuntoVista getPuntoSeleccionado() {
        return control.obtenerSeleccion();
    }

    @Override
    public List<PuntoVista> getAdyacentesDisponibles() {
        return control.obtenerAdyacentesDisponibles();
    }

    @Override
    public PuntoVista traducirClickACoordenadas(int px, int py, int ancho, int alto, int margen) {
        return control.traducirClickACoordenadas(px, py, ancho, alto, margen);
    }

    @Override
    public Point traducirModeloACoordenadasPantalla(int x, int y, int ancho, int alto, int margen) {
        return control.traducirModeloACoordenadasPantalla(x, y, ancho, alto, margen);
    }

    @Override
    public boolean estaFinalizada() {
        return control.estaFinalizada();
    }

    @Override
    public String obtenerResultado() {
        return control.obtenerResultado();
    }
}
