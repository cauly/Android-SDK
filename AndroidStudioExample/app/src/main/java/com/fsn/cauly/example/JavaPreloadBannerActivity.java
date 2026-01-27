package com.fsn.cauly.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fsn.cauly.CaulyAdBannerView;
import com.fsn.cauly.CaulyAdBannerViewListener;
import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.Logger;
import com.fsn.cauly.Logger.LogLevel;

/**
 * CaulyAdBannerView Preload 샘플 (Load / Show 분리)
 * - Load: caulyAdBannerView.load(context, parent)
 * - Show: caulyAdBannerView.show()
 */
public class JavaPreloadBannerActivity extends Activity implements CaulyAdBannerViewListener {

    private static final String TAG = "CaulyExample";
    private static final String APP_CODE = "CAULY";

    private RelativeLayout adViewRoot;
    private Button btnLoad;
    private Button btnShow;

    private CaulyAdBannerView bannerView;
    private boolean isAdLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_preload_banner);

        Logger.setLogLevel(LogLevel.Debug);

        adViewRoot = (RelativeLayout) findViewById(R.id.adViewRoot);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnShow = (Button) findViewById(R.id.btnShow);
        btnShow.setEnabled(false);
    }

    /**
     * Load 버튼 처리 (XML: android:onClick="onLoadBanner")
     */
    public void onLoadBanner(View button) {
        btnShow.setEnabled(false);
        isAdLoaded = false;

        cleanupBanner();

        CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APP_CODE)
                .bannerHeight(CaulyAdInfoBuilder.FIXED)
                .effect("None")
                .build();

        bannerView = new CaulyAdBannerView(this);
        bannerView.setAdInfo(adInfo);
        bannerView.setShowPreExpandableAd(true);
        bannerView.setAdViewListener(this);

        // load()의 parent로 실제 화면 컨테이너를 넘김 (show()에서 parent.addView 처리)
        bannerView.load(getApplication(), adViewRoot);
        Log.d(TAG, "banner load() called");
    }

    /**
     * Show 버튼 처리 (XML: android:onClick="onShowBanner")
     */
    public void onShowBanner(View button) {
        if (bannerView == null || !isAdLoaded) {
            Toast.makeText(this, "먼저 Load를 완료해주세요.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "show skipped: not loaded yet");
            return;
        }
        bannerView.show();
        Log.d(TAG, "banner show() called");
        btnShow.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanupBanner();
    }

    private void cleanupBanner() {
        if (bannerView == null) return;
        try {
            if (adViewRoot != null) adViewRoot.removeView(bannerView);
        } catch (Throwable ignored) {
        }
        try {
            bannerView.destroy();
        } catch (Throwable ignored) {
        } finally {
            bannerView = null;
            isAdLoaded = false;
            if (btnShow != null) btnShow.setEnabled(false);
        }
    }

    // CaulyAdBannerViewListener
    // 광고 동작에 대해 별도 처리가 필요 없는 경우,
    // Activity의 "implements CaulyAdBannerViewListener" 부분 제거하고 생략 가능.
    @Override
    public void onReceiveAd(CaulyAdBannerView adView, boolean isChargeableAd) {
        Log.d(TAG, "banner AD received.");
        isAdLoaded = true;
        btnShow.setEnabled(true);
    }

    @Override
    public void onFailedToReceiveAd(CaulyAdBannerView adView, int errorCode, String errorMsg) {
        Log.d(TAG, "failed to receive banner AD. code=" + errorCode + ", msg=" + errorMsg);
        isAdLoaded = false;
        btnShow.setEnabled(false);
    }

    @Override
    public void onShowLandingScreen(CaulyAdBannerView adView) {
        Log.d(TAG, "banner AD landing screen opened.");
    }

    @Override
    public void onCloseLandingScreen(CaulyAdBannerView adView) {
        Log.d(TAG, "banner AD landing screen closed.");
    }

    @Override
    public void onClickAd(CaulyAdBannerView adView) {
        Log.d(TAG, "banner AD clicked.");
    }

    @Override
    public void onTimeout(CaulyAdBannerView adView, String msg) {
        Log.d(TAG, "banner AD timeout. msg=" + msg);
        isAdLoaded = false;
        btnShow.setEnabled(false);
    }
}

