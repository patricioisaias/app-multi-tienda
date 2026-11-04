package com.example.multitienda.ui.screens.gerencial

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.ui.components.BotonAccion
import com.example.multitienda.ui.components.CustomTextField
import com.example.multitienda.util.HapticFeedbackHelper
import com.example.multitienda.viewmodel.ReposicionViewModel

/**
 * Pantalla 20: DeclararReposicionScreen (Angelo - Registro de reposición y respuesta háptica)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeclararReposicionScreen(
    productoId: Int,
    tiendaActiva: String = "Bodega Pudahuel (Central)",
    onVolver: () -> Unit,
    viewModel: ReposicionViewModel = viewModel()
) {
    val context = LocalContext.current
    val estado by viewModel.estado.collectAsState()
    var mostrarDialogoExito by remember { mutableStateOf(false) }

    LaunchedEffect(productoId) {
        viewModel.cargarProducto(productoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Declarar Reposición") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        val prod = estado.producto

        if (prod == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Cargando producto...", style = MaterialTheme.typography.bodyLarge)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta con información del producto crítico
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = prod.nombre,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Código: ${prod.codigoBarras} | Sucursal: $tiendaActiva",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Stock Actual: ${prod.stock}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Stock Mínimo: ${prod.stockMinimo}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Detalles de la Solicitud de Reposición",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                value = estado.cantidad,
                onValueChange = { viewModel.onCantidadChange(it) },
                label = "Cantidad a Reponer",
                isError = estado.errorCantidad != null,
                errorMessage = estado.errorCantidad,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon = {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = estado.motivo,
                onValueChange = { viewModel.onMotivoChange(it) },
                label = "Motivo de la reposición"
            )

            Spacer(modifier = Modifier.height(24.dp))

            estado.mensajeError?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            BotonAccion(
                texto = "Confirmar Orden de Reposición",
                isLoading = estado.cargando,
                onClick = {
                    viewModel.confirmarReposicion(tienda = tiendaActiva) {
                        HapticFeedbackHelper.vibrarDoblePulso(context)
                        mostrarDialogoExito = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }

        if (mostrarDialogoExito) {
            AlertDialog(
                onDismissRequest = {
                    mostrarDialogoExito = false
                    viewModel.resetEstado()
                    onVolver()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                },
                title = { Text("¡Reposición Confirmada!") },
                text = {
                    Text("Se ha registrado la orden de reposición y actualizado el stock en la base de datos Room con éxito.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarDialogoExito = false
                            viewModel.resetEstado()
                            onVolver()
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

