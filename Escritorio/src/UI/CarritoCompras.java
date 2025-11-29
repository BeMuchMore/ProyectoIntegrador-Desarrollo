package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import UI.util.ErrorHandler;
import UI.util.ModernStyles;
import UI.ConfigImagenes;
import UI.Conexion;
import UI.SessionManager;

/**
 * Ventana para ver el carrito de compras y realizar la compra
 */
public class CarritoCompras extends javax.swing.JFrame {
    
    // Colores accesibles WCAG AAA - Paleta femenina y seria (mismos que principal)
    private static final Color COLOR_PRIMARY = new Color(107, 45, 77); // #6B2D4D
    private static final Color COLOR_SECONDARY = new Color(139, 74, 107); // #8B4A6B
    private static final Color COLOR_TEXT_PRIMARY = new Color(26, 26, 26); // #1a1a1a
    private static final Color COLOR_TEXT_SECONDARY = new Color(51, 51, 51); // #333333
    private static final Color COLOR_TEXT_LIGHT = new Color(255, 255, 255); // #FFFFFF
    private static final Color COLOR_BG_LIGHT = new Color(250, 246, 249); // #FAF6F9
    private static final Color COLOR_BG_DARK = new Color(91, 29, 61); // #5B1D3D
    
    private int userId;
    private String username;
    private boolean isLoggedIn;
    private DefaultTableModel modeloTabla;
    private double subtotal = 0.0;
    private double total = 0.0;
    
    // Componentes del header
    private JPanel headerTopPanel;
    private JPanel headerMainPanel;
    
    // Complementos de la aplicación
    private String nombreApp = "FASHION";
    private String correoApp = "";
    private String rutaLogo = "";
    
    /**
     * Constructor
     * @param userId ID del usuario (debe estar logueado)
     */
    public CarritoCompras(int userId) {
        if (userId <= 0) {
            JOptionPane.showMessageDialog(null,
                "Debe iniciar sesión para ver el carrito",
                "Sesión requerida",
                JOptionPane.WARNING_MESSAGE);
            dispose();
            return;
        }
        
        this.userId = userId;
        this.isLoggedIn = (userId > 0);
        
        // Obtener información de sesión
        if (isLoggedIn) {
            SessionManager session = SessionManager.getInstance();
            this.username = session.getUsername();
        }
        
        ModernStyles.applyModernLookAndFeel();
        
        // Cargar complementos
        cargarComplementos();
        
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        // Crear headers modernos
        crearHeaderTop();
        crearHeaderMain();
        agregarHeadersAlLayout();
        
        configurarTabla();
        configurarEventos();
        cargarCarrito();
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
            new EmptyBorder(0, 50, 6, 50)
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
                CarritoCompras.this.dispose();
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
        JLabel navTienda = crearNavLink("Tienda", e -> {
            this.dispose();
            new Categorias(userId, "").setVisible(true);
        });
        JLabel navMujer = crearNavLink("Mujer", e -> {
            this.dispose();
            new Categorias(userId, "Mujer").setVisible(true);
        });
        JLabel navAccesorios = crearNavLink("Accesorios", e -> {
            this.dispose();
            new Categorias(userId, "Accesorios").setVisible(true);
        });
        
        navPanel.add(navInicio);
        navPanel.add(navTienda);
        navPanel.add(navMujer);
        navPanel.add(navAccesorios);
        
        // Panel derecho con iconos
        JPanel iconsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        iconsPanel.setOpaque(false);
        iconsPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        userPanel.setOpaque(false);
        userPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        
        if (isLoggedIn && username != null) {
            JLabel userNameLabel = new JLabel(username);
            userNameLabel.setFont(new Font("Poppins", Font.PLAIN, 11));
            userNameLabel.setForeground(COLOR_TEXT_PRIMARY);
            userNameLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
            userNameLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            userPanel.add(userNameLabel);
            
            JPanel userIcon = crearHeaderIcon("👤", "Mi Perfil", e -> {
                JOptionPane.showMessageDialog(this, "Funcionalidad de perfil en desarrollo",
                    "Perfil", JOptionPane.INFORMATION_MESSAGE);
            });
            userPanel.add(userIcon);
        } else {
            JPanel userIcon = crearHeaderIcon("👤", "Iniciar Sesión", e -> {
                Inicio ventanaLogin = new Inicio(this);
                ventanaLogin.setVisible(true);
            });
            userPanel.add(userIcon);
        }
        
        JPanel cartIcon = crearHeaderIcon("🛒", "Carrito", e -> {
            // Ya estamos en el carrito
        });
        
        iconsPanel.add(userPanel);
        iconsPanel.add(cartIcon);
        
        // Crear panel superior
        JPanel topRow = new JPanel(new BorderLayout(15, 0));
        topRow.setOpaque(false);
        topRow.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        
        JPanel leftPanel = new JPanel(new BorderLayout(20, 0));
        leftPanel.setOpaque(false);
        leftPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        leftPanel.add(logoPanel, BorderLayout.WEST);
        leftPanel.add(navPanel, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new BorderLayout(10, 0));
        rightPanel.setOpaque(false);
        rightPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        rightPanel.add(iconsPanel, BorderLayout.EAST);
        
        topRow.add(leftPanel, BorderLayout.WEST);
        topRow.add(rightPanel, BorderLayout.EAST);
        
        headerMainPanel.add(topRow, BorderLayout.SOUTH);
    }
    
