package com.fsn.cauly.example

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fsn.cauly.CaulyAdBannerView
import com.fsn.cauly.CaulyAdBannerViewListener
import com.fsn.cauly.CaulyAdInfoBuilder
import com.fsn.cauly.Logger

/**
 * CaulyAdBannerView Preload 샘플 (Load / Show 분리)
 * - Load: caulyAdBannerView.load(context, parent)
 * - Show: caulyAdBannerView.show()
 */
class KotlinPreloadBannerActivity : AppCompatActivity(), CaulyAdBannerViewListener {

    companion object {
        private const val TAG = "CaulyExample"
        private const val APP_CODE = "CAULY"
    }

    private lateinit var adViewRoot: RelativeLayout
    private lateinit var btnLoad: Button
    private lateinit var btnShow: Button

    private var bannerView: CaulyAdBannerView? = null
    private var isAdLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_preload_banner)

        Logger.setLogLevel(Logger.LogLevel.Debug)

        adViewRoot = findViewById(R.id.adViewRoot)
        btnLoad = findViewById(R.id.btnLoad)
        btnShow = findViewById(R.id.btnShow)
        btnShow.isEnabled = false
    }

    /**
     * Load 버튼 처리 (XML: android:onClick="onLoadBanner")
     */
    fun onLoadBanner(button: View?) {
        btnShow.isEnabled = false
        isAdLoaded = false

        cleanupBanner()

        val adInfo = CaulyAdInfoBuilder(APP_CODE)
            .bannerHeight(CaulyAdInfoBuilder.FIXED)
            .effect("None")
            .build()

        bannerView = CaulyAdBannerView(this).apply {
            setAdInfo(adInfo)
            setShowPreExpandableAd(true)
            setAdViewListener(this@KotlinPreloadBannerActivity)
        }

        // load()의 parent로 실제 화면 컨테이너를 넘김 (show()에서 parent.addView 처리)
        bannerView?.load(application, adViewRoot)
        Log.d(TAG, "Kotlin banner load() called")
    }

    /**
     * Show 버튼 처리 (XML: android:onClick="onShowBanner")
     */
    fun onShowBanner(button: View?) {
        val view = bannerView
        if (view == null || !isAdLoaded) {
            Toast.makeText(this, "먼저 Load를 완료해주세요.", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "show skipped: not loaded yet")
            return
        }
        view.show()
        Log.d(TAG, "banner show() called")
        btnShow.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanupBanner()
    }

    private fun cleanupBanner() {
        val view = bannerView ?: return
        try {
            adViewRoot.removeView(view)
        } catch (_: Throwable) {
        }
        try {
            view.destroy()
        } catch (_: Throwable) {
        } finally {
            bannerView = null
            isAdLoaded = false
            btnShow.isEnabled = false
        }
    }

    // CaulyAdBannerViewListener
    // 광고 동작에 대해 별도 처리가 필요 없는 경우,
    // Activity의 "implements CaulyAdBannerViewListener" 부분 제거하고 생략 가능.
    override fun onReceiveAd(adView: CaulyAdBannerView, isChargeableAd: Boolean) {
        Log.d(TAG, "banner AD received.")
        isAdLoaded = true
        btnShow.isEnabled = true
    }

    override fun onFailedToReceiveAd(adView: CaulyAdBannerView, errorCode: Int, errorMsg: String) {
        Log.d(TAG, "failed to receive banner AD. code=$errorCode, msg=$errorMsg")
        isAdLoaded = false
        btnShow.isEnabled = false
    }

    override fun onShowLandingScreen(adView: CaulyAdBannerView) {
        Log.d(TAG, "banner AD landing screen opened.")
    }

    override fun onCloseLandingScreen(adView: CaulyAdBannerView) {
        Log.d(TAG, "banner AD landing screen closed.")
    }

    override fun onClickAd(adView: CaulyAdBannerView) {
        Log.d(TAG, "banner AD clicked.")
    }

    override fun onTimeout(adView: CaulyAdBannerView, msg: String) {
        Log.d(TAG, "banner AD timeout. msg=$msg")
        isAdLoaded = false
        btnShow.isEnabled = false
    }
}

