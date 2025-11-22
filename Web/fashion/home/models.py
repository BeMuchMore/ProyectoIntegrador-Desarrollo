from django.db import models
from django.core.validators import MinValueValidator
from decimal import Decimal
import hashlib

# Modelo de Tallas
class Talla(models.Model):
    id = models.AutoField(primary_key=True)
    nombre = models.CharField(max_length=10, null=True, blank=True)

    class Meta:
        db_table = 'tb_tallas'
        managed = False
        verbose_name = 'Talla'
        verbose_name_plural = 'Tallas'

    def __str__(self):
        return self.nombre or f"Talla {self.id}"


# Modelo de Usuarios
class Usuario(models.Model):
    CARGO_CHOICES = [
        ('admin', 'admin'),
        ('cliente', 'cliente'),
    ]
    
    id = models.AutoField(primary_key=True)
    apellido = models.CharField(max_length=255, null=True, blank=True)
    cargo = models.CharField(max_length=10, choices=CARGO_CHOICES, default='cliente')
    contrasena = models.CharField(max_length=255, null=True, blank=True)
    correo = models.CharField(max_length=255, null=True, blank=True, unique=True)
    data = models.CharField(max_length=255, null=True, blank=True)
    descripcion = models.CharField(max_length=255, null=True, blank=True)
    foto = models.CharField(max_length=255, null=True, blank=True)
    identificacion = models.BigIntegerField()
    TipoIdentificacion = models.CharField(max_length=50, null=True, blank=True)
    nombre = models.CharField(max_length=255)
    usuario = models.CharField(max_length=255, unique=True)

    class Meta:
        db_table = 'tb_usuarios'
        managed = False
        verbose_name = 'Usuario'
        verbose_name_plural = 'Usuarios'

    def __str__(self):
        return f"{self.nombre} {self.apellido or ''}"
    
    def check_password(self, raw_password):
        """Verifica la contraseña"""
        # Si la contraseña está hasheada, usar bcrypt o similar
        # Por ahora, comparación simple (NO RECOMENDADO PARA PRODUCCIÓN)
        return self.contrasena == raw_password
    
    def set_password(self, raw_password):
        """Establece la contraseña (debería hashearse en producción)"""
        # En producción, usar: from django.contrib.auth.hashers import make_password
        # self.contrasena = make_password(raw_password)
        self.contrasena = raw_password


# Modelo de Productos
class Producto(models.Model):
    ESTADO_CHOICES = [
        ('Eliminado', 'Eliminado'),
        ('Desabilitado', 'Desabilitado'),
        ('vigente', 'vigente'),
    ]
    
    id = models.AutoField(primary_key=True)
    Categorias = models.CharField(max_length=255, null=True, blank=True)
    Coleccion = models.CharField(max_length=255, null=True, blank=True, db_column='Colección')
    Color = models.CharField(max_length=255, null=True, blank=True)
    Descripcion = models.CharField(max_length=255, null=True, blank=True)
    FecheIngreso = models.DateTimeField(null=True, blank=True)
    ImgUrl = models.CharField(max_length=255, null=True, blank=True)
    MaterialBase = models.CharField(max_length=255, null=True, blank=True)
    OtrosMateriales = models.CharField(max_length=299, null=True, blank=True)
    Nombre = models.CharField(max_length=255, null=True, blank=True)
    estado = models.CharField(max_length=20, choices=ESTADO_CHOICES, default='vigente')

    class Meta:
        db_table = 'tb_productos'
        managed = False
        verbose_name = 'Producto'
        verbose_name_plural = 'Productos'

    def __str__(self):
        return self.Nombre or f"Producto {self.id}"
    
    def get_precio_minimo(self):
        """Retorna el precio mínimo del producto"""
        inventarios = self.inventarios.filter(cantidad__gt=0).exclude(precio__isnull=True)
        if inventarios.exists():
            return min(inv.precio for inv in inventarios)
        return None
    
    def get_precio_maximo(self):
        """Retorna el precio máximo del producto"""
        inventarios = self.inventarios.filter(cantidad__gt=0).exclude(precio__isnull=True)
        if inventarios.exists():
            return max(inv.precio for inv in inventarios)
        return None
    
    def get_tallas_disponibles(self):
        """Retorna las tallas disponibles para el producto"""
        return self.inventarios.filter(cantidad__gt=0).select_related('id_talla')
    
    def tiene_stock(self, id_talla=None):
        """Verifica si el producto tiene stock"""
        if id_talla:
            return self.inventarios.filter(id_talla_id=id_talla, cantidad__gt=0).exists()
        return self.inventarios.filter(cantidad__gt=0).exists()


