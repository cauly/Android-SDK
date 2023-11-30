package kr.co.cauly.sdk.android.mediation.sample.interstitials;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.mediation.MediationAdLoadCallback;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.ads.mediation.MediationInterstitialAd;
import com.google.android.gms.ads.mediation.MediationInterstitialAdCallback;
import com.google.android.gms.ads.mediation.MediationInterstitialAdConfiguration;

import kr.co.cauly.sdk.android.mediation.sample.Config;

public class CaulyInterstitialLoader implements CaulyInterstitialAdListener, MediationInterstitialAd {
    final static String TAG = CaulyInterstitialLoader.class.getSimpleName();
    /** Configuration for requesting the interstitial ad from the third-party network. */
    private final MediationInterstitialAdConfiguration mediationInterstitialAdConfiguration;
    /** Callback that fires on loading success or failure. */
    private final MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback> mediationAdLoadCallback;
    /** Callback for interstitial ad events. */
    private MediationInterstitialAdCallback interstitialAdCallback;

    CaulyInterstitialAd caulyInterstitialAd = null;
    private Activity interstitialActivity = null;

    /** Error raised when the custom event adapter cannot obtain the ad unit id. */
    public static final int ERROR_NO_AD_UNIT_ID = 101;
    /** Error raised when the custom event adapter cannot obtain the activity context. */
    public static final String SAMPLE_SDK_DOMAIN = "kr.co.cauly.sdk.android";
    public static final String CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent";

    /** Constructor. */
    public CaulyInterstitialLoader(MediationInterstitialAdConfiguration mediationInterstitialAdConfiguration, MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback> mediationAdLoadCallback) {
        this.mediationInterstitialAdConfiguration = mediationInterstitialAdConfiguration;
        this.mediationAdLoadCallback = mediationAdLoadCallback;
    }

    /** Loads the interstitial ad from the third-party ad network. */
    public void loadAd() {
        // All custom events have a server parameter named "parameter" that returns
        // back the parameter entered into the UI when defining the custom event.
        Log.i(TAG, "Admob Mediation : Cauly Interstitial loadInterstitialAd.");

        String serverParameter =  mediationInterstitialAdConfiguration.getServerParameters().getString(MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(new AdError(ERROR_NO_AD_UNIT_ID, "Ad unit id is empty", CUSTOM_EVENT_ERROR_DOMAIN));
            return;
        }
        Log.d(TAG, "Received server parameter.");

        Context context = mediationInterstitialAdConfiguration.getContext();
        this.interstitialActivity = (Activity) context;

        CaulyAdInfo adInfo = new CaulyAdInfoBuilder(serverParameter)
                .build();

        // 전면 광고 생성
        CaulyInterstitialAd interstial = new CaulyInterstitialAd();
        interstial.setAdInfo(adInfo);
        interstial.setInterstialAdListener(this);

        // 광고 요청.
        interstial.requestInterstitialAd(this.interstitialActivity);
    }


    @Override
    public void showAd(@NonNull Context context) {
        caulyInterstitialAd.show();
        interstitialAdCallback.reportAdImpression();
        interstitialAdCallback.onAdOpened();
    }

    /** CaulyInterstitialAdListener **/
    @Override
    public void onReceiveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd, boolean isChargeableAd) {
        // 광고 수신 성공한 경우 호출됨.
        // 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
        if (isChargeableAd == false) {
            Log.d("CaulyExample", "free interstitial AD received.");
        } else {
            Log.d("CaulyExample", "normal interstitial AD received.");
        }

        this.caulyInterstitialAd = caulyInterstitialAd;
        interstitialAdCallback = mediationAdLoadCallback.onSuccess(this);
        interstitialAdCallback.reportAdImpression();
    }

    @Override
    public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd, int i, String s) {
        Log.e(TAG, "Failed to fetch the interstitial ad.");
        mediationAdLoadCallback.onFailure(new AdError(i, s, SAMPLE_SDK_DOMAIN));
    }

    @Override
    public void onClosedInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {
        // 전면 광고가 닫힌 경우 호출됨.
        Log.d("CaulyExample", "interstitial AD closed.");
        interstitialAdCallback.onAdClosed();
    }

    @Override
    public void onLeaveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {

    }
}
