package com.hashim.hadmanager.adsmodule.callbacks

import android.view.ViewGroup
import com.hashim.hadmanager.adsmodule.Error
import com.hashim.hadmanager.adsmodule.types.AdsType
import com.hashim.hadmanager.adsmodule.types.WhatAd


abstract class AdCallbacks {

    open fun hAdLoaded(
        hAdType: AdsType,
        hWhatAd: WhatAd,
    ) {
    }

    open fun hAdClicked(
        hAdType: AdsType,
        hWhatAd: WhatAd
    ) {
    }

    open fun hAdImpression(
        hAdType: AdsType,
        hWhatAd: WhatAd
    ) {
    }

    open fun hAdClosed(
        hAdType: AdsType,
        hWhatAd: WhatAd
    ) {
    }

    open fun hAdFailedToLoad(
        hAdType: AdsType,
        hWhatAd: WhatAd,
        hError: Error,
        hNativeView: ViewGroup,
        hIsWithFallback: Boolean
    ) {
    }

    open fun hNativeAdOpened(
        hAdType: AdsType,
        hWhatAd: WhatAd
    ) {
    }

}

