package com.hashim.admanager

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.hashim.admanager.databinding.ActivityMainBinding
import com.hashim.hadmanager.adsmodule.AdManager
import com.hashim.hadmanager.adsmodule.Error
import com.hashim.hadmanager.adsmodule.callbacks.AdCallbacks
import com.hashim.hadmanager.adsmodule.callbacks.InterCallbacks
import com.hashim.hadmanager.adsmodule.types.AdsType
import com.hashim.hadmanager.adsmodule.types.WhatAd
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var hMainBinding: ActivityMainBinding

    private val hInterCallbacks = object : InterCallbacks() {
        override fun hOnAdFailedToLoad(hAdType: AdsType, hError: Error, hActivity: Activity?) {
            Timber.d("InterCallbacks hOnAdFailedToLoad And AdType is $hAdType")
        }

        override fun hOnAddLoaded(hAdType: AdsType) {
            Timber.d("InterCallbacks hOnAddLoaded And AdType is $hAdType")
        }

        override fun hOnAdFailedToShowFullContent(hAdType: AdsType, hError: Error) {
            Timber.d("InterCallbacks hOnAdFailedToShowFullContent And AdType is $hAdType")
        }

        override fun hOnAddShowed(hAdType: AdsType) {
            Timber.d("InterCallbacks hOnAddShowed And AdType is $hAdType")
        }

        override fun hOnAddDismissed(hAdType: AdsType) {
            Timber.d("InterCallbacks hOnAddDismissed And AdType is $hAdType")
        }

        override fun hOnAdTimedOut(hAdType: AdsType) {
            Timber.d("InterCallbacks hOnAdTimedOut And AdType is $hAdType")
        }
    }


    private val hNativeCallbacks = object : AdCallbacks() {

        override fun hAdLoaded(hAdType: AdsType, hWhatAd: WhatAd) {
            Timber.d("AdCallbacks hNativeAdvLoaded And AdType is $hAdType and What Add $hWhatAd")
        }

        override fun hAdClicked(hAdType: AdsType, hWhatAd: WhatAd) {
            Timber.d(" AdCallbacks hNativeAdvClicked And AdType is $hAdType  and What Add $hWhatAd")
        }

        override fun hAdImpression(hAdType: AdsType, hWhatAd: WhatAd) {
            Timber.d("AdCallbacks hNativeAdvImpression And AdType is $hAdType  and What Add $hWhatAd")
        }

        override fun hAdClosed(hAdType: AdsType, hWhatAd: WhatAd) {
            Timber.d("AdCallbacks hNativeAdvClosed And AdType is $hAdType  and What Add $hWhatAd")
        }


        override fun hAdFailedToLoad(
            hAdType: AdsType,
            hWhatAd: WhatAd,
            hError: Error,
            hNativeView: ViewGroup,
            hIsWithFallback: Boolean
        ) {
            Timber.d("AdCallbacks hNativeAdvFailedToLoad And AdType is $hAdType and error is $hError  and What Add $hWhatAd")
        }


        override fun hNativeAdOpened(hAdType: AdsType, hWhatAd: WhatAd) {
            Timber.d("AdCallbacks hNativeAdvOpened And AdType is $hAdType  and What Add $hWhatAd")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(hMainBinding.root)

        AdManager.hSetInterCallbacks(hInterCallbacks)
        AdManager.hSetNativeCallbacks(hNativeCallbacks)


        AdManager.hLoadInterstitial(this)
        AdManager.hShowNativeAdvanced(hMainBinding.hNativeAdvancedBanner)
        AdManager.hShowNativeBanner(hMainBinding.hNativeBanner)
        AdManager.hShowBannerWithOutFallback(hMainBinding.hBannerContainer)


        hMainBinding.hShowInter.setOnClickListener {
            AdManager.hShowInterstitial(this)
        }
    }
}