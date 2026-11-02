package com.example.multitienda.ui.screens.operador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.multitienda.ui.components.BotonAccion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ResumenMovimientoScreen(
    codigo: String,
    tipo: String,
    cantidad: Int,
    motivo: String,
    tienda: String = "Bodega Pudahuel (Central)",
    onNuevoAjuste: () -> Unit,
    onVolverMenu: () -> Unit
) {
    val fechaHora = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Éxito",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "¡Ajuste Registrado con Éxito!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "La transacción quedó almacenada localmente",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                FilaDetalle(titulo = "Código de Producto:", valor = codigo.ifBlank { "N/A" })
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                FilaDetalle(titulo = "Tipo de Movimiento:", valor = tipo)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                FilaDetalle(titulo = "Cantidad Modificada:", valor = "$cantidad unidades")
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                FilaDetalle(titulo = "Sucursal / Tienda:", valor = tienda)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                FilaDetalle(titulo = "Fecha y Hora:", valor = fechaHora)
                if (motivo.isNotBlank()) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    FilaDetalle(titulo = "Observación:", valor = motivo)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        BotonAccion(
            texto = "Registrar Otro Movimiento",
            onClick = onNuevoAjuste
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onVolverMenu,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Menú Principal")
        }
    }
}

@Composable
private fun FilaDetalle(titulo: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

