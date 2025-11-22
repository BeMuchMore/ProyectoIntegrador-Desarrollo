"""
Vistas para el panel de administración con CRUD básico
"""
from django.shortcuts import render, redirect, get_object_or_404
from django.contrib import messages
from django.contrib.auth.decorators import login_required
from django.db import connection
from django.conf import settings
from .models import Usuario, Producto, Inventario, Talla, Carro, CarroItem, Pago, Complemento

def get_config():
    """Obtiene la configuración de la app desde Complemento"""
    try:
        return Complemento.objects.first()
    except:
        return None

def es_admin(request):
    """Verifica si el usuario actual es admin"""
    usuario_id = request.session.get('usuario_id')
    if not usuario_id:
        return False
    
    try:
        usuario = Usuario.objects.get(id=usuario_id)
        # Verificar tanto el campo cargo como el valor en la sesión
        cargo_en_sesion = request.session.get('usuario_cargo')
        cargo_en_db = usuario.cargo
        
        # Debug: imprimir información (solo en desarrollo)
        if settings.DEBUG:
            print(f"DEBUG Admin Check - Usuario ID: {usuario_id}")
            print(f"DEBUG Admin Check - Cargo en sesión: {cargo_en_sesion}")
            print(f"DEBUG Admin Check - Cargo en DB: {cargo_en_db}")
            print(f"DEBUG Admin Check - Es admin: {cargo_en_db == 'admin'}")
        
        return cargo_en_db == 'admin'
    except Usuario.DoesNotExist:
        return False
    except Exception as e:
        if settings.DEBUG:
            print(f"DEBUG Admin Check Error: {str(e)}")
        return False

def admin_required(view_func):
    """Decorador para requerir que el usuario sea admin"""
    def wrapper(request, *args, **kwargs):
        usuario_id = request.session.get('usuario_id')
        
        if not usuario_id:
            messages.error(request, 'Debes iniciar sesión para acceder al panel de administración.')
            return redirect('login')
        
        if not es_admin(request):
            try:
                usuario = Usuario.objects.get(id=usuario_id)
                messages.error(request, f'No tienes permisos de administrador. Tu cargo actual es: {usuario.cargo}.')
            except:
                messages.error(request, 'No tienes permisos para acceder a esta sección.')
            return redirect('home')
        
        return view_func(request, *args, **kwargs)
    return wrapper

def admin_debug(request):
    """Página de depuración para verificar el estado del admin"""
    config = get_config()
    context = {
        'config': config,
    }
    return render(request, 'admin/debug_admin.html', context)

@admin_required
def admin_dashboard(request):
    """Panel principal de administración"""
    config = get_config()
    
    # Estadísticas
    total_productos = Producto.objects.exclude(estado='Eliminado').count()
    total_usuarios = Usuario.objects.count()
    total_pedidos = Pago.objects.count()
    total_carros = Carro.objects.count()
    
    context = {
        'config': config,
        'total_productos': total_productos,
        'total_usuarios': total_usuarios,
        'total_pedidos': total_pedidos,
        'total_carros': total_carros,
    }
    return render(request, 'admin/dashboard.html', context)

# ============ CRUD PRODUCTOS ============

@admin_required
def admin_productos(request):
    """Lista de productos para administración"""
    config = get_config()
    productos = Producto.objects.exclude(estado='Eliminado').order_by('-id')
    
    context = {
        'config': config,
        'productos': productos,
    }
    return render(request, 'admin/productos_list.html', context)

@admin_required
def admin_producto_nuevo(request):
    """Crear nuevo producto"""
    config = get_config()
    
    if request.method == 'POST':
        try:
            # Obtener el siguiente ID
            with connection.cursor() as cursor:
                cursor.execute("SELECT COALESCE(MAX(id), 0) FROM tb_productos")
                max_id = cursor.fetchone()[0]
                next_id = max_id + 1
                
                # Insertar producto
                cursor.execute(
                    """INSERT INTO tb_productos 
                       (id, Nombre, Descripcion, Categorias, Coleccion, Color, MaterialBase, ImgUrl, estado) 
                       VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)""",
                    [
                        next_id,
                        request.POST.get('nombre'),
                        request.POST.get('descripcion', ''),
                        request.POST.get('categorias', ''),
                        request.POST.get('coleccion', ''),
                        request.POST.get('color', ''),
                        request.POST.get('material_base', ''),
                        request.POST.get('img_url', ''),
                        'vigente'
                    ]
                )
            
            messages.success(request, 'Producto creado exitosamente.')
            return redirect('admin_productos')
        except Exception as e:
            messages.error(request, f'Error al crear producto: {str(e)}')
    
    context = {
        'config': config,
    }
    return render(request, 'admin/producto_form.html', context)

