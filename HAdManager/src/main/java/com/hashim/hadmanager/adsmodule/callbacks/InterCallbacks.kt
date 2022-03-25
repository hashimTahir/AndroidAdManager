package com.hashim.hadmanager.adsmodule.callbacks

import android.app.Activity
import com.hashim.hadmanager.adsmodule.Error
import com.hashim.hadmanager.adsmodule.types.AdsType

abstract class InterCallbacks {

    open fun hOnAdFailedToLoad(
        hAdType: AdsType,
        hError: Error,
        hActivity: Activity? = null
    ) {

    }

    open fun hOnAddLoaded(
        hAdType: AdsType
    ) {
    }

    open fun hOnAdFailedToShowFullContent(
        hAdType: AdsType,
        hError: Error
    ) {
    }

    open fun hOnAddShowed(
        hAdType: AdsType
    ) {
    }

    open fun hOnAddDismissed(
        hAdType: AdsType
    ) {
    }

    open fun hOnAdTimedOut(
        hAdType: AdsType
    ) {

    }
}