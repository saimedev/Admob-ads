package com.saimedevs.compose.myads.ui.fragments.sample

import android.content.Context
import android.net.ConnectivityManager
import com.saimedevs.compose.myads.Constant.rcvBannerAd
import com.saimedevs.compose.myads.InternetManager
import com.saimedevs.compose.myads.R
import com.saimedevs.compose.myads.adsconfig.banners.AdmobBanner
import com.saimedevs.compose.myads.adsconfig.banners.callbacks.BannerCallBack
import com.saimedevs.compose.myads.adsconfig.banners.enums.BannerType
import com.saimedevs.compose.myads.databinding.FragmentBannerBinding
import com.saimedevs.compose.myads.ui.fragments.base.BaseFragment

 
class FragmentBanner : BaseFragment<FragmentBannerBinding>(R.layout.fragment_banner) {

    private val admobBanner by lazy { AdmobBanner() }
    private var isCollapsibleOpen = false
    private var isBackPressed = false
    private lateinit var internetManager: InternetManager

    override fun onViewCreatedOneTime() {
        loadAds()
    }

    override fun onViewCreatedEverytime() {

       // initObserver()
    }

  /*  private fun initObserver(){
        adsObserver.observe(this){
            if (true){
                onBack()
            }
        }
    }*/

    override fun navIconBackPressed() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (isAdded){
            try {
                if (!isBackPressed){
                    isBackPressed = true
                    if (isCollapsibleOpen){
                        admobBanner.bannerOnDestroy()
                        binding.adsBannerPlaceHolder.removeAllViews()
                    }else{
                        onBack()
                    }
                }
            }catch (ex:Exception){
                isBackPressed = false
            }
        }
    }

    private fun onBack(){
        popFrom(R.id.fragmentBanner)
    }

    private fun loadAds(){
        internetManager = InternetManager(requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        admobBanner.loadBannerAds(
            activity,
            binding.adsBannerPlaceHolder,
            getResString(R.string.admob_banner_ids),
            rcvBannerAd,
           false,
            internetManager.isInternetConnected,
            BannerType.COLLAPSIBLE_BOTTOM,
            object : BannerCallBack {
                override fun onAdClosed() {
                    isCollapsibleOpen = false

                    if (isBackPressed){

                    }
                }

                override fun onAdOpened() {
                    isCollapsibleOpen = true
                }


            }
        )
    }

    override fun onPause() {
        admobBanner.bannerOnPause()
        super.onPause()
    }

    override fun onResume() {
        admobBanner.bannerOnResume()
        super.onResume()
    }

    override fun onDestroy() {
        admobBanner.bannerOnDestroy()
        super.onDestroy()
    }
}