# Modelo de Inventario
class Inventario(models.Model):
    id = models.AutoField(primary_key=True)
    id_producto = models.ForeignKey(Producto, on_delete=models.CASCADE, db_column='id_producto', related_name='inventarios')
    id_talla = models.ForeignKey(Talla, on_delete=models.CASCADE, db_column='id_talla', related_name='inventarios')
    cantidad = models.IntegerField(null=True, blank=True, validators=[MinValueValidator(0)])
    precio = models.DecimalField(max_digits=10, decimal_places=2, null=True, blank=True)

    class Meta:
        db_table = 'tb_inventario'
        managed = False
        unique_together = [['id_producto', 'id_talla']]
        verbose_name = 'Inventario'
        verbose_name_plural = 'Inventarios'

    def __str__(self):
        return f"{self.id_producto.Nombre} - {self.id_talla.nombre}"


# Modelo de Carro
class Carro(models.Model):
    id = models.AutoField(primary_key=True)
    id_usuario = models.ForeignKey(Usuario, on_delete=models.CASCADE, db_column='id_usuario', related_name='carros', null=True)
    fecha_creacion = models.DateTimeField(auto_now_add=True)

    class Meta:
        db_table = 'tb_carro'
        managed = False
        verbose_name = 'Carro'
        verbose_name_plural = 'Carros'

    def __str__(self):
        return f"Carro {self.id} - Usuario {self.id_usuario_id}"
    
    @property
    def total(self):
        """Calcula el total del carro"""
        total = Decimal('0.00')
        for item in self.items.all():
            total += item.subtotal
        return total
    
    @property
    def cantidad_items(self):
        """Retorna la cantidad total de items en el carro"""
        return sum(item.cantidad for item in self.items.all())


# Modelo de Carro Items
class CarroItem(models.Model):
    id = models.AutoField(primary_key=True)
    id_carro = models.ForeignKey(Carro, on_delete=models.CASCADE, db_column='id_carro', related_name='items')
    id_producto = models.ForeignKey(Producto, on_delete=models.CASCADE, db_column='id_producto', related_name='carro_items')
    id_talla = models.ForeignKey(Talla, on_delete=models.CASCADE, db_column='id_talla', related_name='carro_items')
    cantidad = models.IntegerField(null=True, blank=True, validators=[MinValueValidator(1)])
    precio_unitario = models.DecimalField(max_digits=10, decimal_places=2, null=True, blank=True)

    class Meta:
        db_table = 'tb_carro_items'
        managed = False
        verbose_name = 'Item del Carro'
        verbose_name_plural = 'Items del Carro'

    def __str__(self):
        return f"{self.id_producto.Nombre} - {self.id_talla.nombre} x{self.cantidad}"

    @property
    def subtotal(self):
        if self.cantidad and self.precio_unitario:
            return self.cantidad * self.precio_unitario
        return Decimal('0.00')


# Modelo de Pago
class Pago(models.Model):
    ESTADO_CHOICES = [
        ('Pendiente', 'Pendiente'),
        ('Completado', 'Completado'),
        ('Cancelado', 'Cancelado'),
        ('Reembolsado', 'Reembolsado'),
    ]
    
    id_pago = models.AutoField(primary_key=True)
    id_usuario = models.ForeignKey(Usuario, on_delete=models.CASCADE, db_column='id_usuario', related_name='pagos')
    metodo_pago = models.CharField(max_length=100)
    subtotal = models.DecimalField(max_digits=10, decimal_places=2)
    impuestos = models.DecimalField(max_digits=10, decimal_places=2, default=0.00)
    descuentos = models.DecimalField(max_digits=10, decimal_places=2, default=0.00)
    total = models.DecimalField(max_digits=10, decimal_places=2)
    estado = models.CharField(max_length=20, choices=ESTADO_CHOICES, default='Pendiente')
    fecha_pago = models.DateTimeField(auto_now_add=True)
    fecha_actualizacion = models.DateTimeField(auto_now=True)
    notas = models.TextField(null=True, blank=True)

    class Meta:
        db_table = 'tb_pago'
        managed = False
        verbose_name = 'Pago'
        verbose_name_plural = 'Pagos'

    def __str__(self):
        return f"Pago {self.id_pago} - {self.estado}"


