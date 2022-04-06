package com.example.delivery.utils

import android.util.Log
import com.github.nkzawa.socketio.client.Socket
import com.github.nkzawa.socketio.client.IO
import java.net.URISyntaxException


object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            // combinacion entre la ip de los web services y el metodo del socket
            mSocket = IO.socket("http://192.168.1.60:3000/orders/delivery")
        } catch (e: URISyntaxException) {
            Log.d("Error", "No se pudo conectar el socker ${e.message}")
        }
    }

    @JvmName("getSocket1")
    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }


}