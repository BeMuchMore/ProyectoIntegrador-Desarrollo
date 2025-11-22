# views.py - Django views for e-commerce

from django.shortcuts import render, redirect, get_object_or_404
from django.contrib import messages
from django.core.validators import validate_email
from django.core.exceptions import ValidationError
from django.http import JsonResponse, FileResponse, Http404
from django.views.decorators.csrf import csrf_protect
from django.views.decorators.http import require_POST
from django.db.models import Q, F
from django.core.paginator import Paginator
from django.db import connection
from decimal import Decimal
import json
import os
from django.conf import settings

from .models import (
    Usuario, Producto, Inventario, Talla, 
    Carro, CarroItem, Pago, PagoDetalle, Complemento
)

# ============ IMAGE SERVING ============

def serve_external_image(request, path):
    """
    Sirve imágenes desde una ruta externa al proyecto.
    Las imágenes pueden estar en cualquier ubicación del sistema.
    
    La ruta viene como: '...Z-Global-Img/producto.jpg'
    Y la carpeta base es: 'C:/Users/calam/OneDrive/Documentos/NetBeansProjects/ProyectoIntegrador-Desarrollo/Escritorio/...Z-Global-Img/'
    """
    from django.conf import settings
    # Usar la ruta configurada en settings
    IMAGE_BASE_PATH = getattr(settings, 'EXTERNAL_IMAGE_BASE_PATH', 
                             'C:/Users/calam/OneDrive/Documentos/NetBeansProjects/ProyectoIntegrador-Desarrollo/Escritorio/...Z-Global-Img/')
    
    # Limpiar la ruta del request (puede venir con /media/external/...)
    path = path.lstrip('/')
    
    # Si la ruta comienza con '...Z-Global-Img/', extraer solo el nombre del archivo
    # porque la carpeta base ya incluye '...Z-Global-Img'
    if path.startswith('...Z-Global-Img/'):
        # Extraer solo el nombre del archivo (lo que viene después de '...Z-Global-Img/')
        # Ejemplo: '...Z-Global-Img/producto.jpg' -> 'producto.jpg'
        filename = path.replace('...Z-Global-Img/', '')
        full_path = os.path.join(IMAGE_BASE_PATH, filename)
    elif path.startswith('...Z-Global-Img'):
        # Si no tiene la barra, puede ser solo '...Z-Global-Img' seguido del nombre
        filename = path.replace('...Z-Global-Img', '')
        full_path = os.path.join(IMAGE_BASE_PATH, filename)
    else:
        # Construir la ruta completa normalmente
        full_path = os.path.join(IMAGE_BASE_PATH, path)
    
    # Normalizar la ruta para evitar directory traversal
    full_path = os.path.normpath(full_path)
    base_path = os.path.normpath(IMAGE_BASE_PATH)
    
    # Verificar que la ruta esté dentro del directorio base (seguridad)
    if not full_path.startswith(base_path):
        raise Http404("Imagen no encontrada")
    
    # Verificar que el archivo exista
    if not os.path.exists(full_path) or not os.path.isfile(full_path):
        raise Http404(f"Imagen no encontrada: {full_path}")
    
    # Determinar el tipo de contenido basado en la extensión
    content_type = 'image/jpeg'  # Por defecto
    if full_path.lower().endswith('.png'):
        content_type = 'image/png'
    elif full_path.lower().endswith('.gif'):
        content_type = 'image/gif'
    elif full_path.lower().endswith('.webp'):
        content_type = 'image/webp'
    
    # Servir el archivo
    try:
        return FileResponse(open(full_path, 'rb'), content_type=content_type)
    except Exception as e:
        raise Http404(f"Error al cargar la imagen: {str(e)}")

# ============ AUTHENTICATION VIEWS ============

def get_config():
    """Obtiene la configuración de la app desde Complemento"""
    try:
        return Complemento.objects.first()
    except:
        return None

