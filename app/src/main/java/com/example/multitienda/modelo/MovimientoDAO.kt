package com.example.multitienda.modelo

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDAO {

    @Insert
    suspend fun agregarMovimiento(movimiento: Movimiento): Long

    @Query("SELECT * FROM movimientos ORDER BY fechaMillis DESC")
    fun listarMovimientos(): Flow<List<Movimiento>>

    @Query("SELECT * FROM movimientos WHERE codigoBarras = :codigo ORDER BY fechaMillis DESC")
    fun listarPorProducto(codigo: String): Flow<List<Movimiento>>
}
