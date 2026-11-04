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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.modelo.Producto
import com.example.multitienda.ui.components.CustomTextField
import com.example.multitienda.ui.theme.MultiTiendaTheme
import com.example.multitienda.viewmodel.ProductoViewModel

@Composable
fun CatalogoProductosScreen(
    viewModel: ProductoViewModel = viewModel(),
    onProductoClick: (Int) -> Unit = {},
    onCrearProducto: () -> Unit = {},
    onVerHistorial: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    val productos by viewModel.productos.collectAsState()
    val busqueda by viewModel.busqueda.collectAsState()

    CatalogoContenido(
        productos = productos,
        busqueda = busqueda,
        onBusquedaChange = viewModel::onBusquedaChange,
        onProductoClick = onProductoClick,
        onCrearProducto = onCrearProducto,
        onVerHistorial = onVerHistorial,
        onVolver = onVolver
    )
}

@Composable
private fun CatalogoContenido(
    productos: List<Producto>,
    busqueda: String,
    onBusquedaChange: (String) -> Unit,
    onProductoClick: (Int) -> Unit,
    onCrearProducto: () -> Unit,
    onVerHistorial: () -> Unit,
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
                text = "Catálogo de Productos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = "${productos.size} producto(s) en inventario",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            value = busqueda,
            onValueChange = onBusquedaChange,
            label = "Buscar por nombre",
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onCrearProducto,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Nuevo")
            }
            OutlinedButton(
                onClick = onVerHistorial,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.History, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Historial")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

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

@Preview(showBackground = true)
@Composable
private fun CatalogoProductosPreview() {
    MultiTiendaTheme {
        CatalogoContenido(
            productos = listOf(
                Producto(
                    id = 1,
                    codigoBarras = "7801234567890",
                    nombre = "Leche Entera 1L",
                    categoria = "Lácteos",
                    precio = 1290.0,
                    stock = 24,
                    stockMinimo = 10
                ),
                Producto(
                    id = 2,
                    codigoBarras = "7804443332221",
                    nombre = "Agua Mineral 500ml",
                    categoria = "Bebidas",
                    precio = 790.0,
                    stock = 0,
                    stockMinimo = 20
                )
            ),
            busqueda = "",
            onBusquedaChange = {},
            onProductoClick = {},
            onCrearProducto = {},
            onVerHistorial = {},
            onVolver = {}
        )
    }
}
