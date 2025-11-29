-- =====================================================
-- ACTUALIZACIÓN DE TABLA tb_complementos
-- Incluye todas las mejoras realizadas en la aplicación
-- Fecha: 28 de Noviembre de 2025
-- =====================================================

-- Opción 1: Actualizar el registro existente (recomendado)
UPDATE `tb_complementos` 
SET 
    `NombreDeApp` = 'Fashion',
    `CorreoApp` = 'CorreoPrueba@correounivalle.edu.co',
    `TerminosCondiones` = 'Bienvenido(a) a Fashion – Ropía Propia Colombia, una aplicación desarrollada para ofrecerte una experiencia moderna y segura en la compra de ropa y accesorios.

Al acceder y usar nuestra aplicación, aceptas los siguientes Términos y Condiciones. Te recomendamos leerlos atentamente antes de realizar cualquier compra.

1. ACEPTACIÓN DE LOS TÉRMINOS
El uso de esta aplicación implica la aceptación plena de los presentes Términos y Condiciones. Si no estás de acuerdo con ellos, te pedimos no continuar con el uso de la app.

2. ACCESO A LA APLICACIÓN
El acceso para ver productos es libre y no requiere registro. Sin embargo, para realizar compras, el usuario deberá crear una cuenta personal proporcionando información verídica, actualizada y completa.

3. REGISTRO Y CUENTA DE USUARIO
El usuario se compromete a mantener la confidencialidad de su cuenta y contraseña. Cualquier actividad realizada desde su cuenta se considerará responsabilidad del usuario. Fashion – Ropía Propia Colombia no se hace responsable por el uso indebido de cuentas personales.

4. PRODUCTOS Y PRECIOS
Todos los productos publicados incluyen información detallada sobre su descripción, talla, color y precio. Nos reservamos el derecho de modificar precios, descuentos o disponibilidad de productos en cualquier momento, sin previo aviso. Las imágenes son de carácter ilustrativo y pueden presentar ligeras variaciones respecto al producto real.

5. PAGOS
Los pagos se realizan a través de las opciones disponibles en la aplicación (tarjeta, transferencia o pago contra entrega, según disponibilidad). El usuario garantiza que los datos proporcionados para el pago son verídicos y que cuenta con la autorización para utilizarlos.

6. ENVÍOS Y ENTREGAS
Los tiempos de entrega varían según la ciudad o municipio. Fashion – Ropía Propia Colombia se compromete a despachar los pedidos en los plazos establecidos, sin embargo, no se responsabiliza por retrasos ocasionados por transportadoras o causas de fuerza mayor. El usuario debe verificar los datos de envío antes de confirmar la compra.

7. CAMBIOS Y DEVOLUCIONES
Podrás solicitar cambio o devolución dentro de los 5 días hábiles posteriores a la entrega, siempre que el producto: No haya sido usado, lavado o modificado. Conserve sus etiquetas y empaque original. Los costos de envío para devoluciones o cambios correrán por cuenta del cliente, salvo que el error sea atribuible a Fashion – Ropía Propia Colombia.

8. PROPIEDAD INTELECTUAL
Todos los contenidos de la aplicación (nombre, logotipo, imágenes, textos, diseño y código) son propiedad exclusiva de Fashion – Ropía Propia Colombia y están protegidos por la legislación colombiana. Queda prohibida su copia, distribución o uso sin autorización previa.

9. PRIVACIDAD Y PROTECCIÓN DE DATOS
La información personal del usuario será tratada conforme a nuestra Política de Privacidad, cumpliendo con la Ley 1581 de 2012 de Protección de Datos Personales en Colombia. Tus datos serán utilizados únicamente para la gestión de compras, envíos y comunicación con la empresa.

10. RESPONSABILIDAD
Fashion – Ropía Propia Colombia no será responsable por daños, pérdidas o perjuicios derivados del uso inadecuado de la aplicación o de los productos adquiridos. El usuario es responsable de revisar las especificaciones del producto antes de efectuar la compra.

11. MODIFICACIONES
Nos reservamos el derecho de actualizar o modificar estos Términos y Condiciones en cualquier momento. Las modificaciones serán publicadas en la aplicación y entrarán en vigor de inmediato.

12. CONTACTO
📧 Correo: CorreoPrueba@correounivalle.edu.co
📞 Para más información, contacta con nuestro equipo de atención al cliente.',
    `UltimaActualizacion` = NOW()
WHERE `id` = 1;

-- =====================================================
-- Opción 2: INSERT para crear un nuevo registro 
-- (usar solo si no existe registro con id = 1)
-- =====================================================

