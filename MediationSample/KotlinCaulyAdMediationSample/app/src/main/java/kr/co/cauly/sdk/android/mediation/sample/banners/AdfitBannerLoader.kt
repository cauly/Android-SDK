package kr.co.cauly.sdk.android.mediation.sample.banners

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fsn.cauly.CaulyAdBannerView
import com.fsn.cauly.CaulyAdBannerViewListener
import com.fsn.cauly.CaulyAdInfoBuilder
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.mediation.*
import com.kakao.adfit.ads.AdListener
import com.kakao.adfit.ads.ba.BannerAdView
import com.kakao.adfit.ads.na.*
import kr.co.cauly.sdk.android.mediation.sample.R
import kr.co.cauly.sdk.android.mediation.sample.databinding.ItemNativeAdBinding
import kr.co.cauly.sdk.android.mediation.sample.natives.AdFitNativeLoader
import kr.co.cauly.sdk.android.mediation.sample.natives.SampleUnifiedNativeAdMapper

class AdfitBannerLoader(
    /** Configuration for requesting the native ad from the third-party network. */
    private val mediationBannerAdConfiguration: MediationBannerAdConfiguration,
    /** Callback that fires on loading success or failure. */
    private var mediationAdLoadCallback: MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback>
) {
    /** Callback for native ad events. */
    private var bannerAdCallback: MediationBannerAdCallback? = null
//    private var bannerAdBinder: AdFitBannerBinder? = null
    private var bannerActivity: Activity? = null
    private var adView : BannerAdView? = null;
    /** Loads the interstitial ad from the third-party ad network.  */
    fun loadAd() {
        // All custom events have a server parameter named "parameter" that returns
        // back the parameter entered into the UI when defining the custom event.
        Log.i(TAG, "Admob Mediation : AdFit Native loadAd")
        val serverParameter = mediationBannerAdConfiguration.serverParameters.getString(
            MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD)
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
        val context = mediationBannerAdConfiguration.context
        bannerActivity = context as Activity

        adView = bannerActivity!!.findViewById(R.id.adView)

        if (adView != null) {
            adView!!.destroy()
            adView!!.removeAllViews()
            adView = null
        }

        adView!!.setClientId("12321412") //FIXME 광고단위 ID 설정
        adView!!.setAdListener(object : AdListener {  // 광고 수신 리스너 설정

            override fun onAdLoaded() {
//                toast("Banner is loaded")
            }

            override fun onAdFailed(errorCode: Int) {
//                toast("Failed to load banner :: errorCode = $errorCode")
            }

            override fun onAdClicked() {
//                toast("Banner is clicked")
            }
        })
        adView!!.loadAd()  // 광고 요청
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