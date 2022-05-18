# AndroidAdManager

- Works with Admob, Mopup, Facebook- Bidding and Audience Networks.
- Added ad types are NativeBanner, NativeAdvanced, Interstitial and Banner Ads for Facebook, Admob and MopUp.
- By Default some fallback strategies are added for all Ads in case any fails other takes its place.
- Ui Always stays stable, no displacement.
- Shimmer animations while Ads is loaded.
- Banner size is calculated automatically according to varying screen sizes.

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.hashimTahir:AndroidAdManager:1.6'
	}

Step 3. Add the (Required) debug and release ids in the project level gradle file like so:

	  resValue 'string', 'AdMob_AppId', '"ca-app-pub-3940256099942544~3347511713"'
      resValue 'string', 'Admob_BannerId', '"ca-app-pub-3940256099942544/6300978111"'
      resValue 'string', 'Admob_NativeAdvancedId', '"ca-app-pub-3940256099942544/2247696110"'
      resValue 'string', 'Admob_InterstitialId', '"ca-app-pub-3940256099942544/1033173712"'

      resValue 'string', 'Mopub_BannerId', 'b195f8dd8ded45fe847ad89ed1d016da'
      resValue 'string', 'Mopub_InterstitialId', '24534e1901884e398f1253216226017e'
      resValue 'string', 'Mopup_Native_BannerId', '"11a17b188668469fb0412708c3d16813"'

      resValue 'string', 'Fb_InterstitialId', 'abcd'
      resValue 'string', 'Fb_NativeAdvancedId', 'abcd'
      resValue 'string', 'Fb_Native_BannerId', '"abcd"'
      resValue 'string', 'Fb_BannerId', '"abcd"'

Step 4. Add the relevant strings in strings file

     <string name="admob_app_id">@string/AdMob_AppId</string>
     <string name="admob_banner_id">@string/Admob_BannerId</string>
     <string name="admob_native_advanced_id">@string/Admob_NativeAdvancedId</string>
     <string name="admob_interstitial_id">@string/Admob_InterstitialId</string>

     <string name="fb_interstitial_id">@string/Fb_InterstitialId</string>
     <string name="fb_native_advanced_id">@string/Fb_NativeAdvancedId</string>
     <string name="fb_native_banner_id">@string/Fb_Native_BannerId</string>
     <string name="fb_banner_id">@string/Fb_BannerId</string>

     <string name="mopub_banner_id">@string/Mopub_BannerId</string>
     <string name="mopub_interstitial_id">@string/Mopub_InterstitialId</string>
     <string name="mopup_native_banner_id">@string/Mopup_Native_BannerId</string>

Step 5. Initialize the AndroidAdManager in Application class. by passing the Required

ids and context in the Admanager's constructor.

     val hIdsMap = hashMapOf<AdsType, HashMap<WhatAd, String>>()
     val hAdMobMap = hashMapOf<WhatAd, String>()
     val hMopUpMap = hashMapOf<WhatAd, String>()
     val hFacebookMap = hashMapOf<WhatAd, String>()

     hAdMobMap[WhatAd.H_NATIVE_ADVANCED] = getString(R.string.admob_native_advanced_id)
     hAdMobMap[WhatAd.H_NATIVE_BANNER] = getString(R.string.admob_native_advanced_id)
     hAdMobMap[WhatAd.H_BANNER] = getString(R.string.admob_banner_id)
     hAdMobMap[WhatAd.H_INTER] = getString(R.string.admob_interstitial_id)


     hMopUpMap[WhatAd.H_NATIVE_ADVANCED] = getString(R.string.mopup_native_banner_id)
     hMopUpMap[WhatAd.H_NATIVE_BANNER] = getString(R.string.mopup_native_banner_id)
     hMopUpMap[WhatAd.H_BANNER] = getString(R.string.mopub_banner_id)
     hMopUpMap[WhatAd.H_INTER] = getString(R.string.mopub_interstitial_id)

     hFacebookMap[WhatAd.H_NATIVE_ADVANCED] = getString(R.string.fb_native_advanced_id)
     hFacebookMap[WhatAd.H_NATIVE_BANNER] = getString(R.string.fb_native_banner_id)
     hFacebookMap[WhatAd.H_BANNER] = getString(R.string.fb_banner_id)
     hFacebookMap[WhatAd.H_INTER] = getString(R.string.fb_interstitial_id)

     hIdsMap[AdsType.H_ADMOB] = hAdMobMap
     hIdsMap[AdsType.H_FACEBOOK] = hFacebookMap
     hIdsMap[AdsType.H_MOPUP] = hMopUpMap

     AdManager.hInitializeAds(
            this,
            hIdsMap
        )

Step 6. By default all priorities are set to Admob->MopUp->Facebook-None. if one fails,

