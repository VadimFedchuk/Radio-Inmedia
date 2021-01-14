package com.inmedia.radio.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseActivity<D : ViewDataBinding, out T : BaseViewModel>(viewModelClass: KClass<T>) :
    AppCompatActivity() {

    lateinit var mViewDataBinding: D

    val viewModel: T by viewModel(viewModelClass)

    abstract fun getBindingViewModelId(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mViewDataBinding.lifecycleOwner = this
        performDataBinding(mViewDataBinding)
        lifecycle.addObserver(viewModel)

        initListenerEvent()
    }

    private fun initListenerEvent() {
        viewModel.navControllerLiveEvent.observe(this, {

        })

        viewModel.toastLiveEvent.observe(this, {
            showToast(it)
        })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        initViews(savedInstanceState)
        super.onPostCreate(savedInstanceState)
    }

    open fun initViews(savedInstanceState: Bundle?) {}

    private fun performDataBinding(viewDataBinding: D) {
        viewDataBinding.setVariable(getBindingViewModelId(), viewModel)
    }

    private fun showToast(model: ToastModel) {
        if(model.idResMessage != null) {
            Toast.makeText(this, model.idResMessage, Toast.LENGTH_SHORT).show()
        } else if(model.message != null){
            Toast.makeText(this, model.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}