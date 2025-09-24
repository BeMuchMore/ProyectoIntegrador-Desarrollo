package UI;

public class Producto {
    private int id;
    private String nombre;
    private String imgUrl;
    private int cantidad;
    private String descripcion;
    private double precio;
    private String categorias;
    private String color;
    private String fechaIngreso;
    private String talla;
    private String material;
    private String coleccion;
    
    // Constructor vacío
    public Producto() {}
    
    // Constructor con parámetros principales
    public Producto(String nombre, String imgUrl, int cantidad, String descripcion, 
                   double precio, String categorias, String color, String talla, 
                   String material, String coleccion) {
        this.nombre = nombre;
        this.imgUrl = imgUrl;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categorias = categorias;
        this.color = color;
        this.talla = talla;
        this.material = material;
        this.coleccion = coleccion;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    
    public String getCategorias() { return categorias; }
    public void setCategorias(String categorias) { this.categorias = categorias; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(String fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
    
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    
    public String getColeccion() { return coleccion; }
    public void setColeccion(String coleccion) { this.coleccion = coleccion; }
    
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                '}';
    }
}