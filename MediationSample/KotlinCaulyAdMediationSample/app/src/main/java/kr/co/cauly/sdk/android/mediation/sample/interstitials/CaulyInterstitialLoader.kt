package kr.co.cauly.sdk.android.mediation.sample.interstitials

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.fsn.cauly.CaulyAdInfoBuilder
import com.fsn.cauly.CaulyInterstitialAd
import com.fsn.cauly.CaulyInterstitialAdListener
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.mediation.*

class CaulyInterstitialLoader(
    /** Configuration for requesting the interstitial ad from the third-party network.  */
    private val mediationInterstitialAdConfiguration: MediationInterstitialAdConfiguration,
    /** Callback that fires on loading success or failure.  */
    private var mediationAdLoadCallback: MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback>
) :
    CaulyInterstitialAdListener, MediationInterstitialAd {
    /** Callback for interstitial ad events.  */
    private var interstitialAdCallback: MediationInterstitialAdCallback? = null
    var caulyInterstitialAd: CaulyInterstitialAd? = null
    private var interstitialActivity: Activity? = null

    /** Constructor.  */
    init {
        mediationAdLoadCallback = mediationAdLoadCallback
    }

    /** Loads the interstitial ad from the third-party ad network.  */
    fun loadAd() {
        // All custom events have a server parameter named "parameter" that returns
        // back the parameter entered into the UI when defining the custom event.
        Log.i(TAG, "Admob Mediation : Cauly Interstitial loadInterstitialAd.")
        val serverParameter =
            mediationInterstitialAdConfiguration.serverParameters.getString(MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD)
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
        val context = mediationInterstitialAdConfiguration.context
        interstitialActivity = context as Activity
        val adInfo = CaulyAdInfoBuilder(serverParameter)
            .build()

        // 전면 광고 생성
        val interstial = CaulyInterstitialAd()
        interstial.setAdInfo(adInfo)
        interstial.setInterstialAdListener(this)

        // 광고 요청.
        interstial.requestInterstitialAd(interstitialActivity)
    }

    override fun showAd(context: Context) {
        caulyInterstitialAd!!.show()
        interstitialAdCallback!!.reportAdImpression()
        interstitialAdCallback!!.onAdOpened()
    }

    /** CaulyInterstitialAdListener  */
    override fun onReceiveInterstitialAd(
        caulyInterstitialAd: CaulyInterstitialAd,
        isChargeableAd: Boolean
    ) {
        // 광고 수신 성공한 경우 호출됨.
        // 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
        if (isChargeableAd == false) {
            Log.d("CaulyExample", "free interstitial AD received.")
        } else {
            Log.d("CaulyExample", "normal interstitial AD received.")
        }
        this.caulyInterstitialAd = caulyInterstitialAd
        interstitialAdCallback = mediationAdLoadCallback.onSuccess(this)
        interstitialAdCallback!!.reportAdImpression()
    }

    override fun onFailedToReceiveInterstitialAd(caulyInterstitialAd: CaulyInterstitialAd, i: Int, s: String) {
        Log.e(TAG, "Failed to fetch the interstitial ad.")
        mediationAdLoadCallback.onFailure(AdError(i, s, SAMPLE_SDK_DOMAIN))
    }

    override fun onClosedInterstitialAd(caulyInterstitialAd: CaulyInterstitialAd) {
        // 전면 광고가 닫힌 경우 호출됨.
        Log.d("CaulyExample", "interstitial AD closed.")
        interstitialAdCallback!!.onAdClosed()
    }

    override fun onLeaveInterstitialAd(caulyInterstitialAd: CaulyInterstitialAd) {}

    companion object {
        val TAG = CaulyInterstitialLoader::class.java.simpleName

        /** Error raised when the custom event adapter cannot obtain the ad unit id.  */
        const val ERROR_NO_AD_UNIT_ID = 101

        /** Error raised when the custom event adapter cannot obtain the activity context.  */
        const val SAMPLE_SDK_DOMAIN = "kr.co.cauly.sdk.android"
        const val CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent"
    }
}
