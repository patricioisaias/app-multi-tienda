package com.example.multitienda.modelo

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDAO {

    @Insert
    suspend fun agregarProducto(producto: Producto)

    @Delete
    suspend fun eliminarProducto(producto: Producto)

    @Update
    suspend fun actualizarProducto(producto: Producto)

    @Query("SELECT * FROM productos ORDER BY id")
    fun listarProductos(): Flow<List<Producto>>

}