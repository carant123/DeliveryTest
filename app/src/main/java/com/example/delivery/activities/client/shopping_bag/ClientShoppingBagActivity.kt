package com.example.delivery.activities.client.shopping_bag

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delivery.R
import com.example.delivery.activities.client.address.create.ClientAddressCreateActivity
import com.example.delivery.activities.client.address.list.ClientAddressListActivity
import com.example.delivery.adapters.ShoppingBagAdapter
import com.example.delivery.models.Product
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.activity_client_products_detail.*
import kotlinx.android.synthetic.main.activity_client_shooping_bag.*

class ClientShoppingBagActivity : AppCompatActivity() {

    var adapter: ShoppingBagAdapter? = null
    var sharedPref: SharedPref? = null
    var gson = Gson()
    var selectectedProducts = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_shooping_bag)

        sharedPref = SharedPref(this)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Tu orden"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rv_shopping_bag?.layoutManager = LinearLayoutManager(this)

        getProductsFromSharedPref()

        bt_next?.setOnClickListener { goToAddressList() }
    }

    private fun goToAddressList() {
        val i = Intent(this, ClientAddressListActivity::class.java)
        startActivity(i)
    }

    fun setTotal(total: Double) {
        tv_total?.text = "${total}"
    }

    private fun getProductsFromSharedPref() {

        if(!sharedPref?.getData("order").isNullOrBlank()) {
            val type = object: TypeToken<ArrayList<Product>>() {}.type
            selectectedProducts = gson.fromJson(sharedPref?.getData("order"), type)

            adapter = ShoppingBagAdapter(this, selectectedProducts)
            rv_shopping_bag?.adapter = adapter

        }
    }

}