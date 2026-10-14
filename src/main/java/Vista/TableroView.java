package Vista;

import controlador.EjercerTurnoControlador;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import modelo.Observador;
import presentacion.CuadroVista;
import presentacion.IConsultaModelo;
import presentacion.LineaVista;
import presentacion.PuntoVista;

/**
 * Vista del tablero que representa el modelo del tablero, 
 * aqui se desarrolla la partida.
 * Implementa el observer para redibujarse conforme 
 * de los movimientos de cada jugador.
 * @author Dario
 */
public final class TableroView extends JPanel implements Observador {

    /*
    Margen de los pixeles del panel. pendiente a cambios
    */
    private static final int MARGEN = 40;
    
    /*
    Diametro de los puntos de panel. pendiente a cambios
    */
    private static final int DIAMETRO_PUNTO = 12;
    
    /*
    Interfaz con los metodos para obtener el estado actual del modelo 
    */
    private IConsultaModelo consultaModelo;
    
    /*
    Controlador encargado de la logica del juego.
    */
    private EjercerTurnoControlador controlador;
    
    /*
    Jugador dueño de la ventana.
    */
    private String jugadorAsignado;

    /**
     * Contructor de la vista.
     */
    public TableroView() {
        setBackground(Color.BLACK);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickEnPunto(e.getX(), e.getY());
            }
        });
    }

    /**
     * Asigna al jugador a la vista.
     * @param jugador Asignado a la ventana.
     */
    public void setJugadorAsignado(String jugador) {
        jugadorAsignado = jugador;
    }
    
    /**
     * Referencia al controlador para la ejucucion de jugadas.
     * @param controlador instancia del controlador.
     */
    public void setControlador(EjercerTurnoControlador controlador) {
        this.controlador = controlador;
    }

     /**
     * Referencia el modelo.
     * @param modelo instancia del modelo.
     */
    public void setModelo(IConsultaModelo modelo) {
        consultaModelo = modelo;
    }

    /**
     * Captura y procesa un clic en pantalla sobre las coordenadas en píxeles.
     * Verifica que el modelo exista, la partida no esté finalizada y que sea el turno
     * del jugador local antes de traducir las coordenadas en píxeles a un punto
     * lógico del modelo y delegarlo al controlador.
     * @param pixelX Coordenada X del clic en píxeles.
     * @param pixelY Coordenada Y del clic en píxeles.
     */
    public void clickEnPunto(int pixelX, int pixelY) {
        if (consultaModelo == null || controlador == null || consultaModelo.estaFinalizada()) {
            return;
        }
        if (jugadorAsignado != null && !jugadorAsignado.equals(consultaModelo.getTurnoActual())) {
            return;
        }
        PuntoVista punto = consultaModelo.traducirClickACoordenadas(
                pixelX, pixelY, getWidth(), getHeight(), MARGEN);
        if (punto != null) {
            controlador.seleccionarPunto(punto.x(), punto.y());
        }
    }

    /**
     * Convierte las cordenadas del nodo a una ubicacion en pixeles.
     * @param x Cordenada logica en x.
     * @param y Cordenada logica en x.
     * @return objeto con la posicion en pixeles de la pantalla.
     */
    private Point posicion(int x, int y) {
        return consultaModelo.traducirModeloACoordenadasPantalla(
                x, y, getWidth(), getHeight(), MARGEN);
    }

    /**
     * Renderisa el estado del tablero, los puntos, y las jugadas.
     * @param g grafico para pintar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (consultaModelo == null || consultaModelo.getColumnasNodos() < 2 || getWidth() <= MARGEN * 2
                || getHeight() <= MARGEN * 2) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color colorTurno = consultaModelo.getColorTurno();
            int anchoPintable = getWidth() - MARGEN * 2;
            int altoPintable = getHeight() - MARGEN * 2;
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(MARGEN - 15, MARGEN - 15,
                    anchoPintable + 30, altoPintable + 30, 30, 30);

            for (CuadroVista cuadro : consultaModelo.getCuadros()) {
                Point superior = posicion(cuadro.x(), cuadro.y());
                Point inferior = posicion(cuadro.x() + 1, cuadro.y() + 1);
                int dx = inferior.x - superior.x;
                int dy = inferior.y - superior.y;
                Color c = cuadro.color();
                g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 120));
                g2.fillRect(superior.x, superior.y, dx, dy);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(10, Math.min(dx, dy) / 2)));
                String inicial = cuadro.dueno().substring(0, 1);
                FontMetrics metrica = g2.getFontMetrics();
                g2.drawString(inicial, superior.x + (dx - metrica.stringWidth(inicial)) / 2,
                        superior.y + (dy + metrica.getAscent() - metrica.getDescent()) / 2);
            }

            g2.setStroke(new BasicStroke(3.5f));
            for (LineaVista linea : consultaModelo.getLineas()) {
                Point a = posicion(linea.x1(), linea.y1());
                Point b = posicion(linea.x2(), linea.y2());
                g2.setColor(linea.color());
                g2.drawLine(a.x, a.y, b.x, b.y);
            }

            g2.setColor(Color.WHITE);
            for (PuntoVista nodo : consultaModelo.getNodos()) {
                Point p = posicion(nodo.x(), nodo.y());
                g2.fillOval(p.x - DIAMETRO_PUNTO / 2, p.y - DIAMETRO_PUNTO / 2,
                        DIAMETRO_PUNTO, DIAMETRO_PUNTO);
            }

            PuntoVista seleccionado = consultaModelo.getPuntoSeleccionado();
            if (seleccionado != null) {
                Point p = posicion(seleccionado.x(), seleccionado.y());
                g2.setColor(new Color(colorTurno.getRed(), colorTurno.getGreen(), colorTurno.getBlue(), 120));
                g2.fillOval(p.x - 14, p.y - 14, 28, 28);
                g2.setColor(colorTurno);
                g2.setStroke(new BasicStroke(3f));
                g2.drawOval(p.x - 10, p.y - 10, 20, 20);
                g2.setColor(Color.WHITE);
                g2.fillOval(p.x - 5, p.y - 5, 10, 10);

                for (PuntoVista vecino : consultaModelo.getAdyacentesDisponibles()) {
                    Point v = posicion(vecino.x(), vecino.y());
                    g2.setColor(new Color(colorTurno.getRed(), colorTurno.getGreen(), colorTurno.getBlue(), 150));
                    g2.fillOval(v.x - 9, v.y - 9, 18, 18);
                    g2.setColor(colorTurno);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawOval(v.x - 11, v.y - 11, 22, 22);
                }
            }
        } finally {
            g2.dispose();
        }
    }

    /**
     * Notificacion del patron observer, redibuja la vista.
     */
    @Override
    public void actualizar() {
        repaint();
    }
}
