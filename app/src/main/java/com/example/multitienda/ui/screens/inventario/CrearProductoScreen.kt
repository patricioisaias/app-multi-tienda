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
fun CrearProductoScreen(
    tienda: String = "",
    viewModel: ProductoViewModel = viewModel(),
    onGuardado: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    val form by viewModel.form.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.limpiarFormulario()
    }

    LaunchedEffect(form.guardado) {
        if (form.guardado) {
            viewModel.consumirGuardado()
            onGuardado()
        }
    }

    ProductoFormulario(
        form = form,
        titulo = "Nuevo Producto",
        textoBoton = "Guardar Producto",
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
private fun CrearProductoPreview() {
    MultiTiendaTheme {
        ProductoFormulario(
            form = ProductoFormModel(),
            titulo = "Nuevo Producto",
            textoBoton = "Guardar Producto",
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
