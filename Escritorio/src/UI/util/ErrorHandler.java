package UI.util;

import javax.swing.JOptionPane;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.SQLException;

/**
 * Clase centralizada para manejo de errores y logging
 */
public class ErrorHandler {
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());
    
    /**
     * Maneja errores SQL mostrando mensaje al usuario y logueando
     */
    public static void handleSQLException(javax.swing.JFrame parent, SQLException e, String operacion) {
        String mensaje = "Error en " + operacion + ": " + e.getMessage();
        logger.log(Level.SEVERE, mensaje, e);
        
        String mensajeUsuario = "Error al " + operacion.toLowerCase() + 
            ".\nPor favor, intente nuevamente.";
        
        JOptionPane.showMessageDialog(parent, mensajeUsuario, 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Maneja errores generales
     */
    public static void handleException(javax.swing.JFrame parent, Exception e, String operacion) {
        String mensaje = "Error en " + operacion + ": " + e.getMessage();
        logger.log(Level.SEVERE, mensaje, e);
        
        JOptionPane.showMessageDialog(parent, 
            "Ocurrió un error inesperado: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Log de información (reemplaza System.out.println)
     */
    public static void logInfo(String mensaje) {
        logger.info(mensaje);
    }
    
    /**
     * Log de advertencia (reemplaza System.err.println)
     */
    public static void logWarning(String mensaje) {
        logger.warning(mensaje);
    }
    
    /**
     * Log de advertencia con excepción
     */
    public static void logWarning(String mensaje, Exception e) {
        logger.log(Level.WARNING, mensaje, e);
    }
}