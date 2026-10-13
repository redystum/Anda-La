package com.redystum.andala.ui.screens

import android.preference.PreferenceManager
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.redystum.andala.R
import com.redystum.andala.ui.theme.AndaLaTheme
import com.redystum.andala.ui.theme.AndaPink
import com.redystum.andala.ui.viewmodel.TripViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

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

    AndroidView(
    factory = { ctx ->
        MapView(ctx).apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE) // Default OpenStreetMap tiles
            setMultiTouchControls(true)

            // Centered around Portugal
            controller.setZoom(7.5)
            controller.setCenter(GeoPoint(39.5, -8.0))

            // 1. Example Markers (Pousadas de Juventude)
            val porto = GeoPoint(41.1496, -8.6109)
            val coimbra = GeoPoint(40.2056, -8.4195)
            val lisboa = GeoPoint(38.7223, -9.1393)

            val markerPorto = Marker(this).apply {
                position = porto
                title = "Pousada do Porto"
                icon = ContextCompat.getDrawable(context, R.drawable.baseline_hotel_24)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }

            val markerCoimbra = Marker(this).apply {
                position = coimbra
                title = "Pousada de Coimbra"
                icon = ContextCompat.getDrawable(context, R.drawable.baseline_hotel_24)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }

            val markerLisboa = Marker(this).apply {
                position = lisboa
                title = "Pousada de Lisboa"
                icon = ContextCompat.getDrawable(context, R.drawable.baseline_hotel_24)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }

            overlays.add(markerPorto)
            overlays.add(markerCoimbra)
            overlays.add(markerLisboa)

            // 2. Straight line route between points (Polyline)
            val routeLine = Polyline().apply {
                setPoints(listOf(porto, coimbra, lisboa))
                outlinePaint.color = AndaPink.toArgb() // Matches your theme
                outlinePaint.strokeWidth = 8f
            }

            overlays.add(routeLine)
        }
    },
    update = { mapView ->
        // Triggered on recomposition if dynamic points update
        mapView.invalidate()
    }
    )
}

@Preview(showBackground = true)
@Composable
fun ExplorerPreview() {
    val tripViewModel: TripViewModel = viewModel()

    AndaLaTheme {
        ExploreScreen(tripViewModel) {}
    }
}