package com.example.delivery.fragments.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delivery.R
import com.example.delivery.adapters.CategoriesAdapter
import com.example.delivery.models.Category
import com.example.delivery.models.User
import com.example.delivery.providers.CategoriesProvider
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientCategoriesFragment : Fragment() {

    var myView: View? = null
    var recyclerViewCategory: RecyclerView? = null
    var categoriesProvider: CategoriesProvider? = null
    var adapter: CategoriesAdapter? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()
    var toolbar: Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myView = inflater.inflate(R.layout.fragment_client_categories, container, false)

        toolbar = myView?.findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        toolbar?.title = "Categories"
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        recyclerViewCategory = myView?.findViewById(R.id.recycleview_categories)
        recyclerViewCategory?.layoutManager = LinearLayoutManager(requireContext())

        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        categoriesProvider = CategoriesProvider(user?.session_token!!)

        return myView

    }

    private fun getCategories() {
        categoriesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Category>> {
            override fun onResponse(call: Call<ArrayList<Category>>, response: Response<ArrayList<Category>>) {
                if(response.body() != null) {
                    categories = response.body()!!
                    adapter = CategoriesAdapter(requireActivity(), categories)
                    recyclerViewCategory?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
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

}