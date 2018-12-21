package kayaklers.sdu.dk.kayaklers.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.Network
import android.os.*
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kayaklers.sdu.dk.kayaklers.data.AccelerometerData
import kayaklers.sdu.dk.kayaklers.data.GPSPoint
import java.io.File
import java.lang.Math.abs
import java.lang.Math.round
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class LoggerService : IntentService("LoggerService"), SensorEventListener {


    private lateinit var connectivityManager: ConnectivityManager

    private val logDirectory = File(Environment.getExternalStorageDirectory(), "Kayaklers")
    private var currentLogFilename = ""

    private val serverFacade = ServerFacade()
    private lateinit var mSensorManager : SensorManager
    private lateinit var  compass: Sensor
    private val mBinder = LocalBinder()

    private var startTime:Long = 0

    private val TAG:String = "LOGGERSERVICE"

    var GPSInterval = 1000L
    var minGPSInterval = 1000L
    var maxGPSInterval = 30000L
    private var lastIntervalUpdate: Long = 0

    var isActive = false

    var Locations: ArrayList<GPSPoint> = ArrayList()
        private set


    @SuppressLint("NewApi")
    override fun onCreate() {
        super.onCreate()
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        compass =  mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.e(TAG, "LoggerService Started!")
    }



    @SuppressLint("MissingPermission")
    fun updateGPSInterval(){
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.removeLocationUpdates(locationCallback)

        val request = LocationRequest()
        request.interval = this.GPSInterval
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
    }

    private var hasNetworkConnection = false
    private val networkCallback = @SuppressLint("NewApi")
    object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            hasNetworkConnection = true
        }

        override fun onLost(network: Network?) {
            hasNetworkConnection = false
        }
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
            request.interval = this.GPSInterval
            request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            fusedLocationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
            this.isActive = true
        } else {
            Log.e(TAG, "Location permission not granted, nothing will be tracked.")
        }

        compass.also { light -> mSensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_FASTEST) }

        startTime = System.currentTimeMillis()
    }

    fun stopTracking() {
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.removeLocationUpdates(locationCallback)
        this.isActive = false

        mSensorManager.unregisterListener(this)

        if (hasNetworkConnection) {
            serverFacade.addLog(Locations)
            logDirectory.resolve(currentLogFilename).delete()
        }

        Locations.clear()
    }

    fun uploadFinishedLogs() {
        println(logDirectory.list()[0])
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
        logDirectory.mkdirs()
        var file = File(logDirectory, currentLogFilename)
        if(!file.exists()) {
            file.createNewFile()

        }
        file.appendText(Date().time.toString() + "," + location.latitude + "," + location.longitude + "," + location.altitude + "\n")
    }

    private var prevOrientation: Float = 0.0f
    private var lastGPSUpdate:Long = 0L
    var interpolationFactor = 1.0f
    override fun onSensorChanged(event: SensorEvent?) {
        var orientation = event?.values!![2]
        var diff = prevOrientation - orientation

        var timeSinceLast = System.currentTimeMillis() - lastIntervalUpdate
        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.CEILING
        if(timeSinceLast > 250L) {
            if (abs(diff) > 0.1) {
                prevOrientation = orientation
                interpolationFactor -= 0.1f
            }else{
                interpolationFactor += 0.005f + (interpolationFactor*0.01f)
            }
            interpolationFactor = Math.max(0f, Math.min(1f, interpolationFactor))
            lastIntervalUpdate = System.currentTimeMillis()
            GPSInterval = (interpolationFactor * maxGPSInterval + (1.0f - interpolationFactor) * minGPSInterval).toLong()

            if((System.currentTimeMillis() - lastGPSUpdate) >= GPSInterval){
                updateGPSInterval()
                lastGPSUpdate = System.currentTimeMillis()
            }
        }
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
