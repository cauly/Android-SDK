package kr.co.cauly.sdk.android.mediation.sample.banner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.fsn.cauly.CaulyAdBannerView;
import com.fsn.cauly.CaulyAdBannerViewListener;
import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;

import kr.co.cauly.sdk.android.mediation.sample.R;


public class CaulyBanner implements CustomEventBanner, CaulyAdBannerViewListener {
    final static String TAG = CaulyBanner.class.getSimpleName();
    private CustomEventBannerListener bannerListener;
    private CaulyAdBannerView adView = null;
    private Activity bannerActivity = null;
    private ViewGroup layout = null;
    private boolean k = false;

    @Override
    public void requestBannerAd(Context context, CustomEventBannerListener customEventBannerListener, String s, AdSize adSize, MediationAdRequest mediationAdRequest, Bundle bundle) {
        if (!(context instanceof Activity)) {
            Log.e(TAG, "Context not an Activity. Returning error!");
            customEventBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
        } else {

            try {
                Log.e(TAG, "Admob Mediation : Cauly Banner requestBannerAd");
                this.bannerListener = customEventBannerListener;
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
                CaulyAdInfo ai = new CaulyAdInfoBuilder("RNPLpEkQ") // CAULY app code
                        .bannerHeight(CaulyAdInfoBuilder.FIXED)
                        .banner_interval(false)
                        .dynamicReloadInterval(false)
                        .effect("None")
                        .build();

                // Cauly 배너 광고 View 생성.
                adView = new CaulyAdBannerView(context);

                // 광고 정보 설정
                adView.setAdInfo(ai);
                adView.setAdViewListener(this);


                // 광고로드
                adView.load(context, layout);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "requestBannerAd Exception-=-=-=-=-: ");
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy-=-=-=-=-: ");
        if (adView != null) {
            Log.e("cauly banner", "onDestroy222-=-=-=-=-: ");
            adView.destroy();
            adView = null;

        }
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause-=-=-=-=-: ");

    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume-=-=-=-=-: ");

    }


    @Override
    public void onReceiveAd(CaulyAdBannerView caulyAdBannerView, boolean isChargeableAd) {
        // 광고 요청과 동시에 add된 광고 view를 제거함
        try {
            layout.removeView(adView);
            try {
                if (adView != null) {
                    Log.e(TAG, "onReceiveAd onAdLoaded-=-=-=-=-: ");
                    adView.show();
                    bannerListener.onAdLoaded(adView);
                }
            } catch (Exception e) {
                bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
                Log.e(TAG, "onReceiveAd Exception ERROR_CODE_NO_FILL-=-=-=-=-: ");

            }

        } catch (Exception e) {
            Log.e(TAG, "onReceiveAd Exception-=-=-=-=-: ");
            bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);

        }
    }

    @Override
    public void onFailedToReceiveAd(CaulyAdBannerView caulyAdBannerView, int i, String s) {
        Log.e(TAG, "onFailedToReceiveAd error-=-=-=-=-: " + i + "  String " + s);
        if (i == 100) {
            bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
        } else if (i == 200) {
            bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
        } else if (i == 400) {
            bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
        } else if (i == 500) {
            bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
        } else if (i == -100) {
            bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
        } else if (i == -200) {
            bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
        } else {
            bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
        }
        layout.removeView(adView);
    }

    @Override
    public void onShowLandingScreen(CaulyAdBannerView caulyAdBannerView) {
        Log.e("cauly", "Admob Mediation : Cauly Banner onShowLandingScreen");

    }

    @Override
    public void onCloseLandingScreen(CaulyAdBannerView caulyAdBannerView) {
        Log.e("cauly", "Admob Mediation : Cauly Banner onCloseLandingScreen");
    }

    @Override
    public void onTimeout(CaulyAdBannerView caulyAdBannerView, String s) {

    }
}