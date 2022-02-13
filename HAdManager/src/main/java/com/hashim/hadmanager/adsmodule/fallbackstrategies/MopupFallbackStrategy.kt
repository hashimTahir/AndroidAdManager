package com.hashim.hadmanager.adsmodule.fallbackstrategies


import com.hashim.hadmanager.adsmodule.types.AdPriorityType
import com.hashim.hadmanager.adsmodule.types.AdsType

object MopupFallbackStrategy : Strategy {
    override fun hBannerStrategy(
        hGlobalPriority: AdPriorityType,
        hAdsType: AdsType
    ): AdPriorityType {
        return when (hGlobalPriority) {
            AdPriorityType.H_MOP_UP -> {
                when (hAdsType) {
                    AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
                    AdsType.H_MOPUP -> AdPriorityType.H_AD_MOB
                    AdsType.H_ADMOB -> AdPriorityType.H_FACE_BOOK
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
            AdPriorityType.H_MOP_UP -> {
                when (hAdsType) {
                    AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
                    AdsType.H_MOPUP -> AdPriorityType.H_AD_MOB
                    AdsType.H_ADMOB -> AdPriorityType.H_FACE_BOOK
                }
            }

            else -> AdPriorityType.H_NONE
        }
    }

    override fun hNativeAdvancedtrategy(hGlobalPriority: AdPriorityType, hAdsType: AdsType): AdPriorityType {
        return when (hGlobalPriority) {
            AdPriorityType.H_MOP_UP -> {
                when (hAdsType) {
                    AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
                    AdsType.H_MOPUP -> AdPriorityType.H_AD_MOB
                    AdsType.H_ADMOB -> AdPriorityType.H_FACE_BOOK
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
            AdPriorityType.H_MOP_UP -> {
                when (hAdsType) {
                    AdsType.H_FACEBOOK -> AdPriorityType.H_NONE
                    AdsType.H_MOPUP -> AdPriorityType.H_AD_MOB
                    AdsType.H_ADMOB -> AdPriorityType.H_FACE_BOOK
                }
            }

            else -> AdPriorityType.H_NONE
        }
    }

}