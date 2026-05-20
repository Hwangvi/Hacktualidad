# ⚡ Hacktualidad - E-Commerce & Hacktivity Hub

[![Angular](https://img.shields.io/badge/Angular-17%2B-DD0031?style=for-the-badge&logo=angular&logoColor=white)](https://angular.io/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Cloudinary](https://img.shields.io/badge/Cloudinary-Media-3448C5?style=for-the-badge&logo=cloudinary&logoColor=white)](https://cloudinary.com/)
[![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)

> **Status del Proyecto:** 🟢 Desplegado y Operativo en Producción.

**Hacktualidad** es una plataforma Web Full-Stack e inmersiva que fusiona un **E-Commerce de hardware hacking y software especializado** con una **Plataforma de Comunidad (Foro de Discusión)** 
dedicada al intercambio de conocimiento sobre ciberseguridad. 

Diseñada bajo una estética Cyberpunk/Terminal, la aplicación implementa un control de acceso basado en roles (`RBAC`) 
que transforma por completo la experiencia de usuario y las interfaces dependiendo de los privilegios del perfil autenticado (Usuario vs. Administrador).

---

## 🔐 Credenciales de Acceso Rápido (Demo)

Para evaluar las diferentes vistas y paneles sin necesidad de registrar un nuevo perfil, puedes usar las siguientes credenciales (o usar los botones de auto-completado en la interfaz):

| Rol de Acceso | Identidad (Email) | Contraseña (Token) | Alcance del Entorno |
| :--- | :--- | :--- | :--- |
| **`ROLE_ADMIN`** | `test@test.com` | `password123` | Control Total (Backoffice + Moderación + CRUD) |
| **`ROLE_USER`** | `generico@gmail.com` | `generica` | Vista de Cliente, Compra simulada y Foros |

---

## 🎯 Módulos Destacados de la Aplicación

### 🌐 1. El Hub de Comunidad (Foro de Discusión)
El núcleo social de **Hacktualidad** es un foro estructurado donde los usuarios interactúan y comparten información técnica:
*   **Segmentación por Temáticas:** Los hilos se agrupan en categorías críticas del sector: *Hardware Hacking, Software y Licencias, Red Team / Pentesting, Blue Team / Defensa y Análisis Forense*.
*   **Navegación Relacional:** Flujo de navegación síncrono donde los usuarios pueden seleccionar una temática, explorar los debates activos y sumergirse en busca de payloads, guías o solución de dudas.
*   **Arquitectura de Datos Limpia:** El backend gestiona las relaciones lógicas entre usuarios, hilos de discusión y temáticas asegurando la integridad referencial en todo momento.

### 🛒 2. Sistema de E-Commerce y Carrito Reactivo
*   **Catálogo Técnico:** Venta simulada de productos especializados (desde un *Flipper Zero* hasta licencias avanzadas como *Burp Suite Pro*).
*   **Carrito de Compras Inyectable:** Gestión en tiempo real de productos añadidos, modificación de stock en memoria de sesión, cálculo automático de importes y pasarela de simulación de checkout.
*   **Renderizado de Medios Optimizado:** Integración con Cloudinary para pintar las imágenes del catálogo al vuelo, evitando sobrecargar el servidor local con archivos estáticos pesados.

### 🛡️ 3. Panel de Administración (Backoffice Global)
Si inicias sesión como `ADMIN`, la aplicación desbloquea un centro de mando para gestionar todo el ecosistema mediante operaciones **CRUD completas**:
*   **Administración de Productos:** Control total de stock, descripción, precios y actualización de imágenes de los gadgets de la tienda.
*   **Estructura del Foro:** Capacidad de crear, editar o eliminar nuevas temáticas y moderar los hilos de discusión del foro para mantener el Hub limpio de spam.
*   **Gestión de Usuarios:** Monitorización de las identidades registradas en el sistema, edición de perfiles y escalabilidad de privilegios de acceso (USER / ADMIN).

---

## 🛠️ Arquitectura y Stack Técnico

### Frontend (User Interface)
*   **Framework:** Angular (Arquitectura modular basada en componentes e inyección de servicios reactivos).
*   **Manejo de Estado:** Flujos asíncronos controlados mediante operadores de `RxJS` (`BehaviorSubject`, `Observable`) para sincronizar el estado del carrito y del usuario de forma global.
*   **Estilo Visual:** CSS3 puro con animaciones personalizadas de efecto glitch, variables dinámicas aplicadas al árbol DOM y diseño adaptativo (*Responsive*).

### Backend (Core API) & Almacenamiento
*   **Framework Base:** Spring Boot con persistencia a través de Spring Data JPA / Hibernate.
*   **Seguridad:** Filtros de autenticación y autorización mediante interceptores y control de rutas mediante Angular Guards en el cliente.
*   **CDN / Multimedia Storage:** Integración nativa con la API de **Cloudinary** para la carga de imágenes, permitiendo que el cliente renderice URLs absolutas directamente y el backend procese los ficheros de forma descentralizada.

---

