package kr.co.cauly.sdk.android.mediation.sample.banners;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.fsn.cauly.CaulyAdBannerView;
import com.fsn.cauly.CaulyAdBannerViewListener;
import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.mediation.MediationAdLoadCallback;
import com.google.android.gms.ads.mediation.MediationBannerAd;
import com.google.android.gms.ads.mediation.MediationBannerAdCallback;
import com.google.android.gms.ads.mediation.MediationBannerAdConfiguration;
import com.google.android.gms.ads.mediation.MediationConfiguration;

import kr.co.cauly.sdk.android.mediation.sample.Config;
import kr.co.cauly.sdk.android.mediation.sample.R;

public class CaulyBannerLoader implements CaulyAdBannerViewListener, MediationBannerAd {
    final static String TAG = CaulyBannerLoader.class.getSimpleName();
    private final MediationBannerAdConfiguration mediationBannerAdConfiguration;
    private final MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback> mediationAdLoadCallback;
    private MediationBannerAdCallback bannerAdCallback;

    private CaulyAdBannerView adView = null;
    private Activity bannerActivity = null;
    private ViewGroup layout = null;

    /** Error raised when the custom event adapter cannot obtain the ad unit id. */
    public static final int ERROR_NO_AD_UNIT_ID = 101;
    /** Error raised when the custom event adapter cannot obtain the activity context. */
    public static final int ERROR_NO_ACTIVITY_CONTEXT = 103;
    public static final String SAMPLE_SDK_DOMAIN = "kr.co.cauly.sdk.android";
    public static final String CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent";

    public CaulyBannerLoader(MediationBannerAdConfiguration mediationBannerAdConfiguration, MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback> mediationAdLoadCallback) {
        this.mediationBannerAdConfiguration = mediationBannerAdConfiguration;
        this.mediationAdLoadCallback = mediationAdLoadCallback;
    }

    /** Loads a banner ad from the third party ad network. */
    public void loadAd() {
        Log.i(TAG, "Admob Mediation : Cauly Banner loadBannerAd.");
        String serverParameter =  mediationBannerAdConfiguration.getServerParameters().getString(MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(new AdError(ERROR_NO_AD_UNIT_ID, "Ad unit id is empty", CUSTOM_EVENT_ERROR_DOMAIN));
            return;
        }
        Log.d(TAG, "Received server parameter.");

        Context context = mediationBannerAdConfiguration.getContext();

        this.bannerActivity = (Activity) context;
        if (adView != null) {
            adView.destroy();
            adView = null;
            adView.removeAllViews();
            adView = null;
        }

        //AdView의 parent layout
        layout = bannerActivity.findViewById(R.id.adView);

        // 광고 정보 설정
        CaulyAdInfo adInfo = new CaulyAdInfoBuilder(serverParameter)
                .bannerHeight(CaulyAdInfoBuilder.FIXED)
                .banner_interval(false)
                .dynamicReloadInterval(false)
                .effect("None")
                .build();

        // Cauly 배너 광고 View 생성.
        adView = new CaulyAdBannerView(context);

        // 광고 정보 설정
        adView.setAdInfo(adInfo);
        adView.setAdViewListener(this);


        // 광고로드
        adView.load(context, layout);
    }

    @Override
    public void onReceiveAd(CaulyAdBannerView caulyAdBannerView, boolean isChargeableAd) {
        // 광고 요청과 동시에 add된 광고 view를 제거함
        layout.removeView(adView);

        if (adView != null) {
            Log.e(TAG, "onReceiveAd onAdLoaded-=-=-=-=-: ");
            adView.show();

            bannerAdCallback = mediationAdLoadCallback.onSuccess(this);
            bannerAdCallback.onAdOpened();
            bannerAdCallback.reportAdImpression();
        }
    }

    @Override
    public void onFailedToReceiveAd(CaulyAdBannerView caulyAdBannerView, int i, String s) {
        Log.e(TAG, "Failed to fetch the banner ad.");
        mediationAdLoadCallback.onFailure(new AdError(i, s, SAMPLE_SDK_DOMAIN));
        layout.removeView(adView);
    }

    @Override
    public void onShowLandingScreen(CaulyAdBannerView caulyAdBannerView) {
        Log.e("cauly", "Admob Mediation : Cauly Banner onShowLandingScreen");
        bannerAdCallback.onAdLeftApplication();
        bannerAdCallback.reportAdClicked();
    }

    @Override
    public void onCloseLandingScreen(CaulyAdBannerView caulyAdBannerView) {
        Log.e("cauly", "Admob Mediation : Cauly Banner onCloseLandingScreen");
    }

    @Override
    public void onTimeout(CaulyAdBannerView caulyAdBannerView, String s) {

    }

    @NonNull
    @Override
    public View getView() {
        return adView;
    }
}
