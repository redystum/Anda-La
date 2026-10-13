package com.redystum.andala.ui.screens.exploreScreen

import android.preference.PreferenceManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.redystum.andala.data.Pousada
import com.redystum.andala.data.Pousadas
import com.redystum.andala.ui.screens.exploreScreen.components.PousadaMapScreen
import com.redystum.andala.ui.theme.AndaLaTheme
import com.redystum.andala.ui.viewmodel.TripViewModel
import org.osmdroid.config.Configuration

@Composable
fun ExploreScreen(viewModel: TripViewModel, onNavigateToPlan: () -> Unit) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            PreferenceManager.getDefaultSharedPreferences(context)
        )
        Configuration.getInstance().userAgentValue = context.packageName
    }

    val pousadas = Pousadas.listaPousadas
    val highlightPousada = remember { mutableStateListOf<Pousada>() }
    val selectedPousada = remember { mutableStateListOf<Pousada>() }

    PousadaMapScreen(pousadas, highlightPousada, selectedPousada, onNavigateToPlan )

}


@Preview(showBackground = true)
@Composable
fun ExplorerPreview() {
    val tripViewModel: TripViewModel = viewModel()

    AndaLaTheme {
        ExploreScreen(tripViewModel) {}
    }
}