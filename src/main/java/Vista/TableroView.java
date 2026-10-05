package Vista;

import controlador.Controlador;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import modelo.IConsultaModelo;
import modelo.Observador;

/**
 *
 * @author Dario
 */
public class TableroView extends javax.swing.JPanel implements Observador{

    private Controlador controlador;
    private IConsultaModelo modelo;

    private final int FILAS_NODOS = 10;
    private final int COLUMNAS_NODOS = 10;
    private final int MARGEN = 40;
    private final int DIAMETRO_PUNTO = 12;

    public TableroView() {
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int anchoPintable = getWidth() - (2 * MARGEN);
        int altoPintable = getHeight() - (2 * MARGEN);

        int pasoX = anchoPintable / (COLUMNAS_NODOS - 1);
        int pasoY = altoPintable / (FILAS_NODOS - 1);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(MARGEN - 15, MARGEN - 15, anchoPintable + 30, altoPintable + 30, 30, 30);

        for (int fila = 0; fila < FILAS_NODOS; fila++) {
            for (int col = 0; col < COLUMNAS_NODOS; col++) {
                int x = MARGEN + (col * pasoX) - (DIAMETRO_PUNTO / 2);
                int y = MARGEN + (fila * pasoY) - (DIAMETRO_PUNTO / 2);
                g2.fillOval(x, y, DIAMETRO_PUNTO, DIAMETRO_PUNTO);
            }
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

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public void setModelo(IConsultaModelo modelo) {
        this.modelo = modelo;
    }

    @Override
    public void actualizar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
