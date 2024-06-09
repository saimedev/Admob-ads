package com.saimedevs.compose.myads.adsconfig.interstitial.callbacks


interface InterstitialOnShowCallBack {
    fun onAdDismissedFullScreenContent(){}
    fun onAdFailedToShowFullScreenContent(){}
    fun onAdShowedFullScreenContent(){}
    fun onAdImpression(){}
}