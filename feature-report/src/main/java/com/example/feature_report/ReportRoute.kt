package com.example.sinaliza.feature.report

import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReportRoute(
    reportId: String? = null
) {

    // Prefill title if coming from map
    var title by remember {
        mutableStateOf(reportId ?: "")
    }

    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Select category") }
    var attachmentsCount by remember { mutableStateOf(0) }
    var locationText by remember {
        mutableStateOf(
            if (reportId != null) "Location selected from map"
            else "No location chosen"
        )
    }

    val canSubmit = title.isNotBlank() && description.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        // Title
        Text(
            text = if (reportId == null) "Report an issue" else "Edit report",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Give us a short title and a clear description. Add a photo and location if available.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title input
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category & Location
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ElevatedCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(2.dp)
            ) {
                TextButton(
                    onClick = { /* TODO: category picker */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(category)
                }
            }

            ElevatedCard(
                modifier = Modifier.width(140.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(2.dp)
            ) {
                TextButton(
                    onClick = { /* TODO: location picker */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text("Location")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Attachments
        Text("Attachments", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Card(
                modifier = Modifier
                    .size(96.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        if (attachmentsCount > 0)
                            "$attachmentsCount photo(s)"
                        else
                            "No photo"
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Button(onClick = { attachmentsCount++ }) {
                    Text("Add photo")
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Photos help us identify issues faster",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Location preview
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    locationText,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = { /* TODO */ }) {
                    Text("Choose")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit
        Button(
            onClick = {
                // TODO: submit report
            },
            enabled = canSubmit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Report")
        }
    }
}
