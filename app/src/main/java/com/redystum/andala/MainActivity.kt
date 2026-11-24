package com.redystum.andala

import android.content.Context
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
import android.preference.PreferenceManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.redystum.andala.ui.screens.onBoardinScreen.OnboardingPage
import com.redystum.andala.ui.screens.onBoardinScreen.OnboardingScreen
import org.osmdroid.config.Configuration
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        Configuration.getInstance().userAgentValue = packageName

        Places.initializeWithNewPlacesApiEnabled(this, BuildConfig.MAPS_API_KEY)

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val hasSeenOnboarding = prefs.getBoolean("onboarding_completed", false)

        setContent {
            AndaLaTheme {
                var showOnboarding by remember { mutableStateOf(!hasSeenOnboarding) }

                if (showOnboarding) {
                    OnboardingScreen(
                        pages = listOf(
                            OnboardingPage(
                                "Veja as pousadas",
                                "Clique para ver mais detalhes sobre cada pousada no mapa!",
                                null,
                                null,
                                null,
                                R.raw.step_1
                            ),
                            OnboardingPage(
                                "Selecione as pousadas",
                                "Selecione as pousada que seja ficar para planear a sua rota.",
                                null,
                                null,
                                null,
                                R.raw.step_2
                            ),
                            OnboardingPage(
                                "Organize a viagem",
                                "Escolha o local de partida e a ordem das pousadas",
                                null,
                                null,
                                null,
                                R.raw.step_3
                            ),
                            OnboardingPage(
                                "Veja os itinerarios possíveis",
                                "Veja os itinerários possíveis com base nas pousadas selecionadas.",
                                null,
                                null,
                                null,
                                R.raw.step_4
                            )
                        ),
                        onFinish = {
                            prefs.edit { putBoolean("onboarding_completed", true) }
                            showOnboarding = false
                        }
                    )
                } else {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AppNavigation(innerPadding)
                    }
                }
            }
        }
    }
}