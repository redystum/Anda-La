package com.redystum.andala.ui.screens.planTripScreen.components

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.PlaceAutocomplete
import com.google.android.libraries.places.widget.PlaceAutocompleteActivity

@Composable
fun LocationAutocompleteInput(
    selectedLocation: String?,
    onLocationSelected: (String, LatLng) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val placesClient: PlacesClient = remember { Places.createClient(context) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intent = result.data
        if (result.resultCode == PlaceAutocompleteActivity.RESULT_OK && intent != null) {
            val prediction = PlaceAutocomplete.getPredictionFromIntent(intent)
            prediction?.let {
                val location = it.getFullText(null).toString()
                val request = FetchPlaceRequest.newInstance(
                    it.placeId,
                    listOf(Place.Field.LOCATION)
                )
                placesClient.fetchPlace(request)
                    .addOnSuccessListener { response ->
                        response.place.location?.let { coordinates ->
                            onLocationSelected(location, coordinates)
                        } ?: Log.e(
                            "LocationAutocompleteInput",
                            "Selected place has no coordinates: ${it.placeId}"
                        )
                    }
                    .addOnFailureListener { exception ->
                        Log.e(
                            "LocationAutocompleteInput",
                            "Unable to fetch coordinates for place: ${it.placeId}",
                            exception
                        )
                    }
            }
        }
    }

    val startAutocomplete = {
        val sessionToken = AutocompleteSessionToken.newInstance()
        val intent = PlaceAutocomplete.createIntent(context) {
            setAutocompleteSessionToken(sessionToken)
            setCountries(listOf("PT"))
        }
        launcher.launch(intent)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = if (selectedLocation == null) Icons.Default.LocationOn else Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (selectedLocation == null) "Nenhum local selecionado" else "Local selecionado",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = selectedLocation ?: "Escolha a estação de partida e chegada",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (selectedLocation == null) {
                Button(
                    onClick = startAutocomplete,
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text("Definir", modifier = Modifier.padding(start = 6.dp))
                }
            } else {
                OutlinedButton(
                    onClick = startAutocomplete,
                    contentPadding = ButtonDefaults.ContentPadding
                ) {
                    Text("Alterar")
                }
            }
        }
    }
}