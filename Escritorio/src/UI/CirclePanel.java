
package UI;

import java.awt.*;
import javax.swing.*;

public class CirclePanel extends JPanel {

    public CirclePanel() {
        setOpaque(false); // Transparente fuera del círculo
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Suavizar bordes
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Hacerlo cuadrado (para que siempre sea círculo)
        int size = Math.min(getWidth(), getHeight());

        // Fondo
        g2.setColor(getBackground());
        g2.fillOval(0, 0, size, size);

        // Borde (opcional)
        g2.setColor(getForeground());
        g2.drawOval(0, 0, size - 1, size - 1);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 100); // Tamaño inicial del círculo
    }
}
