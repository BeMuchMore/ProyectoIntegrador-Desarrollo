package UI;

public class SessionManager {
    
    // Instancia única del singleton
    private static SessionManager instance;
    
    // Información del usuario actual
    private int userId;
    private String username;
    private String cargo;
    private boolean isLoggedIn;
    
    // Constructor privado para singleton
    private SessionManager() {
        this.isLoggedIn = false;
    }
    
    /**
     * Obtiene la instancia única de SessionManager
     * @return instancia de SessionManager
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Inicia sesión con los datos del usuario
     * @param userId ID del usuario
     * @param username nombre de usuario
     * @param cargo cargo del usuario
     */
    public void login(int userId, String username, String cargo) {
        this.userId = userId;
        this.username = username;
        this.cargo = cargo != null ? cargo : "";
        this.isLoggedIn = true;
        
        System.out.println("Sesión iniciada para: " + username + " (ID: " + userId + ", Cargo: " + cargo + ")");
    }
    
    /**
     * Cierra la sesión actual
     */
    public void logout() {
        this.userId = 0;
        this.username = null;
        this.cargo = null;
        this.isLoggedIn = false;
        
        System.out.println("Sesión cerrada");
    }
    
    /**
     * Verifica si hay una sesión activa
     * @return true si hay sesión activa
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    /**
     * Obtiene el ID del usuario actual
     * @return ID del usuario o 0 si no hay sesión
     */
    public int getUserId() {
        return isLoggedIn ? userId : 0;
    }
    
    /**
     * Obtiene el nombre de usuario actual
     * @return nombre de usuario o null si no hay sesión
     */
    public String getUsername() {
        return isLoggedIn ? username : null;
    }
    
    /**
     * Obtiene el cargo del usuario actual
     * @return cargo del usuario o string vacío si no hay sesión
     */
    public String getCargo() {
        return isLoggedIn ? cargo : "";
    }
    
    /**
     * Verifica si el usuario tiene un cargo específico
     * @param cargo cargo a verificar
     * @return true si el usuario tiene ese cargo
     */
    public boolean hasCargo(String cargo) {
        return isLoggedIn && this.cargo != null && this.cargo.equalsIgnoreCase(cargo);
    }
    
    /**
     * Verifica si el usuario es administrador
     * @return true si es administrador
     */
    public boolean isAdmin() {
        return hasCargo("admin") || hasCargo("administrador");
    }
    
    /**
     * Obtiene información completa del usuario
     * @return string con información del usuario
     */
    public String getUserInfo() {
        if (!isLoggedIn) {
            return "Sin sesión activa";
        }
        return String.format("Usuario: %s (ID: %d) - Cargo: %s", 
                           username, userId, cargo.isEmpty() ? "Sin cargo" : cargo);
    }
    
    /**
     * Requiere autenticación - lanza excepción si no hay sesión
     * @throws IllegalStateException si no hay sesión activa
     */
    public void requireAuth() throws IllegalStateException {
        if (!isLoggedIn) {
            throw new IllegalStateException("Se requiere autenticación");
        }
    }
}