package UX;

import UI.Conexion;
import UI.SessionManager;
import java.sql.*;
import java.time.LocalTime;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Gestor de permisos basado en niveles de cargo
 * Verifica permisos según tb_funcionessuperusuarios
 */
public class PermisosManager {
    
    private static final Logger logger = Logger.getLogger(PermisosManager.class.getName());
    
    // Tipos de permisos
    public enum TipoPermiso {
        BASE_USUARIOS,
        BASE_PRODUCTOS,
        BASE_INTERFAZ,
        BASE_PERMISOS,
        TEMPORALES
    }
    
    // Niveles mínimos requeridos (0 = sin acceso, 1-5 = niveles)
    private static final int NIVEL_MINIMO_ACCESO = 1;
    
    /**
     * Verifica si el usuario actual tiene acceso a los módulos UX
     */
    public static boolean tieneAccesoUX(int userId) {
        if (userId <= 0) {
            return false;
        }
        
        try (Connection conn = Conexion.getConnection()) {
            String query = """
                SELECT BaseUsuarios, BaseProductos, BaseInterfaz, BasePermisos, Temporales
                FROM tb_funcionessuperusuarios
                WHERE id_usuario = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Si tiene al menos un permiso con nivel >= 1, tiene acceso
                return rs.getInt("BaseUsuarios") >= NIVEL_MINIMO_ACCESO ||
                       rs.getInt("BaseProductos") >= NIVEL_MINIMO_ACCESO ||
                       rs.getInt("BaseInterfaz") >= NIVEL_MINIMO_ACCESO ||
                       rs.getInt("BasePermisos") >= NIVEL_MINIMO_ACCESO ||
                       rs.getInt("Temporales") >= NIVEL_MINIMO_ACCESO;
            }
            
            // Si no tiene registro en tb_funcionessuperusuarios, verificar cargo
            return verificarCargoAdmin(userId, conn);
            
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al verificar acceso UX", e);
            return false;
        }
    }
    
    /**
     * Verifica si el usuario tiene un permiso específico con nivel mínimo
     */
    public static boolean tienePermiso(int userId, TipoPermiso tipo, int nivelMinimo) {
        if (userId <= 0 || nivelMinimo < NIVEL_MINIMO_ACCESO) {
            return false;
        }
        
        try (Connection conn = Conexion.getConnection()) {
            String campo = obtenerCampoPermiso(tipo);
            String query = "SELECT " + campo + " FROM tb_funcionessuperusuarios WHERE id_usuario = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int nivel = rs.getInt(campo);
                return nivel >= nivelMinimo;
            }
            
            // Si no tiene registro, verificar cargo admin
            return verificarCargoAdmin(userId, conn) && nivelMinimo <= 3;
            
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al verificar permiso", e);
            return false;
        }
    }
    
    /**
     * Obtiene el nivel de un permiso específico
     */
    public static int obtenerNivelPermiso(int userId, TipoPermiso tipo) {
        if (userId <= 0) {
            return 0;
        }
        
        try (Connection conn = Conexion.getConnection()) {
            String campo = obtenerCampoPermiso(tipo);
            String query = "SELECT " + campo + " FROM tb_funcionessuperusuarios WHERE id_usuario = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(campo);
            }
            
            // Si no tiene registro, verificar cargo
            if (verificarCargoAdmin(userId, conn)) {
                return 3; // Nivel por defecto para admin
            }
            
            return 0;
            
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al obtener nivel de permiso", e);
            return 0;
        }
    }
    
    /**
     * Verifica si la operación está permitida en el horario actual
     * Nivel 1-2: Solo 8:00-18:00
     * Nivel 3-4: 6:00-22:00
     * Nivel 5: Sin restricción
     */
    public static boolean esHorarioPermitido(int nivel) {
        if (nivel >= 5) {
            return true; // Sin restricción
        }
        
        LocalTime ahora = LocalTime.now();
        
        if (nivel >= 3) {
            // Nivel 3-4: 6:00-22:00
            return ahora.isAfter(LocalTime.of(6, 0)) && ahora.isBefore(LocalTime.of(22, 0));
        } else {
            // Nivel 1-2: 8:00-18:00
            return ahora.isAfter(LocalTime.of(8, 0)) && ahora.isBefore(LocalTime.of(18, 0));
        }
    }
    
    /**
     * Verifica si puede eliminar según el nivel
     * Solo niveles 3+ pueden eliminar
     */
    public static boolean puedeEliminar(int nivel) {
        return nivel >= 3;
    }
    
    /**
     * Verifica si puede modificar según el nivel
     * Niveles 2+ pueden modificar
     */
    public static boolean puedeModificar(int nivel) {
        return nivel >= 2;
    }
    
    /**
     * Verifica si puede crear según el nivel
     * Niveles 1+ pueden crear
     */
    public static boolean puedeCrear(int nivel) {
        return nivel >= 1;
    }
    
    /**
     * Obtiene el nombre del campo de permiso
     */
    private static String obtenerCampoPermiso(TipoPermiso tipo) {
        switch (tipo) {
            case BASE_USUARIOS: return "BaseUsuarios";
            case BASE_PRODUCTOS: return "BaseProductos";
            case BASE_INTERFAZ: return "BaseInterfaz";
            case BASE_PERMISOS: return "BasePermisos";
            case TEMPORALES: return "Temporales";
            default: return "BaseUsuarios";
        }
    }
    
    /**
     * Verifica si el usuario tiene cargo admin
     */
    private static boolean verificarCargoAdmin(int userId, Connection conn) throws SQLException {
        String query = "SELECT cargo FROM tb_usuarios WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            String cargo = rs.getString("cargo");
            return "admin".equalsIgnoreCase(cargo);
        }
        
        return false;
    }
    
    /**
     * Obtiene información completa de permisos del usuario
     */
    public static class InfoPermisos {
        public int baseUsuarios;
        public int baseProductos;
        public int baseInterfaz;
        public int basePermisos;
        public int temporales;
        public String cargo;
        
        public InfoPermisos(int baseUsuarios, int baseProductos, int baseInterfaz, 
                           int basePermisos, int temporales, String cargo) {
            this.baseUsuarios = baseUsuarios;
            this.baseProductos = baseProductos;
            this.baseInterfaz = baseInterfaz;
            this.basePermisos = basePermisos;
            this.temporales = temporales;
            this.cargo = cargo;
        }
    }
    
    /**
     * Obtiene información completa de permisos
     */
    public static InfoPermisos obtenerInfoPermisos(int userId) {
        try (Connection conn = Conexion.getConnection()) {
            String query = """
                SELECT f.BaseUsuarios, f.BaseProductos, f.BaseInterfaz, 
                       f.BasePermisos, f.Temporales, u.cargo
                FROM tb_funcionessuperusuarios f
                JOIN tb_usuarios u ON f.id_usuario = u.id
                WHERE f.id_usuario = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new InfoPermisos(
                    rs.getInt("BaseUsuarios"),
                    rs.getInt("BaseProductos"),
                    rs.getInt("BaseInterfaz"),
                    rs.getInt("BasePermisos"),
                    rs.getInt("Temporales"),
                    rs.getString("cargo")
                );
            }
            
            // Si no tiene registro, obtener solo cargo
            String queryCargo = "SELECT cargo FROM tb_usuarios WHERE id = ?";
            PreparedStatement pstmtCargo = conn.prepareStatement(queryCargo);
            pstmtCargo.setInt(1, userId);
            ResultSet rsCargo = pstmtCargo.executeQuery();
            
            if (rsCargo.next()) {
                String cargo = rsCargo.getString("cargo");
                if ("admin".equalsIgnoreCase(cargo)) {
                    return new InfoPermisos(3, 3, 3, 3, 3, cargo);
                }
            }
            
            return new InfoPermisos(0, 0, 0, 0, 0, "cliente");
            
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al obtener info de permisos", e);
            return new InfoPermisos(0, 0, 0, 0, 0, "cliente");
        }
    }
}

