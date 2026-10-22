package com.redystum.andala.ui.screens.exploreScreen.components

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import coil3.load
import coil3.request.crossfade
import com.redystum.andala.R
import com.redystum.andala.data.Pousada
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow
import coil3.request.error

class CustomMarkerInfoWindow(
    mapView: MapView,
    private val pousada: Pousada,
    private val isPSelected: Boolean,
    private val screen: Int,
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

        actionButton.visibility = if (screen == 0) View.VISIBLE else View.GONE

        actionButton?.apply {
            text = if (isPSelected) "Selecionada" else "Selecionar"
            backgroundTintList = null
            setBackgroundResource(
                if (isPSelected) R.drawable.bg_popup_button
                else R.drawable.bg_popup_button_unselected
            )
            setTextColor(
                resources.getColor(
                    if (isPSelected) R.color.white else R.color.anda_pink,
                    context.theme
                )
            )
        }

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