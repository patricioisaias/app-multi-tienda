# MultiTienda — App Mobile de Inventario con Escaneo de Códigos

> **Institución:** Duoc UC — Escuela de Informática y Telecomunicaciones | CITT  
> **Asignatura:** `DSY1105` — Desarrollo de Aplicaciones Móviles  
> **Caso:** BexAI — Inventario Descentralizado Multi-Tienda y Bodegas  
> **Arquitectura:** MVVM (*Model - View - ViewModel*) + *Repository Pattern* + *Single-Activity*  
> **Tecnologías:** Kotlin `2.2.10`, Jetpack Compose (Material 3), Room 3 (SQLite), CameraX, Google ML Kit Barcode Scanning, Android Vibrator / VibratorManager API.

---

## Integrantes del Equipo

| Integrante | Responsabilidad Principal en el Proyecto |
| :--- | :--- |
| **Patricio Céspedes** | Autenticación, Perfiles de Usuario, Navegación Principal, Componentes Reutilizables y Operaciones de Terreno |
| **Daniel Aedo** | Persistencia Local (Room SQLite), Repositorios, Catálogo CRUD, Conteo Físico, Control de Diferencias e Historial |
| **Angelo Valenzuela** | Recursos Nativos (Cámara CameraX + ML Kit y Respuesta Háptica), Dashboard Gerencial, Alertas de Quiebre y Exportación de Reportes |

---

## Credenciales y Perfiles de Acceso

La aplicación cuenta con **3 perfiles de usuario diferenciados** según las responsabilidades definidas en el Caso BexAI. En `LoginScreen` puedes escribir las credenciales manualmente o tocar los **chips de selección rápida** (*Operador*, *Encargado Bodega*, *Gerencial*) para autocompletar en un clic:

| Perfil de Usuario | Correo Corporativo | Contraseña | Permisos y Herramientas en la Aplicación |
| :--- | :--- | :---: | :--- |
| **Operador de Terreno** | `test1@bexai.cl` / `operador@bexai.cl` | `1234` *(o `123456`)* | • Seleccionar tienda o bodega activa<br>• **Escanear Producto** con cámara (CameraX + ML Kit)<br>• **Registrar Movimiento** directo (Entrada, Salida, Ajuste)<br>• **Conteo Físico** y rectificación en pasillo |
| **Encargado de Bodega** | `test2@bexai.cl` / `bodega@bexai.cl` | `1234` *(o `123456`)* | • Seleccionar tienda o bodega activa<br>• **Agregar Producto** nuevo al catálogo<br>• **Consultar Stock Disponible** (Catálogo completo)<br>• **Control de Diferencias** (Sistema vs Físico)<br>• **Alertas de Reposición** y solicitud de abastecimiento |
| **Perfil Gerencial** | `test3@bexai.cl` / `gerente@bexai.cl` | `1234` *(o `admin123`)* | • **Tablero Analítico (Dashboard)** con KPIs en tiempo real<br>• **Auditoría de Existencias Globales**<br>• **Alertas de Quiebre de Stock**<br>• **Historial de Modificaciones** (Trazabilidad completa)<br>• **Administración de Tiendas** habilitadas<br>• **Exportar Reportes** (Excel `.xlsx` / `.csv`) |

---

## Funcionalidades e Indicadores de Logro Implementados

1. **Interfaz Estructurada y Navegación Unificada (IE 2.1.1):**
   - Arquitectura *Single-Activity* (`MainActivity`) con `NavHost` centralizado que adapta dinámicamente `MenuPrincipalScreen` según el perfil autenticado.
   - Manejo limpio del *Backstack* al iniciar y cerrar sesión (`popUpTo`).
2. **Formularios Validados con Lógica Desacoplada (IE 2.1.2 / IE 2.2.1):**
   - Componentes reutilizables (`CustomTextField` y `BotonAccion`) con retroalimentación visual inmediata (bordes de error, íconos de advertencia y mensajes por campo).
   - Validaciones centralizadas en los `ViewModels` mediante flujos reactivos `StateFlow`.
