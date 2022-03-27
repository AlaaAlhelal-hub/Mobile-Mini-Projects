package com.example.manageincidents.presentaion.base

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer

abstract class BaseMVVMActivity <STATE, VM : BaseViewModel<STATE>> : BaseActivity() {

    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        if (this is HasDataBinding<*>) {
            setContentView(this, getLayoutId())
        } else
            setContentView(getLayoutId())
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onInitView()
    }


    private fun setupViewModel() {
        viewModel.onLoadData(intent.extras)
        viewModel.state.observe(this, Observer(::onStateChanged))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * @return  a layout resource id ex. R.layout.activity_main
     */
    abstract fun getLayoutId(): Int

    /**
     * for set up views like toolbars and other stuff
     */
    open fun onInitView() {}

    abstract fun onStateChanged(state: STATE?)

}