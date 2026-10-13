package com.redystum.andala.ui.screens.exploreScreen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.redystum.andala.R
import com.redystum.andala.data.Pousada
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapComponent(
    screen: Int = 0,
    pousadas: List<Pousada> = listOf(),
    highlightPousada: List<Pousada> = listOf(),
    selectedPousadas: MutableList<Pousada>
) {
    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

                // Centered around Portugal
                controller.setZoom(7.5)
                controller.setCenter(GeoPoint(39.5, -8.0))
            }
        },
        update = { mapView ->
            mapView.overlays.removeAll { it is Marker }

            if (screen == 0) {
                for (p in pousadas) {
                    val coords = GeoPoint(p.coordinates.first, p.coordinates.second)

                    var drawable = ContextCompat.getDrawable(mapView.context, R.drawable.hotel)
                    if (selectedPousadas.contains(p)) {
                        drawable = ContextCompat.getDrawable(mapView.context, R.drawable.hotel_selected)
                    }
                    if (highlightPousada.contains(p)) {
                        drawable = ContextCompat.getDrawable(mapView.context, R.drawable.hotel_highlight)
                    }

                    val marker = Marker(mapView).apply {
                        position = coords
                        title = p.name
                        snippet = p.location
                        icon = drawable
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    mapView.overlays.add(marker)
                }
            } else {
                // Straight line route between points (Polyline)
//                val routeLine = Polyline().apply {
//                    setPoints(listOf(porto, coimbra, lisboa))
//                    outlinePaint.color = AndaPink.toArgb() // Matches your theme
//                    outlinePaint.strokeWidth = 8f
//                }
//                overlays.add(routeLine)
            }

            mapView.invalidate()
        }
    )
}