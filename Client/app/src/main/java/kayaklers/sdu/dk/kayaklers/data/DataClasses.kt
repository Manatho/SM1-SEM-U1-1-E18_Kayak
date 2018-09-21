package kayaklers.sdu.dk.kayaklers.data

data class Log (val id: Long, val duration: Long, val distance : Double, val valid : Boolean, val points : Int, val gpsPoints : List<GPSPoint>)
data class GPSPoint(val latitude : Long, val longitude : Long, val altitude : Long)