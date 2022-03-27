package com.example.manageincidents.presentaion.app.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.manageincidents.presentaion.app.verifyOTP.OtpVerificationActivity
import com.example.manageincidents.data.utils.ApiStatus
import com.example.manageincidents.databinding.ActivityMainBinding
import com.example.manageincidents.presentaion.base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    companion object {
        fun startLoginActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }

        fun startLoginActivityClearStack(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(getLayoutInflater())

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        initStatusBar()

        initUI()

        binding.LoginBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            viewModel.onLoginClicked(email)
        }

    setContentView(binding.root)
    }



    //////////////
    private fun initUI() {

        viewModel.emptyEmail.observe(this, Observer {
            if (it.flag)
                binding.emailInput.error = it.messageId
        })

        viewModel.invalidEmail.observe(this, Observer {
            if (it.flag)
                binding.emailInput.error = it.messageId
        })

        viewModel.responseError.observe(this, Observer {
            if (it.errorFlag) {
                showSnackBar(it.message.toString(), Snackbar.LENGTH_LONG)
            }
        })

        viewModel.loadingStatus.observe(this, Observer {
            disableScreenElementWhileLoading(it)
        })

        viewModel.loginStatus.observe(this, Observer { newStatus ->
            if (newStatus == ApiStatus.Done){
                startActivity(OtpVerificationActivity.newIntent(this))
            }
        })

        viewModel.loadingStatus.observe(this, Observer { newStatus ->
            if (newStatus) {
                binding.progressLayout.visibility = View.VISIBLE
            }
            else {
                binding.progressLayout.visibility = View.GONE
            }
        })
    }



    private fun disableScreenElementWhileLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressLayout.visibility = View.VISIBLE
            binding.LoginBtn.isEnabled = false
            binding.emailInput.isEnabled = false
        } else {
            binding.progressLayout.visibility = View.GONE
            binding.LoginBtn.isEnabled = true
            binding.emailInput.isEnabled = true
        }
    }



}