package com.saimedevs.compose.myads.ui.fragments.splash

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.saimedevs.compose.myads.Constant.rcvInterAd
import com.saimedevs.compose.myads.InternetManager
import com.saimedevs.compose.myads.R
import com.saimedevs.compose.myads.adsconfig.interstitial.AdmobInterstitial
import com.saimedevs.compose.myads.adsconfig.interstitial.callbacks.InterstitialOnLoadCallBack
import com.saimedevs.compose.myads.adsconfig.interstitial.callbacks.InterstitialOnShowCallBack
import com.saimedevs.compose.myads.databinding.FragmentSplashBinding
import com.saimedevs.compose.myads.ui.activities.SplashActivity
import com.saimedevs.compose.myads.ui.fragments.base.BaseFragment

 
class FragmentSplash : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    private val admobInterstitial by lazy { AdmobInterstitial() }

    private val mHandler = Handler(Looper.getMainLooper())
    private val adsRunner = Runnable { checkAdvertisement() }
    private var isInterLoadOrFailed = false
    private var mCounter: Int = 0
    private lateinit var internetManager: InternetManager

    private var startTime = 0L

    override fun onViewCreatedOneTime() {
        internetManager = InternetManager(requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

        loadAds()    }

    override fun onViewCreatedEverytime() {}


    private fun loadAds() {
        if (isAdded) {
            startTime = System.currentTimeMillis()
            when (rcvInterAd) {
                0 -> {
                    isInterLoadOrFailed = true
                }
                1 -> {
                    Log.d("AdsInformation", "Call Admob Splash Interstitial")
                    admobInterstitial.loadInterstitialAd(
                        activity,
                        getResString(R.string.admob_inter_ids),
                        rcvInterAd,
                        false,
                        internetManager.isInternetConnected,
                        object : InterstitialOnLoadCallBack {
                            override fun onAdFailedToLoad(adError: String) {
                                isInterLoadOrFailed = true
                            }

                            override fun onAdLoaded() {
                                isInterLoadOrFailed = true
                                val endTime = System.currentTimeMillis()
                                val loadingTime:Int = ((endTime - startTime)/1000).toInt()
                                Log.d("AdsInformation", "InterLoadingTime: ${loadingTime}s")
                            }

                            override fun onPreloaded() {
                                isInterLoadOrFailed = true
                            }

                        })
                }
                else -> {
                    isInterLoadOrFailed = true
                }
            }
        }
    }

    private fun checkAdvertisement() {
        if (internetManager.isInternetConnected) {
            if (mCounter < 16) {
                try {
                    mCounter++
                    if (isInterLoadOrFailed) {
                        moveNext()
                        mHandler.removeCallbacks { adsRunner }
                    } else {
                        mHandler.removeCallbacks { adsRunner }
                        mHandler.postDelayed(
                            adsRunner,
                            (1000)
                        )
                    }

                } catch (e: Exception) {
                    Log.e("checkAdvertisementTAG", "${e.message}")
                }
            } else {
                moveNext()
                mHandler.removeCallbacks { adsRunner }
            }
        } else {
            moveNext(3000)
        }

    }

    private fun  moveNext(timeMilli:Long = 500) {
        withDelay(timeMilli) {
            lifecycleScope.launchWhenResumed {
                if (isAdded){
                    (activity as SplashActivity).nextActivity()
                    admobInterstitial.showInterstitialAd(activity,object :
                        InterstitialOnShowCallBack {
                        override fun onAdDismissedFullScreenContent() {}
                        override fun onAdFailedToShowFullScreenContent() {}
                        override fun onAdShowedFullScreenContent() {}
                        override fun onAdImpression() {}

                    })
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopHandler()
    }

    override fun onResume() {
        super.onResume()
        resumeHandler()
    }

    private fun stopHandler() {
        mHandler.removeCallbacks(adsRunner)
    }

    private fun resumeHandler() {
        mHandler.post(adsRunner)
    }

    override fun navIconBackPressed() {}

    override fun onBackPressed() {}

}