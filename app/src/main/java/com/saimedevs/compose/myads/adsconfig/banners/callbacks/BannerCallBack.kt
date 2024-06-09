package com.saimedevs.compose.myads.adsconfig.banners.callbacks


interface BannerCallBack {
    fun onAdFailedToLoad(adError:String){}
    fun onAdLoaded(){}
    fun onAdImpression(){}
    fun onAdClicked(){}
    fun onAdClosed(){}
    fun onAdOpened(){}
}