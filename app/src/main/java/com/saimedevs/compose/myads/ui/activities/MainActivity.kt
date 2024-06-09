package com.saimedevs.compose.myads.ui.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.saimedevs.compose.myads.Constant.rcvInterAd
import com.saimedevs.compose.myads.Constant.rcvRemoteCounter
import com.saimedevs.compose.myads.Constant.totalCount
import com.saimedevs.compose.myads.InternetManager
import com.saimedevs.compose.myads.R
import com.saimedevs.compose.myads.adsconfig.interstitial.AdmobInterstitial
import com.saimedevs.compose.myads.adsconfig.interstitial.callbacks.InterstitialOnLoadCallBack
import com.saimedevs.compose.myads.adsconfig.interstitial.callbacks.InterstitialOnShowCallBack
import com.saimedevs.compose.myads.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var internetManager: InternetManager

    private val admobInterstitial by lazy { AdmobInterstitial() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.materialToolbar)
        internetManager = InternetManager(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        initNavController()
    }


    private fun initNavController() {
        navController =
            (supportFragmentManager.findFragmentById(binding.navHostFragmentContainer.id) as NavHostFragment).navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.fragmentHome))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun checkCounter(){
        try {
            if (admobInterstitial.isInterstitialLoaded()){
                showInterstitialAd()
                totalCount += 1
            }else{
                if (totalCount >= rcvRemoteCounter) {
                    totalCount = 1
                    loadInterstitialAd()
                }else{
                    totalCount += 1
                }
            }
        }catch (e:Exception){
            Log.d("AdsInformation","${e.message}")
        }
    }


    fun loadInterstitialAd(){
        admobInterstitial.loadInterstitialAd(
            this,
            getString(R.string.admob_inter_ids),
            rcvInterAd,
            false,
            internetManager.isInternetConnected,
            object : InterstitialOnLoadCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onPreloaded() {}
            }
        )
    }

    fun showInterstitialAd(){
        admobInterstitial.showInterstitialAd(
            this,
            object : InterstitialOnShowCallBack {
                override fun onAdDismissedFullScreenContent() {}
                override fun onAdFailedToShowFullScreenContent() {}
                override fun onAdShowedFullScreenContent() {}
                override fun onAdImpression() {}
            }
        )
    }


    /**
     *  Call 'CleanMemory.clean()' to avoid memory leaks.
     *  This destroys all the resources
     */

    override fun onDestroy() {
        super.onDestroy()
    }
}