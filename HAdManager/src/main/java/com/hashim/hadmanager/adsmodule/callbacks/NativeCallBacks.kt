package com.hashim.hadmanager.adsmodule.callbacks

import android.view.ViewGroup
import com.hashim.hadmanager.adsmodule.Error
import com.hashim.hadmanager.adsmodule.types.AdsType
import com.hashim.hadmanager.adsmodule.types.WhatAd


interface NativeCallbacks {

    fun hAdLoaded(
        hAdType: AdsType,
        hWhatAd: WhatAd,
    )

    fun hAdClicked(
        hAdType: AdsType,
        hWhatAd: WhatAd
    )

    fun hAdImpression(
        hAdType: AdsType,
        hWhatAd: WhatAd
    )

    fun hAdClosed(
        hAdType: AdsType,
        hWhatAd: WhatAd
    )

    fun hAdFailedToLoad(
        hAdType: AdsType,
        hWhatAd: WhatAd,
        hError: Error,
        hNativeView: ViewGroup
    )

    fun hNativeAdOpened(
        hAdType: AdsType,
        hWhatAd: WhatAd
    )

}

