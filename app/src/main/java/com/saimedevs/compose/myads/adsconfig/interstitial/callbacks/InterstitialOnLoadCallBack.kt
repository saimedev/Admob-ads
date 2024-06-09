package com.saimedevs.compose.myads.adsconfig.interstitial.callbacks


interface InterstitialOnLoadCallBack {
    fun onAdFailedToLoad(adError:String){}
    fun onAdLoaded(){}
    fun onPreloaded(){}
}