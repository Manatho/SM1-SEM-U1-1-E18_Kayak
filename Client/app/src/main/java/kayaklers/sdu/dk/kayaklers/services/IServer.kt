package kayaklers.sdu.dk.kayaklers.services

import kayaklers.sdu.dk.kayaklers.data.GPSPoint
import kayaklers.sdu.dk.kayaklers.data.Log

interface IServer {
    fun getLogs(cb : Callback<MutableList<Log>>) : MutableList<Log>?
    fun getLog(id: Int, cb : Callback<Log>) : Log?

    fun getGPSPoint(id: Int, cb : Callback<GPSPoint>) : GPSPoint?
    fun getGPSPoints(cb : Callback<MutableList<GPSPoint>>) : MutableList<GPSPoint>?

    fun addGPSPoint(gpsPoint: GPSPoint, logID : Int)
    fun addGPSPoints(gpsPoints : MutableList<GPSPoint>, logID : Int)

    fun addLog(log : Log)
    fun addLogs(logs : MutableList<Log>)

    fun getLogCount() : Int

    fun getTotalTravelTime() : Long
    fun getTotalTravelDistance() : Double
}

interface Callback<T> {
    fun call(t : T)
}

