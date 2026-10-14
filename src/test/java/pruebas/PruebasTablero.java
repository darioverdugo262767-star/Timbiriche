package pruebas;

import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import modelo.ControlPartida;
import modelo.Cuadro;
import modelo.FachadaModeloPartida;
import modelo.Jugador;
import modelo.Linea;
import modelo.NodoArbolTablero;
import modelo.Partida;
import modelo.Tablero;
import presentacion.FachadaPresentacionPartida;
import presentacion.PuntoVista;

/** Ejecutar con java -ea -cp target/test-classes:target/classes pruebas.PruebasTablero */
public class PruebasTablero {
    public static void main(String[] args) {
        probarArbol();
        probarCuadroYSusTurnos();
        probarDobleCuadro();
        probarEntradaMVCYObserver();
        System.out.println("OK: arbol, adyacencias, turnos, puntajes, fin, MVC y Observer");
    }

    private static void probarArbol() {
        Tablero t = new Tablero(30, 30);
        assert t.getNodos().size() == 900;
        assert t.getLineas().size() == 1740;
        assert t.getCuadros().size() == 841;
        assert t.buscarNodo(-1, 2) == null;
        assert t.buscarNodo(30, 2) == null;
        assert t.buscarNodo(0, 0) != null;
        assert t.buscarNodo(29, 29) != null;
        assert t.getRaiz().getIzquierdo() != null;
        assert t.getRaiz().getDerecho() != null;
        for (NodoArbolTablero nodo : t.getNodos()) {
            assert t.buscarNodo(nodo.getX(), nodo.getY()) == nodo;
            assert nodo.getAdyacentes().size() >= 2 && nodo.getAdyacentes().size() <= 4;
            for (NodoArbolTablero v : nodo.getAdyacentes()) {
                assert v.esAdyacente(nodo);
                assert t.buscarLinea(nodo.getX(), nodo.getY(), v.getX(), v.getY()) != null;
            }
        }
        // La cuadricula tiene ciclos: no debe confundirse con las ramas del arbol.
        assert t.buscarLinea(0, 0, 1, 0) != null;
        assert t.buscarLinea(1, 0, 1, 1) != null;
        assert t.buscarLinea(1, 1, 0, 1) != null;
        assert t.buscarLinea(0, 1, 0, 0) != null;
    }

    private static List<Jugador> jugadores() {
        return List.of(new Jugador("A", Color.RED, null), new Jugador("B", Color.BLUE, null));
    }

    private static void probarCuadroYSusTurnos() {
        ControlPartida c = new ControlPartida();
        c.iniciarPartida(jugadores(), 2, 2);
        Partida p = c.getPartida();
        Tablero t = p.getTablero();
        Jugador a = p.getJugadores().get(0), b = p.getJugadores().get(1);
        assert !c.realizarJugada(0, 0, 2, 0); // No adyacentes
        assert p.getTurnoActual() == a;
        c.seleccionarPunto(-1, 0); // Fuera del tablero
        assert c.getxPuntoSeleccionado() == null;
        c.seleccionarPunto(0, 0);
        assert c.getxPuntoSeleccionado() == 0;
        c.seleccionarPunto(0, 0); // Cancela
        assert c.getxPuntoSeleccionado() == null;
        assert c.realizarJugada(0, 0, 1, 0);
        assert p.getTurnoActual() == b;
        assert !c.realizarJugada(1, 0, 0, 0); // Repetida
        assert p.getTurnoActual() == b;
        assert c.realizarJugada(1, 0, 1, 1);
        assert c.realizarJugada(1, 1, 0, 1);
        assert p.getTurnoActual() == b;
        assert c.realizarJugada(0, 1, 0, 0);
        assert b.getPuntuacion() == 1;
        assert a.getPuntuacion() == 0;
        assert p.getTurnoActual() == b; // Conserva turno al cerrar cuadro
        assert p.estaFinalizada() && !t.hayMovimientosDisponibles();
        assert p.determinarGanadores().equals(List.of(b));
        Cuadro cuadro = t.getCuadros().get(0);
        assert cuadro.estaCompleto() && cuadro.getDueno() == b;
        assert !c.realizarJugada(0, 0, 0, 1);
    }

    private static void probarDobleCuadro() {
        ControlPartida c = new ControlPartida();
        c.iniciarPartida(jugadores(), 3, 2);
        Partida partida = c.getPartida();
        int[][] trazos = {
            {0,0,1,0}, {1,0,2,0}, {0,1,1,1}, {1,1,2,1},
            {0,0,0,1}, {2,0,2,1}
        };
        for (int[] t : trazos) assert c.realizarJugada(t[0],t[1],t[2],t[3]);
        assert !partida.estaFinalizada();
        Jugador ultimo = partida.getTurnoActual();
        assert c.realizarJugada(1,0,1,1);
        assert ultimo.getPuntuacion() == 2;
        assert partida.getTablero().getCuadros().stream().allMatch(Cuadro::estaCompleto);
        assert partida.estaFinalizada();
    }

    private static void probarEntradaMVCYObserver() {
        FachadaModeloPartida modelo = new FachadaModeloPartida();
        FachadaPresentacionPartida presentacion = new FachadaPresentacionPartida(modelo, modelo);
        modelo.agregarObservador(presentacion);
        AtomicInteger notificaciones = new AtomicInteger();
        presentacion.agregarObservador(notificaciones::incrementAndGet);
        modelo.setPartida(List.of("Uno", "Dos"), List.of(Color.RED, Color.BLUE));
        assert notificaciones.get() == 1;
        assert presentacion.getNodos().size() == 100;
        Point centro = presentacion.traducirModeloACoordenadasPantalla(2, 3, 650, 650, 40);
        PuntoVista convertido = presentacion.traducirClickACoordenadas(centro.x, centro.y, 650, 650, 40);
        assert convertido.equals(new PuntoVista(2, 3));
        assert presentacion.traducirClickACoordenadas(0, 0, 650, 650, 40) == null;
        presentacion.seleccionarPunto(0, 0);
        assert notificaciones.get() == 2;
        assert presentacion.getAdyacentesDisponibles().size() == 2;
        presentacion.realizarJugada(1, 0);
        assert presentacion.getLineas().size() == 1;
        assert presentacion.getPuntoSeleccionado() == null;
        assert presentacion.getJugadores().get(1).turno();
        assert !presentacion.estaFinalizada();
    }
}
