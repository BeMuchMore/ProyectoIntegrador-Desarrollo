/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UX;

import UI.Conexion;
import UI.SessionManager;
import UX.PermisosManager;
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
 *
 * @author calam
 */
public class Interfaz extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Interfaz.class.getName());
    
    // Variables de instancia
    private String rutaFondoPrincipal = "";
    private String rutaLogo = "";
    private int idComplemento = -1; // ID del registro actual en la BD

    /**
     * Creates new form Interfaz
     */
    public Interfaz() {
        initComponents();
        configurarVentana();
        configurarEventos();
        cargarDatosDesdeBD();
    }
    
    private void configurarVentana() {
        setLocationRelativeTo(null);
        SessionManager session = SessionManager.getInstance();
        if (session.isLoggedIn()) {
            this.setTitle("Configuración de Interfaz - Usuario: " + session.getUsername());
        } else {
            this.setTitle("Configuración de Interfaz");
        }
    }
    
    private void configurarEventos() {
        // Botones de subida de imágenes
        SubirFondoPrincipalButton.addActionListener(e -> seleccionarYGuardarFondo());
        SubirLogoButton.addActionListener(e -> seleccionarYGuardarLogo());
        
        // Botón actualizar
        ActualizarButton.addActionListener(e -> guardarDatos());
        
        // Navegación del menú lateral
        // Cursores de mano
        ProductosAgregarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ProductosActualizarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ProductosEliminarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UsuariosAgregarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UsuariosActualizarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UsuariosEliminarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        InterfazPrincipalButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        InterfazCategoriasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        InterfazOfertasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        AdministradorPermisosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        AdministradorOtrosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabel23.setCursor(new Cursor(Cursor.HAND_CURSOR));

        jLabel23.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                salir();
            }
        });
        
        ProductosAgregarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirProductos(UX.Productos.ModoOperacion.AGREGAR);
                cerrar();
            }
        });
        
        ProductosActualizarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirProductos(UX.Productos.ModoOperacion.ACTUALIZAR);
                cerrar();
            }
        });
        
        ProductosEliminarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirProductos(UX.Productos.ModoOperacion.ELIMINAR);
                cerrar();
            }
        });
        
        UsuariosAgregarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirUsuarios("agregar");
                cerrar();
            }
        });
        
        UsuariosActualizarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirUsuarios("actualizar");
                cerrar();
            }
        });
        
        UsuariosEliminarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirUsuarios("eliminar");
                cerrar();
            }
        });
        
        InterfazPrincipalButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ya estamos en Interfaz
            }
        });
        
        InterfazCategoriasButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirCategorias();
                cerrar();
            }
        });
        
        InterfazOfertasButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Implementar si existe ventana de ofertas
            }
        });
        
        AdministradorPermisosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SessionManager session = SessionManager.getInstance();
                int userId = session.getUserId();
                if (PermisosManager.tienePermiso(userId, PermisosManager.TipoPermiso.BASE_PERMISOS, 3)) {
                    Interfaz.this.dispose();
                    new Permisos().setVisible(true);
                    cerrar();
                } else {
                    JOptionPane.showMessageDialog(Interfaz.this,
                        "No tiene permisos suficientes para acceder a esta sección.\n" +
                        "Se requiere nivel 3 o superior en BasePermisos.",
                        "Acceso Denegado",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        AdministradorOtrosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirAdministrador();
                cerrar();
            }
        });
    }
    
    private void abrirProductos(UX.Productos.ModoOperacion modo) {
        UX.Productos productos = new UX.Productos(modo);
        productos.setVisible(true);
        this.dispose();
    }
    
    private void abrirUsuarios(String modo) {
        UX.Usuarios usuarios = new UX.Usuarios();
        usuarios.setVisible(true);
        this.dispose();
    }
    
    private void abrirCategorias() {
        UX.PanelAdministracion panelAdmin = new UX.PanelAdministracion();
        panelAdmin.setVisible(true);
        this.dispose();
    }
    
    private void abrirAdministrador() {
        UX.Administrador admin = new UX.Administrador();
        admin.setVisible(true);
        this.dispose();
    }

    private void cerrar() {
        this.dispose();
    }
    
    private void salir() {
        int respuesta = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de salir?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            UI.principal principal = new UI.principal(0);
            principal.setVisible(true);
            this.dispose();
        }
    }
    
    // ===== MÉTODOS DE BASE DE DATOS =====
    
    /**
     * Carga los datos desde la tabla tb_complementos
     */
    private void cargarDatosDesdeBD() {
        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT * FROM tb_complementos ORDER BY id DESC LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                idComplemento = rs.getInt("id");
                
                // Cargar datos en los campos
                String nombreApp = rs.getString("NombreDeApp");
                String correoApp = rs.getString("CorreoApp");
                String terminos = rs.getString("TerminosCondiones");
                String fondo = rs.getString("fondoPrincipal");
                String logo = rs.getString("Logo");
                Timestamp ultimaActualizacion = rs.getTimestamp("UltimaActualizacion");
                
                if (nombreApp != null) NombreAppTextField.setText(nombreApp);
                if (correoApp != null) CorreoAppTextField.setText(correoApp);
                if (terminos != null) jTextArea1.setText(terminos);
                
                // Cargar imágenes
                if (fondo != null && !fondo.isEmpty()) {
                    rutaFondoPrincipal = fondo;
                    mostrarImagenFondo(fondo);
                }
                
                if (logo != null && !logo.isEmpty()) {
                    rutaLogo = logo;
                    mostrarImagenLogo(logo);
                }
                
                // Mostrar última actualización
                if (ultimaActualizacion != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    UltimaActualizacionLabel.setText(ultimaActualizacion.toLocalDateTime().format(formatter));
                } else {
                    UltimaActualizacionLabel.setText("Nunca");
                }
            } else {
                // No hay registros, crear uno vacío
                UltimaActualizacionLabel.setText("Nunca");
            }
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar datos", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar datos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Guarda o actualiza los datos en la base de datos
     */
    private void guardarDatos() {
        try (Connection conn = Conexion.getConnection()) {
            String nombreApp = NombreAppTextField.getText().trim();
            String correoApp = CorreoAppTextField.getText().trim();
            String terminos = jTextArea1.getText().trim();
            
            // Validaciones básicas
            if (nombreApp.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "El nombre de la aplicación no puede estar vacío", 
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String sql;
            PreparedStatement pstmt;
            
            if (idComplemento > 0) {
                // Actualizar registro existente
                sql = """
                    UPDATE tb_complementos 
                    SET NombreDeApp = ?, CorreoApp = ?, TerminosCondiones = ?, 
                        fondoPrincipal = ?, Logo = ?, UltimaActualizacion = NOW()
                    WHERE id = ?
                    """;
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nombreApp);
                pstmt.setString(2, correoApp);
                pstmt.setString(3, terminos);
                pstmt.setString(4, rutaFondoPrincipal);
                pstmt.setString(5, rutaLogo);
                pstmt.setInt(6, idComplemento);
            } else {
                // Insertar nuevo registro
                sql = """
                    INSERT INTO tb_complementos 
                    (NombreDeApp, CorreoApp, TerminosCondiones, fondoPrincipal, Logo, UltimaActualizacion)
                    VALUES (?, ?, ?, ?, ?, NOW())
                    """;
                pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, nombreApp);
                pstmt.setString(2, correoApp);
                pstmt.setString(3, terminos);
                pstmt.setString(4, rutaFondoPrincipal);
                pstmt.setString(5, rutaLogo);
            }
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                if (idComplemento <= 0) {
                    // Obtener el ID del nuevo registro
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        idComplemento = rs.getInt(1);
                    }
                }
                
                // Actualizar label de última actualización
                UltimaActualizacionLabel.setText(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                
                JOptionPane.showMessageDialog(this, 
                    "Datos guardados correctamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se pudieron guardar los datos", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al guardar datos", e);
            JOptionPane.showMessageDialog(this, 
                "Error al guardar datos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ===== MÉTODOS DE MANEJO DE IMÁGENES =====
    
    /**
     * Selecciona y guarda la imagen de fondo
     */
    private void seleccionarYGuardarFondo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Imagen de Fondo");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            try {
                String rutaGuardada = guardarImagen(archivoSeleccionado, "fondo");
                rutaFondoPrincipal = rutaGuardada;
                mostrarImagenFondo(rutaGuardada);
                JOptionPane.showMessageDialog(this, 
                    "Imagen de fondo guardada correctamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                logger.log(java.util.logging.Level.SEVERE, "Error al guardar imagen", e);
                JOptionPane.showMessageDialog(this, 
                    "Error al guardar imagen: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Selecciona y guarda el logo
     */
    private void seleccionarYGuardarLogo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Logo");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            try {
                String rutaGuardada = guardarImagen(archivoSeleccionado, "logo");
                rutaLogo = rutaGuardada;
                mostrarImagenLogo(rutaGuardada);
                JOptionPane.showMessageDialog(this, 
                    "Logo guardado correctamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                logger.log(java.util.logging.Level.SEVERE, "Error al guardar logo", e);
                JOptionPane.showMessageDialog(this, 
                    "Error al guardar logo: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Guarda una imagen en la carpeta de complementos
     */
    private String guardarImagen(File archivoOrigen, String tipo) throws IOException {
        if (archivoOrigen == null || !archivoOrigen.exists()) {
            throw new IOException("Archivo de origen no válido");
        }
        
        // Obtener la carpeta de imágenes usando ConfigImagenes
        File carpetaBase = UI.ConfigImagenes.obtenerCarpetaImagenes();
        File carpetaImagenes = new File(carpetaBase, "complementos");
        
        if (!carpetaImagenes.exists()) {
            boolean creada = carpetaImagenes.mkdirs();
            if (!creada) {
                throw new IOException("No se pudo crear la carpeta de imágenes: " + carpetaImagenes.getAbsolutePath());
            }
        }
        
        String extension = obtenerExtension(archivoOrigen.getName());
        String nombreUnico = generarNombreUnico(tipo, extension);
        
        Path rutaDestino = Paths.get(carpetaImagenes.getAbsolutePath(), nombreUnico);
        
        Files.copy(archivoOrigen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("Imagen guardada: " + rutaDestino.toString());
        
        // Retornar ruta relativa para la BD
        return "Z-Global-Img/complementos/" + nombreUnico;
    }
    
    /**
     * Convierte una ruta relativa de BD a ruta absoluta del sistema
     * Usa ConfigImagenes para centralizar la lógica
     */
    private String obtenerRutaAbsoluta(String rutaBD) {
        return UI.ConfigImagenes.obtenerRutaAbsoluta(rutaBD);
    }
    
    /**
     * Muestra la imagen de fondo en el label correspondiente
     */
    private void mostrarImagenFondo(String rutaImagen) {
        try {
            String rutaAbsoluta = obtenerRutaAbsoluta(rutaImagen);
            File archivo = new File(rutaAbsoluta);
            
            if (archivo.exists()) {
                ImageIcon icon = new ImageIcon(rutaAbsoluta);
                Image img = icon.getImage();
                // Escalar para que quepa en el panel (280x130)
                Image imgEscalada = img.getScaledInstance(250, 100, Image.SCALE_SMOOTH);
                FondoVenetanaPrincipal.setIcon(new ImageIcon(imgEscalada));
                FondoVenetanaPrincipal.setText("");
            } else {
                FondoVenetanaPrincipal.setText("Imagen no encontrada");
                logger.warning("No se encontró la imagen en: " + rutaAbsoluta);
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING, "Error al mostrar imagen de fondo", e);
            FondoVenetanaPrincipal.setText("Error al cargar imagen");
        }
    }
    
    /**
     * Muestra el logo en el label correspondiente
     */
    private void mostrarImagenLogo(String rutaImagen) {
        try {
            String rutaAbsoluta = obtenerRutaAbsoluta(rutaImagen);
            File archivo = new File(rutaAbsoluta);
            
            if (archivo.exists()) {
                ImageIcon icon = new ImageIcon(rutaAbsoluta);
                Image img = icon.getImage();
                // Escalar para que quepa en el panel (280x160)
                Image imgEscalada = img.getScaledInstance(200, 120, Image.SCALE_SMOOTH);
                jLabel6.setIcon(new ImageIcon(imgEscalada));
                jLabel6.setText("");
            } else {
                jLabel6.setText("Imagen no encontrada");
                logger.warning("No se encontró el logo en: " + rutaAbsoluta);
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING, "Error al mostrar logo", e);
            jLabel6.setText("Error al cargar imagen");
        }
    }
    
    /**
     * Obtiene la extensión de un archivo
     */
    private String obtenerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto > 0 && ultimoPunto < nombreArchivo.length() - 1) {
            return nombreArchivo.substring(ultimoPunto + 1).toLowerCase();
        }
        return "jpg";
    }
    
    /**
     * Genera un nombre único para el archivo
     */
    private String generarNombreUnico(String tipo, String extension) {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = ahora.format(formatter);
        return tipo + "_" + timestamp + "." + extension;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelb = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        FondoVenetanaPrincipal = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        NombreAppTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        CorreoAppTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        UltimaActualizacionLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        SubirFondoPrincipalButton = new javax.swing.JButton();
        SubirLogoButton = new javax.swing.JButton();
        ActualizarButton = new javax.swing.JButton();
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

        jPanelb.setBackground(new java.awt.Color(255, 255, 255));
        jPanelb.setMaximumSize(new java.awt.Dimension(949, 391));
        jPanelb.setMinimumSize(new java.awt.Dimension(949, 391));
        jPanelb.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel11.setText("Funciones Principales");
        jPanelb.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));
        jPanelb.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, -1, -1));

        jLabel4.setText("____________________________________________________________________________________________________________________________________________________________________________________________________");
        jPanelb.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 980, -1));

        FondoVenetanaPrincipal.setText("Imagen");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(FondoVenetanaPrincipal)
                .addContainerGap(224, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(FondoVenetanaPrincipal)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        jPanelb.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 280, 130));

        jLabel6.setText("Imagen");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel6)
                .addContainerGap(225, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel6)
                .addContainerGap(127, Short.MAX_VALUE))
        );

        jPanelb.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 280, 160));

        jLabel7.setText("Fondo De la Ventana Principal");
        jPanelb.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        jLabel8.setText("Logo");
        jPanelb.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, -1, -1));

        jLabel10.setText("Nombre De la App");
        jPanelb.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, -1, -1));

        NombreAppTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NombreAppTextFieldActionPerformed(evt);
            }
        });
        jPanelb.add(NombreAppTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 280, -1));

        jLabel12.setText("Terminos y Condiciones");
        jPanelb.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 170, -1, -1));

        CorreoAppTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CorreoAppTextFieldActionPerformed(evt);
            }
        });
        jPanelb.add(CorreoAppTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 280, -1));

        jLabel13.setText("Correo de la App");
        jPanelb.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, -1, -1));

        jLabel5.setText("Ultima Actualizacion");
        jPanelb.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, -1, -1));

        UltimaActualizacionLabel.setText("-");
        jPanelb.add(UltimaActualizacionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, 130, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jPanelb.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, 640, 370));

        SubirFondoPrincipalButton.setText("Subir Fondo");
        jPanelb.add(SubirFondoPrincipalButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, -1, -1));

        SubirLogoButton.setText("Subir Logo");
        jPanelb.add(SubirLogoButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 370, -1, -1));

        ActualizarButton.setText("Actualizar");
        jPanelb.add(ActualizarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 110, 130, 40));

        jPanel7.setBackground(new java.awt.Color(153, 153, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/usuario.png"))); // NOI18N
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

        ProductosEliminarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/cesta-de-la-compra.png"))); // NOI18N
        ProductosEliminarButton.setText("Eliminar");

        ProductosActualizarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar (1).png"))); // NOI18N
        ProductosActualizarButton.setText("Actualizar");

        ProductosAgregarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/mas (1).png"))); // NOI18N
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
        UsuariosAgregarButton.setText("Agregar");

        UsuariosEliminarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/borrar-usuario.png"))); // NOI18N
        UsuariosEliminarButton.setText("Eliminar");

        UsuariosActualizarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar (1).png"))); // NOI18N
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
                    .addComponent(jPanelb, javax.swing.GroupLayout.PREFERRED_SIZE, 1020, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(jPanelb, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CorreoAppTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CorreoAppTextFieldActionPerformed
        // Validación opcional en tiempo real
    }//GEN-LAST:event_CorreoAppTextFieldActionPerformed

    private void NombreAppTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NombreAppTextFieldActionPerformed
        // Validación opcional en tiempo real
    }//GEN-LAST:event_NombreAppTextFieldActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Interfaz().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ActualizarButton;
    private javax.swing.JLabel AdministradorOtrosButton;
    private javax.swing.JLabel AdministradorPermisosButton;
    private javax.swing.JTextField CorreoAppTextField;
    private javax.swing.JLabel FondoVenetanaPrincipal;
    private javax.swing.JLabel InterfazCategoriasButton;
    private javax.swing.JLabel InterfazOfertasButton;
    private javax.swing.JLabel InterfazPrincipalButton;
    private javax.swing.JTextField NombreAppTextField;
    private javax.swing.JLabel ProductosActualizarButton;
    private javax.swing.JLabel ProductosAgregarButton;
    private javax.swing.JLabel ProductosEliminarButton;
    private javax.swing.JButton SubirFondoPrincipalButton;
    private javax.swing.JButton SubirLogoButton;
    private javax.swing.JLabel UltimaActualizacionLabel;
    private javax.swing.JLabel UsuariosActualizarButton;
    private javax.swing.JLabel UsuariosAgregarButton;
    private javax.swing.JLabel UsuariosEliminarButton;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelb;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
