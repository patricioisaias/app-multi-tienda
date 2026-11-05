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
    val email: String = "",
    val password: String = "",
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val isLoading: Boolean = false
)

data class LoginCredenciales(
    val nombre: String,
    val puesto: String,
    val rol: String
)

private const val PASSWORD = "1234"

private val USUARIOS = mapOf(
    "test1@bexai.cl" to LoginCredenciales("Usuario 1", "Operador de Terreno", "Operador"),
    "test2@bexai.cl" to LoginCredenciales("Usuario 2", "Encargado de Bodega", "Bodega"),
    "test3@bexai.cl" to LoginCredenciales("Usuario 3", "Gerencial", "Gerente")
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
        } else if (password != PASSWORD) {
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
