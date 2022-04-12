package com.example.delivery.providers

import com.example.delivery.api.ApiRoutes
import com.example.delivery.models.Address
import com.example.delivery.models.Category
import com.example.delivery.models.Order
import com.example.delivery.models.ResponseHttp
import com.example.delivery.routes.AddressRoutes
import com.example.delivery.routes.CategoriesRoutes
import com.example.delivery.routes.OrdersRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class OrdersProvider(val token: String) {

    private var ordersRoutes: OrdersRoutes? = null

    init {
        val api = ApiRoutes()
        ordersRoutes = api.getOrderRoutes(token)
    }

//    fun getAddress(idUser: String): Call<ArrayList<Address>>? {
//        return ordersRoutes?.getAddress(idUser, token)
//    }

    fun create(order: Order): Call<ResponseHttp>? {
        return ordersRoutes?.create(order, token)
    }

}