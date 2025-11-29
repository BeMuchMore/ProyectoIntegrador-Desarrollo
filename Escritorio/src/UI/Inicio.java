
package UI;

import UI.Conexion;
import UI.SessionManager;
import UI.util.ModernStyles;
import UX.Usuario;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

    
public class Inicio extends javax.swing.JDialog {
    
    private static final Logger logger = Logger.getLogger(Inicio.class.getName());
    
    // Colores consistentes con el resto de la aplicación
    private static final Color COLOR_PRIMARY = new Color(107, 45, 77); // #6B2D4D
    private static final Color COLOR_SECONDARY = new Color(139, 74, 107); // #8B4A6B
    private static final Color COLOR_ACCENT = new Color(168, 85, 122); // #A8557A
    private static final Color COLOR_TEXT_PRIMARY = new Color(26, 26, 26); // #1a1a1a
    private static final Color COLOR_TEXT_SECONDARY = new Color(51, 51, 51); // #333333
    private static final Color COLOR_TEXT_LIGHT = new Color(255, 255, 255); // #FFFFFF
    private static final Color COLOR_BG_LIGHT = new Color(250, 246, 249); // #FAF6F9
    private static final Color COLOR_BG_DARK = new Color(91, 29, 61); // #5B1D3D
    private static final Color COLOR_BG_GRADIENT_START = new Color(255, 245, 252); // Rosa muy claro
    private static final Color COLOR_BG_GRADIENT_END = new Color(250, 246, 249); // #FAF6F9
    
    private JButton btnCerrar;
    private String nombreApp = "FASHION";
    private String correoApp = "";

