package UI;

import javax.swing.*;
import javax.swing.BorderFactory;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.LineBorder;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import UI.ConfigImagenes;
import UI.util.ModernStyles;

/**
 * Ventana para mostrar productos filtrados por categoría
 * Similar en tamaño y diseño a principal.java
 */
public class Categorias extends javax.swing.JFrame {
    
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
     * Crea un panel para un producto
     */
    private JPanel crearPanelProducto(UI.Producto producto) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 320));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        
        // Panel principal con BoxLayout
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Color.WHITE);
        
        // Imagen del producto
        ImageIcon imagen = null;
        if (producto.getImgUrl() != null && !producto.getImgUrl().isEmpty()) {
            imagen = ConfigImagenes.cargarImagenEscalada(producto.getImgUrl(), 230, 180);
        }
        
        if (imagen != null) {
            JLabel lblImagen = new JLabel(imagen);
            lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenido.add(lblImagen);
        } else {
            JLabel lblNoImagen = new JLabel("Sin imagen");
            lblNoImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblNoImagen.setPreferredSize(new Dimension(230, 180));
            lblNoImagen.setHorizontalAlignment(SwingConstants.CENTER);
            lblNoImagen.setBackground(new Color(240, 240, 240));
            lblNoImagen.setOpaque(true);
            contenido.add(lblNoImagen);
        }
        
        contenido.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Nombre del producto con tipografía mejorada
        JLabel lblNombre = new JLabel("<html><center>" + producto.getNombre() + "</center></html>");
        lblNombre.setFont(ModernStyles.getSemiboldFont(14));
        lblNombre.setForeground(ModernStyles.TEXT_PRIMARY);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        ModernStyles.enableTextAntialiasing(lblNombre);
        contenido.add(lblNombre);
        
        // Precio con tipografía mejorada (color primario rosado como web)
        JLabel lblPrecio = new JLabel("$" + String.format("%.2f", producto.getPrecio()));
        lblPrecio.setFont(ModernStyles.getBoldFont(18));
        lblPrecio.setForeground(ModernStyles.PRIMARY_COLOR);
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        ModernStyles.enableTextAntialiasing(lblPrecio);
        contenido.add(lblPrecio);
        
        // Color y Stock con tipografía mejorada
        JLabel lblDetalles = new JLabel(
            "<html><center>Color: " + (producto.getColor() != null ? producto.getColor() : "N/A") + 
            "<br>Stock: " + producto.getCantidad() + " unidades</center></html>"
        );
        lblDetalles.setFont(ModernStyles.getBodySmallFont());
        lblDetalles.setForeground(ModernStyles.TEXT_SECONDARY);
        lblDetalles.setAlignmentX(Component.CENTER_ALIGNMENT);
        ModernStyles.enableTextAntialiasing(lblDetalles);
        contenido.add(lblDetalles);
        
        contenido.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Botón Ver Detalles con tipografía mejorada
        JButton btnVer = new JButton("Ver Detalles");
        btnVer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVer.setFont(ModernStyles.getSemiboldFont(12));
        ModernStyles.enableTextAntialiasing(btnVer);
        btnVer.addActionListener(e -> mostrarDetallesProducto(producto));
        contenido.add(btnVer);
        
        panel.add(contenido, BorderLayout.CENTER);
        
        // Efecto hover
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(new LineBorder(Color.BLUE, 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            }
        });
        
        return panel;
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
            // Mostrar botones de login/registro
            JButton btnIniciar = new JButton("Iniciar");
            btnIniciar.addActionListener(e -> {
                this.dispose();
                new UI.pruebas.Inicio().setVisible(true);
            });
            jPanelHeader.add(btnIniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 50, 80, 23));
            
            JButton btnRegistrar = new JButton("Registrar");
            btnRegistrar.addActionListener(e -> {
                this.dispose();
                new registro().setVisible(true);
            });
            jPanelHeader.add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 50, 100, 23));
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
        jPanelHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTituloApp.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabelTituloApp.setText("FASHION");
        jPanelHeader.add(jLabelTituloApp, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 150, 30));

        jButtonVolver.setText("← Volver");
        jButtonVolver.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jPanelHeader.add(jButtonVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 100, 30));

        jPanelMain.add(jPanelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1363, 100));

        jPanelBuscar.setBackground(new java.awt.Color(245, 245, 245));
        jPanelBuscar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextFieldBuscar.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jTextFieldBuscar.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanelBuscar.add(jTextFieldBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 400, 30));

        jButtonBuscar.setText("Buscar");
        jButtonBuscar.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jPanelBuscar.add(jButtonBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 100, 30));

        jLabelCategoria.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabelCategoria.setText("Categoría");
        jPanelBuscar.add(jLabelCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 500, 30));

        jLabelContador.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jLabelContador.setText("Mostrando 0 productos");
        jPanelBuscar.add(jLabelContador, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 300, 20));

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

