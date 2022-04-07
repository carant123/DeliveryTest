package com.example.delivery.fragments.restaurant

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.delivery.R
import com.example.delivery.models.Category
import com.example.delivery.models.ResponseHttp
import com.example.delivery.models.User
import com.example.delivery.providers.CategoriesProvider
import com.example.delivery.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_save_image.*
import kotlinx.android.synthetic.main.fragment_restaurant_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RestaurantCategoryFragment : Fragment() {

    var myView: View? = null
    private var imageFile: File? = null

    var sharedPref: SharedPref? = null
    var user: User? = null

    var categoriesProvider: CategoriesProvider? = null

    var ivCategory: ImageView? = null
    var btCreateCategory: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_restaurant_category, container, false)

        sharedPref = SharedPref(requireActivity())

        ivCategory = myView?.findViewById(R.id.iv_category)
        ivCategory?.setOnClickListener { selectImage() }

        btCreateCategory = myView?.findViewById(R.id.bt_create_category)
        btCreateCategory?.setOnClickListener { createCategory() }

        getUserFromSession()
        categoriesProvider = CategoriesProvider(user?.session_token!!)

        return myView
    }

    private fun getUserFromSession() {
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()) {
            // Asegurarse de que el servicio no devuelva el password
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

    private fun createCategory() {
        val name = et_category?.text.toString()

        if(imageFile != null) {

            val category = Category(name = name)

            categoriesProvider?.create(imageFile!!, category)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    if(response.body()?.isSuccess == true) {
                        clearForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d("TAG", "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })

        } else {
            Toast.makeText(requireContext(), "Selecciona una imagen", Toast.LENGTH_LONG).show()
        }

    }

    private fun clearForm() {
        et_category?.setText("")
        imageFile = null
        iv_category?.setImageResource(R.drawable.ic_image_300)
    }

    private val startImageForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                if(resultCode == Activity.RESULT_OK) {
                    val fileUri = data?.data
                    imageFile = File(fileUri?.path)
                    iv_category?.setImageURI(fileUri)
                } else if(resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Tarea se cancelo", Toast.LENGTH_LONG).show()
                }

            }

    private fun selectImage() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1000, 1000)
                .createIntent { intent ->
                    startImageForResult.launch(intent)
                }
    }

}