other takes its place. To change the priorities use following methods:

     AdManager.hSetNativeAdvancedPriority(AdPriorityType.H_MOP_UP)
     AdManager.hSetNativeBannerPriority(AdPriorityType.H_MOP_UP)   

Above methods change the priorities to MopUp->AdMob-Facebook->None. Strategies can be

found in "fallbackstrategies" package. To create ur own, create a class which extends the

"Strategy" interface.

Step 7. Call the relevant methods from Ui to display Ads.

     AdManager.hShowNativeAdvanced(hMainBinding.hNativeAdvancedBanner)
     AdManager.hShowNativeBanner(hMainBinding.hNativeBanner)
     AdManager.hShowBanner(hMainBinding.hBannerContainer)

Step 8. To set the Ads Callback use the following methods in your calling activity,
and override the methods.

       AdManager.hSetInterCallbacks(hInterCallbacks)
        AdManager.hSetNativeCallbacks(hNativeCallbacks)

Callbacks for these adevents are available in "AdCallbacks" and "InterCallbacks"
Abstract classes.

    fun hAdLoaded(
    hAdType: AdsType,
    hWhatAd: WhatAd,
    )
    
    fun hAdClicked(
        hAdType: AdsType,
        hWhatAd: WhatAd
    )

    fun hAdImpression(
        hAdType: AdsType,
        hWhatAd: WhatAd
    )

    fun hAdClosed(
        hAdType: AdsType,
        hWhatAd: WhatAd
    )

    fun hAdFailedToLoad(
        hAdType: AdsType,
        hWhatAd: WhatAd,
        hError: Error,
        hNativeView: ViewGroup,
        hIsWithFallback: Boolean
    )

    fun hNativeAdOpened(
        hAdType: AdsType,
        hWhatAd: WhatAd
    )

    open fun hOnAdTimedOut(
    hAdType: AdsType
    ) {

    }



Step 9. Default timeout is 3 seconds. To Change the timeout use the method.

        AdManager.hSetTimeout(Constants.h3SecTimeOut)

if for some reason the ad fails to even make request and gets stuck,
 fun hOnAdTimedOut will get called after the given time.


If you don't want to fallback to others Ads and only use fixed Ads then use the following

methods.

      AdManager.hShowNativeBannerWithOutFallback(
            hMainBinding.hNativeBanner,
            AdPriorityType.H_MOP_UP
        )
      AdManager.hShowBannerWithOutFallback(
      hMainBinding.hNativeBanner,
      AdPriorityType.H_MOP_UP
       )
      AdManager.hShowNativeAdvancedWithOutFallback(
      hMainBinding.hNativeAdvancedBanner,
      AdPriorityType.H_MOP_UP
      )

Above methods will only show Mopup Ads, if it fails, none will be shown in its place.

By default AdContainers i.e. HnativeBannerView and HnativeAdvacncedView are rounded and

are given app's primary color and stroke. To change use method:

     fun hSetBackGroundDrawable(
        hBackgroundColor: Int,
        hCornerRadius: Int = 6,
        hStroke: Stroke = Stroke(context)
    ) {
        val hGradientDrawable = GradientDrawable()
        hGradientDrawable.apply {
            setColor(hBackgroundColor)
            cornerRadius = hDp(context, hCornerRadius)
            setStroke(
                hStroke.hWidth,
                hStroke.hColor
            )
            hLayoutHAdcontainerBinding.hAdRootCL.background = hGradientDrawable
        }
    }

Or Change from layout as:

     <com.hashim.hadmanager.adsmodule.customadview.HnativeBannerView
     android:id="@+id/hNativeBanner"
     android:layout_width="match_parent"
     android:layout_height="wrap_content"
     app:hBackgroundColor="@color/black"
     app:hCornerRadius="6"
     app:hStrokeWidth="6"
     app:hStrokeColor="@color/black"
     app:layout_constraintEnd_toEndOf="parent"
     app:layout_constraintStart_toStartOf="parent"
     app:layout_constraintTop_toBottomOf="@id/hNativeAdvancedBanner" />

To Show you own Holder while ad is loading over NativeAdvanced use following:

     <com.hashim.hadmanager.adsmodule.customadview.HnativeAdvancedView
     android:id="@+id/hNativeAdvancedBanner"
     android:layout_width="match_parent"
     android:layout_height="wrap_content"
     app:hLoaderContainer="@layout/native_advanced_alternative"
     app:layout_constraintEnd_toEndOf="parent"
     app:layout_constraintStart_toStartOf="parent"
     app:layout_constraintTop_toTopOf="parent" />

Where native_advanced_alternative is your own Holder containing anything, Image,

animations. etc.

View when ad is loading

![ScreenShot](/images/loading_1.jpg)
![ScreenShot](/images/loading_2.jpg)

View when ad is loaded

![ScreenShot](/images/loaded_1.jpg)
![ScreenShot](/images/loaded_2.jpg)






