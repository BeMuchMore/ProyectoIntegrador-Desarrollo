"""
Context processors para hacer disponible el usuario en todos los templates
"""
from .models import Usuario, Complemento, Producto

def usuario_context(request):
    """
    Hace disponible el usuario logueado en todos los templates
    """
    usuario = None
    usuario_id = request.session.get('usuario_id')
    
    if usuario_id:
        try:
            usuario = Usuario.objects.get(id=usuario_id)
        except Usuario.DoesNotExist:
            pass
    
    return {
        'usuario_actual': usuario,
        'esta_logueado': usuario is not None,
    }

def config_context(request):
    """
    Hace disponible la configuración de la app y categorías en todos los templates
    """
    try:
        config = Complemento.objects.first()
    except:
        config = None
    
    # Obtener categorías para el menú
    try:
        categorias = Producto.objects.exclude(estado='Eliminado').exclude(Categorias__isnull=True).exclude(Categorias='').values_list('Categorias', flat=True).distinct()[:10]
    except:
        categorias = []
    
    return {
        'config': config,
        'categorias': categorias,
    }

