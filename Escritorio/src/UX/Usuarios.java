package UX;

import UI.Conexion;
import UI.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Sistema CRUD completo de usuarios - TODO EN UN SOLO ARCHIVO
 * @author Julian
 */
public class Usuarios extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Usuarios.class.getName());
    private JPanel panelUsuarios;
    private JScrollPane jScrollPaneUsuarios;
   
    
    
    

    public Usuarios() {
        initComponents();
        configurarPanelUsuarios();
        cargarUsuarios();
        configurarEventos();
    }

    private void cerrar() {
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jBuscarTextFiel = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jBuscarButton = new javax.swing.JButton();
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
        setTitle("Gestión de Usuarios");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMaximumSize(new java.awt.Dimension(949, 391));
        jPanel4.setMinimumSize(new java.awt.Dimension(949, 391));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Arial", 1, 24));
        jLabel11.setText("Usuarios");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jBuscarTextFiel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBuscarTextFielActionPerformed(evt);
            }
        });
        jPanel4.add(jBuscarTextFiel, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, 300, 30));
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, -1, -1));

        jBuscarButton.setText("Buscar");
        jBuscarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBuscarButtonActionPerformed(evt);
            }
        });
        jPanel4.add(jBuscarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 20, 100, 30));

        jLabel4.setText("____________________________________________________________________________________________________________________________________________________________________________________________________");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 980, -1));

        jScrollPaneUsuarios = new JScrollPane();
        jScrollPaneUsuarios.setBorder(null);
        jScrollPaneUsuarios.getVerticalScrollBar().setUnitIncrement(16);
        jPanel4.add(jScrollPaneUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 950, 480));

        jPanel7.setBackground(new java.awt.Color(153, 153, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/usuario.png")));
      jLabel20.setCursor(new Cursor(Cursor.HAND_CURSOR));
jLabel20.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        
  
    }
});
        
        jLabel20.setText("Usuario");

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
             ProductosEliminarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ProductosEliminarButton.addMouseListener(new MouseAdapter() {
            @Override
                    public void mouseClicked(MouseEvent e) {
                        Productos ventana = new Productos(Productos.ModoOperacion.ELIMINAR);
ventana.setVisible(true);
 cerrar();
    }
        });
        
        ProductosEliminarButton.setText("Eliminar");

        ProductosActualizarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar (1).png")));
                        ProductosActualizarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ProductosActualizarButton.addMouseListener(new MouseAdapter() {
            @Override
                    public void mouseClicked(MouseEvent e) {
                        Productos ventana = new Productos(Productos.ModoOperacion.ACTUALIZAR);
ventana.setVisible(true);
cerrar();
    }
        });
        ProductosActualizarButton.setText("Actualizar");

        ProductosAgregarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/mas (1).png")));
         ProductosAgregarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ProductosAgregarButton.addMouseListener(new MouseAdapter() {
            @Override
                    public void mouseClicked(MouseEvent e) {
                        Productos ventana = new Productos(Productos.ModoOperacion.AGREGAR);
ventana.setVisible(true);
cerrar();
    }
        });
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
        UsuariosAgregarButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

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

        jPanel7.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, -1, -1));

        jLabel9.setText("Usuarios");
        jPanel7.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, -1, -1));

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Arial", 1, 14));
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/flecha-izquierda.png")));
        jLabel23.setText("Salir");
        jLabel23.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1020, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        
    
    private void configurarPanelUsuarios() {
        panelUsuarios = new JPanel();
        panelUsuarios.setLayout(new BoxLayout(panelUsuarios, BoxLayout.Y_AXIS));
        panelUsuarios.setBackground(Color.WHITE);
        jScrollPaneUsuarios.setViewportView(panelUsuarios);
    }

    private void configurarEventos() {
        // Usuarios (acciones propias)
        UsuariosAgregarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarUsuario();
            }
        });
        UsuariosActualizarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarUsuarios();
            }
        });
        UsuariosEliminarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(Usuarios.this, "Seleccione un usuario y use el botón Eliminar en la tarjeta.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Navegación lateral
        InterfazPrincipalButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        InterfazCategoriasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        InterfazOfertasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        AdministradorPermisosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        AdministradorOtrosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        InterfazPrincipalButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Interfaz().setVisible(true);
                cerrar();
            }
        });
        InterfazCategoriasButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new PanelAdministracion().setVisible(true);
                cerrar();
            }
        });
        AdministradorPermisosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Permisos().setVisible(true);
                cerrar();
            }
        });
        AdministradorOtrosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Administrador().setVisible(true);
                cerrar();
            }
        });

        // Salir a principal
        jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int respuesta = JOptionPane.showConfirmDialog(Usuarios.this,
                        "¿Está seguro de salir?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    new UI.principal(0).setVisible(true);
                    cerrar();
                }
            }
        });
    }

    // ==================== MÉTODOS CRUD ====================
    
    private void cargarUsuarios() {
        List<Usuario> usuarios = obtenerTodosDB();
        mostrarUsuarios(usuarios);
    }

    private void mostrarUsuarios(List<Usuario> usuarios) {
        panelUsuarios.removeAll();
        
        if (usuarios.isEmpty()) {
            JLabel lblNoHay = new JLabel("No se encontraron usuarios");
            lblNoHay.setFont(new Font("Arial", Font.ITALIC, 16));
            lblNoHay.setForeground(Color.GRAY);
            lblNoHay.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelUsuarios.add(Box.createVerticalStrut(50));
            panelUsuarios.add(lblNoHay);
        } else {
            for (Usuario usuario : usuarios) {
                JPanel panel = crearPanelUsuario(usuario);
                panelUsuarios.add(panel);
                panelUsuarios.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        panelUsuarios.revalidate();
        panelUsuarios.repaint();
    }

    private JPanel crearPanelUsuario(Usuario usuario) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(950, 100));
        panel.setMaximumSize(new Dimension(1950, 100));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        
        // Foto
        JLabel lblFoto = new JLabel("👤");
        lblFoto.setBounds(10, 10, 80, 80);
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setFont(new Font("Arial", Font.PLAIN, 40));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.add(lblFoto);
        
        // Nombre
        JLabel lblNombre = new JLabel(usuario.nombre + " " + usuario.apellido);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblNombre.setBounds(100, 10, 300, 25);
        panel.add(lblNombre);
        
        // Usuario
        JLabel lblUsuario = new JLabel("@" + usuario.usuario);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsuario.setForeground(new Color(100, 100, 100));
        lblUsuario.setBounds(100, 35, 300, 20);
        panel.add(lblUsuario);
        
        // Identificación
        JLabel lblId = new JLabel((usuario.tipoIdentificacion != null ? usuario.tipoIdentificacion : "ID") + 
                                  ": " + String.format("%,d", usuario.identificacion));
        lblId.setFont(new Font("Arial", Font.PLAIN, 12));
        lblId.setForeground(new Color(120, 120, 120));
        lblId.setBounds(100, 58, 200, 18);
        panel.add(lblId);
        
        // Badge cargo
        JLabel lblCargo = new JLabel(usuario.cargo.toUpperCase());
        lblCargo.setFont(new Font("Arial", Font.BOLD, 11));
        lblCargo.setHorizontalAlignment(SwingConstants.CENTER);
        lblCargo.setBounds(420, 15, 80, 25);
        lblCargo.setOpaque(true);
        if ("admin".equals(usuario.cargo)) {
            lblCargo.setBackground(new Color(255, 107, 107));
            lblCargo.setForeground(Color.WHITE);
        } else {
            lblCargo.setBackground(new Color(200, 230, 255));
            lblCargo.setForeground(new Color(0, 102, 204));
        }
        panel.add(lblCargo);
        
        // Correo
        if (usuario.correo != null && !usuario.correo.trim().isEmpty()) {
            JLabel lblCorreo = new JLabel("✉ " + usuario.correo);
            lblCorreo.setFont(new Font("Arial", Font.PLAIN, 12));
            lblCorreo.setForeground(new Color(100, 100, 100));
            lblCorreo.setBounds(420, 48, 300, 18);
            panel.add(lblCorreo);
        }
        
        // Botón Editar
        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(750, 15, 120, 30);
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.addActionListener(e -> editarUsuario(usuario));
        panel.add(btnEditar);
        
        // Botón Eliminar
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(750, 50, 120, 30);
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.addActionListener(e -> eliminarUsuario(usuario));
        panel.add(btnEliminar);
        
        return panel;
    }

    private void agregarUsuario() {
        UsuarioDialog dialog = new UsuarioDialog(this, null);
        dialog.setVisible(true);
        if (dialog.guardado) {
            cargarUsuarios();
        }
    }

    private void editarUsuario(Usuario usuario) {
        UsuarioDialog dialog = new UsuarioDialog(this, usuario);
        dialog.setVisible(true);
        if (dialog.guardado) {
            cargarUsuarios();
        }
    }

    private void eliminarUsuario(Usuario usuario) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Eliminar al usuario '" + usuario.usuario + "'?\nEsta acción no se puede deshacer.",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if ("admin".equals(usuario.cargo) && contarAdministradoresDB() <= 1) {
                JOptionPane.showMessageDialog(this,
                    "No se puede eliminar el último administrador",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (eliminarDB(usuario.id)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void jBuscarTextFielActionPerformed(java.awt.event.ActionEvent evt) {                                                
        buscarUsuarios();
    }                                               

    private void jBuscarButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        buscarUsuarios();
    }                                             

    private void buscarUsuarios() {
        String termino = jBuscarTextFiel.getText().trim();
        List<Usuario> usuarios = termino.isEmpty() ? obtenerTodosDB() : buscarDB(termino);
        mostrarUsuarios(usuarios);
    }

    // ==================== OPERACIONES DE BASE DE DATOS ====================
    
    private List<Usuario> obtenerTodosDB() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM tb_usuarios ORDER BY id DESC";
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al obtener usuarios", e);
        }
        return usuarios;
    }

    private List<Usuario> buscarDB(String termino) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM tb_usuarios WHERE nombre LIKE ? OR apellido LIKE ? OR " +
                    "usuario LIKE ? OR CAST(identificacion AS CHAR) LIKE ? ORDER BY id DESC";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String like = "%" + termino + "%";
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);
            pstmt.setString(4, like);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al buscar", e);
        }
        return usuarios;
    }

    private boolean insertarDB(Usuario usuario) {
        String sql = "INSERT INTO tb_usuarios (nombre, apellido, identificacion, TipoIdentificacion, " +
                    "usuario, contrasena, correo, cargo, descripcion, foto, data) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario.nombre);
            pstmt.setString(2, usuario.apellido);
            pstmt.setLong(3, usuario.identificacion);
            pstmt.setString(4, usuario.tipoIdentificacion);
            pstmt.setString(5, usuario.usuario);
            pstmt.setString(6, usuario.contrasena);
            pstmt.setString(7, usuario.correo);
            pstmt.setString(8, usuario.cargo);
            pstmt.setString(9, usuario.descripcion);
            pstmt.setString(10, usuario.foto);
            pstmt.setString(11, usuario.data);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al insertar", e);
            return false;
        }
    }

    private boolean actualizarDB(Usuario usuario) {
        String sql = "UPDATE tb_usuarios SET nombre=?, apellido=?, identificacion=?, TipoIdentificacion=?, " +
                    "usuario=?, contrasena=?, correo=?, cargo=?, descripcion=?, foto=?, data=? WHERE id=?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.nombre);
            pstmt.setString(2, usuario.apellido);
            pstmt.setLong(3, usuario.identificacion);
            pstmt.setString(4, usuario.tipoIdentificacion);
            pstmt.setString(5, usuario.usuario);
            pstmt.setString(6, usuario.contrasena);
            pstmt.setString(7, usuario.correo);
            pstmt.setString(8, usuario.cargo);
            pstmt.setString(9, usuario.descripcion);
            pstmt.setString(10, usuario.foto);
            pstmt.setString(11, usuario.data);
            pstmt.setInt(12, usuario.id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al actualizar", e);
            return false;
        }
    }

    private boolean eliminarDB(int id) {
        String sql = "DELETE FROM tb_usuarios WHERE id = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al eliminar", e);
            return false;
        }
    }

    private boolean existeUsuarioDB(String nombreUsuario, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM tb_usuarios WHERE usuario = ?" + (idExcluir > 0 ? " AND id != ?" : "");
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            if (idExcluir > 0) pstmt.setInt(2, idExcluir);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean existeCorreoDB(String correo, int idExcluir) {
        if (correo == null || correo.trim().isEmpty()) return false;
        String sql = "SELECT COUNT(*) FROM tb_usuarios WHERE correo = ?" + (idExcluir > 0 ? " AND id != ?" : "");
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            if (idExcluir > 0) pstmt.setInt(2, idExcluir);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private int contarAdministradoresDB() {
        String sql = "SELECT COUNT(*) FROM tb_usuarios WHERE cargo = 'admin'";
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.id = rs.getInt("id");
        u.nombre = rs.getString("nombre");
        u.apellido = rs.getString("apellido");
        u.identificacion = rs.getLong("identificacion");
        u.tipoIdentificacion = rs.getString("TipoIdentificacion");
        u.usuario = rs.getString("usuario");
        u.contrasena = rs.getString("contrasena");
        u.correo = rs.getString("correo");
        u.cargo = rs.getString("cargo");
        u.descripcion = rs.getString("descripcion");
        u.foto = rs.getString("foto");
        u.data = rs.getString("data");
        return u;
    }

    // ==================== CLASE INTERNA: USUARIO ====================
    
    class Usuario {
        int id;
        String nombre, apellido, usuario, contrasena, correo, cargo;
        long identificacion;
        String tipoIdentificacion, descripcion, foto, data;
        
        Usuario() {}
    }

    // ==================== CLASE INTERNA: DIÁLOGO ====================
    
    class UsuarioDialog extends JDialog {
        boolean guardado = false;
        Usuario usuario;
        JTextField txtNombre, txtApellido, txtIdentificacion, txtUsuario, txtCorreo;
        JPasswordField txtContrasena, txtConfirmar;
        JComboBox<String> cmbTipoId, cmbCargo;
        JTextArea txtDescripcion;
        String rutaFoto = "";

        UsuarioDialog(Frame parent, Usuario usuario) {
            super(parent, usuario == null ? "Agregar Usuario" : "Editar Usuario", true);
            this.usuario = usuario;
            initDialog();
            if (usuario != null) cargarDatos();
            setLocationRelativeTo(parent);
        }

        void initDialog() {
            setSize(600, 650);
            setLayout(new BorderLayout());
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.WHITE);
            
            JLabel titulo = new JLabel(usuario == null ? "Nuevo Usuario" : "Editar Usuario");
            titulo.setFont(new Font("Arial", Font.BOLD, 20));
            panel.add(titulo);
            panel.add(Box.createVerticalStrut(20));
            
            // Campos
            txtNombre = new JTextField(20);
            panel.add(crearCampo("Nombre", txtNombre));
            
            txtApellido = new JTextField(20);
            panel.add(crearCampo("Apellido", txtApellido));
            
            JPanel panelId = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelId.setBackground(Color.WHITE);
            cmbTipoId = new JComboBox<>(new String[]{"CC (cédula de ciudadanía)", "CE (carnet de extranjería)" });
            txtIdentificacion = new JTextField(20);
            
            panelId.add(new JLabel("Tipo ID"));
            panelId.add(cmbTipoId);
            
            panelId.add(new JLabel("Número"));
            panelId.add(txtIdentificacion);
            panel.add(panelId);
            panel.add(Box.createVerticalStrut(5));
            
            txtUsuario = new JTextField(20);
            panel.add(crearCampo("Usuario", txtUsuario));
            
            txtContrasena = new JPasswordField(20);
            panel.add(crearCampo((usuario == null ? "Contraseña" : "Nueva Contraseña"), txtContrasena));
            
            txtConfirmar = new JPasswordField(20);
            panel.add(crearCampo("Confirmar", txtConfirmar));
            
            txtCorreo = new JTextField(20);
            panel.add(crearCampo("Correo", txtCorreo));
            
            cmbCargo = new JComboBox<>(new String[]{"cliente", "admin"});
            panel.add(crearCampo("Cargo", cmbCargo));
            
            panel.add(new JLabel("Descripción"));
            txtDescripcion = new JTextArea(3, 20);
            txtDescripcion.setLineWrap(true);
            JScrollPane scroll = new JScrollPane(txtDescripcion);
            scroll.setMaximumSize(new Dimension(950, 70));
            panel.add(scroll);
            
            JScrollPane scrollPanel = new JScrollPane(panel);
            add(scrollPanel, BorderLayout.CENTER);
            
            // Botones
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.addActionListener(e -> dispose());
            
            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.setBackground(new Color(46, 204, 113));
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.addActionListener(e -> guardar());
            
            panelBotones.add(btnCancelar);
            panelBotones.add(btnGuardar);
            add(panelBotones, BorderLayout.SOUTH);
        }

        JPanel crearCampo(String etiqueta, JComponent comp) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p.setBackground(Color.WHITE);
            JLabel lbl = new JLabel(etiqueta);
            lbl.setPreferredSize(new Dimension(150, 25));
            p.add(lbl);
            if (comp instanceof JTextField || comp instanceof JPasswordField) {
                comp.setPreferredSize(new Dimension(280, 25));
            } else if (comp instanceof JComboBox) {
                comp.setPreferredSize(new Dimension(280, 25));
            }
            p.add(comp);
            p.setMaximumSize(new Dimension(450, 35));
            return p;
        }

        void cargarDatos() {
            txtNombre.setText(usuario.nombre);
            txtApellido.setText(usuario.apellido);
            txtIdentificacion.setText(String.valueOf(usuario.identificacion));
            if (usuario.tipoIdentificacion != null) cmbTipoId.setSelectedItem(usuario.tipoIdentificacion);
            txtUsuario.setText(usuario.usuario);
            txtCorreo.setText(usuario.correo);
            cmbCargo.setSelectedItem(usuario.cargo);
            txtDescripcion.setText(usuario.descripcion);
        }

        void guardar() {
            if (!validar()) return;
            
            if (usuario == null) usuario = new Usuario();
            
            usuario.nombre = txtNombre.getText().trim();
            usuario.apellido = txtApellido.getText().trim();
            usuario.identificacion = Long.parseLong(txtIdentificacion.getText().trim());
            usuario.tipoIdentificacion = cmbTipoId.getSelectedItem().toString();
            usuario.usuario = txtUsuario.getText().trim();
            
            String nuevaPass = new String(txtContrasena.getPassword()).trim();
            if (!nuevaPass.isEmpty()) usuario.contrasena = nuevaPass;
            
            usuario.correo = txtCorreo.getText().trim();
            usuario.cargo = cmbCargo.getSelectedItem().toString();
            usuario.descripcion = txtDescripcion.getText().trim();
            usuario.foto = rutaFoto;
            
            boolean exito = usuario.id == 0 ? insertarDB(usuario) : actualizarDB(usuario);
            
            if (exito) {
                guardado = true;
                JOptionPane.showMessageDialog(this, "Usuario guardado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        boolean validar() {
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (txtApellido.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El apellido es obligatorio", "Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (txtIdentificacion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La identificación es obligatoria", "Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            try {
                Long.parseLong(txtIdentificacion.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "La identificación debe ser numérica", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (txtUsuario.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El usuario es obligatorio", "Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            String user = txtUsuario.getText().trim();
            if (existeUsuarioDB(user, usuario != null ? usuario.id : 0)) {
                JOptionPane.showMessageDialog(this, "El usuario ya existe", "Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            String correo = txtCorreo.getText().trim();
            if (!correo.isEmpty()) {
                if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    JOptionPane.showMessageDialog(this, "Formato de correo inválido", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (existeCorreoDB(correo, usuario != null ? usuario.id : 0)) {
                    JOptionPane.showMessageDialog(this, "El correo ya existe", "Error", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
            
            String pass = new String(txtContrasena.getPassword()).trim();
            String conf = new String(txtConfirmar.getPassword()).trim();
            
            if (usuario == null && pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La contraseña es obligatoria", "Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (!pass.isEmpty()) {
                if (pass.length() < 6) {
                    JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres", "Error", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                if (!pass.equals(conf)) {
                    JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            return true;
        }
    }

    // ==================== MAIN ====================
    
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
        java.awt.EventQueue.invokeLater(() -> new Usuarios().setVisible(true));
    }

    // Variables declaration
    private javax.swing.JLabel AdministradorOtrosButton;
    private javax.swing.JLabel AdministradorPermisosButton;
    private javax.swing.JLabel InterfazCategoriasButton;
    private javax.swing.JLabel InterfazOfertasButton;
    private javax.swing.JLabel InterfazPrincipalButton;
    private javax.swing.JLabel ProductosActualizarButton;
    private javax.swing.JLabel ProductosAgregarButton;
    private javax.swing.JLabel ProductosEliminarButton;
    private javax.swing.JLabel UsuariosActualizarButton;
    private javax.swing.JLabel UsuariosAgregarButton;
    private javax.swing.JLabel UsuariosEliminarButton;
    private javax.swing.JButton jBuscarButton;
    private javax.swing.JTextField jBuscarTextFiel;
    private javax.swing.JLabel jLabel11;
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
}