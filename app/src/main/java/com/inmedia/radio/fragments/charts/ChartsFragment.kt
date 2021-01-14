package com.inmedia.radio.fragments.charts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inmedia.radio.BR
import com.inmedia.radio.R
import com.inmedia.radio.base.BaseFragment
import com.inmedia.radio.databinding.FragmentChartsBinding
import com.inmedia.radio.databinding.FragmentOrderSongBinding
import com.inmedia.radio.fragments.order_song.OrderSongVM


class ChartsFragment : BaseFragment<FragmentChartsBinding, ChartsVM>(ChartsVM::class) {

    override fun getBindingViewModelId() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_charts

    override fun initFragmentViews(savedInstanceState: Bundle?) {
        super.initFragmentViews(savedInstanceState)

    }
}