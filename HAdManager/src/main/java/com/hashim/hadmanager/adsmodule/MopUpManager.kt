package com.hashim.hadmanager.adsmodule

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.facebook.ads.AudienceNetworkAds
import com.hashim.hadmanager.BuildConfig
import com.hashim.hadmanager.R
import com.hashim.hadmanager.adsmodule.callbacks.InterCallbacks
import com.hashim.hadmanager.adsmodule.callbacks.NativeCallBacks
import com.hashim.hadmanager.adsmodule.customadview.HnativeAdvancedView
import com.hashim.hadmanager.adsmodule.customadview.HnativeBannerView
import com.hashim.hadmanager.adsmodule.types.AdsType
import com.hashim.hadmanager.adsmodule.types.WhatAd
import com.mopub.common.BaseAdapterConfiguration
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.logging.MoPubLog
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubInterstitial
import com.mopub.mobileads.MoPubView
import com.mopub.nativeads.*


class MopUpManager(
    private val myApplication: Context,
    private val hIdsMap: HashMap<WhatAd, String>?
) {
    var hMopubInterstitial: MoPubInterstitial? = null
        private set

    private var hMopupNativeAdvanced: MoPubNative? = null
    private var hMopupNativeBanner: MoPubNative? = null
    private var hInterCallbacks: InterCallbacks? = null
    private var hNativeCallBacks: NativeCallBacks? = null


    fun loadInterstitialAd(
        activity: Activity?,
    ) {

        hIdsMap?.get(WhatAd.H_INTER)?.let { interId ->
            hMopubInterstitial = MoPubInterstitial(
                activity!!,
                interId
            )
            hMopubInterstitial?.interstitialAdListener =
                object : MoPubInterstitial.InterstitialAdListener {
                    override fun onInterstitialLoaded(interstitial: MoPubInterstitial) {
                        hInterCallbacks?.hOnAddLoaded(hAdType = AdsType.H_MOPUP)
                    }

                    override fun onInterstitialFailed(
                        interstitial: MoPubInterstitial,
                        errorCode: MoPubErrorCode
                    ) {
                        hInterCallbacks?.hOnAdFailedToLoad(
                            hAdType = AdsType.H_MOPUP,
                            hActivity = activity,
                            hError = Error(
                                hMessage = errorCode.name,
                                hCode = errorCode.intCode
                            )
                        )
                    }

                    override fun onInterstitialShown(interstitial: MoPubInterstitial) {
                        hInterCallbacks?.hOnAddShowed(AdsType.H_MOPUP)
                    }

                    override fun onInterstitialClicked(interstitial: MoPubInterstitial) {

                    }

                    override fun onInterstitialDismissed(interstitial: MoPubInterstitial) {
                        hInterCallbacks?.hOnAddDismissed(AdsType.H_MOPUP)
                    }
                }
            hMopubInterstitial?.load()
        }

    }

    fun hShowBanner(
        bannerView: ViewGroup,
        hOnMopUpBannerFailed: (
            adsType: AdsType,
            message: String,
            adContainerView: ViewGroup
        ) -> Unit,
    ) {

        hIdsMap?.get(WhatAd.H_BANNER)?.let { bannerId ->
            val moPubView = MoPubView(myApplication)
            moPubView.setAdUnitId(bannerId)
            moPubView.adSize = MoPubView.MoPubAdSize.HEIGHT_50
            moPubView.loadAd()
            bannerView.layoutParams.height = bannerHeightInPixel
            addPlaceHolderTextView(bannerView)
            moPubView.bannerAdListener = object : MoPubView.BannerAdListener {
                override fun onBannerLoaded(banner: MoPubView) {
                    bannerView.removeAllViews()
                    bannerView.addView(moPubView)
                }

                override fun onBannerFailed(banner: MoPubView, errorCode: MoPubErrorCode) {
                    hOnMopUpBannerFailed(
                        AdsType.H_MOPUP,
                        errorCode.name,
                        bannerView
                    )
                }

                override fun onBannerClicked(banner: MoPubView) {}
                override fun onBannerExpanded(banner: MoPubView) {}
                override fun onBannerCollapsed(banner: MoPubView) {}
            }
        }

    }

    private val bannerHeightInPixel: Int
        get() {
            val display =
                (myApplication.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val scale = outMetrics.density
            return (50 * scale + 0.5f).toInt()
        }

    private fun addPlaceHolderTextView(adContainerView: ViewGroup) {
        val valueTV = TextView(myApplication)
        valueTV.setText(R.string.ad_loading)
        valueTV.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        valueTV.gravity = Gravity.CENTER
        adContainerView.addView(valueTV)
    }

    fun hShowNativeAdvanced(
        nativeAdvancedView: HnativeAdvancedView,
    ) {
        hIdsMap?.get(WhatAd.H_NATIVE_ADVANCED)?.let { nativeAdvancedId ->
            hMopupNativeAdvanced = MoPubNative(
                myApplication,
                nativeAdvancedId,
                object : MoPubNative.MoPubNativeNetworkListener {
                    override fun onNativeLoad(nativeAd: NativeAd) {
                        hNativeCallBacks?.hOnAdLoaded(hAdType = AdsType.H_MOPUP)
                        val ctx = myApplication
                        val moPubNativeEventListener: NativeAd.MoPubNativeEventListener =
                            object : NativeAd.MoPubNativeEventListener {

                                override fun onImpression(view: View?) {
                                    hNativeCallBacks?.hOnAdImpression(hAdType = AdsType.H_MOPUP)
                                }

                                override fun onClick(view: View?) {
                                    hNativeCallBacks?.hOnAdClicked(hAdType = AdsType.H_MOPUP)
                                }
                            }
                        val adapterHelper = AdapterHelper(ctx, 0, 2)
                        val adView: View = adapterHelper.getAdView(
                            null,
                            null,
                            nativeAd,
                            ViewBinder.Builder(0).build()
                        )
                        nativeAd.setMoPubNativeEventListener(moPubNativeEventListener)
                        nativeAdvancedView.hShowAdView(view = adView)

                    }

                    override fun onNativeFail(errorCode: NativeErrorCode) {
                        nativeAdvancedView.hShowHideAdLoader(hShowLoader = true)
                        hNativeCallBacks?.hOnNativeAdvancedFailed(
                            hAdType = AdsType.H_MOPUP,
                            hError = Error(
                                hMessage = errorCode.name,
                                hCode = errorCode.intCode,
                            ),
                            hNativeAdvanceView = nativeAdvancedView,
                        )
                    }
                }
            )

            MoPubStaticNativeAdRenderer(
                ViewBinder.Builder(R.layout.mopup_native_advanced_layout)
                    .titleId(R.id.native_title)
                    .textId(R.id.native_text)
                    .mainImageId(R.id.native_main_image)
                    .iconImageId(R.id.native_icon_image)
                    .callToActionId(R.id.native_cta)
                    .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                    .sponsoredTextId(R.id.native_sponsored_text_view)
                    .build()
            ).also {
                hMopupNativeAdvanced?.apply {
                    registerAdRenderer(it)
                    makeRequest()
                }
            }
        }


    }


    fun hShowNativeBanner(
        hAdContainer: HnativeBannerView,
        hOnMopUpNativeBannerFailed: (
            adsType: AdsType,
            errorMessage: String,
            hAdContainer: HnativeBannerView,
        ) -> Unit
    ) {
        hIdsMap?.get(WhatAd.H_NATIVE_BANNER)?.let { nativeBannerId ->
            hMopupNativeBanner = MoPubNative(
                myApplication,
                nativeBannerId,
                object : MoPubNative.MoPubNativeNetworkListener {

                    override fun onNativeLoad(nativeAd: NativeAd) {
                        val ctx = myApplication


                        val adapterHelper = AdapterHelper(ctx, 0, 2)
                        val adView: View = adapterHelper.getAdView(
                            null,
                            hAdContainer.hLayoutHAdcontainerBinding.hAdContainer,
                            nativeAd,
                            ViewBinder.Builder(0).build()
                        )

                        nativeAd.prepare(adView)
                        nativeAd.renderAdView(adView)

                        hAdContainer.hShowAdView(view = adView)


                        val moPubNativeEventListener: NativeAd.MoPubNativeEventListener =
                            object : NativeAd.MoPubNativeEventListener {

                                override fun onImpression(view: View?) {

                                }

                                override fun onClick(view: View?) {
                                }
                            }

                        nativeAd.setMoPubNativeEventListener(moPubNativeEventListener)
                    }

                    override fun onNativeFail(errorCode: NativeErrorCode) {
                        hAdContainer.hShowHideAdLoader(hShowLoader = true)
                        hOnMopUpNativeBannerFailed(
                            AdsType.H_MOPUP,
                            errorCode.name,
                            hAdContainer
                        )
                    }
                }
            )


            MoPubStaticNativeAdRenderer(
                ViewBinder.Builder(R.layout.mopup_native_banner_layout)
                    .titleId(R.id.native_title)
                    .textId(R.id.native_text)
                    .mainImageId(R.id.native_main_image)
                    .iconImageId(R.id.native_icon_image)
                    .callToActionId(R.id.native_cta)
                    .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                    .build()
            ).also {
                hMopupNativeBanner?.apply {
                    registerAdRenderer(it)
                    makeRequest()


                }
            }
        }
    }

    fun hSetInterCallbacks(interCallbacks: AdManager) {
        hInterCallbacks = interCallbacks
    }
    fun hSetNativeCallBacks(nativeCallBacks: NativeCallBacks) {
        hNativeCallBacks = nativeCallBacks
    }

    init {
        val configBuilder = SdkConfiguration.Builder("adunit")
        if (BuildConfig.DEBUG) {
            configBuilder.withLogLevel(MoPubLog.LogLevel.DEBUG)
        } else {
            configBuilder.withLogLevel(MoPubLog.LogLevel.INFO)
        }
        val localExtras: MutableMap<String, String> = HashMap()
        localExtras["native_banner"] = "true"
        configBuilder.withMediatedNetworkConfiguration(
            BaseAdapterConfiguration::class.java.name,
            localExtras
        )
        AudienceNetworkAds.initialize(myApplication)
        MoPub.initializeSdk(myApplication, configBuilder.build()) { }
    }


}