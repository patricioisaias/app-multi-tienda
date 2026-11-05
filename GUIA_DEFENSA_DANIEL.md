# Guía de Defensa y Estudio - Daniel Aedo

> **Proyecto:** MultiTienda BexAI - Inventario descentralizado multi-tienda
> **Asignatura:** DSY1105 - Desarrollo de Aplicaciones Móviles (Duoc UC)
> **Evaluaciones:** Parcial 3 - Defensa individual (32%) y Parcial 2 - Encargo grupal (8%)
> **Rama actual del proyecto:** `feat/InterfaceDaniel`
> **Base de la rúbrica:** `session-ses_e579.md`, sección "Evaluación Parcial 3" y "Evaluación Parcial 2"
> **Guía técnica complementaria:** `GUIA_ESTUDIO_DANIEL.md`

---

## 0. Cómo usar esta guía

Esta guía no reemplaza el código: te prepara para explicarlo, demostrarlo y modificarlo en vivo durante los 15 minutos de defensa.

Plan sugerido de 5 sesiones:

| Sesión | Duración | Objetivo |
| :--- | :--- | :--- |
| 1 | 60 min | Memorizar el pitch, la arquitectura y el mapa de la rúbrica |
| 2 | 90 min | Recorrer el módulo de Daniel archivo por archivo |
| 3 | 90 min | Practicar las modificaciones en vivo (validación, componente, DAO, animación) |
| 4 | 60 min | Ensayar persistencia y recursos nativos |
| 5 | 60 min | Defensa simulada completa y banco de preguntas |

Regla de oro: si no puedes explicarlo sin leer el código, todavía no lo dominas.

---

## 1. Contexto del proyecto

### 1.1 Qué es MultiTienda BexAI

Aplicación Android para gestionar inventario de una red de tiendas y bodegas. Permite autenticarse con distintos perfiles, consultar productos, escanear códigos de barra, registrar entradas, salidas y conteos físicos, revisar historial de movimientos y visualizar alertas de quiebre de stock.

### 1.2 Equipo y responsabilidades

| Integrante | Rol principal | Zona del código |
| :--- | :--- | :--- |
| Patricio Céspedes | UI, componentes, navegación y operador | `ui/components/`, `ui/screens/operador/`, `ui/navigation/` |
| Daniel Aedo | Persistencia Room, repositorios y validaciones | `modelo/`, `viewmodel/`, `data/repository/`, `ui/screens/inventario/` |
| Angelo Valenzuela | Cámara, háptico, dashboard y gerencial | `util/`, `ui/screens/scanner/`, `ui/screens/gerencial/` |

### 1.3 Stack técnico real

| Tecnología | Versión / detalle |
| :--- | :--- |
| Kotlin | 2.2.10 |
| Android Gradle Plugin | 9.5.0 |
| Jetpack Compose BOM | 2026.02.01 |
| Material 3 | Incluido vía BOM |
| Room | `androidx.room3` 3.0.3 |
| KSP | 2.3.6 |
| Navigation Compose | 2.8.4 |
| CameraX | 1.4.0 |
| ML Kit Barcode Scanning | 17.3.0 |
| compileSdk / targetSdk | 37 |
| minSdk | 24 |

### 1.4 Credenciales de prueba actuales

El login actual está en `viewmodel/LoginViewModel.kt:29`.

| Perfil | Correo | Contraseña | Nombre que muestra la app |
| :--- | :--- | :--- | :--- |
| Operador | `test1@bexai.cl` | `1234` | Patricio Céspedes |
| Bodega (Daniel) | `test2@bexai.cl` | `1234` | Daniel Aedo |
| Gerencial | `test3@bexai.cl` | `1234` | Angelo Valenzuela |
| Operador alterno | `operador@bexai.cl` | `1234`, `123456` o `admin123` | Patricio Céspedes |
| Bodega alterno | `bodega@bexai.cl` | `1234`, `123456` o `admin123` | Daniel Aedo |
| Gerencial alterno | `gerente@bexai.cl` | `1234`, `123456` o `admin123` | Angelo Valenzuela |

Para la defensa de Daniel usa `test2@bexai.cl` / `1234`. El botón rápido "Encargado Bodega" también completa esos datos.

### 1.5 Productos semilla

Se insertan automáticamente si la tabla está vacía (`ProductoRepository.kt:30`).

| Código | Producto | Stock | Stock mínimo | Uso en demo |
| :--- | :--- | ---: | ---: | :--- |
| `7801234567890` | Leche Entera 1L | 24 | 10 | Producto normal |
| `7809876543210` | Arroz Grado 1 1kg | 8 | 12 | Bajo stock |
| `7801112223334` | Aceite Vegetal 900ml | 3 | 6 | Bajo stock |
| `7805556667778` | Detergente 3kg | 15 | 5 | Producto normal |
| `7804443332221` | Agua Mineral 500ml | 0 | 20 | Quiebre total |
| `7807778889990` | Café Molido 250g | 11 | 6 | Producto normal |

---

## 2. Mapa de la rúbrica

### 2.1 Parcial 3 - Defensa individual (32%)

