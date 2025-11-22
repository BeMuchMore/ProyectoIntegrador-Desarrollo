# FASHION - E-commerce Django

Sistema de e-commerce completo desarrollado con Django y MySQL.

## Características

- ✅ Sistema de autenticación personalizado
- ✅ Catálogo de productos con categorías
- ✅ Carrito de compras funcional
- ✅ Sistema de checkout y pagos
- ✅ Perfil de usuario
- ✅ Historial de pedidos
- ✅ Panel de administración
- ✅ Gestión de inventario por tallas
- ✅ Búsqueda y filtrado de productos

## Requisitos

- Python 3.8+
- Django 4.2+
- MySQL 5.7+ o MariaDB 10.4+
- pip

## Instalación

1. **Clonar el repositorio o navegar al directorio del proyecto**

2. **Crear un entorno virtual (recomendado)**
```bash
python -m venv Ambiente
```

3. **Activar el entorno virtual**
- Windows:
```bash
Ambiente\Scripts\activate
```
- Linux/Mac:
```bash
source Ambiente/bin/activate
```

4. **Instalar dependencias**
```bash
pip install django mysqlclient pymysql
```

5. **Configurar la base de datos**

   - Crear una base de datos MySQL llamada `basedatos`
   - Importar el archivo SQL proporcionado:
   ```bash
   mysql -u root -p basedatos < "basedatos (7).sql"
   ```

6. **Configurar settings.py**

   Editar `fashion/fashion/settings.py` y ajustar las credenciales de la base de datos:
   ```python
   DATABASES = {
       'default': {
           'ENGINE': 'django.db.backends.mysql',
           'NAME': 'basedatos',
           'USER': 'root',  # Tu usuario de MySQL
           'PASSWORD': '',  # Tu contraseña de MySQL
           'HOST': '127.0.0.1',
           'PORT': '3306',
       }
   }
   ```

7. **Ejecutar migraciones (si es necesario)**
```bash
cd fashion
python manage.py makemigrations
python manage.py migrate
```

8. **Crear superusuario (opcional)**
```bash
python manage.py createsuperuser
```

9. **Ejecutar el servidor de desarrollo**
```bash
python manage.py runserver
```

10. **Acceder a la aplicación**
   - Frontend: http://127.0.0.1:8000/
   - Admin: http://127.0.0.1:8000/admin/

## Estructura del Proyecto

```
fashion/
├── fashion/          # Configuración del proyecto
│   ├── settings.py   # Configuración
│   ├── urls.py       # URLs principales
│   └── wsgi.py       # WSGI config
├── home/             # Aplicación principal
│   ├── models.py     # Modelos de datos
│   ├── views.py      # Vistas
│   ├── urls.py       # URLs de la app
│   └── admin.py      # Configuración del admin
├── templates/        # Templates HTML
│   ├── index.html    # Página principal
│   ├── shop.html     # Tienda
│   ├── producto_detalle.html
│   ├── carrito.html
│   ├── checkout.html
│   ├── perfil.html
│   ├── login.html
│   └── register.html
├── static/           # Archivos estáticos
│   ├── css/
│   └── js/
└── manage.py
```

## Modelos de Datos

- **Usuario**: Usuarios del sistema (clientes y administradores)
- **Producto**: Productos de la tienda
- **Talla**: Tallas disponibles
- **Inventario**: Stock de productos por talla
- **Carro**: Carritos de compra
- **CarroItem**: Items en el carrito
- **Pago**: Pagos realizados
- **PagoDetalle**: Detalles de cada pago
- **ProductoHistorial**: Historial de cambios en productos
- **Complemento**: Configuración de la aplicación

## Funcionalidades Principales

### Para Clientes
- Registro e inicio de sesión
- Navegación por productos
- Búsqueda y filtrado
- Agregar productos al carrito
- Proceso de checkout
- Ver historial de pedidos
- Actualizar perfil

### Para Administradores
- Panel de administración Django
- Gestión de productos
- Gestión de usuarios
- Gestión de inventario
- Ver pedidos y pagos
- Historial de cambios

## Uso

1. **Registrarse**: Crear una cuenta nueva
2. **Navegar**: Explorar productos en la tienda
3. **Agregar al carrito**: Seleccionar talla y cantidad
4. **Checkout**: Completar información de envío y pago
5. **Confirmar**: Procesar el pedido

## Notas Importantes

- Las contraseñas se almacenan en texto plano (NO RECOMENDADO PARA PRODUCCIÓN)
- En producción, usar hash de contraseñas con `django.contrib.auth.hashers`
- Configurar `DEBUG = False` en producción
- Configurar `ALLOWED_HOSTS` en producción
- Usar HTTPS en producción
- Configurar servidor de archivos estáticos en producción

## Solución de Problemas

### Error de conexión a MySQL
- Verificar que MySQL esté corriendo
- Verificar credenciales en settings.py
- Verificar que la base de datos exista

### Error de migraciones
- Verificar que los modelos estén correctamente definidos
- Ejecutar `python manage.py makemigrations` nuevamente

### Error de archivos estáticos
- Ejecutar `python manage.py collectstatic`
- Verificar configuración de STATIC_URL y STATIC_ROOT

## Licencia

Este proyecto es de uso educativo.

## Autor

Desarrollado para Proyecto Integrador - Desarrollo

