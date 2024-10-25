package kr.co.cauly.sdk.android.mediation.sample.natives

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.mediation.*
import com.kakao.adfit.ads.na.AdFitAdInfoIconPosition
import com.kakao.adfit.ads.na.AdFitNativeAdBinder
import com.kakao.adfit.ads.na.AdFitNativeAdLayout
import com.kakao.adfit.ads.na.AdFitNativeAdLoader
import com.kakao.adfit.ads.na.AdFitNativeAdRequest
import com.kakao.adfit.ads.na.AdFitVideoAutoPlayPolicy
import kr.co.cauly.sdk.android.mediation.sample.databinding.ItemNativeAdBinding

class AdFitNativeLoader(
    /** Configuration for requesting the native ad from the third-party network. */
    private val mediationNativeAdConfiguration: MediationNativeAdConfiguration,
    /** Callback that fires on loading success or failure. */
    private var mediationAdLoadCallback: MediationAdLoadCallback<NativeAdMapper, MediationNativeAdCallback>
) {
    /** Callback for native ad events. */
    private var nativeAdCallback: MediationNativeAdCallback? = null
    private var nativeAdBinder: AdFitNativeAdBinder? = null


    /** Loads the interstitial ad from the third-party ad network.  */
    fun loadAd() {
        // All custom events have a server parameter named "parameter" that returns
        // back the parameter entered into the UI when defining the custom event.
        Log.i(TAG, "Admob Mediation : AdFit Native loadAd")
        val serverParameter = mediationNativeAdConfiguration.serverParameters.getString(
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

        Log.d(TAG, "Received server parameter.")
        val context = mediationNativeAdConfiguration.context

        // AdFitNativeAdLoader 생성
        val nativeAdLoader: AdFitNativeAdLoader = AdFitNativeAdLoader.create(context, serverParameter.toString())

        // 네이티브 광고 요청 정보 설정
        val request = AdFitNativeAdRequest.Builder()
            .setAdInfoIconPosition(AdFitAdInfoIconPosition.RIGHT_TOP)
            .setVideoAutoPlayPolicy(AdFitVideoAutoPlayPolicy.WIFI_ONLY)
            .build()

        nativeAdLoader.loadAd(request, object : AdFitNativeAdLoader.AdLoadListener {
            override fun onAdLoaded(binder: AdFitNativeAdBinder) {
                val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val nativeAdView = ItemNativeAdBinding.inflate(inflater)


                val nativeAdLayout: AdFitNativeAdLayout =
                    AdFitNativeAdLayout.Builder(nativeAdView.containerView)
                        .setTitleView(nativeAdView.titleTextView)
                        .setBodyView(nativeAdView.bodyTextView)
                        .setProfileIconView(nativeAdView.profileIconView)
                        .setProfileNameView(nativeAdView.profileNameTextView)
                        .setMediaView(nativeAdView.mediaView)
                        .setCallToActionButton(nativeAdView.callToActionButton)
                        .build()

                nativeAdBinder = binder
                binder.bind(nativeAdLayout)

                val unifiedNativeAdMapper = SampleUnifiedNativeAdMapper(context, nativeAdView)
                nativeAdCallback = mediationAdLoadCallback.onSuccess(unifiedNativeAdMapper)
                nativeAdCallback!!.reportAdImpression()
            }

            override fun onAdLoadError(errorCode: Int) {
                mediationAdLoadCallback.onFailure(AdError(errorCode, "AdFit native ad load failed", SAMPLE_SDK_DOMAIN))
            }
        })
    }

    companion object {
        val TAG = AdFitNativeLoader::class.java.simpleName

        /** Error raised when the custom event adapter cannot obtain the ad unit id.  */
        const val ERROR_NO_AD_UNIT_ID = 101

        /** Error raised when the custom event adapter cannot obtain the activity context.  */
        const val SAMPLE_SDK_DOMAIN = "kr.co.cauly.sdk.android"
        const val CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent"
    }
}