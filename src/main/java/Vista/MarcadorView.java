package Vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import modelo.IConsultaModelo;
import modelo.Jugador;
import modelo.Observador;

/**
 * Vista de marcador que desplieaga informacion sobre el 
 * marcador/los cuadros que los jugadores han completado.
 * Implementa un observador para cambiar con las modificaciones 
 * que se hagan en el modelo.
 * Se adapta dinamicamente para partidas de 2 a 4 jugadores.
 * @author Dario
 */
public class MarcadorView extends JPanel implements Observador {

    /*
    Interfaz con los metodos para obtener el estado actual del modelo.
    */
    private IConsultaModelo modelo;
    /*
    Mapa que vincula al jugador con su tarjeta en el marcador.
    */
    private final Map<Jugador, TarjetaJugador> mapaTarjetas = new HashMap<>();

    /**
     * Contructor de la vista.
     */
    public MarcadorView() {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout(10, 10));
    }

    /**
     * Asigna el modelo a la vista.
     * @param modelo Instancia del modelo que implementa la interfaz.
     */
    public void setModelo(IConsultaModelo modelo) {
        this.modelo = modelo;
        inicializarInterfaz();
    }

    /**
     * Metodo que inicializa la interfaz dibujando las tarjetas de los 
     * jugadores dependiendo de cuando jugadores se encuentran y su informacion.
     */
    private void inicializarInterfaz() {
        if (modelo == null) return;

        List<Jugador> jugadores = modelo.getJugadores();
        if (jugadores == null || jugadores.isEmpty()) return;

        mapaTarjetas.clear();

        TarjetaJugador t1 = new TarjetaJugador(jugadores.get(0));
        mapaTarjetas.put(jugadores.get(0), t1);

        TarjetaJugador t2 = null;
        if (jugadores.size() >= 2) {
            t2 = new TarjetaJugador(jugadores.get(1));
            mapaTarjetas.put(jugadores.get(1), t2);
        }
        
        TarjetaJugador t3 = null;
        if (jugadores.size() >= 3) {
            t3 = new TarjetaJugador(jugadores.get(2));
            mapaTarjetas.put(jugadores.get(2), t3);
        }

        TarjetaJugador t4 = null;
        if (jugadores.size() >= 4) {
            t4 = new TarjetaJugador(jugadores.get(3));
            mapaTarjetas.put(jugadores.get(3), t4);
        }

        JComponent arribaIzquierda = t1;
        JComponent arribaDerecha = null;
        JComponent abajoIzquierda = null;
        JComponent abajoDerecha = null;
        int cantidadJugadores = jugadores.size();

        if (cantidadJugadores == 2) {
            abajoDerecha = t2;
        } else if (cantidadJugadores == 3) {
            arribaDerecha = t2;
            abajoIzquierda = t3;
        } else if (cantidadJugadores >= 4) {
            arribaDerecha = t2;
            abajoIzquierda = t3;
            abajoDerecha = t4;
        }

        JPanel panelNorte = crearFilaContainer(arribaIzquierda, crearBoton("🚪"), arribaDerecha);
        JPanel panelSur = crearFilaContainer(abajoIzquierda, crearBoton("🚩"), abajoDerecha);

        java.awt.Component centroActual = null;
        BorderLayout bl = (BorderLayout) getLayout();
        if (bl != null) {
            centroActual = bl.getLayoutComponent(BorderLayout.CENTER);
        }

        this.removeAll();
        add(panelNorte, BorderLayout.NORTH);
        add(panelSur, BorderLayout.SOUTH);

        if (centroActual != null) {
            add(centroActual, BorderLayout.CENTER);
        }

        revalidate();
        repaint();
    }

    /**
     * Crea un contenedor con filas para facilitar el acomomodo de componentes en la vista.
     * @param izq Contenedor izquierdo.
     * @param centro Contenedor centro.
     * @param der Contenedor derecho.
     * @return Contenedor con las 3 columnas.
     */
    private JPanel crearFilaContainer(JComponent izq, JComponent centro, JComponent der) {
        JPanel fila = new JPanel(new GridLayout(1, 3));
        fila.setOpaque(false);
        fila.setPreferredSize(new Dimension(700, 60));

        JPanel colIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        colIzq.setOpaque(false);
        if (izq != null) colIzq.add(izq);

        JPanel colCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        colCentro.setOpaque(false);
        if (centro != null) colCentro.add(centro);

        JPanel colDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        colDer.setOpaque(false);
        if (der != null) colDer.add(der);

        fila.add(colIzq);
        fila.add(colCentro);
        fila.add(colDer);

        return fila;
    }

    /**
     * Crea los botones secundarios.
     * @param icono El icono que tendra el boton. 
     * (Es string por que el emoji queda muy bien en el boton y no tenemos
     * que estar ajustando los tamaños de supuestas imagenes)
     * @return El boton creado.
     */
    private JButton crearBoton(String icono) {
        JButton btn = new JButton(icono) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 150, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 20));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(75, 40));
        return btn;
    }

    /**
     * Notificacion del patron observer, actualiza los datoos mostrados en cada 
     * una de las tarjetas, eso implica el recolorear su contenido.
     */
    @Override
    public void actualizar() {
        if (modelo == null) return;

        if (mapaTarjetas.isEmpty()) {
            inicializarInterfaz();
        }
        
        Jugador turnoActual = modelo.getTurnoActual();

        for (Map.Entry<Jugador, TarjetaJugador> entry : mapaTarjetas.entrySet()) {
            Jugador jugador = entry.getKey();
            TarjetaJugador tarjeta = entry.getValue();
            boolean esSuTurno = (turnoActual != null && turnoActual.equals(jugador));
            tarjeta.actualizarDatos(jugador.getPuntuacion(), esSuTurno);
        }
        repaint();
    }

    /**
     * Representacion visual de la tarjeta del jugador.
     */
    private static class TarjetaJugador extends JPanel {
        private final Jugador jugador;
        private final JLabel lblPuntaje;
        private boolean esSuTurno;

        /**
         * Contruccion de la tarjeta del jugador.
         * @param jugador Jugador asignado a la tarjeta.
         */
        public TarjetaJugador(Jugador jugador) {
            this.jugador = jugador;
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            setPreferredSize(new Dimension(180, 48));
            JPanel avatarPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            avatarPanel.setOpaque(false);
            avatarPanel.setPreferredSize(new Dimension(36, 36));
            avatarPanel.setLayout(new GridBagLayout());

            //Foto/Emoji temporal, se asignara imagen de jugador
            JLabel lblFoto = new JLabel("👤");
            lblFoto.setFont(new Font("SansSerif", Font.PLAIN, 18));
            avatarPanel.add(lblFoto);

            JLabel lblNombre = new JLabel(jugador.getNombre());
            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 17));
            lblNombre.setForeground(Color.BLACK);

            lblPuntaje = new JLabel(String.valueOf(jugador.getPuntuacion()));
            lblPuntaje.setFont(new Font("SansSerif", Font.BOLD, 22));
            lblPuntaje.setForeground(Color.WHITE);

            add(avatarPanel);
            add(lblNombre);
            add(lblPuntaje);
        }

        /**
         * Metodo para actualizar los datos de la tarjeta.
         * @param puntaje El puntaje del jugador.
         * @param esSuTurno Bandera para saber si es turno del jugador.
         */
        public void actualizarDatos(int puntaje, boolean esSuTurno) {
            this.lblPuntaje.setText(String.valueOf(puntaje));
            this.esSuTurno = esSuTurno;
            repaint();
        }

        /**
         * Renderiza el fondo redondeado con el color del jugador y un borde blanco si es su turno.
         * @param g grafico para pintar.
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(jugador.getColor());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 38, 38);
            // Borde brillante para resaltar el turno activo
            if (esSuTurno) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3.5f));
                g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 38, 38);
            }
            g2.dispose();
            super.paintComponent(g);
        }
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
