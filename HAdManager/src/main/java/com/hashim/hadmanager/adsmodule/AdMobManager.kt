package com.hashim.hadmanager.adsmodule

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.hashim.hadmanager.adsmodule.callbacks.AdCallbacks
import com.hashim.hadmanager.adsmodule.callbacks.InterCallbacks
import com.hashim.hadmanager.adsmodule.customadview.HnativeAdvancedView
import com.hashim.hadmanager.adsmodule.customadview.HnativeBannerView
import com.hashim.hadmanager.adsmodule.types.AdsType
import com.hashim.hadmanager.adsmodule.types.WhatAd
import com.hashim.hadmanager.databinding.AdmobNativeAdvancedLayoutBinding
import com.hashim.hadmanager.databinding.AdmobNativeBannerLayoutBinding
import com.hashim.hadmanager.templates.NativeTemplateStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class AdMobManager(
    private val hContext: Context?,
    private val hIdsMap: HashMap<WhatAd, String>?,
) {
    var hInterstitialAd: InterstitialAd? = null
        private set
    private var hBannerAdView: AdView? = null
    private var hInterCallbacks: InterCallbacks? = null
    private var hAdCallbacks: AdCallbacks? = null


    fun hLoadInterstitialAd(
        hTimeOut: Long = Constants.h3SecTimeOut
    ) {
        hIdsMap?.get(WhatAd.H_INTER)?.let { interId ->
            val adRequest = AdRequest.Builder().build()

            CoroutineScope(Dispatchers.Main).launch {
                var hCallBackCalled = false
                InterstitialAd.load(
                    hContext!!,
                    interId,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {

                            this@AdMobManager.hInterstitialAd = interstitialAd

                            interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    hInterCallbacks?.hOnAddDismissed(AdsType.H_ADMOB)
                                    hCallBackCalled = true
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {

                                    hInterCallbacks?.hOnAdFailedToShowFullContent(
                                        hAdType = AdsType.H_ADMOB,
                                        hError = Error(
                                            hMessage = adError.message,
                                            hCode = adError.code,
                                            hDomain = adError.domain,
                                        )
                                    )
                                    hCallBackCalled = true
                                }

                                override fun onAdShowedFullScreenContent() {
                                    hInterCallbacks?.hOnAddShowed(AdsType.H_ADMOB)
                                    hCallBackCalled = true
                                    hInterstitialAd = null
                                }
                            }
                            hInterCallbacks?.hOnAddLoaded(hAdType = AdsType.H_ADMOB)
                            hCallBackCalled = true
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            hInterCallbacks?.hOnAdFailedToLoad(
                                hAdType = AdsType.H_ADMOB,
                                hError = Error(
                                    hMessage = loadAdError.message,
                                    hCode = loadAdError.code,
                                    hDomain = loadAdError.domain,
                                )
                            )
                            hCallBackCalled = true
                        }
                    }
                )

                delay(hTimeOut)

                if (hCallBackCalled.not()) {
                    hInterCallbacks?.hOnAdTimedOut(AdsType.H_ADMOB)
                }
            }
        }
    }


    fun hShowNativeBanner(
        hNativeBannerView: HnativeBannerView,
        hIsWithFallback: Boolean = true
    ) {

        hIdsMap?.get(WhatAd.H_NATIVE_BANNER)?.let { nativeAdvancedId ->
            val adLoader = AdLoader.Builder(
                hContext,
                nativeAdvancedId
            )
                .forNativeAd { NativeAd: NativeAd? ->
                    val cd = ColorDrawable()
                    val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build()
                    AdmobNativeBannerLayoutBinding.inflate(
                        LayoutInflater.from(hContext),
                        null,
                        false
                    ).apply {
                        myTemplate.visibility = View.VISIBLE
                        myTemplate.setStyles(styles)
                        NativeAd?.let {
                            myTemplate.setNativeAd(it)
                        }
                        hNativeBannerView.hShowAdView(viewGroup = root)
                    }


                }.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        hNativeBannerView.hShowHideAdLoader(true)
                        hAdCallbacks?.hAdFailedToLoad(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_BANNER,
                            hError = Error(
                                hMessage = loadAdError.message,
                                hCode = loadAdError.code,
                                hDomain = loadAdError.domain,
                            ),
                            hNativeView = hNativeBannerView,
                            hIsWithFallback = hIsWithFallback

                        )
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        hAdCallbacks?.hAdClosed(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_BANNER
                        )
                    }

                    override fun onAdOpened() {
                        super.onAdOpened()
                        hAdCallbacks?.hNativeAdOpened(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_BANNER
                        )
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        hAdCallbacks?.hAdLoaded(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_BANNER
                        )
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        hAdCallbacks?.hAdClicked(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_BANNER
                        )
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        hAdCallbacks?.hAdImpression(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_BANNER
                        )
                    }
                })
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }

    private val hAdSize: AdSize
        get() {
            val display = (hContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val widthPixels = outMetrics.widthPixels.toFloat()
            val density = outMetrics.density
            val adWidth = (widthPixels / density).toInt()

            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(hContext, adWidth)
        }

    fun hShowBanner(
        hAdViewGroup: ViewGroup,
        hIsWithFallback: Boolean = true
    ) {
        try {

            hIdsMap?.get(WhatAd.H_BANNER)?.let { bannerId ->
                if (hContext != null) {
                    hBannerAdView = AdView(hContext)
                    hBannerAdView!!.adUnitId = bannerId
                    val adSize = hAdSize
                    hAdViewGroup.layoutParams.height =
                        hGetPixelFromDp(hContext, 60)
                    hAddPlaceHolderTextView(hAdViewGroup)
                    hBannerAdView?.adSize = adSize
                    hBannerAdView?.adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            hAdCallbacks?.hAdLoaded(
                                hAdType = AdsType.H_ADMOB,
                                hWhatAd = WhatAd.H_BANNER
                            )
                            if (hBannerAdView!!.parent != null) {
                                (hBannerAdView!!.parent as ViewGroup).removeView(hBannerAdView)
                            }
                            hAdViewGroup.addView(hBannerAdView)
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            super.onAdFailedToLoad(loadAdError)
                            hAdCallbacks?.hAdFailedToLoad(
                                hAdType = AdsType.H_ADMOB,
                                hError = Error(
                                    hMessage = loadAdError.message,
                                    hCode = loadAdError.code,
                                    hDomain = loadAdError.domain,
                                ),
                                hNativeView = hAdViewGroup,
                                hWhatAd = WhatAd.H_BANNER,
                                hIsWithFallback = hIsWithFallback,
                            )
                        }

                        override fun onAdClosed() {
                            hAdCallbacks?.hAdClosed(
                                hAdType = AdsType.H_ADMOB,
                                hWhatAd = WhatAd.H_BANNER
                            )
                        }

                        override fun onAdOpened() {
                            hAdCallbacks?.hNativeAdOpened(
                                hAdType = AdsType.H_ADMOB,
                                hWhatAd = WhatAd.H_BANNER
                            )
                        }

                        override fun onAdClicked() {
                            hAdCallbacks?.hAdClicked(
                                hAdType = AdsType.H_ADMOB,
                                hWhatAd = WhatAd.H_BANNER
                            )
                        }

                        override fun onAdImpression() {
                            hAdCallbacks?.hAdImpression(
                                hAdType = AdsType.H_ADMOB,
                                hWhatAd = WhatAd.H_BANNER
                            )
                        }
                    }
                    val adRequest = AdRequest.Builder().build()
                    hBannerAdView?.loadAd(adRequest)
                }
            }

        } catch (e: NotFoundException) {
            e.printStackTrace()
        }
    }

    private fun hAddPlaceHolderTextView(adContainerView: ViewGroup?) {
        val valueTV = TextView(hContext)
        valueTV.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        valueTV.gravity = Gravity.CENTER
        adContainerView!!.addView(valueTV)
    }

    fun hShowNativeAdvanced(
        hNativeAdvancedView: HnativeAdvancedView,
        hIsWithFallback: Boolean = true
    ) {
        hIdsMap?.get(WhatAd.H_NATIVE_ADVANCED)?.let { nativeAdvancedId ->
            val adLoader = AdLoader.Builder(
                hContext!!,
                nativeAdvancedId
            )
                .forNativeAd { unifiedNativeAd: NativeAd? ->
                    val cd = ColorDrawable()
                    val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build()
                    AdmobNativeAdvancedLayoutBinding.inflate(
                        LayoutInflater.from(hContext),
                        null,
                        false
                    ).apply {
                        myTemplate.visibility = View.VISIBLE
                        myTemplate.setStyles(styles)
                        unifiedNativeAd?.let {
                            myTemplate.setNativeAd(it)
                        }
                        hNativeAdvancedView.hShowAdView(viewGroup = root)
                    }

                }.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        hNativeAdvancedView.hShowHideAdLoader(true)
                        hAdCallbacks?.hAdFailedToLoad(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_ADVANCED,
                            hError = Error(
                                hMessage = loadAdError.message,
                                hCode = loadAdError.code,
                                hDomain = loadAdError.domain,
                            ),
                            hNativeView = hNativeAdvancedView,
                            hIsWithFallback = hIsWithFallback
                        )
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        hAdCallbacks?.hAdClosed(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_BANNER
                        )
                    }

                    override fun onAdOpened() {
                        super.onAdOpened()
                        hAdCallbacks?.hNativeAdOpened(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_BANNER
                        )
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        hAdCallbacks?.hAdLoaded(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_BANNER
                        )
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        hAdCallbacks?.hAdClicked(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_BANNER
                        )
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        hAdCallbacks?.hAdImpression(
                            hAdType = AdsType.H_ADMOB,
                            hWhatAd = WhatAd.H_NATIVE_BANNER
                        )
                    }
                })
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }

    }

    fun hSetInterCallbacks(interCallbacks: InterCallbacks) {
        hInterCallbacks = interCallbacks
    }

    fun hSetNativeCallbacks(adCallbacks: AdCallbacks) {
        hAdCallbacks = adCallbacks
    }

    companion object {
        fun hGetPixelFromDp(application: Context?, dp: Int): Int {
            val display =
                (application!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val scale = outMetrics.density
            return (dp * scale + 0.5f).toInt()
        }
    }


    init {
        hContext?.let { context ->
            MobileAds.initialize(context) { initializationStatus ->
                Timber.d("Ad Mob Initiliztion status ${initializationStatus.adapterStatusMap}")
            }
        }
    }

}