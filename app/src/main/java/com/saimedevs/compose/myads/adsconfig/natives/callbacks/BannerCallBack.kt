package com.saimedevs.compose.myads.adsconfig.natives.callbacks


interface NativeCallBack {
    fun onAdFailedToLoad(adError:String){}
    fun onAdLoaded(){}
    fun onAdImpression(){}
    fun onPreloaded(){}
}