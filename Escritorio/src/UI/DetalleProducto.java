package UI;

import javax.swing.*;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import UI.ConfigImagenes;
import UI.util.ErrorHandler;
import UI.CarritoCompras;

/**
 * Ventana para ver detalles de un producto y agregarlo al carrito
 */
public class DetalleProducto extends javax.swing.JFrame {
    
    // Colores accesibles WCAG AAA - Paleta femenina y seria (mismos que principal)
    private static final Color COLOR_PRIMARY = new Color(107, 45, 77); // #6B2D4D
    private static final Color COLOR_SECONDARY = new Color(139, 74, 107); // #8B4A6B
    private static final Color COLOR_ACCENT = new Color(168, 85, 122); // #A8557A
    private static final Color COLOR_TEXT_PRIMARY = new Color(26, 26, 26); // #1a1a1a
    private static final Color COLOR_TEXT_SECONDARY = new Color(51, 51, 51); // #333333
    private static final Color COLOR_TEXT_LIGHT = new Color(255, 255, 255); // #FFFFFF
    private static final Color COLOR_BG_LIGHT = new Color(250, 246, 249); // #FAF6F9
    
    private int userId;
    private UI.Producto producto;
    private List<TallaDisponible> tallasDisponibles;
    
    // Clase interna para manejar tallas disponibles
    private static class TallaDisponible {
        int idTalla;
        String nombreTalla;
        int cantidad;
        double precio;
        
        TallaDisponible(int idTalla, String nombreTalla, int cantidad, double precio) {
            this.idTalla = idTalla;
            this.nombreTalla = nombreTalla;
            this.cantidad = cantidad;
            this.precio = precio;
        }
        
        @Override
        public String toString() {
            return nombreTalla + " - $" + String.format("%.2f", precio) + " (" + cantidad + " disponibles)";
        }
    }
    
    /**
     * Constructor
     * @param userId ID del usuario (0 si no está logueado)
     * @param producto Producto a mostrar
     */
    public DetalleProducto(int userId, UI.Producto producto) {
        this.userId = userId;
        this.producto = producto;
        this.tallasDisponibles = new ArrayList<>();
        
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        cargarDetallesProducto();
        configurarEventos();
    }
    
