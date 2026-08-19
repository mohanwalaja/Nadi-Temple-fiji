package com.example.data.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import java.util.TimeZone
import kotlin.coroutines.resume

data class GpsLocationData(
    val latitude: Double,
    val longitude: Double,
    val utcOffsetHours: Double,
    val locationName: String
)

class GpsLocationHelper(private val context: Context) {

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): GpsLocationData? {
        if (!hasLocationPermission()) return null

        val location = fetchFusedLocation() ?: fetchManagerLocation()
        return if (location != null) {
            val tz = TimeZone.getDefault()
            val offsetHours = tz.getOffset(System.currentTimeMillis()) / 3600000.0
            val placeName = resolvePlaceName(location.latitude, location.longitude)
            GpsLocationData(
                latitude = location.latitude,
                longitude = location.longitude,
                utcOffsetHours = offsetHours,
                locationName = placeName
            )
        } else {
            null
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun fetchFusedLocation(): Location? = suspendCancellableCoroutine { cont ->
        try {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            val cts = CancellationTokenSource()
            fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                .addOnSuccessListener { loc ->
                    if (loc != null) {
                        cont.resume(loc)
                    } else {
                        // Try last location
                        fusedClient.lastLocation.addOnSuccessListener { lastLoc ->
                            cont.resume(lastLoc)
                        }.addOnFailureListener {
                            cont.resume(null)
                        }
                    }
                }
                .addOnFailureListener {
                    cont.resume(null)
                }

            cont.invokeOnCancellation {
                cts.cancel()
            }
        } catch (e: Exception) {
            cont.resume(null)
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchManagerLocation(): Location? {
        return try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val providers = locationManager?.getProviders(true) ?: return null
            var bestLocation: Location? = null
            for (provider in providers) {
                val l = locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    bestLocation = l
                }
            }
            bestLocation
        } catch (e: Exception) {
            null
        }
    }

    private fun resolvePlaceName(lat: Double, lon: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                val city = addr.locality ?: addr.subAdminArea ?: addr.adminArea
                val country = addr.countryName
                if (!city.isNullOrBlank() && !country.isNullOrBlank()) {
                    "$city, $country"
                } else if (!city.isNullOrBlank()) {
                    city
                } else if (!country.isNullOrBlank()) {
                    country
                } else {
                    String.format(Locale.US, "GPS (%.2f°, %.2f°)", lat, lon)
                }
            } else {
                String.format(Locale.US, "GPS (%.2f°, %.2f°)", lat, lon)
            }
        } catch (e: Exception) {
            String.format(Locale.US, "GPS (%.2f°, %.2f°)", lat, lon)
        }
    }
}
