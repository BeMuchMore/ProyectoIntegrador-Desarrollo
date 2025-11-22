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
import java.text.DecimalFormat;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Panel de administración avanzado con reportes y estadísticas
 * @author calam
 */
public class Administrador extends javax.swing.JFrame {
    
    private static final Logger logger = Logger.getLogger(Administrador.class.getName());
    private int usuarioActualId;
    private PermisosManager.InfoPermisos permisosActuales;
    private DefaultTableModel modeloTabla;
    
    /**
     * Creates new form Administrador
     */
    public Administrador() {
        SessionManager session = SessionManager.getInstance();
        this.usuarioActualId = session.getUserId();
        this.permisosActuales = PermisosManager.obtenerInfoPermisos(usuarioActualId);
        
        // Verificar acceso
        if (!PermisosManager.tienePermiso(usuarioActualId, PermisosManager.TipoPermiso.BASE_PERMISOS, 2)) {
            JOptionPane.showMessageDialog(null,
                "No tiene permisos suficientes para acceder a esta sección.\n" +
                "Se requiere nivel 2 o superior en BasePermisos.",
                "Acceso Denegado",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        configurarTabla();
        configurarEventos();
        cargarReportes();
        actualizarInfoUsuario();
    }
    
    /**
     * Configura la tabla de reportes
     */
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Usuario");
        modeloTabla.addColumn("Producto");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Total");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Estado");
        
        jTableReportes.setModel(modeloTabla);
        jTableReportes.setRowHeight(25);
        
        // Ocultar columna ID
        jTableReportes.getColumnModel().getColumn(0).setMinWidth(0);
        jTableReportes.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableReportes.getColumnModel().getColumn(0).setWidth(0);
    }
    