def home(request):
    """Home page view with featured products"""
    productos_destacados = Producto.objects.exclude(estado='Eliminado').order_by('-FecheIngreso')[:8]
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    config = get_config()
    
    context = {
        'productos': productos_destacados,
        'categorias': categorias,
        'config': config,
    }
    return render(request, 'index.html', context)

@csrf_protect
def login_view(request):
    """Login view using custom Usuario model"""
    if request.session.get('usuario_id'):
        return redirect('home')
    
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    
    if request.method == 'POST':
        email = request.POST.get('email', '').strip()
        password = request.POST.get('password', '')
        remember_me = request.POST.get('remember', False)
        
        if not email or not password:
            messages.error(request, 'Por favor completa todos los campos.')
            context = {
                'config': config,
                'categorias': categorias,
            }
            return render(request, 'login.html', context)
        
        try:
            validate_email(email)
        except ValidationError:
            messages.error(request, 'Por favor ingresa un email válido.')
            context = {
                'config': config,
                'categorias': categorias,
            }
            return render(request, 'login.html', context)
        
        try:
            usuario = Usuario.objects.get(correo=email)
            if usuario.check_password(password):
                # Guardar información en la sesión
                request.session['usuario_id'] = usuario.id
                request.session['usuario_nombre'] = usuario.nombre
                request.session['usuario_cargo'] = usuario.cargo
                
                # Forzar guardado de sesión
                request.session.save()
                
                # Debug en desarrollo
                if settings.DEBUG:
                    print(f"DEBUG Login - Usuario ID: {usuario.id}")
                    print(f"DEBUG Login - Nombre: {usuario.nombre}")
                    print(f"DEBUG Login - Cargo: {usuario.cargo}")
                    print(f"DEBUG Login - Cargo en sesión: {request.session.get('usuario_cargo')}")
                
                # La sesión persiste hasta que el usuario cierre sesión
                # Si marca "remember me", la sesión dura más tiempo
                if remember_me:
                    request.session.set_expiry(60 * 60 * 24 * 30)  # 30 días
                else:
                    request.session.set_expiry(60 * 60 * 24 * 7)  # 7 días por defecto
                
                messages.success(request, f'¡Bienvenido, {usuario.nombre}!')
                
                # Si es admin, mostrar mensaje especial
                if usuario.cargo == 'admin':
                    messages.info(request, 'Tienes acceso al panel de administración.')
                
                next_page = request.GET.get('next', 'home')
                return redirect(next_page)
            else:
                messages.error(request, 'Email o contraseña incorrectos.')
        except Usuario.DoesNotExist:
            messages.error(request, 'Email o contraseña incorrectos.')
    
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'login.html', context)

@csrf_protect
def register_view(request):
    """Register view using custom Usuario model"""
    if request.session.get('usuario_id'):
        return redirect('home')
    
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    
    if request.method == 'POST':
        nombre = request.POST.get('first_name', '').strip()
        apellido = request.POST.get('last_name', '').strip()
        email = request.POST.get('email', '').strip()
        password1 = request.POST.get('password1', '')
        password2 = request.POST.get('password2', '')
        identificacion = request.POST.get('identificacion', '').strip()
        tipo_identificacion = request.POST.get('tipo_identificacion', 'CC')
        usuario_nombre = request.POST.get('usuario', '').strip() or email.split('@')[0]
        
        errors = []
        
        if not nombre:
            errors.append('El nombre es requerido.')
        if not email:
            errors.append('El email es requerido.')
        if not password1:
            errors.append('La contraseña es requerida.')
        if password1 != password2:
            errors.append('Las contraseñas no coinciden.')
        if len(password1) < 6:
            errors.append('La contraseña debe tener al menos 6 caracteres.')
        
        if email:
            try:
                validate_email(email)
                if Usuario.objects.filter(correo=email).exists():
                    errors.append('Ya existe un usuario con este email.')
            except ValidationError:
                errors.append('Por favor ingresa un email válido.')
        
        if usuario_nombre and Usuario.objects.filter(usuario=usuario_nombre).exists():
            errors.append('El nombre de usuario ya está en uso.')
        
        if identificacion and Usuario.objects.filter(identificacion=identificacion).exists():
            errors.append('Ya existe un usuario con esta identificación.')
        
        if errors:
            for error in errors:
                messages.error(request, error)
            context = {
                'config': config,
                'categorias': categorias,
            }
            return render(request, 'register.html', context)
        
        try:
            usuario = Usuario.objects.create(
                nombre=nombre,
                apellido=apellido,
                correo=email,
                usuario=usuario_nombre,
                identificacion=int(identificacion) if identificacion else 0,
                TipoIdentificacion=tipo_identificacion,
                cargo='cliente'
            )
            usuario.set_password(password1)
            usuario.save()
            
            messages.success(request, '¡Cuenta creada exitosamente! Ya puedes iniciar sesión.')
            return redirect('login')
        except Exception as e:
            messages.error(request, f'Error al crear la cuenta: {str(e)}')
    
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'register.html', context)

