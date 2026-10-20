package com.redystum.andala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.android.libraries.places.api.Places
import com.redystum.andala.ui.navigation.AppNavigation
import com.redystum.andala.ui.theme.AndaLaTheme
import com.redystum.andala.BuildConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Places.initializeWithNewPlacesApiEnabled(this, BuildConfig.MAPS_API_KEY)

        setContent {
            AndaLaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(innerPadding)
                }
            }
        }
    }
}