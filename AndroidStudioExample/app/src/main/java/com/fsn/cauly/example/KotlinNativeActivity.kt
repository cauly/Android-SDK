package com.fsn.cauly.example

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fsn.cauly.CaulyMediaView
import com.fsn.cauly.CaulyNativeAd
import com.fsn.cauly.CaulyNativeAdInfoBuilder
import com.fsn.cauly.CaulyNativeAdListener
import com.fsn.cauly.CaulyNativeAdLoader
import com.fsn.cauly.CaulyNativeAdView
import com.fsn.cauly.CaulyNativeTemplateType
import com.fsn.cauly.CaulyNativeTemplateView

/**
 * 네이티브 V2 테스트 (Kotlin)
 *
 * 1. CaulyNativeAdLoader 로 광고를 요청한다.
 * 2. onAdLoaded() 로 받은 CaulyNativeAd 를 화면에 연결한다.
 *    - 템플릿: CaulyNativeTemplateView.setNativeAd() 만 호출하면 SDK 가 배치·노출·클릭을 처리한다.
 *    - 커스텀 레이아웃: 매체 레이아웃의 뷰를 CaulyNativeAdView 에 등록한 뒤 setNativeAd() 를 호출한다.
 * 3. 광고 교체·화면 종료 시 loader.cancel(), 뷰와 광고의 destroy() 로 정리한다.
 */
class KotlinNativeActivity : AppCompatActivity() {

    private lateinit var displayGroup: RadioGroup
    private lateinit var container: FrameLayout
    private lateinit var logView: TextView

    private var loader: CaulyNativeAdLoader? = null
    private var currentAd: CaulyNativeAd? = null
    private var currentAdView: CaulyNativeAdView? = null
    private var currentTemplateView: CaulyNativeTemplateView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native)

        displayGroup = findViewById(R.id.radio_display)
        container = findViewById(R.id.native_ad_container)
        logView = findViewById(R.id.text_native_log)
        findViewById<Button>(R.id.btn_request_native).setOnClickListener { requestAd() }
    }

    private fun requestAd() {
        val adInfo = CaulyNativeAdInfoBuilder(APP_CODE).build()

        loader?.cancel()
        loader = CaulyNativeAdLoader(this).apply {
            setAdInfo(adInfo)
            setAdListener(object : CaulyNativeAdListener() {
                override fun onAdLoaded(ad: CaulyNativeAd) {
                    if (isFinishing) {
                        ad.destroy()
                        return
                    }
                    log("onAdLoaded chargeable=${ad.isChargeable} headline=${ad.headline}")
                    showAd(ad)
                }

                override fun onAdFailedToLoad(errorCode: Int, errorMessage: String?) {
                    log("onAdFailedToLoad $errorCode $errorMessage")
                }

                override fun onAdImpression() {
                    log("onAdImpression")
                }

                override fun onAdClicked() {
                    log("onAdClicked")
                }
            })
        }
        log("request")
        loader?.request()
    }

    private fun showAd(ad: CaulyNativeAd) {
        releaseAd()
        currentAd = ad

        val displayId = displayGroup.checkedRadioButtonId
        if (displayId == R.id.radio_display_small || displayId == R.id.radio_display_medium) {
            // 템플릿: 레이아웃·뷰 등록 없이 setNativeAd() 만 호출한다.
            val type = if (displayId == R.id.radio_display_small) CaulyNativeTemplateType.SMALL else CaulyNativeTemplateType.MEDIUM
            val template = CaulyNativeTemplateView(this, type)
            template.setNativeAd(ad)
            container.addView(template)
            currentTemplateView = template
            return
        }

        // 커스텀 레이아웃: 매체 레이아웃의 뷰를 CaulyNativeAdView 에 등록한 뒤 광고를 연결한다.
        val adView = layoutInflater.inflate(R.layout.native_ad_layout, container, false) as CaulyNativeAdView
        val bodyView = adView.findViewById<TextView>(R.id.ad_body)
        val iconView = adView.findViewById<ImageView>(R.id.ad_icon)
        val mediaView = adView.findViewById<CaulyMediaView>(R.id.ad_media)

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline))
        adView.setCallToActionView(adView.findViewById(R.id.ad_cta))
        adView.setBodyView(bodyView)
        adView.setIconView(iconView)
        adView.setMediaView(mediaView)
        adView.setAdBadgeView(adView.findViewById(R.id.ad_badge))

        // 본문·아이콘·메인 이미지는 없을 수 있다.
        if (ad.body.isNullOrEmpty()) bodyView.visibility = View.GONE
        if (ad.icon == null) iconView.visibility = View.GONE
        if (ad.mediaContent == null) mediaView.visibility = View.GONE

        adView.setNativeAd(ad)
        container.addView(adView)
        currentAdView = adView
    }

    // 광고 교체·화면 종료 시 뷰와 광고를 모두 해제한다.
    private fun releaseAd() {
        currentTemplateView?.destroy()
        currentTemplateView = null
        currentAdView?.destroy()
        currentAdView = null
        container.removeAllViews()
        currentAd?.destroy()
        currentAd = null
    }

    private fun log(message: String) {
        Log.d(TAG, message)
        logView.text = "$message\n${logView.text}"
    }

    override fun onDestroy() {
        loader?.cancel()
        releaseAd()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "KotlinNativeActivity"
        private const val APP_CODE = "CAULY" // 발급받은 APP_CODE
    }
}
