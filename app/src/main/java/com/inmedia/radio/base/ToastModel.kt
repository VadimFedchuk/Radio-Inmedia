package com.inmedia.radio.base

import androidx.annotation.StringRes

data class ToastModel constructor(
    var message: String? = null,
    @StringRes val idResMessage: Int? = null
)