package kayaklers.sdu.dk.kayaklers.services

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kayaklers.sdu.dk.kayaklers.apollo.ApolloClient
import kayaklers.sdu.dk.kayaklers.data.GPSPoint
import kayaklers.sdu.dk.kayaklers.data.Log
import org.jetbrains.annotations.NotNull
import type.GPSPointInput
import type.LogInput
import java.util.*

class ServerFacade: IServer{

    private val TAG: String = "ServerFacade"
    private val apolloClient = ApolloClient.setupApollo()

    override fun getLogs(cb : Callback<MutableList<Log>>): MutableList<Log>? {
        var logs: MutableList<Log> = mutableListOf()
        apolloClient.query(
                AllLogsQuery.builder().build()).enqueue(object: ApolloCall.Callback<AllLogsQuery.Data>() {
            override fun onFailure(@NotNull e: ApolloException) {
                android.util.Log.e(TAG, e.message.toString())
            }

            override fun onResponse(@NotNull response:  Response<AllLogsQuery.Data>) {
                response.data()?.allLogs()?.forEach {
                    var gpsPoints: MutableList<GPSPoint> = mutableListOf()
                    for (gpsPoint in it.gpsPoints().orEmpty()){
                        gpsPoints.add(GPSPoint(gpsPoint?.time() as Date, gpsPoint?.latitude()!!.toDouble(), gpsPoint?.longitude()!!.toDouble(), gpsPoint?.altitude()!!.toDouble(), gpsPoint?.valid() as Boolean, gpsPoint?.speed()!!.toDouble()))
                    }
                    val startTime : Date? = it?.startTime() as Date
                    val endTime : Date? = it?.endTime() as Date
                    val duration : Long? = it.duration()?.toLong()
                    val distance : Double? = it?.distance()
                    val points : Int? = it?.points()
                    logs.add(Log(startTime!!, endTime!!, duration!!, distance!!, points!!, gpsPoints))
                    cb.call(logs)
                }
            }
        })
        return logs
    }

    override fun getLog(id: String, cb : Callback<Log>): Log? {
        lateinit var log : Log
        apolloClient.query(
                LogQuery.builder().id(id).build()).enqueue(object: ApolloCall.Callback<LogQuery.Data>() {
            override fun onFailure(@NotNull e: ApolloException) {
                android.util.Log.e(TAG, e.message.toString())
            }

            override fun onResponse(@NotNull response: Response<LogQuery.Data>) {
                if(response.data() == null) return

                var gpsPoints: MutableList<GPSPoint> = mutableListOf()
                response.data()?.log()?.gpsPoints()?.forEach{
                    gpsPoints.add(GPSPoint(Date(it?.time() as Long), it?.latitude()!!.toDouble(), it?.longitude()!!.toDouble(), it?.altitude()!!.toDouble(), it?.valid() as Boolean, it?.speed()!!.toDouble()))
                }

                var it:LogQuery.Log = response.data()!!.log()!!

                val startTime : Date? = it.startTime() as Date
                val endTime : Date? = it.endTime() as Date
                val duration : Long? = it.duration()?.toLong()
                val distance : Double? = it.distance()
                val points : Int? = it?.points()
                log = Log(startTime!!, endTime!!, duration!!, distance!!, points!!, gpsPoints)
                cb.call(log)
            }
            })
        return log
    }

    override fun addLog(gpsPoints : MutableList<GPSPoint>) {

        var gpsPointsInput = gpsPoints.map {
            GPSPointInput.builder()
                    .time(it.time)
                    .longitude(it.longitude)
                    .latitude(it.latitude)
                    .altitude(it.altitude)
                    .build()
        }
        var logInput = LogInput.builder().gpsPoints(gpsPointsInput).build()

        //TODO: get startTime and endTime from gps points

        apolloClient.mutate(
                CreateLogMutation.builder().logInput(logInput)
                        .build()
        ).enqueue(object: ApolloCall.Callback<CreateLogMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                android.util.Log.e(TAG, e.message.toString())
            }

            override fun onResponse(response: Response<CreateLogMutation.Data>) {
                println(response.errors().count())
            }
        })
    }


    override fun getLogCount(): Int {
        var count : Int = 0
        getLogs(cb = object : Callback<MutableList<Log>> {
            override fun call(logs: MutableList<Log>) {
                for(log in logs) {
                    count += 1
                }
            }
        })
        return count
    }

    override fun getTotalTravelTime(): Long {
        var totalTravelTime : Long = 0
        getLogs(cb = object : Callback<MutableList<Log>> {
            override fun call(logs: MutableList<Log>) {
                for(log in logs) {
                    totalTravelTime += log.duration
                }
            }
        })
        return totalTravelTime
    }

    override fun getTotalTravelDistance(): Double {
        var totalTravelDistance : Double = 0.0
        getLogs(cb = object : Callback<MutableList<Log>> {
            override fun call(logs: MutableList<Log>) {
                for(log in logs) {
                    totalTravelDistance += log.distance
                }
            }
        })
        return totalTravelDistance
    }


}