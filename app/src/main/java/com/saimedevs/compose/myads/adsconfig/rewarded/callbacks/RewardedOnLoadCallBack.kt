package com.saimedevs.compose.myads.adsconfig.rewarded.callbacks


interface RewardedOnLoadCallBack {
    fun onAdFailedToLoad(adError:String){}
    fun onAdLoaded(){}
    fun onPreloaded(){}
}