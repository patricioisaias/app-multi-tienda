package com.example.multitienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitienda.data.repository.ProductoRepository
import com.example.multitienda.modelo.FormularioModel
import com.example.multitienda.modelo.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormularioViewModel(application: Application) : AndroidViewModel(application) {

    private val prodDao = AppDatabase.getDatabase(application).productoDAO()

    init {
        viewModelScope.launch {
            ProductoRepository(prodDao).sembrarSiVacio()
        }
    }

    val lista: StateFlow<List<Producto>> =
        prodDao.listarProductos().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        );

    // estados del formulario
    private val _uiState = MutableStateFlow(FormularioModel())
    val uiState: StateFlow<FormularioModel> = _uiState.asStateFlow()

    // funciones de Actualizacion de Datos
    fun onNombreChange(nuevoNombre: String) {
        _uiState.update { it.copy(nombre = nuevoNombre, errorNombre = null) }
    }

    fun onPrecioChange(nuevoPrecio: String) {
        _uiState.update { it.copy(precio = nuevoPrecio, errorPrecio = null) }
    }

    fun onStockChange(nuevoStock: String) {
        _uiState.update { it.copy(stock = nuevoStock, errorStock = null) }
    }

    fun limpiarDatos() {
        _uiState.value = FormularioModel()
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            prodDao.eliminarProducto(producto)
        }
    }

    fun guardar(): Boolean {
        val f = _uiState.value
        var errorNombre: String? = null
        var errorPrecio: String? = null
        var errorStock: String? = null

        var valido: Boolean = true
        // reglas dfe negocio
        if (f.nombre.length < 3 || f.nombre.isEmpty()) {
            errorNombre = "el nombre esta vacio"
            valido = false
        }
        if (f.precio.toDoubleOrNull() == null) {
            errorPrecio = "el valor del precio es nulo"
            valido = false
        }
        val stock = f.stock.toIntOrNull()
        if (stock == null || stock < 0) {
            errorStock = "el stock debe ser un numero mayor o igual a 0"
            valido = false
        }
        // validacion
        if (valido == false) {
            _uiState.update {
                it.copy(
                    errorNombre = errorNombre,
                    errorPrecio = errorPrecio,
                    errorStock = errorStock
                )
            }
            return false
        }
        // proceso de grabar
        val prod = Producto(
            id = f.id,
            nombre = f.nombre,
            precio = f.precio.toDoubleOrNull()!!,
            stock = stock!!
        )
        viewModelScope.launch {
            prodDao.agregarProducto(prod)
            limpiarDatos()
        }
        return valido
    }


}