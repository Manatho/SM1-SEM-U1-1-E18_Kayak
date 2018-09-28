package kayaklers.sdu.dk.kayaklers.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import kayaklers.sdu.dk.kayaklers.R
import kayaklers.sdu.dk.kayaklers.data.Log

class MainActivity : AppCompatActivity() {

    private lateinit var btn_se_logs: Button
    private lateinit var btn_ny_log: Button
    private lateinit var btn_kort: Button
    private lateinit var btn_vejr: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_se_logs = findViewById(R.id.se_logs) as Button
        btn_ny_log = findViewById(R.id.ny_log) as Button
        btn_kort = findViewById(R.id.kort) as Button
        btn_vejr = findViewById(R.id.vejr) as Button

        btn_se_logs.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                val data : ArrayList<Log> = ArrayList()
                data.add(Log(86400  * 1000,60 * 1000,10.0, true, 10, ArrayList()))
                data.add(Log(86400  * 1000 * 2,120 * 1000,20.0, true, 20, ArrayList()))
                data.add(Log(86400  * 1000 * 3,180 * 1000,30.0, true, 30, ArrayList()))
                data.add(Log(86400  * 1000 * 4,240 * 1000,40.0, true, 40, ArrayList()))


                val intent = Intent(this@MainActivity, GetLogsActivity::class.java);
                intent.putExtra(DATA_MESSAGE, data)
                startActivity(intent)
            }
        })

        btn_ny_log.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@MainActivity, NewLogActivity::class.java);
                startActivity(intent)
            }
        })

        btn_kort.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@MainActivity, MapActivity::class.java);
                startActivity(intent)
            }
        })

        btn_vejr.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@MainActivity, WeatherActivity::class.java);
                startActivity(intent)
            }
        })
    }
}