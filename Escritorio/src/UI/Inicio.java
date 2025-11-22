
package UI;


import UI.Conexion;
import UI.SessionManager;
import UI.util.ModernStyles;
import UX.Usuario;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

    
public class Inicio extends javax.swing.JFrame {

    public Inicio() {
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        aplicarEstilosModernos();
        cargarComplementos();
    }
    
    /**
     * Aplica estilos modernos a todos los componentes (diseño web)
     */
    private void aplicarEstilosModernos() {
        // Panel principal con gradiente rosado (como body de la web)
        jPanel1.setBackground(ModernStyles.BG_LIGHT);
        
        // Panel superior (header) con gradiente rosado (como header-top de la web)
        jPanel2.setBackground(ModernStyles.PRIMARY_COLOR);
        jPanel2.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Nombre de la app con tipografía mejorada (color blanco en header)
        NombreApp.setFont(ModernStyles.getHeading2Font());
        NombreApp.setForeground(ModernStyles.TEXT_LIGHT);
        ModernStyles.enableTextAntialiasing(NombreApp);
        
        // Panel de formulario (auth-card estilo web: blanco con sombra y bordes redondeados)
        jPanel3.setBackground(ModernStyles.BG_WHITE);
        jPanel3.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 229, 233), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        // Título con tipografía mejorada (gradiente morado como auth-header de la web)
        jLabel6.setFont(ModernStyles.getHeading2Font());
        jLabel6.setForeground(ModernStyles.INFO_COLOR); // Morado azulado como web
        ModernStyles.enableTextAntialiasing(jLabel6);
        
        // Labels de campos con tipografía mejorada
        jLabel1.setFont(ModernStyles.getSemiboldFont(13));
        jLabel1.setForeground(ModernStyles.TEXT_PRIMARY);
        ModernStyles.enableTextAntialiasing(jLabel1);
        
        jLabel2.setFont(ModernStyles.getSemiboldFont(13));
        jLabel2.setForeground(ModernStyles.TEXT_PRIMARY);
        ModernStyles.enableTextAntialiasing(jLabel2);
        
        // Campos de texto
        ModernStyles.styleTextField(txtCorreo);
        ModernStyles.stylePasswordField(textContrasena);
        
        // Botón ingresar (estilo auth-button morado azulado como web)
        ModernStyles.styleAuthButton(btnIngresar);
        btnIngresar.setPreferredSize(new java.awt.Dimension(240, 45));
        
        // Labels de enlaces con tipografía mejorada (color morado azulado como web)
        irRegistro.setFont(ModernStyles.getSemiboldFont(13));
        irRegistro.setForeground(ModernStyles.INFO_COLOR);
        ModernStyles.enableTextAntialiasing(irRegistro);
        
        VerificacionCorreo.setFont(ModernStyles.getSemiboldFont(13));
        VerificacionCorreo.setForeground(ModernStyles.INFO_COLOR);
        ModernStyles.enableTextAntialiasing(VerificacionCorreo);
        
        jLabel13.setFont(ModernStyles.getBodyFont());
        jLabel13.setForeground(ModernStyles.TEXT_SECONDARY);
        ModernStyles.enableTextAntialiasing(jLabel13);
        
        jLabel14.setFont(ModernStyles.getBodySmallFont());
        jLabel14.setForeground(ModernStyles.TEXT_SECONDARY);
        ModernStyles.enableTextAntialiasing(jLabel14);
        
        jLabel3.setFont(ModernStyles.getBodySmallFont());
        jLabel3.setForeground(ModernStyles.TEXT_SECONDARY);
        ModernStyles.enableTextAntialiasing(jLabel3);
        
        // Panel footer con fondo oscuro (como footer de la web #0a0a0a)
        jPanel5.setBackground(ModernStyles.BG_DARK);
        jPanel5.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        NombreApp2.setFont(ModernStyles.getHeading4Font());
        NombreApp2.setForeground(ModernStyles.TEXT_LIGHT);
        ModernStyles.enableTextAntialiasing(NombreApp2);
        
        CorreoApp.setFont(ModernStyles.getBodySmallFont());
        CorreoApp.setForeground(new Color(200, 200, 200));
        ModernStyles.enableTextAntialiasing(CorreoApp);
        
        jLabel8.setFont(ModernStyles.getCaptionFont());
        jLabel8.setForeground(new Color(150, 150, 150));
        ModernStyles.enableTextAntialiasing(jLabel8);
        
        // Centrar ventana
        setLocationRelativeTo(null);
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
                
