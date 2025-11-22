from django.contrib import admin
from .models import (
    Usuario, Producto, Inventario, Talla,
    Carro, CarroItem, Pago, PagoDetalle,
    ProductoHistorial, Complemento, FuncionSuperUsuario
)

@admin.register(Usuario)
class UsuarioAdmin(admin.ModelAdmin):
    list_display = ('id', 'nombre', 'apellido', 'correo', 'usuario', 'cargo', 'identificacion')
    list_filter = ('cargo',)
    search_fields = ('nombre', 'apellido', 'correo', 'usuario', 'identificacion')
    readonly_fields = ('id',)

@admin.register(Producto)
class ProductoAdmin(admin.ModelAdmin):
    list_display = ('id', 'Nombre', 'Categorias', 'Coleccion', 'Color', 'estado', 'FecheIngreso')
    list_filter = ('estado', 'Categorias', 'Coleccion')
    search_fields = ('Nombre', 'Descripcion', 'Categorias')
    readonly_fields = ('id',)

@admin.register(Talla)
class TallaAdmin(admin.ModelAdmin):
    list_display = ('id', 'nombre')
    search_fields = ('nombre',)

@admin.register(Inventario)
class InventarioAdmin(admin.ModelAdmin):
    list_display = ('id', 'id_producto', 'id_talla', 'cantidad', 'precio')
    list_filter = ('id_producto__Categorias',)
    search_fields = ('id_producto__Nombre',)
    readonly_fields = ('id',)

@admin.register(Carro)
class CarroAdmin(admin.ModelAdmin):
    list_display = ('id', 'id_usuario', 'fecha_creacion', 'cantidad_items', 'total')
    list_filter = ('fecha_creacion',)
    search_fields = ('id_usuario__nombre', 'id_usuario__correo')
    readonly_fields = ('id', 'fecha_creacion')

@admin.register(CarroItem)
class CarroItemAdmin(admin.ModelAdmin):
    list_display = ('id', 'id_carro', 'id_producto', 'id_talla', 'cantidad', 'precio_unitario', 'subtotal')
    list_filter = ('id_producto__Categorias',)
    search_fields = ('id_producto__Nombre',)
    readonly_fields = ('id',)

@admin.register(Pago)
class PagoAdmin(admin.ModelAdmin):
    list_display = ('id_pago', 'id_usuario', 'metodo_pago', 'total', 'estado', 'fecha_pago')
    list_filter = ('estado', 'metodo_pago', 'fecha_pago')
    search_fields = ('id_usuario__nombre', 'id_usuario__correo')
    readonly_fields = ('id_pago', 'fecha_pago', 'fecha_actualizacion')

@admin.register(PagoDetalle)
class PagoDetalleAdmin(admin.ModelAdmin):
    list_display = ('id', 'id_pago', 'id_producto', 'id_talla', 'cantidad', 'precio_unitario', 'subtotal')
    list_filter = ('id_pago__estado',)
    search_fields = ('id_producto__Nombre',)
    readonly_fields = ('id',)

@admin.register(ProductoHistorial)
class ProductoHistorialAdmin(admin.ModelAdmin):
    list_display = ('id', 'id_producto', 'id_usuario', 'tipo_cambio', 'fecha_modificacion')
    list_filter = ('tipo_cambio', 'fecha_modificacion', 'modificacion_admin')
    search_fields = ('id_producto__Nombre', 'id_usuario__nombre')
    readonly_fields = ('id', 'fecha_modificacion')

@admin.register(Complemento)
class ComplementoAdmin(admin.ModelAdmin):
    list_display = ('id', 'NombreDeApp', 'TipoMoneda', 'UltimaActualizacion')
    readonly_fields = ('id', 'UltimaActualizacion')

@admin.register(FuncionSuperUsuario)
class FuncionSuperUsuarioAdmin(admin.ModelAdmin):
    list_display = ('id_usuario', 'cargo', 'BaseUsuarios', 'BaseProductos', 'BaseInterfaz', 'BasePermisos')
    list_filter = ('BaseUsuarios', 'BaseProductos', 'BaseInterfaz', 'BasePermisos')
    search_fields = ('id_usuario__nombre', 'cargo')