3. **Animaciones Funcionales (IE 2.2.2):**
   - Indicadores de carga animados con `AnimatedVisibility` (`fadeIn` / `fadeOut`) y `CircularProgressIndicator` en botones de acción y pantalla `SplashScreen`.
4. **Persistencia Local con Room y Patrón Repository (IE 2.3.1):**
   - Base de datos SQLite local (`AppDatabase`) con tablas relacionales `productos` y `movimientos`, consultas reactivas mediante `Flow` y semilla automática de datos sintéticos (`sembrarSiVacio()`).
5. **Acceso a Dos Recursos Nativos del Dispositivo (IE 2.4.1):**
   - **Recurso Nativo 1 — Cámara y Flash (`android.permission.CAMERA`):** Escaneo en tiempo real con **CameraX** y **Google ML Kit** en `ScannerScreen`, con solicitud segura de permisos en tiempo de ejecución y alternativa de entrada manual.
   - **Recurso Nativo 2 — Motor Háptico / Vibración (`android.permission.VIBRATE`):** Confirmación táctil mediante `HapticFeedbackHelper` compatible con `VibratorManager` (Android 12+) y `Vibrator`.

---

## Estructura del Proyecto (MVVM + Organización por Dominio)

```text
com.example.multitienda/
├── MainActivity.kt                        # Actividad única contenedora
├── data/
│   ├── local/                             # Capa de Persistencia Room (SQLite)
│   │   ├── AppDatabase.kt                 # Configuración Singleton @Database (versión 2)
│   │   ├── ProductoDAO.kt                 # @Dao consultas SQL de productos
│   │   └── MovimientoDAO.kt               # @Dao consultas SQL de movimientos
│   └── repository/                        # Repositorios (Single Source of Truth)
│       ├── ProductoRepository.kt          # Gestión de productos y semilla inicial
│       ├── MovimientoRepository.kt        # Gestión de historial de transacciones
│       └── TiendaRepository.kt            # Persistencia de tiendas habilitadas/suspendidas
├── modelo/                                # Entidades y Modelos de Estado
│   ├── Producto.kt                        # @Entity tabla "productos"
│   ├── Movimiento.kt                      # @Entity tabla "movimientos"
│   └── ProductoFormModel.kt               # Estado y errores de formulario de producto
├── viewmodel/                             # Capa ViewModel (Lógica de Negocio y StateFlow)
│   ├── LoginViewModel.kt                  # Autenticación y selección rápida de perfiles
│   ├── ProductoViewModel.kt               # CRUD de productos, búsqueda y conteo físico
│   ├── AjusteViewModel.kt                 # Cálculo y validación de entradas/salidas
│   ├── DashboardViewModel.kt              # Cálculo reactivo de KPIs gerenciales
│   ├── HistorialViewModel.kt              # Filtrado reactivo de movimientos
│   └── ReposicionViewModel.kt             # Solicitudes de reposición por quiebre
├── util/                                  # Utilidades de Hardware Nativo
│   └── HapticFeedbackHelper.kt            # Controlador de vibración física multi-API
└── ui/                                    # Capa View (Jetpack Compose)
    ├── components/                        # Componentes Visuales Reutilizables
    │   ├── BotonAccion.kt
    │   ├── CustomTextField.kt
    │   ├── ProductoCard.kt
    │   └── ProductoFormulario.kt
    ├── navigation/                        # Enrutamiento y Grafos de Navegación
    │   ├── AppRoutes.kt
    │   ├── AppNavigation.kt
    │   └── InventarioNavigation.kt
    ├── theme/                             # Sistema de Diseño Material 3
    │   ├── Color.kt
    │   ├── Theme.kt
    │   └── Type.kt
    └── screens/                           # Pantallas Organizadas por Dominio
        ├── auth/                          # Inicio y Autenticación
        │   ├── SplashScreen.kt
        │   └── LoginScreen.kt
        ├── home/                          # Menú Principal, Perfil y Sucursal (3 roles)
        │   ├── MenuPrincipalScreen.kt
        │   ├── PerfilUsuarioScreen.kt
        │   └── SeleccionTiendaScreen.kt
        ├── operador/                      # Operaciones de Ajuste en Terreno
        │   ├── FormularioAjusteScreen.kt
        │   └── ResumenMovimientoScreen.kt
        ├── scanner/                       # Lector de Códigos con Cámara y Respaldo
        │   ├── ScannerScreen.kt
        │   ├── ResultadoScannerScreen.kt
        │   └── EntradaManualCodigoScreen.kt
        ├── inventario/                    # Catálogo, Conteo, Diferencias y Auditoría
        │   ├── CatalogoProductosScreen.kt
        │   ├── DetalleProductoScreen.kt
        │   ├── CrearProductoScreen.kt
        │   ├── EditarProductoScreen.kt
        │   ├── ConfirmarEliminarScreen.kt
        │   ├── ConteoFisicoScreen.kt
        │   ├── DiferenciasStockScreen.kt
        │   └── HistorialMovimientosScreen.kt
        └── gerencial/                     # Supervisión, Alertas, Tiendas y Reportes
            ├── DashboardGerencialScreen.kt
            ├── AlertasQuiebreScreen.kt
            ├── DeclararReposicionScreen.kt
            ├── AdminTiendasScreen.kt
            └── ExportarReportesScreen.kt
```

