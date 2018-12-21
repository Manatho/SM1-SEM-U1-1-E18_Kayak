package kayaklers.sdu.dk.kayaklers.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import kayaklers.sdu.dk.kayaklers.R
import kayaklers.sdu.dk.kayaklers.data.GPSPoint
import kayaklers.sdu.dk.kayaklers.data.Log
import kayaklers.sdu.dk.kayaklers.services.ServerFacade
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var btn_logs: Button
    private lateinit var btn_new_log: Button
    private lateinit var btn_map: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_logs = findViewById(R.id.logs) as Button
        btn_new_log = findViewById(R.id.new_log) as Button
        btn_map = findViewById(R.id.map) as Button

        btn_logs.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                /*
                val data : ArrayList<Log> = ArrayList()
                data.add(Log(Date(),null, 60 * 1000,10.0, true, 10, ArrayList()))
                data.add(Log(Date(), null,120 * 1000,20.0, true, 20, ArrayList()))
                data.add(Log(Date(), null, 180 * 1000,30.0, true, 30, ArrayList()))
                data.add(Log(Date(), null, 240 * 1000,40.0, true, 40, ArrayList()))


                */
                val intent = Intent(this@MainActivity, GetLogsActivity::class.java);
                //intent.putExtra(DATA_MESSAGE, data)

                startActivity(intent)
            }
        })

        btn_new_log.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@MainActivity, NewLogActivity::class.java);
                startActivity(intent)
            }
        })

        btn_map.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@MainActivity, MapActivity::class.java);
                startActivity(intent)
            }
        })
    }
}