    public Inicio(java.awt.Window parent) {
        super(parent, parent == null ? java.awt.Dialog.ModalityType.MODELESS : java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        aplicarEstilosModernos();
        cargarComplementos(); // Cargar después de crear los componentes
        if (parent != null) {
            setLocationRelativeTo(parent);
        } else {
            setLocationRelativeTo(null);
        }
    }
    
    // Constructor con Frame para compatibilidad
    public Inicio(java.awt.Frame parent) {
        super(parent, true); // Modal dialog
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        aplicarEstilosModernos();
        cargarComplementos(); // Cargar después de crear los componentes
        if (parent != null) {
            setLocationRelativeTo(parent);
        } else {
            setLocationRelativeTo(null);
        }
    }
    
    // Constructor sin parámetros para compatibilidad
    public Inicio() {
        this((java.awt.Window) null);
    }
    
    /**
     * Aplica estilos modernos a todos los componentes (diseño moderno mejorado)
     */
    private void aplicarEstilosModernos() {
        // Panel principal con gradiente elegante
        jPanel1.setBackground(COLOR_BG_LIGHT);
        
        // Panel de formulario con diseño moderno
        jPanel3.setBackground(COLOR_BG_LIGHT);
        
        // Título mejorado
        jLabel6.setFont(new Font("Playfair Display", Font.BOLD, 32));
        jLabel6.setForeground(COLOR_PRIMARY);
        
        // Labels de campos
        jLabel1.setFont(new Font("Poppins", Font.PLAIN, 13));
        jLabel1.setForeground(COLOR_TEXT_PRIMARY);
        
        jLabel2.setFont(new Font("Poppins", Font.PLAIN, 13));
        jLabel2.setForeground(COLOR_TEXT_PRIMARY);
        
        // Campos de texto mejorados
        estiloTextFieldModerno(txtCorreo);
        estiloPasswordFieldModerno(textContrasena);
        
        // Botón ingresar mejorado
        estiloBotonIngresar();
        
        // Links mejorados
        estiloLinks();
        
        // Centrar ventana sobre el padre o pantalla
        if (getOwner() != null) {
            setLocationRelativeTo(getOwner());
        } else {
            setLocationRelativeTo(null);
        }
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
     * Estiliza un campo de texto de forma moderna
     */
    private void estiloTextFieldModerno(JTextField field) {
        field.setFont(new Font("Poppins", Font.PLAIN, 14));
        field.setForeground(COLOR_TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setOpaque(true);
    }
    
    /**
     * Estiliza un campo de contraseña de forma moderna
     */
    private void estiloPasswordFieldModerno(JPasswordField field) {
        field.setFont(new Font("Poppins", Font.PLAIN, 14));
        field.setForeground(COLOR_TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setOpaque(true);
    }
    
    /**
     * Estiliza el botón de ingresar
     */
    private void estiloBotonIngresar() {
        btnIngresar.setFont(new Font("Poppins", Font.BOLD, 16));
        btnIngresar.setForeground(COLOR_TEXT_LIGHT);
        btnIngresar.setBackground(COLOR_PRIMARY);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setPreferredSize(new Dimension(300, 50));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnIngresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnIngresar.setBackground(COLOR_SECONDARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnIngresar.setBackground(COLOR_PRIMARY);
            }
        });
    }
    
    /**
     * Estiliza los enlaces
     */
    private void estiloLinks() {
        irRegistro.setFont(new Font("Poppins", Font.BOLD, 13));
        irRegistro.setForeground(COLOR_PRIMARY);
        irRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        VerificacionCorreo.setFont(new Font("Poppins", Font.BOLD, 13));
        VerificacionCorreo.setForeground(COLOR_PRIMARY);
        VerificacionCorreo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        jLabel13.setFont(new Font("Poppins", Font.PLAIN, 13));
        jLabel13.setForeground(COLOR_TEXT_SECONDARY);
        
        jLabel14.setFont(new Font("Poppins", Font.PLAIN, 12));
        jLabel14.setForeground(COLOR_TEXT_SECONDARY);
        
        jLabel3.setFont(new Font("Poppins", Font.PLAIN, 12));
        jLabel3.setForeground(COLOR_TEXT_SECONDARY);
    }
    
    /**
     * Carga los complementos desde la base de datos
     */
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
            nombreApp = "FASHION";
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
        Component[] components = jPanel1.getComponents();
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
                    String nuevoTexto = String.format("© 2025 %s. Todos los derechos reservados.", nombreApp);
                    label.setText("<html><div style='line-height: 1.6;'>" + nuevoTexto + "</div></html>");
                    return;
                }
            } else if (comp instanceof Container) {
                buscarYActualizarFooter((Container) comp);
            }
        }
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        NombreApp = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtCorreo = new javax.swing.JTextField();
        textContrasena = new javax.swing.JPasswordField();
        btnIngresar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        irRegistro = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        VerificacionCorreo = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        NombreApp2 = new javax.swing.JLabel();
        CorreoApp = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true); // Sin decoración del sistema
        setResizable(false);
        setSize(1200, 700); // Tamaño fijo
        
        // Panel principal con diseño dividido
        jPanel1 = new JPanel(new BorderLayout());
        jPanel1.setBackground(COLOR_BG_LIGHT);
        jPanel1.setPreferredSize(new Dimension(1200, 700));
        
        // Panel izquierdo decorativo con gradiente
        JPanel leftPanel = crearPanelIzquierdo();
        
        // Panel derecho con formulario
        JPanel rightPanel = crearPanelFormulario();
        
        jPanel1.add(leftPanel, BorderLayout.WEST);
        jPanel1.add(rightPanel, BorderLayout.CENTER);
        
        // Crear JLayeredPane para superponer el botón cerrar
        javax.swing.JLayeredPane layeredPane = new javax.swing.JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1200, 700));
        
        // Agregar jPanel1 en la capa inferior
        jPanel1.setBounds(0, 0, 1200, 700);
        layeredPane.add(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        // Crear y agregar botón cerrar en la capa superior
        crearBotonCerrar(layeredPane);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(layeredPane, BorderLayout.CENTER);
    }// </editor-fold>
    
    /**
     * Crea el panel izquierdo decorativo
     */
    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Gradiente de fondo
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_BG_GRADIENT_START,
                    width, height, COLOR_BG_GRADIENT_END
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, width, height);
                
                // Formas decorativas circulares
                g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 20));
                g2.fillOval(width - 150, 100, 250, 250);
                g2.setColor(new Color(COLOR_SECONDARY.getRed(), COLOR_SECONDARY.getGreen(), COLOR_SECONDARY.getBlue(), 15));
                g2.fillOval(-50, height - 200, 300, 300);
                
                g2.dispose();
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(550, 700));
        panel.setBorder(new EmptyBorder(60, 60, 60, 60));
        
        // Contenido del panel izquierdo
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        // Logo/Nombre de la app grande
        NombreApp = new JLabel(nombreApp);
        NombreApp.setFont(new Font("Playfair Display", Font.BOLD, 56));
        NombreApp.setForeground(COLOR_PRIMARY);
        NombreApp.setAlignmentX(Component.LEFT_ALIGNMENT);
        NombreApp.setBorder(new EmptyBorder(0, 0, 30, 0));
        content.add(NombreApp);
        
        // Título bienvenida
        JLabel welcomeTitle = new JLabel("<html><div style='line-height: 1.4;'>" +
                "<span style='font-size: 42px; color: #6B2D4D;'>Bienvenido</span><br>" +
                "<span style='font-size: 42px; color: #8B4A6B;'>de Nuevo</span></div></html>");
        welcomeTitle.setFont(new Font("Playfair Display", Font.BOLD, 42));
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomeTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        content.add(welcomeTitle);
        
        // Subtítulo
        JLabel subtitle = new JLabel("<html><div style='width: 400px; line-height: 1.6;'>" +
                "Inicia sesión para acceder a nuestra exclusiva colección de moda femenina premium.</div></html>");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 16));
        subtitle.setForeground(COLOR_TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(0, 0, 60, 0));
        content.add(subtitle);
        
        // Estadísticas decorativas
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel stat1 = crearStatCard("500+", "Productos");
        JPanel stat2 = crearStatCard("10K+", "Clientes");
        JPanel stat3 = crearStatCard("4.9★", "Calificación");
        
        statsPanel.add(stat1);
        statsPanel.add(stat2);
        statsPanel.add(stat3);
        
        content.add(statsPanel);
        content.add(Box.createVerticalGlue());
        
        // Footer del panel izquierdo
        JPanel leftFooter = new JPanel(new BorderLayout());
        leftFooter.setOpaque(false);
        
        JLabel footerText = new JLabel("<html><div style='line-height: 1.6;'>" +
                "© 2025 " + nombreApp + ". Todos los derechos reservados.</div></html>");
        footerText.setFont(new Font("Poppins", Font.PLAIN, 12));
        footerText.setForeground(COLOR_TEXT_SECONDARY);
        leftFooter.add(footerText, BorderLayout.WEST);
        
        content.add(leftFooter);
        
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Crea una tarjeta de estadística
     */
    private JPanel crearStatCard(String numero, String texto) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        
        JLabel numLabel = new JLabel(numero);
        numLabel.setFont(new Font("Poppins", Font.BOLD, 28));
        numLabel.setForeground(COLOR_PRIMARY);
        numLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel textLabel = new JLabel(texto);
        textLabel.setFont(new Font("Poppins", Font.PLAIN, 13));
        textLabel.setForeground(COLOR_TEXT_SECONDARY);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(numLabel);
        card.add(textLabel);
        
        return card;
    }
    
    /**
     * Crea el panel derecho con el formulario
     */
    private JPanel crearPanelFormulario() {
        jPanel3 = new JPanel();
        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.Y_AXIS));
        jPanel3.setBackground(Color.WHITE);
        jPanel3.setBorder(new EmptyBorder(80, 80, 80, 80));
        
        // Header del formulario
        JPanel formHeader = new JPanel();
        formHeader.setLayout(new BoxLayout(formHeader, BoxLayout.Y_AXIS));
        formHeader.setOpaque(false);
        formHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        jLabel6 = new JLabel("Inicia sesión para comprar");
        jLabel6.setFont(new Font("Playfair Display", Font.BOLD, 32));
        jLabel6.setForeground(COLOR_PRIMARY);
        jLabel6.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabel6.setBorder(new EmptyBorder(0, 0, 50, 0));
        formHeader.add(jLabel6);
        
        jPanel3.add(formHeader);
        
        // Formulario
        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setOpaque(false);
        formContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        formContent.setMaximumSize(new Dimension(450, Integer.MAX_VALUE));
        
        // Campo Usuario
        JPanel usuarioContainer = new JPanel();
        usuarioContainer.setLayout(new BoxLayout(usuarioContainer, BoxLayout.Y_AXIS));
        usuarioContainer.setOpaque(false);
        usuarioContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        usuarioContainer.setMaximumSize(new Dimension(450, Integer.MAX_VALUE));
        
        jLabel1 = new JLabel("Usuario");
        jLabel1.setFont(new Font("Poppins", Font.PLAIN, 13));
        jLabel1.setForeground(COLOR_TEXT_PRIMARY);
        jLabel1.setBorder(new EmptyBorder(0, 0, 8, 0));
        usuarioContainer.add(jLabel1);
        
        txtCorreo = new JTextField();
        txtCorreo.setFont(new Font("Poppins", Font.PLAIN, 14));
        txtCorreo.setMaximumSize(new Dimension(450, 45));
        txtCorreo.setPreferredSize(new Dimension(450, 45));
        txtCorreo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtCorreo.addActionListener(e -> txtCorreoActionPerformed(e));
        usuarioContainer.add(txtCorreo);
        
        formContent.add(usuarioContainer);
        formContent.add(Box.createVerticalStrut(25));
        
        // Campo Contraseña
        JPanel passwordContainer = new JPanel();
        passwordContainer.setLayout(new BoxLayout(passwordContainer, BoxLayout.Y_AXIS));
        passwordContainer.setOpaque(false);
        passwordContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordContainer.setMaximumSize(new Dimension(450, Integer.MAX_VALUE));
        
        jLabel2 = new JLabel("Contraseña");
        jLabel2.setFont(new Font("Poppins", Font.PLAIN, 13));
        jLabel2.setForeground(COLOR_TEXT_PRIMARY);
        jLabel2.setBorder(new EmptyBorder(0, 0, 8, 0));
        passwordContainer.add(jLabel2);
        
        textContrasena = new JPasswordField();
        textContrasena.setFont(new Font("Poppins", Font.PLAIN, 14));
        textContrasena.setMaximumSize(new Dimension(450, 45));
        textContrasena.setPreferredSize(new Dimension(450, 45));
        textContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        textContrasena.addActionListener(e -> textContrasenaActionPerformed(e));
        passwordContainer.add(textContrasena);
        
        formContent.add(passwordContainer);
        formContent.add(Box.createVerticalStrut(15));
        
        // Link de recuperación
        JPanel recoveryContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        recoveryContainer.setOpaque(false);
        recoveryContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        jLabel14 = new JLabel("¿Olvidaste tu contraseña o Usuario? No te preocupes, ");
        jLabel14.setFont(new Font("Poppins", Font.PLAIN, 12));
        jLabel14.setForeground(COLOR_TEXT_SECONDARY);
        recoveryContainer.add(jLabel14);
        
        VerificacionCorreo = new JLabel("correo");
        VerificacionCorreo.setFont(new Font("Poppins", Font.BOLD, 12));
        VerificacionCorreo.setForeground(COLOR_PRIMARY);
        VerificacionCorreo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        VerificacionCorreo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                  UI.Recuperacion recuperacion = new UI.Recuperacion(Inicio.this);
                    recuperacion.setVisible(true);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                VerificacionCorreo.setForeground(COLOR_SECONDARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                VerificacionCorreo.setForeground(COLOR_PRIMARY);
            }
        });
        recoveryContainer.add(VerificacionCorreo);
        
        formContent.add(recoveryContainer);
        formContent.add(Box.createVerticalStrut(35));
        
        // Botón Ingresar
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setFont(new Font("Poppins", Font.BOLD, 16));
        btnIngresar.setForeground(COLOR_TEXT_LIGHT);
        btnIngresar.setBackground(COLOR_PRIMARY);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setPreferredSize(new Dimension(450, 50));
        btnIngresar.setMaximumSize(new Dimension(450, 50));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.addActionListener(e -> btnIngresarActionPerformed(e));
        btnIngresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnIngresar.setBackground(COLOR_SECONDARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnIngresar.setBackground(COLOR_PRIMARY);
            }
        });
        formContent.add(btnIngresar);
        formContent.add(Box.createVerticalStrut(30));
        
        // Link de registro
        JPanel registerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerContainer.setOpaque(false);
        registerContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        jLabel13 = new JLabel("¿Aún no tienes cuenta?");
        jLabel13.setFont(new Font("Poppins", Font.PLAIN, 13));
        jLabel13.setForeground(COLOR_TEXT_SECONDARY);
        registerContainer.add(jLabel13);
        
        irRegistro = new JLabel("Regístrate");
        irRegistro.setFont(new Font("Poppins", Font.BOLD, 13));
        irRegistro.setForeground(COLOR_PRIMARY);
        irRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        irRegistro.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                UI.registro Registro = new UI.registro(Inicio.this);
                Registro.setVisible(true);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                irRegistro.setForeground(COLOR_SECONDARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                irRegistro.setForeground(COLOR_PRIMARY);
            }
        });
        registerContainer.add(irRegistro);
        
        formContent.add(registerContainer);
        formContent.add(Box.createVerticalGlue());
        
        jPanel3.add(formContent);
        
        return jPanel3;
    }                        

    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {                                          
        textContrasena.requestFocus();
    }                                         

    private void textContrasenaActionPerformed(java.awt.event.ActionEvent evt) {                                               
        btnIngresarActionPerformed(evt);
    }                                              

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {                                            

     diagnosticarConexion(); 
        System.out.println("=== INICIO DEL LOGIN ===");
    
        String usuario = txtCorreo.getText().trim();
        String contrasena = new String(textContrasena.getPassword()).trim();
    
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos");
            return;
        }
    
        try (Connection conn = Conexion.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "No se pudo establecer conexión con la base de datos");
                return;
            }

            String sql = "SELECT id, cargo, nombre FROM tb_usuarios WHERE usuario=? AND contrasena=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String cargo = rs.getString("cargo");
                String nombre = rs.getString("nombre");

                SessionManager.getInstance().login(userId, usuario, cargo);
                JOptionPane.showMessageDialog(this, "Bienvenido " + nombre);

                // Redirección según tipo de usuario
                if ("admin".equalsIgnoreCase(cargo)) {
                    UI.principal ventanaAdmin = new UI.principal(userId);
                    ventanaAdmin.setVisible(true);
                    
                   UX.Usuario usuarioAdmin = new UX.Usuario(userId, usuario);
                    usuarioAdmin.setVisible(true);
                } else {
                    UI.principal ventanaCliente = new UI.principal(userId);
                    ventanaCliente.setVisible(true);
                }

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage());
        }

        System.out.println("=== FIN DEL LOGIN ===");
   
    }                                           

   private void diagnosticarConexion() {
        String diagnostico = Conexion.diagnosticarConexion();
        System.out.println(diagnostico);
        
        // Si hay error, también mostrar en un diálogo
        if (diagnostico.contains("✗") || diagnostico.contains("ERROR")) {
            javax.swing.JOptionPane.showMessageDialog(
                this, 
                diagnostico.replace("===", "").replace("===", ""), 
                "Error de Conexión a la Base de Datos", 
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }



    /**
     * @param args the command line arguments
     */
public static void main(String args[]) {
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
        // Usar directamente Logger.getLogger en lugar del campo estático
       
    }

    java.awt.EventQueue.invokeLater(() -> {
        new Inicio().setVisible(true);
    });
}


    // Variables declaration - do not modify                     
    private javax.swing.JLabel CorreoApp;
    private javax.swing.JLabel NombreApp;
    private javax.swing.JLabel NombreApp2;
    private javax.swing.JLabel VerificacionCorreo;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JLabel irRegistro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPasswordField textContrasena;
    private javax.swing.JTextField txtCorreo;
    // End of variables declaration                   

  
}
