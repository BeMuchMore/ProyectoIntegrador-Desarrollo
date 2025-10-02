
package UI;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.border.LineBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.MediaTracker;
import javax.swing.BorderFactory;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;




public class principal extends javax.swing.JFrame {
    
      private static SessionManager instance;
    
    // Información del usuario actual
    private int userId;
    private String username;
    private String cargo;
    private boolean isLoggedIn;
    
    
  
    public principal(int userId) {
         this.userId = userId;
        this.isLoggedIn = (userId > 0);
      
        initComponents();
         configurarImagenFondo();
       
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
          configurarSesion();
        
        // Cargar productos en los paneles
        cargarCategorias();
        cargarProductosAleatorios();
        cargarProductosConMayorCantidad();
        
        // Configurar imagen principal
        cargarImagenPrincipal();
         cargarCategorias();
    cargarProductosAleatorios();
    cargarProductosConMayorCantidad();
        
        
     }
private void configurarImagenFondo() {
    try {
        // Cargar la imagen desde src/Img/fondo.jpg
        ImageIcon iconoFondo = new ImageIcon(getClass().getResource("/Img/fondo.jpg"));
        Color fondoSemiTransparente = new Color(255, 255, 255, 100);
    
        // Obtener las dimensiones del panel
        int anchoPanel = 1340;
        int altoPanel = 430;
        
        // Escalar la imagen al tamaño del panel
        Image img = iconoFondo.getImage();
        Image imgEscalada = img.getScaledInstance(anchoPanel, altoPanel, Image.SCALE_SMOOTH);
        
        // Establecer la imagen escalada en el label
        jFondoLabel.setIcon(new ImageIcon(imgEscalada));
        jFondoLabel.setText(""); // Quitar el texto "Fondo"
        jFondoLabel.setBounds(0, 0, anchoPanel, altoPanel);
        jFondoLabel.setOpaque(true);
        jFondoLabel.setBackground(fondoSemiTransparente);
        
        // Enviar el label al fondo (atrás de todos los componentes)
        jFondoPanel.setComponentZOrder(jFondoLabel, jFondoPanel.getComponentCount() - 1);
        
        //esto pa que se vean las letras
        jLabel4.setOpaque(true);
       jLabel4.setBackground(fondoSemiTransparente);
        jLabel7.setOpaque(true);
       jLabel7.setBackground(fondoSemiTransparente);
     jLabel26.setOpaque(true);
       jLabel26.setBackground(fondoSemiTransparente);
        
    } catch (Exception e) {
        System.err.println("Error al cargar imagen de fondo: " + e.getMessage());
        jFondoLabel.setText("No se pudo cargar la imagen de fondo");
    }
}


 private void configurarSesion() {
     
     
   if (isLoggedIn && userId > 0) {
    System.err.println("Referencias: " + isLoggedIn + " o " + userId);
    try (Connection conn = Conexion.getConnection()) {
        String query = "SELECT nombre FROM usuariouiux WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String username = rs.getString("nombre"); // ← aquí el cambio

            jIniciarButton.setVisible(false);
            jRegistrarButton.setVisible(false);
            jTextField1.setVisible(false);
            jLabel2.setVisible(false);

            JLabel lblUsuario = new JLabel("Bienvenido, " + username);
            lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
            lblUsuario.setBounds(1040, 50, 200, 23);
            jPanel15.add(lblUsuario);

            JButton btnCerrarSesion = new JButton("Cerrar Sesión");
            btnCerrarSesion.setBounds(1250, 50, 120, 23);
            btnCerrarSesion.addActionListener(e -> cerrarSesion());
            jPanel15.add(btnCerrarSesion);
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener datos del usuario: " + e.getMessage());
    }
}
    }
    
    
    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de cerrar sesión?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
            
