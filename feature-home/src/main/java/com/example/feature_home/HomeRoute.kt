package com.example.sinaliza.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.example.sinaliza.feature.home.HomeReport
import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute() {
    val ctx = LocalContext.current
    val application = ctx.applicationContext as? Application
    if (application == null) {
        Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Text("Unable to access application context")
        }
        return
    }

    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(application))

    val uiState by homeViewModel.uiState.collectAsState()
    val query = uiState.query
    val selectedCategory = uiState.selectedCategory
    val filtered = uiState.reports

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Header
        Text(text = "Welcome back", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Recent community reports nearby",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search + location quick action
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { homeViewModel.setQuery(it) },
                modifier = Modifier.weight(1f),
                label = { Text("Search reports") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { /* TODO: pick location */ }) {
                Icon(Icons.Default.Place, contentDescription = "Location")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category chips row (use simple fallback implementation to avoid Material3 API mismatch)
        val categories = listOf("All", "Pothole", "Lighting", "Trash")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = (selectedCategory == cat) || (cat == "All" && selectedCategory == null)
                SimpleChip(
                    selected = isSelected,
                    onClick = { homeViewModel.setSelectedCategory(if (cat == "All") null else cat) },
                    label = cat
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lazy list of report cards
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered) { report: HomeReport ->
                ReportCard(report = report, onClick = { /* TODO: open detail */ })
            }
        }
    }
}

@Composable
private fun ReportCard(report: HomeReport, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // Thumbnail placeholder (replace with Image when available)
            val thumb = Modifier
                .size(72.dp)
                .clip(MaterialTheme.shapes.small)
                .shadow(1.dp, shape = MaterialTheme.shapes.small)
            Box(modifier = thumb.background(Color(0xFFE6EDF7))) {
                // small centered placeholder
                Image(
                    painter = ColorPainter(Color(0xFFBFD9FF)),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(report.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(report.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(report.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(report.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun SimpleChip(selected: Boolean, onClick: () -> Unit, label: String) {
    val background = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

    androidx.compose.material3.Surface(
        color = background,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = label, color = contentColor)
        }
    }
}
