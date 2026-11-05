package com.example.multitienda.ui.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.multitienda.ui.screens.inventario.InventarioRoutes
import com.example.multitienda.ui.screens.inventario.inventarioNavGraph
import com.example.multitienda.ui.screens.operador.FormularioAjusteScreen
import com.example.multitienda.ui.screens.operador.LoginScreen
import com.example.multitienda.ui.screens.operador.MenuOperadorScreen
import com.example.multitienda.ui.screens.operador.PerfilUsuarioScreen
import com.example.multitienda.ui.screens.operador.ResumenMovimientoScreen
import com.example.multitienda.ui.screens.operador.SeleccionTiendaScreen
import com.example.multitienda.ui.screens.operador.SplashScreen
import com.example.multitienda.ui.screens.scanner.EntradaManualCodigoScreen
import com.example.multitienda.ui.screens.scanner.ResultadoScannerScreen
import com.example.multitienda.ui.screens.scanner.ScannerScreen
import com.example.multitienda.ui.screens.gerencial.AlertasQuiebreScreen
import com.example.multitienda.ui.screens.gerencial.ConfigHardwareScreen
import com.example.multitienda.ui.screens.gerencial.DashboardGerencialScreen
import com.example.multitienda.ui.screens.gerencial.DeclararReposicionScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var tiendaActual by rememberSaveable { mutableStateOf("Bodega Pudahuel (Central)") }
    var usuarioActual by rememberSaveable { mutableStateOf("Usuario") }
    var rolActual by rememberSaveable { mutableStateOf("Operador") }

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
                onLoginSuccess = { nombre, puesto, rol ->
                    usuarioActual = nombre
                    rolActual = puesto
                    if (puesto == "Gerencial") {
                        // Gerencial va directo al Dashboard (sin seleccionar sucursal)
                        navController.navigate(AppRoutes.DASHBOARD)
                    } else {
                        // Operador / Bodega va directo al Menú con sucursal predeterminada
                        tiendaActual = "Bodega Pudahuel (Central)"
                        navController.navigate(AppRoutes.MENU)
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
                }
            )
        }

        // 4. Menú Principal del Operador
        composable(AppRoutes.MENU) {
            MenuOperadorScreen(
                usuario = usuarioActual,
                rol = rolActual,
                tiendaActiva = tiendaActual,
                onNavigateToScanner = {
                    navController.navigate(AppRoutes.SCANNER)
                },
                onNavigateToAjuste = {
                    navController.navigate(AppRoutes.ajuste("manual"))
                },
                onNavigateToCatalogo = {
                    navController.navigate(InventarioRoutes.CATALOGO)
                },
                onNavigateToDashboard = {
                    navController.navigate(AppRoutes.DASHBOARD)
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
                    val codigoRuta = Uri.encode(codigo)
                    val tipoRuta = Uri.encode(tipo)
                    val motivoRuta = Uri.encode(motivoSeguro)
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
            PerfilUsuarioScreen(
                usuario = usuarioActual,
                rol = rolActual,
                tienda = tiendaActual,
                onCambiarTienda = {
                    navController.navigate(AppRoutes.TIENDAS)
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

        // --- Catálogo y Persistencia Room (Módulo Daniel: Pantallas 8-14) ---
        inventarioNavGraph(navController, tiendaActual)

        // --- Recursos Nativos, Scanner y Gerencial (Módulo Angelo: Pantallas 15-21) ---

        // Pantalla 15: Scanner con CameraX y Google ML Kit
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

        // Pantalla 16: Resultado de Escaneo con Vibración Háptica y Consulta Room
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
                    navController.navigate("menu_operador") {
                        popUpTo("menu_operador") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla 17: Entrada Manual de Código de Barras
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

        // Pantalla 18: Dashboard Gerencial (Exclusivo Gerencial)
        composable(AppRoutes.DASHBOARD) {
            DashboardGerencialScreen(
                usuario = usuarioActual,
                rolActual = rolActual,
                onNavigateToAlertas = {
                    navController.navigate("alertas")
                },
                onNavigateToConfigHardware = {
                    navController.navigate("config_hardware")
                },
                onCerrarSesion = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla 19: Alertas de Quiebre de Stock
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

        // Pantalla 20: Declarar Reposición
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

        // Pantalla 21: Diagnóstico de Hardware y Prueba de Recursos Nativos
        composable("config_hardware") {
            ConfigHardwareScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}
