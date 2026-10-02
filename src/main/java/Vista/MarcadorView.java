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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Dario
 */
public class MarcadorView extends javax.swing.JPanel {

    public MarcadorView(int numJugadores) {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout(10, 10));
        
        JPanel tarjetaPepe  = crearTarjetaJugador("Pepe", "0", Color.RED);
        JPanel tarjetaJuan  = crearTarjetaJugador("Juan", "0", new Color(130, 230, 0));
        JPanel tarjetaJorge = crearTarjetaJugador("Jorge", "0", Color.YELLOW);
        JPanel tarjetaMaria = crearTarjetaJugador("Maria", "0", Color.BLUE);

        JPanel jugadorArribaDer  = (numJugadores >= 3) ? tarjetaJuan : null;
        JPanel jugadorAbajoIzq   = (numJugadores == 4) ? tarjetaJorge : null;

        JPanel panelNorte = crearFilaControl(tarjetaPepe, crearBotonControl("🚪"), jugadorArribaDer);
        JPanel panelSur   = crearFilaControl(jugadorAbajoIzq, crearBotonControl("🚩"), tarjetaMaria);

        add(panelNorte, BorderLayout.NORTH);
        add(panelSur, BorderLayout.SOUTH);
    }

    private JPanel crearFilaControl(JComponent izq, JComponent centro, JComponent der) {
        JPanel panelFila = new JPanel(new GridLayout(1, 3));
        panelFila.setOpaque(false);

        JPanel colIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        colIzq.setOpaque(false);
        if (izq != null) colIzq.add(izq);

        JPanel colCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        colCentro.setOpaque(false);
        if (centro != null) colCentro.add(centro);

        JPanel colDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        colDer.setOpaque(false);
        if (der != null) colDer.add(der);

        panelFila.add(colIzq);
        panelFila.add(colCentro);
        panelFila.add(colDer);

        return panelFila;
    }

    private JPanel crearTarjetaJugador(String nombre, String puntaje, Color colorFondo) {
        JPanel tarjeta = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(colorFondo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                super.paintComponent(g2);
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 5));

        // Espacio para la foto
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                g2.setColor(Color.DARK_GRAY);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                
                super.paintComponent(g2);
            }
        };
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(38, 38));
        avatarPanel.setLayout(new GridBagLayout());

        JLabel lblFoto = new JLabel("👤");
        lblFoto.setFont(new Font("SansSerif", Font.PLAIN, 18));
        avatarPanel.add(lblFoto);

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNombre.setForeground(Color.BLACK);

        JLabel lblPuntaje = new JLabel(puntaje);
        lblPuntaje.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblPuntaje.setForeground(Color.WHITE);

        tarjeta.add(avatarPanel);
        tarjeta.add(lblNombre);
        tarjeta.add(lblPuntaje);

        return tarjeta;
    }

    private JButton crearBotonControl(String icono) {
        JButton btn = new JButton(icono) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 165, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
            }
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 20));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(75, 40));
        return btn;
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
