package com.example.sinaliza.feature.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.core.Report
import com.example.core.ReportRepository
import com.example.core.toReport
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// ---------------- SAMPLE DEMO MARKERS ----------------

data class SampleMarker(
    val title: String,
    val snippet: String,
    val latLng: LatLng
)

private val sampleMarkers = listOf(
    SampleMarker("Pothole", "Rua A near building 10", LatLng(38.726, -9.140)),
    SampleMarker("Streetlight down", "Block 7", LatLng(38.727, -9.142)),
    SampleMarker("Illegal dumping", "Park area", LatLng(38.724, -9.138))
)

// ---------------- MAP ROUTE ----------------

@Composable
fun MapRoute(
    navController: NavController
) {
    val context = LocalContext.current
    val repository = remember { ReportRepository(context) }

    val reportEntities by repository
        .getReports()
        .collectAsState(initial = emptyList())

    val reports = remember(reportEntities) {
        reportEntities.map { it.toReport() }
    }

    val startLocation = LatLng(38.726, -9.140)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startLocation, 14f)
    }

    var selectedSampleMarker by remember { mutableStateOf<SampleMarker?>(null) }
    var selectedReport by remember { mutableStateOf<Report?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = false),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false
            ),
            onMapLongClick = { latLng ->
                navController.navigate(
                    "report?lat=${latLng.latitude}&lng=${latLng.longitude}"
                )
            }
        ) {

            // 🔹 SAMPLE MARKERS
            sampleMarkers.forEach { marker ->
                Marker(
                    state = MarkerState(marker.latLng),
                    title = marker.title,
                    snippet = marker.snippet,
                    onClick = {
                        selectedSampleMarker = marker
                        selectedReport = null

                        cameraPositionState.position =
                            CameraPosition.fromLatLngZoom(marker.latLng, 16f)

                        false
                    }
                )
            }

            // 🔹 USER REPORT MARKERS
            reports.forEach { report ->
                Marker(
                    state = MarkerState(
                        LatLng(report.latitude, report.longitude)
                    ),
                    title = report.title,
                    snippet = report.description,
                    onClick = {
                        selectedSampleMarker = null
                        selectedReport = report
                        false
                    }
                )
            }
        }

        // ---------------- RESET CAMERA ----------------

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                cameraPositionState.position =
                    CameraPosition.fromLatLngZoom(startLocation, 14f)
            }
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "Reset")
        }

        // ---------------- SAMPLE DETAILS ----------------

        selectedSampleMarker?.let { marker ->
            MarkerBottomCard(
                title = marker.title,
                description = marker.snippet,
                onClose = { selectedSampleMarker = null },
                onCenter = {
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(marker.latLng, 16f)
                }
            )
        }

        // ---------------- REPORT DETAILS ----------------

        selectedReport?.let { report ->
            SimpleBottomCard(
                title = report.title,
                onClose = { selectedReport = null }
            )
        }
    }
}

// ---------------- UI CARDS ----------------

@Composable
private fun MarkerBottomCard(
    title: String,
    description: String,
    onClose: () -> Unit,
    onCenter: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Button(onClick = onCenter) {
                Text("Center on map")
            }
        }
    }
}

@Composable
private fun SimpleBottomCard(
    title: String,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, modifier = Modifier.weight(1f))
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
    }
}
