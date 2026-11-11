package com.example.multitienda.ui.screens.inventario

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.ui.components.ProductoFormulario
import com.example.multitienda.ui.screens.scanner.ScannerScreen
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
    var mostrarEscaner by remember { mutableStateOf(false) }

    LaunchedEffect(productoId) {
        viewModel.cargarParaEditar(productoId)
    }

    LaunchedEffect(form.guardado) {
        if (form.guardado) {
            viewModel.consumirGuardado()
            onGuardado()
        }
    }

    if (mostrarEscaner) {
        ScannerScreen(
            onCodigoDetectado = { codigo ->
                viewModel.onCodigoBarrasChange(codigo)
                mostrarEscaner = false
            },
            onNavigateToManual = { mostrarEscaner = false },
            onVolver = { mostrarEscaner = false }
        )
        return
    }

    ProductoFormulario(
        form = form,
        onCodigoBarrasChange = viewModel::onCodigoBarrasChange,
        onNombreChange = viewModel::onNombreChange,
        onCategoriaChange = viewModel::onCategoriaChange,
        onPrecioChange = viewModel::onPrecioChange,
        onStockChange = viewModel::onStockChange,
        onStockMinimoChange = viewModel::onStockMinimoChange,
        onEscanearCodigo = { mostrarEscaner = true },
        onGuardar = { viewModel.guardar(tienda) },
        onVolver = onVolver
    )
}
