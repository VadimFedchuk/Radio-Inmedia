package com.inmedia.radio.fragments.order_song

import android.os.Bundle
import com.inmedia.radio.BR
import com.inmedia.radio.R
import com.inmedia.radio.base.BaseFragment
import com.inmedia.radio.databinding.FragmentOrderSongBinding


class OrderSongFragment : BaseFragment<FragmentOrderSongBinding, OrderSongVM>(OrderSongVM::class) {

    override fun getBindingViewModelId() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_order_song

    override fun initFragmentViews(savedInstanceState: Bundle?) {
        super.initFragmentViews(savedInstanceState)

    }
}