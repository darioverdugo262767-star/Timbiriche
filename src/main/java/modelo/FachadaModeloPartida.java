/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.LinkedList;

/**
 *
 * @author jesus
 */
public class FachadaModeloPartida extends SujetoObservado
        implements IConsultaModelo, IOperacionesModelo {

    private final ControlPartida ControlPartida;

    public FachadaModeloPartida() {
        this.ControlPartida = new ControlPartida();
    }

    @Override
    public LinkedList<Jugador> getJugadores() {

        return ControlPartida.getPartida().getJugadores();

    }

    @Override
    public void realizarJugada(int x, int y) {

        ControlPartida.realizarJugada(x, y);

        notificarObservadores();

    }

    @Override
    public void setPartida(Partida partida) {

        ControlPartida.setPartida(partida);

    }

    @Override
    public LinkedList<Linea> getLineas() {

        return ControlPartida.getPartida().getTablero().getLineas();

    }

}
