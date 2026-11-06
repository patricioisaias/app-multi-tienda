package com.example.multitienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitienda.data.local.AppDatabase
import com.example.multitienda.data.repository.ProductoRepository
import com.example.multitienda.modelo.Producto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MetricasDashboard(
    val totalSkus: Int = 0,
    val totalUnidades: Int = 0,
    val totalQuiebres: Int = 0,
    val porcentajeQuiebre: Float = 0f
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val productoRepository = ProductoRepository(
        AppDatabase.getDatabase(application).productoDAO()
    )

    init {
        viewModelScope.launch {
            productoRepository.sembrarSiVacio()
        }
    }

    val metricas: StateFlow<MetricasDashboard> = productoRepository.productos.map { lista ->
        val skus = lista.size
        val unidades = lista.sumOf { it.stock }
        val quiebres = lista.count { it.stock <= it.stockMinimo }
        val pct = if (skus > 0) (quiebres.toFloat() / skus.toFloat()) * 100f else 0f
        MetricasDashboard(
            totalSkus = skus,
            totalUnidades = unidades,
            totalQuiebres = quiebres,
            porcentajeQuiebre = pct
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MetricasDashboard()
    )

    val productosCriticos: StateFlow<List<Producto>> = productoRepository.productosBajoStock().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
}
