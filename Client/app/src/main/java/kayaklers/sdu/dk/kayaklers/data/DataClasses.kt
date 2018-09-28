package kayaklers.sdu.dk.kayaklers.data

import android.location.Location
import java.io.Serializable

data class Log (val startTime: Long, val duration: Long, val distance : Double, val valid : Boolean, val points : Int, val gpsPoints : List<GPSPoint>)  : Serializable

data class GPSPoint(var latitude : Double, var longitude : Double, var altitude : Double) : Serializable{
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