    /**
     * Agrega los headers al layout
     */
    private void agregarHeadersAlLayout() {
        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
        headerContainer.setOpaque(false);
        headerContainer.add(headerTopPanel);
        headerContainer.add(headerMainPanel);
        jPanelMain.add(headerContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1363, 95));
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
     * Configura la tabla
     */
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Solo la cantidad es editable
            }
        };
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Producto");
        modeloTabla.addColumn("Talla");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Precio Unitario");
        modeloTabla.addColumn("Subtotal");
        
        jTableCarrito.setModel(modeloTabla);
        jTableCarrito.getColumnModel().getColumn(0).setMinWidth(0);
        jTableCarrito.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableCarrito.getColumnModel().getColumn(0).setWidth(0);
    }
    
    /**
     * Configura los eventos
     */
    private void configurarEventos() {
        jButtonEliminar.addActionListener(e -> eliminarItem());
        jButtonActualizar.addActionListener(e -> actualizarCantidad());
        jButtonVaciar.addActionListener(e -> vaciarCarrito());
        jButtonComprar.addActionListener(e -> realizarCompra());
        // Eliminado jButtonVolver - ya no se necesita con el header moderno
        
        // Listener para cambios en la tabla
        jTableCarrito.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 3) { // Columna de cantidad
                actualizarTotal();
            }
        });
    }
    
    /**
     * Carga el carrito del usuario
     */
    private void cargarCarrito() {
        modeloTabla.setRowCount(0);
        subtotal = 0.0;
        
        try (Connection conn = Conexion.getConnection()) {
            // Obtener carrito activo
            String queryCarrito = "SELECT id FROM tb_carro WHERE id_usuario = ? ORDER BY fecha_creacion DESC LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(queryCarrito);
            pstmt.setInt(1, userId);
            ResultSet rsCarrito = pstmt.executeQuery();
            
            if (!rsCarrito.next()) {
                jLabelMensaje.setText("El carrito está vacío");
                actualizarTotal();
                return;
            }
            
            int idCarrito = rsCarrito.getInt("id");
            
            // Obtener items del carrito
            String query = """
                SELECT ci.id, p.Nombre, t.nombre as talla, ci.cantidad, ci.precio_unitario,
                       (ci.cantidad * ci.precio_unitario) as subtotal
                FROM tb_carro_items ci
                JOIN tb_productos p ON ci.id_producto = p.id
                JOIN tb_tallas t ON ci.id_talla = t.id
                WHERE ci.id_carro = ?
                ORDER BY ci.id
                """;
            
            PreparedStatement pstmtItems = conn.prepareStatement(query);
            pstmtItems.setInt(1, idCarrito);
            ResultSet rs = pstmtItems.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("Nombre"),
                    rs.getString("talla"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio_unitario"),
                    rs.getDouble("subtotal")
                };
                modeloTabla.addRow(row);
                subtotal += rs.getDouble("subtotal");
            }
            
            actualizarTotal();
            
        } catch (SQLException e) {
            ErrorHandler.handleSQLException(this, e, "cargar carrito");
        }
    }
    
    /**
     * Actualiza el total
     */
    private void actualizarTotal() {
        subtotal = 0.0;
        
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            int cantidad = (Integer) modeloTabla.getValueAt(i, 3);
            double precio = (Double) modeloTabla.getValueAt(i, 4);
            double subtotalItem = cantidad * precio;
            modeloTabla.setValueAt(subtotalItem, i, 5);
            subtotal += subtotalItem;
        }
        
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "CO"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
        jLabelSubtotal.setText("Subtotal: $" + df.format(subtotal));
        jLabelTotal.setText("Total: $" + df.format(subtotal));
        total = subtotal;
    }
    
    /**
     * Elimina un item del carrito
     */
    private void eliminarItem() {
        int fila = jTableCarrito.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un item para eliminar",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idItem = (Integer) modeloTabla.getValueAt(fila, 0);
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este item del carrito?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection conn = Conexion.getConnection()) {
                String delete = "DELETE FROM tb_carro_items WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(delete);
                pstmt.setInt(1, idItem);
                pstmt.executeUpdate();
                
                cargarCarrito();
                
            } catch (SQLException e) {
                ErrorHandler.handleSQLException(this, e, "eliminar item del carrito");
            }
        }
    }
    
    /**
     * Actualiza la cantidad de un item
     */
    private void actualizarCantidad() {
        int fila = jTableCarrito.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un item para actualizar",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int idItem = (Integer) modeloTabla.getValueAt(fila, 0);
            int nuevaCantidad = (Integer) modeloTabla.getValueAt(fila, 3);
            
            if (nuevaCantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                    "La cantidad debe ser mayor a 0",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                cargarCarrito();
                return;
            }
            
            // Verificar stock disponible
            try (Connection conn = Conexion.getConnection()) {
                String queryStock = """
                    SELECT i.cantidad as stock_disponible
                    FROM tb_carro_items ci
                    JOIN tb_inventario i ON ci.id_producto = i.id_producto AND ci.id_talla = i.id_talla
                    WHERE ci.id = ?
                    """;
                PreparedStatement pstmt = conn.prepareStatement(queryStock);
                pstmt.setInt(1, idItem);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    int stockDisponible = rs.getInt("stock_disponible");
                    if (nuevaCantidad > stockDisponible) {
                        JOptionPane.showMessageDialog(this,
                            "La cantidad solicitada excede el stock disponible (" + stockDisponible + " unidades)",
                            "Stock insuficiente",
                            JOptionPane.WARNING_MESSAGE);
                        cargarCarrito();
                        return;
                    }
                }
                
                // Actualizar cantidad
                String update = "UPDATE tb_carro_items SET cantidad = ? WHERE id = ?";
                PreparedStatement pstmtUpdate = conn.prepareStatement(update);
                pstmtUpdate.setInt(1, nuevaCantidad);
                pstmtUpdate.setInt(2, idItem);
                pstmtUpdate.executeUpdate();
                
                cargarCarrito();
                
            } catch (SQLException e) {
                ErrorHandler.handleSQLException(this, e, "actualizar cantidad");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar cantidad: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            cargarCarrito();
        }
    }
    
    /**
     * Vacía el carrito
     */
    private void vaciarCarrito() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "El carrito ya está vacío",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de vaciar todo el carrito?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection conn = Conexion.getConnection()) {
                // Obtener id del carrito
                String queryCarrito = "SELECT id FROM tb_carro WHERE id_usuario = ? ORDER BY fecha_creacion DESC LIMIT 1";
                PreparedStatement pstmt = conn.prepareStatement(queryCarrito);
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    int idCarrito = rs.getInt("id");
                    String delete = "DELETE FROM tb_carro_items WHERE id_carro = ?";
                    PreparedStatement pstmtDelete = conn.prepareStatement(delete);
                    pstmtDelete.setInt(1, idCarrito);
                    pstmtDelete.executeUpdate();
                }
                
                cargarCarrito();
                
            } catch (SQLException e) {
                ErrorHandler.handleSQLException(this, e, "vaciar carrito");
            }
        }
    }
    
    /**
     * Realiza la compra
     */
    private void realizarCompra() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "El carrito está vacío",
                "Carrito vacío",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Mostrar opciones de pago
        String[] opcionesPago = {"Tarjeta de Crédito", "Tarjeta de Débito", "Transferencia", "Pago Contra Entrega"};
        String metodoPago = (String) JOptionPane.showInputDialog(this,
            "Seleccione el método de pago:",
            "Método de Pago",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesPago,
            opcionesPago[0]);
        
        if (metodoPago == null) {
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Confirmar compra por $" + String.format("%.2f", total) + "?",
            "Confirmar Compra",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection conn = Conexion.getConnection()) {
                Conexion.beginTransaction(conn);
                
                // Obtener id del carrito
                String queryCarrito = "SELECT id FROM tb_carro WHERE id_usuario = ? ORDER BY fecha_creacion DESC LIMIT 1";
                PreparedStatement pstmt = conn.prepareStatement(queryCarrito);
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                
                if (!rs.next()) {
                    throw new SQLException("No se encontró el carrito");
                }
                
                int idCarrito = rs.getInt("id");
                
                // Crear registro de pago
                String insertPago = """
                    INSERT INTO tb_pago (id_usuario, metodo_pago, subtotal, impuestos, descuentos, total, estado)
                    VALUES (?, ?, ?, 0.00, 0.00, ?, 'Pendiente')
                    """;
                PreparedStatement pstmtPago = conn.prepareStatement(insertPago, Statement.RETURN_GENERATED_KEYS);
                pstmtPago.setInt(1, userId);
                pstmtPago.setString(2, metodoPago);
                pstmtPago.setDouble(3, subtotal);
                pstmtPago.setDouble(4, total);
                pstmtPago.executeUpdate();
                
                ResultSet rsPago = pstmtPago.getGeneratedKeys();
                if (!rsPago.next()) {
                    throw new SQLException("No se pudo crear el registro de pago");
                }
                
                int idPago = rsPago.getInt(1);
                
                // Crear detalles de pago y actualizar inventario
                String queryItems = """
                    SELECT ci.id_producto, ci.id_talla, ci.cantidad, ci.precio_unitario
                    FROM tb_carro_items ci
                    WHERE ci.id_carro = ?
                    """;
                PreparedStatement pstmtItems = conn.prepareStatement(queryItems);
                pstmtItems.setInt(1, idCarrito);
                ResultSet rsItems = pstmtItems.executeQuery();
                
                String insertDetalle = """
                    INSERT INTO tb_pago_detalle (id_pago, id_producto, id_talla, cantidad, precio_unitario, subtotal)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;
                PreparedStatement pstmtDetalle = conn.prepareStatement(insertDetalle);
                
                String updateInventario = """
                    UPDATE tb_inventario 
                    SET cantidad = cantidad - ?
                    WHERE id_producto = ? AND id_talla = ?
                    """;
                PreparedStatement pstmtInventario = conn.prepareStatement(updateInventario);
                
                while (rsItems.next()) {
                    int idProducto = rsItems.getInt("id_producto");
                    int idTalla = rsItems.getInt("id_talla");
                    int cantidad = rsItems.getInt("cantidad");
                    double precioUnitario = rsItems.getDouble("precio_unitario");
                    double subtotalItem = cantidad * precioUnitario;
                    
                    // Insertar detalle
                    pstmtDetalle.setInt(1, idPago);
                    pstmtDetalle.setInt(2, idProducto);
                    pstmtDetalle.setInt(3, idTalla);
                    pstmtDetalle.setInt(4, cantidad);
                    pstmtDetalle.setDouble(5, precioUnitario);
                    pstmtDetalle.setDouble(6, subtotalItem);
                    pstmtDetalle.executeUpdate();
                    
                    // Actualizar inventario
                    pstmtInventario.setInt(1, cantidad);
                    pstmtInventario.setInt(2, idProducto);
                    pstmtInventario.setInt(3, idTalla);
                    pstmtInventario.executeUpdate();
                }
                
                // Eliminar items del carrito
                String deleteItems = "DELETE FROM tb_carro_items WHERE id_carro = ?";
                PreparedStatement pstmtDelete = conn.prepareStatement(deleteItems);
                pstmtDelete.setInt(1, idCarrito);
                pstmtDelete.executeUpdate();
                
                Conexion.commit(conn);
                
                JOptionPane.showMessageDialog(this,
                    "¡Compra realizada exitosamente!\n" +
                    "ID de pago: " + idPago + "\n" +
                    "Total: $" + String.format("%.2f", total),
                    "Compra Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
                
                cargarCarrito();
                
            } catch (SQLException e) {
                ErrorHandler.handleSQLException(this, e, "realizar compra");
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanelMain = new javax.swing.JPanel();
        jPanelHeader = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jButtonVolver = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCarrito = new javax.swing.JTable();
        jPanelAcciones = new javax.swing.JPanel();
        jButtonEliminar = new javax.swing.JButton();
        jButtonActualizar = new javax.swing.JButton();
        jButtonVaciar = new javax.swing.JButton();
        jPanelTotal = new javax.swing.JPanel();
        jLabelSubtotal = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jButtonComprar = new javax.swing.JButton();
        jLabelMensaje = new javax.swing.JLabel();
        
        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelMain.setBackground(COLOR_BG_LIGHT);
        
        // Título de la página
        jLabelTitulo.setFont(new Font("Playfair Display", Font.BOLD, 32));
        jLabelTitulo.setText("Carrito de Compras");
        jLabelTitulo.setForeground(COLOR_PRIMARY);
        jPanelMain.add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 115, 500, 40));
        
        // Tabla con diseño moderno
        jTableCarrito.setFont(new Font("Poppins", Font.PLAIN, 13));
        jTableCarrito.setRowHeight(50);
        jTableCarrito.setShowGrid(false);
        jTableCarrito.setIntercellSpacing(new Dimension(0, 0));
        jTableCarrito.setSelectionBackground(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 50));
        jTableCarrito.setSelectionForeground(COLOR_PRIMARY);
        jScrollPane1.setViewportView(jTableCarrito);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
        jScrollPane1.setBackground(Color.WHITE);
        
        jPanelMain.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 900, 450));
        
        jPanelAcciones.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jPanelAcciones.setOpaque(false);
        jPanelAcciones.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Botón Eliminar Item
        jButtonEliminar.setText("🗑️ Eliminar Item");
        jButtonEliminar.setFont(new Font("Poppins", Font.BOLD, 13));
        jButtonEliminar.setForeground(COLOR_TEXT_LIGHT);
        jButtonEliminar.setBackground(new Color(220, 53, 69)); // Rojo para eliminar
        jButtonEliminar.setBorderPainted(false);
        jButtonEliminar.setFocusPainted(false);
        jButtonEliminar.setPreferredSize(new Dimension(160, 40));
        jButtonEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jPanelAcciones.add(jButtonEliminar);
        
        // Botón Actualizar Cantidad
        jButtonActualizar.setText("🔄 Actualizar");
        jButtonActualizar.setFont(new Font("Poppins", Font.BOLD, 13));
        jButtonActualizar.setForeground(COLOR_TEXT_LIGHT);
        jButtonActualizar.setBackground(COLOR_PRIMARY);
        jButtonActualizar.setBorderPainted(false);
        jButtonActualizar.setFocusPainted(false);
        jButtonActualizar.setPreferredSize(new Dimension(160, 40));
        jButtonActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jPanelAcciones.add(jButtonActualizar);
        
        // Botón Vaciar Carrito
        jButtonVaciar.setText("🗑️ Vaciar Todo");
        jButtonVaciar.setFont(new Font("Poppins", Font.BOLD, 13));
        jButtonVaciar.setForeground(COLOR_TEXT_LIGHT);
        jButtonVaciar.setBackground(new Color(220, 53, 69));
        jButtonVaciar.setBorderPainted(false);
        jButtonVaciar.setFocusPainted(false);
        jButtonVaciar.setPreferredSize(new Dimension(160, 40));
        jButtonVaciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jPanelAcciones.add(jButtonVaciar);
        
        jPanelMain.add(jPanelAcciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 640, 520, 60));
        
        // Panel de resumen moderno
        jPanelTotal.setBackground(Color.WHITE);
        jPanelTotal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 30), 2),
            new EmptyBorder(25, 25, 25, 25)
        ));
        jPanelTotal.setLayout(new BoxLayout(jPanelTotal, BoxLayout.Y_AXIS));
        
        JLabel resumenTitle = new JLabel("Resumen de Compra");
        resumenTitle.setFont(new Font("Poppins", Font.BOLD, 18));
        resumenTitle.setForeground(COLOR_PRIMARY);
        resumenTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        jPanelTotal.add(resumenTitle);
        
        jLabelSubtotal.setFont(new Font("Poppins", Font.PLAIN, 14));
        jLabelSubtotal.setText("Subtotal: $0.00");
        jLabelSubtotal.setForeground(COLOR_TEXT_SECONDARY);
        jLabelSubtotal.setBorder(new EmptyBorder(0, 0, 10, 0));
        jPanelTotal.add(jLabelSubtotal);
        
        jLabelTotal.setFont(new Font("Poppins", Font.BOLD, 22));
        jLabelTotal.setText("Total: $0.00");
        jLabelTotal.setForeground(COLOR_PRIMARY);
        jLabelTotal.setBorder(new EmptyBorder(0, 0, 20, 0));
        jPanelTotal.add(jLabelTotal);
        
        jButtonComprar.setFont(new Font("Poppins", Font.BOLD, 16));
        jButtonComprar.setText("💳 Realizar Compra");
        jButtonComprar.setForeground(COLOR_TEXT_LIGHT);
        jButtonComprar.setBackground(new Color(40, 167, 69)); // Verde para comprar
        jButtonComprar.setBorderPainted(false);
        jButtonComprar.setFocusPainted(false);
        jButtonComprar.setPreferredSize(new Dimension(300, 50));
        jButtonComprar.setMaximumSize(new Dimension(300, 50));
        jButtonComprar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButtonComprar.setAlignmentX(Component.CENTER_ALIGNMENT);
        jButtonComprar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                jButtonComprar.setBackground(new Color(33, 136, 56));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                jButtonComprar.setBackground(new Color(40, 167, 69));
            }
        });
        jPanelTotal.add(jButtonComprar);
        
        jPanelMain.add(jPanelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 170, 330, 250));
        
        jLabelMensaje.setFont(new Font("Poppins", Font.ITALIC, 13));
        jLabelMensaje.setForeground(COLOR_TEXT_SECONDARY);
        jLabelMensaje.setText("");
        jPanelMain.add(jLabelMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 710, 800, 30));
        
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
    }
    
    // Variables declaration
    private javax.swing.JButton jButtonActualizar;
    private javax.swing.JButton jButtonComprar;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonVaciar;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JLabel jLabelMensaje;
    private javax.swing.JLabel jLabelSubtotal;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JPanel jPanelAcciones;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelTotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCarrito;
}

