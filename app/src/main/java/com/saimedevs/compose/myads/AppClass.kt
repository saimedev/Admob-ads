package com.saimedevs.compose.myads

import android.app.AppOpsManager
import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.saimedevs.compose.myads.adsconfig.appOpen.AdmobAppOpen
import com.saimedevs.compose.myads.adsconfig.appOpen.AppOpen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppClass:Application() {

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
             MobileAds.initialize(this@AppClass)

         }
       // AdmobAppOpen(this@AppClass)

        AppOpen(this@AppClass, getString(R.string.admob_app_open_ids))
    }
}