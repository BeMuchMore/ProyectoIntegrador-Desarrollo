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
 * Gestión de permisos de usuarios
 * Permite asignar niveles de permisos a usuarios
 */
public class Permisos extends javax.swing.JFrame {
    
    private static final Logger logger = Logger.getLogger(Permisos.class.getName());
    private DefaultTableModel modeloTabla;
    private int usuarioActualId;
    private PermisosManager.InfoPermisos permisosActuales;
    
    /**
     * Constructor
     */
    public Permisos() {
        SessionManager session = SessionManager.getInstance();
        this.usuarioActualId = session.getUserId();
        this.permisosActuales = PermisosManager.obtenerInfoPermisos(usuarioActualId);
        
        // Verificar acceso
        if (!PermisosManager.tienePermiso(usuarioActualId, PermisosManager.TipoPermiso.BASE_PERMISOS, 3)) {
            JOptionPane.showMessageDialog(null,
                "No tiene permisos suficientes para acceder a esta sección.\n" +
                "Se requiere nivel 3 o superior en BasePermisos.",
                "Acceso Denegado",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        configurarTabla();
        configurarEventos();
        cargarUsuarios();
        actualizarInfoUsuario();
    }
    
    /**
     * Configura la tabla
     */
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 6 && column <= 10; // Solo los niveles son editables
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column >= 6 && column <= 10) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Usuario");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Cargo");
        modeloTabla.addColumn("Correo");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("BaseUsuarios");
        modeloTabla.addColumn("BaseProductos");
        modeloTabla.addColumn("BaseInterfaz");
        modeloTabla.addColumn("BasePermisos");
        modeloTabla.addColumn("Temporales");
        
        jTablePermisos.setModel(modeloTabla);
        
