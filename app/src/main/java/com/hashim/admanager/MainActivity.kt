package com.hashim.admanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hashim.admanager.databinding.ActivityMainBinding
import com.hashim.hadmanager.adsmodule.AdManager

class MainActivity : AppCompatActivity() {
    private lateinit var hMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(hMainBinding.root)


        AdManager.hShowNativeAdvanced(hMainBinding.hNativeAdvancedBanner)
        AdManager.hShowNativeBanner(hMainBinding.hNativeBanner)
        AdManager.hShowBanner(hMainBinding.hBannerContainer)


        hMainBinding.hShowInter.setOnClickListener {
            AdManager.hShowInterstitial(this)
        }


    }
}