package com.example.multitienda.ui.screens.inventario

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

object InventarioRoutes {
    const val CATALOGO = "inventario/catalogo"
    const val CREAR = "inventario/crear"
    const val HISTORIAL = "inventario/historial"
    const val DETALLE = "inventario/detalle/{id}"
    const val EDITAR = "inventario/editar/{id}"
    const val ELIMINAR = "inventario/eliminar/{id}"
    const val CONTEO = "inventario/conteo/{id}"

    fun detalle(id: Int) = "inventario/detalle/$id"
    fun editar(id: Int) = "inventario/editar/$id"
    fun eliminar(id: Int) = "inventario/eliminar/$id"
    fun conteo(id: Int) = "inventario/conteo/$id"
}

fun NavGraphBuilder.inventarioNavGraph(
    navController: NavHostController,
    tienda: String = ""
) {
    composable(InventarioRoutes.CATALOGO) {
        CatalogoProductosScreen(
            onProductoClick = { id ->
                navController.navigate(InventarioRoutes.detalle(id))
            },
            onCrearProducto = {
                navController.navigate(InventarioRoutes.CREAR)
            },
            onVerHistorial = {
                navController.navigate(InventarioRoutes.HISTORIAL)
            },
            onVolver = { navController.popBackStack() }
        )
    }

    composable(
        route = InventarioRoutes.DETALLE,
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getInt("id") ?: 0
        DetalleProductoScreen(
            productoId = id,
            onEditar = { navController.navigate(InventarioRoutes.editar(it)) },
            onEliminar = { navController.navigate(InventarioRoutes.eliminar(it)) },
            onConteo = { navController.navigate(InventarioRoutes.conteo(it)) },
            onVolver = { navController.popBackStack() }
        )
    }

    composable(InventarioRoutes.CREAR) {
        CrearProductoScreen(
            tienda = tienda,
            onGuardado = { navController.popBackStack() },
            onVolver = { navController.popBackStack() }
        )
    }

    composable(
        route = InventarioRoutes.EDITAR,
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getInt("id") ?: 0
        EditarProductoScreen(
            productoId = id,
            tienda = tienda,
            onGuardado = { navController.popBackStack() },
            onVolver = { navController.popBackStack() }
        )
    }

    composable(
        route = InventarioRoutes.ELIMINAR,
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getInt("id") ?: 0
        ConfirmarEliminarScreen(
            productoId = id,
            onEliminado = {
                navController.popBackStack(InventarioRoutes.CATALOGO, inclusive = false)
            },
            onVolver = { navController.popBackStack() }
        )
    }

    composable(
        route = InventarioRoutes.CONTEO,
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getInt("id") ?: 0
        ConteoFisicoScreen(
            productoId = id,
            tienda = tienda,
            onRegistrado = { navController.popBackStack() },
            onVolver = { navController.popBackStack() }
        )
    }

    composable(InventarioRoutes.HISTORIAL) {
        HistorialMovimientosScreen(
            onVolver = { navController.popBackStack() }
        )
    }
}
