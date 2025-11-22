/*
 * PRODUCTOS.JAVA - CRUD Completo de Productos
 * Gestión de productos con inventario por talla usando BD normalizada
 */
package UX;

import UI.Conexion;
import UI.SessionManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Julian David
 */
public class Productos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Productos.class.getName());
    
    // Constantes
    private static final String CARPETA_IMAGENES = "...Z-Global-Img/";
    
    // Variables de instancia
    private String imagenUrlSeleccionada = "";
    private int productoSeleccionadoId = -1;
    private ModoOperacion modoActual = ModoOperacion.AGREGAR;
    
    // Enum para modos de operación
    public enum ModoOperacion {
        AGREGAR, ACTUALIZAR, ELIMINAR
    }
    public Productos() {
    this(ModoOperacion.AGREGAR); // Modo por defecto
}

    /**
     * Constructor
     */
    public Productos(ModoOperacion modo) {
        
        initComponents();
         cambiarModo(modo);
        configurarVentana();
        configurarComponentesIniciales();
        cargarDatosComboBoxes();
        configurarEventos();
        cargarTallasEnTabla();
        cambiarModo(ModoOperacion.AGREGAR);
    }
    
    // ===== CONFIGURACIÓN INICIAL =====
    
    private void configurarVentana() {
        setResizable(false);
        setLocationRelativeTo(null);
        
        SessionManager session = SessionManager.getInstance();
        this.setTitle("Gestión de Productos - Usuario: " + session.getUsername());
        
        System.out.println("Usuario " + session.getUsername() + " accedió a Productos");
    }
    
    private void configurarComponentesIniciales() {
        // ComboBoxes vs TextFields
        MaterialesBaseComboBox.setVisible(true);
        jTextFieldMaterialBase.setVisible(false);
        
        ColeccionComboBox.setVisible(true);
        ColeccionTextField.setVisible(false);
        
        // Configurar cursores
        jLabelCambiarMaterialBase.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabelCambiarColeccion1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabelCambiarMaterialBase.setToolTipText("Cambiar a entrada manual");
        jLabelCambiarColeccion1.setToolTipText("Cambiar a entrada manual");
        
        // Configurar tabla de tallas
        TablaCantidades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void configurarEventos() {
        // Botón subir imagen
        jSubirImgButton.addActionListener(e -> seleccionarYGuardarImagen());
        
        // Botón limpiar
        jButton1.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this,
                "¿Limpiar todos los campos?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                limpiarFormulario();
            }
        });
        
        // Labels de cambio
        jLabelCambiarMaterialBase.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                alternarMaterialBase();
            }
        });
        
        jLabelCambiarColeccion1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                alternarColeccion();
            }
        });
        
        // ComboBoxes especiales
        MaterialesBaseComboBox.addActionListener(e -> {
            if ("+ Otro Material".equals(MaterialesBaseComboBox.getSelectedItem())) {
                alternarMaterialBase();
            }
        });
        
        ColeccionComboBox.addActionListener(e -> {
            if ("+ Nueva Colección".equals(ColeccionComboBox.getSelectedItem())) {
                alternarColeccion();
            }
        });
        
        // Botones del panel lateral
        ProductosAgregarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cambiarModo(ModoOperacion.AGREGAR);
            }
        });
        
        ProductosActualizarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cambiarModo(ModoOperacion.ACTUALIZAR);
            }
        });
        
        ProductosEliminarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cambiarModo(ModoOperacion.ELIMINAR);
            }
        });
        
        // Listener de tabla para calcular total
        TablaCantidades.getModel().addTableModelListener(e -> calcularTotalCantidades());
    }
    
    // ===== GESTIÓN DE MODOS =====
    
    private void cambiarModo(ModoOperacion modo) {
        this.modoActual = modo;
        
        // Ocultar todos los labels y botones
        LabelAgregar.setVisible(false);
        LabelActualizar.setVisible(false);
        labelEliminar.setVisible(false);
        AgregarButton.setVisible(false);
        ActualizarButton.setVisible(false);
        Eliminarbutton.setVisible(false);
        
        // Mostrar según modo
        switch (modo) {
            case AGREGAR:
                LabelAgregar.setVisible(true);
                AgregarButton.setVisible(true);
                jBuscarTextFiel.setEnabled(false);
                jBuscarButton.setEnabled(false);
                limpiarFormulario();
                break;
                
            case ACTUALIZAR:
                LabelActualizar.setVisible(true);
                ActualizarButton.setVisible(true);
                jBuscarTextFiel.setEnabled(true);
                jBuscarButton.setEnabled(true);
                limpiarFormulario();
                break;
                
            case ELIMINAR:
                labelEliminar.setVisible(true);
                Eliminarbutton.setVisible(true);
                jBuscarTextFiel.setEnabled(true);
                jBuscarButton.setEnabled(true);
                limpiarFormulario();
                break;
        }
        
        System.out.println("Modo cambiado a: " + modo);
    }
    
    // ===== CARGA DE TALLAS DESDE BD =====
    
    private void cargarTallasEnTabla() {
        String sql = "SELECT id, nombre FROM tb_tallas ORDER BY id";
        
        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Talla", "Cantidad", "Precio"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return String.class;
                return Integer.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0; // Solo cantidad y precio son editables
            }
        };
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombre"),
                    0,  // Cantidad inicial
                    0   // Precio inicial
                });
            }
            
            // Si no hay tallas en la BD, agregar tallas por defecto
            if (modelo.getRowCount() == 0) {
                String[] tallasDefault = {"XS", "S", "M", "L", "XL", "26", "28", "30", 
                    "32", "34", "36", "37", "38", "39", "40", "41", "Única"};
                
                for (String talla : tallasDefault) {
                    modelo.addRow(new Object[]{talla, 0, 0});
                }
                
                // Insertar tallas en BD
                insertarTallasDefault(tallasDefault);
            }
            
            TablaCantidades.setModel(modelo);
            
            // Configurar ancho de columnas
            TablaCantidades.getColumnModel().getColumn(0).setPreferredWidth(80);
            TablaCantidades.getColumnModel().getColumn(1).setPreferredWidth(100);
            TablaCantidades.getColumnModel().getColumn(2).setPreferredWidth(100);
            
            System.out.println("Tallas cargadas en tabla: " + modelo.getRowCount());
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar tallas", e);
            JOptionPane.showMessageDialog(this,
                "Error al cargar tallas desde la base de datos.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void insertarTallasDefault(String[] tallas) {
        String sql = "INSERT INTO tb_tallas (nombre) VALUES (?)";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (String talla : tallas) {
                pstmt.setString(1, talla);
                pstmt.executeUpdate();
            }
            
            System.out.println("Tallas por defecto insertadas en BD");
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.WARNING, "Error al insertar tallas default", e);
        }
    }
    
    private void calcularTotalCantidades() {
        int total = 0;
        DefaultTableModel modelo = (DefaultTableModel) TablaCantidades.getModel();
        
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object valor = modelo.getValueAt(i, 1); // Columna cantidad
            if (valor != null) {
                try {
                    total += Integer.parseInt(valor.toString());
                } catch (NumberFormatException e) {
                    // Ignorar valores no numéricos
                }
            }
        }
        
        SumaCantidades.setText(String.valueOf(total));
    }
    
    // ===== COMBOBOXES DINÁMICOS =====
    
    private void cargarDatosComboBoxes() {
        cargarColecciones();
        cargarMaterialesBase();
    }
    
    private void cargarColecciones() {
        String sql = "SELECT DISTINCT Colección FROM tb_productos WHERE Colección IS NOT NULL AND Colección != '' ORDER BY Colección";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            ColeccionComboBox.removeAllItems();
            ColeccionComboBox.addItem("-Seleccione-");
            
            while (rs.next()) {
                String coleccion = rs.getString("Colección");
                if (coleccion != null && !coleccion.trim().isEmpty()) {
                    ColeccionComboBox.addItem(coleccion.trim());
                }
            }
            
            ColeccionComboBox.addItem("+ Nueva Colección");
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.WARNING, "Error al cargar colecciones", e);
        }
    }
    
    private void cargarMaterialesBase() {
        String sql = "SELECT DISTINCT MaterialBase FROM tb_productos WHERE MaterialBase IS NOT NULL AND MaterialBase != '' ORDER BY MaterialBase";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            String[] materialesPredeterminados = {
                "-Ninguno-", "Acrílico", "Algodón", "Algodón orgánico", "Bambú", 
                "Cachemira", "Cáñamo", "Chiffón", "Corcho", "Cuero", "Denim", 
                "Elastano", "Encaje", "Gamuza", "Lana", "Lino", "Microfibra", 
                "Modal", "Nylon", "Piñatex", "Poliamida", "Poliéster", "Rayón", 
                "Satinado", "Seda", "Tencel", "Terciopelo", "Tul"
            };
            
            MaterialesBaseComboBox.removeAllItems();
            for (String material : materialesPredeterminados) {
                MaterialesBaseComboBox.addItem(material);
            }
            
            java.util.Set<String> materialesUnicos = new java.util.HashSet<>(
                java.util.Arrays.asList(materialesPredeterminados));
            
            while (rs.next()) {
                String material = rs.getString("MaterialBase");
                if (material != null && !material.trim().isEmpty() && 
                    !materialesUnicos.contains(material.trim())) {
                    MaterialesBaseComboBox.addItem(material.trim());
                    materialesUnicos.add(material.trim());
                }
            }
            
            MaterialesBaseComboBox.addItem("+ Otro Material");
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.WARNING, "Error al cargar materiales", e);
        }
    }
    
    // ===== ALTERNAR COMBOBOX/TEXTFIELD =====
    
    private void alternarMaterialBase() {
        boolean comboVisible = MaterialesBaseComboBox.isVisible();
        
        if (comboVisible) {
            String valorCombo = (String) MaterialesBaseComboBox.getSelectedItem();
            if (valorCombo != null && !valorCombo.equals("-Ninguno-") && 
                !valorCombo.equals("+ Otro Material")) {
                jTextFieldMaterialBase.setText(valorCombo);
            }
            
            MaterialesBaseComboBox.setVisible(false);
            jTextFieldMaterialBase.setVisible(true);
            jTextFieldMaterialBase.requestFocus();
            jLabelCambiarMaterialBase.setToolTipText("Cambiar a selección predefinida");
        } else {
            String valorTextField = jTextFieldMaterialBase.getText().trim();
            if (!valorTextField.isEmpty()) {
                boolean encontrado = false;
                for (int i = 0; i < MaterialesBaseComboBox.getItemCount(); i++) {
                    if (MaterialesBaseComboBox.getItemAt(i).equalsIgnoreCase(valorTextField)) {
                        MaterialesBaseComboBox.setSelectedIndex(i);
                        encontrado = true;
                        break;
                    }
                }
                
                if (!encontrado && !valorTextField.equals("+ Otro Material")) {
                    MaterialesBaseComboBox.addItem(valorTextField);
                    MaterialesBaseComboBox.setSelectedItem(valorTextField);
                }
            }
            
            jTextFieldMaterialBase.setVisible(false);
            MaterialesBaseComboBox.setVisible(true);
            jLabelCambiarMaterialBase.setToolTipText("Cambiar a entrada manual");
        }
    }
    
    private void alternarColeccion() {
        boolean comboVisible = ColeccionComboBox.isVisible();
        
        if (comboVisible) {
            String valorCombo = (String) ColeccionComboBox.getSelectedItem();
            if (valorCombo != null && !valorCombo.equals("-Seleccione-") && 
                !valorCombo.equals("+ Nueva Colección")) {
                ColeccionTextField.setText(valorCombo);
            }
            
            ColeccionComboBox.setVisible(false);
            ColeccionTextField.setVisible(true);
            ColeccionTextField.requestFocus();
            jLabelCambiarColeccion1.setToolTipText("Cambiar a selección predefinida");
        } else {
            String valorTextField = ColeccionTextField.getText().trim();
            if (!valorTextField.isEmpty()) {
                boolean encontrado = false;
                for (int i = 0; i < ColeccionComboBox.getItemCount(); i++) {
                    if (ColeccionComboBox.getItemAt(i).equalsIgnoreCase(valorTextField)) {
                        ColeccionComboBox.setSelectedIndex(i);
                        encontrado = true;
                        break;
                    }
                }
                
                if (!encontrado && !valorTextField.equals("+ Nueva Colección")) {
                    ColeccionComboBox.addItem(valorTextField);
                    ColeccionComboBox.setSelectedItem(valorTextField);
                }
            }
            
            ColeccionTextField.setVisible(false);
            ColeccionComboBox.setVisible(true);
            jLabelCambiarColeccion1.setToolTipText("Cambiar a entrada manual");
        }
    }
    
    private String obtenerMaterialBase() {
        if (MaterialesBaseComboBox.isVisible()) {
            String valor = (String) MaterialesBaseComboBox.getSelectedItem();
            if (valor != null && !valor.equals("-Ninguno-") && !valor.equals("+ Otro Material")) {
                return valor;
            }
            return "";
        } else {
            return jTextFieldMaterialBase.getText().trim();
        }
    }
    
    private String obtenerColeccion() {
        if (ColeccionComboBox.isVisible()) {
            String valor = (String) ColeccionComboBox.getSelectedItem();
            if (valor != null && !valor.equals("-Seleccione-") && !valor.equals("+ Nueva Colección")) {
                return valor;
            }
            return "";
        } else {
            return ColeccionTextField.getText().trim();
        }
    }
    
    // ===== GESTIÓN DE IMÁGENES =====
    
    private void seleccionarYGuardarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar imagen del producto");
        
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
                String rutaGuardada = guardarImagen(archivoSeleccionado);
                
                if (rutaGuardada != null && !rutaGuardada.isEmpty()) {
                    imagenUrlSeleccionada = rutaGuardada;
                    mostrarVistaPrevia(rutaGuardada);
                    
                    JOptionPane.showMessageDialog(this,
                        "Imagen cargada exitosamente\nArchivo: " + archivoSeleccionado.getName(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (IOException e) {
                logger.log(java.util.logging.Level.SEVERE, "Error al procesar imagen", e);
                JOptionPane.showMessageDialog(this,
                    "Error al guardar la imagen:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String guardarImagen(File archivoOrigen) throws IOException {
        if (archivoOrigen == null || !archivoOrigen.exists()) {
            throw new IOException("Archivo de origen no válido");
        }
        
        File carpetaImagenes = new File(CARPETA_IMAGENES);
        if (!carpetaImagenes.exists()) {
            boolean creada = carpetaImagenes.mkdirs();
            if (!creada) {
                throw new IOException("No se pudo crear la carpeta de imágenes: " + CARPETA_IMAGENES);
            }
        }
        
        String extension = obtenerExtension(archivoOrigen.getName());
        String nombreUnico = generarNombreUnico(extension);
        
        Path rutaDestino = Paths.get(CARPETA_IMAGENES + nombreUnico);
        
        Files.copy(archivoOrigen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("Imagen guardada: " + rutaDestino.toString());
        
        return CARPETA_IMAGENES + nombreUnico;
    }
    
    private void mostrarVistaPrevia(String rutaImagen) {
        try {
            ImageIcon icon = new ImageIcon(rutaImagen);
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
        
        return String.format("producto_%s_%s_%04d.%s", username, timestamp, random, extension);
    }
    
    private boolean esImagenValida(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || 
               nombre.endsWith(".png") || nombre.endsWith(".gif");
    }
    
    // ===== VALIDACIONES =====
    
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();
        
        if (jNombreTextFiel.getText().trim().isEmpty()) {
            errores.append("- El nombre es obligatorio\n");
        }
        
        if (!obtenerCategoriaSeleccionada()) {
            errores.append("- Debe seleccionar al menos una categoría\n");
        }
        
        // Validar que al menos una talla tenga cantidad > 0
        if (!validarTallasConCantidad()) {
            errores.append("- Debe ingresar cantidad para al menos una talla\n");
        }
        
        // Validar precios
        if (!validarPrecios()) {
            errores.append("- Las tallas con cantidad deben tener precio mayor a 0\n");
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
    
    private boolean validarTallasConCantidad() {
        DefaultTableModel modelo = (DefaultTableModel) TablaCantidades.getModel();
        
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object cantidad = modelo.getValueAt(i, 1);
            if (cantidad != null) {
                try {
                    int cant = Integer.parseInt(cantidad.toString());
                    if (cant > 0) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    // Continuar
                }
            }
        }
        
        return false;
    }
    
    private boolean validarPrecios() {
        DefaultTableModel modelo = (DefaultTableModel) TablaCantidades.getModel();
        
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object cantidad = modelo.getValueAt(i, 1);
            Object precio = modelo.getValueAt(i, 2);
            
            try {
                int cant = Integer.parseInt(cantidad != null ? cantidad.toString() : "0");
                int prec = Integer.parseInt(precio != null ? precio.toString() : "0");
                
                if (cant > 0 && prec <= 0) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean obtenerCategoriaSeleccionada() {
        return jBlusasTopsCheckBox1.isSelected() ||
               jPantalonesCheckBox2.isSelected() ||
               jVestidosCheckBox3.isSelected() ||
               jCalzadoCheckBox4.isSelected() ||
               jAccesoriosCheckBox5.isSelected();
    }
    
    private String obtenerCategoriasString() {
        StringBuilder categorias = new StringBuilder();
        
        if (jBlusasTopsCheckBox1.isSelected()) categorias.append("Blusas & Tops,");
        if (jPantalonesCheckBox2.isSelected()) categorias.append("Pantalones,");
        if (jVestidosCheckBox3.isSelected()) categorias.append("Vestidos,");
        if (jCalzadoCheckBox4.isSelected()) categorias.append("Calzado,");
        if (jAccesoriosCheckBox5.isSelected()) categorias.append("Accesorios,");
        
        if (categorias.length() > 0) {
            categorias.setLength(categorias.length() - 1);
        }
        
        return categorias.toString();
    }
    
    // ===== OPERACIÓN AGREGAR =====
    
    private void ejecutarAgregar() {
        if (!validarCampos()) {
            return;
        }
        
        String nombreProducto = jNombreTextFiel.getText().trim();
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Agregar el producto: " + nombreProducto + "?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            if (agregarProducto()) {
                JOptionPane.showMessageDialog(this,
                    "Producto agregado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                limpiarFormulario();
            }
        }
    }
    
    private boolean agregarProducto() {
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false);
            
            // 1. Insertar producto
            String sqlProducto = """
                INSERT INTO tb_productos 
                (Nombre, Categorias, Color, MaterialBase, OtrosMateriales, 
                 Colección, Descripcion, ImgUrl, FecheIngreso, estado)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), 'vigente')
                """;
            
            int idProducto;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlProducto, 
                    Statement.RETURN_GENERATED_KEYS)) {
                
                pstmt.setString(1, jNombreTextFiel.getText().trim());
                pstmt.setString(2, obtenerCategoriasString());
                pstmt.setString(3, jColorTextField.getText().trim());
                pstmt.setString(4, obtenerMaterialBase());
                pstmt.setString(5, jOtrosMaterialTextField.getText().trim());
                pstmt.setString(6, obtenerColeccion());
                pstmt.setString(7, jDescripcionText.getText().trim());
                pstmt.setString(8, imagenUrlSeleccionada);
                
                pstmt.executeUpdate();
                
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idProducto = rs.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID del producto");
                }
            }
            
            // 2. Insertar inventario por talla
            String sqlInventario = """
                INSERT INTO tb_inventario (id_producto, id_talla, cantidad, precio)
                VALUES (?, ?, ?, ?)
                """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInventario)) {
                DefaultTableModel modelo = (DefaultTableModel) TablaCantidades.getModel();
                
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    String talla = modelo.getValueAt(i, 0).toString();
                    int cantidad = Integer.parseInt(modelo.getValueAt(i, 1).toString());
                    int precio = Integer.parseInt(modelo.getValueAt(i, 2).toString());
                    
                    if (cantidad > 0) {
                        int idTalla = obtenerIdTalla(conn, talla);
                        
                        pstmt.setInt(1, idProducto);
                        pstmt.setInt(2, idTalla);
                        pstmt.setInt(3, cantidad);
                        pstmt.setInt(4, precio);
                        pstmt.addBatch();
                    }
                }
                
                pstmt.executeBatch();
            }
            
            conn.commit();
            
            System.out.println("Producto agregado con ID: " + idProducto);
            cargarDatosComboBoxes();
            
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.log(java.util.logging.Level.SEVERE, "Error en rollback", ex);
                }
            }
            
            logger.log(java.util.logging.Level.SEVERE, "Error al agregar producto", e);
            JOptionPane.showMessageDialog(this,
                "Error al agregar el producto: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
            
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.log(java.util.logging.Level.WARNING, "Error al cerrar conexión", e);
                }
            }
        }
    }
    
    private int obtenerIdTalla(Connection conn, String nombreTalla) throws SQLException {
        String sql = "SELECT id FROM tb_tallas WHERE nombre = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreTalla);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                // Si no existe, insertarla
                String sqlInsert = "INSERT INTO tb_tallas (nombre) VALUES (?)";
                try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert, 
                        Statement.RETURN_GENERATED_KEYS)) {
                    pstmtInsert.setString(1, nombreTalla);
                    pstmtInsert.executeUpdate();
                    
                    ResultSet rsInsert = pstmtInsert.getGeneratedKeys();
                    if (rsInsert.next()) {
                        return rsInsert.getInt(1);
                    }
                }
            }
        }
        
        throw new SQLException("No se pudo obtener/crear ID de talla: " + nombreTalla);
    }
    
    // ===== OPERACIÓN BUSCAR =====
    
    private void ejecutarBuscar() {
        String busqueda = jBuscarTextFiel.getText().trim();
        
        if (busqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingrese un término de búsqueda",
                "Búsqueda vacía",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<ProductoInfo> productos = buscarProductos(busqueda);
        
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No se encontraron productos",
                "Sin resultados",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        mostrarResultadosBusqueda(productos);
    }
    
    private List<ProductoInfo> buscarProductos(String termino) {
        List<ProductoInfo> productos = new ArrayList<>();
        
        String sql = """
            SELECT * FROM tb_productos 
            WHERE LOWER(Nombre) LIKE LOWER(?) 
            OR LOWER(Color) LIKE LOWER(?) 
            OR LOWER(Categorias) LIKE LOWER(?) 
            OR LOWER(MaterialBase) LIKE LOWER(?) 
            OR LOWER(Colección) LIKE LOWER(?) 
            OR id = ?
            """;
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String terminoBusqueda = "%" + termino + "%";
            pstmt.setString(1, terminoBusqueda);
            pstmt.setString(2, terminoBusqueda);
            pstmt.setString(3, terminoBusqueda);
            pstmt.setString(4, terminoBusqueda);
            pstmt.setString(5, terminoBusqueda);
            
            try {
                int id = Integer.parseInt(termino);
                pstmt.setInt(6, id);
            } catch (NumberFormatException e) {
                pstmt.setInt(6, -1);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ProductoInfo producto = new ProductoInfo();
                producto.id = rs.getInt("id");
                producto.nombre = rs.getString("Nombre");
                producto.categorias = rs.getString("Categorias");
                producto.color = rs.getString("Color");
                producto.materialBase = rs.getString("MaterialBase");
                producto.otrosMateriales = rs.getString("OtrosMateriales");
                producto.coleccion = rs.getString("Colección");
                producto.descripcion = rs.getString("Descripcion");
                producto.imgUrl = rs.getString("ImgUrl");
                producto.estado = rs.getString("estado");
                
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al buscar productos", e);
            JOptionPane.showMessageDialog(this,
                "Error al buscar productos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        return productos;
    }
    
    private void mostrarResultadosBusqueda(List<ProductoInfo> productos) {
        JDialog dialogoResultados = new JDialog(this, "Resultados de Búsqueda", true);
        dialogoResultados.setSize(600, 400);
        dialogoResultados.setLocationRelativeTo(this);
        
        DefaultListModel<ProductoInfo> modelo = new DefaultListModel<>();
        for (ProductoInfo producto : productos) {
            modelo.addElement(producto);
        }
        
        JList<ProductoInfo> lista = new JList<>(modelo);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ProductoInfo) {
                    ProductoInfo p = (ProductoInfo) value;
                    setText(String.format("ID: %d - %s (%s)", p.id, p.nombre, p.categorias));
                }
                return this;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(lista);
        
        JPanel panelBotones = new JPanel();
        JButton btnSeleccionar = new JButton("Seleccionar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnSeleccionar.addActionListener(e -> {
            ProductoInfo seleccionado = lista.getSelectedValue();
            if (seleccionado != null) {
                cargarProductoEnFormulario(seleccionado);
                dialogoResultados.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogoResultados,
                    "Seleccione un producto",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialogoResultados.dispose());
        
        panelBotones.add(btnSeleccionar);
        panelBotones.add(btnCancelar);
        
        dialogoResultados.setLayout(new java.awt.BorderLayout());
        dialogoResultados.add(scrollPane, java.awt.BorderLayout.CENTER);
        dialogoResultados.add(panelBotones, java.awt.BorderLayout.SOUTH);
        
        dialogoResultados.setVisible(true);
    }
    
    private void cargarProductoEnFormulario(ProductoInfo producto) {
        jNombreTextFiel.setText(producto.nombre);
        jColorTextField.setText(producto.color);
        jOtrosMaterialTextField.setText(producto.otrosMateriales);
        jDescripcionText.setText(producto.descripcion);
        
        // Material Base
        if (producto.materialBase != null && !producto.materialBase.isEmpty()) {
            boolean encontrado = false;
            for (int i = 0; i < MaterialesBaseComboBox.getItemCount(); i++) {
                if (MaterialesBaseComboBox.getItemAt(i).equalsIgnoreCase(producto.materialBase)) {
                    MaterialesBaseComboBox.setSelectedIndex(i);
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado) {
                jTextFieldMaterialBase.setText(producto.materialBase);
                alternarMaterialBase();
            }
        }
        
        // Colección
        if (producto.coleccion != null && !producto.coleccion.isEmpty()) {
            boolean encontrado = false;
            for (int i = 0; i < ColeccionComboBox.getItemCount(); i++) {
                if (ColeccionComboBox.getItemAt(i).equalsIgnoreCase(producto.coleccion)) {
                    ColeccionComboBox.setSelectedIndex(i);
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado) {
                ColeccionTextField.setText(producto.coleccion);
                alternarColeccion();
            }
        }
        
        // Categorías
        limpiarCheckboxes();
        if (producto.categorias != null) {
            String[] cats = producto.categorias.split(",");
            for (String cat : cats) {
                seleccionarCategoria(cat.trim());
            }
        }
        
        // Cargar inventario
        cargarInventarioProducto(producto.id);
        
        // Imagen
        if (producto.imgUrl != null && !producto.imgUrl.isEmpty()) {
            imagenUrlSeleccionada = producto.imgUrl;
            mostrarVistaPrevia(producto.imgUrl);
        }
        
        this.productoSeleccionadoId = producto.id;
    }
    
    private void cargarInventarioProducto(int idProducto) {
        String sql = """
            SELECT t.nombre, i.cantidad, i.precio
            FROM tb_inventario i
            INNER JOIN tb_tallas t ON i.id_talla = t.id
            WHERE i.id_producto = ?
            ORDER BY t.id
            """;
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            
            // Limpiar tabla
            DefaultTableModel modelo = (DefaultTableModel) TablaCantidades.getModel();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                modelo.setValueAt(0, i, 1);
                modelo.setValueAt(0, i, 2);
            }
            
            // Cargar datos
            while (rs.next()) {
                String nombreTalla = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                int precio = rs.getInt("precio");
                
                // Buscar fila correspondiente
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    if (modelo.getValueAt(i, 0).toString().equals(nombreTalla)) {
                        modelo.setValueAt(cantidad, i, 1);
                        modelo.setValueAt(precio, i, 2);
                        break;
                    }
                }
            }
            
            calcularTotalCantidades();
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar inventario", e);
        }
    }
    
    // ===== OPERACIÓN ACTUALIZAR =====
    
    private void ejecutarActualizar() {
        if (productoSeleccionadoId == -1) {
            JOptionPane.showMessageDialog(this,
                "Primero debe buscar y seleccionar un producto",
                "Producto no seleccionado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validarCampos()) {
            return;
        }
        
        String nombreProducto = jNombreTextFiel.getText().trim();
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Actualizar el producto: " + nombreProducto + "?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            if (actualizarProducto()) {
                JOptionPane.showMessageDialog(this,
                    "Producto actualizado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                limpiarFormulario();
                productoSeleccionadoId = -1;
            }
        }
    }
    
    private boolean actualizarProducto() {
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false);
            
            // 1. Actualizar producto
            String sqlProducto = """
                UPDATE tb_productos SET 
                Nombre = ?, Categorias = ?, Color = ?, MaterialBase = ?, 
                OtrosMateriales = ?, Colección = ?, Descripcion = ?, ImgUrl = ?
                WHERE id = ?
                """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sqlProducto)) {
                pstmt.setString(1, jNombreTextFiel.getText().trim());
                pstmt.setString(2, obtenerCategoriasString());
                pstmt.setString(3, jColorTextField.getText().trim());
                pstmt.setString(4, obtenerMaterialBase());
                pstmt.setString(5, jOtrosMaterialTextField.getText().trim());
                pstmt.setString(6, obtenerColeccion());
                pstmt.setString(7, jDescripcionText.getText().trim());
                pstmt.setString(8, imagenUrlSeleccionada);
                pstmt.setInt(9, productoSeleccionadoId);
                
                pstmt.executeUpdate();
            }
            
            // 2. Eliminar inventario anterior
            String sqlDelete = "DELETE FROM tb_inventario WHERE id_producto = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
                pstmt.setInt(1, productoSeleccionadoId);
                pstmt.executeUpdate();
            }
            
            // 3. Insertar nuevo inventario
            String sqlInventario = """
                INSERT INTO tb_inventario (id_producto, id_talla, cantidad, precio)
                VALUES (?, ?, ?, ?)
                """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInventario)) {
                DefaultTableModel modelo = (DefaultTableModel) TablaCantidades.getModel();
                
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    String talla = modelo.getValueAt(i, 0).toString();
                    int cantidad = Integer.parseInt(modelo.getValueAt(i, 1).toString());
                    int precio = Integer.parseInt(modelo.getValueAt(i, 2).toString());
                    
                    if (cantidad > 0) {
                        int idTalla = obtenerIdTalla(conn, talla);
                        
                        pstmt.setInt(1, productoSeleccionadoId);
                        pstmt.setInt(2, idTalla);
                        pstmt.setInt(3, cantidad);
                        pstmt.setInt(4, precio);
                        pstmt.addBatch();
                    }
                }
                
                pstmt.executeBatch();
            }
            
            conn.commit();
            
            System.out.println("Producto actualizado ID: " + productoSeleccionadoId);
            cargarDatosComboBoxes();
            
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.log(java.util.logging.Level.SEVERE, "Error en rollback", ex);
                }
            }
            
            logger.log(java.util.logging.Level.SEVERE, "Error al actualizar producto", e);
            JOptionPane.showMessageDialog(this,
                "Error al actualizar el producto: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
            
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.log(java.util.logging.Level.WARNING, "Error al cerrar conexión", e);
                }
            }
        }
    }
    
    // ===== OPERACIÓN ELIMINAR =====
    
    private void ejecutarEliminar() {
        if (productoSeleccionadoId == -1) {
            JOptionPane.showMessageDialog(this,
                "Primero debe buscar y seleccionar un producto",
                "Producto no seleccionado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombreProducto = jNombreTextFiel.getText().trim();
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el producto: " + nombreProducto + "?\n" +
            "(Se marcará como eliminado, no se borrará físicamente)",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            if (eliminarProducto()) {
                JOptionPane.showMessageDialog(this,
                    "Producto eliminado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                limpiarFormulario();
                productoSeleccionadoId = -1;
            }
        }
    }
    
    private boolean eliminarProducto() {
        String sql = "UPDATE tb_productos SET estado = 'Eliminado' WHERE id = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, productoSeleccionadoId);
            int filasActualizadas = pstmt.executeUpdate();
            
            if (filasActualizadas > 0) {
                System.out.println("Producto eliminado (soft delete) ID: " + productoSeleccionadoId);
                return true;
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el producto",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
        } catch (SQLException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al eliminar producto", e);
            JOptionPane.showMessageDialog(this,
                "Error al eliminar el producto: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // ===== UTILIDADES =====
    
    private void limpiarFormulario() {
        jNombreTextFiel.setText("");
        jColorTextField.setText("");
        jOtrosMaterialTextField.setText("");
        jDescripcionText.setText("");
        jBuscarTextFiel.setText("");
        
        // Asegurar ComboBoxes visibles
        if (!MaterialesBaseComboBox.isVisible()) {
            alternarMaterialBase();
        }
        if (!ColeccionComboBox.isVisible()) {
            alternarColeccion();
        }
        
        MaterialesBaseComboBox.setSelectedIndex(0);
        jTextFieldMaterialBase.setText("");
        ColeccionComboBox.setSelectedIndex(0);
        ColeccionTextField.setText("");
        
        // Limpiar imagen
        imagenUrlSeleccionada = "";
        VistaPreviaImg.setIcon(null);
        VistaPreviaImg.setText("imagen");
        
        // Limpiar checkboxes
        limpiarCheckboxes();
        
        // Limpiar tabla
        DefaultTableModel modelo = (DefaultTableModel) TablaCantidades.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.setValueAt(0, i, 1);
            modelo.setValueAt(0, i, 2);
        }
        
        SumaCantidades.setText("0");
        
        productoSeleccionadoId = -1;
        
        jNombreTextFiel.requestFocus();
    }
    
    private void limpiarCheckboxes() {
        jBlusasTopsCheckBox1.setSelected(false);
        jPantalonesCheckBox2.setSelected(false);
        jVestidosCheckBox3.setSelected(false);
        jCalzadoCheckBox4.setSelected(false);
        jAccesoriosCheckBox5.setSelected(false);
    }
    
    private void seleccionarCategoria(String categoria) {
        if (categoria == null) return;
        
        switch (categoria.toLowerCase()) {
            case "blusas & tops":
            case "blusas":
                jBlusasTopsCheckBox1.setSelected(true);
                break;
            case "pantalones":
                jPantalonesCheckBox2.setSelected(true);
                break;
            case "vestidos":
                jVestidosCheckBox3.setSelected(true);
                break;
            case "calzado":
                jCalzadoCheckBox4.setSelected(true);
                break;
            case "accesorios":
                jAccesoriosCheckBox5.setSelected(true);
                break;
        }
    }
    
    // ===== CLASE AUXILIAR =====
    
    private static class ProductoInfo {
        int id;
        String nombre;
        String categorias;
        String color;
        String materialBase;
        String otrosMateriales;
        String coleccion;
        String descripcion;
        String imgUrl;
        String estado;
    }

    /**
     * CÓDIGO GENERADO POR NETBEANS - NO MODIFICAR MANUALMENTE
     */
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        LabelActualizar = new javax.swing.JLabel();
        jNombreTextFiel = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jColorTextField = new javax.swing.JTextField();
        jOtrosMaterialTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ActualizarButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jDescripcionText = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jBuscarTextFiel = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jBuscarButton = new javax.swing.JButton();
        MaterialesBaseComboBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ColeccionComboBox = new javax.swing.JComboBox<>();
        jSubirImgButton = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        VistaPreviaImg = new javax.swing.JLabel();
        AgregarButton = new javax.swing.JButton();
        Eliminarbutton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jBlusasTopsCheckBox1 = new javax.swing.JCheckBox();
        jPantalonesCheckBox2 = new javax.swing.JCheckBox();
        jVestidosCheckBox3 = new javax.swing.JCheckBox();
        jCalzadoCheckBox4 = new javax.swing.JCheckBox();
        jAccesoriosCheckBox5 = new javax.swing.JCheckBox();
        // AGREGAR ESTO ANTES DE jScrollPane3
            TablaCantidades = new javax.swing.JTable();
            TablaCantidades.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null}
                },
                new String [] {
                    "Talla", "Cantidad", "Precio"
                }
            ));
            jScrollPane3 = new javax.swing.JScrollPane();
            jScrollPane3.setViewportView(TablaCantidades);
        
        
        jLabel15 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        SumaCantidades = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        LabelAgregar = new javax.swing.JLabel();
        labelEliminar = new javax.swing.JLabel();
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

        jPanel6.setBackground(new java.awt.Color(102, 102, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 268, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(jPanel6);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 6, -1, -1));

        jPanel8.setMaximumSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 476, -1, -1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMaximumSize(new java.awt.Dimension(949, 391));
        jPanel4.setMinimumSize(new java.awt.Dimension(949, 391));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LabelActualizar.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        LabelActualizar.setText("Actualizar");
        jPanel4.add(LabelActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jNombreTextFiel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNombreTextFielActionPerformed(evt);
            }
        });
        jPanel4.add(jNombreTextFiel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 260, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setText("Nombre del Producto");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, -1, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setText("Imagen");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 90, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Talla");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 290, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Color");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, -1, -1));
        
        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 310, 360, 220));

        jColorTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jColorTextFieldActionPerformed(evt);
            }
        });
        jPanel4.add(jColorTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 260, -1));

        jOtrosMaterialTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOtrosMaterialTextFieldActionPerformed(evt);
            }
        });
        jPanel4.add(jOtrosMaterialTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 260, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Material Base");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("Coleccion");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, -1, -1));

        ActualizarButton.setText("Actualizar");
        ActualizarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarButtonActionPerformed(evt);
            }
        });
        jPanel4.add(ActualizarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 460, 150, 30));

        jDescripcionText.setColumns(20);
        jDescripcionText.setRows(5);
        jScrollPane1.setViewportView(jDescripcionText);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 260, 140));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setText("Descripcion");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, -1, 20));

        jBuscarTextFiel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBuscarTextFielActionPerformed(evt);
            }
        });
        jPanel4.add(jBuscarTextFiel, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, 300, -1));
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, -1, -1));

        jBuscarButton.setText("Buscar");
        jBuscarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBuscarButtonActionPerformed(evt);
            }
        });
        jPanel4.add(jBuscarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 20, -1, -1));

        MaterialesBaseComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Ninguno-", "Acrílico", "Algodón", "Algodón orgánico", "Bambú", "Cachemira", "Cáñamo", "Chiffón", "Corcho", "Cuero", "Denim", "Elastano", "Encaje", "Gamuza", "Lana", "Lino", "Microfibra", "Modal", "Nylon", "Piñatex", "Poliamida", "Poliéster", "Rayón", "Satinado", "Seda", "Tencel", "Terciopelo", "Tul" }));
        jPanel4.add(MaterialesBaseComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 260, -1));

        jButton1.setText("Limpiar");
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 460, 110, 30));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setText("Otros Materiales");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, -1, -1));

        jLabel4.setText("____________________________________________________________________________________________________________________________________________________________________________________________________");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 980, -1));

        ColeccionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", " " }));
        jPanel4.add(ColeccionComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 260, -1));

        jSubirImgButton.setText("Subir Imagen");
        jPanel4.add(jSubirImgButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 80, -1, -1));

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

        jPanel4.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 110, 270, 280));

        AgregarButton.setText("Agregar");
        jPanel4.add(AgregarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 460, 150, 30));

        Eliminarbutton.setText("Eliminar");
        jPanel4.add(Eliminarbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 460, 150, 30));

        jBlusasTopsCheckBox1.setText("Blusas & Tops");
        jBlusasTopsCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBlusasTopsCheckBox1ActionPerformed(evt);
            }
        });

        jPantalonesCheckBox2.setText("Pantalones");
        jPantalonesCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPantalonesCheckBox2ActionPerformed(evt);
            }
        });

        jVestidosCheckBox3.setText("Vestidos");
        jVestidosCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jVestidosCheckBox3ActionPerformed(evt);
            }
        });

        jCalzadoCheckBox4.setText("Calzado");

        jAccesoriosCheckBox5.setText("Accesorios");
        jAccesoriosCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAccesoriosCheckBox5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCalzadoCheckBox4)
                    .addComponent(jBlusasTopsCheckBox1)
                    .addComponent(jPantalonesCheckBox2)
                    .addComponent(jAccesoriosCheckBox5)
                    .addComponent(jVestidosCheckBox3))
                .addContainerGap(244, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jBlusasTopsCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPantalonesCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCalzadoCheckBox4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jVestidosCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jAccesoriosCheckBox5)
                .addContainerGap(194, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel3);

        jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 110, 360, 170));
        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 310, 360, 220));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel15.setText("Categoria");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, -1, -1));

       

        LabelAgregar.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        LabelAgregar.setText("Agregar");
        jPanel4.add(LabelAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

        labelEliminar.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        labelEliminar.setText("Eliminar");
        jPanel4.add(labelEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 1020, 582));

        jPanel7.setBackground(new java.awt.Color(153, 153, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/usuario.png"))); // NOI18N
        jLabel20.setText("nombre");
         jLabel20.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        jLabel20.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               
            }
        });

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
        ProductosEliminarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
       ProductosEliminarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
             
            }
        });

        ProductosActualizarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar (1).png"))); // NOI18N
        ProductosActualizarButton.setText("Actualizar");
           ProductosActualizarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        ProductosActualizarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
             
            }
        });

        ProductosAgregarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/mas (1).png"))); // NOI18N
        ProductosAgregarButton.setText("Agregar");
          ProductosAgregarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        ProductosAgregarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
             
            }
        });


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
        UsuariosAgregarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        UsuariosAgregarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                  
                  
                        Usuarios ventana = new Usuarios();
                                    ventana.setVisible(true);
                                    cerrar();
             
            }
        });

        UsuariosEliminarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/borrar-usuario.png"))); // NOI18N
        UsuariosEliminarButton.setText("Eliminar");
          UsuariosEliminarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        UsuariosEliminarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                   Usuarios ventana = new Usuarios();
                                    ventana.setVisible(true);
                                    cerrar();
             
            }
        });

        UsuariosActualizarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar (1).png"))); // NOI18N
        UsuariosActualizarButton.setText("Actualizar");
            UsuariosActualizarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        UsuariosActualizarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                    Usuarios ventana = new Usuarios();
                                    ventana.setVisible(true);
                                    cerrar();
             
            }
        });

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
            jLabel23.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        jLabel23.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
             
            }
        });
        
        jPanel7.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 80, 20));

        jLabel24.setText("Interfaz");  
        jPanel7.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 500, -1, -1));

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        InterfazPrincipalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/hogar.png"))); // NOI18N
        InterfazPrincipalButton.setText("Principal");
          InterfazPrincipalButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
       InterfazPrincipalButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
             
            }
        });

        InterfazCategoriasButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/aplicaciones.png"))); // NOI18N
        InterfazCategoriasButton.setText("Categorias");
             InterfazCategoriasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
       InterfazCategoriasButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
             
            }
        });

        InterfazOfertasButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/corazon.png"))); // NOI18N
        InterfazOfertasButton.setText("Ofertas");
            InterfazOfertasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
       InterfazOfertasButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
             
            }
        });

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
          AdministradorPermisosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
       AdministradorPermisosButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
             
            }
        });

        AdministradorOtrosButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/menu-hamburguesa.png"))); // NOI18N
        AdministradorOtrosButton.setText("Otros");
          AdministradorOtrosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
       AdministradorOtrosButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
             
            }
        });

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

        getContentPane().add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 580));

        // Al final de initComponents(), antes del pack():