    /**
     * Carga los detalles completos del producto
     */
    private void cargarDetallesProducto() {
        try (Connection conn = Conexion.getConnection()) {
            // Obtener información completa del producto
            String queryProducto = "SELECT * FROM tb_productos WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(queryProducto);
            pstmt.setInt(1, producto.getId());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Cargar imagen
                String imgUrl = rs.getString("ImgUrl");
                if (imgUrl != null && !imgUrl.isEmpty()) {
                    ImageIcon imagen = ConfigImagenes.cargarImagenEscalada(imgUrl, 400, 500);
                    if (imagen != null) {
                        jLabelImagen.setIcon(imagen);
                    }
                }
                
                // Información del producto
                jLabelNombre.setText(rs.getString("Nombre"));
                jTextAreaDescripcion.setText(rs.getString("Descripcion") != null ? rs.getString("Descripcion") : "Sin descripción");
                jLabelCategoria.setText("Categoría: " + (rs.getString("Categorias") != null ? rs.getString("Categorias") : "N/A"));
                jLabelColor.setText("Color: " + (rs.getString("Color") != null ? rs.getString("Color") : "N/A"));
                jLabelMaterial.setText("Material: " + (rs.getString("MaterialBase") != null ? rs.getString("MaterialBase") : "N/A"));
                
                // Cargar tallas disponibles
                cargarTallasDisponibles(conn);
            }
        } catch (SQLException e) {
            ErrorHandler.handleSQLException(this, e, "cargar detalles del producto");
        }
    }
    
    /**
     * Carga las tallas disponibles para el producto
     */
    private void cargarTallasDisponibles(Connection conn) throws SQLException {
        tallasDisponibles.clear();
        jComboBoxTalla.removeAllItems();
        
        String query = """
            SELECT t.id, t.nombre, i.cantidad, i.precio
            FROM tb_inventario i
            JOIN tb_tallas t ON i.id_talla = t.id
            WHERE i.id_producto = ? AND i.cantidad > 0
            ORDER BY t.nombre
            """;
        
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, producto.getId());
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            TallaDisponible talla = new TallaDisponible(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getInt("cantidad"),
                rs.getDouble("precio")
            );
            tallasDisponibles.add(talla);
            jComboBoxTalla.addItem(talla.toString());
        }
        
        if (tallasDisponibles.isEmpty()) {
            jComboBoxTalla.addItem("No hay tallas disponibles");
            jButtonAgregarCarrito.setEnabled(false);
        } else {
            actualizarPrecio();
        }
    }
    
    /**
     * Actualiza el precio según la talla seleccionada
     */
    private void actualizarPrecio() {
        int index = jComboBoxTalla.getSelectedIndex();
        if (index >= 0 && index < tallasDisponibles.size()) {
            TallaDisponible talla = tallasDisponibles.get(index);
            jLabelPrecio.setText("$" + String.format("%.2f", talla.precio));
            
            // Actualizar el modelo del spinner con el nuevo máximo
            int maxCantidad = Math.max(1, talla.cantidad);
            SpinnerNumberModel model = new SpinnerNumberModel(1, 1, maxCantidad, 1);
            jSpinnerCantidad.setModel(model);
        }
    }
    
    /**
     * Configura los eventos
     */
    private void configurarEventos() {
        jComboBoxTalla.addActionListener(e -> actualizarPrecio());
        
        jButtonAgregarCarrito.addActionListener(e -> agregarAlCarrito());
        
        jButtonVolver.addActionListener(e -> {
            this.dispose();
            // Volver a la página principal - usar SwingUtilities para asegurar que se ejecute en el EDT
            SwingUtilities.invokeLater(() -> {
                try {
                    principal ventanaPrincipal = new principal(userId);
                    ventanaPrincipal.setVisible(true);
                } catch (Exception ex) {
                    ErrorHandler.logError(ex, "Error al volver a la página principal");
                    JOptionPane.showMessageDialog(this,
                        "Error al volver a la página principal",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
        });
    }
    
    /**
     * Agrega el producto al carrito
     */
    private void agregarAlCarrito() {
        if (userId <= 0) {
            JOptionPane.showMessageDialog(this,
                "Debe iniciar sesión para agregar productos al carrito",
                "Sesión requerida",
                JOptionPane.WARNING_MESSAGE);
            this.dispose();
            new Inicio(null).setVisible(true);
            return;
        }
        
        int index = jComboBoxTalla.getSelectedIndex();
        if (index < 0 || index >= tallasDisponibles.size()) {
            JOptionPane.showMessageDialog(this,
                "Seleccione una talla válida",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        TallaDisponible talla = tallasDisponibles.get(index);
        int cantidad = (Integer) jSpinnerCantidad.getValue();
        
        if (cantidad > talla.cantidad) {
            JOptionPane.showMessageDialog(this,
                "La cantidad solicitada excede el stock disponible",
                "Stock insuficiente",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (Connection conn = Conexion.getConnection()) {
            Conexion.beginTransaction(conn);
            
            // Obtener o crear carrito del usuario
            int idCarrito = obtenerOCrearCarrito(conn, userId);
            
            // Verificar si el producto ya está en el carrito con la misma talla
            String queryExiste = """
                SELECT id, cantidad FROM tb_carro_items 
                WHERE id_carro = ? AND id_producto = ? AND id_talla = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(queryExiste);
            pstmt.setInt(1, idCarrito);
            pstmt.setInt(2, producto.getId());
            pstmt.setInt(3, talla.idTalla);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Actualizar cantidad
                int cantidadActual = rs.getInt("cantidad");
                int nuevaCantidad = cantidadActual + cantidad;
                
                if (nuevaCantidad > talla.cantidad) {
                    JOptionPane.showMessageDialog(this,
                        "La cantidad total excede el stock disponible",
                        "Stock insuficiente",
                        JOptionPane.WARNING_MESSAGE);
                    Conexion.rollback(conn);
                    return;
                }
                
                String update = "UPDATE tb_carro_items SET cantidad = ?, precio_unitario = ? WHERE id = ?";
                PreparedStatement pstmtUpdate = conn.prepareStatement(update);
                pstmtUpdate.setInt(1, nuevaCantidad);
                pstmtUpdate.setDouble(2, talla.precio);
                pstmtUpdate.setInt(3, rs.getInt("id"));
                pstmtUpdate.executeUpdate();
            } else {
                // Insertar nuevo item
                String insert = """
                    INSERT INTO tb_carro_items (id_carro, id_producto, id_talla, cantidad, precio_unitario)
                    VALUES (?, ?, ?, ?, ?)
                    """;
                PreparedStatement pstmtInsert = conn.prepareStatement(insert);
                pstmtInsert.setInt(1, idCarrito);
                pstmtInsert.setInt(2, producto.getId());
                pstmtInsert.setInt(3, talla.idTalla);
                pstmtInsert.setInt(4, cantidad);
                pstmtInsert.setDouble(5, talla.precio);
                pstmtInsert.executeUpdate();
            }
            
            Conexion.commit(conn);
            
            int opcion = JOptionPane.showConfirmDialog(this,
                "Producto agregado al carrito exitosamente.\n¿Desea ver el carrito?",
                "Éxito",
                JOptionPane.YES_NO_OPTION);
            
            if (opcion == JOptionPane.YES_OPTION) {
                this.dispose();
                new CarritoCompras(userId).setVisible(true);
            }
            
        } catch (SQLException e) {
            ErrorHandler.handleSQLException(this, e, "agregar producto al carrito");
        }
    }
    
    /**
     * Obtiene o crea un carrito para el usuario
     */
    private int obtenerOCrearCarrito(Connection conn, int userId) throws SQLException {
        // Buscar carrito activo
        String query = "SELECT id FROM tb_carro WHERE id_usuario = ? ORDER BY fecha_creacion DESC LIMIT 1";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt("id");
        }
        
        // Crear nuevo carrito
        String insert = "INSERT INTO tb_carro (id_usuario) VALUES (?)";
        PreparedStatement pstmtInsert = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        pstmtInsert.setInt(1, userId);
        pstmtInsert.executeUpdate();
        
        ResultSet rsInsert = pstmtInsert.getGeneratedKeys();
        if (rsInsert.next()) {
            return rsInsert.getInt(1);
        }
        
        throw new SQLException("No se pudo crear el carrito");
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
        jPanelContenido = new javax.swing.JPanel();
        jLabelImagen = new javax.swing.JLabel();
        jPanelInfo = new javax.swing.JPanel();
        jLabelNombre = new javax.swing.JLabel();
        jLabelPrecio = new javax.swing.JLabel();
        jLabelCategoria = new javax.swing.JLabel();
        jLabelColor = new javax.swing.JLabel();
        jLabelMaterial = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescripcion = new javax.swing.JTextArea();
        jPanelCompra = new javax.swing.JPanel();
        jLabelTalla = new javax.swing.JLabel();
        jComboBoxTalla = new javax.swing.JComboBox<>();
        jLabelCantidad = new javax.swing.JLabel();
        jSpinnerCantidad = new javax.swing.JSpinner();
        jButtonAgregarCarrito = new javax.swing.JButton();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jPanelHeader.setBackground(new java.awt.Color(250, 246, 249)); // #FAF6F9
        jPanelHeader.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 50)),
            javax.swing.BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        jPanelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jLabelTitulo.setFont(new java.awt.Font("Playfair Display", Font.BOLD, 28));
        jLabelTitulo.setText("Detalle del Producto");
        jLabelTitulo.setForeground(COLOR_PRIMARY);
        jPanelHeader.add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 400, 40));
        
        jButtonVolver.setText("← Volver");
        jButtonVolver.setFont(new java.awt.Font("Poppins", Font.BOLD, 13));
        jButtonVolver.setForeground(COLOR_PRIMARY);
        jButtonVolver.setBackground(COLOR_BG_LIGHT);
        jButtonVolver.setBorderPainted(false);
        jButtonVolver.setFocusPainted(false);
        jButtonVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jPanelHeader.add(jButtonVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 25, 120, 35));
        
        jPanelMain.add(jPanelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1363, 80));
        
        jPanelContenido.setBackground(new java.awt.Color(250, 246, 249)); // #FAF6F9
        jPanelContenido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jLabelImagen.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelImagen.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 30), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        jLabelImagen.setBackground(Color.WHITE);
        jLabelImagen.setOpaque(true);
        jPanelContenido.add(jLabelImagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 400, 500));
        
        jPanelInfo.setBackground(new java.awt.Color(250, 246, 249)); // #FAF6F9
        jPanelInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jLabelNombre.setFont(new java.awt.Font("Playfair Display", Font.BOLD, 32));
        jLabelNombre.setText("Nombre del Producto");
        jLabelNombre.setForeground(COLOR_TEXT_PRIMARY);
        jPanelInfo.add(jLabelNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 800, 50));
        
        jLabelPrecio.setFont(new java.awt.Font("Poppins", Font.BOLD, 36));
        jLabelPrecio.setForeground(COLOR_PRIMARY);
        jLabelPrecio.setText("$0.00");
        jPanelInfo.add(jLabelPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 300, 45));
        
        jLabelCategoria.setFont(new java.awt.Font("Poppins", Font.PLAIN, 15));
        jLabelCategoria.setText("Categoría: ");
        jLabelCategoria.setForeground(COLOR_TEXT_SECONDARY);
        jPanelInfo.add(jLabelCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 600, 30));
        
        jLabelColor.setFont(new java.awt.Font("Poppins", Font.PLAIN, 15));
        jLabelColor.setText("Color: ");
        jLabelColor.setForeground(COLOR_TEXT_SECONDARY);
        jPanelInfo.add(jLabelColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 175, 600, 30));
        
        jLabelMaterial.setFont(new java.awt.Font("Poppins", Font.PLAIN, 15));
        jLabelMaterial.setText("Material: ");
        jLabelMaterial.setForeground(COLOR_TEXT_SECONDARY);
        jPanelInfo.add(jLabelMaterial, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 600, 30));
        
        jTextAreaDescripcion.setEditable(false);
        jTextAreaDescripcion.setFont(new java.awt.Font("Poppins", Font.PLAIN, 14));
        jTextAreaDescripcion.setLineWrap(true);
        jTextAreaDescripcion.setWrapStyleWord(true);
        jTextAreaDescripcion.setBackground(new java.awt.Color(255, 255, 255));
        jTextAreaDescripcion.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 30), 1),
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        jScrollPane1.setViewportView(jTextAreaDescripcion);
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jPanelInfo.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 800, 140));
        
        jPanelCompra.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCompra.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 50), 2),
            javax.swing.BorderFactory.createTitledBorder(
                javax.swing.BorderFactory.createEmptyBorder(),
                "Opciones de Compra",
                0, 0,
                new java.awt.Font("Poppins", Font.BOLD, 16),
                COLOR_PRIMARY
            )
        ));
        jPanelCompra.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jLabelTalla.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelTalla.setText("Talla:");
        jPanelCompra.add(jLabelTalla, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 100, 30));
        
        jComboBoxTalla.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jComboBoxTalla.setPreferredSize(new java.awt.Dimension(300, 30));
        jPanelCompra.add(jComboBoxTalla, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 300, 30));
        
        jLabelCantidad.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelCantidad.setText("Cantidad:");
        jPanelCompra.add(jLabelCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 100, 30));
        
        jSpinnerCantidad.setModel(new SpinnerNumberModel(1, 1, 10, 1));
        jSpinnerCantidad.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelCompra.add(jSpinnerCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 100, 30));
        
        jButtonAgregarCarrito.setFont(new java.awt.Font("Arial", Font.BOLD, 16));
        jButtonAgregarCarrito.setText("Agregar al Carrito");
        jButtonAgregarCarrito.setBackground(new java.awt.Color(0, 150, 0));
        jButtonAgregarCarrito.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAgregarCarrito.setPreferredSize(new java.awt.Dimension(250, 50));
        jPanelCompra.add(jButtonAgregarCarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 400, 50));
        
        jPanelInfo.add(jPanelCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 450, 200));
        
        jPanelContenido.add(jPanelInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 30, 800, 630));
        
        jPanelMain.add(jPanelContenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 1363, 680));
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1363, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
        );
        
        pack();
    }
    
    // Variables declaration
    private javax.swing.JButton jButtonAgregarCarrito;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JComboBox<String> jComboBoxTalla;
    private javax.swing.JLabel jLabelCantidad;
    private javax.swing.JLabel jLabelCategoria;
    private javax.swing.JLabel jLabelColor;
    private javax.swing.JLabel jLabelImagen;
    private javax.swing.JLabel jLabelMaterial;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelPrecio;
    private javax.swing.JLabel jLabelTalla;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanelCompra;
    private javax.swing.JPanel jPanelContenido;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelInfo;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerCantidad;
    private javax.swing.JTextArea jTextAreaDescripcion;
}

