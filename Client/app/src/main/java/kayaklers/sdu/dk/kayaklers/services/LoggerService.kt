package kayaklers.sdu.dk.kayaklers.services

import android.Manifest
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.*
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kayaklers.sdu.dk.kayaklers.data.AccelerometerData
import kayaklers.sdu.dk.kayaklers.data.GPSPoint
import type.LogInput
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer


class LoggerService : IntentService("LoggerService"), SensorEventListener {


    private var currentLogFilename = ""

    private val serverFacade = ServerFacade()
    private lateinit var mSensorManager : SensorManager
    private lateinit var  accelerometer: Sensor
    private val mBinder = LocalBinder()

    private var startTime:Long = 0

    private val TAG:String = "LOGGERSERVICE"

    var isActive = false

    var Locations: ArrayList<GPSPoint> = ArrayList()
        private set

    var Accelerations: ArrayList<AccelerometerData> = ArrayList()
        private set

    override fun onCreate() {
        super.onCreate()
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer =  mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.e(TAG, "LoggerService Started!")
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            onLocationChanged(locationResult!!.lastLocation)
        }
    }

    fun startTracking(){
        currentLogFilename = Date().toString() + ".txt"
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            val request = LocationRequest()
            request.interval = 1000 //Every minute
            request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            fusedLocationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
            this.isActive = true
        } else {
            Log.e(TAG, "Location permission not granted, nothing will be tracked.")
        }

        accelerometer.also { light -> mSensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_FASTEST) }

        startTime = System.currentTimeMillis()
    }

    fun stopTracking(){
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.removeLocationUpdates(locationCallback)
        this.isActive = false

        mSensorManager.unregisterListener(this)

        serverFacade.addLog(Locations)

        Locations.clear()
        Accelerations.clear()
    }

    fun  getElapsedTime() : String{
        if(!isActive) return "00:00:00"
        val tDelta = System.currentTimeMillis() - startTime
        val date = Date(tDelta)
        val dateformat = SimpleDateFormat("HH:mm:ss")
        dateformat.timeZone = TimeZone.getTimeZone("UTC")
        return dateformat.format(date)
    }

    fun getApproximateDistance(): String {
        var accumulativeDistance = 0.0
        val kmConvertion = 1.6093
        if(Locations.count() > 0){

            var lastloc = Locations.first()
            for (loc in Locations){
                accumulativeDistance += distance(lastloc, loc)
                lastloc = loc
            }
        }
        return String.format("%.2f", accumulativeDistance * kmConvertion) + " km";
    }

    private fun distance(a:GPSPoint, b:GPSPoint): Double {
        val theta = a.longitude - b.longitude
        var dist = Math.sin(deg2rad(a.latitude)) * Math.sin(deg2rad(b.latitude)) + (Math.cos(deg2rad(a.latitude))
                * Math.cos(deg2rad(b.latitude))
                * Math.cos(deg2rad(theta)))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }


    fun onLocationChanged(location: Location) {
        Log.i(TAG, "Location logged, count:\t${Locations.size}")
        Locations.add(GPSPoint(Date(), location.latitude, location.longitude, location.altitude))

        //Also save to a file in case server connection glitches
        val path = Environment.getExternalStorageDirectory()
        val letDirectory = File(path, "Kayaklers")
        letDirectory.mkdirs()
        var file = File(letDirectory, currentLogFilename)
        if(!file.exists()) {
            file.createNewFile()

        }
        file.appendText(Date().time.toString() + "," + location.latitude + "," + location.longitude + "," + location.altitude + "\n")

    }


    override fun onSensorChanged(event: SensorEvent?) {
        var x  = event?.values!![0]
        var y  = event?.values!![1]
        var z  = event?.values!![2]
        Accelerations.add(AccelerometerData(x,y,z))
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //nothing
    }

    inner class LocalBinder : Binder() {
        fun getService() : LoggerService {
            return this@LoggerService
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }



}
