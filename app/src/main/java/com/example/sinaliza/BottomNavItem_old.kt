package com.example.sinaliza

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem_old(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem_old("home", Icons.Filled.Home, "Home")
    object Report : BottomNavItem_old("report", Icons.Filled.Create, "Report")
    object Map : BottomNavItem_old("map", Icons.Filled.LocationOn, "Map")
    object Profile : BottomNavItem_old("profile", Icons.Filled.Person, "Profile")
}
