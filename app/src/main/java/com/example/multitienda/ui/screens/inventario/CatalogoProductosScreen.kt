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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.ui.components.CustomTextField
import com.example.multitienda.ui.components.ProductoCard
import com.example.multitienda.viewmodel.ProductoViewModel

@Composable
fun CatalogoProductosScreen(
    rol: String = "Bodega",
    viewModel: ProductoViewModel = viewModel(),
    onProductoClick: (Int) -> Unit = {},
    onCrearProducto: () -> Unit = {},
    onVerHistorial: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    val productos by viewModel.productos.collectAsState()
    val busqueda by viewModel.busqueda.collectAsState()

    val esGerencial = rol.contains("Gerencial", ignoreCase = true) || rol.contains("Gerente", ignoreCase = true)
    val esBodega = rol.contains("Bodega", ignoreCase = true)
    val esOperador = !esGerencial && !esBodega

    val tituloPantalla = when {
        esOperador -> "Conteo Físico — Catálogo"
        esGerencial -> "Existencias Globales"
        else -> "Catálogo de Productos"
    }

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
                text = tituloPantalla,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = if (esOperador) {
                "${productos.size} producto(s) • Toque un artículo para registrar su conteo físico"
            } else {
                "${productos.size} producto(s) en inventario"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            value = busqueda,
            onValueChange = viewModel::onBusquedaChange,
            label = "Buscar por nombre",
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") }
        )

        if (esBodega || esGerencial) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (esBodega) {
                    Button(
                        onClick = onCrearProducto,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Nuevo Producto")
                    }
                }
                if (esGerencial) {
                    OutlinedButton(
                        onClick = onVerHistorial,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.History, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ver Historial de Modificaciones")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (productos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay productos que coincidan con la búsqueda.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productos, key = { it.id }) { producto ->
                    ProductoCard(
                        producto = producto,
                        onClick = { onProductoClick(producto.id) }
                    )
                }
            }
        }
    }
}
