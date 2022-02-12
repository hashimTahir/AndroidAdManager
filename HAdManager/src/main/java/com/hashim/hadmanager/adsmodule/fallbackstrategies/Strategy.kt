package com.hashim.hadmanager.adsmodule.fallbackstrategies

import com.hashim.hadmanager.adsmodule.types.AdPriorityType
import com.hashim.hadmanager.adsmodule.types.AdsType

interface Strategy {
    fun hBannerStrategy(
        hGlobalPriority: AdPriorityType,
        hAdsType: AdsType,
    ): AdPriorityType

    fun hNativeAdvancedtrategy(
        hGlobalPriority: AdPriorityType,
        hAdsType: AdsType,
    ): AdPriorityType

    fun hInterstetialStrategy(
        hGlobalPriority: AdPriorityType,
        hAdsType: AdsType,
    ): AdPriorityType

    fun hNativeBannerStrategy(
        hGlobalPriority: AdPriorityType,
        hAdsType: AdsType,
    ): AdPriorityType
}