| IE | Peso | Qué evalúa el profesor | Evidencia en tu proyecto | Estrategia de Daniel |
| :--- | ---: | :--- | :--- | :--- |
| IE 2.1.1 | 20% | Explicar la lógica de validación, justificarla y **agregar condiciones o corregir errores en vivo** | `ProductoViewModel.guardar()`, `AjusteViewModel.registrar()`, `LoginViewModel.login()` | Ensayar el Drill A hasta hacerlo en menos de 5 minutos |
| IE 2.1.2 | 7% | Dominar la estructura visual, navegación, formularios y usabilidad | `AppNavigation.kt`, `MenuOperadorScreen.kt`, `ProductoFormulario.kt` | Explicar el flujo sin leer y justificar por qué es simple |
| IE 2.2.1 | 22% | **Incorporar una clase, módulo, componente o animación nueva** respetando la organización | Paquetes `modelo`, `viewmodel`, `data/repository`, `ui/screens/inventario` | Ensayar el Drill B y el Drill C |
| IE 2.2.2 | 7% | Explicar estructuras de control, gestión de estado y respuesta visual | `StateFlow`, `combine`, `stateIn`, `collectAsState`, `LaunchedEffect` | Ensayar el Drill D y explicar el loader de `BotonAccion` |
| IE 2.3.1 | 18% | **Demostrar en vivo** guardar y recuperar datos locales | Room 3, `AppDatabase`, DAOs, repositorios, seed | Crear producto, cerrar app, reabrir y mostrar historial |
| IE 2.3.2 | 7% | Explicar la arquitectura MVVM y el uso de GitHub/Trello | README, commits, ramas, planificación | Tener a mano el enlace de Trello y 3 commits propios |
| IE 2.4.1 | 7% | Explicar la integración de recursos nativos, permisos y seguridad | `ScannerScreen.kt`, `HapticFeedbackHelper.kt`, `AndroidManifest.xml` | Explicar CAMERA/VIBRATE y el manejo de permisos |
| IE 2.4.2 | 12% | **Ejecutar en vivo** el recurso nativo y mostrar su resultado en la UI | Scanner con CameraX + ML Kit, vibración en `ResultadoScannerScreen` y `ConfigHardwareScreen` | Demostrar cámara y vibración; si falla la cámara, usar entrada manual |

### 2.2 Parcial 2 - Encargo grupal (8%)

| IE | Peso | Qué revisa el profesor |
| :--- | ---: | :--- |
| IE 2.1.1 | 15% | Interfaz coherente, jerárquica, con navegación funcional |
| IE 2.1.2 | 15% | Formularios completos con validación visual por campo |
| IE 2.2.1 | 10% | Validación centralizada y desacoplada de la UI |
| IE 2.2.2 | 10% | Animaciones funcionales que aportan retroalimentación |
| IE 2.3.1 | 15% | Modularidad y persistencia local integrada |
| IE 2.3.2 | 20% | GitHub + Trello con actividad real y commits distribuidos |
| IE 2.4.1 | 15% | Dos o más recursos nativos integrados de forma segura |

### 2.3 Reglas críticas de la evaluación

1. Si el proyecto **no se ejecuta** al momento de la defensa, los ítems posteriores al error se califican con 0.
2. No se permite usar IA ni material de apoyo durante la defensa.
3. El profesor puede pedirte **modificar código en tiempo real**.
4. Si un recurso declarado en la entrega no está implementado, los ítems asociados se califican con 0.
5. La defensa es individual aunque el proyecto sea en pareja.

---

## 3. Pitch de 60 segundos

> "MultiTienda BexAI es una aplicación Android para controlar el inventario de una red de tiendas y bodegas. El problema es que el stock se descuadra porque las existencias teóricas no coinciden con lo contado en pasillo. La app permite iniciar sesión según el perfil, seleccionar la sucursal, consultar el catálogo, escanear códigos de barra con la cámara, registrar entradas, salidas y conteos, y revisar el historial de movimientos. Está construida con Kotlin y Jetpack Compose, siguiendo MVVM y Repository, con persistencia local en Room. Además integra dos recursos nativos: la cámara con CameraX y ML Kit, y el motor háptico del dispositivo. Mi módulo es la capa de datos: entidades, DAOs, repositorios, validaciones en ViewModel y las pantallas de catálogo, detalle, creación, edición, eliminación, conteo e historial."

---

## 4. Arquitectura del proyecto

### 4.1 Estructura real de paquetes

```text
com.example.multitienda/
├── MainActivity.kt
├── data/
│   └── repository/
│       ├── ProductoRepository.kt
│       └── MovimientoRepository.kt
├── modelo/
│   ├── Producto.kt
│   ├── ProductoDAO.kt
│   ├── ProductoFormModel.kt
│   ├── Movimiento.kt
│   ├── MovimientoDAO.kt
│   └── FormularioModel.kt
├── viewmodel/
│   ├── AppDatabase.kt
│   ├── ProductoViewModel.kt
│   ├── HistorialViewModel.kt
│   ├── AjusteViewModel.kt
│   ├── LoginViewModel.kt
│   ├── DashboardViewModel.kt
│   └── ReposicionViewModel.kt
├── ui/
│   ├── components/
│   │   ├── BotonAccion.kt
│   │   └── CustomTextField.kt
│   ├── navigation/
│   │   ├── AppNavigation.kt
│   │   └── AppRoutes.kt
│   ├── screens/
│   │   ├── operador/
│   │   ├── inventario/
│   │   ├── scanner/
│   │   └── gerencial/
│   └── theme/
└── util/
    └── HapticFeedbackHelper.kt
```

### 4.2 MVVM en una frase

- **Model:** `Producto`, `Movimiento`, DAOs y repositorios. Datos y reglas de negocio.
- **View:** composables de Compose. Solo dibujan y emiten eventos.
- **ViewModel:** mantiene el estado de la pantalla, valida y llama al repositorio. Sobrevive a cambios de configuración.

### 4.3 Cadena de datos

```text
Compose (collectAsState)
        │ evento: onValueChange, onClick
        ▼
ViewModel (StateFlow + validaciones)
        │ llama funciones suspend
        ▼
Repository (fuente única de verdad)
        │ llama al DAO
        ▼
DAO (@Query, @Insert, @Update, @Delete)
        │
        ▼
Room / SQLite
        │ emite Flow<List<...>>
        ▼
ViewModel actualiza StateFlow
        │
        ▼
Compose se recompone
```

