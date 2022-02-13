package com.hashim.hadmanager.adsmodule

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import com.facebook.ads.*
import com.hashim.hadmanager.R
import com.hashim.hadmanager.adsmodule.callbacks.InterCallbacks
import com.hashim.hadmanager.adsmodule.callbacks.AdCallbacks
import com.hashim.hadmanager.adsmodule.customadview.HnativeAdvancedView
import com.hashim.hadmanager.adsmodule.customadview.HnativeBannerView
import com.hashim.hadmanager.adsmodule.types.AdsType
import com.hashim.hadmanager.adsmodule.types.WhatAd
import com.hashim.hadmanager.databinding.FbNativeAdvancedLayoutBinding
import com.hashim.hadmanager.databinding.FbNativeBannerLayoutBinding

class FaceBookAdManager(
    private val myApplication: Context,
    private val hIdsMap: HashMap<WhatAd, String>?
) {

    private var hNativeBanner: NativeBannerAd? = null
    private var hFbInterstitailAd: InterstitialAd? = null
    private var hFbBanner: AdView? = null
    private var nativeAd: NativeAd? = null
    private var hInterCallbacks: InterCallbacks? = null
    private var hAdCallbacks: AdCallbacks? = null


    fun hGetFbInterstitialAd(): InterstitialAd? {
        return hFbInterstitailAd
    }

    fun hLoadFbInterstitial() {
        hIdsMap?.get(WhatAd.H_INTER)?.let { interId ->
            if (hFbInterstitailAd != null) {
                hFbInterstitailAd?.destroy()
                hFbInterstitailAd = null
            }
            object : InterstitialAdExtendedListener {
                override fun onInterstitialActivityDestroyed() {}

                override fun onInterstitialDisplayed(ad: Ad?) {
                    hInterCallbacks?.hOnAddShowed(AdsType.H_FACEBOOK)
                }

                override fun onInterstitialDismissed(ad: Ad?) {
                    hInterCallbacks?.hOnAddDismissed(AdsType.H_FACEBOOK)
                }

                override fun onError(ad: Ad?, adError: AdError?) {
                    hInterCallbacks?.hOnAdFailedToLoad(
                        AdsType.H_FACEBOOK,
                        hError = Error(
                            hMessage = adError?.errorMessage,
                            hCode = adError?.errorCode,
                        ),
                    )
                }

                override fun onAdLoaded(ad: Ad?) {
                    hInterCallbacks?.hOnAddLoaded(AdsType.H_FACEBOOK)
                }

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
        hAdViewGroup: ViewGroup,
        hIsWithFallback: Boolean = true
    ) {
        hIdsMap?.get(WhatAd.H_BANNER)?.let { bannerId ->
            hAdViewGroup.layoutParams.height = hGetBannerHeightInPixel()
            hAddPlaceHolderTextView(hAdViewGroup)
            object : AdListener {
                override fun onError(ad: Ad?, adError: AdError?) {
                    adError?.errorMessage?.let {
                        hAdCallbacks?.hAdFailedToLoad(
                            hAdType = AdsType.H_FACEBOOK,
                            hWhatAd = WhatAd.H_BANNER,
                            hError = Error(
                                hMessage = adError.errorMessage,
                                hCode = adError.errorCode,
                            ),
                            hNativeView = hAdViewGroup,
                            hIsWithFallback = hIsWithFallback
                        )
                    }
                }

                override fun onAdLoaded(ad: Ad?) {
                    hAdViewGroup.visibility = View.VISIBLE
                    hAdCallbacks?.hAdLoaded(
                        hAdType = AdsType.H_FACEBOOK,
                        hWhatAd = WhatAd.H_BANNER
                    )
                    if (hFbBanner?.parent != null) {
                        (hFbBanner?.parent as ViewGroup).removeView(hFbBanner)
                    }
                    hAdViewGroup.addView(hFbBanner)
                }

                override fun onAdClicked(ad: Ad?) {
                    hAdCallbacks?.hAdClicked(
                        hAdType = AdsType.H_FACEBOOK,
                        hWhatAd = WhatAd.H_BANNER
                    )
                }

                override fun onLoggingImpression(ad: Ad?) {
                    hAdCallbacks?.hAdImpression(
                        hAdType = AdsType.H_FACEBOOK,
                        hWhatAd = WhatAd.H_BANNER
                    )
                }
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
        hNativeAdvancedView: HnativeAdvancedView,
        hIsWithFallback: Boolean = true
    ) {
        hIdsMap?.get(WhatAd.H_NATIVE_ADVANCED)?.let { nativeAdvancedId ->
            val nativeAdListener: NativeAdListener = object : NativeAdListener {
                override fun onMediaDownloaded(ad: Ad?) {}
                override fun onError(ad: Ad?, adError: AdError) {
                    hNativeAdvancedView.hShowHideAdLoader(true)

                    hAdCallbacks?.hAdFailedToLoad(
                        hAdType = AdsType.H_FACEBOOK,
                        hWhatAd = WhatAd.H_NATIVE_ADVANCED,
                        hError = Error(
                            hMessage = adError.errorMessage,
                            hCode = adError.errorCode,
                        ),
                        hNativeView = hNativeAdvancedView,
                        hIsWithFallback = hIsWithFallback
                    )
                }

                override fun onAdLoaded(ad: Ad?) {
                    hAdCallbacks?.hAdLoaded(
                        hAdType = AdsType.H_FACEBOOK,
                        hWhatAd = WhatAd.H_NATIVE_ADVANCED
                    )
                    if (nativeAd != null) {
                        hInflateFbNativeAdvanced(
                            nativeAd,
                            hNativeAdvancedView.context
                        ).let { nativeAdLayout ->
                            hNativeAdvancedView.hShowAdView(viewGroup = nativeAdLayout)
                        }
                    }
                }


                override fun onAdClicked(ad: Ad?) {
                    hAdCallbacks?.hAdClicked(
                        hAdType = AdsType.H_FACEBOOK,
                        hWhatAd = WhatAd.H_NATIVE_ADVANCED
                    )
                }

                override fun onLoggingImpression(ad: Ad?) {
                    hAdCallbacks?.hAdImpression(
                        hAdType = AdsType.H_FACEBOOK,
                        hWhatAd = WhatAd.H_NATIVE_ADVANCED
                    )
                }
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
        hIsWithFallback: Boolean = true
    ) {
        hIdsMap?.get(WhatAd.H_NATIVE_BANNER)?.let { nativeBannerId ->

            val nativeAdListener: NativeAdListener = object : NativeAdListener {
                override fun onMediaDownloaded(ad: Ad?) {}
                override fun onError(ad: Ad?, adError: AdError) {
                    hNativeBannerView.hShowHideAdLoader(true)
                    hAdCallbacks?.hAdFailedToLoad(
                        hAdType = AdsType.H_FACEBOOK,
                        hWhatAd = WhatAd.H_NATIVE_BANNER,
                        hError = Error(
                            hMessage = adError.errorMessage,
                            hCode = adError.errorCode,
                        ),
                        hNativeView = hNativeBannerView,
                        hIsWithFallback = hIsWithFallback
                    )
                }

                override fun onAdLoaded(ad: Ad?) {
                    hAdCallbacks?.hAdLoaded(
                        hAdType = AdsType.H_FACEBOOK,
                        hWhatAd = WhatAd.H_NATIVE_BANNER
                    )
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

                override fun onAdClicked(ad: Ad?) {
                    hAdCallbacks?.hAdClicked(
                        hAdType = AdsType.H_FACEBOOK,
                        hWhatAd = WhatAd.H_NATIVE_BANNER
                    )
                }

                override fun onLoggingImpression(ad: Ad?) {
                    hAdCallbacks?.hAdImpression(
                        hAdType = AdsType.H_FACEBOOK,
                        hWhatAd = WhatAd.H_NATIVE_BANNER
                    )
                }
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

    fun hSetInterCallbacks(interCallbacks: InterCallbacks) {
        hInterCallbacks = interCallbacks
    }

    fun hSetNativeCallbacks(adCallbacks: AdCallbacks) {
        hAdCallbacks = adCallbacks
    }

}