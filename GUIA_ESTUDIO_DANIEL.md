# Guía de Estudio - Módulo de Datos e Inventario (Daniel)

> Documento de estudio personal sobre el módulo de persistencia, catálogo y bodega
> de la app MultiTienda (BexAI). Rama de trabajo: `viewmodel-test-daniel`.
>
> Este documento se actualiza a medida que avanza la implementación. Al final de
> cada paso se registra qué se hizo, por qué y cómo probarlo.

---

## 1. Objetivo y alcance

El módulo de Daniel cubre las **pantallas 8 a 14** de la guía de desarrollo:

| # | Pantalla | Responsabilidad |
| :-: | :--- | :--- |
| 8 | CatalogoProductosScreen | Listar y buscar productos desde Room |
| 9 | DetalleProductoScreen | Ver un producto completo por id |
| 10 | CrearProductoScreen | Crear un producto con validaciones |
| 11 | EditarProductoScreen | Actualizar un producto existente |
| 12 | ConfirmarEliminarScreen | Confirmar el borrado de un producto |
| 13 | ConteoFisicoScreen | Registrar el conteo físico y ajustar stock |
| 14 | HistorialMovimientosScreen | Ver movimientos registrados con fecha |

**Dentro de alcance (carpetas propias):**
`modelo/`, `viewmodel/`, `data/repository/` y `ui/screens/inventario/`.

**Fuera de alcance (no se toca):**
`ui/navigation/AppNavigation.kt`, `ui/components/`, `ui/screens/operador/`,
`ui/screens/scanner/`, `ui/screens/gerencial/`, `ui/view/ProductoView.kt`,
`MainActivity.kt`, `AndroidManifest.xml` y los archivos Gradle.

---

## 2. Conceptos clave

### MVVM (Model - View - ViewModel)
- **Model:** datos y reglas de negocio (entidades, DAOs, repositorios).
- **View:** interfaz en Jetpack Compose. Solo dibuja y envía eventos.
- **ViewModel:** guarda el estado de la pantalla y ejecuta la lógica. Sobrevive
  a cambios de configuración (por ejemplo, girar el teléfono).

### Repository
Capa intermedia entre el ViewModel y la base de datos. Evita que el ViewModel
conozca detalles de Room. Si mañana se agrega una API REST, solo cambia el
repositorio y el ViewModel no se entera.

### Room
Librería de persistencia local sobre SQLite. Se compone de:
- `@Entity`: una tabla (por ejemplo, `productos`).
- `@Dao`: funciones para leer y escribir (consultas).
- `@Database`: el contenedor que une entidades y DAOs.

> En este proyecto se usa **Room 3** (`androidx.room3`). Room 3 **solo genera
> código con KSP**, no con kapt. Sin el plugin KSP y el artefacto
> `room3-compiler`, la base falla en tiempo de ejecución.

### Flow y StateFlow
- `Flow`: flujo de datos asíncrono que emite cambios de la base de datos.
- `StateFlow`: estado observable que siempre tiene un valor actual. Compose lo
  consume con `collectAsState()` y se redibuja solo cuando cambia.

### KSP
Kotlin Symbol Processing. Es el procesador que lee las anotaciones de Room y
genera el código de implementación (`ProductoDAO_Impl`, `AppDatabase_Impl`).

---

## 3. Flujo de datos del módulo

```text
Compose (pantalla)
      │  evento: onValueChange, onClick
      ▼
ViewModel (estado + validaciones)
      │  llama funciones del repositorio
      ▼
Repository (fuente única de verdad)
      │  llama al DAO
      ▼
DAO (@Query, @Insert, @Update, @Delete)
      │
      ▼
Room / SQLite (tabla productos y movimientos)
      │  emite Flow<List<...>>
      ▼
ViewModel actualiza el StateFlow → Compose se redibuja
```

---

## 4. Bitácora por paso

> Se completa al terminar cada paso.

### Paso 1 - Rama y guía de estudio

- **Qué se hizo:** crear la rama `viewmodel-test-daniel` desde `master` y este
  documento.
- **Por qué:** aislar el trabajo para no afectar `master` ni el trabajo de
  Patricio y Angelo.
- **Archivos:** `GUIA_ESTUDIO_DANIEL.md`.

### Paso 2 - Modelo y DAOs

- **Qué se hizo:** se amplió `Producto` con `codigoBarras`, `categoria` y
  `stockMinimo`; se crearon `Movimiento`, `MovimientoDAO` y `ProductoFormModel`;
  se agregaron consultas a `ProductoDAO`.
