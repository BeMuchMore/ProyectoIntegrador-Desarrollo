package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import UI.Conexion;
import UI.util.ModernStyles;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * Ventana de Términos y Condiciones con diseño moderno
 * @author calam
 */
public class Condiciones extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Condiciones.class.getName());
    
    // Colores consistentes con el resto de la aplicación
    private static final Color COLOR_PRIMARY = new Color(107, 45, 77); // #6B2D4D
    private static final Color COLOR_SECONDARY = new Color(139, 74, 107); // #8B4A6B
    private static final Color COLOR_TEXT_PRIMARY = new Color(26, 26, 26); // #1a1a1a
    private static final Color COLOR_TEXT_SECONDARY = new Color(102, 102, 102); // #666666
    private static final Color COLOR_TEXT_LIGHT = new Color(255, 255, 255); // #FFFFFF
    private static final Color COLOR_BG_LIGHT = new Color(250, 246, 249); // #FAF6F9
    private static final Color COLOR_BG_GRADIENT_START = new Color(255, 245, 252); // Rosa muy claro
    private static final Color COLOR_BG_GRADIENT_END = new Color(250, 246, 249); // #FAF6F9
    
    // Componentes principales
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel footerPanel;
    private JLabel nombreAppLabel;
    private JLabel correoAppLabel;
    private JTextPane terminosPane;
    private JButton btnCerrar;
    private String nombreApp = "Fashion";
    private String correoApp = "";
    
    /**
     * Creates new form Condiciones
     */
    public Condiciones(java.awt.Window parent) {
        super(parent, parent == null ? java.awt.Dialog.ModalityType.MODELESS : java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        cargarComplementos();
        cargarTerminos();
        
        // Asegurar que aparezca encima
        setAlwaysOnTop(false); // No siempre encima para no molestar
        if (parent != null) {
            setLocationRelativeTo(parent);
        } else {
            setLocationRelativeTo(null);
        }
        
        // Asegurar que aparezca al frente cuando se hace visible
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                toFront();
                requestFocus();
            }
        });
    }
    
    // Constructor con Frame para compatibilidad
    public Condiciones(java.awt.Frame parent) {
        super(parent, true); // Modal dialog
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        cargarComplementos();
        cargarTerminos();
        if (parent != null) {
            setLocationRelativeTo(parent);
        } else {
            setLocationRelativeTo(null);
        }
    }
    
    // Constructor sin parámetros para compatibilidad
    public Condiciones() {
        this((java.awt.Window) null);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true); // Sin decoración del sistema
        setTitle("Términos y Condiciones de Uso");
        setSize(1200, 700);
        setResizable(false);
        
        // Panel principal con diseño moderno
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente elegante
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(250, 246, 249), // #FAF6F9
                    getWidth(), getHeight(), new Color(255, 255, 255)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(true);
        
        // Header decorativo moderno
        crearHeaderModerno();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel de contenido con scroll
        crearPanelContenido();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        crearPanelFooter();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
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
     * Crea un header moderno y decorativo
     */
    private void crearHeaderModerno() {
        headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente elegante
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_PRIMARY,
                    getWidth(), getHeight(), COLOR_SECONDARY
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Formas decorativas
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(getWidth() - 100, -30, 200, 200);
                g2.fillOval(-50, getHeight() - 50, 150, 150);
                
                g2.dispose();
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(1200, 100));
        headerPanel.setMaximumSize(new Dimension(1200, 100));
        headerPanel.setOpaque(true);
        headerPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        
        JLabel titleLabel = new JLabel("📋 TÉRMINOS Y CONDICIONES DE USO");
        titleLabel.setFont(new Font("Playfair Display", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel de contenido con scroll mejorado y diseño moderno
     */
    private void crearPanelContenido() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false);
        
        // Panel interno con el contenido - DISEÑO MODERNO Y LEGIBLE CON ANCHO LIMITADO
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setOpaque(false);
        innerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        innerPanel.setBorder(new EmptyBorder(35, 20, 45, 20)); // Padding reducido lateral para centrar
        
        // Badge de fecha actualización - FUERA del panel blanco
        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fechaPanel.setOpaque(false);
        fechaPanel.setBorder(new EmptyBorder(0, 0, 30, 0));
        fechaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel fechaIcon = new JLabel("📅");
        fechaIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        fechaPanel.add(fechaIcon);
        
        JLabel fechaLabel = new JLabel("Última actualización: ");
        fechaLabel.setFont(new Font("Poppins", Font.PLAIN, 13));
        fechaLabel.setForeground(COLOR_TEXT_SECONDARY);
        fechaLabel.setBorder(new EmptyBorder(0, 8, 0, 0));
        fechaPanel.add(fechaLabel);
        
        innerPanel.add(fechaPanel);
        
        // Panel de contenido con fondo blanco y sombra - ANCHO LIMITADO PARA MEJOR LEGIBILIDAD
        JPanel contentCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra más pronunciada
                g2.setColor(new Color(0, 0, 0, 12));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 20, 20);
                
                // Fondo blanco brillante
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);
                
                g2.dispose();
            }
        };
        contentCard.setLayout(new BorderLayout());
        contentCard.setOpaque(false);
        contentCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentCard.setMaximumSize(new Dimension(1200, 500)); // Dimensiones fijas: 1200x500
        contentCard.setPreferredSize(new Dimension(1200, 500)); // Dimensiones preferidas: 1200x500
        contentCard.setMinimumSize(new Dimension(1200, 500));
        contentCard.setBorder(new EmptyBorder(30, 50, 30, 50)); // Padding con márgenes izquierda y derecha
        
        // JTextPane para mostrar los términos con formato HTML mejorado - TEXTO GRANDE Y LEGIBLE
        terminosPane = new JTextPane();
        terminosPane.setEditable(false);
        terminosPane.setOpaque(false);
        terminosPane.setContentType("text/html");
        terminosPane.setFont(new Font("Poppins", Font.PLAIN, 20)); // Fuente base grande para legibilidad
        
        // Configurar tamaño para que esté dentro del rango 1200x500 con márgenes izquierda y derecha
        terminosPane.setMaximumSize(new Dimension(1100, Integer.MAX_VALUE)); // 1200 - 50 (izquierda) - 50 (derecha) = 1100
        terminosPane.setPreferredSize(new Dimension(1100, 440)); // 500 - 30 (arriba) - 30 (abajo) = 440
        
        // Estilos CSS mejorados - LETRAS GRANDES Y LEGIBLES CON ANCHO CONTROLADO Y AJUSTE DE TEXTO
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body { font-family: 'Poppins', Arial, sans-serif; font-size: 20px !important; line-height: 2.6 !important; color: #000000 !important; padding: 0px !important; background-color: transparent !important; max-width: 1100px !important; width: 1100px !important; word-wrap: break-word !important; overflow-wrap: break-word !important; }");
        styleSheet.addRule("h1 { font-family: 'Playfair Display', serif; font-size: 36px !important; color: #6B2D4D !important; margin-top: 50px !important; margin-bottom: 30px !important; font-weight: bold !important; border-bottom: 3px solid #6B2D4D !important; padding-bottom: 20px !important; max-width: 1100px !important; word-wrap: break-word !important; }");
        styleSheet.addRule("h2 { font-family: 'Playfair Display', serif; font-size: 30px !important; color: #6B2D4D !important; margin-top: 45px !important; margin-bottom: 25px !important; font-weight: bold !important; max-width: 1100px !important; word-wrap: break-word !important; }");
        styleSheet.addRule("p { margin-bottom: 30px !important; text-align: justify !important; line-height: 2.6 !important; font-size: 20px !important; color: #000000 !important; max-width: 1100px !important; word-wrap: break-word !important; overflow-wrap: break-word !important; }");
        styleSheet.addRule("strong { color: #6B2D4D !important; font-weight: bold !important; font-size: 21px !important; }");
        styleSheet.addRule("ul, ol { margin-left: 45px !important; margin-bottom: 30px !important; line-height: 2.6 !important; max-width: 1100px !important; word-wrap: break-word !important; }");
        styleSheet.addRule("li { margin-bottom: 20px !important; font-size: 20px !important; color: #000000 !important; word-wrap: break-word !important; }");
        
        terminosPane.setEditorKit(kit);
        terminosPane.setText("<html><body>Cargando términos y condiciones...</body></html>");
        
        // Configurar propiedades para mejor renderizado del texto
        terminosPane.putClientProperty(javax.swing.JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        
        // SCROLL DENTRO DEL PANEL BLANCO - Solo el contenido de términos tiene scroll
        JScrollPane contentScrollPane = new JScrollPane(terminosPane);
        contentScrollPane.setBorder(null);
        contentScrollPane.setOpaque(false);
        contentScrollPane.getViewport().setOpaque(false);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Aplicar el mismo scrollbar personalizado (igual que principal.java)
        personalizarScrollBar(contentScrollPane.getVerticalScrollBar());
        
        // Agregar el scroll dentro del panel blanco
        contentCard.add(contentScrollPane, BorderLayout.CENTER);
        
        // Panel wrapper para centrar el contentCard
        JPanel cardWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cardWrapper.setOpaque(false);
        cardWrapper.add(contentCard);
        
        innerPanel.add(cardWrapper);
        
        // Agregar el innerPanel al contentPanel SIN scroll externo (solo el scroll interno)
        contentPanel.add(innerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel del footer con diseño moderno y elegante
     */
    private void crearPanelFooter() {
        footerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente elegante
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_PRIMARY,
                    getWidth(), getHeight(), COLOR_SECONDARY
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Línea decorativa superior
                g2.setColor(new Color(255, 255, 255, 20));
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawLine(0, 0, getWidth(), 0);
                
                // Formas decorativas sutiles
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillOval(getWidth() - 100, -30, 150, 150);
                
                g2.dispose();
            }
        };
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setPreferredSize(new Dimension(1200, 75));
        footerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Panel izquierdo con información - diseño mejorado
        JPanel leftFooter = new JPanel();
        leftFooter.setLayout(new BoxLayout(leftFooter, BoxLayout.Y_AXIS));
        leftFooter.setOpaque(false);
        leftFooter.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        // Nombre de la app con estilo elegante
        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nombrePanel.setOpaque(false);
        nombreAppLabel = new JLabel("Fashion");
        nombreAppLabel.setFont(new Font("Playfair Display", Font.BOLD, 16));
        nombreAppLabel.setForeground(COLOR_TEXT_LIGHT);
        nombrePanel.add(nombreAppLabel);
        
        // Separador elegante
        JLabel separator = new JLabel(" • ");
        separator.setFont(new Font("Poppins", Font.PLAIN, 12));
        separator.setForeground(new Color(255, 255, 255, 180));
        nombrePanel.add(separator);
        
        correoAppLabel = new JLabel("contacto@fashion.com");
        correoAppLabel.setFont(new Font("Poppins", Font.PLAIN, 12));
        correoAppLabel.setForeground(new Color(255, 255, 255, 220));
        nombrePanel.add(correoAppLabel);
        
        // Copyright con estilo más sutil
        JLabel copyright = new JLabel("© 2025 Todos los derechos reservados");
        copyright.setFont(new Font("Poppins", Font.PLAIN, 10));
        copyright.setForeground(new Color(255, 255, 255, 160));
        copyright.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        leftFooter.add(nombrePanel);
        leftFooter.add(copyright);
        
        footerPanel.add(leftFooter, BorderLayout.WEST);
        
        // Panel derecho con información adicional - diseño mejorado
        JPanel rightFooter = new JPanel();
        rightFooter.setLayout(new BoxLayout(rightFooter, BoxLayout.Y_AXIS));
        rightFooter.setOpaque(false);
        rightFooter.setAlignmentY(Component.CENTER_ALIGNMENT);
        rightFooter.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        // Texto informativo elegante
        JLabel infoLabel = new JLabel("Términos y Condiciones de Uso");
        infoLabel.setFont(new Font("Poppins", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(255, 255, 255, 200));
        infoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JLabel versionLabel = new JLabel("Versión 1.0");
        versionLabel.setFont(new Font("Poppins", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(255, 255, 255, 160));
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        versionLabel.setBorder(new EmptyBorder(3, 0, 0, 0));
        
        rightFooter.add(infoLabel);
        rightFooter.add(versionLabel);
        
        footerPanel.add(rightFooter, BorderLayout.EAST);
        
        // Panel central (opcional) - se puede usar para más información
        JPanel centerFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerFooter.setOpaque(false);
        // Por ahora vacío, pero se puede agregar información adicional aquí
        footerPanel.add(centerFooter, BorderLayout.CENTER);
    }
    
    /**
     * Carga los complementos desde la base de datos
     */
    private void cargarComplementos() {
        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT NombreDeApp, CorreoApp FROM tb_complementos ORDER BY id DESC LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String nombre = rs.getString("NombreDeApp");
                String correo = rs.getString("CorreoApp");
                
                if (nombre != null && !nombre.isEmpty()) {
                    nombreApp = nombre;
                }
                if (correo != null && !correo.isEmpty()) {
                    correoApp = correo;
                }
            }
            
            // Actualizar labels
            if (nombreAppLabel != null) {
                nombreAppLabel.setText(nombreApp);
            }
            if (correoAppLabel != null) {
                correoAppLabel.setText(correoApp.isEmpty() ? "contacto@fashion.com" : correoApp);
            }
            
        } catch (SQLException e) {
            logger.severe("Error al cargar complementos: " + e.getMessage());
        }
    }
    
    /**
     * Carga los términos y condiciones desde la base de datos
     */
    private void cargarTerminos() {
        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT TerminosCondiones, UltimaActualizacion FROM tb_complementos ORDER BY id DESC LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String terminos = rs.getString("TerminosCondiones");
                Timestamp fechaActualizacion = rs.getTimestamp("UltimaActualizacion");
                
                // Actualizar fecha de actualización
                if (fechaActualizacion != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new java.util.Locale("es", "ES"));
                    // Buscar el label de fecha en el contentPanel
                    SwingUtilities.invokeLater(() -> {
                        actualizarFechaLabel(sdf.format(fechaActualizacion));
                    });
                }
                
                // Formatear y mostrar los términos
                if (terminos != null && !terminos.isEmpty()) {
                    String htmlContent = formatearTerminosHTML(terminos);
                    terminosPane.setText(htmlContent);
                } else {
                    // Términos por defecto si no hay en BD
                    terminosPane.setText(formatearTerminosHTML(obtenerTerminosPorDefecto()));
                }
            } else {
                // Términos por defecto si no hay registro
                terminosPane.setText(formatearTerminosHTML(obtenerTerminosPorDefecto()));
            }
            
        } catch (SQLException e) {
            logger.severe("Error al cargar términos: " + e.getMessage());
            terminosPane.setText(formatearTerminosHTML(obtenerTerminosPorDefecto()));
        }
    }
    
    /**
     * Actualiza el label de fecha de actualización
     */
    private void actualizarFechaLabel(String fecha) {
        SwingUtilities.invokeLater(() -> {
            // Buscar recursivamente el label de fecha en el contentPanel
            buscarYActualizarFechaLabel(contentPanel, fecha);
        });
    }
    
    /**
     * Busca recursivamente el label de fecha y lo actualiza
     */
    private void buscarYActualizarFechaLabel(Container container, String fecha) {
        Component[] components = container.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                String text = lbl.getText();
                if (text != null && text.contains("Última actualización:")) {
                    lbl.setText("Última actualización: " + fecha);
                    return;
                }
            } else if (comp instanceof Container) {
                buscarYActualizarFechaLabel((Container) comp, fecha);
            }
        }
    }
    
    /**
     * Formatea el texto plano de términos en HTML
     */
    private String formatearTerminosHTML(String texto) {
        if (texto == null || texto.isEmpty()) {
            return obtenerTerminosPorDefectoHTML();
        }
        
        // Reemplazar saltos de línea por <br/>
        String html = texto.replace("\n", "<br/>");
        
        // Detectar y formatear secciones numeradas (1., 2., etc.)
        html = html.replaceAll("(\\d+\\.)\\s+([A-ZÁÉÍÓÚÑ][^\\d]*?)(?=\\d+\\.|$)", "<h2>$1 $2</h2>");
        
        // Formatear texto entre secciones como párrafos
        String[] partes = html.split("<h2>");
        StringBuilder resultado = new StringBuilder();
        // Contenedor principal con ancho máximo fijo para controlar el ajuste del texto (1100px dentro del rango 1200x500 con márgenes)
        resultado.append("<html><body><div style='max-width: 1100px; width: 1100px; word-wrap: break-word; overflow-wrap: break-word;'>");
        
        for (int i = 0; i < partes.length; i++) {
            if (i == 0 && !partes[i].isEmpty()) {
                resultado.append("<p>").append(partes[i].replace("<br/>", "</p><p>")).append("</p>");
            } else if (!partes[i].isEmpty()) {
                String[] seccion = partes[i].split("</h2>", 2);
                resultado.append("<h2>").append(seccion[0]).append("</h2>");
                if (seccion.length > 1 && !seccion[1].trim().isEmpty()) {
                    String contenido = seccion[1].trim().replace("<br/><br/>", "</p><p>").replace("<br/>", " ");
                    resultado.append("<p>").append(contenido).append("</p>");
                }
            }
        }
        
        resultado.append("</div></body></html>");
        return resultado.toString();
    }
    
    /**
     * Obtiene términos y condiciones por defecto si no hay en BD
     */
    private String obtenerTerminosPorDefecto() {
        return "Bienvenido(a) a Fashion – Ropía Propia Colombia, una aplicación desarrollada para ofrecerte una experiencia moderna y segura en la compra de ropa y accesorios.\n\n" +
               "Al acceder y usar nuestra aplicación, aceptas los siguientes Términos y Condiciones. Te recomendamos leerlos atentamente antes de realizar cualquier compra.\n\n" +
               "1. Aceptación de los términos\n" +
               "El uso de esta aplicación implica la aceptación plena de los presentes Términos y Condiciones. Si no estás de acuerdo con ellos, te pedimos no continuar con el uso de la app.\n\n" +
               "2. Acceso a la aplicación\n" +
               "El acceso para ver productos es libre y no requiere registro. Sin embargo, para realizar compras, el usuario deberá crear una cuenta personal proporcionando información verídica, actualizada y completa.\n\n" +
               "3. Registro y cuenta de usuario\n" +
               "El usuario se compromete a mantener la confidencialidad de su cuenta y contraseña. Cualquier actividad realizada desde su cuenta se considerará responsabilidad del usuario. Fashion – Ropía Propia Colombia no se hace responsable por el uso indebido de cuentas personales.\n\n" +
               "4. Productos y precios\n" +
               "Todos los productos publicados incluyen información detallada sobre su descripción, talla, color y precio. Nos reservamos el derecho de modificar precios, descuentos o disponibilidad de productos en cualquier momento, sin previo aviso. Las imágenes son de carácter ilustrativo y pueden presentar ligeras variaciones respecto al producto real.\n\n" +
               "5. Pagos\n" +
               "Los pagos se realizan a través de las opciones disponibles en la aplicación (tarjeta, transferencia o pago contra entrega, según disponibilidad). El usuario garantiza que los datos proporcionados para el pago son verídicos y que cuenta con la autorización para utilizarlos.\n\n" +
               "6. Envíos y entregas\n" +
               "Los tiempos de entrega varían según la ciudad o municipio. Fashion – Ropía Propia Colombia se compromete a despachar los pedidos en los plazos establecidos, sin embargo, no se responsabiliza por retrasos ocasionados por transportadoras o causas de fuerza mayor. El usuario debe verificar los datos de envío antes de confirmar la compra.\n\n" +
               "7. Cambios y devoluciones\n" +
               "Podrás solicitar cambio o devolución dentro de los 5 días hábiles posteriores a la entrega, siempre que el producto no haya sido usado, lavado o modificado y conserve sus etiquetas y empaque original. Los costos de envío para devoluciones o cambios correrán por cuenta del cliente, salvo que el error sea atribuible a Fashion – Ropía Propia Colombia.\n\n" +
               "8. Propiedad intelectual\n" +
               "Todos los contenidos de la aplicación (nombre, logotipo, imágenes, textos, diseño y código) son propiedad exclusiva de Fashion – Ropía Propia Colombia y están protegidos por la legislación colombiana. Queda prohibida su copia, distribución o uso sin autorización previa.\n\n" +
               "9. Privacidad y protección de datos\n" +
               "La información personal del usuario será tratada conforme a nuestra Política de Privacidad, cumpliendo con la Ley 1581 de 2012 de Protección de Datos Personales en Colombia. Tus datos serán utilizados únicamente para la gestión de compras, envíos y comunicación con la empresa.\n\n" +
               "10. Responsabilidad\n" +
               "Fashion – Ropía Propia Colombia no será responsable por daños, pérdidas o perjuicios derivados del uso inadecuado de la aplicación o de los productos adquiridos. El usuario es responsable de revisar las especificaciones del producto antes de efectuar la compra.\n\n" +
               "11. Modificaciones\n" +
               "Nos reservamos el derecho de actualizar o modificar estos Términos y Condiciones en cualquier momento. Las modificaciones serán publicadas en la aplicación y entrarán en vigor de inmediato.\n\n" +
               "12. Contacto\n" +
               "Para más información, contacta con nuestro equipo de atención al cliente.";
    }
    
    /**
     * Obtiene términos y condiciones por defecto en formato HTML
     */
    private String obtenerTerminosPorDefectoHTML() {
        return "<html><body>" +
               "<h2>1. Aceptación de los términos</h2>" +
               "<p>El uso de esta aplicación implica la aceptación plena de los presentes Términos y Condiciones.</p>" +
               "<h2>2. Acceso a la aplicación</h2>" +
               "<p>El acceso para ver productos es libre y no requiere registro.</p>" +
               "</body></html>";
    }
    
    /**
     * Personaliza el scrollbar con un diseño moderno y atractivo (exactamente igual que principal.java)
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
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Condiciones().setVisible(true));
    }
}