package com.redystum.andala.ui.screens.exploreScreen.components

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.redystum.andala.R
import com.redystum.andala.data.Pousada
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow
import coil3.request.error

class CustomMarkerInfoWindow(
    mapView: MapView,
    private val pousada: Pousada,
    private val onActionClick: (Pousada) -> Unit
) : InfoWindow(R.layout.custom_info_window, mapView) {

    override fun onOpen(item: Any?) {
        mView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // fix offset
        mView.translationX = -15f

        val titleView = mView.findViewById<TextView>(R.id.info_title)
        val snippetView = mView.findViewById<TextView>(R.id.info_snippet)
        val actionButton = mView.findViewById<Button>(R.id.btn_action)
        val imageView = mView.findViewById<ImageView>(R.id.info_image)


        titleView?.text = pousada.name
        snippetView?.text = pousada.location

        imageView?.load(pousada.imageUrl) {
            crossfade(true)
            error(R.drawable.hotel)
        }

        mView.setOnClickListener {
            close()
        }

        actionButton?.setOnClickListener {
            onActionClick(pousada)
            close()
        }
    }

    override fun onClose() {
        mView.translationX = 0f
    }
}