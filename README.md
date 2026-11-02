# 📦 MultiTienda - App Mobile de Inventario con Escaneo de Códigos

> **Institución:** Duoc UC - Escuela de Informática y Telecomunicaciones | CITT  
> **Asignatura:** DSY1105 - Desarrollo de Aplicaciones Móviles  
> **Caso:** BexAI - Inventario Descentralizado Multi-Tienda y Bodegas  
> **Arquitectura:** MVVM (Model - View - ViewModel) + Repository Pattern  
> **Tecnologías:** Kotlin, Jetpack Compose, Room (SQLite), CameraX, Google ML Kit, Android Haptics/Vibrator API.

---

## 👥 Integrantes del Equipo

| Nombre Integrante | Rol Principal en el Proyecto | Rama Principal |
| :--- | :--- | :--- |
| **Patricio Céspedes** | UI, Componentes, Navegación y Animaciones | `feature/ui-navegacion` |
| **Daniel** | Persistencia Local (Room), Repository y Validaciones ViewModel | `feature/data-viewmodel` |
| **Angelo** | Recursos Nativos (Cámara + Háptico/Vibración) y Dashboard | `feature/camara-dashboard` |

---

## 🚀 Funcionalidades Implementadas

1. **Gestión de Inventario Multi-Tienda:**
   - Registro, actualización y visualización reactiva de productos con persistencia local mediante SQLite / Room.
   - Cálculo automático de existencias y control de stock mínimo.
2. **Formularios con Validación Desacoplada:**
   - Validaciones de campos numéricos, no vacíos y reglas de negocio centralizadas en el ViewModel (`StateFlow`).
   - Retroalimentación visual inmediata con bordes dinámicos, iconos de alerta y mensajes de error accesibles.
3. **Acceso Seguro a Múltiples Recursos Nativos (Cumplimiento IE 2.4.1):**
   - **Recurso Nativo 1 (Cámara):** Escáner de códigos de barra integrado con CameraX y Google ML Kit Barcode Scanning con solicitud de permisos en tiempo de ejecución.
   - **Recurso Nativo 2 (Motor de Respuesta Háptica / Vibración):** Confirmación física instantánea en el dispositivo al detectar códigos y confirmar transacciones.
4. **Dashboard y Trazabilidad:**
   - Resumen de existencias y panel visual con alertas de quiebre de stock para perfiles gerenciales y de bodega.
5. **Animaciones y Usabilidad:**
   - Transiciones visuales fluidas entre pantallas, indicadores de carga animados y alertas dinámicas.

---

## 📂 Estructura del Proyecto (MVVM + Modular)

```text
com.example.multitienda/
├── data/
│   ├── local/            # AppDatabase, Entidades Room, DAOs
│   └── repository/       # ProductoRepository (Single Source of Truth)
├── modelo/               # Modelos de datos y estados de UI (FormularioModel)
├── ui/
│   ├── components/       # Componentes reusables (CustomTextField, BotonAccion, FichaProducto)
│   ├── navigation/       # AppNavigation y definición de rutas
│   ├── screens/          # Pantallas principales (Splash, Login, Formulario, Dashboard, Scanner)
│   └── theme/            # Material Design 3 (Color, Typography, Theme)
├── util/                 # Helpers nativos (HapticFeedbackHelper, Permisos)
└── viewmodel/            # ViewModels (FormularioViewModel, DashboardViewModel)
```

---

## ⚙️ Pasos para Ejecutar el Proyecto

1. **Requisitos Previos:**
   - Android Studio Iguana / Jellyfish / Ladybug o superior.
   - JDK 17 o JDK 21 configurado en Android Studio.
   - Dispositivo físico con Android 8.0 (API 26)+ o emulador con soporte para cámara.
2. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/patricioisaias/app-multi-tienda.git
   ```
3. **Abrir y Sincronizar:**
   - Abrir el proyecto en Android Studio.
   - Esperar la sincronización de Gradle (`Sync Project with Gradle Files`).
4. **Ejecutar:**
   - Seleccionar el dispositivo o emulador y presionar `Run 'app'` (Shift + F10).
