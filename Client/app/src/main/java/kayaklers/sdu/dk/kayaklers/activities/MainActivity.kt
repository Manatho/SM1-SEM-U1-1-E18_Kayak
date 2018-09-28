package kayaklers.sdu.dk.kayaklers.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import kayaklers.sdu.dk.kayaklers.R

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
                val intent = Intent(this@MainActivity, GetLogsActivity::class.java);
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