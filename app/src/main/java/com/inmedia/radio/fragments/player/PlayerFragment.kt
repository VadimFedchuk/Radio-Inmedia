package com.inmedia.radio.fragments.player

import android.os.Bundle
import com.inmedia.radio.BR
import com.inmedia.radio.R
import com.inmedia.radio.base.BaseFragment
import com.inmedia.radio.databinding.FragmentPlayerBinding

class PlayerFragment : BaseFragment<FragmentPlayerBinding, PlayerVM>(PlayerVM::class) {

    override fun getBindingViewModelId() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_player

    override fun initFragmentViews(savedInstanceState: Bundle?) {
        super.initFragmentViews(savedInstanceState)

    }
}