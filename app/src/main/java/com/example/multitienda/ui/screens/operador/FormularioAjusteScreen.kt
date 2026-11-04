package com.example.multitienda.ui.screens.operador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.ui.components.BotonAccion
import com.example.multitienda.ui.components.CustomTextField
import com.example.multitienda.viewmodel.AjusteViewModel

private data class AjustePendiente(
    val codigo: String,
    val tipo: String,
    val cantidad: Int,
    val motivo: String
)

@Composable
fun FormularioAjusteScreen(
    codigoInicial: String = "",
    tienda: String = "",
    onAbrirScanner: () -> Unit,
    onAjusteConfirmado: (codigo: String, tipo: String, cantidad: Int, motivo: String) -> Unit,
    onVolver: () -> Unit,
    viewModel: AjusteViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()
    var ajustePendiente by remember { mutableStateOf<AjustePendiente?>(null) }
    var codigo by remember { mutableStateOf(codigoInicial) }
    var tipoMovimiento by remember { mutableStateOf("Entrada") }
    var cantidad by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }

    var errorCodigo by remember { mutableStateOf<String?>(null) }
    var errorCantidad by remember { mutableStateOf<String?>(null) }

    val tipos = listOf("Entrada", "Salida", "Ajuste / Conteo")

    LaunchedEffect(estado.confirmado) {
        if (estado.confirmado) {
            viewModel.consumirConfirmado()
            ajustePendiente?.let { pendiente ->
                onAjusteConfirmado(
                    pendiente.codigo,
                    pendiente.tipo,
                    pendiente.cantidad,
                    pendiente.motivo
                )
            }
            ajustePendiente = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Registrar Ajuste de Stock",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Actualice existencias en la sucursal activa",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextField(
                value = codigo,
                onValueChange = {
                    codigo = it
                    errorCodigo = null
                    viewModel.limpiarError()
                },
                label = "Código de Barras",
                isError = errorCodigo != null,
                errorMessage = errorCodigo,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onAbrirScanner,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = "Escanear")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tipo de Movimiento:",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tipos.forEach { tipo ->
                FilterChip(
                    selected = tipoMovimiento == tipo,
                    onClick = {
                        tipoMovimiento = tipo
                        viewModel.limpiarError()
                    },
                    label = { Text(tipo) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = cantidad,
            onValueChange = {
                cantidad = it
                errorCantidad = null
                viewModel.limpiarError()
            },
            label = "Cantidad a Registrar",
            isError = errorCantidad != null,
            errorMessage = errorCantidad,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CustomTextField(
            value = motivo,
            onValueChange = {
                motivo = it
                viewModel.limpiarError()
            },
            label = "Motivo u Observación (Opcional)"
        )

        Spacer(modifier = Modifier.height(24.dp))

        estado.error?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        BotonAccion(
            texto = "Confirmar Movimiento",
            isLoading = estado.cargando,
            onClick = {
                var valido = true
                if (codigo.isBlank()) {
                    errorCodigo = "El código es obligatorio"
                    valido = false
                }
                val cantInt = cantidad.toIntOrNull()
                if (cantInt == null || cantInt <= 0) {
                    errorCantidad = "Ingrese una cantidad válida mayor a 0"
                    valido = false
                }

                if (valido) {
                    val cantidadValida = cantInt ?: 0
                    val motivoFinal = motivo.trim()
                    ajustePendiente = AjustePendiente(
                        codigo = codigo.trim(),
                        tipo = tipoMovimiento,
                        cantidad = cantidadValida,
                        motivo = motivoFinal
                    )
                    viewModel.registrar(
                        codigo = codigo.trim(),
                        tipo = tipoMovimiento,
                        cantidad = cantidadValida,
                        motivo = motivoFinal,
                        tienda = tienda
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Menú")
        }
    }
}
