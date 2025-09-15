package UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    private static final String URL = "jdbc:mysql://localhost:3306/basedatos?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        
        try {
            // Cargar el driver explícitamente
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            System.out.println("Driver MySQL cargado correctamente");
            System.out.println("Intentando conectar a: " + URL);
            
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            if (conn != null) {
                System.out.println("✓ Conexión establecida exitosamente");
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Driver MySQL no encontrado");
            System.err.println("Asegúrate de tener mysql-connector-java en el classpath");
            e.printStackTrace();
            throw new SQLException("Driver MySQL no encontrado: " + e.getMessage());
            
        } catch (SQLException e) {
            System.err.println("ERROR SQL: " + e.getMessage());
            System.err.println("Código de error: " + e.getErrorCode());
            System.err.println("Estado SQL: " + e.getSQLState());
            
            // Mensajes más específicos según el error
            if (e.getErrorCode() == 1045) {
                throw new SQLException("Credenciales incorrectas para MySQL");
            } else if (e.getErrorCode() == 1049) {
                throw new SQLException("La base de datos 'basedatos' no existe");
            } else if (e.getMessage().contains("Connection refused")) {
                throw new SQLException("MySQL server no está ejecutándose");
            }
            
            throw e;
        }
        
        return conn;
    }
    
    // Método para probar la conexión
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Test de conexión falló: " + e.getMessage());
            return false;
        }
    }
}