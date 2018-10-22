package kayaklers.sdu.dk.kayaklers.services

import kayaklers.sdu.dk.kayaklers.data.GPSPoint
import kayaklers.sdu.dk.kayaklers.data.Log

interface IServer {
    fun getLogs() : MutableList<Log>?
    fun getLog(id: Int) : Log?

    fun getGPSPoint(id: Int) : GPSPoint?
    fun getGPSPoints() : MutableList<GPSPoint>?

    fun addGPSPoint(gpsPoint: GPSPoint, logID : Int)
    fun addGPSPoints(gpsPoints : MutableList<GPSPoint>, logID : Int)

    fun addLog(log : Log)
    fun addLogs(logs : MutableList<Log>)

    fun getLogCount() : Int

    fun getTotalTravelTime() : Long
    fun getTotalTravelDistance() : Double


}