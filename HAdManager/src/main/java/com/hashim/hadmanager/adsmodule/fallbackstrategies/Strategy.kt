package com.hashim.hadmanager.adsmodule.fallbackstrategies

import com.hashim.hadmanager.adsmodule.types.AdPriorityType
import com.hashim.hadmanager.adsmodule.types.AdsType

interface Strategy {
    fun hBannerStrategy(hAdsType: AdsType): AdPriorityType

    fun hNativeAdvancedtrategy(hAdsType: AdsType): AdPriorityType

    fun hInterstetialStrategy(hAdsType: AdsType): AdPriorityType

    fun hNativeBannerStrategy(hAdsType: AdsType): AdPriorityType
}