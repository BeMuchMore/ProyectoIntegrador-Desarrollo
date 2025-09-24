package UI;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    
    // Configuración de la base de datos - MODIFICA ESTOS VALORES SEGÚN TU CONFIGURACIÓN
    private static final String URL = "jdbc:mysql://localhost:3306/basedatos?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            logger.info("Conexión a base de datos establecida correctamente");
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "Error al conectar con la base de datos", e);
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener conexión", e);
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al cerrar conexión", e);
        }
    }
}