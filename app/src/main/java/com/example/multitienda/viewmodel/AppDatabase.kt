package com.example.multitienda.viewmodel

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.multitienda.modelo.Producto
import com.example.multitienda.modelo.ProductoDAO

@Database(entities = [Producto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    //atributo para manejar la data de "Productos"
    abstract fun productoDAO(): ProductoDAO

    companion object {
        @Volatile
        private var INSTANCIA: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCIA ?: synchronized(this) {
                var instancia = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "base_productos"
                ).build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}