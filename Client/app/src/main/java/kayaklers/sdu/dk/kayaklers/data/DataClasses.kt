package kayaklers.sdu.dk.kayaklers.data

import android.location.Location
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Log (val startTime: Long, val duration: Long, val distance : Double, val valid : Boolean, val points : Int, val gpsPoints : List<GPSPoint>)  : Parcelable

@Parcelize
data class GPSPoint(var latitude : Double, var longitude : Double, var altitude : Double) : Parcelable{
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