jTextFieldMaterialBase = new javax.swing.JTextField();
ColeccionTextField = new javax.swing.JTextField();
jLabelCambiarMaterialBase = new javax.swing.JLabel();
jLabelCambiarColeccion1 = new javax.swing.JLabel();

// Configurar los iconos de cambio
jLabelCambiarMaterialBase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar.png")));
jLabelCambiarColeccion1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar.png")));

// Agregar al panel
jPanel4.add(jTextFieldMaterialBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 250, -1));
jPanel4.add(ColeccionTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 250, -1));
jPanel4.add(jLabelCambiarMaterialBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 20, 20));
jPanel4.add(jLabelCambiarColeccion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 360, 20, 20));

// Cambiar los ActionListeners de los botones:
AgregarButton.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        ejecutarAgregar();
    }
});

ActualizarButton.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        ejecutarActualizar();
    }
});

Eliminarbutton.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        ejecutarEliminar();
    }
});
        pack();
    }// </editor-fold>                        
   private void cerrar(){
    this.dispose();
    }
    // ===== EVENT HANDLERS =====
    
    private void jNombreTextFielActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // Implementado
    }                                               

    private void ActualizarButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        ejecutarActualizar();
    }                                                

    private void jBuscarButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        ejecutarBuscar();
    }                                             
    
    private void jBlusasTopsCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Implementado
    }
    
    private void jPantalonesCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {
        // Implementado
    }
    
    private void jVestidosCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {
        // Implementado
    }
    
    private void jAccesoriosCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {
        // Implementado
    }
    
    private void jColorTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // Implementado
    }
    
    private void jOtrosMaterialTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // Implementado
    }
    
    private void jBuscarTextFielActionPerformed(java.awt.event.ActionEvent evt) {
        // Implementado
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
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Productos().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton ActualizarButton;
    private javax.swing.JLabel AdministradorOtrosButton;
    private javax.swing.JLabel AdministradorPermisosButton;
    private javax.swing.JButton AgregarButton;
    private javax.swing.JComboBox<String> ColeccionComboBox;
    private javax.swing.JTextField ColeccionTextField;
    private javax.swing.JButton Eliminarbutton;
    private javax.swing.JLabel InterfazCategoriasButton;
    private javax.swing.JLabel InterfazOfertasButton;
    private javax.swing.JLabel InterfazPrincipalButton;
    private javax.swing.JLabel LabelActualizar;
    private javax.swing.JLabel LabelAgregar;
    private javax.swing.JComboBox<String> MaterialesBaseComboBox;
    private javax.swing.JLabel ProductosActualizarButton;
    private javax.swing.JLabel ProductosAgregarButton;
    private javax.swing.JLabel ProductosEliminarButton;
    private javax.swing.JLabel SumaCantidades;
    private javax.swing.JTable TablaCantidades;
    private javax.swing.JLabel UsuariosActualizarButton;
    private javax.swing.JLabel UsuariosAgregarButton;
    private javax.swing.JLabel UsuariosEliminarButton;
    private javax.swing.JLabel VistaPreviaImg;
    private javax.swing.JCheckBox jAccesoriosCheckBox5;
    private javax.swing.JCheckBox jBlusasTopsCheckBox1;
    private javax.swing.JButton jBuscarButton;
    private javax.swing.JTextField jBuscarTextFiel;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCalzadoCheckBox4;
    private javax.swing.JTextField jColorTextField;
    private javax.swing.JTextArea jDescripcionText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JLabel jLabelCambiarColeccion1;
    private javax.swing.JLabel jLabelCambiarMaterialBase;
    private javax.swing.JTextField jNombreTextFiel;
    private javax.swing.JTextField jOtrosMaterialTextField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JCheckBox jPantalonesCheckBox2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton jSubirImgButton;
    private javax.swing.JTextField jTextFieldMaterialBase;
    private javax.swing.JCheckBox jVestidosCheckBox3;
    private javax.swing.JLabel labelEliminar;
    // End of variables declaration                   
}