package com.example.multitienda.ui.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.ui.components.FichaProducto
import com.example.multitienda.viewmodel.FormularioViewModel

@Composable
fun ProductoView(viewModel: FormularioViewModel = viewModel()){
    val contexto = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Agregar Producto",
            style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = { viewModel.onNombreChange(it)},
            label = { Text(text = "Ingrese el Nombre:")},
            supportingText = { uiState.errorNombre?.let { Text(text = it) }}
        )

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = uiState.precio,
            onValueChange = { viewModel.onPrecioChange(it)},
            label = { Text(text = "Ingrese el Precio:")},
            supportingText = { uiState.errorPrecio?.let { Text(text = it) }}
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = uiState.stock,
            onValueChange = { viewModel.onStockChange(it)},
            label = { Text(text = "Ingrese el Stock:")},
            supportingText = { uiState.errorStock?.let { Text(text = it) }}
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            val resp: Boolean= viewModel.guardar()
            if (resp){
                Toast.makeText(contexto,"Grabo", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(contexto,"No Grabo", Toast.LENGTH_LONG).show()
            }
        }) {
            Text(text = "Agregar Producto")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Listado de Productos")
        var productos = viewModel.lista.collectAsState()
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(productos.value, key = {it.id}){
                    item ->
                FichaProducto(p = item,
                    onDelete = { viewModel.eliminarProducto(item)})
            }

        }
    }
}
