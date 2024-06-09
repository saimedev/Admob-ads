package com.saimedevs.compose.myads.ui.fragments.splash

import com.saimedevs.compose.myads.R
import com.saimedevs.compose.myads.adsconfig.interstitial.AdmobInterstitial
import com.saimedevs.compose.myads.adsconfig.natives.AdmobNativePreload
import com.saimedevs.compose.myads.adsconfig.interstitial.callbacks.InterstitialOnShowCallBack
import com.saimedevs.compose.myads.adsconfig.natives.enums.NativeType
import com.saimedevs.compose.myads.databinding.FragmentSplashLanguageBinding
import com.saimedevs.compose.myads.ui.activities.SplashActivity
import com.saimedevs.compose.myads.ui.fragments.base.BaseFragment

 
class FragmentSplashLanguage : BaseFragment<FragmentSplashLanguageBinding>(R.layout.fragment_splash_language) {

    private val admobInterstitial by lazy { AdmobInterstitial() }
    private val admobNativePreload by lazy { AdmobNativePreload() }

    override fun onViewCreatedOneTime() {
        binding.mbContinueLanguage.setOnClickListener { onContinueClick() }

        showNativeAd()
    }

    override fun onViewCreatedEverytime() {}


    /**
     * Add Service in Manifest first
     */

    private fun onContinueClick() {
        if (isAdded){
           // diComponent.sharedPreferenceUtils.showFirstScreen = false
            (activity as SplashActivity).nextActivity()
            admobInterstitial.showInterstitialAd(activity,object : InterstitialOnShowCallBack {
                override fun onAdDismissedFullScreenContent() {}
                override fun onAdFailedToShowFullScreenContent() {}
                override fun onAdShowedFullScreenContent() {}
                override fun onAdImpression() {}

            })
        }
    }

    private fun showNativeAd(){
        if (isAdded){
            admobNativePreload.showNativeAds(
                activity,
                binding.adsPlaceHolder,
                NativeType.LARGE_ADJUSTED
            )
        }
    }

    override fun navIconBackPressed() {}

    override fun onBackPressed() {}
}