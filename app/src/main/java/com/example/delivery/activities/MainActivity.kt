package com.example.delivery.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.delivery.R
import com.example.delivery.activities.client.home.ClientHomeActivity
import com.example.delivery.models.ResponseHttp
import com.example.delivery.models.User
import com.example.delivery.providers.UsersProvider
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var usersProvider = UsersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeButton()
        getUserFromSession()
    }

    private fun initializeButton() {
        iv_go_to_register?.setOnClickListener {
            irARegistro()
        }

        bt_login?.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = et_email.text.toString()
        val password = et_password.text.toString()

        if(isValidForm(email, password)) {

            usersProvider.login(email, password)?.enqueue(object: Callback<ResponseHttp> {

                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    if(response.body()?.isSuccess == true){
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    } else {
                        Toast.makeText(this@MainActivity, "Los datos no son correctos", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        } else {
            Toast.makeText(this, "No es valido", Toast.LENGTH_LONG).show()
        }

        Toast.makeText(this, "El email es: $email", Toast.LENGTH_LONG).show()
        Toast.makeText(this, "El passwrod es: $password", Toast.LENGTH_LONG).show()
    }

    private fun irARegistro() {
        var intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun goToClientHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        startActivity(i)
    }

    private fun saveUserInSession(data: String) {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun getUserFromSession() {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        if(!sharedPref.getData("user").isNullOrBlank()) {
            // Asegurarse de que el servicio no devuelva el password
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)
            goToClientHome()
        }
    }

    private fun isValidForm(email: String, password: String): Boolean {

        if (email.isBlank()) {
            return false
        }

        if(password.isBlank()) {
            return false
        }

        if(email.isEmailValid()) {
            return false
        }

        return true

    }

}