# Modelo de Pago Detalle
class PagoDetalle(models.Model):
    id = models.AutoField(primary_key=True)
    id_pago = models.ForeignKey(Pago, on_delete=models.CASCADE, db_column='id_pago', related_name='detalles')
    id_producto = models.ForeignKey(Producto, on_delete=models.CASCADE, db_column='id_producto', related_name='pago_detalles')
    id_talla = models.ForeignKey(Talla, on_delete=models.CASCADE, db_column='id_talla', related_name='pago_detalles')
    cantidad = models.IntegerField()
    precio_unitario = models.DecimalField(max_digits=10, decimal_places=2)
    subtotal = models.DecimalField(max_digits=10, decimal_places=2)

    class Meta:
        db_table = 'tb_pago_detalle'
        managed = False
        verbose_name = 'Detalle de Pago'
        verbose_name_plural = 'Detalles de Pago'

    def __str__(self):
        return f"Detalle {self.id} - Pago {self.id_pago_id}"


# Modelo de Productos Historial
class ProductoHistorial(models.Model):
    TIPO_CAMBIO_CHOICES = [
        ('Creacion', 'Creacion'),
        ('Actualizacion', 'Actualizacion'),
        ('Eliminacion', 'Eliminacion'),
        ('Restauracion', 'Restauracion'),
    ]
    
    MODIFICACION_ADMIN_CHOICES = [
        ('Si', 'Si'),
        ('No', 'No'),
    ]
    
    id = models.AutoField(primary_key=True)
    id_producto = models.ForeignKey(Producto, on_delete=models.CASCADE, db_column='id_producto', related_name='historiales')
    id_usuario = models.ForeignKey(Usuario, on_delete=models.CASCADE, db_column='id_usuario', related_name='producto_historiales')
    fecha_modificacion = models.DateTimeField(auto_now_add=True)
    tipo_cambio = models.CharField(max_length=20, choices=TIPO_CAMBIO_CHOICES)
    campo_modificado = models.CharField(max_length=100, null=True, blank=True)
    valor_anterior = models.TextField(null=True, blank=True)
    valor_nuevo = models.TextField(null=True, blank=True)
    modificacion_admin = models.CharField(max_length=2, choices=MODIFICACION_ADMIN_CHOICES, default='No')
    notas = models.TextField(null=True, blank=True)

    class Meta:
        db_table = 'tb_productos_historial'
        managed = False
        verbose_name = 'Historial de Producto'
        verbose_name_plural = 'Historiales de Productos'

    def __str__(self):
        return f"Historial {self.id} - {self.tipo_cambio}"


# Modelo de Complementos (Configuración de la App)
class Complemento(models.Model):
    id = models.AutoField(primary_key=True)
    fondoPrincipal = models.CharField(max_length=299, null=True, blank=True)
    NombreDeApp = models.CharField(max_length=100, null=True, blank=True)
    CorreoApp = models.CharField(max_length=299, null=True, blank=True)
    TerminosCondiones = models.CharField(max_length=399, null=True, blank=True)
    TipoMoneda = models.CharField(max_length=299, null=True, blank=True)
    Logo = models.CharField(max_length=299, null=True, blank=True)
    UltimaActualizacion = models.DateTimeField()

    class Meta:
        db_table = 'tb_complementos'
        managed = False
        verbose_name = 'Complemento'
        verbose_name_plural = 'Complementos'

    def __str__(self):
        return self.NombreDeApp or "Configuración"


# Modelo de Funciones Super Usuarios
class FuncionSuperUsuario(models.Model):
    id_usuario = models.OneToOneField(Usuario, on_delete=models.CASCADE, db_column='id_usuario', primary_key=True, related_name='funciones')
    cargo = models.CharField(max_length=399)
    BaseUsuarios = models.BooleanField(default=False)
    BaseProductos = models.BooleanField(default=False)
    BaseInterfaz = models.BooleanField(default=False)
    BasePermisos = models.BooleanField(default=False)
    Temporales = models.BooleanField(default=False)

    class Meta:
        db_table = 'tb_funcionessuperusuarios'
        managed = False
        verbose_name = 'Función de Super Usuario'
        verbose_name_plural = 'Funciones de Super Usuarios'

    def __str__(self):
        return f"Funciones de {self.id_usuario.nombre}"