package com.example.multitienda.data.repository

import com.example.multitienda.modelo.Producto
import com.example.multitienda.modelo.ProductoDAO
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val dao: ProductoDAO) {

    val productos: Flow<List<Producto>> = dao.listarProductosOrdenados()

    fun buscar(texto: String): Flow<List<Producto>> =
        if (texto.isBlank()) productos else dao.buscarPorNombre(texto.trim())

    fun productosBajoStock(): Flow<List<Producto>> = dao.productosBajoStock()

    suspend fun obtenerPorId(id: Int): Producto? = dao.obtenerPorId(id)

    suspend fun obtenerPorCodigo(codigo: String): Producto? =
        dao.obtenerPorCodigo(codigo.trim())

    suspend fun insertar(producto: Producto): Long = dao.agregarProducto(producto)

    suspend fun actualizar(producto: Producto) = dao.actualizarProducto(producto)

    suspend fun eliminar(producto: Producto) = dao.eliminarProducto(producto)

    suspend fun actualizarStock(id: Int, nuevoStock: Int) =
        dao.actualizarStock(id, nuevoStock)

    suspend fun sembrarSiVacio() {
        if (dao.contarProductos() == 0) {
            productosDeEjemplo.forEach { dao.agregarProducto(it) }
        }
    }

    companion object {
        val productosDeEjemplo = listOf(
            Producto(
                codigoBarras = "7801234567890",
                nombre = "Leche Entera 1L",
                categoria = "Lácteos",
                precio = 1290.0,
                stock = 24,
                stockMinimo = 10
            ),
            Producto(
                codigoBarras = "7809876543210",
                nombre = "Arroz Grado 1 1kg",
                categoria = "Abarrotes",
                precio = 1590.0,
                stock = 8,
                stockMinimo = 12
            ),
            Producto(
                codigoBarras = "7801112223334",
                nombre = "Aceite Vegetal 900ml",
                categoria = "Abarrotes",
                precio = 2490.0,
                stock = 3,
                stockMinimo = 6
            ),
            Producto(
                codigoBarras = "7805556667778",
                nombre = "Detergente 3kg",
                categoria = "Limpieza",
                precio = 7990.0,
                stock = 15,
                stockMinimo = 5
            ),
            Producto(
                codigoBarras = "7804443332221",
                nombre = "Agua Mineral 500ml",
                categoria = "Bebidas",
                precio = 790.0,
                stock = 0,
                stockMinimo = 20
            ),
            Producto(
                codigoBarras = "7807778889990",
                nombre = "Café Molido 250g",
                categoria = "Abarrotes",
                precio = 3990.0,
                stock = 11,
                stockMinimo = 6
            )
        )
    }
}
