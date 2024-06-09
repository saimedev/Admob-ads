package com.saimedevs.compose.myads.ui.fragments.sample

import android.content.Context
import android.net.ConnectivityManager
import com.saimedevs.compose.myads.Constant.rcvNativeAd
import com.saimedevs.compose.myads.InternetManager
import com.saimedevs.compose.myads.R
import com.saimedevs.compose.myads.adsconfig.natives.AdmobNative
import com.saimedevs.compose.myads.adsconfig.natives.callbacks.NativeCallBack
import com.saimedevs.compose.myads.adsconfig.natives.enums.NativeType
import com.saimedevs.compose.myads.databinding.FragmentSampleBinding
import com.saimedevs.compose.myads.ui.fragments.base.BaseFragment

 
class FragmentSample : BaseFragment<FragmentSampleBinding>(R.layout.fragment_sample) {
    private lateinit var internetManager: InternetManager
    private val admobNative by lazy { AdmobNative() }

    override fun onViewCreatedOneTime() {
        internetManager = InternetManager(requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        loadAds()
    }

    override fun onViewCreatedEverytime() {}

    private fun loadAds() {
        admobNative.loadNativeAds(
            activity,
            binding.adsPlaceHolder,
            getResString(R.string.admob_native_ids),
            rcvNativeAd,
            false,
            internetManager.isInternetConnected,
            NativeType.LARGE,
            object : NativeCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
                override fun onPreloaded() {}
            }
        )
    }

    override fun navIconBackPressed() {
        onBackPressed()
    }

    override fun onBackPressed() {
        popFrom(R.id.fragmentSample)
    }
}