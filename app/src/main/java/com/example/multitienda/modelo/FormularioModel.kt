package com.example.multitienda.modelo

data class FormularioModel(
    val id: Int = 0,
    val nombre: String = "",
    val precio: String = "",
    val stock: String = "",

    // errores
    val errorNombre: String? = null,
    val errorPrecio: String? = null,
    val errorStock: String? = null,
    val esValido: Boolean = false
)