        if (respuesta == JOptionPane.YES_OPTION) {
            this.dispose();
            new principal(0).setVisible(true);
        }
    }
    
   
    private void cargarCategorias() {
        // Configurar panel Blusas & Tops
        configurarPanelCategoria(jBlusasTopsPanel, "Blusas & Tops", 
            obtenerImagenCategoria("Blusas & Tops"), "blusas-tops");
        
        
        // Configurar panel Pantalones
        configurarPanelCategoria(jPantalonesPanel, "Pantalones", 
            obtenerImagenCategoria("Pantalones"), "pantalones");
        
        // Configurar panel Vestidos
        configurarPanelCategoria(jVestidosPanel, "Vestidos", 
            obtenerImagenCategoria("Vestidos"), "vestidos");
        System.out.println("url arriba " + obtenerImagenCategoria("Vestidos"));
        // Configurar panel Calzado
        configurarPanelCategoria(jCalzadoPanel, "Calzado", 
            obtenerImagenCategoria("Calzado"), "calzado");
        
        // Configurar panel Accesorios
        configurarPanelCategoria(jAccesoriosPanel, "Accesorios", 
            obtenerImagenCategoria("Accesorios"), "accesorios");
    }
    
   
    private void configurarPanelCategoria(JPanel panel, String nombreCategoria, 
                                           String rutaImagen, String categoriaId) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        
        // Cargar imagen
        ImageIcon imagen = null;
          System.out.println("url que necesito" + rutaImagen  );
        
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            imagen = ConfigImagenes.cargarImagenEscalada(rutaImagen, 
                panel.getWidth() > 0 ? panel.getWidth() : 210, 
                panel.getHeight() > 0 ? panel.getHeight() - 30 : 190);
        }
        
        if (imagen == null) {
            // Si no hay imagen, usar un color de fondo
            JPanel imagenPanel = new JPanel();
            imagenPanel.setBackground(new Color(240, 240, 240));
            JLabel noImagen = new JLabel("Sin imagen", SwingConstants.CENTER);
            noImagen.setForeground(Color.GRAY);
            imagenPanel.add(noImagen);
            panel.add(imagenPanel, BorderLayout.CENTER);
        } else {
            JLabel lblImagen = new JLabel(imagen);
            lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(lblImagen, BorderLayout.CENTER);
        }
        
        // Agregar etiqueta de categoría
        JLabel lblCategoria = new JLabel(nombreCategoria, SwingConstants.CENTER);
        lblCategoria.setFont(new Font("Arial", Font.BOLD, 14));
        lblCategoria.setBackground(new Color(245, 245, 245));
        lblCategoria.setOpaque(true);
        lblCategoria.setPreferredSize(new Dimension(panel.getWidth(), 30));
        panel.add(lblCategoria, BorderLayout.SOUTH);
        
        // Agregar efecto hover y click
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(new LineBorder(Color.BLUE, 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarProductosCategoria(categoriaId, nombreCategoria);
            }
        });
        
        panel.revalidate();
        panel.repaint();
    }
    
    /**
     * Obtiene la imagen de un producto de una categoría específica
     */
    private String obtenerImagenCategoria(String categoria) {
        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT ImgUrl FROM tb_productos WHERE Categorias LIKE ? LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(query);
           
            pstmt.setString(1, "%" + categoria + "%");
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                
                return rs.getString("ImgUrl");
                
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener imagen de categoría: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Carga productos aleatorios
     */
    private void cargarProductosAleatorios() {
        List<Producto> productos = obtenerProductosAleatorios(5);
        
        if (productos.size() > 0) configurarPanelProducto(jProductoAletorio1Panel, productos.get(0));
        if (productos.size() > 1) configurarPanelProducto(jProductoAletorio2Panel, productos.get(1));
        if (productos.size() > 2) configurarPanelProducto(jProductoAletorio3Panel, productos.get(2));
        if (productos.size() > 3) configurarPanelProducto(jProductoAletorio4Panel, productos.get(3));
        if (productos.size() > 4) configurarPanelProducto(jProductoAletorio5Panel, productos.get(4));
    }
    
    /**
     * Obtiene productos aleatorios de la base de datos
     */
    private List<Producto> obtenerProductosAleatorios(int cantidad) {
        List<Producto> productos = new ArrayList<>();
        
        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT * FROM tb_productos ORDER BY RAND() LIMIT ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, cantidad);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("Nombre"));
                p.setImgUrl(rs.getString("ImgUrl"));
                p.setPrecio(rs.getDouble("Precio"));
                p.setColor(rs.getString("Color"));
                p.setTalla(rs.getString("Talla"));
                p.setCantidad(rs.getInt("Cantidad"));
                productos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos aleatorios: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Configura un panel de producto con toda la información
     */
    private void configurarPanelProducto(JPanel panel, Producto producto) {
        if (producto == null) return;
        
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        
        // Panel principal con BoxLayout para organizar verticalmente
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Color.WHITE);
        
        // Imagen del producto
        ImageIcon imagen = null;
        if (producto.getImgUrl() != null && !producto.getImgUrl().isEmpty()) {
            imagen = ConfigImagenes.cargarImagenEscalada(producto.getImgUrl(), 180, 120);
        }
        
        if (imagen != null) {
            JLabel lblImagen = new JLabel(imagen);
            lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenido.add(lblImagen);
        } else {
            JLabel lblNoImagen = new JLabel("Sin imagen");
            lblNoImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblNoImagen.setPreferredSize(new Dimension(180, 120));
            lblNoImagen.setHorizontalAlignment(SwingConstants.CENTER);
            lblNoImagen.setBackground(new Color(240, 240, 240));
            lblNoImagen.setOpaque(true);
            contenido.add(lblNoImagen);
        }
        
        // Espacio
        contenido.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Nombre del producto
        JLabel lblNombre = new JLabel(producto.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenido.add(lblNombre);
        
        // Precio
        JLabel lblPrecio = new JLabel("$" + String.format("%.2f", producto.getPrecio()));
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecio.setForeground(new Color(0, 100, 0));
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenido.add(lblPrecio);
        
        // Color y Talla
        JLabel lblDetalles = new JLabel(
            "<html><center>Color: " + (producto.getColor() != null ? producto.getColor() : "N/A") + 
            "<br>Talla: " + (producto.getTalla() != null ? producto.getTalla() : "N/A") + "</center></html>"
        );
        lblDetalles.setFont(new Font("Arial", Font.PLAIN, 10));
        lblDetalles.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenido.add(lblDetalles);
        
        // Espacio
        contenido.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Botón Ver Detalles
        JButton btnVer = new JButton("Ver Detalles");
        btnVer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVer.setFont(new Font("Arial", Font.PLAIN, 11));
        btnVer.addActionListener(e -> mostrarDetallesProducto(producto));
        contenido.add(btnVer);
        
        panel.add(contenido, BorderLayout.CENTER);
        
        // Efecto hover
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(new LineBorder(Color.BLUE, 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            }
        });
        
        panel.revalidate();
        panel.repaint();
    }
    
    /**
     * Carga productos con mayor cantidad
     */
    private void cargarProductosConMayorCantidad() {
        List<Producto> productos = obtenerProductosConMayorCantidad(4);
        
        if (productos.size() > 0) configurarPanelProducto(jProductosConMayorCantidadPanel, productos.get(0));
        if (productos.size() > 1) configurarPanelProducto(jProductosConMayorCantidad2Panel, productos.get(1));
        if (productos.size() > 2) configurarPanelProducto(jProductosConMayorCantidad3Panel, productos.get(2));
        if (productos.size() > 3) configurarPanelProducto(jProductosConMayorCantidad4Panel, productos.get(3));
    }
    
    /**
     * Obtiene productos con mayor cantidad en stock
     */
    private List<Producto> obtenerProductosConMayorCantidad(int limite) {
        List<Producto> productos = new ArrayList<>();
        
        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT * FROM tb_productos ORDER BY Cantidad DESC LIMIT ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, limite);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("Nombre"));
                p.setImgUrl(rs.getString("ImgUrl"));
                p.setPrecio(rs.getDouble("Precio"));
                p.setColor(rs.getString("Color"));
                p.setTalla(rs.getString("Talla"));
                p.setCantidad(rs.getInt("Cantidad"));
                productos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos con mayor cantidad: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Carga la imagen principal
     */
    private void cargarImagenPrincipal() {
        // Intentar cargar una imagen destacada
        try {
            String rutaImagen = "Z-Global-Img/banner-principal.jpg"; // Ajusta según tu imagen
            ImageIcon imagen = ConfigImagenes.cargarImagenEscalada(rutaImagen, 450, 120);
            if (imagen != null) {
                // Quitar el texto si hay imagen
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen principal: " + e.getMessage());
        }
    }
    
    /**
     * Muestra los detalles de un producto
     */
    private void mostrarDetallesProducto(Producto producto) {
        String mensaje = String.format(
            "Producto: %s\n" +
            "Precio: $%.2f\n" +
            "Color: %s\n" +
            "Talla: %s\n" +
            "Stock disponible: %d unidades",
            producto.getNombre(),
            producto.getPrecio(),
            producto.getColor() != null ? producto.getColor() : "N/A",
            producto.getTalla() != null ? producto.getTalla() : "N/A",
            producto.getCantidad()
        );
        
        JOptionPane.showMessageDialog(this, mensaje, 
            "Detalles del Producto", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Muestra productos de una categoría específica
     */
    private void mostrarProductosCategoria(String categoriaId, String nombreCategoria) {
        JOptionPane.showMessageDialog(this, 
            "Mostrando productos de la categoría: " + nombreCategoria,
            "Categoría", 
            JOptionPane.INFORMATION_MESSAGE);
        // Aquí puedes abrir una nueva ventana con los productos de esa categoría
    }
    
    // Clase interna para manejar productos
    private class Producto {
        private int id;
        private String nombre;
        private String imgUrl;
        private double precio;
        private String color;
        private String talla;
        private int cantidad;
        
        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getImgUrl() { return imgUrl; }
        public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
        
        public double getPrecio() { return precio; }
        public void setPrecio(double precio) { this.precio = precio; }
        
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        
        public String getTalla() { return talla; }
        public void setTalla(String talla) { this.talla = talla; }
        
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
    
    // Clase para paneles redondeados (si no existe)
    class RoundedPanel extends JPanel {
        private int cornerRadius;
        
        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        }
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jRegistrarButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jIniciarButton = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel10 = new RoundedPanel(30);
        jFondoPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jFondoLabel = new javax.swing.JLabel();
        jComprarButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jBlusasTopsPanel = new javax.swing.JPanel();
        jPantalonesPanel = new javax.swing.JPanel();
        jVestidosPanel = new javax.swing.JPanel();
        jCalzadoPanel = new javax.swing.JPanel();
        jAccesoriosPanel = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jProductoAletorio5Panel = new javax.swing.JPanel();
        jProductoAletorio1Panel = new javax.swing.JPanel();
        jProductoAletorio2Panel = new javax.swing.JPanel();
        jProductoAletorio3Panel = new javax.swing.JPanel();
        jProductoAletorio4Panel = new javax.swing.JPanel();
        jNuevasLlegadas1Button = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jOfertasButton = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jProductosConMayorCantidadPanel = new javax.swing.JPanel();
        jProductosConMayorCantidad2Panel = new javax.swing.JPanel();
        jProductosConMayorCantidad3Panel = new javax.swing.JPanel();
        jProductosConMayorCantidad4Panel = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jBuscarTextField = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jContactoButton = new javax.swing.JButton();
        jGuiaTallasButton = new javax.swing.JButton();
        jEnviosDevolucionesButton = new javax.swing.JButton();
        jPegruntasFrecuentesButton = new javax.swing.JButton();
        jMujerButton = new javax.swing.JButton();
        jNuevasLlegadasButton = new javax.swing.JButton();
        jOfertas2Button = new javax.swing.JButton();
        jSobreNosotrosButton = new javax.swing.JButton();
        jCarrerasButton = new javax.swing.JButton();
        jPoliticaPrivacidadButton = new javax.swing.JButton();
        jTerminosServicioButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(1404, 650));
        jPanel1.setMinimumSize(new java.awt.Dimension(1404, 650));
        jPanel1.setPreferredSize(new java.awt.Dimension(1404, 650));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel15.setMaximumSize(new java.awt.Dimension(70, 120));
        jPanel15.setMinimumSize(new java.awt.Dimension(70, 120));
        jPanel15.setName(""); // NOI18N

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

        jLabel27.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel27.setText("Mujer");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addContainerGap())
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Nuevas Llegadas ");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Ofertas");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(25, 25, 25))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap())
        );

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setText("FASHION");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap())
        );

        jPanel22.setBackground(new java.awt.Color(248, 244, 220));

        jLabel55.setText("Envío gratis en pedidos superiores a $100");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(512, 512, 512)
                .addComponent(jLabel55)
                .addContainerGap(633, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jRegistrarButton.setText("Registrar");
        jRegistrarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRegistrarButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("o");

        jIniciarButton.setText("Iniciar");
        jIniciarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jIniciarButtonActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(107, 107, 107)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jIniciarButton)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRegistrarButton))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRegistrarButton)
                            .addComponent(jLabel2)
                            .addComponent(jIniciarButton)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel10.setMaximumSize(new java.awt.Dimension(0, 423));
        jPanel10.setMinimumSize(new java.awt.Dimension(0, 423));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jFondoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setText("Descubre nuestras últimas piezas de moda diseñadas para la mujer ");
        jFondoPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(338, 250, 730, 40));

        jLabel26.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel26.setText("Nueva Colección ");
        jFondoPanel.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 140, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel7.setText("moderna. Elegantes, atemporales y elaboradas con cuidado.");
        jFondoPanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 310, -1, -1));

        jFondoLabel.setText("Fondo");
        jFondoPanel.add(jFondoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jComprarButton.setText("Comprar Ahora");
        jFondoPanel.add(jComprarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 370, -1, -1));

        jPanel10.add(jFondoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 10, 1340, 430));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel8.setText("Comprar por Categoría");
        jPanel10.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 580, -1, -1));

        javax.swing.GroupLayout jBlusasTopsPanelLayout = new javax.swing.GroupLayout(jBlusasTopsPanel);
        jBlusasTopsPanel.setLayout(jBlusasTopsPanelLayout);
        jBlusasTopsPanelLayout.setHorizontalGroup(
            jBlusasTopsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 210, Short.MAX_VALUE)
        );
        jBlusasTopsPanelLayout.setVerticalGroup(
            jBlusasTopsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jBlusasTopsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 710, 210, 220));

        jPantalonesPanel.setPreferredSize(new java.awt.Dimension(190, 220));

        javax.swing.GroupLayout jPantalonesPanelLayout = new javax.swing.GroupLayout(jPantalonesPanel);
        jPantalonesPanel.setLayout(jPantalonesPanelLayout);
        jPantalonesPanelLayout.setHorizontalGroup(
            jPantalonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        jPantalonesPanelLayout.setVerticalGroup(
            jPantalonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jPantalonesPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 710, 220, 220));

        jVestidosPanel.setPreferredSize(new java.awt.Dimension(190, 220));

        javax.swing.GroupLayout jVestidosPanelLayout = new javax.swing.GroupLayout(jVestidosPanel);
        jVestidosPanel.setLayout(jVestidosPanelLayout);
        jVestidosPanelLayout.setHorizontalGroup(
            jVestidosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        jVestidosPanelLayout.setVerticalGroup(
            jVestidosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jVestidosPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 710, 220, 220));

        jCalzadoPanel.setPreferredSize(new java.awt.Dimension(190, 220));

        javax.swing.GroupLayout jCalzadoPanelLayout = new javax.swing.GroupLayout(jCalzadoPanel);
        jCalzadoPanel.setLayout(jCalzadoPanelLayout);
        jCalzadoPanelLayout.setHorizontalGroup(
            jCalzadoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        jCalzadoPanelLayout.setVerticalGroup(
            jCalzadoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jCalzadoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 710, 200, 220));

        javax.swing.GroupLayout jAccesoriosPanelLayout = new javax.swing.GroupLayout(jAccesoriosPanel);
        jAccesoriosPanel.setLayout(jAccesoriosPanelLayout);
        jAccesoriosPanelLayout.setHorizontalGroup(
            jAccesoriosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );
        jAccesoriosPanelLayout.setVerticalGroup(
            jAccesoriosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jAccesoriosPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 710, 190, 220));

        jLabel14.setText("Nuevas Llegadas");
        jPanel10.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 1040, -1, -1));

        jLabel15.setText("Mantente a la vanguardia de las tendencias con nuestras últimas piezas de moda,");
        jPanel10.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 1080, -1, -1));

        jLabel16.setText("cuidadosamente seleccionadas por estilo y calidad.");
        jPanel10.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 1100, -1, -1));

        jPanel11.setPreferredSize(new java.awt.Dimension(190, 220));

        jLabel18.setText("Calzado");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel18)
                .addContainerGap(101, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(198, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addContainerGap())
        );

        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 710, 200, 220));

        jPanel12.setPreferredSize(new java.awt.Dimension(190, 220));

        jLabel19.setText("Vestidos");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel19)
                .addContainerGap(116, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(198, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addContainerGap())
        );

        jPanel10.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 710, 220, 220));

        jPanel13.setPreferredSize(new java.awt.Dimension(190, 220));

        jLabel20.setText("Pantalones");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel20)
                .addContainerGap(110, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(198, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addContainerGap())
        );

        jPanel10.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 710, 220, 220));

        jLabel23.setText("Accesorios");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel23)
                .addContainerGap(80, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(198, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addContainerGap())
        );

        jPanel10.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 710, 190, 220));

        javax.swing.GroupLayout jProductoAletorio5PanelLayout = new javax.swing.GroupLayout(jProductoAletorio5Panel);
        jProductoAletorio5Panel.setLayout(jProductoAletorio5PanelLayout);
        jProductoAletorio5PanelLayout.setHorizontalGroup(
            jProductoAletorio5PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );
        jProductoAletorio5PanelLayout.setVerticalGroup(
            jProductoAletorio5PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jProductoAletorio5Panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 1160, -1, -1));

        javax.swing.GroupLayout jProductoAletorio1PanelLayout = new javax.swing.GroupLayout(jProductoAletorio1Panel);
        jProductoAletorio1Panel.setLayout(jProductoAletorio1PanelLayout);
        jProductoAletorio1PanelLayout.setHorizontalGroup(
            jProductoAletorio1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );
        jProductoAletorio1PanelLayout.setVerticalGroup(
            jProductoAletorio1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jProductoAletorio1Panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 1160, -1, -1));

        jProductoAletorio2Panel.setPreferredSize(new java.awt.Dimension(190, 220));

        javax.swing.GroupLayout jProductoAletorio2PanelLayout = new javax.swing.GroupLayout(jProductoAletorio2Panel);
        jProductoAletorio2Panel.setLayout(jProductoAletorio2PanelLayout);
        jProductoAletorio2PanelLayout.setHorizontalGroup(
            jProductoAletorio2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        jProductoAletorio2PanelLayout.setVerticalGroup(
            jProductoAletorio2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jProductoAletorio2Panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 1160, 220, -1));

        jProductoAletorio3Panel.setPreferredSize(new java.awt.Dimension(190, 220));

        javax.swing.GroupLayout jProductoAletorio3PanelLayout = new javax.swing.GroupLayout(jProductoAletorio3Panel);
        jProductoAletorio3Panel.setLayout(jProductoAletorio3PanelLayout);
        jProductoAletorio3PanelLayout.setHorizontalGroup(
            jProductoAletorio3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        jProductoAletorio3PanelLayout.setVerticalGroup(
            jProductoAletorio3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jProductoAletorio3Panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 1160, 220, -1));

        jProductoAletorio4Panel.setPreferredSize(new java.awt.Dimension(190, 220));

        javax.swing.GroupLayout jProductoAletorio4PanelLayout = new javax.swing.GroupLayout(jProductoAletorio4Panel);
        jProductoAletorio4Panel.setLayout(jProductoAletorio4PanelLayout);
        jProductoAletorio4PanelLayout.setHorizontalGroup(
            jProductoAletorio4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        jProductoAletorio4PanelLayout.setVerticalGroup(
            jProductoAletorio4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jProductoAletorio4Panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 1160, 200, -1));

        jNuevasLlegadas1Button.setText("Ver Todas Las Nuevas Llegadas");
        jPanel10.add(jNuevasLlegadas1Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 1430, -1, -1));

        jPanel26.setBackground(new java.awt.Color(245, 245, 220));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setText("Oferta Especial");
        jPanel26.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 50, -1, -1));

        jButton3.setText("jButton3");
        jPanel26.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1684, 216, -1, -1));

        jLabel37.setText("todos aman.");
        jPanel26.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 1830, -1, -1));

        jLabel44.setText("Obtén 20% de descuento en todos los artículos de nuestra Colección de Otoño. ¡Oferta por");
        jPanel26.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, -1, -1));

        jLabel45.setText("tiempo limitado!");
        jPanel26.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 110, -1, -1));

        jOfertasButton.setText("Comprar Ofertas");
        jPanel26.add(jOfertasButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 190, -1, -1));

        jPanel10.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 1500, 1340, 290));

        jLabel34.setText("Productos Destacados");
        jPanel10.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 1830, -1, -1));

        jLabel35.setText("Selecciones cuidadosamente elegidas de nuestros artículos más populares. Descubre lo que");
        jPanel10.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 1860, -1, -1));

        jLabel36.setText("todos aman.");
        jPanel10.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 1890, -1, -1));

        jProductosConMayorCantidadPanel.setPreferredSize(new java.awt.Dimension(190, 220));

        javax.swing.GroupLayout jProductosConMayorCantidadPanelLayout = new javax.swing.GroupLayout(jProductosConMayorCantidadPanel);
        jProductosConMayorCantidadPanel.setLayout(jProductosConMayorCantidadPanelLayout);
        jProductosConMayorCantidadPanelLayout.setHorizontalGroup(
            jProductosConMayorCantidadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        jProductosConMayorCantidadPanelLayout.setVerticalGroup(
            jProductosConMayorCantidadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jProductosConMayorCantidadPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 1960, 220, -1));

        jProductosConMayorCantidad2Panel.setPreferredSize(new java.awt.Dimension(190, 220));

        javax.swing.GroupLayout jProductosConMayorCantidad2PanelLayout = new javax.swing.GroupLayout(jProductosConMayorCantidad2Panel);
        jProductosConMayorCantidad2Panel.setLayout(jProductosConMayorCantidad2PanelLayout);
        jProductosConMayorCantidad2PanelLayout.setHorizontalGroup(
            jProductosConMayorCantidad2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        jProductosConMayorCantidad2PanelLayout.setVerticalGroup(
            jProductosConMayorCantidad2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jProductosConMayorCantidad2Panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 1960, 220, -1));

        jProductosConMayorCantidad3Panel.setPreferredSize(new java.awt.Dimension(190, 220));

        javax.swing.GroupLayout jProductosConMayorCantidad3PanelLayout = new javax.swing.GroupLayout(jProductosConMayorCantidad3Panel);
        jProductosConMayorCantidad3Panel.setLayout(jProductosConMayorCantidad3PanelLayout);
        jProductosConMayorCantidad3PanelLayout.setHorizontalGroup(
            jProductosConMayorCantidad3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        jProductosConMayorCantidad3PanelLayout.setVerticalGroup(
            jProductosConMayorCantidad3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jProductosConMayorCantidad3Panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 1960, 200, -1));

        javax.swing.GroupLayout jProductosConMayorCantidad4PanelLayout = new javax.swing.GroupLayout(jProductosConMayorCantidad4Panel);
        jProductosConMayorCantidad4Panel.setLayout(jProductosConMayorCantidad4PanelLayout);
        jProductosConMayorCantidad4PanelLayout.setHorizontalGroup(
            jProductosConMayorCantidad4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );
        jProductosConMayorCantidad4PanelLayout.setVerticalGroup(
            jProductosConMayorCantidad4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jPanel10.add(jProductosConMayorCantidad4Panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 1960, 190, 220));

        jPanel31.setBackground(new java.awt.Color(245, 245, 220));

        jLabel42.setText("Mantente con Estilo");

        jLabel43.setText("Suscríbete a nuestro boletín para conocer las últimas tendencias de moda y ofertas exclusivas.");

        jButton4.setText("Buscar");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel42)
                .addGap(553, 553, 553))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addContainerGap(491, Short.MAX_VALUE)
                        .addComponent(jBuscarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel43)))
                .addGap(356, 356, 356))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jBuscarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel42)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel43)
                        .addGap(0, 58, Short.MAX_VALUE)))
                .addGap(36, 36, 36))
        );

        jPanel10.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2280, 1340, 190));

        jPanel32.setBackground(new java.awt.Color(204, 204, 255));
        jPanel32.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setText("FASHION ");
        jPanel32.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        jLabel10.setText("Descubre las últimas tendencias en");
        jPanel32.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        jLabel11.setText("moda con nuestra colección curada");
        jPanel32.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        jLabel12.setText("de ropa y accesorios de alta calidad.");
        jPanel32.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jLabel13.setText("jLabel13");
        jPanel32.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, -1));

        jLabel17.setText("jLabel17");
        jPanel32.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, -1, -1));

        jLabel21.setText("jLabel21");
        jPanel32.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, -1, -1));

        jLabel22.setText("Atención al Cliente ");
        jPanel32.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, -1, -1));

        jLabel24.setText("Compras");
        jPanel32.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 60, -1, -1));

        jLabel25.setText("Empresa");
        jPanel32.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 60, -1, -1));

        jContactoButton.setText("Contáctanos");
        jPanel32.add(jContactoButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 90, -1, -1));

        jGuiaTallasButton.setText("Guía de Tallas");
        jPanel32.add(jGuiaTallasButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 120, -1, -1));

        jEnviosDevolucionesButton.setText("Envíos y Devoluciones ");
        jPanel32.add(jEnviosDevolucionesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 150, -1, -1));

        jPegruntasFrecuentesButton.setText("Preguntas Frecuentes");
        jPanel32.add(jPegruntasFrecuentesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 180, -1, -1));

        jMujerButton.setText("Mujer");
        jPanel32.add(jMujerButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 90, -1, -1));

        jNuevasLlegadasButton.setText("Nuevas Llegadas");
        jPanel32.add(jNuevasLlegadasButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 120, -1, -1));

        jOfertas2Button.setText("Ofertas");
        jPanel32.add(jOfertas2Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 150, -1, -1));

        jSobreNosotrosButton.setText("Sobre Nosotros");
        jPanel32.add(jSobreNosotrosButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 90, -1, -1));

        jCarrerasButton.setText("Carreras");
        jPanel32.add(jCarrerasButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 120, -1, -1));

        jPoliticaPrivacidadButton.setText("Política de Privacidad");
        jPanel32.add(jPoliticaPrivacidadButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 150, -1, -1));

        jTerminosServicioButton.setText("Términos de Servicio");
        jPanel32.add(jTerminosServicioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 180, -1, -1));

        jPanel10.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2470, 1340, 250));

        jScrollPane1.setViewportView(jPanel10);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel1))
                            .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(139, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1378, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRegistrarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRegistrarButtonActionPerformed
        // TODO add your handling code here:
        registro temp = new registro();
        temp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jRegistrarButtonActionPerformed

    private void jIniciarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jIniciarButtonActionPerformed
        // TODO add your handling code here:
        
         Inicio temp = new Inicio();
        temp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jIniciarButtonActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(() -> {
        new principal(1).setVisible(true); // Usar ID temporal para testing
    });
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jAccesoriosPanel;
    private javax.swing.JPanel jBlusasTopsPanel;
    private javax.swing.JTextField jBuscarTextField;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jCalzadoPanel;
    private javax.swing.JButton jCarrerasButton;
    private javax.swing.JButton jComprarButton;
    private javax.swing.JButton jContactoButton;
    private javax.swing.JButton jEnviosDevolucionesButton;
    private javax.swing.JLabel jFondoLabel;
    private javax.swing.JPanel jFondoPanel;
    private javax.swing.JButton jGuiaTallasButton;
    private javax.swing.JButton jIniciarButton;
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
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton jMujerButton;
    private javax.swing.JButton jNuevasLlegadas1Button;
    private javax.swing.JButton jNuevasLlegadasButton;
    private javax.swing.JButton jOfertas2Button;
    private javax.swing.JButton jOfertasButton;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPantalonesPanel;
    private javax.swing.JButton jPegruntasFrecuentesButton;
    private javax.swing.JButton jPoliticaPrivacidadButton;
    private javax.swing.JPanel jProductoAletorio1Panel;
    private javax.swing.JPanel jProductoAletorio2Panel;
    private javax.swing.JPanel jProductoAletorio3Panel;
    private javax.swing.JPanel jProductoAletorio4Panel;
    private javax.swing.JPanel jProductoAletorio5Panel;
    private javax.swing.JPanel jProductosConMayorCantidad2Panel;
    private javax.swing.JPanel jProductosConMayorCantidad3Panel;
    private javax.swing.JPanel jProductosConMayorCantidad4Panel;
    private javax.swing.JPanel jProductosConMayorCantidadPanel;
    private javax.swing.JButton jRegistrarButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jSobreNosotrosButton;
    private javax.swing.JButton jTerminosServicioButton;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel jVestidosPanel;
    // End of variables declaration//GEN-END:variables
}
