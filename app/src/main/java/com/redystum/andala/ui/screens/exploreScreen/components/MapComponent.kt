package com.redystum.andala.ui.screens.exploreScreen.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import com.redystum.andala.R
import com.redystum.andala.data.Pousada
import com.redystum.andala.data.remote.LatLngDto
import androidx.compose.ui.graphics.Color
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
    routeColors: List<Color> = emptyList(),
    onPousadaSelected: (Pousada) -> Unit
) {
    val markerCache = remember { mutableMapOf<Pousada, Marker>() }
    val lastFittedPaths = remember { arrayOfNulls<List<List<LatLngDto>>>(1) }
    val defaultRouteColor = MaterialTheme.colorScheme.primary.toArgb()

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
        onRelease = { mapView ->
            mapView.onDetach()
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
            paths.forEachIndexed { index, path ->
                if (path.size > 1) {
                    mapView.overlays.add(
                        Polyline(mapView).apply {
                            setPoints(path.map { GeoPoint(it.latitude, it.longitude) })
                            outlinePaint.color = routeColors.getOrNull(index)?.toArgb()
                                ?: defaultRouteColor
                            outlinePaint.strokeWidth = 9f
                            outlinePaint.strokeCap = android.graphics.Paint.Cap.ROUND
                            outlinePaint.strokeJoin = android.graphics.Paint.Join.ROUND
                        }
                    )
                }
            }

            // Keep every selected trip in view, and refit only when a route changes.
            val visiblePaths = paths.filter { it.size > 1 }
            if (visiblePaths.isNotEmpty() && lastFittedPaths[0] != visiblePaths) {
                val points = visiblePaths.flatten()
                val north = points.maxOf { it.latitude }
                val south = points.minOf { it.latitude }
                val east = points.maxOf { it.longitude }
                val west = points.minOf { it.longitude }

                // zoomToBoundingBox can spin forever when the map has not been
                // measured yet (width/height are 0) or when the box is a single
                // point. Wait for a real layout and always pass a box with a
                // minimum span and a padding that fits the view.
                mapView.doOnLayout {
                    if (lastFittedPaths[0] == visiblePaths) return@doOnLayout

                    val width = mapView.width
                    val height = mapView.height
                    if (width <= 0 || height <= 0) return@doOnLayout

                    lastFittedPaths[0] = visiblePaths
                    val centerLat = (north + south) / 2.0
                    val centerLon = (east + west) / 2.0
                    val halfLat = ((north - south) / 2.0).coerceAtLeast(0.0005)
                    val halfLon = ((east - west) / 2.0).coerceAtLeast(0.0005)
                    val padding = minOf(80, width / 4, height / 4)

                    mapView.zoomToBoundingBox(
                        BoundingBox(
                            centerLat + halfLat,
                            centerLon + halfLon,
                            centerLat - halfLat,
                            centerLon - halfLon
                        ),
                        true,
                        padding
                    )
                }
            }

            mapView.invalidate()
        }
    )
}