### 4.4 Conceptos que debes explicar sin dudar

**¿Por qué Repository?**
Para desacoplar Room del ViewModel. Si mañana se agrega una API REST, se cambia el repositorio y el ViewModel no se modifica.

**¿Diferencia entre Flow y StateFlow?**
`Flow` es un flujo de datos asíncrono. `StateFlow` es un flujo con estado que siempre tiene un valor actual y es ideal para que Compose lo observe con `collectAsState()`.

**¿Qué hace `stateIn`?**
Convierte un `Flow` en `StateFlow` dentro de `viewModelScope`, con un valor inicial y una política de compartición. En el proyecto se usa `SharingStarted.WhileSubscribed(5000)` para mantener la observación mientras haya suscriptores.

**¿Por qué `AndroidViewModel`?**
Porque necesita el `Application` para obtener el contexto y crear la base de datos Room.

**¿Qué es KSP?**
Kotlin Symbol Processing. Room 3 lo usa para generar `AppDatabase_Impl` y las implementaciones de los DAOs. Sin KSP y `room3-compiler`, la app falla en ejecución con `cannot find implementation for AppDatabase`.

**¿Por qué la base usa `fallbackToDestructiveMigration()`?**
Porque el esquema cambió al agregar `movimientos` y la app es un prototipo. Si cambia el esquema sin migración manual, Room borra y recrea la base; el seed la vuelve a llenar.

**¿Qué pasa con `stock` y `stockActual`?**
El código real usa `stock` (`Producto.kt:13`). La guía de desarrollo antigua habla de `stockActual`; no uses ese nombre en la defensa.

---

## 5. Módulo de Daniel: pantallas y archivos

### 5.1 Pantallas principales

| # | Pantalla | Ruta | Archivo | Función |
| :-: | :--- | :--- | :--- | :--- |
| 8 | Catálogo | `inventario/catalogo` | `CatalogoProductosScreen.kt` | Lista y busca productos desde Room |
| 9 | Detalle | `inventario/detalle/{id}` | `DetalleProductoScreen.kt` | Muestra todos los datos y acciones |
| 10 | Crear | `inventario/crear` | `CrearProductoScreen.kt` | Formulario de alta con validación |
| 11 | Editar | `inventario/editar/{id}` | `EditarProductoScreen.kt` | Carga el producto y actualiza |
| 12 | Eliminar | `inventario/eliminar/{id}` | `ConfirmarEliminarScreen.kt` | Confirmación destructiva |
| 13 | Conteo físico | `inventario/conteo/{id}` | `ConteoFisicoScreen.kt` | Registra conteo real y ajusta stock |
| 14 | Historial | `inventario/historial` | `HistorialMovimientosScreen.kt` | Lista movimientos con fecha y filtro |

### 5.2 Pantallas adicionales que podrían aparecer en la demo

| # | Pantalla | Ruta | Archivo | Nota |
| :-: | :--- | :--- | :--- | :--- |
| 22 | Administrar tiendas | `admin_tiendas` | `AdminTiendasScreen.kt` | Datos en memoria, no persistidos |
| 23 | Exportar reportes | `exportar_reportes` | `ExportarReportesScreen.kt` | Simulación visual, no genera archivo real |
| 24 | Control de diferencias | `diferencias_stock` | `DiferenciasStockScreen.kt` | Lista productos bajo mínimo y permite rectificar |

No las presentes como si fueran persistencia real. Si el profesor pregunta, responde con honestidad: son pantallas de prototipo.

### 5.3 Archivos clave del módulo

| Archivo | Responsabilidad |
| :--- | :--- |
| `modelo/Producto.kt` | Entidad Room de producto |
| `modelo/Movimiento.kt` | Entidad Room de movimiento con fecha |
| `modelo/ProductoDAO.kt` | Consultas de productos |
| `modelo/MovimientoDAO.kt` | Consultas de movimientos |
| `modelo/ProductoFormModel.kt` | Estado del formulario y errores por campo |
| `viewmodel/AppDatabase.kt` | Base de datos Room, versión 2, singleton |
| `data/repository/ProductoRepository.kt` | Acceso a productos y seed de 6 productos |
| `data/repository/MovimientoRepository.kt` | Acceso a movimientos |
| `viewmodel/ProductoViewModel.kt` | Catálogo, búsqueda, CRUD, conteo y movimientos |
| `viewmodel/HistorialViewModel.kt` | Historial con filtro por código o tipo |
| `viewmodel/AjusteViewModel.kt` | Ajustes de entrada, salida y conteo |
| `ui/screens/inventario/ProductoCard.kt` | Tarjeta reutilizable del catálogo |
| `ui/screens/inventario/ProductoFormulario.kt` | Formulario compartido por crear y editar |
| `ui/screens/inventario/InventarioNavigation.kt` | Rutas del módulo |
| `ui/components/CustomTextField.kt` | Campo con error e ícono de advertencia |
| `ui/components/BotonAccion.kt` | Botón con loader animado |

### 5.4 Flujo del catálogo

1. `ProductoViewModel` crea `ProductoRepository` con el DAO de `AppDatabase`.
2. `productoRepository.productos` es un `Flow<List<Producto>>` que viene de `listarProductosOrdenados()`.
3. `combine` une la lista con el texto de búsqueda.
4. `stateIn` convierte el resultado en `StateFlow`.
5. `CatalogoProductosScreen` usa `collectAsState()` y dibuja un `LazyColumn`.
6. Al escribir en el buscador, `onBusquedaChange` actualiza `_busqueda` y la lista se filtra.

