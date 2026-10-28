package com.redystum.andala.ui.screens.exploreScreen.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.redystum.andala.R
import com.redystum.andala.data.Pousada
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

@Composable
fun MapComponent(
    screen: Int = 0,
    pousadas: List<Pousada> = listOf(),
    highlightPousada: List<Pousada> = listOf(),
    selectedPousadas: List<Pousada>,
    onPousadaSelected: (Pousada) -> Unit
) {
    val markerCache = remember { mutableMapOf<Pousada, Marker>() }

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


                if (markerCache.isEmpty()) {
                    for (p in pousadas) {
                        val coords = GeoPoint(p.coordinates.first, p.coordinates.second)

                        val customInfoWindow = CustomMarkerInfoWindow(
                            mapView = mapView,
                            pousada = p,
                            isPSelected = selectedPousadas.contains(p),
                            screen = screen
                        ) { clickedPousada ->
                            onPousadaSelected(clickedPousada)
                        }
                        val marker = Marker(mapView).apply {
                            position = coords
                            title = p.name
                            snippet = p.location
                            icon = ContextCompat.getDrawable(mapView.context, R.drawable.hotel)
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            infoWindow = customInfoWindow

                            setOnMarkerClickListener { clickedMarker, map ->
                                InfoWindow.closeAllInfoWindowsOn(map)
                                if (clickedMarker.isInfoWindowOpen) {
                                    clickedMarker.closeInfoWindow()
                                } else {
                                    // some offset fixes because of the custom tooltip
                                    map.controller.animateTo(
                                        GeoPoint(
                                            clickedMarker.position.latitude + 1,
                                            clickedMarker.position.longitude - 0.13
                                        )
                                    )
                                    clickedMarker.showInfoWindow()
                                }
                                true
                            }
                        }
                        mapView.overlays.add(marker)
                        markerCache[p] = marker
                    }

                }

                for ((p, marker) in markerCache) {
                    var drawable = ContextCompat.getDrawable(mapView.context, R.drawable.hotel)
                    marker.closeInfoWindow()

                    if (highlightPousada.contains(p) && selectedPousadas.contains(p)) {
                        drawable = ContextCompat.getDrawable(
                            mapView.context,
                            R.drawable.hotel_selected_highlight
                        )
                    } else if (selectedPousadas.contains(p)) {
                        drawable =
                            ContextCompat.getDrawable(mapView.context, R.drawable.hotel_selected)
                    } else if (highlightPousada.contains(p)) {
                        drawable =
                            ContextCompat.getDrawable(mapView.context, R.drawable.hotel_highlight)

                        marker.showInfoWindow()
                    }

                    marker.icon = drawable
                }


            if (screen == 1) {
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