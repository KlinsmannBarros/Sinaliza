package com.example.sinaliza.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeRoute() {
    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        // Optional header / hero area
        Text("Welcome back", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(8.dp))

        // Example list of "reports"
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)) {
            items(sampleReports) { r ->
                ReportCard(title = r.title, description = r.description)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ReportCard(title: String, description: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private val sampleReports = listOf(
    Report("Pothole", "Large pothole on Rua A near building 10"),
    Report("Streetlight down", "Lamp not working in block 7"),
    Report("Trash", "Illegal dumping near the park")
)

data class Report(val title: String, val description: String)