**Dato importante:** la búsqueda actual filtra en memoria sobre la lista que emite Room (`ProductoViewModel.kt:32-40`). El DAO sí tiene una consulta SQL `buscarPorNombre` y el repositorio tiene `buscar()`, pero el ViewModel no la usa. Si el profesor pregunta, di la verdad y explica que se puede mover a SQL para mejorar rendimiento.

### 5.5 Flujo de crear y editar

1. `CrearProductoScreen` limpia el formulario con `limpiarFormulario()`.
2. El usuario escribe y cada `on...Change` actualiza `ProductoFormModel` y limpia el error de ese campo.
3. Al presionar Guardar, `ProductoViewModel.guardar(tienda)` convierte los textos a número con `toDoubleOrNull()` y `toIntOrNull()`.
4. Valida código, nombre, precio, stock y stock mínimo.
5. Si hay errores, actualiza `errorCodigoBarras`, `errorNombre`, `errorPrecio`, `errorStock` y `errorStockMinimo`; no guarda.
6. Verifica que el código de barras no esté duplicado.
7. Inserta o actualiza el producto.
8. Registra un `Movimiento`:
   - creación con stock mayor a 0: tipo "Entrada";
   - edición con cambio de stock: "Entrada" o "Salida" según la diferencia.
9. Marca `guardado = true` y la pantalla navega hacia atrás.

### 5.6 Flujo de conteo físico

1. `ConteoFisicoScreen` carga el producto con `cargarDetalle(id)`.
2. El usuario escribe el conteo real y una observación opcional.
3. `registrarConteo()` valida que sea un entero mayor o igual a 0.
4. Calcula `diferencia = nuevoStock - producto.stock`.
5. Actualiza el stock con `actualizarStock()`.
6. Registra un `Movimiento` de tipo "Conteo" con la diferencia.
7. Actualiza `_detalle` y marca `conteoGuardado = true` para navegar.

### 5.7 Flujo del historial

1. `HistorialViewModel` obtiene `movimientos` del `MovimientoRepository`.
2. `combine` une los movimientos con el filtro.
3. El filtro busca por código o por tipo.
4. `HistorialMovimientosScreen` formatea la fecha con `SimpleDateFormat("dd/MM/yyyy HH:mm")`.
5. Cada tarjeta muestra tipo, fecha, código, cantidad, motivo y tienda.

---

## 6. Guion de defensa de 15 minutos

| Tiempo | Acción | Qué decir / hacer | IE |
| :--- | :--- | :--- | :--- |
| 0:00 - 1:00 | Entorno | Abrir Android Studio, mostrar JDK 17/21, sincronizar Gradle, ejecutar en emulador o dispositivo | Base para todo |
| 1:00 - 3:30 | Arquitectura | Mostrar paquetes y explicar `Compose -> ViewModel -> Repository -> DAO -> Room` | IE 2.3.2 |
| 3:30 - 5:30 | Diseño y navegación | Recorrer `Splash -> Login -> Menú -> Catálogo -> Detalle`; justificar Material 3 y menús por rol | IE 2.1.2 |
| 5:30 - 8:30 | Formularios y validación | Abrir Crear producto, dejar campos vacíos, mostrar errores por campo, luego crear un producto válido | IE 2.1.1 |
| 8:30 - 9:30 | Estado | Explicar `StateFlow`, `collectAsState`, `combine`, `stateIn` y `LaunchedEffect` | IE 2.2.2 |
| 9:30 - 10:30 | Animaciones | Mostrar loader de `BotonAccion`, splash y transición de navegación; explicar qué aportan | IE 2.2.2 |
| 10:30 - 12:30 | Persistencia | Crear producto, mostrar historial, cerrar la app, reabrir y verificar que el dato sigue | IE 2.3.1 |
| 12:30 - 14:30 | Recursos nativos | Cambiar a perfil Operador (`test1@bexai.cl`) para abrir el scanner, escanear `7801234567890` y mostrar vibración; si necesitas ConfigHardware, cambiar a perfil Gerencial (`test3@bexai.cl`) y entrar por Dashboard | IE 2.4.1 / 2.4.2 |
| 14:30 - 15:00 | Modificación en vivo | Agregar la validación del Drill A o el componente del Drill B y compilar | IE 2.1.1 / 2.2.1 |

Consejo: no esperes a que el profesor pregunte. Mientras demuestras, explica el "por qué" de cada decisión.

---

## 7. Checklist de demostración

### 7.1 Antes de entrar a la defensa

- [ ] Android Studio abre el proyecto sin errores de sincronización.
- [ ] Gradle usa JDK 17 o 21 completo.
- [ ] El emulador tiene cámara configurada (`Webcam0` o `VirtualScene`) o tienes un teléfono físico.
- [ ] El volumen del teléfono está activo para sentir la vibración.
- [ ] La app compila con `assembleDebug`.
- [ ] Conoces las credenciales `test2@bexai.cl` / `1234`.
- [ ] Tienes a mano el enlace de GitHub y, si existe, el de Trello.
- [ ] No hay cambios sin commitear en el código de la app que puedan romper la demo.
- [ ] Sabes qué productos están bajo stock: Arroz, Aceite y Agua.

### 7.2 Recorrido recomendado

1. **Login:** perfil Encargado de Bodega -> `test2@bexai.cl` / `1234`.
2. **Menú:** mostrar nombre, rol y sucursal activa.
3. **Catálogo:** entrar por "Consultar Stock Disponible".
4. **Búsqueda:** escribir "Arroz" y mostrar el producto bajo stock.
5. **Crear:** presionar "Nuevo", dejar campos vacíos para mostrar errores y luego crear:
   - código: `7809990001112`
   - nombre: `Yerba Mate 1kg`
   - categoría: `Abarrotes`
   - precio: `4990`
   - stock: `10`
   - stock mínimo: `4`
