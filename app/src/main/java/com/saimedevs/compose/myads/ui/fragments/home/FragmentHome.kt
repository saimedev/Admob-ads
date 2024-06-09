package com.saimedevs.compose.myads.ui.fragments.home

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.saimedevs.compose.myads.Constant
import com.saimedevs.compose.myads.Constant.rcvNativeAd
import com.saimedevs.compose.myads.Constant.rcvRewardAd
import com.saimedevs.compose.myads.InternetManager
import com.saimedevs.compose.myads.R
import com.saimedevs.compose.myads.adsconfig.banners.AdmobBanner
import com.saimedevs.compose.myads.adsconfig.banners.callbacks.BannerCallBack
import com.saimedevs.compose.myads.adsconfig.banners.enums.BannerType
import com.saimedevs.compose.myads.adsconfig.natives.AdmobNative
import com.saimedevs.compose.myads.adsconfig.natives.callbacks.NativeCallBack
import com.saimedevs.compose.myads.adsconfig.natives.enums.NativeType
import com.saimedevs.compose.myads.adsconfig.rewarded.AdmobRewarded
import com.saimedevs.compose.myads.adsconfig.rewarded.callbacks.RewardedOnLoadCallBack
import com.saimedevs.compose.myads.adsconfig.rewarded.callbacks.RewardedOnShowCallBack
import com.saimedevs.compose.myads.databinding.FragmentHomeBinding
import com.saimedevs.compose.myads.ui.activities.MainActivity
import com.saimedevs.compose.myads.ui.fragments.base.BaseFragment

 
class FragmentHome : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val admobBanner by lazy { AdmobBanner() }
    private val admobNative by lazy { AdmobNative() }
    private val admobRewarded by lazy { AdmobRewarded() }
    private lateinit var internetManager: InternetManager
    override fun onViewCreatedOneTime() {
        internetManager = InternetManager(requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

        binding.mbClickSample.setOnClickListener {
            navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentSample)
            (activity as MainActivity).checkCounter()
        }
        binding.mbClickBanner.setOnClickListener {
            navigateTo(R.id.fragmentHome, R.id.action_fragmentHome_to_fragmentBanner)
            (activity as MainActivity).checkCounter()
        }

        binding.mbClickRewarded.setOnClickListener {
            binding.mbClickRewarded.isEnabled = false
            loadRewardedAd()
        }

        loadAds()
    }

    override fun onViewCreatedEverytime() {}

    override fun navIconBackPressed() {
        onBackPressed()
    }

    override fun onBackPressed() {}

    fun loadRewardedAd(){
        Log.d("AdsInformation", "Call Admob Rewarded")
        admobRewarded.loadRewardedAd(
            activity,
            getString(R.string.admob_rewarded_ids),
            rcvRewardAd,
            false,
            internetManager.isInternetConnected,
            object : RewardedOnLoadCallBack {
                override fun onAdFailedToLoad(adError: String) {
                    binding.mbClickRewarded.isEnabled = true
                }
                override fun onAdLoaded() {
                    showRewardedAd()
                    binding.mbClickRewarded.isEnabled = true
                }
                override fun onPreloaded() {
                    showRewardedAd()
                    binding.mbClickRewarded.isEnabled = true
                }
            }
        )
    }

    fun showRewardedAd(){
        admobRewarded.showRewardedAd(
            activity,
            object : RewardedOnShowCallBack {
                override fun onAdDismissedFullScreenContent() {}
                override fun onAdFailedToShowFullScreenContent() {}
                override fun onAdShowedFullScreenContent() {}
                override fun onUserEarnedReward() {}
            }
        )
    }

    private fun loadAds() {
        Log.d("AdsInformation", "Call Admob Banner")
        admobBanner.loadBannerAds(
            activity,
            binding.adsBannerPlaceHolder,
            getResString(R.string.admob_banner_ids),
            Constant.rcvBannerAd,
            false,
            internetManager.isInternetConnected,
            BannerType.ADAPTIVE_BANNER,
            object : BannerCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
            }
        )

        Log.d("AdsInformation", "Call Admob Native")
        admobNative.loadNativeAds(
            activity,
            binding.adsNativePlaceHolder,
            getResString(R.string.admob_native_ids),
            rcvNativeAd,
            false,
            internetManager.isInternetConnected,
            NativeType.BANNER,
            object : NativeCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
            }
        )
    }

    override fun onPause() {
        admobBanner.bannerOnPause()
        super.onPause()
    }

    override fun onResume() {
        admobBanner.bannerOnResume()
        super.onResume()
    }

    override fun onDestroy() {
        admobBanner.bannerOnDestroy()
        super.onDestroy()
    }


}