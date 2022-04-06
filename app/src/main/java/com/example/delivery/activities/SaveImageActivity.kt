package com.example.delivery.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.delivery.R
import com.example.delivery.activities.client.home.ClientHomeActivity
import com.example.delivery.models.ResponseHttp
import com.example.delivery.models.User
import com.example.delivery.providers.UsersProvider
import com.example.delivery.utils.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_save_image.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {

    private var imageFile: File? = null
    var usersProvider: UsersProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)

        sharedPref = SharedPref(this)
        getUserFromSession()
        usersProvider = UsersProvider(user?.session_token)

        circleImageUser.setOnClickListener { selectImage() }
        bt_next?.setOnClickListener { goToClientHome() }
        bt_confirm?.setOnClickListener { saveImage() }
    }

    private fun saveImage() {

        if(imageFile != null && user != null){
            usersProvider?.update(imageFile!!, user!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    Log.d("TAG", "RESPONSE: $response")
                    Log.d("TAG", "BODY: ${response.body()}")
                    saveUserInSession(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d("TAG", "Error: ${t.message}")
                    Toast.makeText(this@SaveImageActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this@SaveImageActivity, "La imagen no puede ser nula ni tampoco los datos de sesion de usuario", Toast.LENGTH_LONG).show()
        }

    }

    private fun saveUserInSession(data: String) {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
        goToClientHome()
    }

    private fun goToSelectRol() {
        val i = Intent(this, SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Eliminar el historial de pantallas
        startActivity(i)
    }

    private fun goToClientHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Eliminar el historial de pantallas
        startActivity(i)
    }

    private fun getUserFromSession() {
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()) {
            // Asegurarse de que el servicio no devuelva el password
             user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d("TAG", "Usuario: $user")
        }

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