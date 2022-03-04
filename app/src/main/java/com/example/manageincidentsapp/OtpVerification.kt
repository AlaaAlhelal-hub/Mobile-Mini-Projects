package com.example.manageincidentsapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.manageincidentsapp.databinding.ActivityOtpVerficationBinding
import com.example.manageincidentsapp.user.IncidentApiStatus
import com.example.manageincidentsapp.user.UserProperty
import com.example.manageincidentsapp.user.UserViewModel

class OtpVerification : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private val SHARED_PREFS = "shared_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding :ActivityOtpVerficationBinding = ActivityOtpVerficationBinding.inflate(layoutInflater)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.lifecycleOwner = this

        binding.userViewModel = userViewModel

        binding.sendOtp.setOnClickListener {
            var sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            var email = ""
            email = sharedPreferences.getString("USER_EMAIL", null).toString()
            val otp = binding.otpNumber.text.toString()
            if (email != null && otp != null) {
                userViewModel.otpVerification(UserProperty(email, otp))
            }
        }

        userViewModel.otpStatus.observe(this, Observer { newStatus ->
            if (newStatus == IncidentApiStatus.Completed) {
                val intentListOfIncidents = Intent(this, ListOfIncidents::class.java)
                startActivity(intentListOfIncidents)
            }
            else {
                binding.codeErrorMessage.visibility = View.VISIBLE
            }
        })

        userViewModel.loginResponse.observe(this, Observer { newResponse ->
            if ( newResponse != null) {
                val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("TOKEN", newResponse.token)
                editor.apply()
            }
        })

        setContentView(binding.root)
    }

}