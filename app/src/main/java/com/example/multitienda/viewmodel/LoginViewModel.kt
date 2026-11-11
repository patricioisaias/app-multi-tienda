package com.example.multitienda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "test1@bexai.cl",
    val password: String = "1234",
    val rolSeleccionado: String = "Operador",
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val isLoading: Boolean = false
)

data class LoginCredenciales(
    val nombre: String,
    val puesto: String,
    val rol: String
)

private val PASSWORDS = setOf("1234", "123456", "admin123")

private val USUARIOS = mapOf(
    "test1@bexai.cl" to LoginCredenciales("Patricio Céspedes", "Operador de Terreno", "Operador"),
    "test2@bexai.cl" to LoginCredenciales("Daniel Aedo", "Encargado de Bodega", "Bodega"),
    "test3@bexai.cl" to LoginCredenciales("Angelo Ponce", "Gerencial", "Gerencial"),
    "operador@bexai.cl" to LoginCredenciales("Patricio Céspedes", "Operador de Terreno", "Operador"),
    "bodega@bexai.cl" to LoginCredenciales("Daniel Aedo", "Encargado de Bodega", "Bodega"),
    "gerente@bexai.cl" to LoginCredenciales("Angelo Ponce", "Gerencial", "Gerencial")
)

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _loginExitoso = MutableStateFlow<LoginCredenciales?>(null)
    val loginExitoso: StateFlow<LoginCredenciales?> = _loginExitoso.asStateFlow()

    fun onEmailChange(valor: String) {
        _state.update { it.copy(email = valor, errorEmail = null) }
    }

    fun onPasswordChange(valor: String) {
        _state.update { it.copy(password = valor, errorPassword = null) }
    }

    fun seleccionarPerfilRapido(rol: String) {
        val (email, pass) = when (rol) {
            "Operador" -> "test1@bexai.cl" to "1234"
            "Bodega" -> "test2@bexai.cl" to "1234"
            "Gerencial" -> "test3@bexai.cl" to "1234"
            else -> "" to ""
        }
        _state.update {
            it.copy(
                rolSeleccionado = rol,
                email = email,
                password = pass,
                errorEmail = null,
                errorPassword = null
            )
        }
    }

    fun login() {
        val s = _state.value
        val email = s.email.trim()
        val password = s.password.trim()

        var errorEmail: String? = null
        var errorPassword: String? = null

        if (email.isEmpty()) {
            errorEmail = "El correo es obligatorio"
        } else if (!email.contains("@")) {
            errorEmail = "Correo inválido"
        } else if (USUARIOS[email] == null) {
            errorEmail = "Usuario no registrado"
        }
        if (password.isEmpty()) {
            errorPassword = "La contraseña es obligatoria"
        } else if (!PASSWORDS.contains(password)) {
            errorPassword = "Contraseña incorrecta"
        }

        _state.update {
            it.copy(
                errorEmail = errorEmail,
                errorPassword = errorPassword
            )
        }

        if (errorEmail != null || errorPassword != null) {
            return
        }

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            delay(350)
            _state.update { it.copy(isLoading = false) }
            _loginExitoso.value = USUARIOS[email]!!
        }
    }

    fun consumirLogin() {
        _loginExitoso.value = null
    }
}