                if (nombre != null && !nombre.isEmpty()) {
                    NombreApp.setText(nombre);
                    NombreApp2.setText(nombre);
                }
                if (correo != null && !correo.isEmpty()) {
                    CorreoApp.setText(correo);
                }
            }
        } catch (SQLException e) {
            // Usar valores por defecto si hay error
            NombreApp.setText("FASHION");
            NombreApp2.setText("FASHION");
            CorreoApp.setText("contacto@fashion.com");
        }
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        NombreApp = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtCorreo = new javax.swing.JTextField();
        textContrasena = new javax.swing.JPasswordField();
        btnIngresar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        irRegistro = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        VerificacionCorreo = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        NombreApp2 = new javax.swing.JLabel();
        CorreoApp = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(ModernStyles.BG_LIGHT);
        jPanel1.setPreferredSize(new java.awt.Dimension(790, 550));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NombreApp.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        NombreApp.setText("-");
        jPanel2.add(NombreApp, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 660, 40));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtCorreo.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoActionPerformed(evt);
            }
        });
        jPanel3.add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 306, 30));

        textContrasena.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        textContrasena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textContrasenaActionPerformed(evt);
            }
        });
        jPanel3.add(textContrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 306, 30));

        btnIngresar.setFont(ModernStyles.getBoldFont(16));
        btnIngresar.setText("INGRESAR");
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        jPanel3.add(btnIngresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 310, 240, 40));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Usuario");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Contraseña");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel6.setText("Inicia sesión para comprar");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, -1, 20));

        irRegistro.setText("Registrate");
          irRegistro.setForeground(Color.BLUE);
           irRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        irRegistro.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
                
                   UI.registro Registro = new UI.registro();
                    Registro.setVisible(true);
                                  cerrar();
            }
        });
        jPanel3.add(irRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 360, -1, -1));

        jLabel13.setText("¿Aún no tienes cuenta? ");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 360, 130, -1));

        jLabel14.setText("¿Olvidaste tu contraseña o Usuario? No te preocupes, ");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 240, -1, 20));

        jLabel3.setText("pide un código verificador por ");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 260, -1, -1));

        VerificacionCorreo.setText("correo");
              VerificacionCorreo.setForeground(Color.BLUE);
          VerificacionCorreo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        VerificacionCorreo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                  UI.Recuperacion recuperacion = new UI.Recuperacion();
                    recuperacion.setVisible(true);
                           cerrar();

            }
        });
        jPanel3.add(VerificacionCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 260, -1, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 45, 660, 440));

        jPanel5.setBackground(new Color(33, 37, 41));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NombreApp2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        NombreApp2.setText("INVOICE DAY");
        jPanel5.add(NombreApp2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 157, -1));

        CorreoApp.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        CorreoApp.setText("INVOICEDAY@CORREOUNIVALLE.EDU.CO");
        jPanel5.add(CorreoApp, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 253, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setText("COPYRIGHT (C) 2025");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        // Iconos de redes sociales - cargar desde recursos si existen, sino ocultar
        try {
            jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/insta.png")));
        } catch (Exception e) {
            jLabel9.setVisible(false);
        }
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, -1));

        try {
            jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/face.png")));
        } catch (Exception e) {
            jLabel10.setVisible(false);
        }
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 10, -1, -1));

        try {
            jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/twiter.png")));
        } catch (Exception e) {
            jLabel11.setVisible(false);
        }
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, -1, -1));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 660, 70));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

      private void cerrar(){
    this.dispose();
    }
    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {                                          
        
    }                                         

    private void textContrasenaActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {                                            

     diagnosticarConexion(); 
        System.out.println("=== INICIO DEL LOGIN ===");
    
        String usuario = txtCorreo.getText().trim();
        String contrasena = new String(textContrasena.getPassword()).trim();
    
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos");
            return;
        }
    
        try (Connection conn = Conexion.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "No se pudo establecer conexión con la base de datos");
                return;
            }

            String sql = "SELECT id, cargo, nombre FROM tb_usuarios WHERE usuario=? AND contrasena=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String cargo = rs.getString("cargo");
                String nombre = rs.getString("nombre");

                SessionManager.getInstance().login(userId, usuario, cargo);
                JOptionPane.showMessageDialog(this, "Bienvenido " + nombre);

                // Redirección según tipo de usuario
                if ("admin".equalsIgnoreCase(cargo)) {
                    UI.principal ventanaAdmin = new UI.principal(userId);
                    ventanaAdmin.setVisible(true);
                    
                   UX.Usuario usuarioAdmin = new UX.Usuario(userId, usuario);
                    usuarioAdmin.setVisible(true);
                } else {
                    UI.principal ventanaCliente = new UI.principal(userId);
                    ventanaCliente.setVisible(true);
                }

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage());
        }

        System.out.println("=== FIN DEL LOGIN ===");
   
    }                                           

   private void diagnosticarConexion() {
        String diagnostico = Conexion.diagnosticarConexion();
        System.out.println(diagnostico);
        
        // Si hay error, también mostrar en un diálogo
        if (diagnostico.contains("✗") || diagnostico.contains("ERROR")) {
            javax.swing.JOptionPane.showMessageDialog(
                this, 
                diagnostico.replace("===", "").replace("===", ""), 
                "Error de Conexión a la Base de Datos", 
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }



    /**
     * @param args the command line arguments
     */
public static void main(String args[]) {
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
        // Usar directamente Logger.getLogger en lugar del campo estático
       
    }

    java.awt.EventQueue.invokeLater(() -> {
        new Inicio().setVisible(true);
    });
}


    // Variables declaration - do not modify                     
    private javax.swing.JLabel CorreoApp;
    private javax.swing.JLabel NombreApp;
    private javax.swing.JLabel NombreApp2;
    private javax.swing.JLabel VerificacionCorreo;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JLabel irRegistro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPasswordField textContrasena;
    private javax.swing.JTextField txtCorreo;
    // End of variables declaration                   

  
}
