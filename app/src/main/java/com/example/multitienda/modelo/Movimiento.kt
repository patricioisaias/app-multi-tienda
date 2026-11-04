package com.example.multitienda.modelo

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigoBarras: String = "",
    val tipo: String = "",
    val cantidad: Int = 0,
    val motivo: String = "",
    val tienda: String = "",
    val fechaMillis: Long = System.currentTimeMillis()
)
