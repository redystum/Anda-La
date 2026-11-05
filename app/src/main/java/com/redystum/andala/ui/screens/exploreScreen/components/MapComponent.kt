package com.redystum.andala.ui.screens.exploreScreen.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.redystum.andala.R
import com.redystum.andala.data.Pousada
import com.redystum.andala.data.remote.LatLngDto
import androidx.compose.ui.graphics.toArgb
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.InfoWindow

@Composable
fun MapComponent(
    screen: Int = 0,
    pousadas: List<Pousada> = listOf(),
    highlightPousada: List<Pousada> = listOf(),
    selectedPousadas: List<Pousada>,
    routePath: List<LatLngDto> = emptyList(),
    routePaths: List<List<LatLngDto>> = emptyList(),
    onPousadaSelected: (Pousada) -> Unit
) {
    val markerCache = remember { mutableMapOf<Pousada, Marker>() }
    val lastFittedPaths = remember { arrayOfNulls<List<List<LatLngDto>>>(1) }
    val routeColor = MaterialTheme.colorScheme.primary.toArgb()

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
                                    map.controller.animateTo(clickedMarker.position)
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


            mapView.overlays.removeAll { it is Polyline }
            // Draw each trip as its own line so all selected legs stay visible.
            val paths = routePaths.ifEmpty { listOf(routePath) }
                .filter { it.size > 1 }
            paths.forEach { path ->
                mapView.overlays.add(
                    Polyline(mapView).apply {
                        setPoints(path.map { GeoPoint(it.latitude, it.longitude) })
                        outlinePaint.color = routeColor
                        outlinePaint.strokeWidth = 9f
                        outlinePaint.strokeCap = android.graphics.Paint.Cap.ROUND
                        outlinePaint.strokeJoin = android.graphics.Paint.Join.ROUND
                    }
                )
            }

            // Keep every selected trip in view, and refit only when a route changes.
            if (paths.isNotEmpty() && lastFittedPaths[0] != paths) {
                lastFittedPaths[0] = paths
                val points = paths.flatten()
                mapView.zoomToBoundingBox(
                    BoundingBox(
                        points.maxOf { it.latitude },
                        points.maxOf { it.longitude },
                        points.minOf { it.latitude },
                        points.minOf { it.longitude }
                    ),
                    true,
                    80
                )
            }

            mapView.invalidate()
        }
    )
}