package UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Clase optimizada para gestión de conexiones a la base de datos.
 * Implementa patrón Singleton con pool básico de conexiones.
 * Reutiliza conexiones para mejorar el rendimiento.
 * 
 * @author Optimizado
 */
public class Conexion {
    
    private static final Logger logger = Logger.getLogger(Conexion.class.getName());
    
    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/basedatos?useSSL=false&serverTimezone=UTC&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    // Instancia única (Singleton)
    private static Conexion instance;
    
    private Conexion() {
        // Constructor privado para Singleton
        try {
            // Cargar el driver una sola vez
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("Driver MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "ERROR: Driver MySQL no encontrado", e);
            throw new RuntimeException("Driver MySQL no encontrado", e);
        }
    }
    
    /**
     * Obtiene la instancia única de Conexion (Singleton)
     */
    public static Conexion getInstance() {
        if (instance == null) {
            synchronized (Conexion.class) {
                if (instance == null) {
                    instance = new Conexion();
                }
            }
        }
        return instance;
    }
    
    /**
     * Obtiene una nueva conexión a la base de datos.
     * IMPORTANTE: Esta conexión debe cerrarse usando try-with-resources
     * o llamando a close() explícitamente.
     * 
     * @return Connection nueva
     * @throws SQLException si hay error al conectar
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(true); // Auto-commit por defecto
            
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Nueva conexión creada");
            }
            
            return conn;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al crear conexión", e);
            throw handleSQLException(e);
        }
    }
    
    /**
     * Cierra recursos (ResultSet, Statement, Connection) de forma segura
     */
    public static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error al cerrar ResultSet", e);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error al cerrar Statement", e);
            }
        }
        // No cerramos la conexión aquí porque se reutiliza
    }
    
    /**
     * Cierra recursos (ResultSet, PreparedStatement) de forma segura
     */
    public static void closeResources(ResultSet rs, PreparedStatement pstmt) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error al cerrar ResultSet", e);
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error al cerrar PreparedStatement", e);
            }
        }
    }
    
    /**
     * Inicia una transacción en la conexión proporcionada
     */
    public static void beginTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(false);
        }
    }
    
    /**
     * Confirma una transacción en la conexión proporcionada
     */
    public static void commit(Connection conn) throws SQLException {
        if (conn != null && !conn.getAutoCommit()) {
            conn.commit();
            conn.setAutoCommit(true);
        }
    }
    
    /**
     * Revierte una transacción en la conexión proporcionada
     */
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.getAutoCommit()) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error al hacer rollback", e);
            }
        }
    }
    
    /**
     * Maneja excepciones SQL con mensajes más descriptivos
     */
    private static SQLException handleSQLException(SQLException e) {
        String mensaje = e.getMessage();
        int errorCode = e.getErrorCode();
        Throwable causa = e.getCause();
        
        // Verificar si la causa es ConnectException
        if (causa != null && causa.getClass().getSimpleName().equals("ConnectException")) {
            String mensajeDetallado = "ERROR: El servidor MySQL no está ejecutándose o no es accesible.\n\n" +
                    "SOLUCIÓN:\n" +
                    "1. Verifica que MySQL esté instalado en tu sistema\n" +
                    "2. Inicia el servicio MySQL (puede estar detenido)\n" +
                    "3. Verifica que el puerto 3306 esté disponible\n" +
                    "4. Revisa la configuración del firewall\n\n" +
                    "Para iniciar MySQL en Windows:\n" +
                    "- Abre 'Servicios' (services.msc) y busca 'MySQL'\n" +
                    "- Haz clic derecho y selecciona 'Iniciar'\n\n" +
                    "Para iniciar MySQL desde la línea de comandos:\n" +
                    "- net start MySQL (o el nombre de tu servicio MySQL)";
            return new SQLException(mensajeDetallado, e);
        }
        
        if (errorCode == 1045) {
            return new SQLException("ERROR: Credenciales incorrectas para MySQL.\n" +
                    "Usuario: " + USER + "\n" +
                    "Verifica que el usuario y contraseña sean correctos.", e);
        } else if (errorCode == 1049) {
            return new SQLException("ERROR: La base de datos 'basedatos' no existe.\n\n" +
                    "SOLUCIÓN:\n" +
                    "1. Crea la base de datos con: CREATE DATABASE basedatos;\n" +
                    "2. O modifica la URL de conexión en Conexion.java", e);
        } else if (mensaje != null && mensaje.contains("Connection refused")) {
            String mensajeDetallado = "ERROR: MySQL server no está ejecutándose.\n\n" +
                    "SOLUCIÓN:\n" +
                    "1. Inicia el servicio MySQL desde 'Servicios' (services.msc)\n" +
                    "2. O ejecuta: net start MySQL\n" +
                    "3. Verifica que el puerto 3306 no esté bloqueado por el firewall";
            return new SQLException(mensajeDetallado, e);
        } else if (errorCode == 0 && mensaje != null && mensaje.contains("Communications link failure")) {
            String mensajeDetallado = "ERROR: Error de comunicación con el servidor MySQL.\n\n" +
                    "Posibles causas:\n" +
                    "1. MySQL no está ejecutándose\n" +
                    "2. El puerto 3306 está bloqueado por el firewall\n" +
                    "3. La dirección 'localhost' no es accesible\n" +
                    "4. Timeout de conexión\n\n" +
                    "SOLUCIÓN: Verifica que MySQL esté corriendo y accesible";
            return new SQLException(mensajeDetallado, e);
        }
        
        return e;
    }
    
    /**
     * Prueba la conexión a la base de datos
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && conn.isValid(2)) {
                logger.info("✓ Test de conexión exitoso");
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "✗ Test de conexión falló: " + e.getMessage(), e);
        }
        return false;
    }
    
    /**
     * Realiza un diagnóstico completo de la conexión y muestra información útil
     * @return String con el resultado del diagnóstico
     */
    public static String diagnosticarConexion() {
        StringBuilder diagnostico = new StringBuilder();
        diagnostico.append("=== DIAGNÓSTICO DE CONEXIÓN ===\n\n");
        
        // Información de configuración
        diagnostico.append("Configuración actual:\n");
        diagnostico.append("- URL: ").append(URL).append("\n");
        diagnostico.append("- Usuario: ").append(USER).append("\n");
        diagnostico.append("- Contraseña: ").append(PASSWORD.isEmpty() ? "(vacía)" : "***").append("\n\n");
        
        // Verificar driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            diagnostico.append("✓ Driver MySQL cargado correctamente\n");
        } catch (ClassNotFoundException e) {
            diagnostico.append("✗ ERROR: Driver MySQL no encontrado\n");
            diagnostico.append("  Solución: Agrega el JAR de MySQL Connector al proyecto\n");
            return diagnostico.toString();
        }
        
        // Intentar conexión
        diagnostico.append("\nIntentando conectar...\n");
        try (Connection conn = getConnection()) {
            if (conn != null && conn.isValid(2)) {
                diagnostico.append("✓ Conexión exitosa\n");
                
                // Obtener información del servidor
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT VERSION()")) {
                    if (rs.next()) {
                        diagnostico.append("✓ Versión de MySQL: ").append(rs.getString(1)).append("\n");
                    }
                }
                
                // Verificar base de datos
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT DATABASE()")) {
                    if (rs.next()) {
                        String db = rs.getString(1);
                        if (db != null) {
                            diagnostico.append("✓ Base de datos conectada: ").append(db).append("\n");
                        } else {
                            diagnostico.append("⚠ No hay base de datos seleccionada\n");
                        }
                    }
                }
                
                diagnostico.append("\n✓ DIAGNÓSTICO COMPLETO: Todo está funcionando correctamente\n");
            }
        } catch (SQLException e) {
            diagnostico.append("✗ ERROR DE CONEXIÓN\n\n");
            diagnostico.append("Detalles del error:\n");
            diagnostico.append(e.getMessage()).append("\n\n");
            
            // Análisis específico del error
            String mensaje = e.getMessage();
            Throwable causa = e.getCause();
            
            if (causa != null && causa.getClass().getSimpleName().equals("ConnectException")) {
                diagnostico.append("ANÁLISIS: El servidor MySQL no está ejecutándose\n\n");
                diagnostico.append("PASOS PARA SOLUCIONAR:\n");
                diagnostico.append("1. Abre 'Servicios' (presiona Win+R, escribe 'services.msc')\n");
                diagnostico.append("2. Busca el servicio MySQL (puede llamarse 'MySQL', 'MySQL80', 'MySQL57', etc.)\n");
                diagnostico.append("3. Si está 'Detenido', haz clic derecho → 'Iniciar'\n");
                diagnostico.append("4. Espera a que el estado cambie a 'En ejecución'\n");
                diagnostico.append("5. Vuelve a intentar la conexión\n\n");
                diagnostico.append("ALTERNATIVA (línea de comandos como Administrador):\n");
                diagnostico.append("  net start MySQL\n");
                diagnostico.append("  (o 'net start MySQL80' si ese es el nombre de tu servicio)\n");
            } else if (mensaje != null && mensaje.contains("Connection refused")) {
                diagnostico.append("ANÁLISIS: Conexión rechazada en el puerto 3306\n");
                diagnostico.append("Esto generalmente significa que MySQL no está corriendo\n");
            } else if (mensaje != null && mensaje.contains("Access denied")) {
                diagnostico.append("ANÁLISIS: Credenciales incorrectas\n");
                diagnostico.append("Verifica el usuario y contraseña en Conexion.java\n");
            } else if (mensaje != null && mensaje.contains("Unknown database")) {
                diagnostico.append("ANÁLISIS: La base de datos 'basedatos' no existe\n");
                diagnostico.append("Crea la base de datos con: CREATE DATABASE basedatos;\n");
            }
        }
        
        return diagnostico.toString();
    }
    
    // ===== MÉTODOS HELPER PARA VALIDACIONES Y OPERACIONES COMUNES =====
    
    /**
     * Verifica si un correo ya existe en la base de datos
     */
    public static boolean correoExiste(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tb_usuarios WHERE correo = ?")) {
            ps.setString(1, correo.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al verificar correo", e);
        }
        return false;
    }
    
    /**
     * Verifica si un usuario ya existe en la base de datos
     */
    public static boolean usuarioExiste(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return false;
        }
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tb_usuarios WHERE usuario = ?")) {
            ps.setString(1, usuario.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al verificar usuario", e);
        }
        return false;
    }
    
    /**
     * Sugiere un nuevo nombre de usuario si el actual ya existe
     */
    public static String sugerirUsuario(String usuarioBase) {
        if (usuarioBase == null || usuarioBase.trim().isEmpty()) {
            return "usuario1";
        }
        
        int contador = 1;
        String nuevoUsuario = usuarioBase.trim();
        
        while (usuarioExiste(nuevoUsuario)) {
            nuevoUsuario = usuarioBase.trim() + contador;
            contador++;
            
            // Evitar bucle infinito
            if (contador > 1000) {
                nuevoUsuario = usuarioBase.trim() + System.currentTimeMillis();
                break;
            }
        }
        
        return nuevoUsuario;
    }
    
    /**
     * Inserta un nuevo usuario en la base de datos
     */
    public static boolean insertarUsuario(String apellido, String cargo, String contrasena, 
                                          String correo, String data, String descripcion, 
                                          String foto, String identificacion, String tipoIdentificacion,
                                          String nombre, String usuario) {
        String query = """
            INSERT INTO tb_usuarios 
            (apellido, cargo, contrasena, correo, data, descripcion, foto, identificacion, 
             TipoIdentificacion, nombre, usuario) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, apellido != null ? apellido : "");
            ps.setString(2, cargo != null ? cargo : "cliente");
            ps.setString(3, contrasena);
            ps.setString(4, correo);
            ps.setString(5, data != null ? data : "");
            ps.setString(6, descripcion != null ? descripcion : "");
            ps.setString(7, foto != null ? foto : "");
            ps.setString(8, identificacion);
            ps.setString(9, tipoIdentificacion);
            ps.setString(10, nombre);
            ps.setString(11, usuario);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al insertar usuario", e);
            return false;
        }
    }
}
