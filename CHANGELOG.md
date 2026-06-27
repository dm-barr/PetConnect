# Changelog

Todos los cambios notables de este proyecto se documentan en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es/1.0.0/).

---

## [Unreleased]

### Pendiente
- Diferenciación de roles adoptante / refugio usando el atributo `esRefugio`
- Panel de administración para refugios (editar y eliminar publicaciones propias)
- Filtrado avanzado por especie, edad y tamaño
- Campos de salud de la mascota (vacunación, tamaño) en el modelo `Mascota`
- Notificaciones push para nuevas mascotas disponibles
- Documentación técnica del proyecto (arquitectura, guía de contribución)

---

## [0.2.0] — Semana 2 · 2026

### Añadido
- `PublicarActivity`: formulario de registro de mascota con subida de imagen a Firebase Storage
- `DetalleActivity`: pantalla de detalle con imagen en alta resolución cargada mediante Coil
- `PerfilActivity`: pantalla de perfil con datos del usuario obtenidos desde Firestore
- Sistema de favoritos: marcado/desmarcado persistido en la sub-colección `usuarios/{userId}/favoritos`
- Lista de mascotas favoritas en la pantalla de perfil (RecyclerView)
- Badge visual de estado "Disponible" en la pantalla de detalle
- Botón de contacto en detalle con integración a WhatsApp (número estático)
- `MascotaViewModel`: gestión de lógica de mascotas separada de la vista
- `MascotaAdapter`: adaptador RecyclerView para las tarjetas de mascotas
- Modelos de datos `Mascota` y `Usuario` como data classes de Kotlin
- Paleta de colores y sistema de estilos definidos en `colors.xml` y `themes.xml`
- Atributo `esRefugio` en el modelo `Usuario` como base para futura diferenciación de roles

### Modificado
- `MainActivity`: integración de búsqueda en tiempo real por nombre, especie o raza

---

## [0.1.0] — Semana 1 · 2026

### Añadido
- Estructura inicial del proyecto Android con Android Gradle Plugin 8.13.2 y Kotlin 2.0.21
- Configuración de Firebase (Authentication, Cloud Firestore, Storage) mediante `google-services.json`
- `LoginActivity`: pantalla de inicio de sesión con validación de formulario y Firebase Auth
- `RegisterActivity`: pantalla de registro con nombre, correo y contraseña (mínimo 6 caracteres)
- `MainActivity`: pantalla principal con RecyclerView de mascotas disponibles
- `AuthViewModel`: gestión del flujo de autenticación separado de la vista
- Navegación entre pantallas mediante Intents explícitos de Android
- Colección `mascotas` y `usuarios` en Cloud Firestore
- Configuración de dependencias: Coil 2.6.0, Material Design 1.13.0, ConstraintLayout 2.2.1, CardView
- `AndroidManifest.xml` con `LoginActivity` como punto de entrada (LAUNCHER)
- Configuración de versión mínima API 28 y objetivo API 36

---

[Unreleased]: https://github.com/dm-barr/PetConnect/compare/v0.2.0...HEAD
[0.2.0]: https://github.com/dm-barr/PetConnect/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/dm-barr/PetConnect/releases/tag/v0.1.0