def logout_view(request):
    """Logout view"""
    request.session.flush()
    messages.success(request, 'Has cerrado sesión exitosamente.')
    return redirect('home')

# ============ PRODUCT VIEWS ============

def shop(request):
    """Shop page with all products"""
    productos = Producto.objects.exclude(estado='Eliminado')
    config = get_config()
    
    # Filtros
    categoria = request.GET.get('categoria', '')
    busqueda = request.GET.get('q', '')
    orden = request.GET.get('orden', 'reciente')
    
    if categoria:
        productos = productos.filter(Categorias=categoria)
    
    if busqueda:
        productos = productos.filter(
            Q(Nombre__icontains=busqueda) |
            Q(Descripcion__icontains=busqueda) |
            Q(Categorias__icontains=busqueda)
        )
    
    # Ordenamiento
    if orden == 'precio_asc':
        productos = productos.order_by('inventarios__precio')
    elif orden == 'precio_desc':
        productos = productos.order_by('-inventarios__precio')
    elif orden == 'nombre':
        productos = productos.order_by('Nombre')
    else:
        productos = productos.order_by('-FecheIngreso')
    
    # Paginación
    paginator = Paginator(productos, 12)
    page = request.GET.get('page', 1)
    productos_paginados = paginator.get_page(page)
    
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    
    context = {
        'productos': productos_paginados,
        'categorias': categorias,
        'categoria_actual': categoria,
        'busqueda': busqueda,
        'orden': orden,
        'config': config,
    }
    return render(request, 'shop.html', context)

def producto_detalle(request, producto_id):
    """Product detail page"""
    producto = get_object_or_404(Producto, id=producto_id)
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    
    if producto.estado == 'Eliminado':
        messages.error(request, 'Este producto no está disponible.')
        return redirect('shop')
    
    inventarios = producto.inventarios.filter(cantidad__gt=0).select_related('id_talla')
    tallas_disponibles = [inv.id_talla for inv in inventarios]
    
    productos_relacionados = Producto.objects.filter(
        Categorias=producto.Categorias
    ).exclude(estado='Eliminado').exclude(id=producto_id)[:4]
    
    context = {
        'producto': producto,
        'inventarios': inventarios,
        'tallas_disponibles': tallas_disponibles,
        'productos_relacionados': productos_relacionados,
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'producto_detalle.html', context)

# ============ CART VIEWS ============

def get_or_create_cart(request):
    """Obtiene o crea un carro para el usuario"""
    usuario_id = request.session.get('usuario_id')
    if usuario_id:
        try:
            usuario = Usuario.objects.get(id=usuario_id)
            carro, created = Carro.objects.get_or_create(id_usuario=usuario)
            return carro
        except Usuario.DoesNotExist:
            pass
    
    # Si no hay usuario, usar carro de sesión
    carro_id = request.session.get('carro_id')
    if carro_id:
        try:
            return Carro.objects.get(id=carro_id)
        except Carro.DoesNotExist:
            pass
    
    # Crear nuevo carro usando SQL directo para evitar el error de AUTO_INCREMENT
    with connection.cursor() as cursor:
        if usuario_id:
            cursor.execute(
                "INSERT INTO tb_carro (id_usuario) VALUES (%s)",
                [usuario_id]
            )
        else:
            cursor.execute(
                "INSERT INTO tb_carro (id_usuario) VALUES (NULL)"
            )
        carro_id = cursor.lastrowid
    
    carro = Carro.objects.get(id=carro_id)
    request.session['carro_id'] = carro.id
    return carro

