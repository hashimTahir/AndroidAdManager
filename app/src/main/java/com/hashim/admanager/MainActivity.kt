package com.hashim.admanager

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hashim.admanager.databinding.ActivityMainBinding
import com.hashim.hadmanager.adsmodule.AdManager
import com.hashim.hadmanager.adsmodule.Error
import com.hashim.hadmanager.adsmodule.callbacks.InterCallbacks
import com.hashim.hadmanager.adsmodule.callbacks.NativeCallBacks
import com.hashim.hadmanager.adsmodule.customadview.HnativeAdvancedView
import com.hashim.hadmanager.adsmodule.types.AdsType
import timber.log.Timber

class MainActivity : AppCompatActivity(), InterCallbacks, NativeCallBacks {
    private lateinit var hMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(hMainBinding.root)

//        AdManager.hLoadInterstitial(this)
        AdManager.hSetInterCallbacks(this)
        AdManager.hSetNativeCallBacks(this)


        AdManager.hShowNativeAdvanced(hMainBinding.hNativeAdvancedBanner)
//        AdManager.hShowNativeBanner(hMainBinding.hNativeBanner)
//        AdManager.hShowBanner(hMainBinding.hBannerContainer)


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

    override fun hOnNativeAdvancedFailed(hAdType: AdsType, hError: Error, hNativeAdvanceView: HnativeAdvancedView) {
        Timber.d("hOnNativeAdvancedFailed And AdType is $hAdType")
    }

    override fun hOnAdClosed(hAdType: AdsType) {
        Timber.d("hOnAdClosed And AdType is $hAdType")
    }

    override fun hOnAdOpened(hAdType: AdsType) {
        Timber.d("hOnAdOpened And AdType is $hAdType")
    }

    override fun hOnAdLoaded(hAdType: AdsType) {
        Timber.d("hOnAdLoaded And AdType is $hAdType")
    }

    override fun hOnAdClicked(hAdType: AdsType) {
        Timber.d("hOnAdClicked And AdType is $hAdType")
    }

    override fun hOnAdImpression(hAdType: AdsType) {
        Timber.d("hOnAdImpression And AdType is $hAdType")
    }
}