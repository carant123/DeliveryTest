package com.example.delivery.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.delivery.R
import com.example.delivery.activities.client.home.ClientHomeActivity
import com.example.delivery.models.ResponseHttp
import com.example.delivery.models.User
import com.example.delivery.providers.UsersProvider
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    var usersProvider = UsersProvider(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        iv_go_to_login.setOnClickListener {
            goToClientHome()
        }

        bt_registrarse.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name = et_nombre?.text.toString()
        val lastname = et_apellido?.text.toString()
        val email = et_email?.text.toString()
        val phone = et_telefono?.text.toString()
        val password = et_password?.text.toString()
        val confirmPassword = et_password_confirm?.text.toString()

        if(isValidForm(name, lastname, email, phone, password, confirmPassword)) {
            Toast.makeText(this, "El formulario es valido", Toast.LENGTH_LONG).show()

            val user = User(
                name = name,
                lastname = lastname,
                email = email,
                phone = phone,
                password = password
            )

            usersProvider.register(user)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    if(response.body()?.isSuccess == true) {
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }

                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        } else {
            Toast.makeText(this, "No es valido", Toast.LENGTH_LONG).show()
        }

    }

    private fun goToClientHome() {
        val i = Intent(this, SaveImageActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Eliminar el historial de pantallas
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

    private fun isValidForm(
            name: String,
            lastname: String,
            email: String,
            phone: String,
            password: String,
            confirmpassword: String
    ): Boolean {

        if (name.isBlank()) {
            return false
        }

        if (lastname.isBlank()) {
            return false
        }

        if (email.isBlank()) {
            return false
        }

        if (phone.isBlank()) {
            return false
        }

        if(password.isBlank()) {
            return false
        }

        if(!email.isEmailValid()) {
            return false
        }

        if(password != confirmpassword) {
            return false
        }

        return true

    }


}