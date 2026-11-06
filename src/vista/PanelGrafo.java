package vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import modelo.Grafo;

public class PanelGrafo extends JPanel {
    private static final long serialVersionUID = 1L;

    private Grafo grafo;
    private Set<Integer> conjuntoDominante;
    private Map<Integer, Point> posicionesVertices;
    private static final int RADIO_NODO = 20; 

    public PanelGrafo() {
        this.conjuntoDominante = new HashSet<>();
        this.posicionesVertices = new HashMap<>();
        setBackground(Color.WHITE);
    }

    public void actualizarGrafo(Grafo grafo, Set<Integer> conjuntoDominante) {
        this.grafo = grafo;
        this.conjuntoDominante = (conjuntoDominante != null) ? conjuntoDominante : new HashSet<>();
        calcularPosiciones();
        repaint();
    }

    private void calcularPosiciones() {
        posicionesVertices.clear();
        if (grafo == null || grafo.getCantVertices() == 0) return;

        int n = grafo.getCantVertices();
        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        
        if (centroX == 0) centroX = 250;
        if (centroY == 0) centroY = 250;

        int radioDistribucion = Math.min(centroX, centroY) - 50;
        if (radioDistribucion < 50) radioDistribucion = 100;

        // acomodar los vértices alrededor del círculo
        for (int i = 0; i < n; i++) {
            double angulo = 2 * Math.PI * i / n;
            int x = (int) (centroX + radioDistribucion * Math.cos(angulo));
            int y = (int) (centroY + radioDistribucion * Math.sin(angulo));
            posicionesVertices.put(i, new Point(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grafo == null || posicionesVertices.isEmpty()) {
            return;
        }

        // Si el panel cambió de tamaño, recalculamos las posiciones
        if (posicionesVertices.size() != grafo.getCantVertices()) {
            calcularPosiciones();
        }

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(2));

        int n = grafo.getCantVertices();
        for (int i = 0; i < n; i++) {
            Point p1 = posicionesVertices.get(i);
            if (p1 == null) continue;

            for (int vecino : grafo.getVecinos(i)) {
                // Dibujar aristas solo una vez
                if (i < vecino) {
                    Point p2 = posicionesVertices.get(vecino);
                    if (p2 != null) {
                        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }
        }

        g2.setFont(new Font("SansSerif", Font.BOLD, 13));

        for (int i = 0; i < n; i++) {
            Point p = posicionesVertices.get(i);
            if (p == null) continue;

            boolean esDominante = conjuntoDominante.contains(i);

            // Color: Rojo si pertenece al Conjunto Dominante
            Color colorRelleno = esDominante ? new Color(220, 53, 69) : new Color(0, 123, 255);
            Color colorBorde = esDominante ? new Color(139, 0, 0) : new Color(0, 56, 117);

            // Círculo
            g2.setColor(colorRelleno);
            g2.fillOval(p.x - RADIO_NODO, p.y - RADIO_NODO, 2 * RADIO_NODO, 2 * RADIO_NODO);

            g2.setColor(colorBorde);
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawOval(p.x - RADIO_NODO, p.y - RADIO_NODO, 2 * RADIO_NODO, 2 * RADIO_NODO);

            // Texto del Vértice (N�mero alineado al centro del círculo)
            String texto = String.valueOf(i);
            FontMetrics fm = g2.getFontMetrics();
            int textoX = p.x - fm.stringWidth(texto) / 2;
            int textoY = p.y + fm.getAscent() / 2 - 2;

            g2.setColor(Color.WHITE);
            g2.drawString(texto, textoX, textoY);
        }
    }
}