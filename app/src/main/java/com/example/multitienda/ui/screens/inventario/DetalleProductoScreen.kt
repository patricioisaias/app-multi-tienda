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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.modelo.Producto
import com.example.multitienda.ui.components.BotonAccion
import com.example.multitienda.ui.theme.MultiTiendaTheme
import com.example.multitienda.viewmodel.ProductoViewModel

@Composable
fun DetalleProductoScreen(
    productoId: Int,
    viewModel: ProductoViewModel = viewModel(),
    onEditar: (Int) -> Unit = {},
    onEliminar: (Int) -> Unit = {},
    onConteo: (Int) -> Unit = {},
    onVolver: () -> Unit = {}
) {
    val producto by viewModel.detalle.collectAsState()

    LaunchedEffect(productoId) {
        viewModel.cargarDetalle(productoId)
    }

    DetalleContenido(
        producto = producto,
        onEditar = onEditar,
        onEliminar = onEliminar,
        onConteo = onConteo,
        onVolver = onVolver
    )
}

@Composable
private fun DetalleContenido(
    producto: Producto?,
    onEditar: (Int) -> Unit,
    onEliminar: (Int) -> Unit,
    onConteo: (Int) -> Unit,
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
                text = "Detalle del Producto",
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

        val bajoStock = producto.stock <= producto.stockMinimo

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = producto.categoria,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                FilaDato("Código de barras", producto.codigoBarras)
                FilaDato("Precio", "$${producto.precio}")
                FilaDato("Stock actual", producto.stock.toString())
                FilaDato("Stock mínimo", producto.stockMinimo.toString())
                if (bajoStock) {
                    Text(
                        text = "Alerta: producto bajo el stock mínimo",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BotonAccion(
            texto = "Editar Producto",
            onClick = { onEditar(producto.id) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { onConteo(producto.id) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Conteo Físico")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onEliminar(producto.id) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text("Eliminar Producto")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Catálogo")
        }
    }
}

@Composable
private fun FilaDato(titulo: String, valor: String) {
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
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetalleProductoPreview() {
    MultiTiendaTheme {
        DetalleContenido(
            producto = Producto(
                id = 1,
                codigoBarras = "7801234567890",
                nombre = "Leche Entera 1L",
                categoria = "Lácteos",
                precio = 1290.0,
                stock = 24,
                stockMinimo = 10
            ),
            onEditar = {},
            onEliminar = {},
            onConteo = {},
            onVolver = {}
        )
    }
}