def carrito(request):
    """Cart page"""
    carro = get_or_create_cart(request)
    items = carro.items.all().select_related('id_producto', 'id_talla')
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    
    context = {
        'carro': carro,
        'items': items,
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'carrito.html', context)

@require_POST
@csrf_protect
def agregar_al_carrito(request):
    """Add product to cart"""
    producto_id = request.POST.get('producto_id')
    talla_id = request.POST.get('talla_id')
    cantidad = int(request.POST.get('cantidad', 1))
    
    try:
        producto = Producto.objects.get(id=producto_id)
        if producto.estado == 'Eliminado':
            return JsonResponse({
                'success': False,
                'message': 'Este producto no está disponible.'
            })
        talla = Talla.objects.get(id=talla_id)
        inventario = Inventario.objects.get(id_producto=producto, id_talla=talla)
        
        if inventario.cantidad < cantidad:
            return JsonResponse({
                'success': False,
                'message': f'Solo hay {inventario.cantidad} unidades disponibles.'
            })
        
        carro = get_or_create_cart(request)
        
        # Verificar si ya existe el item
        item_existente = CarroItem.objects.filter(
            id_carro=carro,
            id_producto=producto,
            id_talla=talla
        ).first()
        
        if item_existente:
            nueva_cantidad = item_existente.cantidad + cantidad
            if nueva_cantidad > inventario.cantidad:
                return JsonResponse({
                    'success': False,
                    'message': f'No hay suficiente stock. Disponible: {inventario.cantidad}'
                })
            item_existente.cantidad = nueva_cantidad
            item_existente.save()
        else:
            # Usar SQL directo para evitar el error de AUTO_INCREMENT
            # Primero obtener el siguiente ID disponible
            with connection.cursor() as cursor:
                # Obtener el máximo ID actual
                cursor.execute("SELECT COALESCE(MAX(id), 0) FROM tb_carro_items")
                max_id = cursor.fetchone()[0]
                next_id = max_id + 1
                
                # Insertar con el ID explícito
                cursor.execute(
                    """INSERT INTO tb_carro_items 
                       (id, id_carro, id_producto, id_talla, cantidad, precio_unitario) 
                       VALUES (%s, %s, %s, %s, %s, %s)""",
                    [next_id, carro.id, producto.id, talla.id, cantidad, inventario.precio]
                )
        
        # Actualizar el carro después de agregar el item
        carro.refresh_from_db()
        
        return JsonResponse({
            'success': True,
            'message': 'Producto agregado al carrito exitosamente',
            'carrito_count': carro.cantidad_items
        })
    except Producto.DoesNotExist:
        return JsonResponse({
            'success': False,
            'message': 'Producto no encontrado.'
        })
    except Talla.DoesNotExist:
        return JsonResponse({
            'success': False,
            'message': 'Talla no encontrada.'
        })
    except Inventario.DoesNotExist:
        return JsonResponse({
            'success': False,
            'message': 'Inventario no disponible para esta talla.'
        })
    except Exception as e:
        import traceback
        error_detail = traceback.format_exc() if settings.DEBUG else None
        return JsonResponse({
            'success': False,
            'message': f'Error al agregar al carrito: {str(e)}',
            'error_detail': error_detail
        })

