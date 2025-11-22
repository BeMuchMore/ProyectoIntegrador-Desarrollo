package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import UI.util.ErrorHandler;

/**
 * Ventana para ver el carrito de compras y realizar la compra
 */
public class CarritoCompras extends javax.swing.JFrame {
    
    private int userId;
    private DefaultTableModel modeloTabla;
    private double subtotal = 0.0;
    private double total = 0.0;
    
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
        
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        configurarTabla();
        configurarEventos();
        cargarCarrito();
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
        jButtonVolver.addActionListener(e -> {
            this.dispose();
        });
        
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
        
        DecimalFormat df = new DecimalFormat("#,##0.00");
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
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jPanelHeader.setBackground(new java.awt.Color(255, 255, 255));
        jPanelHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jLabelTitulo.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        jLabelTitulo.setText("Carrito de Compras");
        jPanelHeader.add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 400, 40));
        
        jButtonVolver.setText("← Volver");
        jButtonVolver.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelHeader.add(jButtonVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 25, 120, 35));
        
        jPanelMain.add(jPanelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1363, 80));
        
        jTableCarrito.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jTableCarrito.setRowHeight(30);
        jScrollPane1.setViewportView(jTableCarrito);
        
        jPanelMain.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 1000, 400));
        
        jPanelAcciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jButtonEliminar.setText("Eliminar Item");
        jButtonEliminar.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelAcciones.add(jButtonEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 150, 40));
        
        jButtonActualizar.setText("Actualizar Cantidad");
        jButtonActualizar.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelAcciones.add(jButtonActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 180, 40));
        
        jButtonVaciar.setText("Vaciar Carrito");
        jButtonVaciar.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelAcciones.add(jButtonVaciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 150, 40));
        
        jPanelMain.add(jPanelAcciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 520, 520, 60));
        
        jPanelTotal.setBackground(new java.awt.Color(245, 245, 245));
        jPanelTotal.setBorder(javax.swing.BorderFactory.createTitledBorder("Resumen"));
        jPanelTotal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jLabelSubtotal.setFont(new java.awt.Font("Arial", Font.PLAIN, 16));
        jLabelSubtotal.setText("Subtotal: $0.00");
        jPanelTotal.add(jLabelSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 300, 30));
        
        jLabelTotal.setFont(new java.awt.Font("Arial", Font.BOLD, 20));
        jLabelTotal.setText("Total: $0.00");
        jPanelTotal.add(jLabelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 300, 40));
        
        jButtonComprar.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        jButtonComprar.setText("Realizar Compra");
        jButtonComprar.setBackground(new java.awt.Color(0, 150, 0));
        jButtonComprar.setForeground(new java.awt.Color(255, 255, 255));
        jPanelTotal.add(jButtonComprar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 300, 50));
        
        jPanelMain.add(jPanelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 100, 280, 200));
        
        jLabelMensaje.setFont(new java.awt.Font("Arial", Font.ITALIC, 14));
        jLabelMensaje.setForeground(new java.awt.Color(128, 128, 128));
        jLabelMensaje.setText("");
        jPanelMain.add(jLabelMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, 800, 30));
        
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

