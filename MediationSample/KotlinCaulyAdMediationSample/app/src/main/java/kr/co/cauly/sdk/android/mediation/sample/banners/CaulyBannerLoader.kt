package kr.co.cauly.sdk.android.mediation.sample.banners

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.RelativeLayout
import com.fsn.cauly.CaulyAdBannerView
import com.fsn.cauly.CaulyAdBannerViewListener
import com.fsn.cauly.CaulyAdInfoBuilder
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.mediation.*
import kr.co.cauly.sdk.android.mediation.sample.R

class CaulyBannerLoader(
    private val mediationBannerAdConfiguration: MediationBannerAdConfiguration,
    private val mediationAdLoadCallback: MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback>
) :
    CaulyAdBannerViewListener, MediationBannerAd {
    private var bannerAdCallback: MediationBannerAdCallback? = null
    private var adView: CaulyAdBannerView? = null
    private var bannerActivity: Activity? = null

    /** Loads a banner ad from the third party ad network.  */
    fun loadAd() {
        Log.i(TAG, "Admob Mediation : Cauly Banner loadBannerAd.")
        //아래가 미디에이션 서버로부터 파라미터 설정한 값 받아오는 부분
        val serverParameter =
            mediationBannerAdConfiguration.serverParameters.getString(MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD)

        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(
                AdError(
                    ERROR_NO_AD_UNIT_ID,
                    "Ad unit id is empty",
                    CUSTOM_EVENT_ERROR_DOMAIN
                )
            )
            return
        }
        Log.d(TAG, "Received server parameter.$serverParameter")
        val context = mediationBannerAdConfiguration.context
        bannerActivity = context as Activity

        if (adView != null) {
            adView!!.destroy()
            adView!!.removeAllViews()
            adView = null
        }

        //AdView의 parent layout
        layout = bannerActivity!!.findViewById(R.id.adView)

        // 광고 정보 설정
        val adInfo = CaulyAdInfoBuilder(serverParameter)
            .bannerHeight(CaulyAdInfoBuilder.FIXED)
            .banner_interval(false)
            .dynamicReloadInterval(false)
            .effect("None")
            .build()

        // Cauly 배너 광고 View 생성.
        adView = CaulyAdBannerView(context)

        // 광고 정보 설정
        adView!!.setAdInfo(adInfo)
        adView!!.setAdViewListener(this)

        // 광고로드
        adView!!.load(context, layout)
    }

    override fun onReceiveAd(caulyAdBannerView: CaulyAdBannerView, isChargeableAd: Boolean) {
        // 광고 요청과 동시에 add된 광고 view를 제거함
        layout!!.removeView(adView)
        if (adView != null) {
            Log.e(TAG, "onReceiveAd onAdLoaded-=-=-=-=-: ")
            adView!!.show()
            bannerAdCallback = mediationAdLoadCallback.onSuccess(this)
            bannerAdCallback!!.onAdOpened()
            bannerAdCallback!!.reportAdImpression()
        }
    }

    override fun onFailedToReceiveAd(caulyAdBannerView: CaulyAdBannerView, i: Int, s: String) {
        Log.e(TAG, "Failed to fetch the banner ad.")
        mediationAdLoadCallback.onFailure(AdError(i, s, SAMPLE_SDK_DOMAIN))
        layout!!.removeView(adView)
    }

    override fun onShowLandingScreen(caulyAdBannerView: CaulyAdBannerView) {
        Log.e("cauly", "Admob Mediation : Cauly Banner onShowLandingScreen")
        bannerAdCallback!!.onAdLeftApplication()
        bannerAdCallback!!.reportAdClicked()
    }

    override fun onCloseLandingScreen(caulyAdBannerView: CaulyAdBannerView) {
        Log.e("cauly", "Admob Mediation : Cauly Banner onCloseLandingScreen")
    }

    override fun onTimeout(caulyAdBannerView: CaulyAdBannerView, s: String) {}


    override fun getView(): View {
        return adView!!
    }

    companion object {
        val TAG = CaulyBannerLoader::class.java.simpleName
        private var layout: ViewGroup? = null

        /** Error raised when the custom event adapter cannot obtain the ad unit id.  */
        const val ERROR_NO_AD_UNIT_ID = 101

        /** Error raised when the custom event adapter cannot obtain the activity context.  */
        const val ERROR_NO_ACTIVITY_CONTEXT = 103
        const val SAMPLE_SDK_DOMAIN = "kr.co.cauly.sdk.android"
        const val CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent"
        fun init(viewGroup: ViewGroup?) {
            layout = viewGroup
        }
    }
}