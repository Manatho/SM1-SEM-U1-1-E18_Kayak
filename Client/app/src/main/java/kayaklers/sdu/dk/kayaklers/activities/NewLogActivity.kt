package kayaklers.sdu.dk.kayaklers.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kayaklers.sdu.dk.kayaklers.R
import kayaklers.sdu.dk.kayaklers.services.LocationMonitoringService
import kotlinx.android.synthetic.main.activity_new_log.*

class NewLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_log)

        this.btnStartLog.setOnClickListener {startNewLog()}

    }

    fun startNewLog(){
        //Start LocationMonitoring Service
        val locationTracker = Intent(this@NewLogActivity, LocationMonitoringService::class.java)
        startService(locationTracker)
    }

    //Opens up map fragment with and passes in route data to see current route
    fun trackRoute() {

    }
}
