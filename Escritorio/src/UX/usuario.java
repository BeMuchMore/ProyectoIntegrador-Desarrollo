
package UX;

import UI.registro;
import UI.SessionManager;
import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;


import javax.swing.JOptionPane;

public class Usuario extends javax.swing.JFrame {
     
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Usuario.class.getName());

   
    public Usuario(int userId, String cargo) {
        
        initComponents();
        
        setResizable(false);
        setLocationRelativeTo(null);
  
        SessionManager session = SessionManager.getInstance();
        this.setTitle("Usuario: " + session.getUsername());
        
        // Log de acceso
        System.out.println("Usuario " + session.getUsername() + " accedió a Agregar Productos");
        
        cargarDatosUsuario();
 
    }
    
     private void cargarDatosUsuario() {
            SessionManager session = SessionManager.getInstance();
            
             if (!session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Sesión no válida");
            return;
        }
         
          int userId = session.getUserId();
        String cargo = session.getCargo();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            // Aquí debes configurar tu conexión a la base de datos
            // Ejemplo genérico - ajusta según tu configuración
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/basedatos", "root", "");
            
            String sql = "SELECT identificacion, nombre, apellido, usuario, contrasena, correo, foto ,descripcion FROM usuariouiux WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Llenar los campos con los datos del usuario
                JNombreText.setText(rs.getString("nombre"));
                jApellidosText.setText(rs.getString("apellido"));
                jUsuarioText.setText(rs.getString("usuario"));
                jCorreoText.setText(rs.getString("correo"));
                jContrasenaText.setText(rs.getString("contrasena"));
                jidentificacionText.setText(rs.getString("identificacion"));
                jDescripcionText.setText(rs.getString("descripcion"));
                
                // Mostrar el cargo actual
                jCargoLabel.setText(cargo);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron datos para el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            logger.severe("Error al cargar datos del usuario: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al cargar los datos: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cerrar conexiones
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                logger.warning("Error al cerrar conexiones: " + e.getMessage());
            }
        }
    }
    

    private void actualizarDatosUsuario() {
         SessionManager session = SessionManager.getInstance();
            
             if (!session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Sesión no válida");
            return;
        }
         
          int userId = session.getUserId();
        String cargo = session.getCargo();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            // Validar que los campos obligatorios no estén vacíos
            if (JNombreText.getText().trim().isEmpty() || 
                jApellidosText.getText().trim().isEmpty() || 
                jUsuarioText.getText().trim().isEmpty() ||
                jCorreoText.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/basedatos", "root", "");
            
            String sql = "UPDATE usuariouiux SET nombre=?, apellido=?, usuario=?, correo=?, contrasena=?, identificacion=?, descripcion=? WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, JNombreText.getText().trim());
            pstmt.setString(2, jApellidosText.getText().trim());
            pstmt.setString(3, jUsuarioText.getText().trim());
            pstmt.setString(4, jCorreoText.getText().trim());
            pstmt.setString(5, jContrasenaText.getText().trim());
            pstmt.setString(6, jidentificacionText.getText().trim());
            pstmt.setString(7, jDescripcionText.getText().trim());
            pstmt.setInt(8, userId);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la información.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            logger.severe("Error al actualizar datos del usuario: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al actualizar los datos: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                logger.warning("Error al cerrar conexiones: " + e.getMessage());
            }
        }
    }

  private void eliminarUsuarioActual() {
       SessionManager session = SessionManager.getInstance();
            
             if (!session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Sesión no válida");
            return;
        }
         
          int userId = session.getUserId();
        String cargo = session.getCargo();
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de que desea eliminar su cuenta? Esta acción no se puede deshacer.", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/basedatos", "root", "");
                
                String sql = "DELETE FROM usuariouiux WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, userId);
                
                int filasAfectadas = pstmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Cuenta eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    // Regresar al login o cerrar la aplicación
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (SQLException e) {
                logger.severe("Error al eliminar usuario: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error al eliminar la cuenta: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    logger.warning("Error al cerrar conexiones: " + e.getMessage());
                }
            }
        }
    }
   
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jUsuarioButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jCargoLabel = new javax.swing.JLabel();
        JNombreText = new javax.swing.JTextField();
        jApellidosText = new javax.swing.JTextField();
        jUsuarioText = new javax.swing.JTextField();
        jCorreoText = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jContrasenaText = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jDescripcionText = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jidentificacionText = new javax.swing.JTextField();
        jActualizarUsuarioButton1 = new javax.swing.JButton();
        jEliminarUsuarioButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jAgregarButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jEliminarButton = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jActualizarButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLeerButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(1067, 536));
        jPanel1.setMinimumSize(new java.awt.Dimension(1067, 536));

        jUsuarioButton.setText("Usuario");
        jUsuarioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUsuarioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jUsuarioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jUsuarioButton, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMaximumSize(new java.awt.Dimension(949, 391));
        jPanel4.setMinimumSize(new java.awt.Dimension(949, 391));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Nombres:");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Usuario:");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Correo:");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Cargo actual:");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Contraseña:");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel7.setText("Cuenta con permisos");

        jCargoLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jCargoLabel.setText("--------------------------------------------------");

        JNombreText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JNombreTextActionPerformed(evt);
            }
        });

        jApellidosText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jApellidosTextActionPerformed(evt);
            }
        });

        jUsuarioText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUsuarioTextActionPerformed(evt);
            }
        });

        jCorreoText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCorreoTextActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setText("Foto:");

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setText("Descripcion:");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Apellidos:");

        jContrasenaText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jContrasenaTextActionPerformed(evt);
            }
        });

        jDescripcionText.setColumns(20);
        jDescripcionText.setRows(5);
        jDescripcionText.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jDescripcionTextAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane1.setViewportView(jDescripcionText);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setText("Identificacion:");

        jidentificacionText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jidentificacionTextActionPerformed(evt);
            }
        });

        jActualizarUsuarioButton1.setText("Actualizar");
        jActualizarUsuarioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jActualizarUsuarioButton1ActionPerformed(evt);
            }
        });

        jEliminarUsuarioButton.setText("Elminar");
        jEliminarUsuarioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEliminarUsuarioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(jLabel7))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                                .addComponent(jCargoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6))
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(73, 73, 73)))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jActualizarUsuarioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jEliminarUsuarioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jContrasenaText, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                                    .addComponent(JNombreText)
                                    .addComponent(jApellidosText)
                                    .addComponent(jUsuarioText)
                                    .addComponent(jCorreoText)
                                    .addComponent(jidentificacionText))))
                        .addGap(71, 71, 71)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel7)
                .addGap(61, 61, 61)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(23, 23, 23)
                        .addComponent(jLabel1)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel3)
                        .addGap(23, 23, 23)
                        .addComponent(jLabel4))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JNombreText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addComponent(jApellidosText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jUsuarioText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCorreoText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel5)
                                .addGap(15, 15, 15))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jCargoLabel)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jContrasenaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jidentificacionText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jActualizarUsuarioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jEliminarUsuarioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34))))
        );

        jPanel5.setMaximumSize(new java.awt.Dimension(100, 100));

        jAgregarButton.setText("Agregar");
        jAgregarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAgregarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jAgregarButton, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jAgregarButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jPanel6.setMaximumSize(new java.awt.Dimension(100, 100));

        jEliminarButton.setText("Eliminar");
        jEliminarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEliminarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jEliminarButton, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jEliminarButton, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jPanel7.setMaximumSize(new java.awt.Dimension(100, 100));

        jActualizarButton.setText("Actualizar");
        jActualizarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jActualizarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jActualizarButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jActualizarButton, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jPanel8.setMaximumSize(new java.awt.Dimension(100, 100));

        jLeerButton.setText("Leer");
        jLeerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLeerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLeerButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLeerButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jAgregarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAgregarButtonActionPerformed
        // TODO add your handling code here:
        Agregar temp = new Agregar();
        temp.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jAgregarButtonActionPerformed

    private void jEliminarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEliminarButtonActionPerformed
        // TODO add your handling code here:
        Eliminar temp = new Eliminar();
        temp.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jEliminarButtonActionPerformed

    private void jLeerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLeerButtonActionPerformed
        // TODO add your handling code here:
        Leer temp = new Leer();
        temp.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jLeerButtonActionPerformed

    private void jActualizarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jActualizarButtonActionPerformed
        // TODO add your handling code here:
        Actualizar temp = new Actualizar();
        temp.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jActualizarButtonActionPerformed

    
    
    
    private void JNombreTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JNombreTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JNombreTextActionPerformed

    private void jContrasenaTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jContrasenaTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jContrasenaTextActionPerformed

    private void jApellidosTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jApellidosTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jApellidosTextActionPerformed

    private void jUsuarioTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUsuarioTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jUsuarioTextActionPerformed

    private void jCorreoTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCorreoTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCorreoTextActionPerformed

    private void jidentificacionTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jidentificacionTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jidentificacionTextActionPerformed

    private void jDescripcionTextAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jDescripcionTextAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jDescripcionTextAncestorAdded

    private void jUsuarioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUsuarioButtonActionPerformed
        // TODO add your handling code here:
