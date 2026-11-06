package com.example.multitienda.modelo

data class ProductoFormModel(
    val id: Int = 0,
    val codigoBarras: String = "",
    val nombre: String = "",
    val categoria: String = "General",
    val precio: String = "",
    val stock: String = "",
    val stockMinimo: String = "5",

    val errorCodigoBarras: String? = null,
    val errorNombre: String? = null,
    val errorCategoria: String? = null,
    val errorPrecio: String? = null,
    val errorStock: String? = null,
    val errorStockMinimo: String? = null,

    val guardando: Boolean = false,
    val guardado: Boolean = false
) {
    val esEdicion: Boolean get() = id != 0
}
