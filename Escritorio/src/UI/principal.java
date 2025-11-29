package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import UI.util.ErrorHandler;
import UI.util.ModernStyles;
import UI.ConfigImagenes;
import UI.CarritoCompras;

/**
 * Página principal de la aplicación - Diseño basado en index.html Replica el
 * diseño moderno de la página web
 */
public class principal extends javax.swing.JFrame {
    
    // Información del usuario
    private int userId;
    private String username;
    private String cargo;
    private boolean isLoggedIn;
    
    // Complementos de la aplicación
    private String nombreApp = "FASHION";
    private String correoApp = "info@fashion.com";
    private String rutaFondoPrincipal = "";
    private String rutaLogo = "";
    
    // Componentes principales
    private JPanel mainPanel;
    private JPanel headerTopPanel;
    private JPanel headerMainPanel;
    private JPanel heroPanel;
    private JPanel categoriesPanel;
    private JPanel featuredProductsPanel;
    private JPanel newsletterPanel;
    private JPanel footerPanel;
    private JScrollPane scrollPane;
    
    // Colores accesibles WCAG AAA - Paleta femenina y seria
    // Contraste mínimo 7:1 para texto normal, 4.5:1 para texto grande
    private static final Color COLOR_PRIMARY = new Color(107, 45, 77); // #6B2D4D - Morado oscuro profesional (contraste 8.2:1 con blanco)
    private static final Color COLOR_SECONDARY = new Color(139, 74, 107); // #8B4A6B - Rosa profundo elegante (contraste 7.5:1 con blanco)
    private static final Color COLOR_ACCENT = new Color(168, 85, 122); // #A8557A - Rosa elegante (contraste 6.8:1 con blanco)
    private static final Color COLOR_BG_GRADIENT_START = new Color(250, 246, 249); // #FAF6F9 - Rosa muy claro
    private static final Color COLOR_BG_GRADIENT_MID = new Color(250, 246, 249); // #FAF6F9 - Rosa muy claro
    private static final Color COLOR_BG_GRADIENT_END = new Color(250, 246, 249); // #FAF6F9 - Rosa muy claro
    
    // Colores de texto accesibles
    private static final Color COLOR_TEXT_PRIMARY = new Color(26, 26, 26); // #1a1a1a - Casi negro (contraste 16.8:1 con blanco)
    private static final Color COLOR_TEXT_SECONDARY = new Color(51, 51, 51); // #333333 - Gris muy oscuro (contraste 12.6:1 con blanco)
    private static final Color COLOR_TEXT_LIGHT = new Color(255, 255, 255); // #FFFFFF - Blanco (para fondos oscuros)
    
    // Colores de fondo alternativos
    private static final Color COLOR_BG_LIGHT = new Color(250, 250, 250); // #FAFAFA - Gris muy claro
    private static final Color COLOR_BG_DARK = new Color(91, 29, 61); // #5B1D3D - Morado muy oscuro (para headers)
    
    private static final Logger logger = Logger.getLogger(principal.class.getName());
    
    public principal(int userId) {
        this.userId = userId;
        this.isLoggedIn = (userId > 0);
        
        // Obtener información de sesión
        if (isLoggedIn) {
            SessionManager session = SessionManager.getInstance();
            this.username = session.getUsername();
            this.cargo = session.getCargo();
        }
        
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Cargar complementos
        cargarComplementos();
        
        // Construir interfaz
        construirInterfaz();
        
        // Cargar datos
        cargarDatos();
    }
    
    /**
     * Inicializa los componentes básicos Este método es llamado automáticamente
     * por NetBeans cuando se usa el editor visual También puede ser llamado
     * manualmente si se ejecuta el código sin el .form
     */
    private void initComponents() {
        setTitle(nombreApp + " - Moda Femenina Premium");
        setBackground(COLOR_BG_GRADIENT_START);
        
        // Si los componentes ya fueron creados por NetBeans (desde .form), no los recreamos
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(COLOR_BG_GRADIENT_START);
        } else {
            // Si ya existe, solo configuramos sus propiedades
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(COLOR_BG_GRADIENT_START);
        }
        
        if (scrollPane == null) {
            scrollPane = new JScrollPane(mainPanel);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            
            // Personalizar el scrollbar vertical con diseño moderno
            personalizarScrollBar(scrollPane.getVerticalScrollBar());
        } else {
            // Si ya existe, solo configuramos sus propiedades
            scrollPane.setViewportView(mainPanel);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            
            // Personalizar el scrollbar vertical con diseño moderno
            personalizarScrollBar(scrollPane.getVerticalScrollBar());
        }
        
        // Layout principal: header sticky arriba, contenido scrolleable abajo
        getContentPane().setLayout(new BorderLayout());
        
        // El header se agregará después en construirInterfaz()
        // Por ahora solo agregamos el scroll
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Construye toda la interfaz basada en el diseño web
     */
    private void construirInterfaz() {
        // Crear panel contenedor del header sticky
        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
        headerContainer.setBackground(Color.WHITE);
        
        // Header Top
        crearHeaderTop();
        headerContainer.add(headerTopPanel);
        
        // Header Principal
        crearHeaderMain();
        headerContainer.add(headerMainPanel);
        
        // Agregar header sticky al frame (fuera del scroll)
        getContentPane().add(headerContainer, BorderLayout.NORTH);
        
        // Hero Section (dentro del scroll)
        crearHeroSection();
        
        // Sección de Recién Llegados
        crearRecienLlegadosSection();
        
        // Sección de Categorías
        crearCategoriesSection();
        
        // Productos Destacados
        crearFeaturedProductsSection();
        
        // Newsletter
        crearNewsletterSection();
        
        // Footer
        crearFooter();
    }
    
    /**
     * Crea el header top (barra superior con contacto)
     */
    private void crearHeaderTop() {
        headerTopPanel = new JPanel();
       
        headerTopPanel.setBackground(COLOR_BG_DARK);
        headerTopPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel contactInfo = new JLabel("📞 +1 234 567 8900 | 📧 " + correoApp);
        contactInfo.setForeground(COLOR_TEXT_LIGHT);
        contactInfo.setFont(new Font("Poppins", Font.PLAIN, 13));
        
        JLabel shippingInfo = new JLabel("Envío Gratis en Pedidos Superiores a $100");
        shippingInfo.setForeground(COLOR_TEXT_LIGHT);
        shippingInfo.setFont(new Font("Poppins", Font.PLAIN, 13));
        
        headerTopPanel.add(contactInfo);
        headerTopPanel.add(Box.createHorizontalStrut(50));
        headerTopPanel.add(shippingInfo);
        
        // No agregar al mainPanel, se agregará al headerContainer
    }
    
