package kayaklers.sdu.dk.kayaklers.services

import kayaklers.sdu.dk.kayaklers.data.GPSPoint
import kayaklers.sdu.dk.kayaklers.data.Log
import type.LogInput

interface IServer {
    fun getLogs(cb : Callback<MutableList<Log>>) : MutableList<Log>?
    fun getLog(id: String, cb : Callback<Log>) : Log?

    fun addLog(gpsPoints : MutableList<GPSPoint>)

    fun getLogCount() : Int

    fun getTotalTravelTime() : Long
    fun getTotalTravelDistance() : Double
}

interface Callback<T> {
    fun call(t : T)
}

