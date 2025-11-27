package UI;

import javax.swing.*;
import javax.swing.BorderFactory;
import java.awt.*;
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
    
    // Información del usuario actual
    private int userId;
    private boolean isLoggedIn;
    
    // Categoría actual
    private String categoriaActual;
    private String nombreCategoria;
    
    // Complementos de la aplicación
    private String nombreApp = "FASHION";
    private String correoApp = "";
    private String rutaLogo = "";
    
    // Lista de productos
    private List<UI.Producto> productos;
    
    // Panel contenedor de productos
    private JPanel productosPanel;
    
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
        
        // Configurar interfaz
        configurarInterfaz();
        configurarSesion();
        
        // Configurar eventos
        configurarEventos();
        
        // Cargar productos de la categoría
        cargarProductosCategoria();
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
        // Actualizar nombre de la app
        jLabelTituloApp.setText(nombreApp);
        
        // Actualizar título de categoría
        jLabelCategoria.setText(nombreCategoria);
        
        // Cargar logo si existe
        if (!rutaLogo.isEmpty()) {
            cargarLogo();
        }
    }
    
    /**
     * Carga el logo de la aplicación
     */
    private void cargarLogo() {
        try {
            String rutaAbsoluta = obtenerRutaAbsoluta(rutaLogo);
            if (rutaAbsoluta == null || rutaAbsoluta.isEmpty()) {
                return;
            }
            
            File archivo = new File(rutaAbsoluta);
            
            if (archivo.exists()) {
                ImageIcon icon = new ImageIcon(rutaAbsoluta);
                Image img = icon.getImage();
                Image imgEscalada = img.getScaledInstance(120, 40, Image.SCALE_SMOOTH);
                
                JLabel logoLabel = new JLabel(new ImageIcon(imgEscalada));
                jPanelHeader.add(logoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 40));
                jPanelHeader.revalidate();
                jPanelHeader.repaint();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar logo: " + e.getMessage());
        }
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
        // Botón volver
        jButtonVolver.addActionListener(e -> {
            this.dispose();
            new principal(userId).setVisible(true);
        });
        
        // Botón buscar
        jButtonBuscar.addActionListener(e -> buscarProductos());
        jTextFieldBuscar.addActionListener(e -> buscarProductos());
    }
    
    /**
     * Busca productos en la categoría actual
     */
    private void buscarProductos() {
        String busqueda = jTextFieldBuscar.getText().trim();
        if (busqueda.isEmpty()) {
            cargarProductosCategoria();
            return;
        }
        
        try (Connection conn = Conexion.getConnection()) {
            String query = construirQueryBusqueda(busqueda);
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            int paramIndex = 1;
            String busquedaPattern = "%" + busqueda + "%";
            pstmt.setString(paramIndex++, busquedaPattern);
            pstmt.setString(paramIndex++, busquedaPattern);
            pstmt.setString(paramIndex++, busquedaPattern);
            
            if (!categoriaActual.isEmpty() && !esCategoriaEspecial()) {
                pstmt.setString(paramIndex++, "%" + categoriaActual + "%");
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            productos.clear();
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
            System.err.println("Error en búsqueda: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error al buscar productos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
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
     * Carga productos de la categoría actual
     */
    private void cargarProductosCategoria() {
        productos.clear();
        
        try (Connection conn = Conexion.getConnection()) {
            String query = construirQueryCategoria();
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            if (!categoriaActual.isEmpty() && !esCategoriaEspecial()) {
                pstmt.setString(1, "%" + categoriaActual + "%");
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
     * Construye la query según la categoría
     */
    private String construirQueryCategoria() {
        String cat = categoriaActual.toLowerCase();
        
        if (cat.equals("mujer")) {
            // Todos los productos (asumiendo que todos son para mujer)
            return """
                SELECT p.*, 
                       COALESCE(SUM(i.cantidad), 0) as total_cantidad,
                       COALESCE(AVG(i.precio), 0) as precio_promedio
                FROM tb_productos p
                LEFT JOIN tb_inventario i ON p.id = i.id_producto
                WHERE p.estado = 'vigente'
                GROUP BY p.id
                ORDER BY p.Nombre
                LIMIT 100
                """;
        } else if (cat.equals("nuevas-llegadas") || cat.equals("nuevas llegadas")) {
            // Productos más recientes
            return """
                SELECT p.*, 
                       COALESCE(SUM(i.cantidad), 0) as total_cantidad,
                       COALESCE(AVG(i.precio), 0) as precio_promedio
                FROM tb_productos p
                LEFT JOIN tb_inventario i ON p.id = i.id_producto
                WHERE p.estado = 'vigente'
                GROUP BY p.id
                ORDER BY p.id DESC
                LIMIT 50
                """;
        } else if (cat.equals("ofertas")) {
            // Productos con descuento (puedes ajustar la lógica)
            return """
                SELECT p.*, 
                       COALESCE(SUM(i.cantidad), 0) as total_cantidad,
                       COALESCE(AVG(i.precio), 0) as precio_promedio
                FROM tb_productos p
                LEFT JOIN tb_inventario i ON p.id = i.id_producto
                WHERE p.estado = 'vigente'
                GROUP BY p.id
                HAVING precio_promedio > 0
                ORDER BY precio_promedio ASC
                LIMIT 50
                """;
        } else if (!categoriaActual.isEmpty()) {
            // Categoría específica
            return """
                SELECT p.*, 
                       COALESCE(SUM(i.cantidad), 0) as total_cantidad,
                       COALESCE(AVG(i.precio), 0) as precio_promedio
                FROM tb_productos p
                LEFT JOIN tb_inventario i ON p.id = i.id_producto
                WHERE p.estado = 'vigente'
                  AND p.Categorias LIKE ?
                GROUP BY p.id
                ORDER BY p.Nombre
                LIMIT 100
                """;
        } else {
            // Todos los productos
            return """
                SELECT p.*, 
                       COALESCE(SUM(i.cantidad), 0) as total_cantidad,
                       COALESCE(AVG(i.precio), 0) as precio_promedio
                FROM tb_productos p
                LEFT JOIN tb_inventario i ON p.id = i.id_producto
                WHERE p.estado = 'vigente'
                GROUP BY p.id
                ORDER BY p.Nombre
                LIMIT 100
                """;
        }
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
        int productosPorFila = 4; // 4 productos por fila
        
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
                String query = "SELECT nombre FROM tb_usuarios WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    String username = rs.getString("nombre");
                    
                    JLabel lblUsuario = new JLabel("Bienvenido, " + username);
                    lblUsuario.setFont(ModernStyles.getSemiboldFont(13));
                    lblUsuario.setForeground(ModernStyles.TEXT_PRIMARY);
                    ModernStyles.enableTextAntialiasing(lblUsuario);
                    jPanelHeader.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 50, 200, 23));
                    
                    JButton btnPerfil = new JButton("Mi Perfil");
                    btnPerfil.addActionListener(e -> {
                        this.dispose();
                        new PerfilUsuario(userId).setVisible(true);
                    });
                    jPanelHeader.add(btnPerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 80, 100, 23));
                    
                    JButton btnCarrito = new JButton("Carrito");
                    btnCarrito.addActionListener(e -> {
                        this.dispose();
                        new CarritoCompras(userId).setVisible(true);
                    });
                    jPanelHeader.add(btnCarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 80, 100, 23));
                    
                    JButton btnCerrarSesion = new JButton("Cerrar Sesión");
                    btnCerrarSesion.addActionListener(e -> {
                        this.dispose();
                        new principal(0).setVisible(true);
                    });
                    jPanelHeader.add(btnCerrarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(1250, 50, 120, 23));
                }
            } catch (SQLException e) {
                System.err.println("Error al obtener datos del usuario: " + e.getMessage());
            }
        } else {
            // Mostrar botones de login/registro sin cerrar la página actual
            JButton btnIniciar = crearBotonHeader("Iniciar", e -> {
                Inicio ventanaLogin = new Inicio();
                ventanaLogin.setVisible(true);
            });
            jPanelHeader.add(btnIniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 50, 100, 35));
            
            JButton btnRegistrar = crearBotonHeader("Registrar", e -> {
                registro ventanaRegistro = new registro();
                ventanaRegistro.setVisible(true);
            });
            jPanelHeader.add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 50, 100, 35));
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

        jPanelHeader.setBackground(new java.awt.Color(255, 255, 255));
        jPanelHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 30)),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        jPanelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTituloApp.setFont(new Font("Playfair Display", Font.BOLD, 24));
        jLabelTituloApp.setText("FASHION");
        jLabelTituloApp.setForeground(COLOR_PRIMARY);
        jPanelHeader.add(jLabelTituloApp, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 150, 30));

        jButtonVolver.setText("← Volver");
        jButtonVolver.setFont(new Font("Poppins", Font.PLAIN, 14));
        jButtonVolver.setForeground(COLOR_PRIMARY);
        jPanelHeader.add(jButtonVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 100, 30));

        jPanelMain.add(jPanelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1363, 100));

        jPanelBuscar.setBackground(COLOR_BG_LIGHT);
        jPanelBuscar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelBuscar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        jTextFieldBuscar.setFont(new Font("Poppins", Font.PLAIN, 14));
        jTextFieldBuscar.setPreferredSize(new java.awt.Dimension(400, 35));
        jTextFieldBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        jPanelBuscar.add(jTextFieldBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 400, 35));

        jButtonBuscar.setText("Buscar");
        jButtonBuscar.setFont(new Font("Poppins", Font.BOLD, 14));
        jButtonBuscar.setForeground(COLOR_TEXT_LIGHT);
        jButtonBuscar.setBackground(COLOR_PRIMARY);
        jPanelBuscar.add(jButtonBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 120, 35));

        jLabelCategoria.setFont(new Font("Playfair Display", Font.BOLD, 32));
        jLabelCategoria.setText("Categoría");
        jLabelCategoria.setForeground(COLOR_TEXT_PRIMARY);
        jPanelBuscar.add(jLabelCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 500, 35));

        jLabelContador.setFont(new Font("Poppins", Font.PLAIN, 14));
        jLabelContador.setText("Mostrando 0 productos");
        jLabelContador.setForeground(COLOR_TEXT_SECONDARY);
        jPanelBuscar.add(jLabelContador, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 85, 300, 25));

        jPanelMain.add(jPanelBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 1363, 120));

        productosPanel.setBackground(new java.awt.Color(255, 255, 255));
        productosPanel.setLayout(new java.awt.GridBagLayout());
        jScrollPane1.setViewportView(productosPanel);

        jPanelMain.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 1363, 480));

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

