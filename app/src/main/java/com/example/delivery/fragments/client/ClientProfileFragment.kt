package com.example.delivery.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.delivery.R
import com.example.delivery.activities.MainActivity
import com.example.delivery.activities.SelectRolesActivity
import com.example.delivery.activities.client.update.ClientUpdateActivity
import com.example.delivery.models.User
import com.example.delivery.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_client_profile.*

class ClientProfileFragment : Fragment() {

    var myView: View? = null
    var sharedPref: SharedPref? = null
    var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_profile, container, false)

        sharedPref = SharedPref(requireActivity())

        btn_select_rol?.setOnClickListener { goToSelectRol() }
        btn_update_profile?.setOnClickListener { goToUpdate() }
        iv_logout?.setOnClickListener { logout() }


        getUserFromSession()

        tv_name?.text = "${user?.name}  ${user?.lastname}"
        tv_email?.text = user?.email
        tv_telefono?.text = user?.phone

        if(!user?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(user?.image).into(circleimage_user)
        }

        return myView
    }

    private fun goToUpdate() {
        val i = Intent(requireContext(), ClientUpdateActivity::class.java)
        startActivity(i)
    }

    private fun goToSelectRol() {
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Eliminar el historial de pantallas
        startActivity(i)
    }

    private fun logout() {
        sharedPref?.remove("user")
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession() {
        val gson = Gson()

        if(!sharedPref?.getData("user").isNullOrBlank()) {
            // Asegurarse de que el servicio no devuelva el password
             user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }

    }

}