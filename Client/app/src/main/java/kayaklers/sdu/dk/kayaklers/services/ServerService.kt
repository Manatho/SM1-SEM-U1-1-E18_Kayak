package kayaklers.sdu.dk.kayaklers.services

import android.support.annotation.NonNull
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kayaklers.sdu.dk.kayaklers.apollo.ApolloClient
import kayaklers.sdu.dk.kayaklers.data.GPSPoint
import kayaklers.sdu.dk.kayaklers.data.Log
import org.jetbrains.annotations.NotNull

class ServerService: IServer {

    private val TAG: String = "ServerService"
    private val apolloClient = ApolloClient.setupApollo()

    override fun getLogs(): MutableList<Log>? {
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
                        gpsPoints.add(GPSPoint(gpsPoint.latitude() as Double, gpsPoint.longitude() as Double, gpsPoint.altitude() as Double))
                    }
                    logs.add(Log(it.startTime() as Long, it.duration() as Long, it.distance() as Double, it.valid() as Boolean, it.points() as Int, gpsPoints))
                }
            }
        })
        return logs
    }

    override fun getLog(id: Int): Log? {
        lateinit var log : Log
        apolloClient.query(
                LogQuery.builder().id(id).build()).enqueue(object: ApolloCall.Callback<LogQuery.Data>() {
            override fun onFailure(@NotNull e: ApolloException) {
                android.util.Log.e(TAG, e.message.toString())
            }

            override fun onResponse(@NotNull response: Response<LogQuery.Data>) {
                var gpsPoints: MutableList<GPSPoint> = mutableListOf()
                response.data()?.log()?.GPSPoints()?.forEach{
                    gpsPoints.add(GPSPoint(it.latitude() as Double, it.longitude() as Double, it.altitude() as Double))
                }
                log = Log(response.data()?.log()?.startTime() as Long, response.data()?.log()?.duration() as Long, response.data()?.log()?.distance() as Double, response.data()?.log()?.valid() as Boolean, response.data()?.log()?.points() as Int, gpsPoints)
            }
            })
        return log
    }

    override fun getGPSPoint(id: Int): GPSPoint? {
        lateinit var gpsPoint : GPSPoint
        apolloClient.query(
                GPSPointQuery.builder().id(id).build()).enqueue(object: ApolloCall.Callback<GPSPointQuery.Data>() {
            override fun onFailure(@NotNull e: ApolloException) {
                android.util.Log.e(TAG, e.message.toString())
            }

            override fun onResponse(@NotNull response: Response<GPSPointQuery.Data>) {
                gpsPoint = GPSPoint(response?.data()?.GPSPoint()?.latitude() as Double, response?.data()?.GPSPoint()?.longitude() as Double, response?.data()?.GPSPoint()?.altitude() as Double)
            }
        })
        return gpsPoint
    }

    override fun getGPSPoints(): MutableList<GPSPoint>? {
        var gpsPoints: MutableList<GPSPoint> = mutableListOf()
        apolloClient.query(
                AllGPSPointsQuery.builder().build()).enqueue(object: ApolloCall.Callback<AllGPSPointsQuery.Data>() {
            override fun onFailure(@NotNull e: ApolloException) {
                android.util.Log.e(TAG, e.message.toString())
            }

            override fun onResponse(@NotNull response:  Response<AllGPSPointsQuery.Data>) {
                response.data()?.allGPSPoints()?.forEach {
                    gpsPoints.add(GPSPoint(it.latitude() as Double, it.longitude() as Double, it.altitude() as Double))
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

    override fun getTotalTravelTime(id : Int): Long {
        var totalTravelTime : Long = 0
        for (log in getLogs().orEmpty()) {
            totalTravelTime += log.duration
        }
        return totalTravelTime
    }

    override fun getTotalTravelDistance(id : Int): Double {
        var totalTravelDistance : Double = 0.0
        for (log in getLogs().orEmpty()) {
            totalTravelDistance += log.distance
        }
        return totalTravelDistance
    }

}