6. **Detalle:** abrir el producto creado y mostrar todos los campos.
7. **Editar:** cambiar el stock de `10` a `12`; guardar. Esto genera un movimiento de entrada.
8. **Conteo:** registrar un conteo de `7`; el ajuste debe ser `-5` y quedar en el historial.
9. **Historial:** filtrar por `Conteo` o por el código del producto.
10. **Eliminar:** crear otro producto de prueba y eliminarlo desde el detalle.
11. **Diferencias:** abrir "Control de Diferencias" y mostrar los productos bajo mínimo.
12. **Perfil:** abrir "Mi Perfil", mostrar datos y opción de cerrar sesión.
13. **Persistencia:** cerrar la app por completo, volver a abrir, iniciar sesión y verificar que el producto y el historial siguen.
14. **Recursos nativos:** cerrar sesión, entrar como Operador (`test1@bexai.cl` / `1234`), abrir "Escanear Producto", escanear `7801234567890`, mostrar el resultado y la vibración. Si la cámara falla, usar entrada manual.
15. **ConfigHardware:** si necesitas mostrar la prueba aislada de vibración, cerrar sesión, entrar como Gerencial (`test3@bexai.cl` / `1234`), abrir el Dashboard y luego "Diagnóstico de Hardware". Probar pulso corto, alerta y doble pulso.

### 7.3 Rutas de emergencia

Si algo falla:

| Falla | Plan B |
| :--- | :--- |
| La cámara no enfoca | Usar "Entrada Manual" con `7801234567890` |
| El emulador no vibra | Entrar como Gerencial (`test3@bexai.cl`), abrir Dashboard -> Diagnóstico de Hardware y mostrar el estado del pulso; explicar que el emulador no tiene motor físico |
| El producto no aparece | Verificar que se guardó y volver al catálogo; el `Flow` debería actualizar solo |
| La app se cae | Revisar Logcat; no improvises cambios grandes; vuelve a ejecutar |
| El build falla | Revisar el primer error de compilación, no el último |

---

## 8. Talleres de modificación en vivo

Estos talleres están pensados para que el profesor te pida algo y puedas hacerlo sin romper la arquitectura.

### Drill A - Agregar una validación nueva (IE 2.1.1)

**Archivo:** `viewmodel/ProductoViewModel.kt`, función `guardar()`.

Código actual:

```kotlin
val errorCodigo = if (codigo.isBlank()) "El código es obligatorio" else null
```

Cámbialo por:

```kotlin
val errorCodigo = when {
    codigo.isBlank() -> "El código es obligatorio"
    codigo.length < 8 -> "El código debe tener al menos 8 dígitos"
    else -> null
}
```

**Por qué funciona:** el error se guarda en `errorCodigoBarras` y `ProductoFormulario` ya lo muestra con borde rojo, ícono de advertencia y mensaje.

**Cómo demostrarlo:**

1. Abrir Crear producto.
2. Escribir un código corto, por ejemplo `123`.
3. Presionar Guardar.
4. Mostrar el mensaje "El código debe tener al menos 8 dígitos".
5. Corregir el código y guardar.

**Si el profesor pide una validación de categoría:**

En `ProductoFormModel` ya existe `errorCategoria`. Agrega en `guardar()`:

```kotlin
val errorCategoria =
    if (f.categoria.trim().length < 3) "La categoría debe tener al menos 3 caracteres" else null
```

Luego agrégalo a la lista de errores y al `copy()`:

```kotlin
if (listOf(
        errorCodigo,
        errorNombre,
        errorCategoria,
        errorPrecio,
        errorStock,
        errorStockMinimo
    ).any { it != null }
) {
    _form.update {
        it.copy(
            errorCodigoBarras = errorCodigo,
            errorNombre = errorNombre,
            errorCategoria = errorCategoria,
            errorPrecio = errorPrecio,
            errorStock = errorStock,
            errorStockMinimo = errorStockMinimo
        )
    }
    return
}
```

**Ojo:** hoy la categoría vacía se reemplaza por "General", así que para que la validación sea visible usa una categoría de menos de 3 caracteres o cambia el `ifBlank` por un valor vacío. La opción más segura en vivo es la validación del código.

### Drill B - Crear un componente nuevo (IE 2.2.1)

**Objetivo:** agregar un componente reutilizable sin tocar la base de datos.

**Paso 1:** crear `app/src/main/java/com/example/multitienda/ui/screens/inventario/EstadoStockBadge.kt`.

```kotlin
package com.example.multitienda.ui.screens.inventario

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EstadoStockBadge(
    stock: Int,
    stockMinimo: Int,
    modifier: Modifier = Modifier
) {
    val critico = stock <= stockMinimo
    Surface(
        color = if (critico) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.primaryContainer
        },
        contentColor = if (critico) {
            MaterialTheme.colorScheme.onErrorContainer
        } else {
            MaterialTheme.colorScheme.onPrimaryContainer
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = if (critico) "Stock crítico" else "Stock OK",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
```

**Paso 2:** usarlo en `ProductoCard.kt`.

Reemplaza el bloque:

```kotlin
if (bajoStock) {
    Text(
        text = "BAJO STOCK",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.error
    )
}
```

por:

```kotlin
EstadoStockBadge(
    stock = producto.stock,
    stockMinimo = producto.stockMinimo
)
```

Y elimina la línea `val bajoStock = producto.stock <= producto.stockMinimo`, porque ya no se usa.

**Qué explicar:** el componente respeta la organización del módulo, está en `ui/screens/inventario`, es reutilizable y no acopla la lógica de stock a la pantalla.

### Drill C - Agregar una consulta nueva en Room (IE 2.2.1 / 2.3.1)

