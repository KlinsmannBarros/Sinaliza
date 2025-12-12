package com.example.sinaliza.feature.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

// sample markers
private val sampleMarkers = listOf(
    SampleMarker("Pothole", "Rua A near building 10", LatLng(38.726, -9.140)),
    SampleMarker("Streetlight down", "Block 7", LatLng(38.727, -9.142)),
    SampleMarker("Illegal dumping", "Park area", LatLng(38.724, -9.138))
)

data class SampleMarker(val title: String, val snippet: String, val latLng: LatLng)

@Composable
fun MapRoute() {
    val start = sampleMarkers.firstOrNull()?.latLng ?: LatLng(38.726, -9.140)
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(start, 14f)
    }

    val coroutineScope = rememberCoroutineScope()
    var selectedMarker by remember { mutableStateOf<SampleMarker?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = false),
            uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
        ) {
            sampleMarkers.forEach { marker ->
                Marker(
                    state = MarkerState(position = marker.latLng),
                    title = marker.title,
                    snippet = marker.snippet,
                    onClick = {
                        selectedMarker = marker
                        coroutineScope.launch {
                            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(marker.latLng, 16f))
                        }
                        false
                    }
                )
            }
        }

        ElevatedCard(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopStart),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Map", style = MaterialTheme.typography.titleSmall)
                Text("Tap markers for details", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FloatingActionButton(onClick = {
                selectedMarker?.let { m ->
                    coroutineScope.launch {
                        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(m.latLng, 16f))
                    }
                }
            }) {
                Icon(Icons.Default.Place, contentDescription = "Center")
            }

            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(start, 14f))
                }
            }) {
                Icon(Icons.Default.MyLocation, contentDescription = "Reset")
            }
        }

        selectedMarker?.let { marker ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(marker.title, style = MaterialTheme.typography.titleMedium)
                            Text(marker.snippet, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { selectedMarker = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { /* TODO: details */ }) {
                            Text("View")
                        }
                        Button(onClick = {
                            coroutineScope.launch {
                                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(marker.latLng, 16f))
                            }
                        }) {
                            Text("Center")
                        }
                    }
                }
            }
        }
    }
}
