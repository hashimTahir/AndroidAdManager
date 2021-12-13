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
import com.hashim.hadmanager.R
import com.hashim.hadmanager.adsmodule.customadview.HnativeAdvancedView
import com.hashim.hadmanager.adsmodule.customadview.HnativeBannerView
import com.hashim.hadmanager.adsmodule.types.AdsType
import com.hashim.hadmanager.adsmodule.types.WhatAd
import com.hashim.hadmanager.databinding.AdmobNativeAdvancedLayoutBinding
import com.hashim.hadmanager.databinding.AdmobNativeBannerLayoutBinding
import com.hashim.hadmanager.templates.NativeTemplateStyle
import timber.log.Timber
import java.util.*


class AdMobManager(
    private val hContext: Context?,
    private val hIdsMap: HashMap<WhatAd, String>?,
) {
    var hInterstitialAd: InterstitialAd? = null
        private set
    private var hBannerAdView: AdView? = null


    fun hLoadInterstitialAd(
        hOnAdMobInterStitialFailed: (
            adsType: AdsType,
            errorMessage: String
        ) -> Unit
    ) {
        hIdsMap?.get(WhatAd.H_INTER)?.let { interId ->
            val adRequest = AdRequest.Builder().build()

            InterstitialAd.load(
                hContext!!,
                interId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        this@AdMobManager.hInterstitialAd = interstitialAd
                        interstitialAd.fullScreenContentCallback = hFullScreenContentCallback
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        hOnAdMobInterStitialFailed(
                            AdsType.H_ADMOB,
                            loadAdError.message
                        )
                    }
                })
        }
    }

    var hFullScreenContentCallback: FullScreenContentCallback =
        object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                hLoadInterstitialAd { adsType, errorMessage ->

                }
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            }

            override fun onAdShowedFullScreenContent() {

                hInterstitialAd = null
            }
        }


    fun hShowNativeBanner(
        hNativeBannerView: HnativeBannerView,
        hOnAdMobNativeBannerFailed: (
            adsType: AdsType,
            errorMessage: String,
            hAdContainer: HnativeBannerView,
        ) -> Unit
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
                        Timber.d("On AdMob Failed.")
                        hNativeBannerView.hShowHideAdLoader(true)
                        hOnAdMobNativeBannerFailed(
                            AdsType.H_ADMOB,
                            loadAdError.message,
                            hNativeBannerView
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
        hOnAdMobBannerFailed: (
            adsType: AdsType,
            message: String,
            adContainerView: ViewGroup
        ) -> Unit,
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
                            if (hBannerAdView!!.parent != null) {
                                (hBannerAdView!!.parent as ViewGroup).removeView(hBannerAdView)
                            }
                            hAdViewGroup.addView(hBannerAdView)
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            super.onAdFailedToLoad(loadAdError)
                            hOnAdMobBannerFailed(
                                AdsType.H_ADMOB,
                                loadAdError.message,
                                hAdViewGroup
                            )
                        }

                        override fun onAdClosed() {}
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
        valueTV.setText(R.string.ad_loading)
        valueTV.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        valueTV.gravity = Gravity.CENTER
        adContainerView!!.addView(valueTV)
    }

    fun hShowNativeAdvanced(
        hNativeAdvancedView: HnativeAdvancedView,
        hOnAdMobNativeAdvancedFailded: (
            adsType: AdsType,
            message: String,
            nativeAdvancedView: HnativeAdvancedView,
        ) -> Unit,
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
                        hOnAdMobNativeAdvancedFailded(
                            AdsType.H_ADMOB,
                            loadAdError.message,
                            hNativeAdvancedView,
                        )
                    }
                })
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }

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
                Timber.d("Ad Mob Initiliztion status $initializationStatus")
            }
        }
    }

}