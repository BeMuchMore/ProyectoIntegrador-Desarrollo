/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;

import UI.Conexion;
import UI.principal;
import UI.registro;
import java.sql.Connection;
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
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtUsuario = new javax.swing.JTextField();
        textContrasena = new javax.swing.JTextField();
        btnIngresar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnRegistrarse = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setForeground(new java.awt.Color(102, 102, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(790, 550));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setIcon(new javax.swing.ImageIcon("C:\\Users\\calam\\OneDrive\\Pictures\\istockphoto-1410493250-612x612.jpg")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel3)
                .addContainerGap(626, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        txtUsuario.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });

        textContrasena.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        textContrasena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textContrasenaActionPerformed(evt);
            }
        });

        btnIngresar.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(0, 0, 153));
        btnIngresar.setText("INGRESAR");
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setText("USUARIO:");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setText("CONTRASEÑA:");

        btnRegistrarse.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnRegistrarse.setForeground(new java.awt.Color(0, 0, 153));
        btnRegistrarse.setText("REGISTRARSE");
        btnRegistrarse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(btnRegistrarse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(textContrasena)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                    .addComponent(btnIngresar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnIngresar, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(btnRegistrarse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 173, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 303, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel5.setText("INVOICE DAY");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("JULIAN DAVID CALAMBAS CHILO");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText("INVOICEDAY@CORREOUNIVALLE.EDU.CO");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setText("COPYRIGHT (C) 2025");

        jLabel9.setIcon(new javax.swing.ImageIcon("C:\\Users\\calam\\OneDrive\\Pictures\\insta.png")); // NOI18N

        jLabel10.setIcon(new javax.swing.ImageIcon("C:\\Users\\calam\\OneDrive\\Pictures\\face.png")); // NOI18N

        jLabel11.setIcon(new javax.swing.ImageIcon("C:\\Users\\calam\\OneDrive\\Pictures\\twiter.png")); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 286, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void textContrasenaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textContrasenaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textContrasenaActionPerformed

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed

    diagnosticarConexion(); 
    System.out.println("=== INICIO DEL LOGIN ===");
    
    String usuario = txtUsuario.getText();
    String contrasena = textContrasena.getText();
    
    System.out.println("Usuario: " + usuario);
    System.out.println("Contraseña: " + (contrasena.isEmpty() ? "vacía" : "tiene contenido"));
    
    // Verificar si los campos están vacíos
    if (usuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos");
        return;
    }
    
    try {
        System.out.println("Intentando obtener conexión...");
        java.sql.Connection conn = Conexion.getConnection();
        
        if (conn == null) {
            System.out.println("ERROR: Conexión es null");
            JOptionPane.showMessageDialog(this, "No se pudo establecer conexión con la base de datos");
            return;
        }
        
        System.out.println("Conexión establecida exitosamente");
        
        String sql = "SELECT id, cargo FROM usuariouiux WHERE usuario=? AND contrasena=?";
        System.out.println("Preparando consulta: " + sql);
        
        java.sql.PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, usuario);
        ps.setString(2, contrasena);
        
        System.out.println("Ejecutando consulta...");
        java.sql.ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            System.out.println("Usuario encontrado en la base de datos");
            int userId = rs.getInt("id");
            String cargo = rs.getString("cargo");
            
            System.out.println("UserID: " + userId + ", Cargo: " + cargo);
            
            // *** NUEVA FUNCIONALIDAD: Iniciar sesión ***
            SessionManager.getInstance().login(userId, usuario, cargo);
            
            JOptionPane.showMessageDialog(this, "Bienvenido " + usuario);
            
            if (cargo != null && !cargo.isEmpty()) {
                System.out.println("Abriendo ventana de usuario...");
                UX.Usuario ventanaUsr = new UX.Usuario(userId, cargo);
                ventanaUsr.setVisible(true);
            } else {
                System.out.println("Abriendo ventana principal...");
                principal ventanaCliente = new principal(userId);
                ventanaCliente.setVisible(true);
            }
            
            this.dispose();
        } else {
            System.out.println("Usuario no encontrado");
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
        }
        
        // Cerrar recursos
        rs.close();
        ps.close();
        conn.close();
        
    } catch (Exception e) {
        System.out.println("ERROR EN LOGIN: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
    }
    
    System.out.println("=== FIN DEL LOGIN ===");
   
    }//GEN-LAST:event_btnIngresarActionPerformed
private void diagnosticarConexion() {
    System.out.println("=== DIAGNÓSTICO DE CONEXIÓN ===");
    
    // 1. Verificar driver
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("✓ Driver MySQL encontrado");
    } catch (ClassNotFoundException e) {
        System.out.println("✗ Driver MySQL NO encontrado");
        System.out.println("  Necesitas agregar mysql-connector-java.jar al proyecto");
        JOptionPane.showMessageDialog(this, "Driver MySQL no encontrado. Agrega mysql-connector-java.jar");
        return;
    }
    
    // 2. Probar conexión básica
    try {
        System.out.println("Intentando conectar a MySQL...");
        Connection conn = Conexion.getConnection();
        
        if (conn != null) {
            System.out.println("✓ Conexión exitosa");
            
            // 3. Verificar base de datos
            System.out.println("Verificando base de datos...");
            java.sql.Statement stmt = conn.createStatement();
            
            // Listar bases de datos
            java.sql.ResultSet rs1 = stmt.executeQuery("SHOW DATABASES");
            System.out.println("Bases de datos disponibles:");
            while (rs1.next()) {
                System.out.println("  - " + rs1.getString(1));
            }
            rs1.close();
            
            // 4. Verificar tabla usuariouiux
            try {
                java.sql.ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM usuariouiux");
                if (rs2.next()) {
                    int count = rs2.getInt(1);
                    System.out.println("✓ Tabla usuariouiux existe con " + count + " registros");
                }
                rs2.close();
            } catch (SQLException e) {
                System.out.println("✗ Tabla usuariouiux no existe o error: " + e.getMessage());
            }
            
            // 5. Verificar estructura de la tabla
            try {
                java.sql.ResultSet rs3 = stmt.executeQuery("DESCRIBE usuariouiux");
                System.out.println("Estructura de la tabla usuariouiux:");
                while (rs3.next()) {
                    System.out.println("  " + rs3.getString("Field") + " - " + rs3.getString("Type"));
                }
                rs3.close();
            } catch (SQLException e) {
                System.out.println("No se pudo obtener estructura de la tabla: " + e.getMessage());
            }
            
            stmt.close();
            conn.close();
            
          
            
        } else {
            System.out.println("✗ No se pudo establecer conexión");
            JOptionPane.showMessageDialog(this, "No se pudo conectar a MySQL");
        }
        
    } catch (SQLException e) {
        System.out.println("✗ Error SQL: " + e.getMessage());
        System.out.println("  Código: " + e.getErrorCode());
        
        String mensaje = "";
        switch (e.getErrorCode()) {
            case 1045:
                mensaje = "Usuario/contraseña incorrectos para MySQL";
                break;
            case 1049:
                mensaje = "La base de datos 'basedatos' no existe";
                break;
            case 0:
                if (e.getMessage().contains("Connection refused")) {
                    mensaje = "MySQL server no está ejecutándose";
                } else {
                    mensaje = "Error de conexión: " + e.getMessage();
                }
                break;
            default:
                mensaje = "Error SQL: " + e.getMessage();
        }
        
        System.out.println("  " + mensaje);
        JOptionPane.showMessageDialog(this, mensaje);
    }
}



    private void btnRegistrarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarseActionPerformed
        // TODO add 
        registro temp = new registro();
        temp.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnRegistrarseActionPerformed

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
    private javax.swing.JButton btnIngresar;
    private javax.swing.JButton btnRegistrarse;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField textContrasena;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