- **Por qué:** las pantallas 9 (Detalle), 11 (Editar) y 19 (Alertas, de Angelo)
  necesitan código de barras y stock mínimo. El historial necesita una entidad
  propia con fecha.
- **Decisión:** se mantuvo el campo `stock` (no se renombró a `stockActual`)
  para no romper `FichaProducto` ni `ProductoView`, que ya lo usan.
- **Archivos:** `modelo/Producto.kt`, `modelo/Movimiento.kt`,
  `modelo/ProductoDAO.kt`, `modelo/MovimientoDAO.kt`,
  `modelo/ProductoFormModel.kt`.
- **Conceptos aplicados:**
  - `@Entity(tableName = "productos")` indica que la clase es una tabla.
  - `@PrimaryKey(autoGenerate = true)`: si el `id` vale 0, Room lo asigna solo.
  - `Flow<List<Producto>>`: la lista se emite cada vez que cambia la tabla.
  - `LIKE '%' || :texto || '%'`: búsqueda parcial por nombre.
  - `stock <= stockMinimo`: consulta de productos críticos.
  - `ProductoFormModel`: guarda los campos como `String` porque el usuario
    escribe texto; las validaciones convierten a número.

### Paso 3 - Base de datos

- **Qué se hizo:** `AppDatabase` subió a versión 2 y ahora declara las entidades
  `Producto` y `Movimiento`, expone `productoDAO()` y `movimientoDAO()`, y usa
  `fallbackToDestructiveMigration()`.
- **Por qué:** se agregó una tabla nueva (`movimientos`). Al cambiar el esquema,
  Room exige subir la versión. Sin una migración manual, el fallback borra y
  recrea la base; el seed del repositorio la vuelve a llenar.
- **Archivos:** `viewmodel/AppDatabase.kt`.
- **Concepto:** `@Database` une las tablas y los DAOs. `INSTANCIA` con
  `synchronized` evita crear varias conexiones a la vez (patrón singleton).

### Paso 4 - Repositorios

- **Qué se hizo:** `ProductoRepository` y `MovimientoRepository`.
- **Por qué:** el ViewModel no debe conocer Room. El repositorio es la fuente
  única de verdad y el punto que se cambiaría si se agregara una API REST.
- **Archivos:** `data/repository/ProductoRepository.kt`,
  `data/repository/MovimientoRepository.kt`.
- **Detalle clave:** `sembrarSiVacio()` inserta 6 productos de ejemplo solo si la
  tabla está vacía. Algunos quedan bajo el stock mínimo (`Arroz 8/12`,
  `Aceite 3/6`, `Agua 0/20`) para que las alertas tengan datos.
- **Cómo probarlo:** al abrir el catálogo por primera vez deberían aparecer los
  6 productos (requiere KSP para que Room funcione en ejecución).

### Paso 5 - ViewModels

- **Qué se hizo:** `ProductoViewModel` e `HistorialViewModel`.
- **Por qué:** se necesitaba un ViewModel nuevo para el módulo sin tocar
  `FormularioViewModel` (usado por la pantalla antigua `ProductoView`).
- **Archivos:** `viewmodel/ProductoViewModel.kt`,
  `viewmodel/HistorialViewModel.kt`.
- **Qué hace `ProductoViewModel`:**
  - `productos`: `combine` entre la lista de Room y el texto de búsqueda.
  - `busqueda`: `StateFlow` con lo que escribe el usuario.
  - `form`: `StateFlow<ProductoFormModel>` con los datos y errores del formulario.
  - `guardar()`: valida en el ViewModel (IE 2.2.1), verifica código duplicado,
    inserta o actualiza, y registra un `Movimiento`.
  - `cargarParaEditar()`, `cargarDetalle()`, `eliminar()`.
  - `registrarConteo()`: valida el conteo, actualiza el stock y registra el
    movimiento con la diferencia.
  - `sembrarSiVacio()` en `init`.
- **Conceptos aplicados:** `viewModelScope.launch` ejecuta la lógica sin bloquear
  la UI; `stateIn` convierte el `Flow` en `StateFlow`; `update` cambia el estado
  de forma segura.
- **`HistorialViewModel`:** `combine` entre los movimientos y un filtro por
  código o tipo.

### Paso 6 - Pantallas

- **Qué se hizo:** las 7 pantallas del módulo en `ui/screens/inventario/`.
- **Por qué:** cada pantalla tiene un contenedor "con estado" que conecta el
  ViewModel y un contenido "sin estado" que solo dibuja. Así se puede previsualizar
  con `@Preview` sin base de datos.
