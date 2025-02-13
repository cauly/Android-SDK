package com.fsn.cauly.example

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.fsn.cauly.CaulyAdInfo
import com.fsn.cauly.CaulyNativeAdInfoBuilder
import com.fsn.cauly.CaulyNativeAdView
import com.fsn.cauly.CaulyNativeAdViewListener

class KotlinNativeViewActivity : AppCompatActivity(), CaulyNativeAdViewListener {
    var APP_CODE = "CAULY" // your app code which you are assigned.
    var native_container: ViewGroup? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_native_view)
        native_container = findViewById<View>(R.id.native_container) as ViewGroup
        showNative()
    }

    // Request Native AD
    // 네이티브 애드에 보여질 디자인을 정의하고 세팅하는 작업을 수행한다. (icon, image, title, subtitle, description ...)
    // CaulyNativeAdViewListener 를 등록하여 onReceiveNativeAd or onFailedToReceiveNativeAd 로 네이티브광고의 상태를 전달받는다.
    fun showNative() {
        val adInfo = CaulyNativeAdInfoBuilder(APP_CODE)
            .layoutID(R.layout.activity_native_view)
            .iconImageID(R.id.icon)
            .titleID(R.id.title)
            .subtitleID(R.id.subtitle)
            .sponsorPosition(R.id.sponsor, CaulyAdInfo.Direction.CENTER)
            .build()
        val nativeAd = CaulyNativeAdView(this)
        nativeAd.adInfo = adInfo
        nativeAd.setAdViewListener(this)
        nativeAd.request()
    }

    // 네이티브애드가 없거나, 네트웍상의 이유로 정상적인 수신이 불가능 할 경우 호출이 된다.
    override fun onFailedToReceiveNativeAd(
        adView: CaulyNativeAdView,
        errorCode: Int,
        errorMsg: String
    ) {
    }

    // 네이티브애드가 정상적으로 수신되었을 떄, 호출된다.
    override fun onReceiveNativeAd(adView: CaulyNativeAdView, isChargeableAd: Boolean) {
        adView.attachToView(native_container) //지정된 위치에 adView를 붙인다.
    }

    // 네이티브 애드가 클릭되었을 때, 호출된다.
    override fun onClickNativeAd(adView: CaulyNativeAdView) {
        Log.d("CaulyExample", "naive AD clicked.")
    }
}