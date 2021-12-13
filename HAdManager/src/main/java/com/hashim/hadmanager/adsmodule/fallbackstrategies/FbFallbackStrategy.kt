package com.hashim.hadmanager.adsmodule.fallbackstrategies

import com.hashim.hadmanager.adsmodule.types.AdPriorityType
import com.hashim.hadmanager.adsmodule.types.AdsType

object FbFallbackStrategy : Strategy {

    override fun hBannerStrategy(hAdsType: AdsType): AdPriorityType {
        return when (hAdsType) {
            AdsType.H_MOPUP -> AdPriorityType.H_NONE
            AdsType.H_ADMOB -> AdPriorityType.H_MOP_UP
            AdsType.H_FACEBOOK -> AdPriorityType.H_AD_MOB
        }
    }


    override fun hNativeBannerStrategy(hAdsType: AdsType): AdPriorityType {
        return when (hAdsType) {
            AdsType.H_MOPUP -> AdPriorityType.H_NONE
            AdsType.H_ADMOB -> AdPriorityType.H_MOP_UP
            AdsType.H_FACEBOOK -> AdPriorityType.H_AD_MOB
        }
    }

    override fun hNativeAdvancedtrategy(hAdsType: AdsType): AdPriorityType {
        return when (hAdsType) {
            AdsType.H_MOPUP -> AdPriorityType.H_NONE
            AdsType.H_ADMOB -> AdPriorityType.H_MOP_UP
            AdsType.H_FACEBOOK -> AdPriorityType.H_AD_MOB
        }
    }

    override fun hInterstetialStrategy(hAdsType: AdsType): AdPriorityType {
        return when (hAdsType) {
            AdsType.H_MOPUP -> AdPriorityType.H_NONE
            AdsType.H_ADMOB -> AdPriorityType.H_MOP_UP
            AdsType.H_FACEBOOK -> AdPriorityType.H_AD_MOB
        }
    }

}