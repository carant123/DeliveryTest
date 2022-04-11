package com.example.delivery.activities.client.products.detail

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.delivery.R
import com.example.delivery.models.Product
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_client_products_detail.*

class ClientProductsDetailActivity : AppCompatActivity() {

    var product: Product? = null
    val gson = Gson()

    var counter = 1
    var productPrice = 0.0

    var sharedPref: SharedPref? = null
    var selectectedProducts = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_detail)

        product = gson.fromJson(intent.getStringExtra("product"), Product::class.java)
        sharedPref = SharedPref(this)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(product?.image1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image3, ScaleTypes.CENTER_CROP))

        is_images?.setImageList(imageList)

        tv_name?.text = product?.name
        tv_description?.text = product?.description
        tv_price?.text = "${product?.price}"

        iv_add?.setOnClickListener { addItem() }
        iv_remove?.setOnClickListener { removeItem() }

        getProductsFromSharedPref()

    }

    private fun addToBag() {
        val index = getIndexOf(product?.id!!) // INDICE DEL PRODUCTO SI ES QUE EXISTE EN SHARED Pref

        if(index == -1) {
            if(product?.quantity == 0){
                product?.quantity = 1
            }

            selectectedProducts.add(product!!)
        } else {
            selectectedProducts[index].quantity = counter
        }

        sharedPref?.save("order", selectectedProducts)
        Toast.makeText(this, "Producto agregado", Toast.LENGTH_LONG).show()
    }

    private fun getProductsFromSharedPref() {

        if(!sharedPref?.getData("order").isNullOrBlank()) {
            val type = object: TypeToken<ArrayList<Product>>() {}.type
            selectectedProducts = gson.fromJson(sharedPref?.getData("order"), type)
            val index = getIndexOf(product?.id!!)

            if(index != -1) {
                product?.quantity = selectectedProducts[index].quantity
                tv_counter?.text = "${product?.quantity}"
                productPrice = product?.price!! * product?.quantity!!
                tv_price?.text = "${productPrice}"
                bt_add_product?.setText("Editar producto")
                bt_add_product?.backgroundTintList = ColorStateList.valueOf(Color.RED)
            }

            for (p in selectectedProducts) {
                Log.d("TAG", "Shared pref: $p")
            }
        }
    }

    // ES PARA COMPARAR SI UN PRODUCTO YA EXISTE EN SHARED PREF Y ASI PODER EDITAR LA CANTIDAD DEL PRODUCTO SELECCION
    private fun getIndexOf(idProduct: String): Int {
        var pos = 0

        for(p in selectectedProducts) {
            if(p.id == idProduct) {
                return pos
            }
            pos++
        }

        return -1
    }

    private fun removeItem() {
        if(counter > 1) {
            counter--
            productPrice = product?.price!! * counter
            product?.quantity = counter
            tv_counter?.text = "${product?.quantity}"
            tv_price?.text = "${productPrice}"
        }
    }

    private fun addItem() {
        counter++
        productPrice = product?.price!! * counter
        product?.quantity = counter
        tv_counter?.text = "${product?.quantity}"
        tv_price?.text = "${productPrice}"
    }


}