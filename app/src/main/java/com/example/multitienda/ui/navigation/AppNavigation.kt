package com.example.multitienda.ui.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.multitienda.ui.view.ProductoView

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var tiendaActual by remember { mutableStateOf("Bodega Pudahuel (Central)") }

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
                onLoginSuccess = { _ ->
                    navController.navigate("tiendas") {
                        popUpTo("login") { inclusive = true }
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

        // --- Rutas de Daniel y Angelo (Integradas) ---

        // Catálogo rápido (mantiene la vista ProductoView existente)
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

        // Catálogo completo, detalle, CRUD, conteo e historial de Daniel
        inventarioNavGraph(navController, tiendaActual)

        // Dashboard (Ruta para Angelo)
        composable("dashboard") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Dashboard y Alertas de Quiebre",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "(Módulo en desarrollo por Angelo)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Volver")
                }
            }
        }

        // Scanner (Ruta para Angelo)
        composable("scanner") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Lector de Códigos de Barra",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "(Módulo de CameraX en desarrollo por Angelo)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = {
                        // TODO(Angelo): al integrar ScannerScreen, usar:
                        // onCodigoEscaneado = { codigo ->
                        //     navController.navigate("ajuste/${Uri.encode(codigo)}") {
                        //         popUpTo("scanner") { inclusive = true }
                        //     }
                        // }
                        navController.navigate("ajuste/7801234567890") {
                            popUpTo("scanner") { inclusive = true }
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Simular Escaneo Código '7801234567890'")
                }
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}
