package com.example.manageincidents.presentaion.base

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.example.manageincidents.R
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment

class BaseFragment : DaggerFragment() {
    protected var snackbar: Snackbar? = null

    protected fun showSnackBar(message: String, lenght: Int = Snackbar.LENGTH_LONG) {
        snackbar = view?.let { Snackbar.make(it, message, lenght) }
        snackbar?.show()
    }

    protected fun showSnackBar(
        message: String,
        actionTest: String,
        actionListener: View.OnClickListener,
        length: Int = Snackbar.LENGTH_LONG
    ) {
        snackbar = view?.let { Snackbar.make(it, message, length) }
        snackbar?.setAction(actionTest, actionListener)
        snackbar?.show()
    }

    protected fun showDialog(title: String, message: String): MaterialDialog {
        return MaterialDialog(Application().applicationContext!!)
            .title(text = title)
            .message(text = message)
            .cancelable(false)
            .cancelOnTouchOutside(false)
            .noAutoDismiss()
            .positiveButton(R.string.ok)
    }

    protected fun hideKeyboard() {
        val view = requireActivity()!!.currentFocus
        view?.let { v ->
            val imm =
                Application().applicationContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun initStatusBar(){
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            requireActivity().window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = requireActivity().window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    protected fun showSnackBar(message: String, length: Int = Snackbar.LENGTH_LONG, @StringRes actionText: Int = R.string.dismiss, @ColorRes actionTextColor: Int= R.color.primaryDarkColor, action: () -> Unit = {}) {
        snackbar = requireActivity().findViewById<View>(android.R.id.content)?.let { Snackbar.make(it, message, length) }
        snackbar?.setAction(getString(actionText)) {
            action()
            snackbar?.dismiss()
        }
        snackbar?.setActionTextColor(ContextCompat.getColor( requireActivity().applicationContext, actionTextColor))
        snackbar?.show()
    }
}