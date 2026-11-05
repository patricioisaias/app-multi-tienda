package com.example.multitienda.ui.screens.inventario

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.multitienda.modelo.ProductoFormModel
import com.example.multitienda.ui.components.BotonAccion
import com.example.multitienda.ui.components.CustomTextField

@Composable
fun ProductoFormulario(
    form: ProductoFormModel,
    onCodigoBarrasChange: (String) -> Unit,
    onNombreChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onStockMinimoChange: (String) -> Unit,
    onEscanearCodigo: (() -> Unit)? = null,
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
            text = "Complete los datos del producto",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextField(
                value = form.codigoBarras,
                onValueChange = onCodigoBarrasChange,
                label = "Código de barras",
                modifier = Modifier.weight(1f),
                isError = form.errorCodigoBarras != null,
                errorMessage = form.errorCodigoBarras,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            if (onEscanearCodigo != null) {
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = onEscanearCodigo) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Escanear código de barras"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Escanear")
                }
            }
        }

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
            texto = "Guardar",
            isLoading = form.guardando,
            onClick = onGuardar
        )

        Spacer(modifier = Modifier.height(8.dp))

        androidx.compose.material3.OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material3.Text("Volver")
        }
    }
}
