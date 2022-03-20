package com.example.manageincidentsapp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.manageincidentsapp.databinding.ActivityMainBinding
import com.example.manageincidentsapp.network.SharedPreferenceManager
import com.example.manageincidentsapp.user.IncidentApiStatus
import com.example.manageincidentsapp.user.UserViewModel

class MainActivity : AppCompatActivity() {


    lateinit var userViewModel: UserViewModel
    private  lateinit var USER_EMAIL: String
    lateinit var sharedpreferences: SharedPreferences
    val SHARED_PREFS = "shared_prefs"

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater())

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.lifecycleOwner = this

        binding.userViewModel = userViewModel

        binding.LoginBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            USER_EMAIL = email
            userViewModel.checkUserEmail(email)
        }

        userViewModel.loadingStatus.observe(this, Observer { newStatus ->
            if (newStatus) {
                binding.progressLayout.visibility = View.VISIBLE
            }
            else {
                binding.progressLayout.visibility = View.GONE
            }

        })
        userViewModel.errorHandler.observe(this, Observer { newError ->
            binding.errorMessage.text = newError.errorMessage
        })

        userViewModel.loginStatus.observe(this, Observer { newStatus ->

            if (newStatus == IncidentApiStatus.Done){
                /*
                sharedpreferences = SharedPreferenceManager.getInstance(applicationContext).sharedPreferences
                //getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                val editor = sharedpreferences.edit()
                editor.putString("USER_EMAIL", USER_EMAIL)
                editor.apply()
                */
                SharedPreferenceManager.getInstance(applicationContext).putStringValue("USER_EMAIL", USER_EMAIL)
                val intentOtpVerification = Intent(this, OtpVerification::class.java)
                startActivity(intentOtpVerification)
            }

        })


    setContentView(binding.root)
    }


}