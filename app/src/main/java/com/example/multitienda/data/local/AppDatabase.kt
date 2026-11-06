package com.example.multitienda.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.multitienda.modelo.Movimiento
import com.example.multitienda.modelo.Producto

@Database(
    entities = [Producto::class, Movimiento::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDAO(): ProductoDAO
    abstract fun movimientoDAO(): MovimientoDAO

    companion object {
        @Volatile
        private var INSTANCIA: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "base_productos"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}

