/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UX;

import UI.Coneccion.dbConnection;
import UI.Conexion;
import UI.SessionManager;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 * 
 * 
 *
 * @author calam
 */
public class Leer extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Leer.class.getName());

    
    // Variables de conexión a base de datos
     private boolean vistaActual = true; 
    /**
    /**
     * Creates new form Leer
     */
    public Leer() {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
  
        cargarProductosActuales();
        
           SessionManager session = SessionManager.getInstance();
        this.setTitle("Listado Productos - Usuario: " + session.getUsername());
        
        // Log de acceso
        System.out.println("Usuario " + session.getUsername() + " accedió a Agregar Productos");
    }

    
    private Connection getConnection() throws SQLException {
        Connection conn = dbConnection.conectar();
        if (conn == null) {
            throw new SQLException("No se pudo establecer conexión con la base de datos");
        }
        return conn;
    }
    
    /**
     * Método para cargar productos actuales en la tabla
     */
    private void cargarProductosActuales() {
        String query = "SELECT `id`, `Nombre`, `ImgUrl`, `Cantidad`, `Descripcion`, " +
                      "`Precio`, `Categorias`, `Color`, `FecheIngreso`, `Talla`, " +
                      "`Material`, `Colección` FROM `tb_productos`";
        
        // Definir los nombres de las columnas para productos actuales
        String[] columnNames = {
            "ID", "Nombre", "URL Imagen", "Cantidad", "Descripción", 
            "Precio", "Categorías", "Color", "Fecha Ingreso", "Talla", 
            "Material", "Colección"
        };
        
        cargarDatosEnTabla(query, columnNames);
    }
    
    /**
     * Método para cargar historial de cambios en la tabla
     */
    private void cargarHistorialCambios() {
        String query = "SELECT `id`, `producto_id`, `nombre_anterior`, `cantidad_anterior`, " +
                      "`precio_anterior`, `categorias_anterior`, `talla_anterior`, " +
                      "`color_anterior`, `material_anterior`, `coleccion_anterior`, " +
                      "`descripcion_anterior`, `fecha_cambio` FROM `tb_productos_historial`";
        
        // Definir los nombres de las columnas para el historial
        String[] columnNames = {
            "ID", "ID Producto", "Nombre Anterior", "Cantidad Anterior", 
            "Precio Anterior", "Categorías Anteriores", "Talla Anterior", 
            "Color Anterior", "Material Anterior", "Colección Anterior", 
            "Descripción Anterior", "Fecha de Cambio"
        };
        
        cargarDatosEnTabla(query, columnNames);
    }
    
    /**
     * Método genérico para cargar datos en la tabla
     */
    private void cargarDatosEnTabla(String query, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel();
        
        // Establecer los nombres de las columnas
        model.setColumnIdentifiers(columnNames);
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            // Obtener metadatos para saber el número de columnas
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Limpiar cualquier dato existente
            model.setRowCount(0);
            
            // Iterar sobre los resultados y agregar cada fila
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
            
            // Establecer el modelo en la tabla
            jProductosTable.setModel(model);
            
            // Ajustar el ancho de las columnas (opcional)
            ajustarAnchoColumnas();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar los datos: " + e.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar datos", e);
        }
    }
    
    /**
     * Método para ajustar el ancho de las columnas automáticamente
     */
    private void ajustarAnchoColumnas() {
        // Puedes personalizar los anchos según tus necesidades
        if (jProductosTable.getColumnCount() > 0) {
            jProductosTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
            jProductosTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
            
            // Ajusta más columnas según necesites
            for (int i = 2; i < jProductosTable.getColumnCount(); i++) {
                jProductosTable.getColumnModel().getColumn(i).setPreferredWidth(100);
            }
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jUsuarioButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jActualesButton = new javax.swing.JButton();
        jCambiosButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jProductosTable = new javax.swing.JTable();
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

        jLabel11.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel11.setText("Todos Los Productos actualmente y anteriormente.");

        jActualesButton.setText("Actuales");
        jActualesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jActualesButtonActionPerformed(evt);
            }
        });

        jCambiosButton.setText("Cambios");
        jCambiosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCambiosButtonActionPerformed(evt);
            }
        });

        jProductosTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12"
            }
        ));
        jScrollPane2.setViewportView(jProductosTable);

        jScrollPane1.setViewportView(jScrollPane2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(43, 43, 43)
                        .addComponent(jActualesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(jCambiosButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jActualesButton)
                        .addComponent(jCambiosButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
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
         
        if (!SessionManager.getInstance().isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Sesión no válida");
            return;
        }
        
            Agregar temp = new Agregar();
            temp.setVisible(true);
            this.setVisible(false);
    }//GEN-LAST:event_jAgregarButtonActionPerformed

    private void jEliminarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEliminarButtonActionPerformed
        // TODO add your handling code here:
         if (!SessionManager.getInstance().isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Sesión no válida");
            return;
        }
        
        Eliminar temp = new Eliminar();
        temp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jEliminarButtonActionPerformed

    private void jActualizarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jActualizarButtonActionPerformed
       
        if (!SessionManager.getInstance().isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Sesión no válida");
            return;
        }
        
        Actualizar temp = new Actualizar();
        temp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jActualizarButtonActionPerformed

    private void jLeerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLeerButtonActionPerformed
       
          cargarProductosActuales();
    }//GEN-LAST:event_jLeerButtonActionPerformed

    private void jActualesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jActualesButtonActionPerformed
        // TODO add your handling code here:
        cargarProductosActuales();
        JOptionPane.showMessageDialog(this, 
            "Productos actuales cargados correctamente", 
            "Información", 
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jActualesButtonActionPerformed

    private void jCambiosButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCambiosButtonActionPerformed
        // TODO add your handling code here:
           // Cargar historial de cambios cuando se presiona el botón "Cambios"
        cargarHistorialCambios();
        JOptionPane.showMessageDialog(this, 
            "Historial de cambios cargado correctamente", 
            "Información", 
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jCambiosButtonActionPerformed

    private void jUsuarioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUsuarioButtonActionPerformed
        // TODO add your handling code here:
        SessionManager session = SessionManager.getInstance();
        
        if (!session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Sesión no válida");
            return;
        }
        
        // Obtener datos de la sesión
        int userId = session.getUserId();
        String cargo = session.getCargo();
        
        System.out.println("Navegando a Usuario con datos: ID=" + userId + ", Cargo=" + cargo);
        
        Usuario temp = new Usuario(userId, cargo);
        temp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jUsuarioButtonActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Leer().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jActualesButton;
    private javax.swing.JButton jActualizarButton;
    private javax.swing.JButton jAgregarButton;
    private javax.swing.JButton jCambiosButton;
    private javax.swing.JButton jEliminarButton;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JButton jLeerButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTable jProductosTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jUsuarioButton;
    // End of variables declaration//GEN-END:variables
}
