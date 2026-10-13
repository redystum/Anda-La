package com.redystum.andala.ui.screens.exploreScreen

import android.preference.PreferenceManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.redystum.andala.R
import com.redystum.andala.data.Pousada
import com.redystum.andala.data.Pousadas
import com.redystum.andala.ui.screens.exploreScreen.components.ExploreNavBar
import com.redystum.andala.ui.screens.exploreScreen.components.PousadaMapScreen
import com.redystum.andala.ui.theme.AndaLaTheme
import com.redystum.andala.ui.viewmodel.TripViewModel
import org.osmdroid.config.Configuration

@Composable
fun ExploreScreen(
    innerPadding: PaddingValues,
    viewModel: TripViewModel,
    onNavigateToPlan: () -> Unit
) {

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

    Column(
        modifier = Modifier.padding(innerPadding).fillMaxSize()
    ) {
        ExploreNavBar(
            onSearchQueryChanged = { query ->
                val filteredPousadas = pousadas.filter { pousada ->
                    pousada.name.contains(query, ignoreCase = true) || pousada.location.contains(query, ignoreCase = true)
                }
                highlightPousada.clear()
                highlightPousada.addAll(filteredPousadas)
            },
            logoPainter = painterResource(id = R.drawable.ic_launcher_foreground)
        )

        Box(modifier = Modifier.weight(1f)) {
            PousadaMapScreen(pousadas, highlightPousada, selectedPousada, onNavigateToPlan)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ExplorerPreview() {
    val tripViewModel: TripViewModel = viewModel()
    val innerPadding = PaddingValues(16.dp)

    AndaLaTheme {
        ExploreScreen(innerPadding, tripViewModel, {},)
    }
}