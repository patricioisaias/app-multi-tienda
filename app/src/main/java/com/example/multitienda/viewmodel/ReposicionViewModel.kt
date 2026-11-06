package com.example.multitienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitienda.data.local.AppDatabase
import com.example.multitienda.data.repository.MovimientoRepository
import com.example.multitienda.data.repository.ProductoRepository
import com.example.multitienda.modelo.Movimiento
import com.example.multitienda.modelo.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReposicionUiState(
    val producto: Producto? = null,
    val cantidad: String = "10",
    val motivo: String = "Reposición urgente por quiebre de stock",
    val errorCantidad: String? = null,
    val cargando: Boolean = false,
    val confirmada: Boolean = false,
    val mensajeError: String? = null
)

class ReposicionViewModel(application: Application) : AndroidViewModel(application) {

    private val productoRepo = ProductoRepository(AppDatabase.getDatabase(application).productoDAO())
    private val movimientoRepo = MovimientoRepository(AppDatabase.getDatabase(application).movimientoDAO())

    private val _estado = MutableStateFlow(ReposicionUiState())
    val estado: StateFlow<ReposicionUiState> = _estado.asStateFlow()

    fun cargarProducto(id: Int) {
        viewModelScope.launch {
            val prod = productoRepo.obtenerPorId(id)
            _estado.update { it.copy(producto = prod) }
        }
    }

    fun onCantidadChange(nuevaCantidad: String) {
        _estado.update { it.copy(cantidad = nuevaCantidad, errorCantidad = null, mensajeError = null) }
    }

    fun onMotivoChange(nuevoMotivo: String) {
        _estado.update { it.copy(motivo = nuevoMotivo) }
    }

    fun confirmarReposicion(tienda: String, onExito: () -> Unit) {
        val prod = _estado.value.producto ?: return
        val cant = _estado.value.cantidad.toIntOrNull()
        if (cant == null || cant <= 0) {
            _estado.update { it.copy(errorCantidad = "Ingrese una cantidad válida mayor a 0") }
            return
        }

        _estado.update { it.copy(cargando = true) }
        viewModelScope.launch {
            try {
                // Registrar movimiento de reposición
                movimientoRepo.registrar(
                    Movimiento(
                        codigoBarras = prod.codigoBarras,
                        tipo = "Entrada / Reposición",
                        cantidad = cant,
                        motivo = _estado.value.motivo.trim(),
                        tienda = tienda.ifBlank { "Bodega Pudahuel (Central)" },
                        fechaMillis = System.currentTimeMillis()
                    )
                )
                // Actualizar stock del producto
                val nuevoStock = prod.stock + cant
                productoRepo.actualizarStock(prod.id, nuevoStock)

                _estado.update { it.copy(cargando = false, confirmada = true) }
                onExito()
            } catch (e: Exception) {
                _estado.update { it.copy(cargando = false, mensajeError = "Error al registrar reposición: ${e.message}") }
            }
        }
    }

    fun resetEstado() {
        _estado.update { it.copy(confirmada = false, cargando = false, errorCantidad = null) }
    }
}
