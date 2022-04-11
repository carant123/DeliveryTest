package com.example.delivery.activities.client.address.create

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.delivery.R
import com.example.delivery.activities.client.address.map.ClientAddressMapActivity
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.activity_client_address_create.*

class ClientAddressCreateActivity : AppCompatActivity() {

    var addressLat = 0.0
    var addressLng = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_create)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Nueva direccion"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        et_ref_point?.setOnClickListener { goToAddressMap() }

    }

    private fun createAddress() {
        val address = et_address?.text.toString()
        val neighborhood = et_neighborhood?.text.toString()

        if(isValidForm(address,neighborhood)) {

        }
    }

    private fun isValidForm(address: String, neighborhood: String): Boolean {
        if(address.isNullOrBlank()) {
            Toast.makeText(this, "Ingresa la direccion", Toast.LENGTH_LONG).show()
            return false
        }
        if(neighborhood.isNullOrBlank()) {
            Toast.makeText(this, "Ingresa el barrio  o ridencia", Toast.LENGTH_LONG).show()
            return false
        }

        if(addressLat == 0.0) {
            Toast.makeText(this, "Selecciona un punto de referencia", Toast.LENGTH_LONG).show()
            return false
        }

        if(addressLng == 0.0) {
            Toast.makeText(this, "Selecciona un punto de referencia", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if(result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val city = data?.getStringExtra("city")
            val address = data?.getStringExtra("address")
            val country = data?.getStringExtra("country")
            addressLat = data?.getDoubleExtra("lat",0.0)!!
            addressLng = data?.getDoubleExtra("lng",0.0)!!
            et_ref_point?.setText("$address $city")
        }

    }

    private fun goToAddressMap() {
        val i = Intent(this, ClientAddressMapActivity::class.java)
        resultLauncher.launch(i)
    }

}