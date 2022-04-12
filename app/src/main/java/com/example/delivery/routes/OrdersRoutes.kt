package com.example.delivery.routes

import com.example.delivery.models.Address
import com.example.delivery.models.Category
import com.example.delivery.models.Order
import com.example.delivery.models.ResponseHttp
import retrofit2.Call
import retrofit2.http.*

interface OrdersRoutes {

//    @GET("address/findByUser/:id_user")
//    fun getAddress(
//            @Path("id_user") idUser: String,
//            @Header("Authorization") token: String
//    ): Call<ArrayList<Address>>


    @POST("orders/create")
    fun create(
        @Body order: Order,
        @Header("Authorization") token: String
    ) : Call<ResponseHttp>

}