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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Store
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
    onNavigateToScanner: () -> Unit,
    onNavigateToAjuste: () -> Unit,
    onNavigateToCatalogo: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onCambiarTienda: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = "Tienda",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Sucursal Activa:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = tiendaActiva,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                TextButton(onClick = onCambiarTienda) {
                    Text("Cambiar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mi Perfil (arriba, con icono pequeño)
        OpcionMenuCard(
            titulo = "Mi Perfil",
            descripcion = "Datos de sesión activa y opciones de desconexión",
            icono = Icons.Default.Person,
            colorContenedor = MaterialTheme.colorScheme.surfaceVariant,
            colorTexto = MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = onNavigateToPerfil,
            iconSize = 24.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Operaciones de Terreno",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        OpcionMenuCard(
            titulo = "Escanear Código de Barras",
            descripcion = "Usar cámara nativa para captura rápida de productos",
            icono = Icons.Default.QrCodeScanner,
            colorContenedor = MaterialTheme.colorScheme.secondaryContainer,
            colorTexto = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = onNavigateToScanner
        )

        Spacer(modifier = Modifier.height(8.dp))

        OpcionMenuCard(
            titulo = "Registrar Ajuste de Stock",
            descripcion = "Entrada, salida o regularización física de inventario",
            icono = Icons.Default.EditCalendar,
            onClick = onNavigateToAjuste
        )

        Spacer(modifier = Modifier.height(8.dp))

        OpcionMenuCard(
            titulo = "Catálogo General de Productos",
            descripcion = "Consultar existencias y productos en Room",
            icono = Icons.Default.Inventory2,
            onClick = onNavigateToCatalogo
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Supervisión y Cuenta",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dashboard solo para Gerencial
        if (rol == "Gerencial") {
            OpcionMenuCard(
                titulo = "Dashboard y Alertas de Quiebre",
                descripcion = "Monitoreo de existencias mínimas y reportes",
                icono = Icons.Default.Assessment,
                colorContenedor = MaterialTheme.colorScheme.secondaryContainer,
                colorTexto = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = onNavigateToDashboard
            )

            Spacer(modifier = Modifier.height(8.dp))
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = colorTexto,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
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
