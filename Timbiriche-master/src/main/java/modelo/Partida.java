package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Partida: jugadores, turnos, tablero y cierre de juego.
 */
public final class Partida {

    private final List<Jugador> jugadores;
    private Jugador turnoActual;
    private final Tablero tablero;
    private boolean finalizada;

    public Partida(List<Jugador> jugadores, Tablero tablero) {
        if (jugadores == null || jugadores.size() < 2 || jugadores.size() > 4 || tablero == null) {
            throw new IllegalArgumentException("Se requieren entre 2 y 4 jugadores y un tablero");
        }
        this.jugadores = new ArrayList<>(jugadores);
        this.tablero = tablero;
        this.turnoActual = jugadores.get(0);
    }

    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    public Jugador getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(Jugador jugador) {
        if (!jugadores.contains(jugador)) {
            throw new IllegalArgumentException("Jugador ajeno a la partida");
        }
        turnoActual = jugador;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public boolean estaFinalizada() {
        return finalizada;
    }

    public void finalizarPartida() {
        finalizada = true;
    }

    public void sumarPuntos(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("Puntos negativos");
        }
        turnoActual.setPuntuacion(turnoActual.getPuntuacion() + puntos);
    }

    /**
     * En caso de empate se devuelve la lista de jugadores empatados.
     */
    public List<Jugador> determinarGanadores() {
        int maximo = Integer.MIN_VALUE;
        List<Jugador> ganadores = new ArrayList<>();
        for (Jugador jugador : jugadores) {
            if (jugador.getPuntuacion() > maximo) {
                ganadores.clear();
                maximo = jugador.getPuntuacion();
            }
            if (jugador.getPuntuacion() == maximo) {
                ganadores.add(jugador);
            }
        }
        return Collections.unmodifiableList(ganadores);
    }
}
