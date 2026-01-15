package com.example.sinaliza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sinaliza.coreui.AppTopBar
import com.example.sinaliza.coreui.BottomNavItem
import com.example.sinaliza.coreui.BottomNavigationBar
import com.example.sinaliza.coreui.SinalizaCoreTheme
import com.example.sinaliza.feature.home.HomeRoute
import com.example.sinaliza.feature.map.MapRoute
import com.example.sinaliza.feature.profile.ProfileRoute
import com.example.sinaliza.feature.report.ReportRoute
import android.util.Log
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install a default uncaught exception handler that writes the stacktrace to a file
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, ex ->
            try {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                ex.printStackTrace(pw)
                pw.flush()
                val text = sw.toString()
                val file = File(applicationContext.filesDir, "crash_${'$'}{System.currentTimeMillis()}.txt")
                file.writeText(text)
                Log.e("MainActivity", "Wrote crash log to ${file.absolutePath}")
            } catch (_: Throwable) {
                // ignore
            }
            // Delegate to original handler to let the system handle the crash as usual
            defaultHandler?.uncaughtException(thread, ex)
        }

        super.onCreate(savedInstanceState)

        setContent {
            MyApp()
        }
    }
}

@Composable
fun MinimalStartupProbe() {
    androidx.compose.material3.Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFEEEEEE)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            androidx.compose.material3.Text(text = "MINIMAL STARTUP - Compose is running", color = Color.Black)
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

        val title = when {
            currentRoute?.startsWith("report") == true -> "Report"
            currentRoute == BottomNavItem.Home.route -> "Home"
            currentRoute == BottomNavItem.Map.route -> "Map"
            currentRoute == BottomNavItem.Profile.route -> "Profile"
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

            // DEBUG: temporary bypass to render HomeRoute directly.
            // This helps determine whether NavHost/destination composables are responsible for the black screen.
            // Set to false to restore normal NavHost behavior.
            val debugBypassNav = false

            if (debugBypassNav) {
                // Visible debug overlay to confirm Compose rendering
                DebugVisibilityProbe()
                HomeRoute()
            } else {
                NavHost(
                    navController = navController,
                    startDestination = BottomNavItem.Home.route,
                    modifier = Modifier.padding(innerPadding)
                ) {

                    composable(BottomNavItem.Home.route) {
                        HomeRoute(onViewOnMap = { lat, lng ->
                            // navigate to map with coordinates using args; save/restore state for smooth back behavior
                            navController.navigate("map?lat=${lat}&lng=${lng}") {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(BottomNavItem.Home.route) {
                                    saveState = true
                                }
                            }
                        })
                    }

                    composable(
                        route = "map?lat={lat}&lng={lng}",
                        arguments = listOf(
                            navArgument("lat") {
                                type = NavType.StringType
                                nullable = true
                            },
                            navArgument("lng") {
                                type = NavType.StringType
                                nullable = true
                            }
                        )
                    ) { backStackEntry ->

                        val lat = backStackEntry.arguments
                            ?.getString("lat")
                            ?.toDoubleOrNull()

                        val lng = backStackEntry.arguments
                            ?.getString("lng")
                            ?.toDoubleOrNull()

                        MapRoute(navController = navController, centerLat = lat, centerLng = lng)
                    }

                    // Keep a plain map route (no args) so bottom nav can navigate directly
                    composable(BottomNavItem.Map.route) {
                        MapRoute(navController = navController)
                    }

                    composable(
                        route = "report?lat={lat}&lng={lng}",
                        arguments = listOf(
                            navArgument("lat") {
                                type = NavType.StringType
                                nullable = true
                            },
                            navArgument("lng") {
                                type = NavType.StringType
                                nullable = true
                            }
                        )
                    ) { backStackEntry ->

                        val lat = backStackEntry.arguments
                            ?.getString("lat")
                            ?.toDoubleOrNull()

                        val lng = backStackEntry.arguments
                            ?.getString("lng")
                            ?.toDoubleOrNull()

                        ReportRoute(
                            navController = navController,
                            latitude = lat,
                            longitude = lng
                        )
                    }

                    composable(BottomNavItem.Profile.route) {
                        ProfileRoute()
                    }
                }
            }
        }
    }
}

@Composable
private fun DebugVisibilityProbe() {
    // This draws a thin banner at the top so we can see whether Compose is rendering
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .background(Color.Red)
    ) {
        androidx.compose.material3.Text(
            text = "DEBUG: UI RENDERING",
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
