package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import modelo.Observador;
import presentacion.IConsultaModelo;
import presentacion.JugadorVista;

/**
 * Vista encargada de presentar los resultados finales al agotarse todas las lineas
 * TODO tambien sale cuando un jugador se rinde o se sale.
 */
public final class ScoreView extends JPanel implements Observador {

    /*
    Interfaz con los metodos para obtener el estado actual del modelo.
    */
    private IConsultaModelo consultaModelo;
    
    /*
    Ventana desplegable para presentar la vista.
    */
    private JFrame ventana;
    
    /*
    Bandera que dice que estado tiene la vista.
    */
    private boolean mostrada;

    /**
     * Contructor de la vista.
     */
    public ScoreView() {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout(12, 18));
    }

    /**
     * Asigna el modelo a la vista.
     * @param modelo Instancia del modelo que implementa la interfaz.
     */
    public void setModelo(IConsultaModelo modelo) {
        this.consultaModelo = modelo;
    }

    /**
     * Notificación del Observer: recorre la lista de tarjetas de manera directa.
     */
    @Override
    public void actualizar() {
        if (consultaModelo == null || !consultaModelo.estaFinalizada() || mostrada) {
            return;
        }
        mostrada = true;
        invocarPantalla();
    }

    /**
     * Construye dinamicamente los componentes visuales con los puntajes de los
     * jugadores, asigna sus colores representativos y despliega la ventana
     * contenedora con el ganador de la partida.
     */
    public void invocarPantalla() {
        removeAll();
        JLabel titulo = new JLabel(consultaModelo.obtenerResultado(), SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        add(titulo, BorderLayout.NORTH);
        JPanel resultados = new JPanel(new GridLayout(0, 1, 6, 6));
        resultados.setBackground(Color.BLACK);
        for (JugadorVista jugador : consultaModelo.getJugadores()) {
            JLabel fila = new JLabel(jugador.nombre() + "  —  " + jugador.puntuacion() + " puntos", SwingConstants.CENTER);
            fila.setForeground(jugador.color());
            fila.setFont(new Font("SansSerif", Font.BOLD, 22));
            resultados.add(fila);
        }
        add(resultados, BorderLayout.CENTER);
        revalidate();
        repaint();
        if (ventana == null) {
            ventana = new JFrame("Timbiriche - Resultado final");
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setSize(450, 300);
            ventana.setLocationRelativeTo(null);
            ventana.add(this);
        }
        ventana.setVisible(true);
    }
}
