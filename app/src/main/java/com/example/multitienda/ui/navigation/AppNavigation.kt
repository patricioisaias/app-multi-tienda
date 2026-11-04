package com.example.multitienda.ui.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.multitienda.ui.screens.gerencial.AlertasQuiebreScreen
import com.example.multitienda.ui.screens.gerencial.ConfigHardwareScreen
import com.example.multitienda.ui.screens.gerencial.DashboardGerencialScreen
import com.example.multitienda.ui.screens.gerencial.DeclararReposicionScreen
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
import com.example.multitienda.ui.view.ProductoView

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var tiendaActual by remember { mutableStateOf("Bodega Pudahuel (Central)") }
    var rolActual by remember { mutableStateOf("Operador") }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 1. Splash Screen
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // 2. Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = { rol ->
                    rolActual = rol
                    if (rol == "Gerencial") {
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("tiendas") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }

        // 3. Selección de Tienda
        composable("tiendas") {
            SeleccionTiendaScreen(
                onTiendaSeleccionada = { tienda ->
                    tiendaActual = tienda
                    navController.navigate("menu_operador") {
                        popUpTo("tiendas") { inclusive = true }
                    }
                }
            )
        }

        // 4. Menú Principal del Operador
        composable("menu_operador") {
            MenuOperadorScreen(
                tiendaActiva = tiendaActual,
                onNavigateToScanner = {
                    navController.navigate("scanner")
                },
                onNavigateToAjuste = {
                    navController.navigate("ajuste/manual")
                },
                onNavigateToCatalogo = {
                    navController.navigate("catalogo")
                },
                onNavigateToDashboard = {
                    navController.navigate("dashboard")
                },
                onNavigateToPerfil = {
                    navController.navigate("perfil")
                },
                onCambiarTienda = {
                    navController.navigate("tiendas")
                }
            )
        }

        // 5. Formulario de Ajuste
        composable(
            route = "ajuste/{codigo}",
            arguments = listOf(navArgument("codigo") { type = NavType.StringType })
        ) { backStackEntry ->
            val codigoArg = backStackEntry.arguments?.getString("codigo") ?: ""
            val codigoLimpio = if (codigoArg == "manual") "" else codigoArg

            FormularioAjusteScreen(
                codigoInicial = codigoLimpio,
                tienda = tiendaActual,
                onAbrirScanner = {
                    navController.navigate("scanner")
                },
                onAjusteConfirmado = { codigo, tipo, cantidad, motivo ->
                    val motivoSeguro = if (motivo.isBlank()) "SinObservacion" else motivo
                    val codigoRuta = Uri.encode(codigo)
                    val tipoRuta = Uri.encode(tipo)
                    val motivoRuta = Uri.encode(motivoSeguro)
                    navController.navigate("resumen/$codigoRuta/$tipoRuta/$cantidad/$motivoRuta")
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // 6. Resumen de Movimiento Confirmado
        composable(
            route = "resumen/{codigo}/{tipo}/{cantidad}/{motivo}",
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
                    navController.navigate("ajuste/manual") {
                        popUpTo("menu_operador")
                    }
                },
                onVolverMenu = {
                    navController.navigate("menu_operador") {
                        popUpTo("menu_operador") { inclusive = true }
                    }
                }
            )
        }

        // 7. Perfil de Usuario
        composable("perfil") {
            PerfilUsuarioScreen(
                tienda = tiendaActual,
                onCambiarTienda = {
                    navController.navigate("tiendas")
                },
                onCerrarSesion = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // --- Catálogo y Persistencia Room (Módulo Daniel: Pantallas 8-14) ---

        // Catálogo rápido (mantiene la vista ProductoView)
        composable("catalogo") {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Volver al Menú")
                    }
                    Button(onClick = { navController.navigate(InventarioRoutes.CATALOGO) }) {
                        Text("Catálogo de inventario (Room)")
                    }
                }
                ProductoView()
            }
        }

        // Grafo de inventario con Pantallas 8 a 14 de Daniel
        inventarioNavGraph(navController, tiendaActual)

        // --- Recursos Nativos, Scanner y Gerencial (Módulo Angelo: Pantallas 15-21) ---

        // Pantalla 15: Scanner con CameraX y Google ML Kit
        composable("scanner") {
            ScannerScreen(
                onCodigoDetectado = { codigo ->
                    val codigoEncoded = Uri.encode(codigo)
                    navController.navigate("scan_result/$codigoEncoded") {
                        popUpTo("scanner") { inclusive = true }
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
        composable("dashboard") {
            DashboardGerencialScreen(
                rolActual = rolActual,
                onNavigateToAlertas = {
                    navController.navigate("alertas")
                },
                onNavigateToConfigHardware = {
                    navController.navigate("config_hardware")
                },
                onCerrarSesion = {
                    navController.navigate("login") {
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
