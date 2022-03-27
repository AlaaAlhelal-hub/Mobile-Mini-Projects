package com.example.manageincidents.presentaion.base

import android.graphics.Color
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.example.manageincidents.R
import com.example.manageincidents.presentaion.utils.LocaleContextWrapper
import com.example.manageincidents.presentaion.utils.LocaleUtil

import com.google.android.material.snackbar.Snackbar

import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    var snackbar: Snackbar? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatusBar()
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun attachBaseContext(newBase: Context) {
        val localeUtil = LocaleUtil(newBase.applicationContext)
        val locale = if (localeUtil.isArabicLocale) Locale("ar", "SA") else Locale.US
        val localeContextWrapper = LocaleContextWrapper.wrap(newBase, locale)
        super.attachBaseContext(localeContextWrapper)
    }

    fun initStatusBar(){
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    protected fun showSnackBar(message: String, length: Int = Snackbar.LENGTH_LONG, @StringRes actionText: Int = R.string.dismiss, @ColorRes actionTextColor: Int= R.color.secondaryLightColor, action: () -> Unit = {}) {
        snackbar = findViewById<View>(android.R.id.content)?.let { Snackbar.make(it, message, length) }
        snackbar?.setAction(getString(actionText)) {
            action()
            snackbar?.dismiss()
        }
        snackbar?.setActionTextColor(ContextCompat.getColor(this, actionTextColor))
        snackbar?.show()
    }

    protected fun showSnackBar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        snackbar =
            findViewById<View>(android.R.id.content)?.let { Snackbar.make(it, message, length) }
        snackbar?.show()
    }

    protected fun showSnackBar(
        message: String,
        actionTest: String,
        actionListener: View.OnClickListener,
        length: Int = Snackbar.LENGTH_LONG
    ) {
        snackbar =
            findViewById<View>(android.R.id.content)?.let { Snackbar.make(it, message, length) }
        snackbar?.setAction(actionTest, actionListener)
        snackbar?.setActionTextColor(resources.getColor(R.color.primaryDarkColor))
        snackbar?.show()
    }


    protected fun showDialog(title: String, message: String): MaterialDialog {
        return MaterialDialog(this)
            .title(text = title)
            .message(text = message)
            .cancelable(false)
            .cancelOnTouchOutside(false)
            .noAutoDismiss()
            .positiveButton(R.string.ok)
    }

    protected fun hideKeyboard() {
        val view = this.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}
