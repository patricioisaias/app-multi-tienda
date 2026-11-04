package com.example.multitienda.data.repository

import com.example.multitienda.modelo.Movimiento
import com.example.multitienda.modelo.MovimientoDAO
import kotlinx.coroutines.flow.Flow

class MovimientoRepository(private val dao: MovimientoDAO) {

    val movimientos: Flow<List<Movimiento>> = dao.listarMovimientos()

    fun porProducto(codigo: String): Flow<List<Movimiento>> =
        dao.listarPorProducto(codigo.trim())

    suspend fun registrar(movimiento: Movimiento): Long =
        dao.agregarMovimiento(movimiento)
}
