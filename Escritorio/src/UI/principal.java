
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
    
    // Colores del diseño web
    private static final Color COLOR_PRIMARY = new Color(212, 165, 199); // #d4a5c7
    private static final Color COLOR_SECONDARY = new Color(201, 139, 184); // #c98bb8
    private static final Color COLOR_ACCENT = new Color(255, 107, 157); // #ff6b9d
    private static final Color COLOR_BG_GRADIENT_START = new Color(232, 213, 227); // #e8d5e3
    private static final Color COLOR_BG_GRADIENT_MID = new Color(240, 224, 235); // #f0e0eb
    private static final Color COLOR_BG_GRADIENT_END = new Color(245, 230, 240); // #f5e6f0
    
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
        } else {
            // Si ya existe, solo configuramos sus propiedades
            scrollPane.setViewportView(mainPanel);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
       
        headerTopPanel.setBackground(new Color(212, 165, 199));
        headerTopPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 25));
       
        
        JLabel contactInfo = new JLabel("📞 +1 234 567 8900 | 📧 " + correoApp);
        contactInfo.setForeground(Color.WHITE);
        contactInfo.setFont(new Font("Poppins", Font.PLAIN, 13));
        
        JLabel shippingInfo = new JLabel("Envío Gratis en Pedidos Superiores a $100");
        shippingInfo.setForeground(Color.WHITE);
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
        logoLabel.setForeground(new Color(139, 69, 69));
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
            userNameLabel.setForeground(new Color(51, 51, 51));
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
                link.setForeground(Color.BLACK);
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
        heroPanel = new JPanel();
        heroPanel.setLayout(new BorderLayout(40, 0));
        heroPanel.setOpaque(false);
        heroPanel.setBorder(new EmptyBorder(80, 50, 80, 50));
        
        // Texto del hero
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel title = new JLabel("<html><div style='text-align: center;'>" +
            "Descubre Tu <span style='color: #8b4a6b;'>Estilo Femenino Perfecto</span></div></html>");
        title.setFont(new Font("Playfair Display", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("<html><div style='text-align: center; width: 500px;'>" +
            "Eleva tu guardarropa con nuestra colección exclusiva de moda femenina premium " +
            "diseñada para empoderarte y hacerte brillar en cada ocasión.</div></html>");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 16));
        subtitle.setForeground(new Color(26, 26, 26));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(20, 0, 30, 0));
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);
        
        JButton btnComprar = crearBotonPrimario("Comprar Ahora", e -> abrirTienda());
        buttonsPanel.add(btnComprar);
        
        if (isLoggedIn && "admin".equalsIgnoreCase(cargo)) {
            JButton btnAdmin = crearBotonSecundario("⚙️ Panel Admin", e -> abrirPanelAdmin());
            buttonsPanel.add(btnAdmin);
        }
        
        textPanel.add(Box.createVerticalGlue());
        textPanel.add(title);
        textPanel.add(subtitle);
        textPanel.add(buttonsPanel);
        textPanel.add(Box.createVerticalGlue());
        
        // Imagen del hero
        JLabel heroImage = new JLabel();
        heroImage.setPreferredSize(new Dimension(400, 500));
        heroImage.setMaximumSize(new Dimension(400, 500));
        heroImage.setOpaque(true);
        heroImage.setBackground(Color.WHITE);
        heroImage.setHorizontalAlignment(JLabel.CENTER);
        
        // Intentar cargar imagen del hero desde complementos o usar placeholder
        cargarImagenHero(heroImage);
        
        heroPanel.add(textPanel, BorderLayout.CENTER);
        heroPanel.add(heroImage, BorderLayout.EAST);
        
        mainPanel.add(heroPanel);
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
     * Crea la sección de Recién Llegados
     */
    private void crearRecienLlegadosSection() {
        JPanel recienLlegadosPanel = new JPanel();
        recienLlegadosPanel.setLayout(new BoxLayout(recienLlegadosPanel, BoxLayout.Y_AXIS));
        recienLlegadosPanel.setBackground(new Color(248, 249, 250));
        recienLlegadosPanel.setBorder(new EmptyBorder(60, 50, 60, 50));
        
        // Título con badge
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        titlePanel.setOpaque(false);
        
        JLabel badge = new JLabel("🆕");
        badge.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel title = new JLabel("Recién Llegados");
        title.setFont(new Font("Playfair Display", Font.BOLD, 36));
        title.setForeground(new Color(51, 51, 51));
        
        titlePanel.add(badge);
        titlePanel.add(title);
        
        JLabel subtitle = new JLabel("Descubre las últimas incorporaciones a nuestra colección");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 14));
        subtitle.setForeground(new Color(102, 102, 102));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(10, 0, 40, 0));
        
        // Panel de productos con scroll horizontal
        JPanel productsContainer = new JPanel();
        productsContainer.setLayout(new BoxLayout(productsContainer, BoxLayout.X_AXIS));
        productsContainer.setOpaque(false);
        productsContainer.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JScrollPane horizontalScroll = new JScrollPane(productsContainer);
        horizontalScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        horizontalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        horizontalScroll.setBorder(null);
        horizontalScroll.setOpaque(false);
        horizontalScroll.getViewport().setOpaque(false);
        horizontalScroll.setPreferredSize(new Dimension(1200, 400));
        horizontalScroll.setMaximumSize(new Dimension(1200, 400));
        
        // Cargar productos recién llegados
        recienLlegadosProductsPanel = productsContainer;
        cargarRecienLlegados();
        
        JButton verTodosBtn = crearBotonPrimario("Ver Todos los Recién Llegados", e -> abrirCategoria("Nuevas Llegadas"));
        verTodosBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        recienLlegadosPanel.add(titlePanel);
        recienLlegadosPanel.add(subtitle);
        recienLlegadosPanel.add(horizontalScroll);
        recienLlegadosPanel.add(Box.createVerticalStrut(20));
        recienLlegadosPanel.add(verTodosBtn);
        
        mainPanel.add(recienLlegadosPanel);
    }
    
    private JPanel recienLlegadosProductsPanel;
    
    /**
     * Crea la sección de categorías con diseño mejorado
     */
    private void crearCategoriesSection() {
        categoriesPanel = new JPanel();
        categoriesPanel.setLayout(new BoxLayout(categoriesPanel, BoxLayout.Y_AXIS));
        categoriesPanel.setBackground(Color.WHITE);
        categoriesPanel.setBorder(new EmptyBorder(60, 50, 60, 50));
        
        JLabel title = new JLabel("Comprar por Categoría");
        title.setFont(new Font("Playfair Display", Font.BOLD, 36));
        title.setForeground(new Color(51, 51, 51));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel subtitle = new JLabel("Explora nuestras colecciones cuidadosamente seleccionadas");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 14));
        subtitle.setForeground(new Color(102, 102, 102));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(0, 0, 40, 0));
        
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        gridPanel.setOpaque(false);
        gridPanel.setMaximumSize(new Dimension(1200, 550));
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Crear cards de categorías mejorados
        gridPanel.add(crearCategoryCardMejorado("Colección de Mujer", "Elegante y Moderna", "Mujer"));
        gridPanel.add(crearCategoryCardMejorado("Nueva Colección", "Clásico y Moderno", "Nueva Colección"));
        gridPanel.add(crearCategoryCardMejorado("Accesorios", "Completa Tu Look", "Accesorios"));
        gridPanel.add(crearCategoryCardMejorado("Ofertas Especiales", "Ahorra hasta 50%", "Ofertas"));
        
        categoriesPanel.add(title);
        categoriesPanel.add(subtitle);
        categoriesPanel.add(gridPanel);
        
        mainPanel.add(categoriesPanel);
    }
    
    /**
     * Crea un card de categoría mejorado con mejor diseño
     */
    private JPanel crearCategoryCardMejorado(String titulo, String subtitulo, String categoria) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(300, 260));
        card.setMaximumSize(new Dimension(300, 260));
        
        // Panel de imagen con overlay
        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(300, 180));
        imageContainer.setMaximumSize(new Dimension(300, 180));
        imageContainer.setOpaque(false);
        
        // Imagen de categoría
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(300, 180));
        imageLabel.setMaximumSize(new Dimension(300, 180));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        
        // Intentar cargar imagen de categoría desde productos o usar gradiente
        cargarImagenCategoria(imageLabel, categoria);
        
        // Overlay con efecto hover
        JPanel overlay = new JPanel();
        overlay.setOpaque(false);
        overlay.setBackground(new Color(0, 0, 0, 0));
        overlay.setPreferredSize(new Dimension(300, 180));
        overlay.setMaximumSize(new Dimension(300, 180));
        
        JLabel overlayText = new JLabel("Explorar →");
        overlayText.setFont(new Font("Poppins", Font.BOLD, 14));
        overlayText.setForeground(Color.WHITE);
        overlayText.setHorizontalAlignment(JLabel.CENTER);
        overlayText.setVerticalAlignment(JLabel.CENTER);
        overlayText.setVisible(false);
        overlay.add(overlayText);
        
        imageContainer.add(imageLabel, BorderLayout.CENTER);
        imageContainer.add(overlay, BorderLayout.CENTER);
        
        // Info panel mejorado
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        infoPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Playfair Display", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        JLabel subtitleLabel = new JLabel(subtitulo);
        subtitleLabel.setFont(new Font("Poppins", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        subtitleLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        
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
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_PRIMARY, 3),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ));
                overlay.setBackground(new Color(212, 165, 199, 200));
                overlayText.setVisible(true);
                card.setBackground(new Color(252, 252, 252));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ));
                overlay.setBackground(new Color(0, 0, 0, 0));
                overlayText.setVisible(false);
                card.setBackground(Color.WHITE);
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
        featuredProductsPanel.setBackground(new Color(248, 249, 250));
        featuredProductsPanel.setBorder(new EmptyBorder(60, 50, 60, 50));
        
        JLabel title = new JLabel("Productos Destacados");
        title.setFont(new Font("Playfair Display", Font.BOLD, 36));
        title.setForeground(new Color(51, 51, 51));
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
        
        JButton verTodosBtn = crearBotonPrimario("Ver Todos los Productos", e -> abrirTienda());
        verTodosBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        verTodosBtn.setBorder(new EmptyBorder(30, 0, 0, 0));
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
        newsletterPanel.setBackground(new Color(102, 126, 234));
        newsletterPanel.setBorder(new EmptyBorder(60, 50, 60, 50));
        
        JLabel title = new JLabel("Mantente a la Moda");
        title.setFont(new Font("Playfair Display", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Suscríbete a nuestro boletín y obtén 20% de descuento en tu primera compra");
        subtitle.setFont(new Font("Poppins", Font.PLAIN, 16));
        subtitle.setForeground(Color.WHITE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(15, 0, 30, 0));
        
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(500, 50));
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
        suscribirBtn.setForeground(new Color(102, 126, 234));
        suscribirBtn.setBackground(Color.WHITE);
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
        footerPanel.setBackground(new Color(10, 10, 10));
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
        copyright.setForeground(new Color(224, 224, 224));
        
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
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        section.add(title);
        
        if (descripcion != null) {
            JLabel desc = new JLabel("<html><div style='width: 200px;'>" + descripcion + "</div></html>");
            desc.setFont(new Font("Poppins", Font.PLAIN, 12));
            desc.setForeground(new Color(224, 224, 224));
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
                link.setForeground(new Color(224, 224, 224));
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
                
                recienLlegadosProductsPanel.add(Box.createHorizontalStrut(15));
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
    }
    
    /**
     * Crea un card de producto para la sección de recién llegados
     */
    private JPanel crearProductCardRecienLlegado(UI.Producto producto) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(240, 350));
        card.setMaximumSize(new Dimension(240, 350));
        
        // Panel de imagen con badge "NUEVO"
        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(240, 240));
        imageContainer.setMaximumSize(new Dimension(240, 240));
        imageContainer.setOpaque(false);
        
        // Badge "NUEVO"
        JLabel badge = new JLabel("NUEVO");
        badge.setFont(new Font("Poppins", Font.BOLD, 10));
        badge.setForeground(Color.WHITE);
        badge.setBackground(new Color(255, 107, 107));
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));
        badge.setHorizontalAlignment(JLabel.CENTER);
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Imagen del producto
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(240, 240));
        imageLabel.setMaximumSize(new Dimension(240, 240));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        
        // Cargar imagen
        if (producto.getImgUrl() != null && !producto.getImgUrl().isEmpty()) {
            try {
                String rutaAbsoluta = ConfigImagenes.obtenerRutaAbsoluta(producto.getImgUrl());
                File archivo = new File(rutaAbsoluta);
                if (archivo.exists()) {
                    ImageIcon icon = new ImageIcon(rutaAbsoluta);
                    Image img = icon.getImage();
                    Image imgEscalada = img.getScaledInstance(240, 240, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(imgEscalada));
                    imageLabel.setText("");
                } else {
                    ImageIcon icon = ConfigImagenes.cargarImagenEscalada(producto.getImgUrl(), 240, 240);
                    if (icon != null) {
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    } else {
                        imageLabel.setText("<html><div style='text-align: center; padding-top: 100px; color: #999;'>" +
                            "Sin imagen</div></html>");
                    }
                }
            } catch (Exception e) {
                imageLabel.setText("<html><div style='text-align: center; padding-top: 100px; color: #999;'>" +
                    "Sin imagen</div></html>");
            }
        } else {
            imageLabel.setText("<html><div style='text-align: center; padding-top: 100px; color: #999;'>" +
                "Sin imagen</div></html>");
        }
        
        // Panel para el badge (arriba a la izquierda)
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        badgePanel.setOpaque(false);
        badgePanel.add(badge);
        
        imageContainer.add(badgePanel, BorderLayout.NORTH);
        imageContainer.add(imageLabel, BorderLayout.CENTER);
        
        // Info del producto
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(producto.getNombre() != null ? producto.getNombre() : "Sin nombre");
        nameLabel.setFont(new Font("Poppins", Font.BOLD, 13));
        nameLabel.setForeground(new Color(51, 51, 51));
        
        JLabel priceLabel = new JLabel("$" + String.format("%.2f", producto.getPrecio()));
        priceLabel.setFont(new Font("Poppins", Font.BOLD, 15));
        priceLabel.setForeground(COLOR_PRIMARY);
        priceLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        
        card.add(imageContainer, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetallesProducto(producto);
            }
            
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
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ));
                card.setBackground(Color.WHITE);
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
        nameLabel.setForeground(new Color(51, 51, 51));
        
        JLabel priceLabel = new JLabel("$" + String.format("%.2f", producto.getPrecio()));
        priceLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        priceLabel.setForeground(new Color(51, 51, 51));
        
        JLabel categoryLabel = new JLabel(producto.getCategorias() != null ? producto.getCategorias() : "");
        categoryLabel.setFont(new Font("Poppins", Font.PLAIN, 11));
        categoryLabel.setForeground(new Color(102, 102, 102));
        
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
