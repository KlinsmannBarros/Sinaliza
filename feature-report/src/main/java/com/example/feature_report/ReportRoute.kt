package com.example.sinaliza.feature.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.core.Report
import com.example.core.ReportRepository
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun ReportRoute(
    navController: NavController,
    latitude: Double?,
    longitude: Double?
) {
    val context = LocalContext.current
    val repository = remember { ReportRepository(context) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val locationText = if (latitude != null && longitude != null) {
        "Lat: %.5f, Lng: %.5f".format(latitude, longitude)
    } else {
        "No location chosen"
    }

    val canSubmit =
        title.isNotBlank() &&
                description.isNotBlank() &&
                latitude != null &&
                longitude != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Report an issue", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(locationText)
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            enabled = canSubmit,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                scope.launch {
                    repository.addReport(
                        Report(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            latitude = latitude!!,
                            longitude = longitude!!
                        )
                    )
                    navController.popBackStack()
                }
            }
        ) {
            Text("Submit Report")
        }
    }
}
