package com.hashim.hadmanager.adsmodule

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.hashim.hadmanager.adsmodule.callbacks.InterCallbacks
import com.hashim.hadmanager.adsmodule.callbacks.NativeCallBacks
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

object AdManager : InterCallbacks, NativeCallBacks {

    private var hMopUpManager: MopUpManager? = null
    private var hFacebookManger: FaceBookAdManager? = null
    private var hAdMobManager: AdMobManager? = null


    private var hBannerPriorityType: AdPriorityType = H_AD_MOB
    private var hNativeBannerPriorityType: AdPriorityType = H_AD_MOB
    private var hNativeAdvancedPriorityType: AdPriorityType = H_AD_MOB
    private var hInterstitialPriorityType: AdPriorityType = H_AD_MOB

    private var hInterCallbacks: InterCallbacks? = null
    private var hNativeCallBacks: NativeCallBacks? = null

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
                        it.hSetNativeCallBacks(this)
                    }
                }
                H_MOPUP -> {
                    hMopUpManager = MopUpManager(
                        hContext,
                        hIdsMap[adsType]
                    ).also {
                        it.hSetInterCallbacks(this)
                        it.hSetNativeCallBacks(this)
                    }

                }
                H_ADMOB -> {
                    hAdMobManager = AdMobManager(
                        hContext,
                        hIdsMap[adsType],
                    ).also {
                        it.hSetInterCallbacks(this)
                        it.hSetNativeCallBacks(this)
                    }
                }
            }
        }
    }

    fun hSetInterCallbacks(interCallbacks: InterCallbacks) {
        hInterCallbacks = interCallbacks
    }

    fun hSetNativeCallBacks(nativeCallBacks: NativeCallBacks) {
        hNativeCallBacks = nativeCallBacks
    }


    fun hLoadInterstitial(
        hActivity: Activity,
        hPriorityType: AdPriorityType = hInterstitialPriorityType
    ) {
        Timber.d("Priritory is $hPriorityType")
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
            H_AD_MOB -> hAdMobManager?.hShowBanner(
                bannerAdContainer
            ) { adsType, message, adContainerView: ViewGroup ->
//                Timber.d("H_AD_MOB Banner failed error message $message")
                hCheckFallBackSequenceForBanner(
                    hAdsType = adsType,
                    hAdContainer = adContainerView,
                    hPriorityType = hPriorityType
                )
            }
            H_MOP_UP -> hMopUpManager?.hShowBanner(
                bannerAdContainer
            ) { adsType, message, adContainerView: ViewGroup ->
//                Timber.d("H_MOP_UP Banner failed error message $message")
                hCheckFallBackSequenceForBanner(
                    hAdsType = adsType,
                    hAdContainer = adContainerView,
                    hPriorityType = hPriorityType
                )
            }
            H_FACE_BOOK -> hFacebookManger?.hShowBanner(
                bannerAdContainer
            ) { adsType, message, adContainerView: ViewGroup ->
//                Timber.d("H_FACE_BOOK banner failed error message $message")
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
        Timber.d("Global pri passed $hNativeBannerPriorityType")
        Timber.d("Local pri passed $hPriorityType")
        when (hPriorityType) {
            H_AD_MOB -> hAdMobManager?.hShowNativeBanner(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
//                Timber.d("hOnNativeBannerFailed Type ${adsType.name} and message $errorMessage")

                hCheckFallBackSequenceForNativeBanner(
                    hPriorityType = hPriorityType,
                    hAdsType = adsType,
                    hHnativeBannerView = hHnativeBannerView,
                )
            }
            H_MOP_UP -> hMopUpManager?.hShowNativeBanner(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
//                Timber.d("hOnNativeBannerFailed Type ${adsType.name} and message $errorMessage")
                hCheckFallBackSequenceForNativeBanner(
                    hPriorityType = hPriorityType,
                    hAdsType = adsType,
                    hHnativeBannerView = hHnativeBannerView,
                )
            }
            H_FACE_BOOK -> hFacebookManger?.hShowNativeBanner(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
//                Timber.d("hOnNativeBannerFailed Type ${adsType.name} and message $errorMessage")
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
            H_AD_MOB -> {
                Timber.d("This Case is executeing 1")
                hShowNativeBanner(
                    hHnativeBannerView = hHnativeBannerView,
                    hPriorityType = AdMobFallbackStrategy.hNativeBannerStrategy(
                        hGlobalPriority = hNativeBannerPriorityType,
                        hAdsType = hAdsType
                    ),
                )
            }
            H_MOP_UP -> {
                Timber.d("This Case is executeing 2")
                hShowNativeBanner(
                    hHnativeBannerView = hHnativeBannerView,
                    hPriorityType = MopupFallbackStrategy.hNativeBannerStrategy(
                        hGlobalPriority = hNativeBannerPriorityType,
                        hAdsType = hAdsType
                    ),
                )
            }
            H_FACE_BOOK -> {
                Timber.d("This Case is executeing 3")
                hShowNativeBanner(
                    hHnativeBannerView = hHnativeBannerView,
                    hPriorityType = FbFallbackStrategy.hNativeBannerStrategy(
                        hGlobalPriority = hNativeBannerPriorityType,
                        hAdsType = hAdsType
                    ),
                )
            }
            else -> {
                Unit
            }
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
            else -> Unit
        }
    }

    private fun hGetFallBackPriorityForNativeAdvacnced(
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

    private fun hCheckFallBackSequenceForBanner(
        hAdsType: AdsType,
        hAdContainer: ViewGroup,
        hPriorityType: AdPriorityType
    ) {
        when (hPriorityType) {
            H_AD_MOB -> hShowBanner(
                hAdContainer,
                AdMobFallbackStrategy.hBannerStrategy(
                    hGlobalPriority = hBannerPriorityType,
                    hAdsType = hAdsType
                )
            )
            H_MOP_UP -> hShowBanner(
                hAdContainer,
                MopupFallbackStrategy.hBannerStrategy(
                    hGlobalPriority = hBannerPriorityType,
                    hAdsType = hAdsType
                )
            )
            H_FACE_BOOK -> hShowBanner(
                hAdContainer,
                FbFallbackStrategy.hBannerStrategy(
                    hGlobalPriority = hBannerPriorityType,
                    hAdsType = hAdsType
                )
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
//                Timber.d("hShowBannerWithOutFallback $message")
                adContainerView.visibility = View.GONE
            }
            H_MOP_UP -> hMopUpManager?.hShowBanner(
                bannerAdContainer
            ) { adsType, message, adContainerView: ViewGroup ->
//                Timber.d("hShowBannerWithOutFallback $message")
                adContainerView.visibility = View.GONE
            }
            H_FACE_BOOK -> hFacebookManger?.hShowBanner(
                bannerAdContainer
            ) { adsType, message, adContainerView: ViewGroup ->
//                Timber.d("hShowBannerWithOutFallback $message")
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
//                Timber.d("hShowNativeBannerWithOutFallback $errorMessage")
            }
            H_MOP_UP -> hMopUpManager?.hShowNativeBanner(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
//                Timber.d("hShowNativeBannerWithOutFallback $errorMessage")
            }
            H_FACE_BOOK -> hFacebookManger?.hShowNativeBanner(
                hHnativeBannerView
            ) { adsType, errorMessage, hAdContainer ->
//                Timber.d("hShowNativeBannerWithOutFallback $errorMessage")

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
            )
            H_MOP_UP -> hMopUpManager?.hShowNativeAdvanced(
                hHnativeBannerView
            )
            H_FACE_BOOK -> hFacebookManger?.hShowNativeAdvanced(
                hHnativeBannerView
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
//        Timber.d("Setting Native advanced pririoty $hNativeAdvancedPriorityType")
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
    /*-End---------------InterCallBacks ------------------------*/


    /*---------------------NativeCallbacks ------------------------*/
    override fun hOnNativeAdvancedFailed(hAdType: AdsType, hError: Error, hNativeAdvanceView: HnativeAdvancedView) {
        hNativeCallBacks?.hOnNativeAdvancedFailed(
            hAdType = hAdType,
            hError = hError,
            hNativeAdvanceView = hNativeAdvanceView
        )
        hShowNativeAdvanced(
            hnativeAdvancedView = hNativeAdvanceView,
            hPriorityType = hGetFallBackPriorityForNativeAdvacnced(
                hAdsType = hAdType,
            )
        )
    }

    override fun hOnAdClosed(hAdType: AdsType) {
        hNativeCallBacks?.hOnAdClosed(hAdType = hAdType)
    }

    override fun hOnAdOpened(hAdType: AdsType) {
        hNativeCallBacks?.hOnAdOpened(hAdType = hAdType)
    }

    override fun hOnAdLoaded(hAdType: AdsType) {
        hNativeCallBacks?.hOnAdLoaded(hAdType = hAdType)
    }

    override fun hOnAdClicked(hAdType: AdsType) {
        hNativeCallBacks?.hOnAdClicked(hAdType = hAdType)

    }

    override fun hOnAdImpression(hAdType: AdsType) {
        hNativeCallBacks?.hOnAdImpression(hAdType = hAdType)

    }

    /*-End---------------InterCallbacks ------------------------*/

}
