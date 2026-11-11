# Guía Rápida de Defensa: "¿Dónde está cada cosa en el código?"
**Proyecto:** MultiTienda (Caso BexAI) — `DSY1105`  
**Objetivo:** Responder en menos de 5 segundos cuando el profesor pregunte *"¿Dónde está implementado X?"*, *"¿Cómo se llama Y?"* o *"Muéstrame en el código dónde hacen Z"*.

---

## Parte 1: Respuestas Directas a tus Preguntas Clave

### 1. ¿Dónde está la Base de Datos y cómo se llama?
* **Archivo donde se configura:** [`data/local/AppDatabase.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/data/local/AppDatabase.kt#L12-L35)
* **Nombre exacto de la Base de Datos:** **`"base_productos"`** (está en la **línea 26** de [`AppDatabase.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/data/local/AppDatabase.kt#L26) dentro de `Room.databaseBuilder`).
* **Versión actual de la BD:** `version = 2` (línea 12 de [`AppDatabase.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/data/local/AppDatabase.kt#L12)).
* **Nombre de las 2 tablas SQLite:**
  1. Tabla **`"productos"`** ➔ definida en [`modelo/Producto.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/modelo/Producto.kt#L6) (`@Entity(tableName = "productos")`).
  2. Tabla **`"movimientos"`** ➔ definida en [`modelo/Movimiento.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/modelo/Movimiento.kt#L6) (`@Entity(tableName = "movimientos")`).
* **¿Dónde se guarda físicamente dentro del celular/emulador?:**  
  En la ruta interna privada de Android: `/data/data/com.example.multitienda/databases/base_productos`.
* **¿Cómo mostrársela en vivo al profesor en Android Studio?:**  
  Con la app corriendo en el emulador, abre la pestaña inferior **App Inspection ➔ Database Inspector** y verás `"base_productos"` con las tablas `productos` y `movimientos` en tiempo real.

---

### 2. ¿Cómo se ocupa Room en el proyecto? (Los 4 pasos)
Si el profesor pregunta *"¿Cómo implementaron Room?"*, muéstrale estos 4 archivos en orden:

1. **Paso 1 — Las Entidades (`@Entity` en `modelo/`):**  
   En [`Producto.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/modelo/Producto.kt) y [`Movimiento.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/modelo/Movimiento.kt) usamos `@Entity` para que Room convierta la `data class` en una tabla SQL, y `@PrimaryKey(autoGenerate = true)` para que el `id` sea autoincremental.
2. **Paso 2 — Los DAOs (`@Dao` en `data/local/`):**  
   En [`ProductoDAO.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/data/local/ProductoDAO.kt) y [`MovimientoDAO.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/data/local/MovimientoDAO.kt) definimos las funciones SQL:
   * `@Insert`, `@Update`, `@Delete` con `suspend` (corren en segundo plano con corrutinas).
   * `@Query("SELECT ...")` que devuelven `Flow<List<Producto>>` (actualizan la pantalla automáticamente cuando cambia un dato).
3. **Paso 3 — El Motor `@Database` (`data/local/AppDatabase.kt`):**  
   En [`AppDatabase.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/data/local/AppDatabase.kt#L12-L35) unimos las dos tablas y aplicamos el patrón **Singleton** (`@Volatile` + `synchronized`) en `getDatabase(context)` para abrir una sola conexión en toda la app.
4. **Paso 4 — Los Repositorios y ViewModels (`data/repository/` y `viewmodel/`):**  
   [`ProductoRepository.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/data/repository/ProductoRepository.kt) recibe el `productoDAO()` (además carga los 6 productos iniciales con `sembrarSiVacio()`), y los ViewModels (como [`ProductoViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/ProductoViewModel.kt#L23-L28)) llaman al repositorio dentro de `viewModelScope.launch`.

---

### 3. ¿Dónde se hacen las Validaciones?
**Respuesta clave:** *"Por arquitectura MVVM, todas las validaciones de negocio se hacen en los **ViewModels**, nunca en las pantallas. Si hay un error, el ViewModel actualiza el `StateFlow` y el componente [`CustomTextField.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/CustomTextField.kt#L35-L58) se pinta rojo con `isError = true`"*.

Aquí está el mapa exacto de cada validación por archivo y línea:

| Qué se valida | Archivo exacto | Función y Líneas | Reglas que comprueba |
| :--- | :--- | :--- | :--- |
| **1. Login (Correo y Clave)** | [`viewmodel/LoginViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/LoginViewModel.kt#L72-L102) | `fun login()`<br>**Líneas 80–91** | • Que el correo no esté vacío (`email.isEmpty()`)<br>• Que contenga `"@"`<br>• Que el correo exista en el mapa `USUARIOS`<br>• Que la clave no esté vacía y esté en `PASSWORDS` (`1234`) |
| **2. Crear / Editar Producto** | [`viewmodel/ProductoViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/ProductoViewModel.kt#L117-L165) | `fun guardar()`<br>**Líneas 126–164** | • Código obligatorio (`codigo.isBlank()`)<br>• Nombre mínimo 3 letras (`nombre.length < 3`)<br>• Precio numérico `>= 0`<br>• Stock y Stock Mínimo enteros `>= 0`<br>• **Código único (Línea 156):** `obtenerPorCodigo(codigo)` evita códigos duplicados |
| **3. Conteo Físico** | [`viewmodel/ProductoViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/ProductoViewModel.kt#L213-L218) | `fun registrarConteo()`<br>**Líneas 214–218** | • Que el conteo ingresado sea un número entero mayor o igual a 0 (`nuevoStock == null \|\| nuevoStock < 0`) |
| **4. Ajuste / Salida de Stock** | [`viewmodel/AjusteViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/AjusteViewModel.kt#L37-L80) | `fun registrar()`<br>**Líneas 44–72** | • Cantidad `> 0` (Línea 44)<br>• Que el código de barras exista en Room (Línea 54)<br>• **Stock suficiente en Salidas (Línea 65):** `if (cantidad > producto.stock)` impide dejar stock negativo |
| **5. Declarar Reposición** | [`viewmodel/ReposicionViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/ReposicionViewModel.kt#L50-L56) | `fun confirmarReposicion()`<br>**Líneas 52–56** | • Que la cantidad a reponer sea un entero mayor a 0 (`cant == null \|\| cant <= 0`) |
| **6. Entrada Manual EAN** | [`ui/screens/scanner/EntradaManualCodigoScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/scanner/EntradaManualCodigoScreen.kt#L68-L76) | Botón `"Validar Código"`<br>**Líneas 69–75** | • Que el código escrito manualmente tenga al menos 8 dígitos (`codigo.length < 8`) |

---

### 4. ¿Dónde se ocupan los Recursos Nativos del Dispositivo?
El proyecto utiliza **2 recursos nativos de hardware**, cuyos permisos se declaran en [`app/src/main/AndroidManifest.xml`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/AndroidManifest.xml#L5-L12):

1. **Recurso Nativo 1: Cámara Trasera y Linterna/Flash (`android.permission.CAMERA`)**
   * **Dónde está implementado:** [`ui/screens/scanner/ScannerScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/scanner/ScannerScreen.kt)
   * **Líneas clave para mostrar:**
     * **Líneas 62–72:** Pide el permiso de cámara en tiempo de ejecución con `rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission())`.
     * **Líneas 123–198:** Levanta la cámara con **CameraX** (`ProcessCameraProvider`, `PreviewView` dentro de `AndroidView`) y analiza cada fotograma con **Google ML Kit** (`BarcodeScanning.getClient`).
     * **Línea 226:** Enciende y apaga físicamente el **Flash/Linterna** del teléfono con `camera?.cameraControl?.enableTorch(nuevoEstado)`.

2. **Recurso Nativo 2: Motor Háptico de Vibración (`android.permission.VIBRATE`)**
   * **Dónde está creado el controlador:** [`util/HapticFeedbackHelper.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/util/HapticFeedbackHelper.kt)
     * Soporta tanto Android 12+ (`VibratorManager`, líneas 20–21) como Android 7–11 (`Vibrator`, línea 24).
     * Tiene dos funciones: `vibrarExito(context)` (vibración corta de `80ms`) y `vibrarAlerta(context)` (doble pulso `0, 100, 80, 150` para errores o quiebres de stock).
   * **Dónde se llama/ocupa en la app:**
     1. En [`ScannerScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/scanner/ScannerScreen.kt#L170) (**Línea 170**): Vibra apenas la cámara detecta un código de barras.
     2. En [`ResultadoScannerScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/scanner/ResultadoScannerScreen.kt#L63-L69) (**Líneas 65 y 68**): Vibra con alerta si el producto escaneado tiene quiebre de stock (`stock <= stockMinimo`) o si no existe.
     3. En [`EntradaManualCodigoScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/scanner/EntradaManualCodigoScreen.kt#L72-L76) (**Líneas 72 y 75**): Vibra al validar o fallar un código manual.

---

### 5. ¿Dónde está la Animación del Splash (y las demás animaciones)?

1. **En la Pantalla Splash ([`ui/screens/auth/SplashScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/auth/SplashScreen.kt)):**
   * **Líneas 28–31 (`LaunchedEffect` + `delay`):** Ejecuta una corrutina al abrir la pantalla que espera `2000` milisegundos (2 segundos) y navega automáticamente al Login:
     ```kotlin
     LaunchedEffect(key1 = true) {
         delay(2000)
         onNavigateToLogin()
     }
     ```
   * **Líneas 63–67 (`CircularProgressIndicator`):** Dibuja la animación circular continua de carga debajo del logo (`Icons.Default.Warehouse`) mientras transcurren los 2 segundos.

2. **En el Botón de Acción Reutilizable ([`ui/components/BotonAccion.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/BotonAccion.kt#L43-L52)):**
   * **Línea 43 (`AnimatedVisibility`):** Es la animación funcional reactiva (Indicador IE 2.2.2). Usa `AnimatedVisibility(visible = isLoading, enter = fadeIn(), exit = fadeOut())` para que cuando el usuario presiona *"Ingresar"* o *"Guardar"*, aparezca suavemente un spinner giratorio y el texto cambie a `"Procesando..."`.

---

### 6. ¿Dónde se define y dónde se ocupa `MaterialTheme`?

1. **Dónde se DEFINE el tema (`ui/theme/`):**
   * **[`ui/theme/Color.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/theme/Color.kt):** Define los colores base (`Purple40`, `Purple80`, `Pink40`, etc.).
   * **[`ui/theme/Type.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/theme/Type.kt):** Define la tipografía (`Typography`).
   * **[`ui/theme/Theme.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/theme/Theme.kt#L36-L56):** En las **líneas 36–56** está la función `@Composable fun MultiTiendaTheme(...)`, que selecciona el esquema claro u oscuro y en la **línea 52** invoca a `MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)`.

2. **Dónde se INYECTA a toda la aplicación (`MainActivity.kt`):**
   * En [`MainActivity.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/MainActivity.kt#L20-L26) (**Línea 20**): `MultiTiendaTheme { ... }` envuelve a `AppNavigation()`, haciendo que las 23 pantallas hereden automáticamente los colores y tipografías.

3. **Dónde se OCUPA en los componentes y pantallas:**
   * En todas las vistas invocando dos propiedades de `MaterialTheme`:
     * **`MaterialTheme.colorScheme`:** para pintar colores coherentes (ej. `MaterialTheme.colorScheme.primary` en [`BotonAccion.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/BotonAccion.kt#L38), `MaterialTheme.colorScheme.primaryContainer` en [`SplashScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/auth/SplashScreen.kt#L36), `MaterialTheme.colorScheme.error` para alertas y quiebres de stock en [`ProductoCard.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/ProductoCard.kt#L46)).
     * **`MaterialTheme.typography`:** para tamaños y estilos de letra estandarizados (ej. `MaterialTheme.typography.headlineMedium` en títulos, `MaterialTheme.typography.bodyMedium` en textos y `MaterialTheme.typography.labelLarge` en botones).

---

## Parte 2: 25 Preguntas Adicionales de Ronda Técnica ("Muéstrame dónde está...")

### Bloque A: Base de Datos Room y Datos Iniciales
1. **¿Dónde están los 6 productos que aparecen cargados cuando recién instalas la app?**
   * **Respuesta:** En [`data/repository/ProductoRepository.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/data/repository/ProductoRepository.kt#L36-L92) (**Líneas 36–92**), dentro de la lista `productosDeEjemplo`. Se insertan mediante la función `sembrarSiVacio()` (línea 30) solo cuando `dao.contarProductos() == 0`.
2. **¿En qué momento se llama a `sembrarSiVacio()`?**
   * **Respuesta:** En el bloque `init { ... }` de [`ProductoViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/ProductoViewModel.kt#L62-L66) (línea 64), [`AjusteViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/AjusteViewModel.kt#L31-L35) (línea 33) y [`DashboardViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/DashboardViewModel.kt#L30-L34) (línea 32).
3. **¿Dónde está la consulta SQL que detecta los productos con quiebre o bajo stock?**
   * **Respuesta:** En [`data/local/ProductoDAO.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/data/local/ProductoDAO.kt#L29-L30) (**Línea 29**):  
     `@Query("SELECT * FROM productos WHERE stock <= stockMinimo ORDER BY stock ASC")`.
4. **¿Dónde está la consulta SQL que actualiza el stock de un producto sin sobrescribir todo el objeto?**
   * **Respuesta:** En [`data/local/ProductoDAO.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/data/local/ProductoDAO.kt#L38-L39) (**Línea 38**):  
     `@Query("UPDATE productos SET stock = :nuevoStock WHERE id = :id")`.
5. **¿Cómo se guarda la fecha y hora exacta de cada movimiento en Room?**
   * **Respuesta:** En [`modelo/Movimiento.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/modelo/Movimiento.kt#L14) (**Línea 14**), el campo `val fechaMillis: Long = System.currentTimeMillis()` guarda el timestamp en milisegundos, y en [`HistorialMovimientosScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/inventario/HistorialMovimientosScreen.kt) se formatea a `dd/MM/yyyy HH:mm` con `SimpleDateFormat`.

---

### Bloque B: Arquitectura MVVM, ViewModels y Estado Reactivo
6. **¿Dónde está implementado el buscador en tiempo real del catálogo?**
   * **Respuesta:** En [`viewmodel/ProductoViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/ProductoViewModel.kt#L33-L41) (**Líneas 33–41**). Usa el operador `combine(productoRepository.productos, _busqueda)` para filtrar por nombre instantáneamente a medida que el usuario escribe.
7. **¿Dónde se calculan los KPIs del Dashboard Gerencial (Total SKUs, Unidades Totales, Alertas y % de Quiebre)?**
   * **Respuesta:** En [`viewmodel/DashboardViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/DashboardViewModel.kt#L36-L56) (**Líneas 36–56**). Transforma el `Flow` de productos de Room con `.map { lista -> ... }` calculando `lista.size`, `lista.sumOf { it.stock }`, `lista.count { it.stock <= it.stockMinimo }` y el porcentaje de quiebre.
8. **¿Cómo observa una pantalla de Compose los datos de un ViewModel?**
   * **Respuesta:** Usando `.collectAsState()`. Por ejemplo, en [`LoginScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/auth/LoginScreen.kt#L50-L51) (**Líneas 50–51**):  
     `val state by viewModel.state.collectAsState()`.
9. **¿Por qué en los ViewModels hay variables con guion bajo (`_state`) y otras sin guion bajo (`state`)?**
   * **Respuesta:** Es encapsulamiento (Backing Property). `_state` es `private val _state = MutableStateFlow(...)` para que solo el ViewModel pueda modificarlo, mientras que `val state: StateFlow = _state.asStateFlow()` es público y de solo lectura para que la pantalla nunca altere el estado directamente.
10. **¿Dónde se registra automáticamente un movimiento en el historial cuando alguien crea o edita un producto?**
    * **Respuesta:** En [`viewmodel/ProductoViewModel.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/viewmodel/ProductoViewModel.kt#L178-L201) (**Líneas 178–201**). Al editar un producto calcula `val diferencia = producto.stock - anterior.stock` y si cambió el stock llama a `registrarMovimiento(...)`.

---

### Bloque C: Navegación, Perfiles y Cierre de Sesión
11. **¿Dónde están definidas todas las rutas de navegación de la app?**
    * **Respuesta:** En [`ui/navigation/AppRoutes.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/navigation/AppRoutes.kt) (para el flujo general) y en [`ui/navigation/InventarioNavigation.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/navigation/InventarioNavigation.kt) (para el subgrafo del módulo de catálogo, conteo e historial).
12. **¿Dónde está el `NavHost` principal y cuál es la pantalla de inicio (`startDestination`)?**
    * **Respuesta:** En [`ui/navigation/AppNavigation.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/navigation/AppNavigation.kt#L53-L56) (**Línea 55**), con `startDestination = AppRoutes.SPLASH`.
13. **¿Dónde se guarda qué tienda o bodega está activa actualmente?**
    * **Respuesta:** En [`ui/navigation/AppNavigation.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/navigation/AppNavigation.kt#L45) (**Línea 45**):  
      `var tiendaActiva by rememberSaveable { mutableStateOf("Bodega Pudahuel") }`. Al usar `rememberSaveable`, se mantiene para todas las pantallas y sobrevive si se rota el teléfono.
14. **¿Dónde se filtra qué botones ve cada uno de los 3 perfiles (Operador, Bodega, Gerencial)?**
    * **Respuesta:** En [`ui/screens/home/MenuPrincipalScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/home/MenuPrincipalScreen.kt#L54-L60) (**Líneas 54–60** definen `esGerencial`, `esBodega`, `esOperador`, y en las **líneas 138–260** los bloques `if (esOperador)`, `else if (esBodega)`, `else if (esGerencial)` dibujan únicamente las tarjetas de ese rol).
15. **¿Dónde está programado el botón de Cerrar Sesión y cómo evita que el usuario vuelva atrás con la flecha del celular?**
    * **Respuesta:** El botón visual está en [`ui/screens/home/PerfilUsuarioScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/home/PerfilUsuarioScreen.kt#L139-L152) (**Líneas 139–152**), y la lógica de limpieza de historial está en [`ui/navigation/AppNavigation.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/navigation/AppNavigation.kt#L279-L284) (**Líneas 279–284**), donde ejecuta:
      ```kotlin
      navController.navigate(AppRoutes.LOGIN) {
          popUpTo(0) { inclusive = true }
      }
      ```
16. **¿Por qué usamos `Uri.encode()` y `Uri.decode()` en `AppRoutes.kt` y `AppNavigation.kt`?**
    * **Respuesta:** Porque en Navigation Compose los parámetros viajan como segmentos de una URL (por ejemplo, `"Ajuste / Conteo"` o `"Patricio Céspedes"`). Si un texto lleva una barra `/` o espacios sin codificar, rompe la ruta de navegación.

---

### Bloque D: Componentes Reutilizables e Interfaz (Compose)
17. **¿Cuáles son los 4 componentes reutilizables del proyecto y dónde están?**
    * **Respuesta:** Están en la carpeta `ui/components/`:
      1. [`BotonAccion.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/BotonAccion.kt) (Botón con animación de carga).
      2. [`CustomTextField.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/CustomTextField.kt) (Input con soporte de errores y teclado configurable).
      3. [`ProductoCard.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/ProductoCard.kt) (Tarjeta de ítem en el catálogo con badge de `"BAJO STOCK"`).
      4. [`ProductoFormulario.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/ProductoFormulario.kt) (Formulario compartido por Crear y Editar Producto).
18. **¿Dónde se pinta la etiqueta roja `"BAJO STOCK"` en las tarjetas de productos?**
    * **Respuesta:** En [`ui/components/ProductoCard.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/ProductoCard.kt#L40-L79) (**Línea 40** calcula `val bajoStock = producto.stock <= producto.stockMinimo`, y las **líneas 63–78** dibujan el contenedor `errorContainer` con el texto `"BAJO STOCK"`).
19. **¿Cómo hacen para que el campo de Precio o Stock solo abra el teclado numérico en el teléfono?**
    * **Respuesta:** Pasando el parámetro `keyboardType = KeyboardType.Number` o `KeyboardType.Decimal` a [`CustomTextField.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/CustomTextField.kt#L28-L55) (como se ve en [`ProductoFormulario.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/ProductoFormulario.kt#L69-L90), líneas 69, 80 y 91).
20. **¿Cómo se oculta la contraseña con puntos (`••••`) en el Login?**
    * **Respuesta:** En [`CustomTextField.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/CustomTextField.kt#L52-L53) (**Línea 53**), cuando `isPassword = true` aplica `PasswordVisualTransformation()`.

---

### Bloque E: Configuración Gradle y Librerías
21. **¿Dónde se declaran las librerías y versiones del proyecto?**
    * **Respuesta:** Usando *Version Catalogs*: las versiones están en [`gradle/libs.versions.toml`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/gradle/libs.versions.toml) y se aplican en [`app/build.gradle.kts`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/build.gradle.kts#L40-L66).
22. **¿Para qué sirve el plugin `ksp` en `app/build.gradle.kts` (línea 4 y línea 52)?**
    * **Respuesta:** *Kotlin Symbol Processing* lee las anotaciones `@Entity`, `@Dao` y `@Database` de Room al compilar y genera automáticamente el código Kotlin/SQLite que ejecuta las consultas.
23. **¿Qué pasa si probamos el escáner en un emulador de PC o si la etiqueta física está rota y la cámara no lee el código?**
    * **Respuesta:** En [`ScannerScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/scanner/ScannerScreen.kt#L280-L315) incluimos un botón de **"Ingresar código manualmente"** que lleva a [`EntradaManualCodigoScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/scanner/EntradaManualCodigoScreen.kt), además de botones de simulación rápida de lectura para demostrar el flujo completo incluso en un PC de laboratorio sin cámara.
24. **¿Qué pasa cuando escaneas un código de barras que SÍ existe vs. uno que NO existe en Room?**
    * **Respuesta:** En [`ui/screens/scanner/ResultadoScannerScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/scanner/ResultadoScannerScreen.kt#L58-L70), se busca el código con `productoRepository.obtenerPorCodigo(codigo)`. Si existe, muestra su ficha completa, su stock actual y permite ir directo a **Ajustar Stock** o **Conteo Físico**. Si devuelve `null` (no existe), vibra en modo alerta y muestra la tarjeta *"Producto no registrado en el catálogo"*.
25. **¿Dónde está la pantalla que compara el Stock del Sistema contra el Stock Mínimo esperado para el Encargado de Bodega?**
    * **Respuesta:** En [`ui/screens/inventario/DiferenciasStockScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/inventario/DiferenciasStockScreen.kt), que lista todos los productos resaltando aquellos con descalce (`producto.stock <= producto.stockMinimo`) y tiene un botón directo para abrir [`ConteoFisicoScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/inventario/ConteoFisicoScreen.kt) y rectificar la cantidad.
