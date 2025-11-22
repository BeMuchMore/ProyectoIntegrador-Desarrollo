package UI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;
import UI.ConfigImagenes;
import UI.util.ErrorHandler;

/**
 * Ventana para ver y editar el perfil del usuario
 * Similar en tamaño a principal.java
 */
public class PerfilUsuario extends javax.swing.JFrame {
    
    private int userId;
    private String fotoUrlActual = "";
    
    /**
     * Constructor
     * @param userId ID del usuario (debe estar logueado)
     */
    public PerfilUsuario(int userId) {
        if (userId <= 0) {
            JOptionPane.showMessageDialog(null,
                "Debe iniciar sesión para ver su perfil",
                "Sesión requerida",
                JOptionPane.WARNING_MESSAGE);
            dispose();
            return;
        }
        
        this.userId = userId;
        
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        configurarEventos();
        cargarDatosUsuario();
    }
    
    /**
     * Carga los datos del usuario
     */
    private void cargarDatosUsuario() {
        try (Connection conn = Conexion.getConnection()) {
            String query = """
                SELECT nombre, apellido, correo, usuario, identificacion, 
                       TipoIdentificacion, descripcion, foto, cargo
                FROM tb_usuarios 
                WHERE id = ?
                """;
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                jTextFieldNombre.setText(rs.getString("nombre") != null ? rs.getString("nombre") : "");
                jTextFieldApellido.setText(rs.getString("apellido") != null ? rs.getString("apellido") : "");
                jTextFieldCorreo.setText(rs.getString("correo") != null ? rs.getString("correo") : "");
                jTextFieldUsuario.setText(rs.getString("usuario") != null ? rs.getString("usuario") : "");
                jTextFieldIdentificacion.setText(rs.getString("identificacion") != null ? rs.getString("identificacion") : "");
                jTextAreaDescripcion.setText(rs.getString("descripcion") != null ? rs.getString("descripcion") : "");
                
                // Tipo de identificación
                String tipoId = rs.getString("TipoIdentificacion");
                if (tipoId != null) {
                    if (tipoId.equalsIgnoreCase("CC")) {
                        jComboBoxTipoId.setSelectedIndex(0);
                    } else if (tipoId.equalsIgnoreCase("CE")) {
                        jComboBoxTipoId.setSelectedIndex(1);
                    }
                }
                
                // Cargo (solo lectura)
                String cargo = rs.getString("cargo");
                jLabelCargo.setText("Cargo: " + (cargo != null ? cargo : "Cliente"));
                
                // Foto
                String foto = rs.getString("foto");
                if (foto != null && !foto.isEmpty()) {
                    fotoUrlActual = foto;
                    cargarFoto(foto);
                }
            }
            
        } catch (SQLException e) {
            ErrorHandler.handleSQLException(this, e, "cargar datos del usuario");
        }
    }
    
    /**
     * Carga la foto del usuario
     */
    private void cargarFoto(String rutaFoto) {
        try {
            String rutaAbsoluta = ConfigImagenes.obtenerRutaAbsoluta(rutaFoto);
            if (rutaAbsoluta != null && !rutaAbsoluta.isEmpty()) {
                File archivo = new File(rutaAbsoluta);
                if (archivo.exists()) {
                    ImageIcon icon = new ImageIcon(rutaAbsoluta);
                    Image img = icon.getImage();
                    Image imgEscalada = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    jLabelFoto.setIcon(new ImageIcon(imgEscalada));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar foto: " + e.getMessage());
        }
    }
    
    /**
     * Configura los eventos
     */
    private void configurarEventos() {
        jButtonSeleccionarFoto.addActionListener(e -> seleccionarFoto());
        jButtonGuardar.addActionListener(e -> guardarCambios());
        jButtonCambiarContrasena.addActionListener(e -> cambiarContrasena());
        jButtonVolver.addActionListener(e -> {
            this.dispose();
        });
    }
    
    /**
     * Selecciona una nueva foto
     */
    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imágenes", "jpg", "jpeg", "png", "gif"));
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            // Aquí podrías guardar la imagen y obtener la ruta
            // Por ahora, solo mostramos una vista previa
            ImageIcon icon = new ImageIcon(archivoSeleccionado.getAbsolutePath());
            Image img = icon.getImage();
            Image imgEscalada = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            jLabelFoto.setIcon(new ImageIcon(imgEscalada));
            
            // Guardar ruta temporal (en producción, deberías guardar la imagen en el servidor)
            fotoUrlActual = archivoSeleccionado.getAbsolutePath();
        }
    }
    
    /**
     * Guarda los cambios del perfil
     */
    private void guardarCambios() {
        try (Connection conn = Conexion.getConnection()) {
            String update = """
                UPDATE tb_usuarios 
                SET nombre = ?, apellido = ?, correo = ?, identificacion = ?, 
                    TipoIdentificacion = ?, descripcion = ?, foto = ?
                WHERE id = ?
                """;
            
            PreparedStatement pstmt = conn.prepareStatement(update);
            pstmt.setString(1, jTextFieldNombre.getText().trim());
            pstmt.setString(2, jTextFieldApellido.getText().trim());
            pstmt.setString(3, jTextFieldCorreo.getText().trim());
            pstmt.setLong(4, Long.parseLong(jTextFieldIdentificacion.getText().trim()));
            
            String tipoId = jComboBoxTipoId.getSelectedIndex() == 0 ? "CC" : "CE";
            pstmt.setString(5, tipoId);
            
            pstmt.setString(6, jTextAreaDescripcion.getText().trim());
            pstmt.setString(7, fotoUrlActual);
            pstmt.setInt(8, userId);
            
            int filasActualizadas = pstmt.executeUpdate();
            
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(this,
                    "Perfil actualizado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el perfil",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            ErrorHandler.handleSQLException(this, e, "actualizar perfil");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "La identificación debe ser un número válido",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cambia la contraseña del usuario
     */
    private void cambiarContrasena() {
        JPasswordField campoActual = new JPasswordField();
        JPasswordField campoNueva = new JPasswordField();
        JPasswordField campoConfirmar = new JPasswordField();
        
        Object[] mensaje = {
            "Contraseña actual:", campoActual,
            "Nueva contraseña:", campoNueva,
            "Confirmar nueva contraseña:", campoConfirmar
        };
        
        int opcion = JOptionPane.showConfirmDialog(this,
            mensaje,
            "Cambiar Contraseña",
            JOptionPane.OK_CANCEL_OPTION);
        
        if (opcion == JOptionPane.OK_OPTION) {
            String contrasenaActual = new String(campoActual.getPassword());
            String nuevaContrasena = new String(campoNueva.getPassword());
            String confirmarContrasena = new String(campoConfirmar.getPassword());
            
            if (nuevaContrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "La nueva contraseña no puede estar vacía",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!nuevaContrasena.equals(confirmarContrasena)) {
                JOptionPane.showMessageDialog(this,
                    "Las contraseñas no coinciden",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verificar contraseña actual
            try (Connection conn = Conexion.getConnection()) {
                String query = "SELECT contrasena FROM tb_usuarios WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    String contrasenaBD = rs.getString("contrasena");
                    if (!contrasenaActual.equals(contrasenaBD)) {
                        JOptionPane.showMessageDialog(this,
                            "La contraseña actual es incorrecta",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Actualizar contraseña
                    String update = "UPDATE tb_usuarios SET contrasena = ? WHERE id = ?";
                    PreparedStatement pstmtUpdate = conn.prepareStatement(update);
                    pstmtUpdate.setString(1, nuevaContrasena);
                    pstmtUpdate.setInt(2, userId);
                    pstmtUpdate.executeUpdate();
                    
                    JOptionPane.showMessageDialog(this,
                        "Contraseña actualizada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (SQLException e) {
                ErrorHandler.handleSQLException(this, e, "cambiar contraseña");
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
        jPanelContenido = new javax.swing.JPanel();
        jPanelFoto = new javax.swing.JPanel();
        jLabelFoto = new javax.swing.JLabel();
        jButtonSeleccionarFoto = new javax.swing.JButton();
        jPanelDatos = new javax.swing.JPanel();
        jLabelNombre = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabelApellido = new javax.swing.JLabel();
        jTextFieldApellido = new javax.swing.JTextField();
        jLabelCorreo = new javax.swing.JLabel();
        jTextFieldCorreo = new javax.swing.JTextField();
        jLabelUsuario = new javax.swing.JLabel();
        jTextFieldUsuario = new javax.swing.JTextField();
        jLabelIdentificacion = new javax.swing.JLabel();
        jTextFieldIdentificacion = new javax.swing.JTextField();
        jLabelTipoId = new javax.swing.JLabel();
        jComboBoxTipoId = new javax.swing.JComboBox<>(new String[]{"CC", "CE"});
        jLabelDescripcion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescripcion = new javax.swing.JTextArea();
        jLabelCargo = new javax.swing.JLabel();
        jButtonGuardar = new javax.swing.JButton();
        jButtonCambiarContrasena = new javax.swing.JButton();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jPanelHeader.setBackground(new java.awt.Color(255, 255, 255));
        jPanelHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jLabelTitulo.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        jLabelTitulo.setText("Mi Perfil");
        jPanelHeader.add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 400, 40));
        
        jButtonVolver.setText("← Volver");
        jButtonVolver.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelHeader.add(jButtonVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 25, 120, 35));
        
        jPanelMain.add(jPanelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1363, 80));
        
        jPanelContenido.setBackground(new java.awt.Color(255, 255, 255));
        jPanelContenido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jPanelFoto.setBackground(new java.awt.Color(245, 245, 245));
        jPanelFoto.setBorder(javax.swing.BorderFactory.createTitledBorder("Foto de Perfil"));
        jPanelFoto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jLabelFoto.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabelFoto.setPreferredSize(new Dimension(150, 150));
        jPanelFoto.add(jLabelFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 30, 150, 150));
        
        jButtonSeleccionarFoto.setText("Seleccionar Foto");
        jButtonSeleccionarFoto.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
        jPanelFoto.add(jButtonSeleccionarFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 190, 150, 30));
        
        jPanelContenido.add(jPanelFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 200, 240));
        
        jPanelDatos.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDatos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        jLabelNombre.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelNombre.setText("Nombre:");
        jPanelDatos.add(jLabelNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 30));
        
        jTextFieldNombre.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jTextFieldNombre.setPreferredSize(new Dimension(300, 30));
        jPanelDatos.add(jTextFieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 300, 30));
        
        jLabelApellido.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelApellido.setText("Apellido:");
        jPanelDatos.add(jLabelApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 100, 30));
        
        jTextFieldApellido.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelDatos.add(jTextFieldApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 300, 30));
        
        jLabelCorreo.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelCorreo.setText("Correo:");
        jPanelDatos.add(jLabelCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 100, 30));
        
        jTextFieldCorreo.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelDatos.add(jTextFieldCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 300, 30));
        
        jLabelUsuario.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelUsuario.setText("Usuario:");
        jPanelDatos.add(jLabelUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 100, 30));
        
        jTextFieldUsuario.setEditable(false);
        jTextFieldUsuario.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jTextFieldUsuario.setBackground(new java.awt.Color(240, 240, 240));
        jPanelDatos.add(jTextFieldUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 300, 30));
        
        jLabelIdentificacion.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelIdentificacion.setText("Identificación:");
        jPanelDatos.add(jLabelIdentificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 100, 30));
        
        jTextFieldIdentificacion.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelDatos.add(jTextFieldIdentificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 200, 30));
        
        jLabelTipoId.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelTipoId.setText("Tipo ID:");
        jPanelDatos.add(jLabelTipoId, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 80, 30));
        
        jComboBoxTipoId.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jPanelDatos.add(jComboBoxTipoId, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 180, 100, 30));
        
        jLabelDescripcion.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelDescripcion.setText("Descripción:");
        jPanelDatos.add(jLabelDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 100, 30));
        
        jTextAreaDescripcion.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jTextAreaDescripcion.setLineWrap(true);
        jTextAreaDescripcion.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextAreaDescripcion);
        jPanelDatos.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 400, 100));
        
        jLabelCargo.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        jLabelCargo.setText("Cargo: Cliente");
        jPanelDatos.add(jLabelCargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 300, 30));
        
        jButtonGuardar.setFont(new java.awt.Font("Arial", Font.BOLD, 16));
        jButtonGuardar.setText("Guardar Cambios");
        jButtonGuardar.setBackground(new java.awt.Color(0, 150, 0));
        jButtonGuardar.setForeground(new java.awt.Color(255, 255, 255));
        jPanelDatos.add(jButtonGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 200, 40));
        
        jButtonCambiarContrasena.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jButtonCambiarContrasena.setText("Cambiar Contraseña");
        jPanelDatos.add(jButtonCambiarContrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 380, 180, 40));
        
        jPanelContenido.add(jPanelDatos, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 600, 450));
        
        jPanelMain.add(jPanelContenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 1363, 500));
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1363, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
        );
        
        pack();
    }
    
    // Variables declaration
    private javax.swing.JButton jButtonCambiarContrasena;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonSeleccionarFoto;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JComboBox<String> jComboBoxTipoId;
    private javax.swing.JLabel jLabelApellido;
    private javax.swing.JLabel jLabelCargo;
    private javax.swing.JLabel jLabelCorreo;
    private javax.swing.JLabel jLabelDescripcion;
    private javax.swing.JLabel jLabelFoto;
    private javax.swing.JLabel jLabelIdentificacion;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelTipoId;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JPanel jPanelContenido;
    private javax.swing.JPanel jPanelDatos;
    private javax.swing.JPanel jPanelFoto;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaDescripcion;
    private javax.swing.JTextField jTextFieldApellido;
    private javax.swing.JTextField jTextFieldCorreo;
    private javax.swing.JTextField jTextFieldIdentificacion;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldUsuario;
}

