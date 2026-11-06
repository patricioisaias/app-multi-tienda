package com.example.multitienda.modelo

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigoBarras: String = "",
    val nombre: String = "",
    val categoria: String = "General",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val stockMinimo: Int = 5
)
