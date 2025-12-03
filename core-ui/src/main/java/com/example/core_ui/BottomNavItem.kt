package com.example.sinaliza.coreui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Report : BottomNavItem("report", "Report", Icons.Default.Create)
    object Map : BottomNavItem("map", "Map", Icons.Default.Map)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}
