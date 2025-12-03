@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.sinaliza.coreui

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AppTopBar(title: String) {
    CenterAlignedTopAppBar(
        title = { Text(title) }
    )
}
