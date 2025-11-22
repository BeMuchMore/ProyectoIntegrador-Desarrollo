# FASHION - Moda Femenina Premium

Sitio web estático para GitHub Pages del proyecto FASHION.

## 🚀 Ver el sitio en vivo

Una vez configurado GitHub Pages, tu sitio estará disponible en:
`https://tu-usuario.github.io/nombre-del-repositorio/`

## 📁 Estructura del proyecto

```
github-pages/
├── index.html          # Página principal
├── css/
│   └── style.css      # Estilos CSS
├── js/
│   └── script.js      # JavaScript interactivo
├── images/            # Imágenes del sitio
└── README.md          # Este archivo
```

## 🎨 Características

- ✅ Diseño responsive (móvil, tablet, desktop)
- ✅ Navegación suave entre secciones
- ✅ Efectos de hover y animaciones
- ✅ Formulario de newsletter
- ✅ Carrito de compras simulado
- ✅ Optimizado para SEO
- ✅ Carga rápida y eficiente

## 🛠️ Configuración de GitHub Pages

### Paso 1: Crear repositorio en GitHub
1. Ve a [GitHub](https://github.com) e inicia sesión
2. Haz clic en "New repository" (Nuevo repositorio)
3. Nombra tu repositorio (ej: `fashion-website`)
4. Marca como público
5. Haz clic en "Create repository"

### Paso 2: Subir archivos
```bash
# En tu terminal/cmd, navega a la carpeta github-pages
cd github-pages

# Inicializar git
git init

# Agregar archivos
git add .

# Hacer commit
git commit -m "Initial commit - FASHION website"

# Conectar con GitHub (reemplaza con tu URL)
git remote add origin https://github.com/TU-USUARIO/TU-REPOSITORIO.git

# Subir archivos
git push -u origin main
```

### Paso 3: Activar GitHub Pages
1. Ve a tu repositorio en GitHub
2. Haz clic en "Settings" (Configuración)
3. Scroll hacia abajo hasta "Pages"
4. En "Source", selecciona "Deploy from a branch"
5. Selecciona "main" branch
6. Selecciona "/ (root)" folder
7. Haz clic en "Save"

### Paso 4: ¡Listo!
Tu sitio estará disponible en unos minutos en:
`https://tu-usuario.github.io/nombre-del-repositorio/`

## 📱 Personalización

### Cambiar colores
Edita las variables CSS en `css/style.css`:
```css
/* Colores principales */
--color-primary: #d4a5c7;
--color-secondary: #c98bb8;
--color-accent: #ff6b9d;
```

### Agregar imágenes
1. Sube tus imágenes a la carpeta `images/`
2. Actualiza las rutas en `index.html`
3. Formatos recomendados: JPG, PNG, WebP
4. Tamaño recomendado: máximo 1MB por imagen

### Modificar contenido
- Edita `index.html` para cambiar textos y estructura
- Modifica `css/style.css` para cambiar estilos
- Actualiza `js/script.js` para nueva funcionalidad

## 🔧 Comandos útiles

### Actualizar el sitio
```bash
git add .
git commit -m "Actualización del sitio"
git push
```

### Clonar en otro dispositivo
```bash
git clone https://github.com/TU-USUARIO/TU-REPOSITORIO.git
```

## 📞 Soporte

Si necesitas ayuda:
1. Revisa la [documentación de GitHub Pages](https://docs.github.com/es/pages)
2. Verifica que todos los archivos estén subidos correctamente
3. Asegúrate de que el repositorio sea público
4. Los cambios pueden tardar hasta 10 minutos en aparecer

## 🌟 Próximas mejoras

- [ ] Integración con carrito de compras real
- [ ] Sistema de usuarios y login
- [ ] Pasarela de pagos
- [ ] Panel de administración
- [ ] Base de datos de productos
- [ ] Sistema de comentarios y reseñas

---

**¡Tu sitio web FASHION está listo para brillar en GitHub Pages! 🎉**
