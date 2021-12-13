package com.hashim.admanager

import android.app.Application
import com.hashim.hadmanager.adsmodule.AdManager
import com.hashim.hadmanager.adsmodule.types.AdPriorityType
import com.hashim.hadmanager.adsmodule.types.AdsType
import com.hashim.hadmanager.adsmodule.types.WhatAd
import timber.log.Timber

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        hInintTimber()

        val hIdsMap = hashMapOf<AdsType, HashMap<WhatAd, String>>()
        val hAdMobMap = hashMapOf<WhatAd, String>()
        val hMopUpMap = hashMapOf<WhatAd, String>()
        val hFacebookMap = hashMapOf<WhatAd, String>()

        hAdMobMap[WhatAd.H_NATIVE_ADVANCED] = getString(R.string.admob_native_advanced_id)
        hAdMobMap[WhatAd.H_NATIVE_BANNER] = getString(R.string.admob_native_advanced_id)
        hAdMobMap[WhatAd.H_BANNER] = getString(R.string.admob_banner_id)
        hAdMobMap[WhatAd.H_INTER] = getString(R.string.admob_interstitial_id)


        hMopUpMap[WhatAd.H_NATIVE_ADVANCED] = getString(R.string.mopup_native_banner_id)
        hMopUpMap[WhatAd.H_NATIVE_BANNER] = getString(R.string.mopup_native_banner_id)
        hMopUpMap[WhatAd.H_BANNER] = getString(R.string.mopub_banner_id)
        hMopUpMap[WhatAd.H_INTER] = getString(R.string.mopub_interstitial_id)

        hFacebookMap[WhatAd.H_NATIVE_ADVANCED] = getString(R.string.fb_native_advanced_id)
        hFacebookMap[WhatAd.H_NATIVE_BANNER] = getString(R.string.fb_native_banner_id)
        hFacebookMap[WhatAd.H_BANNER] = getString(R.string.fb_banner_id)
        hFacebookMap[WhatAd.H_INTER] = getString(R.string.fb_interstitial_id)

        hIdsMap[AdsType.H_ADMOB] = hAdMobMap
        hIdsMap[AdsType.H_FACEBOOK] = hFacebookMap
        hIdsMap[AdsType.H_MOPUP] = hMopUpMap

        AdManager.hInitializeAds(
            this,
            hIdsMap
        )
        AdManager.hSetNativeAdvancedPriority(AdPriorityType.H_MOP_UP)
        AdManager.hSetNativeBannerPriority(AdPriorityType.H_MOP_UP)

    }


    private fun hInintTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    message: String,
                    t: Throwable?
                ) {
                    super.log(priority, String.format("HashimTimberTags %s", tag), message, t)
                }
            })
        }
    }

}