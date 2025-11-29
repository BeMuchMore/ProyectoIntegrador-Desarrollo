package UI;

import javax.swing.*;
import javax.swing.BorderFactory;
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
import javax.swing.border.EmptyBorder;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import UI.ConfigImagenes;
import UI.util.ModernStyles;
import UI.Conexion;
import UI.SessionManager;
import UI.CarritoCompras;

/**
 * Ventana para mostrar productos filtrados por categoría
 * Similar en tamaño y diseño a principal.java
 */
public class Categorias extends javax.swing.JFrame {
    
    // Colores accesibles WCAG AAA - Paleta femenina y seria (mismos que principal)
    private static final Color COLOR_PRIMARY = new Color(107, 45, 77); // #6B2D4D
    private static final Color COLOR_SECONDARY = new Color(139, 74, 107); // #8B4A6B
    private static final Color COLOR_ACCENT = new Color(168, 85, 122); // #A8557A
    private static final Color COLOR_TEXT_PRIMARY = new Color(26, 26, 26); // #1a1a1a
    private static final Color COLOR_TEXT_SECONDARY = new Color(51, 51, 51); // #333333
    private static final Color COLOR_TEXT_LIGHT = new Color(255, 255, 255); // #FFFFFF
    private static final Color COLOR_BG_LIGHT = new Color(250, 250, 250); // #FAFAFA
    private static final Color COLOR_BG_DARK = new Color(91, 29, 61); // #5B1D3D - Morado muy oscuro (para headers)
    
    // Información del usuario actual
    private int userId;
    private String username;
    private String cargo;
    private boolean isLoggedIn;
    
    // Categoría actual
    private String categoriaActual;
    private String nombreCategoria;
    
    // Complementos de la aplicación
    private String nombreApp = "FASHION";
    private String correoApp = "";
    private String rutaLogo = "";
    
    // Componentes del header
    private JPanel headerTopPanel;
    private JPanel headerMainPanel;
    
    // Lista de productos
    private List<UI.Producto> productos;
    
    // Panel contenedor de productos
    private JPanel productosPanel;
    
    // Panel lateral izquierdo con filtros
    private JPanel sidebarPanel;
    
    // Filtros seleccionados
    private String filtroTalla = null;
    private String filtroColor = null;
    
    /**
     * Constructor con categoría específica
     * @param userId ID del usuario (0 si no está logueado)
     * @param categoria Categoría a mostrar (ej: "Blusas & Tops", "Pantalones", "Mujer", "Nuevas Llegadas", "Ofertas")
     */
    public Categorias(int userId, String categoria) {
        this.userId = userId;
        this.isLoggedIn = (userId > 0);
        this.categoriaActual = categoria != null ? categoria : "";
        this.nombreCategoria = obtenerNombreCategoria(categoria);
        this.productos = new ArrayList<>();
        
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        // Aplicar estilos modernos
        aplicarEstilosModernos();
        
        // Cargar complementos
        cargarComplementos();
        
        // Crear headers (después de cargar complementos para tener correoApp y rutaLogo)
        crearHeaderTop();
        crearHeaderMain();
        
        // Agregar headers al panel principal
        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
        headerContainer.setOpaque(false);
        headerContainer.add(headerTopPanel);
        headerContainer.add(headerMainPanel);
        jPanelMain.add(headerContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1363, 95));
        jPanelMain.revalidate();
        jPanelMain.repaint();
        
        // Crear sidebar con filtros
        crearSidebar();
        
        // Configurar interfaz
        configurarInterfaz();
        configurarSesion();
        
        // Configurar eventos
        configurarEventos();
        
        // Agregar sidebar al layout
        agregarSidebarAlLayout();
        
