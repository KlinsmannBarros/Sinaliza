package com.example.sinaliza.feature.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

data class Report(
    val id: String,
    val title: String,
    val description: String,
    val location: LatLng
)

class MapViewModel : ViewModel() {

    private val _reports = mutableStateOf<List<Report>>(emptyList())
    val reports: State<List<Report>> = _reports

    fun addReport(
        title: String,
        description: String,
        location: LatLng
    ) {
        val newReport = Report(
            id = System.currentTimeMillis().toString(),
            title = title,
            description = description,
            location = location
        )

        _reports.value = _reports.value + newReport
    }
}
