package com.example.delivery.activities.client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.delivery.R
import com.example.delivery.adapters.ProductsAdapter
import com.example.delivery.models.Product
import com.example.delivery.models.User
import com.example.delivery.providers.ProductsProvider
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_client_products_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductsListActivity : AppCompatActivity() {

    var adapter: ProductsAdapter? = null
    var productsProvider: ProductsProvider? = null
    var products: ArrayList<Product> = ArrayList()
    var sharedPref: SharedPref? = null
    var user: User? = null

    var idCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list)

        idCategory = intent.getStringExtra("idCategory")
        sharedPref = SharedPref(this)

        getUserFromSession()
        productsProvider = ProductsProvider(user?.session_token!!)

        recycleview_products?.layoutManager = GridLayoutManager(this, 2)

        getProducts()
    }

    private fun getUserFromSession() {
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()) {
            // Asegurarse de que el servicio no devuelva el password
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

    private fun getProducts() {
        productsProvider?.findByCategory(idCategory!!)?.enqueue(object: Callback<ArrayList<Product>> {
            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: Response<ArrayList<Product>>
            ) {
                if(response.body() != null) {
                    products = response.body()!!
                    adapter = ProductsAdapter(this@ClientProductsListActivity, products)
                    recycleview_products?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Toast.makeText(this@ClientProductsListActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

}