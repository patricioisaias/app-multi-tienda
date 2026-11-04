package com.example.multitienda.ui.screens.inventario

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.modelo.ProductoFormModel
import com.example.multitienda.ui.theme.MultiTiendaTheme
import com.example.multitienda.viewmodel.ProductoViewModel

@Composable
fun EditarProductoScreen(
    productoId: Int,
    tienda: String = "",
    viewModel: ProductoViewModel = viewModel(),
    onGuardado: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    val form by viewModel.form.collectAsState()

    LaunchedEffect(productoId) {
        viewModel.cargarParaEditar(productoId)
    }

    LaunchedEffect(form.guardado) {
        if (form.guardado) {
            viewModel.consumirGuardado()
            onGuardado()
        }
    }

    ProductoFormulario(
        form = form,
        titulo = "Editar Producto",
        textoBoton = "Actualizar Producto",
        onCodigoBarrasChange = viewModel::onCodigoBarrasChange,
        onNombreChange = viewModel::onNombreChange,
        onCategoriaChange = viewModel::onCategoriaChange,
        onPrecioChange = viewModel::onPrecioChange,
        onStockChange = viewModel::onStockChange,
        onStockMinimoChange = viewModel::onStockMinimoChange,
        onGuardar = { viewModel.guardar(tienda) },
        onVolver = onVolver
    )
}

@Preview(showBackground = true)
@Composable
private fun EditarProductoPreview() {
    MultiTiendaTheme {
        ProductoFormulario(
            form = ProductoFormModel(
                id = 1,
                codigoBarras = "7801234567890",
                nombre = "Leche Entera 1L",
                categoria = "Lácteos",
                precio = "1290",
                stock = "24",
                stockMinimo = "10"
            ),
            titulo = "Editar Producto",
            textoBoton = "Actualizar Producto",
            onCodigoBarrasChange = {},
            onNombreChange = {},
            onCategoriaChange = {},
            onPrecioChange = {},
            onStockChange = {},
            onStockMinimoChange = {},
            onGuardar = {},
            onVolver = {}
        )
    }
}
