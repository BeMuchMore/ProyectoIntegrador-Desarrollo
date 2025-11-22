package UI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoDAO {
    private static final Logger logger = Logger.getLogger(ProductoDAO.class.getName());
    
    public ProductoDAO() {
        // Constructor
    }
    
    public boolean insertarProducto(Producto producto) {
        String sql = "INSERT INTO tb_productos (Nombre, ImgUrl, Cantidad, Descripcion, Precio, " +
                     "Categorias, Color, FecheIngreso, Talla, Material, Colección) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getImgUrl());
            pstmt.setInt(3, producto.getCantidad());
            pstmt.setString(4, producto.getDescripcion());
            pstmt.setDouble(5, producto.getPrecio());
            pstmt.setString(6, producto.getCategorias());
            pstmt.setString(7, producto.getColor());
            pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis())); // Fecha actual
            pstmt.setString(9, producto.getTalla());
            pstmt.setString(10, producto.getMaterial());
            pstmt.setString(11, producto.getColeccion());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Producto insertado correctamente: " + producto.getNombre());
                return true;
            } else {
                logger.warning("No se pudo insertar el producto");
                return false;
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al insertar producto", e);
            return false;
        }
    }
}