package UI;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;
import java.sql.*;
import UI.util.ModernStyles;
import UI.Conexion;

public class registro extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(registro.class.getName());
    private ValidadorContrasena.NivelSeguridad nivelActual;
    
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
    
    // Componentes principales
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel NombreApp;
    private JButton btnCerrar;
    private String nombreApp = "INVOICE DAY";
    private String correoApp = "";
    
    // Campos del formulario
    private JTextField textCorreo;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtUsuarios;
    private JPasswordField txtContrasena;
    private JTextField txtIdent;
    private JComboBox<String> jTipoDeIdentificacionComboBox;
    private JCheckBox jTerminosCondicionesCheckBox;
    private JButton btnRegistrar;
    private JButton btnRegresar;
    private JLabel LabelCorreoInformacion;
    private JLabel LabelUsuarioInforacion;
    private JLabel LabelContraseñaInformacion;

    /**
     * Creates new form PRF
     */
    public registro(java.awt.Window parent) {
        super(parent, parent == null ? java.awt.Dialog.ModalityType.MODELESS : java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        // Configurar validaciones después de que los campos estén creados
        configurarValidaciones();
        cargarComplementos();
        if (parent != null) {
            setLocationRelativeTo(parent);
        } else {
            setLocationRelativeTo(null);
        }
    }
    
    // Constructor con Frame para compatibilidad
    public registro(java.awt.Frame parent) {
        super(parent, true); // Modal dialog
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        // Configurar validaciones después de que los campos estén creados
        configurarValidaciones();
        cargarComplementos();
        if (parent != null) {
            setLocationRelativeTo(parent);
        } else {
            setLocationRelativeTo(null);
        }
    }
    
    // Constructor sin parámetros para compatibilidad
    public registro() {
        this((java.awt.Window) null);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true); // Sin decoración del sistema
        setTitle("Registro de Usuario");
        setSize(1200, 700);
        setResizable(false);
        
        // Panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG_LIGHT);
        
        // Panel izquierdo (decorativo con beneficios)
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
     * Crea el panel izquierdo decorativo con beneficios
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
        panel.setPreferredSize(new Dimension(550, 700));
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
        JLabel titleLabel = new JLabel("Únete a Nosotros");
        titleLabel.setFont(new Font("Playfair Display", Font.BOLD, 42));
        titleLabel.setForeground(COLOR_PRIMARY);
        titleLabel.setAlignmentX(0.0f);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("<html><div style='text-align: left; width: 400px;'>" +
            "Completa tus datos personales y disfruta una compra más fácil y personalizada." +
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
        contentPanel.add(Box.createVerticalStrut(40));
        
        // Sección de beneficios
        JLabel beneficiosTitle = new JLabel("Beneficios");
        beneficiosTitle.setFont(new Font("Playfair Display", Font.BOLD, 28));
        beneficiosTitle.setForeground(COLOR_PRIMARY);
        beneficiosTitle.setAlignmentX(0.0f);
        contentPanel.add(beneficiosTitle);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Lista de beneficios
        JPanel beneficiosList = new JPanel();
        beneficiosList.setLayout(new BoxLayout(beneficiosList, BoxLayout.Y_AXIS));
        beneficiosList.setOpaque(false);
        beneficiosList.setAlignmentX(0.0f);
        
        String[] beneficios = {
            "✓ Recibir notificaciones en tiempo real de tus pedidos",
            "✓ Guardar medios de pago y direcciones favoritas",
            "✓ Promociones especiales, cupones de descuento y más",
            "✓ Revisar tus boletas online"
        };
        
        for (String beneficio : beneficios) {
            JLabel benefitLabel = new JLabel("<html><div style='width: 400px;'>" + beneficio + "</div></html>");
            benefitLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
            benefitLabel.setForeground(COLOR_TEXT_SECONDARY);
            benefitLabel.setAlignmentX(0.0f);
            benefitLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
            beneficiosList.add(benefitLabel);
        }
        
        contentPanel.add(beneficiosList);
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
     * Crea el panel derecho con el formulario de registro
     */
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));
        
        // Scroll pane para el formulario
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Aplicar el diseño de scrollbar moderno de principal.java
        personalizarScrollBar(scrollPane.getVerticalScrollBar());
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Título del formulario
        JLabel titleLabel = new JLabel("Registro de Usuario");
        titleLabel.setFont(new Font("Playfair Display", Font.BOLD, 32));
        titleLabel.setForeground(COLOR_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        formPanel.add(titleLabel);
        
        // Campos del formulario (se crearán en métodos separados para mantener el código limpio)
        crearCamposFormulario(formPanel);
        
        scrollPane.setViewportView(formPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crea los campos del formulario de registro
     */
    private void crearCamposFormulario(JPanel formPanel) {
        // Contenedor centrado para todos los campos
        JPanel camposContainer = new JPanel();
        camposContainer.setLayout(new BoxLayout(camposContainer, BoxLayout.Y_AXIS));
        camposContainer.setOpaque(false);
        camposContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.setMaximumSize(new Dimension(450, Integer.MAX_VALUE));
        
        // Campo Correo
        JLabel labelCorreo = new JLabel("Correo Electrónico");
        labelCorreo.setFont(new Font("Poppins", Font.PLAIN, 13));
        labelCorreo.setForeground(COLOR_TEXT_PRIMARY);
        labelCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(labelCorreo);
        camposContainer.add(Box.createVerticalStrut(5));
        
        textCorreo = new JTextField();
        textCorreo.setFont(new Font("Poppins", Font.PLAIN, 14));
        textCorreo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        textCorreo.setMaximumSize(new Dimension(450, 45));
        textCorreo.setPreferredSize(new Dimension(450, 45));
        textCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(textCorreo);
        
        LabelCorreoInformacion = new JLabel("-");
        LabelCorreoInformacion.setFont(new Font("Poppins", Font.PLAIN, 11));
        LabelCorreoInformacion.setForeground(Color.GRAY);
        LabelCorreoInformacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(LabelCorreoInformacion);
        camposContainer.add(Box.createVerticalStrut(20));
        
        // Campo Nombres
        JLabel labelNombres = new JLabel("Nombres");
        labelNombres.setFont(new Font("Poppins", Font.PLAIN, 13));
        labelNombres.setForeground(COLOR_TEXT_PRIMARY);
        labelNombres.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(labelNombres);
        camposContainer.add(Box.createVerticalStrut(5));
        
        txtNombres = new JTextField();
        txtNombres.setFont(new Font("Poppins", Font.PLAIN, 14));
        txtNombres.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtNombres.setMaximumSize(new Dimension(450, 45));
        txtNombres.setPreferredSize(new Dimension(450, 45));
        txtNombres.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(txtNombres);
        camposContainer.add(Box.createVerticalStrut(20));
        
        // Campo Apellidos
        JLabel labelApellidos = new JLabel("Apellidos");
        labelApellidos.setFont(new Font("Poppins", Font.PLAIN, 13));
        labelApellidos.setForeground(COLOR_TEXT_PRIMARY);
        labelApellidos.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(labelApellidos);
        camposContainer.add(Box.createVerticalStrut(5));
        
        txtApellidos = new JTextField();
        txtApellidos.setFont(new Font("Poppins", Font.PLAIN, 14));
        txtApellidos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtApellidos.setMaximumSize(new Dimension(450, 45));
        txtApellidos.setPreferredSize(new Dimension(450, 45));
        txtApellidos.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(txtApellidos);
        camposContainer.add(Box.createVerticalStrut(20));
        
        // Campo Usuario
        JLabel labelUsuario = new JLabel("Usuario");
        labelUsuario.setFont(new Font("Poppins", Font.PLAIN, 13));
        labelUsuario.setForeground(COLOR_TEXT_PRIMARY);
        labelUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(labelUsuario);
        camposContainer.add(Box.createVerticalStrut(5));
        
        txtUsuarios = new JTextField();
        txtUsuarios.setFont(new Font("Poppins", Font.PLAIN, 14));
        txtUsuarios.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtUsuarios.setMaximumSize(new Dimension(450, 45));
        txtUsuarios.setPreferredSize(new Dimension(450, 45));
        txtUsuarios.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(txtUsuarios);
        
        LabelUsuarioInforacion = new JLabel("-");
        LabelUsuarioInforacion.setFont(new Font("Poppins", Font.PLAIN, 11));
        LabelUsuarioInforacion.setForeground(Color.GRAY);
        LabelUsuarioInforacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(LabelUsuarioInforacion);
        camposContainer.add(Box.createVerticalStrut(20));
        
        // Campo Contraseña
        JLabel labelContrasena = new JLabel("Contraseña");
        labelContrasena.setFont(new Font("Poppins", Font.PLAIN, 13));
        labelContrasena.setForeground(COLOR_TEXT_PRIMARY);
        labelContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(labelContrasena);
        camposContainer.add(Box.createVerticalStrut(5));
        
        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Poppins", Font.PLAIN, 14));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtContrasena.setMaximumSize(new Dimension(450, 45));
        txtContrasena.setPreferredSize(new Dimension(450, 45));
        txtContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(txtContrasena);
        
        LabelContraseñaInformacion = new JLabel("-");
        LabelContraseñaInformacion.setFont(new Font("Poppins", Font.PLAIN, 11));
        LabelContraseñaInformacion.setForeground(Color.GRAY);
        LabelContraseñaInformacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(LabelContraseñaInformacion);
        camposContainer.add(Box.createVerticalStrut(20));
        
        // Campo Identificación
        JLabel labelIdent = new JLabel("Identificación");
        labelIdent.setFont(new Font("Poppins", Font.PLAIN, 13));
        labelIdent.setForeground(COLOR_TEXT_PRIMARY);
        labelIdent.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposContainer.add(labelIdent);
        camposContainer.add(Box.createVerticalStrut(5));
        
        JPanel identPanel = new JPanel();
        identPanel.setLayout(new BoxLayout(identPanel, BoxLayout.X_AXIS));
        identPanel.setOpaque(false);
        identPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        identPanel.setMaximumSize(new Dimension(450, 45));
        
        jTipoDeIdentificacionComboBox = new JComboBox<>(new String[]{"CC (cédula de ciudadanía)", "CE (carnet de extranjería)"});
        jTipoDeIdentificacionComboBox.setFont(new Font("Poppins", Font.PLAIN, 13));
        jTipoDeIdentificacionComboBox.setMaximumSize(new Dimension(220, 35));
        jTipoDeIdentificacionComboBox.setPreferredSize(new Dimension(220, 35));
        
        identPanel.add(jTipoDeIdentificacionComboBox);
        identPanel.add(Box.createHorizontalStrut(10));
        
        txtIdent = new JTextField();
        txtIdent.setFont(new Font("Poppins", Font.PLAIN, 14));
        txtIdent.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        txtIdent.setPreferredSize(new Dimension(220, 45));
        txtIdent.setMaximumSize(new Dimension(220, 45));
        identPanel.add(txtIdent);
        
        camposContainer.add(identPanel);
        camposContainer.add(Box.createVerticalStrut(25));
        
        // Checkbox de términos y condiciones con enlace
        JPanel terminosPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        terminosPanel.setOpaque(false);
        terminosPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        jTerminosCondicionesCheckBox = new JCheckBox();
        jTerminosCondicionesCheckBox.setOpaque(false);
        
        JLabel terminosLabel = new JLabel("Acepto los ");
        terminosLabel.setFont(new Font("Poppins", Font.PLAIN, 12));
        terminosLabel.setForeground(COLOR_TEXT_PRIMARY);
        
        JLabel terminosLink = new JLabel("términos y condiciones");
        terminosLink.setFont(new Font("Poppins", Font.PLAIN, 12));
        terminosLink.setForeground(COLOR_PRIMARY);
        terminosLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        terminosLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                UI.Condiciones condiciones = new UI.Condiciones(registro.this);
                condiciones.setVisible(true);
                condiciones.toFront();
                condiciones.requestFocus();
            }
        });
        
        terminosPanel.add(jTerminosCondicionesCheckBox);
        terminosPanel.add(terminosLabel);
        terminosPanel.add(terminosLink);
        
        camposContainer.add(terminosPanel);
        camposContainer.add(Box.createVerticalStrut(25));
        
        // Botones
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnRegistrar = new JButton("Registrarse") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(0, 0, COLOR_PRIMARY, 0, getHeight(), COLOR_SECONDARY);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btnRegistrar.setFont(new Font("Poppins", Font.BOLD, 16));
        btnRegistrar.setForeground(COLOR_TEXT_LIGHT);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setContentAreaFilled(false);
        btnRegistrar.setPreferredSize(new Dimension(200, 50));
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(e -> btnRegistrarActionPerformed(e));
        
        btnRegresar = new JButton("Iniciar Sesión");
        btnRegresar.setFont(new Font("Poppins", Font.BOLD, 16));
        btnRegresar.setForeground(COLOR_PRIMARY);
        btnRegresar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARY, 2),
            BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        btnRegresar.setBackground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setPreferredSize(new Dimension(200, 50));
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.addActionListener(e -> btnRegresarActionPerformed(e));
        
        buttonsPanel.add(btnRegistrar);
        buttonsPanel.add(btnRegresar);
        
        camposContainer.add(buttonsPanel);
        
        formPanel.add(Box.createHorizontalGlue());
        formPanel.add(camposContainer);
        formPanel.add(Box.createHorizontalGlue());
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
    
    // Configurar listeners para validaciones en tiempo real
    private void configurarValidaciones() {
        // Listener para el campo de contraseña
        txtContrasena.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarNivelContrasena();
            }
        });
        
        // Listener para el campo de correo
        textCorreo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarCorreo();
            }
        });
        
        // Listener para el campo de usuario
        txtUsuarios.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarUsuario();
            }
        });
    }
    
    // Actualizar indicador de nivel de contraseña
    private void actualizarNivelContrasena() {
        String contrasena = new String(txtContrasena.getPassword());
        if (contrasena.isEmpty()) {
            LabelContraseñaInformacion.setText("-");
            LabelContraseñaInformacion.setForeground(Color.BLACK);
            return;
        }
        
        nivelActual = ValidadorContrasena.evaluarContrasena(contrasena);
        LabelContraseñaInformacion.setText("Nivel: " + nivelActual.getTexto());
        
        switch (nivelActual) {
            case FACIL:
                LabelContraseñaInformacion.setForeground(Color.RED);
                break;
            case MEDIO:
                LabelContraseñaInformacion.setForeground(Color.ORANGE);
                break;
            case DIFICIL:
                LabelContraseñaInformacion.setForeground(new Color(204, 204, 0)); // Yellow-ish
                break;
            case AVANZADO:
                LabelContraseñaInformacion.setForeground(Color.GREEN);
                break;
        }
    }
    
    // Validar formato de correo
    private void validarCorreo() {
        String correo = textCorreo.getText().trim();
        if (correo.isEmpty()) {
            LabelCorreoInformacion.setText("-");
            LabelCorreoInformacion.setForeground(Color.BLACK);
            return;
        }
        
        // Validar formato de correo
        String patronCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(patronCorreo, correo)) {
            LabelCorreoInformacion.setText("Formato inválido");
            LabelCorreoInformacion.setForeground(Color.RED);
            return;
        }
        
        // Verificar si el correo ya existe
        if (Conexion.correoExiste(correo)) {
            LabelCorreoInformacion.setText("Correo ya registrado");
            LabelCorreoInformacion.setForeground(Color.RED);
        } else {
            LabelCorreoInformacion.setText("Correo disponible ✓");
            LabelCorreoInformacion.setForeground(Color.GREEN);
        }
    }
    
    // Validar usuario
    private void validarUsuario() {
        String usuario = txtUsuarios.getText().trim();
        if (usuario.isEmpty()) {
            LabelUsuarioInforacion.setText("-");
            LabelUsuarioInforacion.setForeground(Color.BLACK);
            return;
        }
        
        // Verificar si el usuario ya existe
        if (Conexion.usuarioExiste(usuario)) {
            String sugerencia = Conexion.sugerirUsuario(usuario);
            LabelUsuarioInforacion.setText("Ya existe. Sugerencia: " + sugerencia);
            LabelUsuarioInforacion.setForeground(Color.ORANGE);
            
            // Preguntar si quiere usar la sugerencia
            int respuesta = JOptionPane.showConfirmDialog(this,
                "El usuario '" + usuario + "' ya existe.\n" +
                "¿Desea usar '" + sugerencia + "' como nombre de usuario?",
                "Usuario existente",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
            if (respuesta == JOptionPane.YES_OPTION) {
                txtUsuarios.setText(sugerencia);
                LabelUsuarioInforacion.setText("Usuario disponible ✓");
                LabelUsuarioInforacion.setForeground(Color.GREEN);
            }
        } else {
            LabelUsuarioInforacion.setText("Usuario disponible ✓");
            LabelUsuarioInforacion.setForeground(Color.GREEN);
        }
    }

    // El método initComponents() antiguo ha sido reemplazado por el nuevo diseño de dos paneles                        

    private void txtIdentActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void txtApellidosActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void txtNombresActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void txtContrasenaActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void txtUsuariosActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // IMPLEMENTACIÓN COMPLETA DEL REGISTRO CON VALIDACIONES
        
        // 1. Verificar que se aceptaron los términos y condiciones
        if (!jTerminosCondicionesCheckBox.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "Debe aceptar los términos y condiciones para continuar", 
                "Términos y Condiciones", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 2. Obtener todos los campos
        String correo = textCorreo.getText().trim();
        String nombre = txtNombres.getText().trim();
        String apellido = txtApellidos.getText().trim();
        String usuario = txtUsuarios.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        String identificacion = txtIdent.getText().trim();
        String tipoIdentificacion = jTipoDeIdentificacionComboBox.getSelectedItem().toString();
        
        // 3. Validar campos obligatorios
        if (correo.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || 
            usuario.isEmpty() || contrasena.isEmpty() || identificacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos obligatorios", 
                "Campos incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 4. Validar formato de correo
        String patronCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(patronCorreo, correo)) {
            JOptionPane.showMessageDialog(this, 
                "El formato del correo electrónico no es válido", 
                "Correo inválido", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 5. Verificar si el correo ya existe
        if (Conexion.correoExiste(correo)) {
            JOptionPane.showMessageDialog(this, 
                "El correo electrónico ya está registrado en el sistema.\n" +
                "Por favor, use otro correo o inicie sesión con su cuenta existente.", 
                "Correo ya registrado", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 6. Verificar si el usuario ya existe
        if (Conexion.usuarioExiste(usuario)) {
            String nuevoUsuario = Conexion.sugerirUsuario(usuario);
            int respuesta = JOptionPane.showConfirmDialog(this,
                "El nombre de usuario '" + usuario + "' ya está en uso.\n" +
                "¿Desea usar '" + nuevoUsuario + "' como nombre de usuario?",
                "Usuario existente",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
            if (respuesta == JOptionPane.YES_OPTION) {
                usuario = nuevoUsuario;
                txtUsuarios.setText(usuario);
            } else {
                return;
            }
        }
        
        // 7. Verificar nivel de seguridad de la contraseña
        ValidadorContrasena.NivelSeguridad nivel = ValidadorContrasena.evaluarContrasena(contrasena);
        if (nivel == ValidadorContrasena.NivelSeguridad.FACIL) {
            String sugerencias = ValidadorContrasena.obtenerSugerencias(contrasena);
            int respuesta = JOptionPane.showConfirmDialog(this,
                "Su contraseña es muy débil y podría ser fácilmente comprometida.\n\n" +
                sugerencias + "\n" +
                "¿Desea continuar con esta contraseña de todos modos?",
                "Contraseña débil",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (respuesta == JOptionPane.NO_OPTION) {
                txtContrasena.requestFocus();
                return;
            }
        }
        
        // 8. Intentar registrar el usuario
        boolean registroExitoso = Conexion.insertarUsuario(
            apellido,
            "", // cargo - vacío por defecto
            contrasena,
            correo,
            "", // data - vacío por defecto
            "", // descripcion - vacío por defecto
            "", // foto - vacío por defecto
            identificacion,
            tipoIdentificacion.substring(0, 2), // Extraer solo "CC" o "CE"
            nombre,
            usuario
        );
        
        if (registroExitoso) {
            JOptionPane.showMessageDialog(this,
                "¡Registro exitoso!\n" +
                "Usuario: " + usuario + "\n" +
                "Correo: " + correo + "\n\n" +
                "Ya puede iniciar sesión con sus credenciales.",
                "Registro completado",
                JOptionPane.INFORMATION_MESSAGE);
                
            // Limpiar campos después del registro exitoso
            limpiarCampos();
            
            // Opción: Ir directamente al login
            int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Desea ir a la pantalla de inicio de sesión?",
                "Ir a Login",
                JOptionPane.YES_NO_OPTION);
                
            if (respuesta == JOptionPane.YES_OPTION) {
                btnRegresarActionPerformed(evt);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Ocurrió un error al registrar el usuario.\n" +
                "Por favor, intente nuevamente.",
                "Error en registro",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                            
    
    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        textCorreo.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtUsuarios.setText("");
            txtContrasena.setText(""); // JPasswordField también tiene setText()
        txtIdent.setText("");
        jTipoDeIdentificacionComboBox.setSelectedIndex(0);
        jTerminosCondicionesCheckBox.setSelected(false);
        LabelCorreoInformacion.setText("-");
        LabelUsuarioInforacion.setText("-");
        LabelContraseñaInformacion.setText("-");
        LabelCorreoInformacion.setForeground(Color.BLACK);
        LabelUsuarioInforacion.setForeground(Color.BLACK);
        LabelContraseñaInformacion.setForeground(Color.BLACK);
    }

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // Obtener el window padre para abrir Inicio sobre él
        java.awt.Window parent = getOwner();
        this.dispose();
        if (parent != null) {
            new Inicio(parent).setVisible(true);
        } else {
            new Inicio().setVisible(true);
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
        
        btnCerrar.addActionListener(e -> dispose());
        
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

    private void jTerminosCondicionesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {                                                             
        // Puedes agregar alguna lógica aquí si necesitas hacer algo cuando se marca/desmarca
    }                                                            

    private void jTipoDeIdentificacionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {                                                              
        // Puedes agregar lógica para validar el formato según el tipo de identificación
    }
    
    /**
     * Personaliza el scrollbar con un diseño moderno y atractivo (igual que principal.java)
     */
    private void personalizarScrollBar(JScrollBar scrollBar) {
        scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 120);
                this.trackColor = new Color(248, 248, 248);
                this.thumbDarkShadowColor = new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 180);
                this.thumbLightShadowColor = new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100);
                this.thumbHighlightColor = new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 60);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
            
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }
                
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int w = thumbBounds.width;
                int h = thumbBounds.height;
                int x = thumbBounds.x;
                int y = thumbBounds.y;
                
                // Sombra sutil
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(x + 1, y + 2, w - 2, h - 2, 12, 12);
                
                // Gradiente para el thumb
                GradientPaint gradient = new GradientPaint(
                    x, y, new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 150),
                    x, y + h, new Color(COLOR_SECONDARY.getRed(), COLOR_SECONDARY.getGreen(), COLOR_SECONDARY.getBlue(), 150)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(x, y, w - 1, h - 3, 12, 12);
                
                // Borde sutil
                g2.setStroke(new BasicStroke(1.0f));
                g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 200));
                g2.drawRoundRect(x, y, w - 1, h - 3, 12, 12);
                
                g2.dispose();
            }
            
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(trackColor);
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                
                // Borde sutil a la izquierda
                g2.setColor(new Color(240, 240, 240));
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawLine(trackBounds.x, trackBounds.y, trackBounds.x, trackBounds.y + trackBounds.height);
                
                g2.dispose();
            }
        });
        
        // Ancho del scrollbar
        scrollBar.setPreferredSize(new Dimension(12, 0));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new registro().setVisible(true));
    }

    // Todas las variables están declaradas arriba en la sección de componentes principales
}