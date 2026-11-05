package com.example.multitienda.ui.screens.operador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MenuOperadorScreen(
    usuario: String = "Usuario",
    rol: String = "Operador",
    tiendaActiva: String = "Bodega Pudahuel (Central)",
    onNavigateToScanner: () -> Unit = {},
    onNavigateToAjuste: () -> Unit = {},
    onNavigateToConteo: () -> Unit = {},
    onNavigateToCrearProducto: () -> Unit = {},
    onNavigateToCatalogo: () -> Unit = {},
    onNavigateToDiferencias: () -> Unit = {},
    onNavigateToAlertas: () -> Unit = {},
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToHistorial: () -> Unit = {},
    onNavigateToAdminTiendas: () -> Unit = {},
    onNavigateToExportar: () -> Unit = {},
    onNavigateToPerfil: () -> Unit = {},
    onCambiarTienda: () -> Unit = {}
) {
    val esGerencial = rol.contains("Gerencial", ignoreCase = true) || rol.contains("Gerente", ignoreCase = true)
    val esBodega = rol.contains("Bodega", ignoreCase = true)
    val esOperador = !esGerencial && !esBodega

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Cabecera de Usuario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Bienvenido, $usuario",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = rol,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Tarjeta de Sucursal Activa
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = "Sucursal",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (esGerencial) "Visión de Red:" else "Sucursal Activa:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = if (esGerencial) "Red Comercial Multi-Tienda" else tiendaActiva,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                TextButton(onClick = if (esGerencial) onNavigateToAdminTiendas else onCambiarTienda) {
                    Text(if (esGerencial) "Tiendas" else "Cambiar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Acceso directo a Mi Perfil
        OpcionMenuCard(
            titulo = "Mi Perfil",
            descripcion = "Consulta tus datos de usuario o cierra tu sesión",
            icono = Icons.Default.Person,
            colorContenedor = MaterialTheme.colorScheme.surfaceVariant,
            colorTexto = MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = onNavigateToPerfil,
            iconSize = 24.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // FUNCIONALIDADES SEGÚN PERFIL DE USUARIO
        // ==========================================

        if (esOperador) {
            // 1.1 PERFIL OPERADOR
            Text(
                text = "Herramientas de Operador",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Escanear Producto",
                descripcion = "Usa la cámara del teléfono para escanear códigos de barra",
                icono = Icons.Default.QrCodeScanner,
                colorContenedor = MaterialTheme.colorScheme.secondaryContainer,
                colorTexto = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = onNavigateToScanner
            )

            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Registrar Movimiento",
                descripcion = "Registra entradas, salidas o ajustes de mercadería",
                icono = Icons.Default.EditCalendar,
                onClick = onNavigateToAjuste
            )

            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Conteo Físico",
                descripcion = "Ingresa y rectifica las cantidades contadas físicamente",
                icono = Icons.Default.Inventory2,
                onClick = onNavigateToConteo
            )
        } else if (esBodega) {
            // 1.3 PERFIL ENCARGADO DE BODEGA
            Text(
                text = "Herramientas de Bodega",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Agregar Producto",
                descripcion = "Da de alta un producto nuevo con su stock inicial",
                icono = Icons.Default.Add,
                colorContenedor = MaterialTheme.colorScheme.secondaryContainer,
                colorTexto = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = onNavigateToCrearProducto
            )

            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Consultar Stock Disponible",
                descripcion = "Revisa el catálogo completo de productos y existencias",
                icono = Icons.Default.Inventory2,
                colorContenedor = MaterialTheme.colorScheme.secondaryContainer,
                colorTexto = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = onNavigateToCatalogo
            )

            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Control de Diferencias",
                descripcion = "Compara el inventario físico con el sistema para detectar descuadres",
                icono = Icons.Default.Rule,
                onClick = onNavigateToDiferencias
            )

            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Alertas de Reposición",
                descripcion = "Revisa productos con stock crítico y solicita reabastecimiento",
                icono = Icons.Default.Warning,
                onClick = onNavigateToAlertas
            )
        } else if (esGerencial) {
            // 1.2 PERFIL GERENCIAL
            Text(
                text = "Herramientas Gerenciales",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Tablero Analítico (Dashboard)",
                descripcion = "Visualiza métricas clave, volumen de stock y productos críticos",
                icono = Icons.Default.Assessment,
                colorContenedor = MaterialTheme.colorScheme.secondaryContainer,
                colorTexto = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = onNavigateToDashboard
            )

            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Auditoría de Existencias Globales",
                descripcion = "Revisa el catálogo consolidado de productos en toda la red",
                icono = Icons.Default.Category,
                onClick = onNavigateToCatalogo
            )

            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Alertas de Quiebre de Stock",
                descripcion = "Monitorea artículos bajo stock mínimo en las sucursales",
                icono = Icons.Default.Warning,
                onClick = onNavigateToAlertas
            )

            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Historial de Modificaciones",
                descripcion = "Revisa el registro de auditoría de movimientos y transacciones",
                icono = Icons.Default.History,
                onClick = onNavigateToHistorial
            )

            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Administración de Tiendas",
                descripcion = "Gestiona las sucursales y bodegas habilitadas para operar",
                icono = Icons.Default.Store,
                onClick = onNavigateToAdminTiendas
            )

            Spacer(modifier = Modifier.height(10.dp))

            OpcionMenuCard(
                titulo = "Exportar Reportes",
                descripcion = "Genera y descarga informes consolidados en Excel o CSV",
                icono = Icons.Default.FileDownload,
                onClick = onNavigateToExportar
            )
        }
    }
}

@Composable
private fun OpcionMenuCard(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    colorContenedor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceVariant,
    colorTexto: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit,
    iconSize: Dp = 36.dp
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.elevatedCardColors(containerColor = colorContenedor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = colorTexto,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colorTexto
                )
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorTexto.copy(alpha = 0.8f)
                )
            }
        }
    }
}
