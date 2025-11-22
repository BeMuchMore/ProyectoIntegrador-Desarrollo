/*
 * USUARIO.JAVA - Edición de perfil del usuario actual
 * Permite al usuario loggeado ver y editar su información personal
 */
package UX;



import UI.Conexion;
import UI.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Julian David
 */
public class Usuario extends javax.swing.JFrame {
     
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Usuario.class.getName());
    
    // Constantes
    private static final String CARPETA_FOTOS = "...Z-Global-Img/usuarios/";
    
    // Variables de instancia
    private String fotoUrlSeleccionada = "";
    private int usuarioActualId;
    private String cargoOriginal;

    /**
     * Constructor
     */
    public Usuario(int userId, String cargo) {
        initComponents();
        configurarVentana();
        
        this.usuarioActualId = userId;
        this.cargoOriginal = cargo;
        
        configurarEventos();
        cargarDatosUsuario();
    }
    
    // ===== CONFIGURACIÓN INICIAL =====
    
    private void configurarVentana() {
        setResizable(false);
        setLocationRelativeTo(null);
  
        SessionManager session = SessionManager.getInstance();
        this.setTitle("Perfil - Usuario: " + session.getUsername());
        
        System.out.println("Usuario " + session.getUsername() + " accedió a su perfil");
    }
    
    private void configurarEventos() {
        // Botón subir imagen
        jSubirImgButton.addActionListener(e -> seleccionarYGuardarFoto());
        
        // Botón actualizar
        ActualizarButton.addActionListener(e -> ejecutarActualizar());
        
        // Configurar cursores para botones del menú lateral
        ProductosAgregarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ProductosActualizarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ProductosEliminarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Agregar listeners si necesitas navegar a otras ventanas
        ProductosAgregarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                abrirProductos();
            }
        });
        
        jLabel23.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabel23.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cerrarSesion();
            }
        });
    }
    
    // ===== CARGA DE DATOS =====
    
    private void cargarDatosUsuario() {
        String sql = """
            SELECT nombre, apellido, correo, usuario, identificacion, 
                   TipoIdentificacion, descripcion, foto, cargo
            FROM tb_usuarios 
            WHERE id = ?
            """;
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioActualId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Cargar datos en campos
                NombreTextField.setText(rs.getString("nombre"));
                ApellidoTextField.setText(rs.getString("apellido"));
                CorreoTextField.setText(rs.getString("correo"));
                UsuarioTextField.setText(rs.getString("usuario"));
                IdentificacionTextField.setText(String.valueOf(rs.getLong("identificacion")));
                IdTextField.setText(String.valueOf(usuarioActualId));
                DescripcionTextArea.setText(rs.getString("descripcion"));
                
                // Tipo de identificación
                String tipoId = rs.getString("TipoIdentificacion");
                if (tipoId != null) {
                    if (tipoId.equalsIgnoreCase("CC")) {
                        jComboBox1.setSelectedIndex(0);
                    } else if (tipoId.equalsIgnoreCase("CE")) {
                        jComboBox1.setSelectedIndex(1);
                    }
                }
                
                // Cargo
                String cargo = rs.getString("cargo");
                CargoLabel.setText(cargo != null ? cargo : "-");
                
                // Foto
                String foto = rs.getString("foto");
                if (foto != null && !foto.isEmpty()) {
                    fotoUrlSeleccionada = foto;
                    mostrarVistaPrevia(foto);
                }
                
                // Actualizar label con nombre de usuario
                SessionManager session = SessionManager.getInstance();
                UsuarioLabel.setText(session.getUsername());
               
                
                // Campos no editables
                IdTextField.setEditable(false);
                UsuarioTextField.setEditable(false); // Usuario no se puede cambiar
                
                System.out.println("Datos del usuario cargados correctamente");
                
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron datos del usuario",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar datos del usuario", e);
            JOptionPane.showMessageDialog(this,
                "Error al cargar los datos: " + e.getMessage(),
                "Error de Base de Datos",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ===== VALIDACIONES =====
    
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();
        
        if (NombreTextField.getText().trim().isEmpty()) {
            errores.append("- El nombre es obligatorio\n");
        }
        
        if (ApellidoTextField.getText().trim().isEmpty()) {
            errores.append("- El apellido es obligatorio\n");
        }
        
        if (CorreoTextField.getText().trim().isEmpty()) {
            errores.append("- El correo es obligatorio\n");
        } else if (!validarEmail(CorreoTextField.getText().trim())) {
            errores.append("- El correo no tiene un formato válido\n");
        }
        
        if (IdentificacionTextField.getText().trim().isEmpty()) {
            errores.append("- La identificación es obligatoria\n");
        } else {
            try {
                long id = Long.parseLong(IdentificacionTextField.getText().trim());
                if (id <= 0) {
                    errores.append("- La identificación debe ser un número positivo\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- La identificación debe ser un número válido\n");
            }
        }
        
        // Validar contraseña solo si se ingresó una nueva
        String nuevaContrasena = ContrasenaTextField.getText().trim();
        if (!nuevaContrasena.isEmpty() && nuevaContrasena.length() < 6) {
            errores.append("- La contraseña debe tener al menos 6 caracteres\n");
        }
        
        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this,
                "Corrija los siguientes errores:\n\n" + errores.toString(),
                "Errores de Validación",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private boolean validarEmail(String email) {
        // Validación básica de email
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
    
    // ===== OPERACIÓN ACTUALIZAR =====
    
    private void ejecutarActualizar() {
        if (!validarCampos()) {
            return;
        }
        
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Desea actualizar su información de perfil?",
            "Confirmar Actualización",
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            if (actualizarUsuario()) {
                JOptionPane.showMessageDialog(this,
                    "Perfil actualizado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Actualizar SessionManager si cambió el nombre
                SessionManager session = SessionManager.getInstance();
                session.login(usuarioActualId, UsuarioTextField.getText().trim(), cargoOriginal);
                
                // Recargar datos para reflejar cambios
                cargarDatosUsuario();
            }
        }
    }
    
    private boolean actualizarUsuario() {
        StringBuilder sql = new StringBuilder("""
            UPDATE tb_usuarios SET 
            nombre = ?, apellido = ?, correo = ?, 
            identificacion = ?, TipoIdentificacion = ?, 
            descripcion = ?, foto = ?
            """);
        
        // Si ingresó nueva contraseña, actualizarla también
        String nuevaContrasena = ContrasenaTextField.getText().trim();
        boolean actualizarContrasena = !nuevaContrasena.isEmpty();
        
        if (actualizarContrasena) {
            sql.append(", contrasena = ?");
        }
        
        sql.append(" WHERE id = ?");
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            pstmt.setString(paramIndex++, NombreTextField.getText().trim());
            pstmt.setString(paramIndex++, ApellidoTextField.getText().trim());
            pstmt.setString(paramIndex++, CorreoTextField.getText().trim());
            pstmt.setLong(paramIndex++, Long.parseLong(IdentificacionTextField.getText().trim()));
            
            // Tipo de identificación
            String tipoId = jComboBox1.getSelectedIndex() == 0 ? "CC" : "CE";
            pstmt.setString(paramIndex++, tipoId);
            
            pstmt.setString(paramIndex++, DescripcionTextArea.getText().trim());
            pstmt.setString(paramIndex++, fotoUrlSeleccionada);
            
            if (actualizarContrasena) {
                // IMPORTANTE: En producción, hashear la contraseña con BCrypt
                pstmt.setString(paramIndex++, nuevaContrasena);
            }
            
            pstmt.setInt(paramIndex, usuarioActualId);
            
            int filasActualizadas = pstmt.executeUpdate();
            
            if (filasActualizadas > 0) {
                System.out.println("Usuario actualizado: ID " + usuarioActualId);
                
                // Limpiar campo de contraseña después de actualizar
                if (actualizarContrasena) {
                    ContrasenaTextField.setText("");
                }
                
                return true;
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el perfil",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al actualizar usuario", e);
            JOptionPane.showMessageDialog(this,
                "Error al actualizar el perfil: " + e.getMessage(),
                "Error de Base de Datos",
                JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Error en el formato de la identificación",
                "Error de Formato",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // ===== GESTIÓN DE FOTO =====
    
    private void seleccionarYGuardarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar foto de perfil");
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Imágenes (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        
        String directorioUsuario = System.getProperty("user.home");
        fileChooser.setCurrentDirectory(new File(directorioUsuario));
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            
            if (!esImagenValida(archivoSeleccionado)) {
                JOptionPane.showMessageDialog(this,
                    "Seleccione un archivo de imagen válido (JPG, JPEG, PNG, GIF)",
                    "Archivo no válido",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            long tamañoMB = archivoSeleccionado.length() / (1024 * 1024);
            if (tamañoMB > 5) {
                JOptionPane.showMessageDialog(this,
                    "Imagen muy grande. Seleccione una menor a 5MB.\nTamaño actual: " + tamañoMB + "MB",
                    "Imagen muy grande",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                String rutaGuardada = guardarFoto(archivoSeleccionado);
                
                if (rutaGuardada != null && !rutaGuardada.isEmpty()) {
                    fotoUrlSeleccionada = rutaGuardada;
                    mostrarVistaPrevia(rutaGuardada);
                    
                    JOptionPane.showMessageDialog(this,
                        "Foto cargada exitosamente\nArchivo: " + archivoSeleccionado.getName(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (IOException e) {
                logger.log(java.util.logging.Level.SEVERE, "Error al procesar foto", e);
                JOptionPane.showMessageDialog(this,
                    "Error al guardar la foto:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String guardarFoto(File archivoOrigen) throws IOException {
        if (archivoOrigen == null || !archivoOrigen.exists()) {
            throw new IOException("Archivo de origen no válido");
        }
        
        File carpetaFotos = new File(CARPETA_FOTOS);
        if (!carpetaFotos.exists()) {
            boolean creada = carpetaFotos.mkdirs();
            if (!creada) {
                throw new IOException("No se pudo crear la carpeta de fotos: " + CARPETA_FOTOS);
            }
        }
        
        String extension = obtenerExtension(archivoOrigen.getName());
        String nombreUnico = generarNombreUnico(extension);
        
        Path rutaDestino = Paths.get(CARPETA_FOTOS + nombreUnico);
        
        Files.copy(archivoOrigen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("Foto guardada: " + rutaDestino.toString());
        
        return CARPETA_FOTOS + nombreUnico;
    }
    
    private void mostrarVistaPrevia(String rutaFoto) {
        try {
            ImageIcon icon = new ImageIcon(rutaFoto);
            Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            VistaPreviaImg.setIcon(new ImageIcon(img));
            VistaPreviaImg.setText("");
        } catch (Exception e) {
            VistaPreviaImg.setText("Error al cargar vista previa");
            logger.log(java.util.logging.Level.WARNING, "Error en vista previa", e);
        }
    }
    
    private String obtenerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto > 0 && ultimoPunto < nombreArchivo.length() - 1) {
            return nombreArchivo.substring(ultimoPunto + 1).toLowerCase();
        }
        return "jpg";
    }
    
    private String generarNombreUnico(String extension) {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = ahora.format(formatter);
        
        String username = "usuario";
        try {
            SessionManager session = SessionManager.getInstance();
            if (session != null && session.getUsername() != null && !session.getUsername().isEmpty()) {
                username = session.getUsername();
                username = username.replaceAll("[^a-zA-Z0-9]", "");
                
                if (username.isEmpty()) {
                    username = "usuario";
                }
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING, "Error al obtener username", e);
        }
        
        int random = (int) (Math.random() * 10000);
        
        return String.format("foto_%s_%s_%04d.%s", username, timestamp, random, extension);
    }
    
    private boolean esImagenValida(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || 
               nombre.endsWith(".png") || nombre.endsWith(".gif");
    }
    
    // ===== NAVEGACIÓN =====
    
    private void abrirProductos() {
        // Implementar navegación a Productos si es necesario
        System.out.println("Navegando a Productos...");
        // Productos productos = new Productos();
        // productos.setVisible(true);
        // this.dispose();
    }
    
    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Desea cerrar sesión?",
            "Confirmar Cierre de Sesión",
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            SessionManager.getInstance().logout();
            System.out.println("Sesión cerrada");
            
            // Aquí debes abrir tu ventana de login
            // Login login = new Login();
            // login.setVisible(true);
            this.dispose();
        }
    }

    /**
     * CÓDIGO GENERADO POR NETBEANS - NO MODIFICAR MANUALMENTE
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        UsuarioLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        VistaPreviaImg = new javax.swing.JLabel();
        jSubirImgButton = new javax.swing.JButton();
        ApellidoTextField = new javax.swing.JTextField();
        CorreoTextField = new javax.swing.JTextField();
        ContrasenaTextField = new javax.swing.JTextField();
        UsuarioTextField = new javax.swing.JTextField();
        IdTextField = new javax.swing.JTextField();
        IdentificacionTextField = new javax.swing.JTextField();
        NombreTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        DescripcionTextArea = new javax.swing.JTextArea();
        CargoLabel = new javax.swing.JLabel();
        ActualizarButton = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
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

        UsuarioLabel.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        UsuarioLabel.setText("Usuario");
        jPanel4.add(UsuarioLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 270, -1));
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, -1, -1));

        jLabel4.setText("____________________________________________________________________________________________________________________________________________________________________________________________________");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 980, -1));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Apellido");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Cargo");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 340, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Contraseña (dejar vacío para no cambiar)");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Descripcion");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("Foto");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 110, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setText("Id");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 500, -1, -1));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel10.setText("Bienvenido");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setText("Correo");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, -1, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setText("Identificacion");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 500, -1, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setText("Nombre");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, -1, -1));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel14.setText("Usuario");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, -1, -1));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel15.setText("Tipo de Identificacion");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, -1, -1));

        VistaPreviaImg.setText("imagen");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(109, Short.MAX_VALUE)
                .addComponent(VistaPreviaImg)
                .addGap(121, 121, 121))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(131, Short.MAX_VALUE)
                .addComponent(VistaPreviaImg)
                .addGap(133, 133, 133))
        );

        jPanel4.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 130, 270, 280));

        jSubirImgButton.setText("Subir Imagen");
        jPanel4.add(jSubirImgButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 100, -1, -1));
        jPanel4.add(ApellidoTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 250, 30));
        jPanel4.add(CorreoTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 250, 30));
        
        ContrasenaTextField.setToolTipText("Dejar vacío para no cambiar la contraseña");
        jPanel4.add(ContrasenaTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 250, 30));
        jPanel4.add(UsuarioTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 440, 250, 30));
        jPanel4.add(IdTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 520, 250, 30));
        jPanel4.add(IdentificacionTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 520, 250, 30));
        jPanel4.add(NombreTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 250, 30));

        DescripcionTextArea.setColumns(20);
        DescripcionTextArea.setRows(5);
        jScrollPane1.setViewportView(DescripcionTextArea);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 130, 260, 190));

        CargoLabel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        CargoLabel.setText("-");
        jPanel4.add(CargoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 370, -1, -1));

        ActualizarButton.setText("Actualizar");
        jPanel4.add(ActualizarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 480, 130, 60));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CC (cédula de ciudadanía)", "CE (carnet de extranjería)" }));
        jPanel4.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 440, 250, 30));

        jPanel7.setBackground(new java.awt.Color(153, 153, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/usuario.png"))); // NOI18N
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

        ProductosEliminarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/cesta-de-la-compra.png"))); // NOI18N esta es una
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

        ProductosActualizarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar (1).png"))); // NOI18N
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

        ProductosAgregarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/mas (1).png"))); // NOI18N
           ProductosAgregarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ProductosAgregarButton.addMouseListener(new MouseAdapter() {
            @Override
                    public void mouseClicked(MouseEvent e) {
                        Productos ventana = new Productos(Productos.ModoOperacion.ELIMINAR);
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

        UsuariosAgregarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/agregar-usuario.png"))); // NOI18N
          UsuariosAgregarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UsuariosAgregarButton.addMouseListener(new MouseAdapter() {
            @Override
                    public void mouseClicked(MouseEvent e) {
                        Usuarios ventana = new Usuarios();
                                    ventana.setVisible(true);
                                    cerrar();
    }
        });
        UsuariosAgregarButton.setText("Agregar");

        UsuariosEliminarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/borrar-usuario.png"))); // NOI18N
           UsuariosEliminarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UsuariosEliminarButton.addMouseListener(new MouseAdapter() {
            @Override
                    public void mouseClicked(MouseEvent e) {
                        Usuarios ventana = new Usuarios();
                                    ventana.setVisible(true);
                                    cerrar();
    }
        });
        UsuariosEliminarButton.setText("Eliminar");

        UsuariosActualizarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar (1).png"))); // NOI18N
            UsuariosActualizarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UsuariosActualizarButton.addMouseListener(new MouseAdapter() {
            @Override
                    public void mouseClicked(MouseEvent e) {
                        Usuarios ventana = new Usuarios();
                                    ventana.setVisible(true);
                                    cerrar();
    }
        });
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
    }// </editor-fold>                        

     private void cerrar(){
    this.dispose();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    // Carga la ventana Usuario directamente para pruebas
    java.awt.EventQueue.invokeLater(() -> {
        Usuario ventana = new Usuario(1, "admin"); // Puedes cambiar el id y nombre
        ventana.setVisible(true);
    });
}
    
    // Variables declaration - do not modify                     
    private javax.swing.JButton ActualizarButton;
    private javax.swing.JLabel AdministradorOtrosButton;
    private javax.swing.JLabel AdministradorPermisosButton;
    private javax.swing.JTextField ApellidoTextField;
    private javax.swing.JLabel CargoLabel;
    private javax.swing.JTextField ContrasenaTextField;
    private javax.swing.JTextField CorreoTextField;
    private javax.swing.JTextArea DescripcionTextArea;
    private javax.swing.JTextField IdTextField;
    private javax.swing.JTextField IdentificacionTextField;
    private javax.swing.JLabel InterfazCategoriasButton;
    private javax.swing.JLabel InterfazOfertasButton;
    private javax.swing.JLabel InterfazPrincipalButton;
    private javax.swing.JTextField NombreTextField;
    private javax.swing.JLabel ProductosActualizarButton;
    private javax.swing.JLabel ProductosAgregarButton;
    private javax.swing.JLabel ProductosEliminarButton;
    private javax.swing.JLabel UsuarioLabel;
    private javax.swing.JTextField UsuarioTextField;
    private javax.swing.JLabel UsuariosActualizarButton;
    private javax.swing.JLabel UsuariosAgregarButton;
    private javax.swing.JLabel UsuariosEliminarButton;
    private javax.swing.JLabel VistaPreviaImg;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton jSubirImgButton;
    // End of variables declaration                   

   
}