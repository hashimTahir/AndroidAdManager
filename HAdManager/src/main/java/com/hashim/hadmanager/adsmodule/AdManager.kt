package com.hashim.hadmanager.adsmodule

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.hashim.hadmanager.adsmodule.callbacks.AdCallbacks
import com.hashim.hadmanager.adsmodule.callbacks.InterCallbacks
import com.hashim.hadmanager.adsmodule.customadview.HnativeAdvancedView
import com.hashim.hadmanager.adsmodule.customadview.HnativeBannerView
import com.hashim.hadmanager.adsmodule.fallbackstrategies.AdMobFallbackStrategy
import com.hashim.hadmanager.adsmodule.fallbackstrategies.FbFallbackStrategy
import com.hashim.hadmanager.adsmodule.fallbackstrategies.MopupFallbackStrategy
import com.hashim.hadmanager.adsmodule.types.AdPriorityType
import com.hashim.hadmanager.adsmodule.types.AdPriorityType.*
import com.hashim.hadmanager.adsmodule.types.AdsType
import com.hashim.hadmanager.adsmodule.types.AdsType.*
import com.hashim.hadmanager.adsmodule.types.WhatAd

object AdManager : InterCallbacks, AdCallbacks {

    private var hMopUpManager: MopUpManager? = null
    private var hFacebookManger: FaceBookAdManager? = null
    private var hAdMobManager: AdMobManager? = null


    private var hBannerPriorityType: AdPriorityType = H_AD_MOB
    private var hNativeBannerPriorityType: AdPriorityType = H_AD_MOB
    private var hNativeAdvancedPriorityType: AdPriorityType = H_AD_MOB
    private var hInterstitialPriorityType: AdPriorityType = H_AD_MOB

    private var hInterCallbacks: InterCallbacks? = null
    private var hAdCallbacks: AdCallbacks? = null

    fun hInitializeAds(
        hContext: Context,
        hIdsMap: HashMap<AdsType, HashMap<WhatAd, String>>,

        ) {
        hIdsMap.keys.forEach { adsType ->
            when (adsType) {
                H_FACEBOOK -> {
                    hFacebookManger = FaceBookAdManager(
                        hContext,
                        hIdsMap[adsType]
                    ).also {
                        it.hSetInterCallbacks(this)
                        it.hSetNativeCallbacks(this)
                    }
                }
                H_MOPUP -> {
                    hMopUpManager = MopUpManager(
                        hContext,
                        hIdsMap[adsType]
                    ).also {
                        it.hSetInterCallbacks(this)
                        it.hSetNativeCallbacks(this)
                    }

                }
                H_ADMOB -> {
                    hAdMobManager = AdMobManager(
                        hContext,
                        hIdsMap[adsType],
                    ).also {
                        it.hSetInterCallbacks(this)
                        it.hSetNativeCallbacks(this)
                    }
                }
            }
        }
    }

    fun hSetInterCallbacks(interCallbacks: InterCallbacks) {
        hInterCallbacks = interCallbacks
    }

    fun hSetNativeCallbacks(adCallbacks: AdCallbacks) {
        hAdCallbacks = adCallbacks
    }