**Archivo:** `modelo/ProductoDAO.kt`.

Agrega:

```kotlin
@Query("SELECT * FROM productos WHERE precio BETWEEN :min AND :max ORDER BY precio ASC")
fun filtrarPorPrecio(min: Double, max: Double): Flow<List<Producto>>
```

**Archivo:** `data/repository/ProductoRepository.kt`.

Agrega:

```kotlin
fun filtrarPorPrecio(min: Double, max: Double): Flow<List<Producto>> =
    dao.filtrarPorPrecio(min, max)
```

**Qué explicar:** la consulta vive en el DAO, el repositorio la expone y el ViewModel podría consumirla con `stateIn`. Así se respeta la separación de responsabilidades.

Si el profesor pide verla en pantalla, puedes usar el catálogo y cambiar temporalmente la fuente de `productos` por `productoRepository.filtrarPorPrecio(0.0, 5000.0)`; no es necesario para demostrar que compila.

### Drill D - Agregar una animación simple (IE 2.2.2)

**Archivo:** `ui/screens/inventario/ProductoCard.kt`.

Agrega el import:

```kotlin
import androidx.compose.animation.animateContentSize
```

Y modifica el `modifier` de la tarjeta:

```kotlin
Card(
    modifier = Modifier
        .fillMaxWidth()
        .animateContentSize()
        .clickable { onClick() },
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    )
)
```

**Qué explicar:** la animación suaviza los cambios de tamaño de la tarjeta cuando aparece o cambia el contenido. Es funcional, simple y no distrae.

También puedes defender las animaciones existentes:

- `BotonAccion` usa `AnimatedVisibility` con `fadeIn` y `fadeOut` mientras carga.
- `SplashScreen` muestra `CircularProgressIndicator` y navega después de 2 segundos.
- La navegación entre pantallas usa las transiciones por defecto de Navigation Compose.

### Drill E - Demostrar recursos nativos (IE 2.4.1 / 2.4.2)

No necesitas modificar código, solo ejecutar y explicar:

1. Entrar como Operador (`test1@bexai.cl`) y abrir `ScannerScreen` desde el menú.
2. Mostrar que solicita el permiso `CAMERA` en tiempo de ejecución.
3. Escanear un código.
4. `ResultadoScannerScreen` dispara `HapticFeedbackHelper.vibrarExito(context)` y consulta el producto en Room.
5. Para `ConfigHardwareScreen`, entrar como Gerencial (`test3@bexai.cl`), abrir el Dashboard y luego "Diagnóstico de Hardware"; probar los tres pulsos.
6. Explicar que `VIBRATE` está declarado en `AndroidManifest.xml` y que `HapticFeedbackHelper` usa `VibratorManager` en Android 12+ y `Vibrator` en versiones anteriores.
7. Explicar que la cámara se dibuja con `PreviewView` dentro de un `AndroidView` y que el análisis de imágenes usa ML Kit con `ImageAnalysis`.

---

## 9. Banco de preguntas y respuestas

### Arquitectura

**1. ¿Qué es MVVM y cómo se aplica?**
Modelo para datos, Vista para Compose y ViewModel para estado y lógica. La vista no conoce Room; el ViewModel no conoce detalles de Compose.

**2. ¿Por qué usas Repository?**
Para desacoplar la fuente de datos. Si se agrega una API REST, cambia el repositorio y el ViewModel sigue igual.

**3. ¿Dónde se crea la base de datos?**
En `AppDatabase.getDatabase(context)`, con patrón singleton y `synchronized` para evitar múltiples instancias.

**4. ¿Qué hace KSP?**
Procesa las anotaciones de Room y genera las implementaciones de DAOs y base de datos.

**5. ¿Qué pasa si falta KSP?**
Room no genera `AppDatabase_Impl` y la app falla con `cannot find implementation for AppDatabase`.

**6. ¿Por qué Room 3 y no Room 2?**
El proyecto usa `androidx.room3`, que requiere KSP y tiene una API actualizada. La versión está en `gradle/libs.versions.toml`.

### Estado y flujos

**7. ¿Diferencia entre `State` y `StateFlow`?**
`State` es local a Compose y se usa con `remember`/`mutableStateOf`. `StateFlow` vive en el ViewModel, sobrevive a recomposiciones y se observa con `collectAsState()`.

**8. ¿Qué hace `collectAsState()`?**
Convierte un `StateFlow` en un estado observable por Compose; cuando cambia, el composable se vuelve a dibujar.

**9. ¿Qué hace `combine`?**
Combina dos flujos; en el catálogo une la lista de productos con el texto de búsqueda (o en el historial, movimientos con filtro).

**10. ¿Qué hace `stateIn`?**
Convierte un flujo en `StateFlow`, le da un valor inicial y define cuándo se mantiene activo.

**11. ¿Para qué usas `LaunchedEffect`?**
Para ejecutar efectos que dependen de una clave: cargar el detalle de un producto, limpiar el formulario, navegar cuando `guardado` cambia.

### Persistencia

**12. ¿Cómo se guardan los datos?**
Room sobre SQLite. `Producto` y `Movimiento` son entidades; `ProductoDAO` y `MovimientoDAO` tienen las consultas; los repositorios encapsulan el acceso.

**13. ¿Cómo se recuperan los datos?**
El DAO devuelve `Flow<List<Producto>>`; el ViewModel lo transforma con `combine`/`stateIn`; Compose lo observa con `collectAsState`.

**14. ¿Cómo evitas códigos de barras duplicados?**
`guardar()` consulta `obtenerPorCodigo(codigo)` y compara el `id`; si existe otro producto con el mismo código, muestra "Ya existe un producto con este código".

