package kayaklers.sdu.dk.kayaklers.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kayaklers.sdu.dk.kayaklers.R
import kayaklers.sdu.dk.kayaklers.services.LoggerService
import kotlinx.android.synthetic.main.activity_new_log.*
import android.os.IBinder
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.view.WindowManager
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlin.concurrent.fixedRateTimer


class NewLogActivity : AppCompatActivity() {

    var loggerService:LoggerService? = null


    var mConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            loggerService = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val mLocalBinder = service as LoggerService.LocalBinder
            loggerService = mLocalBinder.getService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_log)

        this.btnStartLog.setOnClickListener {startStopLog()}

        this.textDuration.text = "00:00:00"

        //Start LocationMonitoring Service
        val locationTracker = Intent(this@NewLogActivity, LoggerService::class.java)
        bindService(locationTracker, mConnection, Context.BIND_AUTO_CREATE)

        fixedRateTimer("default", false, 0L, 1000){
            runOnUiThread {
                textDuration.text = loggerService?.getElapsedTime() ?: "00:00:00"
                textDistance.text = loggerService?.getApproximateDistance() ?: "0.000"
                debugText.text = "Locations: ${loggerService?.Locations?.count() ?: 0}" +
                        "\nAccelerationData: ${loggerService?.Accelerations?.count() ?: 0}"
            }
        }

    }

    fun startStopLog(){

        if(!loggerService?.isActive!!){
            loggerService?.startTracking()
            btnStartLog.text = "Finish log"
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }else{
            loggerService?.stopTracking()
            btnStartLog.text = "Start log"
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    //Opens up map fragment with and passes in route data to see current route
    fun trackRoute() {

    }
}