- **Archivos:**
  - `CatalogoProductosScreen.kt` (8): búsqueda, lista y accesos a crear/historial.
  - `DetalleProductoScreen.kt` (9): ficha completa y acciones.
  - `CrearProductoScreen.kt` (10): formulario de alta.
  - `EditarProductoScreen.kt` (11): formulario de edición.
  - `ConfirmarEliminarScreen.kt` (12): confirmación destructiva.
  - `ConteoFisicoScreen.kt` (13): conteo real y ajuste calculado.
  - `HistorialMovimientosScreen.kt` (14): movimientos con fecha y filtro.
  - `ProductoCard.kt` y `ProductoFormulario.kt`: componentes propios del módulo.
- **Rúbrica:** validaciones en el ViewModel (IE 2.2.1) y
  `CircularProgressIndicator` al guardar mediante `BotonAccion` (IE 2.2.2).
- **Cómo probarlo:** abrir cada `@Preview` en Android Studio. Para probar en la
  app hace falta KSP y las rutas conectadas.

### Paso 7 - Navegación preparada

- **Qué se hizo:** `InventarioNavigation.kt` con `InventarioRoutes` y la función
  `NavGraphBuilder.inventarioNavGraph(navController, tienda)`.
- **Por qué:** `AppNavigation.kt` es de Patricio. Se deja todo listo en un archivo
  propio para no modificarlo.
- **Archivos:** `ui/screens/inventario/InventarioNavigation.kt`.
- **Integración pendiente (Patricio):**
  ```kotlin
  // En ui/navigation/AppNavigation.kt
  import com.example.multitienda.ui.screens.inventario.inventarioNavGraph

  // Dentro del NavHost:
  inventarioNavGraph(navController, tiendaActual)
  ```
  Opcionalmente, reemplazar la ruta `"catalogo"` que hoy muestra `ProductoView()`
  por una navegación a `InventarioRoutes.CATALOGO`.

### Paso 8 - Verificación

- **Build:** `assembleDebug` compiló correctamente con JDK 21 (Temurin). El JDK
  que trae el IDE fallaba por un `jlink` incompleto, así que se usó:
  ```powershell
  $env:JAVA_HOME='C:\Users\Daniel\.gradle\jdks\eclipse_adoptium-21-amd64-windows.2'
  .\gradlew.bat :app:assembleDebug --no-configuration-cache
  ```
- **Advertencias resueltas:** se cambió `Icons.Default.ArrowBack` por
  `Icons.AutoMirrored.Filled.ArrowBack`.
- **Git:** `git status` confirma que solo cambiaron archivos del módulo de Daniel
  y `GUIA_ESTUDIO_DANIEL.md`. `master` no se tocó.
- **Corrección:** se habilitó KSP (`2.3.6`) y `room3-compiler`; Room ya genera
  `AppDatabase_Impl` y la app funciona en ejecución.

### Paso 9 - Ajustes persistidos y contrato para Angelo

- **Qué se hizo:** se creó `AjusteViewModel` para registrar entradas, salidas y
  conteos desde `FormularioAjusteScreen`; actualiza el stock y guarda el
  `Movimiento` antes de mostrar el resumen.
- **Reglas:** `Entrada` suma, `Salida` resta y valida stock suficiente,
  `Ajuste / Conteo` fija el stock absoluto y registra la diferencia. Si el
  código no existe, se muestra un error y no se navega.
- **Navegación:** `inventarioNavGraph` quedó conectado. Se mantiene
  `ProductoView` como acceso rápido y se agregó un botón al catálogo de Daniel.
- **Angelo (scanner):** la ruta `"scanner"` ya navega a `ajuste/{codigo}`.
  Al reemplazar el placeholder por `ScannerScreen`, usar:
  ```kotlin
  onCodigoEscaneado = { codigo ->
      navController.navigate("ajuste/${Uri.encode(codigo)}") {
          popUpTo("scanner") { inclusive = true }
      }
  }
  ```
- **Angelo (dashboard/alertas):** `ProductoViewModel.productosBajoStock` (o
  `ProductoRepository.productosBajoStock`) expone los productos con
  `stock <= stockMinimo`.
- **Nota:** no se agregaron entidades nuevas. Si se requiere persistir una
  solicitud de reposición, coordinar el cambio de esquema.

---

## 5. Tabla de archivos

