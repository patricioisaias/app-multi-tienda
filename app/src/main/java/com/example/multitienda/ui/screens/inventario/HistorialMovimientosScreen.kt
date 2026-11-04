package com.example.multitienda.ui.screens.inventario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.modelo.Movimiento
import com.example.multitienda.ui.components.CustomTextField
import com.example.multitienda.ui.theme.MultiTiendaTheme
import com.example.multitienda.viewmodel.HistorialViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistorialMovimientosScreen(
    viewModel: HistorialViewModel = viewModel(),
    onVolver: () -> Unit = {}
) {
    val movimientos by viewModel.movimientos.collectAsState()
    val filtro by viewModel.filtro.collectAsState()

    HistorialContenido(
        movimientos = movimientos,
        filtro = filtro,
        onFiltroChange = viewModel::onFiltroChange,
        onVolver = onVolver
    )
}

@Composable
private fun HistorialContenido(
    movimientos: List<Movimiento>,
    filtro: String,
    onFiltroChange: (String) -> Unit,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Historial de Movimientos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = "${movimientos.size} movimiento(s) registrado(s)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            value = filtro,
            onValueChange = onFiltroChange,
            label = "Filtrar por código o tipo",
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Filtrar") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (movimientos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no hay movimientos registrados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movimientos, key = { it.id }) { movimiento ->
                    MovimientoCard(movimiento)
                }
            }
        }
    }
}

@Composable
private fun MovimientoCard(movimiento: Movimiento) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = movimiento.tipo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = formatearFecha(movimiento.fechaMillis),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "Código: ${movimiento.codigoBarras}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Cantidad: ${movimiento.cantidad}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (movimiento.motivo.isNotBlank()) {
                Text(
                    text = "Motivo: ${movimiento.motivo}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (movimiento.tienda.isNotBlank()) {
                Text(
                    text = "Tienda: ${movimiento.tienda}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatearFecha(millis: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formato.format(Date(millis))
}

@Preview(showBackground = true)
@Composable
private fun HistorialMovimientosPreview() {
    MultiTiendaTheme {
        HistorialContenido(
            movimientos = listOf(
                Movimiento(
                    id = 1,
                    codigoBarras = "7801234567890",
                    tipo = "Entrada",
                    cantidad = 24,
                    motivo = "Creación de producto",
                    tienda = "Bodega Pudahuel (Central)",
                    fechaMillis = 1_760_000_000_000
                ),
                Movimiento(
                    id = 2,
                    codigoBarras = "7804443332221",
                    tipo = "Conteo",
                    cantidad = -3,
                    motivo = "Conteo físico en pasillo",
                    tienda = "Sucursal Santiago Centro",
                    fechaMillis = 1_760_100_000_000
                )
            ),
            filtro = "",
            onFiltroChange = {},
            onVolver = {}
        )
    }
}
