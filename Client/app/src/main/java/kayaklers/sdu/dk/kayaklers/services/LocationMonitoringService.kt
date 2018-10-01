package kayaklers.sdu.dk.kayaklers.services

import android.Manifest
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kayaklers.sdu.dk.kayaklers.R
import kayaklers.sdu.dk.kayaklers.data.GPSPoint


class LocationMonitoringService : IntentService("LocationMonitoringService") {


    private val TAG:String = "LOCATIONTRACKER"
    private val CHANNEL_ID = "KayaklersNotificationlers"

    var Locations: ArrayList<GPSPoint> = ArrayList()
        private set

    override fun onHandleIntent(intent: Intent?) {
        Log.e(TAG, "LocationTracker Service Started!")
        createNotificationChannel()
        startTracking()
    }

    fun startTracking(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            val request = LocationRequest()
            request.interval = 1 * 60 * 1000 //Every minute
            request.interval = 1 * 60 * 1000
            request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    onLocationChanged(locationResult!!.getLastLocation())
                }
            }

            fusedLocationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
            showNotification()
        } else {
            Log.e(TAG, "Location permission not granted, nothing will be tracked.")
        }
    }

    fun stopTracking(){
        var manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    fun onLocationChanged(location: Location) {
        Log.i(TAG, "Location tracked, count:\t${Locations.size}")
        Locations.add(GPSPoint(location.latitude, location.longitude, location.altitude))
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(){

        var textTitle = "Kayaklers tracking active"
        var textContent = "Your location is being tracked, tap here to view log"
        var mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, mBuilder.build())
            Log.i("INFO", "Notifcation should be shown now!")
        }
    }


}