@require_POST
@csrf_protect
def actualizar_carrito(request, item_id):
    """Update cart item quantity"""
    cantidad = int(request.POST.get('cantidad', 1))
    
    try:
        item = CarroItem.objects.get(id=item_id)
        inventario = Inventario.objects.get(
            id_producto=item.id_producto,
            id_talla=item.id_talla
        )
        
        if cantidad > inventario.cantidad:
            messages.error(request, f'Solo hay {inventario.cantidad} unidades disponibles.')
            return redirect('carrito')
        
        if cantidad <= 0:
            item.delete()
            messages.success(request, 'Item eliminado del carrito.')
        else:
            item.cantidad = cantidad
            item.save()
            messages.success(request, 'Carrito actualizado.')
        
        return redirect('carrito')
    except Exception as e:
        messages.error(request, f'Error: {str(e)}')
        return redirect('carrito')

@require_POST
@csrf_protect
def eliminar_del_carrito(request, item_id):
    """Remove item from cart"""
    try:
        item = CarroItem.objects.get(id=item_id)
        item.delete()
        messages.success(request, 'Item eliminado del carrito.')
    except Exception as e:
        messages.error(request, f'Error: {str(e)}')
    
    return redirect('carrito')

# ============ CHECKOUT VIEWS ============

def checkout(request):
    """Checkout page"""
    if not request.session.get('usuario_id'):
        messages.warning(request, 'Debes iniciar sesión para realizar una compra.')
        return redirect('login')
    
    carro = get_or_create_cart(request)
    items = carro.items.all().select_related('id_producto', 'id_talla')
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    
    if not items.exists():
        messages.warning(request, 'Tu carrito está vacío.')
        return redirect('carrito')
    
    for item in items:
        inventario = Inventario.objects.get(
            id_producto=item.id_producto,
            id_talla=item.id_talla
        )
        if item.cantidad > inventario.cantidad:
            messages.error(request, f'{item.id_producto.Nombre} - Talla {item.id_talla.nombre}: Solo hay {inventario.cantidad} unidades disponibles.')
            return redirect('carrito')
    
    try:
        usuario = Usuario.objects.get(id=request.session['usuario_id'])
    except Usuario.DoesNotExist:
        messages.error(request, 'Usuario no encontrado.')
        return redirect('login')
    
    subtotal = carro.total
    impuestos = subtotal * Decimal('0.19')
    total_con_impuestos = subtotal + impuestos
    
    context = {
        'carro': carro,
        'items': items,
        'usuario': usuario,
        'subtotal': subtotal,
        'impuestos': impuestos,
        'total_con_impuestos': total_con_impuestos,
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'checkout.html', context)

@require_POST
@csrf_protect
def procesar_pago(request):
    """Process payment"""
    if not request.session.get('usuario_id'):
        return JsonResponse({'success': False, 'message': 'Debes iniciar sesión.'})
    
    try:
        usuario = Usuario.objects.get(id=request.session['usuario_id'])
        carro = get_or_create_cart(request)
        items = carro.items.all()
        
        if not items.exists():
            return JsonResponse({'success': False, 'message': 'El carrito está vacío.'})
        
        metodo_pago = request.POST.get('metodo_pago', 'Efectivo')
        subtotal = carro.total
        impuestos = subtotal * Decimal('0.19')  # IVA 19%
        total = subtotal + impuestos
        
        # Crear pago
        pago = Pago.objects.create(
            id_usuario=usuario,
            metodo_pago=metodo_pago,
            subtotal=subtotal,
            impuestos=impuestos,
            descuentos=Decimal('0.00'),
            total=total,
            estado='Completado'
        )
        
        # Crear detalles del pago y actualizar inventario
        for item in items:
            PagoDetalle.objects.create(
                id_pago=pago,
                id_producto=item.id_producto,
                id_talla=item.id_talla,
                cantidad=item.cantidad,
                precio_unitario=item.precio_unitario,
                subtotal=item.subtotal
            )
            
            # Actualizar inventario
            inventario = Inventario.objects.get(
                id_producto=item.id_producto,
                id_talla=item.id_talla
            )
            inventario.cantidad -= item.cantidad
            inventario.save()
        
        # Limpiar carrito
        items.delete()
        carro.delete()
        if 'carro_id' in request.session:
            del request.session['carro_id']
        
        messages.success(request, f'¡Pago procesado exitosamente! ID de pago: {pago.id_pago}')
        return JsonResponse({
            'success': True,
            'message': 'Pago procesado exitosamente',
            'pago_id': pago.id_pago
        })
    except Exception as e:
        return JsonResponse({
            'success': False,
            'message': f'Error al procesar el pago: {str(e)}'
        })

