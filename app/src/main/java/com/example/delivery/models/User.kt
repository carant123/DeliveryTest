package com.example.delivery.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") var name: String,
    @SerializedName("lastname") var lastname: String,
    @SerializedName("email") var email: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("password") val password: String,
    @SerializedName("image") var image: String? = null,
    @SerializedName("session_token") val session_token: String? = null,
    @SerializedName("is_available") val isAvailable: Boolean? = null,
    @SerializedName("roles") val roles: ArrayList<Rol>? = null,
) {

    override fun toString(): String {
        return "User(id=$id, name='$name', lastname='$lastname', email='$email', phone='$phone', password='$password', image=$image, session_token=$session_token, isAvailable=$isAvailable, roles=$roles)"
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}