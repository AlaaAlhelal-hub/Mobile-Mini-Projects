package com.example.manageincidentsapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.manageincidentsapp.databinding.ActivityOtpVerficationBinding
import com.example.manageincidentsapp.user.IncidentApiStatus
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
            val otp = binding.editTextNumber.text.toString()
            if (email != null && otp != null) {
                userViewModel.otpVerification(email, otp)
            }
        }

        userViewModel.otpStatus.observe(this, Observer { newStatus ->
            if (newStatus == IncidentApiStatus.Completed) {
                val intentListOfIncidents = Intent(this, ListOfIncidents::class.java)
                startActivity(intentListOfIncidents)
            }
        })

        userViewModel.property.observe(this, Observer { newProperty ->
            val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            var editor = sharedPreferences.edit()
            editor.putString("TOKEN", newProperty.token)
            editor.apply()

        })

        setContentView(binding.root)
    }

}