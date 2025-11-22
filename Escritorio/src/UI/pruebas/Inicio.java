
package UI.pruebas;


import UI.Conexion;
import UI.SessionManager;
import UX.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.swing.JOptionPane;

    
public class Inicio extends javax.swing.JFrame {

    public Inicio() {
        initComponents();
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        NombreApp = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtCorreo = new javax.swing.JTextField();
        textContrasena = new javax.swing.JTextField();
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

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setForeground(new java.awt.Color(102, 102, 255));
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

        btnIngresar.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(0, 0, 153));
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
        jPanel3.add(irRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 360, -1, -1));

        jLabel13.setText("¿Aún no tienes cuenta? ");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 360, 130, -1));

        jLabel14.setText("¿Olvidaste tu contraseña o Usuario? No te preocupes, ");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 240, -1, 20));

        jLabel3.setText("pide un código verificador por ");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 260, -1, -1));

        VerificacionCorreo.setText("correo");
        jPanel3.add(VerificacionCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 260, -1, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 45, 660, 440));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
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

        jLabel9.setIcon(new javax.swing.ImageIcon("C:\\Users\\calam\\OneDrive\\Pictures\\insta.png")); // NOI18N
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon("C:\\Users\\calam\\OneDrive\\Pictures\\face.png")); // NOI18N
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 10, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon("C:\\Users\\calam\\OneDrive\\Pictures\\twiter.png")); // NOI18N
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
    }// </editor-fold>//GEN-END:initComponents

    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoActionPerformed
        
    }//GEN-LAST:event_txtCorreoActionPerformed

    private void textContrasenaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textContrasenaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textContrasenaActionPerformed

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed

     diagnosticarConexion(); 
        System.out.println("=== INICIO DEL LOGIN ===");
    
        String usuario = txtCorreo.getText().trim();
        String contrasena = textContrasena.getText().trim();
    
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
   
    }//GEN-LAST:event_btnIngresarActionPerformed

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JTextField textContrasena;
    private javax.swing.JTextField txtCorreo;
    // End of variables declaration//GEN-END:variables

  
}
