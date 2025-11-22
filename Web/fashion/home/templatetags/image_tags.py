# Template tags para procesar URLs de imágenes y formatear precios
from django import template
import os
from django.conf import settings
from decimal import Decimal

register = template.Library()

@register.filter
def formato_colombiano(value):
    """
    Formatea un número en formato colombiano con puntos como separadores de miles.
    Ejemplo: 1000000 -> 1.000.000
    """
    if value is None:
        return "0"
    
    try:
        # Convertir a entero si es Decimal o float
        if isinstance(value, (Decimal, float)):
            num = int(value)
        else:
            num = int(value)
        
        # Formatear con puntos como separadores de miles
        return f"{num:,}".replace(",", ".")
    except (ValueError, TypeError):
        return str(value)

@register.filter
def precio_colombiano(value):
    """
    Formatea un precio en formato colombiano con símbolo de peso.
    Ejemplo: 1000000 -> $1.000.000 COP
    """
    if value is None:
        return "$0 COP"
    
    formatted = formato_colombiano(value)
    return f"${formatted} COP"

@register.filter
def fix_image_url(img_url):
    """
    Convierte las URLs de imágenes de la base de datos en URLs accesibles.
    Las imágenes pueden estar en rutas externas como '...Z-Global-Img/...'
    
    Ejemplo:
    - Entrada: '...Z-Global-Img/producto.jpg'
    - Salida: '/media/external/...Z-Global-Img/producto.jpg'
    """
    if not img_url or img_url == '...' or img_url == '':
        return None
    
    # Si la URL ya es una URL completa (http:// o https://)
    if img_url.startswith('http://') or img_url.startswith('https://'):
        return img_url
    
    # Si la URL comienza con '...', mantenerla tal cual para que coincida con la carpeta
    # La carpeta base ya incluye '...Z-Global-Img', así que mantenemos la ruta completa
    if img_url.startswith('...'):
        # Limpiar la ruta (remover barras iniciales si las hay, pero mantener los puntos)
        relative_path = img_url.lstrip('/')
        
        # Construir la URL para servir desde la vista
        return f'/media/external/{relative_path}'
    
    # Si la URL es relativa pero no comienza con '...'
    if not img_url.startswith('/'):
        # Limpiar la ruta
        clean_path = img_url.lstrip('/')
        return f'/media/external/{clean_path}'
    
    # Si ya es una ruta absoluta que comienza con /, procesarla
    clean_path = img_url.lstrip('/')
    return f'/media/external/{clean_path}'