    fun hLoadInterstitial(
        hActivity: Activity,
        hPriorityType: AdPriorityType = hInterstitialPriorityType
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hLoadInterstitialAd()
            H_MOP_UP -> hMopUpManager?.loadInterstitialAd(hActivity)
            H_FACE_BOOK -> hFacebookManger?.hLoadFbInterstitial()
            H_NONE -> Unit
        }

    }

    fun hShowInterstitial(
        activity: Activity,
        priority: AdPriorityType = hInterstitialPriorityType
    ) {
        when (priority) {
            H_AD_MOB -> {
                if (hAdMobManager?.hInterstitialAd != null) {
                    hAdMobManager?.hInterstitialAd?.show(activity)
                    return
                } else
                    hAdMobManager?.hLoadInterstitialAd()

            }
            H_FACE_BOOK -> {
                if (hFacebookManger?.hGetFbInterstitialAd() != null &&
                    hFacebookManger?.hGetFbInterstitialAd()?.isAdLoaded == true
                ) {
                    hFacebookManger?.hGetFbInterstitialAd()?.show()
                    return
                } else
                    hFacebookManger?.hLoadFbInterstitial()
            }
            H_MOP_UP -> {
                if (hMopUpManager?.hMopubInterstitial != null &&
                    hMopUpManager?.hMopubInterstitial?.isReady == true
                ) {
                    hMopUpManager!!.hMopubInterstitial?.show()
                    return
                } else
                    hMopUpManager?.loadInterstitialAd(activity)
            }
            else -> Unit
        }
    }

    fun hIsInterstitialAvailable(activity: Activity): Boolean {
        val hPriorityType: AdPriorityType = hInterstitialPriorityType
        when (hPriorityType) {
            H_AD_MOB -> {
                if (hAdMobManager?.hInterstitialAd != null) {
                    return true
                } else {
                    hAdMobManager?.hLoadInterstitialAd()
                }
            }
            H_FACE_BOOK -> {

                if (hFacebookManger?.hGetFbInterstitialAd() != null &&
                    hFacebookManger!!.hGetFbInterstitialAd()?.isAdLoaded == true
                ) {
                    return true
                } else {
                    hFacebookManger?.hLoadFbInterstitial()
                }
            }
            H_MOP_UP -> {
                if (hMopUpManager?.hMopubInterstitial != null &&
                    hMopUpManager?.hMopubInterstitial?.isReady == true
                ) {
                    return true
                } else
                    hMopUpManager?.loadInterstitialAd(activity)
            }
            else -> Unit
        }
        return false
    }

    private fun hGetInterFallBackPriority(hAdsType: AdsType): AdPriorityType {
        return when (hInterstitialPriorityType) {
            H_AD_MOB -> AdMobFallbackStrategy.hInterstetialStrategy(
                hGlobalPriority = hInterstitialPriorityType,
                hAdsType = hAdsType
            )
            H_MOP_UP -> MopupFallbackStrategy.hInterstetialStrategy(
                hGlobalPriority = hInterstitialPriorityType,
                hAdsType = hAdsType
            )
            H_FACE_BOOK -> FbFallbackStrategy.hInterstetialStrategy(
                hGlobalPriority = hInterstitialPriorityType,
                hAdsType = hAdsType
            )
            else -> H_NONE
        }
    }

    fun hShowBanner(
        bannerAdContainer: ViewGroup,
        hPriorityType: AdPriorityType = hBannerPriorityType
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowBanner(bannerAdContainer)
            H_MOP_UP -> hMopUpManager?.hShowBanner(bannerAdContainer)
            H_FACE_BOOK -> hFacebookManger?.hShowBanner(bannerAdContainer)
            else -> Unit
        }
    }


    fun hShowNativeBanner(
        hHnativeBannerView: HnativeBannerView,
        hPriorityType: AdPriorityType = hNativeBannerPriorityType,
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowNativeBanner(hHnativeBannerView)
            H_MOP_UP -> hMopUpManager?.hShowNativeBanner(hHnativeBannerView)
            H_FACE_BOOK -> hFacebookManger?.hShowNativeBanner(hHnativeBannerView)
            else -> Unit
        }
    }

    private fun hGetFallBackPriorityForNativeBanner(
        hAdsType: AdsType,
    ): AdPriorityType {
        return when (hNativeBannerPriorityType) {
            H_AD_MOB ->
                AdMobFallbackStrategy.hNativeBannerStrategy(
                    hGlobalPriority = hNativeBannerPriorityType,
                    hAdsType = hAdsType
                )
            H_MOP_UP -> MopupFallbackStrategy.hNativeBannerStrategy(
                hGlobalPriority = hNativeBannerPriorityType,
                hAdsType = hAdsType
            )
            H_FACE_BOOK -> FbFallbackStrategy.hNativeBannerStrategy(
                hGlobalPriority = hNativeBannerPriorityType,
                hAdsType = hAdsType
            )
            else -> H_NONE
        }
    }

    fun hShowNativeAdvanced(
        hnativeAdvancedView: HnativeAdvancedView,
        hPriorityType: AdPriorityType = hNativeAdvancedPriorityType
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowNativeAdvanced(hnativeAdvancedView)
            H_MOP_UP -> hMopUpManager?.hShowNativeAdvanced(hnativeAdvancedView)
            H_FACE_BOOK -> hFacebookManger?.hShowNativeAdvanced(hnativeAdvancedView)
            H_NONE -> Unit
        }
    }

    private fun hGetFallbackPriorityForNativeAdvanced(
        hAdsType: AdsType,
    ): AdPriorityType {
        return when (hNativeAdvancedPriorityType) {
            H_AD_MOB -> AdMobFallbackStrategy.hNativeAdvancedtrategy(
                hGlobalPriority = hNativeAdvancedPriorityType,
                hAdsType = hAdsType
            )
            H_MOP_UP -> MopupFallbackStrategy.hNativeAdvancedtrategy(
                hGlobalPriority = hNativeAdvancedPriorityType,
                hAdsType = hAdsType
            )
            H_FACE_BOOK -> FbFallbackStrategy.hNativeAdvancedtrategy(
                hGlobalPriority = hNativeAdvancedPriorityType,
                hAdsType = hAdsType
            )
            else -> H_NONE
        }
    }

    private fun hGetFallbackPriorityForBanner(
        hAdsType: AdsType,
    ): AdPriorityType {
        return when (hBannerPriorityType) {
            H_AD_MOB -> AdMobFallbackStrategy.hBannerStrategy(
                hGlobalPriority = hBannerPriorityType,
                hAdsType = hAdsType
            )
            H_MOP_UP -> MopupFallbackStrategy.hBannerStrategy(
                hGlobalPriority = hBannerPriorityType,
                hAdsType = hAdsType
            )
            H_FACE_BOOK -> FbFallbackStrategy.hBannerStrategy(
                hGlobalPriority = hBannerPriorityType,
                hAdsType = hAdsType
            )
            else -> H_NONE
        }
    }


    fun hShowBannerWithOutFallback(
        bannerAdContainer: ViewGroup,
        hPriorityType: AdPriorityType = hBannerPriorityType
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowBanner(
                hAdViewGroup = bannerAdContainer,
                hIsWithFallback = false
            )
            H_MOP_UP -> hMopUpManager?.hShowBanner(
                hAdViewGroup = bannerAdContainer,
                hIsWithFallback = false
            )
            H_FACE_BOOK -> hFacebookManger?.hShowBanner(
                hAdViewGroup = bannerAdContainer,
                hIsWithFallback = false
            )
            else -> Unit
        }
    }


    fun hShowNativeBannerWithOutFallback(
        hHnativeBannerView: HnativeBannerView,
        hPriorityType: AdPriorityType = hNativeBannerPriorityType,
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowNativeBanner(
                hNativeBannerView = hHnativeBannerView,
                hIsWithFallback = false
            )
            H_MOP_UP -> hMopUpManager?.hShowNativeBanner(
                hNativeBannerView = hHnativeBannerView,
                hIsWithFallback = false
            )
            H_FACE_BOOK -> hFacebookManger?.hShowNativeBanner(
                hNativeBannerView = hHnativeBannerView,
                hIsWithFallback = false
            )
            else -> Unit
        }
    }


    fun hShowNativeAdvancedWithOutFallback(
        hHnativeBannerView: HnativeAdvancedView,
        hPriorityType: AdPriorityType = hNativeBannerPriorityType,
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowNativeAdvanced(
                hNativeAdvancedView = hHnativeBannerView,
                hIsWithFallback = false
            )
            H_MOP_UP -> hMopUpManager?.hShowNativeAdvanced(
                hNativeAdvancedView = hHnativeBannerView,
                hIsWithFallback = false
            )
            H_FACE_BOOK -> hFacebookManger?.hShowNativeAdvanced(
                hNativeAdvancedView = hHnativeBannerView,
                hIsWithFallback = false
            )
            else -> Unit
        }
    }


    /*For Manually chaning the priorities*/
    fun hSetNativeBannerPriority(
        nativeBannerPriorityType: AdPriorityType
    ) {
        hNativeBannerPriorityType = nativeBannerPriorityType
    }

    fun hSetNativeAdvancedPriority(
        nativeAdvancedPriorityType: AdPriorityType
    ) {
        hNativeAdvancedPriorityType = nativeAdvancedPriorityType
    }

    fun hSetInterstitialPriority(
        interstitialPriorityType: AdPriorityType
    ) {
        hInterstitialPriorityType = interstitialPriorityType
    }

    fun hSetBannerPriority(
        bannerPriorityType: AdPriorityType
    ) {
        hBannerPriorityType = bannerPriorityType
    }


    /*---------------------InterCallBacks ------------------------*/
    override fun hOnAdFailedToLoad(hAdType: AdsType, hError: Error, hActivity: Activity?) {
        hInterCallbacks?.hOnAdFailedToLoad(hAdType, hError)
        hActivity?.let {
            hLoadInterstitial(
                hPriorityType = hGetInterFallBackPriority(hAdType),
                hActivity = it
            )
        }
    }

    override fun hOnAddLoaded(hAdType: AdsType) {
        hInterCallbacks?.hOnAddLoaded(hAdType)
    }

    override fun hOnAdFailedToShowFullContent(hAdType: AdsType, hError: Error) {
        hInterCallbacks?.hOnAdFailedToShowFullContent(hAdType, hError)
    }

    override fun hOnAddShowed(hAdType: AdsType) {
        hInterCallbacks?.hOnAddShowed(hAdType)
    }

    override fun hOnAddDismissed(hAdType: AdsType) {
        hInterCallbacks?.hOnAddDismissed(hAdType)
    }

    /*-------------------End-InterCallBacks ------------------------*/



    /*---------------------AddCallbacks ------------------------*/
    override fun hAdLoaded(
        hAdType: AdsType,
        hWhatAd: WhatAd
    ) {
        hAdCallbacks?.hAdLoaded(
            hAdType = hAdType,
            hWhatAd = hWhatAd
        )
    }

    override fun hAdClicked(
        hAdType: AdsType,
        hWhatAd: WhatAd
    ) {
        hAdCallbacks?.hAdClicked(
            hAdType = hAdType,
            hWhatAd = hWhatAd
        )
    }

    override fun hAdImpression(
        hAdType: AdsType,
        hWhatAd: WhatAd
    ) {
        hAdCallbacks?.hAdImpression(
            hAdType = hAdType,
            hWhatAd = hWhatAd
        )
    }

    override fun hAdClosed(
        hAdType: AdsType,
        hWhatAd: WhatAd
    ) {
        hAdCallbacks?.hAdClosed(
            hAdType = hAdType,
            hWhatAd = hWhatAd
        )
    }

    override fun hAdFailedToLoad(
        hAdType: AdsType,
        hWhatAd: WhatAd,
        hError: Error,
        hNativeView: ViewGroup,
        hIsWithFallback: Boolean
    ) {
        hAdCallbacks?.hAdFailedToLoad(
            hAdType = hAdType,
            hWhatAd = hWhatAd,
            hError = hError,
            hNativeView = hNativeView,
            hIsWithFallback = hIsWithFallback,

            )
        when (hIsWithFallback) {
            true -> when (hWhatAd) {
                WhatAd.H_NATIVE_BANNER -> hShowNativeBanner(
                    hHnativeBannerView = hNativeView as HnativeBannerView,
                    hPriorityType = hGetFallBackPriorityForNativeBanner(hAdsType = hAdType)
                )
                WhatAd.H_NATIVE_ADVANCED -> hShowNativeAdvanced(
                    hnativeAdvancedView = hNativeView as HnativeAdvancedView,
                    hPriorityType = hGetFallbackPriorityForNativeAdvanced(hAdsType = hAdType)
                )
                WhatAd.H_BANNER -> hShowBanner(
                    bannerAdContainer = hNativeView,
                    hPriorityType = hGetFallbackPriorityForBanner(hAdsType = hAdType)
                )
                WhatAd.H_INTER -> Unit
            }
            false -> Unit
        }

    }

    override fun hNativeAdOpened(
        hAdType: AdsType,
        hWhatAd: WhatAd
    ) {
        hAdCallbacks?.hNativeAdOpened(
            hAdType = hAdType,
            hWhatAd = hWhatAd
        )
    }

    /*-------------------End-NativeCallbacks ------------------------*/
}




