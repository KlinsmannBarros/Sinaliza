package com.example.sinaliza.feature.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.core.ReportRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.collect

@Composable
fun MapRoute(
    navController: NavController
) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()

    // Fused location client
    val fusedLocationClient: FusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Runtime permission state
    val initialPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    var permissionGranted by remember { mutableStateOf(initialPermission) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted: Boolean ->
        permissionGranted = granted
    }

    // Load reports from repository (collect Flow as state)
    val repository = remember { ReportRepository(context) }
    val reportEntities by repository
        .getReports()
        .collectAsState(initial = emptyList())

    // Hold a reference to the GoogleMap when it's ready so we can update location layer later
    // Use Compose state instead of kotlin.jvm.internal.Ref.ObjectRef
    val googleMapRef = remember { mutableStateOf<GoogleMap?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { _ -> mapView }, modifier = Modifier.fillMaxSize(), update = { mapView ->
             // Ensure Google Maps system is initialized
             MapsInitializer.initialize(context)

             mapView.getMapAsync { googleMap ->
                googleMapRef.value = googleMap

                // Basic UI settings
                googleGoogleMapUiSettingsSafe(googleMap)

                val startLocation = LatLng(38.726, -9.140)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 14f))

                // Add sample marker
                googleMap.addMarker(
                    MarkerOptions()
                        .position(startLocation)
                        .title("Sample location")
                )

                // Add user's reports as markers
                reportEntities.forEach { entity ->
                    val latLng = LatLng(entity.latitude, entity.longitude)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(entity.title)
                            .snippet(entity.description)
                    )
                }

                // Set location layer based on current permission --- check at call site to satisfy lint
                val hasLocationPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (hasLocationPermission) {
                    try {
                        setMyLocationEnabledSafe(googleMap, permissionGranted)
                    } catch (_: SecurityException) {
                        // ignore if permission not granted at runtime
                    }
                }

                // Attach long-press listener to start Report flow
                googleMap.setOnMapLongClickListener { latLng ->
                    navController.navigate("report?lat=${latLng.latitude}&lng=${latLng.longitude}")
                }
            }
        })

        // If permission is not granted show a small button to request it
        if (!permissionGranted) {
            Box(modifier = Modifier.align(Alignment.TopCenter).padding(12.dp)) {
                Button(onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                    Text("Enable location")
                }
            }
        }

        // Floating button to center: if permission granted -> center on last location, else -> center on start
        FloatingActionButton(
            onClick = {
                val gmap = googleMapRef.value
                if (gmap != null) {
                    // Make an explicit permission check before calling getLastLocation
                    val hasLocationPermissionClick = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (permissionGranted && hasLocationPermissionClick) {
                        try {
                            getLastLocationSafe(fusedLocationClient) { loc: Location? ->
                                if (loc != null) {
                                    val userLatLng = LatLng(loc.latitude, loc.longitude)
                                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                                } else {
                                    // fallback to default
                                    val startLocation = LatLng(38.726, -9.140)
                                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 14f))
                                }
                            }
                        } catch (_: SecurityException) {
                            // ignore
                        }
                    } else if (!permissionGranted) {
                        val startLocation = LatLng(38.726, -9.140)
                        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 14f))
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "Reset")
        }
    }

    // React to permission changes and enable/disable MyLocation on the map
    LaunchedEffect(permissionGranted) {
        val gmap = googleMapRef.value
        if (gmap != null) {
            val hasLocationPermissionLaunched = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (hasLocationPermissionLaunched) {
                try {
                    setMyLocationEnabledSafe(gmap, permissionGranted)
                } catch (_: SecurityException) {
                    // ignore
                }
            }

            // If permission was just granted, attempt to move camera to last location
            if (permissionGranted && hasLocationPermissionLaunched) {
                try {
                    getLastLocationSafe(fusedLocationClient) { loc: Location? ->
                        if (loc != null) {
                            val userLatLng = LatLng(loc.latitude, loc.longitude)
                            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                        }
                    }
                } catch (_: SecurityException) {
                    // ignore
                }
            }
        }
    }
}

// Small helper to set UI settings safely (keeps main code concise)
private fun googleGoogleMapUiSettingsSafe(googleMap: GoogleMap) {
    googleMap.uiSettings.isMapToolbarEnabled = false
    googleMap.uiSettings.isZoomControlsEnabled = true
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val mapViewOnCreateBundle = remember { Bundle() }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onDestroy()
        }
    }

    // Ensure the MapView has been created
    LaunchedEffect(mapView) {
        // Call MapView.onCreate safely. Passing null is fine for our usage;
        // we avoid reflection into private fields which may not exist on some Maps SDK versions.
        try {
            mapView.onCreate(null)
        } catch (e: Exception) {
            // Log and fallback to safe no-op
            android.util.Log.w("MapRoute", "mapView.onCreate failed: $e")
        }
    }

    return mapView
}

// Helper wrappers to call APIs that require location permission. These are annotated to suppress
// the lint 'MissingPermission' error because we call them only after an explicit permission check.
@SuppressLint("MissingPermission")
private fun setMyLocationEnabledSafe(googleMap: GoogleMap, enabled: Boolean) {
    googleMap.isMyLocationEnabled = enabled
}

@SuppressLint("MissingPermission")
private fun getLastLocationSafe(fused: FusedLocationProviderClient, callback: (Location?) -> Unit) {
    fused.lastLocation.addOnSuccessListener { loc: Location? ->
        callback(loc)
    }
}
