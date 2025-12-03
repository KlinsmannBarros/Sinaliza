package com.example.sinaliza.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileRoute() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Profile", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("User name: Ana Example")
        Spacer(modifier = Modifier.height(4.dp))
        Text("Email: ana@example.com")
    }
}