        // Cargar productos de la categoría
        cargarProductosCategoria();
    }
    
    /**
     * Agrega el sidebar al layout principal
     */
    private void agregarSidebarAlLayout() {
        jPanelMain.add(sidebarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 155, 280, 550));
    }
    
    /**
     * Aplica estilos modernos a los componentes
     */
    private void aplicarEstilosModernos() {
        // Aplicar renderizado mejorado a todos los componentes principales
        // Los componentes específicos ya tienen sus estilos aplicados en los métodos correspondientes
    }
    
    /**
     * Convierte el ID de categoría a nombre legible
     */
    private String obtenerNombreCategoria(String categoria) {
        if (categoria == null || categoria.isEmpty()) {
            // Si no hay categoría, usar "Mujer" como predeterminada
            categoriaActual = "Mujer";
            return "Mujer";
        }
        
        switch (categoria.toLowerCase()) {
            case "blusas-tops":
            case "blusas & tops":
                categoriaActual = "Blusas & Tops"; // Actualizar para búsqueda en BD
                return "Blusas & Tops";
            case "pantalones":
                categoriaActual = "Pantalones";
                return "Pantalones";
            case "vestidos":
                categoriaActual = "Vestidos";
                return "Vestidos";
            case "calzado":
                categoriaActual = "Calzado";
                return "Calzado";
            case "accesorios":
                categoriaActual = "Accesorios";
                return "Accesorios";
            case "mujer":
                categoriaActual = "Mujer";
                return "Mujer";
            case "nuevas-llegadas":
            case "nuevas llegadas":
                categoriaActual = "Nuevas Llegadas";
                return "Nuevas Llegadas";
            case "ofertas":
                categoriaActual = "Ofertas";
                return "Ofertas";
            default:
                categoriaActual = categoria;
                return categoria;
        }
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
                String logo = rs.getString("Logo");
                
                if (nombre != null && !nombre.isEmpty()) {
                    nombreApp = nombre;
                }
                if (correo != null && !correo.isEmpty()) {
                    correoApp = correo;
                }
                if (logo != null && !logo.isEmpty()) {
                    rutaLogo = logo;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar complementos: " + e.getMessage());
        }
    }
    
    /**
     * Configura la interfaz con los complementos cargados
     */
    private void configurarInterfaz() {
        // Configurar panel de categoría mejorado
        configurarPanelCategoria();
    }
    
    /**
     * Configura el panel de categoría con título y contador (más pequeño)
     */
    private void configurarPanelCategoria() {
        jPanelBuscar.removeAll();
        
        // Panel superior compacto con título y contador
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(nombreCategoria);
        titleLabel.setFont(new Font("Playfair Display", Font.BOLD, 24));
        titleLabel.setForeground(COLOR_PRIMARY);
        
        jLabelContador.setFont(new Font("Poppins", Font.PLAIN, 12));
        jLabelContador.setText("Mostrando 0 productos");
        jLabelContador.setForeground(COLOR_TEXT_SECONDARY);
        jLabelContador.setBorder(new EmptyBorder(3, 0, 0, 0));
        
        JPanel titleLeft = new JPanel();
        titleLeft.setLayout(new BoxLayout(titleLeft, BoxLayout.Y_AXIS));
        titleLeft.setOpaque(false);
        titleLeft.add(titleLabel);
        titleLeft.add(jLabelContador);
        
        titlePanel.add(titleLeft, BorderLayout.WEST);
        jPanelBuscar.add(titlePanel, BorderLayout.CENTER);
        jPanelBuscar.revalidate();
        jPanelBuscar.repaint();
    }
    
    /**
     * Crea el panel lateral izquierdo con categorías y filtros
     */
    private void crearSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(250, 246, 249)); // COLOR_BG_GRADIENT_START
        sidebarPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        sidebarPanel.setPreferredSize(new Dimension(280, 0));
        
        // Sección de Categorías
        JPanel categoriasSection = crearSeccionFiltro("Categorías", false);
        
        String[] categorias = {"Todas", "Mujer", "Nueva Colección", "Blusas & Tops", "Pantalones", "Vestidos", "Accesorios", "Ofertas"};
        for (String cat : categorias) {
            boolean esActiva = cat.equalsIgnoreCase(nombreCategoria) || 
                             (cat.equals("Todas") && (categoriaActual == null || categoriaActual.isEmpty()));
            JButton catBtn = crearBotonSidebar(cat, esActiva);
            final String categoriaSeleccionada = cat;
            catBtn.addActionListener(e -> {
                if (categoriaSeleccionada.equals("Todas")) {
                    this.dispose();
                    new Categorias(userId, "").setVisible(true);
                } else {
                    this.dispose();
                    new Categorias(userId, categoriaSeleccionada).setVisible(true);
                }
            });
            categoriasSection.add(catBtn);
        }
        
        sidebarPanel.add(categoriasSection);
        sidebarPanel.add(Box.createVerticalStrut(20));
        
        // Sección de Filtros - Tallas
        JPanel tallasSection = crearSeccionFiltro("Filtrar por Talla", true);
        cargarFiltroTallas(tallasSection);
        sidebarPanel.add(tallasSection);
        sidebarPanel.add(Box.createVerticalStrut(20));
        
        // Sección de Filtros - Colores
        JPanel coloresSection = crearSeccionFiltro("Filtrar por Color", true);
        cargarFiltroColores(coloresSection);
        sidebarPanel.add(coloresSection);
        
        sidebarPanel.add(Box.createVerticalGlue());
    }
    
    /**
     * Crea una sección de filtro con título
     */
    private JPanel crearSeccionFiltro(String titulo, boolean esFiltro) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        titleLabel.setForeground(COLOR_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        section.add(titleLabel);
        
        return section;
    }
    
    /**
     * Crea un botón para el sidebar
     */
    private JButton crearBotonSidebar(String texto, boolean activo) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (activo) {
                    g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 150));
                } else {
                    g2.setColor(Color.WHITE);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                if (activo) {
                    g2.setColor(COLOR_PRIMARY);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Poppins", Font.PLAIN, 13));
        btn.setForeground(activo ? COLOR_TEXT_LIGHT : COLOR_TEXT_PRIMARY);
        btn.setBackground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!activo) {
                    btn.setForeground(COLOR_SECONDARY);
                }
                btn.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(activo ? COLOR_TEXT_LIGHT : COLOR_TEXT_PRIMARY);
                btn.repaint();
            }
        });
        
        return btn;
    }
    
    /**
     * Carga las opciones de filtro por talla
     */
    private void cargarFiltroTallas(JPanel container) {
        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT DISTINCT t.id, t.nombre FROM tb_tallas t " +
                          "JOIN tb_inventario i ON t.id = i.id_talla " +
                          "JOIN tb_productos p ON i.id_producto = p.id " +
                          "WHERE p.estado = 'vigente' AND i.cantidad > 0 " +
                          "ORDER BY t.nombre";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            List<String> tallas = new ArrayList<>();
            while (rs.next()) {
                tallas.add(rs.getString("nombre"));
            }
            
            for (String talla : tallas) {
                JCheckBox checkBox = crearCheckBoxFiltro(talla, filtroTalla != null && filtroTalla.equals(talla));
                final String tallaSeleccionada = talla;
                checkBox.addActionListener(e -> {
                    if (checkBox.isSelected()) {
                        filtroTalla = tallaSeleccionada;
                    } else {
                        filtroTalla = null;
                    }
                    cargarProductosCategoria();
                });
                container.add(checkBox);
                container.add(Box.createVerticalStrut(8));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar tallas: " + e.getMessage());
        }
    }
    
    /**
     * Carga las opciones de filtro por color
     */
    private void cargarFiltroColores(JPanel container) {
        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT DISTINCT Color FROM tb_productos " +
                          "WHERE estado = 'vigente' AND Color IS NOT NULL AND Color != '' " +
                          "ORDER BY Color";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            List<String> colores = new ArrayList<>();
            while (rs.next()) {
                String color = rs.getString("Color");
                if (color != null && !color.isEmpty() && !colores.contains(color)) {
                    colores.add(color);
                }
            }
            
            for (String color : colores) {
                JCheckBox checkBox = crearCheckBoxFiltro(color, filtroColor != null && filtroColor.equals(color));
                final String colorSeleccionado = color;
                checkBox.addActionListener(e -> {
                    if (checkBox.isSelected()) {
                        filtroColor = colorSeleccionado;
                    } else {
                        filtroColor = null;
                    }
                    cargarProductosCategoria();
                });
                container.add(checkBox);
                container.add(Box.createVerticalStrut(8));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar colores: " + e.getMessage());
        }
    }
    
    /**
     * Crea un checkbox para filtros
     */
    private JCheckBox crearCheckBoxFiltro(String texto, boolean seleccionado) {
        JCheckBox checkBox = new JCheckBox(texto);
        checkBox.setSelected(seleccionado);
        checkBox.setFont(new Font("Poppins", Font.PLAIN, 13));
        checkBox.setForeground(COLOR_TEXT_PRIMARY);
        checkBox.setOpaque(false);
        checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        return checkBox;
    }
    
    /**
     * Convierte una ruta relativa de BD a ruta absoluta del sistema
     */
    private String obtenerRutaAbsoluta(String rutaBD) {
        return ConfigImagenes.obtenerRutaAbsoluta(rutaBD);
    }
    
    /**
     * Configura los eventos de los botones
     */
    private void configurarEventos() {
        // Los eventos de búsqueda se manejan en el header
        // Los filtros de categorías se manejan en initComponents()
    }
    
    /**
     * Crea un botón de filtro de categoría
     */
    private JButton crearBotonFiltro(String texto, boolean activo) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (activo) {
                    // Fondo activo con gradiente
                    GradientPaint gradient = new GradientPaint(
                        0, 0, COLOR_PRIMARY,
                        getWidth(), getHeight(), COLOR_SECONDARY
                    );
                    g2.setPaint(gradient);
                } else {
                    // Fondo inactivo con borde
                    g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 20));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                if (!activo) {
                    // Borde para botones inactivos
                    g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 100));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Poppins", Font.BOLD, 13));
        btn.setForeground(activo ? COLOR_TEXT_LIGHT : COLOR_PRIMARY);
        btn.setBackground(activo ? COLOR_PRIMARY : Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setMaximumSize(new Dimension(120, 38));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!activo) {
                    btn.setForeground(COLOR_SECONDARY);
                }
                btn.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(activo ? COLOR_TEXT_LIGHT : COLOR_PRIMARY);
                btn.repaint();
            }
        });
        
        return btn;
    }
    
    /**
     * Construye la query de búsqueda según la categoría
     */
    private String construirQueryBusqueda(String busqueda) {
        StringBuilder query = new StringBuilder("""
            SELECT p.*, 
                   COALESCE(SUM(i.cantidad), 0) as total_cantidad,
                   COALESCE(AVG(i.precio), 0) as precio_promedio
            FROM tb_productos p
            LEFT JOIN tb_inventario i ON p.id = i.id_producto
            WHERE p.estado = 'vigente'
              AND (p.Nombre LIKE ? OR p.Descripcion LIKE ? OR p.Categorias LIKE ?)
            """);
        
        if (!categoriaActual.isEmpty() && !esCategoriaEspecial()) {
            query.append(" AND p.Categorias LIKE ?");
        }
        
        query.append(" GROUP BY p.id ORDER BY p.Nombre LIMIT 50");
        
        return query.toString();
    }
    
    /**
     * Verifica si la categoría es especial (Mujer, Nuevas Llegadas, Ofertas)
     */
    private boolean esCategoriaEspecial() {
        String cat = categoriaActual.toLowerCase();
        return cat.equals("mujer") || cat.equals("nuevas-llegadas") || 
               cat.equals("nuevas llegadas") || cat.equals("ofertas");
    }
    
    /**
     * Carga productos de la categoría actual con filtros
     */
    private void cargarProductosCategoria() {
        productos.clear();
        
        try (Connection conn = Conexion.getConnection()) {
            QueryResult queryResult = construirQueryCategoria();
            PreparedStatement pstmt = conn.prepareStatement(queryResult.query);
            
            int paramIndex = 1;
            
            // Parámetro de categoría
            if (!categoriaActual.isEmpty() && !esCategoriaEspecial()) {
                pstmt.setString(paramIndex++, "%" + categoriaActual + "%");
            }
            
            // Parámetro de talla
            if (filtroTalla != null && !filtroTalla.isEmpty()) {
                pstmt.setString(paramIndex++, filtroTalla);
            }
            
            // Parámetro de color
            if (filtroColor != null && !filtroColor.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + filtroColor + "%");
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                UI.Producto p = new UI.Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("Nombre"));
                p.setImgUrl(rs.getString("ImgUrl"));
                p.setPrecio(rs.getDouble("precio_promedio"));
                p.setColor(rs.getString("Color"));
                p.setCantidad(rs.getInt("total_cantidad"));
                productos.add(p);
            }
            
            mostrarProductos();
            
        } catch (SQLException e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error al cargar productos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Clase auxiliar para resultado de query
     */
    private static class QueryResult {
        String query;
        QueryResult(String query) {
            this.query = query;
        }
    }
    
    /**
     * Construye la query según la categoría y filtros
     */
    private QueryResult construirQueryCategoria() {
        String cat = categoriaActual.toLowerCase();
        StringBuilder query = new StringBuilder("""
            SELECT DISTINCT p.*, 
                   COALESCE(SUM(i.cantidad), 0) as total_cantidad,
                   COALESCE(AVG(i.precio), 0) as precio_promedio
            FROM tb_productos p
            LEFT JOIN tb_inventario i ON p.id = i.id_producto
            """);
        
        // Join con tallas si hay filtro de talla
        if (filtroTalla != null && !filtroTalla.isEmpty()) {
            query.append("""
                JOIN tb_tallas t ON i.id_talla = t.id
                """);
        }
        
        query.append(" WHERE p.estado = 'vigente'");
        
        // Filtro de categoría
        if (!categoriaActual.isEmpty() && !esCategoriaEspecial()) {
            query.append(" AND p.Categorias LIKE ?");
        }
        
        // Filtro de talla
        if (filtroTalla != null && !filtroTalla.isEmpty()) {
            query.append(" AND t.nombre = ? AND i.cantidad > 0");
        }
        
        // Filtro de color
        if (filtroColor != null && !filtroColor.isEmpty()) {
            query.append(" AND p.Color LIKE ?");
        }
        
        query.append(" GROUP BY p.id");
        
        // Ordenamiento según categoría
        if (cat.equals("nuevas-llegadas") || cat.equals("nuevas llegadas")) {
            query.append(" ORDER BY p.FecheIngreso DESC");
        } else if (cat.equals("ofertas")) {
            query.append(" HAVING precio_promedio > 0 ORDER BY precio_promedio ASC");
        } else {
            query.append(" ORDER BY p.Nombre");
        }
        
        query.append(" LIMIT 100");
        
        return new QueryResult(query.toString());
    }
    
    /**
     * Muestra los productos en el panel
     */
    private void mostrarProductos() {
        productosPanel.removeAll();
        productosPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        int columna = 0;
        int fila = 0;
        int productosPorFila = 3; // 3 productos por fila (ajustado para sidebar)
        
        for (UI.Producto producto : productos) {
            gbc.gridx = columna;
            gbc.gridy = fila;
            
            JPanel panelProducto = crearPanelProducto(producto);
            productosPanel.add(panelProducto, gbc);
            
            columna++;
            if (columna >= productosPorFila) {
                columna = 0;
                fila++;
            }
        }
        
        // Actualizar contador
        jLabelContador.setText("Mostrando " + productos.size() + " producto(s)");
        
        productosPanel.revalidate();
        productosPanel.repaint();
    }
    
    /**
     * Crea un panel para un producto con diseño moderno
     */
    private JPanel crearPanelProducto(UI.Producto producto) {
        // Panel principal con sombra y bordes redondeados
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Sombra sutil
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 4, getWidth() - 2, getHeight() - 2, 15, 15);
                
                // Fondo blanco con bordes redondeados
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 4, 15, 15);
                
                g2.dispose();
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(280, 400));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Panel principal con BoxLayout
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        
        // Panel de imagen con bordes redondeados
        JPanel imageContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        imageContainer.setOpaque(false);
        imageContainer.setPreferredSize(new Dimension(250, 200));
        imageContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Imagen del producto mejorada
        JLabel lblImagen = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                
                // Fondo blanco
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Si hay icono, dibujarlo
                if (getIcon() != null) {
                    ImageIcon icon = (ImageIcon) getIcon();
                    Image img = icon.getImage();
                    if (img != null) {
                        int iconWidth = icon.getIconWidth();
                        int iconHeight = icon.getIconHeight();
                        int availableWidth = getWidth() - 10;
                        int availableHeight = getHeight() - 10;
                        
                        double scaleX = (double) availableWidth / iconWidth;
                        double scaleY = (double) availableHeight / iconHeight;
                        double scale = Math.min(Math.min(scaleX, scaleY), 1.0);
                        
                        int scaledWidth = (int) (iconWidth * scale);
                        int scaledHeight = (int) (iconHeight * scale);
                        
                        int x = (getWidth() - scaledWidth) / 2;
                        int y = (getHeight() - scaledHeight) / 2;
                        
                        g2.drawImage(img, x, y, scaledWidth, scaledHeight, null);
                    }
                } else {
                    // Texto si no hay imagen
                    g2.setColor(COLOR_TEXT_SECONDARY);
                    g2.setFont(new Font("Poppins", Font.PLAIN, 14));
                    FontMetrics fm = g2.getFontMetrics();
                    String texto = "Sin imagen";
                    int textX = (getWidth() - fm.stringWidth(texto)) / 2;
                    int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                    g2.drawString(texto, textX, textY);
                }
                
                g2.dispose();
            }
        };
        lblImagen.setOpaque(false);
        lblImagen.setPreferredSize(new Dimension(230, 180));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        
        // Cargar imagen
        if (producto.getImgUrl() != null && !producto.getImgUrl().isEmpty()) {
            cargarImagenProducto(lblImagen, producto.getImgUrl());
        }
        
        imageContainer.add(lblImagen, BorderLayout.CENTER);
        contenido.add(imageContainer);
        contenido.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Nombre del producto con tipografía mejorada
        String nombre = producto.getNombre() != null ? producto.getNombre() : "Sin nombre";
        if (nombre.length() > 30) {
            nombre = nombre.substring(0, 27) + "...";
        }
        JLabel lblNombre = new JLabel("<html><div style='text-align: center; line-height: 1.4;'>" + nombre + "</div></html>");
        lblNombre.setFont(new Font("Poppins", Font.BOLD, 16));
        lblNombre.setForeground(COLOR_TEXT_PRIMARY);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNombre.setBorder(new EmptyBorder(0, 10, 10, 10));
        contenido.add(lblNombre);
        
        // Precio con formato mejorado (puntos de miles) y tipografía mejorada
        JLabel lblPrecio = new JLabel("<html><div style='text-align: center;'>" +
            "<span style='font-size: 20px; font-weight: bold; color: #6B2D4D;'>" +
            formatearPrecio(producto.getPrecio()) + "</span></div></html>");
        lblPrecio.setFont(new Font("Poppins", Font.BOLD, 18));
        lblPrecio.setForeground(COLOR_PRIMARY);
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPrecio.setBorder(new EmptyBorder(0, 10, 10, 10));
        contenido.add(lblPrecio);
        
        // Color y Stock con tipografía mejorada
        JLabel lblDetalles = new JLabel(
            "<html><div style='text-align: center; line-height: 1.6; color: #333;'>" +
            "Color: " + (producto.getColor() != null ? producto.getColor() : "N/A") + 
            "<br>Stock: " + producto.getCantidad() + " unidades</div></html>"
        );
        lblDetalles.setFont(new Font("Poppins", Font.PLAIN, 12));
        lblDetalles.setForeground(COLOR_TEXT_SECONDARY);
        lblDetalles.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDetalles.setBorder(new EmptyBorder(0, 10, 15, 10));
        contenido.add(lblDetalles);
        
        // Botón Ver Detalles mejorado
        JButton btnVer = crearBotonModerno("Ver Detalles", e -> mostrarDetallesProducto(producto));
        btnVer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVer.setPreferredSize(new Dimension(200, 40));
        contenido.add(btnVer);
        
        panel.add(contenido, BorderLayout.CENTER);
        
        // Efecto hover mejorado
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_PRIMARY, 2),
                    new EmptyBorder(13, 13, 13, 13)
                ));
                panel.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(new EmptyBorder(15, 15, 15, 15));
                panel.repaint();
            }
        });
        
        return panel;
    }
    
    /**
     * Carga la imagen de un producto
     */
    private void cargarImagenProducto(JLabel imageLabel, String imgUrl) {
        try {
            String rutaAbsoluta = ConfigImagenes.obtenerRutaAbsoluta(imgUrl);
            File archivo = new File(rutaAbsoluta);
            
            if (archivo.exists()) {
                ImageIcon icon = new ImageIcon(rutaAbsoluta);
                Image img = icon.getImage();
                if (img != null) {
                    Image imgEscalada = img.getScaledInstance(230, 180, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(imgEscalada));
                    imageLabel.setText("");
                    imageLabel.repaint();
                    return;
                }
            }
            
            // Si no hay imagen local, intentar cargar imagen web
            cargarImagenWebProducto(imageLabel);
        } catch (Exception e) {
            // Intentar cargar imagen web
            cargarImagenWebProducto(imageLabel);
        }
    }
    
    /**
     * Carga una imagen web como fallback
     */
    private void cargarImagenWebProducto(JLabel imageLabel) {
        String imageUrl = "https://picsum.photos/seed/fashion-product/230/180";
        
        new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(10000); // 10 segundos
                connection.setReadTimeout(10000); // 10 segundos
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
                
                InputStream inputStream = connection.getInputStream();
                Image img = ImageIO.read(inputStream);
                inputStream.close();
                
                if (img != null && img.getWidth(null) > 0 && img.getHeight(null) > 0) {
                    Image imgEscalada = img.getScaledInstance(230, 180, Image.SCALE_SMOOTH);
                    SwingUtilities.invokeLater(() -> {
                        imageLabel.setIcon(new ImageIcon(imgEscalada));
                        imageLabel.setText("");
                        imageLabel.repaint();
                    });
                }
            } catch (java.net.SocketTimeoutException e) {
                // Timeout silencioso - el texto "Sin imagen" ya está en el paintComponent
            } catch (Exception e) {
                // Solo loguear errores no relacionados con timeout
                if (!(e instanceof java.net.SocketTimeoutException)) {
                    // Error silencioso - no mostrar en consola
                }
            }
        }).start();
    }
    
    /**
     * Crea un botón moderno con estilo
     */
    private JButton crearBotonModerno(String texto, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente de fondo
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_PRIMARY,
                    getWidth(), getHeight(), COLOR_SECONDARY
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Poppins", Font.BOLD, 14));
        btn.setForeground(COLOR_TEXT_LIGHT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.addActionListener(action);
        
        // Efecto hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setFont(new Font("Poppins", Font.BOLD, 15));
                btn.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setFont(new Font("Poppins", Font.BOLD, 14));
                btn.repaint();
            }
        });
        
        return btn;
    }
    
    /**
     * Formatea un precio con puntos de miles y dos decimales
     */
    private String formatearPrecio(double precio) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "CO"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat formatter = new DecimalFormat("#,##0.00", symbols);
        return "$" + formatter.format(precio);
    }
    
    /**
     * Crea un botón moderno para el header
     */
    private JButton crearBotonHeader(String texto, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente de fondo
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_PRIMARY,
                    getWidth(), getHeight(), COLOR_SECONDARY
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Poppins", Font.BOLD, 13));
        btn.setForeground(COLOR_TEXT_LIGHT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.addActionListener(action);
        
        return btn;
    }
    
    /**
     * Muestra los detalles de un producto
     */
    private void mostrarDetallesProducto(UI.Producto producto) {
        this.dispose();
        new DetalleProducto(userId, producto).setVisible(true);
    }
    
    /**
     * Configura la sesión del usuario
     */
    private void configurarSesion() {
        if (isLoggedIn && userId > 0) {
            try (Connection conn = Conexion.getConnection()) {
                String query = "SELECT nombre, cargo FROM tb_usuarios WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    this.username = rs.getString("nombre");
                    this.cargo = rs.getString("cargo");
                    if (this.cargo == null) {
                        this.cargo = "";
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al obtener datos del usuario: " + e.getMessage());
            }
        }
    }
    
    /**
     * Crea el header superior con información de contacto
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
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
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
                System.err.println("Error al cargar logo: " + e.getMessage());
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
                Categorias.this.dispose();
                new principal(userId).setVisible(true);
            }
        });
        logoPanel.add(logoLabel);
        
        // Navegación
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        navPanel.setOpaque(false);
        navPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        
        JLabel navInicio = crearNavLink("Inicio", e -> {
            this.dispose();
            new principal(userId).setVisible(true);
        });
        JLabel navTienda = crearNavLink("Tienda", e -> abrirTienda());
        JLabel navMujer = crearNavLink("Mujer", e -> abrirCategoria("Mujer"));
        JLabel navAccesorios = crearNavLink("Accesorios", e -> abrirCategoria("Accesorios"));
        
        navPanel.add(navInicio);
        navPanel.add(navTienda);
        navPanel.add(navMujer);
        navPanel.add(navAccesorios);

        // Campo de búsqueda integrado
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(new EmptyBorder(0, 15, 0, 15));
        searchPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(180, 28));
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

        // Botón de búsqueda
        JButton searchButton = new JButton("Buscar");
        searchButton.setFont(new Font("Poppins", Font.BOLD, 11));
        searchButton.setForeground(COLOR_TEXT_LIGHT);
        searchButton.setBackground(COLOR_PRIMARY);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setPreferredSize(new Dimension(70, 28));
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

        headerMainPanel.add(topRow, BorderLayout.SOUTH);
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
     * Métodos auxiliares para navegación
     */
    private void abrirTienda() {
        this.dispose();
        new Categorias(userId, "").setVisible(true);
    }
    
    private void abrirCategoria(String categoria) {
        this.dispose();
        new Categorias(userId, categoria).setVisible(true);
    }
    
    private void manejarUsuario() {
        if (isLoggedIn) {
            // Abrir perfil
            JOptionPane.showMessageDialog(this, "Funcionalidad de perfil en desarrollo", 
                "Perfil", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Abrir login sobre la ventana actual
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
            new principal(0).setVisible(true);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelMain = new javax.swing.JPanel();
        jPanelHeader = new javax.swing.JPanel();
        jLabelTituloApp = new javax.swing.JLabel();
        jButtonVolver = new javax.swing.JButton();
        jPanelBuscar = new javax.swing.JPanel();
        jTextFieldBuscar = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jLabelCategoria = new javax.swing.JLabel();
        jLabelContador = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productosPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelMain.setBackground(new java.awt.Color(255, 255, 255));
        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // Headers se crearán después de cargar complementos en el constructor
        // Se agregarán aquí después de que se creen

        // Panel de categoría mejorado con filtros y navegación
        // Se configurará después de cargar complementos en configurarInterfaz()
        jPanelBuscar.setLayout(new BorderLayout());
        jPanelBuscar.setOpaque(false);
        jPanelBuscar.setBorder(new EmptyBorder(15, 50, 15, 50));

        jPanelMain.add(jPanelBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 95, 1363, 60));

        productosPanel.setBackground(new java.awt.Color(255, 255, 255));
        productosPanel.setLayout(new java.awt.GridBagLayout());
        jScrollPane1.setViewportView(productosPanel);

        // Área de productos comienza después del header y del panel de título, a la derecha del sidebar
        jPanelMain.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 155, 1083, 550));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1363, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            // Si se ejecuta directamente, mostrar categoría "Mujer" como predeterminada
            new Categorias(0, "Mujer").setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JLabel jLabelCategoria;
    private javax.swing.JLabel jLabelContador;
    private javax.swing.JLabel jLabelTituloApp;
    private javax.swing.JPanel jPanelBuscar;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelMain;

    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldBuscar;
    // End of variables declaration//GEN-END:variables
}

