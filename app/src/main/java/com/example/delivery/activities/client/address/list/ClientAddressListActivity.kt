package com.example.delivery.activities.client.address.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delivery.R
import com.example.delivery.activities.client.address.create.ClientAddressCreateActivity
import com.example.delivery.adapters.AddressAdapter
import com.example.delivery.models.Address
import com.example.delivery.models.User
import com.example.delivery.providers.AddressProvider
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.activity_client_address_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddressListActivity : AppCompatActivity() {

    var adapter: AddressAdapter? = null
    var addressProvider: AddressProvider? = null
    var sharedPref: SharedPref? = null
    var user: User? = null

    var address = ArrayList<Address>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)

        sharedPref = SharedPref(this)

        rv_address?.layoutManager = LinearLayoutManager(this)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Mis direcciones"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getUserFromSession()

        addressProvider = AddressProvider(user?.session_token!!)

        fab_address_create?.setOnClickListener { goToAddressCreate() }

        getAdress()

    }

    private fun getAdress() {
        addressProvider?.getAddress(user?.id!!)?.enqueue(object: Callback<ArrayList<Address>> {
            override fun onResponse(call: Call<ArrayList<Address>>, response: Response<ArrayList<Address>>) {
                if(response.body() != null) {
                    address = response.body()!!
                    adapter = AddressAdapter(this@ClientAddressListActivity, address)
                    rv_address?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Address>>, t: Throwable) {
                Toast.makeText(this@ClientAddressListActivity, "Ocurrio un error en la peticion", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getUserFromSession() {
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()) {
            // Asegurarse de que el servicio no devuelva el password
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

    private fun goToAddressCreate() {
        val i = Intent(this, ClientAddressCreateActivity::class.java)
        startActivity(i)
    }

}