package com.inmedia.radio.base

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.reflect.KClass

abstract class BaseFragment <D : ViewDataBinding, out T : BaseViewModel>(viewModelClass: KClass<T>) :
    Fragment(), KoinComponent {

    val viewModel: T by viewModel(viewModelClass)
    private val preferences: SharedPreferences by inject()

    var mActivity: BaseActivity<*,*>? = null
    lateinit var mViewDataBinding: D
        private set
    private lateinit var navController: NavController

    abstract fun getBindingViewModelId(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun initFragmentViews(savedInstanceState: Bundle?) { }

    open fun attachFragmentViews(view: View) { }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner
        performDataBinding(mViewDataBinding)
        viewModel.navControllerLiveEvent.observe(this, Observer { onNavigateTo(it) })
        viewModel.toastLiveEvent.observe(this, Observer { showToast(it) })
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        navController = NavHostFragment.findNavController(this)
        attachFragmentViews(view)
        initFragmentViews(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mActivity = context as BaseActivity<*,*>
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.addObserver(lifecycle)
    }

    fun performDataBinding(viewDataBinding: D) {
        viewDataBinding.setVariable(getBindingViewModelId(), viewModel)
        mViewDataBinding?.executePendingBindings()
    }

    fun onNavigateTo(navigationModel: NavigationModel) {
        if (navigationModel.popBack) {
            navController.navigateUp()
        } else if (navigationModel.direction != null && navController.currentDestination?.id != navigationModel.actionId) {
            navController.navigate(navigationModel.direction)
        } else if(navController.currentDestination?.id != navigationModel.actionId){
            navController.navigate(navigationModel.actionId, navigationModel.bundle)
        }
    }

//    fun openEmail(text: String, callbackSuccess:() -> Unit) {
//        val intent = Intent(Intent.ACTION_SEND).apply {
//            putExtra(Intent.EXTRA_EMAIL, arrayOf("appus.developer@gmail.com"))
//            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
//            type = "text/plain"
//            putExtra(Intent.EXTRA_TEXT, text)
//        }
//
//        if(intent.resolveActivity(requireActivity()?.packageManager) != null) {
//            val i = Intent.createChooser(intent, "Send email by:")
//            startActivity(i)
//            callbackSuccess.invoke()
//        } else {
//            showToast(ToastModel(getString(R.string.intent_not_resolve)))
//        }
//    }

//    fun share() {
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.type = "text/plain"
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
//        var shareMessage = "\nLet me recommend you this application\n"
//        shareMessage =
//            """
//            $shareMessage https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}&launch=true
//            """.trimIndent()
//        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
//        startActivity(Intent.createChooser(shareIntent, "choose one"))
//    }

    private fun showToast(model: ToastModel) {
        if(model.idResMessage != null) {
            Toast.makeText(mActivity, model.idResMessage, Toast.LENGTH_SHORT).show()
        } else if(model.message != null){
            Toast.makeText(mActivity, model.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun isIntentAvailable(ctx: Context, intent: Intent): Boolean {
        val packageManager: PackageManager = ctx.packageManager
        val list =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0
    }

    fun rateApp(callbackOpenSuccess:(Boolean) -> Unit) {
        callbackOpenSuccess(true)
        val uri = Uri.parse("market://details?id=" + requireContext().packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + requireContext().packageName)
                )
            )
        }
    }

    override fun onDestroyView() {
        viewModel.removeObserver(lifecycle)
        super.onDestroyView()
    }
}