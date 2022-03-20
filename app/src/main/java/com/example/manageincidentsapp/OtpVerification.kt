package com.example.manageincidentsapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.manageincidentsapp.databinding.ActivityOtpVerficationBinding
import com.example.manageincidentsapp.network.SharedPreferenceManager
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
            //val sharedPreferences = SharedPreferenceManager.getInstance(applicationContext).sharedPreferences
            var email = ""
            //email = sharedPreferences.getString("USER_EMAIL", null).toString()
            //val sharedPreferences = SharedPreferenceManager.getInstance(applicationContext).sharedPreferences
            email = SharedPreferenceManager.getInstance(applicationContext).getStringValue("USER_EMAIL", null).toString()

            val otp = binding.otpNumber.text.toString()
            if (email != null && otp != null) {
                userViewModel.otpVerification(UserProperty(email, otp))
            }
        }

        userViewModel.loadingStatus.observe(this, Observer { newStatus ->
            if (newStatus) {
                binding.progressLayout.visibility = View.VISIBLE
            }
            else {
                binding.progressLayout.visibility = View.GONE
            }
        })


        userViewModel.otpStatus.observe(this, Observer { newStatus ->
            if (newStatus == IncidentApiStatus.Done) {
                val intentListOfIncidents = Intent(this, ListOfIncidents::class.java)
                startActivity(intentListOfIncidents)
            }
            else {
                binding.codeErrorMessage.visibility = View.VISIBLE
            }
        })

        userViewModel.loginResponse.observe(this, Observer { newResponse ->
            if ( newResponse != null) {
                SharedPreferenceManager.getInstance(applicationContext).putStringValue("TOKEN", "Bearer ${newResponse.token}")
               // sharedPreferences.
                //val editor = sharedPreferences.edit()
                //editor.putString("TOKEN", newResponse.token)
                //editor.apply()
            }
        })

        setContentView(binding.root)
    }

}