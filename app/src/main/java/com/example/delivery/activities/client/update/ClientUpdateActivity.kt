package com.example.delivery.activities.client.update

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.delivery.R
import com.example.delivery.models.ResponseHttp
import com.example.delivery.models.User
import com.example.delivery.providers.UsersProvider
import com.example.delivery.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_client_update.*
import kotlinx.android.synthetic.main.activity_client_update.et_apellido
import kotlinx.android.synthetic.main.activity_client_update.et_nombre
import kotlinx.android.synthetic.main.activity_client_update.et_telefono
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_save_image.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ClientUpdateActivity : AppCompatActivity() {

    var sharedPref: SharedPref? = null
    var user: User? = null

    private var imageFile: File? = null
    var usersProvider: UsersProvider? = null

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_update)
        sharedPref = SharedPref(this)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Editar perfil"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getUserFromSession()

        usersProvider = UsersProvider(user?.session_token)

        et_nombre?.setText(user?.name)
        et_apellido?.setText(user?.lastname)
        et_telefono?.setText(user?.phone)

        if(!user?.image.isNullOrBlank()){
            Glide.with(this).load(user?.image).into(circleimage_user)
        }

        circleimage_user?.setOnClickListener { selectImage() }
        bt_update?.setOnClickListener { updateData() }

    }

    private fun getUserFromSession() {
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()) {
            // Asegurarse de que el servicio no devuelva el password
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

    private fun updateData() {

        val name = et_nombre?.text.toString()
        val lastname = et_apellido?.text.toString()
        val phone = et_telefono?.text.toString()

        user?.name = name
        user?.lastname = lastname
        user?.phone = phone

        if(imageFile != null) {
            usersProvider?.update(imageFile!!, user!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    if(response.body()?.isSuccess == true) {
                        Log.d("TAG", "RESPONSE: $response")
                        Log.d("TAG", "BODY: ${response.body()}")
                        saveUserInSession(response.body()?.data.toString())
                    }

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d("TAG", "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            usersProvider?.updateWithoutImage(user!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    if(response.body()?.isSuccess == true) {
                        Log.d("TAG", "RESPONSE: $response")
                        Log.d("TAG", "BODY: ${response.body()}")
                        saveUserInSession(response.body()?.data.toString())
                    }

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d("TAG", "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }


    }

    private fun saveUserInSession(data: String) {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if(resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path)
                circleImageUser?.setImageURI(fileUri)
            } else if(resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Tarea se cancelo", Toast.LENGTH_LONG).show()
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