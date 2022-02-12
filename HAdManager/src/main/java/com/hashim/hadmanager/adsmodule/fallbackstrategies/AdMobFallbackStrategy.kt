package com.hashim.hadmanager.adsmodule.fallbackstrategies

import com.hashim.hadmanager.adsmodule.types.AdPriorityType
import com.hashim.hadmanager.adsmodule.types.AdsType

object AdMobFallbackStrategy : Strategy {
    override fun hBannerStrategy(hGlobalPriority: AdPriorityType, hAdsType: AdsType): AdPriorityType {
        return when (hGlobalPriority) {
            AdPriorityType.H_AD_MOB -> {
                when (hAdsType) {
                    AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
                    AdsType.H_MOPUP -> AdPriorityType.H_FACE_BOOK
                    AdsType.H_ADMOB -> AdPriorityType.H_MOP_UP
                }
            }

            else -> AdPriorityType.H_NONE
        }
    }


    override fun hNativeBannerStrategy(
        hGlobalPriority: AdPriorityType,
        hAdsType: AdsType
    ): AdPriorityType {
        return when (hGlobalPriority) {
            AdPriorityType.H_AD_MOB -> {
                when (hAdsType) {
                    AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
                    AdsType.H_MOPUP -> AdPriorityType.H_FACE_BOOK
                    AdsType.H_ADMOB -> AdPriorityType.H_MOP_UP
                }
            }

            else -> AdPriorityType.H_NONE
        }
    }


    override fun hNativeAdvancedtrategy(
        hGlobalPriority: AdPriorityType,
        hAdsType: AdsType
    ): AdPriorityType {
        return when (hGlobalPriority) {
            AdPriorityType.H_AD_MOB -> {
                when (hAdsType) {
                    AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
                    AdsType.H_MOPUP -> AdPriorityType.H_FACE_BOOK
                    AdsType.H_ADMOB -> AdPriorityType.H_MOP_UP
                }
            }

            else -> AdPriorityType.H_NONE
        }
    }

    override fun hInterstetialStrategy(
        hGlobalPriority: AdPriorityType,
        hAdsType: AdsType
    ): AdPriorityType {
        return when (hGlobalPriority) {
            AdPriorityType.H_AD_MOB -> {
                when (hAdsType) {
                    AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
                    AdsType.H_MOPUP -> AdPriorityType.H_FACE_BOOK
                    AdsType.H_ADMOB -> AdPriorityType.H_MOP_UP
                }
            }

            else -> AdPriorityType.H_NONE
        }
    }

}