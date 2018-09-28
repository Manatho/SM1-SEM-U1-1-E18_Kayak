package kayaklers.sdu.dk.kayaklers.data

import android.location.Location

data class Log (val startTime: Long, val duration: Long, val distance : Double, val valid : Boolean, val points : Int, val gpsPoints : List<GPSPoint>)

data class GPSPoint(var latitude : Double, var longitude : Double, var altitude : Double){
    fun of(location : Location) {
        latitude = location.latitude
        longitude = location.longitude
        altitude = location.altitude
    }


    fun toLocation() : Location{
        var location = Location("")
        location.latitude = latitude
        location.longitude = longitude
        location.altitude = altitude
        return location;
    }
}