# 📋 INSTRUCCIONES PASO A PASO - GitHub Pages

## 🎯 Objetivo
Publicar tu sitio web FASHION en GitHub Pages de forma gratuita.

---

## 📋 PASO 1: Preparar GitHub

### 1.1 Crear cuenta en GitHub (si no tienes)
1. Ve a [github.com](https://github.com)
2. Haz clic en "Sign up" (Registrarse)
3. Completa el formulario con:
   - Username (nombre de usuario único)
   - Email
   - Contraseña
4. Verifica tu email

### 1.2 Crear nuevo repositorio
1. Inicia sesión en GitHub
2. Haz clic en el botón verde "New" o "+" → "New repository"
3. Completa:
   - **Repository name**: `fashion-website` (o el nombre que prefieras)
   - **Description**: "Sitio web de moda femenina premium"
   - ✅ Marcar "Public" (debe ser público para GitHub Pages gratuito)
   - ✅ Marcar "Add a README file"
4. Haz clic en "Create repository"

---

## 📋 PASO 2: Subir archivos

### Opción A: Usando la interfaz web de GitHub (Más fácil)

1. En tu repositorio recién creado, haz clic en "uploading an existing file"
2. Arrastra TODOS los archivos de la carpeta `github-pages`:
   - `index.html`
   - Carpeta `css/` con `style.css`
   - Carpeta `js/` con `script.js`
   - Carpeta `images/` (vacía por ahora)
3. Escribe un mensaje de commit: "Agregar sitio web FASHION"
4. Haz clic en "Commit changes"

### Opción B: Usando Git (Más avanzado)

```bash
# 1. Abrir terminal/cmd en la carpeta github-pages
cd ruta/a/tu/github-pages

# 2. Inicializar repositorio git
git init
 
# 3. Agregar archivos
git add .

# 4. Hacer primer commit
git commit -m "Sitio web FASHION inicial"

# 5. Conectar con GitHub (reemplaza con tu URL)
git remote add origin https://github.com/TU-USUARIO/fashion-website.git

# 6. Subir archivos
git branch -M main
git push -u origin main
```

---

## 📋 PASO 3: Activar GitHub Pages

1. En tu repositorio de GitHub, haz clic en **"Settings"** (Configuración)
2. Scroll hacia abajo hasta encontrar **"Pages"** en el menú lateral
3. En la sección **"Source"**:
   - Selecciona **"Deploy from a branch"**
   - Branch: **"main"**
   - Folder: **"/ (root)"**
4. Haz clic en **"Save"**
5. ¡Listo! GitHub te mostrará la URL de tu sitio

---

## 📋 PASO 4: Verificar tu sitio

### 4.1 Obtener la URL
Tu sitio estará disponible en:
```
https://TU-USUARIO.github.io/fashion-website/
```

### 4.2 Tiempo de espera
- Primera vez: 5-10 minutos
- Actualizaciones: 1-5 minutos

### 4.3 Verificar que funciona
1. Abre la URL en tu navegador
2. Verifica que se vea correctamente
3. Prueba la navegación entre secciones
4. Verifica que sea responsive (móvil/tablet)

---

## 📋 PASO 5: Agregar imágenes (Opcional)

### 5.1 Preparar imágenes
1. Optimiza tus imágenes:
   - Formato: JPG o PNG
   - Tamaño: máximo 1MB cada una
   - Resolución: 1200px de ancho máximo

### 5.2 Subir imágenes
1. Ve a tu repositorio en GitHub
2. Entra a la carpeta `images/`
3. Haz clic en "Add file" → "Upload files"
4. Sube tus imágenes:
   - `hero-fashion.jpg` (imagen principal)
   - `producto1.jpg`, `producto2.jpg`, etc.
   - `categoria-vestidos.jpg`, etc.

### 5.3 Actualizar HTML
Edita `index.html` y cambia las rutas de imágenes:
```html
<!-- Antes -->
<img src="images/hero-fashion.jpg" alt="Moda Femenina" />

<!-- Después (con tu imagen real) -->
<img src="images/tu-imagen-hero.jpg" alt="Moda Femenina" />
```

---

## 📋 PASO 6: Personalizar contenido

### 6.1 Cambiar textos
Edita `index.html`:
- Título de la página
- Textos del hero
- Nombres de productos
- Información de contacto

### 6.2 Cambiar colores
Edita `css/style.css`:
```css
/* Busca estas líneas y cambia los colores */
background: linear-gradient(135deg, #d4a5c7 0%, #c98bb8 100%);
```

### 6.3 Agregar más productos
Copia y pega este código en `index.html`:
```html
<div class="producto-card">
    <img src="images/tu-producto.jpg" alt="Nombre del Producto" />
    <div class="producto-info">
        <h3>Nombre del Producto</h3>
        <p class="precio">$XX.XX</p>
        <button class="btn-add-cart">Agregar al Carrito</button>
    </div>
</div>
```

---

## 📋 PASO 7: Mantener actualizado

### 7.1 Hacer cambios
1. Edita los archivos localmente
2. Sube los cambios a GitHub:
   - **Interfaz web**: Edita directamente en GitHub
   - **Git**: `git add .` → `git commit -m "mensaje"` → `git push`

### 7.2 Ver cambios
Los cambios aparecerán en tu sitio en 1-5 minutos.

---

## 🚨 SOLUCIÓN DE PROBLEMAS

### Problema: "404 - Page not found"
**Solución**: 
- Verifica que el archivo se llame exactamente `index.html`
- Asegúrate de que esté en la raíz del repositorio

### Problema: "CSS no se carga"
**Solución**:
- Verifica que la carpeta `css/` esté en la raíz
- Revisa que el archivo se llame `style.css`
- Verifica la ruta en `index.html`: `<link rel="stylesheet" href="css/style.css">`

### Problema: "Imágenes no aparecen"
**Solución**:
- Verifica que las imágenes estén en la carpeta `images/`
- Revisa las rutas en `index.html`
- Asegúrate de que los nombres coincidan exactamente

### Problema: "Cambios no aparecen"
**Solución**:
- Espera 5-10 minutos
- Refresca la página con Ctrl+F5 (Windows) o Cmd+Shift+R (Mac)
- Verifica que los archivos se hayan subido correctamente

---

## 🎉 ¡FELICIDADES!

Tu sitio web FASHION ya está en línea y disponible para todo el mundo.

### Próximos pasos:
1. ✅ Comparte tu URL con amigos y familiares
2. ✅ Agrega más productos e imágenes
3. ✅ Personaliza colores y textos
4. ✅ Considera agregar un dominio personalizado
5. ✅ Explora más funcionalidades de GitHub Pages

### URL de tu sitio:
```
https://TU-USUARIO.github.io/fashion-website/
```

**¡Tu sitio web está listo para conquistar el mundo de la moda! 👗✨**
