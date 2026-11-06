package com.example.multitienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitienda.data.repository.MovimientoRepository
import com.example.multitienda.data.repository.ProductoRepository
import com.example.multitienda.modelo.Movimiento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AjusteEstado(
    val cargando: Boolean = false,
    val error: String? = null,
    val confirmado: Boolean = false
)

class AjusteViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val productoRepository = ProductoRepository(db.productoDAO())
    private val movimientoRepository = MovimientoRepository(db.movimientoDAO())

    private val _estado = MutableStateFlow(AjusteEstado())
    val estado: StateFlow<AjusteEstado> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            productoRepository.sembrarSiVacio()
        }
    }

    fun registrar(
        codigo: String,
        tipo: String,
        cantidad: Int,
        motivo: String,
        tienda: String
    ) {
        if (cantidad <= 0) {
            _estado.value = AjusteEstado(error = "Ingrese una cantidad válida mayor a 0")
            return
        }

        _estado.value = AjusteEstado(cargando = true)

        viewModelScope.launch {
            try {
                val producto = productoRepository.obtenerPorCodigo(codigo)
                if (producto == null) {
                    _estado.value = AjusteEstado(
                        error = "No existe un producto con el código ${codigo.trim()}"
                    )
                    return@launch
                }

                val (nuevoStock, diferencia) = when (tipo) {
                    "Entrada" -> (producto.stock + cantidad) to cantidad

                    "Salida" -> {
                        if (cantidad > producto.stock) {
                            _estado.value = AjusteEstado(
                                error = "Stock insuficiente: disponible ${producto.stock}"
                            )
                            return@launch
                        }
                        (producto.stock - cantidad) to -cantidad
                    }

                    "Ajuste / Conteo" -> cantidad to (cantidad - producto.stock)

                    else -> {
                        _estado.value = AjusteEstado(error = "Tipo de movimiento no válido")
                        return@launch
                    }
                }

                productoRepository.actualizarStock(producto.id, nuevoStock)
                movimientoRepository.registrar(
                    Movimiento(
                        codigoBarras = producto.codigoBarras,
                        tipo = tipo,
                        cantidad = diferencia,
                        motivo = motivo.trim().ifBlank { "Sin observación" },
                        tienda = tienda
                    )
                )

                _estado.value = AjusteEstado(confirmado = true)
            } catch (e: Exception) {
                _estado.value = AjusteEstado(
                    error = "No se pudo registrar el ajuste: ${e.message ?: "error desconocido"}"
                )
            }
        }
    }

    fun consumirConfirmado() {
        _estado.update { it.copy(confirmado = false) }
    }

    fun limpiarError() {
        _estado.update { it.copy(error = null) }
    }
}
