package com.inmedia.radio.fragments.more

import android.os.Bundle
import com.inmedia.radio.BR
import com.inmedia.radio.R
import com.inmedia.radio.base.BaseFragment
import com.inmedia.radio.databinding.FragmentMoreBinding

class MoreFragment : BaseFragment<FragmentMoreBinding, MoreVM>(MoreVM::class) {

    override fun getBindingViewModelId() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_more

    override fun initFragmentViews(savedInstanceState: Bundle?) {
        super.initFragmentViews(savedInstanceState)

    }
}