        // Ocultar columna ID
        jTablePermisos.getColumnModel().getColumn(0).setMinWidth(0);
        jTablePermisos.getColumnModel().getColumn(0).setMaxWidth(0);
        jTablePermisos.getColumnModel().getColumn(0).setWidth(0);
    }
    
    /**
     * Configura los eventos
     */
    private void configurarEventos() {
        jButtonGuardar.addActionListener(e -> guardarPermisos());
        jButtonActualizar.addActionListener(e -> cargarUsuarios());
        jButtonVolver.addActionListener(e -> {
            this.dispose();
            new Interfaz().setVisible(true);
        });
        
        // Navegación lateral
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
                    Permisos.this.dispose();
                    new Productos(Productos.ModoOperacion.AGREGAR).setVisible(true);
                }
            }
        });
        
        ProductosActualizarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PRODUCTOS, 2)) {
                    Permisos.this.dispose();
                    new Productos(Productos.ModoOperacion.ACTUALIZAR).setVisible(true);
                }
            }
        });
        
        ProductosEliminarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PRODUCTOS, 3)) {
                    Permisos.this.dispose();
                    new Productos(Productos.ModoOperacion.ELIMINAR).setVisible(true);
                }
            }
        });
        
        // Usuarios
        UsuariosAgregarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_USUARIOS, 1)) {
                    Permisos.this.dispose();
                    new Usuarios().setVisible(true);
                }
            }
        });
        
        UsuariosActualizarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_USUARIOS, 2)) {
                    Permisos.this.dispose();
                    new Usuarios().setVisible(true);
                }
            }
        });
        
        UsuariosEliminarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_USUARIOS, 3)) {
                    Permisos.this.dispose();
                    new Usuarios().setVisible(true);
                }
            }
        });
        
        // Interfaz
        InterfazPrincipalButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_INTERFAZ, 1)) {
                    Permisos.this.dispose();
                    new Interfaz().setVisible(true);
                }
            }
        });
        
        InterfazCategoriasButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_INTERFAZ, 1)) {
                    Permisos.this.dispose();
                    new PanelAdministracion().setVisible(true);
                }
            }
        });
        
        // Administrador
        AdministradorPermisosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PERMISOS, 3)) {
                    // Ya estamos aquí
                }
            }
        });
        
        AdministradorOtrosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (verificarPermisoYHorario(PermisosManager.TipoPermiso.BASE_PERMISOS, 2)) {
                    Permisos.this.dispose();
                    new Administrador().setVisible(true);
                }
            }
        });
        
        jLabel23.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(Permisos.this,
                    "¿Está seguro de salir?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    Permisos.this.dispose();
                    new UI.principal(0).setVisible(true);
                }
            }
        });
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
     * Actualiza la información del usuario en la barra lateral
     */
    private void actualizarInfoUsuario() {
        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT nombre, foto FROM tb_usuarios WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, usuarioActualId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                jLabel20.setText(rs.getString("nombre"));
                // Cargar foto si existe
                String foto = rs.getString("foto");
                if (foto != null && !foto.isEmpty()) {
                    // Cargar foto
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al cargar info usuario", e);
        }
    }
    
    /**
     * Carga los usuarios con sus permisos
     */
    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        
        try (Connection conn = Conexion.getConnection()) {
            String query = """
                SELECT u.id, u.usuario, u.nombre, u.cargo, u.correo,
                       COALESCE(f.BaseUsuarios, 0) as BaseUsuarios,
                       COALESCE(f.BaseProductos, 0) as BaseProductos,
                       COALESCE(f.BaseInterfaz, 0) as BaseInterfaz,
                       COALESCE(f.BasePermisos, 0) as BasePermisos,
                       COALESCE(f.Temporales, 0) as Temporales
                FROM tb_usuarios u
                LEFT JOIN tb_funcionessuperusuarios f ON u.id = f.id_usuario
                WHERE u.cargo = 'admin' OR f.id_usuario IS NOT NULL
                ORDER BY u.nombre
                """;
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("usuario"),
                    rs.getString("nombre"),
                    rs.getString("cargo"),
                    rs.getString("correo") != null ? rs.getString("correo") : "",
                    rs.getString("cargo").equals("admin") ? "Activo" : 
                        (rs.getInt("BaseUsuarios") > 0 ? "Activo" : "Inactivo"),
                    rs.getInt("BaseUsuarios"),
                    rs.getInt("BaseProductos"),
                    rs.getInt("BaseInterfaz"),
                    rs.getInt("BasePermisos"),
                    rs.getInt("Temporales")
                };
                modeloTabla.addRow(row);
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al cargar usuarios", e);
            JOptionPane.showMessageDialog(this,
                "Error al cargar usuarios: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Guarda los permisos modificados
     */
    private void guardarPermisos() {
        if (!PermisosManager.esHorarioPermitido(permisosActuales.basePermisos)) {
            JOptionPane.showMessageDialog(this,
                "No puede modificar permisos en el horario actual.",
                "Horario No Permitido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            Conexion.beginTransaction(conn);
            
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                int userId = (Integer) modeloTabla.getValueAt(i, 0);
                int baseUsuarios = (Integer) modeloTabla.getValueAt(i, 6);
                int baseProductos = (Integer) modeloTabla.getValueAt(i, 7);
                int baseInterfaz = (Integer) modeloTabla.getValueAt(i, 8);
                int basePermisos = (Integer) modeloTabla.getValueAt(i, 9);
                int temporales = (Integer) modeloTabla.getValueAt(i, 10);
                
                // Validar niveles (0-5)
                if (baseUsuarios < 0 || baseUsuarios > 5 ||
                    baseProductos < 0 || baseProductos > 5 ||
                    baseInterfaz < 0 || baseInterfaz > 5 ||
                    basePermisos < 0 || basePermisos > 5 ||
                    temporales < 0 || temporales > 5) {
                    throw new IllegalArgumentException("Los niveles deben estar entre 0 y 5");
                }
                
                // Verificar si existe registro
                String queryExiste = "SELECT id_usuario FROM tb_funcionessuperusuarios WHERE id_usuario = ?";
                PreparedStatement pstmtExiste = conn.prepareStatement(queryExiste);
                pstmtExiste.setInt(1, userId);
                ResultSet rs = pstmtExiste.executeQuery();
                
                if (rs.next()) {
                    // Actualizar
                    String update = """
                        UPDATE tb_funcionessuperusuarios
                        SET BaseUsuarios = ?, BaseProductos = ?, BaseInterfaz = ?,
                            BasePermisos = ?, Temporales = ?
                        WHERE id_usuario = ?
                        """;
                    PreparedStatement pstmt = conn.prepareStatement(update);
                    pstmt.setInt(1, baseUsuarios);
                    pstmt.setInt(2, baseProductos);
                    pstmt.setInt(3, baseInterfaz);
                    pstmt.setInt(4, basePermisos);
                    pstmt.setInt(5, temporales);
                    pstmt.setInt(6, userId);
                    pstmt.executeUpdate();
                } else {
                    // Insertar
                    String insert = """
                        INSERT INTO tb_funcionessuperusuarios
                        (id_usuario, cargo, BaseUsuarios, BaseProductos, BaseInterfaz, BasePermisos, Temporales)
                        VALUES (?, 'admin', ?, ?, ?, ?, ?)
                        """;
                    PreparedStatement pstmt = conn.prepareStatement(insert);
                    pstmt.setInt(1, userId);
                    pstmt.setInt(2, baseUsuarios);
                    pstmt.setInt(3, baseProductos);
                    pstmt.setInt(4, baseInterfaz);
                    pstmt.setInt(5, basePermisos);
                    pstmt.setInt(6, temporales);
                    pstmt.executeUpdate();
                }
            }
            
            Conexion.commit(conn);
            
            JOptionPane.showMessageDialog(this,
                "Permisos guardados correctamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            
            cargarUsuarios();
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Error al hacer rollback", ex);
                }
            }
            logger.log(Level.SEVERE, "Error al guardar permisos", e);
            JOptionPane.showMessageDialog(this,
                "Error al guardar permisos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Error al hacer rollback", ex);
                }
            }
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.log(Level.WARNING, "Error al cerrar conexión", e);
                }
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
        jButtonActualizar = new javax.swing.JButton();
        jButtonGuardar = new javax.swing.JButton();
        jButtonVolver = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePermisos = new javax.swing.JTable();
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
        jLabelInfo = new javax.swing.JLabel();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jPanelHeader.setBackground(new java.awt.Color(255, 255, 255));
        jPanelHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jLabelTitulo.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        jLabelTitulo.setText("Gestión de Permisos");
        jPanelHeader.add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 400, 40));
        
        jButtonActualizar.setText("Actualizar");
        jButtonActualizar.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelHeader.add(jButtonActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 25, 120, 35));
        
        jButtonGuardar.setText("Guardar Cambios");
        jButtonGuardar.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jButtonGuardar.setBackground(new java.awt.Color(0, 150, 0));
        jButtonGuardar.setForeground(new java.awt.Color(255, 255, 255));
        jPanelHeader.add(jButtonGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 25, 150, 35));
        
        jButtonVolver.setText("← Volver");
        jButtonVolver.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelHeader.add(jButtonVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 25, 120, 35));
        
        jPanelMain.add(jPanelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 1363, 80));
        
        jTablePermisos.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jTablePermisos.setRowHeight(25);
        jScrollPane1.setViewportView(jTablePermisos);
        
        jPanelMain.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 1200, 500));
        
        jLabelInfo.setFont(new java.awt.Font("Arial", Font.ITALIC, 12));
        jLabelInfo.setForeground(new java.awt.Color(100, 100, 100));
        jLabelInfo.setText("<html>Niveles: 0=Sin acceso, 1=Crear, 2=Modificar, 3=Eliminar, 4=Administrar, 5=Super Admin<br>" +
                          "Restricciones: Nivel 1-2 (8:00-18:00), Nivel 3-4 (6:00-22:00), Nivel 5 (Sin restricción)</html>");
        jPanelMain.add(jLabelInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 610, 1200, 50));
        
        // Barra lateral (copiada de Interfaz.java)
        jPanel7.setBackground(new java.awt.Color(153, 153, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/usuario.png")));
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
        ProductosEliminarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/cesta-de-la-compra.png")));
        ProductosEliminarButton.setText("Eliminar");
        ProductosActualizarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar (1).png")));
        ProductosActualizarButton.setText("Actualizar");
        ProductosAgregarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/mas (1).png")));
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
        UsuariosAgregarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/agregar-usuario.png")));
        UsuariosAgregarButton.setText("Agregar");
        UsuariosEliminarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/borrar-usuario.png")));
        UsuariosEliminarButton.setText("Eliminar");
        UsuariosActualizarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar (1).png")));
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
        
        jPanel7.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 120, -1));
        
        jLabel9.setText("Usuarios");
        jPanel7.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, -1, -1));
        
        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/flecha-izquierda.png")));
        jLabel23.setText("Salir");
        jPanel7.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 80, 20));
        
        jLabel24.setText("Interfaz");
        jPanel7.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 500, -1, -1));
        
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        InterfazPrincipalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/hogar.png")));
        InterfazPrincipalButton.setText("Principal");
        InterfazCategoriasButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/aplicaciones.png")));
        InterfazCategoriasButton.setText("Categorias");
        InterfazOfertasButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/corazon.png")));
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
        AdministradorPermisosButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/usuarios-alt.png")));
        AdministradorPermisosButton.setText("Permisos");
        AdministradorOtrosButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/menu-hamburguesa.png")));
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
        jPanelMain.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 700));
        
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
    private javax.swing.JLabel AdministradorOtrosButton;
    private javax.swing.JLabel AdministradorPermisosButton;
    private javax.swing.JButton jButtonActualizar;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelInfo;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTablePermisos;
    private javax.swing.JLabel InterfazCategoriasButton;
    private javax.swing.JLabel InterfazOfertasButton;
    private javax.swing.JLabel InterfazPrincipalButton;
    private javax.swing.JLabel ProductosActualizarButton;
    private javax.swing.JLabel ProductosAgregarButton;
    private javax.swing.JLabel ProductosEliminarButton;
    private javax.swing.JLabel UsuariosActualizarButton;
    private javax.swing.JLabel UsuariosAgregarButton;
    private javax.swing.JLabel UsuariosEliminarButton;
}

