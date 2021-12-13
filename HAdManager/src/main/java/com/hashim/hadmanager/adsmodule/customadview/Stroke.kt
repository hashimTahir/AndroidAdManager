package com.hashim.hadmanager.adsmodule.customadview

import android.content.Context
import androidx.core.content.ContextCompat
import com.hashim.hadmanager.R
import com.hashim.hadmanager.adsmodule.hDp


data class Stroke(
    private val hContext: Context,
    var hColor: Int = ContextCompat.getColor(
        hContext,
        R.color.colorPrimaryDark
    ),
    var hWidth: Int = 1,
) {
    init {
        hWidth = hDp(hContext, hWidth).toInt()
    }
}