@admin_required
def admin_producto_editar(request, producto_id):
    """Editar producto existente"""
    config = get_config()
    producto = get_object_or_404(Producto, id=producto_id)
    
    if request.method == 'POST':
        try:
            producto.Nombre = request.POST.get('nombre')
            producto.Descripcion = request.POST.get('descripcion', '')
            producto.Categorias = request.POST.get('categorias', '')
            producto.Coleccion = request.POST.get('coleccion', '')
            producto.Color = request.POST.get('color', '')
            producto.MaterialBase = request.POST.get('material_base', '')
            producto.ImgUrl = request.POST.get('img_url', '')
            producto.estado = request.POST.get('estado', 'vigente')
            
            with connection.cursor() as cursor:
                cursor.execute(
                    """UPDATE tb_productos 
                       SET Nombre=%s, Descripcion=%s, Categorias=%s, Coleccion=%s, 
                           Color=%s, MaterialBase=%s, ImgUrl=%s, estado=%s 
                       WHERE id=%s""",
                    [
                        producto.Nombre,
                        producto.Descripcion,
                        producto.Categorias,
                        producto.Coleccion,
                        producto.Color,
                        producto.MaterialBase,
                        producto.ImgUrl,
                        producto.estado,
                        producto.id
                    ]
                )
            
            messages.success(request, 'Producto actualizado exitosamente.')
            return redirect('admin_productos')
        except Exception as e:
            messages.error(request, f'Error al actualizar producto: {str(e)}')
    
    context = {
        'config': config,
        'producto': producto,
    }
    return render(request, 'admin/producto_form.html', context)

@admin_required
def admin_producto_eliminar(request, producto_id):
    """Eliminar producto (marcar como eliminado)"""
    producto = get_object_or_404(Producto, id=producto_id)
    
    try:
        with connection.cursor() as cursor:
            cursor.execute(
                "UPDATE tb_productos SET estado='Eliminado' WHERE id=%s",
                [producto.id]
            )
        messages.success(request, 'Producto eliminado exitosamente.')
    except Exception as e:
        messages.error(request, f'Error al eliminar producto: {str(e)}')
    
    return redirect('admin_productos')

# ============ CRUD USUARIOS ============

@admin_required
def admin_usuarios(request):
    """Lista de usuarios para administración"""
    config = get_config()
    usuarios = Usuario.objects.all().order_by('-id')
    
    context = {
        'config': config,
        'usuarios': usuarios,
    }
    return render(request, 'admin/usuarios_list.html', context)

@admin_required
def admin_usuario_nuevo(request):
    """Crear nuevo usuario"""
    config = get_config()
    
    if request.method == 'POST':
        try:
            with connection.cursor() as cursor:
                cursor.execute("SELECT COALESCE(MAX(id), 0) FROM tb_usuarios")
                max_id = cursor.fetchone()[0]
                next_id = max_id + 1
                
                cursor.execute(
                    """INSERT INTO tb_usuarios 
                       (id, nombre, apellido, correo, usuario, contrasena, identificacion, TipoIdentificacion, cargo) 
                       VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)""",
                    [
                        next_id,
                        request.POST.get('nombre'),
                        request.POST.get('apellido', ''),
                        request.POST.get('correo'),
                        request.POST.get('usuario'),
                        request.POST.get('contrasena'),
                        request.POST.get('identificacion'),
                        request.POST.get('tipo_identificacion', 'CC'),
                        request.POST.get('cargo', 'cliente')
                    ]
                )
            
            messages.success(request, 'Usuario creado exitosamente.')
            return redirect('admin_usuarios')
        except Exception as e:
            messages.error(request, f'Error al crear usuario: {str(e)}')
    
    context = {
        'config': config,
    }
    return render(request, 'admin/usuario_form.html', context)

