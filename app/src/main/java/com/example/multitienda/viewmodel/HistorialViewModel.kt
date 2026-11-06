package com.example.multitienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitienda.data.local.AppDatabase
import com.example.multitienda.data.repository.MovimientoRepository
import com.example.multitienda.modelo.Movimiento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HistorialViewModel(application: Application) : AndroidViewModel(application) {

    private val movimientoRepository = MovimientoRepository(
        AppDatabase.getDatabase(application).movimientoDAO()
    )

    private val _filtro = MutableStateFlow("")
    val filtro: StateFlow<String> = _filtro.asStateFlow()

    val movimientos: StateFlow<List<Movimiento>> =
        combine(movimientoRepository.movimientos, _filtro) { lista, texto ->
            if (texto.isBlank()) lista
            else lista.filter {
                it.codigoBarras.contains(texto, ignoreCase = true) ||
                    it.tipo.contains(texto, ignoreCase = true)
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onFiltroChange(texto: String) {
        _filtro.value = texto
    }
}
