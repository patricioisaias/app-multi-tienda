package com.example.multitienda.modelo

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String = "",
    val precio: Double,
    val stock: Int = 0
)