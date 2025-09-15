package UI.pruebas;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuHorizontalFlotante extends JFrame {

    public MenuHorizontalFlotante() {
        initUI();
    }

    private void initUI() {
        // Panel principal de contenido
        JPanel content = new JPanel();
        content.setBackground(new Color(236, 240, 241));
        content.add(new JLabel("Aquí va el contenido de la aplicación"));
        add(content, BorderLayout.CENTER);

        // Barra superior
        JPanel menuBar = new JPanel();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        menuBar.setBackground(new Color(45, 52, 54));

        // Categorías principales
        JLabel cat1 = crearCategoria("Categoría 1", new String[]{"Subcat 1.1", "Subcat 1.2"});
        JLabel cat2 = crearCategoria("Categoría 2", new String[]{"Subcat 2.1", "Subcat 2.2", "Subcat 2.3"});
        JLabel cat3 = crearCategoria("Categoría 3", new String[]{"Subcat 3.1", "Subcat 3.2"});

        menuBar.add(cat1);
        menuBar.add(cat2);
        menuBar.add(cat3);

        add(menuBar, BorderLayout.NORTH);

        // Config ventana
        setTitle("Menú Horizontal Flotante");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JLabel crearCategoria(String nombre, String[] subOpciones) {
        JLabel label = new JLabel(nombre);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Crear submenú flotante con JPopupMenu
        JPopupMenu subMenu = new JPopupMenu();
        subMenu.setBackground(new Color(0, 0, 0));
        subMenu.setBorder(new RoundedBorder(120, new Color(0, 0, 0))); // esquinas redondeadas

        for (String opcion : subOpciones) {
            JMenuItem item = new JMenuItem(opcion);
            item.setBackground(new Color(0, 0, 0));
            item.setForeground(Color.WHITE);
            item.setFont(new Font("Segoe UI", Font.PLAIN, 15));

            // Efecto hover
            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    item.setBackground(new Color(100, 181, 246));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    item.setBackground(new Color(85, 90, 95));
                }
            });

            subMenu.add(item);
        }

        // Mostrar submenú al pasar el cursor
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                subMenu.show(label, 0, label.getHeight());
            }
        });

        return label;
    }

    // Clase para bordes redondeados
    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color borderColor;

        RoundedBorder(int radius, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.drawRoundRect(x, y, width - 2, height - 2, radius, radius);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuHorizontalFlotante().setVisible(true);
        });
    }
}
