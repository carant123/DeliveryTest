package com.example.delivery.activities.delivery.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.delivery.R
import com.example.delivery.activities.MainActivity
import com.example.delivery.fragments.client.ClientProfileFragment
import com.example.delivery.fragments.delivery.DeliveryOrdersFragment
import com.example.delivery.models.User
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_client_home.*

class DeliveryHomeActivity : AppCompatActivity() {

    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_home)
        sharedPref = SharedPref(this)
        // bt_logout?.setOnClickListener { logout() }

        openFragment(DeliveryOrdersFragment())

        bottom_navigation.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.item_orders -> {
                    openFragment(DeliveryOrdersFragment())
                    true
                }
                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }
                else -> false
            }

        }



        getUserFromSession()
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun logout() {
        sharedPref?.remove("user")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession() {
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()) {
            // Asegurarse de que el servicio no devuelva el password
            val user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d("TAG", "Usuario: $user")
        }

    }

}