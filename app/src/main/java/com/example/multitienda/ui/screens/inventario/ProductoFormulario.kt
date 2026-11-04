package com.example.multitienda.ui.screens.inventario

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.multitienda.modelo.ProductoFormModel
import com.example.multitienda.ui.components.BotonAccion
import com.example.multitienda.ui.components.CustomTextField
import com.example.multitienda.ui.theme.MultiTiendaTheme

@Composable
fun ProductoFormulario(
    form: ProductoFormModel,
    titulo: String,
    textoBoton: String,
    onCodigoBarrasChange: (String) -> Unit,
    onNombreChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onStockMinimoChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Complete los datos del producto",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = form.codigoBarras,
            onValueChange = onCodigoBarrasChange,
            label = "Código de barras",
            isError = form.errorCodigoBarras != null,
            errorMessage = form.errorCodigoBarras,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CustomTextField(
            value = form.nombre,
            onValueChange = onNombreChange,
            label = "Nombre del producto",
            isError = form.errorNombre != null,
            errorMessage = form.errorNombre
        )

        CustomTextField(
            value = form.categoria,
            onValueChange = onCategoriaChange,
            label = "Categoría",
            isError = form.errorCategoria != null,
            errorMessage = form.errorCategoria
        )

        CustomTextField(
            value = form.precio,
            onValueChange = onPrecioChange,
            label = "Precio",
            isError = form.errorPrecio != null,
            errorMessage = form.errorPrecio,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        CustomTextField(
            value = form.stock,
            onValueChange = onStockChange,
            label = "Stock actual",
            isError = form.errorStock != null,
            errorMessage = form.errorStock,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CustomTextField(
            value = form.stockMinimo,
            onValueChange = onStockMinimoChange,
            label = "Stock mínimo",
            isError = form.errorStockMinimo != null,
            errorMessage = form.errorStockMinimo,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(24.dp))

        BotonAccion(
            texto = textoBoton,
            isLoading = form.guardando,
            onClick = onGuardar
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
private fun ProductoFormularioPreview() {
    MultiTiendaTheme {
        ProductoFormulario(
            form = ProductoFormModel(
                codigoBarras = "7801234567890",
                nombre = "Leche Entera 1L",
                categoria = "Lácteos",
                precio = "1290",
                stock = "24",
                stockMinimo = "10"
            ),
            titulo = "Nuevo Producto",
            textoBoton = "Guardar Producto",
            onCodigoBarrasChange = {},
            onNombreChange = {},
            onCategoriaChange = {},
            onPrecioChange = {},
            onStockChange = {},
            onStockMinimoChange = {},
            onGuardar = {},
            onVolver = {}
        )
    }
}
