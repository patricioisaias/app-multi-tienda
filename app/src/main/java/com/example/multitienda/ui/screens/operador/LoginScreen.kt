package com.example.multitienda.ui.screens.operador

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.multitienda.ui.components.BotonAccion
import com.example.multitienda.ui.components.CustomTextField

@Composable
fun LoginScreen(onLoginSuccess: (rol: String) -> Unit) {
    var email by remember { mutableStateOf("operador@bexai.cl") }
    var password by remember { mutableStateOf("123456") }
    var rolSeleccionado by remember { mutableStateOf("Operador") }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorPassword by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val roles = listOf("Operador", "Encargado Bodega", "Gerencial")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Acceso seguro a inventario BexAI",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Seleccione Perfil de Acceso:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    roles.forEach { rol ->
                        FilterChip(
                            selected = rolSeleccionado == rol,
                            onClick = {
                                rolSeleccionado = rol
                                when (rol) {
                                    "Operador" -> {
                                        email = "operador@bexai.cl"
                                        password = "123456"
                                    }
                                    "Encargado Bodega" -> {
                                        email = "bodega@bexai.cl"
                                        password = "123456"
                                    }
                                    "Gerencial" -> {
                                        email = "gerente@bexai.cl"
                                        password = "admin123"
                                    }
                                }
                                errorEmail = null
                                errorPassword = null
                            },
                            label = { Text(rol, style = MaterialTheme.typography.bodySmall) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = email,
            onValueChange = {
                email = it
                errorEmail = null
            },
            label = "Correo o Usuario Corporativo",
            isError = errorEmail != null,
            errorMessage = errorEmail,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email")
            }
        )

        CustomTextField(
            value = password,
            onValueChange = {
                password = it
                errorPassword = null
            },
            label = "Contraseña",
            isError = errorPassword != null,
            errorMessage = errorPassword,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        BotonAccion(
            texto = "Ingresar al Sistema",
            isLoading = isLoading,
            onClick = {
                var valido = true
                if (email.isBlank()) {
                    errorEmail = "El usuario no puede estar vacío"
                    valido = false
                }
                if (password.length < 4) {
                    errorPassword = "La contraseña debe tener al menos 4 caracteres"
                    valido = false
                }
                if (valido) {
                    isLoading = true
                    onLoginSuccess(rolSeleccionado)
                }
            }
        )
    }
}

