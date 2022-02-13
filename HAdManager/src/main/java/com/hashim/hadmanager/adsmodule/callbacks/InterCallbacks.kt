package com.hashim.hadmanager.adsmodule.callbacks

import android.app.Activity
import com.hashim.hadmanager.adsmodule.Error
import com.hashim.hadmanager.adsmodule.types.AdsType

interface InterCallbacks {

    fun hOnAdFailedToLoad(
        hAdType: AdsType,
        hError: Error,
        hActivity: Activity? = null
    )

    fun hOnAddLoaded(
        hAdType: AdsType
    )

    fun hOnAdFailedToShowFullContent(
        hAdType: AdsType,
        hError: Error
    )

    fun hOnAddShowed(
        hAdType: AdsType
    )

    fun hOnAddDismissed(
        hAdType: AdsType
    )
}