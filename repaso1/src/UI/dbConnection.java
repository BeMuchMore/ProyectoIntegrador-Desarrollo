
package UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/repaso1"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; // o la que tengas en XAMPP

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión exitosa a MySQL");
        } catch (SQLException e) {
            System.out.println("❌ Error en la conexión: " + e.getMessage());
        }
        return conexion;
    }
    


    
    
}