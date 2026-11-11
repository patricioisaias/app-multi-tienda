package com.example.multitienda.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.multitienda.data.repository.TiendaRepository
import com.example.multitienda.ui.screens.auth.LoginScreen
import com.example.multitienda.ui.screens.auth.SplashScreen
import com.example.multitienda.ui.screens.gerencial.AdminTiendasScreen
import com.example.multitienda.ui.screens.gerencial.AlertasQuiebreScreen
import com.example.multitienda.ui.screens.gerencial.DashboardGerencialScreen
import com.example.multitienda.ui.screens.gerencial.DeclararReposicionScreen
import com.example.multitienda.ui.screens.gerencial.ExportarReportesScreen
import com.example.multitienda.ui.screens.home.MenuPrincipalScreen
import com.example.multitienda.ui.screens.home.PerfilUsuarioScreen
import com.example.multitienda.ui.screens.home.SeleccionTiendaScreen
import com.example.multitienda.ui.screens.inventario.DiferenciasStockScreen
import com.example.multitienda.ui.screens.operador.FormularioAjusteScreen
import com.example.multitienda.ui.screens.operador.ResumenMovimientoScreen
import com.example.multitienda.ui.screens.scanner.EntradaManualCodigoScreen
import com.example.multitienda.ui.screens.scanner.ResultadoScannerScreen
import com.example.multitienda.ui.screens.scanner.ScannerScreen

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    var tiendaActual by rememberSaveable { mutableStateOf("Bodega Pudahuel (Central)") }
    var usuarioActual by rememberSaveable { mutableStateOf("Usuario") }
    var rolActual by rememberSaveable { mutableStateOf("Operador") }

    val tiendas by TiendaRepository.tiendas.collectAsState()

    // Cargar estado persistido de tiendas habilitadas al iniciar
    LaunchedEffect(Unit) {
        TiendaRepository.cargarTiendas(context)
    }

    // Si la tienda actual fue suspendida por el perfil Gerencial, reasignar a la primera tienda habilitada
    LaunchedEffect(tiendas, rolActual) {
        val esGerencial = rolActual.contains("Gerencial", ignoreCase = true) || rolActual.contains("Gerente", ignoreCase = true)
        if (!esGerencial) {
            tiendaActual = TiendaRepository.obtenerTiendaActivaValida(context, tiendaActual)
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {
        // 1. Splash Screen
        composable(AppRoutes.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // 2. Login
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { nombre, puesto, _ ->
                    usuarioActual = nombre
                    rolActual = puesto
                    val esGerencial = puesto.contains("Gerencial", ignoreCase = true) || puesto.contains("Gerente", ignoreCase = true)
                    tiendaActual = if (esGerencial) {
                        "Red Comercial Multi-Tienda"
                    } else {
                        TiendaRepository.obtenerTiendaActivaValida(context, tiendaActual)
                    }
                    navController.navigate(AppRoutes.MENU) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // 3. Selección de Tienda
        composable(AppRoutes.TIENDAS) {
            SeleccionTiendaScreen(
                onTiendaSeleccionada = { tienda ->
                    tiendaActual = tienda
                    navController.navigate(AppRoutes.MENU) {
                        popUpTo(AppRoutes.TIENDAS) { inclusive = true }
                    }
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // 4. Menú Principal Adaptativo por Perfil
        composable(AppRoutes.MENU) {
            MenuPrincipalScreen(
                usuario = usuarioActual,
                rol = rolActual,
                tiendaActiva = tiendaActual,
                onNavigateToScanner = {
                    navController.navigate(AppRoutes.SCANNER)
                },
                onNavigateToAjuste = {
                    navController.navigate(AppRoutes.ajuste("manual"))
                },
                onNavigateToConteo = {
                    navController.navigate(InventarioRoutes.CATALOGO)
                },
                onNavigateToCrearProducto = {
                    navController.navigate(InventarioRoutes.CREAR)
                },
                onNavigateToCatalogo = {
                    navController.navigate(InventarioRoutes.CATALOGO)
                },
                onNavigateToDiferencias = {
                    navController.navigate(AppRoutes.DIFERENCIAS_STOCK)
                },
                onNavigateToAlertas = {
                    navController.navigate("alertas")
                },
                onNavigateToDashboard = {
                    navController.navigate(AppRoutes.DASHBOARD)
                },
                onNavigateToHistorial = {
                    navController.navigate(InventarioRoutes.HISTORIAL)
                },
                onNavigateToAdminTiendas = {
                    navController.navigate(AppRoutes.ADMIN_TIENDAS)
                },
                onNavigateToExportar = {
                    navController.navigate(AppRoutes.EXPORTAR_REPORTES)
                },
                onNavigateToPerfil = {
                    navController.navigate(AppRoutes.PERFIL)
                },
                onCambiarTienda = {
                    navController.navigate(AppRoutes.TIENDAS)
                }
            )
        }

        // 5. Formulario de Ajuste
        composable(
            route = AppRoutes.AJUSTE,
            arguments = listOf(navArgument("codigo") { type = NavType.StringType })
        ) { backStackEntry ->
            val codigoArg = backStackEntry.arguments?.getString("codigo") ?: ""
            val codigoLimpio = if (codigoArg == "manual") "" else codigoArg

            FormularioAjusteScreen(
                codigoInicial = codigoLimpio,
                tienda = tiendaActual,
                onAbrirScanner = {
                    navController.navigate(AppRoutes.SCANNER)
                },
                onAjusteConfirmado = { codigo, tipo, cantidad, motivo ->
                    val motivoSeguro = if (motivo.isBlank()) "SinObservacion" else motivo
                    navController.navigate(
                        AppRoutes.resumen(codigo, tipo, cantidad, motivoSeguro)
                    )
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // 6. Resumen de Movimiento Confirmado
        composable(
            route = AppRoutes.RESUMEN,
            arguments = listOf(
                navArgument("codigo") { type = NavType.StringType },
                navArgument("tipo") { type = NavType.StringType },
                navArgument("cantidad") { type = NavType.IntType },
                navArgument("motivo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val codigo = Uri.decode(backStackEntry.arguments?.getString("codigo") ?: "")
            val tipo = Uri.decode(backStackEntry.arguments?.getString("tipo") ?: "")
            val cantidad = backStackEntry.arguments?.getInt("cantidad") ?: 0
            val motivoRaw = Uri.decode(backStackEntry.arguments?.getString("motivo") ?: "")
            val motivo = if (motivoRaw == "SinObservacion") "" else motivoRaw

            ResumenMovimientoScreen(
                codigo = codigo,
                tipo = tipo,
                cantidad = cantidad,
                motivo = motivo,
                tienda = tiendaActual,
                onNuevoAjuste = {
                    navController.navigate(AppRoutes.ajuste("manual")) {
                        popUpTo(AppRoutes.MENU)
                    }
                },
                onVolverMenu = {
                    navController.navigate(AppRoutes.MENU) {
                        popUpTo(AppRoutes.MENU) { inclusive = true }
                    }
                }
            )
        }

        // 7. Perfil de Usuario
        composable(AppRoutes.PERFIL) {
            val esGerencial = rolActual.contains("Gerencial", ignoreCase = true) || rolActual.contains("Gerente", ignoreCase = true)
            PerfilUsuarioScreen(
                usuario = usuarioActual,
                rol = rolActual,
                tienda = tiendaActual,
                onCambiarTienda = {
                    if (esGerencial) {
                        navController.navigate(AppRoutes.ADMIN_TIENDAS)
                    } else {
                        navController.navigate(AppRoutes.TIENDAS)
                    }
                },
                onCerrarSesion = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // --- Catálogo y Persistencia Room ---
        inventarioNavGraph(
            navController = navController,
            tienda = tiendaActual,
            rol = rolActual
        )

        // --- Recursos Nativos, Scanner y Gerencial ---

        // Scanner con CameraX y Google ML Kit
        composable(AppRoutes.SCANNER) {
            ScannerScreen(
                onCodigoDetectado = { codigo ->
                    val codigoEncoded = Uri.encode(codigo)
                    navController.navigate("scan_result/$codigoEncoded") {
                        popUpTo(AppRoutes.SCANNER) { inclusive = true }
                    }
                },
                onNavigateToManual = {
                    navController.navigate("scanner_manual")
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // Resultado de Escaneo con Vibración Háptica y Consulta Room
        composable(
            route = "scan_result/{codigo}",
            arguments = listOf(navArgument("codigo") { type = NavType.StringType })
        ) { backStackEntry ->
            val codigo = Uri.decode(backStackEntry.arguments?.getString("codigo") ?: "")
            ResultadoScannerScreen(
                codigo = codigo,
                onAjustarStock = { cod ->
                    val codEncoded = Uri.encode(cod)
                    navController.navigate("ajuste/$codEncoded")
                },
                onEscanearOtro = {
                    navController.navigate("scanner") {
                        popUpTo("scan_result/{codigo}") { inclusive = true }
                    }
                },
                onVolverMenu = {
                    navController.navigate(AppRoutes.MENU) {
                        popUpTo(AppRoutes.MENU) { inclusive = true }
                    }
                }
            )
        }

        // Entrada Manual de Código de Barras
        composable("scanner_manual") {
            EntradaManualCodigoScreen(
                onCodigoIngresado = { codigo ->
                    val codigoEncoded = Uri.encode(codigo)
                    navController.navigate("scan_result/$codigoEncoded") {
                        popUpTo("scanner_manual") { inclusive = true }
                    }
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // Dashboard Gerencial
        composable(AppRoutes.DASHBOARD) {
            DashboardGerencialScreen(
                usuario = usuarioActual,
                rolActual = rolActual,
                onNavigateToAlertas = {
                    navController.navigate("alertas")
                },
                onVolver = {
                    navController.popBackStack()
                },
                onCerrarSesion = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Alertas de Quiebre de Stock
        composable("alertas") {
            AlertasQuiebreScreen(
                onDeclararReposicion = { id ->
                    navController.navigate("reposicion/$id")
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // Declarar Reposición
        composable(
            route = "reposicion/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val prodId = backStackEntry.arguments?.getInt("id") ?: 0
            DeclararReposicionScreen(
                productoId = prodId,
                tiendaActiva = tiendaActual,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // Administración de Tiendas (Gerencial)
        composable(AppRoutes.ADMIN_TIENDAS) {
            AdminTiendasScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // Exportar Reportes (Gerencial)
        composable(AppRoutes.EXPORTAR_REPORTES) {
            ExportarReportesScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // Control de Diferencias de Stock (Encargado de Bodega)
        composable(AppRoutes.DIFERENCIAS_STOCK) {
            DiferenciasStockScreen(
                onRectificar = { id ->
                    navController.navigate(InventarioRoutes.conteo(id))
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}
