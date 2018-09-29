package kayaklers.sdu.dk.kayaklers.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kayaklers.sdu.dk.kayaklers.R
import kayaklers.sdu.dk.kayaklers.services.LocationMonitoringService

class NewLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_log)
    }

    fun createNewLog(){
        //Start LocationMonitoring Service
        val locationService = Intent(this@NewLogActivity, LocationMonitoringService::class.java)
        startService(locationService)
    }

    //Opens up map fragment with and passes in route data to see current route
    fun trackRoute() {

    }
}
