package com.hashim.hadmanager.adsmodule.callbacks

import com.hashim.hadmanager.adsmodule.Error
import com.hashim.hadmanager.adsmodule.customadview.HnativeAdvancedView
import com.hashim.hadmanager.adsmodule.types.AdsType

interface NativeCallBacks {
    fun hOnNativeAdvancedFailed(
        hAdType: AdsType,
        hError: Error,
        hNativeAdvanceView: HnativeAdvancedView
    )

    fun hOnAdClosed(hAdType: AdsType)
    fun hOnAdOpened(hAdType: AdsType)
    fun hOnAdLoaded(hAdType: AdsType)
    fun hOnAdClicked(hAdType: AdsType)
    fun hOnAdImpression(hAdType: AdsType)

}