---

## Pasos para Levantar el Proyecto (Android Studio Rabbit 2 y Rabbit 1 en Duoc UC)

### Opción A: En Android Studio Rabbit 2 (Configuración por Defecto)
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/patricioisaias/app-multi-tienda.git
   ```
2. Abrir la carpeta del proyecto en **Android Studio Rabbit 2**.
3. Ejecutar **Sync Project with Gradle Files** (`Ctrl + Shift + O`) y presionar **Run 'app'** (`Shift + F10`).

---

### Opción B: En PCs de Laboratorio Duoc UC (Android Studio Rabbit 1)
El proyecto viene configurado con **`agp = "9.5.0"`** (versión de Rabbit 2). Al abrirlo en un PC del Duoc con **Rabbit 1**, Android Studio mostrará un mensaje indicando la versión máxima de AGP que soporta ese equipo:
> `The project is using an incompatible version (AGP 9.5.0) of the Android Gradle plugin. Latest supported version is AGP X.X.X`

Sigue estos **3 pasos rápidos** para levantarlo en **Rabbit 1**:

1. **Ajustar la versión de `agp` en `gradle/libs.versions.toml`:**
   Abre `gradle/libs.versions.toml` y en la **línea 2** reemplaza `"9.5.0"` por la versión exacta que pida el mensaje de Rabbit 1 (por ejemplo `"9.4.0"` o `"9.1.0"`):
   ```toml
   [versions]
   agp = "9.4.0" # <-- Poner la versión "Latest supported version" que indique Rabbit 1
   ```
2. **Revisar `gradle/wrapper/gradle-wrapper.properties` (solo si pide bajar Gradle):**
   Si Rabbit 1 solicita cambiar `gradle-9.6.0-bin.zip` por una versión anterior en `distributionUrl`, **borra la línea 4 (`distributionSha256Sum=...`)** para evitar errores de verificación de hash SHA-256:
   ```properties
   # Borrar o comentar distributionSha256Sum si cambias la versión de gradle-9.6.0-bin.zip
   distributionUrl=https\://services.gradle.org/distributions/gradle-9.6.0-bin.zip
   ```
3. **Verificar `app/build.gradle.kts` y SDK 37:**
   - Si la versión de AGP de Rabbit 1 marca en rojo el bloque `optimization { ... }` dentro de `buildTypes.release` (líneas 25–28 de `app/build.gradle.kts`), cámbialo por:
     ```kotlin
     buildTypes {
         release {
             isMinifyEnabled = false
         }
     }
     ```
   - Si el PC del laboratorio no tiene descargado **Android SDK Platform 37**, ve a **Tools ➔ SDK Manager**, marca la casilla **Android API 37** y presiona **Apply** (se instala en la carpeta del usuario sin pedir permisos de administrador).
   - Presiona **Sync Project with Gradle Files** (ícono del elefante) y ejecuta la app.
