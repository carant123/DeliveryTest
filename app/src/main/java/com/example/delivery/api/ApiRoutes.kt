package com.example.delivery.api

import com.example.delivery.routes.UsersRoutes

class ApiRoutes {

    val API_URL = "http://192.168.1.60:3000/api/"
    val retrofit = RetrofitClient()

    fun getUsersRoutes(): UsersRoutes {
        return retrofit.getClient(API_URL).create(UsersRoutes::class.java)
    }

}