package com.example.manageincidents.presentaion.app.verifyOTP

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.manageincidents.ListOfIncidents
import com.example.manageincidents.data.utils.ApiStatus
import com.example.manageincidents.databinding.ActivityOtpVerficationBinding
import com.example.manageincidents.presentaion.base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OtpVerificationActivity :  BaseActivity() {


    private val viewModel: OtpViewModel by lazy {
        ViewModelProvider(this).get(OtpViewModel::class.java)
    }


    private lateinit var binding: ActivityOtpVerficationBinding


    companion object {
        fun startOtpActivity(context: Context) {
            val intent = Intent(context, OtpVerificationActivity::class.java)
            context.startActivity(intent)
        }
        fun newIntent(context: Context): Intent {
            return Intent(context, OtpVerificationActivity::class.java)
        }

        fun startOtpActivityClearStack(context: Context) {
            val intent = Intent(context, OtpVerificationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivityOtpVerficationBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this

        binding.otpViewModel = viewModel

        initUI()

        setContentView(binding.root)
    }

    ////////////
    private fun initUI(){

        binding.sendOtp.setOnClickListener {
            val otp = binding.otpNumber.text.toString()
            if (!otp.isNullOrEmpty()){
                viewModel.onDoneClicked(otp)
            }else{
                Log.i("Error ","${binding.otpNumber.text}")
            }
        }

        viewModel.loadingStatus.observe(this, Observer { newStatus ->
            if (newStatus) {
                binding.progressLayout.visibility = View.VISIBLE
            }
            else {
                binding.progressLayout.visibility = View.GONE
            }
        })

        viewModel.status.observe(this, Observer { newStatus ->
            if (newStatus == ApiStatus.Done) {
                startActivity(ListOfIncidents.newIntent(this))
            }
            /*
            else {
                binding.codeErrorMessage.visibility = View.VISIBLE
            }

             */
        })

        viewModel.responseError.observe(this, Observer {
            if (it.errorFlag) {
                showSnackBar(it.message.toString(), Snackbar.LENGTH_LONG)
            }
        })
    }
}