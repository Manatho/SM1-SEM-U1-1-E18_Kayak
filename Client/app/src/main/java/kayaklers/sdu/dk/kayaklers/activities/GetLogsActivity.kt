package kayaklers.sdu.dk.kayaklers.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import kayaklers.sdu.dk.kayaklers.R
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kayaklers.sdu.dk.kayaklers.data.Log
import java.util.*
import kotlin.collections.ArrayList

const val DATA_MESSAGE = "kayaklers.sdu.dk.kayaklers.activities.GetLogsActivity.DATA_MESSAGE"

class GetLogsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_logs)

        /*val data : ArrayList<Log> = ArrayList()
        data.add(Log(86400  * 1000,60 * 1000,10.0, true, 10, ArrayList()))
        data.add(Log(86400  * 1000 * 2,120 * 1000,20.0, true, 20, ArrayList()))
        data.add(Log(86400  * 1000 * 3,180 * 1000,30.0, true, 30, ArrayList()))
        data.add(Log(86400  * 1000 * 4,240 * 1000,40.0, true, 40, ArrayList()))*/

        var data = intent.getSerializableExtra(DATA_MESSAGE)


        val rv : RecyclerView = findViewById(R.id.list)
        rv.layoutManager = LinearLayoutManager(this)

        if(data != null){
            rv.adapter = LogAdapter((data as ArrayList<Log>).toTypedArray(), this)
        } else {
            rv.adapter = LogAdapter(arrayOf(), this)
        }

    }

    class LogAdapter(private val logs : Array<Log>, private val activity : AppCompatActivity) : RecyclerView.Adapter<LogAdapter.MyViewHolder>() {

        override fun getItemCount() = logs.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.log_overview_entry, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val log = logs[position]
            holder.display(log)
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val date: TextView
            private val startTime: TextView
            private val endTime: TextView

            private val distance: TextView
            private val points: TextView

            private var currentLog: Log? = null

            init {

                date = itemView.findViewById(R.id.date)
                startTime = itemView.findViewById(R.id.timestart)
                endTime = itemView.findViewById(R.id.timeend)
                distance = itemView.findViewById(R.id.distance)
                points = itemView.findViewById(R.id.points)

                itemView.setOnClickListener {
                    AlertDialog.Builder(itemView.context)
                            .setTitle(currentLog!!.startTime.toString())
                            .setMessage(currentLog!!.distance.toString())
                            .setPositiveButton("Map", ({d,_->
                                //pass currentLog as bundle to MapActivity
                                val intent = Intent(activity, MapActivity::class.java)
                                val bundle = Bundle()
                                bundle.putParcelable("selected_log", currentLog)
                                intent.putExtra("logBundle", bundle)
                                activity.startActivity(intent)
                            }))
                            .setNegativeButton("Close", ({d, _ -> d.dismiss()}))
                            .show()

                }
            }

            @SuppressLint("SetTextI18n")
            fun display(log: Log) {
                currentLog = log

                val logDate = Date(log.startTime)
                val logEndDate = Date(log.startTime + log.duration)

                date.text = DateFormat.format("dd-MMM-yyyy", logDate)
                startTime.text = DateFormat.format("HH:mm", logDate)
                endTime.text = DateFormat.format("HH:mm", logEndDate)
                distance.text = log.distance.toString() + "m"
                points.text = log.points.toString()
            }
        }

    }
}
