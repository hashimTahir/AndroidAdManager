package com.hashim.hadmanager.adsmodule

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
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
import timber.log.Timber

object AdManager {

    private var hMopUpManager: MopUpManager? = null
    private var hFacebookManger: FaceBookAdManager? = null
    private var hAdMobManager: AdMobManager? = null


    private var hBannerPriorityType: AdPriorityType = H_AD_MOB
    private var hNativeBannerPriorityType: AdPriorityType = H_AD_MOB
    private var hNativeAdvancedPriorityType: AdPriorityType = H_AD_MOB
    private var hInterstitialPriorityType: AdPriorityType = H_AD_MOB

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
                    )
                }
                H_MOPUP -> {
                    hMopUpManager = MopUpManager(
                        hContext,
                        hIdsMap[adsType]
                    )

                }
                H_ADMOB -> {
                    hAdMobManager = AdMobManager(
                        hContext,
                        hIdsMap[adsType]
                    )
                }
            }
        }
    }


    fun hLoadInterstitial(activity: Activity) {
        hAdMobManager?.hLoadInterstitialAd { adsType, errorMessage ->
            Timber.d("hAdMobManager failed with errorr message $errorMessage")
            hCheckFallBackSequenceForInterstitial(adsType, activity)

        }
        hFacebookManger?.hLoadFbInterstitial { adsType, errorMessage ->
            Timber.d("hFacebookManger failed with errorr message $errorMessage")
            hCheckFallBackSequenceForInterstitial(adsType, activity)
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
                    hAdMobManager?.hLoadInterstitialAd { adsType, errorMessage ->
                        Timber.d("AdMob Inter failed with error message $errorMessage")
                        hCheckFallBackSequenceForInterstitial(
                            adsType,
                            activity
                        )
                    }

            }
            H_FACE_BOOK -> {
                if (hFacebookManger?.hGetFbInterstitialAd() != null &&
                    hFacebookManger?.hGetFbInterstitialAd()?.isAdLoaded == true
                ) {
                    hFacebookManger?.hGetFbInterstitialAd()?.show()
                    return
                } else
                    hFacebookManger?.hLoadFbInterstitial { adsType, errorMessage ->
                        Timber.d("Facebook Inter failed with error message $errorMessage")
                        hCheckFallBackSequenceForInterstitial(
                            adsType,
                            activity
                        )
                    }
            }
            H_MOP_UP -> {
                if (hMopUpManager?.hMopubInterstitial != null &&
                    hMopUpManager?.hMopubInterstitial?.isReady == true
                ) {
                    hMopUpManager!!.hMopubInterstitial?.show()
                    return
                } else
                    hMopUpManager?.loadInterstitialAd(activity) { adsType, errorMessage ->
                        Timber.d("OnMop Inter failed with error message $errorMessage")
                        hCheckFallBackSequenceForInterstitial(
                            adsType,
                            activity
                        )
                    }
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
                    hAdMobManager?.hLoadInterstitialAd { adsType, errorMessage ->
                        Timber.d("hAdMobManager failed with errorr message $errorMessage")
                        hCheckFallBackSequenceForInterstitial(
                            adsType,
                            activity
                        )
                    }
                }
            }
            H_FACE_BOOK -> {

                if (hFacebookManger?.hGetFbInterstitialAd() != null &&
                    hFacebookManger!!.hGetFbInterstitialAd()?.isAdLoaded == true
                ) {
                    return true
                } else {
                    hFacebookManger?.hLoadFbInterstitial { adsType, errorMessage ->
                        Timber.d("hFacebookManger failed with errorr message $errorMessage")
                        hCheckFallBackSequenceForInterstitial(
                            adsType,
                            activity
                        )
                    }
                }
            }
            H_MOP_UP -> {
                if (hMopUpManager?.hMopubInterstitial != null &&
                    hMopUpManager?.hMopubInterstitial?.isReady == true
                ) {
                    return true
                } else
                    hMopUpManager?.loadInterstitialAd(activity) { adsType, errorMessage ->
                        Timber.d("OnMop failed with errorr message $errorMessage")
                        hCheckFallBackSequenceForInterstitial(
                            adsType,
                            activity
                        )
                    }
            }
            else -> Unit
        }
        return false
    }

    private fun hCheckFallBackSequenceForInterstitial(adsType: AdsType, activity: Activity) {
        when (hInterstitialPriorityType) {
            H_AD_MOB -> hShowInterstitial(
                activity = activity,
                priority = AdMobFallbackStrategy.hInterstetialStrategy(adsType)
            )
            H_MOP_UP -> hShowInterstitial(
                activity = activity,
                priority = MopupFallbackStrategy.hInterstetialStrategy(adsType)
            )
            H_FACE_BOOK -> hShowInterstitial(
                activity = activity,
                priority = FbFallbackStrategy.hInterstetialStrategy(adsType)
            )
            else -> Unit
        }
    }

    fun hShowBanner(
        bannerAdContainer: ViewGroup,
        hPriorityType: AdPriorityType = hBannerPriorityType
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowBanner(
                bannerAdContainer
            ) { adsType, message, adContainerView: ViewGroup ->
                Timber.d("H_AD_MOB Banner failed error message $message")
                hCheckFallBackSequenceForBanner(
                    hAdsType = adsType,
                    hAdContainer = adContainerView,
                    hPriorityType = hPriorityType
                )
            }
            H_MOP_UP -> hMopUpManager?.hShowBanner(
                bannerAdContainer
            ) { adsType, message, adContainerView: ViewGroup ->
                Timber.d("H_MOP_UP Banner failed error message $message")
                hCheckFallBackSequenceForBanner(
                    hAdsType = adsType,
                    hAdContainer = adContainerView,
                    hPriorityType = hPriorityType
                )
            }
            H_FACE_BOOK -> hFacebookManger?.hShowBanner(
                bannerAdContainer
            ) { adsType, message, adContainerView: ViewGroup ->
                Timber.d("H_FACE_BOOK banner failed error message $message")
                hCheckFallBackSequenceForBanner(
                    hAdsType = adsType,
                    hAdContainer = adContainerView,
                    hPriorityType = hPriorityType
                )
            }
            else -> Unit
        }
    }


    fun hShowNativeBanner(
        hHnativeBannerView: HnativeBannerView,
        hPriorityType: AdPriorityType = hNativeBannerPriorityType,
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowNativeBanner(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
                Timber.d("hOnNativeBannerFailed Type ${adsType.name} and message $errorMessage")

                hCheckFallBackSequenceForNativeBanner(
                    hPriorityType = hPriorityType,
                    hAdsType = adsType,
                    hHnativeBannerView = hHnativeBannerView,
                )
            }
            H_MOP_UP -> hMopUpManager?.hShowNativeBanner(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
                Timber.d("hOnNativeBannerFailed Type ${adsType.name} and message $errorMessage")
                hCheckFallBackSequenceForNativeBanner(
                    hPriorityType = hPriorityType,
                    hAdsType = adsType,
                    hHnativeBannerView = hHnativeBannerView,
                )
            }
            H_FACE_BOOK -> hFacebookManger?.hShowNativeBanner(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
                Timber.d("hOnNativeBannerFailed Type ${adsType.name} and message $errorMessage")
                hCheckFallBackSequenceForNativeBanner(
                    hPriorityType = hPriorityType,
                    hAdsType = adsType,
                    hHnativeBannerView = hHnativeBannerView,
                )
            }
            else -> Unit
        }
    }

    private fun hCheckFallBackSequenceForNativeBanner(
        hAdsType: AdsType,
        hHnativeBannerView: HnativeBannerView,
        hPriorityType: AdPriorityType
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hShowNativeBanner(
                hHnativeBannerView = hHnativeBannerView,
                hPriorityType = AdMobFallbackStrategy.hNativeBannerStrategy(hAdsType),
            )
            H_MOP_UP -> hShowNativeBanner(
                hHnativeBannerView = hHnativeBannerView,
                hPriorityType = MopupFallbackStrategy.hNativeBannerStrategy(hAdsType),
            )
            H_FACE_BOOK -> hShowNativeBanner(
                hHnativeBannerView = hHnativeBannerView,
                hPriorityType = FbFallbackStrategy.hNativeBannerStrategy(hAdsType),
            )
            else -> Unit
        }
    }

    fun hShowNativeAdvanced(
        hnativeAdvancedView: HnativeAdvancedView,
        hPriorityType: AdPriorityType = hNativeAdvancedPriorityType
    ) {
        Timber.d("Pririotry is $hPriorityType")
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowNativeAdvanced(
                hnativeAdvancedView,
            ) { adsType, message, nativeAdvancedView ->
                Timber.d("hShowNativeAdvanced Type ${adsType.name} and message $message")
                hCheckFallBackSequenceForNativeAdvanced(
                    hPriorityType = hPriorityType,
                    hAdsType = adsType,
                    hNativeAdvancedView = nativeAdvancedView,
                )
            }
            H_MOP_UP -> hMopUpManager?.hShowNativeAdvanced(
                hnativeAdvancedView,
            ) { adsType, message, nativeAdvancedView ->
                Timber.d("hShowNativeAdvanced Type ${adsType.name} and message $message")
                hCheckFallBackSequenceForNativeAdvanced(
                    hAdsType = adsType,
                    hNativeAdvancedView = nativeAdvancedView,
                    hPriorityType = hPriorityType,
                )
            }
            H_FACE_BOOK -> hFacebookManger?.hShowNativeAdvanced(
                hnativeAdvancedView,
            ) { adsType, message, hAdContainer ->
                Timber.d("hShowNativeAdvanced Type ${adsType.name} and message $message")
                hCheckFallBackSequenceForNativeAdvanced(
                    hAdsType = adsType,
                    hNativeAdvancedView = hAdContainer,
                    hPriorityType = hPriorityType,
                )
            }
            else -> Unit
        }
    }

    private fun hCheckFallBackSequenceForNativeAdvanced(
        hAdsType: AdsType,
        hNativeAdvancedView: HnativeAdvancedView,
        hPriorityType: AdPriorityType,
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hShowNativeAdvanced(
                hnativeAdvancedView = hNativeAdvancedView,
                hPriorityType = AdMobFallbackStrategy.hNativeAdvancedtrategy(hAdsType)
            )
            H_MOP_UP -> hShowNativeAdvanced(
                hnativeAdvancedView = hNativeAdvancedView,
                hPriorityType = MopupFallbackStrategy.hNativeAdvancedtrategy(hAdsType)
            )
            H_FACE_BOOK -> hShowNativeAdvanced(
                hnativeAdvancedView = hNativeAdvancedView,
                hPriorityType = FbFallbackStrategy.hNativeAdvancedtrategy(hAdsType)
            )
            else -> Unit
        }
    }

    private fun hCheckFallBackSequenceForBanner(
        hAdsType: AdsType,
        hAdContainer: ViewGroup,
        hPriorityType: AdPriorityType
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hShowBanner(
                hAdContainer,
                AdMobFallbackStrategy.hBannerStrategy(hAdsType)
            )
            H_MOP_UP -> hShowBanner(
                hAdContainer,
                MopupFallbackStrategy.hBannerStrategy(hAdsType)
            )
            H_FACE_BOOK -> hShowBanner(
                hAdContainer,
                FbFallbackStrategy.hBannerStrategy(hAdsType)
            )
            else -> Unit
        }
    }


    fun hShowBannerWithOutFallback(
        bannerAdContainer: ViewGroup,
        hPriorityType: AdPriorityType = hBannerPriorityType
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowBanner(
                bannerAdContainer
            ) { adsType, message, adContainerView: ViewGroup ->
                Timber.d("hShowBannerWithOutFallback $message")
                adContainerView.visibility = View.GONE
            }
            H_MOP_UP -> hMopUpManager?.hShowBanner(
                bannerAdContainer
            ) { adsType, message, adContainerView: ViewGroup ->
                Timber.d("hShowBannerWithOutFallback $message")
                adContainerView.visibility = View.GONE
            }
            H_FACE_BOOK -> hFacebookManger?.hShowBanner(
                bannerAdContainer
            ) { adsType, message, adContainerView: ViewGroup ->
                Timber.d("hShowBannerWithOutFallback $message")
                adContainerView.visibility = View.GONE
            }
            else -> Unit
        }
    }


    fun hShowNativeBannerWithOutFallback(
        hHnativeBannerView: HnativeBannerView,
        hPriorityType: AdPriorityType = hNativeBannerPriorityType,
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowNativeBanner(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
                Timber.d("hShowNativeBannerWithOutFallback $errorMessage")
            }
            H_MOP_UP -> hMopUpManager?.hShowNativeBanner(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
                Timber.d("hShowNativeBannerWithOutFallback $errorMessage")
            }
            H_FACE_BOOK -> hFacebookManger?.hShowNativeBanner(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
                Timber.d("hShowNativeBannerWithOutFallback $errorMessage")

            }
            else -> Unit
        }
    }


    fun hShowNativeAdvancedWithOutFallback(
        hHnativeBannerView: HnativeAdvancedView,
        hPriorityType: AdPriorityType = hNativeBannerPriorityType,
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowNativeAdvanced(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
                Timber.d("hShowNativeAdvancedWithOutFallback $errorMessage")
            }
            H_MOP_UP -> hMopUpManager?.hShowNativeAdvanced(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
                Timber.d("hShowNativeAdvancedWithOutFallback $errorMessage")
            }
            H_FACE_BOOK -> hFacebookManger?.hShowNativeAdvanced(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
                Timber.d("hShowNativeAdvancedWithOutFallback $errorMessage")
            }
            else -> Unit
        }
    }


    /*For Manually chaning the priorities*/
    fun hSetNativeBannerPriority(
        nativeBannerPriorityType: AdPriorityType = H_AD_MOB,
    ) {
        hNativeBannerPriorityType = nativeBannerPriorityType
    }

    fun hSetNativeAdvancedPriority(
        nativeAdvancedPriorityType: AdPriorityType = H_AD_MOB,
    ) {
        hNativeAdvancedPriorityType = nativeAdvancedPriorityType
        Timber.d("Setting Native advanced pririoty $hNativeAdvancedPriorityType")
    }

    fun hSetInterstitialPriority(
        interstitialPriorityType: AdPriorityType = H_AD_MOB,
    ) {
        hInterstitialPriorityType = interstitialPriorityType
    }

    fun hSetBannerPriority(
        bannerPriorityType: AdPriorityType = H_MOP_UP,
    ) {
        hBannerPriorityType = bannerPriorityType
    }
}