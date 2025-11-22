/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UX;

import UI.Conexion;
import UI.SessionManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Panel de administración para gestionar categorías y estadísticas
 * @author calam
 */
public class PanelAdministracion extends javax.swing.JFrame {
    
    private static final Logger logger = Logger.getLogger(PanelAdministracion.class.getName());
    private int usuarioActualId;
    private PermisosManager.InfoPermisos permisosActuales;
    private DefaultTableModel modeloTabla;
    
    /**
     * Creates new form PanelAdministracion
     */
    public PanelAdministracion() {
        SessionManager session = SessionManager.getInstance();
        this.usuarioActualId = session.getUserId();
        this.permisosActuales = PermisosManager.obtenerInfoPermisos(usuarioActualId);
        
        // Verificar acceso
        if (!PermisosManager.tienePermiso(usuarioActualId, PermisosManager.TipoPermiso.BASE_INTERFAZ, 1)) {
            JOptionPane.showMessageDialog(null,
                "No tiene permisos suficientes para acceder a esta sección.\n" +
                "Se requiere nivel 1 o superior en BaseInterfaz.",
                "Acceso Denegado",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        configurarTabla();
        configurarEventos();
        cargarEstadisticas();
        cargarCategorias();
        actualizarInfoUsuario();
    }
    
    /**
     * Configura la tabla de categorías
     */
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };
        
        modeloTabla.addColumn("Categoría");
        modeloTabla.addColumn("Total Productos");
        modeloTabla.addColumn("Stock Total");
        modeloTabla.addColumn("Valor Total");
        
        jTableCategorias.setModel(modeloTabla);
        jTableCategorias.setRowHeight(30);
    }
    
    /**
     * Configura los eventos
     */
    private void configurarEventos() {
        jButtonActualizar.addActionListener(e -> {
            cargarEstadisticas();
            cargarCategorias();
        });
        
        // Cursores de mano
        ProductosAgregarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ProductosActualizarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ProductosEliminarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UsuariosAgregarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UsuariosActualizarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UsuariosEliminarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        InterfazPrincipalButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        InterfazCategoriasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        InterfazOfertasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        AdministradorPermisosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        AdministradorOtrosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabel23.setCursor(new Cursor(Cursor.HAND_CURSOR));

        configurarNavegacionLateral();
    }
    
