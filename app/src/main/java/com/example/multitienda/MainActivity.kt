package com.example.multitienda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.multitienda.ui.theme.MultiTiendaTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.multitienda.ui.view.ProductoView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiTiendaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ProductoView()
                        BotonAnimado1()
                    }
                }
            }
        }
    }

    /* elementos animados */
    @Composable
    fun BotonAnimado1() {
        var estado by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        Button(onClick = {
            estado = true
            scope.launch {
                delay(2.seconds)
                estado = false
            }
        }) {
            if (estado) {
                Text(text = "Cargando...")
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.Blue
                )
            } else {
                Text(text = "Pinchame")
            }
        }
    }
}