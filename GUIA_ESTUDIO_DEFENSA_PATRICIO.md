# 🎓 Guía Maestra de Estudio y Defensa Individual - Patricio Céspedes

> **Asignatura:** DSY1105 - Desarrollo de Aplicaciones Móviles (Duoc UC)  
> **Evaluación:** Parcial 3 - Presentación y Defensa Técnica Individual (Ponderación: **32%**)  
> **Tiempo Asignado:** 15 minutos cronometrados frente al docente.  
> **Rol de Patricio:** Líder de UI, Experiencia de Usuario (UX), Componentes Reutilizables y Navegación Central.  
> **Rama Oficial:** `feature/ui-navegacion`

---

## 📌 Índice de Preparación Rápida
1. [Estructura de la Defensa (Los 9 Pasos Obligatorios)](#1-estructura-de-la-defensa-los-9-pasos-obligatorios)
2. [Paso a Paso: Guión Técnico de Qué Decir y Qué Mostrar](#2-paso-a-paso-guión-técnico-de-qué-decir-y-qué-mostrar)
3. [Componentes Clave Creados por Patricio (Explicación de Código)](#3-componentes-clave-creados-por-patricio)
4. [Las 7 Pantallas del Flujo de Operador](#4-las-7-pantallas-del-flujo-de-operador)
5. [Enrutamiento y Navegación (`AppNavigation.kt`)](#5-enrutamiento-y-navegación)
6. [Machete para el Código en Vivo (Lo que te pedirá modificar el profesor)](#6-machete-para-el-código-en-vivo)
7. [Preguntas Teóricas Trampa y Respuestas Perfectas](#7-preguntas-teóricas-trampa-y-respuestas-perfectas)

---

## ⏱️ 1. Estructura de la Defensa (Los 9 Pasos Obligatorios)

La pauta oficial de evaluación exige que sigas este orden exacto durante tus 15 minutos:

```text
[00:00 - 02:00] 1. Configuración del entorno y ejecución de la App.
[02:00 - 04:00] 2. Estructura del proyecto y justificación arquitectónica (MVVM).
[04:00 - 06:00] 3. Diseño visual, usabilidad y accesibilidad (Material Design 3).
[06:00 - 08:00] 4. Formularios, validación visual y retroalimentación de errores.
[08:00 - 09:30] 5. Gestión del estado reactivo (State / StateFlow / remember).
[09:30 - 10:30] 6. Animaciones funcionales implementadas.
[10:30 - 11:30] 7. Persistencia local Room y modularidad del código.
[11:30 - 12:30] 8. Recursos nativos del dispositivo (Cámara + Motor Háptico).
[12:30 - 15:00] 9. MODIFICACIÓN DE CÓDIGO EN TIEMPO REAL (En vivo frente al profesor).
```

---

## 🎙️ 2. Paso a Paso: Guión Técnico de Qué Decir y Qué Mostrar

### 1️⃣ Configuración del Entorno (Minuto 0 a 2)
* **Qué hacer en pantalla:** Abre Android Studio, muestra el emulador o teléfono físico conectado y presiona **Run** (`Shift + F10`).
* **Qué decir textualmente:**
  > *"Profesor, el proyecto está configurado sobre Android Studio con Gradle 9.6 y el Android Gradle Plugin 9.5. Como JDK de compilación estamos utilizando el entorno oficial embebido **jbr-17/jbr-21** para garantizar compatibilidad con Jetpack Compose y las librerías modernas de AndroidX. La app corre de forma nativa a partir de Android 8.0 (API 24) hasta API 37."*

---

### 2️⃣ Estructura del Proyecto y Arquitectura MVVM (Minuto 2 a 4)
* **Qué hacer en pantalla:** Abre el panel de carpetas del proyecto en Android Studio y muestra los paquetes.
* **Qué decir textualmente:**
  > *"Diseñamos el proyecto siguiendo el patrón arquitectónico **MVVM (Model - View - ViewModel)** combinado con el **Repository Pattern** para mantener una separación estricta de responsabilidades:"*
  * **`ui/screens/`**: Vistas Composable independientes divididas por perfil (`operador`, `inventario`, `scanner`, `gerencial`).
  * **`ui/components/`**: Componentes reutilizables modulares (`CustomTextField`, `BotonAccion`, `FichaProducto`).
  * **`ui/navigation/`**: Enrutador desacoplado (`AppNavigation.kt`) que centraliza la navegación con Compose Navigation.
  * **`modelo/`**: Clases de datos inmutables y estados de UI (`FormularioModel`, `Producto`).
  * **`data/local/` y `data/repository/`**: Persistencia local SQLite con Room (`AppDatabase`, `ProductoDAO`, `ProductoRepository`).
  * **`viewmodel/`**: Lógica de negocio y validaciones desacopladas que emiten estado reactivo mediante `StateFlow`.
  > *"¿Por qué MVVM? Porque la interfaz de usuario en Compose es puramente declarativa; no debe contener lógica de base de datos ni validaciones pesadas, permitiendo que la app sea escalable, testeable y mantenible."*

---

### 3️⃣ Diseño Visual y Usabilidad en Terreno (Minuto 4 a 6)
* **Qué hacer en pantalla:** Muestra la app corriendo en el emulador, pasando por `SplashScreen`, `LoginScreen` y `SeleccionTiendaScreen`.
* **Qué decir textualmente:**
  > *"El caso BexAI trata de operarios que trabajan en bodegas y tiendas con iluminación variable y ritmo rápido. Por eso aplicamos principios de alta usabilidad y Material Design 3:"*
  * **Jerarquía visual:** Títulos claros, espaciados consistentes con `Modifier.padding` y tarjetas (`Card` / `ElevatedCard`) con contraste marcado.
  * **Zonas táctiles amplias:** Botones de al menos 48-50dp de alto (`BotonAccion`) para que el operario pueda pulsarlos con guantes o en movimiento.
  * **Diseño adaptativo:** Scroll vertical (`verticalScroll(rememberScrollState())`) en todas las pantallas para que ningún campo quede oculto por el teclado virtual (`windowSoftInputMode="adjustResize"`).

---

### 4️⃣ Formularios y Validación Visual (Minuto 6 a 8)
* **Qué hacer en pantalla:** Entra a `FormularioAjusteScreen` o `LoginScreen`. Deja los campos vacíos y pulsa el botón para que salten los errores en rojo.
* **Qué decir textualmente:**
  > *"Para los formularios creamos un componente exclusivo llamado `CustomTextField`. Este componente recibe un booleano `isError` y un mensaje `errorMessage`:"*
  * Si el dato es inválido, el campo resalta el borde en color de error del tema (`MaterialTheme.colorScheme.error`).
  * Muestra automáticamente un icono de advertencia (`Icons.Default.Warning`) a la derecha.
  * Despliega el texto descriptivo del error debajo del campo en tipografía pequeña.
  * Las validaciones se ejecutan en tiempo real o al presionar guardar, garantizando que jamás se guarden registros corruptos o vacíos.

---

### 5️⃣ Gestión de Estado Reactivo (Minuto 8 a 9:30)
* **Qué hacer en pantalla:** Muestra en el código de `LoginScreen.kt` o `FormularioAjusteScreen.kt` cómo se declaran las variables de estado.
* **Qué decir textualmente:**
  > *"En Compose la interfaz no se modifica manualmente; se 'recompone' cuando cambia un estado. Usamos dos mecanismos según el alcance:"*
  1. **Estado local de UI:** `var email by remember { mutableStateOf("...") }`. `remember` asegura que el valor sobreviva a las recomposiciones mientras el Composable esté activo en pantalla.
  2. **Estado global / reactivo:** `StateFlow` en el ViewModel, recolectado en Compose mediante `collectAsState()`, permitiendo que si Daniel inserta un producto en Room, la lista del catálogo se refresque sola sin llamar a ningún 'notifyDataSetChanged()'.

---

### 6️⃣ Animaciones Funcionales (Minuto 9:30 a 10:30)
* **Qué hacer en pantalla:** Pulsa el botón de confirmar en el formulario o en el login para que se aprecie la animación del botón.
* **Qué decir textualmente:**
  > *"Cumpliendo con el indicador IE 2.2.2 de la rúbrica, implementamos animaciones que aportan valor real a la experiencia de usuario:"*
  1. **Animación en Botón (`BotonAccion`):** Usa `AnimatedVisibility` con transiciones `fadeIn()` y `fadeOut()`. Al presionar el botón, el texto desaparece suavemente y entra un `CircularProgressIndicator` giratorio, entregando retroalimentación inmediata de que la app está procesando la operación.
  2. **Transición inicial de bienvenida (`SplashScreen`):** Un retardo temporal no bloqueante con corrutinas (`delay(2000)`) que da paso fluido al inicio de sesión.

---

### 7️⃣ Persistencia Local SQLite / Room (Minuto 10:30 a 11:30)
* **Qué hacer en pantalla:** Abre `AppDatabase.kt` y `ProductoDAO.kt`, y abre la pantalla de Catálogo (`ProductoView`) dentro de la app.
* **Qué decir textualmente:**
  > *"La persistencia se implementó con Room sobre SQLite bajo el patrón Repository. Daniel configuró la entidad `Producto` con su clave primaria, `codigoBarras` y `stockMinimo`. El DAO expone consultas que retornan `Flow<List<Producto>>`, lo que permite que la capa de datos sea 100% asíncrona mediante corrutinas de Kotlin. Mi enrutador `AppNavigation` conecta la ruta `'catalogo'` directamente con esta vista de datos."*

---

### 8️⃣ Acceso a DOS Recursos Nativos (Minuto 11:30 a 12:30)
* **Qué decir textualmente (¡CLAVE PARA OBTENER EL 100%!):**
  > *"La rúbrica exige obligatoriamente al menos **dos recursos nativos del dispositivo**, y en nuestro proyecto ambos tienen sentido operativo para el caso BexAI:"*
  1. **Recurso 1 (Cámara + Linterna):** Implementado con **CameraX** y **Google ML Kit** para escanear códigos de barras automáticamente en bodega. Se gestiona de forma segura pidiendo permisos en tiempo de ejecución con `rememberLauncherForActivityResult`.
  2. **Recurso 2 (Motor de Respuesta Háptica / Vibrador):** Implementado a nivel de hardware con `Vibrator` / `VibratorManager` mediante nuestro helper nativo `HapticFeedbackHelper.kt`. Cada vez que el operario escanea un código con éxito o confirma un ajuste de inventario, el teléfono emite una vibración física corta de 100ms para confirmar la acción en ambientes ruidosos de bodega.
  * Ambos recursos están protegidos y declarados en el [`AndroidManifest.xml`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/AndroidManifest.xml) con `android.permission.CAMERA` y `android.permission.VIBRATE`.

---

## 🧩 3. Componentes Clave Creados por Patricio

### 1. `CustomTextField.kt` ([Ver Archivo](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/CustomTextField.kt))
* **Qué hace:** Envuelve un `OutlinedTextField` de Material 3 agregando soporte automático para errores visuales.
* **Por qué es profesional:**
  * Si `isError == true`, inyecta en el `trailingIcon` un `Icon(Icons.Default.Warning)`.
  * Si hay `errorMessage`, dibuja un `Text` con el color `MaterialTheme.colorScheme.error` debajo del campo.
  * Soporta `visualTransformation` (para ocultar contraseñas) y `keyboardOptions` (para teclados numéricos).

### 2. `BotonAccion.kt` ([Ver Archivo](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/BotonAccion.kt))
* **Qué hace:** Botón primario de 50dp de alto que previene dobles pulsaciones involuntarias.
* **Por qué es profesional:**
  * Si `isLoading == true`, deshabilita el botón (`enabled = enabled && !isLoading`).
  * Usa `AnimatedVisibility(visible = isLoading, enter = fadeIn(), exit = fadeOut())` para mostrar el `CircularProgressIndicator`.

---

## 📱 4. Las 7 Pantallas del Flujo de Operador

Todas están creadas y ubicadas en el paquete [`ui/screens/operador/`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/operador/):

1. **`SplashScreen.kt`:**
   * Utiliza un `LaunchedEffect(key1 = true)` con `delay(2000)` para no congelar el hilo principal y navegar a login.
2. **`LoginScreen.kt`:**
   * Contiene **3 FilterChips interactivos**: al tocar *"Operador"*, *"Encargado Bodega"* o *"Gerencial"*, rellena automáticamente las credenciales (`operador@bexai.cl`, `123456`, etc.) para una demostración instantánea.
3. **`SeleccionTiendaScreen.kt`:**
   * Lista en un `LazyColumn` las tiendas disponibles (*Bodega Pudahuel*, *Santiago Centro*, *Providencia*). Al tocar una, guarda el nombre en el estado global de navegación y abre el menú principal.
4. **`MenuOperadorScreen.kt`:**
   * Muestra arriba en un `Card` la **Sucursal Activa** con un botón *"Cambiar"*.
   * Despliega tarjetas táctiles grandes (`OpcionMenuCard`) con iconos distintivos para las acciones de terreno.
5. **`FormularioAjusteScreen.kt`:**
   * Recibe opcionalmente un código de barra prellenado (desde el escáner o manual).
   * Tiene un selector de tipo de movimiento (*Entrada*, *Salida*, *Ajuste/Conteo*).
   * Valida que el código no esté vacío y que la cantidad sea un entero positivo mayor a cero.
   * Incluye botón directo con icono de escáner para abrir la cámara.
6. **`ResumenMovimientoScreen.kt`:**
   * Comprobante tipo "voucher" que resume la transacción: Código, Tipo, Cantidad, Sucursal, Observación y Fecha/Hora formateada con `SimpleDateFormat`.
7. **`PerfilUsuarioScreen.kt`:**
   * Datos del operario activo, modo offline Room, y botón de *"Cerrar Sesión"* que limpia la pila con `popUpTo(0)`.

---

## 🧭 5. Enrutamiento y Navegación (`AppNavigation.kt`)

Ubicado en [`ui/navigation/AppNavigation.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/navigation/AppNavigation.kt).

### Conceptos Clave que Debes Saber Explicar:
1. **`rememberNavController()`:** Instancia que gestiona la pila de pantallas (Backstack).
2. **`NavHost`:** Contenedor donde se definen las rutas y su destino inicial (`startDestination = "splash"`).
3. **Paso de Parámetros entre Pantallas:**
   ```kotlin
   composable(
       route = "ajuste/{codigo}",
       arguments = listOf(navArgument("codigo") { type = NavType.StringType })
   ) { backStackEntry ->
       val codigoArg = backStackEntry.arguments?.getString("codigo") ?: ""
       FormularioAjusteScreen(codigoInicial = codigoArg, ...)
   }
   ```
4. **Limpieza de Pila (`popUpTo` e `inclusive = true`):**
   * En el Splash y Login, usamos `popUpTo("splash") { inclusive = true }` para que al presionar el botón "Atrás" del celular, el usuario **no regrese a la pantalla de carga ni al login** una vez autenticado.

---

## 💻 6. Machete para el Código en Vivo (Lo que te pedirá el Profesor)

En el minuto 12, el profesor te pedirá modificar código en tiempo real. Aquí tienes exactamente cómo resolver las solicitudes típicas:

### Caso A: *"Agrega un campo nuevo al formulario (ej: 'Proveedor') y valídalo"*
1. Abre [`FormularioAjusteScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/operador/FormularioAjusteScreen.kt).
2. Arriba, agrega el estado:
   ```kotlin
   var proveedor by remember { mutableStateOf("") }
   var errorProveedor by remember { mutableStateOf<String?>(null) }
   ```
3. En la columna de campos, agrega el `CustomTextField`:
   ```kotlin
   CustomTextField(
       value = proveedor,
       onValueChange = {
           proveedor = it
           errorProveedor = null
       },
       label = "Proveedor o Distribuidor",
       isError = errorProveedor != null,
       errorMessage = errorProveedor
   )
   ```
4. En el `onClick` del botón guardar, agrega la validación:
   ```kotlin
   if (proveedor.isBlank()) {
       errorProveedor = "El proveedor es obligatorio"
       valido = false
   }
   ```
5. Guarda (`Ctrl + S`), corre la app y muestra el error en vivo.

---

### Caso B: *"Cambia el color o icono de error de tu CustomTextField"*
1. Abre [`CustomTextField.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/components/CustomTextField.kt).
2. Busca la línea donde dice:
   ```kotlin
   Icon(
       imageVector = Icons.Default.Warning,
       contentDescription = "Error",
       tint = MaterialTheme.colorScheme.error
   )
   ```
3. Cámbialo por otro ícono (ej: `Icons.Default.Info` o `Icons.Default.ErrorOutline`) o cambia el color a `Color.Red`.

---

### Caso C: *"Haz que al confirmar el ajuste, en vez del comprobante vuelva directo a la lista de tiendas"*
1. Abre [`AppNavigation.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/navigation/AppNavigation.kt).
2. Busca la ruta `"ajuste/{codigo}"` en la línea del callback `onAjusteConfirmado`.
3. Cambia:
   ```kotlin
   onAjusteConfirmado = { codigo, tipo, cantidad, motivo ->
       navController.navigate("tiendas") {
           popUpTo("menu_operador")
       }
   }
   ```

---

### Caso D: *"Agrega un botón en el Perfil para volver al Splash"*
1. Abre [`PerfilUsuarioScreen.kt`](file:///c:/Users/patri/AndroidStudioProjects/MultiTienda/app/src/main/java/com/example/multitienda/ui/screens/operador/PerfilUsuarioScreen.kt).
2. Agrega:
   ```kotlin
   Button(onClick = { onCerrarSesion() }) {
       Text("Reiniciar Aplicación")
   }
   ```

---

## ❓ 7. Preguntas Teóricas Trampa y Respuestas Perfectas

| Pregunta del Profesor | Respuesta Ideal de Patricio |
| :--- | :--- |
| **"¿Por qué usas `remember` con `mutableStateOf`?"** | *"Porque `mutableStateOf` crea un valor observable por Compose para disparar la recomposición cuando cambia, y `remember` hace que ese valor no se reinicie a su estado inicial cada vez que la función se vuelve a dibujar."* |
| **"¿Qué diferencia hay entre `Spacer` y `Modifier.padding`?"** | *"`padding` añade espacio interno o externo alrededor del propio componente, mientras que `Spacer` es un componente vacío que ocupa una dimensión física fija (ancho o alto) para separar dos elementos adyacentes."* |
| **"¿Cómo se asegura la app de que no se metan datos nulos en cantidad?"** | *"Usamos la función de Kotlin `cantidad.toIntOrNull()`. Si el usuario ingresa letras o símbolos raros, retorna `null`, activando inmediatamente el mensaje de error en nuestro `CustomTextField`."* |
| **"¿Por qué tu `MainActivity` tiene tan pocas líneas?"** | *"Porque aplicamos Clean Architecture y separación de responsabilidades: `MainActivity` solo inicializa el tema y llama a `AppNavigation()`. Toda la lógica de pantallas y rutas está desacoplada en archivos modulares."* |
| **"¿Por qué la cámara y el motor de vibración son recursos nativos?"** | *"Porque requieren interactuar directamente con drivers y hardware del dispositivo físico a través del sistema operativo Android (`CameraManager` y `VibratorService`), requiriendo permisos declarados en el `AndroidManifest.xml` y en tiempo de ejecución."* |

---

### 🚀 Resumen para estar 100% Tranquilo:
Todo lo que acabas de leer **ya está programado, probado y subido a tu rama `feature/ui-navegacion`**. Ten abierta esta guía en una pestaña o en tu teléfono durante tu estudio previo a la evaluación y dominarás la defensa de 15 minutos sin complicaciones.

