package com.example.manageincidentsapp

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.manageincidentsapp.user.ErrorHandler
import com.example.manageincidentsapp.user.IncidentApiStatus


@SuppressLint("ResourceAsColor")
@BindingAdapter(value = ["bind:loginStatus", "bind:errorHandler"], requireAll = false)
fun bindLoginStatus(statusTextView: TextView, loginStatus: IncidentApiStatus?, errorHandler: ErrorHandler? ) {
    when (loginStatus) {
        IncidentApiStatus.InProgress -> {
            statusTextView.setTextAppearance(R.style.MessageLoading)
            statusTextView.visibility = View.VISIBLE
            statusTextView.setText(R.string.sending_data)
        }
        IncidentApiStatus.Rejected -> {
            statusTextView.setTextAppearance(R.style.MessageError)
            statusTextView.visibility = View.VISIBLE
            if (errorHandler?.errorMessage != null) {
                statusTextView.text = errorHandler.errorMessage
            } else {
                statusTextView.setText(R.string.email_error_message)
            }
        }
        IncidentApiStatus.Completed -> {
            statusTextView.visibility = View.INVISIBLE
        }
    }
}



@SuppressLint("ResourceAsColor")
@BindingAdapter("OtpStatus")
fun bindOtpStatus(statusTextView: TextView, status: IncidentApiStatus?) {
    when (status) {
        IncidentApiStatus.InProgress -> {
            statusTextView.setTextAppearance(R.style.MessageLoading)
            statusTextView.visibility = View.VISIBLE
            statusTextView.setText(R.string.sending_data)
        }
        IncidentApiStatus.Rejected -> {
            statusTextView.setTextAppearance(R.style.MessageError)
            statusTextView.visibility = View.VISIBLE
            statusTextView.setText(R.string.wrong_code)
        }
        IncidentApiStatus.Completed -> {
            statusTextView.visibility = View.INVISIBLE
        }
    }
}
