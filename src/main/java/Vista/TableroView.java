package Vista;

import controlador.Controlador;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import modelo.IConsultaModelo;
import modelo.Jugador;
import modelo.Linea;
import modelo.Observador;

/**
 * Vista del tablero que representa el modelo del tablero, 
 * aqui se desarrolla la partida.
 * Implementa el observer para redibujarse conforme 
 * de los movimientos de cada jugador.
 * @author Dario
 */
public class TableroView extends javax.swing.JPanel implements Observador {

    /*
    Controlador encargado de la logica del juego.
    */
    private Controlador controlador;
    
    /*
    Interfaz con los metodos para obtener el estado actual del modelo 
    */
    private IConsultaModelo modelo;
    
    /*
    Jugador dueño de la ventana.
    */
    private Jugador jugadorAsignado;
    
    /*
    Margen de los pixeles del panel. pendiente a cambios
    */
    private final int MARGEN = 40;
    
    /*
    Diametro de los puntos de panel. pendiente a cambios
    */
    private final int DIAMETRO_PUNTO = 12;

    /**
     * Contructor de la vista
     */
    public TableroView() {
        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (modelo == null || controlador == null) return;

                // Bloqueo
                if (jugadorAsignado != null && !jugadorAsignado.equals(modelo.getTurnoActual())) {
                    return;
                }

                int colsNodos = modelo.getColumnasNodos();
                int filasNodos = modelo.getFilasNodos();
                int anchoPintable = getWidth() - (2 * MARGEN);
                int altoPintable = getHeight() - (2 * MARGEN);

                if (colsNodos <= 1 || filasNodos <= 1) return;

                int pasoX = anchoPintable / (colsNodos - 1);
                int pasoY = altoPintable / (filasNodos - 1);

                int col = Math.round((float) (e.getX() - MARGEN) / pasoX);
                int fila = Math.round((float) (e.getY() - MARGEN) / pasoY);

                if (col >= 0 && col < colsNodos && fila >= 0 && fila < filasNodos) {
                    int centroX = MARGEN + (col * pasoX);
                    int centroY = MARGEN + (fila * pasoY);

                    if (Math.hypot(e.getX() - centroX, e.getY() - centroY) <= 20) {
                        controlador.realizarJugada(col, fila);
                    }
                }
            }
        });
    }

    /**
     * Asigna al jugador a la vista.
     * @param jugador Asignado a la ventana.
     */
    public void setJugadorAsignado(Jugador jugador) {
        this.jugadorAsignado = jugador;
    }
    
    /**
     * Referencia al controlador para la ejucucion de jugadas.
     * @param controlador instancia del controlador.
     */
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Referencia el modelo.
     * @param modelo instancia del modelo.
     */
    public void setModelo(IConsultaModelo modelo) {
        this.modelo = modelo;
    }

    /**
     * Comprueva si ya hay una linea entre dos puntos.
     * @param x1 Coordenada X del primer punto.
     * @param y1 Coordenada Y del primer punto.
     * @param x2 Coordenada X del segundo punto.
     * @param y2 Coordenada Y del segundo punto.
     * @return devuelve si la linea existe o no.
     */
    private boolean existeLinea(int x1, int y1, int x2, int y2) {
        if (modelo == null || modelo.getLineas() == null) return false;
        for (Linea l : modelo.getLineas()) {
            if ((l.getX1() == x1 && l.getY1() == y1 && l.getX2() == x2 && l.getY2() == y2) ||
                (l.getX1() == x2 && l.getY1() == y2 && l.getX2() == x1 && l.getY2() == y1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Renderisa el estado del tablero, los puntos, y las jugadas.
     * @param g grafico para pintar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (modelo == null) return;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int colsNodos = modelo.getColumnasNodos();
        int filasNodos = modelo.getFilasNodos();

        if (colsNodos <= 1 || filasNodos <= 1) return;

        int anchoPintable = getWidth() - (2 * MARGEN);
        int altoPintable = getHeight() - (2 * MARGEN);

        int pasoX = anchoPintable / (colsNodos - 1);
        int pasoY = altoPintable / (filasNodos - 1);

        Jugador turnoActual = modelo.getTurnoActual();
        Color colorTurno = (turnoActual != null) ? turnoActual.getColor() : Color.YELLOW;

        //MARCO EXTERIOR
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(MARGEN - 15, MARGEN - 15, anchoPintable + 30, altoPintable + 30, 30, 30);

        //CUADROS COMPLETADOS
        Jugador[][] cuadros = modelo.getCuadros();
        if (cuadros != null) {
            for (int col = 0; col < cuadros.length; col++) {
                for (int fila = 0; fila < cuadros[col].length; fila++) {
                    Jugador dueno = cuadros[col][fila];
                    if (dueno != null) {
                        int sqX = MARGEN + (col * pasoX);
                        int sqY = MARGEN + (fila * pasoY);

                        Color colJugador = dueno.getColor();
                        g2.setColor(new Color(colJugador.getRed(), colJugador.getGreen(), colJugador.getBlue(), 120));
                        g2.fillRect(sqX, sqY, pasoX, pasoY);

                        int fontSize = Math.max(10, Math.min(pasoX, pasoY) / 2);
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
                        String inicial = String.valueOf(dueno.getNombre().charAt(0));
                        FontMetrics fm = g2.getFontMetrics();
                        int textX = sqX + (pasoX - fm.stringWidth(inicial)) / 2;
                        int textY = sqY + (pasoY + fm.getAscent() - fm.getDescent()) / 2;
                        g2.drawString(inicial, textX, textY);
                    }
                }
            }
        }

        //LINEAS TRAZADAS
        g2.setStroke(new BasicStroke(3.5f));
        for (Linea linea : modelo.getLineas()) {
            int x1 = MARGEN + (linea.getX1() * pasoX);
            int y1 = MARGEN + (linea.getY1() * pasoY);
            int x2 = MARGEN + (linea.getX2() * pasoX);
            int y2 = MARGEN + (linea.getY2() * pasoY);

            g2.setColor(linea.getJugador().getColor());
            g2.drawLine(x1, y1, x2, y2);
        }

        //PUNTOS DE LA GRILLA
        g2.setColor(Color.WHITE);
        for (int fila = 0; fila < filasNodos; fila++) {
            for (int col = 0; col < colsNodos; col++) {
                int x = MARGEN + (col * pasoX) - (DIAMETRO_PUNTO / 2);
                int y = MARGEN + (fila * pasoY) - (DIAMETRO_PUNTO / 2);
                g2.fillOval(x, y, DIAMETRO_PUNTO, DIAMETRO_PUNTO);
            }
        }

        //SELECCION DE PUNTO Y RESALTADO DE ADYACENTES DISPONIBLES
        Integer selX = modelo.getXPuntoSeleccionado();
        Integer selY = modelo.getYPuntoSeleccionado();
        if (selX != null && selY != null) {

            int px = MARGEN + (selX * pasoX);
            int py = MARGEN + (selY * pasoY);

            g2.setColor(new Color(colorTurno.getRed(), colorTurno.getGreen(), colorTurno.getBlue(), 120));
            g2.fillOval(px - 14, py - 14, 28, 28);

            g2.setColor(colorTurno);
            g2.setStroke(new BasicStroke(3f));
            g2.drawOval(px - 10, py - 10, 20, 20);

            g2.setColor(Color.WHITE);
            g2.fillOval(px - 5, py - 5, 10, 10);

            int[][] direcciones = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

            for (int[] dir : direcciones) {
                int nx = selX + dir[0];
                int ny = selY + dir[1];

                if (nx >= 0 && nx < colsNodos && ny >= 0 && ny < filasNodos) {
                    if (!existeLinea(selX, selY, nx, ny)) {
                        int adyX = MARGEN + (nx * pasoX);
                        int adyY = MARGEN + (ny * pasoY);

                        g2.setColor(new Color(colorTurno.getRed(), colorTurno.getGreen(), colorTurno.getBlue(), 150));
                        g2.fillOval(adyX - 9, adyY - 9, 18, 18);

                        g2.setColor(colorTurno);
                        g2.setStroke(new BasicStroke(2f));
                        g2.drawOval(adyX - 11, adyY - 11, 22, 22);
                    }
                }
            }
        }
    }

    /**
     * Notificacion del patron observer, redibuja la vista.
     */
    @Override
    public void actualizar() {
        repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