/*
INSERT INTO `tb_complementos` (
    `id`, 
    `fondoPrincipal`, 
    `NombreDeApp`, 
    `CorreoApp`, 
    `TerminosCondiones`, 
    `TipoMoneda`, 
    `Logo`, 
    `UltimaActualizacion`
) VALUES (
    1,
    '...Z-Global-Img/complementos/fondo_20251110_220052.jpeg',
    'Fashion',
    'CorreoPrueba@correounivalle.edu.co',
    'Bienvenido(a) a Fashion – Ropía Propia Colombia, una aplicación desarrollada para ofrecerte una experiencia moderna y segura en la compra de ropa y accesorios.

Al acceder y usar nuestra aplicación, aceptas los siguientes Términos y Condiciones. Te recomendamos leerlos atentamente antes de realizar cualquier compra.

1. ACEPTACIÓN DE LOS TÉRMINOS
El uso de esta aplicación implica la aceptación plena de los presentes Términos y Condiciones. Si no estás de acuerdo con ellos, te pedimos no continuar con el uso de la app.

2. ACCESO A LA APLICACIÓN
El acceso para ver productos es libre y no requiere registro. Sin embargo, para realizar compras, el usuario deberá crear una cuenta personal proporcionando información verídica, actualizada y completa.

3. REGISTRO Y CUENTA DE USUARIO
El usuario se compromete a mantener la confidencialidad de su cuenta y contraseña. Cualquier actividad realizada desde su cuenta se considerará responsabilidad del usuario. Fashion – Ropía Propia Colombia no se hace responsable por el uso indebido de cuentas personales.

4. PRODUCTOS Y PRECIOS
Todos los productos publicados incluyen información detallada sobre su descripción, talla, color y precio. Nos reservamos el derecho de modificar precios, descuentos o disponibilidad de productos en cualquier momento, sin previo aviso. Las imágenes son de carácter ilustrativo y pueden presentar ligeras variaciones respecto al producto real.

5. PAGOS
Los pagos se realizan a través de las opciones disponibles en la aplicación (tarjeta, transferencia o pago contra entrega, según disponibilidad). El usuario garantiza que los datos proporcionados para el pago son verídicos y que cuenta con la autorización para utilizarlos.

6. ENVÍOS Y ENTREGAS
Los tiempos de entrega varían según la ciudad o municipio. Fashion – Ropía Propia Colombia se compromete a despachar los pedidos en los plazos establecidos, sin embargo, no se responsabiliza por retrasos ocasionados por transportadoras o causas de fuerza mayor. El usuario debe verificar los datos de envío antes de confirmar la compra.

7. CAMBIOS Y DEVOLUCIONES
Podrás solicitar cambio o devolución dentro de los 5 días hábiles posteriores a la entrega, siempre que el producto: No haya sido usado, lavado o modificado. Conserve sus etiquetas y empaque original. Los costos de envío para devoluciones o cambios correrán por cuenta del cliente, salvo que el error sea atribuible a Fashion – Ropía Propia Colombia.

8. PROPIEDAD INTELECTUAL
Todos los contenidos de la aplicación (nombre, logotipo, imágenes, textos, diseño y código) son propiedad exclusiva de Fashion – Ropía Propia Colombia y están protegidos por la legislación colombiana. Queda prohibida su copia, distribución o uso sin autorización previa.

9. PRIVACIDAD Y PROTECCIÓN DE DATOS
La información personal del usuario será tratada conforme a nuestra Política de Privacidad, cumpliendo con la Ley 1581 de 2012 de Protección de Datos Personales en Colombia. Tus datos serán utilizados únicamente para la gestión de compras, envíos y comunicación con la empresa.

10. RESPONSABILIDAD
Fashion – Ropía Propia Colombia no será responsable por daños, pérdidas o perjuicios derivados del uso inadecuado de la aplicación o de los productos adquiridos. El usuario es responsable de revisar las especificaciones del producto antes de efectuar la compra.

11. MODIFICACIONES
Nos reservamos el derecho de actualizar o modificar estos Términos y Condiciones en cualquier momento. Las modificaciones serán publicadas en la aplicación y entrarán en vigor de inmediato.

12. CONTACTO
📧 Correo: CorreoPrueba@correounivalle.edu.co
📞 Para más información, contacta con nuestro equipo de atención al cliente.',
    NULL,
    '...Z-Global-Img/complementos/logo_20251110_220205.jpg',
    NOW()
);
*/

-- =====================================================
-- RESUMEN DE ACTUALIZACIONES REALIZADAS:
-- =====================================================
-- ✅ NombreDeApp actualizado a 'Fashion'
-- ✅ CorreoApp actualizado a 'CorreoPrueba@correounivalle.edu.co'
-- ✅ TerminosCondiones actualizado con texto completo
-- ✅ UltimaActualizacion actualizada a fecha/hora actual
-- ✅ Mantiene Logo y fondoPrincipal existentes
-- 
-- FUNCIONALIDADES IMPLEMENTADAS:
-- ✅ Todos los diálogos (Inicio, Registro, Recuperación, Condiciones) 
--    ahora cargan datos dinámicamente desde tb_complementos
-- ✅ Todos los diálogos aparecen como modales encima de las ventanas principales
-- ✅ Todos los diálogos tienen botón cerrar personalizado
-- ✅ Tamaño unificado de 1200x700 píxeles
-- ✅ Diseño moderno y consistente en todos los diálogos
-- =====================================================

