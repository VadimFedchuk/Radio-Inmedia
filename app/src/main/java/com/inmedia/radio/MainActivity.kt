package com.inmedia.radio


import android.os.Bundle
import com.inmedia.radio.base.BaseActivity
import com.inmedia.radio.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, MainVM>(MainVM::class) {

    override fun getBindingViewModelId() = BR.viewModel

    override fun getLayoutId() = R.layout.activity_main

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        viewModel.addObserver(lifecycle)
    }

    override fun onDestroy() {
        viewModel.removeObserver(lifecycle)
        super.onDestroy()
    }
}