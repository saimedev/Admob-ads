package com.saimedevs.compose.myads.adsconfig.natives

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.saimedevs.compose.myads.R
import com.saimedevs.compose.myads.adsconfig.natives.callbacks.NativeCallBack
import com.saimedevs.compose.myads.adsconfig.natives.enums.NativeType
import com.saimedevs.compose.myads.adsconfig.utils.AdsConstants.preloadNativeAd
import com.saimedevs.compose.myads.adsconfig.utils.ScreenUtils.isSupportFullScreen
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AdmobNative {

    /**
     * 0 = Ads Off
     * 1 = Ads On
     */

    private var adMobNativeAd: NativeAd? = null

    /**
     * load native ad and show
     */
    fun loadNativeAds(
        activity: Activity?,
        adsPlaceHolder: FrameLayout,
        nativeId: String,
        adEnable: Int,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        nativeType: NativeType,
        nativeCallBack: NativeCallBack? = null
    ) {
        val handlerException = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e("AdsInformation", "${throwable.message}")
            nativeCallBack?.onAdFailedToLoad("${throwable.message}")
        }

        if (isAppPurchased) {
            Log.e("AdsInformation", "onAdFailedToLoad -> Premium user")
            nativeCallBack?.onAdFailedToLoad("onAdFailedToLoad -> Premium user")
            return
        }

        if (adEnable == 0) {
            Log.e("AdsInformation", "onAdFailedToLoad -> Remote config is off")
            nativeCallBack?.onAdFailedToLoad("onAdFailedToLoad -> Remote config is off")
            return
        }

        if (isInternetConnected.not()) {
            Log.e("AdsInformation", "onAdFailedToLoad -> Internet is not connected")
            nativeCallBack?.onAdFailedToLoad("onAdFailedToLoad -> Internet is not connected")
            return
        }

        if (activity == null) {
            Log.e("AdsInformation", "onAdFailedToLoad -> Context is null")
            nativeCallBack?.onAdFailedToLoad("onAdFailedToLoad -> Context is null")
            return
        }

        if (activity.isFinishing || activity.isDestroyed) {
            Log.e("AdsInformation", "onAdFailedToLoad -> activity is finishing or destroyed")
              nativeCallBack?.onAdFailedToLoad("onAdFailedToLoad -> activity is finishing or destroyed")
            return
        }

        if (nativeId.trim().isEmpty()) {
            Log.e("AdsInformation", "onAdFailedToLoad -> Ad id is empty")
              nativeCallBack?.onAdFailedToLoad("onAdFailedToLoad -> Ad id is empty")
            return
        }

        try {
            adsPlaceHolder.visibility = View.VISIBLE
            // reuse of preloaded native ad
            // if miss first native then use it next
            if (preloadNativeAd != null) {
                adMobNativeAd = preloadNativeAd
                preloadNativeAd = null
                Log.i("AdsInformation", "admob native onPreloaded")
                nativeCallBack?.onPreloaded()
                displayNativeAd(activity, adsPlaceHolder, nativeType)
                return
            }
            if (adMobNativeAd == null) {
                CoroutineScope(Dispatchers.IO + handlerException).launch {
                    val builder: AdLoader.Builder = AdLoader.Builder(activity, nativeId)
                    val adLoader =
                        builder.forNativeAd { unifiedNativeAd: NativeAd? ->
                            adMobNativeAd = unifiedNativeAd
                        }
                            .withAdListener(object : AdListener() {
                                override fun onAdImpression() {
                                    super.onAdImpression()
                                    Log.d("AdsInformation", "admob native onAdImpression")
                                    nativeCallBack?.onAdImpression()
                                    adMobNativeAd = null
                                }

                                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                    Log.e("AdsInformation", "admob native onAdFailedToLoad: ${loadAdError.message}")
                                    nativeCallBack?.onAdFailedToLoad(loadAdError.message)
                                    adsPlaceHolder.visibility = View.GONE
                                    adMobNativeAd = null
                                    super.onAdFailedToLoad(loadAdError)
                                }

                                override fun onAdLoaded() {
                                    super.onAdLoaded()
                                    Log.i("AdsInformation", "admob native onAdLoaded")
                                    nativeCallBack?.onAdLoaded()
                                    displayNativeAd(activity, adsPlaceHolder, nativeType)

                                }

                            }).withNativeAdOptions(
                                com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                                    .setAdChoicesPlacement(
                                        NativeAdOptions.ADCHOICES_TOP_RIGHT
                                    ).build()
                            )
                            .build()
                    adLoader.loadAd(AdRequest.Builder().build())

                }
            } else {
                Log.e("AdsInformation", "Native is already onPreloaded")
                nativeCallBack?.onPreloaded()
                displayNativeAd(activity, adsPlaceHolder, nativeType)
            }
        } catch (ex: Exception) {
            Log.e("AdsInformation", "${ex.message}")
            nativeCallBack?.onAdFailedToLoad("${ex.message}")
        }
    }

    private fun displayNativeAd(
        activity: Activity?,
        adMobNativeContainer: FrameLayout,
        nativeType: NativeType,
    ) {
        activity?.let { mActivity ->
            try {
                adMobNativeAd?.let { ad ->
                    val inflater = LayoutInflater.from(mActivity)

                    val adView: NativeAdView = when (nativeType) {
                        NativeType.BANNER -> inflater.inflate(R.layout.native_banner, null) as NativeAdView
                        NativeType.SMALL -> inflater.inflate(R.layout.native_small, null) as NativeAdView
                        NativeType.LARGE -> inflater.inflate(R.layout.native_large, null) as NativeAdView
                        NativeType.LARGE_ADJUSTED -> if (mActivity.isSupportFullScreen()) {
                            inflater.inflate(R.layout.native_large, null) as NativeAdView
                        } else {
                            inflater.inflate(R.layout.native_small, null) as NativeAdView
                        }
                    }
                    val viewGroup: ViewGroup? = adView.parent as ViewGroup?
                    viewGroup?.removeView(adView)

                    adMobNativeContainer.removeAllViews()
                    adMobNativeContainer.addView(adView)

                    if (nativeType == NativeType.LARGE) {
                        val mediaView: MediaView = adView.findViewById(R.id.media_view)
                        adView.mediaView = mediaView
                    }
                    if (nativeType == NativeType.LARGE_ADJUSTED) {
                        if (mActivity.isSupportFullScreen()) {
                            val mediaView: MediaView = adView.findViewById(R.id.media_view)
                            adView.mediaView = mediaView
                        }
                    }

                    // Set other ad assets.
                    adView.headlineView = adView.findViewById(R.id.ad_headline)
                    adView.bodyView = adView.findViewById(R.id.ad_body)
                    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
                    adView.iconView = adView.findViewById(R.id.ad_app_icon)

                    //Headline
                    adView.headlineView?.let { headline ->
                        (headline as TextView).text = ad.headline
                        headline.isSelected = true
                    }

                    //Body
                    adView.bodyView?.let { bodyView ->
                        if (ad.body == null) {
                            bodyView.visibility = View.INVISIBLE
                        } else {
                            bodyView.visibility = View.VISIBLE
                            (bodyView as TextView).text = ad.body
                        }
                    }

                    //Call to Action
                    adView.callToActionView?.let { ctaView ->
                        if (ad.callToAction == null) {
                            ctaView.visibility = View.GONE
                        } else {
                            ctaView.visibility = View.VISIBLE
                            (ctaView as Button).text = ad.callToAction
                        }
                    }

                    //Icon
                    adView.iconView?.let { iconView ->
                        if (ad.icon == null) {
                            iconView.visibility = View.GONE
                        } else {
                            (iconView as ImageView).setImageDrawable(ad.icon?.drawable)
                            iconView.visibility = View.VISIBLE
                        }
                    }

                    adView.advertiserView?.let { adverView ->
                        if (ad.advertiser == null) {
                            adverView.visibility = View.GONE
                        } else {
                            (adverView as TextView).text = ad.advertiser
                            adverView.visibility = View.GONE
                        }
                    }

                    adView.setNativeAd(ad)
                }
            } catch (ex: Exception) {
                Log.e("AdsInformation", "displayNativeAd: ${ex.message}")
            }
        }
    }
}