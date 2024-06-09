package com.saimedevs.compose.myads.adsconfig.rewarded.callbacks


interface RewardedOnShowCallBack {
    fun onAdDismissedFullScreenContent(){}
    fun onAdFailedToShowFullScreenContent(){}
    fun onAdImpression(){}
    fun onAdShowedFullScreenContent(){}
    fun onUserEarnedReward(){}
}