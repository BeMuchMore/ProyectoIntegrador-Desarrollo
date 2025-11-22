# urls.py - URL configuration for e-commerce

from django.urls import path
from . import views
from . import admin_views

urlpatterns = [
    # Main pages
    path('', views.home, name='home'),
    
    # Authentication URLs
    path('login/', views.login_view, name='login'),
    path('register/', views.register_view, name='register'),
    path('logout/', views.logout_view, name='logout'),
    
    # Product URLs
    path('shop/', views.shop, name='shop'),
    path('producto/<int:producto_id>/', views.producto_detalle, name='producto_detalle'),
    
    # Cart URLs
    path('carrito/', views.carrito, name='carrito'),
    path('carrito/agregar/', views.agregar_al_carrito, name='agregar_al_carrito'),
    path('carrito/actualizar/<int:item_id>/', views.actualizar_carrito, name='actualizar_carrito'),
    path('carrito/eliminar/<int:item_id>/', views.eliminar_del_carrito, name='eliminar_del_carrito'),
    
    # Checkout URLs
    path('checkout/', views.checkout, name='checkout'),
    path('checkout/procesar/', views.procesar_pago, name='procesar_pago'),
    
    # Profile URLs
    path('perfil/', views.perfil, name='perfil'),
    path('perfil/actualizar/', views.actualizar_perfil, name='actualizar_perfil'),
    
    # AJAX URLs
    path('ajax/tallas/<int:producto_id>/', views.obtener_tallas_disponibles, name='obtener_tallas'),
    path('ajax/carrito/count/', views.obtener_carrito_count, name='carrito_count'),
    
    # Image serving
    path('media/external/<path:path>', views.serve_external_image, name='serve_external_image'),
    
    # Footer pages
    path('sobre-nosotros/', views.sobre_nosotros, name='sobre_nosotros'),
    path('contacto/', views.contacto, name='contacto'),
    path('guia-tallas/', views.guia_tallas, name='guia_tallas'),
    path('informacion-envio/', views.info_envio, name='info_envio'),
    path('devoluciones/', views.devoluciones, name='devoluciones'),
    path('rastrear-pedido/', views.rastrear_pedido, name='rastrear_pedido'),
    path('preguntas-frecuentes/', views.preguntas_frecuentes, name='preguntas_frecuentes'),
    path('metodos-pago/', views.metodos_pago, name='metodos_pago'),
    path('tarjetas-regalo/', views.tarjetas_regalo, name='tarjetas_regalo'),
    path('ubicacion-tiendas/', views.ubicacion_tiendas, name='ubicacion_tiendas'),
    path('politica-privacidad/', views.politica_privacidad, name='politica_privacidad'),
    path('terminos-condiciones/', views.terminos_condiciones, name='terminos_condiciones'),
    
    # Admin URLs
    path('admin/', admin_views.admin_dashboard, name='admin_dashboard'),
    path('admin/debug/', admin_views.admin_debug, name='admin_debug'),
    
    # Admin Productos
    path('admin/productos/', admin_views.admin_productos, name='admin_productos'),
    path('admin/productos/nuevo/', admin_views.admin_producto_nuevo, name='admin_producto_nuevo'),
    path('admin/productos/editar/<int:producto_id>/', admin_views.admin_producto_editar, name='admin_producto_editar'),
    path('admin/productos/eliminar/<int:producto_id>/', admin_views.admin_producto_eliminar, name='admin_producto_eliminar'),
    
    # Admin Usuarios
    path('admin/usuarios/', admin_views.admin_usuarios, name='admin_usuarios'),
    path('admin/usuarios/nuevo/', admin_views.admin_usuario_nuevo, name='admin_usuario_nuevo'),
    path('admin/usuarios/editar/<int:usuario_id>/', admin_views.admin_usuario_editar, name='admin_usuario_editar'),
    path('admin/usuarios/eliminar/<int:usuario_id>/', admin_views.admin_usuario_eliminar, name='admin_usuario_eliminar'),
    
    # Admin Inventario
    path('admin/inventario/', admin_views.admin_inventario, name='admin_inventario'),
    path('admin/inventario/nuevo/', admin_views.admin_inventario_nuevo, name='admin_inventario_nuevo'),
    path('admin/inventario/editar/<int:inventario_id>/', admin_views.admin_inventario_editar, name='admin_inventario_editar'),
    path('admin/inventario/eliminar/<int:inventario_id>/', admin_views.admin_inventario_eliminar, name='admin_inventario_eliminar'),
]
