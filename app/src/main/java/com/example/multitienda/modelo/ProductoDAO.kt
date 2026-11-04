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
    suspend fun agregarProducto(producto: Producto): Long

    @Delete
    suspend fun eliminarProducto(producto: Producto)

    @Update
    suspend fun actualizarProducto(producto: Producto)

    @Query("SELECT * FROM productos ORDER BY id")
    fun listarProductos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun listarProductosOrdenados(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Producto?

    @Query("SELECT * FROM productos WHERE codigoBarras = :codigo LIMIT 1")
    suspend fun obtenerPorCodigo(codigo: String): Producto?

    @Query("SELECT * FROM productos WHERE nombre LIKE '%' || :texto || '%' ORDER BY nombre ASC")
    fun buscarPorNombre(texto: String): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE stock <= stockMinimo ORDER BY stock ASC")
    fun productosBajoStock(): Flow<List<Producto>>

    @Query("UPDATE productos SET stock = :nuevoStock WHERE id = :id")
    suspend fun actualizarStock(id: Int, nuevoStock: Int)

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun contarProductos(): Int
}