| Archivo | Responsabilidad | Paso |
| :--- | :--- | :-: |
| `GUIA_ESTUDIO_DANIEL.md` | Documentación de estudio | 1 |
| `modelo/Producto.kt` | Entidad Room de producto | 2 |
| `modelo/Movimiento.kt` | Entidad Room de movimiento | 2 |
| `modelo/ProductoDAO.kt` | Consultas de productos | 2 |
| `modelo/MovimientoDAO.kt` | Consultas de movimientos | 2 |
| `modelo/ProductoFormModel.kt` | Estado del formulario y errores | 2 |
| `viewmodel/AppDatabase.kt` | Base de datos Room | 3 |
| `data/repository/ProductoRepository.kt` | Acceso a productos + seed | 4 |
| `data/repository/MovimientoRepository.kt` | Acceso a movimientos | 4 |
| `viewmodel/ProductoViewModel.kt` | Lógica de catálogo y CRUD | 5 |
| `viewmodel/HistorialViewModel.kt` | Lógica del historial | 5 |
| `ui/screens/inventario/CatalogoProductosScreen.kt` | Pantalla 8 | 6 |
| `ui/screens/inventario/DetalleProductoScreen.kt` | Pantalla 9 | 6 |
| `ui/screens/inventario/CrearProductoScreen.kt` | Pantalla 10 | 6 |
| `ui/screens/inventario/EditarProductoScreen.kt` | Pantalla 11 | 6 |
| `ui/screens/inventario/ConfirmarEliminarScreen.kt` | Pantalla 12 | 6 |
| `ui/screens/inventario/ConteoFisicoScreen.kt` | Pantalla 13 | 6 |
| `ui/screens/inventario/HistorialMovimientosScreen.kt` | Pantalla 14 | 6 |
| `ui/screens/inventario/InventarioNavigation.kt` | Rutas del módulo (conectadas en `AppNavigation`) | 7 |
| `viewmodel/AjusteViewModel.kt` | Persistencia y validación de ajustes de stock | 9 |

---

## 6. Preguntas de defensa (Parcial 3)

**¿Por qué usamos Repository?**
Para desacoplar Room del ViewModel. Permite cambiar la fuente de datos (por
ejemplo, agregar una API REST) sin romper la interfaz.

**¿Qué diferencia hay entre Flow y StateFlow?**
`Flow` es un flujo de datos; `StateFlow` es un flujo con estado que siempre
tiene un valor actual y es ideal para que Compose observe la UI.

**¿Dónde están las validaciones?**
En el ViewModel, dentro de un `StateFlow`. Así la vista no contiene reglas de
negocio y se pueden probar por separado.

**¿Cuáles son los dos recursos nativos del proyecto?**
Cámara (CameraX + ML Kit) y motor háptico/vibración (`Vibrator`).

**¿Qué pasa si falta KSP?**
Room no genera `AppDatabase_Impl` y la app falla al abrir la base con
`cannot find implementation for AppDatabase`.

---

## 7. Estado de integración

1. **KSP + `room3-compiler`:** habilitado (`ksp = 2.3.6`). Room genera
   `AppDatabase_Impl` y el CRUD funciona en ejecución.
2. **Rutas en `AppNavigation.kt`:** `inventarioNavGraph` conectado. Se mantiene
   `ProductoView` como acceso rápido y un botón abre el catálogo de Daniel.
3. **Ajustes:** `AjusteViewModel` persiste stock y movimiento; el resumen solo
   se muestra después de escribir en Room.
4. **Prueba en dispositivo:** verificada en emulador (seed, catálogo, detalle,
   conteo, historial, ajuste manual y ajuste desde escaneo simulado).

---

## 8. Errores comunes y cómo resolverlos

| Error | Causa | Solución |
| :--- | :--- | :--- |
| `cannot find implementation for AppDatabase` | Falta KSP / `room3-compiler` | Habilitar KSP y el compilador |
| `Room cannot verify the data integrity` | Cambió el esquema sin migración | Subir versión y usar `fallbackToDestructiveMigration()` |
| `NullPointerException` al guardar stock vacío | Conversión forzada con `!!` | Validar con `toIntOrNull()` y mostrar error |
| No se ven cambios en la lista | No se observa el `Flow` | Usar `collectAsStateWithLifecycle` o `collectAsState` |
| `jlink executable ... does not exist` | Gradle usa un JRE incompleto del IDE | Configurar `JAVA_HOME` a un JDK 17/21 completo |
| `Cannot find a parameter with this name: ...` en Compose | Nombre de parámetro distinto al de la versión | Revisar la firma del composable en esa versión |
