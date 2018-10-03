package kayaklers.sdu.dk.kayaklers.services

import kayaklers.sdu.dk.kayaklers.data.Log

interface IServer {
    fun getLogs() : List<Log>
    fun getLog() : Log
    fun addLog() : Log
    fun addLogs() : List<Log>
    fun connect()

    fun getTotalTravelTime() : Long
    fun getTotalTravelDistance() : Double


}