package com.example.delivery.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.delivery.R
import com.example.delivery.activities.client.home.ClientHomeActivity
import com.example.delivery.activities.client.products.list.ClientProductsListActivity
import com.example.delivery.activities.delivery.home.DeliveryHomeActivity
import com.example.delivery.activities.restaurant.home.RestaurantHomeActivity
import com.example.delivery.models.Address
import com.example.delivery.models.Category
import com.example.delivery.models.Rol
import com.example.delivery.utils.SharedPref

class AddressAdapter(val context: Activity, val address: ArrayList<Address>) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return address.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val a = address[position]

        holder.textViewAddress.text = a.address
        holder.textViewNeighborhood.text = a.neighborhood

    }

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewAddress: TextView
        val textViewNeighborhood: TextView
        val imageViewCheck: ImageView

        init {
            textViewAddress = view.findViewById(R.id.tv_address)
            textViewNeighborhood = view.findViewById(R.id.tv_neightborhood)
            imageViewCheck = view.findViewById(R.id.iv_check)
        }

    }

}