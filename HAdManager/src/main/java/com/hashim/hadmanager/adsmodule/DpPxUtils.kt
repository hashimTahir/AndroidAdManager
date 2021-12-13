package com.hashim.hadmanager.adsmodule

import android.content.Context
import android.util.TypedValue


fun hDp(context: Context, hCornerRadius: Int): Float {

    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        hCornerRadius.toFloat(),
        context.resources.displayMetrics
    )
}