package com.example.delivery.fragments.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.delivery.R
import com.example.delivery.adapters.CategoriesAdapter
import com.example.delivery.models.Category
import com.example.delivery.models.Product
import com.example.delivery.models.ResponseHttp
import com.example.delivery.models.User
import com.example.delivery.providers.CategoriesProvider
import com.example.delivery.providers.ProductsProvider
import com.example.delivery.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import kotlinx.android.synthetic.main.activity_save_image.*
import kotlinx.android.synthetic.main.fragment_restaurant_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RestaurantProductFragment : Fragment() {

    var myView: View? = null

    var etTextName: TextView? = null
    var etTextDescripcion: TextView? = null
    var etTextPrice: TextView? = null

    var ivProduct1: ImageView? = null
    var ivProduct2: ImageView? = null
    var ivProduct3: ImageView? = null

    var btCreate: Button? = null
    private var imageFile1: File? = null
    private var imageFile2: File? = null
    private var imageFile3: File? = null

    var categoriesProvider: CategoriesProvider? = null
    var productsProvider: ProductsProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()

    var spinnerCategories: Spinner? = null
    var idCategory = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_restaurant_product, container, false)

        etTextName = myView?.findViewById(R.id.et_name)
        etTextDescripcion = myView?.findViewById(R.id.et_description)
        etTextPrice = myView?.findViewById(R.id.et_precio)

        ivProduct1 = myView?.findViewById(R.id.iv_image1)
        ivProduct2 = myView?.findViewById(R.id.iv_image2)
        ivProduct3 = myView?.findViewById(R.id.iv_image3)

        btCreate = myView?.findViewById(R.id.btn_create)
        btCreate?.setOnClickListener { createProduct() }

        ivProduct1?.setOnClickListener { selectImage(101) }
        ivProduct2?.setOnClickListener { selectImage(102) }
        ivProduct3?.setOnClickListener { selectImage(103) }

        spinnerCategories = myView?.findViewById(R.id.spinner_categories)

        sharedPref = SharedPref(requireActivity())
        getUserFromSession()
        categoriesProvider = CategoriesProvider(user?.session_token!!)
        productsProvider = ProductsProvider(user?.session_token!!)

        getCategories()

        return myView
    }

    private fun createProduct() {
        val name = etTextName?.text.toString()
        val descripcion = etTextDescripcion?.text.toString()
        val priceText = etTextPrice?.text.toString()

        val files = ArrayList<File>()

        if(isValidForm(name, descripcion, priceText)) {

            val product = Product(
                    name = name,
                    description = descripcion,
                    price = priceText.toDouble(),
                    idCategory = idCategory
            )

            files.add(imageFile1!!)
            files.add(imageFile1!!)
            files.add(imageFile1!!)

            ProgressDialogFragment.showProgressBar(requireActivity())

            productsProvider?.create(files, product)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    ProgressDialogFragment.hideProgressBar(requireActivity())
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                    if(response.body()?.isSuccess == true) {
                        resetForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    ProgressDialogFragment.hideProgressBar(requireActivity())
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        }

    }

    private fun resetForm() {
        et_name?.setText("")
        et_description?.setText("")
        et_precio?.setText("")
        imageFile1 = null
        imageFile2 = null
        imageFile3 = null
        iv_image1?.setImageResource(R.drawable.ic_image_300)
        iv_image2?.setImageResource(R.drawable.ic_image_300)
        iv_image3?.setImageResource(R.drawable.ic_image_300)
    }

    private fun isValidForm(name: String, description: String, price: String) : Boolean {

        if(name.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Ingresa el nombre del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        if(description.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Ingresa el descripcion del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        if(price.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Ingresa el precio del producto", Toast.LENGTH_SHORT).show()
            return false
        }

        if(imageFile1 == null) {
            Toast.makeText(requireContext(), "Seleciona una imagen 1", Toast.LENGTH_SHORT).show()
            return false
        }

        if(imageFile2 == null) {
            Toast.makeText(requireContext(), "Seleciona una imagen 2", Toast.LENGTH_SHORT).show()
            return false
        }

        if(imageFile3 == null) {
            Toast.makeText(requireContext(), "Seleciona una imagen 3", Toast.LENGTH_SHORT).show()
            return false
        }

        if(idCategory.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Seleciona la categoria", Toast.LENGTH_SHORT).show()
            return false
        }

        return true

    }

    private fun getCategories() {
        categoriesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Category>> {
            override fun onResponse(call: Call<ArrayList<Category>>, response: Response<ArrayList<Category>>) {
                if(response.body() != null) {
                    categories = response.body()!!

                    val arrayAdapter = ArrayAdapter<Category>(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
                    spinnerCategories?.adapter = arrayAdapter
                    spinnerCategories?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
                            idCategory = categories[position].id!!
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }


                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            val fileUri = data?.data

            if(requestCode == 101) {
                imageFile1 = File(fileUri?.path)
                ivProduct1?.setImageURI(fileUri)
            } else if(requestCode == 102) {
                imageFile2 = File(fileUri?.path)
                ivProduct2?.setImageURI(fileUri)
            } else if(requestCode == 103) {
                imageFile3 = File(fileUri?.path)
                ivProduct3?.setImageURI(fileUri)
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage(requestCode: Int) {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1000, 1000)
                .start(requestCode)
    }

    private fun getUserFromSession() {
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()) {
            // Asegurarse de que el servicio no devuelva el password
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

}