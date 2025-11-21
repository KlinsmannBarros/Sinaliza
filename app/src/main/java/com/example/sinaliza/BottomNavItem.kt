package com.example.sinaliza

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Home")
    object Report : BottomNavItem("report", Icons.Filled.Create, "Report")
    object Map : BottomNavItem("map", Icons.Filled.LocationOn, "Map")
    object Profile : BottomNavItem("profile", Icons.Filled.Person, "Profile")
}
