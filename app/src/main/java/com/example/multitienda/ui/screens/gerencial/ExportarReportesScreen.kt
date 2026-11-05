package com.example.multitienda.ui.screens.gerencial

import android.widget.Toast
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.multitienda.ui.components.BotonAccion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportarReportesScreen(onVolver: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var reporteSeleccionado by remember { mutableStateOf("Consolidado Global") }
    var formatoSeleccionado by remember { mutableStateOf("Excel (.xlsx)") }
    var exportando by remember { mutableStateOf(false) }
    var mensajeExito by remember { mutableStateOf<String?>(null) }

    val tiposReporte = listOf(
        "Consolidado Global",
        "Quiebres de Stock",
        "Auditoría de Movimientos"
    )

    val formatos = listOf("Excel (.xlsx)", "CSV (.csv)")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exportar Reportes") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Generación de Informes Gerenciales",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Exporta datos analíticos para la toma de decisiones comerciales y financieras",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Tipo de informe:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tiposReporte.forEach { tipo ->
                    FilterChip(
                        selected = reporteSeleccionado == tipo,
                        onClick = {
                            reporteSeleccionado = tipo
                            mensajeExito = null
                        },
                        label = { Text(tipo) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Formato de archivo:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                formatos.forEach { formato ->
                    FilterChip(
                        selected = formatoSeleccionado == formato,
                        onClick = {
                            formatoSeleccionado = formato
                            mensajeExito = null
                        },
                        label = { Text(formato) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            BotonAccion(
                texto = "Generar y Descargar Reporte",
                isLoading = exportando,
                leadingIcon = {
                    Icon(Icons.Default.FileDownload, contentDescription = null)
                },
                onClick = {
                    exportando = true
                    mensajeExito = null
                    scope.launch {
                        delay(1200) // Simulación de generación del archivo
                        exportando = false
                        val extension = if (formatoSeleccionado.contains("xlsx")) "xlsx" else "csv"
                        mensajeExito = "Reporte '$reporteSeleccionado.$extension' generado exitosamente."
                        Toast.makeText(context, "Archivo descargado correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            if (mensajeExito != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Descarga Completa",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = mensajeExito!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

