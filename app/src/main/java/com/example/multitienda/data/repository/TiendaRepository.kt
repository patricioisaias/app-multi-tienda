package com.example.multitienda.data.repository

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SucursalTienda(
    val id: String,
    val nombre: String,
    val direccion: String,
    val tipo: String,
    val habilitada: Boolean = true
)

object TiendaRepository {

    private const val PREFS_NAME = "multitienda_sucursales_prefs"

    private val sucursalesBase = listOf(
        SucursalTienda(
            id = "T-01",
            nombre = "Bodega Pudahuel (Central)",
            direccion = "Parque Enea #1240",
            tipo = "Bodega Principal",
            habilitada = true
        ),
        SucursalTienda(
            id = "T-02",
            nombre = "Sucursal Santiago Centro",
            direccion = "Av. Libertador B. O'Higgins #450",
            tipo = "Tienda Retail",
            habilitada = true
        ),
        SucursalTienda(
            id = "T-03",
            nombre = "Tienda Providencia",
            direccion = "Av. Providencia #1890",
            tipo = "Tienda Retail",
            habilitada = true
        ),
        SucursalTienda(
            id = "T-04",
            nombre = "Bodega Logística Sur",
            direccion = "Camino Nos #500, San Bernardo",
            tipo = "Bodega Satélite",
            habilitada = true
        )
    )

    private val _tiendas = MutableStateFlow(sucursalesBase)
    val tiendas: StateFlow<List<SucursalTienda>> = _tiendas.asStateFlow()

    fun cargarTiendas(context: Context): List<SucursalTienda> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val actualizadas = sucursalesBase.map { tienda ->
            val habilitada = prefs.getBoolean("tienda_${tienda.id}", true)
            tienda.copy(habilitada = habilitada)
        }
        // Garantizar que al menos una tienda esté habilitada
        val listaSegura = if (actualizadas.none { it.habilitada }) {
            actualizadas.mapIndexed { index, t -> if (index == 0) t.copy(habilitada = true) else t }
        } else {
            actualizadas
        }
        _tiendas.value = listaSegura
        return listaSegura
    }

    fun cambiarEstadoTienda(context: Context, id: String, nuevoEstado: Boolean): Boolean {
        val actuales = cargarTiendas(context)
        val habilitadasCount = actuales.count { it.habilitada }
        // Impedir que se desactive la última tienda habilitada
        if (!nuevoEstado && habilitadasCount <= 1) {
            return false
        }

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("tienda_$id", nuevoEstado).apply()
        cargarTiendas(context)
        return true
    }

    fun obtenerTiendaActivaValida(context: Context, tiendaPreferida: String): String {
        val lista = cargarTiendas(context)
        val encontrada = lista.find { it.nombre == tiendaPreferida && it.habilitada }
        return encontrada?.nombre
            ?: lista.firstOrNull { it.habilitada }?.nombre
            ?: "Bodega Pudahuel (Central)"
    }
}
