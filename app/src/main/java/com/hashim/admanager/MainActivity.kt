package com.hashim.admanager

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.hashim.admanager.databinding.ActivityMainBinding
import com.hashim.hadmanager.adsmodule.AdManager
import com.hashim.hadmanager.adsmodule.Error
import com.hashim.hadmanager.adsmodule.callbacks.InterCallbacks
import com.hashim.hadmanager.adsmodule.callbacks.NativeCallbacks
import com.hashim.hadmanager.adsmodule.types.AdsType
import com.hashim.hadmanager.adsmodule.types.WhatAd
import timber.log.Timber

class MainActivity : AppCompatActivity(), InterCallbacks, NativeCallbacks {
    private lateinit var hMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(hMainBinding.root)

        AdManager.hSetInterCallbacks(this)
        AdManager.hSetNativeCallbacks(this)


        AdManager.hLoadInterstitial(this)
        AdManager.hShowNativeAdvanced(hMainBinding.hNativeAdvancedBanner)
        AdManager.hShowNativeBanner(hMainBinding.hNativeBanner)
        AdManager.hShowBannerWithOutFallback(hMainBinding.hBannerContainer)


        hMainBinding.hShowInter.setOnClickListener {
            AdManager.hShowInterstitial(this)
        }


    }

    override fun hOnAdFailedToLoad(hAdType: AdsType, hError: Error, hActivity: Activity?) {
        Timber.d("hOnAdFailedToLoad And AdType is $hAdType")
    }

    override fun hOnAddLoaded(hAdType: AdsType) {
        Timber.d("hOnAddLoaded And AdType is $hAdType")
    }

    override fun hOnAdFailedToShowFullContent(hAdType: AdsType, hError: Error) {
        Timber.d("hOnAdFailedToShowFullContent And AdType is $hAdType")
    }

    override fun hOnAddShowed(hAdType: AdsType) {
        Timber.d("hOnAddShowed And AdType is $hAdType")
    }

    override fun hOnAddDismissed(hAdType: AdsType) {
        Timber.d("hOnAddDismissed And AdType is $hAdType")
    }

    override fun hAdLoaded(hAdType: AdsType, hWhatAd: WhatAd) {
        Timber.d("hNativeAdvLoaded And AdType is $hAdType and What Add $hWhatAd")
    }

    override fun hAdClicked(hAdType: AdsType, hWhatAd: WhatAd) {
        Timber.d("hNativeAdvClicked And AdType is $hAdType  and What Add $hWhatAd")
    }

    override fun hAdImpression(hAdType: AdsType, hWhatAd: WhatAd) {
        Timber.d("hNativeAdvImpression And AdType is $hAdType  and What Add $hWhatAd")
    }

    override fun hAdClosed(hAdType: AdsType, hWhatAd: WhatAd) {
        Timber.d("hNativeAdvClosed And AdType is $hAdType  and What Add $hWhatAd")
    }

    override fun hAdFailedToLoad(hAdType: AdsType, hWhatAd: WhatAd, hError: Error, hNativeView: ViewGroup) {
        Timber.d("hNativeAdvFailedToLoad And AdType is $hAdType and error is $hError  and What Add $hWhatAd")
    }


    override fun hNativeAdOpened(hAdType: AdsType, hWhatAd: WhatAd) {
        Timber.d("hNativeAdvOpened And AdType is $hAdType  and What Add $hWhatAd")
    }
}