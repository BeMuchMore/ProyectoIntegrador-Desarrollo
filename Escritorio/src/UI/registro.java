package UI;


import javax.swing.*;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;
import java.sql.*;
import UI.util.ModernStyles;
import UI.Conexion;

public class registro extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(registro.class.getName());
    private ValidadorContrasena.NivelSeguridad nivelActual;

    /**
     * Creates new form PRF
     */
    public registro() {
        ModernStyles.applyModernLookAndFeel();
        initComponents();
        aplicarEstilosModernos();
        configurarValidaciones();
        cargarComplementos();
    }
    
    /**
     * Aplica estilos modernos a todos los componentes (diseño web)
     */
    private void aplicarEstilosModernos() {
        // Panel principal con gradiente rosado (como body de la web)
        jPanel1.setBackground(ModernStyles.BG_LIGHT);
        
        // Panel de formulario (auth-card estilo web: blanco con sombra y bordes redondeados)
        jPanel3.setBackground(ModernStyles.BG_WHITE);
        jPanel3.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 229, 233), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Títulos con tipografía mejorada (gradiente morado como auth-header de la web)
        jLabel4.setFont(ModernStyles.getHeading2Font());
        jLabel4.setForeground(ModernStyles.INFO_COLOR); // Morado azulado como web
        ModernStyles.enableTextAntialiasing(jLabel4);
        
        jLabel3.setFont(ModernStyles.getBodyLargeFont());
        jLabel3.setForeground(ModernStyles.TEXT_SECONDARY);
        ModernStyles.enableTextAntialiasing(jLabel3);
        
        // Labels de campos con tipografía mejorada
        jLabel17.setFont(ModernStyles.getSemiboldFont(13));
        jLabel17.setForeground(ModernStyles.TEXT_PRIMARY);
        ModernStyles.enableTextAntialiasing(jLabel17);
        
        jLabel13.setFont(ModernStyles.getSemiboldFont(13));
        jLabel13.setForeground(ModernStyles.TEXT_PRIMARY);
        ModernStyles.enableTextAntialiasing(jLabel13);
        
        jLabel14.setFont(ModernStyles.getSemiboldFont(13));
        jLabel14.setForeground(ModernStyles.TEXT_PRIMARY);
        ModernStyles.enableTextAntialiasing(jLabel14);
        
        jLabel15.setFont(ModernStyles.getSemiboldFont(13));
        jLabel15.setForeground(ModernStyles.TEXT_PRIMARY);
        ModernStyles.enableTextAntialiasing(jLabel15);
        
        jLabel16.setFont(ModernStyles.getSemiboldFont(13));
        jLabel16.setForeground(ModernStyles.TEXT_PRIMARY);
        ModernStyles.enableTextAntialiasing(jLabel16);
        
        jLabel12.setFont(ModernStyles.getSemiboldFont(13));
        jLabel12.setForeground(ModernStyles.TEXT_PRIMARY);
        ModernStyles.enableTextAntialiasing(jLabel12);
        
        // Campos de texto
        ModernStyles.styleTextField(textCorreo);
        ModernStyles.styleTextField(txtNombres);
        ModernStyles.styleTextField(txtApellidos);
        ModernStyles.styleTextField(txtUsuarios);
        ModernStyles.stylePasswordField(txtContrasena);
        ModernStyles.styleTextField(txtIdent);
        
        // Botones (estilo auth-button morado azulado como web)
        ModernStyles.styleAuthButton(btnRegistrar);
        ModernStyles.styleAuthButton(btnRegresar);
        
        // Labels informativos con tipografía mejorada
        LabelCorreoInformacion.setFont(ModernStyles.getCaptionFont());
        LabelCorreoInformacion.setForeground(ModernStyles.TEXT_SECONDARY);
        ModernStyles.enableTextAntialiasing(LabelCorreoInformacion);
        
        LabelUsuarioInforacion.setFont(ModernStyles.getCaptionFont());
        LabelUsuarioInforacion.setForeground(ModernStyles.TEXT_SECONDARY);
        ModernStyles.enableTextAntialiasing(LabelUsuarioInforacion);
        
        LabelContraseñaInformacion.setFont(ModernStyles.getCaptionFont());
        LabelContraseñaInformacion.setForeground(ModernStyles.TEXT_SECONDARY);
        ModernStyles.enableTextAntialiasing(LabelContraseñaInformacion);
        
        // Panel de beneficios con tipografía mejorada
        jLabel18.setFont(ModernStyles.getHeading3Font());
        jLabel18.setForeground(ModernStyles.TEXT_PRIMARY);
        ModernStyles.enableTextAntialiasing(jLabel18);
        
        jLabel20.setFont(ModernStyles.getBodyFont());
        jLabel20.setForeground(ModernStyles.TEXT_SECONDARY);
        ModernStyles.enableTextAntialiasing(jLabel20);
        
        jLabel22.setFont(ModernStyles.getBodyFont());
        jLabel22.setForeground(ModernStyles.TEXT_SECONDARY);
        ModernStyles.enableTextAntialiasing(jLabel22);
        
        jLabel24.setFont(ModernStyles.getBodyFont());
        jLabel24.setForeground(ModernStyles.TEXT_SECONDARY);
        ModernStyles.enableTextAntialiasing(jLabel24);
        
        jLabel26.setFont(ModernStyles.getBodyFont());
        jLabel26.setForeground(ModernStyles.TEXT_SECONDARY);
        ModernStyles.enableTextAntialiasing(jLabel26);
        
        // Panel footer con fondo oscuro (como footer de la web #0a0a0a)
        jPanel5.setBackground(ModernStyles.BG_DARK);
        jPanel5.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        jLabel6.setFont(ModernStyles.getHeading4Font());
        jLabel6.setForeground(ModernStyles.TEXT_LIGHT);
        ModernStyles.enableTextAntialiasing(jLabel6);
        
        jLabel7.setFont(ModernStyles.getBodySmallFont());
        jLabel7.setForeground(new Color(200, 200, 200));
        ModernStyles.enableTextAntialiasing(jLabel7);
        
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
                    jLabel6.setText(nombre);
                }
                if (correo != null && !correo.isEmpty()) {
                    jLabel7.setText(correo);
                }
            }
        } catch (SQLException e) {
            // Usar valores por defecto si hay error
            jLabel6.setText("FASHION");
            jLabel7.setText("contacto@fashion.com");
        }
    }
    
    // Configurar listeners para validaciones en tiempo real
    private void configurarValidaciones() {
        // Listener para el campo de contraseña
        txtContrasena.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarNivelContrasena();
            }
        });
        
        // Listener para el campo de correo
        textCorreo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarCorreo();
            }
        });
        
        // Listener para el campo de usuario
        txtUsuarios.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarUsuario();
            }
        });
    }
    
    // Actualizar indicador de nivel de contraseña
    private void actualizarNivelContrasena() {
        String contrasena = new String(txtContrasena.getPassword());
        if (contrasena.isEmpty()) {
            LabelContraseñaInformacion.setText("-");
            LabelContraseñaInformacion.setForeground(Color.BLACK);
            return;
        }
        
        nivelActual = ValidadorContrasena.evaluarContrasena(contrasena);
        LabelContraseñaInformacion.setText("Nivel: " + nivelActual.getTexto());
        
        switch (nivelActual) {
            case FACIL:
                LabelContraseñaInformacion.setForeground(Color.RED);
                break;
            case MEDIO:
                LabelContraseñaInformacion.setForeground(Color.ORANGE);
                break;
            case DIFICIL:
                LabelContraseñaInformacion.setForeground(new Color(204, 204, 0)); // Yellow-ish
                break;
            case AVANZADO:
                LabelContraseñaInformacion.setForeground(Color.GREEN);
                break;
        }
    }
    
    // Validar formato de correo
    private void validarCorreo() {
        String correo = textCorreo.getText().trim();
        if (correo.isEmpty()) {
            LabelCorreoInformacion.setText("-");
            LabelCorreoInformacion.setForeground(Color.BLACK);
            return;
        }
        
        // Validar formato de correo
        String patronCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(patronCorreo, correo)) {
            LabelCorreoInformacion.setText("Formato inválido");
            LabelCorreoInformacion.setForeground(Color.RED);
            return;
        }
        
        // Verificar si el correo ya existe
        if (Conexion.correoExiste(correo)) {
            LabelCorreoInformacion.setText("Correo ya registrado");
            LabelCorreoInformacion.setForeground(Color.RED);
        } else {
            LabelCorreoInformacion.setText("Correo disponible ✓");
            LabelCorreoInformacion.setForeground(Color.GREEN);
        }
    }
    
    // Validar usuario
    private void validarUsuario() {
        String usuario = txtUsuarios.getText().trim();
        if (usuario.isEmpty()) {
            LabelUsuarioInforacion.setText("-");
            LabelUsuarioInforacion.setForeground(Color.BLACK);
            return;
        }
        
        // Verificar si el usuario ya existe
        if (Conexion.usuarioExiste(usuario)) {
            String sugerencia = Conexion.sugerirUsuario(usuario);
            LabelUsuarioInforacion.setText("Ya existe. Sugerencia: " + sugerencia);
            LabelUsuarioInforacion.setForeground(Color.ORANGE);
            
            // Preguntar si quiere usar la sugerencia
            int respuesta = JOptionPane.showConfirmDialog(this,
                "El usuario '" + usuario + "' ya existe.\n" +
                "¿Desea usar '" + sugerencia + "' como nombre de usuario?",
                "Usuario existente",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
            if (respuesta == JOptionPane.YES_OPTION) {
                txtUsuarios.setText(sugerencia);
                LabelUsuarioInforacion.setText("Usuario disponible ✓");
                LabelUsuarioInforacion.setForeground(Color.GREEN);
            }
        } else {
            LabelUsuarioInforacion.setText("Usuario disponible ✓");
            LabelUsuarioInforacion.setForeground(Color.GREEN);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        txtIdent = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        txtContrasena = new javax.swing.JPasswordField();
        txtUsuarios = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        textCorreo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTerminosCondicionesCheckBox = new javax.swing.JCheckBox();
        jTipoDeIdentificacionComboBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        LabelContraseñaInformacion = new javax.swing.JLabel();
        LabelUsuarioInforacion = new javax.swing.JLabel();
        LabelCorreoInformacion = new javax.swing.JLabel();
        
        jLabel5 = new javax.swing.JLabel();//condiciones

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(757, 544));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("FASHION");
        jLabel6.setAlignmentX(0.5F);
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 203, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText("INVOICEDAY@CORREOUNIVALLE.EDU.CO");
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 253, 20));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setText("COPYRIGHT (C) 2025");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        // Iconos de redes sociales - cargar desde recursos si existen, sino ocultar
        try {
            jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/insta.png")));
        } catch (Exception e) {
            jLabel9.setVisible(false);
        }
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 0, 60, 60));

        try {
            jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/face.png")));
        } catch (Exception e) {
            jLabel10.setVisible(false);
        }
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 5, -1, -1));

        try {
            jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/twiter.png")));
        } catch (Exception e) {
            jLabel11.setVisible(false);
        }
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 5, -1, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtIdent.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtIdent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdentActionPerformed(evt);
            }
        });
        jPanel3.add(txtIdent, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 450, 230, 30));

        txtApellidos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtApellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidosActionPerformed(evt);
            }
        });
        jPanel3.add(txtApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 400, 30));

        txtNombres.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtNombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombresActionPerformed(evt);
            }
        });
        jPanel3.add(txtNombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 400, 30));

        txtContrasena.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtContrasena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContrasenaActionPerformed(evt);
            }
        });
        jPanel3.add(txtContrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 400, 30));

        txtUsuarios.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuariosActionPerformed(evt);
            }
        });
        jPanel3.add(txtUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 400, 30));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setText("Identificación");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, -1, 20));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setText("Nombres");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, 20));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel14.setText("Apellidos");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, -1, 20));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel15.setText("Usuario");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, 20));

        jLabel16.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel16.setText("Contraseña");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, 20));

        btnRegistrar.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnRegistrar.setText("Registrate");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        jPanel3.add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 430, 150, 46));

        btnRegresar.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnRegresar.setText("Iniciar Sesión");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });
        jPanel3.add(btnRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(687, 430, 190, 46));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel17.setText("Correo");
        jLabel17.setPreferredSize(new java.awt.Dimension(40, 10));
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 110, 20));
        jPanel3.add(textCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 400, 30));

        jLabel3.setText("Completa tus datos personales y disfruta una compra más fácil.");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setText("Inicia sesión o regístrate para comprar");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        jLabel18.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel18.setText("Beneficios");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, -1, -1));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/sobre.png"))); // NOI18N
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, 40, 30));

        jLabel20.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel20.setText("Recibir notificaciones en tiempo real de tus pedidos.");
        jPanel3.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, -1, -1));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/corazon.png"))); // NOI18N
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 220, -1, -1));

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setText("Guardar medios de pago y direcciones favoritas.");
        jPanel3.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 180, -1, -1));

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/banco.png"))); // NOI18N
        jPanel3.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, -1, -1));

        jLabel24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel24.setText("Promociones especiales, cupones de descuento y más.");
        jPanel3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 230, -1, -1));

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/comprobacion-de-lista.png"))); // NOI18N
        jPanel3.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 270, -1, -1));

        jLabel26.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel26.setText("Revisar tus boletas online.");
        jPanel3.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 280, -1, -1));

        jTerminosCondicionesCheckBox.setText("  ");
        jTerminosCondicionesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTerminosCondicionesCheckBoxActionPerformed(evt);
            }
        });
        jPanel3.add(jTerminosCondicionesCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 340, -1, -1));

        jTipoDeIdentificacionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CC (cédula de ciudadanía)", "CE (carnet de extranjería)" }));
        jTipoDeIdentificacionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTipoDeIdentificacionComboBoxActionPerformed(evt);
            }
        });
        jPanel3.add(jTipoDeIdentificacionComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 160, -1));

        jButton1.setText("o");
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 440, -1, -1));

        jLabel27.setText("Autorizo el tratamiento de mis datos personales.");
        jPanel3.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 360, -1, -1));

        LabelContraseñaInformacion.setText("-");
        jPanel3.add(LabelContraseñaInformacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 350, 250, -1));

        LabelUsuarioInforacion.setText("-");
        jPanel3.add(LabelUsuarioInforacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 280, 250, -1));

        LabelCorreoInformacion.setText("-");
        jPanel3.add(LabelCorreoInformacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 250, -1));

        jLabel5.setText("Acepto los términos y condiciones.");
        jLabel5.setForeground(Color.BLUE);
        jLabel5.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        jLabel5.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new Condiciones().setVisible(true);  
            }
        });
        
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 340, -1, -1));

        jScrollPane1.setViewportView(jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 912, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>                        

    private void txtIdentActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void txtApellidosActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void txtNombresActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void txtContrasenaActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void txtUsuariosActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // IMPLEMENTACIÓN COMPLETA DEL REGISTRO CON VALIDACIONES
        
        // 1. Verificar que se aceptaron los términos y condiciones
        if (!jTerminosCondicionesCheckBox.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "Debe aceptar los términos y condiciones para continuar", 
                "Términos y Condiciones", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 2. Obtener todos los campos
        String correo = textCorreo.getText().trim();
        String nombre = txtNombres.getText().trim();
        String apellido = txtApellidos.getText().trim();
        String usuario = txtUsuarios.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        String identificacion = txtIdent.getText().trim();
        String tipoIdentificacion = jTipoDeIdentificacionComboBox.getSelectedItem().toString();
        
        // 3. Validar campos obligatorios
        if (correo.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || 
            usuario.isEmpty() || contrasena.isEmpty() || identificacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos obligatorios", 
                "Campos incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 4. Validar formato de correo
        String patronCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(patronCorreo, correo)) {
            JOptionPane.showMessageDialog(this, 
                "El formato del correo electrónico no es válido", 
                "Correo inválido", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 5. Verificar si el correo ya existe
        if (Conexion.correoExiste(correo)) {
            JOptionPane.showMessageDialog(this, 
                "El correo electrónico ya está registrado en el sistema.\n" +
                "Por favor, use otro correo o inicie sesión con su cuenta existente.", 
                "Correo ya registrado", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 6. Verificar si el usuario ya existe
        if (Conexion.usuarioExiste(usuario)) {
            String nuevoUsuario = Conexion.sugerirUsuario(usuario);
            int respuesta = JOptionPane.showConfirmDialog(this,
                "El nombre de usuario '" + usuario + "' ya está en uso.\n" +
                "¿Desea usar '" + nuevoUsuario + "' como nombre de usuario?",
                "Usuario existente",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
            if (respuesta == JOptionPane.YES_OPTION) {
                usuario = nuevoUsuario;
                txtUsuarios.setText(usuario);
            } else {
                return;
            }
        }
        
        // 7. Verificar nivel de seguridad de la contraseña
        ValidadorContrasena.NivelSeguridad nivel = ValidadorContrasena.evaluarContrasena(contrasena);
        if (nivel == ValidadorContrasena.NivelSeguridad.FACIL) {
            String sugerencias = ValidadorContrasena.obtenerSugerencias(contrasena);
            int respuesta = JOptionPane.showConfirmDialog(this,
                "Su contraseña es muy débil y podría ser fácilmente comprometida.\n\n" +
                sugerencias + "\n" +
                "¿Desea continuar con esta contraseña de todos modos?",
                "Contraseña débil",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (respuesta == JOptionPane.NO_OPTION) {
                txtContrasena.requestFocus();
                return;
            }
        }
        
        // 8. Intentar registrar el usuario
        boolean registroExitoso = Conexion.insertarUsuario(
            apellido,
            "", // cargo - vacío por defecto
            contrasena,
            correo,
            "", // data - vacío por defecto
            "", // descripcion - vacío por defecto
            "", // foto - vacío por defecto
            identificacion,
            tipoIdentificacion.substring(0, 2), // Extraer solo "CC" o "CE"
            nombre,
            usuario
        );
        
        if (registroExitoso) {
            JOptionPane.showMessageDialog(this,
                "¡Registro exitoso!\n" +
                "Usuario: " + usuario + "\n" +
                "Correo: " + correo + "\n\n" +
                "Ya puede iniciar sesión con sus credenciales.",
                "Registro completado",
                JOptionPane.INFORMATION_MESSAGE);
                
            // Limpiar campos después del registro exitoso
            limpiarCampos();
            
            // Opción: Ir directamente al login
            int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Desea ir a la pantalla de inicio de sesión?",
                "Ir a Login",
                JOptionPane.YES_NO_OPTION);
                
            if (respuesta == JOptionPane.YES_OPTION) {
                btnRegresarActionPerformed(evt);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Ocurrió un error al registrar el usuario.\n" +
                "Por favor, intente nuevamente.",
                "Error en registro",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                            
    
    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        textCorreo.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtUsuarios.setText("");
            txtContrasena.setText(""); // JPasswordField también tiene setText()
        txtIdent.setText("");
        jTipoDeIdentificacionComboBox.setSelectedIndex(0);
        jTerminosCondicionesCheckBox.setSelected(false);
        LabelCorreoInformacion.setText("-");
        LabelUsuarioInforacion.setText("-");
        LabelContraseñaInformacion.setText("-");
        LabelCorreoInformacion.setForeground(Color.BLACK);
        LabelUsuarioInforacion.setForeground(Color.BLACK);
        LabelContraseñaInformacion.setForeground(Color.BLACK);
    }

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // Aquí debes implementar la navegación a la pantalla de login
        // Por ejemplo:
         new Inicio().setVisible(true);
         this.dispose();
       
    }                                           

    private void jTerminosCondicionesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {                                                             
        // Puedes agregar alguna lógica aquí si necesitas hacer algo cuando se marca/desmarca
    }                                                            

    private void jTipoDeIdentificacionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {                                                              
        // Puedes agregar lógica para validar el formato según el tipo de identificación
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
        java.awt.EventQueue.invokeLater(() -> new registro().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel LabelContraseñaInformacion;
    private javax.swing.JLabel LabelCorreoInformacion;
    private javax.swing.JLabel LabelUsuarioInforacion;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox jTerminosCondicionesCheckBox;
    private javax.swing.JComboBox<String> jTipoDeIdentificacionComboBox;
    private javax.swing.JTextField textCorreo;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JPasswordField txtContrasena;
    private javax.swing.JTextField txtIdent;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtUsuarios;
    // End of variables declaration                   
}