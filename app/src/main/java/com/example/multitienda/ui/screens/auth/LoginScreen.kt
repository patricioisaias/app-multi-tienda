package com.example.multitienda.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multitienda.ui.components.BotonAccion
import com.example.multitienda.ui.components.CustomTextField
import com.example.multitienda.viewmodel.LoginViewModel

@Composable
fun LoginScreen(onLoginSuccess: (nombre: String, puesto: String, rol: String) -> Unit) {
    val viewModel: LoginViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val loginExitoso by viewModel.loginExitoso.collectAsState()

    LaunchedEffect(loginExitoso) {
        loginExitoso?.let {
            onLoginSuccess(it.nombre, it.puesto, it.rol)
            viewModel.consumirLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warehouse,
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "MultiTienda BexAI",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Acceso seguro a inventario",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Seleccione perfil para pruebas rápidas:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val perfiles = listOf(
                        "Operador" to "Operador",
                        "Bodega" to "Encargado Bodega",
                        "Gerencial" to "Gerencial"
                    )
                    perfiles.forEach { (rolId, etiqueta) ->
                        FilterChip(
                            selected = state.rolSeleccionado == rolId,
                            onClick = { viewModel.seleccionarPerfilRapido(rolId) },
                            label = { Text(etiqueta, style = MaterialTheme.typography.bodySmall) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = "Correo corporativo",
            isError = state.errorEmail != null,
            errorMessage = state.errorEmail,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") }
        )

        CustomTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = "Contraseña",
            isError = state.errorPassword != null,
            errorMessage = state.errorPassword,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        BotonAccion(
            texto = "Ingresar",
            isLoading = state.isLoading,
            onClick = viewModel::login
        )
    }
}

