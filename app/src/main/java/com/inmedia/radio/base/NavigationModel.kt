package com.inmedia.radio.base

import android.os.Bundle
import androidx.navigation.NavDirections

data class NavigationModel(
    var actionId: Int = 0,
    var bundle: Bundle? = null,
    val direction: NavDirections? = null,
    var popTo: Int? = null,
    var inclusive: Boolean = false,
    val popBack: Boolean = false
)