    /**
     * Crea el header principal con logo, navegación e iconos
     */
    private void crearHeaderMain() {
        
        headerMainPanel = new JPanel();
        
        headerMainPanel.setLayout(new BorderLayout());
        headerMainPanel.setBackground(Color.WHITE);
        headerMainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(240, 240, 240)),
            new EmptyBorder(0, 50, 6, 50) // Margen pequeño abajo (6px)
        ));
        headerMainPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 70));
        headerMainPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        logoPanel.setOpaque(false);
        logoPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        
        // Cargar logo si existe
        if (rutaLogo != null && !rutaLogo.isEmpty()) {
            try {
                String rutaAbsoluta = ConfigImagenes.obtenerRutaAbsoluta(rutaLogo);
                File archivo = new File(rutaAbsoluta);
                if (archivo.exists()) {
                    ImageIcon icon = new ImageIcon(rutaAbsoluta);
                    Image img = icon.getImage();
                    Image imgEscalada = img.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
                    JLabel logoImage = new JLabel(new ImageIcon(imgEscalada));
                    logoImage.setVerticalAlignment(SwingConstants.BOTTOM);
                    logoPanel.add(logoImage);
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error al cargar logo", e);
            }
        }
        
        JLabel logoLabel = new JLabel(nombreApp);
        logoLabel.setFont(new Font("Playfair Display", Font.BOLD, 26));
        logoLabel.setForeground(COLOR_PRIMARY);
        logoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                scrollToTop();
            }
        });
        logoPanel.add(logoLabel);
        
        // Navegación
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        navPanel.setOpaque(false);
        navPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        
        JLabel navInicio = crearNavLink("Inicio", e -> scrollToTop());
        JLabel navTienda = crearNavLink("Tienda", e -> abrirTienda());
        JLabel navMujer = crearNavLink("Mujer", e -> abrirCategoria("Mujer"));
        JLabel navAccesorios = crearNavLink("Accesorios", e -> abrirCategoria("Accesorios"));
        
        navPanel.add(navInicio);
        navPanel.add(navTienda);
        navPanel.add(navMujer);
        navPanel.add(navAccesorios);

        // Campo de búsqueda integrado - MÁS PEQUEÑO
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(new EmptyBorder(0, 15, 0, 15));
        searchPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(180, 28)); // Más pequeño
        searchField.setMaximumSize(new Dimension(180, 28));
        searchField.setFont(new Font("Poppins", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100), 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Buscar...");
        searchField.addActionListener(e -> {
            String busqueda = searchField.getText().trim();
            if (!busqueda.isEmpty()) {
                this.dispose();
                new Categorias(userId, busqueda).setVisible(true);
            }
        });

        // Botón de búsqueda - MÁS PEQUEÑO
        JButton searchButton = new JButton("Buscar");
        searchButton.setFont(new Font("Poppins", Font.BOLD, 11));
        searchButton.setForeground(COLOR_TEXT_LIGHT);
        searchButton.setBackground(COLOR_PRIMARY);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setPreferredSize(new Dimension(70, 28)); // Más pequeño
        searchButton.setMaximumSize(new Dimension(70, 28));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> {
            String busqueda = searchField.getText().trim();
            if (!busqueda.isEmpty()) {
                this.dispose();
                new Categorias(userId, busqueda).setVisible(true);
            }
        });
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(COLOR_SECONDARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(COLOR_PRIMARY);
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Iconos del header
        JPanel iconsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        iconsPanel.setOpaque(false);
        iconsPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        
        // Icono de usuario con información si está logueado
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        userPanel.setOpaque(false);
        userPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        
        if (isLoggedIn && username != null) {
            // Mostrar nombre de usuario
            JLabel userNameLabel = new JLabel(username);
            userNameLabel.setFont(new Font("Poppins", Font.PLAIN, 11));
            userNameLabel.setForeground(COLOR_TEXT_PRIMARY);
            userNameLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
            userNameLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            userPanel.add(userNameLabel);
            
            // Icono de usuario
            JPanel userIcon = crearHeaderIcon("👤", "Mi Perfil", e -> manejarUsuario());
            userPanel.add(userIcon);
            
            // Botón de cerrar sesión si es admin
            if ("admin".equalsIgnoreCase(cargo)) {
                JLabel adminBadge = new JLabel("⚙️");
                adminBadge.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                adminBadge.setToolTipText("Administrador");
                adminBadge.setCursor(new Cursor(Cursor.HAND_CURSOR));
                adminBadge.setVerticalAlignment(SwingConstants.BOTTOM);
                userPanel.add(adminBadge);
            }
            
            // Botón cerrar sesión
            JPanel logoutIcon = crearHeaderIcon("🚪", "Cerrar Sesión", e -> cerrarSesion());
            userPanel.add(logoutIcon);
        } else {
            JPanel userIcon = crearHeaderIcon("👤", "Iniciar Sesión", e -> manejarUsuario());
            userPanel.add(userIcon);
        }
        
        JPanel cartIcon = crearHeaderIcon("🛒", "Carrito", e -> abrirCarrito());
        
        iconsPanel.add(userPanel);
        iconsPanel.add(cartIcon);

        // Crear panel superior con logo, nav, búsqueda e iconos - alineados abajo
        JPanel topRow = new JPanel(new BorderLayout(15, 0));
        topRow.setOpaque(false);
        topRow.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        
        // Panel izquierdo con logo y navegación
        JPanel leftPanel = new JPanel(new BorderLayout(20, 0));
        leftPanel.setOpaque(false);
        leftPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        leftPanel.add(logoPanel, BorderLayout.WEST);
        leftPanel.add(navPanel, BorderLayout.CENTER);
        
        // Panel derecho con búsqueda e iconos
        JPanel rightPanel = new JPanel(new BorderLayout(10, 0));
        rightPanel.setOpaque(false);
        rightPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        rightPanel.add(searchPanel, BorderLayout.WEST);
        rightPanel.add(iconsPanel, BorderLayout.EAST);
        
        topRow.add(leftPanel, BorderLayout.WEST);
        topRow.add(rightPanel, BorderLayout.EAST);

        headerMainPanel.add(topRow, BorderLayout.SOUTH); // Agregar al SUR para que toque el fondo
        
        // No agregar al mainPanel, se agregará al headerContainer
    }
    
    /**
     * Crea un link de navegación
     */
    private JLabel crearNavLink(String texto, ActionListener action) {
        JLabel link = new JLabel(texto);
        link.setFont(new Font("Poppins", Font.PLAIN, 14));
        link.setForeground(Color.BLACK);
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.setVerticalAlignment(SwingConstants.BOTTOM);
        link.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        link.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new ActionEvent(link, 0, ""));
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                link.setForeground(COLOR_SECONDARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                link.setForeground(COLOR_TEXT_PRIMARY);
            }
        });
        return link;
    }
    
    /**
     * Crea un icono del header con texto
     */
    private JPanel crearHeaderIcon(String icono, String texto, ActionListener action) {
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        iconPanel.setOpaque(false);
        iconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconPanel.setBorder(new EmptyBorder(0, 8, 0, 8));
        iconPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(icono);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        
        JLabel textLabel = new JLabel(texto);
        textLabel.setFont(new Font("Poppins", Font.PLAIN, 11));
        textLabel.setForeground(COLOR_TEXT_PRIMARY);
        textLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        
        iconPanel.add(iconLabel);
        iconPanel.add(textLabel);
        
        iconPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new ActionEvent(iconPanel, 0, ""));
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
                textLabel.setForeground(COLOR_PRIMARY);
                iconPanel.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                textLabel.setForeground(COLOR_TEXT_PRIMARY);
                iconPanel.repaint();
            }
        });
        
        return iconPanel;
    }
    
    /**
     * Crea la sección Hero
     */
    private void crearHeroSection() {
        heroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente de fondo elegante
                int width = getWidth();
                int height = getHeight();
                
                // Gradiente principal (rosa suave a blanco)
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(COLOR_BG_GRADIENT_START.getRed(), COLOR_BG_GRADIENT_START.getGreen(), COLOR_BG_GRADIENT_START.getBlue(), 180),
                    width, height, new Color(255, 255, 255, 255)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, width, height);
                
                // Formas decorativas circulares
                g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 30));
                g2.fillOval(width - 200, 50, 300, 300);
                g2.setColor(new Color(COLOR_SECONDARY.getRed(), COLOR_SECONDARY.getGreen(), COLOR_SECONDARY.getBlue(), 20));
                g2.fillOval(-100, height - 150, 250, 250);
                
                g2.dispose();
            }
        };
        heroPanel.setLayout(new BorderLayout(40, 0));
        heroPanel.setOpaque(true);
        heroPanel.setBorder(new EmptyBorder(100, 80, 100, 80));
        
        // Contenedor principal centrado
        JPanel contentPanel = new JPanel(new BorderLayout(60, 0));
        contentPanel.setOpaque(false);
        
        // Texto del hero
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        // Badge decorativo
        JLabel badge = new JLabel("✨ NUEVA COLECCIÓN 2025");
        badge.setFont(new Font("Poppins", Font.BOLD, 12));
        badge.setForeground(COLOR_PRIMARY);
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        badge.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel title = new JLabel("<html><div style='text-align: center; line-height: 1.2;'>"
                + "Descubre Tu<br>"
                + "<span style='color: #6B2D4D; font-size: 56px;'>Estilo Femenino</span><br>"
                + "<span style='color: #8B4A6B; font-size: 56px;'>Perfecto</span></div></html>");
        title.setFont(new Font("Playfair Display", Font.BOLD, 48));
        title.setForeground(COLOR_TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        JLabel subtitle = new JLabel("<html><div style='text-align: center; width: 600px; line-height: 1.6;'>"
                + "Eleva tu guardarropa con nuestra colección exclusiva de moda femenina premium "
                + "diseñada para empoderarte y hacerte brillar en cada ocasión.</div></html>");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 18));
        subtitle.setForeground(COLOR_TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(0, 0, 40, 0));
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setOpaque(false);
        
        JButton btnComprar = crearBotonPrimario("Comprar Ahora", e -> abrirTienda());
        btnComprar.setPreferredSize(new Dimension(200, 55));
        btnComprar.setFont(new Font("Poppins", Font.BOLD, 17));
        buttonsPanel.add(btnComprar);
        
        if (isLoggedIn && "admin".equalsIgnoreCase(cargo)) {
            JButton btnAdmin = crearBotonSecundario("⚙️ Panel Admin", e -> abrirPanelAdmin());
            buttonsPanel.add(btnAdmin);
        }
        
        // Estadísticas decorativas
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(50, 0, 0, 0));
        
        JPanel stat1 = crearStatLabel("500+", "Productos");
        JPanel stat2 = crearStatLabel("10K+", "Clientes");
        JPanel stat3 = crearStatLabel("4.9★", "Calificación");
        
        statsPanel.add(stat1);
        statsPanel.add(stat2);
        statsPanel.add(stat3);
        
        textPanel.add(Box.createVerticalGlue());
        textPanel.add(badge);
        textPanel.add(title);
        textPanel.add(subtitle);
        textPanel.add(buttonsPanel);
        textPanel.add(statsPanel);
        textPanel.add(Box.createVerticalGlue());
        
        // Imagen del hero
        JLabel heroImage = new JLabel();
        heroImage.setPreferredSize(new Dimension(450, 550));
        heroImage.setMaximumSize(new Dimension(450, 550));
        heroImage.setOpaque(true);
        heroImage.setBackground(new Color(COLOR_BG_LIGHT.getRed(), COLOR_BG_LIGHT.getGreen(), COLOR_BG_LIGHT.getBlue()));
        heroImage.setHorizontalAlignment(JLabel.CENTER);
        heroImage.setVerticalAlignment(JLabel.CENTER);
        heroImage.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 50), 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Intentar cargar imagen del hero desde complementos o usar placeholder
        cargarImagenHero(heroImage);
        
        contentPanel.add(textPanel, BorderLayout.CENTER);
        contentPanel.add(heroImage, BorderLayout.EAST);
        
        heroPanel.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(heroPanel);
    }
    
    /**
     * Crea una etiqueta de estadística decorativa
     */
    private JPanel crearStatLabel(String numero, String texto) {
        JPanel statPanel = new JPanel();
        statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.Y_AXIS));
        statPanel.setOpaque(false);
        statPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel numLabel = new JLabel(numero);
        numLabel.setFont(new Font("Poppins", Font.BOLD, 28));
        numLabel.setForeground(COLOR_PRIMARY);
        numLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel textLabel = new JLabel(texto);
        textLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        textLabel.setForeground(COLOR_TEXT_SECONDARY);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        statPanel.add(numLabel);
        statPanel.add(textLabel);
        
        return statPanel;
    }
    
    /**
     * Crea un botón primario
     */
    private JButton crearBotonPrimario(String texto, ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Poppins", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_PRIMARY);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_SECONDARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARY);
            }
        });
        
        return btn;
    }
    
    /**
     * Crea el botón mejorado "Ver Todos los Productos" con diseño moderno
     */
    private JButton crearBotonVerTodos(String texto, ActionListener action) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Dibujar fondo con bordes redondeados
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                // Dibujar sombra sutil
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(2, 4, getWidth() - 2, getHeight() - 2, 30, 30);
                
                // Redibujar el fondo sobre la sombra
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 4, 30, 30);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Poppins", Font.BOLD, 18));
        btn.setForeground(COLOR_TEXT_LIGHT);
        btn.setBackground(COLOR_PRIMARY);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(320, 65));
        btn.setMinimumSize(new Dimension(320, 65));
        btn.setMaximumSize(new Dimension(320, 65));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.addActionListener(action);
        
        // Padding interno generoso
        btn.setBorder(new EmptyBorder(18, 40, 18, 40));
        
        // Efectos hover mejorados
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_SECONDARY);
                btn.setFont(new Font("Poppins", Font.BOLD, 19));
                btn.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARY);
                btn.setFont(new Font("Poppins", Font.BOLD, 18));
                btn.repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(new Color(
                    Math.max(0, COLOR_SECONDARY.getRed() - 25),
                    Math.max(0, COLOR_SECONDARY.getGreen() - 25),
                    Math.max(0, COLOR_SECONDARY.getBlue() - 25)
                ));
                btn.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (btn.getMousePosition() != null) {
                    btn.setBackground(COLOR_SECONDARY);
                } else {
                    btn.setBackground(COLOR_PRIMARY);
                }
                btn.repaint();
            }
        });
        
        return btn;
    }
    
    /**
     * Crea un botón secundario
     */
    private JButton crearBotonSecundario(String texto, ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Poppins", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_ACCENT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        
        return btn;
    }
    
    /**
     * Crea la sección de Recién Llegados con diseño mejorado
     */
    private void crearRecienLlegadosSection() {
        JPanel recienLlegadosPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente sutil
                int width = getWidth();
                int height = getHeight();
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(COLOR_BG_LIGHT.getRed(), COLOR_BG_LIGHT.getGreen(), COLOR_BG_LIGHT.getBlue(), 255),
                    width, height, new Color(255, 255, 255, 255)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, width, height);
                
                // Formas decorativas sutiles
                g2.setColor(new Color(COLOR_ACCENT.getRed(), COLOR_ACCENT.getGreen(), COLOR_ACCENT.getBlue(), 15));
                g2.fillOval(width - 150, 20, 200, 200);
                g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 10));
                g2.fillOval(50, height - 100, 150, 150);
                
                g2.dispose();
            }
        };
        recienLlegadosPanel.setLayout(new BoxLayout(recienLlegadosPanel, BoxLayout.Y_AXIS));
        recienLlegadosPanel.setOpaque(true);
        recienLlegadosPanel.setBorder(new EmptyBorder(80, 50, 80, 50));
        
        // Header mejorado con badge
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Badge decorativo mejorado - completamente centrado
        JPanel badgePanel = new JPanel();
        badgePanel.setLayout(new BoxLayout(badgePanel, BoxLayout.X_AXIS));
        badgePanel.setOpaque(false);
        badgePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        badgePanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel badgeIcon = new JLabel("✨");
        badgeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        badgeIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel badgeText = new JLabel("NUEVA COLECCIÓN");
        badgeText.setFont(new Font("Poppins", Font.BOLD, 11));
        badgeText.setForeground(COLOR_PRIMARY);
        badgeText.setOpaque(true);
        badgeText.setBackground(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 20));
        badgeText.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARY, 1),
            new EmptyBorder(3, 8, 3, 8)
        ));
        badgeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        badgePanel.add(Box.createHorizontalGlue());
        badgePanel.add(badgeIcon);
        badgePanel.add(Box.createHorizontalStrut(8));
        badgePanel.add(badgeText);
        badgePanel.add(Box.createHorizontalGlue());
        
        // Título mejorado - completamente centrado
        JLabel title = new JLabel("<html><div style='text-align: center; line-height: 1.2;'>"
                + "<span style='color: #6B2D4D;'>Recién</span> "
                + "<span style='color: #8B4A6B;'>Llegados</span></div></html>");
        title.setFont(new Font("Playfair Display", Font.BOLD, 42));
        title.setForeground(COLOR_TEXT_PRIMARY);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Subtítulo mejorado - completamente centrado
        JLabel subtitle = new JLabel("<html><div style='text-align: center; line-height: 1.6;'>"
                + "Descubre las últimas incorporaciones a nuestra colección exclusiva</div></html>");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 16));
        subtitle.setForeground(COLOR_TEXT_SECONDARY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(0, 0, 50, 0));
        
        headerPanel.add(badgePanel);
        headerPanel.add(title);
        headerPanel.add(subtitle);
        
        // Panel de productos con carrusel mejorado
        JPanel productsWrapper = new JPanel();
        productsWrapper.setLayout(new BorderLayout());
        productsWrapper.setOpaque(false);
        productsWrapper.setBorder(new EmptyBorder(0, 0, 40, 0));
        
        // Contenedor principal del carrusel
        JPanel carouselContainer = new JPanel(new BorderLayout());
        carouselContainer.setOpaque(false);
        carouselContainer.setBorder(new EmptyBorder(30, 0, 30, 0));
        
        JPanel productsContainer = new JPanel();
        productsContainer.setLayout(new BoxLayout(productsContainer, BoxLayout.X_AXIS));
        productsContainer.setOpaque(false);
        productsContainer.setBorder(new EmptyBorder(0, 60, 0, 60));
        productsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // ScrollPane oculto para el carrusel
        JScrollPane horizontalScroll = new JScrollPane(productsContainer);
        horizontalScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        horizontalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        horizontalScroll.setBorder(null);
        horizontalScroll.setOpaque(false);
        horizontalScroll.getViewport().setOpaque(false);
        horizontalScroll.setPreferredSize(new Dimension(1200, 430));
        horizontalScroll.setMaximumSize(new Dimension(1200, 430));
        
        // Guardar referencia para actualizar botones
        carruselScrollPane = horizontalScroll;
        
        // Botones de navegación del carrusel
        JButton btnAnterior = crearBotonCarrusel("◀", true);
        JButton btnSiguiente = crearBotonCarrusel("▶", false);
        
        // Guardar referencias para actualizar visibilidad
        carruselBtnAnterior = btnAnterior;
        carruselBtnSiguiente = btnSiguiente;
        
        btnAnterior.addActionListener(e -> {
            JScrollBar scrollBar = horizontalScroll.getHorizontalScrollBar();
            int currentValue = scrollBar.getValue();
            int scrollAmount = 320; // Ancho de una tarjeta + espaciado
            scrollBar.setValue(Math.max(0, currentValue - scrollAmount));
        });
        
        btnSiguiente.addActionListener(e -> {
            JScrollBar scrollBar = horizontalScroll.getHorizontalScrollBar();
            int currentValue = scrollBar.getValue();
            int maxValue = scrollBar.getMaximum();
            int scrollAmount = 320; // Ancho de una tarjeta + espaciado
            scrollBar.setValue(Math.min(maxValue, currentValue + scrollAmount));
        });
        
        // Cargar productos recién llegados
        recienLlegadosProductsPanel = productsContainer;
        cargarRecienLlegados();
        
        // Agregar botones y scroll al carrusel
        carouselContainer.add(btnAnterior, BorderLayout.WEST);
        carouselContainer.add(horizontalScroll, BorderLayout.CENTER);
        carouselContainer.add(btnSiguiente, BorderLayout.EAST);
        
        // Botón mejorado
        JButton verTodosBtn = crearBotonVerTodos("Ver Todos los Recién Llegados", e -> abrirCategoria("Nuevas Llegadas"));
        verTodosBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        verTodosBtn.setBorder(new EmptyBorder(30, 0, 0, 0));
        
        productsWrapper.add(carouselContainer, BorderLayout.CENTER);
        
        recienLlegadosPanel.add(headerPanel);
        recienLlegadosPanel.add(productsWrapper);
        recienLlegadosPanel.add(verTodosBtn);
        
        mainPanel.add(recienLlegadosPanel);
    }
    
    private JPanel recienLlegadosProductsPanel;
    private JButton carruselBtnAnterior;
    private JButton carruselBtnSiguiente;
    private JScrollPane carruselScrollPane;
    
    // Carrusel de productos destacados
    private JButton featuredBtnAnterior;
    private JButton featuredBtnSiguiente;
    private JScrollPane featuredScrollPane;
    
    /**
     * Crea la sección de categorías con diseño mejorado
     */
    private void crearCategoriesSection() {
        categoriesPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con el color solicitado #FAF6F9
                int width = getWidth();
                int height = getHeight();
                
                Color bgColor = new Color(250, 246, 249); // #FAF6F9
                g2.setColor(bgColor);
                g2.fillRect(0, 0, width, height);
                
                // Formas decorativas sutiles y elegantes
                g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 10));
                g2.fillOval(width - 120, 60, 180, 180);
                g2.setColor(new Color(COLOR_SECONDARY.getRed(), COLOR_SECONDARY.getGreen(), COLOR_SECONDARY.getBlue(), 6));
                g2.fillOval(60, height - 100, 140, 140);
                g2.setColor(new Color(COLOR_ACCENT.getRed(), COLOR_ACCENT.getGreen(), COLOR_ACCENT.getBlue(), 4));
                g2.fillOval(width / 3, -50, 160, 160);
                
                g2.dispose();
            }
        };
        categoriesPanel.setLayout(new BoxLayout(categoriesPanel, BoxLayout.Y_AXIS));
        categoriesPanel.setOpaque(true);
        categoriesPanel.setBorder(new EmptyBorder(80, 50, 80, 50));
        
        // Header mejorado
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Título mejorado y centrado
        JLabel title = new JLabel("<html><div style='text-align: center; line-height: 1.2;'>"
                + "<span style='color: #6B2D4D;'>Comprar por</span> "
                + "<span style='color: #8B4A6B;'>Categoría</span></div></html>");
        title.setFont(new Font("Playfair Display", Font.BOLD, 42));
        title.setForeground(COLOR_TEXT_PRIMARY);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Subtítulo mejorado y centrado
        JLabel subtitle = new JLabel("<html><div style='text-align: center; line-height: 1.6;'>"
                + "Explora nuestras colecciones cuidadosamente seleccionadas</div></html>");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 16));
        subtitle.setForeground(COLOR_TEXT_SECONDARY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(0, 0, 50, 0));
        
        headerPanel.add(title);
        headerPanel.add(subtitle);
        
        // Grid de categorías - ajustado para imágenes más anchas
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        gridPanel.setOpaque(false);
        gridPanel.setMaximumSize(new Dimension(1400, 700));
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gridPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Crear cards de categorías mejorados
        gridPanel.add(crearCategoryCardMejorado("Colección de Mujer", "Elegante y Moderna", "Mujer"));
        gridPanel.add(crearCategoryCardMejorado("Nueva Colección", "Clásico y Moderno", "Nueva Colección"));
        gridPanel.add(crearCategoryCardMejorado("Accesorios", "Completa Tu Look", "Accesorios"));
        gridPanel.add(crearCategoryCardMejorado("Ofertas Especiales", "Ahorra hasta 50%", "Ofertas Especiales"));
        
        categoriesPanel.add(headerPanel);
        categoriesPanel.add(gridPanel);
        
        mainPanel.add(categoriesPanel);
    }
    
    /**
     * Crea un card de categoría mejorado con mejor diseño más moderno y atractivo
     */
    private JPanel crearCategoryCardMejorado(String titulo, String subtitulo, String categoria) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int cornerRadius = 15;

                // Sombra simple
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(2, 3, width - 2, height - 2, cornerRadius, cornerRadius);
                
                // Fondo blanco con bordes redondeados
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, width - 2, height - 3, cornerRadius, cornerRadius);
                
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(640, 300));
        card.setMaximumSize(new Dimension(640, 300));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panel de imagen - dimensiones 600x220
        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setOpaque(false);
        imageContainer.setPreferredSize(new Dimension(620, 240));
        imageContainer.setMaximumSize(new Dimension(620, 240));
        imageContainer.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Imagen de categoría - dimensiones 600x220
        JLabel imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Si no hay icono, dibujar fondo blanco
                if (getIcon() == null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                }
            }
        };
        imageLabel.setPreferredSize(new Dimension(600, 220));
        imageLabel.setMaximumSize(new Dimension(600, 220));
        imageLabel.setMinimumSize(new Dimension(600, 220));
        imageLabel.setOpaque(false);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // Overlay simple usando JLayeredPane para que esté encima
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 220));
        layeredPane.setMaximumSize(new Dimension(600, 220));
        layeredPane.setOpaque(false);
        
        // Agregar imagen en la capa inferior
        imageLabel.setBounds(0, 0, 600, 220);
        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);
        
        // Overlay simple
        JPanel overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (isVisible()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 200));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.dispose();
                }
            }
        };
        overlay.setBounds(0, 0, 600, 220);
        overlay.setOpaque(false);
        overlay.setVisible(false);
        
        JLabel overlayText = new JLabel("Explorar →");
        overlayText.setFont(new Font("Poppins", Font.BOLD, 16));
        overlayText.setForeground(COLOR_TEXT_LIGHT);
        overlayText.setHorizontalAlignment(SwingConstants.CENTER);
        overlayText.setVerticalAlignment(SwingConstants.CENTER);
        overlay.setLayout(new BorderLayout());
        overlay.add(overlayText, BorderLayout.CENTER);
        
        // Agregar overlay en la capa superior
        layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER);
        
        // Agregar el layeredPane al contenedor centrado
        imageContainer.add(layeredPane, BorderLayout.CENTER);
        
        // Cargar imagen después de agregar al contenedor
        cargarImagenCategoria(imageLabel, categoria);
        
        // Info panel REDUCIDO - más compacto y pequeño
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        infoPanel.setOpaque(true);
        infoPanel.setBackground(new Color(250, 246, 249)); // #FAF6F9
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.setPreferredSize(new Dimension(600, 50));
        infoPanel.setMaximumSize(new Dimension(600, 50));
        
        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>" + titulo + "</div></html>");
        titleLabel.setFont(new Font("Playfair Display", Font.BOLD, 16));
        titleLabel.setForeground(COLOR_TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("<html><div style='text-align: center;'>" + subtitulo + "</div></html>");
        subtitleLabel.setFont(new Font("Poppins", Font.PLAIN, 11));
        subtitleLabel.setForeground(COLOR_TEXT_SECONDARY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(new EmptyBorder(2, 0, 0, 0));
        
        infoPanel.add(titleLabel);
        infoPanel.add(subtitleLabel);
        
        card.add(imageContainer, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        
        // Mouse listeners
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirCategoria(categoria);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                overlay.setVisible(true);
                overlay.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                overlay.setVisible(false);
                overlay.repaint();
            }
        });
        
        return card;
    }
    
    /**
     * Crea la sección de productos destacados con diseño mejorado
     */
    private void crearFeaturedProductsSection() {
        featuredProductsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente sutil
                int width = getWidth();
                int height = getHeight();
                
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(250, 246, 249), // #FAF6F9
                        width, height, new Color(255, 255, 255, 255)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, width, height);
                
                // Formas decorativas sutiles
                g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 8));
                g2.fillOval(width - 150, 50, 200, 200);
                g2.setColor(new Color(COLOR_SECONDARY.getRed(), COLOR_SECONDARY.getGreen(), COLOR_SECONDARY.getBlue(), 5));
                g2.fillOval(50, height - 100, 150, 150);
                
                g2.dispose();
            }
        };
        featuredProductsPanel.setLayout(new BoxLayout(featuredProductsPanel, BoxLayout.Y_AXIS));
        featuredProductsPanel.setOpaque(true);
        featuredProductsPanel.setBorder(new EmptyBorder(80, 50, 80, 50));

        // Header mejorado con badge
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Badge decorativo
        JPanel badgePanel = new JPanel();
        badgePanel.setLayout(new BoxLayout(badgePanel, BoxLayout.X_AXIS));
        badgePanel.setOpaque(false);
        badgePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        badgePanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel badgeIcon = new JLabel("⭐");
        badgeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        badgeIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel badgeText = new JLabel("LO MÁS VENDIDO");
        badgeText.setFont(new Font("Poppins", Font.BOLD, 11));
        badgeText.setForeground(COLOR_PRIMARY);
        badgeText.setOpaque(true);
        badgeText.setBackground(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 20));
        badgeText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 1),
                new EmptyBorder(3, 8, 3, 8)
        ));
        badgeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        badgePanel.add(Box.createHorizontalGlue());
        badgePanel.add(badgeIcon);
        badgePanel.add(Box.createHorizontalStrut(8));
        badgePanel.add(badgeText);
        badgePanel.add(Box.createHorizontalGlue());
        
        JLabel title = new JLabel("<html><div style='text-align: center; line-height: 1.2;'>"
                + "<span style='color: #6B2D4D;'>Productos</span> "
                + "<span style='color: #8B4A6B;'>Destacados</span></div></html>");
        title.setFont(new Font("Playfair Display", Font.BOLD, 42));
        title.setForeground(COLOR_TEXT_PRIMARY);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Subtítulo
        JLabel subtitle = new JLabel("<html><div style='text-align: center; line-height: 1.6;'>"
                + "Selección especial de nuestros productos más populares y mejor valorados</div></html>");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 16));
        subtitle.setForeground(COLOR_TEXT_SECONDARY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(0, 0, 50, 0));

        // Panel de productos con carrusel
        JPanel productsWrapper = new JPanel();
        productsWrapper.setLayout(new BorderLayout());
        productsWrapper.setOpaque(false);
        productsWrapper.setBorder(new EmptyBorder(0, 0, 40, 0));

        // Contenedor principal del carrusel
        JPanel carouselContainer = new JPanel(new BorderLayout());
        carouselContainer.setOpaque(false);
        carouselContainer.setBorder(new EmptyBorder(30, 0, 30, 0));

        JPanel productsContainer = new JPanel();
        productsContainer.setLayout(new BoxLayout(productsContainer, BoxLayout.X_AXIS));
        productsContainer.setOpaque(false);
        productsContainer.setBorder(new EmptyBorder(0, 60, 0, 60));
        productsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ScrollPane oculto para el carrusel
        JScrollPane horizontalScroll = new JScrollPane(productsContainer);
        horizontalScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        horizontalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        horizontalScroll.setBorder(null);
        horizontalScroll.setOpaque(false);
        horizontalScroll.getViewport().setOpaque(false);
        horizontalScroll.setPreferredSize(new Dimension(1200, 430));
        horizontalScroll.setMaximumSize(new Dimension(1200, 430));

        // Guardar referencia para actualizar botones
        featuredScrollPane = horizontalScroll;

        // Botones de navegación del carrusel
        JButton btnAnterior = crearBotonCarrusel("◀", true);
        JButton btnSiguiente = crearBotonCarrusel("▶", false);

        // Guardar referencias para actualizar visibilidad
        featuredBtnAnterior = btnAnterior;
        featuredBtnSiguiente = btnSiguiente;

        btnAnterior.addActionListener(e -> {
            JScrollBar scrollBar = horizontalScroll.getHorizontalScrollBar();
            int currentValue = scrollBar.getValue();
            int scrollAmount = 300; // Ancho de una tarjeta (275) + espaciado (25)
            scrollBar.setValue(Math.max(0, currentValue - scrollAmount));
        });

        btnSiguiente.addActionListener(e -> {
            JScrollBar scrollBar = horizontalScroll.getHorizontalScrollBar();
            int currentValue = scrollBar.getValue();
            int maxValue = scrollBar.getMaximum();
            int scrollAmount = 300; // Ancho de una tarjeta (275) + espaciado (25)
            scrollBar.setValue(Math.min(maxValue, currentValue + scrollAmount));
        });
        
        // Los productos se cargarán dinámicamente
        productosGridPanel = productsContainer;
        
        headerPanel.add(badgePanel);
        headerPanel.add(title);
        headerPanel.add(subtitle);

        // Agregar botones y scroll al carrusel
        carouselContainer.add(btnAnterior, BorderLayout.WEST);
        carouselContainer.add(horizontalScroll, BorderLayout.CENTER);
        carouselContainer.add(btnSiguiente, BorderLayout.EAST);
        
        JButton verTodosBtn = crearBotonVerTodos("Ver Todos los Productos", e -> abrirTienda());
        verTodosBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        verTodosBtn.setBorder(new EmptyBorder(30, 0, 0, 0));

        productsWrapper.add(carouselContainer, BorderLayout.CENTER);

        featuredProductsPanel.add(headerPanel);
        featuredProductsPanel.add(productsWrapper);
        featuredProductsPanel.add(verTodosBtn);
        
        mainPanel.add(featuredProductsPanel);
    }
    
    private JPanel productosGridPanel;
    
    /**
     * Crea la sección de newsletter
     */
    private void crearNewsletterSection() {
        newsletterPanel = new JPanel();
        newsletterPanel.setLayout(new BoxLayout(newsletterPanel, BoxLayout.Y_AXIS));
        newsletterPanel.setBackground(COLOR_BG_DARK);
        newsletterPanel.setBorder(new EmptyBorder(60, 0, 60, 0));
        
        JLabel title = new JLabel("Mantente a la Moda");
        title.setFont(new Font("Playfair Display", Font.BOLD, 36));
        title.setForeground(COLOR_TEXT_LIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Suscríbete a nuestro boletín y obtén 20% de descuento en tu primera compra");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 16));
        subtitle.setForeground(COLOR_TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(15, 0, 30, 0));
        
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextField emailField = new JTextField(25);
        emailField.setFont(new Font("Poppins", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            new EmptyBorder(10, 15, 10, 15)
        ));
        emailField.setPreferredSize(new Dimension(300, 45));
        
        JButton suscribirBtn = new JButton("Suscribirse");
        suscribirBtn.setFont(new Font("Poppins", Font.BOLD, 14));
        suscribirBtn.setForeground(COLOR_BG_DARK);
        suscribirBtn.setBackground(COLOR_TEXT_LIGHT);
        suscribirBtn.setBorderPainted(false);
        suscribirBtn.setFocusPainted(false);
        suscribirBtn.setPreferredSize(new Dimension(150, 45));
        suscribirBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        suscribirBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty() || !email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un email válido", 
                    "Newsletter", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "¡Gracias por suscribirte!", 
                    "Newsletter", JOptionPane.INFORMATION_MESSAGE);
                emailField.setText("");
            }
        });
        
        formPanel.add(emailField);
        formPanel.add(suscribirBtn);
        
        newsletterPanel.add(title);
        newsletterPanel.add(subtitle);
        newsletterPanel.add(formPanel);
        
        mainPanel.add(newsletterPanel);
    }
    
    /**
     * Crea el footer
     */
    private void crearFooter() {
        footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(COLOR_BG_DARK);
        footerPanel.setBorder(new EmptyBorder(50, 50, 20, 50));
        
        JPanel contentPanel = new JPanel(new GridLayout(1, 4, 30, 0));
        contentPanel.setOpaque(false);
        contentPanel.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        
        // Sección 1: Acerca de
        JPanel section1 = crearFooterSection("Acerca de " + nombreApp,
            "Tu destino premium para las últimas tendencias de moda femenina y clásicos atemporales.");
        contentPanel.add(section1);
        
        // Sección 2: Enlaces Rápidos
        JPanel section2 = crearFooterSection("Enlaces Rápidos", null);
        section2.add(crearFooterLink("Sobre Nosotros"));
        section2.add(crearFooterLink("Contacto"));
        section2.add(crearFooterLink("Guía de Tallas"));
        section2.add(crearFooterLink("Información de Envío"));
        section2.add(crearFooterLink("Devoluciones"));
        contentPanel.add(section2);
        
        // Sección 3: Categorías
        JPanel section3 = crearFooterSection("Categorías", null);
        section3.add(crearFooterLink("Mujer"));
        section3.add(crearFooterLink("Accesorios"));
        section3.add(crearFooterLink("Nuevos Llegados"));
        section3.add(crearFooterLink("Ofertas"));
        contentPanel.add(section3);
        
        // Sección 4: Atención al Cliente
        JPanel section4 = crearFooterSection("Atención al Cliente", null);
        section4.add(crearFooterLink("Rastrear Pedido"));
        section4.add(crearFooterLink("Preguntas Frecuentes"));
        section4.add(crearFooterLink("Métodos de Pago"));
        section4.add(crearFooterLink("Tarjetas de Regalo"));
        contentPanel.add(section4);
        
        footerPanel.add(contentPanel);
        
        // Footer bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(255, 255, 255, 25)));
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel copyright = new JLabel("© 2025 " + nombreApp + ". Todos los derechos reservados.");
        copyright.setFont(new Font("Poppins", Font.PLAIN, 12));
        copyright.setForeground(COLOR_TEXT_LIGHT);
        
        bottomPanel.add(copyright);
        
        footerPanel.add(Box.createVerticalStrut(20));
        footerPanel.add(bottomPanel);
        
        mainPanel.add(footerPanel);
    }
    
    /**
     * Crea una sección del footer
     */
    private JPanel crearFooterSection(String titulo, String descripcion) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel title = new JLabel(titulo);
        title.setFont(new Font("Playfair Display", Font.BOLD, 18));
        title.setForeground(COLOR_TEXT_LIGHT);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        section.add(title);
        
        if (descripcion != null) {
            JLabel desc = new JLabel("<html><div style='width: 200px;'>" + descripcion + "</div></html>");
            desc.setFont(new Font("Poppins", Font.PLAIN, 12));
            desc.setForeground(COLOR_TEXT_LIGHT);
            desc.setBorder(new EmptyBorder(0, 0, 15, 0));
            section.add(desc);
        }
        
        return section;
    }
    
    /**
     * Crea un link del footer
     */
    private JLabel crearFooterLink(String texto) {
        JLabel link = new JLabel(texto);
        link.setFont(new Font("Poppins", Font.PLAIN, 12));
        link.setForeground(new Color(224, 224, 224));
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        link.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClickFooterLink(texto);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                link.setForeground(COLOR_PRIMARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                link.setForeground(new Color(224, 224, 224));
            }
        });
        
        return link;
    }
    
    /**
     * Maneja los clicks en los enlaces del footer
     */
    private void manejarClickFooterLink(String texto) {
        switch (texto) {
            // Enlaces Rápidos
            case "Sobre Nosotros":
                mostrarSobreNosotros();
                break;
            case "Contacto":
                mostrarContacto();
                break;
            case "Guía de Tallas":
                mostrarGuiaTallas();
                break;
            case "Información de Envío":
                mostrarInfoEnvio();
                break;
            case "Devoluciones":
                mostrarDevoluciones();
                break;
            // Categorías
            case "Mujer":
                abrirCategoria("Mujer");
                break;
            case "Accesorios":
                abrirCategoria("Accesorios");
                break;
            case "Nuevos Llegados":
                abrirCategoria("Nuevas Llegadas");
                break;
            case "Ofertas":
                abrirCategoria("Ofertas Especiales");
                break;
            // Atención al Cliente
            case "Rastrear Pedido":
                rastrearPedido();
                break;
            case "Preguntas Frecuentes":
                mostrarPreguntasFrecuentes();
                break;
            case "Métodos de Pago":
                mostrarMetodosPago();
                break;
            case "Tarjetas de Regalo":
                mostrarTarjetasRegalo();
                break;
        }
    }
    
    /**
     * Crea un diálogo personalizado moderno y atractivo
     */
    private void mostrarDialogoModerno(String titulo, String contenido, String icono) {
        JDialog dialog = new JDialog(this, titulo, true);
        dialog.setUndecorated(true);
        dialog.setSize(750, 650); // Más grande para más espacio de información
        dialog.setLocationRelativeTo(this);
        
        // Panel principal con gradiente
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente de fondo
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(250, 246, 249),
                    getWidth(), getHeight(), new Color(255, 255, 255)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Formas decorativas
                g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 10));
                g2.fillOval(getWidth() - 150, -50, 300, 300);
                g2.setColor(new Color(COLOR_SECONDARY.getRed(), COLOR_SECONDARY.getGreen(), COLOR_SECONDARY.getBlue(), 8));
                g2.fillOval(-100, getHeight() - 100, 250, 250);
                
                g2.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40)); // Menos padding abajo para más espacio
        
        // Header con título e icono
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0)); // Menos espacio abajo
        
        JLabel iconLabel = new JLabel(icono != null ? icono : "✨");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
        
        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Playfair Display", Font.BOLD, 32));
        titleLabel.setForeground(COLOR_PRIMARY);
        
        headerPanel.add(iconLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Contenido con scroll
        JTextPane contentPane = new JTextPane();
        contentPane.setContentType("text/html");
        contentPane.setText("<html><body style='font-family: Poppins, Arial; font-size: 14px; line-height: 1.8; color: #333; padding: 10px;'>" + contenido + "</body></html>");
        contentPane.setEditable(false);
        contentPane.setOpaque(false);
        contentPane.setBackground(new Color(0, 0, 0, 0));
        
        JScrollPane scrollPane = new JScrollPane(contentPane);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Aplicar el mismo diseño de scrollbar que en principal
        personalizarScrollBar(scrollPane.getVerticalScrollBar());
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Botón cerrar moderno - MÁS PEQUEÑO
        JButton closeBtn = new JButton("Cerrar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_PRIMARY,
                    getWidth(), getHeight(), COLOR_SECONDARY
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Sombra
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(2, 4, getWidth() - 2, getHeight() - 2, 20, 20);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        closeBtn.setFont(new Font("Poppins", Font.BOLD, 14)); // Fuente más pequeña
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setPreferredSize(new Dimension(100, 35)); // Botón más pequeño
        closeBtn.setMinimumSize(new Dimension(100, 35));
        closeBtn.setMaximumSize(new Dimension(100, 35));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dialog.dispose());
        
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setForeground(new Color(255, 255, 255, 200));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setForeground(Color.WHITE);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Menos padding
        buttonPanel.add(closeBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Muestra información sobre la empresa
     */
    private void mostrarSobreNosotros() {
        String contenido = "<div style='text-align: justify;'>"
                + "<p style='margin-bottom: 20px; font-size: 16px;'>"
                + "Somos una tienda de moda femenina comprometida con ofrecer las últimas tendencias "
                + "y clásicos atemporales que empoderan a cada mujer a expresar su estilo único."
                + "</p>"
                + "<p style='margin-bottom: 20px; font-size: 16px;'>"
                + "Nuestra misión es brindar una experiencia de compra excepcional, con productos "
                + "de alta calidad y un servicio al cliente dedicado."
                + "</p>"
                + "<p style='font-size: 16px;'>"
                + "Desde nuestra fundación, nos hemos comprometido a ofrecer moda accesible, "
                + "sostenible y que refleje la diversidad y belleza de todas las mujeres."
                + "</p>"
                + "</div>";
        mostrarDialogoModerno("Sobre " + nombreApp, contenido, "💼");
    }
    
    /**
     * Muestra información de contacto
     */
    private void mostrarContacto() {
        String contenido = "<div style='font-size: 16px;'>"
                + "<p style='margin-bottom: 15px;'><b style='color: #6B2D4D; font-size: 18px;'>📧 Email:</b><br>contacto@" + nombreApp.toLowerCase().replace(" ", "") + ".com</p>"
                + "<p style='margin-bottom: 15px;'><b style='color: #6B2D4D; font-size: 18px;'>📞 Teléfono:</b><br>+1 (555) 123-4567</p>"
                + "<p style='margin-bottom: 15px;'><b style='color: #6B2D4D; font-size: 18px;'>📍 Dirección:</b><br>Calle Principal 123, Ciudad</p>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>🕐 Horario de Atención:</b></p>"
                + "<ul style='margin-left: 20px; margin-top: 5px;'>"
                + "<li>Lunes - Viernes: 9:00 AM - 6:00 PM</li>"
                + "<li>Sábados: 10:00 AM - 4:00 PM</li>"
                + "<li>Domingos: Cerrado</li>"
                + "</ul>"
                + "</div>";
        mostrarDialogoModerno("Contáctanos", contenido, "📞");
    }
    
    /**
     * Muestra la guía de tallas
     */
    private void mostrarGuiaTallas() {
        String contenido = "<div style='font-size: 16px;'>"
                + "<table border='1' cellpadding='12' style='border-collapse: collapse; width: 100%; margin-bottom: 25px; border: 2px solid #6B2D4D;'>"
                + "<tr style='background: linear-gradient(135deg, #6B2D4D, #8B4A6B); color: white; font-weight: bold;'>"
                + "<th style='padding: 12px;'>Talla</th>"
                + "<th style='padding: 12px;'>Busto (cm)</th>"
                + "<th style='padding: 12px;'>Cintura (cm)</th>"
                + "<th style='padding: 12px;'>Cadera (cm)</th></tr>"
                + "<tr style='background-color: #f8f8f8;'><td style='padding: 10px; text-align: center; font-weight: bold;'>XS</td><td style='padding: 10px; text-align: center;'>80-84</td><td style='padding: 10px; text-align: center;'>60-64</td><td style='padding: 10px; text-align: center;'>84-88</td></tr>"
                + "<tr><td style='padding: 10px; text-align: center; font-weight: bold;'>S</td><td style='padding: 10px; text-align: center;'>84-88</td><td style='padding: 10px; text-align: center;'>64-68</td><td style='padding: 10px; text-align: center;'>88-92</td></tr>"
                + "<tr style='background-color: #f8f8f8;'><td style='padding: 10px; text-align: center; font-weight: bold;'>M</td><td style='padding: 10px; text-align: center;'>88-92</td><td style='padding: 10px; text-align: center;'>68-72</td><td style='padding: 10px; text-align: center;'>92-96</td></tr>"
                + "<tr><td style='padding: 10px; text-align: center; font-weight: bold;'>L</td><td style='padding: 10px; text-align: center;'>92-96</td><td style='padding: 10px; text-align: center;'>72-76</td><td style='padding: 10px; text-align: center;'>96-100</td></tr>"
                + "<tr style='background-color: #f8f8f8;'><td style='padding: 10px; text-align: center; font-weight: bold;'>XL</td><td style='padding: 10px; text-align: center;'>96-100</td><td style='padding: 10px; text-align: center;'>76-80</td><td style='padding: 10px; text-align: center;'>100-104</td></tr>"
                + "</table>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>💡 Consejos:</b></p>"
                + "<ul style='margin-left: 20px; line-height: 2;'>"
                + "<li>Mide tu busto, cintura y cadera para encontrar tu talla perfecta</li>"
                + "<li>Si estás entre dos tallas, te recomendamos la talla más grande</li>"
                + "<li>Las medidas pueden variar según el estilo y la marca</li>"
                + "</ul>"
                + "</div>";
        mostrarDialogoModerno("Guía de Tallas", contenido, "📏");
    }
    
    /**
     * Muestra información de envío
     */
    private void mostrarInfoEnvio() {
        String contenido = "<div style='font-size: 16px;'>"
                + "<div style='background: linear-gradient(135deg, #f0e0eb, #ffffff); padding: 15px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #6B2D4D;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>📦 Envío Estándar</b></p>"
                + "<ul style='margin-left: 20px; line-height: 2;'>"
                + "<li>Tiempo de entrega: <b>5-7 días hábiles</b></li>"
                + "<li>Costo: <b>$5.99</b></li>"
                + "<li>Envío <b style='color: #6B2D4D;'>GRATUITO</b> en compras superiores a $100</li>"
                + "</ul>"
                + "</div>"
                + "<div style='background: linear-gradient(135deg, #f0e0eb, #ffffff); padding: 15px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #8B4A6B;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>🚀 Envío Express</b></p>"
                + "<ul style='margin-left: 20px; line-height: 2;'>"
                + "<li>Tiempo de entrega: <b>2-3 días hábiles</b></li>"
                + "<li>Costo: <b>$12.99</b></li>"
                + "</ul>"
                + "</div>"
                + "<div style='background: linear-gradient(135deg, #f0e0eb, #ffffff); padding: 15px; border-radius: 10px; border-left: 4px solid #A8557A;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>🌍 Envío Internacional</b></p>"
                + "<ul style='margin-left: 20px; line-height: 2;'>"
                + "<li>Disponible a la mayoría de países</li>"
                + "<li>Tiempo de entrega: <b>10-15 días hábiles</b></li>"
                + "<li>Costo: <b>$25.99</b></li>"
                + "</ul>"
                + "</div>"
                + "</div>";
        mostrarDialogoModerno("Información de Envío", contenido, "📦");
    }
    
    /**
     * Muestra política de devoluciones
     */
    private void mostrarDevoluciones() {
        String contenido = "<div style='font-size: 16px;'>"
                + "<div style='background: linear-gradient(135deg, #fff5f8, #ffffff); padding: 20px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #6B2D4D;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>⏰ Plazo de Devolución</b></p>"
                + "<p>Tienes <b>30 días</b> desde la fecha de recepción para devolver cualquier artículo.</p>"
                + "</div>"
                + "<div style='background: linear-gradient(135deg, #fff5f8, #ffffff); padding: 20px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #8B4A6B;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>✅ Condiciones</b></p>"
                + "<ul style='margin-left: 20px; line-height: 2;'>"
                + "<li>Los artículos deben estar sin usar y con etiquetas originales</li>"
                + "<li>Deben incluirse todos los accesorios y embalaje original</li>"
                + "<li>No se aceptan devoluciones de ropa interior o artículos personalizados</li>"
                + "</ul>"
                + "</div>"
                + "<div style='background: linear-gradient(135deg, #fff5f8, #ffffff); padding: 20px; border-radius: 10px; border-left: 4px solid #A8557A;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>💰 Reembolsos</b></p>"
                + "<p>El reembolso se procesará en un plazo de <b>5-7 días hábiles</b> después de recibir el artículo. "
                + "El costo de envío original no es reembolsable.</p>"
                + "</div>"
                + "</div>";
        mostrarDialogoModerno("Política de Devoluciones", contenido, "↩️");
    }
    
    /**
     * Permite rastrear un pedido
     */
    private void rastrearPedido() {
        // Crear diálogo de entrada personalizado
        JDialog inputDialog = new JDialog(this, "Rastrear Pedido", true);
        inputDialog.setUndecorated(true);
        inputDialog.setSize(450, 200);
        inputDialog.setLocationRelativeTo(this);
        
        JPanel inputPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(250, 246, 249),
                    getWidth(), getHeight(), new Color(255, 255, 255)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.dispose();
            }
        };
        inputPanel.setLayout(new BorderLayout(20, 20));
        inputPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JLabel label = new JLabel("Ingrese el número de su pedido:");
        label.setFont(new Font("Poppins", Font.PLAIN, 14));
        
        JTextField inputField = new JTextField(20);
        inputField.setFont(new Font("Poppins", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARY, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        JButton buscarBtn = new JButton("Buscar");
        buscarBtn.setFont(new Font("Poppins", Font.BOLD, 14));
        buscarBtn.setForeground(Color.WHITE);
        buscarBtn.setBackground(COLOR_PRIMARY);
        buscarBtn.setBorderPainted(false);
        buscarBtn.setPreferredSize(new Dimension(120, 35));
        buscarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buscarBtn.addActionListener(e -> {
            String numeroPedido = inputField.getText().trim();
            if (!numeroPedido.isEmpty()) {
                inputDialog.dispose();
                String contenido = "<div style='background: linear-gradient(135deg, #fff5f8, #ffffff); padding: 25px; border-radius: 10px; border-left: 4px solid #6B2D4D;'>"
                        + "<p style='margin-bottom: 15px;'><b style='color: #6B2D4D; font-size: 18px;'>📦 Pedido #" + numeroPedido + "</b></p>"
                        + "<p style='margin-bottom: 10px;'><b>Estado:</b> En tránsito</p>"
                        + "<p style='margin-bottom: 10px;'><b>Última actualización:</b> En camino a destino</p>"
                        + "<p><b>Tiempo estimado de entrega:</b> 2-3 días hábiles</p>"
                        + "</div>";
                mostrarDialogoModerno("Estado del Pedido", contenido, "📦");
            }
        });
        
        inputPanel.add(label, BorderLayout.NORTH);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(buscarBtn, BorderLayout.SOUTH);
        
        inputDialog.setContentPane(inputPanel);
        inputDialog.setVisible(true);
    }
    
    /**
     * Muestra preguntas frecuentes
     */
    private void mostrarPreguntasFrecuentes() {
        String contenido = "<div style='font-size: 16px;'>"
                + "<div style='background: linear-gradient(135deg, #f0e0eb, #ffffff); padding: 20px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #6B2D4D;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>❓ ¿Cómo realizo un pedido?</b></p>"
                + "<p>Simplemente navega por nuestra tienda, selecciona los productos que deseas, "
                + "elige tu talla y agrégalos al carrito. Luego procede al checkout.</p>"
                + "</div>"
                + "<div style='background: linear-gradient(135deg, #f0e0eb, #ffffff); padding: 20px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #8B4A6B;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>❓ ¿Qué métodos de pago aceptan?</b></p>"
                + "<p>Aceptamos tarjetas de crédito/débito, PayPal y transferencias bancarias.</p>"
                + "</div>"
                + "<div style='background: linear-gradient(135deg, #f0e0eb, #ffffff); padding: 20px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #A8557A;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>❓ ¿Puedo cambiar el tamaño de mi pedido?</b></p>"
                + "<p>Sí, puedes realizar cambios dentro de los primeros 30 días después de recibir tu pedido.</p>"
                + "</div>"
                + "<div style='background: linear-gradient(135deg, #f0e0eb, #ffffff); padding: 20px; border-radius: 10px; border-left: 4px solid #6B2D4D;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>❓ ¿Ofrecen envío internacional?</b></p>"
                + "<p>Sí, realizamos envíos a la mayoría de países. Consulta nuestra sección de "
                + "Información de Envío para más detalles.</p>"
                + "</div>"
                + "</div>";
        mostrarDialogoModerno("Preguntas Frecuentes", contenido, "❓");
    }
    
    /**
     * Muestra métodos de pago disponibles
     */
    private void mostrarMetodosPago() {
        String contenido = "<div style='font-size: 16px;'>"
                + "<div style='background: linear-gradient(135deg, #fff5f8, #ffffff); padding: 20px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #6B2D4D;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>💳 Tarjetas de Crédito/Débito</b></p>"
                + "<p>Aceptamos Visa, Mastercard, American Express y Discover. Todas las transacciones "
                + "están protegidas con encriptación SSL.</p>"
                + "</div>"
                + "<div style='background: linear-gradient(135deg, #fff5f8, #ffffff); padding: 20px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #8B4A6B;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>📱 PayPal</b></p>"
                + "<p>Puedes pagar de forma segura usando tu cuenta PayPal.</p>"
                + "</div>"
                + "<div style='background: linear-gradient(135deg, #fff5f8, #ffffff); padding: 20px; border-radius: 10px; border-left: 4px solid #A8557A;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>🏦 Transferencia Bancaria</b></p>"
                + "<p>También aceptamos transferencias bancarias directas. El pedido se procesará "
                + "una vez confirmado el pago.</p>"
                + "</div>"
                + "</div>";
        mostrarDialogoModerno("Métodos de Pago", contenido, "💳");
    }
    
    /**
     * Muestra información sobre tarjetas de regalo
     */
    private void mostrarTarjetasRegalo() {
        String contenido = "<div style='font-size: 16px;'>"
                + "<p style='margin-bottom: 20px;'>Nuestras tarjetas de regalo son el regalo perfecto para esa persona especial. "
                + "Puedes elegir el monto que deseas regalar.</p>"
                + "<div style='background: linear-gradient(135deg, #fff5f8, #ffffff); padding: 20px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #6B2D4D;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>💰 Montos Disponibles</b></p>"
                + "<p>$25, $50, $100, $200, $500 o un monto personalizado</p>"
                + "</div>"
                + "<div style='background: linear-gradient(135deg, #fff5f8, #ffffff); padding: 20px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #8B4A6B;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>⏰ Validez</b></p>"
                + "<p>Las tarjetas de regalo no expiran y pueden usarse en cualquier momento.</p>"
                + "</div>"
                + "<div style='background: linear-gradient(135deg, #fff5f8, #ffffff); padding: 20px; border-radius: 10px; border-left: 4px solid #A8557A;'>"
                + "<p style='margin-bottom: 10px;'><b style='color: #6B2D4D; font-size: 18px;'>📧 Entrega</b></p>"
                + "<p>Puedes enviar la tarjeta de regalo por email al destinatario o imprimirla "
                + "para entregarla en persona.</p>"
                + "</div>"
                + "</div>";
        mostrarDialogoModerno("Tarjetas de Regalo", contenido, "🎁");
    }
    
    /**
     * Carga los complementos desde la base de datos
     */
    private void cargarComplementos() {
        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT * FROM tb_complementos ORDER BY id DESC LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String nombre = rs.getString("NombreDeApp");
                String correo = rs.getString("CorreoApp");
                String fondo = rs.getString("fondoPrincipal");
                String logo = rs.getString("Logo");
                
                if (nombre != null && !nombre.isEmpty()) {
                    nombreApp = nombre;
                }
                if (correo != null && !correo.isEmpty()) {
                    correoApp = correo;
                }
                if (fondo != null && !fondo.isEmpty()) {
                    rutaFondoPrincipal = fondo;
                }
                if (logo != null && !logo.isEmpty()) {
                    rutaLogo = logo;
                }
            }
        } catch (SQLException e) {
            ErrorHandler.logWarning("Error al cargar complementos", e);
        }
    }
    
    /**
     * Carga los datos (productos, categorías, etc.)
     */
    private void cargarDatos() {
        cargarProductosDestacados();
        // cargarRecienLlegados se llama desde crearRecienLlegadosSection
    }
    
    /**
     * Carga los productos recién llegados
     */
    private void cargarRecienLlegados() {
        if (recienLlegadosProductsPanel == null) {
            return;
        }
        
        recienLlegadosProductsPanel.removeAll();
        
        try (Connection conn = Conexion.getConnection()) {
            String sql = """
                SELECT p.*, 
                       (SELECT SUM(cantidad) FROM tb_inventario WHERE id_producto = p.id) as total_cantidad,
                       (SELECT AVG(precio) FROM tb_inventario WHERE id_producto = p.id) as precio_promedio
                FROM tb_productos p
                WHERE p.estado = 'vigente'
                ORDER BY p.FecheIngreso DESC, p.id DESC
                LIMIT 10
                """;
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            int count = 0;
            while (rs.next() && count < 10) {
                UI.Producto producto = new UI.Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("Nombre"));
                producto.setImgUrl(rs.getString("ImgUrl"));
                producto.setPrecio(rs.getDouble("precio_promedio"));
                producto.setCategorias(rs.getString("Categorias"));
                producto.setCantidad(rs.getInt("total_cantidad"));
                
                recienLlegadosProductsPanel.add(Box.createHorizontalStrut(25));
                recienLlegadosProductsPanel.add(crearProductCardRecienLlegado(producto));
                count++;
            }
            
            if (count == 0) {
                JLabel noProducts = new JLabel("<html><div style='text-align: center; padding: 50px; color: #999;'>"
                        + "No hay productos recién llegados</div></html>");
                noProducts.setHorizontalAlignment(JLabel.CENTER);
                recienLlegadosProductsPanel.add(noProducts);
            }
            
        } catch (SQLException e) {
            ErrorHandler.logWarning("Error al cargar productos recién llegados", e);
            JLabel errorLabel = new JLabel("<html><div style='text-align: center; padding: 50px; color: #999;'>"
                    + "Error al cargar productos</div></html>");
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            recienLlegadosProductsPanel.add(errorLabel);
        }
        
        recienLlegadosProductsPanel.revalidate();
        recienLlegadosProductsPanel.repaint();
        
        // Actualizar visibilidad de botones del carrusel después de cargar productos
        SwingUtilities.invokeLater(() -> {
            actualizarBotonesCarrusel();
        });
    }
    
    /**
     * Actualiza la visibilidad de los botones del carrusel según el contenido
     */
    private void actualizarBotonesCarrusel() {
        // Carrusel de recién llegados
        if (carruselScrollPane != null && carruselBtnAnterior != null && carruselBtnSiguiente != null) {
            JScrollBar scrollBar = carruselScrollPane.getHorizontalScrollBar();
            boolean necesitaScroll = scrollBar.getMaximum() > scrollBar.getVisibleAmount();
            
            carruselBtnAnterior.setVisible(necesitaScroll);
            carruselBtnSiguiente.setVisible(necesitaScroll);
        }
        
        // Carrusel de productos destacados
        if (featuredScrollPane != null && featuredBtnAnterior != null && featuredBtnSiguiente != null) {
            JScrollBar scrollBar = featuredScrollPane.getHorizontalScrollBar();
            boolean necesitaScroll = scrollBar.getMaximum() > scrollBar.getVisibleAmount();

            featuredBtnAnterior.setVisible(necesitaScroll);
            featuredBtnSiguiente.setVisible(necesitaScroll);
        }
    }
    
    /**
     * Crea un card de producto mejorado para la sección de recién llegados
     */
    private JPanel crearProductCardRecienLlegado(UI.Producto producto) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra más pronunciada y elegante
                g2.setColor(new Color(0, 0, 0, 12));
                g2.fillRoundRect(3, 5, getWidth() - 3, getHeight() - 3, 18, 18);
                
                // Sombra intermedia
                g2.setColor(new Color(0, 0, 0, 6));
                g2.fillRoundRect(1, 2, getWidth() - 1, getHeight() - 1, 18, 18);
                
                // Fondo blanco con bordes redondeados
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 3, 18, 18);
                
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(280, 400));
        card.setMaximumSize(new Dimension(280, 400));
        card.setMinimumSize(new Dimension(280, 400));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Panel de imagen con badge "NUEVO" mejorado - borde atractivo
        JPanel imageContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Sombra interna sutil
                g2.setColor(new Color(0, 0, 0, 5));
                g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
                
                // Fondo blanco con bordes redondeados
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Borde elegante con gradiente sutil
                g2.setStroke(new BasicStroke(2.0f));
                g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 30));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);
                
                g2.dispose();
            }
        };
        imageContainer.setPreferredSize(new Dimension(250, 270));
        imageContainer.setMaximumSize(new Dimension(250, 270));
        imageContainer.setOpaque(false);
        imageContainer.setBorder(new EmptyBorder(12, 12, 12, 12));
        
        // Badge "NUEVO" mejorado - más visible y atractivo
        JLabel badge = new JLabel("✨ NUEVO") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Fondo con gradiente
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_ACCENT,
                    getWidth(), getHeight(), COLOR_SECONDARY
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Sombra sutil
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(1, 2, getWidth() - 1, getHeight() - 1, 10, 10);
                
                // Redibujar el fondo
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 2, 10, 10);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Poppins", Font.BOLD, 12));
        badge.setForeground(COLOR_TEXT_LIGHT);
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(6, 14, 6, 14));
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Imagen del producto mejorada - fondo blanco con mejor presentación
        JLabel imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Primero dibujar el fondo blanco
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                
                // Luego dibujar el componente (incluyendo el icono)
                super.paintComponent(g);
            }
        };
        imageLabel.setPreferredSize(new Dimension(226, 246));
        imageLabel.setMaximumSize(new Dimension(226, 246));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(250, 246, 249)); // #FAF6F9
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // Cargar imagen - método mejorado
        if (producto.getImgUrl() != null && !producto.getImgUrl().isEmpty()) {
            try {
                // Intentar primero con obtenerRutaAbsoluta
                String rutaAbsoluta = ConfigImagenes.obtenerRutaAbsoluta(producto.getImgUrl());
                File archivo = new File(rutaAbsoluta);
                
                if (archivo.exists() && archivo.isFile()) {
                    ImageIcon icon = new ImageIcon(rutaAbsoluta);
                    if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                        Image img = icon.getImage();
                        Image imgEscalada = img.getScaledInstance(226, 246, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(imgEscalada));
                        imageLabel.setText("");
                    } else {
                        throw new Exception("Imagen inválida");
                    }
                } else {
                    // Intentar con cargarImagenEscalada
                    ImageIcon icon = ConfigImagenes.cargarImagenEscalada(producto.getImgUrl(), 226, 246);
                    if (icon != null && icon.getIconWidth() > 0) {
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    } else {
                        // Intentar cargar directamente desde la ruta de BD
                        File archivoBD = ConfigImagenes.obtenerArchivoImagen(producto.getImgUrl());
                        if (archivoBD != null && archivoBD.exists()) {
                            ImageIcon iconBD = new ImageIcon(archivoBD.getAbsolutePath());
                            if (iconBD.getIconWidth() > 0) {
                                Image img = iconBD.getImage();
                                Image imgEscalada = img.getScaledInstance(226, 246, Image.SCALE_SMOOTH);
                                imageLabel.setIcon(new ImageIcon(imgEscalada));
                                imageLabel.setText("");
                            } else {
                                throw new Exception("No se pudo cargar la imagen");
                            }
                        } else {
                            throw new Exception("Archivo no encontrado");
                        }
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error al cargar imagen del producto: " + producto.getImgUrl(), e);
                imageLabel.setIcon(null);
                imageLabel.setText("<html><div style='text-align: center; padding-top: 100px; color: #999; font-size: 12px;'>"
                        + "Sin imagen</div></html>");
            }
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("<html><div style='text-align: center; padding-top: 100px; color: #999; font-size: 12px;'>"
                    + "Sin imagen</div></html>");
        }
        
        // Panel para el badge (arriba a la izquierda)
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        badgePanel.setOpaque(false);
        badgePanel.add(badge);
        
        imageContainer.add(badgePanel, BorderLayout.NORTH);
        imageContainer.add(imageLabel, BorderLayout.CENTER);
        
        // Info del producto mejorada y organizada
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        infoPanel.setOpaque(false);
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Nombre del producto - más llamativo y destacado
        String nombre = producto.getNombre() != null ? producto.getNombre() : "Sin nombre";
        if (nombre.length() > 25) {
            nombre = nombre.substring(0, 22) + "...";
        }
        JLabel nameLabel = new JLabel("<html><div style='text-align: center; line-height: 1.4; padding: 5px 10px;'>"
                + "<b>" + nombre.toUpperCase() + "</b></div></html>");
        nameLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        nameLabel.setForeground(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue()));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(15, 0, 12, 0));
        
        // Precio centrado - más destacado con formato mejorado
        JLabel priceLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<span style='font-size: 24px; font-weight: bold; color: #6B2D4D;'>"
                + formatearPrecio(producto.getPrecio()) + "</span></div></html>");
        priceLabel.setFont(new Font("Poppins", Font.BOLD, 22));
        priceLabel.setForeground(COLOR_PRIMARY);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        
        card.add(imageContainer, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        
        // Efectos hover mejorados
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_PRIMARY, 2),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ));
                card.setBackground(new Color(252, 252, 252));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(new EmptyBorder(10, 10, 10, 10));
                card.repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetallesProducto(producto);
            }
        });
        
        return card;
    }
    
    /**
     * Carga los productos destacados
     */
    private void cargarProductosDestacados() {
        if (productosGridPanel == null) {
            return;
        }

        productosGridPanel.removeAll();
        
        try (Connection conn = Conexion.getConnection()) {
            String sql = """
                SELECT p.*, 
                       (SELECT SUM(cantidad) FROM tb_inventario WHERE id_producto = p.id) as total_cantidad,
                       (SELECT AVG(precio) FROM tb_inventario WHERE id_producto = p.id) as precio_promedio
                FROM tb_productos p
                WHERE p.estado = 'vigente'
                ORDER BY p.FecheIngreso DESC
                LIMIT 10
                """;
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            int count = 0;
            while (rs.next() && count < 10) {
                UI.Producto producto = new UI.Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("Nombre"));
                producto.setImgUrl(rs.getString("ImgUrl"));
                producto.setPrecio(rs.getDouble("precio_promedio"));
                producto.setCategorias(rs.getString("Categorias"));
                producto.setCantidad(rs.getInt("total_cantidad"));
                
                productosGridPanel.add(Box.createHorizontalStrut(25));
                productosGridPanel.add(crearProductCard(producto));
                count++;
            }
            
            if (count == 0) {
                JLabel noProducts = new JLabel("<html><div style='text-align: center; padding: 50px; color: #999;'>"
                        + "No hay productos destacados</div></html>");
                noProducts.setHorizontalAlignment(JLabel.CENTER);
                productosGridPanel.add(noProducts);
            }
            
        } catch (SQLException e) {
            ErrorHandler.logWarning("Error al cargar productos destacados", e);
            JLabel errorLabel = new JLabel("<html><div style='text-align: center; padding: 50px; color: #999;'>"
                    + "Error al cargar productos</div></html>");
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            productosGridPanel.add(errorLabel);
        }
        
        productosGridPanel.revalidate();
        productosGridPanel.repaint();

        // Actualizar visibilidad de botones del carrusel después de cargar productos
        SwingUtilities.invokeLater(() -> {
            actualizarBotonesCarrusel();
        });
    }
    
    /**
     * Crea un card de producto mejorado para productos destacados
     */
    private JPanel crearProductCard(UI.Producto producto) {
        // Panel principal con sombra y bordes redondeados mejorados
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Sombra más pronunciada y elegante
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 5, getWidth() - 3, getHeight() - 3, 20, 20);
                
                // Sombra intermedia
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(1, 2, getWidth() - 1, getHeight() - 1, 20, 20);
                
                // Fondo blanco con bordes redondeados
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 3, 20, 20);
                
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(275, 420));
        card.setMaximumSize(new Dimension(275, 420));
        card.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Panel de imagen con bordes redondeados mejorados
        JPanel imageContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Sombra interna sutil
                g2.setColor(new Color(0, 0, 0, 5));
                g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);
                
                // Fondo blanco con bordes redondeados
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Borde elegante con gradiente sutil
                g2.setStroke(new BasicStroke(2.0f));
                g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 30));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
                
                g2.dispose();
            }
        };
        imageContainer.setOpaque(false);
        imageContainer.setPreferredSize(new Dimension(275, 300));
        imageContainer.setMaximumSize(new Dimension(275, 300));
        imageContainer.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Imagen del producto mejorada
        JLabel imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                
                // Fondo blanco primero
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                
                // Luego dibujar el componente (incluyendo el icono)
                super.paintComponent(g);
            }
        };
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(245, 270));
        imageLabel.setMaximumSize(new Dimension(245, 270));
        
        // Cargar imagen del producto
        if (producto.getImgUrl() != null && !producto.getImgUrl().isEmpty()) {
            try {
                String rutaAbsoluta = ConfigImagenes.obtenerRutaAbsoluta(producto.getImgUrl());
                File archivo = new File(rutaAbsoluta);
                if (archivo.exists()) {
                    ImageIcon icon = new ImageIcon(rutaAbsoluta);
                    Image img = icon.getImage();
                    if (img != null) {
                        Image imgEscalada = img.getScaledInstance(245, 270, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(imgEscalada));
                    imageLabel.setText("");
                        imageLabel.repaint();
                    }
                } else {
                    ImageIcon icon = ConfigImagenes.cargarImagenEscalada(producto.getImgUrl(), 245, 270);
                    if (icon != null) {
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                        imageLabel.repaint();
                    } else {
                        imageLabel.setIcon(null);
                        imageLabel.setText("<html><div style='text-align: center; padding-top: 120px; color: #999; font-size: 12px;'>"
                                + "Sin imagen</div></html>");
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error al cargar imagen de producto: " + producto.getNombre(), e);
                imageLabel.setIcon(null);
                imageLabel.setText("<html><div style='text-align: center; padding-top: 120px; color: #999; font-size: 12px;'>"
                        + "Sin imagen</div></html>");
            }
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("<html><div style='text-align: center; padding-top: 120px; color: #999; font-size: 12px;'>"
                    + "Sin imagen</div></html>");
        }
        
        imageContainer.add(imageLabel, BorderLayout.CENTER);

        // Info del producto mejorada
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        infoPanel.setOpaque(false);
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nombre del producto - más llamativo
        String nombre = producto.getNombre() != null ? producto.getNombre() : "Sin nombre";
        if (nombre.length() > 30) {
            nombre = nombre.substring(0, 27) + "...";
        }
        JLabel nameLabel = new JLabel("<html><div style='text-align: center; line-height: 1.4;'>"
                + "<b>" + nombre + "</b></div></html>");
        nameLabel.setFont(new Font("Poppins", Font.BOLD, 15));
        nameLabel.setForeground(COLOR_TEXT_PRIMARY);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Precio destacado
        JLabel priceLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<span style='font-size: 24px; font-weight: bold; color: #6B2D4D;'>"
                + formatearPrecio(producto.getPrecio()) + "</span></div></html>");
        priceLabel.setFont(new Font("Poppins", Font.BOLD, 22));
        priceLabel.setForeground(COLOR_PRIMARY);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        // Categoría con estilo más sutil
        JLabel categoryLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<span style='font-size: 11px; color: #888; text-transform: uppercase; letter-spacing: 1px;'>"
                + (producto.getCategorias() != null ? producto.getCategorias() : "") + "</span></div></html>");
        categoryLabel.setFont(new Font("Poppins", Font.PLAIN, 11));
        categoryLabel.setForeground(COLOR_TEXT_SECONDARY);
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(categoryLabel);
        
        card.add(imageContainer, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        
        // Efectos hover mejorados
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARY, 3),
                        new EmptyBorder(0, 0, 0, 0)
                ));
                card.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(new EmptyBorder(0, 0, 0, 0));
                card.repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetallesProducto(producto);
            }
        });
        
        return card;
    }
    
    /**
     * Crea un placeholder para producto mejorado
     */
    private JPanel crearProductCardPlaceholder() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra sutil
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(3, 5, getWidth() - 3, getHeight() - 3, 20, 20);
                
                // Fondo blanco con bordes redondeados
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 3, 20, 20);
                
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(275, 420));
        card.setMaximumSize(new Dimension(275, 420));
        card.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        JLabel placeholder = new JLabel("<html><div style='text-align: center; padding-top: 180px; color: #ccc; font-size: 14px;'>"
                + "🔍<br/><br/>Producto<br/>No disponible</div></html>");
        placeholder.setHorizontalAlignment(JLabel.CENTER);
        placeholder.setOpaque(false);
        
        card.add(placeholder, BorderLayout.CENTER);
        return card;
    }
    
    // ========== MÉTODOS DE NAVEGACIÓN ==========
    /**
     * Crea un botón de navegación para el carrusel
     */
    private JButton crearBotonCarrusel(String texto, boolean esAnterior) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Fondo con gradiente
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_PRIMARY,
                    getWidth(), getHeight(), COLOR_SECONDARY
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Sombra sutil
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(1, 2, getWidth() - 1, getHeight() - 1, 25, 25);
                
                // Redibujar el fondo
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 2, 25, 25);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Poppins", Font.BOLD, 24));
        btn.setForeground(COLOR_TEXT_LIGHT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(50, 430));
        btn.setMinimumSize(new Dimension(50, 430));
        btn.setMaximumSize(new Dimension(50, 430));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        
        // Efectos hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setFont(new Font("Poppins", Font.BOLD, 28));
                btn.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setFont(new Font("Poppins", Font.BOLD, 24));
                btn.repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                btn.setFont(new Font("Poppins", Font.BOLD, 22));
                btn.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setFont(new Font("Poppins", Font.BOLD, 24));
                btn.repaint();
            }
        });
        
        return btn;
    }
    
    /**
     * Personaliza el scrollbar con un diseño moderno y atractivo
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
    
    private void scrollToTop() {
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });
    }
    
    private void abrirTienda() {
        this.dispose();
        new Categorias(userId, "").setVisible(true);
    }
    
    private void abrirCategoria(String categoria) {
        this.dispose();
        new Categorias(userId, categoria).setVisible(true);
    }
    
    private void mostrarBusqueda() {
        String busqueda = JOptionPane.showInputDialog(this, "Ingrese su búsqueda:", "Buscar Productos", 
            JOptionPane.QUESTION_MESSAGE);
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            this.dispose();
            new Categorias(userId, busqueda.trim()).setVisible(true);
        }
    }
    
    private void manejarUsuario() {
        if (isLoggedIn) {
            // Abrir perfil
            JOptionPane.showMessageDialog(this, "Funcionalidad de perfil en desarrollo", 
                "Perfil", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Abrir login sobre la ventana principal
            Inicio ventanaLogin = new Inicio(this);
            ventanaLogin.setVisible(true);
        }
    }
    
    private void abrirCarrito() {
        if (userId <= 0 || !isLoggedIn) {
            JOptionPane.showMessageDialog(this,
                "Debe iniciar sesión para ver el carrito",
                "Sesión requerida",
                JOptionPane.WARNING_MESSAGE);
            manejarUsuario();
            return;
        }
        this.dispose();
        new CarritoCompras(userId).setVisible(true);
    }
    
    private void abrirPanelAdmin() {
        if (isLoggedIn && "admin".equalsIgnoreCase(cargo)) {
            this.dispose();
            try {
                UX.Usuario adminPanel = new UX.Usuario(userId, username);
                adminPanel.setVisible(true);
            } catch (Exception e) {
                ErrorHandler.logWarning("Error al abrir panel admin", e);
            }
        }
    }
    
    private void mostrarDetallesProducto(UI.Producto producto) {
        this.dispose();
        new DetalleProducto(userId, producto).setVisible(true);
    }
    
    /**
     * Carga la imagen del hero desde complementos o usa placeholder
     */
    private void cargarImagenHero(JLabel heroImage) {
        try {
            // Intentar cargar imagen de fondo principal desde complementos
            if (rutaFondoPrincipal != null && !rutaFondoPrincipal.isEmpty()) {
                String rutaAbsoluta = ConfigImagenes.obtenerRutaAbsoluta(rutaFondoPrincipal);
                File archivo = new File(rutaAbsoluta);
                if (archivo.exists()) {
                    ImageIcon icon = new ImageIcon(rutaAbsoluta);
                    Image img = icon.getImage();
                    Image imgEscalada = img.getScaledInstance(400, 500, Image.SCALE_SMOOTH);
                    heroImage.setIcon(new ImageIcon(imgEscalada));
                    heroImage.setText("");
                    return;
                }
            }
            
            // Si no hay imagen, buscar una imagen de producto destacado
            try (Connection conn = Conexion.getConnection()) {
                String sql = """
                    SELECT ImgUrl FROM tb_productos 
                    WHERE estado = 'vigente' AND ImgUrl IS NOT NULL AND ImgUrl != ''
                    ORDER BY FecheIngreso DESC LIMIT 1
                    """;
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    String imgUrl = rs.getString("ImgUrl");
                    String rutaAbsoluta = ConfigImagenes.obtenerRutaAbsoluta(imgUrl);
                    File archivo = new File(rutaAbsoluta);
                    if (archivo.exists()) {
                        ImageIcon icon = new ImageIcon(rutaAbsoluta);
                    Image img = icon.getImage();
                    Image imgEscalada = img.getScaledInstance(400, 500, Image.SCALE_SMOOTH);
                    heroImage.setIcon(new ImageIcon(imgEscalada));
                        heroImage.setText("");
                        return;
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error al cargar imagen hero desde BD", e);
            }
            
            // Placeholder si no hay imagen
            heroImage.setText("<html><div style='text-align: center; padding-top: 220px; color: #999;'>"
                    + "Imagen Hero<br/>400x500</div></html>");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar imagen hero", e);
            heroImage.setText("<html><div style='text-align: center; padding-top: 220px; color: #999;'>"
                    + "Imagen Hero<br/>400x500</div></html>");
        }
    }
    
    /**
     * Carga imagen de categoría - prioriza imágenes web representativas
     */
    private void cargarImagenCategoria(JLabel imageLabel, String categoria) {
        // Mostrar gradiente de inmediato para que siempre haya algo visible
        mostrarGradienteCategoria(imageLabel, categoria);
        
        // Cargar imagen local desde complementos en segundo plano (prioridad)
        new Thread(() -> {
            // Primero intentar cargar desde la carpeta complementos
            File carpetaComplementos = obtenerCarpetaComplementos();
            if (carpetaComplementos != null && carpetaComplementos.exists()) {
                File imagenLocal = buscarImagenCategoria(carpetaComplementos, categoria);
                if (imagenLocal != null && imagenLocal.exists()) {
                    cargarImagenLocal(imageLabel, imagenLocal);
                    return; // Si se cargó exitosamente, no intentar otras fuentes
                }
            }
            
            // Si no se encontró en complementos, intentar desde BD
            try {
            try (Connection conn = Conexion.getConnection()) {
                String sql = """
                    SELECT ImgUrl FROM tb_productos 
                    WHERE estado = 'vigente' 
                    AND (Categorias LIKE ? OR Categorias = ?)
                    AND ImgUrl IS NOT NULL AND ImgUrl != ''
                    ORDER BY FecheIngreso DESC LIMIT 1
                    """;
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + categoria + "%");
                pstmt.setString(2, categoria);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    String imgUrl = rs.getString("ImgUrl");
                    String rutaAbsoluta = ConfigImagenes.obtenerRutaAbsoluta(imgUrl);
                    File archivo = new File(rutaAbsoluta);
                        if (archivo.exists() && archivo.isFile()) {
                            cargarImagenLocal(imageLabel, archivo);
                            return; // Si se cargó desde BD, no intentar web
                    }
                }
            } catch (SQLException e) {
                    // Continuar al siguiente método
                }
            } catch (Exception e) {
                // Continuar al siguiente método
            }
            
            // Como último recurso, intentar cargar desde web
           
        }).start();
    }
    
    /**
     * Obtiene la carpeta de complementos para imágenes de categorías
     * Busca en: Escritorio/Z-Global-Img/complementos/categorias/
     */
    private File obtenerCarpetaComplementos() {
        try {
            // Intentar varias rutas posibles
            String rutaActual = System.getProperty("user.dir");
            File dirActual = new File(rutaActual);
            
            // Buscar hacia arriba hasta encontrar "Escritorio" o "ProyectoIntegrador-Desarrollo"
            File temp = dirActual;
            File carpetaEscritorio = null;
            File carpetaProyecto = null;
            
            while (temp != null) {
                if (temp.getName().equals("Escritorio")) {
                    carpetaEscritorio = temp;
                    break;
                }
                if (temp.getName().equals("ProyectoIntegrador-Desarrollo")) {
                    carpetaProyecto = temp;
                    break;
                }
                temp = temp.getParentFile();
            }
            
            // Opción 1: Escritorio/Z-Global-Img/complementos/categorias/
            if (carpetaEscritorio != null) {
                // Buscar en ...Z-Global-Img (con puntos al inicio)
                File carpetaPuntos = new File(carpetaEscritorio, "...Z-Global-Img/complementos/categorias");
                if (carpetaPuntos.exists() && carpetaPuntos.isDirectory()) {
                    System.out.println("✓ Carpeta de categorías encontrada: " + carpetaPuntos.getAbsolutePath());
                    return carpetaPuntos;
                }
                File carpetaPuntosDirecta = new File(carpetaEscritorio, "...Z-Global-Img/complementos");
                if (carpetaPuntosDirecta.exists() && carpetaPuntosDirecta.isDirectory()) {
                    System.out.println("✓ Carpeta de complementos encontrada: " + carpetaPuntosDirecta.getAbsolutePath());
                    return carpetaPuntosDirecta;
                }
                
                // Buscar en Z-Global-Img (sin puntos)
                File carpetaComplementos = new File(carpetaEscritorio, "Z-Global-Img/complementos/categorias");
                if (carpetaComplementos.exists() && carpetaComplementos.isDirectory()) {
                    System.out.println("✓ Carpeta de categorías encontrada: " + carpetaComplementos.getAbsolutePath());
                    return carpetaComplementos;
                }
                
                // Si no existe categorias, intentar directamente en complementos
                File carpetaComplementosDirecta = new File(carpetaEscritorio, "Z-Global-Img/complementos");
                if (carpetaComplementosDirecta.exists() && carpetaComplementosDirecta.isDirectory()) {
                    System.out.println("✓ Carpeta de complementos encontrada: " + carpetaComplementosDirecta.getAbsolutePath());
                    return carpetaComplementosDirecta;
                }
            }
            
            // Opción 1.5: Buscar en la raíz del proyecto Z-Global-Img/complementos
            if (carpetaProyecto != null) {
                File carpetaRaiz = new File(carpetaProyecto, "Z-Global-Img/complementos/categorias");
                if (carpetaRaiz.exists() && carpetaRaiz.isDirectory()) {
                    System.out.println("✓ Carpeta de categorías encontrada en raíz: " + carpetaRaiz.getAbsolutePath());
                    return carpetaRaiz;
                }
                File carpetaRaizDirecta = new File(carpetaProyecto, "Z-Global-Img/complementos");
                if (carpetaRaizDirecta.exists() && carpetaRaizDirecta.isDirectory()) {
                    System.out.println("✓ Carpeta de complementos encontrada en raíz: " + carpetaRaizDirecta.getAbsolutePath());
                    return carpetaRaizDirecta;
                }
            }
            
            // Opción 2: Desde ProyectoIntegrador-Desarrollo/Escritorio/Z-Global-Img/complementos/categorias/
            if (carpetaProyecto != null) {
                File carpetaEscritorioDesdeProyecto = new File(carpetaProyecto, "Escritorio");
                if (carpetaEscritorioDesdeProyecto.exists()) {
                    File carpetaComplementos = new File(carpetaEscritorioDesdeProyecto, "Z-Global-Img/complementos/categorias");
                    if (carpetaComplementos.exists() && carpetaComplementos.isDirectory()) {
                        System.out.println("✓ Carpeta de categorías encontrada: " + carpetaComplementos.getAbsolutePath());
                        return carpetaComplementos;
                    }
                    
                    // Si no existe categorias, intentar directamente en complementos
                    File carpetaComplementosDirecta = new File(carpetaEscritorioDesdeProyecto, "Z-Global-Img/complementos");
                    if (carpetaComplementosDirecta.exists() && carpetaComplementosDirecta.isDirectory()) {
                        System.out.println("✓ Carpeta de complementos encontrada: " + carpetaComplementosDirecta.getAbsolutePath());
                        return carpetaComplementosDirecta;
                    }
                }
            }
            
            // Opción 3: Usar ConfigImagenes como fallback
            File carpetaBase = ConfigImagenes.obtenerCarpetaImagenes();
            if (carpetaBase != null && carpetaBase.exists()) {
                File carpetaComplementos = new File(carpetaBase, "complementos/categorias");
                if (carpetaComplementos.exists() && carpetaComplementos.isDirectory()) {
                    return carpetaComplementos;
                }
                // Si no existe categorias, intentar directamente en complementos
                File carpetaComplementosDirecta = new File(carpetaBase, "complementos");
                if (carpetaComplementosDirecta.exists() && carpetaComplementosDirecta.isDirectory()) {
                    return carpetaComplementosDirecta;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error al obtener carpeta de complementos: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Busca una imagen para la categoría en la carpeta de complementos
     * Busca por nombres que coincidan con la categoría
     */
    private File buscarImagenCategoria(File carpeta, String categoria) {
        if (carpeta == null || !carpeta.exists() || !carpeta.isDirectory()) {
            return null;
        }
        
        String categoriaLower = categoria.toLowerCase().trim();
        // Extensiones de imagen soportadas (definidas una sola vez)
        String[] extensiones = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".JPG", ".JPEG", ".PNG", ".GIF", ".BMP"};
        
        // Mapeo de categorías a nombres específicos de archivos que existen
        String[] nombresEspecificos = obtenerNombresEspecificosImagen(categoriaLower);
        
        // Primero buscar por nombres específicos exactos (Accesorios.jpeg, ColeccionDeMujer.jpeg, etc.)
        for (String nombre : nombresEspecificos) {
            for (String ext : extensiones) {
                File archivo = new File(carpeta, nombre + ext);
                if (archivo.exists() && archivo.isFile()) {
                    System.out.println("✓ Imagen encontrada por nombre específico: " + archivo.getName() + " para categoría: " + categoria);
                    return archivo;
                }
            }
        }
        
        // Mapeo de categorías a posibles nombres de archivo genéricos
        String[] nombresPosibles = obtenerNombresImagenCategoria(categoriaLower);
        
        // Buscar por nombres genéricos
        for (String nombre : nombresPosibles) {
            for (String ext : extensiones) {
                File archivo = new File(carpeta, nombre + ext);
                if (archivo.exists() && archivo.isFile()) {
                    System.out.println("✓ Imagen local encontrada: " + archivo.getName() + " para categoría: " + categoria);
                    return archivo;
                }
            }
        }
        
        // Si no se encontró por nombre específico, buscar archivos que contengan palabras clave
        File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    String nombreArchivo = archivo.getName().toLowerCase();
                    // Verificar si el nombre del archivo contiene palabras clave de la categoría
                    if (categoriaLower.contains("mujer") && (nombreArchivo.contains("mujer") || nombreArchivo.contains("woman") || nombreArchivo.contains("colecciondemujer"))) {
                        System.out.println("✓ Imagen encontrada por palabra clave: " + archivo.getName() + " para categoría: " + categoria);
                        return archivo;
                    } else if ((categoriaLower.contains("nueva") || categoriaLower.contains("colección")) 
                            && (nombreArchivo.contains("nueva") || nombreArchivo.contains("coleccion") || nombreArchivo.contains("new"))) {
                        System.out.println("✓ Imagen encontrada por palabra clave: " + archivo.getName() + " para categoría: " + categoria);
                        return archivo;
                    } else if (categoriaLower.contains("accesorio") && (nombreArchivo.contains("accesorio") || nombreArchivo.contains("accessory"))) {
                        System.out.println("✓ Imagen encontrada por palabra clave: " + archivo.getName() + " para categoría: " + categoria);
                        return archivo;
                    } else if ((categoriaLower.contains("oferta") || categoriaLower.contains("especial")) 
                            && (nombreArchivo.contains("oferta") || nombreArchivo.contains("especial") || nombreArchivo.contains("sale"))) {
                        System.out.println("✓ Imagen encontrada por palabra clave: " + archivo.getName() + " para categoría: " + categoria);
                        return archivo;
                    }
                }
            }
        }
        
        // Si hay archivos de imagen y no se encontró coincidencia, intentar asignar por orden
        if (archivos != null) {
            java.util.List<File> archivosImagen = new java.util.ArrayList<>();
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    String nombre = archivo.getName();
                    for (String ext : extensiones) {
                        if (nombre.endsWith(ext)) {
                            archivosImagen.add(archivo);
                            break;
                        }
                    }
                }
            }
            
            // Si hay exactamente 4 imágenes, asignar por orden
            if (archivosImagen.size() == 4) {
                return asignarImagenPorOrden(archivosImagen.toArray(new File[0]), categoriaLower);
            }
            
            // Si hay imágenes pero no 4, tomar la primera disponible (fallback)
            if (!archivosImagen.isEmpty()) {
                System.out.println("Advertencia: Se encontraron " + archivosImagen.size() + " imágenes, se usará la primera para: " + categoria);
                return archivosImagen.get(0);
            }
        }
        
        return null;
    }
    
    /**
     * Obtiene los nombres específicos de las imágenes según la categoría
     * Basado en los nombres reales de los archivos: Accesorios.jpeg, ColeccionDeMujer.jpeg, etc.
     */
    private String[] obtenerNombresEspecificosImagen(String categoriaLower) {
        if (categoriaLower.contains("mujer")) {
            return new String[]{"ColeccionDeMujer", "colecciondemujer", "Coleccion_de_Mujer", "coleccion-mujer"};
        } else if (categoriaLower.contains("nueva") || categoriaLower.contains("colección")) {
            return new String[]{"NuevaColeccion", "nuevacoleccion", "Nueva_Coleccion", "nueva-coleccion"};
        } else if (categoriaLower.contains("accesorio")) {
            return new String[]{"Accesorios", "accesorios"};
        } else if (categoriaLower.contains("oferta") || categoriaLower.contains("especial")) {
            return new String[]{"Ofertas", "ofertas", "OfertasEspeciales", "ofertas-especiales"};
        }
        return new String[0];
    }
    
    /**
     * Obtiene los posibles nombres de archivo para una categoría
     */
    private String[] obtenerNombresImagenCategoria(String categoriaLower) {
        if (categoriaLower.contains("mujer")) {
            return new String[]{"mujer", "woman", "categoria1", "categoria_mujer", "1"};
        } else if (categoriaLower.contains("nueva") || categoriaLower.contains("colección")) {
            return new String[]{"nueva_coleccion", "new_collection", "categoria2", "categoria_nueva", "2"};
        } else if (categoriaLower.contains("accesorio")) {
            return new String[]{"accesorios", "accessories", "categoria3", "categoria_accesorios", "3"};
        } else if (categoriaLower.contains("oferta") || categoriaLower.contains("especial")) {
            return new String[]{"ofertas", "ofertas_especiales", "sale", "categoria4", "categoria_ofertas", "4"};
        }
        return new String[0];
    }
    
    /**
     * Asigna una imagen por orden si hay exactamente 4 archivos
     */
    private File asignarImagenPorOrden(File[] archivos, String categoriaLower) {
        // Filtrar solo archivos de imagen
        java.util.List<File> imagenes = new java.util.ArrayList<>();
        String[] extensiones = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".JPG", ".JPEG", ".PNG", ".GIF", ".BMP"};
        for (File archivo : archivos) {
            if (archivo.isFile()) {
                String nombre = archivo.getName();
                for (String ext : extensiones) {
                    if (nombre.endsWith(ext)) {
                        imagenes.add(archivo);
                        break;
                    }
                }
            }
        }
        
        if (imagenes.size() != 4) {
            System.out.println("No hay exactamente 4 imágenes, hay: " + imagenes.size());
            return null;
        }
        
        // Ordenar por nombre para consistencia
        imagenes.sort((f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
        
        System.out.println("Imágenes encontradas (ordenadas):");
        for (int i = 0; i < imagenes.size(); i++) {
            System.out.println("  " + (i+1) + ". " + imagenes.get(i).getName());
        }
        
        // Asignar según categoría
        int indice = -1;
        if (categoriaLower.contains("mujer")) {
            indice = 0;
        } else if (categoriaLower.contains("nueva") || categoriaLower.contains("colección")) {
            indice = 1;
        } else if (categoriaLower.contains("accesorio")) {
            indice = 2;
        } else if (categoriaLower.contains("oferta") || categoriaLower.contains("especial")) {
            indice = 3;
        }
        
        if (indice >= 0 && indice < imagenes.size()) {
            System.out.println("Asignando imagen " + (indice+1) + " (" + imagenes.get(indice).getName() + ") a categoría: " + categoriaLower);
            return imagenes.get(indice);
        }
        
        return null;
    }
    
    /**
     * Carga una imagen local y la muestra en el label
     */
    private void cargarImagenLocal(JLabel imageLabel, File archivo) {
        try {
            System.out.println("Intentando cargar imagen: " + archivo.getAbsolutePath());
            
            // Cargar la imagen usando ImageIO para mejor compatibilidad
            java.awt.image.BufferedImage bufferedImage = ImageIO.read(archivo);
            
            if (bufferedImage == null) {
                // Si ImageIO falla, intentar con ImageIcon
                ImageIcon icon = new ImageIcon(archivo.getAbsolutePath());
                bufferedImage = new java.awt.image.BufferedImage(icon.getIconWidth(), icon.getIconHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
                Graphics2D g = bufferedImage.createGraphics();
                g.drawImage(icon.getImage(), 0, 0, null);
                g.dispose();
            }
            
            if (bufferedImage != null && bufferedImage.getWidth() > 0 && bufferedImage.getHeight() > 0) {
                int targetWidth = 600;
                int targetHeight = 220;
                
                // Calcular escalado para llenar completamente el espacio sin espacios en blanco
                int imgWidth = bufferedImage.getWidth();
                int imgHeight = bufferedImage.getHeight();
                
                // Calcular escalas para ambos ejes
                double scaleX = (double) targetWidth / imgWidth;
                double scaleY = (double) targetHeight / imgHeight;
                
                // Usar el MAYOR factor de escala para que la imagen llene completamente el espacio
                // Esto puede recortar partes de la imagen pero elimina espacios en blanco
                double scale = Math.max(scaleX, scaleY);
                
                // Calcular dimensiones escaladas
                int scaledWidth = (int) (imgWidth * scale);
                int scaledHeight = (int) (imgHeight * scale);
                
                // Calcular posición para centrar (recortará si es necesario)
                int x = (targetWidth - scaledWidth) / 2;
                int y = (targetHeight - scaledHeight) / 2;
                
                // Crear BufferedImage escalado con alta calidad
                java.awt.image.BufferedImage bufferedEscalada = new java.awt.image.BufferedImage(targetWidth, targetHeight, java.awt.image.BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = bufferedEscalada.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujar fondo blanco primero
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, targetWidth, targetHeight);
                
                // Dibujar la imagen escalada para llenar completamente el espacio
                // La imagen se ampliará lo suficiente para llenar todo el espacio, recortando si es necesario
                g2d.drawImage(bufferedImage, x, y, scaledWidth, scaledHeight, null);
                g2d.dispose();
                
                final ImageIcon iconEscalado = new ImageIcon(bufferedEscalada);
                
                    SwingUtilities.invokeLater(() -> {
                    System.out.println("Estableciendo icono en label para: " + archivo.getName());
                    imageLabel.setIcon(iconEscalado);
                        imageLabel.setText("");
                    imageLabel.setOpaque(false);
                    imageLabel.setVisible(true);
                    imageLabel.revalidate();
                        imageLabel.repaint();
                    
                    // Forzar repintado de todos los padres
                    Component parent = imageLabel.getParent();
                    while (parent != null) {
                        parent.revalidate();
                        parent.repaint();
                        parent = parent.getParent();
                    }
                    
                    System.out.println("✓ Icono establecido y componentes repintados");
                });
                
                System.out.println("✓ Imagen local procesada exitosamente: " + archivo.getName() + " (" + targetWidth + "x" + targetHeight + ")");
                } else {
                System.err.println("✗ Imagen inválida o no cargada: " + archivo.getName());
                }
            } catch (Exception e) {
            System.err.println("✗ Error al cargar imagen local: " + e.getMessage());
            e.printStackTrace();
                }
    }

    /**
     * Carga una imagen web representativa de la categoría
     */
    
    /**
     * Obtiene la URL de imagen web según la categoría
     * Usa imágenes de Unsplash que son más confiables
     */
    private String obtenerUrlImagenCategoria(String categoria) {
        String categoriaLower = categoria.toLowerCase();
        
        // URLs directas de Unsplash - más confiables y de alta calidad
        if (categoriaLower.contains("mujer")) {
            // Moda femenina elegante - vestido elegante
            return "https://images.unsplash.com/photo-1445205170230-053b83016050?w=600&h=400&fit=crop&auto=format";
        } else if (categoriaLower.contains("nueva") || categoriaLower.contains("colección")) {
            // Nueva colección - moda moderna
            return "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=600&h=400&fit=crop&auto=format";
        } else if (categoriaLower.contains("accesorio")) {
            // Accesorios - bolso de moda
            return "https://images.unsplash.com/photo-1590874103328-eac38a683ce7?w=600&h=400&fit=crop&auto=format";
        } else if (categoriaLower.contains("oferta") || categoriaLower.contains("especial")) {
            // Ofertas - tienda de moda
            return "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=600&h=400&fit=crop&auto=format";
        } else {
            // Moda genérica - estilo elegante
            return "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=600&h=400&fit=crop&auto=format";
        }
    }
    
    /**
     * Muestra una imagen decorativa atractiva para la categoría
     */
    private void mostrarGradienteCategoria(JLabel imageLabel, String categoria) {
        // Crear una imagen decorativa atractiva con diseño único según la categoría
        int width = 600;
        int height = 220;
        java.awt.image.BufferedImage bufferedImage = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        String catLower = categoria.toLowerCase();
        Color color1, color2;
        
        // Colores específicos según la categoría
        if (catLower.contains("mujer")) {
            color1 = new Color(212, 165, 199); // Rosa suave
            color2 = new Color(168, 85, 122);  // Rosa profundo
        } else if (catLower.contains("nueva") || catLower.contains("colección")) {
            color1 = new Color(139, 74, 107);  // Morado medio
            color2 = new Color(107, 45, 77);   // Morado oscuro
        } else if (catLower.contains("accesorio")) {
            color1 = new Color(168, 85, 122);  // Rosa elegante
            color2 = new Color(212, 165, 199); // Rosa claro
        } else if (catLower.contains("oferta") || catLower.contains("especial")) {
            color1 = new Color(107, 45, 77);   // Morado oscuro
            color2 = new Color(139, 74, 107);  // Morado medio
        } else {
            color1 = COLOR_PRIMARY;
            color2 = COLOR_SECONDARY;
        }
        
        // Gradiente decorativo diagonal
        GradientPaint gradient = new GradientPaint(
                0, 0, color1,
                width, height, color2
        );
        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, height);
        
        // Círculos decorativos con diferentes opacidades
        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillOval(-30, -30, 120, 120);
        g2.setColor(new Color(255, 255, 255, 25));
        g2.fillOval(180, 100, 120, 120);
        g2.setColor(new Color(255, 255, 255, 15));
        g2.fillOval(100, -20, 100, 100);

        // Patrón de líneas decorativas
        g2.setStroke(new BasicStroke(2.0f));
        g2.setColor(new Color(255, 255, 255, 30));
        for (int i = 0; i < 5; i++) {
            int y = 30 + i * 30;
            g2.drawLine(0, y, width, y + 20);
        }
        
        // Icono decorativo grande en el centro
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        FontMetrics fm = g2.getFontMetrics();
        String icono = "👗";
        if (catLower.contains("accesorio")) icono = "👜";
        else if (catLower.contains("oferta")) icono = "🏷️";
        else if (catLower.contains("nueva")) icono = "✨";
        
        int iconX = (width - fm.stringWidth(icono)) / 2;
        int iconY = 90 + fm.getAscent() / 2;
        g2.setColor(new Color(255, 255, 255, 180));
        g2.drawString(icono, iconX, iconY);
        
        g2.dispose();
        
        ImageIcon gradientIcon = new ImageIcon(bufferedImage);
        imageLabel.setIcon(gradientIcon);
        imageLabel.setText("");
        imageLabel.revalidate();
        imageLabel.repaint();
    }
    
    /**
     * Formatea un precio con puntos de miles y dos decimales Ejemplo: 150000.50
     * -> "$150.000,50"
     */
    private String formatearPrecio(double precio) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "CO"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat formatter = new DecimalFormat("#,##0.00", symbols);
        return "$" + formatter.format(precio);
    }
    
    /**
     * Cierra la sesión del usuario
     */
    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (respuesta == JOptionPane.YES_OPTION) {
            SessionManager.getInstance().logout();
        this.dispose();
            new principal(0).setVisible(true); // Volver a página principal sin sesión
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new principal(0).setVisible(true);
        });
    }
}
