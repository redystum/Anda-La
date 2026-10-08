package com.redystum.andala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.redystum.andala.ui.theme.AndaLaTheme
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.vector.ImageVector

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndaLaTheme {
                AndaLaApp()
            }
        }
    }
}

@Composable
fun AndaLaApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.MAP) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.label,
                            tint = if (it.active) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // TODO: add top bar
            when (currentDestination) {
                AppDestinations.MAP -> MapScreen(innerPadding)
                AppDestinations.CONFIG -> ConfigScreen(innerPadding)
                AppDestinations.RESULT -> ResultScreen(innerPadding)
            }
        }
    }
}

@Composable
fun MapScreen(innerPadding: PaddingValues) {
    Greeting(name = "Mapa", modifier = Modifier.padding(innerPadding))
}

@Composable
fun ConfigScreen(innerPadding: PaddingValues) {
    Greeting(name = "Configurações", modifier = Modifier.padding(innerPadding))
}

@Composable
fun ResultScreen(innerPadding: PaddingValues) {
    Greeting(name = "Resultado", modifier = Modifier.padding(innerPadding))
}


enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    var selected: Boolean = false,
    var active: Boolean = false
) {
    MAP("Mapa", Icons.Default.Map, true, true),
    CONFIG("Configuração", Icons.Default.Tune),
    RESULT("Resultado", Icons.Default.Route),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndaLaTheme {
        AndaLaApp()
    }
}