package com.example.delivery.activities.delivery.orders.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.delivery.R
import com.example.delivery.models.SocketEmit
import com.example.delivery.utils.SocketHandler
import com.github.nkzawa.socketio.client.Socket
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng

class DeliveryOrdersMapActivity : AppCompatActivity() {

    private lateinit var addressLatLng: Any
    private lateinit var distanceBetween: Any

    var myLocationLatLng: LatLng? = null
    var socket: Socket? = null

    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation = locationResult.lastLocation
            myLocationLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            emitPosition()

            //distanceBetween = getDistanceBetween(myLocationLatLng!!, addressLatLng!!)

            removeDeliveryMarker()
            //addDeliveryMarker(data.lat, data.lng)

        }


    }

//    private fun getDistanceBetween(myLocationLatLng: LatLng, addressLatLng: Any): Any {
//
//    }

    private fun addDeliveryMarker(lat: Any, lng: Any) {

    }

    private fun removeDeliveryMarker() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_orders_map)
        connectSocket()
    }

    private fun emitPosition() {
//        val data = SocketEmit(
//            id_order = order?.id!!,
//            lat = myLocationLatLng?.latitude!!,
//            lng = myLocationLatLng?.longitud!!
//        )
//        socket?.emit("position", data.toJson())

        // cada vez que se actualize la posicion en tiempo real
    }

    private fun connectSocket() {
        SocketHandler.setSocket()
        socket = SocketHandler.getSocket()
        socket?.connect()

        // lo que envia al socket
        //socket?.emit("position/${order.id}", data.toJson())

        // Lo que recibe del socket
//        socket?.on("position/${order?.id}") { args ->
//            if(args[0] != null) {
//                runOnUiThread {
//                    val data = gson.fromJson(args[0].toString(), SocketEmit::class.java)
   //                   removeDeliveryMarker()
//                    addDeliveryMarker(data.lat, data.lng)
//                }
//            }
//        }

    }

    override fun onDestroy() {
        super.onDestroy()
//        if(locationCallback != null && fusedLocationClient != null) {
//            fusedLocationClient?.removeLocationUpdates(locationCallback)
//        }
        socket?.disconnect()
    }

    private fun updateLatLng(lat: Double, lng: Double) {

    }

}