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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import presentacion.IConsultaModelo;
import presentacion.JugadorVista;
import modelo.Observador;

/**
 * Vista de marcador que despliega informacion sobre el marcador / los cuadros
 * que los jugadores han completado.
 * @author Dario
 */
public class MarcadorView extends JPanel implements Observador {

    /*
    Interfaz con los metodos para obtener el estado actual del modelo.
    */
    private IConsultaModelo modelo;
    
    /* 
    Lista simple para mantener las tarjetas en orden visual.
    */
    private final List<TarjetaJugador> listaTarjetas = new ArrayList<>();
    
    /*
    Jugador local de la vista.
    */
    private String jugadorLocal;

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
     * Asigna el jugador local a la vista.
     * @param jugadorLocal jugador perteneciente a la vista.
     */
    public void setJugadorLocal(String jugadorLocal) {
        this.jugadorLocal = jugadorLocal;
        if (modelo != null) {
            inicializarInterfaz();
        }
    }

    /**
     * Metodo que ordena siempre al jugador local primero en la lista.
     * @param jugadores lista de jugadores.
     * @return lista de jugadores ordenados.
     */
    private List<JugadorVista> ordenarConLocalPrimero(List<JugadorVista> jugadores) {
        int indiceLocal = -1;
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).nombre().equals(jugadorLocal)) {
                indiceLocal = i;
                break;
            }
        }
        if (indiceLocal <= 0) {
            return jugadores;
        }
        List<JugadorVista> ordenados = new ArrayList<>(jugadores.size());
        for (int i = 0; i < jugadores.size(); i++) {
            ordenados.add(jugadores.get((indiceLocal + i) % jugadores.size()));
        }
        return ordenados;
    }
    
     /**
     * Metodo que inicializa la interfaz dibujando las tarjetas de los 
     * jugadores dependiendo de cuando jugadores se encuentran y su informacion.
     */
    private void inicializarInterfaz() {
        if (modelo == null) {
            return;
        }

        List<JugadorVista> jugadores = modelo.getJugadores();
        if (jugadores == null || jugadores.isEmpty()) {
            return;
        }

        jugadores = ordenarConLocalPrimero(jugadores);
        
        listaTarjetas.clear();

        for (JugadorVista j : jugadores) {
            listaTarjetas.add(new TarjetaJugador(j));
        }

        TarjetaJugador t1 = listaTarjetas.get(0);
        TarjetaJugador t2 = (listaTarjetas.size() >= 2) ? listaTarjetas.get(1) : null;
        TarjetaJugador t3 = (listaTarjetas.size() >= 3) ? listaTarjetas.get(2) : null;
        TarjetaJugador t4 = (listaTarjetas.size() >= 4) ? listaTarjetas.get(3) : null;

        JComponent arribaIzquierda = t1;
        JComponent arribaDerecha = null;
        JComponent abajoIzquierda = null;
        JComponent abajoDerecha = null;
        int cantidadJugadores = listaTarjetas.size();

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
     * Notificación del Observer: recorre la lista de tarjetas de manera directa.
     */
    @Override
    public void actualizar() {
        if (modelo == null) {
            return;
        }

        if (listaTarjetas.isEmpty()) {
            inicializarInterfaz();
        }

        String turnoActual = modelo.getTurnoActual();
        List<JugadorVista> jugadoresModelo = modelo.getJugadores();

        for (TarjetaJugador tarjeta : listaTarjetas) {
            String nombreJugador = tarjeta.getNombreJugador();
            boolean esSuTurno = (turnoActual != null && turnoActual.equals(nombreJugador));
            
            int puntaje = 0;
            if (jugadoresModelo != null) {
                for (JugadorVista j : jugadoresModelo) {
                    if (j.nombre().equals(nombreJugador)) {
                        puntaje = j.puntuacion();
                        break;
                    }
                }
            }
            
            tarjeta.actualizarDatos(puntaje, esSuTurno);
        }
        repaint();
    }

    /**
     * Representación visual de la tarjeta del jugador.
     */
    private static class TarjetaJugador extends JPanel {

        private final JugadorVista jugador;
        private final JLabel lblPuntaje;
        private boolean esSuTurno;

        public TarjetaJugador(JugadorVista jugador) {
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

            JLabel lblFoto = new JLabel("👤");
            lblFoto.setFont(new Font("SansSerif", Font.PLAIN, 18));
            avatarPanel.add(lblFoto);

            JLabel lblNombre = new JLabel(jugador.nombre());
            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 17));
            lblNombre.setForeground(Color.BLACK);

            lblPuntaje = new JLabel(String.valueOf(jugador.puntuacion()));
            lblPuntaje.setFont(new Font("SansSerif", Font.BOLD, 22));
            lblPuntaje.setForeground(Color.WHITE);

            add(avatarPanel);
            add(lblNombre);
            add(lblPuntaje);
        }

        /**
         * Obtiene el nombre del jugador.
         * @return nombre del jugador.
         */
        public String getNombreJugador() {
            return jugador.nombre();
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
            g2.setColor(jugador.color());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 38, 38);

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