    /**
     * Configura los eventos
     */
    private void configurarEventos() {
        jBuscarButton.addActionListener(e -> buscarReportes());
        jBuscarTextFiel.addActionListener(e -> buscarReportes());
        
        jButtonActualizar.addActionListener(e -> cargarReportes());
        
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
                    Administrador.this.dispose();
                    new Productos(Productos.ModoOperacion.AGREGAR).setVisible(true);
                    cerrar();
                }
            }
        });
        
        ProductosActualizarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PRODUCTOS, 2)) {
                    Administrador.this.dispose();
                    new Productos(Productos.ModoOperacion.ACTUALIZAR).setVisible(true);
                    cerrar();
                }
            }
        });
        
        ProductosEliminarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PRODUCTOS, 3)) {
                    Administrador.this.dispose();
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
                    Administrador.this.dispose();
                    new Usuarios().setVisible(true);
                    cerrar();
                }
            }
        });
        
        UsuariosActualizarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_USUARIOS, 2)) {
                    Administrador.this.dispose();
                    new Usuarios().setVisible(true);
                    cerrar();
                }
            }
        });
        
        UsuariosEliminarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_USUARIOS, 3)) {
                    Administrador.this.dispose();
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
                    Administrador.this.dispose();
                    new Interfaz().setVisible(true);
                    cerrar();
                }
            }
        });
        
        InterfazCategoriasButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_INTERFAZ, 1)) {
                    Administrador.this.dispose();
                    new PanelAdministracion().setVisible(true);
                    cerrar();
                }
            }
        });
        
        // Administrador
        AdministradorPermisosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PERMISOS, 3)) {
                    Administrador.this.dispose();
                    new Permisos().setVisible(true);
                    cerrar();
                }
            }
        });
        
        AdministradorOtrosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ya estamos aquí
            }
        });
        
        jLabel23.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(Administrador.this,
                    "¿Está seguro de salir?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    Administrador.this.dispose();
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
     * Carga los reportes de pagos
     */
    private void cargarReportes() {
        modeloTabla.setRowCount(0);
        double totalGeneral = 0.0;
        
        try (Connection conn = Conexion.getConnection()) {
            String busqueda = jBuscarTextFiel.getText().trim();
            String query;
            PreparedStatement pstmt;
            
            if (busqueda.isEmpty()) {
                query = """
                    SELECT p.id_pago, u.usuario, pr.Nombre as producto, pd.cantidad, 
                           pd.subtotal, p.fecha_pago, p.estado
                    FROM tb_pago p
                    JOIN tb_usuarios u ON p.id_usuario = u.id
                    JOIN tb_pago_detalle pd ON p.id_pago = pd.id_pago
                    JOIN tb_productos pr ON pd.id_producto = pr.id
                    ORDER BY p.fecha_pago DESC
                    LIMIT 100
                    """;
                pstmt = conn.prepareStatement(query);
            } else {
                query = """
                    SELECT p.id_pago, u.usuario, pr.Nombre as producto, pd.cantidad, 
                           pd.subtotal, p.fecha_pago, p.estado
                    FROM tb_pago p
                    JOIN tb_usuarios u ON p.id_usuario = u.id
                    JOIN tb_pago_detalle pd ON p.id_pago = pd.id_pago
                    JOIN tb_productos pr ON pd.id_producto = pr.id
                    WHERE u.usuario LIKE ? OR pr.Nombre LIKE ? OR p.estado LIKE ?
                    ORDER BY p.fecha_pago DESC
                    LIMIT 100
                    """;
                pstmt = conn.prepareStatement(query);
                String pattern = "%" + busqueda + "%";
                pstmt.setString(1, pattern);
                pstmt.setString(2, pattern);
                pstmt.setString(3, pattern);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_pago"),
                    rs.getString("usuario"),
                    rs.getString("producto"),
                    rs.getInt("cantidad"),
                    "$" + String.format("%.2f", rs.getDouble("subtotal")),
                    rs.getTimestamp("fecha_pago").toString().substring(0, 16),
                    rs.getString("estado")
                };
                modeloTabla.addRow(row);
                totalGeneral += rs.getDouble("subtotal");
            }
            
            // Actualizar total
            DecimalFormat df = new DecimalFormat("#,##0.00");
            SumaCantidades.setText("Total General: $" + df.format(totalGeneral));
            jLabel14.setText("Registros: " + modeloTabla.getRowCount());
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al cargar reportes", e);
            JOptionPane.showMessageDialog(this,
                "Error al cargar reportes: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Busca reportes
     */
    private void buscarReportes() {
        cargarReportes();
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
        jLabel11 = new javax.swing.JLabel();
        jBuscarTextFiel = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jBuscarButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        SumaCantidades = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
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

        jLabel11.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        jLabel11.setText("Panel Administrativo - Reportes");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 500, -1));
        
        jButtonActualizar = new javax.swing.JButton();
        jButtonActualizar.setText("Actualizar");
        jButtonActualizar.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanel4.add(jButtonActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 20, 120, 35));

        jBuscarTextFiel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBuscarTextFielActionPerformed(evt);
            }
        });
        jPanel4.add(jBuscarTextFiel, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, 300, -1));
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, -1, -1));

        jBuscarButton.setText("Buscar");
        jBuscarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBuscarButtonActionPerformed(evt);
            }
        });
        jPanel4.add(jBuscarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 20, -1, -1));

        jLabel4.setText("____________________________________________________________________________________________________________________________________________________________________________________________________");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 980, -1));

        jLabel1.setFont(new java.awt.Font("Arial", Font.BOLD, 16));
        jLabel1.setText("Resumen:");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 540, 100, -1));

        SumaCantidades.setFont(new java.awt.Font("Arial", Font.BOLD, 16));
        SumaCantidades.setForeground(new java.awt.Color(0, 150, 0));
        SumaCantidades.setText("Total General: $0.00");
        jPanel4.add(SumaCantidades, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 540, 300, -1));

        jLabel14.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jLabel14.setText("Registros: 0");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 540, 200, -1));
        
        // Tabla de reportes
        jScrollPaneReportes = new javax.swing.JScrollPane();
        jTableReportes = new javax.swing.JTable();
        jTableReportes.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jScrollPaneReportes.setViewportView(jTableReportes);
        jPanel4.add(jScrollPaneReportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 940, 420));

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

    private void jBuscarTextFielActionPerformed(java.awt.event.ActionEvent evt) {
        buscarReportes();
    }

    private void jBuscarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        buscarReportes();
    }

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
        java.awt.EventQueue.invokeLater(() -> new Administrador().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AdministradorOtrosButton;
    private javax.swing.JLabel AdministradorPermisosButton;
    private javax.swing.JButton jButtonActualizar;
    private javax.swing.JLabel InterfazCategoriasButton;
    private javax.swing.JLabel InterfazOfertasButton;
    private javax.swing.JLabel InterfazPrincipalButton;
    private javax.swing.JLabel ProductosActualizarButton;
    private javax.swing.JLabel ProductosAgregarButton;
    private javax.swing.JLabel ProductosEliminarButton;
    private javax.swing.JLabel SumaCantidades;
    private javax.swing.JLabel UsuariosActualizarButton;
    private javax.swing.JLabel UsuariosAgregarButton;
    private javax.swing.JLabel UsuariosEliminarButton;
    private javax.swing.JButton jBuscarButton;
    private javax.swing.JTextField jBuscarTextFiel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPaneReportes;
    private javax.swing.JTable jTableReportes;
    // End of variables declaration//GEN-END:variables
}
