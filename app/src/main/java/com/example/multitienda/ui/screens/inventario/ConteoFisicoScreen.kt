package com.example.multitienda.ui.screens.inventario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.modelo.Producto
import com.example.multitienda.ui.components.BotonAccion
import com.example.multitienda.ui.components.CustomTextField
import com.example.multitienda.ui.theme.MultiTiendaTheme
import com.example.multitienda.viewmodel.ProductoViewModel

@Composable
fun ConteoFisicoScreen(
    productoId: Int,
    tienda: String = "",
    viewModel: ProductoViewModel = viewModel(),
    onRegistrado: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    val producto by viewModel.detalle.collectAsState()
    val error by viewModel.conteoError.collectAsState()
    val guardado by viewModel.conteoGuardado.collectAsState()

    var conteo by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }

    LaunchedEffect(productoId) {
        viewModel.limpiarErrorConteo()
        viewModel.cargarDetalle(productoId)
    }

    LaunchedEffect(guardado) {
        if (guardado) {
            viewModel.consumirConteoGuardado()
            onRegistrado()
        }
    }

    ConteoContenido(
        producto = producto,
        conteo = conteo,
        motivo = motivo,
        error = error,
        onConteoChange = {
            conteo = it
            viewModel.limpiarErrorConteo()
        },
        onMotivoChange = { motivo = it },
        onRegistrar = { viewModel.registrarConteo(productoId, conteo, tienda, motivo) },
        onVolver = onVolver
    )
}

@Composable
private fun ConteoContenido(
    producto: Producto?,
    conteo: String,
    motivo: String,
    error: String?,
    onConteoChange: (String) -> Unit,
    onMotivoChange: (String) -> Unit,
    onRegistrar: () -> Unit,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Conteo Físico",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (producto == null) {
            Text(
                text = "Cargando producto...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            return@Column
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Código: ${producto.codigoBarras}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Stock del sistema: ${producto.stock}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = conteo,
            onValueChange = onConteoChange,
            label = "Conteo real en pasillo",
            isError = error != null,
            errorMessage = error,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CustomTextField(
            value = motivo,
            onValueChange = onMotivoChange,
            label = "Observación (opcional)"
        )

        val conteoInt = conteo.trim().toIntOrNull()
        if (conteoInt != null) {
            val diferencia = conteoInt - producto.stock
            Text(
                text = "Ajuste a registrar: ${if (diferencia >= 0) "+" else ""}$diferencia",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (diferencia == 0) MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        BotonAccion(
            texto = "Registrar Conteo",
            onClick = onRegistrar
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConteoFisicoPreview() {
    MultiTiendaTheme {
        ConteoContenido(
            producto = Producto(
                id = 1,
                codigoBarras = "7801234567890",
                nombre = "Leche Entera 1L",
                categoria = "Lácteos",
                precio = 1290.0,
                stock = 24,
                stockMinimo = 10
            ),
            conteo = "20",
            motivo = "Producto dañado en pasillo",
            error = null,
            onConteoChange = {},
            onMotivoChange = {},
            onRegistrar = {},
            onVolver = {}
        )
    }
}
