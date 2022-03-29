package com.example.delivery.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delivery.R
import com.example.delivery.adapters.RolesAdapter
import com.example.delivery.models.User
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_select_roles.*

class SelectRolesActivity: AppCompatActivity() {

    var user: User? = null
    var adapter: RolesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)

        recycleview_roles?.layoutManager = LinearLayoutManager(this)
        getUserFromSession()
        adapter = RolesAdapter(this, user?.roles!!)
        recycleview_roles?.adapter = adapter
    }

    private fun getUserFromSession() {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        if(!sharedPref.getData("user").isNullOrBlank()) {
            // Asegurarse de que el servicio no devuelva el password
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)

        }
    }
}