    /**
     * Configura la navegación lateral
     */
    private void configurarNavegacionLateral() {
        // Productos
        ProductosAgregarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PRODUCTOS, 1)) {
                    PanelAdministracion.this.dispose();
                    new Productos(Productos.ModoOperacion.AGREGAR).setVisible(true);
                    cerrar();
                }
            }
        });
        
        ProductosActualizarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PRODUCTOS, 2)) {
                    PanelAdministracion.this.dispose();
                    new Productos(Productos.ModoOperacion.ACTUALIZAR).setVisible(true);
                    cerrar();
                }
            }
        });
        
        ProductosEliminarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PRODUCTOS, 3)) {
                    PanelAdministracion.this.dispose();
                    new Productos(Productos.ModoOperacion.ELIMINAR).setVisible(true);
                    cerrar();
                }
            }
        });
        
        // Usuarios
        UsuariosAgregarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_USUARIOS, 1)) {
                    PanelAdministracion.this.dispose();
                    new Usuarios().setVisible(true);
                    cerrar();
                }
            }
        });
        
        UsuariosActualizarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_USUARIOS, 2)) {
                    PanelAdministracion.this.dispose();
                    new Usuarios().setVisible(true);
                    cerrar();
                }
            }
        });
        
        UsuariosEliminarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_USUARIOS, 3)) {
                    PanelAdministracion.this.dispose();
                    new Usuarios().setVisible(true);
                    cerrar();
                }
            }
        });
        
        // Interfaz
        InterfazPrincipalButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_INTERFAZ, 1)) {
                    PanelAdministracion.this.dispose();
                    new Interfaz().setVisible(true);
                    cerrar();
                }
            }
        });
        
        InterfazCategoriasButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ya estamos aquí
            }
        });
        
        InterfazOfertasButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Implementar si existe ventana de ofertas
            }
        });
        
        // Administrador
        AdministradorPermisosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PERMISOS, 3)) {
                    PanelAdministracion.this.dispose();
                    new Permisos().setVisible(true);
                    cerrar();
                }
            }
        });
        
        AdministradorOtrosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PERMISOS, 2)) {
                    PanelAdministracion.this.dispose();
                    new Administrador().setVisible(true);
                    cerrar();
                }
            }
        });
        
        jLabel23.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(PanelAdministracion.this,
                    "¿Está seguro de salir?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    PanelAdministracion.this.dispose();
                    new UI.principal(0).setVisible(true);
                }
            }
        });
    }

    private void cerrar() {
        this.dispose();
    }
    
    /**
     * Verifica permiso y horario
     */
    private boolean verificarPermisoYHorario(PermisosManager.TipoPermiso tipo, int nivelMinimo) {
        int nivel = PermisosManager.obtenerNivelPermiso(usuarioActualId, tipo);
        
        if (nivel < nivelMinimo) {
            JOptionPane.showMessageDialog(this,
                "No tiene permisos suficientes.\n" +
                "Se requiere nivel " + nivelMinimo + " o superior.",
                "Permiso Insuficiente",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!PermisosManager.esHorarioPermitido(nivel)) {
            JOptionPane.showMessageDialog(this,
                "Esta operación no está permitida en el horario actual.\n" +
                "Su nivel (" + nivel + ") tiene restricciones de horario.",
                "Horario No Permitido",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * Actualiza la información del usuario
     */
    private void actualizarInfoUsuario() {
        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT nombre, foto FROM tb_usuarios WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, usuarioActualId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                jLabel20.setText(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al cargar info usuario", e);
        }
    }
    
    /**
     * Carga estadísticas generales
     */
    private void cargarEstadisticas() {
        try (Connection conn = Conexion.getConnection()) {
            // Total productos
            String queryProductos = "SELECT COUNT(*) as total FROM tb_productos WHERE estado = 'vigente'";
            PreparedStatement pstmt = conn.prepareStatement(queryProductos);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                jLabelTotalProductos.setText("Total Productos: " + rs.getInt("total"));
            }
            
            // Total usuarios
            String queryUsuarios = "SELECT COUNT(*) as total FROM tb_usuarios";
            pstmt = conn.prepareStatement(queryUsuarios);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                jLabelTotalUsuarios.setText("Total Usuarios: " + rs.getInt("total"));
            }
            
            // Stock total
            String queryStock = "SELECT SUM(cantidad) as total FROM tb_inventario";
            pstmt = conn.prepareStatement(queryStock);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                jLabelStockTotal.setText("Stock Total: " + rs.getInt("total") + " unidades");
            }
            
            // Valor total inventario
            String queryValor = "SELECT SUM(cantidad * precio) as total FROM tb_inventario";
            pstmt = conn.prepareStatement(queryValor);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                double valor = rs.getDouble("total");
                jLabelValorTotal.setText("Valor Total: $" + String.format("%.2f", valor));
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al cargar estadísticas", e);
        }
    }
    
    /**
     * Carga categorías con estadísticas
     */
    private void cargarCategorias() {
        modeloTabla.setRowCount(0);
        
        try (Connection conn = Conexion.getConnection()) {
            String query = """
                SELECT 
                    p.Categorias,
                    COUNT(DISTINCT p.id) as total_productos,
                    COALESCE(SUM(i.cantidad), 0) as stock_total,
                    COALESCE(SUM(i.cantidad * i.precio), 0) as valor_total
                FROM tb_productos p
                LEFT JOIN tb_inventario i ON p.id = i.id_producto
                WHERE p.estado = 'vigente' AND p.Categorias IS NOT NULL AND p.Categorias != ''
                GROUP BY p.Categorias
                ORDER BY p.Categorias
                """;
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("Categorias"),
                    rs.getInt("total_productos"),
                    rs.getInt("stock_total"),
                    "$" + String.format("%.2f", rs.getDouble("valor_total"))
                };
                modeloTabla.addRow(row);
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al cargar categorías", e);
            JOptionPane.showMessageDialog(this,
                "Error al cargar categorías: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        LabelActualizar = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        ProductosEliminarButton = new javax.swing.JLabel();
        ProductosActualizarButton = new javax.swing.JLabel();
        ProductosAgregarButton = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        UsuariosAgregarButton = new javax.swing.JLabel();
        UsuariosEliminarButton = new javax.swing.JLabel();
        UsuariosActualizarButton = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        InterfazPrincipalButton = new javax.swing.JLabel();
        InterfazCategoriasButton = new javax.swing.JLabel();
        InterfazOfertasButton = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        AdministradorPermisosButton = new javax.swing.JLabel();
        AdministradorOtrosButton = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMaximumSize(new java.awt.Dimension(949, 391));
        jPanel4.setMinimumSize(new java.awt.Dimension(949, 391));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LabelActualizar.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        LabelActualizar.setText("Panel de Administración - Categorías");
        jPanel4.add(LabelActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 600, -1));
        
        jButtonActualizar = new javax.swing.JButton();
        jButtonActualizar.setText("Actualizar");
        jButtonActualizar.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanel4.add(jButtonActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 20, 120, 35));
        
        jLabel4.setText("____________________________________________________________________________________________________________________________________________________________________________________________________");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 980, -1));
        
        // Panel de estadísticas
        jPanelEstadisticas = new javax.swing.JPanel();
        jPanelEstadisticas.setBackground(new java.awt.Color(245, 245, 245));
        jPanelEstadisticas.setBorder(javax.swing.BorderFactory.createTitledBorder("Estadísticas Generales"));
        jPanelEstadisticas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jLabelTotalProductos = new javax.swing.JLabel();
        jLabelTotalProductos.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelTotalProductos.setText("Total Productos: 0");
        jPanelEstadisticas.add(jLabelTotalProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 250, 25));
        
        jLabelTotalUsuarios = new javax.swing.JLabel();
        jLabelTotalUsuarios.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelTotalUsuarios.setText("Total Usuarios: 0");
        jPanelEstadisticas.add(jLabelTotalUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 65, 250, 25));
        
        jLabelStockTotal = new javax.swing.JLabel();
        jLabelStockTotal.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelStockTotal.setText("Stock Total: 0 unidades");
        jPanelEstadisticas.add(jLabelStockTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 300, 25));
        
        jLabelValorTotal = new javax.swing.JLabel();
        jLabelValorTotal.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelValorTotal.setForeground(new java.awt.Color(0, 150, 0));
        jLabelValorTotal.setText("Valor Total: $0.00");
        jPanelEstadisticas.add(jLabelValorTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 65, 300, 25));
        
        jPanel4.add(jPanelEstadisticas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 900, 120));
        
        // Tabla de categorías
        jLabelTituloTabla = new javax.swing.JLabel();
        jLabelTituloTabla.setFont(new java.awt.Font("Arial", Font.BOLD, 18));
        jLabelTituloTabla.setText("Categorías y Estadísticas");
        jPanel4.add(jLabelTituloTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 400, 30));
        
        jScrollPaneCategorias = new javax.swing.JScrollPane();
        jTableCategorias = new javax.swing.JTable();
        jTableCategorias.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jScrollPaneCategorias.setViewportView(jTableCategorias);
        jPanel4.add(jScrollPaneCategorias, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 900, 300));

        jPanel7.setBackground(new java.awt.Color(153, 153, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/usuario.png"))); // NOI18N
        jLabel20.setText("nombre");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel20)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 120, 80));

        jLabel18.setText("Productos");
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, -1, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        ProductosEliminarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/cesta-de-la-compra.png"))); // NOI18N
        ProductosEliminarButton.setText("Eliminar");

        ProductosActualizarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar (1).png"))); // NOI18N
        ProductosActualizarButton.setText("Actualizar");

        ProductosAgregarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/mas (1).png"))); // NOI18N
        ProductosAgregarButton.setText("Agregar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(ProductosActualizarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ProductosAgregarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ProductosEliminarButton))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ProductosAgregarButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ProductosActualizarButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ProductosEliminarButton)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 120, 140));

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        UsuariosAgregarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/agregar-usuario.png"))); // NOI18N
        UsuariosAgregarButton.setText("Agregar");

        UsuariosEliminarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/borrar-usuario.png"))); // NOI18N
        UsuariosEliminarButton.setText("Eliminar");

        UsuariosActualizarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar (1).png"))); // NOI18N
        UsuariosActualizarButton.setText("Actualizar");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UsuariosAgregarButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(UsuariosEliminarButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(UsuariosActualizarButton, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                        .addGap(0, 15, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(UsuariosAgregarButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UsuariosEliminarButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(UsuariosActualizarButton)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, -1, -1));

        jLabel9.setText("Usuarios");
        jPanel7.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, -1, -1));

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/flecha-izquierda.png"))); // NOI18N
        jLabel23.setText("Salir");
        jPanel7.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 80, 20));

        jLabel24.setText("Interfaz");
        jPanel7.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 500, -1, -1));

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        InterfazPrincipalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/hogar.png"))); // NOI18N
        InterfazPrincipalButton.setText("Principal");

        InterfazCategoriasButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/aplicaciones.png"))); // NOI18N
        InterfazCategoriasButton.setText("Categorias");

        InterfazOfertasButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/corazon.png"))); // NOI18N
        InterfazOfertasButton.setText("Ofertas");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(InterfazPrincipalButton)
                    .addComponent(InterfazCategoriasButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(InterfazOfertasButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(InterfazPrincipalButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InterfazCategoriasButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InterfazOfertasButton)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 120, 130));

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AdministradorPermisosButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/usuarios-alt.png"))); // NOI18N
        AdministradorPermisosButton.setText("Permisos");

        AdministradorOtrosButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/menu-hamburguesa.png"))); // NOI18N
        AdministradorOtrosButton.setText("Otros");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AdministradorPermisosButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AdministradorOtrosButton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AdministradorPermisosButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AdministradorOtrosButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 710, 120, 90));

        jLabel28.setText("Administrador");
        jPanel7.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 680, -1, -1));

        jScrollPane5.setViewportView(jPanel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1170, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1020, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 582, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new PanelAdministracion().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AdministradorOtrosButton;
    private javax.swing.JLabel AdministradorPermisosButton;
    private javax.swing.JButton jButtonActualizar;
    private javax.swing.JLabel InterfazCategoriasButton;
    private javax.swing.JLabel InterfazOfertasButton;
    private javax.swing.JLabel InterfazPrincipalButton;
    private javax.swing.JLabel LabelActualizar;
    private javax.swing.JLabel ProductosActualizarButton;
    private javax.swing.JLabel ProductosAgregarButton;
    private javax.swing.JLabel ProductosEliminarButton;
    private javax.swing.JLabel UsuariosActualizarButton;
    private javax.swing.JLabel UsuariosAgregarButton;
    private javax.swing.JLabel UsuariosEliminarButton;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelTotalProductos;
    private javax.swing.JLabel jLabelTotalUsuarios;
    private javax.swing.JLabel jLabelStockTotal;
    private javax.swing.JLabel jLabelValorTotal;
    private javax.swing.JLabel jLabelTituloTabla;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelEstadisticas;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPaneCategorias;
    private javax.swing.JTable jTableCategorias;
    // End of variables declaration//GEN-END:variables
}

