/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;

import UI.Conexion;
import UI.util.ModernStyles;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author calam
 */
public class Recuperacion extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Recuperacion.class.getName());
    
    // Colores consistentes con el resto de la aplicación
    private static final Color COLOR_PRIMARY = new Color(107, 45, 77); // #6B2D4D
    private static final Color COLOR_SECONDARY = new Color(139, 74, 107); // #8B4A6B
    private static final Color COLOR_ACCENT = new Color(168, 85, 122); // #A8557A
    private static final Color COLOR_TEXT_PRIMARY = new Color(26, 26, 26); // #1a1a1a
    private static final Color COLOR_TEXT_SECONDARY = new Color(102, 102, 102); // #666666
    private static final Color COLOR_TEXT_LIGHT = new Color(255, 255, 255); // #FFFFFF
    private static final Color COLOR_BG_LIGHT = new Color(250, 246, 249); // #FAF6F9
    private static final Color COLOR_BG_DARK = new Color(91, 29, 61); // #5B1D3D
    private static final Color COLOR_BG_GRADIENT_START = new Color(255, 245, 252); // Rosa muy claro
    private static final Color COLOR_BG_GRADIENT_END = new Color(250, 246, 249); // #FAF6F9
    
    // Componentes
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField txtCorreo;
    private JButton btnEnviar;
    private JLabel lblBack;
    private JLabel NombreApp;
    private JButton btnCerrar;
    private String nombreApp = "INVOICE DAY";
    private String correoApp = "";
    
    /**
     * Creates new form Recuperacion
     */
    public Recuperacion(java.awt.Window parent) {
        super(parent, parent == null ? java.awt.Dialog.ModalityType.MODELESS : java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        aplicarEstilosModernos();
        cargarComplementos();
        if (parent != null) {
            setLocationRelativeTo(parent);
        } else {
            setLocationRelativeTo(null);
        }
    }
    
    // Constructor con Frame para compatibilidad
    public Recuperacion(java.awt.Frame parent) {
        super(parent, true); // Modal dialog
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        aplicarEstilosModernos();
        cargarComplementos();
        if (parent != null) {
            setLocationRelativeTo(parent);
        } else {
            setLocationRelativeTo(null);
        }
    }
    
    // Constructor sin parámetros para compatibilidad
    public Recuperacion() {
        this((java.awt.Window) null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true); // Sin decoración del sistema
        setTitle("Recuperación de Cuenta");
        setSize(1200, 700);
        setResizable(false);
        
        // Panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG_LIGHT);
        
        // Panel izquierdo (decorativo)
        leftPanel = crearPanelIzquierdo();
        mainPanel.add(leftPanel, BorderLayout.WEST);
        
        // Panel derecho (formulario)
        rightPanel = crearPanelDerecho();
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        // Crear JLayeredPane para superponer el botón cerrar
        javax.swing.JLayeredPane layeredPane = new javax.swing.JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1200, 700));
        
        // Agregar mainPanel en la capa inferior
        mainPanel.setBounds(0, 0, 1200, 700);
        layeredPane.add(mainPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        // Crear y agregar botón cerrar en la capa superior
        crearBotonCerrar(layeredPane);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(layeredPane, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel izquierdo decorativo
     */
    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente de fondo
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_BG_GRADIENT_START,
                    0, getHeight(), COLOR_BG_GRADIENT_END
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Círculos decorativos
                g2d.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 10));
                g2d.fillOval(-100, 50, 200, 200);
                g2d.fillOval(200, 300, 150, 150);
                g2d.fillOval(-50, 450, 180, 180);
                
                g2d.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(500, 700));
        panel.setLayout(new BorderLayout());
        
        // Contenido del panel izquierdo
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(60, 60, 60, 60));
        
        // Nombre de la app
        NombreApp = new JLabel(nombreApp);
        NombreApp.setFont(new Font("Playfair Display", Font.BOLD, 56));
        NombreApp.setForeground(COLOR_PRIMARY);
        NombreApp.setAlignmentX(0.0f);
        
        // Título principal
        JLabel titleLabel = new JLabel("Recupera tu Cuenta");
        titleLabel.setFont(new Font("Playfair Display", Font.BOLD, 42));
        titleLabel.setForeground(COLOR_PRIMARY);
        titleLabel.setAlignmentX(0.0f);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("<html><div style='text-align: left; width: 350px;'>" +
            "Ingresa tu correo electrónico y te enviaremos las instrucciones para recuperar tu cuenta." +
            "</div></html>");
        subtitleLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        subtitleLabel.setForeground(COLOR_TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(0.0f);
        
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(NombreApp);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalGlue());
        
        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setOpaque(false);
        JLabel copyrightLabel = new JLabel("© 2025 " + nombreApp + ". Todos los derechos reservados.");
        copyrightLabel.setFont(new Font("Poppins", Font.PLAIN, 12));
        copyrightLabel.setForeground(COLOR_TEXT_SECONDARY);
        footerPanel.add(copyrightLabel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(footerPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Crea el panel derecho con el formulario
     */
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(80, 80, 80, 80));
        
        // Título del formulario
        JLabel titleLabel = new JLabel("Recuperación de Cuenta");
        titleLabel.setFont(new Font("Playfair Display", Font.BOLD, 32));
        titleLabel.setForeground(COLOR_PRIMARY);
        titleLabel.setAlignmentX(0.5f);
        
        // Descripción
        JLabel descLabel = new JLabel("Ingresa tu correo para recibir las instrucciones");
        descLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        descLabel.setForeground(COLOR_TEXT_SECONDARY);
        descLabel.setAlignmentX(0.5f);
        
        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(descLabel);
        panel.add(Box.createVerticalStrut(50));
        
        // Campo de correo
        JLabel emailLabel = new JLabel("Correo Electrónico");
        emailLabel.setFont(new Font("Poppins", Font.PLAIN, 13));
        emailLabel.setForeground(COLOR_TEXT_PRIMARY);
        emailLabel.setAlignmentX(0.0f);
        panel.add(emailLabel);
        panel.add(Box.createVerticalStrut(8));
        
        txtCorreo = new JTextField();
        txtCorreo.setFont(new Font("Poppins", Font.PLAIN, 14));
        txtCorreo.setForeground(COLOR_TEXT_PRIMARY);
        txtCorreo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtCorreo.setBackground(Color.WHITE);
        txtCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        txtCorreo.setAlignmentX(0.0f);
        txtCorreo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnEnviarActionPerformed(null);
            }
        });
        panel.add(txtCorreo);
        panel.add(Box.createVerticalStrut(20));
        
        // Mensaje informativo
        JLabel infoLabel = new JLabel("Un correo te llegará con la información correspondiente");
        infoLabel.setFont(new Font("Poppins", Font.ITALIC, 12));
        infoLabel.setForeground(COLOR_TEXT_SECONDARY);
        infoLabel.setAlignmentX(0.5f);
        panel.add(infoLabel);
        panel.add(Box.createVerticalStrut(30));
        
        // Botón Enviar
        btnEnviar = new JButton("Enviar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente del botón
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_PRIMARY,
                    0, getHeight(), COLOR_SECONDARY
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btnEnviar.setFont(new Font("Poppins", Font.BOLD, 16));
        btnEnviar.setForeground(COLOR_TEXT_LIGHT);
        btnEnviar.setBorderPainted(false);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setContentAreaFilled(false);
        btnEnviar.setPreferredSize(new Dimension(300, 50));
        btnEnviar.setMaximumSize(new Dimension(300, 50));
        btnEnviar.setAlignmentX(0.5f);
        btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnEnviarActionPerformed(e);
            }
        });
        
        // Efecto hover para el botón
        btnEnviar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        
        panel.add(btnEnviar);
        panel.add(Box.createVerticalStrut(30));
        
        // Botón Regresar
        lblBack = new JLabel("← Regresar");
        lblBack.setFont(new Font("Poppins", Font.PLAIN, 14));
        lblBack.setForeground(COLOR_PRIMARY);
        lblBack.setAlignmentX(0.5f);
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Obtener el window padre para abrir Inicio sobre él
                java.awt.Window parent = getOwner();
                cerrar();
                if (parent != null) {
                    new UI.Inicio(parent).setVisible(true);
                } else {
                    new UI.Inicio().setVisible(true);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                lblBack.setForeground(COLOR_SECONDARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                lblBack.setForeground(COLOR_PRIMARY);
            }
        });
        panel.add(lblBack);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    /**
     * Aplica estilos modernos
     */
    private void aplicarEstilosModernos() {
        // Estilos ya aplicados en crearPanelDerecho y crearPanelIzquierdo
    }
    
    /**
     * Carga los complementos desde la base de datos (NombreDeApp, CorreoApp, etc.)
     */
    private void cargarComplementos() {
        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT NombreDeApp, CorreoApp FROM tb_complementos ORDER BY id DESC LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String nombre = rs.getString("NombreDeApp");
                String correo = rs.getString("CorreoApp");
                
                // Almacenar los valores
                if (nombre != null && !nombre.isEmpty()) {
                    nombreApp = nombre;
                }
                if (correo != null && !correo.isEmpty()) {
                    correoApp = correo;
                }
                
                // Actualizar el label del nombre en el panel izquierdo
                SwingUtilities.invokeLater(() -> {
                    if (NombreApp != null) {
                        NombreApp.setText(nombreApp);
                    }
                    // Actualizar el footer con nombre y correo
                    actualizarFooter();
                });
            }
        } catch (SQLException e) {
            logger.severe("Error al cargar complementos: " + e.getMessage());
            // Usar valores por defecto si hay error
            nombreApp = "INVOICE DAY";
            correoApp = "";
            SwingUtilities.invokeLater(() -> {
                if (NombreApp != null) {
                    NombreApp.setText(nombreApp);
                }
                actualizarFooter();
            });
        }
    }
    
    /**
     * Actualiza el footer con el nombre y correo de la aplicación desde complementos
     */
    private void actualizarFooter() {
        // Buscar y actualizar el footer en el panel izquierdo
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                buscarYActualizarFooter(panel);
            }
        }
    }
    
    /**
     * Busca recursivamente el footer y lo actualiza
     */
    private void buscarYActualizarFooter(Container container) {
        Component[] components = container.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                String text = label.getText();
                if (text != null && text.contains("© 2025")) {
                    label.setText("© 2025 " + nombreApp + ". Todos los derechos reservados.");
                    return;
                }
            } else if (comp instanceof Container) {
                buscarYActualizarFooter((Container) comp);
            }
        }
    }
    
    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {
        btnEnviarActionPerformed(null);
    }

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {
        String correo = txtCorreo.getText().trim();
        
        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingresa tu correo electrónico.", 
                "Campo Vacío", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingresa un correo electrónico válido.", 
                "Correo Inválido", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Aquí puedes agregar la lógica para enviar el correo de recuperación
        JOptionPane.showMessageDialog(this, 
            "Se ha enviado un correo con las instrucciones para recuperar tu cuenta a: " + correo, 
            "Correo Enviado", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Crea y agrega un botón cerrar personalizado en la esquina superior derecha
     */
    private void crearBotonCerrar(javax.swing.JLayeredPane layeredPane) {
        btnCerrar = new JButton("✕") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 220),
                    getWidth(), getHeight(), new Color(COLOR_SECONDARY.getRed(), COLOR_SECONDARY.getGreen(), COLOR_SECONDARY.getBlue(), 220)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 18));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setOpaque(false);
        btnCerrar.setSize(35, 35);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setFocusPainted(false);
        
        btnCerrar.addActionListener(e -> cerrar());
        
        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCerrar.setForeground(new Color(255, 200, 200));
                btnCerrar.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnCerrar.setForeground(Color.WHITE);
                btnCerrar.repaint();
            }
        });
        
        // Posicionar el botón en la esquina superior derecha
        btnCerrar.setBounds(1155, 10, 35, 35);
        
        // Agregar el botón en la capa superior del layeredPane
        layeredPane.add(btnCerrar, javax.swing.JLayeredPane.PALETTE_LAYER);
    }
    
    /**
     * Cierra la ventana
     */
    private void cerrar() {
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Recuperacion().setVisible(true));
    }
}