# ============ PROFILE VIEWS ============

def perfil(request):
    """User profile page"""
    if not request.session.get('usuario_id'):
        messages.warning(request, 'Debes iniciar sesión para ver tu perfil.')
        return redirect('login')
    
    try:
        usuario = Usuario.objects.get(id=request.session['usuario_id'])
        pagos = Pago.objects.filter(id_usuario=usuario).order_by('-fecha_pago')[:10]
    except Usuario.DoesNotExist:
        messages.error(request, 'Usuario no encontrado.')
        return redirect('login')
    
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    
    context = {
        'usuario': usuario,
        'pagos': pagos,
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'perfil.html', context)

@require_POST
@csrf_protect
def actualizar_perfil(request):
    """Update user profile"""
    if not request.session.get('usuario_id'):
        return redirect('login')
    
    try:
        usuario = Usuario.objects.get(id=request.session['usuario_id'])
        
        usuario.nombre = request.POST.get('nombre', usuario.nombre)
        usuario.apellido = request.POST.get('apellido', usuario.apellido)
        usuario.correo = request.POST.get('correo', usuario.correo)
        usuario.descripcion = request.POST.get('descripcion', usuario.descripcion)
        
        password = request.POST.get('password', '')
        if password:
            usuario.set_password(password)
        
        usuario.save()
        messages.success(request, 'Perfil actualizado exitosamente.')
    except Exception as e:
        messages.error(request, f'Error al actualizar perfil: {str(e)}')
    
    return redirect('perfil')

# ============ AJAX VIEWS ============

def obtener_tallas_disponibles(request, producto_id):
    """Get available sizes for a product"""
    try:
        producto = Producto.objects.get(id=producto_id)
        inventarios = producto.inventarios.filter(cantidad__gt=0).select_related('id_talla')
        
        tallas = [{
            'id': inv.id_talla.id,
            'nombre': inv.id_talla.nombre,
            'precio': float(inv.precio),
            'cantidad': inv.cantidad
        } for inv in inventarios]
        
        return JsonResponse({'success': True, 'tallas': tallas})
    except Exception as e:
        return JsonResponse({'success': False, 'message': str(e)})

def obtener_carrito_count(request):
    """Get cart item count"""
    carro = get_or_create_cart(request)
    return JsonResponse({'count': carro.cantidad_items})

# ============ FOOTER PAGES ============

def sobre_nosotros(request):
    """About Us page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/sobre_nosotros.html', context)

def contacto(request):
    """Contact page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/contacto.html', context)

def guia_tallas(request):
    """Size Guide page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/guia_tallas.html', context)

def info_envio(request):
    """Shipping Information page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/info_envio.html', context)

def devoluciones(request):
    """Returns page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/devoluciones.html', context)

def rastrear_pedido(request):
    """Track Order page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/rastrear_pedido.html', context)

def preguntas_frecuentes(request):
    """FAQ page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/preguntas_frecuentes.html', context)

def metodos_pago(request):
    """Payment Methods page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/metodos_pago.html', context)

def tarjetas_regalo(request):
    """Gift Cards page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/tarjetas_regalo.html', context)

def ubicacion_tiendas(request):
    """Store Locations page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/ubicacion_tiendas.html', context)

def politica_privacidad(request):
    """Privacy Policy page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/politica_privacidad.html', context)

def terminos_condiciones(request):
    """Terms and Conditions page"""
    config = get_config()
    categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()
    context = {
        'config': config,
        'categorias': categorias,
    }
    return render(request, 'pages/terminos_condiciones.html', context)
