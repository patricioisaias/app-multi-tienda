package com.example.multitienda.ui.navigation

import android.net.Uri

object AppRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val TIENDAS = "tiendas"
    const val MENU = "menu_operador"
    const val PERFIL = "perfil"
    const val SCANNER = "scanner"
    const val DASHBOARD = "dashboard"
    const val ADMIN_TIENDAS = "admin_tiendas"
    const val EXPORTAR_REPORTES = "exportar_reportes"
    const val DIFERENCIAS_STOCK = "diferencias_stock"

    const val AJUSTE = "ajuste/{codigo}"
    const val AJUSTE_BASE = "ajuste"
    const val RESUMEN = "resumen/{codigo}/{tipo}/{cantidad}/{motivo}"
    const val RESUMEN_BASE = "resumen"

    fun ajuste(codigo: String): String = "$AJUSTE_BASE/${Uri.encode(codigo)}"

    fun resumen(codigo: String, tipo: String, cantidad: Int, motivo: String): String {
        val motivoSeguro = motivo.ifBlank { "SinObservacion" }
        return "$RESUMEN_BASE/${Uri.encode(codigo)}/${Uri.encode(tipo)}/$cantidad/${Uri.encode(motivoSeguro)}"
    }
}
