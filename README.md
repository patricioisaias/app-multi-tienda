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
| **Patricio Céspedes** | UI, Componentes, Navegación y Operador | `feature/ui-navegacion` |
| **Daniel** | Persistencia Local (Room), Repository y Validaciones ViewModel | `feature/data-viewmodel` |
| **Angelo** | Recursos Nativos (Cámara + Háptico/Vibración) y Dashboard | `feature/camara-dashboard` |

---

## 🔐 Credenciales y Perfiles de Acceso (Modo Evaluación)

Para facilitar la revisión docente y las pruebas en laboratorio, la aplicación incluye **3 cuentas sintéticas de prueba** preconfiguradas con diferentes roles según el Caso BexAI:

| Perfil de Usuario | Usuario / Correo | Contraseña | Enfoque y Permisos en el Sistema |
| :--- | :--- | :--- | :--- |
| **👤 Operador de Terreno** | `operador@bexai.cl` | `123456` | Selecciona sucursal activa, escanea códigos de barra con la cámara física, ingresa conteos y registra movimientos directos (Entradas/Salidas/Mermas). |
| **📦 Encargado de Bodega** | `bodega@bexai.cl` | `123456` | Consulta el catálogo completo en Room, detecta descalces entre existencias teóricas y conteo físico, y declara solicitudes de reposición urgente. |
| **📊 Perfil Gerencial** | `gerente@bexai.cl` | `admin123` | Monitorea tableros analíticos (Dashboard con KPIs), audita existencias consolidadas, supervisa alertas de quiebre de stock y exporta reportes. |

> 💡 **Autocompletado Rápido en la App:** En la pantalla de `LoginScreen`, al presionar los botones selectores (*FilterChips*) de **"Operador"**, **"Encargado Bodega"** o **"Gerencial"**, los campos de usuario y contraseña se rellenan automáticamente para ingresar en un solo clic.

---

## 📱 Flujo de Operación de la Aplicación

1. **Pantalla de Bienvenida (`SplashScreen`):**
   * Muestra el logo institucional de BexAI durante 2 segundos y redirige automáticamente al Login.
2. **Autenticación Multi-Perfil (`LoginScreen`):**
   * Valida correo/contraseña y asocia la sesión al rol correspondiente.
3. **Selección de Sucursal Activa (`SeleccionTiendaScreen`):**
   * El usuario selecciona la tienda o bodega en la que está operando (*Bodega Pudahuel Central*, *Sucursal Santiago Centro*, *Tienda Providencia*, *Bodega Logística Sur*).
4. **Menú de Operaciones (`MenuOperadorScreen`):**
   * Accesos táctiles rápidos para escanear código con la cámara, registrar ajustes de stock manuales, consultar el catálogo en Room, ver el Dashboard de alertas o gestionar el perfil.
5. **Formulario de Ajuste y Comprobante (`FormularioAjusteScreen` & `ResumenMovimientoScreen`):**
   * Registro con validación en tiempo real de entradas o salidas y generación de comprobante con marca de tiempo UTC.

---

## 🚀 Funcionalidades y Recursos Nativos Implementados

1. **Gestión de Inventario Multi-Tienda:**
   - Registro, actualización y visualización reactiva de productos con persistencia local mediante SQLite / Room.
   - Control de existencias y umbral de stock mínimo para evitar quiebres.
2. **Formularios con Validación Desacoplada (IE 2.2.1):**
   - Validaciones centralizadas en el ViewModel (`StateFlow`), asegurando que la lógica no esté acoplada a la vista Compose.
   - Retroalimentación visual inmediata con bordes dinámicos, iconos de alerta y mensajes de error accesibles.
3. **Acceso a DOS Recursos Nativos del Dispositivo (IE 2.4.1 Obligatorio):**
   - **Recurso Nativo 1 (Cámara):** Escáner de códigos de barra integrado con CameraX y Google ML Kit Barcode Scanning con gestión segura de permisos en tiempo de ejecución.
   - **Recurso Nativo 2 (Motor de Respuesta Háptica / Vibrador del Hardware):** Emisión de vibración física en el teléfono (`Vibrator` / `VibrationEffect`) al escanear exitosamente y al confirmar transacciones de inventario.
4. **Dashboard y Trazabilidad:**
   - Resumen de existencias y panel visual con alertas de quiebre de stock para perfiles gerenciales y de bodega.
5. **Animaciones Funcionales (IE 2.2.2):**
   - Transiciones fluidas entre pantallas y botones con indicador animado de procesamiento (`CircularProgressIndicator`).

---

## 📂 Estructura del Proyecto (MVVM + Modular)

```text
com.example.multitienda/
├── data/
│   ├── local/            # AppDatabase, Entidades Room, DAOs
│   └── repository/       # ProductoRepository (Single Source of Truth)
├── modelo/               # Modelos de datos y estados de UI (FormularioModel, Producto)
├── ui/
│   ├── components/       # Componentes reusables (CustomTextField, BotonAccion, FichaProducto)
│   ├── navigation/       # AppNavigation y definición de rutas del NavHost
│   ├── screens/
│   │   ├── operador/     # Splash, Login, SeleccionTienda, MenuOperador, FormularioAjuste, Resumen, Perfil
│   │   ├── inventario/   # CatalogoProductos, DetalleProducto, CrearProducto, EditarProducto (Daniel)
│   │   ├── scanner/      # ScannerScreen, ResultadoScanner, EntradaManual (Angelo)
│   │   └── gerencial/    # DashboardGerencial, AlertasQuiebre, DeclararReposicion (Angelo)
│   └── theme/            # Material Design 3 (Color, Typography, Theme)
├── util/                 # Helpers nativos (HapticFeedbackHelper, Permisos)
└── viewmodel/            # ViewModels desacoplados (FormularioViewModel, DashboardViewModel)
```

---

## ⚙️ Pasos para Ejecutar el Proyecto

1. **Requisitos Previos:**
   - Android Studio (Ladybug, Jellyfish, Iguana o superior).
   - JDK 17 o JDK 21 configurado en Android Studio (`Settings -> Build Tools -> Gradle -> Gradle JDK = jbr-17/jbr-21`).
   - Dispositivo físico con Android 8.0+ o emulador con cámara configurada (`Webcam0` o `VirtualScene`).
2. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/patricioisaias/app-multi-tienda.git
   ```
3. **Abrir y Sincronizar:**
   - Abrir el proyecto en Android Studio.
   - Presionar el botón de sincronización de Gradle (`Sync Project with Gradle Files`).
4. **Ejecutar:**
   - Seleccionar el dispositivo o emulador y presionar **Run 'app'** (`Shift + F10`).