**15. ¿Cómo se registra un movimiento?**
Con `MovimientoRepository.registrar()`. Se registra al crear producto con stock, al editar si cambia el stock, en el conteo físico y en los ajustes de entrada/salida.

**16. ¿Qué pasa si cambia el esquema de Room?**
Se debe subir la versión. Si no hay migración manual, `fallbackToDestructiveMigration()` borra y recrea la base. En este prototipo es aceptable porque el seed vuelve a llenar los datos.

**17. ¿La búsqueda usa SQL?**
En la versión actual el ViewModel filtra en memoria sobre la lista emitida por Room. El DAO tiene `buscarPorNombre` y el repositorio tiene `buscar()`, pero no están conectados al ViewModel. Es una mejora pendiente.

### Formularios y validación

**18. ¿Dónde están las validaciones?**
En el ViewModel: `ProductoViewModel.guardar()`, `AjusteViewModel.registrar()` y `LoginViewModel.login()`. La vista solo muestra los errores que vienen del estado.

**19. ¿Por qué los campos del formulario son `String`?**
Porque el usuario escribe texto y las validaciones convierten con `toDoubleOrNull()` o `toIntOrNull()`. Así se evita un `NullPointerException` o un `NumberFormatException`.

**20. ¿Cómo se muestra un error por campo?**
`CustomTextField` recibe `isError` y `errorMessage`; muestra borde rojo, ícono de advertencia y texto de error.

**21. ¿Hay validación duplicada en ajustes?**
Sí. `FormularioAjusteScreen` valida código y cantidad antes de llamar al ViewModel, y `AjusteViewModel.registrar()` vuelve a validar cantidad y existencia. Es una deuda técnica: la recomendación es centralizar toda la validación en el ViewModel.

### Recursos nativos

**22. ¿Cuáles son los dos recursos nativos?**
Cámara con CameraX + ML Kit y motor háptico con `Vibrator`/`VibratorManager`.

**23. ¿Qué permisos usan?**
`CAMERA` y `VIBRATE`, declarados en `AndroidManifest.xml`. El permiso de cámara se solicita en tiempo de ejecución; la vibración no requiere permiso en tiempo de ejecución, solo declaración.

**24. ¿Cómo se maneja el permiso denegado?**
`ScannerScreen` muestra una tarjeta de error, un botón para volver a solicitar el permiso y una opción de entrada manual.

**25. ¿Cómo se integra la cámara con la UI?**
Con `PreviewView` dentro de un `AndroidView`; encima se dibuja un `Canvas` con el visor de escaneo y se agregan controles de linterna y entrada manual.

### Preguntas de cierre

**26. ¿Qué limitaciones tiene el proyecto?**
El stock es global, no por tienda; la exportación de reportes es simulada; la administración de tiendas es en memoria; el login es local y no usa backend; las animaciones son mínimas.

**27. ¿Cómo escalarías el stock por tienda?**
Agregar `tiendaId` a `Producto` o crear una tabla `StockTienda` con clave compuesta `(productoId, tiendaId)`, ajustar DAOs y migrar la base. Es la evolución natural del caso multi-tienda.

**28. ¿Qué mejorarías antes de la entrega final?**
Persistencia real de tiendas, exportación real a CSV, validación centralizada al 100%, stock por sucursal, más animaciones y pruebas unitarias.

**29. ¿Qué fue lo más difícil de tu módulo?**
Integrar Room con Compose de forma reactiva: entender que el DAO emite `Flow`, que el ViewModel lo transforma en `StateFlow` y que la UI se actualiza sola.

**30. ¿Qué parte del proyecto no es tuya?**
El scanner y el dashboard son de Angelo; la navegación y las pantallas de operador son de Patricio. Puedo explicarlas a nivel de integración, pero mi módulo principal es la capa de datos y el inventario.

---

## 10. Riesgos y respuestas honestas

### 10.1 Stock global, no por tienda

La app permite seleccionar una sucursal en memoria (`tiendaActual` en `AppNavigation.kt`), pero el stock de `Producto` no está segmentado por tienda. Los movimientos sí guardan el nombre de la tienda.

**Respuesta sugerida:** "En esta versión el stock es consolidado para evitar duplicar la entidad en el prototipo. La tienda se registra en el movimiento para trazabilidad. Para la siguiente iteración, la tabla `StockTienda` permitiría stock por sucursal."

No inventes que ya está implementado.

### 10.2 Exportación de reportes

`ExportarReportesScreen` simula la generación con `delay(1200)` y muestra un `Toast`. No crea un archivo real.

**Respuesta sugerida:** "La pantalla está maquetada para la experiencia de usuario; la generación real de CSV queda como trabajo futuro."

### 10.3 Administración de tiendas

`AdminTiendasScreen` usa una `mutableStateListOf` en memoria. Al cerrar la app se pierde el estado.

**Respuesta sugerida:** "Es una pantalla de prototipo para mostrar la administración; no persiste porque no forma parte del alcance de mi módulo."

### 10.4 Trello

La rúbrica grupal asigna 20% a GitHub + Trello. Antes de la defensa verifica que exista el tablero y que tenga tareas distribuidas. Ten el enlace y una captura de respaldo.

### 10.5 README desactualizado

El README menciona credenciales y funcionalidades que no coinciden exactamente con el código actual. Si el profesor lo abre, debe encontrar coherencia. Antes de la entrega, actualiza:

- credenciales reales;
- cantidad de pantallas/rutas;
- aclarar que exportar reportes es una maqueta;
- aclarar el alcance del stock global.

### 10.6 JDK y SDK

El proyecto usa `compileSdk 37`, que es muy nuevo. Si el laboratorio no tiene ese SDK, el build puede fallar. Antes de la defensa:

