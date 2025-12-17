package com.example.sinaliza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sinaliza.coreui.AppTopBar
import com.example.sinaliza.coreui.BottomNavItem
import com.example.sinaliza.coreui.BottomNavigationBar
import com.example.sinaliza.coreui.SinalizaCoreTheme
import com.example.sinaliza.feature.home.HomeRoute
import com.example.sinaliza.feature.map.MapRoute
import com.example.sinaliza.feature.profile.ProfileRoute
import com.example.sinaliza.feature.report.ReportRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Report,
        BottomNavItem.Map,
        BottomNavItem.Profile
    )

    SinalizaCoreTheme {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val title = when (currentRoute) {
            BottomNavItem.Home.route -> "Home"
            BottomNavItem.Report.route -> "Report"
            BottomNavItem.Map.route -> "Map"
            BottomNavItem.Profile.route -> "Profile"
            else -> "Sinaliza"
        }

        Scaffold(
            topBar = { AppTopBar(title = title) },
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    items = items
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeRoute()
                }
                composable(BottomNavItem.Report.route) {
                    ReportRoute()
                }
                composable(BottomNavItem.Map.route) {
                    MapRoute()
                }
                composable(BottomNavItem.Profile.route) {
                    ProfileRoute()
                }
            }
        }
    }
}
