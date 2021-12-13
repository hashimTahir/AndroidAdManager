package com.hashim.hadmanager.adsmodule

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import com.facebook.ads.*
import com.hashim.hadmanager.R
import com.hashim.hadmanager.adsmodule.customadview.HnativeAdvancedView
import com.hashim.hadmanager.adsmodule.customadview.HnativeBannerView
import com.hashim.hadmanager.adsmodule.types.AdsType
import com.hashim.hadmanager.adsmodule.types.WhatAd
import com.hashim.hadmanager.databinding.FbNativeAdvancedLayoutBinding
import com.hashim.hadmanager.databinding.FbNativeBannerLayoutBinding
import timber.log.Timber
import java.util.*

class FaceBookAdManager(
    private val myApplication: Context,
    private val hIdsMap: HashMap<WhatAd, String>?
) {

    private var hNativeBanner: NativeBannerAd? = null
    private var hFbInterstitailAd: InterstitialAd? = null
    private var hFbBanner: AdView? = null
    private var nativeAd: NativeAd? = null


    fun hGetFbInterstitialAd(): InterstitialAd? {
        return hFbInterstitailAd
    }

    fun hLoadFbInterstitial(
        hOnFaceBookInterStitialFailed: (
            adsType: AdsType,
            errorMessage: String,
        ) -> Unit,
    ) {
        hIdsMap?.get(WhatAd.H_INTER)?.let { interId ->
            if (hFbInterstitailAd != null) {
                hFbInterstitailAd?.destroy()
                hFbInterstitailAd = null
            }
            object : InterstitialAdExtendedListener {
                override fun onInterstitialActivityDestroyed() {}

                override fun onInterstitialDisplayed(ad: Ad?) {}

                override fun onInterstitialDismissed(ad: Ad?) {
                    hLoadFbInterstitial { adsTyep, errorMessage ->
                    }
                }

                override fun onError(ad: Ad?, adError: AdError?) {
                    adError?.errorMessage?.let {
                        hOnFaceBookInterStitialFailed(
                            AdsType.H_FACEBOOK,
                            it
                        )
                    }
                }

                override fun onAdLoaded(ad: Ad?) {}
                override fun onAdClicked(ad: Ad?) {}
                override fun onLoggingImpression(ad: Ad?) {}
                override fun onRewardedAdCompleted() {}
                override fun onRewardedAdServerSucceeded() {}
                override fun onRewardedAdServerFailed() {}
            }.also { listener ->
                hFbInterstitailAd = InterstitialAd(
                    myApplication,
                    interId
                )
                val loadAdConfig = hFbInterstitailAd!!
                    .buildLoadAdConfig()
                    .withAdListener(listener)
                    .build()
                hFbInterstitailAd!!.loadAd(loadAdConfig)
            }

        }

    }

    private fun hGetBannerHeightInPixel(): Int {
        val display =
            (myApplication?.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val scale = outMetrics.density
        return (50 * scale + 0.5f).toInt()
    }

    fun hShowBanner(
        adContainerView: ViewGroup,
        hOnFaceBookBannerFailed: (
            adsType: AdsType,
            message: String,
            adContainerView: ViewGroup,
        ) -> Unit,
    ) {
        hIdsMap?.get(WhatAd.H_BANNER)?.let { bannerId ->
            adContainerView.layoutParams.height = hGetBannerHeightInPixel()
            hAddPlaceHolderTextView(adContainerView)
            object : AdListener {
                override fun onError(ad: Ad?, adError: AdError?) {
                    Timber.e(adError!!.errorMessage)
                    hOnFaceBookBannerFailed(
                        AdsType.H_FACEBOOK,
                        adError.errorMessage,
                        adContainerView
                    )
                }

                override fun onAdLoaded(ad: Ad?) {
                    adContainerView.visibility = View.VISIBLE
                    if (hFbBanner?.parent != null) {
                        (hFbBanner?.parent as ViewGroup).removeView(hFbBanner) // <- fix
                    }
                    adContainerView.addView(hFbBanner)
                }

                override fun onAdClicked(ad: Ad?) {}
                override fun onLoggingImpression(ad: Ad?) {}
            }.also { listener ->
                hFbBanner = AdView(
                    myApplication,
                    bannerId,
                    AdSize.BANNER_HEIGHT_50
                )
                hFbBanner?.loadAd(
                    hFbBanner?.buildLoadAdConfig()
                        ?.withAdListener(listener)
                        ?.build()
                )
            }

        }
    }

    private fun hAddPlaceHolderTextView(adContainerView: ViewGroup?) {
        TextView(myApplication).apply {
            setText(R.string.ad_loading)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            adContainerView?.addView(this)
        }
    }


    private fun hInflateFbNativeAdvanced(
        hNativeAd: NativeAd?,
        context: Context
    ): NativeAdLayout {
        hNativeAd?.unregisterView()

        FbNativeAdvancedLayoutBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        ).apply {
            val adOptionsView = AdOptionsView(
                myApplication,
                hNativeAd,
                this.root
            )

            nativeAdTitle.text = hNativeAd?.advertiserName
            nativeAdBody.text = hNativeAd?.adBodyText
            nativeAdSocialContext.text = hNativeAd?.adSocialContext
            nativeAdCallToAction.visibility =
                if (hNativeAd?.hasCallToAction() == true) View.VISIBLE
                else View.INVISIBLE

            nativeAdCallToAction.text = hNativeAd?.adCallToAction
            nativeAdSponsoredLabel.text = hNativeAd?.sponsoredTranslation

            val clickableViews: MutableList<View> = ArrayList()
            clickableViews.add(nativeAdTitle)
            clickableViews.add(nativeAdCallToAction)

            adChoicesContainer.apply {
                removeAllViews()
                addView(
                    adOptionsView,
                    0
                )
            }
            hNativeAd?.registerViewForInteraction(
                this.root,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews
            )
            return this.root
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    fun hShowNativeAdvanced(
        hnativeAdvancedView: HnativeAdvancedView,
        hOnFbNativeAdvancedFailded: (
            adsType: AdsType,
            message: String,
            adContainerView: HnativeAdvancedView,
        ) -> Unit,
    ) {
        hIdsMap?.get(WhatAd.H_NATIVE_ADVANCED)?.let { nativeAdvancedId ->
            val nativeAdListener: NativeAdListener = object : NativeAdListener {
                override fun onMediaDownloaded(ad: Ad?) {}
                override fun onError(ad: Ad?, adError: AdError?) {
                    hnativeAdvancedView.hShowHideAdLoader(true)

                    hOnFbNativeAdvancedFailded(
                        AdsType.H_FACEBOOK,
                        adError?.errorMessage.toString(),
                        hnativeAdvancedView,
                    )
                }

                override fun onAdLoaded(ad: Ad?) {
                    if (nativeAd != null) {
                        hInflateFbNativeAdvanced(
                            nativeAd,
                            hnativeAdvancedView.context
                        ).let { nativeAdLayout ->
                            hnativeAdvancedView.hShowAdView(viewGroup = nativeAdLayout)
                        }
                    }
                }

                override fun onAdClicked(ad: Ad?) {}
                override fun onLoggingImpression(ad: Ad?) {}
            }
            nativeAd = NativeAd(
                myApplication,
                nativeAdvancedId,
            )
            nativeAd!!.loadAd(
                nativeAd!!.buildLoadAdConfig().withMediaCacheFlag(NativeAdBase.MediaCacheFlag.ALL)
                    .withAdListener(nativeAdListener).build()
            )
        }
    }


    fun hShowNativeBanner(
        hNativeBannerView: HnativeBannerView,
        hOnFbNativeBannerFailed: (
            adsType: AdsType,
            errorMessage: String?,
            hAdContainer: HnativeBannerView?,
        ) -> Unit,
    ) {
        hIdsMap?.get(WhatAd.H_NATIVE_BANNER)?.let { nativeBannerId ->

            val nativeAdListener: NativeAdListener = object : NativeAdListener {
                override fun onMediaDownloaded(ad: Ad?) {}
                override fun onError(ad: Ad?, adError: AdError?) {
                    hNativeBannerView.hShowHideAdLoader(true)
                    hOnFbNativeBannerFailed(
                        AdsType.H_FACEBOOK,
                        adError?.errorMessage,
                        hNativeBannerView
                    )
                }

                override fun onAdLoaded(ad: Ad?) {
                    if (hNativeBanner == null || hNativeBanner !== ad) {
                        return
                    }
                    hInflateNativeBanner(
                        hNativeBannerView.context,
                        hNativeBanner
                    ).let { nativeAdLayout ->
                        hNativeBannerView.hShowAdView(viewGroup = nativeAdLayout)

                    }
                }

                override fun onAdClicked(ad: Ad?) {}
                override fun onLoggingImpression(ad: Ad?) {}
            }
            hNativeBanner = NativeBannerAd(
                myApplication,
                nativeBannerId
            )
            hNativeBanner!!.loadAd(
                hNativeBanner!!.buildLoadAdConfig().withAdListener(nativeAdListener).build()
            )
        }
    }

    private fun hInflateNativeBanner(
        context: Context,
        nativeBannerAd: NativeBannerAd?,
    ): NativeAdLayout {
        nativeBannerAd?.unregisterView()

        FbNativeBannerLayoutBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        ).apply {
            val adOptionsView = AdOptionsView(
                myApplication,
                nativeBannerAd,
                nativeAdLayout
            )
            nativeAdCallToAction.text = nativeBannerAd?.adCallToAction
            nativeAdCallToAction.visibility =
                if (nativeBannerAd!!.hasCallToAction()) View.VISIBLE
                else View.INVISIBLE
            nativeAdTitle.text = nativeBannerAd.advertiserName
            nativeAdSocialContext.text = nativeBannerAd.adSocialContext
            nativeAdSponsoredLabel.text = nativeBannerAd.sponsoredTranslation

            val clickableViews: MutableList<View?> = ArrayList()
            clickableViews.add(nativeAdTitle)
            clickableViews.add(nativeAdCallToAction)

            adChoicesContainer.removeAllViews()
            adChoicesContainer.addView(adOptionsView, 0)

            nativeBannerAd.registerViewForInteraction(
                nativeAdLayout,
                nativeIconView,
                clickableViews
            )
            return this.root
        }

    }

}