# PetConnect

Aplicación móvil nativa para Android que centraliza el proceso de adopción de mascotas, conectando refugios de animales con personas interesadas en adoptar.

---

## Descripción

PetConnect permite a refugios y organizaciones de rescate animal publicar mascotas disponibles con fotografías e información detallada, mientras que los usuarios pueden explorar el catálogo, consultar fichas completas, guardar favoritos y contactar al refugio directamente desde la aplicación.

---

## Tecnologías

| Categoría | Tecnología |
|---|---|
| Lenguaje | Kotlin 2.0.21 |
| Entorno de desarrollo | Android Studio |
| Arquitectura | MVVM (implementación parcial) |
| Autenticación | Firebase Authentication |
| Base de datos | Cloud Firestore |
| Almacenamiento | Firebase Storage |
| Carga de imágenes | Coil 2.6.0 |
| UI | Material Design 3, ConstraintLayout, CardView |
| Build | Android Gradle Plugin 8.13.2 |
| Control de versiones | GitHub |

**API mínima:** Android 9.0 (API 28) · **API objetivo:** 36

---

## Funcionalidades

### Implementadas
- Registro e inicio de sesión con Firebase Authentication
- Catálogo de mascotas disponibles con búsqueda por nombre, especie o raza
- Publicación de mascotas con subida de imagen desde la galería
- Vista de detalle con información completa y badge de disponibilidad
- Sistema de favoritos con persistencia en Firestore
- Perfil de usuario con lista de mascotas favoritas
- Contacto con el refugio vía WhatsApp

### Pendientes
- Diferenciación de roles (adoptante / refugio) mediante el atributo `esRefugio`
- Panel de administración para refugios (editar y eliminar publicaciones)
- Filtrado avanzado por especie, edad y tamaño
- Campos de salud de la mascota (vacunación, tamaño)
- Notificaciones push

---

## Estructura del proyecto

```
app/src/main/java/com/petconnect/app/
├── model/
│   ├── Mascota.kt
│   └── Usuario.kt
├── viewmodel/
│   ├── AuthViewModel.kt
│   └── MascotaViewModel.kt
├── adapter/
│   └── MascotaAdapter.kt
├── LoginActivity.kt
├── RegisterActivity.kt
├── MainActivity.kt
├── PublicarActivity.kt
├── DetalleActivity.kt
└── PerfilActivity.kt
```

---

## Configuración y ejecución

### Requisitos previos
- Android Studio Ladybug o superior
- JDK 11
- Cuenta de Firebase con proyecto configurado

### Pasos

1. Clona el repositorio:
   ```bash
   git clone https://github.com/dm-barr/PetConnect.git
   ```

2. Abre el proyecto en Android Studio.

3. Coloca tu archivo `google-services.json` en el directorio `app/`.

4. En la consola de Firebase, habilita:
   - **Authentication** → Proveedor: Correo electrónico/contraseña
   - **Cloud Firestore** → Modo de producción o prueba
   - **Firebase Storage**

5. Ejecuta la aplicación en un dispositivo o emulador con Android 9.0 o superior.

---

## Modelo de datos (Firestore)

```
mascotas/
  {mascotaId}
    nombre, especie, raza, edad, descripcion, imageUrl, refugioId

usuarios/
  {userId}
    nombre, email, esRefugio
    favoritos/
      {mascotaId}  ← referencia a mascota guardada
```

---

## Repositorio

- **Código fuente:** https://github.com/dm-barr/PetConnect.git
- **Wiki (Semana 1):** https://github.com/dm-barr/PetConnect/wiki/M1-Borrador-del-proyecto-de-la-aplicaci%C3%B3n-de-Android

---

## Autor

**Diana Michelle Barrantes Gallardo**
Ingeniería en Ciberseguridad — Saint Leo University
Curso: COM-437ES-AV01 Desarrollo de Aplicaciones Móviles · 2026
