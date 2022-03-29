package com.example.delivery.models

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class ResponseHttp(
    @SerializedName("message") val message: String,
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("data") val data: JSONObject,
    @SerializedName("error") val error: String
) {
    override fun toString(): String {
        return "ResponseHttp(message='$message', isSuccess=$isSuccess, data=$data, error='$error')"
    }
}