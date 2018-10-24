package kayaklers.sdu.dk.kayaklers.services

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kayaklers.sdu.dk.kayaklers.apollo.ApolloClient
import kayaklers.sdu.dk.kayaklers.data.GPSPoint
import kayaklers.sdu.dk.kayaklers.data.Log
import org.jetbrains.annotations.NotNull

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
                    for (gpsPoint in it.GPSPoints().orEmpty()){
                        gpsPoints.add(GPSPoint(gpsPoint?.latitude()!!.toDouble(), gpsPoint?.longitude()!!.toDouble(), gpsPoint?.altitude()!!.toDouble()))
                    }
                    var startTime : Long? = it?.startTime()?.toLong()
                    var duration : Long? = it?.startTime()?.toLong()
                    var distance : Double? = it?.startTime()
                    var valid : Boolean? = it?.valid()
                    var points : Int? = it?.points()
                    logs.add(Log(startTime!!, duration!!, distance!!, valid!!, points!!, gpsPoints))
                    cb.call(logs)
                }
            }
        })
        return logs
    }

    override fun getLog(id: Int, cb : Callback<Log>): Log? {
        lateinit var log : Log
        apolloClient.query(
                LogQuery.builder().id(id).build()).enqueue(object: ApolloCall.Callback<LogQuery.Data>() {
            override fun onFailure(@NotNull e: ApolloException) {
                android.util.Log.e(TAG, e.message.toString())
            }

            override fun onResponse(@NotNull response: Response<LogQuery.Data>) {
                var gpsPoints: MutableList<GPSPoint> = mutableListOf()
                response.data()?.log()?.GPSPoints()?.forEach{
                    gpsPoints.add(GPSPoint(it?.latitude()!!.toDouble(), it?.longitude()!!.toDouble(), it?.altitude()!!.toDouble()))
                }
                var startTime : Long? = response.data()?.log()?.startTime()?.toLong()
                var duration : Long? = response.data()?.log()?.startTime()?.toLong()
                var distance : Double? = response.data()?.log()?.startTime()
                var valid : Boolean? = response.data()?.log()?.valid()
                var points : Int? = response.data()?.log()?.points()
                log = Log(startTime!!, duration!!, distance!!, valid!!, points!!, gpsPoints)
                cb.call(log)
            }
            })
        return log
    }

    override fun getGPSPoint(id: Int, cb : Callback<GPSPoint>): GPSPoint? {
        lateinit var gpsPoint : GPSPoint
        apolloClient.query(
                GPSPointQuery.builder().id(id).build()).enqueue(object: ApolloCall.Callback<GPSPointQuery.Data>() {
            override fun onFailure(@NotNull e: ApolloException) {
                android.util.Log.e(TAG, e.message.toString())
            }

            override fun onResponse(@NotNull response: Response<GPSPointQuery.Data>) {
                gpsPoint = GPSPoint(response?.data()?.GPSPoint()?.latitude()!!.toDouble(), response?.data()?.GPSPoint()?.longitude()!!.toDouble(), response?.data()?.GPSPoint()?.altitude()!!.toDouble())
                cb.call(gpsPoint)
            }
        })
        return gpsPoint
    }

    override fun getGPSPoints(cb : Callback<MutableList<GPSPoint>>): MutableList<GPSPoint>? {
        var gpsPoints: MutableList<GPSPoint> = mutableListOf()
        apolloClient.query(
                AllGPSPointsQuery.builder().build()).enqueue(object: ApolloCall.Callback<AllGPSPointsQuery.Data>() {
            override fun onFailure(@NotNull e: ApolloException) {
                android.util.Log.e(TAG, e.message.toString())
            }

            override fun onResponse(@NotNull response:  Response<AllGPSPointsQuery.Data>) {
                response.data()?.allGPSPoints()?.forEach {
                    gpsPoints.add(GPSPoint(it?.latitude()!!.toDouble(), it?.longitude()!!.toDouble(), it?.altitude()!!.toDouble()))
                    cb.call(gpsPoints)
                }
            }
        })
        return gpsPoints
    }

    override fun addGPSPoint(gpsPoint : GPSPoint, logID : Int) {
        apolloClient.mutate(
                CreateGPSPointMutation.builder()
                        .latitude(gpsPoint.latitude)
                        .longitude(gpsPoint.longitude)
                        .altitude(gpsPoint.altitude)
                        .log(logID)
                        .build()
        ).enqueue(object: ApolloCall.Callback<CreateGPSPointMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                android.util.Log.e(TAG, e.message.toString())
            }

            override fun onResponse(response: Response<CreateGPSPointMutation.Data>) {

            }
        })
    }

    override fun addGPSPoints(gpsPoints : MutableList<GPSPoint>, logID : Int) {
        for (gpsPoint in gpsPoints) {
            apolloClient.mutate(
                    CreateGPSPointMutation.builder()
                            .latitude(gpsPoint.latitude)
                            .longitude(gpsPoint.longitude)
                            .altitude(gpsPoint.altitude)
                            .log(logID)
                            .build()
            ).enqueue(object: ApolloCall.Callback<CreateGPSPointMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    android.util.Log.e(TAG, e.message.toString())
                }

                override fun onResponse(response: Response<CreateGPSPointMutation.Data>) {

                }
            })
        }
    }

    override fun addLog(log : Log) {
        apolloClient.mutate(
                CreateLogMutation.builder()
                        .startTime(log.startTime.toDouble())
                        .duration(log.duration.toDouble())
                        .distance(log.distance)
                        .valid(log.valid)
                        .points(log.points)
                        .build()
        ).enqueue(object: ApolloCall.Callback<CreateLogMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                android.util.Log.e(TAG, e.message.toString())
            }

            override fun onResponse(response: Response<CreateLogMutation.Data>) {

            }
        })
    }

    override fun addLogs(logs : MutableList<Log>) {
        for (log in logs) {
            apolloClient.mutate(
                    CreateLogMutation.builder()
                            .startTime(log.startTime.toDouble())
                            .duration(log.duration.toDouble())
                            .distance(log.distance)
                            .valid(log.valid)
                            .points(log.points)
                            .build()
            ).enqueue(object: ApolloCall.Callback<CreateLogMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    android.util.Log.e(TAG, e.message.toString())
                }

                override fun onResponse(response: Response<CreateLogMutation.Data>) {

                }
            })
        }
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