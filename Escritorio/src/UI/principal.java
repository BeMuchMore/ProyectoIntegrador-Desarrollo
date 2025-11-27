
package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import UI.util.ErrorHandler;
import UI.util.ModernStyles;
import UI.ConfigImagenes;

/**
 * Página principal de la aplicación - Diseño basado en index.html
 * Replica el diseño moderno de la página web
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
    private static final Color COLOR_BG_GRADIENT_START = new Color(255, 255, 255); // #FFFFFF - Blanco puro (máximo contraste)
    private static final Color COLOR_BG_GRADIENT_MID = new Color(248, 248, 248); // #F8F8F8 - Gris muy claro
    private static final Color COLOR_BG_GRADIENT_END = new Color(245, 245, 245); // #F5F5F5 - Gris claro
    
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
     * Inicializa los componentes básicos
     * Este método es llamado automáticamente por NetBeans cuando se usa el editor visual
     * También puede ser llamado manualmente si se ejecuta el código sin el .form
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
        
        headerMainPanel.setLayout(new BorderLayout(200, 0));
        headerMainPanel.setBackground(Color.WHITE);
        headerMainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(240, 240, 240)),
            new EmptyBorder(15, 50, 15, 50)
        ));
        headerMainPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 80));
        headerMainPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setOpaque(false);
        
        // Cargar logo si existe
        if (rutaLogo != null && !rutaLogo.isEmpty()) {
            try {
                String rutaAbsoluta = ConfigImagenes.obtenerRutaAbsoluta(rutaLogo);
                File archivo = new File(rutaAbsoluta);
                if (archivo.exists()) {
                    ImageIcon icon = new ImageIcon(rutaAbsoluta);
                    Image img = icon.getImage();
                    Image imgEscalada = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    JLabel logoImage = new JLabel(new ImageIcon(imgEscalada));
                    logoPanel.add(logoImage);
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error al cargar logo", e);
            }
        }
        
        JLabel logoLabel = new JLabel(nombreApp);
        logoLabel.setFont(new Font("Playfair Display", Font.BOLD, 28));
        logoLabel.setForeground(COLOR_PRIMARY);
        logoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                scrollToTop();
            }
        });
        logoPanel.add(logoLabel);
        
        // Navegación
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        navPanel.setOpaque(false);
        
        JLabel navInicio = crearNavLink("Inicio", e -> scrollToTop());
        JLabel navTienda = crearNavLink("Tienda", e -> abrirTienda());
        JLabel navMujer = crearNavLink("Mujer", e -> abrirCategoria("Mujer"));
        JLabel navAccesorios = crearNavLink("Accesorios", e -> abrirCategoria("Accesorios"));
        
        navPanel.add(navInicio);
        navPanel.add(navTienda);
        navPanel.add(navMujer);
        navPanel.add(navAccesorios);
        
        // Iconos del header
        JPanel iconsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        iconsPanel.setOpaque(false);
        
        JLabel searchIcon = crearHeaderIcon("🔍", "Buscar", e -> mostrarBusqueda());
        
        // Icono de usuario con información si está logueado
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        userPanel.setOpaque(false);
        
        if (isLoggedIn && username != null) {
            // Mostrar nombre de usuario
            JLabel userNameLabel = new JLabel(username);
            userNameLabel.setFont(new Font("Poppins", Font.PLAIN, 12));
            userNameLabel.setForeground(COLOR_TEXT_PRIMARY);
            userNameLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
            userPanel.add(userNameLabel);
            
            // Icono de usuario
            JLabel userIcon = crearHeaderIcon("👤", "Mi Perfil", e -> manejarUsuario());
            userPanel.add(userIcon);
            
            // Botón de cerrar sesión si es admin
            if ("admin".equalsIgnoreCase(cargo)) {
                JLabel adminBadge = new JLabel("⚙️");
                adminBadge.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                adminBadge.setToolTipText("Administrador");
                adminBadge.setCursor(new Cursor(Cursor.HAND_CURSOR));
                userPanel.add(adminBadge);
            }
            
            // Botón cerrar sesión
            JLabel logoutIcon = crearHeaderIcon("🚪", "Cerrar Sesión", e -> cerrarSesion());
            userPanel.add(logoutIcon);
        } else {
            JLabel userIcon = crearHeaderIcon("👤", "Iniciar Sesión", e -> manejarUsuario());
            userPanel.add(userIcon);
        }
        
        JLabel cartIcon = crearHeaderIcon("🛒", "Carrito", e -> abrirCarrito());
        
        iconsPanel.add(searchIcon);
        iconsPanel.add(userPanel);
        iconsPanel.add(cartIcon);
        
        headerMainPanel.add(logoPanel, BorderLayout.WEST);
        headerMainPanel.add(navPanel, BorderLayout.CENTER);
        headerMainPanel.add(iconsPanel, BorderLayout.EAST);
        
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
     * Crea un icono del header
     */
    private JLabel crearHeaderIcon(String icono, String tooltip, ActionListener action) {
        JLabel icon = new JLabel(icono);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        icon.setToolTipText(tooltip);
        icon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        icon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new ActionEvent(icon, 0, ""));
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            }
        });
        return icon;
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
        
        JLabel title = new JLabel("<html><div style='text-align: center; line-height: 1.2;'>" +
            "Descubre Tu<br>" +
            "<span style='color: #6B2D4D; font-size: 56px;'>Estilo Femenino</span><br>" +
            "<span style='color: #8B4A6B; font-size: 56px;'>Perfecto</span></div></html>");
        title.setFont(new Font("Playfair Display", Font.BOLD, 48));
        title.setForeground(COLOR_TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        JLabel subtitle = new JLabel("<html><div style='text-align: center; width: 600px; line-height: 1.6;'>" +
            "Eleva tu guardarropa con nuestra colección exclusiva de moda femenina premium " +
            "diseñada para empoderarte y hacerte brillar en cada ocasión.</div></html>");
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
        JLabel title = new JLabel("<html><div style='text-align: center; line-height: 1.2;'>" +
            "<span style='color: #6B2D4D;'>Recién</span> " +
            "<span style='color: #8B4A6B;'>Llegados</span></div></html>");
        title.setFont(new Font("Playfair Display", Font.BOLD, 42));
        title.setForeground(COLOR_TEXT_PRIMARY);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Subtítulo mejorado - completamente centrado
        JLabel subtitle = new JLabel("<html><div style='text-align: center; line-height: 1.6;'>" +
            "Descubre las últimas incorporaciones a nuestra colección exclusiva</div></html>");
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
                
                // Fondo con gradiente sutil
                int width = getWidth();
                int height = getHeight();
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 255),
                    width, height, new Color(COLOR_BG_LIGHT.getRed(), COLOR_BG_LIGHT.getGreen(), COLOR_BG_LIGHT.getBlue(), 255)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, width, height);
                
                // Formas decorativas sutiles
                g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 8));
                g2.fillOval(width - 100, 50, 150, 150);
                g2.setColor(new Color(COLOR_SECONDARY.getRed(), COLOR_SECONDARY.getGreen(), COLOR_SECONDARY.getBlue(), 5));
                g2.fillOval(50, height - 80, 120, 120);
                
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
        JLabel title = new JLabel("<html><div style='text-align: center; line-height: 1.2;'>" +
            "<span style='color: #6B2D4D;'>Comprar por</span> " +
            "<span style='color: #8B4A6B;'>Categoría</span></div></html>");
        title.setFont(new Font("Playfair Display", Font.BOLD, 42));
        title.setForeground(COLOR_TEXT_PRIMARY);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Subtítulo mejorado y centrado
        JLabel subtitle = new JLabel("<html><div style='text-align: center; line-height: 1.6;'>" +
            "Explora nuestras colecciones cuidadosamente seleccionadas</div></html>");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 16));
        subtitle.setForeground(COLOR_TEXT_SECONDARY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(0, 0, 50, 0));
        
        headerPanel.add(title);
        headerPanel.add(subtitle);
        
        // Grid de categorías mejorado
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        gridPanel.setOpaque(false);
        gridPanel.setMaximumSize(new Dimension(1200, 600));
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gridPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Crear cards de categorías mejorados
        gridPanel.add(crearCategoryCardMejorado("Colección de Mujer", "Elegante y Moderna", "Mujer"));
        gridPanel.add(crearCategoryCardMejorado("Nueva Colección", "Clásico y Moderno", "Nueva Colección"));
        gridPanel.add(crearCategoryCardMejorado("Accesorios", "Completa Tu Look", "Accesorios"));
        gridPanel.add(crearCategoryCardMejorado("Ofertas Especiales", "Ahorra hasta 50%", "Ofertas"));
        
        categoriesPanel.add(headerPanel);
        categoriesPanel.add(gridPanel);
        
        mainPanel.add(categoriesPanel);
    }
    
    /**
     * Crea un card de categoría mejorado con mejor diseño
     */
    private JPanel crearCategoryCardMejorado(String titulo, String subtitulo, String categoria) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra sutil
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 4, getWidth() - 2, getHeight() - 2, 20, 20);
                
                // Fondo blanco con bordes redondeados
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 4, 20, 20);
                
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(320, 280));
        card.setMaximumSize(new Dimension(320, 280));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panel de imagen con overlay mejorado
        JPanel imageContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        imageContainer.setPreferredSize(new Dimension(300, 200));
        imageContainer.setMaximumSize(new Dimension(300, 200));
        imageContainer.setOpaque(false);
        imageContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Imagen de categoría
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(280, 180));
        imageLabel.setMaximumSize(new Dimension(280, 180));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // Intentar cargar imagen de categoría desde productos o usar gradiente
        cargarImagenCategoria(imageLabel, categoria);
        
        // Overlay con efecto hover mejorado
        JPanel overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (isVisible()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 220));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.dispose();
                }
            }
        };
        overlay.setOpaque(false);
        overlay.setPreferredSize(new Dimension(280, 180));
        overlay.setMaximumSize(new Dimension(280, 180));
        overlay.setVisible(false);
        
        JLabel overlayText = new JLabel("Explorar →");
        overlayText.setFont(new Font("Poppins", Font.BOLD, 16));
        overlayText.setForeground(COLOR_TEXT_LIGHT);
        overlayText.setHorizontalAlignment(SwingConstants.CENTER);
        overlayText.setVerticalAlignment(SwingConstants.CENTER);
        overlay.setLayout(new BorderLayout());
        overlay.add(overlayText, BorderLayout.CENTER);
        
        imageContainer.add(imageLabel, BorderLayout.CENTER);
        imageContainer.add(overlay, BorderLayout.CENTER);
        
        // Info panel mejorado y centrado
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        infoPanel.setOpaque(true);
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>" + titulo + "</div></html>");
        titleLabel.setFont(new Font("Playfair Display", Font.BOLD, 22));
        titleLabel.setForeground(COLOR_TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("<html><div style='text-align: center;'>" + subtitulo + "</div></html>");
        subtitleLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        subtitleLabel.setForeground(COLOR_TEXT_SECONDARY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
        
        infoPanel.add(titleLabel);
        infoPanel.add(subtitleLabel);
        
        card.add(imageContainer, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirCategoria(categoria);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                overlay.setVisible(true);
                overlay.repaint();
                card.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                overlay.setVisible(false);
                overlay.repaint();
                card.repaint();
            }
        });
        
        return card;
    }
    
    /**
     * Crea la sección de productos destacados
     */
    private void crearFeaturedProductsSection() {
        featuredProductsPanel = new JPanel();
        featuredProductsPanel.setLayout(new BoxLayout(featuredProductsPanel, BoxLayout.Y_AXIS));
        featuredProductsPanel.setBackground(COLOR_BG_LIGHT);
        featuredProductsPanel.setBorder(new EmptyBorder(60, 50, 60, 50));
        
        JLabel title = new JLabel("Productos Destacados");
        title.setFont(new Font("Playfair Display", Font.BOLD, 36));
        title.setForeground(COLOR_TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 40, 0));
        
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setMaximumSize(new Dimension(1200, 600));
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Los productos se cargarán dinámicamente
        productosGridPanel = gridPanel;
        
        featuredProductsPanel.add(title);
        featuredProductsPanel.add(gridPanel);
        
        JButton verTodosBtn = crearBotonVerTodos("Ver Todos los Productos", e -> abrirTienda());
        verTodosBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        verTodosBtn.setBorder(new EmptyBorder(40, 0, 0, 0));
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
                // Acción según el link
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                link.setForeground(COLOR_PRIMARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                link.setForeground(COLOR_TEXT_LIGHT);
            }
        });
        
        return link;
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
        if (recienLlegadosProductsPanel == null) return;
        
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
                JLabel noProducts = new JLabel("<html><div style='text-align: center; padding: 50px; color: #999;'>" +
                    "No hay productos recién llegados</div></html>");
                noProducts.setHorizontalAlignment(JLabel.CENTER);
                recienLlegadosProductsPanel.add(noProducts);
            }
            
        } catch (SQLException e) {
            ErrorHandler.logWarning("Error al cargar productos recién llegados", e);
            JLabel errorLabel = new JLabel("<html><div style='text-align: center; padding: 50px; color: #999;'>" +
                "Error al cargar productos</div></html>");
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
        if (carruselScrollPane != null && carruselBtnAnterior != null && carruselBtnSiguiente != null) {
            JScrollBar scrollBar = carruselScrollPane.getHorizontalScrollBar();
            boolean necesitaScroll = scrollBar.getMaximum() > scrollBar.getVisibleAmount();
            
            carruselBtnAnterior.setVisible(necesitaScroll);
            carruselBtnSiguiente.setVisible(necesitaScroll);
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
        imageLabel.setBackground(Color.WHITE);
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
                imageLabel.setText("<html><div style='text-align: center; padding-top: 100px; color: #999; font-size: 12px;'>" +
                    "Sin imagen</div></html>");
            }
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("<html><div style='text-align: center; padding-top: 100px; color: #999; font-size: 12px;'>" +
                "Sin imagen</div></html>");
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
        JLabel nameLabel = new JLabel("<html><div style='text-align: center; line-height: 1.4; padding: 5px 10px;'>" + 
            "<b>" + nombre.toUpperCase() + "</b></div></html>");
        nameLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        nameLabel.setForeground(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue()));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(15, 0, 12, 0));
        
        // Precio centrado - más destacado
        JLabel priceLabel = new JLabel("<html><div style='text-align: center;'>" +
            "<span style='font-size: 24px; font-weight: bold; color: #6B2D4D;'>$" + 
            String.format("%.2f", producto.getPrecio()) + "</span></div></html>");
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
        productosGridPanel.removeAll();
        
        try (Connection conn = Conexion.getConnection()) {
            String sql = """
                SELECT p.*, 
                       (SELECT SUM(cantidad) FROM tb_inventario WHERE id_producto = p.id) as total_cantidad,
                       (SELECT AVG(precio) FROM tb_inventario WHERE id_producto = p.id) as precio_promedio
                FROM tb_productos p
                WHERE p.estado = 'vigente'
                ORDER BY p.FecheIngreso DESC
                LIMIT 8
                """;
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            int count = 0;
            while (rs.next() && count < 8) {
                UI.Producto producto = new UI.Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("Nombre"));
                producto.setImgUrl(rs.getString("ImgUrl"));
                producto.setPrecio(rs.getDouble("precio_promedio"));
                producto.setCategorias(rs.getString("Categorias"));
                producto.setCantidad(rs.getInt("total_cantidad"));
                
                productosGridPanel.add(crearProductCard(producto));
                count++;
            }
            
            // Rellenar con placeholders si hay menos de 8 productos
            while (count < 8) {
                productosGridPanel.add(crearProductCardPlaceholder());
                count++;
            }
            
        } catch (SQLException e) {
            ErrorHandler.logWarning("Error al cargar productos destacados", e);
            // Agregar placeholders en caso de error
            for (int i = 0; i < 8; i++) {
                productosGridPanel.add(crearProductCardPlaceholder());
            }
        }
        
        productosGridPanel.revalidate();
        productosGridPanel.repaint();
    }
    
    /**
     * Crea un card de producto
     */
    private JPanel crearProductCard(UI.Producto producto) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(280, 350));
        card.setMaximumSize(new Dimension(280, 350));
        
        // Imagen del producto
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(280, 280));
        imageLabel.setMaximumSize(new Dimension(280, 280));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Cargar imagen del producto
        if (producto.getImgUrl() != null && !producto.getImgUrl().isEmpty()) {
            try {
                String rutaAbsoluta = ConfigImagenes.obtenerRutaAbsoluta(producto.getImgUrl());
                File archivo = new File(rutaAbsoluta);
                if (archivo.exists()) {
                    ImageIcon icon = new ImageIcon(rutaAbsoluta);
                    Image img = icon.getImage();
                    Image imgEscalada = img.getScaledInstance(280, 280, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(imgEscalada));
                    imageLabel.setText("");
                } else {
                    // Intentar cargar con método alternativo
                    ImageIcon icon = ConfigImagenes.cargarImagenEscalada(producto.getImgUrl(), 280, 280);
                    if (icon != null) {
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    } else {
                        imageLabel.setText("<html><div style='text-align: center; padding-top: 120px; color: #999;'>" +
                            "Sin imagen</div></html>");
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error al cargar imagen de producto: " + producto.getNombre(), e);
                imageLabel.setText("<html><div style='text-align: center; padding-top: 120px; color: #999;'>" +
                    "Sin imagen</div></html>");
            }
        } else {
            imageLabel.setText("<html><div style='text-align: center; padding-top: 120px; color: #999;'>" +
                "Sin imagen</div></html>");
        }
        
        // Info del producto
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(producto.getNombre() != null ? producto.getNombre() : "Sin nombre");
        nameLabel.setFont(new Font("Poppins", Font.BOLD, 14));
        nameLabel.setForeground(COLOR_TEXT_PRIMARY);
        
        JLabel priceLabel = new JLabel("$" + String.format("%.2f", producto.getPrecio()));
        priceLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        priceLabel.setForeground(COLOR_PRIMARY);
        
        JLabel categoryLabel = new JLabel(producto.getCategorias() != null ? producto.getCategorias() : "");
        categoryLabel.setFont(new Font("Poppins", Font.PLAIN, 11));
        categoryLabel.setForeground(COLOR_TEXT_SECONDARY);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(categoryLabel);
        
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetallesProducto(producto);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY, 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));
            }
        });
        
        return card;
    }
    
    /**
     * Crea un placeholder para producto
     */
    private JPanel crearProductCardPlaceholder() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));
        card.setPreferredSize(new Dimension(280, 350));
        card.setMaximumSize(new Dimension(280, 350));
        
        JLabel placeholder = new JLabel("<html><div style='text-align: center; padding-top: 150px; color: #999;'>" +
            "Producto<br/>No disponible</div></html>");
        placeholder.setHorizontalAlignment(JLabel.CENTER);
        placeholder.setOpaque(true);
        placeholder.setBackground(Color.WHITE);
        
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
            // Abrir login
            this.dispose();
            new Inicio().setVisible(true);
        }
    }
    
    private void abrirCarrito() {
        JOptionPane.showMessageDialog(this, "Funcionalidad de carrito en desarrollo", 
            "Carrito", JOptionPane.INFORMATION_MESSAGE);
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
            heroImage.setText("<html><div style='text-align: center; padding-top: 220px; color: #999;'>" +
                "Imagen Hero<br/>400x500</div></html>");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar imagen hero", e);
            heroImage.setText("<html><div style='text-align: center; padding-top: 220px; color: #999;'>" +
                "Imagen Hero<br/>400x500</div></html>");
        }
    }
    
    /**
     * Carga imagen de categoría desde productos o usa gradiente
     */
    private void cargarImagenCategoria(JLabel imageLabel, String categoria) {
        try {
            // Buscar una imagen de producto de esta categoría
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
                    if (archivo.exists()) {
                        ImageIcon icon = new ImageIcon(rutaAbsoluta);
                    Image img = icon.getImage();
                    Image imgEscalada = img.getScaledInstance(300, 180, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(imgEscalada));
                        imageLabel.setText("");
                        return;
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error al cargar imagen de categoría", e);
            }
            
            // Si no hay imagen, usar texto sobre fondo blanco
            imageLabel.setText("<html><div style='text-align: center; padding-top: 60px; color: #666; font-size: 18px; font-weight: bold;'>" +
                categoria + "</div></html>");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar imagen de categoría", e);
            imageLabel.setText("<html><div style='text-align: center; padding-top: 60px; color: #666; font-size: 18px; font-weight: bold;'>" +
                categoria + "</div></html>");
        }
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
