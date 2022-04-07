package com.example.delivery.fragments.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.delivery.R
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_save_image.*
import kotlinx.android.synthetic.main.fragment_restaurant_product.*
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

        return myView
    }

    private fun createProduct() {
        val name = etTextName?.text.toString()
        val descripcion = etTextDescripcion?.text.toString()
        val priceText = etTextPrice?.text.toString()
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

}