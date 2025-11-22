package UI;

import java.io.File;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Clase de configuración para manejar las rutas de imágenes
 * Adaptada para la estructura específica del proyecto
 */
public class ConfigImagenes {

    // Agregar este método a ConfigImagenes.java después del método obtenerRutaAbsoluta existente

/**
 * Convierte una ruta relativa de BD a ruta absoluta del sistema
 * Método centralizado para evitar duplicación
 */
public static String obtenerRutaAbsoluta(String rutaBD) {
    if (rutaBD == null || rutaBD.isEmpty()) {
        return null;
    }
    
    if (new File(rutaBD).isAbsolute()) {
        return rutaBD;
    }
    
    if (rutaBD.startsWith("...Z-Global-Img/")) {
        String rutaRelativa = rutaBD.substring("...Z-Global-Img/".length());
        String rutaActual = System.getProperty("user.dir");
        File dirActual = new File(rutaActual);
        
        File temp = dirActual;
        while (temp != null && !temp.getName().equals("ProyectoIntegrador-Desarrollo")) {
            temp = temp.getParentFile();
        }
        
        if (temp != null) {
            File carpetaImagenes = new File(temp, "Z-Global-Img");
            File archivo = new File(carpetaImagenes, rutaRelativa);
            if (archivo.exists()) {
                return archivo.getAbsolutePath();
            }
        }
    }
    
    File archivo = new File(rutaBD);
    if (archivo.exists()) {
        return archivo.getAbsolutePath();
    }
    
    return rutaBD;
}
    
    /**
     * Obtiene la ruta de la carpeta Z-Global-Img relativa a la ubicación actual
     * La estructura es:
     * ProyectoIntegrador-Desarrollo/
     *   ├── Z-Global-Img/  (aquí están las imágenes)
     *   └── Escritorio/
     *       └── src/
     *           └── UI/  (aquí está el código)
     */
    public static File obtenerCarpetaImagenes() {
        try {
            // Obtener la ruta donde se está ejecutando el proyecto
            String rutaActual = System.getProperty("user.dir");
            File dirActual = new File(rutaActual);
            
            // Verificar si estamos en la carpeta Escritorio
            if (dirActual.getName().equals("Escritorio")) {
                // Subir un nivel para llegar a ProyectoIntegrador-Desarrollo
                File dirProyecto = dirActual.getParentFile();
                // Acceder a Z-Global-Img
                return new File(dirProyecto, "Z-Global-Img");
            }
            // Si estamos en ProyectoIntegrador-Desarrollo directamente
            else if (dirActual.getName().equals("ProyectoIntegrador-Desarrollo")) {
                return new File(dirActual, "Z-Global-Img");
            }
            // Si estamos en otra ubicación, intentar encontrar la carpeta
            else {
                // Buscar hacia arriba hasta encontrar ProyectoIntegrador-Desarrollo
                File temp = dirActual;
                while (temp != null && !temp.getName().equals("ProyectoIntegrador-Desarrollo")) {
                    temp = temp.getParentFile();
                }
                if (temp != null) {
                    return new File(temp, "Z-Global-Img");
                }
            }
            
            // Si no encontramos la estructura esperada, usar ruta relativa
            return new File("../Z-Global-Img");
            
        } catch (Exception e) {
            System.err.println("Error al obtener carpeta de imágenes: " + e.getMessage());
            return new File("../Z-Global-Img");
        }
    }
    
    /**
     * Obtiene la ruta relativa para guardar en la base de datos
     * Siempre devuelve "Z-Global-Img/nombrearchivo.ext"
     */
    public static String obtenerRutaRelativaBD(String nombreArchivo) {
        return "Z-Global-Img/" + nombreArchivo;
    }
    
    /**
     * Verifica si la carpeta de imágenes existe y es accesible
     */
    public static boolean verificarCarpetaImagenes() {
        File carpeta = obtenerCarpetaImagenes();
        boolean existe = carpeta.exists() && carpeta.isDirectory();
        
        if (existe) {
            System.out.println("✓ Carpeta de imágenes encontrada en: " + carpeta.getAbsolutePath());
        } else {
            System.err.println("✗ Carpeta de imágenes NO encontrada. Se esperaba en: " + carpeta.getAbsolutePath());
        }
        
        return existe;
    }
    
    /**
     * Obtiene la ruta completa de una imagen a partir de su ruta en la BD
     */
    public static File obtenerArchivoImagen(String rutaBD) {
        if (rutaBD == null || rutaBD.isEmpty()) {
            return null;
        }
        
        // Quitar el prefijo "Z-Global-Img/" si existe
        String nombreArchivo = rutaBD;
        if (rutaBD.startsWith("Z-Global-Img/")) {
            nombreArchivo = rutaBD.substring("Z-Global-Img/".length());
        }
        
        File carpetaImagenes = obtenerCarpetaImagenes();
        return new File(carpetaImagenes, nombreArchivo);
    }
    
    /**
     * Carga una imagen desde la ruta guardada en la BD
     */
    public static ImageIcon cargarImagen(String rutaBD) {
        try {
            File archivo = obtenerArchivoImagen(rutaBD);
            if (archivo != null && archivo.exists()) {
                return new ImageIcon(archivo.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Carga una imagen escalada
     */
    public static ImageIcon cargarImagenEscalada(String rutaBD, int ancho, int alto) {
        ImageIcon icon = cargarImagen(rutaBD);
        if (icon != null) {
            Image img = icon.getImage();
            Image imgEscalada = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(imgEscalada);
        }
        return null;
    }
}