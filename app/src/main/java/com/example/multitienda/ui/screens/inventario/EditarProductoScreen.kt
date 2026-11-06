package com.example.multitienda.ui.screens.inventario

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.ui.components.ProductoFormulario
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