//        Usuario temp = new Usuario();
//        temp.setVisible(true);
//        this.setVisible(false);
    }//GEN-LAST:event_jUsuarioButtonActionPerformed

    private void jActualizarUsuarioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jActualizarUsuarioButton1ActionPerformed
        // TODO add your handling code here:
        actualizarDatosUsuario();
    }//GEN-LAST:event_jActualizarUsuarioButton1ActionPerformed

    private void jEliminarUsuarioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEliminarUsuarioButtonActionPerformed
        // TODO add your handling code here:
        eliminarUsuarioActual();

    }//GEN-LAST:event_jEliminarUsuarioButtonActionPerformed

    /**
     * @param args the command line arguments
     */
   public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(() -> {
        new Usuario(1, "admin").setVisible(true); // Valores temporales
    });}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField JNombreText;
    private javax.swing.JButton jActualizarButton;
    private javax.swing.JButton jActualizarUsuarioButton1;
    private javax.swing.JButton jAgregarButton;
    private javax.swing.JTextField jApellidosText;
    private javax.swing.JLabel jCargoLabel;
    private javax.swing.JTextField jContrasenaText;
    private javax.swing.JTextField jCorreoText;
    private javax.swing.JTextArea jDescripcionText;
    private javax.swing.JButton jEliminarButton;
    private javax.swing.JButton jEliminarUsuarioButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton jLeerButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jUsuarioButton;
    private javax.swing.JTextField jUsuarioText;
    private javax.swing.JTextField jidentificacionText;
    // End of variables declaration//GEN-END:variables

    
}
