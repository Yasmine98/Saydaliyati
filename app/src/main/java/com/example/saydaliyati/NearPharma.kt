package com.example.saydaliyati

import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng

class NearPharma()
{
    lateinit var listNearPharma : Map<Int, DistancePharma>


    data class DistancePharma(
        val id: Int ,
        val nom: String,
        val ho: String,
        val hf: String,
        val tel: String, val longi: Double, val lat: Double, val distance:Float): Comparable<DistancePharma>
    {
            override operator fun compareTo(other: DistancePharma):Int{
            return this.distance.compareTo(other.distance)
        }
    }

    fun fill( lat: Double,  long:Double, list:List<pharmacie>)
    {
         var map = hashMapOf<Int, DistancePharma>()
        var dp : DistancePharma
        var latlong = LatLng(lat, long)
        for( p in list)
        {
            dp = DistancePharma(p.id, p.nom, p.ho, p.hf, p.tel, p.longi, p.lat,distanceBetween(latlong, LatLng(p.lat, p.longi)) )
            map.put(dp.id, dp)
        }
         listNearPharma  = map.toList().sortedBy { (_, value) -> value}.toMap()

    }
    private fun distanceBetween(latLng1: LatLng, latLng2: LatLng): Float {
        val loc1 = Location(LocationManager.GPS_PROVIDER)
        val loc2 = Location(LocationManager.GPS_PROVIDER)
        loc1.latitude = latLng1.latitude
        loc1.longitude = latLng1.longitude
        loc2.latitude = latLng2.latitude
        loc2.longitude = latLng2.longitude
        return loc1.distanceTo(loc2)
    }
}