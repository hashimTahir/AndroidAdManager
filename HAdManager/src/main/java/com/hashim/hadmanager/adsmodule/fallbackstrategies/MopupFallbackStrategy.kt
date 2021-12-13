package com.hashim.hadmanager.adsmodule.fallbackstrategies


import com.hashim.hadmanager.adsmodule.types.AdPriorityType
import com.hashim.hadmanager.adsmodule.types.AdsType

object MopupFallbackStrategy : Strategy {
    override fun hBannerStrategy(hAdsType: AdsType): AdPriorityType {
        return when (hAdsType) {
            AdsType.H_MOPUP -> AdPriorityType.H_AD_MOB
            AdsType.H_ADMOB -> AdPriorityType.H_FACE_BOOK
            AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
        }
    }


    override fun hNativeBannerStrategy(hAdsType: AdsType): AdPriorityType {
        return when (hAdsType) {
            AdsType.H_MOPUP -> AdPriorityType.H_AD_MOB
            AdsType.H_ADMOB -> AdPriorityType.H_FACE_BOOK
            AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
        }
    }

    override fun hNativeAdvancedtrategy(hAdsType: AdsType): AdPriorityType {
        return when (hAdsType) {
            AdsType.H_MOPUP -> AdPriorityType.H_AD_MOB
            AdsType.H_ADMOB -> AdPriorityType.H_FACE_BOOK
            AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
        }
    }

    override fun hInterstetialStrategy(hAdsType: AdsType): AdPriorityType {
        return when (hAdsType) {
            AdsType.H_MOPUP -> AdPriorityType.H_AD_MOB
            AdsType.H_ADMOB -> AdPriorityType.H_FACE_BOOK
            AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
        }
    }

}