1. Abrir el proyecto en el PC del laboratorio.
2. Sincronizar Gradle.
3. Ejecutar `assembleDebug`.
4. Si falla por SDK, coordinar con el docente o usar el equipo donde ya funciona.

No cambies `compileSdk` a la ligera: puede afectar dependencias.

---

## 11. Cheat sheet

### 11.1 Comandos

```powershell
# Configurar JDK completo (ajusta la ruta a tu equipo)
$env:JAVA_HOME='C:\Users\Daniel\.gradle\jdks\eclipse_adoptium-21-amd64-windows.2'

# Compilar
.\gradlew.bat :app:assembleDebug --no-configuration-cache

# Instalar en el dispositivo conectado
.\gradlew.bat :app:installDebug
```

### 11.2 Evidencia Git

```powershell
git branch --show-current
git log --oneline --graph --all -20
git log --author="Daniel" --oneline
git shortlog -sn --all
```

### 11.3 Rutas principales

| Ruta | Pantalla |
| :--- | :--- |
| `splash` | Splash |
| `login` | Login |
| `menu_operador` | Menú por rol |
| `inventario/catalogo` | Catálogo |
| `inventario/detalle/{id}` | Detalle |
| `inventario/crear` | Crear producto |
| `inventario/editar/{id}` | Editar producto |
| `inventario/eliminar/{id}` | Confirmar eliminación |
| `inventario/conteo/{id}` | Conteo físico |
| `inventario/historial` | Historial |
| `scanner` | Scanner |
| `scan_result/{codigo}` | Resultado de escaneo |
| `ajuste/{codigo}` | Formulario de ajuste |
| `dashboard` | Dashboard gerencial |
| `alertas` | Alertas de quiebre |
| `config_hardware` | Diagnóstico de hardware |
| `diferencias_stock` | Control de diferencias |

### 11.4 Archivos que debes saber ubicar de memoria

| Necesitas... | Archivo |
| :--- | :--- |
| Validar un producto | `viewmodel/ProductoViewModel.kt` |
| Ver la tabla de productos | `modelo/Producto.kt` |
| Ver las consultas | `modelo/ProductoDAO.kt` |
| Ver la base de datos | `viewmodel/AppDatabase.kt` |
| Ver el seed | `data/repository/ProductoRepository.kt` |
| Ver las rutas | `ui/navigation/AppNavigation.kt` y `InventarioNavigation.kt` |
| Ver el formulario | `ui/screens/inventario/ProductoFormulario.kt` |
| Ver la búsqueda | `viewmodel/ProductoViewModel.kt` y `CatalogoProductosScreen.kt` |
| Ver el historial | `viewmodel/HistorialViewModel.kt` y `HistorialMovimientosScreen.kt` |
| Ver el scanner | `ui/screens/scanner/ScannerScreen.kt` |
| Ver la vibración | `util/HapticFeedbackHelper.kt` |
| Ver los permisos | `app/src/main/AndroidManifest.xml` |

---

## 12. Autoevaluación final

Marca cada punto sin mirar el código:

- [ ] Puedo explicar el problema que resuelve la app en 60 segundos.
- [ ] Puedo dibujar la cadena `Compose -> ViewModel -> Repository -> DAO -> Room`.
- [ ] Puedo explicar qué hace `StateFlow` y por qué no usar solo `Flow`.
- [ ] Puedo explicar qué hace `collectAsState()`.
- [ ] Puedo explicar dónde se valida el formulario de producto.
- [ ] Puedo agregar una validación nueva sin ayuda.
- [ ] Puedo crear un composable nuevo y usarlo en una pantalla.
- [ ] Puedo agregar una consulta nueva a un DAO y compilar.
- [ ] Puedo explicar cómo se guarda un producto y cómo se recupera.
- [ ] Puedo demostrar el historial de movimientos.
- [ ] Puedo demostrar que los datos persisten al cerrar y abrir la app.
- [ ] Puedo explicar los dos recursos nativos y sus permisos.
- [ ] Puedo demostrar la cámara y la vibración en vivo.
- [ ] Puedo responder qué limitaciones tiene el proyecto sin ponerme nervioso.
- [ ] Tengo el enlace de GitHub y, si existe, el de Trello.
- [ ] Mi README coincide con lo que realmente hace la app.

Si marcaste menos de 12 puntos, repite las sesiones 2 y 3 antes de la defensa.

---

## 13. Últimos consejos para la defensa

1. Habla del "por qué", no solo del "qué". El profesor evalúa comprensión, no memoria.
2. Si no sabes algo, dilo y explica cómo lo investigarías. No inventes.
3. Antes de modificar código en vivo, di en voz alta qué archivo vas a abrir y por qué.
4. Compila después de cada cambio. Un error de sintaxis puede costar tiempo.
5. Si el profesor te pide algo que no está implementado, responde con honestidad y propone cómo lo harías.
6. Mantén la calma: la app ya funciona; tu trabajo es demostrar que entiendes cómo.
7. No uses IA ni material externo durante la defensa.
8. Guarda una copia del proyecto que sabes que compila. No hagas cambios grandes la noche anterior.

---

## 14. Referencia rápida de la rúbrica

| Indicador | Peso individual | Peso grupal |
| :--- | ---: | ---: |
| IE 2.1.1 | 20% | 15% |
| IE 2.1.2 | 7% | 15% |
| IE 2.2.1 | 22% | 10% |
| IE 2.2.2 | 7% | 10% |
| IE 2.3.1 | 18% | 15% |
| IE 2.3.2 | 7% | 20% |
| IE 2.4.1 | 7% | 15% |
| IE 2.4.2 | 12% | - |
| **Total** | **100%** | **100%** |

La defensa individual vale 32% de la nota de la asignatura. La entrega grupal vale 8%. La nota individual depende solo de lo que demuestres en los 15 minutos.