@admin_required
def admin_usuario_editar(request, usuario_id):
    """Editar usuario existente"""
    config = get_config()
    usuario = get_object_or_404(Usuario, id=usuario_id)
    
    if request.method == 'POST':
        try:
            with connection.cursor() as cursor:
                cursor.execute(
                    """UPDATE tb_usuarios 
                       SET nombre=%s, apellido=%s, correo=%s, usuario=%s, 
                           identificacion=%s, TipoIdentificacion=%s, cargo=%s 
                       WHERE id=%s""",
                    [
                        request.POST.get('nombre'),
                        request.POST.get('apellido', ''),
                        request.POST.get('correo'),
                        request.POST.get('usuario'),
                        request.POST.get('identificacion'),
                        request.POST.get('tipo_identificacion', 'CC'),
                        request.POST.get('cargo', 'cliente'),
                        usuario.id
                    ]
                )
                
                # Si se cambió la contraseña
                if request.POST.get('contrasena'):
                    cursor.execute(
                        "UPDATE tb_usuarios SET contrasena=%s WHERE id=%s",
                        [request.POST.get('contrasena'), usuario.id]
                    )
            
            messages.success(request, 'Usuario actualizado exitosamente.')
            return redirect('admin_usuarios')
        except Exception as e:
            messages.error(request, f'Error al actualizar usuario: {str(e)}')
    
    context = {
        'config': config,
        'usuario': usuario,
    }
    return render(request, 'admin/usuario_form.html', context)

@admin_required
def admin_usuario_eliminar(request, usuario_id):
    """Eliminar usuario"""
    usuario = get_object_or_404(Usuario, id=usuario_id)
    
    # No permitir eliminar al propio usuario
    if request.session.get('usuario_id') == usuario.id:
        messages.error(request, 'No puedes eliminar tu propio usuario.')
        return redirect('admin_usuarios')
    
    try:
        usuario.delete()
        messages.success(request, 'Usuario eliminado exitosamente.')
    except Exception as e:
        messages.error(request, f'Error al eliminar usuario: {str(e)}')
    
    return redirect('admin_usuarios')

# ============ CRUD INVENTARIO ============

@admin_required
def admin_inventario(request):
    """Lista de inventario para administración"""
    config = get_config()
    inventarios = Inventario.objects.select_related('id_producto', 'id_talla').all().order_by('-id')
    
    context = {
        'config': config,
        'inventarios': inventarios,
    }
    return render(request, 'admin/inventario_list.html', context)

@admin_required
def admin_inventario_nuevo(request):
    """Crear nuevo inventario"""
    config = get_config()
    productos = Producto.objects.exclude(estado='Eliminado')
    tallas = Talla.objects.all()
    
    if request.method == 'POST':
        try:
            with connection.cursor() as cursor:
                cursor.execute("SELECT COALESCE(MAX(id), 0) FROM tb_inventario")
                max_id = cursor.fetchone()[0]
                next_id = max_id + 1
                
                cursor.execute(
                    """INSERT INTO tb_inventario 
                       (id, id_producto, id_talla, cantidad, precio) 
                       VALUES (%s, %s, %s, %s, %s)""",
                    [
                        next_id,
                        request.POST.get('producto_id'),
                        request.POST.get('talla_id'),
                        request.POST.get('cantidad'),
                        request.POST.get('precio')
                    ]
                )
            
            messages.success(request, 'Inventario creado exitosamente.')
            return redirect('admin_inventario')
        except Exception as e:
            messages.error(request, f'Error al crear inventario: {str(e)}')
    
    context = {
        'config': config,
        'productos': productos,
        'tallas': tallas,
    }
    return render(request, 'admin/inventario_form.html', context)

@admin_required
def admin_inventario_editar(request, inventario_id):
    """Editar inventario existente"""
    config = get_config()
    inventario = get_object_or_404(Inventario, id=inventario_id)
    productos = Producto.objects.exclude(estado='Eliminado')
    tallas = Talla.objects.all()
    
    if request.method == 'POST':
        try:
            with connection.cursor() as cursor:
                cursor.execute(
                    """UPDATE tb_inventario 
                       SET id_producto=%s, id_talla=%s, cantidad=%s, precio=%s 
                       WHERE id=%s""",
                    [
                        request.POST.get('producto_id'),
                        request.POST.get('talla_id'),
                        request.POST.get('cantidad'),
                        request.POST.get('precio'),
                        inventario.id
                    ]
                )
            
            messages.success(request, 'Inventario actualizado exitosamente.')
            return redirect('admin_inventario')
        except Exception as e:
            messages.error(request, f'Error al actualizar inventario: {str(e)}')
    
    context = {
        'config': config,
        'inventario': inventario,
        'productos': productos,
        'tallas': tallas,
    }
    return render(request, 'admin/inventario_form.html', context)

@admin_required
def admin_inventario_eliminar(request, inventario_id):
    """Eliminar inventario"""
    inventario = get_object_or_404(Inventario, id=inventario_id)
    
    try:
        inventario.delete()
        messages.success(request, 'Inventario eliminado exitosamente.')
    except Exception as e:
        messages.error(request, f'Error al eliminar inventario: {str(e)}')
    
    return redirect('admin_inventario')

