package com.example.multitienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitienda.data.local.AppDatabase
import com.example.multitienda.data.repository.MovimientoRepository
import com.example.multitienda.data.repository.ProductoRepository
import com.example.multitienda.modelo.Movimiento
import com.example.multitienda.modelo.Producto
import com.example.multitienda.modelo.ProductoFormModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductoViewModel(application: Application) : AndroidViewModel(application) {

    private val productoRepository = ProductoRepository(
        AppDatabase.getDatabase(application).productoDAO()
    )
    private val movimientoRepository = MovimientoRepository(
        AppDatabase.getDatabase(application).movimientoDAO()
    )

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    val productos: StateFlow<List<Producto>> =
        combine(productoRepository.productos, _busqueda) { lista, texto ->
            if (texto.isBlank()) lista
            else lista.filter { it.nombre.contains(texto, ignoreCase = true) }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val productosBajoStock: StateFlow<List<Producto>> =
        productoRepository.productosBajoStock().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _form = MutableStateFlow(ProductoFormModel())
    val form: StateFlow<ProductoFormModel> = _form.asStateFlow()

    private val _detalle = MutableStateFlow<Producto?>(null)
    val detalle: StateFlow<Producto?> = _detalle.asStateFlow()

    private val _conteoError = MutableStateFlow<String?>(null)
    val conteoError: StateFlow<String?> = _conteoError.asStateFlow()

    private val _conteoGuardado = MutableStateFlow(false)
    val conteoGuardado: StateFlow<Boolean> = _conteoGuardado.asStateFlow()

    init {
        viewModelScope.launch {
            productoRepository.sembrarSiVacio()
        }
    }

    fun onBusquedaChange(texto: String) {
        _busqueda.value = texto
    }

    fun onCodigoBarrasChange(valor: String) {
        _form.update { it.copy(codigoBarras = valor, errorCodigoBarras = null) }
    }

    fun onNombreChange(valor: String) {
        _form.update { it.copy(nombre = valor, errorNombre = null) }
    }

    fun onCategoriaChange(valor: String) {
        _form.update { it.copy(categoria = valor, errorCategoria = null) }
    }

    fun onPrecioChange(valor: String) {
        _form.update { it.copy(precio = valor, errorPrecio = null) }
    }

    fun onStockChange(valor: String) {
        _form.update { it.copy(stock = valor, errorStock = null) }
    }

    fun onStockMinimoChange(valor: String) {
        _form.update { it.copy(stockMinimo = valor, errorStockMinimo = null) }
    }

    fun cargarParaEditar(id: Int) {
        viewModelScope.launch {
            val producto = productoRepository.obtenerPorId(id) ?: return@launch
            _form.value = ProductoFormModel(
                id = producto.id,
                codigoBarras = producto.codigoBarras,
                nombre = producto.nombre,
                categoria = producto.categoria,
                precio = producto.precio.toString(),
                stock = producto.stock.toString(),
                stockMinimo = producto.stockMinimo.toString()
            )
        }
    }

    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            _detalle.value = productoRepository.obtenerPorId(id)
        }
    }

    fun guardar(tienda: String = "") {
        val f = _form.value
        val codigo = f.codigoBarras.trim()
        val nombre = f.nombre.trim()
        val categoria = f.categoria.trim().ifBlank { "General" }
        val precio = f.precio.trim().replace(",", ".").toDoubleOrNull()
        val stock = f.stock.trim().toIntOrNull()
        val stockMinimo = f.stockMinimo.trim().toIntOrNull()

        val errorCodigo = if (codigo.isBlank()) "El código es obligatorio" else null
        val errorNombre = if (nombre.length < 3) "Mínimo 3 caracteres" else null
        val errorPrecio = if (precio == null || precio < 0) "Precio inválido" else null
        val errorStock = if (stock == null || stock < 0) "Stock inválido" else null
        val errorStockMinimo =
            if (stockMinimo == null || stockMinimo < 0) "Stock mínimo inválido" else null

        if (listOf(
                errorCodigo,
                errorNombre,
                errorPrecio,
                errorStock,
                errorStockMinimo
            ).any { it != null }
        ) {
            _form.update {
                it.copy(
                    errorCodigoBarras = errorCodigo,
                    errorNombre = errorNombre,
                    errorPrecio = errorPrecio,
                    errorStock = errorStock,
                    errorStockMinimo = errorStockMinimo
                )
            }
            return
        }

        _form.update { it.copy(guardando = true) }

        viewModelScope.launch {
            val existente = productoRepository.obtenerPorCodigo(codigo)
            if (existente != null && existente.id != f.id) {
                _form.update {
                    it.copy(
                        guardando = false,
                        errorCodigoBarras = "Ya existe un producto con este código"
                    )
                }
                return@launch
            }

            val anterior = if (f.esEdicion) productoRepository.obtenerPorId(f.id) else null
            val producto = Producto(
                id = f.id,
                codigoBarras = codigo,
                nombre = nombre,
                categoria = categoria,
                precio = precio!!,
                stock = stock!!,
                stockMinimo = stockMinimo!!
            )

            if (f.esEdicion) {
                productoRepository.actualizar(producto)
                val diferencia = producto.stock - (anterior?.stock ?: producto.stock)
                if (diferencia != 0) {
                    registrarMovimiento(
                        codigo = producto.codigoBarras,
                        tipo = if (diferencia > 0) "Entrada" else "Salida",
                        cantidad = diferencia,
                        motivo = "Edición de producto",
                        tienda = tienda
                    )
                }
            } else {
                productoRepository.insertar(producto)
                if (producto.stock > 0) {
                    registrarMovimiento(
                        codigo = producto.codigoBarras,
                        tipo = "Entrada",
                        cantidad = producto.stock,
                        motivo = "Creación de producto",
                        tienda = tienda
                    )
                }
            }

            _form.update { it.copy(guardando = false, guardado = true) }
        }
    }

    fun eliminar(producto: Producto) {
        viewModelScope.launch {
            productoRepository.eliminar(producto)
        }
    }

    fun registrarConteo(id: Int, nuevoStockTexto: String, tienda: String, motivo: String) {
        val nuevoStock = nuevoStockTexto.trim().toIntOrNull()
        if (nuevoStock == null || nuevoStock < 0) {
            _conteoError.value = "Ingrese un conteo válido (0 o más)"
            return
        }
        _conteoError.value = null

        viewModelScope.launch {
            val producto = productoRepository.obtenerPorId(id) ?: return@launch
            val diferencia = nuevoStock - producto.stock
            productoRepository.actualizarStock(producto.id, nuevoStock)
            registrarMovimiento(
                codigo = producto.codigoBarras,
                tipo = "Conteo",
                cantidad = diferencia,
                motivo = motivo.ifBlank { "Conteo físico" },
                tienda = tienda
            )
            _detalle.value = producto.copy(stock = nuevoStock)
            _conteoGuardado.value = true
        }
    }

    fun consumirConteoGuardado() {
        _conteoGuardado.value = false
    }

    fun limpiarErrorConteo() {
        _conteoError.value = null
    }

    private suspend fun registrarMovimiento(
        codigo: String,
        tipo: String,
        cantidad: Int,
        motivo: String,
        tienda: String
    ) {
        movimientoRepository.registrar(
            Movimiento(
                codigoBarras = codigo,
                tipo = tipo,
                cantidad = cantidad,
                motivo = motivo,
                tienda = tienda
            )
        )
    }

    fun consumirGuardado() {
        _form.update { it.copy(guardado = false) }
    }

    fun limpiarFormulario() {
        _form.value = ProductoFormModel()
    }
}
